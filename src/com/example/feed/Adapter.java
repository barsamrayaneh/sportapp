package com.example.feed;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter  extends BaseAdapter
{
	
    private Context mContext;
    Cursor cursor;
    public Adapter(Context context,Cursor cur)
    {
    	
            super();
            mContext=context;
            cursor=cur;
            
          
    }
      
    public int getCount()
    {
        // return the number of records in cursor
        return cursor.getCount();
    }

    // getView method is called for each item of ListView
    public View getView(int position,  View view, ViewGroup parent)
    {
    	
                    // inflate the layout for each item of listView
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.list_item, null);
           
                    // move the cursor to required position
                    cursor.moveToPosition(position);
                   
                    // fetch the sender number and sms body from cursor
                    String title=cursor.getString(cursor.getColumnIndex("title"));
                    String lead=cursor.getString(cursor.getColumnIndex("lead"));
                    String body=cursor.getString(cursor.getColumnIndex("body"));
                    String image_uri=cursor.getString(cursor.getColumnIndex("image_uri"));
                  
                    // get the reference of textViews
                    TextView ttitle=(TextView)view.findViewById(R.id.title);
                    TextView tlead=(TextView)view.findViewById(R.id.lead);
                    TextView tbody=(TextView)view.findViewById(R.id.body);
                    TextView timage_uri=(TextView)view.findViewById(R.id.image_uri);
                    ImageView iv = (ImageView) view.findViewById(R.id.image);
                    Bitmap b = Save_Load_Image.loadBitmap(view.getContext(), image_uri);
                   
                    // Set the Sender number and smsBody to respective TextViews
                    ttitle.setText(title);
                    tlead.setText(lead);
                    tbody.setText(body);
                    timage_uri.setText(image_uri);
                    iv.setImageBitmap(b);
       
                    return view;
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    static class ViewHolder{
    	ImageView iv; 
    	TextView title,lead,body,image_uri;
    }
}

