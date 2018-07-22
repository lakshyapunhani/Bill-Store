package com.fabuleux.wuntu.billstore;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Adapters.LanguageSelectableAdapter;
import com.fabuleux.wuntu.billstore.Adapters.SelectableViewHolder;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.Language;
import com.fabuleux.wuntu.billstore.Pojos.SelectableLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageSelectionActivity extends AppCompatActivity implements SelectableViewHolder.OnItemSelectedListener{

    @BindView(R.id.languageList)
    RecyclerView languageList;

    @BindView(R.id.btn_done)
    TextView btn_done;

    @BindView(R.id.btn_next) TextView btn_next;

    LanguageSelectableAdapter adapter;

    SessionManager sessionManager;

    private String selectedLanguage = "en";

    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null)
        {
            flag = getIntent().getIntExtra("flag",0);
        }

        if (flag == 1)
        {
            btn_done.setVisibility(View.VISIBLE);
            btn_next.setVisibility(View.GONE);
        }
        else
        {
            btn_done.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
        }

        sessionManager = new SessionManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        languageList.setLayoutManager(layoutManager);
        List<Language> selectableItems = generateLanguages();
        adapter = new LanguageSelectableAdapter(this,selectableItems,false,sessionManager.getLanguagePreference());
        languageList.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(SelectableLanguage selectableLanguage)
    {
        List<Language> selectedItems = adapter.getSelectedItems();
        if (selectableLanguage.getLanguage().matches("English"))
        {
            selectedLanguage = "en";
        }
        else if (selectableLanguage.getLanguage().matches("हिंदी"))
        {
            selectedLanguage = "hi";
        }
        else if (selectableLanguage.getLanguage().matches("ਪੰਜਾਬੀ"))
        {
            selectedLanguage = "pa";
        }
        else if (selectableLanguage.getLanguage().matches("বাঙালি"))
        {
            selectedLanguage = "bn";
        }
        else if (selectableLanguage.getLanguage().matches("ગુજરાતી"))
        {
            selectedLanguage = "gu";
        }
        else if (selectableLanguage.getLanguage().matches("ಕನ್ನಡ"))
        {
            selectedLanguage = "kn";
        }
        else if (selectableLanguage.getLanguage().matches("മലയാളം"))
        {
            selectedLanguage = "ml";
        }
        else if (selectableLanguage.getLanguage().matches("मराठी"))
        {
            selectedLanguage = "mr";
        }
        else if (selectableLanguage.getLanguage().matches("தமிழ்"))
        {
            selectedLanguage = "ta";
        }
        else if (selectableLanguage.getLanguage().matches("తెలుగు"))
        {
            selectedLanguage = "te";
        }
        //Toast.makeText(this, "Selected Language is " +  selectableItem.getLanguage(), Toast.LENGTH_SHORT).show();
    }

    public List<Language> generateLanguages(){

        List<Language> selectableItems = new ArrayList<>();
        selectableItems.add(new Language("English","en"));
        selectableItems.add(new Language("हिंदी","hi"));
        selectableItems.add(new Language("ਪੰਜਾਬੀ","pa"));
        selectableItems.add(new Language("বাঙালি","bn"));
        selectableItems.add(new Language("ગુજરાતી","gu"));
        selectableItems.add(new Language("ಕನ್ನಡ","kn"));
        selectableItems.add(new Language("മലയാളം","ml"));
        selectableItems.add(new Language("मराठी","mr"));
        selectableItems.add(new Language("தமிழ்","ta"));
        selectableItems.add(new Language("తెలుగు","te"));

        return selectableItems;
    }

    @OnClick(R.id.btn_next)
    public void nextClick()
    {
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        sessionManager.saveLanguagePreference(selectedLanguage);

        Intent refresh = new Intent(LanguageSelectionActivity.this, RegisterActivity.class);
        startActivity(refresh);
    }

    @OnClick(R.id.btn_done)
    public void doneClick()
    {
        Locale locale = new Locale(selectedLanguage);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        sessionManager.saveLanguagePreference(selectedLanguage);
        Intent refresh = new Intent(LanguageSelectionActivity.this,MainActivity.class);
        refresh.putExtra("fragmentFlag","home");
        startActivity(refresh);
    }

}
