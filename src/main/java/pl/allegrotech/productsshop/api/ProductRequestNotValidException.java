package pl.allegrotech.productsshop.api;

import pl.allegrotech.productsshop.domain.ProductRequestDto;

public class ProductRequestNotValidException extends RuntimeException {
    public ProductRequestNotValidException(ProductRequestDto productRequest) {
        super("Product request is not valid: " + productRequest);
    }
}
