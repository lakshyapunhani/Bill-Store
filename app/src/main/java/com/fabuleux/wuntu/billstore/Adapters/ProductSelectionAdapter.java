package com.fabuleux.wuntu.billstore.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;
import com.fabuleux.wuntu.billstore.Pojos.ProductModel;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSelectionAdapter extends RecyclerView.Adapter<ProductSelectionAdapter.ViewHolder> implements SectionIndexer
{
    private static final long REP_DELAY = 50;
    private ArrayList<Integer> mSectionPositions;
    int number;
    Context context;
    List<ItemSelectionPojo> mDataArray;
    HashMap<String,Integer> hashMap;
    private Handler repeatUpdateHandler = new Handler();

    public int mValue;

    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    class RptUpdater implements Runnable {
        private ViewHolder viewHolder;
        private int position;
        public RptUpdater(ViewHolder viewHolder,int position)
        {
            this.viewHolder = viewHolder;
            this.position = position;
        }

        public void run()
        {
            if( mAutoIncrement )
            {
                increment(viewHolder,position);
                repeatUpdateHandler.postDelayed( new RptUpdater(viewHolder,position), REP_DELAY );
            } else if( mAutoDecrement)
            {
                decrement(viewHolder,position);
                repeatUpdateHandler.postDelayed( new RptUpdater(viewHolder,position), REP_DELAY );
            }
        }
    }

    public void decrement(ViewHolder viewHolder,int position)
    {
        mValue = hashMap.get(mDataArray.get(position).getId());
        if (mValue <= 0)
        {
            mAutoDecrement =false;
            viewHolder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
            return;
        }
        viewHolder.img_minus.setImageResource(R.drawable.ic_minus);
        mValue--;
        viewHolder.tv_numberProducts.setText( ""+mValue);
        hashMap.put(mDataArray.get(position).getId(),mValue);
        mDataArray.get(position).setNumProducts(mValue);
        RealmManager.updateNumItem(mDataArray.get(position).getId(),mValue);
    }

    public void increment(ViewHolder viewHolder,int position)
    {
        viewHolder.img_minus.setImageResource(R.drawable.ic_minus);
        mValue = hashMap.get(mDataArray.get(position).getId());
        mValue++;
        viewHolder.tv_numberProducts.setText( ""+mValue );
        hashMap.put(mDataArray.get(position).getId(),mValue);
        mDataArray.get(position).setNumProducts(mValue);
        RealmManager.updateNumItem(mDataArray.get(position).getId(),mValue);
    }

    public ProductSelectionAdapter(List<ItemSelectionPojo> mDataArray)
    {
        this.mDataArray = mDataArray;
        hashMap = new HashMap<>();
        for (int i = 0;i<mDataArray.size();i++)
        {
            hashMap.put(mDataArray.get(i).getId(), mDataArray.get(i).getNumProducts());
        }
    }

    @Override
    public int getItemCount()
    {
        if (this.mDataArray == null)
            return 0;
        return this.mDataArray.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {

        number = hashMap.get(mDataArray.get(position).getId());
        holder.tv_productName.setText(mDataArray.get(position).getName());
        holder.tv_numberProducts.setText(String.valueOf(hashMap.get(mDataArray.get(position).getId())));

        if (number == 0)
        {
            holder.img_minus.setEnabled(false);
            holder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
        }
        else
        {
            holder.img_minus.setEnabled(true);
            holder.img_minus.setImageResource(R.drawable.ic_minus);
        }

        holder.tv_numberProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final Dialog dialog=new Dialog(context,R.style.ThemeWithCorners);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater layoutInflater=LayoutInflater.from(context);
                View view1=layoutInflater.inflate(R.layout.dialog_edit_quantity_order,null);
                final android.widget.EditText edt_productQty = view1.findViewById(R.id.edt_productQty);
                Button btn_setQty = view1.findViewById(R.id.btn_setQty);
                edt_productQty.setText(String.valueOf(hashMap.get(mDataArray.get(position).getId())));
                btn_setQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        number = Integer.parseInt(edt_productQty.getText().toString());
                        if (number == 0)
                        {
                            holder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
                        }
                        if (number > 0)
                        {
                            holder.img_minus.setImageResource(R.drawable.ic_minus);
                        }
                        mDataArray.get(position).setNumProducts(number);
                        RealmManager.updateNumItem(mDataArray.get(position).getId(),number);
                        hashMap.put(mDataArray.get(position).getId(),number);
                        holder.tv_numberProducts.setText(String.valueOf(number));
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view1);
                dialog.show();
            }
        });

        holder.img_minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                number = hashMap.get(mDataArray.get(position).getId());

                if (number == 0)
                {
                    holder.img_minus.setEnabled(false);
                    holder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
                    holder.tv_numberProducts.setText(String.valueOf(number));
                    hashMap.put(mDataArray.get(position).getId(),number);
                    RealmManager.updateNumItem(mDataArray.get(position).getId(),number);
                    return;
                }

                if (number == 1)
                {
                    holder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
                }
                number--;
                hashMap.put(mDataArray.get(position).getId(),number);
                holder.tv_numberProducts.setText(String.valueOf(number));
                mDataArray.get(position).setNumProducts(number);
                RealmManager.updateNumItem(mDataArray.get(position).getId(),number);
            }
        });

        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                number = hashMap.get(mDataArray.get(position).getId());
                holder.img_minus.setEnabled(true);
                holder.img_minus.setImageResource(R.drawable.ic_minus);
                number++;
                hashMap.put(mDataArray.get(position).getId(),number);
                holder.tv_numberProducts.setText(String.valueOf(number));
                mDataArray.get(position).setNumProducts(number);
                RealmManager.updateNumItem(mDataArray.get(position).getId(),number);
            }
        });

        holder.img_plus.setOnLongClickListener(
                new View.OnLongClickListener()
                {
                    public boolean onLongClick(View arg0)
                    {
                        mAutoIncrement = true;
                        repeatUpdateHandler.post( new RptUpdater(holder,position) );
                        return false;
                    }
                }
        );

        holder.img_plus.setOnTouchListener( new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement ){
                    mAutoIncrement = false;
                }
                return false;
            }
        });

        holder.img_minus.setOnLongClickListener(
                new View.OnLongClickListener()
                {
                    public boolean onLongClick(View arg0)
                    {
                        if (number == 0)
                        {
                            mAutoDecrement = false;
                            holder.img_minus.setEnabled(false);
                            holder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
                            return false;
                        }
                        mAutoDecrement = true;
                        repeatUpdateHandler.post( new RptUpdater(holder,position));
                        return false;
                    }
                }
        );

        holder.img_minus.setOnTouchListener( new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement )
                {
                    if (number == 0)
                    {
                        mAutoDecrement = false;
                        holder.img_minus.setEnabled(false);
                        holder.img_minus.setImageResource(R.drawable.ic_minus_freeze);
                        return false;
                    }
                    mAutoDecrement = false;
                }
                return false;
            }
        });

        holder.tv_productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog=new Dialog(context,R.style.ThemeWithCorners);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                LayoutInflater layoutInflater=LayoutInflater.from(context);
                View view1=layoutInflater.inflate(R.layout.dialog_item,null);
                TextView productName = (TextView) view1.findViewById(R.id.productName);
                TextView productPrice = (TextView) view1.findViewById(R.id.productPrice);
                TextView productQty = (TextView) view1.findViewById(R.id.productQty);
                TextView productMrp = (TextView) view1.findViewById(R.id.productMrp);
                TextView productDesc = (TextView) view1.findViewById(R.id.productDescription);
                Button btn_dismiss = (Button) view1.findViewById(R.id.btn_dismiss);
                btn_dismiss.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                productName.setText(mDataArray.get(position).getName());
                if (mDataArray.get(position).getBoxQty() != null)
                {
                    productQty.setText(mDataArray.get(position).getBoxQty());
                }
                else
                {
                    productQty.setText("NA");
                }

                if (mDataArray.get(position).getRate() != null)
                {
                    productPrice.setText(mDataArray.get(position).getRate());
                }
                else
                {
                    productPrice.setText("NA");
                }

                if (mDataArray.get(position).getDesc() != null)
                {
                    productDesc.setText(mDataArray.get(position).getDesc());
                }
                else
                {
                    productDesc.setText("NA");
                }
                if (mDataArray.get(position).getMrp() != null)
                {
                    productMrp.setText(mDataArray.get(position).getMrp());
                }
                else
                {
                    productMrp.setText("NA");
                }
                dialog.setContentView(view1);
                dialog.show();
            }
        });
    }


    @Override
    public int getSectionForPosition(int position)
    {
        return 0;
    }

    @Override
    public Object[] getSections()
    {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = this.mDataArray.size(); i < size; i++)
        {
            String section = String.valueOf(this.mDataArray.get(i).getName().charAt(0)).toUpperCase();
            if (!sections.contains(section))
            {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_productName)
        TextView tv_productName;

        @BindView(R.id.img_minus)
        ImageView img_minus;

        @BindView(R.id.img_plus) ImageView img_plus;

        @BindView(R.id.tv_numberProducts)
        TextView tv_numberProducts;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
