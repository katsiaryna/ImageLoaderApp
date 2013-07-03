package com.example.imageloaderapp;

import java.util.ArrayList;
import java.util.List;

public abstract class Reader {
	public static final int MAX_IMG_DWLD_QUANTITY = 20;
	public static final int IMG_DWLD_QUANTITY = 4;
	
	public abstract void parsing();
	public abstract List<Card> getData();

    public List<Card> gatherData(List<String> imageUrls,List<String> authors,List<String>publishedDates) {
	    List<Card> data = new ArrayList<Card>();
    	for(int i = 0; i < imageUrls.size(); i++) {	
	      data.add(new Card(authors.get(i),publishedDates.get(i),imageUrls.get(i)));	
		}
    	return data;
	}
}
