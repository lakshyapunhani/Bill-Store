package com.example.wuntu.billstore.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.wuntu.billstore.Adapters.ProductAdapter;
import com.example.wuntu.billstore.AddItemActivity;
import com.example.wuntu.billstore.AddNewBillActivity;
import com.example.wuntu.billstore.EventBus.ItemToMakeBill;
import com.example.wuntu.billstore.Pojos.ItemPojo;
import com.example.wuntu.billstore.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeBillFragment extends Fragment {

    @BindView(R.id.btn_save)
    TextView btn_save;

    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    @BindView(R.id.invoice_date) TextView invoice_date;

    LinearLayoutManager mLayoutManager;

    ProductAdapter productAdapter;

    private Context mContext;

    private ArrayList<ItemPojo> itemList;

    Calendar myCalendar = Calendar.getInstance();

    SimpleDateFormat convertDf = new SimpleDateFormat("MMMM dd, yyyy");

    public MakeBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_bill, container, false);
        ButterKnife.bind(this,view);

        itemList = new ArrayList<>();
        productAdapter = new ProductAdapter(itemList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_items.setLayoutManager(mLayoutManager);
        recycler_items.setItemAnimator(new DefaultItemAnimator());
        recycler_items.setAdapter(productAdapter);

        long date = System.currentTimeMillis();

        String dateString = convertDf.format(date);
        invoice_date.setText(dateString);

        return view;
    }
    @OnClick(R.id.btn_save)
    public void click()
    {

    }

    @OnClick(R.id.layout_invoiced_items_header)
    public void headerClick()
    {
        Intent intent = new Intent(mContext,AddItemActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(ItemToMakeBill event)
    {
        ItemPojo itemPojo = new ItemPojo(event.getItemName(),event.getCostPerItem(),event.getQuantity(),event.getTotalAmount());
        itemList.add(itemPojo);
        productAdapter.notifyDataSetChanged();
        EventBus.getDefault().removeAllStickyEvents();
    };

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

    @OnClick(R.id.invoice_date)
    public void onClickPickDate()
    {
        DatePickerDialog datePickerDialog = new  DatePickerDialog(mContext, R.style.DatePickerTheme, mDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
                              int selectedDay) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            //SimpleDateFormat convertDf = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                Date reportDate = df.parse(selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay);
                invoice_date.setText((convertDf.format(reportDate)));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

    };
}
