#!/bin/sh
# ********************************************************************
# Ericsson Radio Systems AB                                     SCRIPT
# ********************************************************************
#
#
# (c) Ericsson Radio Systems AB 2015 - All rights reserved.
#
# The copyright to the computer program(s) herein is the property
# of Ericsson Radio Systems AB, Sweden. The programs may be used
# and/or copied only with the written permission from Ericsson Radio
# Systems AB or in accordance with the terms and conditions stipulated
# in the agreement/contract under which the program(s) have been
# supplied.
#
# ********************************************************************
#

#
# ********************************************************************
#
#   Configuration Section
#
# ********************************************************************
#

CLUSTER_ID=$1
MVN_PROFILE=$2
SUITE=$3
CLUSTER_ID_REGEX='^[0-9]+$'

#
# ********************************************************************
#
#   Utility Functions
#
# ********************************************************************
#

### Function: abort_script ###
#
#   This will be called if the script is aborted if an
#   error is encountered during runtime
#
# Arguments:
#       $1 - Error message from part of program
# Return Values:
#       none

abort_script()
{
$CLEAR
if [ "$1" ]; then
    echo "$1"
    exit 1
else
    echo "Script aborted.......\n"
    exit 1
fi
}

### Function: usage_msg ###
#
# This script will execute the suite specified as $3 or kpi suite if none specified.
# And, generate allure-reports for the tests.
#
# Arguments:
#   none
# Return Values:
#   none

usage_msg()
{
$CLEAR
echo "
Usage: $0 <cluster_id> <maven305|maven323> <test_repo_dir> [<suite_profile>]

options:

arg1    : Cluster Id of the server
arg2    : Maven version profile for allure report generation. Must be one of the following:
              maven305 --> if user have Maven 3.0.5
              maven323 --> if user have Maven 3.1.1
arg3    : Optional param to provide profile name for the suite to run. If none specified KPI suite will be executed.
"
}

#
# ********************************************************************
#
# Main body of program
#
# ********************************************************************
#


#
# Check Input Params
#

if [ $# -lt 2 ]; then
	usage_msg
	exit 1
fi

echo $CLUSTER_ID | grep -E $CLUSTER_ID_REGEX
if [ $? -ne 0 ] ; then
	_err_msg_="ERROR- Invalid cluster_id : $CLUSTER_ID"
	abort_script "$_err_msg_"
fi


#
#Execute maven goals
#
if [ $MVN_PROFILE = "maven305" -o $MVN_PROFILE = "maven323" ]; then
  echo "Building project"
  cd ../..
  if [ $? = 0 ] ; then
    mvn clean install -DskipTafTests
    cd test-pom-cm-add-write-node-performance-suite
    if [ ! $SUITE ]; then
      echo "Executing KPI suite aginst cluster_id : $CLUSTER_ID"
      mvn clean test -Dcm.maxNoResponse=1000 -Dtaf.clusterId=$CLUSTER_ID
    else
      echo "Executing $SUITE suite aginst cluster_id : $CLUSTER_ID"
      mvn clean test -Dcm.maxNoResponse=1000 -Dtaf.clusterId=$CLUSTER_ID -P$SUITE
    fi
    echo "Generating allure reports in test-pom-cm-add-write-node-performance-suite/target/site/allure-maven-plugin"
    mvn site -P$MVN_PROFILE -DskipTafTests
    cd -
  else
    _err_msg_="Not able to go to test repo directory."
    abort_script "$_err_msg_"
  fi
else
  _err_msg_="Maven Profile : $MVN_PROFILE not supported . Please see usage for supported profiles."
  abort_script "$_err_msg_"
fi
