# Cleanup after a failed install

## Cleanup mirror registry
1) These commands will give data of images in the image reigstry
```
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list
```

2) Cleanup podman images
```
Gives all the ids of containers
- podman ps

Get all ids from previous step and stop them
- podman stop c7731e038713

Get all ids from previous step and remove them
- podman rm c7731e038713
```

3) podman images, get all ids from previous step and stop them
```
podman rmi -f f00f63be0440
```

4) remove registry data
```
rm -rf /opt/registry/certs/*
rm -rf /opt/registry/auth/*
rm -rf /opt/registry/data/*
rm -rf /etc/pki/ca-trust/source/anchors/domain.crt
```

5) All of these should come up empty
```
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/_catalog
curl -s -u admin:admin https://registry.ocp4.bancs.com:5000/v2/ocp4/openshift4/tags/list
```

## Remove files created by install before restarting the install
Remove files & directories 

```
rm /var/www/html/install/*
rm /var/www/html/ignition/*
rm /usr/local/src/openshift-client-linux.tar.gz
rm /usr/local/src/openshift-install-linux.tar.gz
rm /opt/registry/certs/
rm /usr/local/bin/openshift-install

systemctl restart httpd
```

```
Applicable for ppc64le upi installs
rm /var/www/html/install/bios.raw.gz
rm /var/lib/tftpboot/rhcos/initramfs.img
rm /var/lib/tftpboot/rhcos/kernel
rm /var/lib/tftpboot/boot/grub2/grub.cfg
```