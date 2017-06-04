package com.adida.aka.okhttp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetURL extends AppCompatActivity {

    EditText edtURL;
    Button btnGetUrl;
    TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_url);
        addControl();
        addEvents();


    }


    private void addEvents() {
        btnGetUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new GET_URL().execute("http://"+edtURL.getText().toString().trim());
                    }
                });
            }
        });

    }

    private void addControl() {
        edtURL = (EditText) findViewById(R.id.editTextURL);
        btnGetUrl = (Button) findViewById(R.id.buttonGetUrl);
        txtContent = (TextView) findViewById(R.id.textViewContent);
    }

    class GET_URL extends AsyncTask<String, String, String>{
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();



        @Override
        protected String doInBackground(String... params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);

            Request request   = builder.build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return  response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.isEmpty()){
                txtContent.append(s);
            }else {
                Toast.makeText(GetURL.this, "Path fail!!!", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }
}
