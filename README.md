# Thundr Demo

A super-simple rest client example using Thundr, such as it is.

Intent is to demonstrate the web container, and how requests can be logged to std out or offloaded to a SAAS for recording.

* Web framework: Thundr
* Deployment: google-app-engine
* Data store: BigQuery

## Prerequisites

* gcloud env installed
* maven
* app-engine-sdk-java 1.9.60 
* Service account api credentials (see https://cloud.google.com/bigquery/docs/authentication/service-account-file)
* BigTable table HelloWorld.Requests
* BigTable table HelloWorld.Responses

## Libs / Plugins

* Java8
* Thundr
* Maven App Engine Plugin

## To Run

* `mvn package`
* `mvn appengine:devserver`

## To Test

`curl http://localhost:8080/thundr-demo/`
`curl -v -XPOST -d '{ "firstName": "Joey Joe-Joe", "lastName": "Junior" }' -H 'content-type: application/json'  http://localhost:8080/thundr-demo/greeting/`
`curl http://localhost:8080/thundr-demo/audit/tailRequests`
`curl http://localhost:8080/thundr-demo/audit/tailResponses`

## Deployment

* Edit the version and the application id `src/main/resources/appengine-web.xml`
  * Follow the steps from https://cloud.google.com/appengine/docs/standard/java/tools/uploadinganapp
* Run the maven task

`mvn appengine:update`

Observe the result at `https://{version}-dot-${application}.appspot.com/`

Example: `curl https://2-dot-firstproject-188709.appspot.com/thundr-demo/`
Example: `curl -v -XPOST -d '{ "firstName": "Joey Joe-Joe", "lastName": "Junior" }' -H 'content-type: application/json'  https://2-dot-firstproject-188709.appspot.com/thundr-demo/greeting/`
Example: `curl https://3-dot-firstproject-188709.appspot.com/thundr-demo/audit/tailRequests`
Example: `curl https://3-dot-firstproject-188709.appspot.com/thundr-demo/audit/tailResponses` | json_pp


## TODO

* Show alternate mechanism for getting requests to BigQuery using stackdriver sinks
* Add unit tests