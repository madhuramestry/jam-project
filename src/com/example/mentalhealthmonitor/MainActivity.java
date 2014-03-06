package com.example.mentalhealthmonitor;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    private RadioGroup radioAppetiteGroup;
    private RadioButton radioAppetiteButton;
    private CheckBox checkWorkButton;
    private CheckBox checkExerciseButton;
    private CheckBox checkTVButton;
    private CheckBox checkLeaveButton;
    private CheckBox checkHeadacheButton;
    private CheckBox checkPanicButton;
    private CheckBox checkTiredButton;
    private CheckBox checkRestlessButton;
    private CheckBox checkAngerButton;
    private Button btnDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/mhm_reality_mining");    
        myDir.mkdirs();
  
        File fileNet = new File (myDir,"network_statistics");
        File fileTask= new File (myDir,"application_usage");
        File fileCall= new File (myDir,"call_log");
        File fileLocation= new File (myDir,"location_log");
        File fileSurvey= new File (myDir,"survey_data");
      
        addListenerOnButton();
		saveCallData();
		
		
		
		
}
	
	
	public void saveCallData(){
		
		Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null); 
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER); 
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE); 
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE); 
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		
		
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
			
			try {
	        	String root = Environment.getExternalStorageDirectory().toString();
	        	String fileName=root+ "/mhm_reality_mining/call_log";
	            FileWriter f = new FileWriter(fileName,true);
	            f.append(callLog);
	            f.close();
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	     }
		}
	}
	
	
	public void addListenerOnButton(){
		
		  	radioSleepGroup = (RadioGroup) findViewById(R.id.radioSleep);
	        radioAppetiteGroup = (RadioGroup) findViewById(R.id.radioAppetite);
	        checkWorkButton=(CheckBox) findViewById(R.id.checkActivityWork);
	        checkExerciseButton=(CheckBox) findViewById(R.id.checkActivityExercise);
	        checkTVButton=(CheckBox) findViewById(R.id.checkActivityTV);
	        checkLeaveButton=(CheckBox) findViewById(R.id.checkActivityLeave);
	        
	        
	        checkHeadacheButton=(CheckBox) findViewById(R.id.checkSickHeadache);
	        checkPanicButton=(CheckBox) findViewById(R.id.checkSickPanic);
	        checkTiredButton=(CheckBox) findViewById(R.id.checkSickTired);
	        checkRestlessButton=(CheckBox) findViewById(R.id.checkSickRestless);
	        checkAngerButton=(CheckBox) findViewById(R.id.checkSickAnger);
	        
	   
		
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
	                
	                String work=checkWorkButton.isChecked()?"Yes":"No";
	                String exercise=checkExerciseButton.isChecked()?"Yes":"No";
	                String tv=checkTVButton.isChecked()?"Yes":"No";
	                String leave=checkLeaveButton.isChecked()?"Yes":"No";
	                String activity="\""+work+"\",\""+exercise+"\",\""+tv+"\",\""+leave+"\"";
	 
	                String headache=checkHeadacheButton.isChecked()?"Yes":"No";
	                String panic=checkPanicButton.isChecked()?"Yes":"No";
	                String tired=checkTiredButton.isChecked()?"Yes":"No";
	                String restless=checkRestlessButton.isChecked()?"Yes":"No";
	                String anger=checkAngerButton.isChecked()?"Yes":"No";
	                String mood="\""+headache+"\",\""+panic+"\",\""+tired+"\",\""+restless+"\",\""+anger+"\"";
	                
	                // find the radiobutton by returned id
	                radioSleepButton = (RadioButton) findViewById(selectedSleepId);
	                radioAppetiteButton = (RadioButton) findViewById(selectedAppetiteId);
	                
	                //Toast.makeText(MainActivity.this,
	                   //     radioSleepButton.getText(), Toast.LENGTH_SHORT).show();
	                
	               
	        	    
	                try {
	                	
	                	long date=Calendar.getInstance().getTimeInMillis();
	        	        String dateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
	                	String root = Environment.getExternalStorageDirectory().toString();
	                	String fileName=root+ "/mhm_reality_mining/survey_data";
	                    FileWriter f = new FileWriter(fileName,true);
	                    f.append("\""+dateString+"\",\""+radioSleepButton.getText()+"\",\""+radioAppetiteButton.getText()+"\","+activity+","+mood+"\n");
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
