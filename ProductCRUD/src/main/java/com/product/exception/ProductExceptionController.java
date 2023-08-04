package com.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductExceptionController {

	@ExceptionHandler(value = AmountExceededException.class)
	public ResponseEntity<Object> exception(AmountExceededException exception) {
	      return new ResponseEntity<>("Amount Exceeded for the given product", HttpStatus.BAD_REQUEST);		
	}
	
	@ExceptionHandler(value = ApprovalIdNotFoundException.class)
	public ResponseEntity<Object> exception(ApprovalIdNotFoundException exception) {
	      return new ResponseEntity<>("Approval Id Not Found, Please provide valid Approval Id", HttpStatus.NOT_FOUND);		
	}
	
	@ExceptionHandler(value = ProductIdNotFoundException.class)
	public ResponseEntity<Object> exception(ProductIdNotFoundException exception) {
	      return new ResponseEntity<>("Product Id Not Found, Please provide valid Product Id", HttpStatus.NOT_FOUND);		
	}
	
	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<Object> exception(ConstraintViolationException exception) {
	      return new ResponseEntity<>("The product with the same price and status exists.", HttpStatus.BAD_REQUEST);		
	}
	
	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> exception(RecordNotFoundException exception) {
	      return new ResponseEntity<>("Record not found for the given search criteria.", HttpStatus.NOT_FOUND);		
	}
	
	@ExceptionHandler(value = BadInputException.class)
	public ResponseEntity<Object> exception(BadInputException exception) {
	      return new ResponseEntity<>("Please provide the valid input", HttpStatus.BAD_REQUEST);		
	}
}
