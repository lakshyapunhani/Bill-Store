package com.example.wuntu.billstore.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wuntu.billstore.Pojos.ItemPojo;
import com.example.wuntu.billstore.R;

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

    private class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.itemName)
        TextView itemName;

        @BindView(R.id.totalAmount) TextView totalAmount;

        @BindView(R.id.quantity) TextView quantity;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 5;
    }
}
