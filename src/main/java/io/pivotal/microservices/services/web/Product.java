package io.pivotal.microservices.services.web;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Product DTO - used to interact with the {@link WebProductsService}.
 * 
 * @author Roberto Olivares
 */
@JsonRootName("Product")
public class Product {

	protected Long id;
	protected String number;
	protected String name;
	protected String manufacturer;

	protected BigDecimal price;

	/**
	 * Default constructor for JPA only.
	 */
	protected Product() {
		price = BigDecimal.ZERO;
	}

	public long getId() {
		return id;
	}

	/**
	 * Set JPA id - for testing and JPA only. Not intended for normal use.
	 * 
	 * @param id
	 *            The new id.
	 */
	protected void setId(long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	protected void setNumber(String productNumber) {
		this.number = productNumber;
	}

	public String getName() {
		return name;
	}

	protected void setName(String owner) {
		this.name = owner;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public BigDecimal getPrice() {
		return price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	protected void setPrice(BigDecimal value) {
		price = value;
		price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	public String toString() {
		return number + " [" + name + "]: $" + price;
	}

}
