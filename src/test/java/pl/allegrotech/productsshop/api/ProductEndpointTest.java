package pl.allegrotech.productsshop.api;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.*;

public class ProductEndpointTest extends IntegrationTest {

    @Autowired
    ProductFacade productFacade;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void shouldCreateProduct() {
        // given
        var productRequest = new ProductRequestDto(null, "czerwona sukienka");
        var productRequestJson = mapToJson(productRequest);
        var httpRequest = getHttpRequest(productRequestJson);

        // when
        ResponseEntity<String> response = httpClient.postForEntity(url("/products"), httpRequest, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).contains("\"name\":\"czerwona sukienka\"");
    }

    @Test
    public void shouldGetProduct() {
        // given
        var product = new ProductRequestDto(null, "czerwona sukienka");
        var createdProduct = productFacade.create(product);
        var url = url("/products/") + createdProduct.getId();

        //when
        ResponseEntity<ProductResponseDto> response = httpClient.getForEntity(url, ProductResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(createdProduct);
    }

    @Test
    public void shouldGetNotFoundWhenProductIsNotAvailable() {
        //when
        ResponseEntity<String> response = httpClient.getForEntity(url("/products/dummyProductId"), String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("There is no product with id: dummyProductId");
    }

    @Test
    public void shouldUpdateProduct() {
        //given
        var productRequest = new ProductRequestDto(null, "iphone");
        var existingProduct = productFacade.create(productRequest);

        var updateProductRequest = new ProductRequestDto(existingProduct.getId(), "samsung");
        var updateProductRequestJson = mapToJson(updateProductRequest);

        var url = url("/products/") + existingProduct.getId();

        //when
        ResponseEntity<ProductResponseDto> response = httpClient.exchange(url, PUT,
                getHttpRequest(updateProductRequestJson), ProductResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);

        var productResponseDto = response.getBody();
        assertThat(productResponseDto.getId()).isEqualTo(existingProduct.getId());
        assertThat(productResponseDto.getName()).isEqualTo("samsung");
    }

    @Test
    public void shouldDeleteProduct() {
        //given
        var productRequest = new ProductRequestDto(null, "iphone");
        var existingProduct = productFacade.create(productRequest);
        var url = url("/products/") + existingProduct.getId();

        //when
        ResponseEntity<Void> response = httpClient.exchange(url, DELETE, null, Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(productRepository.count()).isEqualTo(0);
    }

    private HttpEntity<String> getHttpRequest(String json) {
        var headers = new HttpHeaders();
        headers.set("Content-type", "application/json");

        return new HttpEntity<>(json, headers);
    }

}
