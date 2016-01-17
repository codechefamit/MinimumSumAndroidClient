package com.codechefamit.min_sumandroidclient;

import java.util.ArrayList;
import java.util.Scanner;

public class SumGame {
    Deck deck;
    final ArrayList<Card[]> players;
    Card faceUp;

    public SumGame(int no){
        deck=new Deck();
        players=new ArrayList<Card[]>(no);
        for(int i=0;i<no;i++){
            Card c[]=new Card[5];
            for(int j=0;j<5;j++){
                c[j]=deck.draw();
            }
            players.add(c);
        }
        faceUp=deck.draw();
    }

    Card replace(int r,int ch,Card[] c,int playInd){
        int i=0;
        int count=0;
        Card ret = null;
        for(Card cd:c){
            if(cd!=null){
                if(cd.getValue()==r){
                    c[i]=null;
                    if(count==0){
                        ret=cd;
                        count++;
                    }
                    else
                    {
                        deck.toDeck(cd);
                    }
                }
                i++;
            }
        }

        switch(ch){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                i=0;
                for(Card cd:c){
                    if(cd==null){
                        c[i]=faceUp;
                        break;
                    }
                    i++;
                }
                break;

            case 0:
                deck.toDeck(faceUp);
                i=0;
                for(Card cd:c){
                    if(cd==null){
                        cd=deck.draw();
                        c[i]=cd;
                        break;
                    }
                    i++;
                }
                break;
        }
        players.remove(playInd);
        players.add(playInd, removeNull(c));
        return ret;

    }

    int sum(int ind){
        int add=0;
        for(Card cd:players.get(ind)){
            if(cd!=null){
                add+=cd.getValue();
            }
        }
        return add;

    }

    Card[] removeNull(Card[] c){
        ArrayList<Card> list = new ArrayList<Card>();

        for(Card s :c) {
            if(s != null) {
                list.add(s);
            }
        }

        return c= list.toArray(new Card[list.size()]);
    }

    public static void main(String[] args) {
        SumGame sg=new SumGame(2);
        Scanner sc=new Scanner(System.in);
        boolean game =true;
        while(game){
            for(int i=0;i<2;i++){
                System.out.println("Player "+(i+1)+" turn:");
                System.out.println("Make a move :");
                System.out.println("1. Format for move is [int]-[int] ([to replace][choice])");
                System.out.println("2. Choice in the format can be 0 or 1 \n 0 for card from deck \n 1 for card that is face up");
                System.out.println("3. To show send just a 0");
                System.out.println("4. Your face up card is: "+sg.faceUp);
                System.out.println("5. Your cards are: ");
                Card cd[]=sg.players.get(i);
                for(Card c:cd){
                    if(c!=null)
                        System.out.print(c+",");
                }
                String move=sc.next().trim();
                if(move.length()>1){
                    int r=Integer.parseInt(move.split("-")[0]);
                    int ch=Integer.parseInt(move.split("-")[1]);
                    sg.faceUp=sg.replace(r, ch, sg.players.get(i),i);
                }
                else{
                    System.out.println("Sum of players are:");
                    for(int j=0;j<2;j++){
                        System.out.println("Player "+j+1+":");
                        if(j==i){
                            System.out.println(">>"+sg.sum(j));
                        }else
                            System.out.println(sg.sum(j));
                    }
                    game=false;
                    break;
                }
            }
        }
        sc.close();

    }

}
