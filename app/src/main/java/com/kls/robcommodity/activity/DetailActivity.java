package com.kls.robcommodity.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.model.DetailItemResponse;
import com.kls.robcommodity.utils.Api;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.textView5)
    TextView txt_name;
    @BindView(R.id.txt_price)
    TextView txt_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String[] dataName = getResources().getStringArray(R.array.hot_name);
        String[] dataHarga = getResources().getStringArray(R.array.price);
        TypedArray dataPhoto = getResources().obtainTypedArray(R.array.data_gambar);
        Intent i = getIntent();
        Integer id = i.getIntExtra("IDBARANG",0);

        txt_name.setText(dataName[id]);
        txt_price.setText(dataHarga[id]);
        appBarLayout.setBackgroundResource(dataPhoto.getResourceId(id,0));

        Toast.makeText(this, "ID : "+id, Toast.LENGTH_SHORT).show();
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar.setTitle(" ");
    }


}