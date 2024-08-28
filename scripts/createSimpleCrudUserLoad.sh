#!/bin/sh

##############################################################################
#																			 #
# Simple script to generate user load on an enm system. Depends on the files #
# identified by $INPUT_FILES existing.   									 #
# Generates a log file in the same directory identified by $LOG_FILE.   	 #
#																			 #
##############################################################################

EXIT_CODE_1001=1001		# Exit code indicates that some input files required by the script are missing
EXIT_CODE_1002=1002		# Exit code indicates that some executable dependency required by the sript is missing

INPUT_FILES="./inputFiles/createDeleteMo.txt
./inputFiles/moAction.txt
./inputFiles/modify1Mo.txt
./inputFiles/moAction.txt
./inputFiles/readMoAttribute.txt
./inputFiles/readMultipleAttributes.txt"

LOG_FILE="upgradeLog.txt"
SLEEP_SECONDS_BETWEEN_EXECUTIONS=10
BATCH_SCRIPT="executeBatchCliCommands.sh"

#
# Executed if user hits CTRL-C
#
control_c() {
	echo "Stopping the script due to CTRL-C..."
	printf "\nStopped via ctrl c at: $(date)\n\n" >> ${LOG_FILE}
	exit $?
}

#
# Checks to see that all input files required by the script exist
#
confirmInputFilesExist() {
	for file in $INPUT_FILES
	do
		if [ ! -f $file ]
		then
			printf "\nOne or more input files required could not be found.\n\n"
			printf "Requires the folowing input files:\n\n\n"
			printf "${INPUT_FILES}\n\n"
			exit ${EXIT_CODE_1001}
		fi
	done
}

#
# Checks if the log file exists and creates it if it does not
#
createLogFile() {
	if [ ! -f ${LOG_FILE} ]
	then
		touch ${LOG_FILE}
	fi
}

#
# Checks if script dependencies exist
#
checkForDependencies() {
	if [ ! -f ${BATCH_SCRIPT} ]
	then
		printf "\n\nScript [${BATCH_SCRIPT}] is required but does not exist in ${PWD}\n\n"
		exit ${EXIT_CODE_1001}
	fi
}

#
# Executes the cmedit commands
#
# $1 - input file containing cmedit commands
# $2 - String describing the use case
#
execute() {
	./${BATCH_SCRIPT} $1 $2 >> ${LOG_FILE}
	sleep ${SLEEP_SECONDS_BETWEEN_EXECUTIONS}
}

# Main

# trap keyboard interrupt (control-c)
trap control_c SIGINT

createLogFile
checkForDependencies
confirmInputFilesExist

printf "\ntail log file [${LOG_FILE}] to see results of script execution.\n\n"

while true
do
	for file in $INPUT_FILES
	do
		execute $file "Performing CRUD operation"
	done
done
