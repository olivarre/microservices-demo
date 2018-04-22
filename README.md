# Roberto's microservices-demo-modified

This microservices demo has been forked and extended by Roberto Olivares to add new functionality.

The base code came from Spring's microservice demo posted to GitHub by Paul Chapman. (see microservices-demo section and below)

To run the microservices:
1. Clone the repository down to your machine. 
1. In Spring Tool Suite (STS) Eclipse > Import Project > Maven Project > From directory > point it to the .POM file in in the cloned repository to create a project
1. In Spring Tool Suite (STS) Eclipse > Debug As > Spring Boot Application > Main class = XXXServer to create an execution profile
1. You must run the AccountServer and ProductServer first, then the RegistrationServer, then the WebServer 
1. Once the services are running, check out [http://localhost:3333](http://localhost:3333) for an index page with further instructions.

# Roberto's "Products" REST Microservice Addition
Roberto added a fourth backing "Products" REST microservice (ProductsService.java) on port 4444 in its entirety (controller, data model) and its JPA/Hibernate data store.

1. Products by number (REST API) : [http://localhost:4444/products/123456020](http://localhost:4444/products/123456020)
1. Products by name (REST API) : [http://localhost:4444/products/byname/microserv](http://localhost:4444/products/byname/microserv)
1. Products search (REST API) :	[http://localhost:4444/products/search](http://localhost:4444/products/search)

# Roberto's Front-End Additions
Roberto added various Product-based functionalities to the front end microservice on port 3333.

1. Products by number (REST passthrough) : [http://localhost:3333/products/123456020](http://localhost:3333/products/123456020)
1. Products by name (REST passthrough) : [http://localhost:3333/products/byname/microserv](http://localhost:3333/products/byname/microserv)
1. Products search (form to REST passthrough) : [http://localhost:3333/products/search](http://localhost:3333/products/search)

The web microservice's pages delegate REST queries to the Products microservice and wrap the results in a Spring ThymeLeaf based result HTML.

# microservices-demo
 
Demo application to go with Paul Chapman's [Microservices Blog](https://spring.io/blog/2015/07/14/microservices-with-spring) on the spring.io website.

![Demo System Schematic](https://github.com/paulc4/microservices-demo/blob/master/mini-system.jpg)

Clone it and either load into your favorite IDE or use maven/gradle directly.

_Note for gradle users:_ to make the intructions below build-tool independent, the gradle build copies its artifacts from `build/libs` to `target`.

## Versions

Current version v1.2.0 corresponds to Spring Boot 1.5 and Spring Cloud release-train Edgeware.  I will upgrade to Spring Boot 2 and Finchly release train once Finchly is GA.

To access V1.1.0 of the repo, corresponding to Spring Cloud release-train Brixton, click on the `release` tab in https://github.com/paulc4/microservices-demo.  Or use `git checkout v1.1.0` after cloning locally.

To access V1.0.0 of the repo, corresponding to Spring Cloud release-train Angel.SR6, click on the `release` tab in https://github.com/paulc4/microservices-demo.  Or use `git checkout v1.0.0` after cloning locally.

## Using an IDE

You can run the system in your IDE by running the three server classes in order: _RegistrationService_, _AccountsService_ and _WebService_.  Each is a Spring Boot application using embedded Tomcat.  In Spring Tool Suite use `Run As ... Spring Boot App` otherwise just run each as a Java application - each has a static `main()` entry point.

As discussed in the Blog, open the Eureka dashboard [http://localhost:1111](http://localhost:1111) in your browser to see that the `ACCOUNTS-SERVICE` and `WEB-SERVICE` applications have registered.  Next open the Demo Home Page [http://localhost:3333](http://localhost:3333) in and click one of the demo links.

The `localhost:3333` web-site is being handled by a Spring MVC Controller in the _WebService_ application, but you should also see logging output from _AccountsService_ showing requests for Account data.

## Command Line

You may find it easier to view the different applications by running them from a command line since you can place the three windows side-by-side and watch their log output

For convenience we are building a 'fat' executble jar whose start-class (main method entry-point) is defined to be in the class `io.pivotal.microservices.services.Main`.  This application expects a single command-line argument that tells it to run as any of our three servers.

```
java -jar target/microservices-demo-1.2.0.RELEASE.jar registration|accounts|web
```

### Procedure

To run the microservices system from the command-line, open three CMD windows (Windows) or three Terminal windows (MacOS, Linux) and arrange so you can view them conveniently.

 1. In each window, change to the directory where you cloned the demo.
 1. In the first window, build the application using either `mvn clean package` or `gradle clean assemble`.  Either way the
    generated file will be `target/microservices-demo-1.2.0.RELEASE.jar` (even if you used gradle).
 1. In the same window run: `java -jar target/microservices-demo-1.2.0.RELEASE.jar registration`
 1. Switch to the second window and run: `java -jar target/microservices-demo-1.2.0.RELEASE.jar accounts`
 1. In the third window run: `java -jar target/microservices-demo-1.2.0.RELEASE.jar web`
 1. In your favorite browser open the same two links: [http://localhost:1111](http://localhost:1111) and [http://localhost:3333](http://localhost:3333)

You should see servers being registered in the log output of the first (registration) window.
As you interact wiht the Web application, you should logging in the both the second and third windows.

For a list of valid accounts refer to the [data.sql](https://github.com/paulc4/microservices-demo/blob/master/src/main/resources/testdb/data.sql) that is used by the Account Service to set them up.

 1. In a new window, run up a second account-server using HTTP port 2223:
     * `java -jar target/microservices-demo-1.2.0.RELEASE.jar accounts 2223`
 1. Allow it to register itself
 1. Kill the first account-server and see the web-server switch to using the new account-server - no loss of service.

