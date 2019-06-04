package pl.allegrotech.productsshop.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import pl.allegrotech.productsshop.IntegrationTest;
import pl.allegrotech.productsshop.domain.ProductFacade;
import pl.allegrotech.productsshop.domain.ProductRequestDto;
import pl.allegrotech.productsshop.domain.ProductResponseDto;
import pl.allegrotech.productsshop.infrastructure.ProductRepository;

public class ProductEndpointTest extends IntegrationTest {

  @Autowired ProductFacade productFacade;

  @Autowired ProductRepository productRepository;

  @Test
  public void shouldCreateProduct() {
    // given
    var productRequest = new ProductRequestDto(null, "czerwona sukienka");
    var productRequestJson = mapToJson(productRequest);
    var httpRequest = buildRequest(productRequestJson);

    // when
    ResponseEntity<ProductResponseDto> response =
        httpClient.postForEntity(url("/products"), httpRequest, ProductResponseDto.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(OK);

    var productResponse = response.getBody();
    assertThat(productResponse.getId()).isNotBlank();
    assertThat(productResponse.getName()).isEqualTo("czerwona sukienka");
  }

  @Test
  public void shouldGetProduct() {
    // given
    var product = new ProductRequestDto(null, "czerwona sukienka");
    var createdProduct = productFacade.create(product);
    var url = url("/products/") + createdProduct.getId();

    // when
    ResponseEntity<ProductResponseDto> response =
        httpClient.getForEntity(url, ProductResponseDto.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isEqualTo(createdProduct);
  }

  @Test
  public void shouldGetNotFoundWhenProductIsNotAvailable() {
    // when
    ResponseEntity<String> response =
        httpClient.getForEntity(url("/products/dummyProductId"), String.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    assertThat(response.getBody()).isEqualTo("There is no product with id: dummyProductId");
  }

  @Test
  public void shouldUpdateProduct() {
    // given
    var productRequest = new ProductRequestDto(null, "iphone");
    var existingProduct = productFacade.create(productRequest);

    var updateProductRequest = new ProductRequestDto(existingProduct.getId(), "samsung");
    var updateProductRequestJson = mapToJson(updateProductRequest);

    var url = url("/products/") + existingProduct.getId();

    // when
    ResponseEntity<ProductResponseDto> response =
        httpClient.exchange(
            url, PUT, buildRequest(updateProductRequestJson), ProductResponseDto.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(OK);

    var productResponseDto = response.getBody();
    assertThat(productResponseDto.getId()).isEqualTo(existingProduct.getId());
    assertThat(productResponseDto.getName()).isEqualTo("samsung");
  }

  @Test
  public void shouldDeleteProduct() {
    // given
    var productRequest = new ProductRequestDto(null, "iphone");
    var existingProduct = productFacade.create(productRequest);
    var url = url("/products/") + existingProduct.getId();

    // when
    ResponseEntity<Void> response = httpClient.exchange(url, DELETE, null, Void.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThatThrownBy(() -> productFacade.get(existingProduct.getId()))
        .isInstanceOf(ProductNotFoundException.class)
        .hasMessage("There is no product with id: " + existingProduct.getId());
  }

  private HttpEntity<String> buildRequest(String json) {
    var headers = new HttpHeaders();
    headers.set("Content-type", "application/json");

    return new HttpEntity<>(json, headers);
  }
}
