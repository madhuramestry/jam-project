package com.example.mentalhealthmonitor;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.TrafficStats;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Service_class extends Service {


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Updating", Toast.LENGTH_LONG).show();
        saveLocationData();
        saveNetworkData();
        saveTaskData();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Toast.makeText(this, "Application stopped", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    
    public void saveNetworkData(){
       	long total1=TrafficStats.getTotalTxBytes();
           long mobile1=TrafficStats.getMobileTxBytes();
           long total2= TrafficStats.getTotalRxBytes();
           long mobile2=TrafficStats.getMobileRxBytes();
           
           long date=Calendar.getInstance().getTimeInMillis();
           String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
          
           try {
           	String root = Environment.getExternalStorageDirectory().toString();
           	String fileName=root+ "/mhm_reality_mining/network_statistics";
               FileWriter f = new FileWriter(fileName,true);
               f.append("\""+dateString+"\","+total1+","+total2+","+mobile1+","+mobile2+"\n");
               f.close();
           
           } catch (Exception e) {
               e.printStackTrace();
        }
    }
           
           
           
           public void saveTaskData(){
      		 long date=Calendar.getInstance().getTimeInMillis();
              String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
              ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
      		List<RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
      		 String root = Environment.getExternalStorageDirectory().toString();
      		 String fname=root+"/mhm_reality_mining/application_usage";
      		 FileWriter b;
      		try {
      			b = new FileWriter(fname,true);
      		  for (int i = 0; i < tasks.size(); i++) 
      		    {
      		    	String myTasks=tasks.get(i).baseActivity.getPackageName();
      		    	int len=myTasks.length();
      		    	b.append("\""+dateString+"\",\""+myTasks+"\",\""+tasks.get(i).id+"\"\n");
      		    } 
      		  b.close();
      		} catch (IOException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
      	}
public void saveLocationData(){
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();
        
		Geocoder geocoder;
	    String bestProvider;
	    List<Address> user = null;
	    double lat;
	    double lng;

	    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

	     Criteria criteria = new Criteria();
	     bestProvider = lm.getBestProvider(criteria, false);
	     Location location = lm.getLastKnownLocation(bestProvider);
	     
	     
	     if (location == null){
	         return;
	      }else{
	        geocoder = new Geocoder(this);
	        try {
	        user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
	        lat=(double)user.get(0).getLatitude();
	        lng=(double)user.get(0).getLongitude();

	        String str =  "Device ID: "+device_id+"  Latitude: " +lat+"  Longitude: "+lng;
	        
	        long date=Calendar.getInstance().getTimeInMillis();
	        String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	      
	        try {
	        	String root = Environment.getExternalStorageDirectory().toString();
	        	String fileName=root+ "/mhm_reality_mining/location_log";
	            FileWriter f = new FileWriter(fileName,true);
	            f.append("\""+dateString+"\","+lat+","+lng+"\n");
	            f.close();
	           
	        } catch (Exception e) {
	            e.printStackTrace();
	     }
	        
	        }catch (Exception e) {
	                e.printStackTrace();
	        }
	        

	    }
	}


}