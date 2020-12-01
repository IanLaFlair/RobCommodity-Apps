package com.kls.robcommodity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.DetailItemActivtiy;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryOrSearchAdapter extends RecyclerView.Adapter<CategoryOrSearchAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> products;

    {
        this.products = new ArrayList<>();
    }

    public CategoryOrSearchAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hoti_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = this.products.get(position);

        holder.txtProductName.setText(product.getTitle());
        holder.txtProductPrice.setText(Helper.formatToDollarCurrency(Helper.rupiahToDollar(product.getDiscountPrice())));

        if (product.getThumbnailList() != null){
            Glide.with(context)
                    .load(product.getThumbnailList().getUrlImage())
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.cslProductBackground.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_namabarang)
        public TextView txtProductName;
        @BindView(R.id.txt_hargabarang)
        public TextView txtProductPrice;
        @BindView(R.id.bg_barang)
        public ConstraintLayout cslProductBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailItemActivtiy.class);
                    intent.putExtra("ID", products.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
