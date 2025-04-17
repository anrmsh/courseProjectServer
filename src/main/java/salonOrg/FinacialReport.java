package salonOrg;

public class FinacialReport {
    protected double income;
    protected double expenses;


    public FinacialReport() {
    }

    public FinacialReport(double income, double expenses) {
        this.income = income;
        this.expenses = expenses;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }
}
