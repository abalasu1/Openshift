# Install Steps

## Create install-config.yaml
- clusterNetworks.cidr: CIDR range for pods within openshift, should not clash with any existing IP's in the environoment.
- serviceNetwork: CIDR range for pods within openshift, should not clash with any existing IP's in the environoment.

Only needed if you are doing a disconnected installation:
- additionalTrustBundle: certificate value should be aligned and this has to be done manually after the executing this command.
ex:
additionalTrustBundle: |
  -----BEGIN CERTIFICATE-----
  b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAACFwAAAAdzc2gtcn
  NhAAAAAwEAAQAAAgEApe4dGTTTS1xbGwc/odB5gc+YuHlv+egegD+h529oZx+jD5edzHL5
  gpl7p15NkETQP1OLMWFk17Gxq8rGCmOxOI3CEFqmm/BUYLrVuSrq4JKtyBNlLEp7Rt5h+8
  ...
  -----END CERTIFICATE-----

- imageContentSources:
If you are using mirror registry, this should include the mirror registry you have created.

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

## create ignition config’s
```
OPENSHIFT_INSTALL_RELEASE_IMAGE_OVERRIDE=registry.ocp4.ibm.com:5000/ocp4/openshift4:4.3.18-ppc64le openshift-install create ignition-configs
```

10) python3 update_ignition_bootstrap.py

11) copy ignition vm’s to webserver
cp ~/ocp4/*.ign /var/www/html/ignition/
restorecon -vR /var/www/html/
chmod o+r /var/www/html/ignition/*.ign

 
12) openshift-install wait-for bootstrap-complete --log-level debug
    Refer journalctl -f  (log output on bootstrap and master server)
          journalcrl -b -f -u bootkube.services ( log output on bootstrap)


13) Reboot the servers to use DHCP 

 

Final Install Steps:
1) export KUBECONFIG=/root/oc4_new_install/auth/kubeconfig
oc patch configs.imageregistry.operator.openshift.io cluster --type merge --patch ‘{“spec”:{“managementState”:“Managed”}}’
oc patch configs.imageregistry.operator.openshift.io/cluster --type merge -p ‘{“spec”:{“defaultRoute”:true}}’

 

2) watch oc get csr
oc get csr --no-headers | awk ‘{print $1}’ | xargs oc adm certificate approve
oc get csr | grep ‘system:node’
