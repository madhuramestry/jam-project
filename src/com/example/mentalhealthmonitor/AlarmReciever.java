package com.example.mentalhealthmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver
{
         @Override
            public void onReceive(Context context, Intent intent)
            {
                    // TODO Auto-generated method stub
                
                    
                      // here you can start an activity or service depending on your need
                     // for ex you can start an activity to vibrate phone or to ring the phone   
                                     
                    Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
                    Intent i = new Intent();
                    i.setClassName("com.example.mentalhealthmonitor", "com.example.mentalhealthmonitor.MainActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
             }
      
}