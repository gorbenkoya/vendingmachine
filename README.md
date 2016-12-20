Akka HTTP REST service example
=========================

Goal of this example is to show how to create reactive REST services on Typesafe stack using Scala and Akka HTTP framework.

Example contains complete REST service and actor system for simple vending machine emulation.
More info about vending machine principles and how it works can be found here: 
([https://en.wikipedia.org/wiki/Vending_machine](https://en.wikipedia.org/wiki/Vending_machine))

### Features:
* Reactive Akka HTTP framework
* JSON serialization-deserialization using spray-json
* Akka HTTP routes with rejection and exception handlers   
* Akka actor system with supervisor actor implemented
* Easy to run/stop service using Sbt Revolver plugin
* Test coverage with *ScalaTest* and *Akka Testkit*

## Requirements
* JDK 8 (e.g. [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html));
* sbt ([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html));

## Links 

For more information please use following url which explains how Akka HTTP works
step-by-step: [http://doc.akka.io/docs/akka-http/current/scala/http/index.html](http://doc.akka.io/docs/akka-http/current/scala/http/index.html)

### Changing application config
There is only one config file. Application config `src/main/resources/application.conf`. It only contains http.port option (default port is 9002), 
which determines specific http port used by application  

## Run application
To run application, call:
```
sbt
reStart
```
If you want stop application, please use:
```
reStop
```

## Run tests
To run tests, just type in project root folder:
```
sbt test
```

## REST API Endpoints

All example calls below use default port 9002.

####Products list and prices 
can be obtained by using the following GET request

```
curl -H "Content-Type: application/json" -X GET  http://localhost:9002/vm/products
```
Response example:
 
{
  "success": true,
  "products": [{
    "name": "bounty",
    "price": 2
  }, {
    "name": "mars",
    "price": 1
  }, {
    "name": "snickers",
    "price": 3
  }]
}

####Following POST request should be used in order to insert coins into VM 

```
curl -H "Content-Type: application/json" -X POST -d '{"amount": 2}' http://localhost:9002/vm/insertcoin
```
Response example:

{
  "success": true,
  "inserted": 2,
  "total": 2
}

#### If you want purchase product
please use the following POST request

```
curl -H "Content-Type: application/json" -X POST -d '{"product": "mars"}' http://localhost:9002/vm/purchase
```

Response example: 

{
  "success": true,
  "product": "mars"
}

Possible error responses like "missing field" or "not enough coins inserted" are also reported in JSON format 