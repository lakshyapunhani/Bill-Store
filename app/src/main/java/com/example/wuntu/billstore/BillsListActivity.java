package com.example.wuntu.billstore;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.wuntu.billstore.EventBus.InternetStatus;
import com.example.wuntu.billstore.Fragments.CustomerBillListFragment;
import com.example.wuntu.billstore.Fragments.VendorBillListFragment;
import com.example.wuntu.billstore.Manager.SessionManager;
import com.example.wuntu.billstore.Utils.NetworkReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillsListActivity extends AppCompatActivity {

    @BindView(R.id.listContainer)
    FrameLayout listContainer;
    CustomerBillListFragment customerBillListFragment;
    VendorBillListFragment sellerBillListFragment;
    String string = "",vendorName = "";

    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_list);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();
        customerBillListFragment = new CustomerBillListFragment();
        sellerBillListFragment = new VendorBillListFragment();
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
