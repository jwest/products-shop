package pl.allegrotech.productsshop.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import pl.allegrotech.productsshop.IntegrationSpec
import pl.allegrotech.productsshop.domain.ProductFacade
import pl.allegrotech.productsshop.domain.ProductRequestDto
import pl.allegrotech.productsshop.domain.ProductResponseDto
import spock.lang.Unroll

import static org.springframework.http.HttpStatus.OK

class ProductEndpointSpec extends IntegrationSpec {

    @Autowired
    ProductFacade productFacade;

    def "should get product"() {
        given: "create new product"
            def newProduct = new ProductRequestDto(null, "czerwona sukienka")

        and: "create new product"
            def createdProduct = productFacade.create(newProduct);

        and: "define url for get request"
            def url = url("/products/") + createdProduct.getId();

        when: "retrieve product"
            def response = httpClient.getForEntity(url, ProductResponseDto.class);

        then: "request should succeed"
            response.getStatusCode() == OK

        and: "returned product should equal to created product"
            response.getBody() == createdProduct
    }


    @Unroll
    def "should create valid product (#productRequest.getName())"() {
        given:
            def productRequestJson = mapToJson(productRequest)
            def httpRequest = buildRequest(productRequestJson)

        when:
            def response = httpClient.postForEntity(url("/products"), httpRequest, ProductResponseDto.class)

        then:
            response.getStatusCode() == responseStatusCode
            response.getBody().getId().length() > 0
            response.getBody().getName() == createdProduct.getName()

        where:
            productRequest                                   | responseStatusCode | createdProduct
            new ProductRequestDto(null, "czerwona sukienka") | OK                 | new ProductResponseDto("dummyId", "czerwona sukienka")
            new ProductRequestDto(null, "czarne skarpetki")  | OK                 | new ProductResponseDto("dummyId", "czarne skarpetki")
    }

    private static HttpEntity<String> buildRequest(String json) {
        def headers = new HttpHeaders();
        headers.set("Content-type", "application/json");

        new HttpEntity<>(json, headers);
    }
}
