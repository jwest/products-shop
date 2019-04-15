package pl.allegrotech.productsshop;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

public class HelloWorldEndpointTest extends IntegrationTest {

    @Test
    public void shouldReturnGreetings() {
        // when
        ResponseEntity<String> response = httpClient.getForEntity(url("/hello"), String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo("Hello World!");
    }

}
