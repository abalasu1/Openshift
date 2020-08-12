# Setup Mirror Registry

Option 1: Create mirror registry with the manual steps documented here: https://docs.openshift.com/container-platform/4.3/installing/install_config/installing-restricted-networks-preparations.html

- Log into Infrastructure Provider page and download openshift-install, openshift client and pull secret
(Requires redhat id and password)

- Setup podman (yum -y install podman httpd-tools)

- Create directories for data & certs mkdir -p /opt/registry/{auth,certs,data}

- Create Certificates:
cd /opt/registry/certs
openssl req -newkey rsa:4096 -nodes -sha256 -keyout domain.key -x509 -days 365 -out domain.crt

While generating certificates, provide hostname of the machine for "Common Name"

- Generate username and password for the registry
htpasswd -bBc /opt/registry/auth/htpasswd <user_name> <password> 

- Create mirror registry
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

podman ps to check if mirror registry pod is running after this command. If a pod is not running,
this command did not work.

- Issue with machines with multiple nic's (???), try localhost to see if it works, although localhost
cannot be a permanent solution.

- Open firewall ports:
firewall-cmd --add-port=<local_registry_host_port>/tcp --zone=internal --permanent 
firewall-cmd --add-port=<local_registry_host_port>/tcp --zone=public   --permanent 
firewall-cmd --reload

- Trust Self Signed Certificates:
cp /opt/registry/certs/domain.crt /etc/pki/ca-trust/source/anchors/
update-ca-trust

- Confirm Registry is available:
curl -u <user_name>:<password> -k https://<local_registry_host_name>:<local_registry_host_port>/v2/_catalog 
Expected Result: {"repositories":[]}

Option 2: https://github.com/RedHatOfficial/ocp4-helpernode - This ansible script does much more than just the mirror registry. Refer [Setup Helper Node](Helper-Node.md)

## Check if images are downloaded to the mirror registry properly
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list

## Cleanup mirror registry
Assuming you have the mirror registry set up:

1) These commands will give data of images in the image reigstry
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list

2) Cleanup podman images
- podman ps - gives all the ids of containers
- podman stop c7731e038713 - get all ids from previous step and stop them
- podman rm c7731e038713 - get all ids from previous step and remove them

3) podman images, get all ids from previous step and stop them
podman rmi -f f00f63be0440

4) remove registry data
rm -rf /opt/registry/certs/*
rm -rf /opt/registry/auth/*
rm -rf /opt/registry/data/*
rm -rf /etc/pki/ca-trust/source/anchors/domain.crt

5) All of these should come up empty
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list