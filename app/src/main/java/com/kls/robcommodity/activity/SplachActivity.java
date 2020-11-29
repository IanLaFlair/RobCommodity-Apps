package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kls.robcommodity.R;
import com.kls.robcommodity.services.ExchangeRateService;
import com.kls.robcommodity.utils.SharedPreferenceManager;

import butterknife.BindView;

public class SplachActivity extends AppCompatActivity {

    @BindView(R.id.img_icon)
    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        SharedPreferenceManager.getInstance().setSharedPreferences(this.getSharedPreferences(SharedPreferenceManager.NAME, Context.MODE_PRIVATE));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ExchangeRateService exchangeRateService = new ExchangeRateService();
        Intent serviceIntent = new Intent(this, exchangeRateService.getClass());

        if (!serviceIsRunning(exchangeRateService.getClass())){
            startService(serviceIntent);
        }

        YoYo.with(Techniques.FadeInUp)
                .duration(100)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        YoYo.with(Techniques.FadeInUp)
                                .duration(2000)
                                .playOn(findViewById(R.id.img_icon));
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .playOn(findViewById(R.id.img_icon));

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplachActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2500);
    }

    private boolean serviceIsRunning(Class<? extends ExchangeRateService> aClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (aClass.getName().equals(service.service.getClassName())) {
                System.out.println("SERVICE RUNNING "+ true);
                return true;
            }
        }
        System.out.println("SERVICE RUNNING "+ false);
        return false;
    }
}
