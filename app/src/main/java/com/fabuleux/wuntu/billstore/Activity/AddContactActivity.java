package com.fabuleux.wuntu.billstore.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Fragments.AddBillFragment;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Network.CommonRequest;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @BindView(R.id.add_contact_title)
    TextView add_contact_title;

    @BindView(R.id.btn_add_contact)
    TextView btn_add_contact;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference contactReference;

    CollectionReference usersReference;

    ContactPojo contactPojo;

    String contactUID = "";
    String contactAddress = "";
    String contactGstNumber = "";
    String contactPhoneNumber = "";
    String contactName = "";

    SessionManager sessionManager;

    boolean edit;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);

        if (getIntent().getParcelableExtra("pojo") != null)
        {
            edit = true;
            contactPojo = getIntent().getParcelableExtra("pojo");
            edt_contactName.setText(contactPojo.getContactName());
            edt_contactGstNumber.setText(contactPojo.getContactGstNumber());
            edt_contactAddress.setText(contactPojo.getContactAddress());
            edt_contactPhoneNumber.setText(contactPojo.getContactPhoneNumber());
            add_contact_title.setText("Edit Contact");
            edt_contactPhoneNumber.setEnabled(false);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        sessionManager = new SessionManager(AddContactActivity.this);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        contactReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");

        usersReference = db.collection("Users");

    }

    @OnClick(R.id.btn_add_contact)
    public void checkContact()
    {
        hideSoftKeyboard(AddContactActivity.this);
        if (!progressDialog.isShowing() && !AddContactActivity.this.isDestroyed())
        {
            progressDialog.show();
        }

        if (edt_contactPhoneNumber.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please fill contact phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edt_contactName.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please fill contact name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!edt_contactAddress.getText().toString().trim().isEmpty())
        {
            contactAddress = edt_contactAddress.getText().toString().trim();
        }

        if (!edt_contactGstNumber.getText().toString().trim().isEmpty())
        {
            contactGstNumber = edt_contactGstNumber.getText().toString().trim();
        }


        if (edt_contactPhoneNumber.getText().toString().startsWith("+"))
        {
            contactPhoneNumber = edt_contactPhoneNumber.getText().toString().trim();
        }
        else
        {
            contactPhoneNumber = "+91" + edt_contactPhoneNumber.getText().toString().trim();
        }

        contactName = edt_contactName.getText().toString().trim();


        usersReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult())
                    {
                        if (document.exists() && document.contains("mobileNumber"))
                        {
                            if (document.get("mobileNumber").equals(contactPhoneNumber))
                            {
                                contactUID = document.getId();
                                addContactToRegisteredUser(document);
                            }
                        }
                    }
                    addContact();
                }
            }
        });
    }

    private void addContactToRegisteredUser(DocumentSnapshot document)
    {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("deviceId",document.getId());
        HashMap<String,Object> objectHashMap = new HashMap<>();
        HashMap<String,Object> stringHashMap = new HashMap<>();
        stringHashMap.put("score","Added you contact");
        stringHashMap.put("time","Contact Added");
        objectHashMap.put("data",stringHashMap);
        hashMap.put("payload",objectHashMap);

        CommonRequest.getInstance(this).sendNotification(hashMap);


        CollectionReference collectionReference = db.collection("Users").document(document.getId()).collection("Contacts");
        final DocumentReference documentReference = collectionReference.document(firebaseUser.getPhoneNumber());
        final ContactPojo contactPojo = new ContactPojo(sessionManager.getShop_name(),sessionManager.getShop_address(),
                sessionManager.getShop_gst(),firebaseUser.getPhoneNumber(),
                firebaseUser.getUid(),0,"");
        documentReference.set(contactPojo);
    }

        private void addContact()
        {
            final ContactPojo contactPojo = new ContactPojo(contactName,contactAddress,contactGstNumber,contactPhoneNumber, contactUID,0,"");

            final DocumentReference documentReference = contactReference.document(contactPhoneNumber);

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (progressDialog.isShowing() && !AddContactActivity.this.isDestroyed())
                        {
                            progressDialog.dismiss();
                        }

                        if (!edit && document.exists())
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

        public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    }
