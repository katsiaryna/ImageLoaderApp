package com.example.imageloaderapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class XMLReader extends Reader {
	
	 
	 public static final String LOG_TAG = "myLog";
	 
	 private String  mStringForParsing;
	 private List<String> mImagesUrls = new ArrayList<String>();
	 private List<String> mAuthors = new ArrayList<String>();
	 private List<String> mPublishedDates = new ArrayList<String>();
	 
     
	 public XMLReader(InputStream stream) {
    	 
    	 String stringForParsing = "";
    	 BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	      String line;
	      try {
			while ((line = reader.readLine()) != null) {
			     stringForParsing += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	      try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    this.mStringForParsing = stringForParsing;
	 }
	  
	 public void parsing() {
		int counter = 0;
		try {
			XmlPullParser xmlPullParser = prepareXpp();
			int event = xmlPullParser.next();
			while (event != XmlPullParser.END_DOCUMENT) {
				if (event != XmlPullParser.END_TAG) {
					event = xmlPullParser.next();
					continue;
				}
				
				String tag = xmlPullParser.getName();
				if (tag.equals("content")) {
					while (counter < IMG_DWLD_QUANTITY) {
						event = xmlPullParser.next();
						if (event == XmlPullParser.START_TAG) {
							
							if ((xmlPullParser.getName()).equals("name")) {
						
								if (xmlPullParser.next() == XmlPullParser.TEXT){
									  String author = xmlPullParser.getText();
									  mAuthors.add(author);
									  break;  
								}
							}	
						}	
					}
				}
				if (tag.equals("id")) {
					while (counter < IMG_DWLD_QUANTITY) {
						event = xmlPullParser.next();
						if (event == XmlPullParser.START_TAG) {
							
							if ((xmlPullParser.getName()).equals("published")) {
						
								if (xmlPullParser.next() == XmlPullParser.TEXT){
									  String publishedDate = xmlPullParser.getText();
									  mPublishedDates.add(publishedDate);
									  break;  
								}
							}	
						}	
					}
				}
				if (tag.equals("author")) {
					while (counter<IMG_DWLD_QUANTITY) {
						event = xmlPullParser.next();
						if (event == XmlPullParser.START_TAG) {
							
							if ((xmlPullParser.getName()).equals("link")) {
								String url = xmlPullParser.getAttributeValue(2);
								mImagesUrls.add(url);
								counter++;
								break;
							}	
						}
					}
				}
					
				event = xmlPullParser.next();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
	 private XmlPullParser prepareXpp() throws XmlPullParserException {
	
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(mStringForParsing));
			
			return xmlPullParser;
		}
	
	 public List<Card> getData() {
		parsing();	
		return gatherData(mImagesUrls, mAuthors, mPublishedDates);
	 }
	
}
