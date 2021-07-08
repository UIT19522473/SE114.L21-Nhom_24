package com.example.trelloreal.DataBase;

import java.io.Serializable;

public class clImage implements Serializable {
    private String NameImage;
    private String Link;

    public clImage(String nameImage, String link) {
        NameImage = nameImage;
        Link = link;
    }
    public clImage() {

    }

    public String getNameImage() {
        return NameImage;
    }

    public void setNameImage(String nameImage) {
        NameImage = nameImage;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
