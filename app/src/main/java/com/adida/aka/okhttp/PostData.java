package com.adida.aka.okhttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostData extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_data);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                if(user.isEmpty() | pass.isEmpty()){
                    return;
                }
                new POST_DATA(user, pass).execute("http://proakashj.esy.es/postData.php");
            }
        });
    }

    private void addControls() {
        edtUser = (EditText) findViewById(R.id.editTextUser);
        edtPass = (EditText) findViewById(R.id.editTextPass);

        btnPost = (Button) findViewById(R.id.buttonPost);
    }

    class POST_DATA extends AsyncTask<String, Void, String>{
        OkHttpClient okHttpClient =new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        String user, pass;

        public POST_DATA(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("user", user)
                    .addFormDataPart("pass", pass)
                    .setType(MultipartBody.FORM)
                    .build();

            Request request = new Request.Builder()
                    .url(params[0])
                    .post(requestBody)
                    .build();
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
            Toast.makeText(PostData.this, s, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }
}
