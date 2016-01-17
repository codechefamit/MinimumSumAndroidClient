package com.codechefamit.min_sumandroidclient;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    final ArrayList<Card> cards;
    int top;

    Deck(){
        cards=new ArrayList<Card>(52);
        for(int i=1;i<=13;i++){
            for(int j=0;j<4;j++){
                cards.add(new Card(i,j));
            }
        }
        top=51;
        shuffle();
    }

    void shuffle(){
        Collections.shuffle(cards);
    }

    boolean toDeck(Card c){
        if(top==51){
            return false;
        }
        else
        {
            cards.add(c);
            top++;
            shuffle();
            return true;
        }
    }


    Card draw(){
        Card c=cards.get(top);
        cards.remove(top--);
        return c;
    }

}
