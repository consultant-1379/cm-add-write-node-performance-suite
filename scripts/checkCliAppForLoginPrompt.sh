#!/bin/sh

CLI="/opt/ericsson/enmutils/bin/cli_app"
ADMIN_USER="administrator"
ADMIN_PASSWORD="TestPassw0rd"

#############
#			#
# Functions #
#			#
#############

#
# Execute the cli command
#
# $1 - the cmedit command
#
checkForLoginPrompt() {
	testCommand="cmedit get * SubNetwork"
	expect -c "
		set timeout -1
		spawn ${CLI} \"${testCommand}\"
		expect {
			\"]#\" {   }
			\"Username:\" {
				send \"${ADMIN_USER}\r\"
				expect \"Password:\"
				send \"${ADMIN_PASSWORD}\r\"
				expect \"]#\"
			}
		}
	"
}

########
#	   #
# Main #
#	   #
########

checkForLoginPrompt

