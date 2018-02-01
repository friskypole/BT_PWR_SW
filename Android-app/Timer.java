package com.aput.one;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Timer extends AppCompatActivity {
    Button b_send;
    EditText t_val;
    BluetoothArduino Connection_Instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        b_send = (Button)findViewById(R.id.sendButton);
        t_val  = (EditText)findViewById(R.id.editText);
        Intent intent = getIntent();
        String Device_Name = intent.getStringExtra("Device_Name");
        Connection_Instance = BluetoothArduino.getInstance(Device_Name);
        if(!Connection_Instance.Connect()){
            Toast.makeText(getApplicationContext(), "Connection failed." ,Toast.LENGTH_LONG).show();
            startActivity( new Intent(this, Bluetooth.class));                              //If connection fails return to Main Activity
        }
    }

    public  void send_val(View v){                                                                      //read user input and send
        String t_str=t_val.getText().toString();
        int t_int=0;

        if (t_str.length()<8){                                                                          //check for valid string
            invalid();
            return;
        }
        int tmp=Character.getNumericValue(t_str.charAt(0));                                             //hour conversion
        if(!(tmp==0 || tmp==1)) {
            invalid();
            return;
        } else t_int=t_int+36000*tmp;
        tmp=Character.getNumericValue(t_str.charAt(1));
        if(!(tmp>=0 && tmp<=9)) {
            invalid();
            return;
        } else t_int=t_int+3600*tmp;

        tmp=Character.getNumericValue(t_str.charAt(3));                                                 //minute conversion
        if(!(tmp>=0 && tmp<=5)) {
            invalid();
            return;
        } else t_int=t_int+600*tmp;
        tmp=Character.getNumericValue(t_str.charAt(4));
        if(!(tmp>=0 && tmp<=9)) {
            invalid();
            return;
        } else t_int=t_int+60*tmp;

        tmp=Character.getNumericValue(t_str.charAt(6));                                                 //second conversion
        if(!(tmp>=0 && tmp<=5)) {
            invalid();
            return;
        } else t_int=t_int+10*tmp;
        tmp=Character.getNumericValue(t_str.charAt(7));
        if(!(tmp>=0 && tmp<=9)) {
            invalid();
            return;
        } else t_int=t_int+tmp;

        Toast.makeText(getApplicationContext(),String.valueOf(t_int),Toast.LENGTH_LONG).show();
        t_str=new StringBuilder(String.valueOf(t_int)).reverse().toString();                            //Stringify and reverse order of [# of seconds]
        t_str=t_str.concat("@");
        Connection_Instance.SendMessage(t_str);
    }

    public void invalid(){
        Toast.makeText(getApplicationContext(), "Invalid timer input" ,Toast.LENGTH_LONG).show();
    }

}
