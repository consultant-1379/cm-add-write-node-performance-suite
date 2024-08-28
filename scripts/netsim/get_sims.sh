#!/bin/sh

DEFAULT_RNS_LIST="LTE01 LTE02 LTE03 LTE04"
USER_RNS_LIST=""
DEFAULT_SIMPATH="/sims/O15/ENM/15.6/largeDeployment/33KLTE/"
USER_SIMPATH=""
FTP_SERVER="ftp.athtem.eei.ericsson.se"
SIM_DIR="/netsim/netsimdir"

#############
# Functions #
#############

printUsage() {
	printf "\nThis script will attempt to ftp simulations from the ftp server mentioned below based on a default list\n"
	printf "of RNS names or an updated list as provided by the user.\n\n"
	printf "FTP Server: ${FTP_SERVER}\n\n"
}

#
# Ask user to provide input or accept defaults
#
getUserInput() {	
	read -p "Enter RNS List [${DEFAULT_RNS_LIST}]: " USER_RNS_LIST
	USER_RNS_LIST=${USER_RNS_LIST:-${DEFAULT_RNS_LIST}}
	read -p "Enter directory on FTP server where sims can be found [${DEFAULT_SIMPATH}]: " USER_SIMPATH
	USER_SIMPATH=${USER_SIMPATH:-${DEFAULT_SIMPATH}}
}

########
# Main #
########

getUserInput

printf "\nftping sims from [$FTP_SERVER] using [$USER_RNS_LIST] and [$USER_SIMPATH]\n\n"

cd $SIM_DIR

for rns in $USER_RNS_LIST
do
        ftp -n -i $FTP_SERVER <<END_FTP
        user simguest simguest 
        bin
        cd $USER_SIMPATH 
        mget *${rns}*.zip
        bye
END_FTP
done
