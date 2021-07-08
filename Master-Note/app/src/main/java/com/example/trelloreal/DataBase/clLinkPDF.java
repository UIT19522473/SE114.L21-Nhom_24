package com.example.trelloreal.DataBase;

import java.io.Serializable;

public class clLinkPDF implements Serializable {
    private String Id;
    private String NameLinkPDF;
    private String LinkPDF;
    private String Size;

    public clLinkPDF(String id, String nameLinkPDF, String linkPDF, String size) {
        Id = id;
        NameLinkPDF = nameLinkPDF;
        LinkPDF = linkPDF;
        Size = size;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public clLinkPDF() {

    }

    public String getNameLinkPDF() {
        return NameLinkPDF;
    }

    public void setNameLinkPDF(String nameLinkPDF) {
        NameLinkPDF = nameLinkPDF;
    }

    public String getLinkPDF() {
        return LinkPDF;
    }

    public void setLinkPDF(String linkPDF) {
        LinkPDF = linkPDF;
    }
}
