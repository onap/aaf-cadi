# Reverse Proxy Introduction

The **AAF Reverse Proxy** (or **rProxy**) is a proxy microservice which intercepts incoming REST requests by, extracting the credentials from the request and authenticates/authorises
with a configured security provider. It is one of two components (along with the [Forward proxy][1]) deployed as a Kubernetes sidecar to
separate the responsibility of authentication and authorization away from the primary microservice, this service is responsible for
controlling access to the REST URL endpoints exposed by the primary microservice, and propagating security credentials to downstream microservices. 

## Features

**Reverse Proxy**:

* The service will intercept all incoming REST requests to the primary service, extract and cache the token credentials in the [Forward proxy][1].
* Invokes the authentication and authorisation providers to validate the extracted tokens, and retrieves its list of authorisations.
* Invokes the enforcement point filter to determine whether the incoming request URI and retrieved permissions match the list of granted URIs and permissions
  configured in the URI authorisation file. If authorisation is successful, forward the request to the primary service.

## Configuring the rProxy service
The **rProxy service** is configured through properties and JSON files that reside under the `${CONFIG_HOME}` environment variable.

The files have the following configurable properties:

### cadi.properties

- `cadi_loglevel` log level of the cadi filter, e.g. DEBUG, INFO
- `cadi_keyfile`  location to the cadi key file
- `cadi_truststore` 
- `cadi_truststore_password`
- `aaf_url` hostname and port of the server hosting the AAF service, e.g. https://aaf.osaaf.org:30247
- `aaf_env` AAF environment type, e.g. DEV, PROD
- `aaf_id` aafadmin user, e.g. demo@people.osaaf.org
- `aaf_password` aafadmin user password encrypted with the cadi_keyfile, e.g. enc:92w4px0y_rrm265LXLpw58QnNPgDXykyA1YTrflbAKz
- `cadi_x509_issuers` colon separated list of client cert issuers

### reverse-proxy.properties

- `transactionid.header.name`	This is the name of the header in incoming requests that will contain the transaction ID e.g. `X-TransactionId`

### primary-service.properties

- `primary-service.protocol` HTTP protocol of the primary service e.g. https
- `primary-service.host` location of the primary service, since this sidecar resides in the same pod of the primary service. e.g. localhost
- `primary-service.port` port of the primary service

### forward-proxy.properties

- `forward-proxy.protocol` HTTP protocol of the [fproxy service][1] e.g. https
- `forward-proxy.host` location of the [fproxy service][1], since this sidecar resides in the same pod of the primary service. e.g. localhost
- `forward-proxy.port` port of the [fproxy service][1]
- `forward-proxy.cacheurl` URI to the store credential cache. e.g. /credential-cache

### auth/uri-authorization.json
This file is used by the **ReverseProxyAuthorization filter**, the configurable authorization enforcement point, and contains the list
of required AAF permissions needed for the request URI. The content of the file is in JSON format. Permissions will be tested against
the first matching URI. If the user does not have those permissions then the next matching URI will be tested until the list of URIs
is exhausted. URIs will be matched in order as positioned in the configuration file. All permissions listed in the configuration file
for a request URI must have been granted to the user. 

The current implementation of sidecar security retrieves user permissions from AAF. AAF permissions are composed of a type, instance and
action and are returned from AAF as those values separated by the pipe (|) character e.g. org.onap.osaaf.resources.access|rest|read.
Both instance and/or action can be wildcarded with an asterisk (\*) e.g. org.onap.osaaf.resources.access|\*|read,
org.onap.osaaf.resources.access|rest|\* or org.onap.osaaf.resources.access|\*|\*.  If action or instance is wildcarded then a match
between granted and needed permissions is found as long as the non wildcarded parts of the permission is also matched.

Both URIs and permissions are matched using regular expressions which are defined in the `uri-authorization.json` file. Regular
expression tests are applied to the whole permission unless AAF wildcarding has been used in which case the permissions are split
into type, instance and action and the non wildcarded parts are tested individually. Note that owing to regular expression and JSON
format that backslashes need to be escaped twice.
```
[
    {
      "uri": "URI 1",
      "permissions": [
        "permission 1",
        "permission 2",
        "..."]
    },
    {
      "uri": "URI 2",
      "permissions": [
        "permission 3",
        "permission 4",
        "..."]     
    }
]
```
e.g.
```
[
    {
      "uri": "\\/aai\\/v13\\/cloud-infrastructure\\/cloud-regions$",
      "permissions": [
        "org\\.onap\\.osaaf\\.resources\\.access\\|rest\\|read"
       ]
    },
    {
      "uri": "\\/aai\\/v13\\/cloud-infrastructure\\/cloud-regions\\/cloud-region\\/[^\\/]+[\\/][^\\/]+$*",
      "permissions": [
        "org\\.onap\\.osaaf\\.resources\\.access|clouds|read",
        "org\\.onap\\.osaaf\\.auth\\.resources\\.access|tenants|read"
      ]     
    },
    {
      "uri": "\\/aai\\/v13\\/cloud-infrastructure\\/cloud-regions\\/cloud-region\\/[^\\/]+[\\/][^\\/]+\\/tenants\\/tenant/[^\\/]+\\/vservers\\/vserver\\/[^\\/]+$",
      "permissions": [
        "org\\.onap\\.osaaf\\.auth\\.resources\\.access\\|clouds\\|read",
        "org\\.onap\\.osaaf\\.auth\\.resources\\.access\\|tenants\\|read",
        "org\\.onap\\.osaaf\\.auth\\.resources\\.access\\|vservers\\|read"
      ]     
    }
]
```

## Using an Alternative Authorization Service Provider
The current implementation of sidecar security relies on AAF & use of the CADI filter. In order to use an alternative authorization
service provider it will be necessary to modify the Reverse Proxy sidecar filter chain. The first change necessary is replacement of
the CADI filter. The replacing filter will be responsible for extracting the credentials from the incoming request, contacting the
alternative authorization service to return the authorizations/permissions and passing the authorizations through to the
**ReverseProxyAuthorization filter**. The **ReverseProxyAuthorization filter** is next in the filter chain.  Currently authorizations are passed
with the `HttpServletRequestWrapper` derived CADIWrap object. If it is desirable to not have a dependency on the CADI libraries then a
new object derived from `HTTPServletRequestWrapper` can be used or alternatively authorizations could be passed as an attribute set on
the `HTTPServletRequest`. If either of these two options is chosen then the **ReverseProxyAuthorization filter** have to be altered to use
the new object or to retrieve authorizations from the request attribute. Finally the `auth/uri-authorization.json` file will need revision to
match the new format and list of permissions for the URI requests.

[1]: ../fproxy/README.md
