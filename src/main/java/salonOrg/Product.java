package salonOrg;

import java.io.Serializable;

public class Product implements Serializable {
    private int productId;
    private String productName;
    private double sellPrice;
    private String category;
    private double cost;
    private int quantity;

    public Product() {
        this.productName = "";
        this.sellPrice = 0;
        this.category = "";
        this.cost = 0;
        this.quantity = 0;
    }

    public Product(int productId, String productName, double sellPrice, String category, double cost) {
        this.productId = productId;
        this.productName = productName;
        this.sellPrice = sellPrice;
        this.category = category;
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
