# Monitoring

## Create custom Grafana dashbaords

### IBM Bluepages:
- LDAP URL: ldap://bluepages.ibm.com:389/ou=bluepages,o=ibm.com?emailAddress?sub?(objectClass=person)
- preferredUsername: emailAddress
- name: cn
- id: uid
- email: emailAddress

- Leave Bind DN and Bind password and CA File blank.

- Save the setting and login with the ldap authentication provider.