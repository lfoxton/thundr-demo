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

## To Do

* Deploy in Google App Engine
* Demonstrate an alternate to LoggerFilter posting to BigQuery instead of std out 