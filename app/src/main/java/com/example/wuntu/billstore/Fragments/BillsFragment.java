package com.example.wuntu.billstore.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.BillDocumentsAdapter;
import com.example.wuntu.billstore.AddNewBillActivity;
import com.example.wuntu.billstore.Pojos.BillDetails;
import com.example.wuntu.billstore.R;
import com.example.wuntu.billstore.Utils.MarshMallowPermission;
import com.example.wuntu.billstore.Utils.RecyclerViewListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillsFragment extends Fragment {

    @BindView(R.id.wholeSellerName)
    TextView wholeSellerName;

    @BindView(R.id.wholeSellerAddress)
    TextView wholeSellerAddress;

    @BindView(R.id.wholeSellerBillDate)
    TextView wholeSellerBillDate;

    @BindView(R.id.wholeSellerBillAmount)
    TextView wholeSellerBillAmount;

    @BindView(R.id.billStatus)
    TextView billStatus;

    @BindView(R.id.editBillStatus)
    TextView editBillStatus;

    @BindView(R.id.wholeSellerBillDocuments)
    RecyclerView wholeSellerBillDocuments;

    BillDocumentsAdapter billDocumentsAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    BillDetails billDetails;

    Context context;

    String vendorName,billDate,billTime;

    Map<String,String> billImages;

    List<String> keyList;
    List<String> valuesList;

    DocumentReference billDateReference;


    public BillsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bill_view, container, false);
        ButterKnife.bind(this,view);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        billImages = new HashMap<>();

        keyList = new ArrayList<>();
        valuesList = new ArrayList<>();

        billDocumentsAdapter = new BillDocumentsAdapter(keyList,valuesList);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        wholeSellerBillDocuments.setLayoutManager(mLayoutManager);
        wholeSellerBillDocuments.setItemAnimator(new DefaultItemAnimator());
        wholeSellerBillDocuments.setAdapter(billDocumentsAdapter);

        if (getArguments() != null)
        {
            vendorName = getArguments().getString("VendorName");
            billDate = getArguments().getString("BillDate");
            billTime = getArguments().getString("BillTime");
        }

        billDateReference = db.collection("Users").document(firebaseUser.getUid()).collection("Bills")
                .document(vendorName).collection(firebaseUser.getUid()).document(billDate + "&&" + billTime);

        billDateReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null)
                {
                    Toast.makeText(context, "Bill Date Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot.exists())
                {
                    keyList.clear();
                    valuesList.clear();
                    billDetails = documentSnapshot.toObject(BillDetails.class);
                    setUiFields();
                }

            }
        });

        wholeSellerBillDocuments.addOnItemTouchListener(
                new RecyclerViewListener(context, wholeSellerBillDocuments, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(valuesList.get(position)));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position)
                    {}
                }));

        return view;
    }

    private void setUiFields()
    {
        wholeSellerName.setText(billDetails.getVendorName());
        wholeSellerAddress.setText(billDetails.getVendorAddress());
        wholeSellerBillAmount.setText(billDetails.getBillAmount());
        wholeSellerBillDate.setText(billDetails.getBillDate());
        billStatus.setText(billDetails.getBillStatus());
        if (billDetails.getBillStatus().equalsIgnoreCase("due"))
        {
            Log.d("TAG","TAG");
        }
        else
        {
            editBillStatus.setVisibility(View.GONE);
        }
        if (billDetails.getBillImages().size() > 0)
        {
            keyList.addAll(billDetails.getBillImages().keySet());
            valuesList.addAll(billDetails.getBillImages().values());
        }

        billDocumentsAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.editBillStatus)
    public void editBill()
    {
        billDateReference.update("billStatus","Paid")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(context, "Bill Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Bill Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_deleteDocument)
    public void deleteBill()
    {

        billDateReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //BillsFragment billsFragment = new BillsFragment();
                        //getFragmentManager().beginTransaction().remove(billsFragment).commit();
                        getActivity().getSupportFragmentManager().popBackStack();
                        Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


