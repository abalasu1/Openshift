# Openshift 4.x Installtion

## Steps in Openshift Install

- The bootstrap machine boots and starts hosting the remote resources required for the master machines to boot. (Requires manual intervention if you provision the infrastructure)

- The master machines fetch the remote resources from the bootstrap machine and finish booting. (Requires manual intervention if you provision the infrastructure)

- The master machines use the bootstrap machine to form an etcd cluster.

- The bootstrap machine starts a temporary Kubernetes control plane using the new etcd cluster.

- The temporary control plane schedules the production control plane to the master machines.

- The temporary control plane shuts down and passes control to the production control plane.

- The bootstrap machine injects OpenShift Container Platform components into the production control plane.

- The installation program shuts down the bootstrap machine. (Requires manual intervention if you provision the infrastructure)

- The control plane sets up the worker nodes.

- The control plane installs additional services in the form of a set of Operators.

![Install](https://user-images.githubusercontent.com/13202504/89897181-41c6a380-dbfc-11ea-9fa9-3f12527eda6c.png)

## Pre Install Steps

** These steps are needed only if the internet connection is not available from the master and worker nodes and a disconnected
install needs to be performed

### [** URL's to Whitelist](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#urls-to-whitelist-needed-only-in-case-of-an-disconnected-install)
### [** Setup Mirror Registry](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#setup-mirror-registry)

## Openshift Install

### [Create Install Configs]()

## Post Install Steps

### [Deploy a simple application to validate install](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Post-Install.md#deploy-a-simple-application-for-validation)

## Retrying after a failed install

### [** Cleanup mirror registry contents](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Clean-Up.md#cleanup-mirror-registry)

### [Cleanup files after a failed install](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Clean-Up.md#remove-files-created-by-install-before-restarting-the-install)

## Common Issues and Solutions

### X.509 Certificates are expired or invalid errors