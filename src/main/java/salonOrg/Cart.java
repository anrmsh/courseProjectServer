package salonOrg;

import java.io.Serializable;

public class Cart implements Serializable {
    private int userId;

    public Cart(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
