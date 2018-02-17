package com.example.wuntu.billstore.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.wuntu.billstore.Dialogs.DialogAddItem;
import com.example.wuntu.billstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeBillFragment extends Fragment {

    /*@BindView(R.id.btn)
    Button btn;*/

    private Context mContext;
    Dialog dialog;

    DialogAddItem dialogAddItem;


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

        dialogAddItem = new DialogAddItem(mContext);

        dialogAddItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return view;
    }
/*
    @OnClick(R.id.btn)
    public void click()
    {
        dialogAddItem.show();
    }*/

}
