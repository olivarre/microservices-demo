package io.pivotal.microservices.products;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * reo - Home page controller for the Products microservice
 * 
 * @author Roberto Olivares
 */
@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String home() {
		return "index";
	}

}
