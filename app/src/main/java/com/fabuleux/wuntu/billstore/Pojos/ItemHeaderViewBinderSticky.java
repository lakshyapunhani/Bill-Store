package com.fabuleux.wuntu.billstore.Pojos;

import android.view.View;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.R;

import tellh.com.stickyheaderview_rv.adapter.StickyHeaderViewAdapter;
import tellh.com.stickyheaderview_rv.adapter.ViewBinder;

/**
 * Created by roadcast on 6/5/18.
 */

public class ItemHeaderViewBinderSticky extends ViewBinder<ItemHeaderSticky, ItemHeaderViewBinderSticky.ViewHolder> {

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(StickyHeaderViewAdapter adapter, ViewHolder holder, int position, ItemHeaderSticky entity) {
        holder.tvPrefix.setText(entity.getPrefix());
    }

    @Override
    public int getItemLayoutId(StickyHeaderViewAdapter adapter) {
        return R.layout.product_item_header;
    }


    static class ViewHolder extends ViewBinder.ViewHolder {
        TextView tvPrefix;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tvPrefix = (TextView) rootView.findViewById(R.id.tv_prefix);
        }

    }
}
