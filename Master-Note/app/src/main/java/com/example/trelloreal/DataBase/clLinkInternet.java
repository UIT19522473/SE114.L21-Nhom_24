package com.example.trelloreal.DataBase;

import java.io.Serializable;

public class clLinkInternet implements Serializable {
    private String Id;
    private String NameLinkInternet;
    private String LinkInternet;

    public clLinkInternet(String id, String nameLinkInternet, String linkInternet) {
        Id = id;
        NameLinkInternet = nameLinkInternet;
        LinkInternet = linkInternet;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public clLinkInternet() {

    }

    public String getNameLinkInternet() {
        return NameLinkInternet;
    }

    public void setNameLinkInternet(String nameLinkInternet) {
        NameLinkInternet = nameLinkInternet;
    }

    public String getLinkInternet() {
        return LinkInternet;
    }

    public void setLinkInternet(String linkInternet) {
        LinkInternet = linkInternet;
    }
}
