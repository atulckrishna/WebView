package com.ecentiv.SendSmartToGo;
import com.ecentiv.SendSmartToGo.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by Jacek Milewski
 * looksok.wordpress.com
 */

public class MyWidgetProvider extends AppWidgetProvider {	
	
	 public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	        final int N = appWidgetIds.length;	      
	        Log.e("MyWidgetProvide", "In onUpdate() function");
	        // Perform this loop procedure for each App Widget that belongs to this provider
	           for (int i=0; i<N; i++) {
	            int appWidgetId = appWidgetIds[i];

	            // Create an Intent to launch ExampleActivity
	            Intent intent = new Intent(context, SplashActivity.class);
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	            
	            // Get the layout for the App Widget and attach an on-click listener
	            // to the button
	            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
	            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);	            
	            /*   views.setTextViewText(R.id.widget_text, ""+SplashActivity.count);
	           if(SplashActivity.count>0){
	            views.setViewVisibility(R.id.widget_text, View.VISIBLE);
	            }else{
	            	  views.setViewVisibility(R.id.widget_text, View.GONE);
	            }*/
	            //views.setT
	            // Tell the AppWidgetManager to perform an update on the current app widget
	            appWidgetManager.updateAppWidget(appWidgetId, views);	        
	            pushWidgetUpdate(context, views);
	        }
	    }

	public static PendingIntent buildButtonPendingIntent(Context context) {
		Intent intent = new Intent();
	    intent.setAction("com.ecentiv.SendSmartToGo.intent.action.CHANGE_PICTURE");	    
	    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		 Log.e("MyWidgetProvide", "In onUpdate() function");
		ComponentName myWidget = new ComponentName(context, MyWidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);
	    }
	
}
