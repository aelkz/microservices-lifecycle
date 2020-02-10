Create the template within your namespace

```
oc create -f https://raw.githubusercontent.com/microcks/microcks/master/install/openshift/openshift-persistent-full-template-https.yml
```

Deploy the application from the template, be sure to replace <NAMESPACE> with your proper namespace

```
PROJECT_NAMESPACE=microservices-dev
OCP_APP_DOMAIN=apps.sememeve.com
OCP_MASTER_URL=https://console.ocp.sememeve.com

oc new-app --template=microcks-persistent-https \
    --param=APP_ROUTE_HOSTNAME=microcks-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
    --param=KEYCLOAK_ROUTE_HOSTNAME=keycloak-${PROJECT_NAMESPACE}.${OCP_APP_DOMAIN} \
    --param=OPENSHIFT_MASTER=${OCP_MASTER_URL} \
    --param=OPENSHIFT_OAUTH_CLIENT_NAME=microcks-client \
    --param=MONGODB_VOL_SIZE=1Gi \
    --param=MEMORY_LIMIT=512Mi \
    --param=MONGODB_MEMORY_LIMIT=512Mi \
    -n ${PROJECT_NAMESPACE}
```

The ROUTE params above are still necessary for the variables, but in Starter, you can't specify a hostname in a route, so you'll have to manually create the routes

```
oc create route edge microcks --service=microcks --insecure-policy=Redirect -n ${PROJECT_NAMESPACE}
oc create route edge keycloak --service=microcks-keycloak --insecure-policy=Redirect -n ${PROJECT_NAMESPACE}
```
