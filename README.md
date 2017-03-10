
### Install Cassandra

```
# kong needs Postgres or Cassandra as datastore. Install if not present

$ docker run -d --name kong-database \
              -p 9042:9042 \
              cassandra:2.2
```

### Install and start Kong

```
# Link cassandra and start kong
$ docker run -d --name kong \
              --link kong-database:kong-database \
              -e "KONG_DATABASE=cassandra" \
              -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
              -e "KONG_PG_HOST=kong-database" \
              -p 8000:8000 \
              -p 8443:8443 \
              -p 8001:8001 \
              -p 7946:7946 \
              -p 7946:7946/udp \
              kong

# osx install

download the .pkg from https://getkong.org/install/osx/

# before starting open the config at /etc/kong/kong.conf and check the datastore config
# if file not present make a copy of default file and rename to kong.conf

# start kong
$ kong start -c /etc/kong/kong.conf --vv

# check status
$ curl 127.0.0.1:8001
```

#### Add your API

```
# start apache-cxf-jax-ws-demo (SOAP) and producer (REST) projects
# i have httpie installed, not a must curl will do
# i have the admin api running at 8003 instead of 8001
# 2 methods to add api

# Method1: Request Host
$ http POST localhost:8003/apis name=resttest1 upstream_url=http://localhost:8079/counter request_host=resttest1.com

# test
$ http http://localhost:8002 Host:resttest1.com

# Method2: Request Path
$ http POST localhost:8003/apis name=resttest2 upstream_url=http://localhost:8079 request_path=/counter

# test
$ http get http://localhost:8002/counter

# add SOAP service, for test use soap ui
$ http POST localhost:8003/apis name=soaptest1 upstream_url=http://localhost:8080 request_path=/HelloWorld

# get list of api's
$ http GET localhost:8003/apis

# List of commands are available at getkong.org

```

#### Add key Auth
```
$ http POST http://localhost:8003/apis/soaptest1/plugins name=key-auth
$ http POST http://localhost:8003/apis/resttest2/plugins name=key-auth
```

#### Add Consumers
```
$ http POST http://localhost:8003/consumers/ username=soapuser1 custom_id=1
$ curl -X POST http://localhost:8003/consumers/soapuser1/key-auth -d ''

$ http POST http://localhost:8003/consumers/ username=restuser1 custom_id=2
$ curl -X POST http://localhost:8003/consumers/restuser1/key-auth -d ''

# post with apikey header
$ http get http://localhost:8002/counter apikey:xxxx
```

### ACL

```
# Restrict user access based on groups

$ http POST http://localhost:8003/apis/soaptest1/plugins name=acl config.whitelist=soapgroup
$ http POST http://localhost:8003/consumers/soapuser1/acls  group=soapgroup

$ http POST http://localhost:8003/apis/resttest2/plugins name=acl config.whitelist=restgroup
$ http POST http://localhost:8003/consumers/restuser1/acls  group=restgroup

# through soapuser1 key u cannot access the rest service
```

### Dashboard

```
# community project distributed over NPM

# Install Kong Dashboard
$ npm install -g kong-dashboard

# To start Kong Dashboard on a custom port
$ kong-dashboard start -p [port]

# available in the below url
http://localhost:port/

```
