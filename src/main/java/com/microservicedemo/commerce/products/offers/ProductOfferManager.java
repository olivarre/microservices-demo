package com.microservicedemo.commerce.products.offers;

import java.util.HashMap;

import com.microservicedemo.commerce.abstractions.ids.OFFERID;
import com.microservicedemo.commerce.abstractions.ids.PRICE;
import com.microservicedemo.commerce.abstractions.ids.PRODUCTID;
import com.microservicedemo.commerce.exceptions.InvalidProductIdException;

/** Manages a mapping of <productId to ProductOffers> 
 * 
 * @author Roberto Olivares (reo)
 */
public class ProductOfferManager {
	
	HashMap<PRODUCTID, ProductOffers> prodIdToProductsMap;

	public ProductOfferManager() {
		prodIdToProductsMap = new HashMap<>();
	}
	
	public void addProduct(PRODUCTID productId, ProductOffers product) {
		prodIdToProductsMap.put(productId, product);
	}
	
	//
	public ProductOffers getProductOffersById(PRODUCTID product_id) {
		return prodIdToProductsMap.get(product_id);
	}
	
	/**
	 * Adds an offer_id, with the specified price, for the given product_id.
	 * 
	 * @param offer_id
	 * @param product_id
	 * @param price
	 * @throws InvalidProductIdException
	 * @author Roberto Olivares (reo)
	 */
	public void addOffer(OFFERID offer_id, PRODUCTID product_id, PRICE price) throws InvalidProductIdException {
		ProductOffers product = getProductOffersById(product_id);	
		if (product != null)
			product.addOfferIdByPrice(offer_id, price);
		else
			throw new InvalidProductIdException();
	}
	
	/**
	 * QueryClosestOffer returns the id of a offer corresponding to the specified product, which has its prices closest to the query parameter.
	 * 
	 * @param product_id	The product id. Throws InvalidProductIdException if not valid.
	 * @param price			The price to return the immediately lower offer for. Returns null if no offer exists that's lower.
	 * @return				The offer that's immediately lower in price, or null if no offer lower found.
	 * @throws InvalidProductIdException
	 * @author Roberto Olivares (reo)
	 */
	public OFFERID queryClosestOfferIdByPrice(PRODUCTID product_id, PRICE price) throws InvalidProductIdException {
		ProductOffers product = getProductOffersById(product_id);
		if (product == null)
			throw new InvalidProductIdException();
		OFFERID offerId = product.getClosestOfferIdToPrice(price);
		return offerId;
	}
}