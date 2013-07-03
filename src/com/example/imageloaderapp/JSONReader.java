package com.example.imageloaderapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.util.JsonReader;
import android.util.JsonToken;

@TargetApi(11)
public class JSONReader extends Reader {

	public static final String  LOG_TAG = "myLog";
	private InputStream mInputStream;
	private JsonReader mReader ;
 	private JsonToken mToken;
 	 
 	private List<String> mImagesUrls = new ArrayList<String>();
	private List<String> mAuthors = new ArrayList<String>();
	private List<String> mPublishedDates = new ArrayList<String>();
	 
 	public JSONReader(InputStream inputStream) {
	   this.mInputStream = inputStream;
	}
 	 
	public List<Card> getData() {
		parsing();
		return gatherData(mImagesUrls, mAuthors, mPublishedDates);
	}

	@Override
	public void parsing() {
	   try {
	      getCardData();
	   } catch (Exception e) {
	        e.printStackTrace();
		 }	
	}
    
	public void getCardData() throws Exception {
    	mReader = new JsonReader(new InputStreamReader(mInputStream));
    	mReader.setLenient(true);
		mToken = mReader.peek();
		
		while ((mToken != JsonToken.END_DOCUMENT) && (mAuthors.size() < IMG_DWLD_QUANTITY)) {
			switch (mToken) {
			case BEGIN_OBJECT:
				mReader.beginObject();
				break;
			case END_OBJECT:
				mReader.endObject();
				break;
			case BEGIN_ARRAY:
				mReader.beginArray();
				break;
			case END_ARRAY:
				mReader.endArray();
				break;
			case NAME:
				addValues();
				continue;
			default:
				mReader.skipValue();
			}
			mToken = mReader.peek();
		}
		mInputStream.close();
	}
	
	private void addValues() throws Exception {
			
	    String tag = mReader.nextName();
	    
		if (tag.compareTo("author") == 0) {
		    addValueIn(mAuthors,"formated");
			return;
	    }
		if (tag.compareTo("published") == 0) {
		    addValueIn(mPublishedDates);
			return;
	    }
		if (tag.compareTo("m") == 0) {
		    addValueIn(mImagesUrls);
			return;
	    }
	    mToken = mReader.peek();
	}
	 
	private void addValueIn(List<String> listTagValues) throws IOException {
    	String value = "";
		mToken = mReader.peek() ; 
		if (mToken == JsonToken.STRING) {
			value = mReader.nextString();
			mToken = mReader.peek();
		} 
		listTagValues.add(value);
	 }
	private void addValueIn(List<String> listTagValues,String mode) throws IOException {
    	String value = "";
		mToken = mReader.peek() ; 
		if (mToken == JsonToken.STRING) {
			value = mReader.nextString();
			mToken = mReader.peek();
		} 
		listTagValues.add(aurhorNameFormater(value));
	 }
	
	private String aurhorNameFormater(String author) {
		int length = author.length();
		return author.substring(19,length-1);	
	}
}
