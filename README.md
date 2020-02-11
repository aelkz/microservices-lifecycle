
# MICROSERVICES LIFECYCLE
###### HOW TO MANAGE API LIFECYCLE WITH OPEN-SOURCE TOOLS.

<b>TL;DR</b> This is a demonstration on how to manage API lifecycle.

![security](https://raw.githubusercontent.com/aelkz/microservices-lifecycle/master/_images/01.png "Microservices Lifecycle")

- Openshift
- 3Scale AMP
- Keycloak
- Spring Boot
- APICURIO
- Microcks
    
```
# Deploy parent project on nexus
API_NAMESPACE=microservices-dev
NEXUS_NAMESPACE=microservices-dev
MAVEN_URL=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-group/
MAVEN_URL_RELEASES=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-releases/
MAVEN_URL_SNAPSHOTS=http://$(oc get route nexus3 -n ${NEXUS_NAMESPACE} --template='{{ .spec.host }}')/repository/maven-snapshots/

# download maven settings.xml file
curl -o maven-settings-template.xml -s https://raw.githubusercontent.com/aelkz/microservices-security/master/_configuration/nexus/maven-settings-template.xml

awk -v path="$MAVEN_URL" '/<url>/{sub(/>.*</,">"path"<")}1' maven-settings-template.xml > maven-settings.xml
rm -fr maven-settings-template.xml

mvn clean package deploy -DnexusReleaseRepoUrl=$MAVEN_URL_RELEASES -DnexusSnapshotRepoUrl=$MAVEN_URL_SNAPSHOTS -s ./maven-settings.xml -e -X -N

# Deploy bank-statement API
# oc delete all -lapp=bank-statement-api -n ${API_NAMESPACE}
oc new-app openjdk-8-rhel8:latest~https://github.com/aelkz/microservices-lifecycle.git --name=bank-statement-api --context-dir=/bank-statement --build-env='MAVEN_MIRROR_URL='${MAVEN_URL} -e MAVEN_MIRROR_URL=${MAVEN_URL} -n ${API_NAMESPACE}
oc patch svc bank-statement-api -p '{"spec":{"ports":[{"name":"http","port":8080,"protocol":"TCP","targetPort":8080}]}}' -n ${API_NAMESPACE}
oc label svc bank-statement-api monitor=springboot2-api -n ${API_NAMESPACE}
```   
 
REFERENCES: 
https://apicurio-studio.readme.io/docs/integrate-microcks-for-mocking-your-api
 
- - - - - - - - - -
Thanks for reading and taking the time to comment!<br>
Feel free to create a <b>PR</b><br>
[raphael abreu](rabreu@redhat.com)
