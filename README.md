# Thundr Demo

A super-simple rest client example using Thundr, such as it is.

Intent is to demonstrate the web container, and how requests can be logged or offloaded to a SAAS for recording.

## Libs

* Java8
* Tomcat7
* Thundr

## To Run

`mvn package`
`mvn tomcat:run`

## To test

`curl http://localhost:8080/thundr-demo/`

`curl -v -XPOST -d '{ "firstName": "Joey Joe-Joe", "lastName": "Junior" }' -H 'content-type: application/json'  http://localhost:8080/thundr-demo/greeting/`
