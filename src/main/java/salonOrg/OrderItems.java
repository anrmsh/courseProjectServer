package salonOrg;

import java.io.Serializable;

public class OrderItems implements Serializable {
    private int orderItemId;
    private int orderId;
    private String productName;
    private int quantity;

    public OrderItems(int orderItemId, int quantity, String productName, int orderId) {
        this.orderItemId = orderItemId;
        this.quantity = quantity;
        this.productName = productName;
        this.orderId = orderId;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
