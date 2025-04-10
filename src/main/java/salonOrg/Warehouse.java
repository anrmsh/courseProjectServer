package salonOrg;

import java.io.Serializable;

public class Warehouse implements Serializable {
    private int warehouseId;
    private int quantityStock;
    private Product product;

    public Warehouse(int warehouseId, int quantityStock, Product product) {
        this.warehouseId = warehouseId;
        this.quantityStock = quantityStock;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
