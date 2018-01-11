package com.example.wuntu.billstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.example.wuntu.billstore.Adapters.AddDocumentsAdapter;
import com.example.wuntu.billstore.R;
import com.example.wuntu.billstore.Utils.SearchableSpinner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewBillActivity extends AppCompatActivity {

    AddDocumentsAdapter addDocumentsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    ArrayList<String> arrayList;

    @BindView(R.id.vendorSpinner)
    SearchableSpinner vendorSpinner;

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


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(spinnerAdapter);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_out_down);
    }
}
