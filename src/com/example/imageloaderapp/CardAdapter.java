package com.example.imageloaderapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CardAdapter extends BaseAdapter {

	private ArrayList<Card> mItems;
	private final Context mContext;
	private final LayoutInflater mInflater;
	
	public CardAdapter(ArrayList<Card> items,  Context context) {
		this.mContext = context;
		this.mItems = items;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);				
	}	
	 
	public View getView(int position, View convertView, ViewGroup parent) {
	   ViewHolder holder = null;
	   
	   if (convertView == null) {
		   convertView = mInflater.inflate(R.layout.list_item, parent, false);
		   TextView author = (TextView) convertView.findViewById(R.id.tv_author);
		   TextView publichedDate = (TextView) convertView.findViewById(R.id.tv_publishedDate);
		   ImageView image = (ImageView) convertView.findViewById(R.id.iv_image);
		   holder = new ViewHolder(author,publichedDate,image);
		   convertView.setTag(holder);
	   }
	   else {
		   holder = (ViewHolder) convertView.getTag();			  
	   }
	   
	   Card card = getItem(position);
	   holder.author.setText("Author : "+card.mAuthor);
	   holder.publishedDate.setText(dateFormater(card.mPublishedDate));
	   holder.image.setImageBitmap(card.mImage);
	   
	   return convertView;
	}
	
	public int getCount() {		
		return mItems.size();
	}

	public Card getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return mItems.get(position).hashCode();
	}

    private static final class ViewHolder {
    	public final TextView author;
    	public final ImageView image;
    	public final TextView publishedDate;
    	
    	public ViewHolder(TextView author,TextView publishrdDay,ImageView image) {
    		this.author = author;
    		this.publishedDate = publishrdDay;
    		this.image = image;
    	}
    }
    
    private String dateFormater(String publishedDate) {
    	String day = publishedDate.substring(8, 10);
    	if (day.subSequence(0, 1).equals("0")) {
    		day = day.subSequence(1, 2).toString();
    	}
    	String month = publishedDate.substring(5, 7);
    	String year = publishedDate.substring(0,4);
    	String time = publishedDate.substring(11,19);
    	return 
    		"Date: "+day + "-"+ month+"-"+year+" Time: "+time;
    }
}


