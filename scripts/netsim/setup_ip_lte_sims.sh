#!/bin/sh 

# Global Variables
debug=0			# 1 = enabled
count=2
subnetList=""
rnsList=""
DATE=$(date +"%h_%m_%H_%M")
commandFile="tmp_${DATE}.cmd"
configFile="subnetList.cfg"
netsimPipe="/netsim/inst/netsim_pipe"
simDir="/netsim/netsimdir/"

# System variables

EXPECT="/usr/bin/expect"

# Functions

printUsage() {
	echo ""	
	echo "The purpose of this script is to configure all the nodes for all LTE simulations in the directory '/netsim/netsimdir'"
	echo "with a valid ipaddress. It also sets the port which the nodes will use and assumes a port called 'NetSimPort' exists"
	echo "in NetSim which is valid for LTE nodes."
	echo ""
	echo "The script creates a list of available subnets and assigns each simulation to a subnet, assigning the nodes ipaddresses"
	echo "from that subnet. If there are more than 255 nodes in the simulation, this script will skip the simulation. LTE sims"
	echo "normally have a maximum of 160 nodes per sim."
	echo ""
	echo "The script should be executed as user 'netsim'"
	echo ""	
	echo "$0 debug | execute [[<rns>] [count]]"
	echo ""
	echo "where"
	echo ""
	echo "     debug - In this mode the cli commands will not be executed against the netsim_pipe, they will just be printed to the"
	echo "             console along with some other useful debugging information"
	echo ""
	echo "     execute - In this mode the commands will be generated and executed via the netsim_pipe."
	echo ""
	echo "     <rns> - optional parameter allowing a user to specify a specific sim."
	echo "             If not specified, the operations will be performed on all sims"
	echo "             User need only supply an rns name e.g. LTE20. The script wll determine the matching sim."
	echo ""
	echo "     <count> - optional integer that allows a user to determine the subnet that the ipaddress will be taken from."
	echo "               Best to execute the script in debug mode to see how this works"
	echo ""
	
	exit 1
}

checkAction() {
	if [ $1 == "debug" ]; then
		debug=1		
	elif [ $1 == "execute" ]; then
		debug=0
	else
		printUsage
	fi
}

checkSimsExist() {
 	sims=$(ls $simDir | egrep "LTE[0-9]*$" | wc -l)
	if [ $sims -eq 0 ]; then
		echo "Could not find any compatible simulations in $simDir"
		echo "Were the simulations created / uncompressed?"
		exit 1
	fi
}

#
# Uses /usr/bin/expect to execute a command as super user to generate a file 
# containing a list of subnets configured on the server.					 
#
generateSubnetList() {

	cwd=$(pwd)
	password="shroot"

	echo "Constructing List of subnets...takes a few seconds"
		
$EXPECT <<EOD
	set timeout -1
	log_user 0

	spawn su -
	expect "Password: "
	send "$password\r"
	expect "# "
	send "ifconfig -a | grep Mask | grep -v Global | awk -F: '{print \\\$2}' | grep -v Mask | awk -F.  '{print \\\$1\".\"\\\$2\".\"\\\$3}'| sort -u | sort -t \\. -k 3,3n -k 4,4n | awk 'NR>1' > $configFile\r"
	expect "# "
	send "chown netsim $configFile\r"
	expect "# "
	send "chgrp netsim $configFile\r"
	expect "# "
	send "mv $configFile $cwd\r"
	expect "# "
EOD

	subnetList=$(cat $configFile)
}

#
# Creates a command file of echo statements that can be used to updates the nodes for all simulations 
# in the /netsim/netsimdir directory with an ipaddress and port.
#
createCommandFile() {

	touch $commandFile
	subnetCount=1
	
	echo "Constructing Command File..."
	
	for entry in $subnetList
	do
		eval subnet$subnetCount=$entry			# using eval to allow us to simulate array like element

		if [ $debug -eq 1 ]; then
			eval echo "\$subnet$subnetCount"			# need the \ as eval reads the command line twice
														# first pass generates echo "$subnet1,2,3...20" etc.
														# produces variables subnet1, subnet2...subnetx
		fi
		
		subnetCount=$(expr $subnetCount + 1)		
	done
	
	if [ $count -gt 0 ]
	then 
		subnetCount=$count
	else
		subnetCount=1
	fi
	
	for RNS in $rnsList
	do
		SIM=$(ls /netsim/netsimdir | grep ${RNS} | grep -v zip)
		NUMBER_OF_NODES=$(ls /netsim/netsim_dbdir/simdir/netsim/netsimdir/${SIM}/ | grep -c ERBS)


		if [[ $NUMBER_OF_NODES -gt 0 && $NUMBER_OF_NODES -lt 256 ]]; then
			  SUB=$(eval echo "\$subnet$subnetCount")
			  SUBNET="${SUB}.1"

			  echo ".open default" >> $commandFile
			  echo ".open $SIM" >> $commandFile
			  echo ".select network" >> $commandFile
			  echo ".set port NetSimPort" >> $commandFile
			  echo ".modifyne set_subaddr ${SUBNET} subaddr no_value" >> $commandFile
			  echo ".set save" >> $commandFile
			  
			  subnetCount=$(expr $subnetCount + 1)
		fi
	done
}

executeCommandFile() {
	if [ $debug -eq 0 ]; then
		echo "Executing commands $commandFile in against $netsimPipe"
		cat $commandFile | $netsimPipe
	else
		echo ""
		echo "Command File contents..."
		echo ""
		cat $commandFile
		
		echo ""
		echo "Subnet list in config file..."
		echo ""
		cat $configFile
	fi
}

cleanUp() {
		rm $configFile
		rm $commandFile
}

# Main

if [ $# -eq 0 ]; then
	printUsage
fi

checkAction $1
checkSimsExist

if [ $# -eq 2 ] 
then
	rnsList=$2
elif [ $# -eq 3 ] 
then
	rnsList=$2
	count=$3
else
	rnsList=$(ls /netsim/netsimdir/ | egrep "LTE[0-9]*$" | sed -n 's/.*\(LTE[0-9]*\).*/\1/p' | sort -n)
fi

generateSubnetList
createCommandFile
executeCommandFile
cleanUp
