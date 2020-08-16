# Post Install Steps

## Deploy a simple application for validation
For clusters which can pull from internet:
```
ppc64le: 
oc run nginx --image=ppc64le/nginx --replicas=3

x86_64: 
oc run nginx --image=bitnami/nginx --replicas=3
```

For clusters with no internet connection (Need to set internal registry first):
```
ppc64le: 
podman pull ppc64le/nginx
podman tag ppc64le/nginx default-route-openshift-image-registry.apps.gsitest.cp.fyre.ibm.com/default/nginx

podman push default-route-openshift-image-registry.apps.gsitest.cp.fyre.ibm.com/default/nginx
oc run nginx --image=image-registry.openshift-image-registry:5000/default/nginx

x86_64: 
podman pull bitnami/nginx
podman tag bitnami/nginx default-route-openshift-image-registry.apps.gsitest.cp.fyre.ibm.com/default/nginx

podman push default-route-openshift-image-registry.apps.gsitest.cp.fyre.ibm.com/default/nginx
oc run nginx --image=image-registry.openshift-image-registry:5000/default/nginx
```

## Setup internal registry
- Make sure registry is not running
```
oc get pod -n openshift-image-registry
```

- Switch the management state of the registry from Removed to Managed
```
oc patch configs.imageregistry.operator.openshift.io cluster --type merge --patch ‘{“spec”:{“managementState”:“Managed”}}’
```

- Expose registry with a route
```
oc patch configs.imageregistry.operator.openshift.io/cluster --type merge -p ‘{“spec”:{“defaultRoute”:true}}’
```

- Set up persistent storage for image registry
For non production clusters:
```
oc patch configs.imageregistry.operator.openshift.io cluster --type merge --patch '{"spec":{"storage":{"emptyDir":{}}}}'
```

For production clusters:
Create a persistent volume claim and assign that claim to the registry storage
```
cat > pvc-registry.yaml << EOF
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: image-registry-nfs
  annotations:
    volume.beta.kubernetes.io/storage-class: "managed-nfs-storage"
  namespace: openshift-image-registry
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 100Gi
EOF
```

```
oc edit configs.imageregistry.operator.openshift.io

storage:
  pvc:
    claim: image-registry-nfs
```

- Access registry from bastion or any of the masters/workers:
```
HOST=$(oc get route default-route -n openshift-image-registry --template='{{ .spec.host }}')
podman login -u $(oc whoami) -p $(oc whoami -t) --tls-verify=false $HOST 
```

- Adding the registry ad insecure registry https://www.projectatomic.io/blog/2018/05/podman-tls/ to connect from external systems

## Setup nfs provisioner for dynamic provisioning with NFS

- Check if nfs provisioner already exits:
```
oc get pods --all-namespaces | grep provisioner
```

- Create nfs provisioner deployment
```
cat > deployment.yaml << EOF
kind: Deployment
apiVersion: apps/v1
metadata:
  name: nfs-client-provisioner
spec:
  replicas: 1
  selector:
    matchLabels:
      app: nfs-client-provisioner
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: nfs-client-provisioner
    spec:
      serviceAccountName: nfs-client-provisioner
      containers:
        - name: nfs-client-provisioner
          image: registry.ocp4.ibm.com:5000/nfs-client-provisioner-ppc64le:latest
          volumeMounts:
            - name: nfs-client-root
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME
              value: nfs-storage
            - name: NFS_SERVER
              value: bastion_ip_address
            - name: NFS_PATH
              value: nfs_path
      volumes:
        - name: nfs-client-root
          nfs:
            server: bastion_ip_address
            path: nfs_path
EOF
```

- Create storage class:
```
cat > class.yaml << EOF
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: managed-nfs-storage
provisioner: storage.io/nfs-storage # or choose another name, must match deployment's env PROVISIONER_NAME'
parameters:
  archiveOnDelete: "false"
EOF
```

- Test with the following pvc:
```
cat > pvc.yaml << EOF
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: test-claim
  annotations:
    volume.beta.kubernetes.io/storage-class: "managed-nfs-storage"
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 100Mi
EOF
```

- Make the storage class default:
```
oc patch storageclass managed-nfs-storage -p '{"metadata": {"annotations": {"storageclass.kubernetes.io/is-default-class": "true"}}}'
```

5) Setup persistent storage for registry:
a) 

oc create -f deploy/pvc-registry.yaml

b) oc edit configs.imageregistry.operator.openshift.io

add pvc-claim:
storage:
  pvc:
    claim: image-registry-nfs