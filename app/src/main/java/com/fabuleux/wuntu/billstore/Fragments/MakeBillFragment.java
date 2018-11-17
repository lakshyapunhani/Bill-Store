package com.fabuleux.wuntu.billstore.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Activity.ExtraTaxesActivity;
import com.fabuleux.wuntu.billstore.Activity.PreviewActivity;
import com.fabuleux.wuntu.billstore.Activity.ProductSelectionActivity;
import com.fabuleux.wuntu.billstore.Adapters.InvoicePreviewAdapter;
import com.fabuleux.wuntu.billstore.Dialogs.SearchableSpinner;
import com.fabuleux.wuntu.billstore.EventBus.EventClearBill;
import com.fabuleux.wuntu.billstore.EventBus.SendExtraDetails;
import com.fabuleux.wuntu.billstore.EventBus.SendItemsEvent;
import com.fabuleux.wuntu.billstore.Manager.RealmManager;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.ExtraDetailsPojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeBillFragment extends Fragment {

    @BindView(R.id.btn_preview)
    TextView btn_preview;

    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    @BindView(R.id.invoice_date) TextView invoice_date;

    @BindView(R.id.due_date) TextView due_date;

    @BindView(R.id.customerSpinner)
    SearchableSpinner customerSpinner;

    @BindView(R.id.img_addItems)
    ImageView img_addItems;

    ExtraDetailsPojo extraDetailsPojo;

    //////////////////////Billing details
    @BindView(R.id.invoice_total) TextView invoice_total;

    @BindView(R.id.layout_gst) LinearLayout layout_gst;

    @BindView(R.id.rate_gst) TextView rate_gst;

    @BindView(R.id.layout_create_cgst) LinearLayout layout_create_cgst;

    @BindView(R.id.rate_cgst) TextView rate_cgst;

    @BindView(R.id.layout_create_sgst) LinearLayout layout_create_sgst;

    @BindView(R.id.rate_sgst) TextView rate_sgst;

    @BindView(R.id.layout_create_utgst) LinearLayout layout_create_utgst;

    @BindView(R.id.rate_utgst) TextView rate_utgst;

    @BindView(R.id.layout_create_igst) LinearLayout layout_create_igst;

    @BindView(R.id.rate_igst) TextView rate_igst;

    @BindView(R.id.layout_create_shipping) LinearLayout layout_create_shipping;

    @BindView(R.id.txt_shipping_charges) TextView txt_shipping_charges;

    @BindView(R.id.layout_create_discount) LinearLayout layout_create_discount;

    @BindView(R.id.txt_discount_charges) TextView txt_discount_charges;

    @BindView(R.id.layout_create_roundoff) LinearLayout layout_create_roundoff;

    @BindView(R.id.txt_roundOff) TextView txt_roundOff;

    @BindView(R.id.invoice_subTotal) TextView invoice_subTotal;

    SessionManager sessionManager;

    LinearLayoutManager mLayoutManager;

    InvoicePreviewAdapter invoicePreviewAdapter;

    private Context mContext;

    private ArrayList<ItemPojo> itemList;

    Calendar myCalendar = Calendar.getInstance();

    SimpleDateFormat convertDf = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<ContactPojo> customersList;
    ArrayList<String> customerNameList;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    String receiverName ="", receiverAddress = "", receiverGSTNumber ="",
            receiverMobileNumber = "", receiverUID = "";
    int customerNumberInvoices;

    String invoiceDate = "",dueDate = "";

    long timestamp;
    String timestampString;

    ArrayAdapter<String> spinnerAdapter;
    int customerSpinnerValue;

    int itemFlag = 0,taxesFlag = 0;

    double gstRate;

    double sgst = 0,igst = 0,utgst = 0;

    double totalAmount = 0;

    double subTotal = 0;

    double shipping_charges = 0,discount = 0;

    String dateString;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public MakeBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_invoice, container, false);
        ButterKnife.bind(this,view);

        customerNameList = new ArrayList<>();
        customersList = new ArrayList<>();
        itemList = new ArrayList<>();
        invoicePreviewAdapter = new InvoicePreviewAdapter(itemList);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        sessionManager = new SessionManager(mContext);

        firebaseUser = firebaseAuth.getCurrentUser();

        mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_items.setLayoutManager(mLayoutManager);
        recycler_items.setItemAnimator(new DefaultItemAnimator());
        recycler_items.setAdapter(invoicePreviewAdapter);

        spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, customerNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setTitle("Select Contact");
        customerSpinner.setAdapter(spinnerAdapter);

        long date = System.currentTimeMillis();

        dateString = convertDf.format(date);
        invoice_date.setText(dateString);
        due_date.setText(dateString);

        getCustomerList();

        return view;
    }

    private void getCustomerList()
    {
        CollectionReference contactReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");

        contactReference.orderBy("contactName").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    Toast.makeText(mContext, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                customersList.clear();
                customerNameList.clear();
                if (documentSnapshots.isEmpty())
                {
                    return;
                }
                for (DocumentSnapshot documentSnapshot : documentSnapshots)
                {
                    ContactPojo contactDetails = documentSnapshot.toObject(ContactPojo.class);
                    customersList.add(contactDetails);
                    customerNameList.add(contactDetails.getContactName());
                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.img_addItems)
    public void headerClick()
    {
        itemFlag = 1;
        Intent intent = new Intent(mContext,ProductSelectionActivity.class);
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onClearEvent(EventClearBill event)
    {
        itemList.clear();
        invoice_total.setText("");
        rate_gst.setText("");
        rate_cgst.setText("");
        rate_sgst.setText("");
        rate_utgst.setText("");
        rate_igst.setText("");
        txt_shipping_charges.setText("");
        txt_discount_charges.setText("");
        invoice_subTotal.setText("");
        layout_gst.setVisibility(View.GONE);
        layout_create_cgst.setVisibility(View.GONE);
        layout_create_igst.setVisibility(View.GONE);
        layout_create_sgst.setVisibility(View.GONE);
        layout_create_utgst.setVisibility(View.GONE);
        layout_create_discount.setVisibility(View.GONE);
        layout_create_shipping.setVisibility(View.GONE);
        invoicePreviewAdapter.notifyDataSetChanged();
        EventBus.getDefault().removeAllStickyEvents();
        RealmManager.resetItemRealm();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.layout_invoiceDate)
    public void onClickInvoiceDate()
    {
        DatePickerDialog datePickerDialog = new  DatePickerDialog(mContext, R.style.DatePickerTheme, mDateListenerInvoice, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @OnClick(R.id.layout_dueDate)
    public void onClickDueDate()
    {
        DatePickerDialog datePickerDialog = new  DatePickerDialog(mContext, R.style.DatePickerTheme, mDateListenerDue, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener mDateListenerInvoice = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
                              int selectedDay) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date reportDate = df.parse(selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay);
                invoice_date.setText((convertDf.format(reportDate)));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

    };
    DatePickerDialog.OnDateSetListener mDateListenerDue = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
                              int selectedDay) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date reportDate = df.parse(selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay);
                due_date.setText((convertDf.format(reportDate)));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

    };

    @OnItemSelected(value = R.id.customerSpinner, callback = OnItemSelected.Callback.ITEM_SELECTED)
    void selectVehicle(AdapterView<?> adapterView, int newVal) {
        if (customersList.size()>0)
        {
            customerSpinnerValue = newVal;
        }
    }

    @OnClick(R.id.btn_preview)
    public void previewClick()
    {
        saveClick();
    }

    public void saveClick()
    {
        if (itemList.size() == 0)
        {
            Toast.makeText(mContext, "Please add atleast one item", Toast.LENGTH_SHORT).show();
            return;
        }

        invoiceDate = invoice_date.getText().toString().trim();
        dueDate = due_date.getText().toString().trim();
        timestamp = System.currentTimeMillis();
        timestampString = String.valueOf(timestamp);

        if (customerNameList.size() == 0)
        {
            Toast.makeText(mContext, "Please add new customer", Toast.LENGTH_SHORT).show();
            return;
        }
        receiverName = customersList.get(customerSpinnerValue).getContactName();
        receiverAddress = customersList.get(customerSpinnerValue).getContactAddress();
        if (!customersList.get(customerSpinnerValue).getContactGstNumber().isEmpty())
        {
            receiverGSTNumber = customersList.get(customerSpinnerValue).getContactGstNumber();
        }
        receiverMobileNumber = customersList.get(customerSpinnerValue).getContactPhoneNumber();
        receiverUID = customersList.get(customerSpinnerValue).getContactUID();
        customerNumberInvoices = customersList.get(customerSpinnerValue).getNumberInvoices();
        sendDatatoPreview();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(SendItemsEvent itemsEvent)
    {
        if (itemsEvent.getFlag().matches("1") && itemFlag == 1)
        {
            List<ItemSelectionPojo> arrayList = new ArrayList<>();
            arrayList = RealmManager.getSavedItems();
            itemList.clear();
            for (int i = 0;i<arrayList.size();i++)
            {
                int totalAmount = arrayList.get(i).getNumProducts() * Integer.parseInt(arrayList.get(i).getProductRate());
                ItemPojo itemPojo = new ItemPojo(arrayList.get(i).getProductId(),arrayList.get(i).getProductName(),arrayList.get(i).getProductRate(),String.valueOf(arrayList.get(i).getNumProducts()),String.valueOf(totalAmount));
                itemList.add(itemPojo);
            }

            invoicePreviewAdapter.notifyDataSetChanged();
            EventBus.getDefault().removeAllStickyEvents();
            totalAmount = 0;
            subTotal = 0;
            setTaxValues();
        }

    }

    private void sendDatatoPreview()
    {
        Intent intent = new Intent(mContext, PreviewActivity.class);
        intent.putParcelableArrayListExtra("ItemList",itemList);
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("receiverAddress", receiverAddress);
        intent.putExtra("receiverGSTNumber", receiverGSTNumber);
        intent.putExtra("receiverMobileNumber", receiverMobileNumber);
        intent.putExtra("receiverUID", receiverUID);
        intent.putExtra("Customer Number Invoices",customerNumberInvoices);
        intent.putExtra("Invoice Date",invoiceDate);
        intent.putExtra("Due Date",dueDate);
        intent.putExtra("showSave",true);
        intent.putExtra("sgst",sgst);
        intent.putExtra("igst",igst);
        intent.putExtra("utgst",utgst);
        intent.putExtra("shipping charge",shipping_charges);
        intent.putExtra("discount",discount);
        intent.putExtra("subTotal",subTotal);
        intent.putExtra("invoiceNumber","");
        intent.putExtra("billType","");
        intent.putExtra("senderName",sessionManager.getShop_name());
        intent.putExtra("senderAddress",sessionManager.getShop_address());
        intent.putExtra("senderGSTNumber",sessionManager.getShop_gst());
        intent.putExtra("senderMobileNumber",firebaseUser.getPhoneNumber());
        intent.putExtra("senderUID",firebaseUser.getUid());
        startActivity(intent);
    }

    @OnClick(R.id.img_editTaxes)
    public void editTaxes()
    {
        taxesFlag = 1;
        Intent intent = new Intent(mContext, ExtraTaxesActivity.class);
        intent.putExtra("ExtraTaxes",extraDetailsPojo);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(SendExtraDetails taxDetails)
    {
        if (taxDetails.getFlag().matches("1") && taxesFlag == 1 && itemList.size() > 0)
        {
            extraDetailsPojo = taxDetails.getExtraDetailsPojo();

            if (taxDetails.getExtraDetailsPojo().getSgst() != 0)
            {
                layout_gst.setVisibility(View.VISIBLE);
                layout_create_cgst.setVisibility(View.VISIBLE);
                layout_create_sgst.setVisibility(View.VISIBLE);
                layout_create_utgst.setVisibility(View.GONE);
                layout_create_igst.setVisibility(View.GONE);
                sgst = taxDetails.getExtraDetailsPojo().getSgst();
                utgst = 0;
                igst = 0;
                rate_gst.setText(taxDetails.getExtraDetailsPojo().getSgst() + "%");
                double cgstTax = taxDetails.getExtraDetailsPojo().getSgst() / 2.0;
                rate_cgst.setText(cgstTax + "%");
                rate_sgst.setText(cgstTax + "%");
            }
            else if (taxDetails.getExtraDetailsPojo().getUtgst() != 0)
            {
                layout_gst.setVisibility(View.VISIBLE);
                layout_create_cgst.setVisibility(View.VISIBLE);
                layout_create_utgst.setVisibility(View.VISIBLE);
                layout_create_igst.setVisibility(View.GONE);
                layout_create_sgst.setVisibility(View.GONE);
                utgst = taxDetails.getExtraDetailsPojo().getUtgst();
                sgst = 0;
                igst = 0;
                rate_gst.setText(taxDetails.getExtraDetailsPojo().getUtgst()+ "%");
                double cgstTax = taxDetails.getExtraDetailsPojo().getUtgst() / 2.0;
                rate_cgst.setText(cgstTax + "%");
                rate_utgst.setText(cgstTax + "%");
            }
            else if (taxDetails.getExtraDetailsPojo().getIgst() != 0)
            {
                layout_gst.setVisibility(View.VISIBLE);
                layout_create_igst.setVisibility(View.VISIBLE);
                layout_create_sgst.setVisibility(View.GONE);
                layout_create_utgst.setVisibility(View.GONE);
                layout_create_cgst.setVisibility(View.GONE);
                rate_gst.setText(taxDetails.getExtraDetailsPojo().getIgst()+ "%");
                rate_igst.setText(taxDetails.getExtraDetailsPojo().getIgst()+ "%");
                igst = taxDetails.getExtraDetailsPojo().getIgst();
                sgst = 0;
                utgst = 0;
            }
            else
            {
                layout_gst.setVisibility(View.GONE);
                layout_create_cgst.setVisibility(View.GONE);
                layout_create_sgst.setVisibility(View.GONE);
                layout_create_utgst.setVisibility(View.GONE);
                layout_create_igst.setVisibility(View.GONE);
                sgst = 0;
                igst = 0;
                utgst = 0;
            }

            if (taxDetails.getExtraDetailsPojo().getShipping_charges() != 0)
            {
                layout_create_shipping.setVisibility(View.VISIBLE);
                txt_shipping_charges.setText("+ "+getResources().getString(R.string.rupee_sign) +String.valueOf(taxDetails.getExtraDetailsPojo().getShipping_charges()));
                shipping_charges = taxDetails.getExtraDetailsPojo().getShipping_charges();
            }
            else
            {
                layout_create_shipping.setVisibility(View.GONE);
                shipping_charges = 0;
            }

            if (taxDetails.getExtraDetailsPojo().getDiscount() != 0)
            {
                layout_create_discount.setVisibility(View.VISIBLE);
                txt_discount_charges.setText("- " +getResources().getString(R.string.rupee_sign) + String.valueOf(taxDetails.getExtraDetailsPojo().getDiscount()));
                discount = taxDetails.getExtraDetailsPojo().getDiscount();
            }
            else
            {
                layout_create_discount.setVisibility(View.GONE);
                discount = 0;
            }

            totalAmount = 0;
            subTotal = 0;
            setTaxValues();
        }
    }

    private void setTaxValues()
    {


        for (int i = 0;i<itemList.size();i++)
        {
            totalAmount = totalAmount + Double.parseDouble(itemList.get(i).getTotalAmount());
        }
        invoice_total.setText(getResources().getString(R.string.rupee_sign) + String.valueOf(totalAmount));

        if (sgst != 0)
        {
            subTotal = totalAmount + (totalAmount * (sgst/100));
        }
        else if (igst != 0)
        {
            subTotal = totalAmount + (totalAmount *(igst/100));
        }
        else if (utgst != 0)
        {
            subTotal = totalAmount + (totalAmount * (utgst/100));
        }
        else
        {
            subTotal = totalAmount;
        }

        if (shipping_charges != 0)
        {
            subTotal = subTotal + shipping_charges;
        }

        if (discount != 0)
        {
            subTotal = subTotal - discount;
        }

        invoice_subTotal.setText(getResources().getString(R.string.rupee_sign) + String.valueOf(subTotal));
    }

}
