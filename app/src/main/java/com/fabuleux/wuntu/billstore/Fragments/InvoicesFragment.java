package com.fabuleux.wuntu.billstore.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Activity.InvoiceListActivity;
import com.fabuleux.wuntu.billstore.Activity.NotificationsActivity;
import com.fabuleux.wuntu.billstore.Adapters.InvoicesAdapter;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
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
public class InvoicesFragment extends Fragment {


    @BindView(R.id.customersList)
    RecyclerView invoicesList;

    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;

    @BindView(R.id.img_notifications)
    ImageView img_notifications;

    ArrayList<ContactPojo> customerDetailsList;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    InvoicesAdapter invoicesAdapter;

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
        View view =inflater.inflate(R.layout.fragment_invoices, container, false);
        ButterKnife.bind(this,view);
        customerDetailsList = new ArrayList<>();
        invoicesAdapter = new InvoicesAdapter(customerDetailsList);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        invoicesList.setLayoutManager(mLayoutManager);
        invoicesList.setItemAnimator(new DefaultItemAnimator());
        invoicesList.setAdapter(invoicesAdapter);

        invoicesList.addOnItemTouchListener(
                new RecyclerViewListener(context, invoicesList, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, InvoiceListActivity.class);
                        intent.putExtra("contactNumber",customerDetailsList.get(position).getContactPhoneNumber());
                        intent.putExtra("contactName",customerDetailsList.get(position).getContactName());

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
                    Toast.makeText(context, "Some error occurred. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                customerDetailsList.clear();

                for (DocumentSnapshot doc : documentSnapshots) {
                    final ContactPojo customerDetails = doc.toObject(ContactPojo.class);

                    CollectionReference collectionReference =  billsReference.document(customerDetails.getContactPhoneNumber()).collection("Invoices");
                    collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {
                                Toast.makeText(context, "Some error occurred. Please try again", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (documentSnapshots.size() > 0)
                            {
                                customerDetailsList.add(customerDetails);
                            }

                            if (customerDetailsList.isEmpty())
                            {
                                emptyLayout.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                emptyLayout.setVisibility(View.GONE);
                            }

                            invoicesAdapter.notifyDataSetChanged();

                        }
                    });

                }


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
