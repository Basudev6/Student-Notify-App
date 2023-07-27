package com.example.studentnotifyapp.Notification;

import static com.example.studentnotifyapp.Notification.Constants.BASE_URL;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiUtilities {
    private static Retrofit retrofit = null;

    public static ApiInterface getClient()
    {
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
