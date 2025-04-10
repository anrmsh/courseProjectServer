package salonOrg;

import java.io.Serializable;

public class OrderItems implements Serializable {
    private int orderItemId;
    private Order order; // агрегация
    private Product product;
    private int quantity;

    public OrderItems() {};

    public OrderItems(int orderItemId, Order order, Product product, int quantity) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
