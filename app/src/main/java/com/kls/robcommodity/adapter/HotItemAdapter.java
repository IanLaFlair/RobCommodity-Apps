package com.kls.robcommodity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.DetailActivity;
import com.kls.robcommodity.activity.DetailItemActivtiy;
import com.kls.robcommodity.model.CategoryModel;
import com.kls.robcommodity.model.HotItemModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotItemAdapter extends RecyclerView.Adapter<HotItemAdapter.ViewHolder>{

    private ArrayList<HotItemModel> mData = new ArrayList<>();
    Context context;

    public HotItemAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<HotItemModel> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hoti_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_namabarang)
        TextView txt_namabar;
        @BindView(R.id.txt_hargabarang)
        TextView txt_hargabar;
        @BindView(R.id.bg_barang)
        ConstraintLayout bg_barang;
        View mRootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.bind(this,itemView);
        }

        void bind(HotItemModel hotItemModel){
            txt_namabar.setText(hotItemModel.getTitle());
            txt_hargabar.setText("$"+hotItemModel.getPrice());
            Glide.with(context)
                    .load(hotItemModel.getThumbnailResponse().getImage())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            bg_barang.setBackground(resource);
                        }
                    });
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailItemActivtiy.class);
                    i.putExtra("ID",hotItemModel.getId());
                    context.startActivity(i);
                }
            });
        }
    }
}
