package com.kls.robcommodity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.DetailActivity;
import com.kls.robcommodity.model.CategoryModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;

    private ArrayList<CategoryModel> categoryModels;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel categoryModel  = categoryModels.get(position);
        holder.txt_cat.setText(categoryModel.getName());
        holder.img_cat.setImageResource(categoryModel.getIcon());
        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, DetailActivity.class);
//                context.startActivity(i);
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
        View mRootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
