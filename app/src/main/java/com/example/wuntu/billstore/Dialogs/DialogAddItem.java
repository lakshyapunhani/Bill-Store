package com.example.wuntu.billstore.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.example.wuntu.billstore.R;

import butterknife.ButterKnife;

/**
 * Created by Dell on 16-Feb-18.
 */

public class DialogAddItem extends Dialog
{
    public DialogAddItem(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_item);

        ButterKnife.bind(this);
    }
}
