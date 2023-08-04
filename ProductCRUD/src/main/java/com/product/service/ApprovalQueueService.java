package com.product.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.product.entity.ApprovalQueue;
import com.product.entity.Product;
import com.product.exception.ApprovalIdNotFoundException;
import com.product.repository.ApprovalQueueRepository;
import com.product.repository.ProductRepository;
import com.product.utils.Constants;

@Service
public class ApprovalQueueService {
	
	@Autowired
	private ApprovalQueueRepository approvalQueueRepository;
	
	@Autowired
	private ProductRepository productRepository;

	public List<ApprovalQueue> getAllProducts() {
		return approvalQueueRepository.findAll();
	}


	public void deleteApprovalId(Integer approvalId) {
		approvalQueueRepository.deleteById(approvalId);
	}


	public Product approveProduct(Integer approvalId) throws DataIntegrityViolationException {
		
		Optional<ApprovalQueue> approvalQueueOptional = approvalQueueRepository.findById(approvalId);
		if(approvalQueueOptional.isPresent()) {
			approvalQueueRepository.deleteById(approvalId);
			Product product = new Product();
			product.setCreatedBy(Constants.SYSTEM);
			product.setPostedDate(LocalDateTime.now());
			product.setUpdatedBy(Constants.SYSTEM);
			product.setUpdatedDate(LocalDateTime.now());
			product.setStatus(approvalQueueOptional.get().getStatus());
			return productRepository.save(product);
		}else {
			throw new ApprovalIdNotFoundException();
		}
	}


	public Product updateProduct(Integer approvalId, boolean status) throws DataIntegrityViolationException {
		Optional<ApprovalQueue> approvalQueueOptional = approvalQueueRepository.findById(approvalId);
		if(approvalQueueOptional.isPresent()) {
			approvalQueueRepository.deleteById(approvalId);
			Product product = new Product();
			product.setCreatedBy(Constants.SYSTEM);
			product.setPostedDate(LocalDateTime.now());
			product.setUpdatedBy(Constants.SYSTEM);
			product.setUpdatedDate(LocalDateTime.now());
			product.setProductName(approvalQueueOptional.get().getProductName());
			product.setPrice(approvalQueueOptional.get().getPrice());
			product.setRequestedDate(approvalQueueOptional.get().getRequestedDate());
			if(status) {
				product.setStatus(Constants.ACTIVE);
			}else {
				product.setStatus(approvalQueueOptional.get().getStatus());
			}
			return productRepository.save(product);
		}else {
			throw new ApprovalIdNotFoundException();
		}
	}
}
