package com.example.myswipe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class ImagePage {

    public View getImage(ImageView imageView) {

        //ImageView imageView = (ImageView) activity.findViewById(R.id.imageView);

        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setVisibility(View.VISIBLE);

        return imageView;
    }

    Bitmap bmp;
    ImageView imageView;

    public View getImage2(ImageView iv, final String url) {

        this.imageView = iv;
        //storage
        /*

ImageView image = (ImageView) findViewById(R.id.test_image);
Bitmap bMap = BitmapFactory.decodeFile("/sdcard/test.png");
image.setImageBitmap(bMap);

         */
        //imageView = (ImageView) activity.findViewById(R.id.imageView);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(url).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    imageView.setImageBitmap(bmp);

                imageView.setScaleType(ImageView.ScaleType.CENTER);
            }

        }.execute();

        return imageView;
    }
}
