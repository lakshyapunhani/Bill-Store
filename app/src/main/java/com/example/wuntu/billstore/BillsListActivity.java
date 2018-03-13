package com.example.wuntu.billstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.wuntu.billstore.Fragments.CustomerBillListFragment;
import com.example.wuntu.billstore.Fragments.SellerBillListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillsListActivity extends AppCompatActivity {

    @BindView(R.id.listContainer)
    FrameLayout listContainer;
    CustomerBillListFragment customerBillListFragment;
    SellerBillListFragment sellerBillListFragment;
    String string = "",vendorName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_list);
        ButterKnife.bind(this);
        customerBillListFragment = new CustomerBillListFragment();
        sellerBillListFragment = new SellerBillListFragment();
        if (getIntent().getStringExtra("fragment") != null)
        {
            string = getIntent().getStringExtra("fragment");
            vendorName = getIntent().getStringExtra("VendorName");
        }

        if (string.matches("seller"))
        {
            Bundle bundle = new Bundle();
            bundle.putString("VendorName",vendorName);
            sellerBillListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.listContainer,sellerBillListFragment).commit();
        }
        else if (string.matches("customer"))
        {   Bundle bundle = new Bundle();
            bundle.putString("VendorName",vendorName);
            customerBillListFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.listContainer,customerBillListFragment).commit();
        }
    }
}
