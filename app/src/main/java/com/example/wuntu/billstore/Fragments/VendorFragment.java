package com.example.wuntu.billstore.Fragments;


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
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.VendorListAdapter;
import com.example.wuntu.billstore.BillsListActivity;
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.ProfileActivity;
import com.example.wuntu.billstore.R;
import com.example.wuntu.billstore.Utils.RecyclerViewListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorFragment extends Fragment {

    @BindView(R.id.vendorsList)
    RecyclerView vendorsList;

    ArrayList<VendorDetails> vendorDetailsList;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;

    VendorListAdapter vendorListAdapter;

    CollectionReference billsReference;

    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_seller, container, false);
        ButterKnife.bind(this,view);
        vendorDetailsList = new ArrayList<>();
        vendorListAdapter = new VendorListAdapter(vendorDetailsList);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        vendorsList.setLayoutManager(mLayoutManager);
        vendorsList.setItemAnimator(new DefaultItemAnimator());
        vendorsList.setAdapter(vendorListAdapter);

        vendorsList.addOnItemTouchListener(
                new RecyclerViewListener(context, vendorsList, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, BillsListActivity.class);
                        intent.putExtra("fragment","seller");
                        intent.putExtra("VendorName",vendorDetailsList.get(position).getVendorName());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        billsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Vendors");

        billsReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null) {
                    Toast.makeText(context, "Bills Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                vendorDetailsList.clear();

                for (DocumentSnapshot doc : documentSnapshots) {
                    VendorDetails vendorDetails = doc.toObject(VendorDetails.class);
                    vendorDetailsList.add(vendorDetails);
                }

                vendorListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}
