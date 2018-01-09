package com.example.wuntu.billstore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillStoreFragment extends Fragment {


    public BillStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill_store, container, false);

        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick(R.id.btn_addNewBill)
    public void addNewBillClick()
    {
        AddNewBillFragment addNewBillFragment = new AddNewBillFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,addNewBillFragment).addToBackStack(null).commit();
    }

}
