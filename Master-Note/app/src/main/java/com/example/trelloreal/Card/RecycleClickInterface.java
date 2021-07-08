package com.example.trelloreal.Card;

public interface RecycleClickInterface {
    void onItemClick(int position);

    void onLongItemClick(int position);

    void swipeItem(int position);

    void checkedBox(int position, boolean check);
}

