# Binary Build

## Create Imagestream
```
oc apply -f imagestream.yaml
```

## Create Buildconfig, mark build type = Binary, This creates the image and puts it into the internal registry
```
oc apply -f buildconfig.yaml
oc start-build binary-build --from-dir=../../apps/Simple-SpringBoot-App/
```

## Create the deployment, service & router
```
oc apply -f deployment.yaml
oc apply -f service.yaml
oc apply -f route.yaml
```