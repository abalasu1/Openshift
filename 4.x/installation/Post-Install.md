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

## Install standard templates (Needed for ppc64le only)

### Upload images into mirror registry
```
export MIRROR_ADDR=registry.ocp4.ibm.com:5000/ocp4/openshift4

oc image mirror registry.redhat.io/rhel8/redis-5:latest ${MIRROR_ADDR}/rhel8/redis-5:latest
oc image mirror registry.redhat.io/rhel8/postgresql-96:latest ${MIRROR_ADDR}/rhel8/postgresql-96:latest
oc image mirror registry.redhat.io/rhscl/mongodb-36-rhel7 ${MIRROR_ADDR}/rhscl/mongodb-36-rhel7
oc image mirror registry.redhat.io/rhel8/mysql-80 ${MIRROR_ADDR}/rhel8/mysql-80
oc image mirror registry.redhat.io/rhel8/mariadb-103 ${MIRROR_ADDR}/rhel8/mariadb-103

oc create configmap registry-config --from-file=${MIRROR_ADDR_HOSTNAME}:5000=$path/ca.crt -n openshift-config
oc patch image.config.openshift.io/cluster --patch '{"spec":{"additionalTrustedCA":{"name":"registry-config"}}}' --type=merge

oc get configs.samples.operator.openshift.io -n openshift-cluster-samples-operator
```

### Apply the standard templates
```
oc apply -f db-templates.yaml
oc apply -f ci-cd.yaml
```

## Share Openshift across teams

- Disable self provisioning of projects
```
oc patch clusterrolebinding.rbac self-provisioners -p '{"subjects": null}'
```

- Create a new project for a team
```
oc new-project team1
```

- Create a new group for the team
```
cat <<EOF | oc apply -f -
apiVersion: user.openshift.io/v1
kind: Group
metadata:
  name: team1-group
users:
  - user1
  - user2
EOF
```

- Create role bindings within team1 (add as appropriate)
```
# admin within the team1 project
oc adm policy add-role-to-group admin team1-group -n team1

# registry editor to push and pull images from internal registry
oc adm policy add-cluster-role-to-group registry-edit team1-group -n team1

# viewer to access internal registry route
oc adm policy add-cluster-role-to-group view team1-group -n openshift-image-registry

# viewer to access monitoring
oc adm policy add-cluster-role-to-group view team1-group -n openshift-monitoring
```

- Set resource quota (adjust as needed)
```
cat <<EOF | oc apply -f -
apiVersion: v1
kind: ResourceQuota
metadata:
  name: team1-quota
  namespace: team1
spec:
  hard:
    pods: '4'
    requests.cpu: '1'
    requests.memory: 8Gi
    limits.cpu: '2'
    limits.memory: 16Gi
EOF
```