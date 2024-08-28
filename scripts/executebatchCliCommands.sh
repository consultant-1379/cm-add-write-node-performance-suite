#!/bin/sh

CLI="/opt/ericsson/enmutils/bin/cli_app"
INPUT_FILE=""
USE_CASE_DESC=""

#############
#			#
# Functions #
#			#
#############

#
# Prints the script usage.
#
printUsage() {
    printf "\nUsage: $0 <input file> [ <use case description> ]\n\n"
    printf "where\n\n"
    printf "<input file> - file containing the cmedit commands to be executed\n\n"
    printf "<use case description> - Optional string that can be provided to describe the use case under test.\n"
    printf "The script outputs this info during execution and it can be useful when redirecting to a log file.\n\n"
    exit 1
}

#
# Reads the input file
#
executeCommandsFromInputFile() {
	while IFS='' read -r line || [[ -n $line ]]; do
    printf "\n==================================================================================\n\n"
    printf "Executing command:\t\"${CLI} '$line'\"\n\n"
    printf "Use case:\t\t\"${USE_CASE_DESC}\"\n\n"
    printf "Time:\t\t\t\"$(date)\"\n\n"
    printf "Result:\n\n"
    ${CLI} "${line}"
	done < "$INPUT_FILE"
}

########
#	   #
# Main #
#	   #
########

if [ $# -lt 1 ]
then
    printUsage
else
    INPUT_FILE=$1
    USE_CASE_DESC=$2
fi

executeCommandsFromInputFile

