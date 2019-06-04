package pl.allegrotech.productsshop.infrastructure;

import pl.allegrotech.productsshop.domain.Product;

public interface ProductRepository {

  Product save(Product product);

  Product findById(String id);

  void removeById(String id);
}
