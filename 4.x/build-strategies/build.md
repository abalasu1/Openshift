# Build Strategies

## For disconnected installations
### In disconnected installations, all the images needed should be in the internal registry

#### Push the images needed to the internal registry (**This operation should be done on a machine whose cpu architecture matches the one where the cluster is running. For ex: if the cluster is on ppc64le, download the images on a power machine and push it to the cluster internal registry)
```
If you are using podman on your local laptop or a machine which is not one of the cluster nodes:
export REGISTRY_URL=$(oc get routes default-route -n openshift-image-registry -o jsonpath='{.spec.host}')
else if you are running this command on one of the cluster nodes:
export REGISTRY_URL=image-registry.openshift-image-registry.svc:5000

# Login into the private registry (this does not work with kubeadmin, use some other user)
podman login $REGISTRY_URL -u $(oc whoami) -p $(oc whoami -t)

# Pull image from redhat registry
podman pull registry.access.redhat.com/ubi8/openjdk-11
podman pull registry.access.redhat.com/ubi8/ubi:8.2

# Tag the image with the private registry
podman tag registry.access.redhat.com/ubi8/openjdk-11 $REGISTRY_URL/build-strategies/openjdk-11
podman tag registry.access.redhat.com/ubi8/ubi:8.2 $REGISTRY_URL/build-strategies/ubi:8.2

# Push the image to private registry
podman push $REGISTRY_URL/build-strategies/openjdk-11
podman push $REGISTRY_URL/build-strategies/ubi:8.2

# Check if the imagestreams are created
oc get is

(output)
openjdk-11   default-route-openshift-image-registry.apps.travels.cp.fyre.ibm.com/build-strategies/openjdk-11   latest   5 minutes ago
ubi          default-route-openshift-image-registry.apps.travels.cp.fyre.ibm.com/build-strategies/ubi          8.2      4 seconds ago
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
For installations with internet connection:
oc apply -f buildconfig-dockerbuild.yaml
oc start-build build-bc

else disconnected installations:
oc apply -f buildconfig-dockerbuild-internal-registry.yaml
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
For installations with internet connection:
oc apply -f buildconfig-binarybuild.yaml
oc start-build build-bc --from-dir=../apps/Simple-SpringBoot-App/

else disconnected installations:
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

## Optimizing deployment image:

#### - Use [two step build](https://github.com/abalasu1/Openshift/blob/master/4.x/apps/Simple-SpringBoot-App/Dockerfile-optimized) process especially for compile languages like java. First step builds the jar and the second step can only inlcude what is needed to run the image.

#### - Install only what is needed in the [final image](https://github.com/abalasu1/Openshift/blob/master/4.x/apps/Simple-SpringBoot-App/Dockerfile-optimized). For ex. installing java-11-openjdk-headless instead of java-11-openjdk in the docker file. This decreases the file size because headless removes the ui libraries.

#### - Dockerfile consists of multipla layers. Add "imageOptimizationPolicy: SkipLayers" to the [dockerstrategy](./buildconfig-dockerbuild-optimized.yaml). This is applied by default if a two step build is used.

#### - Use podman history <image_name> to see the sized occupied by each layer in a docker image. Optimize the layers with the biggest size.

## Pull images from one project into another

### Create new project base-images
```
oc new-project base-images
```

### Push image to base-images folder
```
export REGISTRY_URL=$(oc get routes default-route -n openshift-image-registry -o jsonpath='{.spec.host}')
docker pull redhat.registry.io/
podman pull registry.access.redhat.com/ubi8/openjdk-11
podman tag registry.access.redhat.com/ubi8/openjdk-11 $REGISTRY_URL/base_images/openjdk-11
```

### Set it up to pull from a different project for build
```
oc project build-strategies
oc policy add-role-to-user system:image-puller system:serviceaccount:common:builder --namespace=base-images
```

### Deploy the application using this image
```
oc apply -f imagestream.yaml

oc apply -f buildconfig-binarybuild-internal-registry.yaml
oc start-build build-bc --from-dir=../apps/Simple-SpringBoot-App/

oc apply -f route.yaml
oc apply -f service.yaml
oc apply -f deployment.yaml
oc apply -f buildconfig-binarybuild.yaml
oc apply -f imagestream.yaml
```