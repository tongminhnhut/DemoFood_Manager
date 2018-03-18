package com.tongminhnhut.orderfood_manager.model;

/**
 * Created by nhut on 2/18/2018.
 */

public class Order {
    private String ProductID ;
    private String ProductName;
    private String Quanlity ;
    private String Price ;
    private String Discount ;

    public Order() {
    }

    public Order(String productID, String productName, String quanlity, String price, String discount) {
        ProductID = productID;
        ProductName = productName;
        Quanlity = quanlity;
        Price = price;
        Discount = discount;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuanlity() {
        return Quanlity;
    }

    public void setQuanlity(String quanlity) {
        Quanlity = quanlity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
