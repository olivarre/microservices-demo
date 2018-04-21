package io.pivotal.microservices.products;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * reo - Persistent Product entity with JPA markup. 
 * 
 * @author Roberto Olivares
 */
@Entity
@Table(name = "T_PRODUCT")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 0L;

	@Id
	protected Long id;

	protected String number;

	@Column(name = "name")
	protected String name;

	@Column(name = "manufacturer")
	protected String manufacturer;	// reo

	@Column(name = "price")
	protected BigDecimal price;

	/**
	 * This is a very simple, and non-scalable solution to generating unique
	 * ids. Not recommended for a real application. Consider using the
	 * <tt>@GeneratedValue</tt> annotation and a sequence to generate ids.
	 * 
	 * @return The next available id.
	 */
	protected static Long getNextId() {
		synchronized (nextId) {
			return nextId++;
		}
	}

	/**
	 * Default constructor for JPA only.
	 */
	protected Product() {
		price = BigDecimal.ZERO;
	}

	public Product(String number, String name, String manufacturer) {
		id = getNextId();
		this.number = number;
		this.name = name;
		this.manufacturer = manufacturer;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getNumber() {
		return number;
	}

	protected void setNumber(String productNumber) {
		this.number = productNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	protected void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public BigDecimal getPrice() {
		return price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public void decreasePrice(BigDecimal amount) {
		price.subtract(amount);
	}

	public void increasePrice(BigDecimal amount) {
		price.add(amount);
	}

	@Override
	public String toString() {
		return number + " [" + manufacturer + "] [" + name + "]: $" + price; // reo
	}

}
