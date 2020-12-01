package com.kls.robcommodity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.DetailActivity;
import com.kls.robcommodity.model.Categories;
import com.kls.robcommodity.model.CategoryModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;

    private ArrayList<Categories> categoryModels;

    {
        this.categoryModels = new ArrayList<>();
    }

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Categories> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<Categories> categoryModels) {
        this.categoryModels.clear();
        this.categoryModels.addAll(categoryModels);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories categories  = categoryModels.get(position);
        holder.txt_cat.setText(categories.getCategory());
        if (categories.getLogo() != null){
            Glide.with(context)
                    .load(categories.getLogo())
                    .into(holder.img_cat);
        }

       // @TODO: kalo mau ngasih background di categorynya, unremark aja
       // just info,, mending dikompress dlu gambarnya,, jadi berat banget diandroid aja

//        if (categories.getImage() != null) {
//            Glide.with(context)
//                    .load(categories.getImage())
//                    .into(new CustomTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            holder.llCategory.setBackground(resource);
//                        }
//
//                        @Override
//                        public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                        }
//                    });
//        }

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("ID", categories.getId());
                i.putExtra("NAME", categories.getCategory());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtCat)
        TextView txt_cat;
        @BindView(R.id.imgCat)
        ImageView img_cat;
        @BindView(R.id.llCategory)
        LinearLayout llCategory;

        View mRootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
