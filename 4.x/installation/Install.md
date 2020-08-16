# Install Steps

## Create install-config.yaml
- clusterNetworks.cidr: CIDR range for pods within openshift, should not clash with any existing IP's in the environoment.
- serviceNetwork: CIDR range for pods within openshift, should not clash with any existing IP's in the environoment.

Only needed if you are doing a disconnected installation:
- additionalTrustBundle: ***Certificate value should be aligned and this has to be done manually after the executing this command.
ex:
```
additionalTrustBundle: |
  -----BEGIN CERTIFICATE-----
  b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAACFwAAAAdzc2gtcn
  NhAAAAAwEAAQAAAgEApe4dGTTTS1xbGwc/odB5gc+YuHlv+egegD+h529oZx+jD5edzHL5
  gpl7p15NkETQP1OLMWFk17Gxq8rGCmOxOI3CEFqmm/BUYLrVuSrq4JKtyBNlLEp7Rt5h+8
  ...
  -----END CERTIFICATE-----
```

- imageContentSources:
If you are using mirror registry, this should include the mirror registry you have created.
```
imageContentSources:
- mirrors:
  - registry.ocp4.ibm.com:5000/ocp4/openshift4
  source: quay.io/openshift-release-dev/ocp-release
- mirrors:
  - registry.ocp4.ibm.com:5000/ocp4/openshift4
  source: quay.io/openshift-release-dev/ocp-v4.0-art-dev  
```

For online installs:
```
cat <<EOF > install-config.yaml
apiVersion: v1
baseDomain: ibm.com
compute:
- hyperthreading: Enabled
  name: worker
  replicas: 0
controlPlane:
  hyperthreading: Enabled
  name: master
  replicas: 3
metadata:
  name: ocp4
networking:
  clusterNetworks:
  - cidr: 192.112.0.0/16
    hostPrefix: 24
  networkType: OpenShiftSDN
  serviceNetwork:
  - 192.113.0.0/16
platform:
  none: {}
EOF
```

For offline/disconnected installs:
```
cat <<EOF > install-config.yaml
apiVersion: v1
baseDomain: bancs.com
compute:
- hyperthreading: Enabled
  name: worker
  replicas: 0
controlPlane:
  hyperthreading: Enabled
  name: master
  replicas: 3
metadata:
  name: ocp4
networking:
  clusterNetworks:
  - cidr: 192.112.0.0/16
    hostPrefix: 24
  networkType: OpenShiftSDN
  serviceNetwork:
  - 192.113.0.0/16
platform:
  none: {}
pullSecret: '$(< ~/.openshift/pull-secret-updated)'
sshKey: '$(< ~/.ssh/helper_rsa.pub)'
additionalTrustBundle: |
  -----BEGIN CERTIFICATE-----
  '$(< /opt/registry/certs/domain.crt)'
  -----END CERTIFICATE-----
imageContentSources:
- mirrors:
  - registry.ocp4.ibm.com:5000/ocp4/openshift4
  source: quay.io/openshift-release-dev/ocp-release
- mirrors:
  - registry.ocp4.ibm.com:5000/ocp4/openshift4
  source: quay.io/openshift-release-dev/ocp-v4.0-art-dev
EOF
```

## Create manifests file
```
openshift-install create manifests
```

## Prevent masters from getting user workloads:
```
sed -i ‘s/mastersSchedulable: true/mastersSchedulable: false/g’ manifests/cluster-scheduler-02-config.yml
```

## Create ignition config’s
x86:
```
OPENSHIFT_INSTALL_RELEASE_IMAGE_OVERRIDE=registry.ocp4.ibm.com:5000/ocp4/openshift4:4.3.18 openshift-install create ignition-configs
```

ppc64le:
```
OPENSHIFT_INSTALL_RELEASE_IMAGE_OVERRIDE=registry.ocp4.ibm.com:5000/ocp4/openshift4:4.3.18-ppc64le openshift-install create ignition-configs
```

## Copy ignition vm’s to webserver
```
cp ~/ocp4/*.ign /var/www/html/ignition/
restorecon -vR /var/www/html/
chmod o+r /var/www/html/ignition/*.ign
```

## Boot Nodes
- Boot bootstrap node first. Coreos will be installed during the startup and after that boot sequence should be changed to boot from hard disk.
- Boot master nodes: Coreos will be installed during the startup and after that boot sequence should be changed to boot from hard disk. Master nodes get restarted once more after the initial
install and should boot from hard disk the second time.
- Boot worker nodes: Worker nodes can be started after the master nodes are up and running.
 
## Wait for install to complete
```
openshift-install wait-for bootstrap-complete --log-level debug
````

Run following commands to debug:
```
Log output on bootstrap and master server
journalctl -f  

Log output on bootstrap node
journalcrl -b -f -u bootkube.services
```

## Final Install Steps
At this point, nodes should get listed properly.
```
export KUBECONFIG=/root/oc4_new_install/auth/kubeconfig
oc get nodes
```

## Approve all pending certificates
```
watch oc get csr
oc get csr --no-headers | awk ‘{print $1}’ | xargs oc adm certificate approve
oc get csr | grep ‘system:node’
```

## Make sure all operators are running
```
watch -n5 oc get clusteroperators

NAME                                 VERSION   AVAILABLE   PROGRESSING   DEGRADED   SINCE
authentication                       4.3.0     True        False         False      69s
cloud-credential                     4.3.0     True        False         False      12m
cluster-autoscaler                   4.3.0     True        False         False      11m
console                              4.3.0     True        False         False      46s
dns                                  4.3.0     True        False         False      11m
image-registry                       4.3.0     True        False         False      5m26s
ingress                              4.3.0     True        False         False      5m36s
kube-apiserver                       4.3.0     True        False         False      8m53s
kube-controller-manager              4.3.0     True        False         False      7m24s
kube-scheduler                       4.3.0     True        False         False      12m
machine-api                          4.3.0     True        False         False      12m
machine-config                       4.3.0     True        False         False      7m36s
marketplace                          4.3.0     True        False         False      7m54m
monitoring                           4.3.0     True        False         False      7h54s
network                              4.3.0     True        False         False      5m9s
node-tuning                          4.3.0     True        False         False      11m
openshift-apiserver                  4.3.0     True        False         False      11m
openshift-controller-manager         4.3.0     True        False         False      5m943s
openshift-samples                    4.3.0     True        False         False      3m55s
operator-lifecycle-manager           4.3.0     True        False         False      11m
operator-lifecycle-manager-catalog   4.3.0     True        False         False      11m
service-ca                           4.3.0     True        False         False      11m
service-catalog-apiserver            4.3.0     True        False         False      5m26s
service-catalog-controller-manager   4.3.0     True        False         False      5m25s
storage                              4.3.0     True        False         False      5m30s
```