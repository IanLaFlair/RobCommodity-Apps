package com.kls.robcommodity.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.CartActivity;
import com.kls.robcommodity.activity.DetailActivity;
import com.kls.robcommodity.activity.HomeActivity;
import com.kls.robcommodity.adapter.CategoryAdapter;
import com.kls.robcommodity.adapter.HotItemAdapter;
import com.kls.robcommodity.model.Categories;
import com.kls.robcommodity.model.CategoryModel;
import com.kls.robcommodity.model.CategoryResponse;
import com.kls.robcommodity.model.HotItemModel;
import com.kls.robcommodity.utils.Api;
import com.kls.robcommodity.viewmodel.HotItemViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.recyclerView)
    RecyclerView rv_cat;
    @BindView(R.id.rv_hoti)
    RecyclerView rv_hoti;
    @BindView(R.id.btnCart)
    ImageButton btnCart;
    @BindView(R.id.edtSearchHome)
    EditText edtSearch;
    private ArrayList<Categories> list = new ArrayList<>();
    private ArrayList<HotItemModel> listHot = new ArrayList<>();

    HotItemViewModel hotItemViewModel;
    HotItemAdapter hotItemAdapter;
    CategoryAdapter listHeroAdapter;
    SweetAlertDialog pDialog;


    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu..");
        pDialog.setCancelable(true);
        showLoading(true);

        rv_hoti.setLayoutManager(new GridLayoutManager(getActivity(),2));
        hotItemAdapter = new HotItemAdapter(getActivity());
        rv_hoti.setAdapter(hotItemAdapter);
        hotItemAdapter.notifyDataSetChanged();

        rv_cat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listHeroAdapter = new CategoryAdapter(getActivity());
        rv_cat.setAdapter(listHeroAdapter);

        getListCat();

        hotItemViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HotItemViewModel.class);
        hotItemViewModel.getHotItemModel().observe(getActivity(), new Observer<ArrayList<HotItemModel>>() {
            @Override
            public void onChanged(ArrayList<HotItemModel> hotItemModels) {
                if (hotItemModels != null) {
                    hotItemAdapter.setData(hotItemModels);
                    showLoading(false);
                }else {
                    showLoading(false);
                }
            }
        });
        hotItemViewModel.setHotItemModel();

        rv_cat.setHasFixedSize(true);

        return view;


    }

    @OnClick(R.id.edtSearchHome)
    public void search() {
        startActivity(new Intent(getActivity(), DetailActivity.class));
    }

    public void getListCat() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(Api.class)
                .getCategories()
                .enqueue(new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        CategoryResponse categoryResponse = response.body();
                        if (categoryResponse != null){
                            if (categoryResponse.getCategories() != null && !categoryResponse.getCategories().isEmpty()){
                                listHeroAdapter.setCategoryModels(categoryResponse.getCategories());
                            }else {
                                Toast.makeText(getActivity(), "Css", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getActivity(), "category null", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void showLoading(Boolean state) {
        if (state) {
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.btnCart)
    public void toCart() {
        startActivity(new Intent(getActivity(), CartActivity.class));
    }

}