package com.fabuleux.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.AddBillDetails;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.InvoicePojo;
import com.fabuleux.wuntu.billstore.Pojos.MakeBillDetails;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 13-03-2018.
 */

public class InvoicesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<InvoicePojo> billsList= new ArrayList<>();

    private InvoicePojo makeBillDetails;


    public InvoicesListAdapter(ArrayList<InvoicePojo> billsList)
    {
        this.billsList = billsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.invoice_number)
        TextView invoice_number;

        @BindView(R.id.item_amount)
        TextView bill_amount;

        @BindView(R.id.invoiceStatus)
        TextView invoiceStatus;

        @BindView(R.id.item_date)
        TextView bill_date;

        @BindView(R.id.img_typeInvoice)
        ImageView img_typeInvoice;

        @BindView(R.id.txt_typeInvoice)
        TextView txt_typeInvoice;

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
        ((ViewHolder)holder).invoice_number.setText("Invoice No : " + makeBillDetails.getInvoiceNumber());
        ((ViewHolder)holder).bill_date.setText(makeBillDetails.getInvoiceDate());
        ((ViewHolder)holder).bill_amount.setText("" +makeBillDetails.getBillAmount());
        ((ViewHolder)holder).txt_typeInvoice.setText(makeBillDetails.getBillType());
        ((ViewHolder)holder).invoiceStatus.setText(makeBillDetails.getBillStatus());

        if (makeBillDetails.getBillType().matches("Sent"))
        {
            ((ViewHolder)holder).img_typeInvoice.setImageResource(R.drawable.ic_invoice_sent);
        }
        else if (makeBillDetails.getBillType().matches("Received"))
        {
            ((ViewHolder)holder).img_typeInvoice.setImageResource(R.drawable.ic_invoice_recieve);
        }
        else if (makeBillDetails.getBillType().matches("Shared"))
        {
            ((ViewHolder)holder).img_typeInvoice.setImageResource(R.drawable.ic_invoice_added);
        }
        else
        {
            ((ViewHolder)holder).img_typeInvoice.setImageResource(R.drawable.ic_invoice_added);
        }

    }

    @Override
    public int getItemCount() {
        return this.billsList.size();
    }
}

