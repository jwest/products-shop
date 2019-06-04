package pl.allegrotech.productsshop.domain;

public interface ProductFacade {

  ProductResponseDto get(String id);

  ProductResponseDto create(ProductRequestDto productRequest);

  ProductResponseDto update(ProductRequestDto productRequest);

  void delete(String id);
}
