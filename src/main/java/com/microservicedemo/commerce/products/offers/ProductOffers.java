package com.microservicedemo.commerce.products.offers;

import java.util.TreeMap;
import java.util.Map.Entry;

import com.microservicedemo.commerce.abstractions.ids.OFFERID;
import com.microservicedemo.commerce.abstractions.ids.PRICE;

public class ProductOffers {

	// Ordered list of OFFERIDs for this product, sorted by PRICE
	transient TreeMap<PRICE, OFFERID> priceToOffersMap = new TreeMap<>();	
	
	/** 
	 * Inserts and offer into the ordered (by price) OFFERID map.
	 *  
	 * @performance		Incurs O(log n) insertion cost in exchange for O(log n) lookup cost (by price)
	 * 
	 * @param offerId
	 * @param price
	 * @author Roberto Olivares (reo)
	 */
	public void addOfferIdByPrice(OFFERID offerId, PRICE price) {
		priceToOffersMap.put(price, offerId);	// RB balanced tree - incurs O(log n) insertion cost in exchange for O(log n) lookup cost (by price)
	}
	
	/**
	 * @performance		Incurs O(log n) insertion cost in exchange for O(log n) lookup cost (by price)
	 * 
	 * @param 	price
	 * @return	The offerid of the offer with the closest lower price to price, or null if non found.
	 * @author Roberto Olivares (reo)
	 */
	public OFFERID getClosestOfferIdToPrice(PRICE price) {
		Entry<PRICE, OFFERID> entry = priceToOffersMap.floorEntry(price);
		if (entry == null)
			return null;
		OFFERID offerId = entry.getValue();	// Find the offerID with the closest lower price to price
		return offerId;
	}
		
}
