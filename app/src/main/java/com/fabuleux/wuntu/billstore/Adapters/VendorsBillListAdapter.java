package com.fabuleux.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.AddBillDetails;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 1/21/2018.
 */

public class VendorsBillListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<AddBillDetails> billsList= new ArrayList<>();

    private AddBillDetails addBillDetails;


    public VendorsBillListAdapter(ArrayList<AddBillDetails> billsList)
    {
        this.billsList = billsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.invoice_number)
        TextView invoice_number;

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
        addBillDetails = billsList.get(position);
        ((ViewHolder)holder).invoice_number.setText("Bill Number : " +addBillDetails.getBillNumber());
        ((ViewHolder)holder).bill_date.setText(addBillDetails.getBillDate());
        ((ViewHolder)holder).bill_amount.setText(addBillDetails.getBillAmount());

    }

    @Override
    public int getItemCount() {
        return this.billsList.size();
    }
}
