package com.tongminhnhut.orderfood_manager.model;

/**
 * Created by tongminhnhut on 01/03/2018.
 */

public class Table_Status {
    private String Status ;
    private String Ngay;
    private String Gio ;

    public Table_Status() {
    }

    public Table_Status(String status, String ngay, String gio) {
        Status = "0";
        Ngay = ngay;
        Gio = gio;
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
