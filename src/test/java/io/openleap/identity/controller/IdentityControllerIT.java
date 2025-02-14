package io.openleap.identity.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IdentityControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void test_ok_unregister_post() {
        URI url = URI.create("/identity/registration/unregister");

        var body = new HashMap<>();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        body.put("instanceId", "some_instance_id");

        var request = new RequestEntity<>(
                body,
                headers,
                HttpMethod.POST,
                url);

        var result = restTemplate.exchange(request, ObjectNode.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void test_ok_register_post() {
        URI url = URI.create("/identity/registration");

        var body = new HashMap<>();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        body.put("clientId", "some_instance_id");
        body.put("clientName", "some_name");
        var request = new RequestEntity<>(
                body,
                headers,
                HttpMethod.POST,
                url);

        var result = restTemplate.exchange(request, ObjectNode.class);
        assertEquals(request.getBody().get("clientName"), result.getBody().get("clientName").asText());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}