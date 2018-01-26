package com.example.wuntu.billstore.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wuntu.billstore.Fragments.DocumentFragment;
import com.example.wuntu.billstore.R;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 1/26/2018.
 */

public class BillDocumentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<String> keyList;
    private List<String> valuesList;
    private String[] keys;
    private Context context;

    public BillDocumentsAdapter(List<String> keyList, List<String> valuesList)
    {
        this.keyList = keyList;
        this.valuesList = valuesList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_document_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        String url = valuesList.get(position);
        String name = keyList.get(position);

        String extension = getFileExt(name);

        if (extension.equalsIgnoreCase("txt"))
        {
            ((ViewHolder)holder).imageView.setImageResource(R.drawable.ic_txt);
        }
        else if (extension.equalsIgnoreCase("pdf"))
        {
            ((ViewHolder)holder).imageView.setImageResource(R.drawable.ic_pdf);
        }
        else if (extension.equalsIgnoreCase("docx"))
        {
            ((ViewHolder)holder).imageView.setImageResource(R.drawable.ic_word);
        }
        else if (extension.equalsIgnoreCase("xls"))
        {
            ((ViewHolder)holder).imageView.setImageResource(R.drawable.ic_file);
        }
        else if (extension.equalsIgnoreCase("ppt"))
        {
            ((ViewHolder)holder).imageView.setImageResource(R.drawable.ic_ppt);
        }
        else
        {
            Glide.with(context).load(url)
                    .into(((ViewHolder)holder).imageView);
        }







    }

    @NonNull
    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }


    @Override
    public int getItemCount() {
        return this.valuesList.size();
    }
}
