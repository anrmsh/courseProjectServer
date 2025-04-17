package salonOrg;

public class ManagerReport {
    private int totalOrders;
    private int completedOrders;
    private int totalProductsSold;
    private String topProduct;
    private double totalRevenue;
    private double averageCheque;

    public ManagerReport(int totalOrders, int completedOrders, int totalProductsSold, String topProduct, double totalRevenue, double averageCheque) {
        this.totalOrders = totalOrders;
        this.completedOrders = completedOrders;
        this.totalProductsSold = totalProductsSold;
        this.topProduct = topProduct;
        this.totalRevenue = totalRevenue;
        this.averageCheque = averageCheque;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public int getTotalProductsSold() {
        return totalProductsSold;
    }

    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }

    public String getTopProduct() {
        return topProduct;
    }

    public void setTopProduct(String topProduct) {
        this.topProduct = topProduct;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getAverageCheque() {
        return averageCheque;
    }

    public void setAverageCheque(double averageCheque) {
        this.averageCheque = averageCheque;
    }
}
