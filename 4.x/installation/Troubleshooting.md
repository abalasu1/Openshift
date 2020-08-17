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