# Monitoring

## Create custom Grafana instance to add dashbaords

- oc new-project app-monitoring

- Deploy custom grafana instance and dashbaords:
a) Search for grafana comunity operator
![](https://user-images.githubusercontent.com/13202504/71592411-1d7af280-2b56-11ea-898e-e1b01948008d.png)

b) Install the grafana operator in app-monitoring namespace
![](https://user-images.githubusercontent.com/13202504/71592590-d6413180-2b56-11ea-9920-5ae753db5a4b.png)

c) Create a grafana instance
![](https://user-images.githubusercontent.com/13202504/71593269-65e7df80-2b59-11ea-909d-3051ae5c2ce1.png)

d) Allow prometheus to connect to custom grafana
oc edit statefulset.apps/prometheus-k8s -n openshift-monitoring
Change '--web.listen-address=127.0.0.1:9090' to '--web.listen-address=:9090'

e) Log into grafana dashboard using the route (oc get routes -n app-monitoring)

d) Sign into the grafana instance using the username & password given during creation of grafana instance (default: root/secret)

e) Add a new datasource to point to the prometheus instance
![](https://user-images.githubusercontent.com/13202504/71593774-579ac300-2b5b-11ea-8dcd-fc77ffadc362.png)

f) Now this grafana instance can be used to create custom application dashboards