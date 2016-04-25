MAX Application Services Microservice Example
=============================================

This project creates a VM with the complete max application services micro-service infrastructure services, and a demo system. 
The services are deployed into Docker containers inside a Vagrant VM. These services are implemented in Java, using Spring and Spring Cloud, and secured behind the MAX Gatway using a mock CAS server.

The demo system consists of two micro-services:
- UI interface showing a list of TODO items.
- Resource service that handles TODO data.

Technologies
------------

- Eureka is used by the MAX Registry for service lookup and load balancing.
- Zuul is used by the MAX Gateway to route HTTP requests from the outside world to the different services.
- Spring Boot is the container that runs the micro-services.


How To Run
----------

The demo is setup with [Vagrant and Docker](docker-vagrant/README.md). 

Remarks on the Code
-------------------

The servers for the infrastruture are pretty simple thanks to Spring Cloud:

- max-registry is the Eureka server for service discovery.
- mmax-gateway is the Zuul server. It distributes the requests to the two microservices, and the CAS security service.

The micro-services are: 
- ui is the application that displays a list of TODO items.
- resource is application responsible for serving data to the ui.