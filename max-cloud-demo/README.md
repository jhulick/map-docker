
# MAX PaaS Cloud MicroServices Template
This repository is an example of how to get MAX PaaS micro-services going using Spring Boot, Spring Cloud, AngularJs and MAX CAS frameworks.

# Table of Content
* [Application Architecture](#application-architecture)
* [Using the application](#using-application)
    * [Prerequisites](#prerequisites)
    * [Running on local m/c](#run_local_mc)
* [Microservices Overview](#microservices-overview)
* [Netflix OSS](#netflix-oss)
* [Spring Boot Overview](#spring-boot-overview)
* [Spring Cloud Overview](#spring-cloud-overview)
* [Spring Cloud Netflix Overview](#spring-cloud-netflix-overview)

## <a name="double-simple"></a>Application Architecture

The application consists of 4 different services 

* [api-gateway](gateway/README.md) - API gateway that proxies all the micro-services
* [resource-server](resource/README.md) - Example resource micro-service 
* [ui-server](user-webservice/README.md) - Single Page Application micro-service that provides the UI
* [mock-cas](gateway/README.md) - Mock CAS SSO server


### Template Architecture

This template has a UI (HTML and JavaScript) and a Resource server. It also has a Gateway, as a separate micro-service providing a security and routing layer. The UI is part of the backend, giving us even more choice to re-configure and re-implement features, and also bringing other benefits as we will see. The browser client goes to the Gateway for everything and it doesn't have to know about the architecture of the backend (fundamentally, it has no idea that there is a back end). One of the things the browser does in this Gateway is authentication, e.g. it authenticates with MAX CAS, and it gets a cookie in return. On subsequent requests it presents the cookie automatically and the Gateway passes it through to the back-ends. No code needs to be written on the client to enable the cookie passing. The back-ends use the cookie to authenticate and because all components share a session they share the same information about the user.


### Application Components

* The UI there is no need for the "/resource" endpoint. When you have done that you will have a very simple Angular application (the same as in the "basic" sample), which simplifies testing and reasoning about its behaviour greatly.
* The Resource Server is the API to our back-end services. Our existing samples serves an API for a TODO list. 
* The API Gateway server implementation, using the simplest implementation that could possibly work, is simply a Spring Boot web application and add the `@EnableZuulProxy` annotation. To apply MAX Security, a CAS server has been added for protection of back-end services. In order to let the Gateway know about our backend services, it is necessary to setup routes in its `application.yml`:

```
.application.yml

zuul:
    routes:
        ui:
            url: http://localhost:8081
        resource:
            url: http://localhost:9000
security:
    sessions: ALWAYS
```

* The Mock CAS server implements the CAS protocol and is used by the API Gateway. Login may be with any use. To access 'ADMIN' role, login with the user 'admin'. See the README.md in gateway/src/test/cas for usage instructions.


## <a name="using-application"></a>Using the application

### <a name="prerequisites"></a>Software Installation

* Install Java 8
* Install Redis
* Install Maven

### <a name="run_local_mc"></a>Running on local m/c

* You can run individual projects on Mac/Linux systems by opening a terminal in each individual sub-project folder and running the `gradle bootRun` command.
* Please refer to the individual readme files on instructions of how to run the services. For demo, you can run the applications in the same order listed above.

Each app comes with three Spring "profiles":

* "dev" for development: it focuses on ease of development and productivity
* "prod" for production: it focuses on performance and scalability
* "test" for test: it focuses on test environment capabilities

Those profiles come in two different configurations:

* The Gradle profiles are used at build time. For example `gradle -Pprod assemble` will package a production application.
* The Spring profiles work at run time. Some Spring beans will behave differently, depending on the profile.
  Spring profiles are set by Gradle, so we have a consistency between the two methods: you will have a "prod" profile on Gradle and Spring at the same time.

### In default mode, each Microservice will use the "dev" profile
* If you run the application without Gradle, launch the "Application" class (you can probably run it easily from your IDE by right-clicking on it).
* If you run the application with Gradle, run gradle

### In production, each Microservice has to run with the "prod" profile
* Use Gradle to build the application with the "prod" profile: gradle -Pprod
* When you run the application add the "prod" profile, by adding --spring.profiles.active=prod, to your program arguments.
* You can test it with Gradle, by running gradle -Pprod



## <a name="microservices-overview"></a>Microservices Overview

There is a growing adoption of Microservices in today's world. Numerous SAAS Companies are moving away from building monolithical products and instead adopting Microservices. 

### Focus on Component

In microservices world, a web serive or a microservice is the unit of component. This unit of component delivers a complete Business functionality and could rely on other microservices to fullfil that. These microservices are build separately and deployed separately unlike monoliths in which components can be built separately but are deployed together.

[//]: # (![Microservices Overview](http://martinfowler.com/articles/microservices/images/sketch.png))

### Focus on Business Capabilities and Running a Product

Another key aspect of microservices is that the focus of a team building a component now moves away from just delivering that component to running and maintainig that business functionality given by that component. 

[//]: # (![Focus on Business Capabilities](http://martinfowler.com/articles/microservices/images/conways-law.png))

### Focus on Decentralized Control and Decentalized Data Management

Due to the ability to build components separately and running them separately means, the notion of centralized control (goverance) and data management goes away. Traditionally monoliths were built around a set of set architecture, technology and frameworks. The key architects would decide what tech was used and key DBAs would decide which and how many databases are used.
With Microservices, since each component caters to a somewhat complete business functionality, that centralized control by Key Architects and DBAs goes away. Some Components are best built using JEE and RDBMS, for some Real time Data Analytics is the key, they could use Apache Storm and Apache Kafka, for some others R is better fit, for IO Intensive systems may Node.js and MongoDB works out. Same way User data could now go in NoSQL databases, Transaction data could go in traditional RDBMS, Recommendation systems could use Hive as their Database and so on.

**Decentralized Control**

[//]: # ( ![Decentralied Control](/images/Decentralized Goverance.png) )


**Decentalized Data Management**

[//]: # (![Decentralied Control](http://martinfowler.com/articles/microservices/images/decentralised-data.png))

Disclaimer - While microservices is much talked about these days, make a note Microservices is not a Free lunch. There is an effort and complexity involved to building and running them, but once you do so, the benefits are plentiful.

You can read more about Microservices here - http://martinfowler.com/articles/microservices.html#CharacteristicsOfAMicroserviceArchitecture

Image References from - http://martinfowler.com/articles/microservices.html

## <a name="netflix-oss"></a>Netflix OSS

![Netflix OSS Home Page](http://netflix.github.io/glisten/lib/img/netflix_oss.jpg)

Netflix is one of the pioneers behind the Microservices Architecture. Not only have they successfully run Microservices in production, but they have outsourced their battle hardened framework under Netflix Open Source Software Center initiative - http://netflix.github.io/#repo

You will find implementation of numerous of Netflix's Microservices platform pieces here. Here are few for your reference
### <img src="http://netflix.github.io/assets/repos/eureka.png" width="30px"> Eureka
Microservices is somewhat like SOA platform, that there are numerous services. Each Service when it comes online registers itself with Service Registry. When some other service wants to communicate with a already registered service, they would ask the Eureka Server the base url for that service. Multiple instances of the same service could register with Eureka, in that case Eureka could help in doing Load Balancing.

### <img src="http://netflix.github.io/assets/repos/hystrix.png" width="30px"> Hystrix
A Microservices environment is built to sustain failures of its parts, that is few of its microservices. In order to keep the system going, Netflix introduced a concept of circuit breaker. A Circuit Breaker provides alternative behavior in case certain microservice is gone down. This way the system gracefully switches to fallback behavior until the system recovers, rather than entire system suffering the ripple effects of failed service.

### <img src="http://netflix.github.io/assets/repos/zuul.png" width="30px"> Zuul
A Microservice environment needs a gateway. A Gateway is the only entity exposed to the outside world, which allows access to Microservices and does more. A Gateway could do
* API Metering
* Centralized Authentication/Authorization
* Load Balancing
* etc


### <img src="http://netflix.github.io/assets/repos/ribbon.png" width="30px"> Ribbon
Ribbon is a Load Balancing Client and is meant to work with Eureka Server. Ribbon talks to Eureka server and relies on it to get base url to one of the instances of microservice in question. 

## <a name="spring-boot-overview"></a>Spring Boot Overview

Spring Boot is Spring's approach towards Convention over Configuration. Spring Boot comes with numerous Start Projects, each starter projects provides a set of conventions which ensures you have a opinionated production ready app.

Spring Boot allows you to write web services with just One or two classes. See the example below.

```
Application.java

@SpringBootApplication
public class Application {
   public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
   }
}
```

```
UserController.java

@RestController
public class UserController {
    @RequestMapping("/")
    public User getUser(String id) {
        return new User(id,"firstName","lastName");
    }

}
```

The idea is to have multiple projects like those in this repo, one for each micro-service. Look at the following directories in this repo:

* resource
* ui


You can read in detail about Spring Boot here - https://spring.io/guides/gs/spring-boot/

## <a name="spring-cloud-overview"></a>Spring Cloud Overview

Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state)

You can read in detail about Spring Cloud here - http://projects.spring.io/spring-cloud/

## <a name="spring-cloud-netflix-overview"></a>Spring Cloud Netflix Overview

Spring Cloud Netflix provides Netflix OSS integrations for Spring Boot apps through auto-configuration and binding to the Spring Environment and other Spring programming model idioms.

You can read in detail about Spring Cloud Netflix here - http://cloud.spring.io/spring-cloud-netflix/