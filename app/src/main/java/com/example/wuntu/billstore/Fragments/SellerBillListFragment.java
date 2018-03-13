package com.example.wuntu.billstore.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.VendorsBillListAdapter;
import com.example.wuntu.billstore.Pojos.AddBillDetails;
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
public class SellerBillListFragment extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.vendorName)
    TextView txt_vendorName;

    LinearLayoutManager mLayoutManager;

    ArrayList<AddBillDetails> billsList;

    VendorsBillListAdapter billsListAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    Context context;

    String vendorName;

    public SellerBillListFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_bill_list, container, false);

        ButterKnife.bind(this,view);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (getArguments() != null)
        {
            vendorName = getArguments().getString("VendorName");
            txt_vendorName.setText(vendorName);
        }

        billsList = new ArrayList<>();

        billsListAdapter = new VendorsBillListAdapter(billsList);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(billsListAdapter);


        /*recyclerView.addOnItemTouchListener(
                new RecyclerViewListener(context, recyclerView, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        BillsFragmentPrevious billsFragment = new BillsFragmentPrevious();
                        Bundle bundle = new Bundle();
                        bundle.putString("VendorName",vendorName);
                        bundle.putString("BillDate",billsList.get(position).getBillDate());
                        bundle.putString("BillTime",billsList.get(position).getBillTime());
                        billsFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout,billsFragment).addToBackStack(null).commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position)
                    {

                    }
                })
        );*/


        CollectionReference billDateReference = db.collection("Users").document(firebaseUser.getUid()).collection("Vendors")
                .document(vendorName).collection(firebaseUser.getUid());


        billDateReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    AddBillDetails addBillDetails = doc.toObject(AddBillDetails.class);
                    billsList.add(addBillDetails);
                }
                billsListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}
