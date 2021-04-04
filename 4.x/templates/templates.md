# Setup, configure and manage openshift standard templates

## Setup templates on Openshift on Power 

```
If you are using podman on your local laptop or a machine which is not one of the cluster nodes:
export REGISTRY_URL=$(oc get routes default-route -n openshift-image-registry -o jsonpath='{.spec.host}')
else if you are running this command on one of the cluster nodes:
export REGISTRY_URL=image-registry.openshift-image-registry.svc:5000

export REGISTRY_URL=$(oc get routes default-route -n openshift-image-registry -o jsonpath='{.spec.host}')
podman login $REGISTRY_URL -u $(oc whoami) -p $(oc whoami -t)
```

### Database templates
#### Setup Redis template
```
For disconnected installations:
podman pull registry.redhat.io/rhel8/redis-5
podman tag registry.redhat.io/rhel8/redis-5 $REGISTRY_URL/openshift/redis
podman push $REGISTRY_URL/openshift/redis

oc apply -f database/redis-ephemeral.yaml
otherwise:
templates will be there by default, nothing needs to be done.
```

#### Deploy redis database
```
oc process redis-ephemeral -p REDIS_PASSWORD=password -n openshift | oc create -f -
```