package com.kls.robcommodity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.DetailItemActivtiy;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.TransactionItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailTransactionAdapter extends RecyclerView.Adapter<ItemDetailTransactionAdapter.ViewHolder> {
    private List<TransactionItem> transactionItems;
    private Context context;

    {
        this.transactionItems = new ArrayList<>();
    }

    public ItemDetailTransactionAdapter(Context context) {
        this.context = context;
    }

    public List<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(List<TransactionItem> transactionItems) {
        this.transactionItems.clear();
        this.transactionItems.addAll(transactionItems);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionItem transactionItem = this.transactionItems.get(position);

        Glide.with(context)
                .load(transactionItem.getProduct().getThumbnailList().getUrlImage())
                .into(holder.imgTsItem);

        holder.txtTitleTsItem.setText(transactionItem.getProduct().getTitle());
        holder.txtPriceTsItem.setText(Helper.formatToDollarCurrency(Helper.rupiahToDollar(transactionItem.getProduct().getDiscountPrice())));
        holder.txtQuantityTsItem.setText("Quantity : " + transactionItem.getQty());
    }

    @Override
    public int getItemCount() {
        return this.transactionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgTsItem)
        public ImageView imgTsItem;
        @BindView(R.id.txtTitleTsItem)
        public TextView txtTitleTsItem;
        @BindView(R.id.txtPriceTsItem)
        public TextView txtPriceTsItem;
        @BindView(R.id.txtQuantityTsItem)
        public TextView txtQuantityTsItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailItemActivtiy.class);
                    i.putExtra("ID", transactionItems.get(getAdapterPosition()).getProductId());

                    context.startActivity(i);
                }
            });
        }
    }
}
