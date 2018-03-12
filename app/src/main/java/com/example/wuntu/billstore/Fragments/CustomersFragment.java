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

import com.example.wuntu.billstore.Adapters.CustomerListAdapter;
import com.example.wuntu.billstore.Adapters.VendorListAdapter;
import com.example.wuntu.billstore.Pojos.CustomerDetails;
import com.example.wuntu.billstore.Pojos.VendorDetails;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomersFragment extends Fragment {


    @BindView(R.id.customersList)
    RecyclerView customersList;

    ArrayList<CustomerDetails> customerDetailsList;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    CustomerListAdapter customerListAdapter;

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
        View view =inflater.inflate(R.layout.fragment_customers, container, false);
        ButterKnife.bind(this,view);
        customerDetailsList = new ArrayList<>();
        customerListAdapter = new CustomerListAdapter(customerDetailsList);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        customersList.setLayoutManager(mLayoutManager);
        customersList.setItemAnimator(new DefaultItemAnimator());
        customersList.setAdapter(customerListAdapter);

        billsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Customers");

        billsReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null) {
                    Toast.makeText(context, "Bills Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                customerDetailsList.clear();

                for (DocumentSnapshot doc : documentSnapshots) {
                    CustomerDetails customerDetails = doc.toObject(CustomerDetails.class);
                    customerDetailsList.add(customerDetails);
                }

                customerListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
