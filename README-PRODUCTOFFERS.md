# Roberto's ProductOffers - Rough Draft Implementation

```java
	public class InvalidProductIdException extends Exception {
	}
	
	public class PRICE extends BigDecimal {
		public PRICE(String arg0) {
			super(arg0);
		}
	}
	
	public class OFFERID extends BigInteger {
		public OFFERID(String arg0) {
			super(arg0);
		}
	}	
	
	public class PRODUCTID extends BigInteger {
		public PRODUCTID(String arg0) {
			super(arg0);
		}
	}	
	
	public class Product {
		TreeMap<PRICE, OFFERID>	priceToOffersMap;	// Ordered list of OFFERIDs for this product, sorted by PRICE
	
		public Product () {
			priceToOffersMap = new TreeMap<>();
		}
	
		public void addOfferIdByPrice(OFFERID offerId, PRICE price) {
			priceToOffersMap.put(price, offerId);	// RB balanced tree - incurs O(log n) insertion cost in exchange for O(log n) lookup cost (by price)
		}
	
		// PRICE price	
		// returns:		The offerid of the offer with the closest lower price to price, or null if non found.
		public OFFERID getClosestOfferIdToPrice(PRICE price) {
			Entry<PRICE, OFFERID> entry = priceToOffersMap.floorEntry(price);	// TODO - FAIL
			if (entry == null)	// TODO - FAIL
				return null;
			OFFERID offerId = entry.getValue();	// Find the offerID with the closest lower price to price
			return offerId;
		}
	}

	public class OfferManager {
	
		HashMap<PRODUCTID, Product> prodIdToProductsMap;
	
		public OfferManager() {
			prodIdToProductsMap = new HashMap<>();
		}
		
		public void addProduct(PRODUCTID productId, Product product) {
			prodIdToProductsMap.put(productId, product);
		}
		
		//
		public Product getProductById(PRODUCTID product_id) {
			return prodIdToProductsMap.get(product_id);
		}
		
		// Adds offer_id, with the specified price, for product_id. 
		public void addOffer(OFFERID offer_id, PRODUCTID product_id, PRICE price) throws InvalidProductIdException {
			Product product = getProductById(product_id);	
			if (product != null)
			product.addOfferIdByPrice(offer_id, price);
			else
				throw new InvalidProductIdException();
		}
		
		// QueryClosestOffer returns the id of a offer corresponding to the specified product, which has its prices closest to the query parameter. 
		// product_id		The product id. Throws InvalidProductIdException if not valid.
		// price		The price to return the immediately lower offer for. Returns null if no offer exists that's lower.
		// Returns: 		The offer that's immediately lower in price, or null if no offer lower found.
		OFFERID queryClosestOffer(PRODUCTID product_id, PRICE price) throws InvalidProductIdException {
			Product product = getProductById(product_id);
			if (product == null)
				throw new InvalidProductIdException();
			OFFERID offerId = product.getClosestOfferIdToPrice(price);
			return offerId;
		}
	}

``` 
