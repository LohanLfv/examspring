package com.lohan.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void leControleurDevraitRetournerLeMessageParDefaut() {
        String responseBody = restTemplate.getForObject("/", String.class);
        assertThat(responseBody).isEqualTo("Hello World, Lohan Lefevre");
    }
}
