package com.example.wuntu.billstore.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.example.wuntu.billstore.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Wuntu on 25-10-2017.
 */

public class CodeSentDialog extends Dialog
{
    public CodeSentDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.code_sent_dialog_layout);

        ButterKnife.bind(this);
    }

    @Optional @OnClick(R.id.btn_ok)
    void btn_click()
    {
        dismiss();
    }


}
