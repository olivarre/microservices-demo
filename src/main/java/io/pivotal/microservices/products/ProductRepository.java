package io.pivotal.microservices.products;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

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
	 * Find Products whose owner name contains the specified string
	 * 
	 * @param partialName	Any alphabetic string.
	 * 
	 * @return The list of matching Products - always non-null, but may be empty.
	 */
	public List<Product> findByManufacturerContainingIgnoreCase(String partialName);

	/**
	 * Fetch the number of Products known to the system.
	 * 
	 * @return The number of Products.
	 */
	@Query("SELECT count(*) from Product")
	public int countProducts();
}
