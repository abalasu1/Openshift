PROJECT=calculator
DOCKERREPOSITORY=image-registry.openshift-image-registry.svc:5000

SUBDOMAIN=apps.gsitest.cp.fyre.ibm.com

BASICOPADD=$(DOCKERREPOSITORY)/$(PROJECT)/add
BASICOPSUB=$(DOCKERREPOSITORY)/$(PROJECT)/subtract
BASICOPMUL=$(DOCKERREPOSITORY)/$(PROJECT)/multiply
BASICOPDIV=$(DOCKERREPOSITORY)/$(PROJECT)/divide
COMPOSITEOP=$(DOCKERREPOSITORY)/$(PROJECT)/composite-op
PROCESSOR=$(DOCKERREPOSITORY)/$(PROJECT)/processor
EVENTPROCESSOR=$(DOCKERREPOSITORY)/$(PROJECT)/eventprocessor
UI=$(DOCKERREPOSITORY)/$(PROJECT)/ui

GITHUBREPO=https://github.com/abalasu1/Openshift.git

FORTIO_POD=$(shell kubectl get pod -n $(PROJECT) -l app=fortio -o jsonpath='{.items[0].metadata.name}')
DEBUG_POD=$(shell kubectl get pod -n $(PROJECT) -l app=curl -o jsonpath='{.items[0].metadata.name}')

init:
	-bash -c 'oc new-project $(PROJECT)'

clean:
	-bash -c 'oc delete project $(PROJECT)'

init-add:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-add/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g' -e 's~<DOMAIN>~add.$(SUBDOMAIN)~g' ./basic-op-add/route.yaml  | oc apply -f -

build-add:
	-sed -e 's~<CONTAINER>~$(BASICOPADD)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-add/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-add/buildconfig.yaml  | oc apply -f -	
	oc start-build add

deploy-add:
	-sed -e 's~<CONTAINER>~$(BASICOPADD)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-add/deployment.yaml  | oc apply -f -

clean-add:
	-oc delete all -l app=add

test-add:
	-bash -c 'basic-op-add/test.sh add.$(SUBDOMAIN)'

init-subtract:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-subtract/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g' -e 's~<DOMAIN>~subtract.$(SUBDOMAIN)~g' ./basic-op-subtract/route.yaml  | oc apply -f -	

build-subtract:
	-sed -e 's~<CONTAINER>~$(BASICOPSUB)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-subtract/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-subtract/buildconfig.yaml  | oc apply -f -	
	oc start-build subtract
	
deploy-subtract:
	-sed -e 's~<CONTAINER>~$(BASICOPSUB)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-subtract/deployment.yaml  | oc apply -f -

clean-subtract:
	-oc delete all -l app=subtract

test-subtract:
	-bash -c 'basic-op-subtract/test.sh subtract.$(SUBDOMAIN)'	

init-multiply:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-multiply/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g' -e 's~<DOMAIN>~multiply.$(SUBDOMAIN)~g' ./basic-op-multiply/route.yaml  | oc apply -f -	

build-multiply:
	-sed -e 's~<CONTAINER>~$(BASICOPMUL)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-multiply/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-multiply/buildconfig.yaml  | oc apply -f -	
	oc start-build multiply
	
deploy-multiply:
	-sed -e 's~<CONTAINER>~$(BASICOPMUL)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-multiply/deployment.yaml  | oc apply -f -

clean-multiply:
	-oc delete all -l app=multiply

test-multiply:
	-bash -c 'basic-op-multiply/test.sh multiply.$(SUBDOMAIN)'	

init-divide:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-divide/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g' -e 's~<DOMAIN>~divide.$(SUBDOMAIN)~g' ./basic-op-divide/route.yaml  | oc apply -f -	

build-divide:
	-sed -e 's~<CONTAINER>~$(BASICOPDIV)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-divide/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-divide/buildconfig.yaml  | oc apply -f -	
	oc start-build divide
	
deploy-divide:
	-sed -e 's~<CONTAINER>~$(BASICOPDIV)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./basic-op-divide/deployment.yaml  | oc apply -f -

clean-divide:
	-oc delete all -l app=divide

test-divide:
	-bash -c 'basic-op-divide/test.sh divide.$(SUBDOMAIN)'	

init-compositeop:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./composite-op/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g' -e 's~<DOMAIN>~composite-op.$(SUBDOMAIN)~g' ./composite-op/route.yaml  | oc apply -f -	

build-compositeop:
	-sed -e 's~<CONTAINER>~$(COMPOSITEOP)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./composite-op/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./composite-op/buildconfig.yaml  | oc apply -f -	
	oc start-build composite-op

deploy-compositeop:
	-sed -e 's~<CONTAINER>~$(COMPOSITEOP)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./composite-op/deployment.yaml  | oc apply -f -

clean-compositeop:
	-oc delete all -l app=compositeop

test-compositeop:
	-bash -c 'composite-op/test.sh composite-op.$(SUBDOMAIN)'	

init-processor:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./processor/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g'  -e 's~<DOMAIN>~processor.$(SUBDOMAIN)~g' ./processor/route.yaml  | oc apply -f -	

build-processor:
	-sed -e 's~<CONTAINER>~$(PROCESSOR)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./processor/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./processor/buildconfig.yaml  | oc apply -f -	
	oc start-build processor

deploy-processor:
	-sed -e 's~<CONTAINER>~$(PROCESSOR)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./processor/deployment.yaml  | oc apply -f -

clean-processor:
	-oc delete all -l app=processor

test-processor:
	-bash -c 'processor/test.sh processor.$(SUBDOMAIN)'	

init-eventprocessor:
	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./eventprocessor/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g'  -e 's~<DOMAIN>~eventprocessor.$(SUBDOMAIN)~g' ./eventprocessor/route.yaml  | oc apply -f -	

build-eventprocessor:
	-sed -e 's~<CONTAINER>~$(EVENTPROCESSOR)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./eventprocessor/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./eventprocessor/buildconfig.yaml  | oc apply -f -	
	oc start-build eventprocessor

deploy-eventprocessor:
	-sed -e 's~<CONTAINER>~$(EVENTPROCESSOR)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./eventprocessor/deployment.yaml  | oc apply -f -

clean-eventprocessor:
	-oc delete all -l app=eventprocessor

test-eventprocessor:
	-bash -c 'eventprocessor/test.sh eventprocessor.$(SUBDOMAIN)'	

init-ui:
	-bash -c 'oc create serviceaccount ui-sa -n $(PROJECT) --as system:admin'
	-bash -c 'oc adm policy add-scc-to-user anyuid -z ui-sa -n $(PROJECT) --as system:admin'

	-sed -e 's~<PROJECT>~$(PROJECT)~g' ./ui/service.yaml  | oc apply -f -
	-sed -e 's~<PROJECT>~$(PROJECT)~g' -e 's~<DOMAIN>~ui.$(SUBDOMAIN)~g' ./ui/route.yaml  | oc apply -f -	

build-ui:
	-sed -e 's~<CONTAINER>~$(UI)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./ui/imagestream.yaml  | oc apply -f -
	-sed -e 's~<GITHUBREPO>~$(GITHUBREPO)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./ui/buildconfig.yaml  | oc apply -f -	
	oc start-build ui
	
deploy-ui:
	-sed -e 's~<CONTAINER>~$(UI)~g' -e 's~<PROJECT>~$(PROJECT)~g' ./ui/deploymentconfig.yaml  | oc apply -f -
	
clean-ui:
	-oc delete sa ui-sa
	-oc delete all -l app=ui

init-all: init init-add init-subtract init-multiply init-divide init-compositeop init-processor init-ui
build-all: build-add build-subtract build-multiply build-divide build-compositeop build-processor build-ui
push-all: push-add push-subtract push-multiply push-divide push-compositeop push-processor push-ui
deploy-all: deploy-add deploy-subtract deploy-multiply deploy-divide deploy-compositeop	deploy-processor deploy-ui
clean-all: clean-add clean-subtract clean-multiply clean-divide clean-compositeop clean-processor clean-ui clean
test-all: test-add test-subtract test-multiply test-divide test-compositeop test-processor

UI_POD_NAME=$(shell kubectl -n $(PROJECT) get pod -l app=ui -o jsonpath='{.items[0].metadata.name}') 

uiaccess:
	$(shell kubectl -n $(PROJECT) port-forward $(UI_POD_NAME) 9080:80)
	
restart-all:
	kubectl delete pods --all -n $(PROJECT)
