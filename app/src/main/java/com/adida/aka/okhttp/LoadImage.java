package com.adida.aka.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadImage extends AppCompatActivity {

    ImageView imageView;
    Button btnLoadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        addControls();
        addEvents();
    }

    private void addControls() {
        imageView = (ImageView) findViewById(R.id.imageView);
        btnLoadImage = (Button) findViewById(R.id.buttonLoadImage);
    }

    private void addEvents() {
        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LOAD_IMAGE().execute("http://songhanhphuc.net/sites/default/files/hinh-anh-phong-canh-thien-nhien-dep-va-lang-man-o-da-lat-2015-24.jpg");
            }
        });

    }

    private class  LOAD_IMAGE extends AsyncTask<String, Void, byte[]>{
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        @Override
        protected byte[] doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);

            Request request = builder.build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return  response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if (bytes.length > 0){
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
            super.onPostExecute(bytes);
        }
    }




}
