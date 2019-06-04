package pl.allegrotech.productsshop.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity handleNotFound(ProductNotFoundException exception) {
    return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ProductRequestNotValidException.class)
  public ResponseEntity handleProductRequestNotValid(ProductRequestNotValidException exception) {
    return new ResponseEntity(exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ProductIdIncorrectException.class)
  public ResponseEntity handleProductIdIncorrect(ProductIdIncorrectException exception) {
    return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
