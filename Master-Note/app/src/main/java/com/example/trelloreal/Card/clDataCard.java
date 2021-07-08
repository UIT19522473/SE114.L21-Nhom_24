package com.example.trelloreal.Card;

import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;

import java.io.Serializable;
import java.util.List;

public class clDataCard implements Serializable {
    private clCard clCard;
    private List<clLinkPDF> linkPDFS;
    private List<clLinkInternet> linkInternets;

    public clDataCard(com.example.trelloreal.Card.clCard clCard, List<clLinkPDF> linkPDFS, List<clLinkInternet> linkInternets) {
        this.clCard = clCard;
        this.linkPDFS = linkPDFS;
        this.linkInternets = linkInternets;
    }

    public com.example.trelloreal.Card.clCard getClCard() {
        return clCard;
    }

    public void setClCard(com.example.trelloreal.Card.clCard clCard) {
        this.clCard = clCard;
    }

    public List<clLinkPDF> getLinkPDFS() {
        return linkPDFS;
    }

    public void setLinkPDFS(List<clLinkPDF> linkPDFS) {
        this.linkPDFS = linkPDFS;
    }

    public List<clLinkInternet> getLinkInternets() {
        return linkInternets;
    }

    public void setLinkInternets(List<clLinkInternet> linkInternets) {
        this.linkInternets = linkInternets;
    }
}
