package com.example.empapp.model;

public class PayDetails {
    private int employeeId;
    private String uanNumber;
    private String pfAccountNumber;
    private String pensionAccountNumber;
    private String esicAccountNumber;
    private String bankName;
    private String bankAccountNumber;
    private double pfPercentage;
    private double professionalTaxPercentage;
    private double esiPercentage;
    private double roundOffRecoveryPercentage;
    private double gtilRecoveryPercentage;
    private double paymentPerHour;

    // Constructor
    public PayDetails(int employeeId, String uanNumber, String pfAccountNumber, String pensionAccountNumber,
                      String esicAccountNumber, String bankName, String bankAccountNumber,
                      double pfPercentage, double professionalTaxPercentage, double esiPercentage,
                      double roundOffRecoveryPercentage, double gtilRecoveryPercentage, double paymentPerHour) {
        this.employeeId = employeeId;
        this.uanNumber = uanNumber;
        this.pfAccountNumber = pfAccountNumber;
        this.pensionAccountNumber = pensionAccountNumber;
        this.esicAccountNumber = esicAccountNumber;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.pfPercentage = pfPercentage;
        this.professionalTaxPercentage = professionalTaxPercentage;
        this.esiPercentage = esiPercentage;
        this.roundOffRecoveryPercentage = roundOffRecoveryPercentage;
        this.gtilRecoveryPercentage = gtilRecoveryPercentage;
        this.paymentPerHour = paymentPerHour;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUanNumber() {
        return uanNumber;
    }

    public void setUanNumber(String uanNumber) {
        this.uanNumber = uanNumber;
    }

    public String getPfAccountNumber() {
        return pfAccountNumber;
    }

    public void setPfAccountNumber(String pfAccountNumber) {
        this.pfAccountNumber = pfAccountNumber;
    }

    public String getPensionAccountNumber() {
        return pensionAccountNumber;
    }

    public void setPensionAccountNumber(String pensionAccountNumber) {
        this.pensionAccountNumber = pensionAccountNumber;
    }

    public String getEsicAccountNumber() {
        return esicAccountNumber;
    }

    public void setEsicAccountNumber(String esicAccountNumber) {
        this.esicAccountNumber = esicAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public double getPfPercentage() {
        return pfPercentage;
    }

    public void setPfPercentage(double pfPercentage) {
        this.pfPercentage = pfPercentage;
    }

    public double getProfessionalTaxPercentage() {
        return professionalTaxPercentage;
    }

    public void setProfessionalTaxPercentage(double professionalTaxPercentage) {
        this.professionalTaxPercentage = professionalTaxPercentage;
    }

    public double getEsiPercentage() {
        return esiPercentage;
    }

    public void setEsiPercentage(double esiPercentage) {
        this.esiPercentage = esiPercentage;
    }

    public double getRoundOffRecoveryPercentage() {
        return roundOffRecoveryPercentage;
    }

    public void setRoundOffRecoveryPercentage(double roundOffRecoveryPercentage) {
        this.roundOffRecoveryPercentage = roundOffRecoveryPercentage;
    }

    public double getGtilRecoveryPercentage() {
        return gtilRecoveryPercentage;
    }

    public void setGtilRecoveryPercentage(double gtilRecoveryPercentage) {
        this.gtilRecoveryPercentage = gtilRecoveryPercentage;
    }

    public double getPaymentPerHour() {
        return paymentPerHour;
    }

    public void setPaymentPerHour(double paymentPerHour) {
        this.paymentPerHour = paymentPerHour;
    }
}
