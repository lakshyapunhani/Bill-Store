package com.fabuleux.wuntu.billstore.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.CustomerBillListAdapter;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.Pojos.MakeBillDetails;
import com.fabuleux.wuntu.billstore.Activity.PreviewActivity;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.RecyclerViewListener;
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

public class CustomerBillListFragment extends Fragment {


    @BindView(R.id.customerRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.customerName)
    TextView txt_vendorName;

    LinearLayoutManager mLayoutManager;

    ArrayList<MakeBillDetails> billsList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_bill_list, container, false);
        ButterKnife.bind(this,view);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (getArguments() != null)
        {
            vendorName = getArguments().getString("VendorName");
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
                        itemList.clear();
                        MakeBillDetails makeBillDetails = new MakeBillDetails();
                        makeBillDetails = billsList.get(position);
                        Double billAmount = makeBillDetails.getBillAmount();
                        billTime = makeBillDetails.getBillTime();
                        cgst = makeBillDetails.getCgst();
                        sgst = makeBillDetails.getSgst();
                        igst = makeBillDetails.getIgst();
                        gstRate = makeBillDetails.getGstRate();
                        CustomerDetails customerDetails = new CustomerDetails();
                        customerDetails = makeBillDetails.getCustomerDetails();
                        customerName = customerDetails.getCustomerName();
                        customerAddress = customerDetails.getCustomerAddress();
                        customerGst = customerDetails.getCustomerGstNumber();
                        billItems = makeBillDetails.getBillItems();
                        itemList.addAll(billItems.values());
                        sendDatatoPreview();

                    }

                    @Override
                    public void onLongItemClick(View view, int position)
                    {

                    }
                })
        );


        CollectionReference collectionReference = db.collection("Users").document(firebaseUser.getUid()).collection("Customers")
                .document(vendorName).collection(firebaseUser.getUid());


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
                    MakeBillDetails makeBillDetails = doc.toObject(MakeBillDetails.class);
                    billsList.add(makeBillDetails);
                }
                customerBillListAdapter.notifyDataSetChanged();
            }
        });

        return view;
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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

}
