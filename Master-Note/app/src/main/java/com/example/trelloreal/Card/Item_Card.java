package com.example.trelloreal.Card;

public class Item_Card {
    private String Title;
    private String TimeStart;
    private String TimeEnd;

    public Item_Card(String title, String timeStart, String timeEnd) {
        Title = title;
        TimeStart = timeStart;
        TimeEnd = timeEnd;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTimeStart() {
        return TimeStart;
    }

    public void setTimeStart(String timeStart) {
        TimeStart = timeStart;
    }

    public String getTimeEnd() {
        return TimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        TimeEnd = timeEnd;
    }
}
