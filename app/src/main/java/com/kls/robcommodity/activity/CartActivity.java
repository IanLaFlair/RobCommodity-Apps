package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.kls.robcommodity.R;
import com.kls.robcommodity.model.BuyNowModel;
import com.kls.robcommodity.model.CartItemResponse;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_cart);

        BuyNowModel buyNowModel = getIntent().getParcelableExtra("buyNow");

        if (buyNowModel != null){
            Bundle bnd = new Bundle();
            bnd.putParcelable("bundle" ,buyNowModel);

            navController.getGraph().setStartDestination(R.id.shipment);

            NavOptions.Builder builder = new NavOptions.Builder();
            builder.setPopUpTo(R.id.cart_list, true);

            navController.navigate(R.id.shipment, bnd, builder.build());
        }

    }
}