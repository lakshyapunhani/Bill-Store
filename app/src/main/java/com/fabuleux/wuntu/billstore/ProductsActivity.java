package com.fabuleux.wuntu.billstore;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.productsList)
    RecyclerView productsList;

    @BindView(R.id.emptyProductLayout)
    LinearLayout emptyProductLayout;

    @BindView(R.id.btn_addProduct)
    FloatingActionButton addProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_addProduct)
    public void addProduct()
    {

        final Dialog dialog=new Dialog(this,R.style.ThemeWithCorners);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view1=layoutInflater.inflate(R.layout.dialog_add_product,null);
        final EditText edt_expenseAmount = (EditText) view1.findViewById(R.id.edt_expenseAmount);
        final EditText edt_expenseDescription = (EditText) view1.findViewById(R.id.edt_expenseDescription);
        final TextView expenseDate = (TextView) view1.findViewById(R.id.expenseDate);
        Button btn_createExpense = (Button) view1.findViewById(R.id.btn_createExpense);

        dialog.setContentView(view1);
        dialog.show();
    }
}
