# Openshift Metering

## Installing Metering Operator:

- In the OpenShift Container Platform web console, click Administration → Namespaces → Create Namespace.
- Set the name to openshift-metering. No other namespace is supported. Label the namespace with openshift.io/cluster-monitoring=true, and click Create.
- Next, click Operators → OperatorHub, and filter for metering to find the Metering Operator.
- Click the Metering card, review the package description, and then click Install.
- On the Create Operator Subscription screen, select the openshift-metering namespace you created above. Specify your update channel and approval strategy, then click Subscribe to install metering.
- On the Installed Operators screen the Status displays InstallSucceeded when metering has finished installing. Click the name of the operator in the first column to view the Operator Details page.

## Prerequisites:
oc project openshift-metering

Create PVC for metering:
oc apply -f pvc.yaml (This pvc should support RWX mode)

## Create meteringconfig:
oc apply -f mc.yaml
- This should be done within openshift-metering namespace.
- Requires dynamic provisioning

## Create Reports:
oc apply -f podcpurequest.yaml

## Getting status on reports: 
oc get reports
oc describe report pod-cpu-request-hourly

## Viewing reports:
reportName=pod-cpu-request-hourly
reportFormat=csv
curl --insecure -H "Authorization: Bearer ${token}" "https://${meteringRoute}/api/v1/reports/get?name=${reportName}&namespace=openshift-metering&format=$reportFormat"

## Query hive using beeline:
oc -n openshift-metering exec -it $(oc -n openshift-metering get pods -l app=hive,hive=server -o name | cut -d/ -f2) -c hiveserver2 -- beeline -u 'jdbc:hive2://127.0.0.1:10000/default;auth=noSasl'
show tables from metering;
select * from metering.report_openshift_metering_pod_cpu_request_hourly; 

## Access Hive UI:
oc -n openshift-metering port-forward hive-server-0 10002
access web console: http://127.0.0.1:10002

## metering operator logs:
detail output:
oc -n openshift-metering logs $(oc -n openshift-metering get pods -l app=metering-operator -o name | cut -d/ -f2) -c ansible

## condensed output:
oc -n openshift-metering logs $(oc -n openshift-metering get pods -l app=metering-operator -o name | cut -d/ -f2) -c operator

## metering config status:
wget https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64