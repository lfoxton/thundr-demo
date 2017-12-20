# Thundr Demo

A super-simple rest client example using Thundr, such as it is.

Intent is to demonstrate the web container, and how requests can be logged to std out or offloaded to a SAAS for recording.

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

# Deployment

* Edit the version and the application id `src/main/resources/appengine-web.xml`
  * Follow the steps from https://cloud.google.com/appengine/docs/standard/java/tools/uploadinganapp
* Run the maven task

`mvn appengine:update`

Observe the result at `https://{version}-dot-${application}.appspot.com/`

Example: `curl https://2-dot-firstproject-188709.appspot.com/thundr-demo/`

## To Do

* Demonstrate an alternate to LoggerFilter posting to BigQuery instead of std out 