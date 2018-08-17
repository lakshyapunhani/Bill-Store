package com.fabuleux.wuntu.billstore.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.fabuleux.wuntu.billstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsActivity extends AppCompatActivity {

    @BindView(R.id.btn_addContact)
    FloatingActionButton btn_addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_addContact)
    public void addContact()
    {
        startActivity(new Intent(this,AddContactActivity.class));
    }
}
