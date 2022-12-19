package com.example.androidappproject1;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("/api/v1/upload")
    Call<ResponseBody> uploadFile(
            @Part("category") RequestBody category,
            @Part MultipartBody.Part file
    );
}
