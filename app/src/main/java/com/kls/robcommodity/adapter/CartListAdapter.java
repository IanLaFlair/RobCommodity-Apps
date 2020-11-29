package com.kls.robcommodity.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kls.robcommodity.R;
import com.kls.robcommodity.fragment.CartListFragment;
import com.kls.robcommodity.fragment.ShipmentFragment;
import com.kls.robcommodity.helper.Helper;
import com.kls.robcommodity.model.CartItemModel;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder>  {

    private ArrayList<CartItemModel> mData = new ArrayList<>();
    private Context context;
    private Fragment fragment;
//    int[] isFirst = {0};


    public CartListAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void setData(ArrayList<CartItemModel> cartItems){
        mData.clear();
        mData.addAll(cartItems);
        notifyDataSetChanged();
    }

    public ArrayList<CartItemModel> getData(){
        return mData;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_cart_item)
        ImageView imgCartItem;
        @BindView(R.id.txt_price_cart_item)
        TextView txtPriceCartItem;
        @BindView(R.id.txt_title_cart_item)
        TextView txtCartItem;
        @BindView(R.id.txt_note_cart_item)
        EditText edtNoteCartItem;
        @BindView(R.id.qty_cart_item)
        NumberPicker numberPicker;
        @BindView(R.id.btn_delete)
        ImageButton btnDelete;
        @BindView(R.id.txt_qty_checkout)
        TextView txtCheckoutQuantity;

        private CountDownTimer countDownTimer;

        View mRootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mRootView = itemView;
            numberPicker.setMin(1);
        }

        public void bind(CartItemModel cartItemModel) {
            NumberFormat nm = NumberFormat.getCurrencyInstance(Locale.US);
            nm.setMaximumFractionDigits(0);

            if (fragment instanceof ShipmentFragment){
                edtNoteCartItem.setVisibility(View.VISIBLE);
                numberPicker.setVisibility(View.GONE);
                txtCheckoutQuantity.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
            }else {
                edtNoteCartItem.setVisibility(View.GONE);
                numberPicker.setVisibility(View.VISIBLE);
                txtCheckoutQuantity.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);

            }
            edtNoteCartItem.setImeOptions(EditorInfo.IME_ACTION_DONE);
            edtNoteCartItem.setSingleLine(true);
            txtCheckoutQuantity.setText("Order : " + String.valueOf(cartItemModel.getQuantity()));

            Glide.with(context)
                    .load(cartItemModel.getHotItemModel().getThumbnailResponse().getImage())
                    .into(imgCartItem);

            txtCartItem.setText(cartItemModel.getHotItemModel().getTitle());
            txtPriceCartItem.setText(Helper.formatToDollarCurrency(Helper.rupiahToDollar(cartItemModel.getHotItemModel().getPrice())));
            if (fragment instanceof CartListFragment){
                CartListFragment cr = (CartListFragment)fragment;
                if (cr.isFirst[0] == 0)
                numberPicker.setValue(cartItemModel.getQuantity());
            }
            numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    Log.d("VALUE NUMBER PICKER ", String.valueOf(value));
                    if (fragment instanceof CartListFragment){
//                        numberPicker.setValue(value);
                        CartListFragment cartListFragment1 = (CartListFragment) fragment;

                        cartListFragment1.isFirst[0] = 1;
                        cartListFragment1.changeQuantity(value, getAdapterPosition(), numberPicker);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragment instanceof CartListFragment) {
                        CartListFragment cartListFragment1 = (CartListFragment) fragment;
                        cartListFragment1.delete(getAdapterPosition());

                    }
                }
            });



            edtNoteCartItem.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (fragment instanceof ShipmentFragment){
                        ShipmentFragment shipmentFragment = (ShipmentFragment) fragment;
                        if (countDownTimer != null){
                            countDownTimer.cancel();
                        }

                        countDownTimer = new CountDownTimer(500, 500) {
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                shipmentFragment.addNote(cartItemModel.getId(), edtNoteCartItem.getText().toString());
                            }
                        };

                        countDownTimer.start();
                    }
                }
            });

            edtNoteCartItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                    if (i == EditorInfo.IME_ACTION_DONE){
                        edtNoteCartItem.clearFocus();
                    }

                    return false;
                }
            });
        }

    }

}
