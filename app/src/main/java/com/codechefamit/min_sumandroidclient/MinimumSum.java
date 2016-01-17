package com.codechefamit.min_sumandroidclient;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MinimumSum extends AppCompatActivity {
    private Socket clientSocket = null;
    private static PrintStream os = null;
    private static BufferedReader is = null;
    private String name,ip;
    private Button btnPlay,btnShow;
    private ImageButton cardImg1,cardImg2,cardImg3,cardImg4,cardImg5,cardDiscard;
    private ImageView cardKeep,cardRem;
    private TextView txtStatus;
    private ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimum_sum);
        listAdapter=new ArrayAdapter<String>(this,R.layout.mylist);
        ListView listView=(ListView)findViewById(R.id.listTicker);
        listView.setAdapter(listAdapter);
        btnShow=(Button)findViewById(R.id.btnShow);
        btnPlay=(Button)findViewById(R.id.btnPlay);
        cardImg1=(ImageButton)findViewById(R.id.cardImg1);
        cardImg2=(ImageButton)findViewById(R.id.cardImg2);
        cardImg3=(ImageButton)findViewById(R.id.cardImg3);
        cardImg4=(ImageButton)findViewById(R.id.cardImg4);
        cardImg5=(ImageButton)findViewById(R.id.cardImg5);
        cardDiscard=(ImageButton)findViewById(R.id.cardDiscard);
        cardKeep=(ImageView)findViewById(R.id.cardKeep);
        cardRem=(ImageView)findViewById(R.id.cardRemove);
        txtStatus=(TextView)findViewById(R.id.txtStatus);
        cardDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardKeep.setBackground(cardDiscard.getBackground());
                cardKeep.setTag(cardDiscard.getTag());
            }
        });
        cardImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRem.setBackground(cardImg1.getBackground());
                cardRem.setTag(cardImg1.getTag());
            }
        });
        cardImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRem.setBackground(cardImg2.getBackground());
                cardRem.setTag(cardImg2.getTag());
            }
        });
        cardImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRem.setBackground(cardImg3.getBackground());
                cardRem.setTag(cardImg3.getTag());
            }
        });
        cardImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRem.setBackground(cardImg4.getBackground());
                cardRem.setTag(cardImg4.getTag());
            }
        });
        cardImg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRem.setBackground(cardImg5.getBackground());
                cardRem.setTag(cardImg5.getTag());
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card r=Card.convertToCard((String) cardRem.getTag());
                String move=null;
                if(cardKeep.getTag()!=null){
                    Card k=Card.convertToCard((String) cardKeep.getTag());
                    move=""+r.getValue()+"-"+k.getValue();
                }else
                {
                    move=""+r.getValue()+"-0";
                }
                os.println(move);
                btnPlay.setEnabled(false);
                cardImg1.setTag(null);
                cardImg1.setVisibility(View.INVISIBLE);
                cardImg2.setTag(null);
                cardImg2.setVisibility(View.INVISIBLE);
                cardImg3.setTag(null);
                cardImg3.setVisibility(View.INVISIBLE);
                cardImg4.setTag(null);
                cardImg4.setVisibility(View.INVISIBLE);
                cardImg5.setTag(null);
                cardImg5.setVisibility(View.INVISIBLE);
                cardKeep.setTag(null);
                cardKeep.setVisibility(View.INVISIBLE);
                cardRem.setTag(null);
                cardRem.setVisibility(View.INVISIBLE);
                new Minion().execute();
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setEnabled(false);
                cardKeep.setTag(null);
                cardKeep.setVisibility(View.INVISIBLE);
                cardRem.setTag(null);
                cardRem.setVisibility(View.INVISIBLE);
                btnShow.setEnabled(false);
                os.println("0");
                new Minion().execute();
            }
        });
        Bundle bd=getIntent().getExtras();
        ip=bd.getString("ip");
        name=bd.getString("name");
        setTitle(name);
        new Connect().execute(ip,name);
    }

    private class Connect extends AsyncTask<String,Void,Socket>{

        @Override
        protected Socket doInBackground(String... params) {
            String ip = params[0];
            String name = params[1];
            Socket socket = null;
            try {
                socket = new Socket(ip, 7777);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return socket;
        }

        @Override
        protected void onPostExecute(Socket socket) {
            super.onPostExecute(socket);
            clientSocket=socket;
            try {
                os = new PrintStream(clientSocket.getOutputStream());
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                txtStatus.setText("Status : Waiting for players...");
                os.println(name);
                new Minion().execute();
            } catch (IOException e) {
                e.printStackTrace();
                txtStatus.setText("Status : Not Connected");
            }
        }
    }
    private class Minion extends AsyncTask<String,String,List<String>>{
        @Override
        protected List<String> doInBackground(String... params) {
            String responseLine = null;
            List<String> list = new ArrayList<String>();
            try {
                while ((responseLine = is.readLine()) != null) {
                    if(responseLine.startsWith("<"))
                        publishProgress(responseLine);
                    else if(responseLine.equals("round")) {
                        list=null;
                        break;
                    }
                    else if(responseLine.equals("end"))
                        break;
                    else if(responseLine.equals("Bye")){
                        list.clear();
                        list.add(responseLine);
                        break;
                    }
                    else if(!responseLine.startsWith("<"))
                        list.add(responseLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            for(String s:values){
                if(s.startsWith("<")){
                    if(s.contains("turn") || s.contains("Game")) {
                        txtStatus.setText("Status : " + s);
                        if(s.contains("Game"))
                        setTitle("Disconnected from "+ip +"| "+name);
                        else
                            setTitle("Connected to "+ip+"| "+name);
                    }
                    listAdapter.add(s);
                }
            }
        }

        @Override
        protected void onPostExecute(List<String> status) {
            super.onPostExecute(status);
                int ch=1;
                if(status==null){
                    txtStatus.setText("Status : Game End");
                    listAdapter.add("<-----Round----->");
                    new Minion().execute();
                }
                else{
                    for(String str:status){
                        if(str.contains("Bye")){
                            txtStatus.setText("Status : Eliminated");
                        }
                        else if(str.startsWith("faceUp")){
                            String parts[]=str.split("-");
                            Card cd=Card.convertToCard(parts[1]);
                            String url="@drawable/c"+cd.getValue()+cd.getSuit();
                            int resId = getResources().getIdentifier(url, null, getPackageName());
                            cardDiscard.setBackgroundResource(resId);
                            cardDiscard.setTag(cd.toString());
                            cardDiscard.setVisibility(View.VISIBLE);
                        }
                        else{
                            Card cd=Card.convertToCard(str);
                            String url="@drawable/c"+cd.getValue()+cd.getSuit();
                            switch(ch){
                                case 1:
                                    cardImg1.setBackgroundResource(getResources().getIdentifier(url, null, getPackageName()));
                                    cardImg1.setVisibility(View.VISIBLE);
                                    cardImg1.setTag(cd.toString());
                                    break;
                                case 2:
                                    cardImg2.setBackgroundResource(getResources().getIdentifier(url, null, getPackageName()));
                                    cardImg2.setVisibility(View.VISIBLE);
                                    cardImg2.setTag(cd.toString());
                                    break;
                                case 3:
                                    cardImg3.setBackgroundResource(getResources().getIdentifier(url, null, getPackageName()));
                                    cardImg3.setVisibility(View.VISIBLE);
                                    cardImg3.setTag(cd.toString());
                                    break;
                                case 4:
                                    cardImg4.setBackgroundResource(getResources().getIdentifier(url, null, getPackageName()));
                                    cardImg4.setVisibility(View.VISIBLE);
                                    cardImg4.setTag(cd.toString());
                                    break;
                                case 5:
                                    cardImg5.setBackgroundResource(getResources().getIdentifier(url, null, getPackageName()));
                                    cardImg5.setVisibility(View.VISIBLE);
                                    cardImg5.setTag(cd.toString());
                                    break;
                            }
                            ch++;
                        }
                    }
                    if(txtStatus.getText().toString().contains("Your")){
                        btnPlay.setEnabled(true);
                        cardKeep.setVisibility(View.VISIBLE);
                        cardKeep.setBackgroundResource(getResources().getIdentifier("@drawable/q", null, getPackageName()));
                        cardKeep.setTag(null);
                        cardRem.setBackgroundResource(getResources().getIdentifier("@drawable/q", null, getPackageName()));
                        cardRem.setVisibility(View.VISIBLE);
                        cardRem.setTag(null);
                        btnShow.setEnabled(true);
                    }
                    else
                    {
                        new Minion().execute();
                        btnShow.setEnabled(false);
                    }
                }
        }
    }

}
