package com.product.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="approval_queue")
@Data
@NoArgsConstructor
public class ApprovalQueue {

	 @Id
	 @GeneratedValue(strategy =  GenerationType.SEQUENCE)
	 private Integer id;
	 
	 @Column(name = "product_name")
	 private String productName;
	 
	 @Column(name = "price")
	 private Double price;
	 
	 @Column(name = "status")
	 private String status;
	 
	 @Column(name = "created_by")
	 private String createdBy;
	 
	 @Column(name = "updated_by")
	 private String updatedBy;
	 
	 @Column(name = "requested_date")
	 private LocalDateTime requestedDate;
	 
	 @Column(name = "updated_date")
	 private LocalDateTime updatedDate;
	 
	 @Column(name = "posted_date")
	 private LocalDateTime postedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public LocalDateTime getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(LocalDateTime requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public LocalDateTime getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDateTime postedDate) {
		this.postedDate = postedDate;
	}
	 
}
