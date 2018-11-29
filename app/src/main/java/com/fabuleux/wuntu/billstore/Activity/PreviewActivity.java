package com.fabuleux.wuntu.billstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.EventBus.EventClearBill;
import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Network.CommonRequest;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.GstPojo;
import com.fabuleux.wuntu.billstore.Pojos.InvoicePojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.Pojos.NotificationPojo;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.pdflayout) NestedScrollView scrollView;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    Bitmap bitmap;

    PopupMenu popup;

    Map<String,String> billImages;

    @BindView(R.id.txt_shopName) TextView txt_shopName;

    @BindView(R.id.txt_shopAddress) TextView txt_shopAddress;

    @BindView(R.id.txt_gstNumber) TextView txt_gstNumber;

    @BindView(R.id.txt_invoiceDate) TextView txt_invoiceDate;

    @BindView(R.id.txt_custName) TextView txt_custName;

    @BindView(R.id.txt_custAddress) TextView txt_custAddress;

    @BindView(R.id.txt_custGstNumber) TextView txt_custGstNumber;

    @BindView(R.id.tableLayoutItems) TableLayout tableLayoutItems;

    @BindView(R.id.invoice_total) TextView invoice_total;

    @BindView(R.id.invoice_subTotal) TextView invoice_subTotal;

    //@BindView(R.id.btn_print) TextView btn_print;

    //@BindView(R.id.menu_dots_preview) ImageView menu_dots;

    @BindView(R.id.layout_gst) LinearLayout layout_gst;

    @BindView(R.id.layout_cgst) LinearLayout layout_cgst;

    @BindView(R.id.layout_sgst) LinearLayout layout_sgst;

    @BindView(R.id.layout_igst) LinearLayout layout_igst;

    @BindView(R.id.layout_utgst) LinearLayout layout_utgst;

    @BindView(R.id.rate_gst) TextView rate_gst;

    @BindView(R.id.invoice_cgst) TextView invoice_cgst;

    @BindView(R.id.invoice_sgst) TextView invoice_sgst;

    @BindView(R.id.invoice_igst) TextView invoice_igst;

    @BindView(R.id.invoice_utgst) TextView invoice_utgst;

    @BindView(R.id.layout_shipping) LinearLayout layout_create_shipping;

    @BindView(R.id.txt_shipping_charges) TextView txt_shipping_charges;

    @BindView(R.id.layout_discount) LinearLayout layout_create_discount;

    @BindView(R.id.txt_discount_charges) TextView txt_discount_charges;

    @BindView(R.id.layout_roundoff) LinearLayout layout_create_roundoff;

    @BindView(R.id.txt_roundOff) TextView txt_roundOff;

    @BindView(R.id.txt_dueDate) TextView txt_dueDate;

    @BindView(R.id.txt_invoiceNumber) TextView txt_invoiceNumber;

    @BindView(R.id.line_extra) View line_extra;

    @BindView(R.id.line_gst) View line_gst;

    @BindView(R.id.createInvoiceFAB)
    FloatingActionsMenu createInvoiceFAB;

    private FloatingActionButton sendInvoice;
    private FloatingActionButton printInvoice;

    @BindView(R.id.sentInvoiceFAB)
    FloatingActionsMenu sentInvoiceFAB;

    private FloatingActionButton cancelSentInvoice;
    private FloatingActionButton markPaidInvoice;
    private FloatingActionButton paymentReminder;

    @BindView(R.id.receivedInvoiceFAB)
    FloatingActionsMenu receivedInvoiceFAB;

    private FloatingActionButton cancelReceiveInvoice;

    private ArrayList<ItemPojo> itemList;
    private String receiverName = "";
    private String receiverAddress = "";
    private String receiverGstNumber = "";
    private String receiverMobileNumber = "";
    private String receiverUID = "";
    int newCustomerNumberInvoices;
    private String invoiceDate = "",dueDate = "";

    private String senderName = "";
    private String senderAddress = "";
    private String senderGstNumber = "";
    private String senderMobileNumber = "";
    private String senderUID = "";

    private String billTime = "";

    HashMap<String,ItemPojo> billItems;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    long timestamp;
    String timestampString;

    double sgst = 0,igst = 0,utgst = 0;

    double totalAmount = 0;

    double subTotal = 0;

    double shipping_charges = 0,discount = 0;

    File filePath;

    String invoiceNumber= "";

    int extraTaxLayout = 2;

    String billType = "";

    boolean printClicked = false;
    int showSave ;
    ProgressDialog progressDialog;

    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;

    ContactPojo receiverPojo;
    ContactPojo senderPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_new);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();

        billImages = new HashMap<>();

        billItems = new HashMap<>();
        itemList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving");
        progressDialog.setMessage("Please wait...");

        //popup = new PopupMenu(this, menu_dots);
        //Inflating the Popup using xml file
        //popup.getMenuInflater().inflate(R.menu.preview_menu, popup.getMenu());

        firebaseUser = firebaseAuth.getCurrentUser();

        timestamp = System.currentTimeMillis();
        timestampString = String.valueOf(timestamp);

        billItems.clear();
        itemList.clear();

        getIntentItems();

        setFloatingActionMenu();

        //getShopDetails();
        initTable();
        setViews();
    }

    private void setFloatingActionMenu()
    {


        ////////////////////////////////////////////// Create Invoice FAB
        sendInvoice = new FloatingActionButton(this);
        sendInvoice.setTag("sendInvoice");
        sendInvoice.setTitle(getString(R.string.sendInvoice));
        sendInvoice.setSize(FloatingActionButton.SIZE_MINI);
        sendInvoice.setImageResource(android.R.drawable.ic_menu_send);

        printInvoice = new FloatingActionButton(this);
        printInvoice.setTag("shareInvoice");
        printInvoice.setTitle(getString(R.string.shareInvoice));
        printInvoice.setSize(FloatingActionButton.SIZE_MINI);
        printInvoice.setImageResource(android.R.drawable.ic_menu_share);

        createInvoiceFAB.addButton(sendInvoice);
        createInvoiceFAB.addButton(printInvoice);

        sendInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //saveButton();
                sendInvoiceToSelectedUser();
            }
        });

        printInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //saveButton();
                printButton();
            }
        });

        ///////////////////////////////////////////////Sent Invoice FAB

        final DocumentReference receiverInvoiceReference = db.collection("Users").document(firebaseUser.getUid()).
                collection("Contacts").document(receiverMobileNumber).collection("Invoices").document(invoiceDate + " && " + billTime);


        final DocumentReference senderInvoiceReference = db.collection("Users").document(receiverUID).
                collection("Contacts").document(senderMobileNumber).collection("Invoices").document(invoiceDate + " && " + billTime);

        cancelSentInvoice = new FloatingActionButton(this);
        cancelSentInvoice.setTag("cancelSentInvoice");
        cancelSentInvoice.setTitle("Cancel Invoice");
        cancelSentInvoice.setSize(FloatingActionButton.SIZE_MINI);
        cancelSentInvoice.setImageResource(android.R.drawable.ic_menu_send);

        markPaidInvoice = new FloatingActionButton(this);
        markPaidInvoice.setTag("markPaidInvoice");
        markPaidInvoice.setTitle("Mark Paid Invoice");
        markPaidInvoice.setSize(FloatingActionButton.SIZE_MINI);
        markPaidInvoice.setImageResource(android.R.drawable.ic_menu_send);

        paymentReminder = new FloatingActionButton(this);
        paymentReminder.setTag("paymentReminderInvoice");
        paymentReminder.setTitle("Remind Payment");
        paymentReminder.setSize(FloatingActionButton.SIZE_MINI);
        paymentReminder.setImageResource(android.R.drawable.ic_menu_send);

        sentInvoiceFAB.addButton(markPaidInvoice);
        sentInvoiceFAB.addButton(cancelSentInvoice);
        sentInvoiceFAB.addButton(paymentReminder);

        cancelSentInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                receiverInvoiceReference.update("billStatus","Cancelled")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(PreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });

                senderInvoiceReference.update("billStatus","Cancelled")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                //Toast.makeText(PreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(PreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(PreviewActivity.this, "Sent cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        markPaidInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiverInvoiceReference.update("billStatus","Paid")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(PreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });

                senderInvoiceReference.update("billStatus","Paid")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                //Toast.makeText(PreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Toast.makeText(PreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });


                Toast.makeText(PreviewActivity.this, "Mark paid clicked", Toast.LENGTH_SHORT).show();
            }
        });

        paymentReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewActivity.this, "Payment Reminder", Toast.LENGTH_SHORT).show();
            }
        });




        //////////////////////////////////////////////////Received Invoice FAB

        cancelReceiveInvoice = new FloatingActionButton(this);
        cancelReceiveInvoice.setTag("cancelReceiveInvoice");
        cancelReceiveInvoice.setTitle("Cancel Invoice");
        cancelReceiveInvoice.setSize(FloatingActionButton.SIZE_MINI);
        cancelReceiveInvoice.setImageResource(android.R.drawable.ic_menu_send);

        receivedInvoiceFAB.addButton(cancelReceiveInvoice);

        cancelReceiveInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiverInvoiceReference.update("billStatus","Cancelled")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(PreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });

                senderInvoiceReference.update("billStatus","Cancelled")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                //Toast.makeText(PreviewActivity.this, "Bill Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(PreviewActivity.this, "Bill Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(PreviewActivity.this, "Receive cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getIntentItems()
    {
        if (getIntent() != null)
        {
            itemList.clear();
            itemList = getIntent().getParcelableArrayListExtra("ItemList");

            receiverName = getIntent().getStringExtra("receiverName");
            receiverAddress = getIntent().getStringExtra("receiverAddress");
            receiverGstNumber = getIntent().getStringExtra("receiverGSTNumber");
            receiverUID = getIntent().getStringExtra("receiverUID");
            receiverMobileNumber = getIntent().getStringExtra("receiverMobileNumber");

            newCustomerNumberInvoices = getIntent().getIntExtra("Customer Number Invoices", 0);

            invoiceDate = getIntent().getStringExtra("Invoice Date");
            dueDate = getIntent().getStringExtra("Due Date");

            showSave = getIntent().getIntExtra("showFab",0);

            utgst = getIntent().getDoubleExtra("utgst",0);
            sgst = getIntent().getDoubleExtra("sgst",0);
            igst = getIntent().getDoubleExtra("igst",0);
            shipping_charges = getIntent().getDoubleExtra("shipping charge",0);
            discount = getIntent().getDoubleExtra("discount",0);

            subTotal = getIntent().getDoubleExtra("subTotal",0);
            invoiceNumber = getIntent().getStringExtra("invoiceNumber");
            billType = getIntent().getStringExtra("billType");

            senderName = getIntent().getStringExtra("senderName");
            senderAddress = getIntent().getStringExtra("senderAddress");
            senderGstNumber = getIntent().getStringExtra("senderGSTNumber");
            senderMobileNumber= getIntent().getStringExtra("senderMobileNumber");
            senderUID  = getIntent().getStringExtra("senderUID");

            billTime = getIntent().getStringExtra("billTime");
        }
    }

    private void setViews()
    {
        String amount = String.valueOf(totalAmount);
        invoice_subTotal.setText(getResources().getString(R.string.rupee_sign) + String.valueOf(subTotal));
        txt_custName.setText(receiverName);
        txt_custAddress.setText(receiverAddress);
        if (receiverGstNumber != null && !receiverGstNumber.isEmpty())
        {
            txt_custGstNumber.setText(receiverGstNumber);
        }
        else
        {
            txt_custGstNumber.setText("NA");
        }

        txt_invoiceDate.setText(invoiceDate);
        txt_dueDate.setText(dueDate);
        invoice_total.setText(getResources().getString(R.string.rupee_sign) + amount);
        if (invoiceNumber.matches(""))
        {
            invoiceNumber = autoGenerateInvoiceNumber();
        }

        txt_invoiceNumber.setText("#" + invoiceNumber);

        if (showSave == 0)
        {
            createInvoiceFAB.setVisibility(View.VISIBLE);
            sentInvoiceFAB.setVisibility(View.GONE);
            receivedInvoiceFAB.setVisibility(View.GONE);
        }
        else if (showSave == 1)
        {
            createInvoiceFAB.setVisibility(View.GONE);
            sentInvoiceFAB.setVisibility(View.VISIBLE);
            receivedInvoiceFAB.setVisibility(View.GONE);
        }
        else
        {
            createInvoiceFAB.setVisibility(View.GONE);
            sentInvoiceFAB.setVisibility(View.GONE);
            receivedInvoiceFAB.setVisibility(View.VISIBLE);
        }

        if (sgst != 0)
        {
            layout_gst.setVisibility(View.VISIBLE);
            layout_cgst.setVisibility(View.VISIBLE);
            layout_sgst.setVisibility(View.VISIBLE);
            layout_utgst.setVisibility(View.GONE);
            layout_igst.setVisibility(View.GONE);
            utgst = 0;
            igst = 0;
            rate_gst.setText(sgst + "%");
            double cgstTax = sgst / 2.0;
            invoice_cgst.setText(cgstTax + "%");
            invoice_sgst.setText(cgstTax + "%");
        }
        else if (utgst != 0)
        {
            layout_gst.setVisibility(View.VISIBLE);
            layout_cgst.setVisibility(View.VISIBLE);
            layout_utgst.setVisibility(View.VISIBLE);
            layout_igst.setVisibility(View.GONE);
            layout_sgst.setVisibility(View.GONE);
            sgst = 0;
            igst = 0;
            rate_gst.setText(utgst + "%");
            double cgstTax = utgst / 2.0;
            invoice_cgst.setText(cgstTax + "%");
            invoice_utgst.setText(cgstTax + "%");
        }
        else if (igst != 0)
        {
            layout_gst.setVisibility(View.VISIBLE);
            layout_igst.setVisibility(View.VISIBLE);
            layout_sgst.setVisibility(View.GONE);
            layout_utgst.setVisibility(View.GONE);
            layout_cgst.setVisibility(View.GONE);
            rate_gst.setText(igst + "%");
            invoice_igst.setText(igst + "%");
            sgst = 0;
            utgst = 0;
        }
        else
        {
            line_gst.setVisibility(View.GONE);
            layout_gst.setVisibility(View.GONE);
            layout_cgst.setVisibility(View.GONE);
            layout_sgst.setVisibility(View.GONE);
            layout_utgst.setVisibility(View.GONE);
            layout_igst.setVisibility(View.GONE);
            sgst = 0;
            igst = 0;
            utgst = 0;
        }

        if (shipping_charges != 0)
        {
            extraTaxLayout++;
            layout_create_shipping.setVisibility(View.VISIBLE);
            txt_shipping_charges.setText("+ "+getResources().getString(R.string.rupee_sign) +String.valueOf(shipping_charges));
        }
        else
        {
            extraTaxLayout--;
            layout_create_shipping.setVisibility(View.GONE);
            shipping_charges = 0;
        }

        if (discount != 0)
        {
            extraTaxLayout++;
            layout_create_discount.setVisibility(View.VISIBLE);
            txt_discount_charges.setText("- " +getResources().getString(R.string.rupee_sign) + String.valueOf(discount));
        }
        else
        {
            extraTaxLayout--;
            layout_create_discount.setVisibility(View.GONE);
            discount = 0;
        }

        if (extraTaxLayout > 0)
        {
            line_extra.setVisibility(View.VISIBLE);
        }
        else
        {
            line_extra.setVisibility(View.GONE);
        }

        if (senderName == null || senderName.isEmpty())
        {
            txt_shopName.setText("Contact shop name");
        }
        else
        {
            txt_shopName.setText(senderName);
        }

        if (senderAddress == null || senderAddress.isEmpty())
        {
            txt_shopAddress.setText("Contact shop address");
        }
        else
        {
            txt_shopAddress.setText(senderAddress);
        }


        if (senderGstNumber == null || senderGstNumber.isEmpty())
        {
            txt_gstNumber.setText("GSTIN: " + "NA");
        }
        else
        {
            txt_gstNumber.setText("GSTIN: " + senderGstNumber);
        }

        if (progressDialog.isShowing() && !PreviewActivity.this.isDestroyed())
        {
            progressDialog.dismiss();
        }


    }

    public void initTable()
    {
        for (int i = 0; i < itemList.size(); i++)
        {

            totalAmount = totalAmount + Double.parseDouble(itemList.get(i).getTotalAmount());

            ItemPojo itemPojo = itemList.get(i);
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.invoice_row, tableLayoutItems, false);
            TextView itemName = row.findViewById(R.id.tv0);
            TextView costPerItem= row.findViewById(R.id.tv1);
            TextView quantity = row.findViewById(R.id.tv2);
            TextView totalAmount = row.findViewById(R.id.tv3);

            itemName.setText(itemPojo.getItemName());
            costPerItem.setText(getResources().getString(R.string.rupee_sign) + itemPojo.getCostPerItem());
            quantity.setText(itemPojo.getQuantity());
            totalAmount.setText(getResources().getString(R.string.rupee_sign) + itemPojo.getTotalAmount());

            tableLayoutItems.addView(row);

        }
    }


    private void sendInvoiceToSelectedUser()
    {
        if (receiverUID != null && !receiverUID.isEmpty())
        {
            //////////////////////////// Bill added to own DB
            for (int i = 0;i<itemList.size();i++)
            {
                ItemPojo itemPojo = new ItemPojo(itemList.get(i).getProductId(),itemList.get(i).getItemName(),itemList.get(i).getCostPerItem(),itemList.get(i).getQuantity(),itemList.get(i).getTotalAmount());
                billItems.put(itemList.get(i).getItemName(),itemPojo);
            }

            final DocumentReference documentReference = db.collection("Users").document(firebaseUser.getUid()).
                    collection("Contacts").document(receiverMobileNumber);
            receiverPojo = new ContactPojo(receiverName, receiverAddress, receiverGstNumber,
                    receiverMobileNumber, receiverUID,newCustomerNumberInvoices + 1,invoiceDate);


            senderPojo = new ContactPojo(senderName,senderAddress,senderGstNumber,senderMobileNumber,
                    senderUID,newCustomerNumberInvoices + 1,invoiceDate);

            GstPojo gstPojo = new GstPojo(sgst,igst,utgst,shipping_charges,discount);

            documentReference.set(receiverPojo);
            InvoicePojo invoicePojo = new InvoicePojo(receiverPojo,senderPojo,invoiceNumber,subTotal,billItems,
                    invoiceDate, dueDate, gstPojo,"","Due","Sent",timestampString,billImages);

            documentReference.collection("Invoices").document(invoiceDate + " && " + timestampString).set(invoicePojo);



            ////////////////////////////Notification send

            String message ;
            if (!sessionManager.getShop_name().isEmpty())
            {
                message = sessionManager.getShop_name() + " sent you an invoice";
            }
            else
            {
                message = firebaseUser.getPhoneNumber() + " sent you an invoice";
            }

            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("deviceId", receiverUID);
            HashMap<String,Object> objectHashMap = new HashMap<>();

            NotificationPojo notificationPojo = new
                    NotificationPojo("Invoice Received",message,
                    ""+ System.currentTimeMillis());

            objectHashMap.put("data",notificationPojo);
            hashMap.put("payload",objectHashMap);

            CommonRequest.getInstance(this).sendNotification(hashMap);


            //////////////////////////////////Bill added to customer DB

            DocumentReference documentReferenceAnotherUser = db.collection("Users").document(receiverUID).
                    collection("Contacts").document(firebaseUser.getPhoneNumber());


            documentReferenceAnotherUser.set(senderPojo);

            GstPojo gstPojoAnotherUser = new GstPojo(sgst, igst, utgst, shipping_charges, discount);
            InvoicePojo invoicePojoAnotherUser = new InvoicePojo(receiverPojo,senderPojo, invoiceNumber, subTotal, billItems,
                    invoiceDate, dueDate, gstPojoAnotherUser, "", "Due", "Recieved", timestampString, billImages);

            documentReferenceAnotherUser.collection("Invoices").document(invoiceDate + " && " + timestampString).set(invoicePojoAnotherUser);
            Toast.makeText(this, "Invoice sent", Toast.LENGTH_SHORT).show();
            //EventBus.getDefault().postSticky(new EventClearBill());
            finish();
            startActivity(new Intent(PreviewActivity.this,MainActivity.class));
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("It seems customer is not on Bill Store")
                    .setCancelable(true)
                    .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            invite();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Share Invoice", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    printButton();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void invite()
    {
        String shareBody = getString(R.string.invite_link);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I love using Bill Store");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
        EventBus.getDefault().postSticky(new EventClearBill());
        finish();
    }

    public void printButton()
    {

        if (!progressDialog.isShowing() && !PreviewActivity.this.isDestroyed())
        {
            progressDialog.show();
        }

        if (receiverUID != null && !receiverUID.isEmpty())
        {
            for (int i = 0; i < itemList.size(); i++) {
                ItemPojo itemPojo = new ItemPojo(itemList.get(i).getProductId(), itemList.get(i).getItemName(), itemList.get(i).getCostPerItem(), itemList.get(i).getQuantity(), itemList.get(i).getTotalAmount());
                billItems.put(itemList.get(i).getItemName(), itemPojo);
            }

            receiverPojo = new ContactPojo(receiverName, receiverAddress, receiverGstNumber,
                    receiverMobileNumber, receiverUID, newCustomerNumberInvoices + 1, invoiceDate);

            senderPojo = new ContactPojo(senderName, senderAddress, senderGstNumber, senderMobileNumber,
                    senderUID, newCustomerNumberInvoices + 1, invoiceDate);

            final DocumentReference documentReference = db.collection("Users").document(firebaseUser.getUid()).
                    collection("Contacts").document(receiverMobileNumber);
            ContactPojo contactPojo = new ContactPojo(receiverName, receiverAddress, receiverGstNumber,
                    receiverMobileNumber, receiverUID, newCustomerNumberInvoices + 1, invoiceDate);
            GstPojo gstPojo = new GstPojo(sgst, igst, utgst, shipping_charges, discount);

            documentReference.set(contactPojo);
            InvoicePojo invoicePojo = new InvoicePojo(contactPojo, senderPojo, invoiceNumber, subTotal, billItems,
                    invoiceDate, dueDate, gstPojo, "", "Shared", "Sent", timestampString, billImages);

            documentReference.collection("Invoices").document(invoiceDate + " && " + timestampString).set(invoicePojo);


            if (progressDialog.isShowing() && !PreviewActivity.this.isDestroyed()) {
                progressDialog.dismiss();
            }

            if (boolean_permission) {
                bitmap = loadBitmapFromView(scrollView, scrollView.getWidth(), scrollView.getChildAt(0).getHeight());
                createPdf();
            } else {
                fn_permission();
            }

        }
    }

    private void createPdf()
    {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels ;
        float scrollHeight = scrollView.getChildAt(0).getHeight();
        float width = displaymetrics.widthPixels ;

        int convertHeight = (int) scrollHeight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/"+"Invoice.pdf";
        String target = Environment.getExternalStorageDirectory().getPath() + "test.pdf";
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            openPdf();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    private void openPdf()
    {
        Intent intent;
        File file=new File(Environment.getExternalStorageDirectory()
                + File.separator + "Invoice.pdf");

        /*Uri uri = FileProvider.getUriForFile(PreviewActivity.this, getPackageName() + ".provider", file);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Please choose app"));
        EventBus.getDefault().postSticky(new EventClearBill());
        finish();*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri uri = FileProvider.getUriForFile(PreviewActivity.this, getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

            EventBus.getDefault().postSticky(new EventClearBill());
            finish();
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent = Intent.createChooser(intent, "Open File");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            EventBus.getDefault().postSticky(new EventClearBill());
            finish();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
                printButton();

            } else if (permissions.length > 0)
            {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String autoGenerateInvoiceNumber()
    {
        double doublea = (Math.random() * 46656);
        double doubleb = (Math.random() * 46656);
        String a = String.valueOf(doublea);
        String b = String.valueOf(doubleb);
        String firstPart = "000" + a;
        String secondPart = "000" + b;
        String result;
        result = firstPart.substring(a.length() - 4,a.length()) + secondPart.substring(b.length() - 4,b.length());
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
