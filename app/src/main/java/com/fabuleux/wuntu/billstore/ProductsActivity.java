package com.fabuleux.wuntu.billstore;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;
    CollectionReference productReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        productReference = db.collection("Users").document(firebaseUser.getUid()).collection("Products");
    }

    @OnClick(R.id.btn_addProduct)
    public void addProduct()
    {
        final Dialog dialog=new Dialog(this,R.style.ThemeWithCorners);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view1=layoutInflater.inflate(R.layout.dialog_add_product,null);
        final EditText edt_productAmount = (EditText) view1.findViewById(R.id.edt_productAmount);
        final EditText edt_productName = (EditText) view1.findViewById(R.id.productName);
        final EditText edt_productDescription = (EditText) view1.findViewById(R.id.edt_productDescription);
        Button btn_createExpense = (Button) view1.findViewById(R.id.btn_addProduct);

        btn_createExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String productName = edt_productName.getText().toString();
                String productRate = edt_productAmount.getText().toString();
                String productDesc = edt_productDescription.getText().toString();
                Map<String,String> map = new HashMap<>();
                map.put("productName",productName);
                map.put("productRate",productRate);
                map.put("productDescription",productDesc);
                productReference.document(productName).set(map);
                dialog.dismiss();
            }
        });

        dialog.setContentView(view1);
        dialog.show();
    }
}
