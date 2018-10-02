package com.fabuleux.wuntu.billstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.CustomerBillListAdapter;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.InvoicePojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.Pojos.MakeBillDetails;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.RecyclerViewListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceListActivity extends AppCompatActivity {

    @BindView(R.id.customerRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.customerName)
    TextView txt_vendorName;

    LinearLayoutManager mLayoutManager;

    ArrayList<InvoicePojo> billsList;

    CustomerBillListAdapter customerBillListAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    Context context;

    String vendorName;
    private ArrayList<ItemPojo> itemList;

    String billTime ="";
    String customerName = "";
    String customerAddress = "";
    String customerGst = "";
    Map<String,ItemPojo> billItems;
    double gstRate;

    double cgst = 0,sgst = 0,igst = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (getIntent().getStringExtra("VendorName") != null)
        {
            vendorName = getIntent().getStringExtra("VendorName");
            txt_vendorName.setText(vendorName);
        }

        itemList = new ArrayList<>();
        billsList = new ArrayList<>();
        billItems = new HashMap<>();

        customerBillListAdapter = new CustomerBillListAdapter(billsList);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customerBillListAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerViewListener(context, recyclerView, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                       /* itemList.clear();
                        InvoicePojo makeBillDetails = new InvoicePojo();
                        makeBillDetails = billsList.get(position);
                        Double billAmount = makeBillDetails.getBillAmount();
                        billTime = makeBillDetails.getBillTime();
                        cgst = makeBillDetails.getGstPojo().getSgst();
                        *//*sgst = makeBillDetails.getSgst();
                        igst = makeBillDetails.getIgst();
                        gstRate = makeBillDetails.getGstRate();
                        CustomerDetails customerDetails = new CustomerDetails();
                        customerDetails = makeBillDetails.getCustomerDetails();
                        customerName = customerDetails.getCustomerName();
                        customerAddress = customerDetails.getCustomerAddress();
                        customerGst = customerDetails.getCustomerGstNumber();
                        billItems = makeBillDetails.getBillItems();
                        itemList.addAll(billItems.values());*//*
                        sendDatatoPreview();
*/
                    }

                    @Override
                    public void onLongItemClick(View view, int position)
                    {

                    }
                })
        );


        CollectionReference collectionReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts")
                .document(vendorName).collection("Invoices");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null)
                {
                    Toast.makeText(context, "Bill Date Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                billsList.clear();
                for (DocumentSnapshot doc : documentSnapshots)
                {
                    InvoicePojo makeBillDetails = doc.toObject(InvoicePojo.class);
                    billsList.add(makeBillDetails);
                }
                customerBillListAdapter.notifyDataSetChanged();
            }
        });

    }

    private void sendDatatoPreview()
    {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putParcelableArrayListExtra("ItemList",itemList);
        intent.putExtra("Customer Name",customerName);
        intent.putExtra("Customer Address",customerAddress);
        intent.putExtra("Customer GST Number",customerGst);
        intent.putExtra("Invoice Date",billTime);
        intent.putExtra("showSave" ,false);
        intent.putExtra("gstValue",gstRate);
        intent.putExtra("cgst",cgst);
        intent.putExtra("sgst",sgst);
        intent.putExtra("igst",igst);

        startActivity(intent);
    }
}
