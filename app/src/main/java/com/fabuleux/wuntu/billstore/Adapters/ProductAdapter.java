package com.fabuleux.wuntu.billstore.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.ProductModel;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;

    private Context context;

    private ArrayList<ProductModel> arrayList;

    public ProductAdapter(ArrayList<ProductModel> arrayList)
    {
        this.arrayList = arrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        @BindView(R.id.hiddenLayout)
        LinearLayout hiddenLayout;

        @BindView(R.id.iv_productImage)
        ImageView iv_productImage;

        @BindView(R.id.tv_productname)
        TextView tv_productName;

        @BindView(R.id.detailsLayout)
        LinearLayout detailsLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        /*final boolean isExpanded = position==mExpandedPosition;
        ((ViewHolder)holder).hiddenLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        ((ViewHolder) holder).mainLayout.setActivated(isExpanded);
        ((ViewHolder) holder).mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(position);
            }
        });*/

        final ProductModel productModel = arrayList.get(position);

        final boolean isExpanded = position==mExpandedPosition;
        ((ViewHolder)holder).hiddenLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        ((ViewHolder)holder).mainLayout.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        ((ViewHolder)holder).mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });

        String name = arrayList.get(position).getProductName();

        ((ViewHolder)holder).tv_productName.setText(name);

        if (name.startsWith("a"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_a);
        }
        else if (name.startsWith("b"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_b);
        }
        else if (name.startsWith("c"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_c);
        }
        else if (name.startsWith("d"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_d);
        }
        else if (name.startsWith("e"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_e);
        }
        else if (name.startsWith("f"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_f);
        }
        else if (name.startsWith("g"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_g);
        }
        else if (name.startsWith("h"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_h);
        }
        else if (name.startsWith("i"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_i);
        }
        else if (name.startsWith("j"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_j);
        }
        else if (name.startsWith("k"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_k);
        }
        else if (name.startsWith("l"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_l);
        }
        else if (name.startsWith("m"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_m);
        }
        else if (name.startsWith("n"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_n);
        }
        else if (name.startsWith("o"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_o);
        }
        else if (name.startsWith("p"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_p);
        }
        else if (name.startsWith("q"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_q);
        }
        else if (name.startsWith("r"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_r);
        }
        else if (name.startsWith("s"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_s);
        }
        else if (name.startsWith("t"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_t);
        }
        else if (name.startsWith("u"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_u);
        }
        else if (name.startsWith("v"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_v);
        }
        else if (name.startsWith("w"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_w);
        }
        else if (name.startsWith("x"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_x);
        }
        else if (name.startsWith("y"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_y);
        }
        else if (name.startsWith("z"))
        {
            ((ViewHolder) holder).iv_productImage.setImageResource(R.drawable.ic_letter_z);
        }

        ((ViewHolder)holder).detailsLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(context,R.style.ThemeWithCorners);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater layoutInflater=LayoutInflater.from(context);
                View view1=layoutInflater.inflate(R.layout.dialog_item,null);
                TextView productName = (TextView) view1.findViewById(R.id.productName);
                TextView productPrice = (TextView) view1.findViewById(R.id.productPrice);
                TextView productDesc = (TextView) view1.findViewById(R.id.productDescription);
                Button btn_dismiss = (Button) view1.findViewById(R.id.btn_dismiss);
                btn_dismiss.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
                productName.setText(productModel.getProductName());

                if (productModel.getProductRate() != null)
                {
                    productPrice.setText(productModel.getProductRate());
                }
                else
                {
                    productPrice.setText("NA");
                }

                if (productModel.getProductDescription() != null)
                {
                    productDesc.setText(productModel.getProductDescription());
                }
                else
                {
                    productDesc.setText("NA");
                }
                dialog.setContentView(view1);
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
