package com.tongminhnhut.orderfood_manager.model;

/**
 * Created by tongminhnhut on 01/03/2018.
 */

public class Table_Status {
    private String Status ;
    private String Ngay;
    private String Gio ;
    private String Cash_Status ;

    public Table_Status() {
    }

    public Table_Status(String status, String ngay, String gio,String cash_Status) {
        Status = "0";
        Ngay = ngay;
        Gio = gio;
        Cash_Status = "0";
    }

    public String getCash_Status() {
        return Cash_Status;
    }

    public void setCash_Status(String cash_Status) {
        Cash_Status = cash_Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }

    public String getGio() {
        return Gio;
    }

    public void setGio(String gio) {
        Gio = gio;
    }
}
