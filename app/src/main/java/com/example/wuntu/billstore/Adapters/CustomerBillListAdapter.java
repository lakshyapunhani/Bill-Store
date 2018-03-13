package com.example.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wuntu.billstore.Pojos.AddBillDetails;
import com.example.wuntu.billstore.Pojos.CustomerDetails;
import com.example.wuntu.billstore.Pojos.MakeBillDetails;
import com.example.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 13-03-2018.
 */

public class CustomerBillListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<MakeBillDetails> billsList= new ArrayList<>();

    private MakeBillDetails makeBillDetails;


    public CustomerBillListAdapter(ArrayList<MakeBillDetails> billsList)
    {
        this.billsList = billsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.item_amount)
        TextView bill_amount;

        @BindView(R.id.item_date)
        TextView bill_date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        makeBillDetails = billsList.get(position);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails = makeBillDetails.getCustomerDetails();
        ((ViewHolder)holder).bill_date.setText(makeBillDetails.getBillTime());
        //((ViewHolder)holder).bill_amount.setText(makeBillDetails.getBillAmount());

    }

    @Override
    public int getItemCount() {
        return this.billsList.size();
    }
}

