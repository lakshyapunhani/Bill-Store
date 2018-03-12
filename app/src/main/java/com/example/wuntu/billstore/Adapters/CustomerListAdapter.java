package com.example.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wuntu.billstore.Pojos.CustomerDetails;
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 12-03-2018.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<CustomerDetails> customerDetailsList;

    private CustomerDetails customerDetails;

    public CustomerListAdapter(ArrayList<CustomerDetails> customerDetailsList) {
        this.customerDetailsList = customerDetailsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.traderName)
        TextView traderName;

        @BindView(R.id.traderAddress)
        TextView traderAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.traderview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            customerDetails = customerDetailsList.get(position);
            ((ViewHolder) holder).traderName.setText(customerDetails.getCustomerName());
            ((ViewHolder) holder).traderAddress.setText(customerDetails.getCustomerAddress());
        }
    }

    @Override
    public int getItemCount() {
        return this.customerDetailsList.size();
    }
}
