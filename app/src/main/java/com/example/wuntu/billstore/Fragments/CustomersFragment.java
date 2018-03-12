package com.example.wuntu.billstore.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuntu.billstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomersFragment extends Fragment {


    @BindView(R.id.customersList)
    RecyclerView customersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_customers, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

}
