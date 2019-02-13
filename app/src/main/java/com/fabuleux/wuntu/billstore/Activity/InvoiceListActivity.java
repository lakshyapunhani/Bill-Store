package com.fabuleux.wuntu.billstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.InvoicesListAdapter;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.InvoicePojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
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

    InvoicesListAdapter customerBillListAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    Context context;

    String contactNumber;
    private ArrayList<ItemPojo> itemList;

    String billType = "";

    Map<String,ItemPojo> billItems;

    Map<String,String> billImages;

    InvoicePojo makeBillDetails;

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
            String contactName = getIntent().getStringExtra("contactName");
            if (contactName != null && !contactName.isEmpty())
            {
                txt_vendorName.setText(contactName);
            }
            else
            {
                txt_vendorName.setText(contactNumber);
            }
        }

        billImages = new HashMap<>();

        itemList = new ArrayList<>();
        billsList = new ArrayList<>();
        billItems = new HashMap<>();

        customerBillListAdapter = new InvoicesListAdapter(billsList);

        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customerBillListAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerViewListener(context, recyclerView, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        makeBillDetails = new InvoicePojo();
                        makeBillDetails = billsList.get(position);

                        if ((makeBillDetails.getBillType().matches("Sent")
                                || makeBillDetails.getBillType().matches("Recieved")
                                || makeBillDetails.getBillType().matches("Sales")
                                || makeBillDetails.getBillType().matches("Purchase")) && !makeBillDetails.isBillAdded())
                        {
                            itemList.clear();
                            billItems = new HashMap<>();
                            billItems = makeBillDetails.getBillItems();
                            itemList.addAll(billItems.values());
                            billType = makeBillDetails.getBillType();
                            sendDatatoPreview();
                        }
                        else if (makeBillDetails.getBillType().matches("Added") || makeBillDetails.isBillAdded())
                        {
                            billItems = new HashMap<>();
                            billItems = makeBillDetails.getBillItems();
                            itemList.clear();
                            itemList.addAll(billItems.values());
                            billImages = makeBillDetails.getBillImages();
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
        intent.putExtra("receiverName", makeBillDetails.getReceiverPojo().getContactName());
        intent.putExtra("receiverAddress", makeBillDetails.getReceiverPojo().getContactAddress());
        intent.putExtra("receiverGSTNumber", makeBillDetails.getReceiverPojo().getContactGstNumber());
        intent.putExtra("receiverMobileNumber", makeBillDetails.getReceiverPojo().getContactPhoneNumber());
        intent.putExtra("receiverUID", makeBillDetails.getReceiverPojo().getContactUID());
        intent.putExtra("Customer Number Invoices",makeBillDetails.getReceiverPojo().getNumberInvoices());
        intent.putExtra("Invoice Date", makeBillDetails.getInvoiceDate());
        intent.putExtra("Due Date", makeBillDetails.getDueDate());
        if (billType.matches("Sent") || billType.matches("Sales"))
        {
            intent.putExtra("showFab", 1);
        }
        else if (billType.matches("Recieved") || billType.matches("Purchase"))
        {
            intent.putExtra("showFab", 2);
        }
        intent.putExtra("sgst", makeBillDetails.getGstPojo().getSgst());
        intent.putExtra("igst", makeBillDetails.getGstPojo().getIgst());
        intent.putExtra("utgst", makeBillDetails.getGstPojo().getUtgst());
        intent.putExtra("shipping charge", makeBillDetails.getGstPojo().getShippingCharges());
        intent.putExtra("discount", makeBillDetails.getGstPojo().getDiscount());
        intent.putExtra("subTotal", makeBillDetails.getBillAmount());
        intent.putExtra("invoiceNumber",makeBillDetails.getInvoiceNumber());
        intent.putExtra("billType",makeBillDetails.getBillType());
        intent.putExtra("senderName",makeBillDetails.getSenderPojo().getContactName());
        intent.putExtra("senderAddress",makeBillDetails.getSenderPojo().getContactAddress());
        intent.putExtra("senderGSTNumber",makeBillDetails.getSenderPojo().getContactGstNumber());
        intent.putExtra("senderMobileNumber",makeBillDetails.getSenderPojo().getContactPhoneNumber());
        intent.putExtra("senderUID",makeBillDetails.getSenderPojo().getContactUID());
        intent.putExtra("billTime",makeBillDetails.getBillTime());
        startActivity(intent);
    }

    private void sendDatatoBillView() {
        Intent intent = new Intent(this, AddedBillPreviewActivity.class);
        intent.putParcelableArrayListExtra("ItemList", itemList);
        intent.putExtra("Customer Name", makeBillDetails.getReceiverPojo().getContactName());
        intent.putExtra("Customer Address", makeBillDetails.getReceiverPojo().getContactAddress());
        intent.putExtra("receiverGSTNumber", makeBillDetails.getReceiverPojo().getContactGstNumber());
        intent.putExtra("Customer Mobile Number", makeBillDetails.getReceiverPojo().getContactPhoneNumber());
        intent.putExtra("Customer UID", makeBillDetails.getReceiverPojo().getContactUID());
        intent.putExtra("Customer Number Invoices", makeBillDetails.getReceiverPojo().getNumberInvoices());
        intent.putExtra("Invoice Date", makeBillDetails.getInvoiceDate());
        intent.putExtra("Due Date", makeBillDetails.getDueDate());
        intent.putExtra("showSave", false);
        intent.putExtra("sgst", makeBillDetails.getGstPojo().getSgst());
        intent.putExtra("igst", makeBillDetails.getGstPojo().getIgst());
        intent.putExtra("utgst", makeBillDetails.getGstPojo().getUtgst());
        intent.putExtra("billStatus",makeBillDetails.getBillStatus());
        intent.putExtra("shipping charge", makeBillDetails.getGstPojo().getShippingCharges());
        intent.putExtra("discount", makeBillDetails.getGstPojo().getDiscount());
        intent.putExtra("subTotal", makeBillDetails.getBillAmount());
        intent.putExtra("billImages", (Serializable) billImages);
        intent.putExtra("billTime",makeBillDetails.getBillTime());
        startActivity(intent);
    }
}
