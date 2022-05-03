package com.example.t_edits_app_tobias;

public class TOrder {

    private String OrderStatus;
    private String ClientName;
    private String LogoName;
    private String PalleteNumber;
    private String AssignedDesigner;
    private String PackageCode;

    public TOrder(String orderStatus, String clientName, String logoName, String palleteNumber, String assignedDesigner, String packageCode) {
        OrderStatus = orderStatus;
        ClientName = clientName;
        LogoName = logoName;
        PalleteNumber = palleteNumber;
        AssignedDesigner = assignedDesigner;
        PackageCode = packageCode;
    }

    public TOrder(){

    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getLogoName() {
        return LogoName;
    }

    public void setLogoName(String logoName) {
        LogoName = logoName;
    }

    public String getPalleteNumber() {
        return PalleteNumber;
    }

    public void setPalleteNumber(String palleteNumber) {
        PalleteNumber = palleteNumber;
    }

    public String getAssignedDesigner() {
        return AssignedDesigner;
    }

    public void setAssignedDesigner(String assignedDesigner) {
        AssignedDesigner = assignedDesigner;
    }

    public String getPackageCode() {
        return PackageCode;
    }

    public void setPackageCode(String packageCode) {
        PackageCode = packageCode;
    }
}
