package com.example.androidappproject1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadImageActivity extends AppCompatActivity {

    File tempImgFilePath;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        //Creating a spinner to show a dropdown for selecting an image category
        Spinner imageCategorySpinner = (Spinner) findViewById(R.id.imgCategorySpinner);

        // Creating an ArrayAdapter using the image category string array defined in the string file
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.image_categories, android.R.layout.simple_spinner_item);

        // Setting the layout of the dropdown to simple spinner dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //applying the adapter to the image category spinner
        imageCategorySpinner.setAdapter(adapter);

        // getting the intent that triggered the upload image activity page, and getting the
        // captured image from the intent
        Intent uploadImgIntent = getIntent();
        String imagePath = uploadImgIntent.getStringExtra("IMAGE PATH");

        //converting the image to bitmap
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bitmapOptions);

        ImageView capturedImgDisplayWidget = findViewById(R.id.capturedImg);
        capturedImgDisplayWidget.setImageBitmap(bitmap);

        tempImgFilePath = new File(imagePath);

        // creating a text view to show status of the upload. Setting it to invisible initially.
        TextView uploadImgStatus =  findViewById(R.id.uploadImgStatus);
        uploadImgStatus.setVisibility(View.INVISIBLE);
    }

    public void uploadImageToServer(View v){
        uploadImageThroughPOST();
    }

    public void uploadImageThroughPOST(){

        RequestBody imageReqFile = RequestBody.create(MediaType.parse("multipart/form-data"), tempImgFilePath);
        MultipartBody.Part imgMultipartBody = MultipartBody.Part.createFormData("image", tempImgFilePath.getName(), imageReqFile);
        Spinner imageCategorySpinner = (Spinner) findViewById(R.id.imgCategorySpinner);
        String imgCategory = imageCategorySpinner.getSelectedItem().toString();
        RequestBody imgCategoryStrBody = RequestBody.create(MediaType.parse("text/plain"), imgCategory);

        UploadRepository uploadRepository = UploadRepository.getInstance();
        uploadRepository.getUploadService().uploadFile(imgCategoryStrBody, imgMultipartBody).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> r) {
                Log.d("onResponse", String.valueOf(r));
                //Toast.makeText(getApplicationContext(), "Successfully uploaded the image", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this);
                builder.setMessage(Html.fromHtml("<font>Successfully uploaded the image.</font>"))
                        .setTitle("Success")
                        .setCancelable(false);

                builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                    finish();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                //Toast.makeText(getApplicationContext(), "Failed to upload the image. Please try again.", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this);
                builder.setMessage(Html.fromHtml("<font color='#BB2124'>Failed to upload the image. Please try again.</font>"))
                        .setTitle("Failed")
                        .setCancelable(false);
                builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                    finish();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}


