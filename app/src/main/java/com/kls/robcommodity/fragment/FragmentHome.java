package com.kls.robcommodity.fragment;

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

import com.kls.robcommodity.R;
import com.kls.robcommodity.activity.HomeActivity;
import com.kls.robcommodity.adapter.CategoryAdapter;
import com.kls.robcommodity.adapter.HotItemAdapter;
import com.kls.robcommodity.model.CategoryModel;
import com.kls.robcommodity.model.HotItemModel;
import com.kls.robcommodity.viewmodel.HotItemViewModel;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private ArrayList<CategoryModel> list = new ArrayList<>();
    private ArrayList<HotItemModel> listHot = new ArrayList<>();

    HotItemViewModel hotItemViewModel;
    HotItemAdapter hotItemAdapter;
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

        hotItemViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HotItemViewModel.class);
        hotItemViewModel.getHotItemModel().observe(getActivity(), new Observer<ArrayList<HotItemModel>>() {
            @Override
            public void onChanged(ArrayList<HotItemModel> hotItemModels) {
                if (hotItemModels != null) {
                    hotItemAdapter.setData(hotItemModels);
                    showLoading(false);
                }else {

                }
            }
        });
        hotItemViewModel.setHotItemModel();

        rv_cat.setHasFixedSize(true);

        list.addAll(getListCat());
        showRecyclerList();

        return view;


    }
    public ArrayList<CategoryModel> getListCat() {
        String[] dataName = getResources().getStringArray(R.array.data_name);
        TypedArray dataPhoto = getResources().obtainTypedArray(R.array.data_photo);
        ArrayList<CategoryModel> listCat = new ArrayList<>();
        for (int i = 0; i < dataName.length; i++) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setName(dataName[i]);
            categoryModel.setIcon(dataPhoto.getResourceId(i,0));
            listCat.add(categoryModel);
        }
        return listCat;
    }

    private void showRecyclerList(){
        rv_cat.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter listHeroAdapter = new CategoryAdapter(getActivity(),list);
        rv_cat.setAdapter(listHeroAdapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

}