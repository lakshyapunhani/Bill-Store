package com.example.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wuntu.billstore.Pojos.BillDetails;
import com.example.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 1/21/2018.
 */

public class BillsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<BillDetails> billsList= new ArrayList<>();

    private BillDetails billDetails;


    public BillsListAdapter(ArrayList<BillDetails> billsList)
    {
        this.billsList = billsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.bill_amount)
        TextView bill_amount;

        @BindView(R.id.bill_date)
        TextView bill_date;

        @BindView(R.id.bill_status)
        TextView bill_status;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bills_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        billDetails = billsList.get(position);
        ((ViewHolder)holder).bill_date.setText(billDetails.getBillDate());
        ((ViewHolder)holder).bill_amount.setText(billDetails.getBillAmount());
        ((ViewHolder)holder).bill_status.setText(billDetails.getBillStatus());

    }

    @Override
    public int getItemCount() {
        return this.billsList.size();
    }
}
