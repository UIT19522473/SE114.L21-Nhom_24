package com.example.trelloreal;

public class CellGrid {
    private String Title;
    private int Image;

    public CellGrid(String title, int image) {
        Title = title;
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
