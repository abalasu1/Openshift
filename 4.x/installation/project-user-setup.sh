oc new-project $1
cat <<EOF | oc apply -f -
kind: ResourceQuota
apiVersion: v1
metadata:
  name: $1-quota
  namespace: $1
spec:
  hard:
    limits.cpu: '1'
    limits.memory: 1Gi
    pods: '2'
    requests.cpu: '0.5'
    requests.memory: 0.5Gi
EOF

oc adm groups new $1-group
oc adm groups add-users $1-group $2 $3 $4
oc adm policy add-role-to-group admin $1-group -n $1