package com.example.mentalhealthmonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private RadioGroup radioRelax;
	private RadioButton radioRelaxButton;
	
	private RadioGroup radioSad;
	private RadioButton radioSadButton;
	
	private RadioGroup radioMotivation;
	private RadioButton radioMotivationButton;
	
	private RadioGroup radioTouchy;
	private RadioButton radioTouchyButton;
	
	private RadioGroup radioPanic;
	private RadioButton radioPanicButton;
	
	private RadioGroup radioWorthless;
	private RadioButton radioWorthlessButton;
	
	private RadioGroup radioDry;
	private RadioButton radioDryButton;
	
	private RadioGroup radioBreath;
	private RadioButton radioBreathButton;
	
	private RadioGroup radioTremble;
	private RadioButton radioTrembleButton;
	
	private RadioGroup radioHeart;
	private RadioButton radioHeartButton;
	
	
	
    
    private Button btnDisplay;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/mhm_reality_mining");    
        myDir.mkdirs();
        addListenerOnButton();
		saveCallData();
		saveSmsData();
}
	
	public void saveSmsData(){
		
		try {
			
			Cursor managedCursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null); 
			int number = managedCursor.getColumnIndex("address"); 
			int date = managedCursor.getColumnIndex("date"); 
			int body=managedCursor.getColumnIndex("body");
			
			
			String root = Environment.getExternalStorageDirectory().toString();
	    	String fileName=root+ "/mhm_reality_mining/sms_log";
	    	
	    	PrintWriter writer = new PrintWriter(fileName);
	    	writer.print("");
	    	writer.close();
			
			while (managedCursor.moveToNext()) { 
				
				String phNumber = managedCursor.getString(number); 
				long callDate = managedCursor.getLong(date); 
				String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(callDate);
				int length=managedCursor.getString(body).length();
			
				
				String smsLog="\""+phNumber+"\",\""+dateString+"\",\""+length+"\"\n";
				
				
		        	
		            FileWriter f = new FileWriter(fileName,true);
		            f.append(smsLog);
		            f.close();
		        
		       
			}
				
			 } catch (Exception e) {
		            e.printStackTrace();
		     }
			
	}
	public void saveCallData(){
		try {
			
		Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null); 
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER); 
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE); 
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE); 
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		
		String root = Environment.getExternalStorageDirectory().toString();
    	String fileName=root+ "/mhm_reality_mining/call_log";
    	
    	PrintWriter writer = new PrintWriter(fileName);
    	writer.print("");
    	writer.close();
		
		while (managedCursor.moveToNext()) { 
			
			String phNumber = managedCursor.getString(number); 
			String callType = managedCursor.getString(type); 
			long callDate = managedCursor.getLong(date); 
			String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(callDate);
			String callDuration = managedCursor.getString(duration);
		
			//int dur=Integer.parseInt(callDuration);
			String dir = null; 
			int dircode = Integer.parseInt(callType); 
			switch (dircode) { 
			case CallLog.Calls.OUTGOING_TYPE: dir = "OUTGOING"; 
			break; 
			case CallLog.Calls.INCOMING_TYPE: dir = "INCOMING"; 
			break; 
			case CallLog.Calls.MISSED_TYPE: dir = "MISSED"; 
			break; }
			
			String callLog="\""+phNumber+"\",\""+dateString+"\",\""+dir+"\","+callDuration+"\n";
			
			
	        	
	            FileWriter f = new FileWriter(fileName,true);
	            f.append(callLog);
	            f.close();
	        
	       
		}
			
		 } catch (Exception e) {
	            e.printStackTrace();
	     }
	}
	
	
	public void addListenerOnButton(){
		
		  	radioRelax = (RadioGroup) findViewById(R.id.radioUpset);
		  	radioSad = (RadioGroup) findViewById(R.id.radioNegative);
		  	radioMotivation = (RadioGroup) findViewById(R.id.radioMotivation);
		  	radioTouchy = (RadioGroup) findViewById(R.id.radioO);
		  	radioPanic = (RadioGroup) findViewById(R.id.radioPanic);
		  	radioWorthless = (RadioGroup) findViewById(R.id.radioWorthless);
		  	radioDry = (RadioGroup) findViewById(R.id.radioDry);
		  	radioBreath = (RadioGroup) findViewById(R.id.radioBreath);
		  	radioTremble = (RadioGroup) findViewById(R.id.radioTrembling);
		  	radioHeart = (RadioGroup) findViewById(R.id.radioHeart);
	       
	        
	        
	   
		
		//Alarm manager to start service
		 Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.SECOND, 10);
		    Intent intent = new Intent(MainActivity.this, Service_class.class);
		    PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent,
		            0);
		    AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		            3600*1000, pintent);

		    // click listener for the button to start service
		    btnDisplay = (Button) findViewById(R.id.button1);
		    btnDisplay.setOnClickListener(new View.OnClickListener() {

		    	 
		        @Override
		        public void onClick(View v) {
		        	
		        	startService(new Intent(getApplicationContext(), LockService.class));
		            startService(new Intent(getBaseContext(), Service_class.class));
		            
		            // get selected radio button from radioGroup
	                int selectedRelaxId = radioRelax.getCheckedRadioButtonId();
	                int selectedSadId = radioSad.getCheckedRadioButtonId();
	                int selectedMotivationId = radioMotivation.getCheckedRadioButtonId();
	                int selectedTouchyId = radioTouchy.getCheckedRadioButtonId();
	                int selectedPanicId = radioPanic.getCheckedRadioButtonId();
	                int selectedWorthlessId = radioWorthless.getCheckedRadioButtonId();
	                int selectedDryId = radioDry.getCheckedRadioButtonId();
	                int selectedBreathId = radioBreath.getCheckedRadioButtonId();
	                int selectedTrembleId = radioTremble.getCheckedRadioButtonId();
	                int selectedHeartId = radioHeart.getCheckedRadioButtonId();
	                
	                
	             // find the radiobutton by returned id
	                radioRelaxButton = (RadioButton) findViewById(selectedRelaxId);
	                radioSadButton = (RadioButton) findViewById(selectedSadId);
	                radioMotivationButton = (RadioButton) findViewById(selectedMotivationId);
	                radioTouchyButton = (RadioButton) findViewById(selectedTouchyId);
	                radioPanicButton = (RadioButton) findViewById(selectedPanicId);
	                radioWorthlessButton = (RadioButton) findViewById(selectedWorthlessId);
	                radioDryButton = (RadioButton) findViewById(selectedDryId);
	                radioBreathButton = (RadioButton) findViewById(selectedBreathId);
	                radioTrembleButton = (RadioButton) findViewById(selectedTrembleId);
	                radioHeartButton = (RadioButton) findViewById(selectedHeartId);
	                
	               String everything=radioRelaxButton.getText()+","+radioSadButton.getText()+","+radioMotivationButton.getText()+","+radioTouchyButton.getText()+","+radioPanicButton.getText()+","+radioWorthlessButton.getText()+","+radioDryButton.getText()+","+radioBreathButton.getText()+","+radioTrembleButton.getText()+","+radioHeartButton.getText();
	                
	                
	                
	                //Toast.makeText(MainActivity.this,
	                   //     radioSleepButton.getText(), Toast.LENGTH_SHORT).show();
	                
	               
	        	    
	                try {
	                	
	                	long date=Calendar.getInstance().getTimeInMillis();
	        	        String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	                	String root = Environment.getExternalStorageDirectory().toString();
	                	String fileName=root+ "/mhm_reality_mining/survey_data";
	                    FileWriter f = new FileWriter(fileName,true);
	                    f.append("\""+dateString+"\","+everything+"\n");
	                    f.close();
	               
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	                
	                try{
						Intent disp= new Intent(MainActivity.this,Display.class);
						startActivity(disp);
						finish();
						} catch(Exception e){
							e.printStackTrace();	
						}
	                
	              }
		    }
		    
		    		);

		    // click listener for the button to stop service
		 /*   Button btnStop = (Button) findViewById(R.id.button2);
		    btnStop.setOnClickListener(new View.OnClickListener() {

		        @Override
		        public void onClick(View v) {
		            stopService(new Intent(getBaseContext(), Service_class.class));
		        }
		    });*/
		    
		    Toast.makeText(this, "Data stored", Toast.LENGTH_LONG).show();
		     
		    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
