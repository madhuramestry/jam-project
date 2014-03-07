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
	
	private RadioGroup radioSleepGroup;
	private RadioButton radioSleepButton;
	private RadioGroup radioTrouble2Sleep;
	private RadioButton radioTrouble2SleepButton;
	private RadioGroup radioAppetiteGroup;
	private RadioButton radioAppetiteButton;
	
	private CheckBox checkPanickyButton;
	private CheckBox checkHopelessButton;
	private CheckBox checkRestlessButton;
	private CheckBox checkAngerButton;
	
	private CheckBox checkWorkButton;
	private CheckBox checkExerciseButton;
    private CheckBox checkTVButton;
    private CheckBox checkLeaveButton;
    
    private CheckBox checkHeadacheButton;
    private CheckBox checkTiredButton;
    private CheckBox checkSweatingButton;
    
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
			
			String root = Environment.getExternalStorageDirectory().toString();
	    	String fileName=root+ "/mhm_reality_mining/sms_log";
	    	
	    	PrintWriter writer = new PrintWriter(fileName);
	    	writer.print("");
	    	writer.close();
			
			while (managedCursor.moveToNext()) { 
				
				String phNumber = managedCursor.getString(number); 
				long callDate = managedCursor.getLong(date); 
				String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(callDate);
			
			
				
				String smsLog="\""+phNumber+"\",\""+dateString+"\"\n";
				
				
		        	
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
		
		  	radioSleepGroup = (RadioGroup) findViewById(R.id.radioSleep);
	        radioAppetiteGroup = (RadioGroup) findViewById(R.id.radioAppetite);
	        radioTrouble2Sleep = (RadioGroup) findViewById(R.id.radio2sleep);
	        
	        checkPanickyButton=(CheckBox) findViewById(R.id.checkPanicky);
	        checkHopelessButton=(CheckBox) findViewById(R.id.checkHopeless);
	        checkRestlessButton=(CheckBox) findViewById(R.id.checkRestless);
	        checkAngerButton=(CheckBox) findViewById(R.id.checkAnger);
	        
	        checkWorkButton=(CheckBox) findViewById(R.id.checkActivityWork);
	        checkExerciseButton=(CheckBox) findViewById(R.id.checkActivityExercise);
	        checkTVButton=(CheckBox) findViewById(R.id.checkActivityTV);
	        checkLeaveButton=(CheckBox) findViewById(R.id.checkActivityLeave);
	        
	        checkHeadacheButton=(CheckBox) findViewById(R.id.checkSickHeadache);
	        checkTiredButton=(CheckBox) findViewById(R.id.checkSickTiredness);
	        checkSweatingButton=(CheckBox) findViewById(R.id.checkSickSweating); 
	   
		
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
		        	

		            startService(new Intent(getBaseContext(), Service_class.class));
		            
		            // get selected radio button from radioGroup
	                int selectedSleepId = radioSleepGroup.getCheckedRadioButtonId();
	                int selectedAppetiteId = radioAppetiteGroup.getCheckedRadioButtonId();
	                int selectedTrouble2SleepId = radioTrouble2Sleep.getCheckedRadioButtonId();
	                
	             // find the radiobutton by returned id
	                radioSleepButton = (RadioButton) findViewById(selectedSleepId);
	                radioAppetiteButton = (RadioButton) findViewById(selectedAppetiteId);
	                radioTrouble2SleepButton = (RadioButton) findViewById(selectedTrouble2SleepId);
	                
	                String panicky=checkPanickyButton.isChecked()?"Yes":"No";
	                String hopeless=checkHopelessButton.isChecked()?"Yes":"No";
	                String restless=checkRestlessButton.isChecked()?"Yes":"No";
	                String anger=checkAngerButton.isChecked()?"Yes":"No";
	                String mood="\""+panicky+"\",\""+hopeless+"\",\""+restless+"\",\""+anger+"\"";
	                
	                String work=checkWorkButton.isChecked()?"Yes":"No";
	                String exercise=checkExerciseButton.isChecked()?"Yes":"No";
	                String tv=checkTVButton.isChecked()?"Yes":"No";
	                String leave=checkLeaveButton.isChecked()?"Yes":"No";
	                String activity="\""+work+"\",\""+exercise+"\",\""+tv+"\",\""+leave+"\"";
	                
	                String headache=checkHeadacheButton.isChecked()?"Yes":"No";
	                String tired=checkTiredButton.isChecked()?"Yes":"No";
	                String sweating=checkSweatingButton.isChecked()?"Yes":"No";
	                String physical="\""+headache+"\",\""+tired+"\",\""+sweating+"\"";
	                
	                
	                
	                //Toast.makeText(MainActivity.this,
	                   //     radioSleepButton.getText(), Toast.LENGTH_SHORT).show();
	                
	               
	        	    
	                try {
	                	
	                	long date=Calendar.getInstance().getTimeInMillis();
	        	        String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	                	String root = Environment.getExternalStorageDirectory().toString();
	                	String fileName=root+ "/mhm_reality_mining/survey_data";
	                    FileWriter f = new FileWriter(fileName,true);
	                    f.append("\""+dateString+"\",\""+radioSleepButton.getText()+"\",\""+radioTrouble2SleepButton.getText()+"\",\""+radioAppetiteButton.getText()+"\","+mood+"\","+activity+","+physical+"\n");
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
