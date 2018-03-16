package com.example.wuntu.billstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.BillDocumentsAdapter;
import com.example.wuntu.billstore.Pojos.AddBillDetails;
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.Utils.RecyclerViewListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillViewActivity extends AppCompatActivity {


    @BindView(R.id.wholeSellerName)
    TextView wholeSellerName;

    @BindView(R.id.wholeSellerAddress)
    TextView wholeSellerAddress;

    @BindView(R.id.wholeSellerBillDate)
    TextView wholeSellerBillDate;

    @BindView(R.id.wholeSellerBillAmount)
    TextView wholeSellerBillAmount;

    @BindView(R.id.billStatus)
    TextView billStatus;

    @BindView(R.id.editBillStatus)
    TextView editBillStatus;

    @BindView(R.id.wholeSellerBillDocuments)
    RecyclerView wholeSellerBillDocuments;

    @BindView(R.id.menu_dots)
    ImageView menu_dots;

    BillDocumentsAdapter billDocumentsAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    AddBillDetails addBillDetails;

    String vendorName,billDate,billTime;

    Map<String,String> billImages;

    List<String> keyList;
    List<String> valuesList;

    DocumentReference billDateReference;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        billImages = new HashMap<>();

        keyList = new ArrayList<>();
        valuesList = new ArrayList<>();

        billDocumentsAdapter = new BillDocumentsAdapter(keyList,valuesList);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        wholeSellerBillDocuments.setLayoutManager(mLayoutManager);
        wholeSellerBillDocuments.setItemAnimator(new DefaultItemAnimator());
        wholeSellerBillDocuments.setAdapter(billDocumentsAdapter);

        if (getIntent() != null)
        {
            vendorName = getIntent().getStringExtra("VendorName");
            billDate = getIntent().getStringExtra("BillDate");
            billTime = getIntent().getStringExtra("BillTime");
        }

        billDateReference = db.collection("Users").document(firebaseUser.getUid()).collection("Vendors")
                .document(vendorName).collection(firebaseUser.getUid()).document(billDate + "&&" + billTime);

        billDateReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null)
                {
                    Toast.makeText(BillViewActivity.this, "Bill Date Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot.exists())
                {
                    keyList.clear();
                    valuesList.clear();
                    addBillDetails = documentSnapshot.toObject(AddBillDetails.class);
                    setUiFields();
                }

            }
        });

        wholeSellerBillDocuments.addOnItemTouchListener(
                new RecyclerViewListener(BillViewActivity.this, wholeSellerBillDocuments, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(valuesList.get(position)));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position)
                    {}
                }));
    }

    @OnClick(R.id.menu_dots)
    public void menuClick()
    {
        PopupMenu popup = new PopupMenu(BillViewActivity.this, menu_dots);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popupmenu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mark_paid)
                {
                    editBill();
                }
                else if (item.getItemId() == R.id.delete_bill)
                {
                    deleteBill();
                }
                return true;
            }
        });

        popup.show();
    }



    private void setUiFields()
    {
        VendorDetails vendorDetails = new VendorDetails();
        vendorDetails = addBillDetails.getVendorDetails();
        wholeSellerName.setText(vendorDetails.getVendorName());
        wholeSellerAddress.setText(vendorDetails.getVendorAddress());
        wholeSellerBillAmount.setText(addBillDetails.getBillAmount());
        wholeSellerBillDate.setText(addBillDetails.getBillDate());
        billStatus.setText(addBillDetails.getBillStatus());
        if (addBillDetails.getBillStatus().equalsIgnoreCase("due"))
        {
            Log.d("TAG","TAG");
        }
        else
        {
            editBillStatus.setVisibility(View.GONE);
        }
        if (addBillDetails.getBillImages().size() > 0)
        {
            keyList.addAll(addBillDetails.getBillImages().keySet());
            valuesList.addAll(addBillDetails.getBillImages().values());
        }

        billDocumentsAdapter.notifyDataSetChanged();

    }

    @OnClick(R.id.editBillStatus)
    public void editBill()
    {
        billDateReference.update("billStatus","Paid")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(BillViewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BillViewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@OnClick(R.id.btn_deleteDocument)*/
    public void deleteBill()
    {
        billDateReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        StorageReference desertRef ;
                        //desertRef = mStorageRef.child(firebaseUser.getUid()).child(vendorName);
                        String parent = billDate + "&&" + billTime;
                        for (int i = 0;i<keyList.size();i++)
                        {
                            desertRef = mStorageRef.child(firebaseUser.getUid()).child(vendorName).child(parent + "/" + keyList.get(i));
                            final int finalI = i;
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    if (finalI == keyList.size() - 1)
                                    {
                                        //getActivity().getSupportFragmentManager().popBackStack();
                                        Toast.makeText(BillViewActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                }
                            });
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BillViewActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
