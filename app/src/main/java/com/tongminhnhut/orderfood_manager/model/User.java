package com.tongminhnhut.orderfood_manager.model;

/**
 * Created by nhut on 2/22/2018.
 */

public class User {
    private String Name, Phone, Password,IsStaff ;

    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = "true" ;
    }


    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
