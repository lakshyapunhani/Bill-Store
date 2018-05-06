package com.fabuleux.wuntu.billstore.Pojos;

import com.fabuleux.wuntu.billstore.R;

import tellh.com.stickyheaderview_rv.adapter.DataBean;
import tellh.com.stickyheaderview_rv.adapter.StickyHeaderViewAdapter;

/**
 * Created by roadcast on 6/5/18.
 */

public class ItemHeaderSticky extends DataBean {
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public ItemHeaderSticky(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public int getItemLayoutId(StickyHeaderViewAdapter adapter) {
        return R.layout.product_item_header;
    }

    @Override
    public boolean shouldSticky() {
        return true;
    }
}
