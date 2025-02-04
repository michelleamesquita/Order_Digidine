Feature: Order Service
  To handle order operations
  As a service layer
  I want to create, update, delete, and retrieve orders

  Scenario: Successfully create a new order
    Given a valid OrderRequestDTO
    When the createOrder method is called
    Then an OrderResponseDTO should be returned
    And the order should be saved in the repository

  Scenario: Update the status of an existing order
    Given an order with orderNumber 1 exists in the repository
    When the updateOrderStatusByOrderNumber method is called with status "PRONTO"
    Then the order status should be updated in the repository
    And the updated OrderResponseDTO should be returned

  Scenario: Retrieve all orders not marked as "Finalizado"
    Given there are orders in the repository with different statuses
    When the listOrders method is called
    Then a sorted list of orders should be returned

  Scenario: Delete an order by orderNumber
    Given an order with orderNumber 1 exists in the repository
    When the delete method is called with orderNumber 1
    Then the order should be removed from the repository
    And a notification should be published
