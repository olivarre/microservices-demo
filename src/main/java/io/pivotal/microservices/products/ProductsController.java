package io.pivotal.microservices.products;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicedemo.commerce.abstractions.ids.OFFERID;
import com.microservicedemo.commerce.abstractions.ids.PRICE;
import com.microservicedemo.commerce.abstractions.ids.PRODUCTID;
import com.microservicedemo.commerce.exceptions.InvalidProductIdException;
import com.microservicedemo.commerce.products.offers.ProductOfferManager;
import com.microservicedemo.commerce.products.offers.ProductOffers;

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

	protected ProductOfferManager offerManager = new ProductOfferManager();

	
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
	
	/** REST API for addProductOffer functionality.
	 * 
	 * @see http://www.baeldung.com/spring-requestmapping
	 * 
	 * @param productIdString	A valid product ID string
	 * @param offerIdString		An offer ID string
	 * @param priceString		The price to try to find the next cheapest offer for.
	 * 
	 * @return JSON response with transaction info.
	 * @throws InvalidProductIdException
	 * @author Roberto Olivares (reo)
	 */
	@RequestMapping(
			  value = "/products/{productIdString}/offers/add", 
			  params = { "offerIdString", "priceString" }
			  ) 
	//@ResponseBody	
	public String addProductOffer(
			@PathVariable("productIdString") String productIdString,
			@RequestParam("offerIdString") String offerIdString,
			@RequestParam("priceString") String priceString
	) throws InvalidProductIdException {
		/*
		logger.info("products-service bySearchText() invoked: "
				+ productRepository.getClass().getName() + " for "
				+ searchText);
		 */
		
		// Attempt to locate the product
		Product product = productRepository.findByNumber(productIdString);
		if (product == null)
			return "ProductID not found: " + productIdString;
		
		// Generate the required synonyms 
		PRICE price = new PRICE(priceString);
		PRODUCTID productId = new PRODUCTID(productIdString);
		OFFERID offerId = new OFFERID(offerIdString);

		// If a product exists, but not a productoffers list for it, create it now
		ProductOffers productOffers = offerManager.getProductOffersById(productId);
		if (productOffers == null) 
			offerManager.addProduct(productId, new ProductOffers());
		
		// Add offer to this product id in the offermanager 
		offerManager.addOffer(offerId, productId, price);

		// Return a description of the transaction
		LinkedHashMap<String, String> result = new LinkedHashMap<>();
		result.put("OfferId", 	"" + priceString);
		result.put("Price", 	"" + offerId);
		result.put("ProductId", "" + productIdString);
		result.put("Product", 	"" + product);
		
		// Convert response to JSON
		String jsonResult = toJson(result);
		return jsonResult;		
	}

	/** REST API for nextCheapestOfferByPrice functionality.
	 * 
	 * @param productIdString	A valid product ID string
	 * @param priceString		The price to try to find the next cheapest offer for.
	 * 
	 * @return JSON response including the next cheapest offer's ID (or null if none).
	 * @throws InvalidProductIdException
	 * @author Roberto Olivares (reo)
	 */
	@RequestMapping("/products/{productIdString}/offers/nextCheapestByPrice/{priceString}")
	public String nextCheapestOfferByPrice(
			@PathVariable("productIdString") String productIdString,
			@PathVariable("priceString") String priceString
	) throws InvalidProductIdException {
		/*
		logger.info("products-service bySearchText() invoked: "
				+ productRepository.getClass().getName() + " for "
				+ searchText);
		 */
		
		// Attempt to locate the product
		Product product = productRepository.findByNumber(productIdString);
		if (product == null)
			return "ProductID not found: " + productIdString;
		
		// Generate the required key synonyms
		PRICE price = new PRICE(priceString);
		PRODUCTID productId = new PRODUCTID(productIdString);
		
		// Add offer to this product id in the offermanager 
		OFFERID offerId = offerManager.queryClosestOfferIdByPrice(productId, price);

		// Return a description of the transaction
		LinkedHashMap<String, String> result = new LinkedHashMap<>();
		result.put("NextCheapestOfferID", 	"" + offerId);
		result.put("TargetPrice", 			"" + priceString);
		result.put("ProductID", 			"" + productIdString);
		result.put("Product", 				"" + product);
		
		// Convert response to JSON
		String jsonResult = toJson(result);
		return jsonResult;
	}
	
	private String toJson(HashMap<String, String>map) {
		String jsonResult = "";
		try {
			jsonResult = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
			
		} catch (JsonProcessingException e) {
			jsonResult = e.getMessage();
		}
		return jsonResult;
	}
}
