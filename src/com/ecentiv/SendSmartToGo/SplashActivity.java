package com.ecentiv.SendSmartToGo;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.analytics.InstrumentedActivity;
import com.urbanairship.location.UALocationManager;
import com.urbanairship.push.PushManager;

public class SplashActivity extends InstrumentedActivity { 

    IntentFilter boundServiceFilter;
    IntentFilter apidUpdateFilter;   
   // static int count=0;
    Context context;
    final String PREFS_NAME = "MyPrefsFile";  
    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        settings = getSharedPreferences(PREFS_NAME, 0);
        
        context=this; 
       /* if(count>0){
        	count=0;
        	updateAllWidgets();        	
        }*/
        
        if(isInternetOn()){      	
        
        boundServiceFilter = new IntentFilter();
        boundServiceFilter.addAction(UALocationManager.getLocationIntentAction(UALocationManager.ACTION_SUFFIX_LOCATION_SERVICE_BOUND));
        boundServiceFilter.addAction(UALocationManager.getLocationIntentAction(UALocationManager.ACTION_SUFFIX_LOCATION_SERVICE_UNBOUND));

        apidUpdateFilter = new IntentFilter();
        apidUpdateFilter.addAction(UAirship.getPackageName()+IntentReceiver.APID_UPDATED_ACTION_SUFFIX);
        }else{
        	//Toast.makeText(this, "Please check internet access", Toast.LENGTH_LONG).show();
        	 MyDialog();
        } 
       
    }
    
    @Override
    public void onStart() {
    	super.onStart();  	
    	
    	 Handler handler = new Handler();
         handler.postDelayed(new Runnable(){
            public void run() {  
            	
            	 if(isInternetOn()){
            		 updateApidField();            		 
                 }            	                 
            }
         }, 3000); 
    }

    @Override
    public void onResume() {
        super.onResume();        
        if(isInternetOn()){
        	 // OPTIONAL! The following block of code removes all notifications from the status bar.
        	try{
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancelAll();       

            registerReceiver(boundServiceReceiver, boundServiceFilter);
            registerReceiver(apidUpdateReceiver, apidUpdateFilter);
           // updateApidField();
        	}catch(Exception e){
        		Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        	}
            }else{
            	//Toast.makeText(this, "Please check internet access ", Toast.LENGTH_LONG).show();
            	 MyDialog();
            }
       
    }
   
    
  /*  private void updateAllWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MyWidgetProvider.class));
        if (appWidgetIds.length > 0) {
            new  MyWidgetProvider().onUpdate(this, appWidgetManager, appWidgetIds);
        }
    }*/
    
    @Override
    public void onPause() {
        super.onPause();        
        try {
            unregisterReceiver(boundServiceReceiver);
            unregisterReceiver(apidUpdateReceiver);
        } catch (IllegalArgumentException e) {
            Logger.error(e.getMessage());
        }
    }

    private BroadcastReceiver boundServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UALocationManager.getLocationIntentAction(UALocationManager.ACTION_SUFFIX_LOCATION_SERVICE_BOUND).equals(intent.getAction())) {
               // locationButton.setEnabled(true);
            } else {
               // locationButton.setEnabled(false);
            }
        }
    };

    private BroadcastReceiver apidUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	SplashActivity.this.updateApidField();
        }
    };

    private void updateApidField() {
        String apidString = PushManager.shared().getAPID();
        try{
        if (!PushManager.shared().getPreferences().isPushEnabled() || apidString == null) {
        	if (settings.getBoolean("my_first_time", true)) {
        	    settings.edit().putBoolean("my_first_time", false).commit();       	   
        	    updateApidField();
        	}
        	Log.e("PushManager.shared().getPreferences().isPushEnabled()", ""+PushManager.shared().getPreferences().isPushEnabled());
        	apidString = "";        	
        }    
        if (!(apidString.equals(null)||apidString.trim().equals(""))) {
        	Intent intent=new Intent(this,WebViewActivity.class);
        	intent.putExtra("AppId", apidString); 
        	intent.putExtra("IdStatus", "appidavailable");
        	startActivity(intent);  
        	finish();
        }else if(((apidString.equals(null)||apidString.trim().equals("")))){
        	Intent intent=new Intent(this,WebViewActivity.class);  
        	intent.putExtra("IdStatus", "appidnotavailable");
        	startActivity(intent);
        	finish();
        }      
       }catch(Exception e){
        	//e.printStackTrace();
        	updateApidField();
        }
    }
    
    
    public final boolean isInternetOn()
    {
      ConnectivityManager connec = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);

      // ARE WE CONNECTED TO THE NET
      if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
           connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED )
      {
        // MESSAGE TO SCREEN FOR TESTING (IF REQ)
        //Toast.makeText(this, connectionType + ” connected”, Toast.LENGTH_SHORT).show();
        return true;
      }
      else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
        ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  )
      {
        return false;
      }

      return false;
    }
    
   /* public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm.getActiveNetworkInfo().isConnectedOrConnecting()) {

                URL url = new URL("http://www.google.com.pk/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }    
    */
    public void MyDialog(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Please check your internet connection.")
           .setCancelable(false)
           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    //do things
            		finish();
               }
           });
    builder.show();
    
    }
}