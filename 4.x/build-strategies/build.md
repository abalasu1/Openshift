# Build Strategies

## For disconnected installations
### In disconnected all the images needed should be in the internal registry

#### Push the images needed to the internal registry (**This operation should be done on a machine whose cpu architecture matches the one where the cluster is running. For ex: if the cluster is on ppc64le, download the images on a power machine and push it to the cluster internal registry)
If you are using podman on your local laptop
```
podman login 
```

## Docker Build
### Create the project
```
oc new-project build-strategies
```

### Create Imagestream
```
oc apply -f imagestream.yaml
```

### Buildconfig with docker strategy
```
oc apply -f buildconfig-dockerbuild.yaml
oc start-build build-bc
```

### Create the deployment, service & router
```
oc apply -f deployment.yaml
oc apply -f service.yaml
oc apply -f route.yaml
```

### Test the deployed application
```
curl "http://$(oc get routes build-rt -o jsonpath='{.spec.host}')/basicop/add?n1=100&n2=200"
```

### Cleanup the deployes application
```
oc delete -f route.yaml
oc delete -f service.yaml
oc delete -f deployment.yaml
oc delete -f buildconfig-binarybuild.yaml
oc delete -f imagestream.yaml
```

## Binary Build
### Create the project
```
oc new-project build-strategies
```

### Create Imagestream
```
oc apply -f imagestream.yaml
```

### Binary build - Buildconfig with binary strategy
```
oc apply -f buildconfig-binarybuild.yaml
oc start-build build-bc --from-dir=../apps/Simple-SpringBoot-App/
```

### Create the deployment, service & router
```
oc apply -f deployment.yaml
oc apply -f service.yaml
oc apply -f route.yaml
```

### Test the deployed application
```
curl "http://$(oc get routes build-rt -o jsonpath='{.spec.host}')/basicop/add?n1=100&n2=200"
```

### Cleanup the deployes application
```
oc delete -f route.yaml
oc delete -f service.yaml
oc delete -f deployment.yaml
oc delete -f buildconfig-binarybuild.yaml
oc delete -f imagestream.yaml
```

## Binary Build - For a disconnection installation without internet connection
### Create the project
```
oc new-project build-strategies
```

### Push the images needed to the internal registry (**This operation should be done on a machine whose cpu architecture matches the one where the cluster is running. For ex: if the cluster is on ppc64le, download the images on a power machine and push it to the cluster internal registry)
```
```

### Create Imagestream
```
oc apply -f imagestream.yaml
```

### Binary build - Buildconfig with binary strategy
```
oc apply -f buildconfig-binarybuild-internal-registry.yaml
oc start-build build-bc --from-dir=../apps/Simple-SpringBoot-App/
```

### Create the deployment, service & router
```
oc apply -f deployment.yaml
oc apply -f service.yaml
oc apply -f route.yaml
```

### Test the deployed application
```
curl "http://$(oc get routes build-rt -o jsonpath='{.spec.host}')/basicop/add?n1=100&n2=200"
```

### Cleanup the deployes application
```
oc delete -f route.yaml
oc delete -f service.yaml
oc delete -f deployment.yaml
oc delete -f buildconfig-binarybuild.yaml
oc delete -f imagestream.yaml
```