package com.fabuleux.wuntu.billstore.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by roadcast on 19/2/18.
 */

public class InvoicePreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private ArrayList<ItemPojo> itemList;

    public InvoicePreviewAdapter(ArrayList<ItemPojo> itemList)
    {
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.itemName)
        TextView itemName;

        @BindView(R.id.itemQty) TextView itemQty;

        @BindView(R.id.itemUnitPrice) TextView itemUnitPrice;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_create_invoice, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        ((MyViewHolder)holder).itemName.setText(itemList.get(position).getItemName());
        ((MyViewHolder)holder).itemQty.setText(itemList.get(position).getQuantity());

        ((MyViewHolder)holder).itemUnitPrice.setText(context.getString(R.string.rupee_sign) +itemList.get(position).getTotalAmount());
    }

    /*private void showAlertDialog(final int position)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Item");
        builder1.setMessage("Are you sure you want to delete this item?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        removeAt(position);
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();

    }*/


    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    /*private void removeAt(int position)
    {
        *//*RealmManager.deleteParticularItem(itemList.get(position).getProductId());
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());*//*
    }*/
}
