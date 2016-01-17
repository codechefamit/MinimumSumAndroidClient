package com.codechefamit.min_sumandroidclient;

public class Turn {
    int t;
    int players;
    public Turn(int p){
        t=1;
        players=p;
    }
    void next(){
        if(t==players)
            t=1;
        else
            t++;
    }

    synchronized int whoseTurn(){
        return t;

    }
}
