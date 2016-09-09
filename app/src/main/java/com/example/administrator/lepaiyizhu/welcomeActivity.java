package com.example.administrator.lepaiyizhu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.IOException;

public class welcomeActivity extends AppCompatActivity {
    Handler handler;
    String url="api.fengniao.com/app_ipad/ipad_index.php?appImei=000000000000000&osType=Android&osVersion=4.1.1";
    LruCacheUtils lruCacheUtils;
    ImageView imageView;
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
         lruCacheUtils=LruCacheUtils.getLruCacheUtils();
         imageView= (ImageView) findViewById(R.id.imageId);

        handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
         String json=DownLoadUtils.getJsonString(url);
                Gson gson=new Gson();
                Bean bean=gson.fromJson(json,Bean.class);
                String imageurl=bean.getAndroid().getPic_480_854();
                try {
                     bitmap= lruCacheUtils.loadBitmapFromHttp(imageurl,100,100);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         imageView.setImageBitmap(bitmap);
                         Intent intent=new Intent("android.intent.action.MYMAIN");
                         startActivity(intent);
                         finish();
                     }
                 });



            }
        }).start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
