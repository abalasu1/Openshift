# Setup Mirror Registry

Option 1: Create mirror registry with the manual steps documented here: https://docs.openshift.com/container-platform/4.3/installing/install_config/installing-restricted-networks-preparations.html

Option 2: https://github.com/RedHatOfficial/ocp4-helpernode - This ansible does much more than just
the mirror registry.

## Cleanup mirror registry

Assuming you have the mirror registry set up:

1) These commands will give data of images in the image reigstry
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list

2) Cleanup podman images
podman ps - gives all the ids of containers
podman stop c7731e038713 - get all ids from previous step and stop them
podman rm c7731e038713 - get all ids from previous step and remove them

3) podman images

get all ids from previous step and stop them
podman rmi f00f63be0440

4) remove registry data
rm -rf /opt/registry/certs/*
rm -rf /opt/registry/auth/*
rm -rf /opt/registry/data/*
rm -rf /etc/pki/ca-trust/source/anchors/domain.crt

5) All of these should come up empty
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog

curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift44/tags/list
