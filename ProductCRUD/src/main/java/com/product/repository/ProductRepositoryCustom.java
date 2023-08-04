package com.product.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.product.entity.Product;

@Repository
public interface ProductRepositoryCustom {

	 List<Product> findProductsBySearchCriteria(String productName, Double minPrice, Double maxPrice, String minPostedDate,
			 String maxPostedDate);
}
