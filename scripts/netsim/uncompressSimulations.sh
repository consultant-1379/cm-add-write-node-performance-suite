#!/bin/sh

####################################################################################################################
#															   #
# This script will uncompress all simulations in the /netsim/netsimdir directory matching the following criteria:  #
#															   #
# - contains 'zip' in the simulation file name										   #
# - all simulations containing LTExx in the simulation file name where xx is any digit, or a single simulation     #
#   containing a string supplied as a single parameter to the script. 							   #
#															   #
####################################################################################################################

DEBUG=0			# 1 = enabled
NETSIM_DIR="/netsim/netsimdir/"
DATE=$(date +"%h_%m_%H_%M")
COMMAND_FILE="tmp_${DATE}.cmd"
NETSIM_PIPE="/netsim/inst/netsim_pipe"
RNS_LIST=""

#############
# Functions #
#############

#
# $1 - simulation name
#
executeNetSimCliCommands() {
	if [ ${DEBUG} -eq 0 ]; then
		echo ".open default" | ${NETSIM_PIPE}
		echo ".uncompressandopen ${1} force" | ${NETSIM_PIPE}
	else
		printf "\nThe following netsim pipe commands will be used:\n\n"
		echo ".open default"
		echo ".uncompressandopen ${1} force"
	fi
}

########
# Main #
########

if [ $# -eq 0 ]; then
	RNS_LIST=$(ls $NETSIM_DIR | grep zip | egrep "LTE[0-9]+" | sed -n 's/.*\(LTE[0-9]*\).*/\1/p' | sort -n)
elif [ $# -eq 1 ]; then
	RNS_LIST=$1
fi

printf "\nWill attempt to find and uncompress all sims containing the following RNS names:\n\n"
printf "${RNS_LIST}\n\n"

for rns in ${RNS_LIST}
do
	sim=`ls ${NETSIM_DIR} | grep ${rns} | grep zip`
	executeNetSimCliCommands ${sim}
	sleep 5
done

