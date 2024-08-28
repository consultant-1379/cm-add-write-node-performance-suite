#!/bin/sh

# Variables
debug=0
DATE=$(date +"%h_%m_%H_%M")
commandFile="tmp_${DATE}.cmd"
host=$(hostname) 
netsimPipe="/netsim/inst/netsim_pipe"
	
#############
# Functions #
#############

printUsage() {
	echo ""	
	echo "Script to create a NetSim port that ipaddresses can bind to."
	echo ""	
	echo "$0 [debug]"
	echo ""
	echo "where"
	echo ""
	echo "     debug - optional parameter allowing a user to check the CLI commands before executing them."
	echo ""
	
	exit 1
}

checkForDebugOption() {
	if [ $1 == "debug" ]; then
		debug=1
	else
		printUsage
		exit 1
	fi
}

createCommandFile() {
	ip=$(grep $host /etc/hosts | /usr/bin/awk '{print $1}')

	echo ".select configuration" >> $commandFile
	echo ".config add port NetSimPort iiop_prot $host" >> $commandFile
 	echo ".config port address NetSimPort nehttpd $ip 56834 56836 no_value" >> $commandFile
	echo ".config save" >> $commandFile
}

executeCommandFile() {
	if [ $debug -eq 0 ]; then
		echo "Creating the port..."
		echo ""
		cat $commandFile | $netsimPipe
	else
		printf "\nThe following netsim pipe commands will be used:\n\n"
		cat $commandFile
	fi
}

cleanUp() {
	rm $commandFile
}

# Main

if [ $# -eq 1 ]; then
	checkForDebugOption $1
fi

createCommandFile
executeCommandFile
cleanUp
