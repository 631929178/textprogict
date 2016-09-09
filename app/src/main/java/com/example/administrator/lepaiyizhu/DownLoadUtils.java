package com.example.administrator.lepaiyizhu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownLoadUtils {

	public static String getJsonString(String url) {
		String jString="";
		OkHttpClient client=new OkHttpClient();
		Request request=new Request.Builder().url(url).build();
		try {
			Response response=client.newCall(request).execute();
			jString=response.body().string();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("zsp", "����json----"+jString);
		return jString;
	}

	public static Bitmap getImageByteUrl(String url){
		URL address;
		Bitmap bitmap=null;
		try {
			address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) address.openConnection();
			connection.connect();
			if (connection.getResponseCode() == 200){
				bitmap = BitmapFactory.decodeStream(connection.getInputStream());

			}else{
				Log.d("LPYZ","connect fail");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
	public static byte[] getImageByte(String url) {
		byte[] b=null;
		OkHttpClient client=new OkHttpClient();
		Request request=new Request.Builder().url(url).build();
		try {
			Response response=client.newCall(request).execute();
			b=response.body().bytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("zsp", "����byte----"+b);
		return b;
	}
}
