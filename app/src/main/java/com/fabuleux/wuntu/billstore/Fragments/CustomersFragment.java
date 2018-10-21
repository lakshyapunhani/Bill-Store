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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Activity.InvoiceListActivity;
import com.fabuleux.wuntu.billstore.Activity.NotificationsActivity;
import com.fabuleux.wuntu.billstore.Adapters.CustomerListAdapter;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomersFragment extends Fragment {


    @BindView(R.id.customersList)
    RecyclerView customersList;

    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    @BindView(R.id.img_notifications)
    ImageView img_notifications;

    ArrayList<ContactPojo> customerDetailsList;
    FirebaseAuth firebaseAuth;
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

        customersList.addOnItemTouchListener(
                new RecyclerViewListener(context, customersList, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, InvoiceListActivity.class);
                        intent.putExtra("contactNumber",customerDetailsList.get(position).getContactPhoneNumber());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        billsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");

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
                    ContactPojo customerDetails = doc.toObject(ContactPojo.class);
                    if (customerDetails.getNumberInvoices() > 0)
                    {
                        customerDetailsList.add(customerDetails);
                    }
                }

                if (customerDetailsList.isEmpty())
                {
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    emptyLayout.setVisibility(View.GONE);
                }

                customerListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @OnClick(R.id.img_notifications)
    public void openNotifications()
    {
        startActivity(new Intent(context,NotificationsActivity.class));
    }
}
