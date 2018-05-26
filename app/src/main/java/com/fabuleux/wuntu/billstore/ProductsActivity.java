package com.fabuleux.wuntu.billstore;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.ProductAdapter;
import com.fabuleux.wuntu.billstore.Pojos.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.productsList)
    RecyclerView productsListRecycler;

    @BindView(R.id.emptyProductLayout)
    LinearLayout emptyProductLayout;

    @BindView(R.id.btn_addProduct)
    FloatingActionButton addProductBtn;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;
    CollectionReference productReference;

    ArrayList<ProductModel> products;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        View view1 = this.getCurrentFocus();

        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        products = new ArrayList<>();

        initView();

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
                productAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnTextChanged(value = R.id.edt_searchProduct, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence cs)
    {
        updateList(filterList(cs));
    }

    private void updateList(ArrayList<ProductModel> arrayList)
    {
        productsListRecycler.setAdapter(new ProductAdapter(arrayList));
    }

    private ArrayList<ProductModel> filterList(CharSequence cs)
    {
        productsListRecycler.removeAllViewsInLayout();
        ArrayList<ProductModel> filteredList = new ArrayList<>();
        for (int i = 0; i < products.size(); i++)
        {
            if (products.get(i).getProductName().toLowerCase().contains(cs.toString().toLowerCase())) {
                filteredList.add(products.get(i));
            }
        }
        return filteredList;

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

        btn_createExpense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String productName = edt_productName.getText().toString();
                final String productRate = edt_productAmount.getText().toString();
                final String productDesc = edt_productDescription.getText().toString();

                final ProductModel productModel = new ProductModel("",productName,productRate,productDesc);

                final DocumentReference documentReference = productReference.document(productName);

                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                Toast.makeText(ProductsActivity.this, "Product Already added", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                documentReference.set(productModel);
                            }
                        } else
                        {
                            Toast.makeText(ProductsActivity.this, "Some error occured. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.setContentView(view1);
        dialog.show();
    }

    private void initView()
    {
        productAdapter = new ProductAdapter(products);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productsListRecycler.setLayoutManager(linearLayoutManager);
        productsListRecycler.setAdapter(productAdapter);
    }
}
