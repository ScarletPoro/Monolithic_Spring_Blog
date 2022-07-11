package it.cgmconsulting.myblogc9.exception;



import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionManagement {

	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex){
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
}
