# Authentication

## Integration with Enterprise LDAP

### IBM Bluepages:
- LDAP URL: ldap://bluepages.ibm.com:389/ou=bluepages,o=ibm.com?emailAddress?sub?(objectClass=person)
- preferredUsername: emailAddress
- name: cn
- id: uid
- email: emailAddress

- Leave Bind DN and Bind password and CA File blank.

- Save the setting and login with the ldap authentication provider.

### To give admin rights to your id, if needed, do this sparingly for a few users:
- oc adm policy add-cluster-role-to-user cluster-admin abalasu1@in.ibm.com
- oc adm policy add-scc-to-user privileged abalasu1@in.ibm.com

### htpasswd:
- Option 1: install htpasswd on mac osx or linux (depends on your os) Option 2: use htpasswd
on a docker container (requires docker on your machine)

- docker run --rm -ti xmartlabs/htpasswd admin password >> htpasswd
- docker run --rm -ti xmartlabs/htpasswd arunb password >> htpasswd

- Setup htpassword authentication on Openshift


