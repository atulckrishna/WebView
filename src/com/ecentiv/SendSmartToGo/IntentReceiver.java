/*
Copyright 2009-2011 Urban Airship Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE URBAN AIRSHIP INC ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL URBAN AIRSHIP INC OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ecentiv.SendSmartToGo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class IntentReceiver extends BroadcastReceiver {

    private static final String logTag = "WebViewApp";	
	public static final String PREFS_MYNAME = "Notification";  
	public static String APID_UPDATED_ACTION_SUFFIX = ".apid.updated"; 
	SharedPreferences mynotification;
	Context ctx;    

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(logTag, "Received intent: " + intent.toString());
        String action = intent.getAction();
        ctx=context;
        mynotification = ctx.getSharedPreferences(PREFS_MYNAME, 0);
       // Intent myintent=new Intent("com.ecentiv.SendSmartToGo", "com.ecentiv.SendSmartToGo.WebViewActivity");
        /*Intent myintent = new Intent();
        myintent.setClassName("com.ecentiv.SendSmartToGo", "com.ecentiv.SendSmartToGo.WebViewActivity");
        myintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
       
        
        if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {
        	 Log.e("SendSmart", "IntentReceiver"+" ACTION_PUSH_RECEIVED");        	 
        	
        	
            int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);
            Log.i(logTag, "Received push notification. Alert: "+ intent.getStringExtra(PushManager.EXTRA_ALERT)+ " [NotificationID="+id+"]");
            logPushExtras(intent);         
            try {	
				if(!isApplicationSentToBackground(context)){
					Log.e("IntentReciver", "Is app forground=:  :::   "+"true");									 
					// mynotification.edit().putString("PushForgroud", "pushforgroud").commit();	
					 /*myintent.putExtra("IdStatus", "pushbackgroud");					 
					 ctx.startActivity(myintent);*/
					 Intent myintent = new Intent("com.ecentiv.SendSmartToGo");
				        ctx.sendBroadcast(myintent);
				}else {
					// ++SplashActivity.count;
					 updateAllWidgets();
					Log.e("IntentReciver", "Is app forground=:  :::   "+"false");					
					mynotification.edit().putString("PushBackgroud", "pushbackgroud").commit();						
					//myintent.putExtra("IdStatus", "pushbackgroud");
				}	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
            
         

        } else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
        	 Log.e("WebView", "IntentReceiver"+" ACTION_NOTIFICATION_OPENED");
             Log.i(logTag, "User clicked notification. Message: " + intent.getStringExtra(PushManager.EXTRA_ALERT));

             logPushExtras(intent);

            Intent launch = new Intent(Intent.ACTION_MAIN);         
            launch.setClass(UAirship.shared().getApplicationContext(), SplashActivity.class);
            launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            UAirship.shared().getApplicationContext().startActivity(launch);

        } else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
        	 Log.e("WebView", "IntentReceiver"+" ACTION_REGISTRATION_FINISHED");
            Log.i(logTag, "Registration complete. APID:" + intent.getStringExtra(PushManager.EXTRA_APID)
                    + ". Valid: " + intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false));

            // Notify any app-specific listeners
            Intent launch = new Intent(UAirship.getPackageName() + APID_UPDATED_ACTION_SUFFIX);
            UAirship.shared().getApplicationContext().sendBroadcast(launch);

        } else if (action.equals(PushManager.ACTION_GCM_DELETED_MESSAGES)) {
            Log.i(logTag, "The GCM service deleted "+intent.getStringExtra(PushManager.EXTRA_GCM_TOTAL_DELETED)+" messages.");
            Log.e("WebView", "IntentReceiver"+" ACTION_GCM_DELETED_MESSAGES");
        }

    }

    /**
     * Log the values sent in the payload's "extra" dictionary.
     * 
     * @param intent A PushManager.ACTION_NOTIFICATION_OPENED or ACTION_PUSH_RECEIVED intent.
     */
    private void logPushExtras(Intent intent) {
        Set<String> keys = intent.getExtras().keySet();
        for (String key : keys) {

            //ignore standard C2DM extra keys
            List<String> ignoredKeys = (List<String>)Arrays.asList(
                    "collapse_key",//c2dm collapse key
                    "from",//c2dm sender
                    PushManager.EXTRA_NOTIFICATION_ID,//int id of generated notification (ACTION_PUSH_RECEIVED only)
                    PushManager.EXTRA_PUSH_ID,//internal UA push id
                    PushManager.EXTRA_ALERT);//ignore alert
            if (ignoredKeys.contains(key)) {
                continue;
            }
            Log.i(logTag, "Push Notification Extra: ["+key+" : " + intent.getStringExtra(key) + "]");
        }     
    }
    
    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
          ComponentName topActivity = tasks.get(0).topActivity;
          if (!topActivity.getPackageName().equals(context.getPackageName())) {
            return true;
          }
        }
        return false;
      }   
    
    private void updateAllWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(ctx, MyWidgetProvider.class));
        if (appWidgetIds.length > 0) {
            new  MyWidgetProvider().onUpdate(ctx, appWidgetManager, appWidgetIds);
        }
    }
}
