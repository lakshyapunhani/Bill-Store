package com.example.wuntu.billstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.BillDocumentsAdapter;
import com.example.wuntu.billstore.EventBus.InternetStatus;
import com.example.wuntu.billstore.Manager.SessionManager;
import com.example.wuntu.billstore.Pojos.AddBillDetails;
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.Utils.NetworkReceiver;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.wholeSellerGst)
    TextView wholeSellerGst;

    @BindView(R.id.wholeSellerBillDate)
    TextView wholeSellerBillDate;

    @BindView(R.id.wholeSellerBillAmount)
    TextView wholeSellerBillAmount;

    @BindView(R.id.billStatus)
    TextView billStatus;

    @BindView(R.id.wholeSellerBillDocuments)
    RecyclerView wholeSellerBillDocuments;

    @BindView(R.id.menu_dots_bill_view)
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

    PopupMenu popup;

    ProgressDialog progressDialog;

    String vendorGst = "NA";
    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Deleting");
        progressDialog.setMessage("Please wait...");

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        billImages = new HashMap<>();

        keyList = new ArrayList<>();
        valuesList = new ArrayList<>();

        popup = new PopupMenu(BillViewActivity.this, menu_dots);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popupmenu, popup.getMenu());

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

    @OnClick(R.id.menu_dots_bill_view)
    public void menuClick()
    {
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
        if (vendorDetails.getVendorGst().matches(""))
        {
            vendorGst = vendorDetails.getVendorGst();
        }

        wholeSellerGst.setText("(GST Number - " + vendorGst + ")");
        wholeSellerBillAmount.setText(addBillDetails.getBillAmount());
        wholeSellerBillDate.setText(addBillDetails.getBillDate());
        billStatus.setText(addBillDetails.getBillStatus());

        if (addBillDetails.getBillImages().size() > 0)
        {
            keyList.addAll(addBillDetails.getBillImages().keySet());
            valuesList.addAll(addBillDetails.getBillImages().values());
        }

        billDocumentsAdapter.notifyDataSetChanged();

    }

    public void editBill()
    {

        if (addBillDetails.getBillStatus().equalsIgnoreCase("due"))
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
        else
        {
            billDateReference.update("billStatus","Due")
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
    }

    public void deleteBill()
    {
        if (!progressDialog.isShowing() && !BillViewActivity.this.isDestroyed())
        {
            progressDialog.show();
        }

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
                                        if (progressDialog.isShowing() && !BillViewActivity.this.isDestroyed())
                                        {
                                            progressDialog.dismiss();
                                        }

                                        //getActivity().getSupportFragmentManager().popBackStack();
                                        Toast.makeText(BillViewActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    if (progressDialog.isShowing() && !BillViewActivity.this.isDestroyed())
                                    {
                                        progressDialog.dismiss();
                                    }
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        this.unregisterReceiver(networkReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InternetStatus event) {
        if (event.getStatus()) {
            sessionManager.setInternetAvailable(true);
        }
        else
        {
            sessionManager.setInternetAvailable(false);
        }
    }
}
