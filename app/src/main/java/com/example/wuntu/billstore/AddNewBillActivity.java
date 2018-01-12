package com.example.wuntu.billstore;

import android.app.DatePickerDialog;
import android.net.ParseException;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.AddDocumentsAdapter;
import com.example.wuntu.billstore.Utils.SearchableSpinner;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class AddNewBillActivity extends AppCompatActivity {

    AddDocumentsAdapter addDocumentsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    ArrayList<String> arrayList;

    @BindView(R.id.vendorSpinner)
    SearchableSpinner vendorSpinner;

    @BindView(R.id.radio_existingVendor)
    RadioButton radio_existingVendor;

    @BindView(R.id.radio_newVendor)
    RadioButton radio_newVendor;

    @BindView(R.id.innerView_existingVendor)
    LinearLayout innerView_existingVendor;

    @BindView(R.id.innerView_newVendor)
    LinearLayout innerView_newVendor;

    @BindView(R.id.outerView_newVendor)
    LinearLayout outerView_newVendor;

    @BindView(R.id.outerView_existingVendor)
    LinearLayout outerView_existingVendor;

    @BindView(R.id.text_pickDate)
    TextView text_pickDate;

    Calendar myCalendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_new_bill);

        ButterKnife.bind(this);


        arrayList = new ArrayList<>();

        addDocumentsAdapter = new AddDocumentsAdapter(arrayList);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setAdapter(addDocumentsAdapter);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(spinnerAdapter);

    }


    DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
                              int selectedDay) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat convertDf = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                Date reportDate = df.parse(selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay);
                text_pickDate.setText((convertDf.format(reportDate)));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

    };

    @OnClick({R.id.view_existingVendor,R.id.radio_existingVendor})
    public void existingVendorViewClick()
    {
        radio_existingVendor.setChecked(true);
        radio_newVendor.setChecked(false);
        TransitionManager.beginDelayedTransition(outerView_newVendor);
        innerView_newVendor.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(outerView_existingVendor);
        innerView_existingVendor.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.view_newVendor,R.id.radio_newVendor})
    public void newVendorViewClick()
    {
        radio_newVendor.setChecked(true);
        radio_existingVendor.setChecked(false);
        TransitionManager.beginDelayedTransition(outerView_newVendor);
        innerView_newVendor.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(outerView_existingVendor);
        innerView_existingVendor.setVisibility(View.GONE);
    }

    @OnClick(R.id.text_pickDate)
    public void onClickPickDate()
    {
        DatePickerDialog datePickerDialog = new  DatePickerDialog(AddNewBillActivity.this, R.style.DatePickerTheme, mDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    @OnClick(R.id.radio_due)
    public void onDueRadioButtonClicked()
    {
        Toast.makeText(this, "Radio Due Checked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.radio_paid)
    public void onPaidRadioButtonClicked()
    {
        Toast.makeText(this, "Radio Paid Checked", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_out_down);
    }
}
