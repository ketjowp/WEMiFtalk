package com.example.wemiftalk;

public class UserObject {

    public void setName(String name) {
        this.name = name;
    }

    private String name,
                    phone;

    public UserObject(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}

