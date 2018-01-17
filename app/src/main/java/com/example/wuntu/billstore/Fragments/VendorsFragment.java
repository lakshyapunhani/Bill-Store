package com.example.wuntu.billstore.Fragments;


import android.content.Context;
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
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.ProfileActivity;
import com.example.wuntu.billstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorsFragment extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager mLayoutManager;

    ArrayList<VendorDetails> vendorDetailsList;

    VendorListAdapter vendorListAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    Context context;

    public VendorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendors, container, false);

        ButterKnife.bind(this,view);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        vendorDetailsList = new ArrayList<>();

        vendorListAdapter = new VendorListAdapter(vendorDetailsList);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vendorListAdapter);


        CollectionReference billsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Bills");

        vendorDetailsList = new ArrayList<>();

        billsReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                //vendorDetailsList.clear();
                if (e != null) {
                    Toast.makeText(context, "Bills Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (DocumentSnapshot doc : documentSnapshots) {
                    VendorDetails vendorDetails = doc.toObject(VendorDetails.class);
                    vendorDetailsList.add(vendorDetails);
                    vendorListAdapter.notifyDataSetChanged();
                }
            }
        });



        return view;
    }

}
