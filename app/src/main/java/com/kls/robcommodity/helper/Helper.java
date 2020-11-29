package com.kls.robcommodity.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.kls.robcommodity.utils.SharedPreferenceKey;
import com.kls.robcommodity.utils.SharedPreferenceManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Helper {

    public static SweetAlertDialog pDialog;


    public static String formatToDollarCurrency(Double total){
        NumberFormat nm = NumberFormat.getCurrencyInstance(Locale.US);
        nm.setMaximumFractionDigits(2);

        return nm.format(total);
    }

    public static Double rupiahToDollar(Double rupiah){

        Long oneDollarInRP = SharedPreferenceManager.getInstance().getValue(SharedPreferenceKey.IDR, Long.class, 0L);

        Double dollarConverted = (rupiah / oneDollarInRP.doubleValue());
        return dollarConverted;
    }

}
