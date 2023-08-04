package com.product.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.product.entity.Product;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 public List<Product> findProductsBySearchCriteria(String productName, Double minPrice, Double maxPrice, String minPostedDateStr,
			 String maxPostedDateStr) {
		 CriteriaBuilder  criteriaBuilder = entityManager.getCriteriaBuilder();
		 CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);      
		 Root<Product> productRoot = criteriaQuery.from(Product.class);
		 List<Predicate> predicates = new ArrayList<>();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 if (productName != null) {
			 predicates.add(criteriaBuilder.equal(productRoot.get("productName"), productName));
		 }
		 
		 if (minPrice != null) {
			 predicates.add(criteriaBuilder.ge(productRoot.get("price").as(Double.class), minPrice));
		 }
		 if (maxPrice != null) {
			 predicates.add(criteriaBuilder.le(productRoot.get("price").as(Double.class), maxPrice));
		 }
		 
		 if(minPostedDateStr!=null && maxPostedDateStr!=null) {
			 predicates.add(criteriaBuilder.between(productRoot.get("postedDate"), LocalDateTime.parse(minPostedDateStr,formatter),
					 LocalDateTime.parse(maxPostedDateStr,formatter)));
		 }else if(minPostedDateStr!=null) {
			 predicates.add(criteriaBuilder.greaterThanOrEqualTo(productRoot.get("postedDate"), LocalDateTime.parse(minPostedDateStr,formatter)));
		 }else if(maxPostedDateStr!=null) {
			 predicates.add(criteriaBuilder.lessThanOrEqualTo(productRoot.get("postedDate"), LocalDateTime.parse(maxPostedDateStr,formatter)));
		 }
		 
		 criteriaQuery.where(predicates.toArray(new Predicate[0]));

		 return entityManager.createQuery(criteriaQuery).getResultList();
	 }
}
