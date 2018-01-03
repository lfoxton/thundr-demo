# Thundr Demo

A super-simple rest client example using Thundr, such as it is.

Intent is to demonstrate the web container, and how requests can be logged to std out or offloaded to a SAAS for recording.

Request content and meta-data is logged to StackDriver, then flushed to BigQuery using a sink

* Web framework: Thundr
* Deployment: google-app-engine
* Data store: BigQuery

## Prerequisites

* gcloud env installed
* maven
* app-engine-sdk-java 1.9.60 
* Service account api credentials (see https://cloud.google.com/bigquery/docs/authentication/service-account-file)

## Libs / Plugins

* Java8
* Thundr
* Maven App Engine Plugin

## To Run

* `mvn package`
* `mvn appengine:devserver`

## To Test

`curl http://localhost:8080/thundr-demo/`
`curl -v -XPOST -d '{ "firstName": "Joey Joe-Joe", "lastName": "Junior" }' -H 'content-type: application/json'  http://localhost:8080/thundr-demo/greeting`
`curl http://localhost:8080/thundr-demo/audit/tailRequests | json_pp`
`curl http://localhost:8080/thundr-demo/audit/tailResponses | json_pp`

## Deployment

* Edit the version and the application id `src/main/resources/appengine-web.xml`
  * Follow the steps from https://cloud.google.com/appengine/docs/standard/java/tools/uploadinganapp
* Run the maven task

`mvn appengine:update`

Observe the result at `https://{version}-dot-${application}.appspot.com/`

Example: `curl https://3-dot-firstproject-188709.appspot.com/thundr-demo/`
Example: `curl -v -XPOST -d '{ "firstName": "Joey Joe-Joe", "lastName": "Junior" }' -H 'content-type: application/json'  https://3-dot-firstproject-188709.appspot.com/thundr-demo/greeting`
Example: `curl https://3-dot-firstproject-188709.appspot.com/thundr-demo/audit/tailRequests | json_pp`
Example: `curl https://3-dot-firstproject-188709.appspot.com/thundr-demo/audit/tailResponses | json_pp` 


## Creating a sink for the 'Access' log created

* Follow the instructions at https://cloud.google.com/logging/docs/export/configure_export_v2
* Create a sink from the Access logs to a BigQuery table
* Bobs your uncle you got requests streaming to BigQuery via stackdriver

## Verify data in StackDriver

Running the code and curling requests will create the log in StackDriver automatically.

After having deployed and curled some requests, find the full name of the Access log using gcloud

`gcloud logging logs list`

Then read the Access log using the listed name, for example: 

`gcloud logging read projects/firstproject-188709/logs/Access`

## TODO

* Add unit tests