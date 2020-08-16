# Openshift 4.x installtion

## Steps in Openshift install

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

## Pre install steps

** These steps are needed only if the internet connection is not available from the master and worker nodes and a disconnected
install needs to be performed

### [Decide on the topology of the cluster](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#decide-on-the-topology-of-the-cluster)
### [Create Bastion Node](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#create-bastion-node)
### [Create bootstrap, master and worker nodes](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#create-bootstrap-master--worker-nodes)
### [** URL's to whitelist](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#urls-to-whitelist-needed-only-in-case-of-offline-installation)
### [Create bootstrap, master & worker Nodes]()
### [** Setup mirror registry](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Pre-Install.md#setup-mirror-registry)
### [Use helper node script to set up the prerequisites](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Helper-Node.md)

## Openshift install

### [Create install configs](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Install.md#create-install-configyaml)

### [Create manifests file](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Install.md#create-manifests-file)

### [Prevent masters from getting user workloads](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Install.md#prevent-masters-from-getting-user-workloads)

### [Create ignition config’s](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Install.md#create-ignition-configs)

### [Copy ignition vm’s to webserver](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Install.md#create-ignition-configs)

## Post install steps

### [Deploy a simple application to validate install](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Post-Install.md#deploy-a-simple-application-for-validation)

## Retrying after a failed install

### [** Cleanup mirror registry contents](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Clean-Up.md#cleanup-mirror-registry)

### [Cleanup files after a failed install](https://github.com/abalasu1/Openshift/blob/master/4.x/installation/Clean-Up.md#remove-files-created-by-install-before-restarting-the-install)

## Common issues and solutions

### X.509 Certificates are expired or invalid errors