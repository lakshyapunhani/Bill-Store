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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Activity.ExtraTaxesActivity;
import com.fabuleux.wuntu.billstore.Adapters.InvoicePreviewAdapter;
import com.fabuleux.wuntu.billstore.Dialogs.SearchableSpinner;
import com.fabuleux.wuntu.billstore.EventBus.EventClearBill;
import com.fabuleux.wuntu.billstore.EventBus.SendItemsEvent;
import com.fabuleux.wuntu.billstore.Manager.RealmManager;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;
import com.fabuleux.wuntu.billstore.Activity.PreviewActivity;
import com.fabuleux.wuntu.billstore.Activity.ProductSelectionActivity;
import com.fabuleux.wuntu.billstore.R;
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
public class MakeBillFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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

    //@BindView(R.id.spinner_gst_rate)
    Spinner spinner_gst_rate;

    ArrayList<String> gstRateList;

    ArrayAdapter<String> gstRateAdapter;

    SessionManager sessionManager;

    LinearLayoutManager mLayoutManager;

    InvoicePreviewAdapter invoicePreviewAdapter;

    private Context mContext;

    private ArrayList<ItemPojo> itemList;

    Calendar myCalendar = Calendar.getInstance();

    SimpleDateFormat convertDf = new SimpleDateFormat("yyyy-MM-dd");

    boolean customerView;

    ArrayList<ContactPojo> customersList;
    ArrayList<String> customerNameList;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    String newCustomerName ="",newCustomerAddress = "",newCustomerGstNumber ="";
    String invoiceDate = "";

    long timestamp;
    String timestampString;

    ArrayAdapter<String> spinnerAdapter;
    int customerSpinnerValue;

    int flag =0;

    int gstPosition;

    double gstRate;

    double cgst = 0,sgst = 0,igst = 0;


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

        gstRateList = new ArrayList<>();

        //getGstList();


        //gstRateList = sessionManager.getGstSlabList();
        /*if (gstRateList != null)
        {

        }*/

//        spinner_gst_rate.setOnItemSelectedListener(this);

        long date = System.currentTimeMillis();

        String dateString = convertDf.format(date);
        //invoice_date.setText(dateString);

        spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, customerNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setTitle("Select Contact");
        customerSpinner.setAdapter(spinnerAdapter);

        getCustomerList();

        return view;
    }

    private void getGstList()
    {
        CollectionReference gstReference = db.collection("GstSlabs");

        gstReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                gstRateList.add(document.getId());
                            }

                            gstRateAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_dropdown_item,gstRateList);
                            spinner_gst_rate.setAdapter(gstRateAdapter);
                            spinner_gst_rate.setSelection(gstRateList.size() - 1);

                        } else
                        {
                            Toast.makeText(mContext, "Error in GST document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        flag = 1;
        Intent intent = new Intent(mContext,ProductSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.spinner_gst_rate)
        {

            //Toast.makeText(mContext, spinner_gst_rate.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            gstPosition = pos;
            getGstRate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getGstRate()
    {
        switch (gstPosition)
        {
            case 0:
                cgst = 2.5;
                sgst = 2.5;
                igst = 0;
                gstRate = 0.05;
                break;
            case 1:
                cgst = 6;
                sgst = 6;
                igst = 0;
                gstRate = 0.12;
                break;
            case 2:
                cgst = 9;
                sgst = 9;
                igst = 0;
                gstRate = 0.18;
                break;
            case 3:
                cgst = 14;
                sgst = 14;
                igst = 0;
                gstRate = 0.28;
                break;
            case 4:
                cgst = 0;
                sgst = 0;
                igst = 5;
                gstRate = 0.05;
                break;
            case 5:
                cgst = 0;
                sgst = 0;
                igst = 12;
                gstRate = 0.12;
                break;
            case 6:
                cgst = 0;
                sgst = 0;
                igst = 18;
                gstRate = 0.18;
                break;
            case 7:
                cgst = 0;
                sgst = 0;
                igst = 28;
                gstRate = 0.28;
                break;
            case 8:
                cgst = 0;
                sgst = 0;
                igst = 0;
                gstRate = 0;
                break;
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onClearEvent(EventClearBill event)
    {
        itemList.clear();
        invoicePreviewAdapter.notifyDataSetChanged();
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onStart() {
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
        timestamp = System.currentTimeMillis();
        timestampString = String.valueOf(timestamp);

        if (customerNameList.size() == 0)
        {
            Toast.makeText(mContext, "Please add new customer", Toast.LENGTH_SHORT).show();
            return;
        }
        newCustomerName = customersList.get(customerSpinnerValue).getContactName();
        newCustomerAddress = customersList.get(customerSpinnerValue).getContactAddress();
        if (!customersList.get(customerSpinnerValue).getContactGstNumber().isEmpty())
        {
            newCustomerGstNumber = customersList.get(customerSpinnerValue).getContactGstNumber();
        }

        sendDatatoPreview();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(SendItemsEvent itemsEvent)
    {
        if (itemsEvent.getFlag().matches("1") && flag == 1)
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
        }

    }


    private void sendDatatoPreview()
    {
        Intent intent = new Intent(mContext, PreviewActivity.class);
        intent.putParcelableArrayListExtra("ItemList",itemList);
        intent.putExtra("Customer Name",newCustomerName);
        intent.putExtra("Customer Address",newCustomerAddress);
        intent.putExtra("Customer GST Number",newCustomerGstNumber);
        intent.putExtra("Invoice Date",invoiceDate);
        intent.putExtra("showSave",true);
        intent.putExtra("gstValue",gstRate);
        intent.putExtra("cgst",cgst);
        intent.putExtra("sgst",sgst);
        intent.putExtra("igst",igst);


        startActivity(intent);
    }

    @OnClick(R.id.img_editTaxes)
    public void editTaxes()
    {
        Intent intent = new Intent(mContext, ExtraTaxesActivity.class);
        startActivity(intent);
    }
}
