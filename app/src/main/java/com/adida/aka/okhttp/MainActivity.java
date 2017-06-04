package com.adida.aka.okhttp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnGetUrl, btnLoadImage, btnPostData, btnPostFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {

        btnGetUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GetURL.class));
            }
        });

        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoadImage.class));
            }
        });

        btnPostData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostData.class));
            }
        });

        btnPostFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostFile.class));
            }
        });

    }

    private void addControls() {
        btnGetUrl    = (Button) findViewById(R.id.buttonGetUrl);
        btnLoadImage = (Button) findViewById(R.id.buttonLoadImage);
        btnPostData  = (Button) findViewById(R.id.buttonPostData);
        btnPostFile  = (Button) findViewById(R.id.buttonPostFile);
    }
}
