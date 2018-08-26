package com.fabuleux.wuntu.billstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.ContactsAdapter;
import com.fabuleux.wuntu.billstore.Adapters.ProductAdapter;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;
import com.fabuleux.wuntu.billstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsActivity extends AppCompatActivity {

    @BindView(R.id.btn_addContact)
    FloatingActionButton btn_addContact;

    @BindView(R.id.contactsList)
    RecyclerView contactsListRecycler;

    @BindView(R.id.edt_searchContact)
    EditText edt_searchContact;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;
    CollectionReference contactReference;

    ArrayList<ContactPojo> contactsList;

    ContactsAdapter contactsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        contactsList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        contactReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");

        contactReference.orderBy("contactName").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    Toast.makeText(ContactsActivity.this, "Not able to show contacts. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                contactsList.clear();

                for (DocumentSnapshot doc : documentSnapshots)
                {
                    ContactPojo itemSelectionPojo = doc.toObject(ContactPojo.class);
                    contactsList.add(itemSelectionPojo);
                }

                contactsAdapter.notifyDataSetChanged();

            }
        });

        initView();
    }

    @OnClick(R.id.btn_addContact)
    public void addContact()
    {
        startActivity(new Intent(this,AddContactActivity.class));
    }

    private void initView()
    {

        Collections.sort(contactsList, new Comparator<ContactPojo>() {
            @Override
            public int compare(ContactPojo s1, ContactPojo s2) {
                return (s1.getContactName()).compareToIgnoreCase(s2.getContactName());
            }
        });

        contactsAdapter = new ContactsAdapter(contactsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactsListRecycler.setLayoutManager(linearLayoutManager);
        contactsListRecycler.setAdapter(contactsAdapter);
    }
}
