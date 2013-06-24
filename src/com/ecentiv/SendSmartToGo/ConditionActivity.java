package com.ecentiv.SendSmartToGo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ConditionActivity extends Activity {
	 WebView  webView;	
	 String value;
	 
   @SuppressLint("SetJavaScriptEnabled")
	@Override
   public void onCreate(Bundle savedInstanceState) {       
       super.onCreate(savedInstanceState);
       
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
       this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.conditionwebview);         
              
       /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy); */
        
        final Intent intent = getIntent();
        value= intent.getStringExtra("Value");
      
       
       webView = (WebView)findViewById(R.id.conditionwebView);
       webView.getSettings().setJavaScriptEnabled(true);
       webView.setWebChromeClient(new WebChromeClient());
       webView.setWebViewClient(new MyWebViewClient()); 
       // webView.loadUrl("file:///android_asset/test.html");  
       // webView.loadUrl("http://192.168.1.65:90/NewTest.html");
       if(value.equals("termsandconditions")){
       webView.loadUrl("file:///android_asset/www/tremsofservice.html");   
       }else if(value.equals("privacypolicy")){
    	   webView.loadUrl("file:///android_asset/www/privacypolicy.html");  
       }else{
    	   Toast.makeText(this, "AboutUs", Toast.LENGTH_LONG).show();
    	   webView.loadUrl("http://www.google.co.in/");  
       }
 }
   
   @Override
   public void onResume(){
       super.onResume();      
   }


@Override
   public void onPause(){
       super.onPause();       
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
	    	super.onPageFinished(view, url);      
	}  
   }
}



