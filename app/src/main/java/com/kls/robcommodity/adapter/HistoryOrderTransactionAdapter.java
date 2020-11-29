package com.kls.robcommodity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.DetailItemActivtiy;
import com.kls.robcommodity.activity.DetailTransactionOrder;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.HistoryOrderModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryOrderTransactionAdapter extends RecyclerView.Adapter<HistoryOrderTransactionAdapter.ViewHolder> {
    private List<HistoryOrderModel> historyOrderModels;
    private Context context;

    {
        historyOrderModels = new ArrayList<>();
    }

    public HistoryOrderTransactionAdapter(Context context) {
        this.context = context;
    }

    public List<HistoryOrderModel> getHistoryOrderModels() {
        return this.historyOrderModels;
    }

    public void setHistoryOrderModels(List<HistoryOrderModel> historyOrderModels) {
        this.historyOrderModels.clear();
        this.historyOrderModels.addAll(historyOrderModels);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryOrderModel historyOrderModel = this.historyOrderModels.get(position);

        holder.txtOrderID.setText(historyOrderModel.getOrderId());
        holder.txtOrderDate.setText(historyOrderModel.getOrderDate());
        holder.txtOrderEmail.setText(historyOrderModel.getUser().getEmail());

        holder.txtAmountDollar.setText(Helper.formatToDollarCurrency(Helper.rupiahToDollar(Double.valueOf(historyOrderModel.getGrandTotal()))));

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.txtAmountIDR.setText(formatRupiah.format(Double.valueOf(historyOrderModel.getGrandTotal()).longValue()));


        switch (historyOrderModel.getStatus()){
            case "created":
                holder.llStatusOrder.setBackgroundColor(Color.BLACK);
                break;
            case "paid":
                holder.llStatusOrder.setBackgroundColor(Color.BLUE);
                break;
            case "approved":
                holder.llStatusOrder.setBackgroundColor(Color.YELLOW);
                break;
            case "delivered":
                holder.llStatusOrder.setBackgroundColor(context.getResources().getColor(R.color.aquamarine_primary));
                break;
            case "completed":
                holder.llStatusOrder.setBackgroundColor(Color.GREEN);
                break;
            case "cancelled":
                holder.llStatusOrder.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.historyOrderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtOrderID)
        public TextView txtOrderID;
        @BindView(R.id.txtOrderDate)
        public TextView txtOrderDate;
        @BindView(R.id.txtEmailOrder)
        public TextView txtOrderEmail;
        @BindView(R.id.txtAmountDollar)
        public TextView txtAmountDollar;
        @BindView(R.id.txtAmountIDR)
        public TextView txtAmountIDR;
        @BindView(R.id.llStatusOrder)
        public LinearLayout llStatusOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("transaction_id" + historyOrderModels.get(getAdapterPosition()).getId());

                    Intent i = new Intent(context, DetailTransactionOrder.class);
                    i.putExtra("transaction_id",historyOrderModels.get(getAdapterPosition()).getId());
                    context.startActivity(i);
                }
            });
        }
    }
}
