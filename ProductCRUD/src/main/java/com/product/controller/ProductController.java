package com.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.product.entity.ApprovalQueue;
import com.product.entity.Product;
import com.product.exception.RecordNotFoundException;
import com.product.exception.ConstraintViolationException;
import com.product.service.ApprovalQueueService;
import com.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProductController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	 
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ApprovalQueueService approvalQueueService;


	@GetMapping(path="/products")
	public ResponseEntity<List<Product>> getActiveProducts(){
		List<Product> lists =  productService.getActiveProducts().stream().sorted((p1,p2)-> (int) p2.getId()-p1.getId()).collect(Collectors.toList());
		return new ResponseEntity<List<Product>>(lists,HttpStatus.OK); 
	}
	
	@GetMapping(path="/products/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam (required=false)String productName,
			@RequestParam (required=false) String minPrice, 
			@RequestParam (required=false) String maxPrice,
			@RequestParam (required=false) String minPostedDate,
			@RequestParam (required=false) String maxPostedDate){
		List<Product> lists = productService.searchProducts(productName,minPrice,maxPrice,minPostedDate,maxPostedDate);
		
		if(CollectionUtils.isEmpty(lists)) {
			throw new RecordNotFoundException();
		}
		return new ResponseEntity<List<Product>>(lists,HttpStatus.OK); 
	}
	
	@PostMapping(path="/products")
	public ResponseEntity<String> createProduct(@RequestBody Product product) {
		String tableName = "";
		try {
			 tableName =  productService.createProduct(product);
		}catch(DataIntegrityViolationException ex) {
			throw new ConstraintViolationException();
		}
		LOGGER.info("Product "+ product.getProductName()+" saved successfully");
		return new ResponseEntity<String>("Product "+ product.getProductName()+" saved successfully into "+tableName+" table.",HttpStatus.OK);
	}
	
	@PutMapping(path="/products/{productId}")
	public ResponseEntity<String> updateProduct(@RequestBody Product product,@PathVariable Integer productId){ 
		String tableName = "";
		try {
			tableName = productService.updateProduct(product,productId);
		}catch(DataIntegrityViolationException ex) {
			throw new ConstraintViolationException();
		}
		LOGGER.info("Product Id "+ productId+" updated successfully");
		return new ResponseEntity<String>("Product Id "+ productId+" updated successfully into "+tableName+" table.",HttpStatus.OK);
	}
	
	@DeleteMapping(path="/products/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer productId){
		try {
			productService.deleteById(productId);
		}catch(DataIntegrityViolationException ex) {
			throw new ConstraintViolationException();
		}
		LOGGER.info("The Product Id "+productId+" deleted successfully");
		return new ResponseEntity<String>("The Product Id "+productId+" deleted successfully",HttpStatus.OK);
	}
	
	@GetMapping(path="/products/approval-queue")
	public ResponseEntity<List<ApprovalQueue>> getProductsFromApprovalQueue(){
		List<ApprovalQueue> lists =  approvalQueueService.getAllProducts()
				.stream().sorted((a1,a2)->a1.getRequestedDate().compareTo(a2.getRequestedDate()))
				.collect(Collectors.toList());
		return new ResponseEntity<List<ApprovalQueue>>(lists,HttpStatus.OK); 
	}
	
	@PutMapping(path="/products/approval-queue/{approvalId}/approve")
	public ResponseEntity<Product> approveProduct(@PathVariable Integer approvalId){
		Product product = null;
		try {
			product =  approvalQueueService.updateProduct(approvalId,true);
		}catch(DataIntegrityViolationException ex) {
			throw new ConstraintViolationException();
		}
		LOGGER.info("The Approval Id "+approvalId+" approved from Approval Queue successfully");
		return new ResponseEntity<Product>(product,HttpStatus.OK); 
	}
	
	@PutMapping(path="/products/approval-queue/{approvalId}/reject")
	public ResponseEntity<String> rejectProduct(@PathVariable Integer approvalId){
		try {
			approvalQueueService.updateProduct(approvalId,false);
		}catch(DataIntegrityViolationException ex) {
			throw new ConstraintViolationException();
		}
		LOGGER.info("The Approval Id "+approvalId+" rejected from Approval Queue successfully");
		return new ResponseEntity<String>("The Approval Id "+approvalId+" rejected from Approval Queue successfully",HttpStatus.OK);
	}
	
}
