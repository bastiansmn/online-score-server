package com.bastiansmn.scoreserver.controller;

import com.bastiansmn.scoreserver.domain.Namespace;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NamespaceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldCreateNamespace() {
        ResponseEntity<Namespace> response = restTemplate.postForEntity("http://localhost:" + port + "/my-namespace", null, Namespace.class);
        Namespace namespace = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(namespace);
        assertEquals(namespace.getName(), "my-namespace");
    }

    @Test
    public void shouldFetchNamespace() {
        ResponseEntity<Namespace> response = restTemplate.getForEntity("http://localhost:" + port + "/my-namespace", Namespace.class);
        Namespace namespace = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(namespace);
        assertEquals(namespace.getName(), "my-namespace");
    }

    @Test
    public void shouldThrowNotFound() {
        ResponseEntity<Namespace> response = restTemplate.getForEntity("http://localhost:" + port + "/not-a-namespace", Namespace.class);
        Namespace namespace = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(namespace);
        assertNull(namespace.getNamespace_id());
    }

    @Test
    @Disabled
    public void shouldConnectToNamespace() {
        ResponseEntity<Namespace> response = restTemplate.postForEntity("http://localhost:" + port + "/my-namespace/connect", null, Namespace.class);
        Namespace namespace = response.getBody();

        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(namespace);
        assertEquals(namespace.getName(), "my-namespace");
    }

}
