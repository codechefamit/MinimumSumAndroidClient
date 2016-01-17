package com.codechefamit.min_sumandroidclient;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private int maxClientsCount,port;
    private  ClientThread[] threads;
    private  Turn turn;
    private  SumGame sg;
    public final String TAG = "com.codechefamit.min";

    GameServer(int players){
        maxClientsCount=players;
        port=7777;
        turn=new Turn(maxClientsCount);
        sg=new SumGame(maxClientsCount);
        threads=new ClientThread[maxClientsCount];
    }

    public void run() {
        try {
            Log.i(TAG,"Listening.....");
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e);
        }
        int count=0;
        while (count<maxClientsCount) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        threads[i] = new ClientThread(clientSocket, threads,turn,count+1,sg);
                        count++;
                        Log.i(TAG,count+" Connected");
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        Log.i(TAG,"Starting Clients");
        try {
            for (int i = 0; i < maxClientsCount; i++)
                threads[i].start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
