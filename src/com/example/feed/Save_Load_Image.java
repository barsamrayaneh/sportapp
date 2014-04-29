package com.example.feed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Save_Load_Image {
	public static void saveFile(Context context, Bitmap b, String picName){ 
	    FileOutputStream fos; 
	    try { 
	        fos = context.openFileOutput(picName, Context.MODE_PRIVATE); 
	        b.compress(Bitmap.CompressFormat.PNG, 100, fos); 
	        fos.close(); 
	    }  
	    catch (FileNotFoundException e) { 
	        Log.d("not found", "file not found"); 
	        e.printStackTrace(); 
	    }  
	    catch (IOException e) { 
	        Log.d("save error", "io exception"); 
	        e.printStackTrace(); 
	    } 

	}
	////////////////////////////////////////////////////
	public static Bitmap loadBitmap(Context context, String picName){ 
	    Bitmap b = null; 
	    FileInputStream fis; 
	    try { 
	        fis = context.openFileInput(picName); 
	        b = BitmapFactory.decodeStream(fis); 
	        fis.close(); 

	    }  
	    catch (FileNotFoundException e) { 
	        Log.d("file not found", "file not found"); 
	        e.printStackTrace(); 
	    }  
	    catch (IOException e) { 
	        Log.d("load error", "io exception"); 
	        e.printStackTrace(); 
	    }  
	    return b; 
	} 
}
