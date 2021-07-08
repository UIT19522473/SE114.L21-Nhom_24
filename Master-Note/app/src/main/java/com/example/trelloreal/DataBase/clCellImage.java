package com.example.trelloreal.DataBase;

import java.io.Serializable;

public class clCellImage implements Serializable {
    private String id;
    private String Name;
    private String link;

    public clCellImage(String id, String name, String link) {
        this.id = id;
        Name = name;
        this.link = link;
    }
    public clCellImage() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
