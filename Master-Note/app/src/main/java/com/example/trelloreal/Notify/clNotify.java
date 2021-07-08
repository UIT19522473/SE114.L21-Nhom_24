package com.example.trelloreal.Notify;

public class clNotify {
    private String IdNotify;
    private String nameUser;
    private String idTable;
    private String idCard;
    private String nameTable;
    private String nameCard;
    private String Title;
    private String Mess;
    private String Time;

    public clNotify(String idNotify, String nameUser, String idTable, String idCard, String nameTable, String nameCard, String title, String mess, String time) {
        IdNotify = idNotify;
        this.nameUser = nameUser;
        this.idTable = idTable;
        this.idCard = idCard;
        this.nameTable = nameTable;
        this.nameCard = nameCard;
        Title = title;
        Mess = mess;
        Time = time;
    }

    public clNotify() {

    }

    public String getIdNotify() {
        return IdNotify;
    }

    public void setIdNotify(String idNotify) {
        IdNotify = idNotify;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMess() {
        return Mess;
    }

    public void setMess(String mess) {
        Mess = mess;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
