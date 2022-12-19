package com.example.androidappproject1;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadRepository {
    //The server url to which the image has to be uploaded
    public static final String IMAGE_UPLOAD_URL ="http://192.168.0.44:80" ;

    private static UploadRepository instance;

    private UploadService UploadService;

    public static UploadRepository getInstance() {
        if (instance == null) {
            instance = new UploadRepository();
        }
        return instance;
    }

    public UploadRepository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IMAGE_UPLOAD_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UploadService = retrofit.create(UploadService.class);
    }

    public UploadService getUploadService() {
        return UploadService;
    }
}
