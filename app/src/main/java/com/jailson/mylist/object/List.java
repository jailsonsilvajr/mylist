package com.jailson.mylist.object;

import java.io.Serializable;

public class List implements Serializable {

    private String id;
    private String name;
    private String id_user;

    public List(String id, String name, String id_user){

        this.id = id;
        this.name = name;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
