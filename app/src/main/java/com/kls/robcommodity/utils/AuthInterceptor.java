package com.kls.robcommodity.utils;

import android.util.Log;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Authorization", "Bearer wqMpKxroIwT4RvcxldXZGluiwTR6vN");

        //  @TODO : Unremark code dibawah terus remark code yang token hardcode diatas
//        String token = SharedPreferenceManager.get(SharedPreferenceKey.TOKEN, String.class, "");
//        if (token != null && !token.equals("")) {
//            builder.addHeader("Authorization", "Bearer " + token);
//            Log.d("TOKEN", token);
//        }else {
//            builder.addHeader("Authorization", "Bearer wqMpKxroIwT4RvcxldXZGluiwTR6vN");
//        }

        return chain.proceed(builder.build());
    }
}