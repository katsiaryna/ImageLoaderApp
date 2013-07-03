package com.example.imageloaderapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

    private WebView mWebView;	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		initializeWebView();	
	}
	
	private void initializeWebView() {
		mWebView = (WebView) findViewById(R.id.wv_brawser);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl(getIntent().getExtras().getString("url")); 
	    mWebView.setWebViewClient(new webViewClient());
	}
	
	private class webViewClient extends WebViewClient {
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
