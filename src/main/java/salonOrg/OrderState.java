package salonOrg;

import java.io.Serializable;

public class OrderState implements Serializable {
    private int orderStateId;
    private String orderStateName;

    public OrderState() {}

    public OrderState(int orderStateId, String orderStateName) {
        this.orderStateId = orderStateId;
        this.orderStateName = orderStateName;
    }

    public int getOrderStateId() {
        return orderStateId;
    }

    public void setOrderStateId(int orderStateId) {
        this.orderStateId = orderStateId;
    }

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }
}
