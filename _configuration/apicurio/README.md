Create the template within your namespace

```
PROJECT_NAMESPACE=microservices-dev
OCP_APP_DOMAIN=apps.sememeve.com
OCP_MASTER_URL=https://console.ocp.sememeve.com

oc create -f https://raw.githubusercontent.com/Apicurio/apicurio-studio/master/distro/openshift/apicurio-standalone-template.yml -n ${PROJECT_NAMESPACE}
```

Deploy the application from the template, be sure to replace <NAMESPACE> with your proper namespace

```
oc new-app --template=apicurio-studio-standalone \
 --param=UI_ROUTE=apicurio-studio-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=API_ROUTE=apicurio-studio-api-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=WS_ROUTE=apicurio-studio-ws-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=AUTH_ROUTE=apicurio-studio-auth-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=GENERATED_KC_USER=admin \
 --param=GENERATED_KC_PASS=admin \
 -n ${PROJECT_NAMESPACE}

oc label dc/apicurio-studio-api app=apicurio-studio --overwrite=true
oc label dc/apicurio-studio-auth app=apicurio-studio --overwrite=true
oc label dc/apicurio-studio-ui app=apicurio-studio --overwrite=true
oc label dc/apicurio-studio-ws app=apicurio-studio --overwrite=true

# if already deployed microcks:
oc set env dc/apicurio-studio-api APICURIO_MICROCKS_API_URL=https://microcks-microservices-dev.apps.sememeve.com/api
# don't change (it represents the client-id from microcks realm from keycloak)
oc set env dc/apicurio-studio-api APICURIO_MICROCKS_CLIENT_ID=microcks-serviceaccount
# acquire the client-secret from the client above
oc set env dc/apicurio-studio-api APICURIO_MICROCKS_CLIENT_SECRET=e78af64f-d8dc-4edb-99ae-925efe585184
oc set env dc/apicurio-studio-ui APICURIO_UI_FEATURE_MICROCKS=true
```

The ROUTE params above are still necessary for the variables, but in Starter, you can't specify a hostname in a route, so you'll have to manually create the routes

```
oc create route edge microcks --service=microcks --insecure-policy=Redirect -n ${PROJECT_NAMESPACE}
oc create route edge keycloak --service=microcks-keycloak --insecure-policy=Redirect -n ${PROJECT_NAMESPACE}
```
