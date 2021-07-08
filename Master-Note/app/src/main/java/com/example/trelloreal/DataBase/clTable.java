package com.example.trelloreal.DataBase;

public class clTable {
    private String Id;
    private String Name;
    private String Link;

    public clTable(String id, String name, String link) {
        Id = id;
        Name = name;
        Link = link;
    }

    public clTable() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
