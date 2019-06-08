package com.example.wemiftalk.User;

public class UserObject {

    public void setName(String name) {
        this.name = name;
    }

    private String uid,
                    name,
                    phone;

    public UserObject(String uid, String name, String phone){
        this.uid = uid;
        this.name = name;
        this.phone = phone;

    }

    public String getUid() { return uid; }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}

