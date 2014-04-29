package com.example.feed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ProgressDialog pDialog;	 
	Adapter adapter;
    // URL to get contacts JSON
    private static String url = "http://spita.ir/feed/index.php";
    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LEAD = "lead";
    private static final String TAG_BODY = "body";
    private static final String TAG_IMAGE_URI = "image_uri";
 
    // contacts JSONArray
    JSONArray contacts = null;
////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase db = openOrCreateDatabase("feed", MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM feed", null);
        
       adapter=new Adapter(this,c);
        
        
       ListView lv = (ListView) findViewById(R.id.list);
      lv.setAdapter(adapter);
      lv.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
		Intent i = new Intent(MainActivity.this,Detail.class);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView lead = (TextView) view.findViewById(R.id.lead);
		TextView body = (TextView) view.findViewById(R.id.body);
		TextView image_uri = (TextView) view.findViewById(R.id.image_uri);
		ImageView iv = (ImageView) view.findViewById(R.id.image);
		
		i.putExtra("title", title.getText().toString());
		i.putExtra("lead", lead.getText().toString());
		i.putExtra("body", body.getText().toString());
		i.putExtra("image_uri", image_uri.getText().toString());
		startActivity(i);
		}
	});
        
        
 
        
    }
///////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////

   
    
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.main, menu);
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.refresh)
		{
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni == null) {
		         
				AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
		        alertDialog.setTitle("خطا");
		        alertDialog.setMessage("اتصال با اینترنت برقرار نیست");
		        alertDialog.setCancelable(false);
		        alertDialog.setButton("دوباره سعی کن", new DialogInterface.OnClickListener() { 
		           public void onClick(DialogInterface dialog, int which) {		        	   
		            	   Intent ii = getIntent();
		            	 finish();
		              startActivity(ii);		              
		         }
		       });
		        alertDialog.show(); 
	        }

		}
		// Calling async task to get json
        
        class GetContacts extends AsyncTask<Void, Void, Void> {
        	 
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("لطفا کمی صبر نمایید ...");
                pDialog.setCancelable(false);
                pDialog.show();
     
            }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

            @Override
            protected Void doInBackground(Void... arg0) {
                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();
     
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
     
                //Log.d("Response: ", "> " + jsonStr);
                if (jsonStr != null) {
                    try {
                        //JSONObject jsonObj = new JSONObject(jsonStr.toString());
                         
                        // Getting JSON Array node
                       // contacts = jsonObj.getJSONArray(jsonStr);
                    	contacts = new JSONArray(jsonStr.toString());
     
                        // looping through All Contacts
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            
                            String id = c.getString(TAG_ID);
                            String title = c.getString(TAG_TITLE);
                            title="'"+title+"'";
                            String lead = c.getString(TAG_LEAD);
                            lead="'"+lead+"'";
                            String body = c.getString(TAG_BODY);
                            body="'"+body+"'";
                            String image_uri =c.getString(TAG_IMAGE_URI);
                            String image_uri_web = "http://spita.ir/feed/image/"+image_uri;
                            String picName = image_uri;
                            image_uri="'"+image_uri+"'";
                            SQLiteDatabase db = openOrCreateDatabase("feed", MODE_PRIVATE, null);
                            
                            
                            
                            final ImageLoader imgLoader = new ImageLoader(MainActivity.this);
                            
                            Bitmap b = imgLoader.getBitmap(image_uri_web,200);
                           Save_Load_Image.saveFile(MainActivity.this, b, picName);
                            db.execSQL("INSERT INTO feed VALUES ("+id+","+title+","+lead+","+body+","+image_uri+","+0+");");
     
                            // tmp hashmap for single contact
                           // HashMap<String, String> contact = new HashMap<String, String>();
     
                            // adding each child node to HashMap key => value
                           // contact.put(TAG_TITLE, title);
                           // contact.put(TAG_LEAD, lead);
                            //contact.put(TAG_BODY, body);
                           // contact.put(TAG_IMAGE_URI, image_uri);
     
                            // adding contact to contact list
                           // contactList.add(contact);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
     
                return null;
            }
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
               pDialog.dismiss();
                
            }
        }
        new GetContacts().execute();
		return super.onOptionsItemSelected(item);
	}
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////
    }

