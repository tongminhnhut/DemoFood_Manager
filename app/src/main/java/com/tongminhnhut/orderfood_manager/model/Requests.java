package com.tongminhnhut.orderfood_manager.model;

import java.util.List;

/**
 * Created by nhut on 2/19/2018.
 */

public class Requests {
    private String Phone ;
    private String Name ;
    private String Address;
    private String Total ;
    private String status ;
    private String Note ;
    private List<Order> foods ;
    private String Time;

    public Requests() {
    }

    public Requests(String phone, String name, String address, String total, String note, List<Order> foods, String time) {
        Phone = phone;
        Name = name;
        Address = address;
        Total = total;
        this.foods = foods;
        Note = note ;
        this.status = "0"; // 0: Tại chỗ,  1:Đang ship ,   2: Đã ship
        Time = time;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
