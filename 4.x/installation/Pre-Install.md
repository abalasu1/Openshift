# Pre Install Staps

## Setup Mirror Registry
** This is needed only if the internet connection is not available from the master and worker nodes, a mirror registry needs to be created, on a machine with internet connection.

### Option 1: Create mirror registry with the manual steps documented here: https://docs.openshift.com/container-platform/4.3/installing/install_config/installing-restricted-networks-preparations.html

- Log into Infrastructure Provider page and download openshift-install, openshift client and pull secret
(Requires redhat id and password)

- Setup podman: 
```
yum -y install podman httpd-tools
```

- Create directories for data & certs: 
```
mkdir -p /opt/registry/auth
mkdir -p /opt/registry/certs
mkdir -p /opt/registry/data
```

- Create Certificates. While generating certificates, "Common Name" is the most important parameter, certificates are generated for this name. Provide hostname/ip of the vm or something like registry.ocp4.ibm.com, which can resolve to the ip of the machine.
```
cd /opt/registry/certs
openssl req -newkey rsa:4096 -nodes -sha256 -keyout domain.key -x509 -days 365 -out domain.crt
```

- Generate username and password for the registry (ex: user_name=admin, password=admin)
```
htpasswd -bBc /opt/registry/auth/htpasswd <user_name> <password> 
```

- Create mirror registry: (ex: local_registry_host_port=registry.ocp4.ibm.com)
```
podman run --name mirror-registry -p <local_registry_host_port>:5000 \ 
     -v /opt/registry/data:/var/lib/registry:z \
     -v /opt/registry/auth:/auth:z \
     -e "REGISTRY_AUTH=htpasswd" \
     -e "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm" \
     -e REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd \
     -v /opt/registry/certs:/certs:z \
     -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
     -e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
     -e REGISTRY_COMPATIBILITY_SCHEMA1_ENABLED=true \
     -d docker.io/library/registry:2
```

podman ps to check if mirror registry pod is running after this command. If a pod is not running,
this command did not work.

Issue with machines with multiple nic's (???), try localhost to see if it works, although localhost
cannot be a permanent solution.

- Open firewall ports (if firewall is not running, these commands are not needed):
```
firewall-cmd --add-port=<local_registry_host_port>/tcp --zone=internal --permanent 
firewall-cmd --add-port=<local_registry_host_port>/tcp --zone=public   --permanent 
firewall-cmd --reload
```

- Trust Self Signed Certificates:
```
cp /opt/registry/certs/domain.crt /etc/pki/ca-trust/source/anchors/
update-ca-trust
```

- Confirm Registry is available. (ex: local_registry_host_port=registry.ocp4.ibm.com, local_registry_host_port= 5000, user_name=admin, password=admin)
```
curl -u <user_name>:<password> -k https://<local_registry_host_name>:<local_registry_host_port>/v2/_catalog 
```
Expected Result: {"repositories":[]}

- Update pullsecret with username and password created with htpasswd earlier:
Create encoded pull secret (ex: user_name=admin, password=admin)
```
echo -n '<user_name>:<password>' | base64 -w0
```

b) Update pull secret downloaded from redhat site  (ex: <path>/<pull-secret-file>=/root/.openshift/pull-secret-updated)
```
cat ./pull-secret.text | jq .  > <path>/<pull-secret-file>
```

c) Create the updated pull secret. (ex: mirror_registry=registry.ocp4.ibm.com:5000, Credentials = base64 encoded credentials)
```
"auths": {
...
    "<mirror_registry>": { 
      "auth": "<credentials>", 
      "email": "you@example.com"
  },
...
```

- Pull images to the image registry. (ex: local_registry_host_port=registry.ocp4.ibm.com, local_registry_host_port= 5000, repository_name=ocp4/openshift4, <path_to_pull_secret>=/root/.openshift/pull-secret-updated)
```
x86:
export OCP_RELEASE=4.3.18 
export LOCAL_REGISTRY='<local_registry_host_name>:<local_registry_host_port>' 
export LOCAL_REPOSITORY='<repository_name>' 
export PRODUCT_REPO='openshift-release-dev' 
export LOCAL_SECRET_JSON='<path_to_pull_secret>' 
export RELEASE_NAME="ocp-release" 

ppc64le:
export OCP_RELEASE=4.3.18-ppc64le
export LOCAL_REGISTRY='<local_registry_host_name>:<local_registry_host_port>' 
export LOCAL_REPOSITORY='<repository_name>' 
export PRODUCT_REPO='openshift-release-dev' 
export LOCAL_SECRET_JSON='<path_to_pull_secret>' 
export RELEASE_NAME="ocp-release" 
```

- Mirror the repository
```
oc adm -a ${LOCAL_SECRET_JSON} release mirror \
     --from=quay.io/${PRODUCT_REPO}/${RELEASE_NAME}:${OCP_RELEASE} \
     --to=${LOCAL_REGISTRY}/${LOCAL_REPOSITORY} \
     --to-release-image=${LOCAL_REGISTRY}/${LOCAL_REPOSITORY}:${OCP_RELEASE}
```

- Note down the imagecontentsources output from the previous command
```
Example Output:

imageContentSources:
- mirrors:
  - registry.ocp4.bancs.com:5000/ocp4/openshift4
  source: quay.io/openshift-release-dev/ocp-release
- mirrors:
  - registry.ocp4.bancs.com:5000/ocp4/openshift4
  source: quay.io/openshift-release-dev/ocp-v4.0-art-dev
```

- Extract the openshift-install for the mirror registry
```
oc adm -a ${LOCAL_SECRET_JSON} release extract --command=openshift-install "${LOCAL_REGISTRY}/${LOCAL_REPOSITORY}:${OCP_RELEASE}"
```

### Option 2: https://github.com/RedHatOfficial/ocp4-helpernode - This ansible script does much more than just the mirror registry. 
[Setup Helper Node](Helper-Node.md)

### Check if images are downloaded to the mirror registry properly
```
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list
```
## URL's to whitelist (Needed only in case of an Disconnected Install)

1) registry.redhat.io - Provides core container images
2) *.quay.io - Provides core container images
3) sso.redhat.com - The https://cloud.redhat.com/openshift site uses authentication from sso.redhat.com
4) mirror.openshift.com - Required to access mirrored installation content and images
5) *.cloudfront.net - Required by the Quay CDN to deliver the Quay.io images that the cluster requires
6) *.apps.<cluster_name>.<base_domain> - Required to access the default cluster routes unless you set an ingress wildcard during installation
7) quay-registry.s3.amazonaws.com - Required to access Quay image content in AWS
8) api.openshift.com - Required to check if updates are available for the cluster
9) art-rhcos-ci.s3.amazonaws.com - Required to download Red Hat Enterprise Linux CoreOS (RHCOS) images
10) cloud.redhat.com/openshift - Required for your cluster token
11) https://dl.fedoraproject.org/pub/epel/epel-release-latest-$(rpm -E %rhel).noarch.rpm - Needed for helpernode