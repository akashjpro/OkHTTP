package com.adida.aka.okhttp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostFile extends AppCompatActivity {

    ImageView imageView;
    Button btnPostFile;
    final int REQUEST_CODE_FOLDER = 123;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_file);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnPostFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new POST_FILE().execute("http://proakashj.esy.es/uploadFile.php");
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });
    }

    private void addControls() {
        imageView = (ImageView) findViewById(R.id.imageView);
        btnPostFile = (Button) findViewById(R.id.buttonPostFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            path = getRealPathFromURI(uri);
            //mo va doc duong dan
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    class POST_FILE extends AsyncTask<String, Void, String>{

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();

        @Override
        protected String doInBackground(String... params) {

            File file = new File(path);
            String content_type = getType(file.getPath());
            String file_path = file.getAbsolutePath();

            RequestBody file_body = RequestBody.create(MediaType.parse(content_type), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("upload_file",
                            file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                    .setType(MultipartBody.FORM)
                    .build();
            Request request = new Request.Builder()
                    .url(params[0])
                    .post(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(PostFile.this, s, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }

    //get duoi file
    private String getType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
