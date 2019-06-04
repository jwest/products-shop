package pl.allegrotech.productsshop.api;

public class ProductIdIncorrectException extends RuntimeException {
  public ProductIdIncorrectException(String urlId, String bodyId) {
    super("Provided products ids are not the same: urlId=" + urlId + ", bodyId=" + bodyId);
  }
}
