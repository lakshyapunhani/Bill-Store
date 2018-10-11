package com.fabuleux.wuntu.billstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.CustomerBillListAdapter;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.InvoicePojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.Pojos.MakeBillDetails;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.RecyclerViewListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceListActivity extends AppCompatActivity {

    @BindView(R.id.customerRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.customerName)
    TextView txt_vendorName;

    LinearLayoutManager mLayoutManager;

    ArrayList<InvoicePojo> billsList;

    CustomerBillListAdapter customerBillListAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    Context context;

    String contactNumber;
    private ArrayList<ItemPojo> itemList;

    String billTime ="";
    String newCustomerName ="",newCustomerAddress = "",newCustomerGstNumber ="",
            newCustomerMobileNumber = "",newCustomerUID = "";
    int customerNumberInvoices;

    String invoiceDate = "",dueDate = "";
    Map<String,ItemPojo> billItems;

    double sgst = 0,igst = 0,utgst = 0;

    double subTotal = 0;

    double shipping_charges = 0,discount = 0;

    double totalAmount = 0;
    String billStatus ="";

    Map<String,String> billImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (getIntent().getStringExtra("contactNumber") != null)
        {
            contactNumber = getIntent().getStringExtra("contactNumber");
            txt_vendorName.setText(contactNumber);
        }

        billImages = new HashMap<>();

        itemList = new ArrayList<>();
        billsList = new ArrayList<>();
        billItems = new HashMap<>();

        customerBillListAdapter = new CustomerBillListAdapter(billsList);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customerBillListAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerViewListener(context, recyclerView, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        InvoicePojo makeBillDetails = new InvoicePojo();
                        makeBillDetails = billsList.get(position);

                        if (makeBillDetails.getBillType().matches("Sent") || makeBillDetails.getBillType().matches("Recieved"))
                        {
                            billTime = makeBillDetails.getBillTime();
                            sgst = makeBillDetails.getGstPojo().getSgst();
                            igst = makeBillDetails.getGstPojo().getIgst();
                            utgst = makeBillDetails.getGstPojo().getUtgst();
                            shipping_charges = makeBillDetails.getGstPojo().getShippingCharges();
                            discount = makeBillDetails.getGstPojo().getDiscount();
                            ContactPojo contactPojo = new ContactPojo();
                            contactPojo = makeBillDetails.getContactPojo();
                            newCustomerName = contactPojo.getContactName();
                            newCustomerAddress = contactPojo.getContactAddress();
                            newCustomerGstNumber = contactPojo.getContactGstNumber();
                            newCustomerMobileNumber = contactPojo.getContactPhoneNumber();
                            newCustomerUID = contactPojo.getContactUID();
                            customerNumberInvoices = contactPojo.getNumberInvoices();
                            billItems = makeBillDetails.getBillItems();
                            itemList.addAll(billItems.values());
                            invoiceDate = makeBillDetails.getInvoiceDate();
                            dueDate = makeBillDetails.getDueDate();
                            subTotal = makeBillDetails.getBillAmount();
                            billStatus = makeBillDetails.getBillStatus();
                            sendDatatoPreview();
                        }
                        else
                        {
                            billTime = makeBillDetails.getBillTime();
                            sgst = makeBillDetails.getGstPojo().getSgst();
                            igst = makeBillDetails.getGstPojo().getIgst();
                            utgst = makeBillDetails.getGstPojo().getUtgst();
                            shipping_charges = makeBillDetails.getGstPojo().getShippingCharges();
                            discount = makeBillDetails.getGstPojo().getDiscount();
                            ContactPojo contactPojo = new ContactPojo();
                            contactPojo = makeBillDetails.getContactPojo();
                            newCustomerName = contactPojo.getContactName();
                            newCustomerAddress = contactPojo.getContactAddress();
                            newCustomerGstNumber = contactPojo.getContactGstNumber();
                            newCustomerMobileNumber = contactPojo.getContactPhoneNumber();
                            newCustomerUID = contactPojo.getContactUID();
                            customerNumberInvoices = contactPojo.getNumberInvoices();
                            billItems = makeBillDetails.getBillItems();
                            itemList.addAll(billItems.values());
                            invoiceDate = makeBillDetails.getInvoiceDate();
                            dueDate = makeBillDetails.getDueDate();
                            subTotal = makeBillDetails.getBillAmount();
                            billImages = makeBillDetails.getBillImages();
                            billStatus = makeBillDetails.getBillStatus();
                            sendDatatoBillView();
                        }


                    }

                    @Override
                    public void onLongItemClick(View view, int position)
                    {

                    }
                })
        );


        CollectionReference collectionReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts")
                .document(contactNumber).collection("Invoices");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    InvoicePojo makeBillDetails = doc.toObject(InvoicePojo.class);
                    billsList.add(makeBillDetails);
                }
                customerBillListAdapter.notifyDataSetChanged();
            }
        });

    }

    private void sendDatatoPreview() {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putParcelableArrayListExtra("ItemList", itemList);
        intent.putExtra("Customer Name", newCustomerName);
        intent.putExtra("Customer Address", newCustomerAddress);
        intent.putExtra("Customer Mobile Number", newCustomerMobileNumber);
        intent.putExtra("Customer UID", newCustomerUID);
        intent.putExtra("Customer Number Invoices", customerNumberInvoices);
        intent.putExtra("Invoice Date", invoiceDate);
        intent.putExtra("Due Date", dueDate);
        intent.putExtra("showSave", true);
        intent.putExtra("sgst", sgst);
        intent.putExtra("igst", igst);
        intent.putExtra("utgst", utgst);
        intent.putExtra("shipping charge", shipping_charges);
        intent.putExtra("discount", discount);
        intent.putExtra("subTotal", subTotal);
        startActivity(intent);
    }

    private void sendDatatoBillView() {
        Intent intent = new Intent(this, BillViewActivity.class);
        intent.putParcelableArrayListExtra("ItemList", itemList);
        intent.putExtra("Customer Name", newCustomerName);
        intent.putExtra("Customer Address", newCustomerAddress);
        intent.putExtra("Customer Mobile Number", newCustomerMobileNumber);
        intent.putExtra("Customer UID", newCustomerUID);
        intent.putExtra("Customer Number Invoices", customerNumberInvoices);
        intent.putExtra("Invoice Date", invoiceDate);
        intent.putExtra("Due Date", dueDate);
        intent.putExtra("showSave", true);
        intent.putExtra("sgst", sgst);
        intent.putExtra("igst", igst);
        intent.putExtra("utgst", utgst);
        intent.putExtra("billStatus",billStatus );
        intent.putExtra("shipping charge", shipping_charges);
        intent.putExtra("discount", discount);
        intent.putExtra("subTotal", subTotal);
        intent.putExtra("billImages", (Serializable) billImages);
        startActivity(intent);
    }
}
