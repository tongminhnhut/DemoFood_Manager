package com.tongminhnhut.orderfood_manager.model;

/**
 * Created by tongminhnhut on 05/03/2018.
 */

public class Notification {
    public String Body;
    public String Title ;

    public Notification() {
    }

    public Notification(String body, String title) {
        Body = body;
        Title = title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
