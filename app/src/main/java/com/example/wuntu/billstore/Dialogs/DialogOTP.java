package com.example.wuntu.billstore.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.EventBus.EventOTP;
import com.example.wuntu.billstore.EventBus.ResendOTPEvent;
import com.example.wuntu.billstore.R;
import com.example.wuntu.billstore.SignInActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dell on 12/27/2017.
 */

public class DialogOTP extends Dialog implements TextWatcher,View.OnKeyListener,View.OnFocusChangeListener
{
    @BindView(R.id.edt_code1)
    EditText editcode1;

    @BindView(R.id.edt_code2)
    EditText editcode2;

    @BindView(R.id.edt_code3)
    EditText editcode3;

    @BindView(R.id.edt_code4)
    EditText editcode4;

    @BindView(R.id.edt_code5)
    EditText editcode5;

    @BindView(R.id.edt_code6)
    EditText editcode6;

    @BindView(R.id.verification_message)
    TextView verification_message;

    private int whoHasFocus;

    private char[] code = new char[6];

    String number = "";

    ProgressDialog progressDialog;


    public DialogOTP(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_otp);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Verifying");

        verification_message.setText("You will get an SMS with a verification number to this number +91" + number);
        setListeners();
    }

    private void setListeners()
    {
        editcode1.addTextChangedListener(this);
        editcode2.addTextChangedListener(this);
        editcode3.addTextChangedListener(this);
        editcode4.addTextChangedListener(this);
        editcode5.addTextChangedListener(this);
        editcode6.addTextChangedListener(this);

        editcode1.setOnKeyListener(this);
        editcode2.setOnKeyListener(this);
        editcode3.setOnKeyListener(this);
        editcode4.setOnKeyListener(this);
        editcode5.setOnKeyListener(this);
        editcode6.setOnKeyListener(this);

        editcode1.setOnFocusChangeListener(this);
        editcode2.setOnFocusChangeListener(this);
        editcode3.setOnFocusChangeListener(this);
        editcode4.setOnFocusChangeListener(this);
        editcode5.setOnFocusChangeListener(this);
        editcode6.setOnFocusChangeListener(this);
    }

    @OnClick(R.id.btn_submitOTP)
    public void submit()
    {

        progressDialog.show();
        if (!editcode1.getText().toString().equals("") &&
                !editcode2.getText().toString().equals("") &&
                !editcode3.getText().toString().equals("") &&
                !editcode4.getText().toString().equals("") &&
                !editcode5.getText().toString().equals("") &&
                !editcode6.getText().toString().equals(""))
        {
            String mCode = editcode1.getText().toString() + editcode2.getText().toString()
                    + editcode3.getText().toString() + editcode4.getText().toString()
                    + editcode5.getText().toString() + editcode6.getText().toString();

            if (progressDialog.isShowing())
            {
                progressDialog.hide();
            }
            EventBus.getDefault().post(new EventOTP(mCode));
        }else {
            if (progressDialog.isShowing())
            {
                progressDialog.hide();
            }
            Toast.makeText(getContext(), "Everything Wrong Bro", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeMessage(String number)
    {
        this.number = number;
        //verification_message.setText("You will get an SMS with a verification number to this number +91" + number);
    }

    @OnClick(R.id.img_editNumber)
    public void editNumber()
    {
        dismiss();
    }

    @OnClick(R.id.text_SendOtpAgain)
    public void sendOTPAgain()
    {
        Toast.makeText(getContext(), "OTP Resent", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new ResendOTPEvent());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        switch (whoHasFocus)
        {
            case 1:
                if(!editcode1.getText().toString().isEmpty()) {
                    editcode2.setHint("");
                    editcode3.setHint("");
                    editcode4.setHint("");
                    editcode5.setHint("");
                    editcode6.setHint("");
                    code[0]= editcode1.getText().toString().charAt(0);
                    editcode2.requestFocus();
                }
                else {
                    editcode2.setHint("*");
                    editcode3.setHint("*");
                    editcode4.setHint("*");
                    editcode5.setHint("*");
                    editcode6.setHint("*");
                }
                break;

            case 2:
                if(!editcode2.getText().toString().isEmpty())
                {
                    code[1]= editcode2.getText().toString().charAt(0);
                    editcode3.requestFocus();
                }
                break;

            case 3:
                if(!editcode3.getText().toString().isEmpty())
                {
                    code[2]= editcode3.getText().toString().charAt(0);
                    editcode4.requestFocus();
                }
                break;

            case 4:
                if(!editcode4.getText().toString().isEmpty())
                {
                    code[3]= editcode4.getText().toString().charAt(0);
                    editcode5.requestFocus();
                }
                break;

            case 5:
                if(!editcode5.getText().toString().isEmpty())
                {
                    code[4]= editcode5.getText().toString().charAt(0);
                    editcode6.requestFocus();
                }
                break;

            case 6:
                if(!editcode6.getText().toString().isEmpty())
                {
                    code[5]= editcode6.getText().toString().charAt(0);
                }
                break;


            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b)
    {
        switch(view.getId())
        {
            case R.id.edt_code1:
                whoHasFocus=1;
                break;

            case R.id.edt_code2:
                whoHasFocus=2;
                break;

            case R.id.edt_code3:
                whoHasFocus=3;
                break;

            case R.id.edt_code4:
                whoHasFocus=4;
                break;

            case R.id.edt_code5:
                whoHasFocus=5;
                break;

            case R.id.edt_code6:
                whoHasFocus=6;
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (i == KeyEvent.KEYCODE_DEL)
            {
                switch(view.getId())
                {
                    case R.id.edt_code2:
                        editcode2.clearFocus();
                        editcode1.requestFocus();
                        break;

                    case R.id.edt_code3:
                        editcode3.clearFocus();
                        editcode2.requestFocus();
                        break;

                    case R.id.edt_code4:
                        editcode4.clearFocus();
                        editcode3.requestFocus();
                        break;

                    case R.id.edt_code5:
                        editcode5.clearFocus();
                        editcode4.requestFocus();
                        break;

                    case R.id.edt_code6:
                        editcode6.clearFocus();
                        editcode5.requestFocus();
                        break;

                    default:
                        break;
                }
            }
        }
        return false;
    }

}
