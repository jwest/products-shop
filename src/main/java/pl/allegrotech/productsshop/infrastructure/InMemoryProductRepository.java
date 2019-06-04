package pl.allegrotech.productsshop.infrastructure;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

import pl.allegrotech.productsshop.domain.Product;

@Repository
class InMemoryProductRepository implements ProductRepository {

  private final Map<String, Product> products = new HashMap<>();

  @Override
  public Product save(Product product) {
    products.put(product.getId(), product);
    return product;
  }

  @Override
  public Product findById(String id) {
    return products.get(id);
  }

  @Override
  public void removeById(String id) {
    products.remove(id);
  }
}
