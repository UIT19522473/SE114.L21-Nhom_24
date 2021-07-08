package com.example.trelloreal.DataBase;

public class clBackGround {
    private String Name;
    private String Link;

    public clBackGround(String name, String link) {
        Name = name;
        Link = link;
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
