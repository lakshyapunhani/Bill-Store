package com.fabuleux.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fabuleux.wuntu.billstore.Pojos.Language;
import com.fabuleux.wuntu.billstore.Pojos.SelectableLanguage;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelectableAdapter extends RecyclerView.Adapter implements SelectableViewHolder.OnItemSelectedListener {

    private final List<SelectableLanguage> mValues;
    private boolean isMultiSelectionEnabled = false;

    SelectableViewHolder.OnItemSelectedListener listener;


    public LanguageSelectableAdapter(SelectableViewHolder.OnItemSelectedListener listener,
                             List<Language> items, boolean isMultiSelectionEnabled,String languagePreference) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (Language item : items)
        {
            if (item.getLanguageCode().matches(languagePreference))
            {
                mValues.add(new SelectableLanguage(item,true));
            }
            else
            {
                mValues.add(new SelectableLanguage(item, false));
            }

        }
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checked_item, parent, false);

        return new SelectableViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableLanguage selectableItem = mValues.get(position);
        String name = selectableItem.getLanguage();
        holder.textView.setText(name);

        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Language> getSelectedItems() {

        List<Language> selectedItems = new ArrayList<>();
        for (SelectableLanguage item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableLanguage item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableLanguage selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}
