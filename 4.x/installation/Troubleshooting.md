# Troubleshooting issues during installtion

## Logging into bootstrap, master & worker nodes
- During the install, as the nodes are installed and booted up for troubleshooting purposes there is a need to log into various nodes. ssh key specified in the install-confi.yaml should be used to login to the nodes.

Ex: Use the virtual machine name exactly 
```
ssh -i helper_rsa.pub core@bootstrap

ssh -i helper_rsa.pub core@master-0
...

ssh -i helper_rsa.pub core@worker-0
...
```

## X.509 Certificates are expired or invalid errors
- Most of the certificate expired or invalid issues are because of time difference between the various nodes in the cluster.
- Configure ntp/chrony and set the timezone of the underlying virtualization platform same as that of bastion. This will ensure bastion, bootstrap, master & worker nodes have the same time and timezone.
- In case of offline installation, use the enterprise ntp servers, instead of internet based sync
- Depending on when the certificate expired occurs, different certificates need to be checked.
if the error occurs during machine bootup, check machine-config certificates:
```
openssl s_client -connect 9.30.38.164:22623 | openssl x509 -noout -text
```
else if error occurs after the etcd cluster gets created:
```
openssl s_client -connect 9.30.38.164:6443 | openssl x509 -noout -text
```
