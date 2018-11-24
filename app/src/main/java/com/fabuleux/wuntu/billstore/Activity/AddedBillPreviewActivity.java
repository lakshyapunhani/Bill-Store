package com.fabuleux.wuntu.billstore.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.BillDocumentsAdapter;
import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.AddBillDetails;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;
import com.fabuleux.wuntu.billstore.Utils.RecyclerViewListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class AddedBillPreviewActivity extends AppCompatActivity {


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

    @BindView(R.id.addedBillFAB)
    FloatingActionsMenu addedBillFAB;

    BillDocumentsAdapter billDocumentsAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;


    String billTime;

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

    private String customerName = "";
    private String customerAddress = "";
    private String customerGstNumber = "",newCustomerMobileNumber = "",newCustomerUID = "";
    int newCustomerNumberInvoices;
    private String invoiceDate = "",dueDate = "";
    private String status;

    double subTotal = 0;

    private FloatingActionButton markPaidFAB;
    private FloatingActionButton deleteBillFAB;

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



        popup = new PopupMenu(AddedBillPreviewActivity.this, menu_dots);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popupmenu, popup.getMenu());

        billDocumentsAdapter = new BillDocumentsAdapter(keyList,valuesList);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        wholeSellerBillDocuments.setLayoutManager(mLayoutManager);
        wholeSellerBillDocuments.setItemAnimator(new DefaultItemAnimator());
        wholeSellerBillDocuments.setAdapter(billDocumentsAdapter);



        getIntentItems();

        setUiFields();

        setFloatingActionMenu();

        billDateReference = db.collection("Users").document(firebaseUser.getUid()).
                collection("Contacts").document(newCustomerMobileNumber).collection("Invoices").document(invoiceDate + " && " + billTime);

        wholeSellerBillDocuments.addOnItemTouchListener(
                new RecyclerViewListener(AddedBillPreviewActivity.this, wholeSellerBillDocuments, new RecyclerViewListener.OnItemClickListener() {
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

    private void setFloatingActionMenu()
    {
        markPaidFAB = new FloatingActionButton(this);
        markPaidFAB.setTag("markPaid");
        markPaidFAB.setTitle(getString(R.string.markPaid));
        markPaidFAB.setSize(FloatingActionButton.SIZE_MINI);
        markPaidFAB.setImageResource(android.R.drawable.ic_menu_send);

        deleteBillFAB = new FloatingActionButton(this);
        deleteBillFAB.setTag("deleteBill");
        deleteBillFAB.setTitle(getString(R.string.deleteBill));
        deleteBillFAB.setSize(FloatingActionButton.SIZE_MINI);
        deleteBillFAB.setImageResource(android.R.drawable.ic_delete);

        addedBillFAB.addButton(markPaidFAB);
        addedBillFAB.addButton(deleteBillFAB);

        markPaidFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //saveButton();
                editBill();
            }
        });

        deleteBillFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //saveButton();
                deleteBill();
            }
        });
    }

    private void getIntentItems()
    {
        if (getIntent() != null)
        {
            //invoicePojo = getIntent().getParcelableExtra("invoicePojo");
            customerName = getIntent().getStringExtra("Customer Name");
            customerAddress = getIntent().getStringExtra("Customer Address");
            customerGstNumber = getIntent().getStringExtra("Customer GST Number");
            newCustomerUID = getIntent().getStringExtra("Customer UID");
            newCustomerMobileNumber= getIntent().getStringExtra("Customer Mobile Number");
            newCustomerNumberInvoices = getIntent().getIntExtra("Customer Number Invoices", 0);
            invoiceDate = getIntent().getStringExtra("Invoice Date");
            billImages = (HashMap<String, String>) getIntent().getSerializableExtra("billImages");
            subTotal = getIntent().getDoubleExtra("subTotal",0);
            status = getIntent().getStringExtra("billStatus");
            billTime = getIntent().getStringExtra("billTime");
        }
    }

    /*@OnClick(R.id.menu_dots_bill_view)
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
    }*/



    private void setUiFields()
    {
        wholeSellerName.setText(customerName);
        wholeSellerAddress.setText(customerAddress);

        if (customerGstNumber != null && !customerGstNumber.isEmpty())
        {
            wholeSellerGst.setText("(GST Number - " + customerGstNumber+ ")");
        }
        else
        {
            wholeSellerGst.setText("(GST Number - NA)");
        }

        wholeSellerBillAmount.setText(getResources().getString(R.string.rupee_sign)  + subTotal);
        wholeSellerBillDate.setText(invoiceDate);
        billStatus.setText(status);

        if (billImages.size() > 0)
        {
            keyList.addAll(billImages.keySet());
            valuesList.addAll(billImages.values());
        }

        billDocumentsAdapter.notifyDataSetChanged();

    }

    public void editBill()
    {

        if (status.equalsIgnoreCase("due"))
        {
            billDateReference.update("billStatus","Paid")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(AddedBillPreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddedBillPreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddedBillPreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddedBillPreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void deleteBill()
    {
        if (!progressDialog.isShowing() && !AddedBillPreviewActivity.this.isDestroyed())
        {
            progressDialog.show();
        }

        billDateReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        StorageReference desertRef ;
                        //desertRef = mStorageRef.child(firebaseUser.getUid()).child(vendorName);
                        String parent = invoiceDate + "&&" + billTime;
                        for (int i = 0;i<keyList.size();i++)
                        {
                            desertRef = mStorageRef.child(firebaseUser.getUid()).child(newCustomerUID).child(parent + "/" + keyList.get(i));
                            final int finalI = i;
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
                                    if (finalI == keyList.size() - 1)
                                    {
                                        if (progressDialog.isShowing() && !AddedBillPreviewActivity.this.isDestroyed())
                                        {
                                            progressDialog.dismiss();
                                        }

                                        Toast.makeText(AddedBillPreviewActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Uh-oh, an error occurred!
                                    if (progressDialog.isShowing() && !AddedBillPreviewActivity.this.isDestroyed())
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
                Toast.makeText(AddedBillPreviewActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();
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
