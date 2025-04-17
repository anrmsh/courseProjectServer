package salonOrg;

import java.io.Serializable;
import java.time.LocalDate;

public class OperationAccounter implements Serializable {
    private String dateOfOperation;
    private String typeOfOperation;
    private double amountOfOperation;

    public OperationAccounter() {}

    public OperationAccounter(double amountOfOperation, String typeOfOperation, String dateOfOperation) {
        this.amountOfOperation = amountOfOperation;
        this.typeOfOperation = typeOfOperation;
        this.dateOfOperation = dateOfOperation;
    }

    public String getDateOfOperation() {
        return dateOfOperation;
    }

    public void setDateOfOperation(String dateOfOperation) {
        this.dateOfOperation = dateOfOperation;
    }

    public String getTypeOfOperation() {
        return typeOfOperation;
    }

    public void setTypeOfOperation(String typeOfOperation) {
        this.typeOfOperation = typeOfOperation;
    }

    public double getAmountOfOperation() {
        return amountOfOperation;
    }

    public void setAmountOfOperation(double amountOfOperation) {
        this.amountOfOperation = amountOfOperation;
    }
}
