package com.fabuleux.wuntu.billstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.LanguageSelectableAdapter;
import com.fabuleux.wuntu.billstore.Adapters.SelectableViewHolder;
import com.fabuleux.wuntu.billstore.Pojos.Language;
import com.fabuleux.wuntu.billstore.Pojos.SelectableLanguage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.adapters.SelectableAdapter;

public class LanguageSelectionActivity extends AppCompatActivity implements SelectableViewHolder.OnItemSelectedListener{

    @BindView(R.id.languageList)
    RecyclerView languageList;

    LanguageSelectableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        languageList.setLayoutManager(layoutManager);
        List<Language> selectableItems = generateItems();
        adapter = new LanguageSelectableAdapter(this,selectableItems,false);
        languageList.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(SelectableLanguage selectableItem) {

        List<Language> selectedItems = adapter.getSelectedItems();
        Toast.makeText(this, "Selected Language is " +  selectableItem.getLanguage(), Toast.LENGTH_SHORT).show();
    }

    public List<Language> generateItems(){

        List<Language> selectableItems = new ArrayList<>();
        selectableItems.add(new Language("English"));
        selectableItems.add(new Language("Hindi"));
        selectableItems.add(new Language("Punjabi"));
        return selectableItems;
    }

}
