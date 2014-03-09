package com.example.mentalhealthmonitor;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
    	try{
    		
    		String root = Environment.getExternalStorageDirectory().toString();
    		String fileName=root+ "/mhm_reality_mining/screen_log";
    		long date=Calendar.getInstance().getTimeInMillis();
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
			FileWriter f = new FileWriter(fileName,true);
			
    		
    		
    		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
        	
    			
    			
    			f.append(dateString+",off\n");
    			f.close();
            
    			wasScreenOn = false;
    			Log.e("test","wasScreenOn"+wasScreenOn);
            
    		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	
    			
    			f.append(dateString+",on\n");
    			f.close();
            
    			wasScreenOn = true;
    			Log.e("test","wasScreenOn"+wasScreenOn);
            
    		}else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
        	
    			f.append(dateString+",unlocked\n");
    			f.close();
            

        }
        

    } catch (Exception e) {
        e.printStackTrace();
 }
    
    }
}