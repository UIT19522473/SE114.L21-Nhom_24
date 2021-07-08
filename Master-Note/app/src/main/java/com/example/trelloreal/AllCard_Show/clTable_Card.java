package com.example.trelloreal.AllCard_Show;

import com.example.trelloreal.Card.clCard;
import com.example.trelloreal.DataBase.clCellImage;

public class clTable_Card {


    private int type;
    private clCellImage Table;
    private clCard Card;

    public clTable_Card(int type, clCellImage table, clCard card) {
        this.type = type;
        Table = table;
        Card = card;
    }

    public clTable_Card() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public clCellImage getTable() {
        return Table;
    }

    public void setTable(clCellImage table) {
        Table = table;
    }

    public clCard getCard() {
        return Card;
    }

    public void setCard(clCard card) {
        Card = card;
    }


}
