package com.example.imageloaderapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
@TargetApi(11)
public class MainActivity extends Activity implements OnItemLongClickListener {
	
	public static final String LOG_TAG = "myLog";
	public static final String EXTRA_KEY = "url";
	
	public static Bitmap DEFAULT_IMAGE;
	public static boolean CONFIG_CHANGED = false;
	public static CardAdapter cardAdapter;
	private static ArrayList<Card> Cards = new ArrayList<Card>();
	
	public final String FORMAT_XML = "xml";
    public final String FORMAT_JSON = "json";
    public final String REQUEST_STRING = "http://www.flickr.com/services/feeds/photos_public.gne?format=";
  
    private ListView mList;
	private String mFormatCurrent;
	private ActionBar mActionBar;
	
	private List<String> mImagesUrls;
    private List<Card> mNewCards;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeData();
        initializeWidgets();
        initializeActionBar();
       
        if (!CONFIG_CHANGED)
          interchangeAsync();
        CONFIG_CHANGED = true;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
      
    	super.onSaveInstanceState(outState);
    }
    
    private void initializeData() {
    	mFormatCurrent = FORMAT_XML;
    	mImagesUrls = new ArrayList<String>();
    	mNewCards = new ArrayList<Card>();
    }
    
    private void initializeWidgets() {
    	cardAdapter = getAdapter();   
        mList = (ListView) findViewById(R.id.lw_cards);
        mList.setAdapter(cardAdapter);
        mList.setOnItemLongClickListener(this); 
    }
    
    private CardAdapter getAdapter(){
    	DEFAULT_IMAGE = createBitmap();
    	return new CardAdapter(Cards, this);
    }
   
    @TargetApi(11)
	private void initializeActionBar() {
    	mActionBar = MainActivity.this.getActionBar();
        mActionBar.setCustomView(R.layout.action_bar); 
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			   interchangeAsync();	
			}
		});		
    }
    
    private void interchangeAsync()  {
   	   GettingDataTask task = new GettingDataTask();
       Log.d(LOG_TAG,Request(mFormatCurrent)); 
   	   task.execute(Request(mFormatCurrent));
    }
    
    private String Request(String format) {
    	return 
    	format.equals(FORMAT_XML)?(REQUEST_STRING+FORMAT_XML):(REQUEST_STRING+FORMAT_JSON);
    }    
    
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
       
        menu.findItem(R.id.menu_xml).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				mFormatCurrent = FORMAT_XML;
				if (!item.isChecked()) {
			        item.setChecked(true);
			        menu.findItem(R.id.menu_json).setChecked(false);
				}
				Toast.makeText(MainActivity.this,"Selected : XML mode ",Toast.LENGTH_SHORT).show();
				return false;
			}
		});
        
	    menu.findItem(R.id.menu_json).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
				
			public boolean onMenuItemClick(MenuItem item) {
				mFormatCurrent = FORMAT_JSON;
				if (!item.isChecked()) {
				    item.setChecked(true);
				    menu.findItem(R.id.menu_xml).setChecked(false);
				}
			    Toast.makeText(MainActivity.this,"Selected : JSON mode",Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		 
        return true;
    }

    class GettingDataTask extends AsyncTask<String, Void, Void> {
    	
    	@Override
    	protected Void doInBackground(String... urls) {
    
    		String urlAddress = urls[0];
    		try {
            	   HttpClient httpClient = new DefaultHttpClient();
            	   HttpPost   httpPost   = new HttpPost(urlAddress);
            	   HttpResponse response;
				   response = httpClient.execute(httpPost);
				   HttpEntity   entity   = response.getEntity();
            	   InputStream  stream   = entity.getContent();
            	   Reader reader = null;
            	   
            	   if (mFormatCurrent.equals(FORMAT_XML)) {
            		   reader = new XMLReader(stream);
            	   }
            	   else if (mFormatCurrent.equals(FORMAT_JSON)) {
            		   reader = new JSONReader(stream);
            	   }
            	   mNewCards = reader.getData();   
            	  
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	    
            	 return null;
    	}
    
    	@SuppressWarnings("unchecked")
		@Override
    	protected void onPostExecute(Void value) {
    		super.onPostExecute(value);
    		for (int i = 0 ; i < mNewCards.size() ; i++) 
				mImagesUrls.add(mNewCards.get(i).mImageUrl);
			
    		DownloadImagesTask task = new DownloadImagesTask();
    		task.execute(mImagesUrls);
    		mImagesUrls = new ArrayList<String>();	
       	}
    }
    
    public class DownloadImagesTask extends AsyncTask<List<String>, Void,ArrayList<Bitmap>> {

        List<String> urls = null;

        @Override
        protected ArrayList<Bitmap> doInBackground(List<String>... urls) {
            ArrayList<Bitmap> arrayListBitmaps = new ArrayList<Bitmap>();
        	this.urls = urls[0];
            for(int i =0 ; i< Reader.IMG_DWLD_QUANTITY; i++) {
              arrayListBitmaps.add(downloadImage(this.urls.get(i)));
            }
            return arrayListBitmaps;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
        	setAllWidgets(result);
        }

        private Bitmap downloadImage(String url) {

            Bitmap bitmap =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection connection = (HttpURLConnection)ulrn.openConnection();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                if (null != bitmap)
                    return bitmap;

                }catch(Exception e){}
            return bitmap;
        }
    }
    
    private void setAllWidgets(ArrayList<Bitmap> arrayListBitmaps) {
    	
    	for (int i = 0; i < arrayListBitmaps.size(); i++)
				mNewCards.get(i).addBitmap(arrayListBitmaps.get(i));
    	
        addCardsToList(mNewCards);
		mNewCards =new ArrayList<Card>();
    }
    
    static void addCardsToList(List<Card> newCards) {
    	for (Card newcard : newCards) {
    		Cards.add(0,newcard);
		}
    	cardAdapter.notifyDataSetChanged();
    }
    
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
    	Intent intent = new Intent(MainActivity.this, WebActivity.class);
		intent.putExtra(EXTRA_KEY, cardAdapter.getItem(position).mImageUrl);
		startActivity(intent);		
		return false;
	}
    
    private Bitmap createBitmap() {
    	return BitmapFactory.decodeResource(MainActivity.this.getResources(),
    			android.R.drawable.ic_menu_gallery);
    }
        
} 
