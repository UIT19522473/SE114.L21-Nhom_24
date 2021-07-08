package com.example.trelloreal.DataBase;

public class clUser {
    private String User;
    private String PassWord;


    public clUser() {
    }

    public clUser(String user, String passWord) {
        User = user;
        PassWord = passWord;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
