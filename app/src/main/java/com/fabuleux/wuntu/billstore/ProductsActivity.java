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
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tellh.com.stickyheaderview_rv.adapter.StickyHeaderViewAdapter;

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

    ArrayList<ProductModel> products;
    private StickyHeaderViewAdapter adapter;
    private Random random = new Random(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        products = new ArrayList<>();

        productReference = db.collection("Users").document(firebaseUser.getUid()).collection("Products");

        productReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    Toast.makeText(ProductsActivity.this, "Not able to show products. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                products.clear();

                for (DocumentSnapshot doc : documentSnapshots)
                {
                    ProductModel productModel = doc.toObject(ProductModel.class);
                    products.add(productModel);
                }
            }
        });
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
                ProductModel productModel = new ProductModel(productName,productRate,productDesc);
                productReference.document(productName).set(productModel);
                dialog.dismiss();
            }
        });

        dialog.setContentView(view1);
        dialog.show();
    }
}
