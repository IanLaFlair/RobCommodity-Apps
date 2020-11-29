package com.kls.robcommodity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.CartListAdapter;
import com.kls.robcommodity.adapter.HistoryOrderTransactionAdapter;
import com.kls.robcommodity.model.HistoryOrderResponse;
import com.kls.robcommodity.viewmodel.HistoryOrderViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryOrderActivity extends AppCompatActivity {
    @BindView(R.id.rvHistoryOrder)
    public RecyclerView rvHistoryOrder;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private HistoryOrderTransactionAdapter historyOrderTransactionAdapter;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        ButterKnife.bind(this);

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        rvHistoryOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        historyOrderTransactionAdapter = new HistoryOrderTransactionAdapter(this);
        rvHistoryOrder.setAdapter(historyOrderTransactionAdapter);
        historyOrderTransactionAdapter.notifyDataSetChanged();

        loadData();

        rvHistoryOrder.setHasFixedSize(true);
    }

    private void loadData() {
        HistoryOrderViewModel historyOrderViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HistoryOrderViewModel.class);

        historyOrderViewModel.getHistoryOrderResponse().observe(this, new Observer<HistoryOrderResponse>() {
            @Override
            public void onChanged(HistoryOrderResponse historyOrderResponse) {
                if (historyOrderResponse.getData() != null && !historyOrderResponse.getData().isEmpty()){
                    historyOrderTransactionAdapter.setHistoryOrderModels(historyOrderResponse.getData());
                    historyOrderTransactionAdapter.notifyDataSetChanged();
                    showLoading(false);
                }
            }
        });

        historyOrderViewModel.setHistoryOrderData();
    }

    private void showLoading(boolean state) {
        if (state){
            this.pDialog.show();
        }else {
            this.pDialog.dismiss();
        }
    }
}