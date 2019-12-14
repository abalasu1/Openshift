#!/bin/bash
# Script to register with redhat and enable the packages required to install openshift on bastion machine.


# Unregister with softlayer subscription
subscription-manager unregister

username=$1
password=$2
localrepo_ip=$3

#subscription-manager refresh
#subscription-manager repos --disable="*"
