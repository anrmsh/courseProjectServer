package salonOrg;

import java.io.Serializable;

public class CartItems implements Serializable {
    private int cartItemId;
    private int cartId;
    private Product product;
    private int quantity;

    public CartItems() {
        this.product = new Product();
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public String getProductName() {
        return product.getProductName();
    }

    public Double getProductPrice() {
        return product.getSellPrice();
    }

    public void setProductName(String productName) {
        this.product.setProductName(productName);
    }

    public void setProductPrice(Double productPrice) {
        this.product.setSellPrice(productPrice);
    }


    public Double getTotalCost(){
        return product.getSellPrice()*quantity;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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
