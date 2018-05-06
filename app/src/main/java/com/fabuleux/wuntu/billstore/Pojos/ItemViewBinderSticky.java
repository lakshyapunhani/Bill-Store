package com.fabuleux.wuntu.billstore.Pojos;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.R;

import tellh.com.stickyheaderview_rv.adapter.StickyHeaderViewAdapter;
import tellh.com.stickyheaderview_rv.adapter.ViewBinder;

/**
 * Created by roadcast on 6/5/18.
 */

public class ItemViewBinderSticky extends ViewBinder<ItemSticky, ItemViewBinderSticky.ViewHolder> {
    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(StickyHeaderViewAdapter adapter, ViewHolder holder, int position, ItemSticky entity) {
        holder.tvName.setText(entity.getProductName());
        String name = entity.getProductName().toLowerCase();
        if (name.startsWith("a"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_a);
        }
        else if (name.startsWith("b"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_b);
        }
        else if (name.startsWith("c"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_c);
        }
        else if (name.startsWith("d"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_d);
        }
        else if (name.startsWith("e"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_e);
        }
        else if (name.startsWith("f"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_f);
        }
        else if (name.startsWith("g"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_g);
        }
        else if (name.startsWith("h"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_h);
        }
        else if (name.startsWith("i"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_i);
        }
        else if (name.startsWith("j"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_j);
        }
        else if (name.startsWith("k"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_k);
        }
        else if (name.startsWith("l"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_l);
        }
        else if (name.startsWith("m"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_m);
        }
        else if (name.startsWith("n"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_n);
        }
        else if (name.startsWith("o"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_o);
        }
        else if (name.startsWith("p"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_p);
        }
        else if (name.startsWith("q"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_q);
        }
        else if (name.startsWith("r"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_r);
        }
        else if (name.startsWith("s"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_s);
        }
        else if (name.startsWith("t"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_t);
        }
        else if (name.startsWith("u"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_u);
        }
        else if (name.startsWith("v"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_v);
        }
        else if (name.startsWith("w"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_w);
        }
        else if (name.startsWith("x"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_x);
        }
        else if (name.startsWith("y"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_y);
        }
        else if (name.startsWith("z"))
        {
            holder.ivAvatar.setImageResource(R.drawable.ic_letter_z);
        }

    }

    @Override
    public int getItemLayoutId(StickyHeaderViewAdapter adapter)
    {
        return R.layout.product_item;
    }

    static class ViewHolder extends ViewBinder.ViewHolder
    {
        public ImageView ivAvatar;
        public TextView tvName;

        public ViewHolder(View rootView)
        {
            super(rootView);
            this.ivAvatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
        }

    }
}
