package salonOrg;

import java.io.Serializable;

public class CartItems implements Serializable {
    private int cartItemId;
    private int cartId;
    private String productName;
    private int quantity;


    public CartItems(int cartItemId, int cartId, String productName, int quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
