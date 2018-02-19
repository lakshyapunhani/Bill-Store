package com.example.wuntu.billstore.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wuntu.billstore.Adapters.ProductAdapter;
import com.example.wuntu.billstore.AddItemActivity;
import com.example.wuntu.billstore.Pojos.ItemPojo;
import com.example.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeBillFragment extends Fragment {

    @BindView(R.id.btn_save)
    TextView btn_save;

    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    LinearLayoutManager mLayoutManager;

    ProductAdapter productAdapter;

    private Context mContext;

    private ArrayList<ItemPojo> itemList;

    public MakeBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_bill, container, false);
        ButterKnife.bind(this,view);

        itemList = new ArrayList<>();
        productAdapter = new ProductAdapter(itemList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_items.setLayoutManager(mLayoutManager);
        recycler_items.setItemAnimator(new DefaultItemAnimator());
        recycler_items.setAdapter(productAdapter);

        return view;
    }
    @OnClick(R.id.btn_save)
    public void click()
    {

    }

    @OnClick(R.id.layout_invoiced_items_header)
    public void headerClick()
    {
        Intent intent = new Intent(mContext,AddItemActivity.class);
        startActivity(intent);
    }

}
