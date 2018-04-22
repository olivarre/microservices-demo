package io.pivotal.microservices.services.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * Accounts web-server. Works as a microservice client, fetching data from the
 * Account-Service. Uses the Discovery Server (Eureka) to find the microservice.
 * 
 * @author Paul Chapman
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
public class WebServer {

	/**
	 * URL uses the logical name of account-service - upper or lower case, doesn't matter.
	 * 
	 * reo - this appears to be what is registered in the Eureka web view for the service
	 */
	public static final String ACCOUNTS_SERVICE_URL = "http://ACCOUNTS-SERVICE";
	public static final String PRODUCTS_SERVICE_URL = "http://PRODUCTS-SERVICE";

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 * 
	 * @param args
	 *            Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for web-server.properties or web-server.yml
		System.setProperty("spring.config.name", "web-server");
		SpringApplication.run(WebServer.class, args);
	}

	/**
	 * A customized RestTemplate that has the ribbon load balancer build in.
	 * Note that prior to the "Brixton" 
	 * 
	 * @return
	 */
	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
	
	/**
	 * The AccountService encapsulates the interaction with the micro-service.
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebAccountsService accountsService() {
		return new WebAccountsService(ACCOUNTS_SERVICE_URL);
	}

	/**
	 * Create the controller, passing it the {@link WebAccountsService} to use.
	 * 
	 * @return
	 */
	@Bean
	public WebAccountsController accountsController() {
		return new WebAccountsController(accountsService());
	}

	/**
	 * Create and expose the ProductService.
	 * The ProductService encapsulates the interaction with the micro-service.
	 * 
	 * @author Roberto Olivares (reo)
	 * 
	 * @return A new service instance.
	 */
	@Bean
	public WebProductsService productsService() {
		return new WebProductsService(PRODUCTS_SERVICE_URL);
	}

	/**
	 * Create and expose the controller, passing it the {@link WebProductsService} to use.
	 * The product controller handles requests and invokes methods on the local WebProductsService 
	 * (which then forwards requests to our separate Products microservice)
	 * 
	 * @author Roberto Olivares (reo)
	 * 
	 * @return A new WebProductsController
	 */
	@Bean
	public WebProductsController productsController() {
		return new WebProductsController(productsService());
	}

}

