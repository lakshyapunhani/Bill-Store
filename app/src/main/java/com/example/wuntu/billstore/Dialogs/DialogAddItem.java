package com.example.wuntu.billstore.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 16-Feb-18.
 */

public class DialogAddItem extends Dialog
{

    @BindView(R.id.unitSpinner)
    Spinner unitSpinner;

    @BindView(R.id.spinner_gst_rate) Spinner spinner_gst_rate;

    ArrayAdapter<String> adapter;

    private Context mcontext = getContext();

    private String[] units = {"Select Unit","Kg"};
    ArrayList<String> arrayList;

    public DialogAddItem(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_item);
        ButterKnife.bind(this);

        adapter = new ArrayAdapter<String>(mcontext,
                android.R.layout.simple_spinner_item, units);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
        unitSpinner.setOnItemSelectedListener(null);

        spinner_gst_rate.setAdapter(adapter);


    }
}
