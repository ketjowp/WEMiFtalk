package com.example.wemiftalk.User;

import java.io.Serializable;

public class UserObject implements Serializable {

    public void setName(String name) {
        this.name = name;
    }

    private String uid,
                    name,
                    phone,
                    notificationKey;
    private Boolean selected = false;
    public UserObject(String uid){  // drugi konstruktor zeby moza bylo miec uzytkownika z user id bez innych parametrow
        this.uid = uid;
    }

    public UserObject(String uid, String name, String phone){
        this.uid = uid;
        this.name = name;
        this.phone = phone;

    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getUid() { return uid; }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}

