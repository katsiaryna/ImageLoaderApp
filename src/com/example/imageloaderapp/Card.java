package com.example.imageloaderapp;

import android.graphics.Bitmap;

public class Card {
	
	public final Bitmap  DEFAULT_IMAGE = MainActivity.DEFAULT_IMAGE;

	//kate;
	
	
	
	public Bitmap mImage;
	public String mPublishedDate;
	public String mAuthor;
	public String mImageUrl;
	
	public Card( String mAuthor, String mPublishedDate,String mImageUrl) {
		this.mAuthor = mAuthor;
		this.mPublishedDate = mPublishedDate;
		this.mImageUrl = mImageUrl;
	}
	
	public void addBitmap(Bitmap bitmap) {
		this.mImage = bitmap==null?DEFAULT_IMAGE:bitmap;
	}
	
}
