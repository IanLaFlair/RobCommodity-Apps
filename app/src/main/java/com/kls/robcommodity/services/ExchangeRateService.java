package com.kls.robcommodity.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.ExchangeRate;
import com.kls.robcommodity.model.ExchangeRateResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;
import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExchangeRateService extends Service {

    private Timer timer;
    private int counter = 0;
    private boolean isFirstHit = true;

    public ExchangeRateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startTimer();
        System.out.println("START COMMAND SERVICE");

        return START_STICKY;
    }

    private void startTimer() {
        if (isFirstHit){
            callExchangeApi();
            isFirstHit = false;
        }

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                callExchangeApi();
            }
        };

        timer.schedule(timerTask, 300000, 300000);
    }

    private void callExchangeApi() {

        //call api exchangeRate
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.exchangeratesapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(Api.class)
                .getExchangeRate("USD")
                .enqueue(new Callback<ExchangeRateResponse>() {
                    @Override
                    public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                        if (response.body() != null){
                            Double currency = response.body().getExchangeRate().getIdr();
                            if (currency != null){
                                try(SharedPreferenceManager sp = SharedPreferenceManager.getInstance()) {
                                    sp.begin();
                                    sp.put(SharedPreferenceKey.IDR, currency.longValue());
                                    sp.commit();
                                }
                                System.out.println("Response IDR" + SharedPreferenceManager.get(SharedPreferenceKey.IDR, Long.class, 0L));
                            }else {
                                System.out.println("Exchange rate response null");
                            }
                        }else {
                            System.out.println("Response rate response null");
                        }
                    }

                    @Override
                    public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                        t.printStackTrace();
                        System.out.println("ONFAILURE HIT");
                    }
                });

//        retrofit.create(Api.class)
//                .getExchangeRate("USD")
//                .enqueue(new Callback<ExchangeRateResponse>() {
//                    @Override
//                    public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
//                        ExchangeRateResponse exchangeRateResponse = response.body();
//                        if (exchangeRateResponse != null){
//                            ExchangeRate exchangeRate = exchangeRateResponse.getExchangeRate();
//
//                            try(SharedPreferenceManager sp = SharedPreferenceManager.getInstance()) {
//                                sp.begin();
//                                sp.put(SharedPreferenceKey.IDR, exchangeRate.idr.longValue());
//                                sp.commit();
//                            }
//                            System.out.println("Response IDR" + SharedPreferenceManager.get(SharedPreferenceKey.IDR, Long.class, 0L));
//
//                        }else {
//                            System.out.println("Exchange rate response null");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
//                        t.printStackTrace();
//
//                    }
//                });
        System.out.println("DELAY ONN "+ counter++);

    }

    @Override
    public void onDestroy() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        System.out.println("ON DESTROY SERVICE");
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
