package com.example.wuntu.billstore.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wuntu.billstore.R;

import java.util.ArrayList;

/**
 * Created by Dell on 1/10/2018.
 */

public class AddDocumentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 0;
    private final int VIEW_PROG = 1;

    private Context context;

    private ArrayList<String> arrayList;

    public AddDocumentsAdapter(ArrayList<String> arrayList)
    {
        this.arrayList = arrayList;
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public MyViewHolder1(View view) {
            super(view);

        }
    }

    public class FooterViewHolder1 extends RecyclerView.ViewHolder {
        public FooterViewHolder1(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
            if (isPositionItem(position))
                return VIEW_ITEM;
            return VIEW_PROG;



    }

    private boolean isPositionItem(int position) {
        return position != getItemCount() -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context = parent.getContext();

        if (viewType == VIEW_ITEM)
        {
            View v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_document_item, parent, false);
            return new MyViewHolder1(v);

        } else if (viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_add_document, parent, false);
            return new FooterViewHolder1(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}