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

### To give admin rights to your id:
- oc adm policy add-cluster-role-to-user cluster-admin abalasu1@in.ibm.com
- oc adm policy add-scc-to-user privileged abalasu1@in.ibm.com