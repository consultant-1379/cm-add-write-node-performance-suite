#!/bin/sh

DEBUG=0
NETSIM_DIR="/netsim/netsimdir/"
NETSIM_PIPE="/netsim/inst/netsim_pipe"
DATE=$(date +"%h_%m_%H_%M")
COMMAND_FILE="tmp_${DATE}.cmd"

#############
# Functions #
#############

checkForDebugOption() {
	if [ $1 == "debug" ]; then
		DEBUG=1
	fi
exit 1
}

createCommandFile() {
	
	touch ${COMMAND_FILE}

	for SIM in `ls ${NETSIM_DIR} | egrep "SGSN|RNC|CORE|LTE" | grep -v zip`
	do
		echo ".open $SIM" >> ${COMMAND_FILE}
		echo ".select network" >> ${COMMAND_FILE}
		echo ".stop" >> ${COMMAND_FILE}
       	echo ".deletesimulation $SIM force" >> ${COMMAND_FILE}
	done
}

executeCommandFile() {
	if [ ${DEBUG} -eq 0 ]; then
		cat ${COMMAND_FILE} | ${NETSIM_PIPE}
		checkNumberOfStartedNodes
	else
		printf "\nThe following netsim pipe commands will be used:\n\n"
		cat ${COMMAND_FILE}
	fi
}

checkNumberOfStartedNodes() {
	echo "Checking number of started nodes"
	echo ".show started" | ${NETSIM_PIPE}
}

deleteCompressedSims() {
    	zipFileList=`ls ${NETSIM_DIR} | egrep "SGSN|RNC|CORE|LTE" | grep zip`
    	for zipFile in ${zipFileList} ; do
       		echo "Deleting ${NETSIM_DIR}${zipFile}"
       		rm -f ${NETSIM_DIR}${zipFile}
    	done
}

cleanUp() {
	rm ${COMMAND_FILE}
}

########
# Main #
########

createCommandFile
executeCommandFile
deleteCompressedSims
cleanUp
