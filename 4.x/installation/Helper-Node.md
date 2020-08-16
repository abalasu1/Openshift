# Helper Node Setup

## Follow instructions in https://github.com/RedHatOfficial/ocp4-helpernode

### Helper node when created with the folloeing script, can provide the following services:
![Helper Node](https://github.com/RedHatOfficial/ocp4-helpernode/blob/master/docs/images/hn.png)

### Providing configurations to the helper node script  (https://github.com/RedHatOfficial/ocp4-helpernode/tree/master/docs/examples)

```
---
disk: vda
helper:
  name: "helper"
  ipaddr: "192.168.7.77"
dns:
  domain: "example.com"
  clusterid: "ocp4"
  forwarder1: "8.8.8.8"
  forwarder2: "8.8.4.4"
dhcp:
  router: "192.168.7.1"
  bcast: "192.168.7.255"
  netmask: "255.255.255.0"
  poolstart: "192.168.7.10"
  poolend: "192.168.7.30"
  ipid: "192.168.7.0"
  netmaskid: "255.255.255.0"
bootstrap:
  name: "bootstrap"
  ipaddr: "192.168.7.20"
  macaddr: "52:54:00:60:72:67"
masters:
  - name: "master0"
    ipaddr: "192.168.7.21"
    macaddr: "52:54:00:e7:9d:67"
  - name: "master1"
    ipaddr: "192.168.7.22"
    macaddr: "52:54:00:80:16:23"
  - name: "master2"
    ipaddr: "192.168.7.23"
    macaddr: "52:54:00:d5:1c:39"
workers:
  - name: "worker0"
    ipaddr: "192.168.7.11"
    macaddr: "52:54:00:f4:26:a1"
  - name: "worker1"
    ipaddr: "192.168.7.12"
    macaddr: "52:54:00:82:90:00"
  - name: "worker2"
    ipaddr: "192.168.7.13"
    macaddr: "52:54:00:8e:10:34"
other:
  - name: "non-cluster-vm"
    ipaddr: "192.168.7.31"
    macaddr: "52:54:00:f4:2e:2e"

setup_registry:
  deploy: true
  autosync_registry: true
  registry_image: docker.io/ibmcom/registry-ppc64le:2.6.2.5
  local_repo: "ocp4/openshift4"
  product_repo: "openshift-release-dev"
  release_name: "ocp-release"
  release_tag: "4.3.18-ppc64le"

ocp_bios: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/dependencies/rhcos/4.3/4.3.18/rhcos-4.3.18-ppc64le-metal.ppc64le.raw.gz"
ocp_initramfs: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/dependencies/rhcos/4.3/4.3.18/rhcos-4.3.18-ppc64le-installer-initramfs.ppc64le.img"
ocp_install_kernel: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/dependencies/rhcos/4.3/4.3.18/rhcos-4.3.18-ppc64le-installer-kernel-ppc64le"
ocp_client: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/clients/ocp/4.3.18/openshift-client-linux-4.3.18.tar.gz"
ocp_installer: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/clients/ocp/4.3.18/openshift-install-linux-4.3.18.tar.gz"
helm_source: "https://get.helm.sh/helm-v3.2.4-linux-ppc64le.tar.gz"
```

### Disk type

- disk: vda/sda (If the value is wrong, macines will not get bootstrapped)

### To setup local registry
x86:
```
setup_registry:
  deploy: true
  autosync_registry: true
  registry_image: docker.io/library/registry:2
  local_repo: "ocp4/openshift4"
  product_repo: "openshift-release-dev"
  release_name: "ocp-release"
  release_tag: "4.3.18-x86_64"
```

ppc64le:
```
setup_registry:
  deploy: true
  autosync_registry: true
  registry_image: docker.io/ibmcom/registry-ppc64le:2.6.2.5
  local_repo: "ocp4/openshift4"
  product_repo: "openshift-release-dev"
  release_name: "ocp-release"
  release_tag: "4.3.18-ppc64le"
```

### Bootstrap, master and worker configuration
Names should exactly match the vm names for the install to succeed

```
bootstrap:
  name: "bootstrap"
  ipaddr: "192.168.7.20"
  macaddr: "52:54:00:60:72:67"
masters:
  - name: "master0"
    ipaddr: "192.168.7.21"
    macaddr: "52:54:00:e7:9d:67"
  - name: "master1"
    ipaddr: "192.168.7.22"
    macaddr: "52:54:00:80:16:23"
  - name: "master2"
    ipaddr: "192.168.7.23"
    macaddr: "52:54:00:d5:1c:39"
workers:
  - name: "worker0"
    ipaddr: "192.168.7.11"
    macaddr: "52:54:00:f4:26:a1"
  - name: "worker1"
    ipaddr: "192.168.7.12"
    macaddr: "52:54:00:82:90:00"
  - name: "worker2"
    ipaddr: "192.168.7.13"
    macaddr: "52:54:00:8e:10:34"
other:
  - name: "non-cluster-vm"
    ipaddr: "192.168.7.31"
    macaddr: "52:54:00:f4:2e:2e"
```

### DNS configuration  (Optional: If the helper node need to function as the DNS server)
For installations with internet connection from the cluster:
```
dns:
  domain: "example.com"
  clusterid: "ocp4"
  forwarder1: "8.8.8.8"
  forwarder2: "8.8.4.4"
```

For offline installations:
```
dns:
  domain: "example.com"
  clusterid: "ocp4"
  forwarder1: <enterprise forwarder, may be from /etc/resolv.conf>
  forwarder2: <enterprise forwarder, may be from /etc/resolv.conf>
```

### DHCP configuration (Not needed if you are using static ip's)
poolstart, poolend - IP range that can be assigned to nodes within Openshift cluster
router, bcast, netmask - use 'ifconfig' command to determine the rest of the values
ipid, netmaskid - use 'ifconfig' command to determine the rest of the values
```
dhcp:
  router: "192.168.7.1"
  bcast: "192.168.7.255"
  netmask: "255.255.255.0"
  poolstart: "192.168.7.10"
  poolend: "192.168.7.30"
  ipid: "192.168.7.0"
  netmaskid: "255.255.255.0"
```

### Other paths
x86:
```
ocp_bios: "https://mirror.openshift.com/pub/openshift-v4/dependencies/rhcos/pre-release/latest/rhcos-4.3.18-metal-bios.raw.gz"
ocp_initramfs: "https://mirror.openshift.com/pub/openshift-v4/dependencies/rhcos/pre-release/latest/rhcos-4.3.18-installer-initramfs.img"
ocp_install_kernel: "https://mirror.openshift.com/pub/openshift-v4/dependencies/rhcos/pre-release/latest/rhcos-4.3.18-installer-kernel"
ocp_client: "https://mirror.openshift.com/pub/openshift-v4/clients/ocp-dev-preview/latest/openshift-client-linux-4.3.18.tar.gz"
ocp_installer: "https://mirror.openshift.com/pub/openshift-v4/clients/ocp-dev-preview/latest/openshift-install-linux-4.3.18.tar.gz"
helm_source: "https://get.helm.sh/helm-v3.2.4-linux.tar.gz"
```

ppc64le:
```
ocp_bios: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/dependencies/rhcos/4.3/4.3.18/rhcos-4.3.18-ppc64le-metal.ppc64le.raw.gz"
ocp_initramfs: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/dependencies/rhcos/4.3/4.3.18/rhcos-4.3.18-ppc64le-installer-initramfs.ppc64le.img"
ocp_install_kernel: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/dependencies/rhcos/4.3/4.3.18/rhcos-4.3.18-ppc64le-installer-kernel-ppc64le"
ocp_client: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/clients/ocp/4.3.18/openshift-client-linux-4.3.18.tar.gz"
ocp_installer: "https://mirror.openshift.com/pub/openshift-v4/ppc64le/clients/ocp/4.3.18/openshift-install-linux-4.3.18.tar.gz"
helm_source: "https://get.helm.sh/helm-v3.2.4-linux-ppc64le.tar.gz"
```

### Running helper node script
Execute the following commands:

```
yum -y install https://dl.fedoraproject.org/pub/epel/epel-release-latest-$(rpm -E %rhel).noarch.rpm

yum -y install ansible git
git clone https://github.com/RedHatOfficial/ocp4-helpernode
cd ocp4-helpernode

cp docs/examples/vars.yaml .
customize vars.yaml according to your requirements
ansible-playbook -e @vars.yaml tasks/main.yml
```

### Validating helper node install
Execute the following commands:

```
/usr/local/bin/helpernodecheck services

/usr/local/bin/helpernodecheck masters
/usr/local/bin/helpernodecheck etcd
/usr/local/bin/helpernodecheck workers
helpernodecheck local-registry-info
```

### Optional NFS setup
Add the following configuration:

```
nfs:
  server: "192.168.1.100"
  path: "/exports/helper"
```