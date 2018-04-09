package com.fabuleux.wuntu.billstore.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by roadcast on 19/2/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private ArrayList<ItemPojo> itemList;

    public ProductAdapter(ArrayList<ItemPojo> itemList)
    {
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.itemName)
        TextView itemName;

        @BindView(R.id.totalAmount) TextView totalAmount;

        @BindView(R.id.quantity) TextView quantity;

        @BindView(R.id.img_delete)
        ImageView img_delete;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_item, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        ((MyViewHolder)holder).itemName.setText(itemList.get(position).getItemName());
        ((MyViewHolder)holder).quantity.setText(itemList.get(position).getQuantity() + " " + itemList.get(position).getItemType() +  " * " + context.getString(R.string.rupee_sign) + itemList.get(position).getCostPerItem() );
        ((MyViewHolder)holder).totalAmount.setText(context.getString(R.string.rupee_sign) + itemList.get(position).getTotalAmount());

        ((MyViewHolder)holder).img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(position);
            }
        });
    }

    private void showAlertDialog(final int position)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Order");
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

    }


    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    private void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }
}
