package com.product.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.product.entity.ApprovalQueue;
import com.product.entity.Product;
import com.product.exception.AmountExceededException;
import com.product.exception.BadInputException;
import com.product.exception.ProductIdNotFoundException;
import com.product.repository.ApprovalQueueRepository;
import com.product.repository.ProductRepository;
import com.product.utils.Constants;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ApprovalQueueRepository approvalQueueRepository;
	
	
	public List<Product> getActiveProducts() {
		return productRepository.findByStatus(Constants.ACTIVE);
	}

	public String createProduct(Product product) throws DataIntegrityViolationException {
		if(product.getPrice()>10000) {
			throw new AmountExceededException();
		}else if(product.getPrice()>5000) {
			ApprovalQueue approvalQueue = new ApprovalQueue();
			approvalQueue.setProductName(product.getProductName());
			approvalQueue.setPrice(product.getPrice());
			approvalQueue.setStatus(Constants.PENDING);
			approvalQueue.setUpdatedBy(Constants.SYSTEM);
			approvalQueue.setCreatedBy(Constants.SYSTEM);
			approvalQueue.setUpdatedDate(LocalDateTime.now());
			approvalQueue.setRequestedDate(LocalDateTime.now());
			approvalQueueRepository.save(approvalQueue);
			return Constants.APPROVAL_QUEUE;
		}else {
			product.setPostedDate(LocalDateTime.now());
			product.setUpdatedBy(Constants.SYSTEM);
			product.setUpdatedDate(LocalDateTime.now());
			product.setCreatedBy(Constants.SYSTEM);
			productRepository.save(product);
			return Constants.PRODUCT;
		}
	}

	public void deleteById(Integer productId) throws DataIntegrityViolationException {
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()) {
			productRepository.deleteById(productId);
			ApprovalQueue approvalQueue = new ApprovalQueue();
			approvalQueue.setProductName(productOptional.get().getProductName());
			approvalQueue.setPrice(productOptional.get().getPrice());
			approvalQueue.setPostedDate(productOptional.get().getPostedDate());
			approvalQueue.setUpdatedBy(Constants.SYSTEM);
			approvalQueue.setCreatedBy(Constants.SYSTEM);
			approvalQueue.setUpdatedDate(LocalDateTime.now());
			approvalQueue.setRequestedDate(LocalDateTime.now());
			approvalQueue.setStatus(Constants.PENDING);
			approvalQueueRepository.save(approvalQueue);
		}else {
			throw new ProductIdNotFoundException();
		}
	}

	public String updateProduct(Product product, Integer productId) throws DataIntegrityViolationException {
		Optional<Product> productOptional = productRepository.findById(productId);
		if(productOptional.isPresent()) {
			if(product.getPrice()>(0.5*productOptional.get().getPrice()+productOptional.get().getPrice())) {
				productRepository.deleteById(productId);
				ApprovalQueue approvalQueue = new ApprovalQueue();
				approvalQueue.setProductName(productOptional.get().getProductName());
				approvalQueue.setPrice(productOptional.get().getPrice());
				approvalQueue.setPostedDate(productOptional.get().getPostedDate());
				approvalQueue.setUpdatedBy(Constants.SYSTEM);
				approvalQueue.setCreatedBy(Constants.SYSTEM);
				approvalQueue.setUpdatedDate(LocalDateTime.now());
				approvalQueue.setRequestedDate(LocalDateTime.now());
				approvalQueue.setStatus(Constants.PENDING);
				approvalQueueRepository.save(approvalQueue);
				return Constants.APPROVAL_QUEUE;
			}else {
				Product updateProduct = productOptional.get();
				updateProduct.setProductName(product.getProductName());
				updateProduct.setPrice(product.getPrice());
				updateProduct.setStatus(product.getStatus());
				updateProduct.setUpdatedBy(Constants.SYSTEM);
				updateProduct.setUpdatedDate(LocalDateTime.now());
				productRepository.save(updateProduct);
				return Constants.PRODUCT;
			}
		}else {
			throw new ProductIdNotFoundException();
		}
	}

	public List<Product> searchProducts(String productName, String minPrice, String maxPrice, String minPostedDateStr,
			String maxPostedDateStr) {
		Double minPriceDouble = null;
		Double maxPriceDouble = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			if(minPrice!=null) {
				minPriceDouble = Double.parseDouble(minPrice);
			}
			if(maxPrice!=null) {
				maxPriceDouble = Double.parseDouble(maxPrice);
			}
			if(minPostedDateStr!=null) {
				LocalDate.parse(minPostedDateStr, formatter);
				minPostedDateStr = minPostedDateStr+" 00:00:00";
			}
			if(maxPostedDateStr!=null) {
				LocalDate.parse(maxPostedDateStr, formatter);
				maxPostedDateStr = maxPostedDateStr+" 23:59:59";
			}
		}catch(Exception e) {
			throw new BadInputException();
		}
		
		return productRepository.findProductsBySearchCriteria(productName,minPriceDouble,maxPriceDouble,minPostedDateStr,maxPostedDateStr);
	}
}
