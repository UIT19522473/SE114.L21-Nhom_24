package com.example.trelloreal.Card;

import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;

import java.io.Serializable;
import java.util.List;

public class clCard implements Serializable {
    private String Id;
    private String NameEvent;
    private String Description;
    private String StartEvent;
    private String EndEvent;
    private String Check;
    private String Notify;
    private String Request;
//    private List<clLinkPDF> linkFileDPF;
//    private List<clLinkInternet> linkInternet;


    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getNotify() {
        return Notify;
    }

    public void setNotify(String notify) {
        Notify = notify;
    }

    public clCard(String id, String nameEvent, String description, String startEvent, String endEvent, String check, String notify,String request) {
        Id = id;
        NameEvent = nameEvent;
        Description = description;
        StartEvent = startEvent;
        EndEvent = endEvent;
        Check = check;
        Notify = notify;
        Request = request;
    }

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }

    public clCard() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNameEvent() {
        return NameEvent;
    }

    public void setNameEvent(String nameEvent) {
        NameEvent = nameEvent;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStartEvent() {
        return StartEvent;
    }

    public void setStartEvent(String startEvent) {
        StartEvent = startEvent;
    }

    public String getEndEvent() {
        return EndEvent;
    }

    public void setEndEvent(String endEvent) {
        EndEvent = endEvent;
    }
}
