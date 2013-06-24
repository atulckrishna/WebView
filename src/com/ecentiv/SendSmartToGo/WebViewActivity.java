package com.ecentiv.SendSmartToGo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class WebViewActivity extends Activity {
	
	 String uadid,status;
	 WebView  webView;	
	 SharedPreferences mynotification;
	 
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

         setContentView(R.layout.webview);   
         mynotification = this.getSharedPreferences(IntentReceiver.PREFS_MYNAME, 0);
         try{
         Log.e("", mynotification.getString("PushForgroud", ""));
         }catch(Exception e){
        	 e.printStackTrace();
         }
         
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy); */
         
         final Intent intent = getIntent();
        uadid = intent.getStringExtra("AppId");
        status = intent.getStringExtra("IdStatus");
        
        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new MyWebViewClient()); 
        // webView.loadUrl("file:///android_asset/test.html");  
        // webView.loadUrl("http://192.168.1.65:90/NewTest.html");
        webView.loadUrl("http://launch.sendsmart.com/togo.html#TgConversationMessages");  
        

        BroadcastReceiver connectionReceiver = new BroadcastReceiver() {	@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
        	Log.e("SendSmart", "On WebActivity Notification forgroud");
        	 webView.loadUrl("javascript:window.conversationListUpdate()");	
		}
        };
        
        registerReceiver(connectionReceiver, new IntentFilter("com.ecentiv.SendSmartToGo"));
  }
    
    @Override
    public void onResume(){
        super.onResume();      
    }


 @Override
    public void onPause(){
        super.onPause();       
    }
  
 @Override
 	public boolean onCreateOptionsMenu(Menu menu) {
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.splash, menu);
     return true;
 	}
 
 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
     // Handle item selection	 
	  Intent intent= new Intent(this, ConditionActivity.class);
     switch (item.getItemId()) {
     
   
         case R.id.aboutus:             
             //webView.loadUrl("file:///android_asset/www/filename.html"); 
            /* intent.putExtra("Value", "aboutus");
             startActivity(intent);*/
             return true;
             
         case R.id.termsandconditions:
        	// Toast.makeText(this, "Terms and Conditions", Toast.LENGTH_LONG).show();
        	// webView.loadUrl("file:///android_asset/www/tremsofservice.html");
        	 intent.putExtra("Value", "termsandconditions");
        	 startActivity(intent);
             return true;
             
         case R.id.privacypolicy:
        	 //Toast.makeText(this, "Privacy Policy", Toast.LENGTH_LONG).show();
        	// webView.loadUrl("file:///android_asset/www/privacypolicy.html");
        	 intent.putExtra("Value", "privacypolicy");        	   
             startActivity(intent);
             return true;
             
         default:
        	 
             return super.onOptionsItemSelected(item);          
     }
 }
 
    private class MyWebViewClient extends WebViewClient {
	    
		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {	      
            view.loadUrl(url);
	        return true;
	    }
	    
	    @Override
	    public void onLoadResource(WebView view, String url){
	    	super.onLoadResource(view, url);	    	
	    }
	    
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon){
	    	super.onPageStarted(view, url, favicon);
	    }
	    
	    @Override
	    public void onPageFinished(WebView view, String url){	    	
	    	
	    	/*if(status.equals("appidavailable")){
	    		// webView.loadUrl("javascript:testFun("+"'"+str+"'"+")");	
	    		//  webView.loadUrl("javascript:MytestFun()");
	    		try{
				 Log.e("SendSmart", "On WebActivity AppId=:  "+uadid);   		
				 webView.loadUrl("javascript:window.uaAvailable("+"'"+uadid+"'"+")");
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
				}else if (status.equals("appidnotavailable")){		
					  Log.e("SendSmart", "On WebActivity AppId=:  "+uadid); 			
					  webView.loadUrl("javascript:window.uaUnavailable()");
					// webView.loadUrl("javascript:MytestFun()");	
				//}else if (status.equals("pushforgroud")){
				}else if ( mynotification.getString(PREFS_MYNAME, null).equals("pushforgroud")){
					 Log.e("SendSmart", "On WebActivity Notification forgroud");						
					 webView.loadUrl("javascript:window.conversationListUpdate()");				
				//}else if (status.equals("pushbackgroud")){
				}else if ( mynotification.getString(PREFS_MYNAME, null).equals("pushforgroud")){
					 Log.e("SendSmart", "On WebActivity Notification backgroud");
					 webView.loadUrl("javascript:window.conversationUpdate()");
				}else{
					Log.e("dfdssfsfsddf", "dsfffffffffffffff");
				}	*/
	    
	    	
	    	//if(status.equals("appidavailable") && mynotification.getString("PushForgroud", "").equals("pushforgroud")){
	    	/* if (status.equals("pushforgroud")){
	    			 Log.e("SendSmart", "On WebActivity Notification forgroud");						
					 webView.loadUrl("javascript:window.conversationListUpdate()");	
					 
					 Editor editor = mynotification.edit();
					 editor.clear();
					 editor.commit();
					 
	    		}else*/ if(status.equals("appidavailable") && mynotification.getString("PushBackgroud", "").equals("pushbackgroud")){
	    			// } else if (status.equals("pushbackgroud")){
					 Log.e("SendSmart", "On WebActivity Notification backgroud");
					 webView.loadUrl("javascript:window.conversationUpdate()");
					 
					 Editor editor = mynotification.edit();
					 editor.clear();
					 editor.commit();
					 
	    		}else if(status.equals("appidavailable")){	    			
						 Log.e("SendSmart", "On WebActivity AppId appidavailable =:  "+uadid);   		
						 webView.loadUrl("javascript:window.uaAvailable("+"'"+uadid+"'"+")");		    		
	    		}else if (status.equals("appidnotavailable")){		
					  Log.e("SendSmart", "On WebActivity AppId appidnotavailable =:  "+uadid); 			
					  webView.loadUrl("javascript:window.uaUnavailable()");
				}
        }
	}     
}



