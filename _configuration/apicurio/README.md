Create the template within your namespace

```
oc create -f https://raw.githubusercontent.com/Apicurio/apicurio-studio/master/distro/openshift/apicurio-template.yml
```

Deploy the application from the template, be sure to replace <NAMESPACE> with your proper namespace

```
PROJECT_NAMESPACE=microservices-dev
OCP_APP_DOMAIN=apps.sememeve.com
OCP_MASTER_URL=https://console.ocp.sememeve.com

oc new-app --template=apicurio-studio \
 --param=UI_ROUTE=apicurio-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=API_ROUTE=apicurio-api-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=WS_ROUTE=apicurio-ws-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=AUTH_ROUTE=apicurio-auth-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
 --param=DB_USER=admin-db \
 --param=DB_PASS=12345 \
 --param=KC_USER=admin \
 --param=KC_PASS=admin \
 -n ${PROJECT_NAMESPACE}
```

The ROUTE params above are still necessary for the variables, but in Starter, you can't specify a hostname in a route, so you'll have to manually create the routes

```
oc create route edge microcks --service=microcks --insecure-policy=Redirect -n ${PROJECT_NAMESPACE}
oc create route edge keycloak --service=microcks-keycloak --insecure-policy=Redirect -n ${PROJECT_NAMESPACE}
```
