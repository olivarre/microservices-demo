package io.pivotal.microservices.services.web;

import io.pivotal.microservices.services.web.Product;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Products web client controller.
 * 
 * Fetches Product info from the microservice via {@link WebProductsService}.
 * 
 * @author Roberto Olivares (reo)
 */
@Controller
public class WebProductsController {

	@Autowired
	protected WebProductsService productsService;

	protected Logger logger = Logger.getLogger(WebProductsController.class.getName());

	public WebProductsController(WebProductsService productsService) {
		this.productsService = productsService;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("productNumber", "name", "searchText"); // TODO reo - sets the fields allowed in REST requests ??
	}

	@RequestMapping("/products")
	public String goHome() {
		return "index";
	}

	/** PAGE - Returns the "product" result page.
	 * Uses the products microservice to find a product by number.
	 *  
	 * @param model
	 * @param productNumber
	 * @return
	 * @author Roberto Olivares (reo)
	 */
	@RequestMapping("/products/{productNumber}")
	public String byNumber(Model model,
			@PathVariable("productNumber") String productNumber) {

		logger.info("web-service byNumber() invoked: " + productNumber);

		Product product = productsService.findByNumber(productNumber);
		logger.info("web-service byNumber() found: " + product);
		model.addAttribute("product", product);
		return "product";
	}

	/** PAGE - Returns the "products" search results page.
	 * Uses the products microservice to find all products with a manufacturer name.
	 *  
	 * @param model
	 * @param name
	 * @return
	 * @author Roberto Olivares (reo)
	 */
	@RequestMapping("/products/byname/{name}")
	public String byName(Model model, @PathVariable("name") String name) {
		logger.info("web-service byName() invoked: " + name);

		// reo - invoke web products service to do the search for us
		List<Product> products = productsService.findByName(name);
		logger.info("web-service byName() found: " + products);
		
		// Update HTML model with product results. 
		model.addAttribute("search", name);
		if (products != null)
			model.addAttribute("products", products);
		
		// Return the ??
		return "products";
	}

	/**	PAGE - Returns a "product search" form.
	 * 
	 * @param model
	 * @return
	 * @author Roberto Olivares (reo)
	 */
	@RequestMapping(value = "/products/search", method = RequestMethod.GET)
	public String searchForm(Model model) {
		model.addAttribute("searchCriteria", new ProductSearchCriteria());
		return "productSearch";
	}

	/** PAGE - Returns a page of {"product", "products", "productSearch"} 
	 * Called by "productSearch.html"
	 * 
	 * @param model
	 * @param criteria
	 * @param result
	 * @return
	 * @author Roberto Olivares (reo)
	 */
	@RequestMapping(value = "/products/dosearch")
	public String doSearch(Model model, 
			ProductSearchCriteria criteria,
			BindingResult result) {
		
		logger.info("web-service search() invoked: " + criteria);

		// reo - validate the web request criteria
		criteria.validate(result);

		if (result.hasErrors())
			return "productSearch";

		String productNumber =  criteria.getProductNumber(); 
		if (StringUtils.hasText(productNumber)) {
			return byNumber(model, productNumber);					// Return results based on product number
		} else {
			String searchText = criteria.getSearchText();
			return byName(model, searchText);			// Return results based on search text
		}
	}
}
