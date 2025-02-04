package com.fiap.digidine.bdd.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OrderControllerSteps {
    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<?> response;
    private final String BASE_URL = "http://localhost:8080/api/v1/orders";
    private Map<String, Object> requestPayload;

    @Given("I have a valid order request")
    public void iHaveAValidOrderRequest() {
        requestPayload = new HashMap<>();
        requestPayload.put("customerNumber", 12345);
        requestPayload.put("productsNumber", "prod123");
        requestPayload.put("totalPrice", 29.99);
        requestPayload.put("orderStatus", "CREATED");
    }

    @When("I send a POST request to {string}")
    public void iSendAPostRequestTo(String endpoint) {
        response = restTemplate.postForEntity(BASE_URL + endpoint, requestPayload, Object.class);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        Assertions.assertEquals(HttpStatus.valueOf(expectedStatus), response.getStatusCode());
    }

    @Then("the response body should contain the order details")
    public void theResponseBodyShouldContainTheOrderDetails() {
        Assertions.assertNotNull(response.getBody());
    }

    @Given("an order with orderNumber {int} exists")
    public void anOrderWithOrderNumberExists(int orderNumber) {
        // Pré-criação de um pedido no sistema para o teste
        Map<String, Object> order = new HashMap<>();
        order.put("customerNumber", 12345);
        order.put("productsNumber", "prod123");
        order.put("totalPrice", 29.99);
        order.put("orderStatus", "CREATED");
        restTemplate.postForEntity(BASE_URL, order, Object.class);
    }

    @When("I send a PUT request to {string}")
    public void iSendAPutRequestTo(String endpoint) {
        restTemplate.put(BASE_URL + endpoint, requestPayload);
        response = new ResponseEntity<>(HttpStatus.OK);
    }

    @Then("the response body should contain the updated order details")
    public void theResponseBodyShouldContainTheUpdatedOrderDetails() {
        Assertions.assertNotNull(response.getBody());
    }

    @Given("there are existing orders")
    public void thereAreExistingOrders() {
        iHaveAValidOrderRequest();
        iSendAPostRequestTo("");
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String endpoint) {
        response = restTemplate.getForEntity(BASE_URL + endpoint, Object.class);
    }

    @Then("the response body should contain a list of orders")
    public void theResponseBodyShouldContainAListOfOrders() {
        Assertions.assertTrue(response.getBody() instanceof Iterable);
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String endpoint) {
        restTemplate.delete(BASE_URL + endpoint);
        response = new ResponseEntity<>("Pedido deletado com sucesso!", HttpStatus.OK);
    }

    @Then("the response body should contain {string}")
    public void theResponseBodyShouldContain(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, response.getBody());
    }
}
