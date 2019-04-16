package pl.allegrotech.productsshop

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration
@SpringBootTest(
        classes = ProductsshopApplication,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class IntegrationSpec extends Specification {

    @Autowired
    TestRestTemplate httpClient;

    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    int port;

    String mapToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    String url(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }
}
