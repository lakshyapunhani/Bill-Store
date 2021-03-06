package com.fabuleux.wuntu.billstore.Activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.EventBus.ItemToMakeBill;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 16-Feb-18.
 */

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.unitSpinner)
    Spinner unitSpinner;

    @BindView(R.id.spinner_gst_rate) Spinner spinner_gst_rate;

    @BindView(R.id.edt_itemName)
    EditText edt_itemName;

    @BindView(R.id.edt_costPerItem) EditText edt_costPerItem;

    @BindView(R.id.edt_quantity) EditText edt_quantity;

    @BindView(R.id.layout_gst)
    LinearLayout layout_gst;

    @BindView(R.id.cb_include_gst)
    CheckBox cb_include_gst;

    @BindView(R.id.edt_gstAmount) EditText edt_gstAmount;

    @BindView(R.id.edt_costPerItemGST) EditText edt_costPerItemGST;

    @BindView(R.id.edt_totalAmount) EditText edt_totalAmount;

    @BindView(R.id.btn_add)
    TextView btn_add;

    ArrayAdapter<String> unitAdapter;

    ArrayAdapter<String> gstRateAdapter;

    int unitPosition,gstPosition;
    boolean gstCheck = false;

    ArrayList<String> gstRateList;
    ArrayList<String> unitList;
    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_item);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();

        unitList = new ArrayList<>();
        unitList = sessionManager.getUnitList();
        unitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, unitList);
        unitSpinner.setAdapter(unitAdapter);
        unitSpinner.setOnItemSelectedListener(this);

        gstRateList = new ArrayList<>();
        gstRateList = sessionManager.getGstSlabList();
        gstRateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,gstRateList);
        spinner_gst_rate.setAdapter(gstRateAdapter);
        spinner_gst_rate.setOnItemSelectedListener(this);


        edt_costPerItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
                setTotalAmount();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)
            {
                setTotalAmount();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.unitSpinner)
        {
            unitPosition = pos;
        }
        else if (spinner.getId() == R.id.spinner_gst_rate)
        {
            gstPosition = pos;
            getGstRate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getGstRate()
    {
        double gstRate;
        switch (gstPosition)
        {
            case 0:
                gstRate = 0.05;
                gstCheck = true;
                calculateGstAmount(gstRate);
                break;
            case 1:
                gstRate = 0.12;
                gstCheck = true;
                calculateGstAmount(gstRate);
                break;
            case 2:
                gstRate = 0.18;
                gstCheck = true;
                calculateGstAmount(gstRate);
                break;
            case 3:
                gstRate = 0.28;
                gstCheck = true;
                calculateGstAmount(gstRate);
                break;
            case 4:
                gstRate = 0.5;
                gstCheck = false;
                calculateGstAmount(gstRate);
                break;
            case 5:
                gstRate = 0.12;
                gstCheck = false;
                calculateGstAmount(gstRate);
                break;
            case 6:
                gstRate = 0.18;
                gstCheck = false;
                calculateGstAmount(gstRate);
                break;
            case 7:
                gstRate = 0.28;
                gstCheck = false;
                calculateGstAmount(gstRate);
                break;
        }

    }

    private void calculateGstAmount(double rate)
    {
        double gstAmount,itemWithGST,totalAmount;
        if (!edt_costPerItem.getText().toString().trim().isEmpty())
        {
            int costItem = Integer.parseInt(edt_costPerItem.getText().toString().trim());
            gstAmount = costItem * rate;
            itemWithGST = costItem + gstAmount;
            edt_gstAmount.setText("" + gstAmount);
            edt_costPerItemGST.setText("" + itemWithGST);

            if (edt_quantity.getText().toString().isEmpty())
            {
                totalAmount = itemWithGST;
                edt_totalAmount.setText("" + totalAmount);
            }
            else
            {
                int qty = Integer.parseInt(edt_quantity.getText().toString().trim());
                totalAmount = qty * itemWithGST;
                edt_totalAmount.setText("" + totalAmount);
            }

        }
        else
        {
            edt_gstAmount.setText("");
            edt_costPerItemGST.setText("");
        }
    }


    private void setTotalAmount()
    {
        if (edt_costPerItem.getText().toString().trim().isEmpty())
        {
            edt_gstAmount.setText("");
            edt_costPerItemGST.setText("");
            edt_totalAmount.setText("");
        }
        else
        {
            getGstRate();
            if (!edt_quantity.getText().toString().isEmpty() && cb_include_gst.isChecked())
            {
                if (edt_costPerItemGST.getText().toString().trim().isEmpty() || edt_gstAmount.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(AddItemActivity.this, "Please fill gst details", Toast.LENGTH_SHORT).show();
                    return;
                }
                double qty = Double.parseDouble(edt_quantity.getText().toString().trim());
                double costGST = Double.parseDouble(edt_costPerItemGST.getText().toString().trim());
                double totalAmount = qty * costGST;
                edt_totalAmount.setText(""+ totalAmount);
            }
            else if (!edt_quantity.getText().toString().isEmpty() && !cb_include_gst.isChecked())
            {
                double qty = Double.parseDouble(edt_quantity.getText().toString().trim());
                double cost = Double.parseDouble(edt_costPerItem.getText().toString().trim());
                double totalAmount = qty * cost;
                edt_totalAmount.setText(""+ totalAmount);
            }
            else if (edt_quantity.getText().toString().isEmpty() && cb_include_gst.isChecked())
            {
                if (edt_costPerItemGST.getText().toString().trim().isEmpty() || edt_gstAmount.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(AddItemActivity.this, "Please fill gst details", Toast.LENGTH_SHORT).show();
                    return;
                }
                double costGST = Double.parseDouble(edt_costPerItemGST.getText().toString().trim());
                edt_totalAmount.setText("" + costGST);
            }
            else if (edt_quantity.getText().toString().isEmpty() && !cb_include_gst.isChecked())
            {
                double cost = Double.parseDouble(edt_costPerItem.getText().toString().trim());
                edt_totalAmount.setText("" + cost);
            }
        }
    }

    private void showGstLayout()
    {
        layout_gst.setVisibility(View.VISIBLE);
        if (edt_costPerItemGST.getText().toString().trim().isEmpty())
        {
            edt_totalAmount.setText("");
        }
        else
        {
            if (edt_quantity.getText().toString().trim().isEmpty())
            {
                edt_totalAmount.setText(edt_costPerItemGST.getText().toString());
            }
            else
            {
                double qty = Double.parseDouble(edt_quantity.getText().toString().trim());
                double cost = Double.parseDouble(edt_costPerItemGST.getText().toString().trim());
                double totalAmount = qty * cost;
                edt_totalAmount.setText(""+ totalAmount);
            }
        }
    }

    private void hideGstLayout()
    {
        layout_gst.setVisibility(View.GONE);
        if (edt_costPerItem.getText().toString().trim().isEmpty())
        {
            edt_totalAmount.setText("");
        }
        else
        {
            if (edt_quantity.getText().toString().trim().isEmpty())
            {
                edt_totalAmount.setText(edt_costPerItem.getText().toString());
            }
            else
            {
                double qty = Double.parseDouble(edt_quantity.getText().toString().trim());
                double cost = Double.parseDouble(edt_costPerItem.getText().toString().trim());
                double totalAmount = qty * cost;
                edt_totalAmount.setText(""+ totalAmount);
            }
        }
    }

    @OnClick(R.id.cb_include_gst)
    public void gstLayoutClick()
    {
        if (cb_include_gst.isChecked())
        {
            showGstLayout();
        }
        else if (!cb_include_gst.isChecked())
        {
            hideGstLayout();
        }
    }

    @OnClick(R.id.back_arrow)
    public void backClick()
    {
        finish();
    }

    @OnClick(R.id.btn_add)
    public void addClick()
    {
        if (edt_itemName.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edt_costPerItem.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter cost per item", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edt_quantity.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please provide quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cb_include_gst.isChecked())
        {
            if (edt_gstAmount.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "Please fill gst amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edt_costPerItemGST.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please fill total amount of item", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (edt_totalAmount.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter total amount", Toast.LENGTH_SHORT).show();
            return;
        }


        sendEventToMakeBill();

    }

    private void sendEventToMakeBill()
    {
        String itemPrice;
        String note;
        if (cb_include_gst.isChecked())
        {
            itemPrice = edt_costPerItemGST.getText().toString().trim();
            if (gstCheck)
            {
                note = "The item price is inclusive of CGST and SGST";
            }
            else
            {
                note = "The item price is inclusive of IGST";
            }
        }
        else
        {
            itemPrice = edt_costPerItem.getText().toString().trim();
            note = "";
        }
        String itemName = edt_itemName.getText().toString().trim();
        String quantity = edt_quantity.getText().toString().trim();
        String totalAmount = edt_totalAmount.getText().toString().trim();
        String itemType = unitList.get(unitPosition);
        EventBus.getDefault().postSticky(new ItemToMakeBill(itemName,itemPrice,quantity,itemType,totalAmount,note));
        finish();
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
