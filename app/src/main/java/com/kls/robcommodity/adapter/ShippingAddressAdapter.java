package com.kls.robcommodity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.kls.robcommodity.R;
import com.kls.robcommodity.fragment.ChangeShippingFragment;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.BaseResponse;
import com.kls.robcommodity.model.ShippingAddressModel;
import com.kls.robcommodity.model.ShippingAddressResponse;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.utils.NetworkHandler;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ShippingAddressModel> shippingAddressModels = new ArrayList<>();
    private Fragment fragment;

    public ShippingAddressAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void setData(ArrayList<ShippingAddressModel> shippingAddressModels){
        this.shippingAddressModels.clear();
        this.shippingAddressModels.addAll(shippingAddressModels);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShippingAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShippingAddressAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shipping_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressAdapter.ViewHolder holder, int position) {
        holder.bind(shippingAddressModels.get(position));
    }

    @Override
    public int getItemCount() {
        return this.shippingAddressModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_recipient_name)
        TextView txtRecipientName;
        @BindView(R.id.txt_shipment_address)
        TextView txtShipmentAddress;
        @BindView(R.id.btnDeleteShipment)
        ImageButton btnDeleteShipment;
        @BindView(R.id.btnEditShipment)
        ImageButton btnEditShipment;

        private View rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rootView = itemView;
        }

        public void bind(ShippingAddressModel shippingAddressModel) {

            txtRecipientName.setText(shippingAddressModel.getRecipientName());
            if (shippingAddressModel.getSelected() == 1){
                txtRecipientName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_location_on_24, 0, 0, 0);
            }else {
                txtRecipientName.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_location_gray_on_24, 0, 0, 0);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(shippingAddressModel.getPhone())
                    .append("\n")
                    .append(shippingAddressModel.getAddress1())
                    .append("\n");

            if (shippingAddressModel.getAddress2() != null && !shippingAddressModel.getAddress2().equals("")){
                sb.append(shippingAddressModel.getAddress2())
                        .append("\n");
            }

            sb.append(shippingAddressModel.getCity())
                    .append(" - ")
                    .append(shippingAddressModel.getState())
                    .append("\n")
                    .append(shippingAddressModel.getCountry())
                    .append(" - ")
                    .append(shippingAddressModel.getPostalCode());

            txtShipmentAddress.setText(sb.toString());

            btnDeleteShipment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragment instanceof  ChangeShippingFragment){
                        ChangeShippingFragment changeShippingFragment = (ChangeShippingFragment) fragment;
                        changeShippingFragment.deleteShipment(getAdapterPosition());
                    }
                }
            });

            btnEditShipment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragment instanceof  ChangeShippingFragment){
                        ((ChangeShippingFragment) fragment).nextDetail(shippingAddressModel.getId());
                    }
                }
            });

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragment instanceof ChangeShippingFragment){
                        ChangeShippingFragment changeShippingFragment = (ChangeShippingFragment) fragment;
                        changeShippingFragment.itemOnClick(shippingAddressModel);
                    }
                }
            });

        }
    }
}
