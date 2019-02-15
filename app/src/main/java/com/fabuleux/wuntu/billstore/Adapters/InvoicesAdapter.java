package com.fabuleux.wuntu.billstore.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 12-03-2018.
 */

public class InvoicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<ContactPojo> customerDetailsList;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference billsReference;

    Context context;

    int count = 0;

    public InvoicesAdapter(ArrayList<ContactPojo> customerDetailsList) {
        this.customerDetailsList = customerDetailsList;
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        billsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.traderName)
        TextView traderName;

        @BindView(R.id.traderAddress)
        TextView traderAddress;

        @BindView(R.id.totalNumberInvoices)
        TextView totalNumberInvoices;

        @BindView(R.id.last_invoice_date)
        TextView last_invoice_date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_customers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder)
        {

            ContactPojo contactPojo = customerDetailsList.get(position);
            CollectionReference collectionReference =  billsReference.document(contactPojo.getContactPhoneNumber()).collection("Invoices");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(context, "Some error occurred. Please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    count = documentSnapshots.size();
                    ((ViewHolder)holder).totalNumberInvoices.setText(String.valueOf(count));

                }
            });
            ((ViewHolder) holder).traderName.setText(contactPojo.getContactName());
            ((ViewHolder) holder).traderAddress.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_house_black, 0, 0, 0);
            ((ViewHolder) holder).traderAddress.setText(contactPojo.getContactAddress());

            ((ViewHolder)holder).last_invoice_date.setText(contactPojo.getLastInvoiceDate());
        }
    }

    @Override
    public int getItemCount() {
        return this.customerDetailsList.size();
    }
}
