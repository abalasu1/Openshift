# Build Strategies

## Binary Build

### Create Imagestream
```
oc apply -f imagestream.yaml
```

#### Create Buildconfig, mark build type = Binary, This creates the image and puts it into the internal registry

##### Docker build
```
oc apply -f buildconfig-dockerbuild.yaml
oc start-build build-bc
```

##### Binary build
```
oc apply -f buildconfig-binarybuild.yaml
oc start-build build-bc --from-dir=../apps/Simple-SpringBoot-App/
```

#### Create the deployment, service & router
```
oc apply -f deployment.yaml
oc apply -f service.yaml
oc apply -f route.yaml
```

#### Test the deployed application
```
curl "http://$(oc get routes build-rt -o jsonpath='{.spec.host}')/basicop/add?n1=100&n2=200"
```

#### Cleanup the deployes application
```
oc delete -f route.yaml
oc delete -f service.yaml
oc delete -f deployment.yaml
oc delete -f buildconfig-binarybuild.yaml
oc delete -f imagestream.yaml
```