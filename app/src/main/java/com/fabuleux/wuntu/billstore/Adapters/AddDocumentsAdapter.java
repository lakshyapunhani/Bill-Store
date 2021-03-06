package com.fabuleux.wuntu.billstore.Adapters;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 1/10/2018.
 */

public class AddDocumentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 0;
    private final int VIEW_FOOT = 1;

    private Context context;

    private boolean show_footer = true;

    private ArrayList<String> arrayList;

    public AddDocumentsAdapter(ArrayList<String> arrayList)
    {
        this.arrayList = arrayList;
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder
    {
        @BindView(R.id.imageView)
        ImageView imageView;

        public MyViewHolder1(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }

    public class FooterViewHolder1 extends RecyclerView.ViewHolder {
        public FooterViewHolder1(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (show_footer)
        {
            if (isPositionFooter(position))
                return VIEW_FOOT;
            return VIEW_ITEM;
        }
        else return VIEW_ITEM;

    }

    private boolean isPositionFooter(int position) {
        return position == this.arrayList.size () ;
    }

    public void hideFooter()
    {
        show_footer = false;
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

        } else if (viewType == VIEW_FOOT){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_add_document, parent, false);
            return new FooterViewHolder1(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof MyViewHolder1)
        {

            String path = arrayList.get(position);
            Uri uri = Uri.parse(path);

            Log.d("TAG","" + position + " " + uri);
            String extension;

            if(path.contains(".")) {
                extension = path.substring(path.lastIndexOf(".") + 1);
                Log.d("Position","" + position + " " + extension );
            }
            else extension = " ";


            if (extension.equalsIgnoreCase("txt"))
            {
                ((MyViewHolder1)holder).imageView.setImageResource(R.drawable.ic_txt);
            }
            else if (extension.equalsIgnoreCase("pdf"))
            {
                ((MyViewHolder1)holder).imageView.setImageResource(R.drawable.ic_pdf);
            }
            else if (extension.equalsIgnoreCase("docx"))
            {
                ((MyViewHolder1)holder).imageView.setImageResource(R.drawable.ic_word);
            }
            else if (extension.equalsIgnoreCase("xls"))
            {
                ((MyViewHolder1)holder).imageView.setImageResource(R.drawable.ic_file);
            }
            else if (extension.equalsIgnoreCase("ppt"))
            {
                ((MyViewHolder1)holder).imageView.setImageResource(R.drawable.ic_ppt);
            }
            else
            {
                ((MyViewHolder1)holder).imageView.setImageURI(uri);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (show_footer)
        {
            return this.arrayList.size() + 1;
        }
        else return this.arrayList.size();

    }
}