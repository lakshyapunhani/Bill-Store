package com.fabuleux.wuntu.billstore.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends AppCompatActivity
{

    @BindView(R.id.back_arrow_contacts)
    ImageView back_arrow_contacts;

    @BindView(R.id.edt_contactName)
    EditText edt_contactName;

    @BindView(R.id.edt_contactAddress)
    EditText edt_contactAddress;

    @BindView(R.id.edt_contactPhone)
    EditText edt_contactPhoneNumber;

    @BindView(R.id.edt_contactGstNumber)
    EditText edt_contactGstNumber;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference contactReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        contactReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");


    }

    @OnClick(R.id.btn_add_contact)
    public void addContact()
    {
        String contactPhoneNumber = edt_contactPhoneNumber.getText().toString().trim();
        String contactName = edt_contactName.getText().toString().trim();
        String contactAddress = edt_contactAddress.getText().toString().trim();
        String contactGstNumber = edt_contactGstNumber.getText().toString().trim();

        final ContactPojo contactPojo = new ContactPojo(contactName,contactAddress,contactGstNumber,contactPhoneNumber);

        final DocumentReference documentReference = contactReference.document(contactPhoneNumber);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        Toast.makeText(AddContactActivity.this, "Contact already added", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        documentReference.set(contactPojo);
                        finish();
                    }
                } else
                {
                    Toast.makeText(AddContactActivity.this, "Some error occured. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.back_arrow_contacts)
    public void backArrowClick()
    {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
