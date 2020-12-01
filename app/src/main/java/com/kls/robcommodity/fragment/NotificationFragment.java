    package com.kls.robcommodity.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kls.robcommodity.R;
import com.kls.robcommodity.adapter.HistoryOrderTransactionAdapter;
import com.kls.robcommodity.model.HistoryOrderResponse;
import com.kls.robcommodity.viewmodel.HistoryOrderViewModel;
import com.kls.robcommodity.viewmodel.TransactionViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.security.PublicKey;

public class NotificationFragment extends Fragment {
    @BindView(R.id.rvTransaction)
    public RecyclerView rvTransaction;

    @BindView(R.id.emptyView)
    public LinearLayout llEmptyView;

    private SweetAlertDialog pDialog;
    private HistoryOrderTransactionAdapter historyOrderTransactionAdapter;
    private TransactionViewModel transactionViewModel;

    public NotificationFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        historyOrderTransactionAdapter = new HistoryOrderTransactionAdapter(getActivity());
        rvTransaction.setAdapter(historyOrderTransactionAdapter);
        historyOrderTransactionAdapter.notifyDataSetChanged();

        loadData();

        rvTransaction.setHasFixedSize(true);

        return view;
    }

    private void loadData() {
        transactionViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TransactionViewModel.class);

        transactionViewModel.getUserTransaction().observe(this, new Observer<HistoryOrderResponse>() {
            @Override
            public void onChanged(HistoryOrderResponse historyOrderResponse) {
                if (historyOrderResponse != null){
                    if (historyOrderResponse.getData() != null && !historyOrderResponse.getData().isEmpty()){
                        historyOrderTransactionAdapter.setHistoryOrderModels(historyOrderResponse.getData());
                        historyOrderTransactionAdapter.notifyDataSetChanged();
                        llEmptyView.setVisibility(View.GONE);
                    }else {
                        llEmptyView.setVisibility(View.VISIBLE);
                    }
                }else {
                    llEmptyView.setVisibility(View.VISIBLE);
                }

                showLoading(false);

            }
        });

        transactionViewModel.setTransactionData();
    }

    private void showLoading(boolean state) {
        if (state){
            pDialog.show();
        }else {
            pDialog.dismiss();
        }
    }
}