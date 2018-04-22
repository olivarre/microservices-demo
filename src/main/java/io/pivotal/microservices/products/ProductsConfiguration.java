package io.pivotal.microservices.products;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

/**
 * The Products Spring configuration.
 * 
 * @author Roberto Olivares
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.products")
@EnableJpaRepositories("io.pivotal.microservices.products")
@PropertySource("classpath:db-config.properties")
public class ProductsConfiguration {

	protected Logger logger;

	public ProductsConfiguration() {
		logger = Logger.getLogger(getClass().getName());
	}

	/**
	 * Creates an in-memory "products" database populated with test data for fast testing
	 */
	@Bean
	public DataSource dataSource() {
		logger.info("dataSource() invoked");

		// Create an in-memory H2 relational database containing some demo products.
		DataSource dataSource = (new EmbeddedDatabaseBuilder())
				.addScript("classpath:testdb/product-schema.sql")
				.addScript("classpath:testdb/product-data.sql")
				.build();

		logger.info("dataSource = " + dataSource);

		// Sanity check
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> products = jdbcTemplate.queryForList("SELECT number FROM T_PRODUCT");
		logger.info("System has " + products.size() + " products");

		// reo - Optionally populate db
		boolean populateDBWithNewEntries = false;
		if (populateDBWithNewEntries)
			populateDBWithRandomEntries(jdbcTemplate, products);

		return dataSource;
	}

	private void populateDBWithRandomEntries(JdbcTemplate jdbcTemplate, List<Map<String, Object>> products) {
		// Populate with random prices
		Random rand = new Random();

		for (Map<String, Object> item : products) {
			String number = (String) item.get("number");
			String manufacturer = (String) item.get("manufacturer");
			String name = (String) item.get("name");
			BigDecimal price = new BigDecimal(rand.nextInt(10000000) / 100.0).setScale(2, BigDecimal.ROUND_HALF_UP);
			jdbcTemplate.update("UPDATE T_PRODUCT SET price = ?, name = ?, manufacturer = ? WHERE number = ?", price, name, manufacturer, number);
		}
	}
}
