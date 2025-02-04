Feature: Order Controller
  To manage orders in the system
  As a customer
  I want to create, update, delete, and retrieve orders

  Scenario: Create a new order successfully
    Given I have a valid order request
    When I send a POST request to "/api/v1/orders"
    Then the response status should be 200
    And the response body should contain the order details

  Scenario: Update an existing order successfully
    Given an order with orderNumber 1 exists
    And I have a valid order update request
    When I send a PUT request to "/api/v1/orders/1"
    Then the response status should be 200
    And the response body should contain the updated order details

  Scenario: Retrieve all orders successfully
    Given there are existing orders
    When I send a GET request to "/api/v1/orders"
    Then the response status should be 200
    And the response body should contain a list of orders

  Scenario: Delete an order successfully
    Given an order with orderNumber 1 exists
    When I send a DELETE request to "/api/v1/orders/1"
    Then the response status should be 200
    And the response body should contain "Pedido deletado com sucesso!"
