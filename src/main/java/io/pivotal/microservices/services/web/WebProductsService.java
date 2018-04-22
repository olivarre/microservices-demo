package io.pivotal.microservices.services.web;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.pivotal.microservices.exceptions.ProductNotFoundException;

/**
 * Hide the access to the microservice inside this local service.
 * This local service attempts to expose the same API as the actual Products microservice.
 * It delegates to the actual microservice via the Spring REST template object for creating REST requests.
 * 
 * Questions:
 * 		Could the microservice interface between the two be standardized into an interface?
 * 			Probably not, because then a change in one would need to be trickled over to this or things would break?
 * 			The point of microservices is to loosely couple the individual pieces so they can be evolved independently
 * 
 * 	@author Roberto Olivares (reo)
 */
@Service
public class WebProductsService {

	@Autowired
	@LoadBalanced
	protected RestTemplate restTemplate;

	protected String serviceUrl;

	protected Logger logger = Logger.getLogger(WebProductsService.class.getName());

	public WebProductsService(String serviceUrl) {
		this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
				: "http://" + serviceUrl;
	}

	/**
	 * The RestTemplate works because it uses a custom request-factory that uses
	 * Ribbon to look-up the service to use. This method simply exists to show
	 * this.
	 */
	@PostConstruct
	public void demoOnly() {
		// Can't do this in the constructor because the RestTemplate injection
		// happens afterwards.
		logger.warning("The RestTemplate request factory is "
				+ restTemplate.getRequestFactory().getClass());
	}

	public Product findByNumber(String productNumber) {

		logger.info("findByNumber() invoked: for " + productNumber);
		return restTemplate.getForObject(serviceUrl + "/products/{number}",
				Product.class, productNumber);
	}

	public Product getByNumber(String productNumber) {
		Product product = restTemplate.getForObject(
				serviceUrl + "/products/{number}", Product.class, productNumber);

		if (product == null)
			throw new ProductNotFoundException(productNumber);
		else
			return product;
	}
	
	/**
	 * @param name
	 * @return
	 * @author Roberto Olivares (reo)
	 */
	public List<Product> findByName(String name) {
		logger.info("findByName() invoked:  for " + name);
		Product[] products = null;

		try {
			// Forward to actual microservice
			products = restTemplate.getForObject(
					serviceUrl + "/products/byname/{name}", Product[].class, name);
		} catch (HttpClientErrorException e) { // 404
			// Nothing found
		}

		if (products == null || products.length == 0)
			return null;
		else
			return Arrays.asList(products);
	}
	
	/**
	 * @param searchText
	 * @return
	 * @author Roberto Olivares (reo)
	 */
	public List<Product> findBySearchText(String searchText) {
		logger.info("findBySearchText() invoked:  for " + searchText);
		Product[] products = null;

		try {
			products = restTemplate.getForObject(
					serviceUrl + "/products/search/text/{searchText}", Product[].class, searchText);
		} catch (HttpClientErrorException e) { // 404
			// Nothing found
		}

		if (products == null || products.length == 0)
			return null;
		else
			return Arrays.asList(products);
	}

}
