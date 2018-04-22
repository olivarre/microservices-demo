package io.pivotal.microservices.products;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * reo - Repository for Product data implemented using Spring Data JPA.
 * 
 * @author Roberto Olivares
 */
public interface ProductRepository extends Repository<Product, Long> {
	/**
	 * Find an Product with the specified Product number.
	 *
	 * @param ProductNumber
	 * 
	 * @return The Product if found, null otherwise.
	 */
	public Product findByNumber(String ProductNumber);

	/**
	 * Find Products whose name contains the specified string
	 * 
	 * @param partialName	Any alphabetic string.
	 * 
	 * @return The list of matching Products - always non-null, but may be empty.
	 */
	public List<Product> findByNameContainingIgnoreCase(String partialName);

	/**
	 * Find Products whose details contain the specified string anywhere (in name, manufacturer, description, etc.)
	 * 
	 * @author Roberto Olivares (reo)
	 * 
	 * @param searchText	Any alphabetic string.
	 * 
	 * Failed to create query method public abstract 
	 * 	java.util.List io.pivotal.microservices.products.ProductRepository
	 * 		.findBySearchTextIgnoreCase(java.lang.String)! 
	 * 
	 * No property searchText found for type Product!
	 * 
	 * @return The list of matching Products - always non-null, but may be empty.
	 */
//	@Query("")
//	@Query("SELECT p FROM Product")
	//@Query("SELECT p FROM Product WHERE CONTAINS(p.MANUFACTURER, :#{#searchText}) OR CONTAINS(p.NAME, :#{#searchText})")
	//@Query("select u from User u where u.firstname = :#{#customer.firstname}")
	//@Query("select u from User u where u.age = ?#{[0]}")
	// https://spring.io/blog/2014/07/15/spel-support-in-spring-data-jpa-query-definitions
//	public List<Product> findBySearchTextIgnoreCase(@Param("searchText") String searchText);
	// TODO 	REO - IMPLEMENT KEYWORD SEARCH ACROSS ENTIRE PRODCUT DESCRIPTION
	
	/**
	 * Fetch the number of Products known to the system.
	 * 
	 * @return The number of Products.
	 */
	@Query("SELECT COUNT(*) FROM Product")
	public int countProducts();
}
