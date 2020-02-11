```bash
NEXUS_NAMESPACE=microservices-dev

oc new-app --docker-image docker.io/sonatype/nexus3:latest -n ${NEXUS_NAMESPACE}

oc rollout pause dc/nexus3 -n ${NEXUS_NAMESPACE}

oc patch dc nexus3 -p '{"spec":{"strategy":{"type":"Recreate"}}}' -n ${NEXUS_NAMESPACE}

oc expose svc nexus3 -n ${NEXUS_NAMESPACE}

oc set resources dc nexus3 --limits=memory=4Gi,cpu=2 --requests=memory=2Gi,cpu=500m -n ${NEXUS_NAMESPACE}

oc set volume dc/nexus3 --add --overwrite --name=nexus3-volume-1 --mount-path=/nexus-data/ --type persistentVolumeClaim --claim-name=nexus-pvc --claim-size=10Gi -n ${NEXUS_NAMESPACE}

oc set probe dc/nexus3 --liveness --failure-threshold 3 --initial-delay-seconds 60 -- echo ok -n ${NEXUS_NAMESPACE}

oc set probe dc/nexus3 --readiness --failure-threshold 3 --initial-delay-seconds 60 --get-url=http://:8081/ -n ${NEXUS_NAMESPACE}

oc rollout resume dc nexus3 -n ${NEXUS_NAMESPACE}

oc get pods -n ${NEXUS_NAMESPACE} | grep Running
#[root@ocp-bastion lab04]# oc get pods | grep Running
#nexus3-xyz    1/1     Running     0          2m36s

export NEXUS_POD=$(oc get pods --selector app=nexus3 -n ${NEXUS_NAMESPACE} | { read line1 ; read line2 ; echo "$line2" ; } | awk '{print $1;}')
oc exec ${NEXUS_POD} cat /nexus-data/admin.password
# 33456b65-7e85-4dfc-a063-78b413cf4a47

oc get routes -n ${NEXUS_NAMESPACE} | grep nexus3

# log into the nexus and change password to admin123

curl -o setup_nexus3.sh -s https://raw.githubusercontent.com/aelkz/microservices-observability/master/_configuration/nexus/setup_nexus3.sh

chmod +x setup_nexus3.sh

./setup_nexus3.sh admin admin123 http://$(oc get route nexus3 --template='{{ .spec.host }}')

oc expose dc nexus3 --port=5000 --name=nexus-registry -n ${NEXUS_NAMESPACE}

oc create route edge nexus-registry --service=nexus-registry --port=5000 -n ${NEXUS_NAMESPACE}

oc annotate route nexus3 --overwrite haproxy.router.openshift.io/timeout=5m
```
