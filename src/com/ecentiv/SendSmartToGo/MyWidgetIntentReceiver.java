package com.ecentiv.SendSmartToGo;

import com.ecentiv.SendSmartToGo.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by Jacek Milewski
 * looksok.wordpress.com
 */

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("MyWidgetIntentReceiver", "We are in onReceive");		
		//if(intent.getAction().equals("pl.looksok.intent.action.CHANGE_PICTURE")){
		if(intent.getAction().equals("com.ecentiv.SendSmartToGo.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
		//remoteViews.setImageViewResource(R.id.widget_image, getImageToSet());
		
		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.widget_image, MyWidgetProvider.buildButtonPendingIntent(context));
		
		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

	/*private int getImageToSet() {
		clickCount++;
		return clickCount % 2 == 0 ? R.drawable.me : R.drawable.wordpress_icon;
	}*/
}
