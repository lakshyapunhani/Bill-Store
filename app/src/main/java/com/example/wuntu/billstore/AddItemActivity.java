package com.example.wuntu.billstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 16-Feb-18.
 */

public class AddItemActivity extends AppCompatActivity
{

    @BindView(R.id.unitSpinner)
    Spinner unitSpinner;

    @BindView(R.id.spinner_gst_rate) Spinner spinner_gst_rate;

    @BindView(R.id.edt_itemName)
    EditText edt_itemName;

    @BindView(R.id.edt_costPerItem) EditText edt_costPerItem;

    @BindView(R.id.edt_quantity) EditText edt_quantity;

    @BindView(R.id.layout_gst)
    LinearLayout layout_gst;

    @BindView(R.id.cb_include_gst)
    CheckBox cb_include_gst;

    @BindView(R.id.edt_gstAmount) EditText edt_gstAmount;

    @BindView(R.id.edt_costPerItemGST) EditText edt_costPerItemGST;

    @BindView(R.id.edt_totalAmount) EditText edt_totalAmount;

    @BindView(R.id.btn_add)
    Button btn_add;

    ArrayAdapter<String> adapter;

    int cost_item,quantity,gst_amount,cost_itemGst,totalAmount;


    private String[] units = {"Select Unit","Kg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, units);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
        unitSpinner.setOnItemSelectedListener(null);

        spinner_gst_rate.setAdapter(adapter);

        edt_costPerItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                if (edt_quantity.getText().toString().isEmpty())
                {
                    edt_totalAmount.setText(edt_costPerItem.getText().toString().trim());
                }
                else
                {
                    int qty = Integer.parseInt(edt_quantity.getText().toString().trim());
                    int costItem = Integer.parseInt(edt_costPerItem.getText().toString().trim());
                    edt_totalAmount.setText("" + costItem * qty);
                }

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                if (!edt_costPerItem.getText().toString().isEmpty())
                {
                    int qty = Integer.parseInt(edt_quantity.getText().toString().trim());
                    int costItem = Integer.parseInt(edt_costPerItem.getText().toString().trim());
                    edt_totalAmount.setText("" + costItem * qty);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void showGstLayout()
    {
        layout_gst.setVisibility(View.VISIBLE);
    }

    private void hideGstLayout()
    {
        layout_gst.setVisibility(View.GONE);
    }

    @OnClick(R.id.cb_include_gst)
    public void gstLayoutClick()
    {
        if (cb_include_gst.isChecked())
        {
            showGstLayout();
        }
        else if (!cb_include_gst.isChecked())
        {
            hideGstLayout();
        }
    }

    @OnClick(R.id.back_arrow)
    public void backClick()
    {
        finish();
    }

    @OnClick(R.id.btn_add)
    public void addClick()
    {
        if (edt_itemName.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edt_costPerItem.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter cost per item", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edt_quantity.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please provide quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cb_include_gst.isChecked())
        {
            if (edt_gstAmount.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "Please provide gst amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edt_costPerItemGST.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please fill total amount of item", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (edt_totalAmount.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter total amount", Toast.LENGTH_SHORT).show();
            return;
        }

    }

}
