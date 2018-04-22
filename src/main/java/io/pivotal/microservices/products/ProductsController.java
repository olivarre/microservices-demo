package io.pivotal.microservices.products;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.microservices.exceptions.ProductNotFoundException;

/**
 * 	A RESTFul controller for accessing Product information.
 * 	Based on the AccountsController class by Chapman 
 * 
 * REST URLs:	
 * 		http://localhost:3333/products/123456789
 * 		http://localhost:3333/products/owner/lee
 * 
 * @author Roberto Olivares
 */
@RestController
public class ProductsController {

	protected Logger logger = Logger.getLogger(ProductsController.class.getName());
	protected ProductRepository productRepository;

	/**
	 * Create an instance plugging in the repository of Products.
	 * 
	 * @param productRepository		A product repository implementation.
	 */
	@Autowired
	public ProductsController(ProductRepository productRepository) {
		this.productRepository = productRepository;

		logger.info("ProductRepository says system has " + productRepository.countProducts() + " products");
	}

	/**
	 * Fetch an Product with the specified Product number.
	 * 
	 * @param productNumber					A numeric, 9 digit product number.
	 * @return The product if found.
	 * @throws ProductNotFoundException		If the number is not recognised.
	 */
	@RequestMapping("/products/{productNumber}")
	public Product byNumber(@PathVariable("productNumber") String productNumber) {

		logger.info("products-service byNumber() invoked: " + productNumber);
		Product product = productRepository.findByNumber(productNumber);
		logger.info("products-service byNumber() found: " + product);

		if (product == null)
			throw new ProductNotFoundException(productNumber);
		else {
			return product;
		}
	}

	/**
	 * Fetch products with the specified name. A partial case-insensitive match
	 * is supported. So <code>http://.../products/manufacturer/a</code> will find any
	 * products with upper or lower case 'a' in their name.
	 * 
	 * @param partialName
	 * @return A non-null, non-empty set of products.
	 * @throws ProductNotFoundException		If there are no matches at all.
	 */
	@RequestMapping("/products/byname/{name}")
	public List<Product> byName(@PathVariable("name") String partialName) {
		logger.info("products-service byName() invoked: "
				+ productRepository.getClass().getName() + " for "
				+ partialName);

		List<Product> products = productRepository.findByNameContainingIgnoreCase(partialName);
		logger.info("products-service byName() found: " + products);

		if (products == null || products.size() == 0)
			throw new ProductNotFoundException(partialName);
		else {
			return products;
		}
	}

// 	THE FOLLOWING IS BROKEN:
	
	/**
	 * Fetch products with the specified name. A partial case-insensitive match
	 * is supported. So <code>http://.../products/search/text/a</code> will find any
	 * products with upper or lower case 'a' in their name.
	 * 
	 * @param searchText
	 * @return A non-null, non-empty set of products.
	 * @throws ProductNotFoundException		If there are no matches at all.
	 */
	@RequestMapping("/products/search/text/{searchText}")
	public List<Product> bySearchText(@PathVariable("searchText") String searchText) {
		logger.info("products-service bySearchText() invoked: "
				+ productRepository.getClass().getName() + " for "
				+ searchText);

		List<Product> products = null; 
		
		// List<Product> products = productRepository.findBySearchTextIgnoreCase(searchText);
		// TODO 	REO - IMPLEMENT KEYWORD SEARCH ACROSS ENTIRE PRODCUT DESCRIPTION
		
		logger.info("products-service bySearchText() found: " + products);

		if (products == null || products.size() == 0)
			throw new ProductNotFoundException(searchText);
		else {
			return products;
		}
	}	
}
