package com.codechefamit.min_sumandroidclient;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "com.codechefamit.min";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnJoin=(Button)findViewById(R.id.btnJoin);
        Button btnHost=(Button)findViewById(R.id.btnHost);
        TextView hostIP=(TextView)findViewById(R.id.hostIP);
        final EditText txtPlayers=(EditText)findViewById(R.id.txtPlayers);
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPlayers.getText().toString()!=null){
                    int pl=Integer.parseInt(txtPlayers.getText().toString());
                    Toast.makeText(MainActivity.this,"Server Started",Toast.LENGTH_LONG).show();
                    new Thread(new GameServer(pl)).start();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Enter Players",Toast.LENGTH_LONG).show();
                }
            }
        });
        final EditText txtName=(EditText)findViewById(R.id.txtName);
        final EditText txtIP=(EditText)findViewById(R.id.txtIP);
        txtName.requestFocus();
        btnJoin.setEnabled(false);
        if(isNetworkAvailable()){
            btnJoin.setEnabled(true);
            btnHost.setEnabled(true);
            hostIP.setText(getLocalIpAddress());
        }
        else
        {
            btnJoin.setEnabled(false);
            btnHost.setEnabled(false);
            hostIP.setText("Not Connected");
        }
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtIP.getText().toString()!=null && txtName.getText().toString()!=null) {
                    Intent it = new Intent(MainActivity.this, MinimumSum.class);
                    it.putExtra("ip", txtIP.getText().toString());
                    it.putExtra("name", txtName.getText().toString());
                    startActivity(it);
                }else{
                    Toast.makeText(MainActivity.this,"Enter IP/Name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private static String getLocalIpAddress() {
        try {
            for (final Enumeration<NetworkInterface> enumerationNetworkInterface = NetworkInterface.getNetworkInterfaces(); enumerationNetworkInterface.hasMoreElements();) {
                final NetworkInterface networkInterface = enumerationNetworkInterface.nextElement();
                for (Enumeration<InetAddress> enumerationInetAddress = networkInterface.getInetAddresses(); enumerationInetAddress.hasMoreElements();) {
                    final InetAddress inetAddress = enumerationInetAddress.nextElement();
                    final String ipAddress = inetAddress.getHostAddress();
                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return ipAddress;
                    }
                }
            }
            return null;
        }
        catch (final Exception e) {
            return null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }





}
