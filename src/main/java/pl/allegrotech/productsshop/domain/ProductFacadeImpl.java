package pl.allegrotech.productsshop.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

import pl.allegrotech.productsshop.api.ProductNotFoundException;
import pl.allegrotech.productsshop.api.ProductRequestNotValidException;
import pl.allegrotech.productsshop.infrastructure.ProductRepository;

@Component
class ProductFacadeImpl implements ProductFacade {

  private final ProductRepository productRepository;

  ProductFacadeImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public ProductResponseDto create(ProductRequestDto productRequest) {
    if (!productRequest.isValid()) {
      throw new ProductRequestNotValidException(productRequest);
    }

    String id = UUID.randomUUID().toString();
    LocalDateTime createdAt = LocalDateTime.now();
    Product product = new Product(id, productRequest.getName(), createdAt);

    productRepository.save(product);

    return new ProductResponseDto(product.getId(), product.getName());
  }

  @Override
  public ProductResponseDto get(String id) {
    var product = productRepository.findById(id);

    if (product == null) {
      throw new ProductNotFoundException(id);
    }

    return new ProductResponseDto(product.getId(), product.getName());
  }

  @Override
  public ProductResponseDto update(ProductRequestDto productRequest) {
    if (!productRequest.isValid()) {
      throw new ProductRequestNotValidException(productRequest);
    }

    var product = productRepository.findById(productRequest.getId());
    var productToUpdate =
        new Product(product.getId(), productRequest.getName(), product.getCreatedAt());
    var updatedProduct = productRepository.save(productToUpdate);

    return new ProductResponseDto(updatedProduct.getId(), updatedProduct.getName());
  }

  @Override
  public void delete(String id) {
    productRepository.removeById(id);
  }
}
