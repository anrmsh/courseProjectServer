package salonOrg;

import java.io.Serializable;

public class Warehouse implements Serializable {
    private int warehouseId;
    private int quantityStock;
    private String productName;

    public Warehouse(int warehouseId, int quantityStock, String productName) {
        this.warehouseId = warehouseId;
        this.quantityStock = quantityStock;
        this.productName = productName;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
