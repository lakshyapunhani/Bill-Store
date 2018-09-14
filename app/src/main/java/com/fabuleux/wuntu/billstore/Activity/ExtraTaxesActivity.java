package com.fabuleux.wuntu.billstore.Activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fabuleux.wuntu.billstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtraTaxesActivity extends AppCompatActivity
{


    //////////////CGST
    @BindView(R.id.toggle_cgst)
    ToggleButton toggle_cgst;

    @BindView(R.id.card_cgst)
    CardView card_cgst;

    @BindView(R.id.cgst_twentyEight)
    TextView cgst_twentyEight;

    @BindView(R.id.cgst_eighteen)
    TextView cgst_eighteen;

    @BindView(R.id.cgst_twelve)
    TextView cgst_twelve;

    @BindView(R.id.cgst_five)
    TextView cgst_five;

    //////////////SGST
    @BindView(R.id.toggle_sgst)
    ToggleButton toggle_sgst;

    @BindView(R.id.card_sgst)
    CardView card_sgst;

    @BindView(R.id.sgst_twentyEight)
    TextView sgst_twentyEight;

    @BindView(R.id.sgst_eighteen)
    TextView sgst_eighteen;

    @BindView(R.id.sgst_twelve)
    TextView sgst_twelve;

    @BindView(R.id.sgst_five)
    TextView sgst_five;

    //////////////IGST
    @BindView(R.id.toggle_igst)
    ToggleButton toggle_igst;

    @BindView(R.id.card_igst)
    CardView card_igst;

    @BindView(R.id.igst_twentyEight)
    TextView igst_twentyEight;

    @BindView(R.id.igst_eighteen)
    TextView igst_eighteen;

    @BindView(R.id.igst_twelve)
    TextView igst_twelve;

    @BindView(R.id.igst_five)
    TextView igst_five;

    //////////////UTGST
    @BindView(R.id.toggle_utgst)
    ToggleButton toggle_utgst;

    @BindView(R.id.card_utgst)
    CardView card_utgst;

    @BindView(R.id.utgst_twentyEight)
    TextView utgst_twentyEight;

    @BindView(R.id.utgst_eighteen)
    TextView utgst_eighteen;

    @BindView(R.id.utgst_twelve)
    TextView utgst_twelve;

    @BindView(R.id.utgst_five)
    TextView utgst_five;

    ///////////////Shipping charges
    @BindView(R.id.edt_shippingCharges)
    EditText edt_shippingCharges;

    @BindView(R.id.toggle_shipping)
    ToggleButton toggle_shipping;

    /////////////////Discount

    @BindView(R.id.edt_discount)
    EditText edt_discount;

    @BindView(R.id.toggle_discount)
    ToggleButton toggle_discount;

    ///////////Round off
    @BindView(R.id.toggle_roundOff)
    ToggleButton toggle_roundOff;

    boolean cgstFive,cgstTwelve,cgstEighteen,cgstTwentyEight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_taxes);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.toggle_cgst)
    public void toggleCGST()
    {
        if (toggle_cgst.isChecked())
        {
            TransitionManager.beginDelayedTransition(card_cgst);
            card_cgst.setVisibility(View.VISIBLE);
        }
        else
        {
            TransitionManager.beginDelayedTransition(card_cgst);
            card_cgst.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toggle_sgst)
    public void toggleSGST()
    {
        if (toggle_sgst.isChecked())
        {
            TransitionManager.beginDelayedTransition(card_sgst);
            card_sgst.setVisibility(View.VISIBLE);
        }
        else
        {
            TransitionManager.beginDelayedTransition(card_cgst);
            card_sgst.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toggle_igst)
    public void toggleIGST()
    {
        if (toggle_igst.isChecked())
        {
            TransitionManager.beginDelayedTransition(card_igst);
            card_igst.setVisibility(View.VISIBLE);
        }
        else
        {
            TransitionManager.beginDelayedTransition(card_igst);
            card_igst.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toggle_utgst)
    public void toggleUTGST()
    {
        if (toggle_utgst.isChecked())
        {
            TransitionManager.beginDelayedTransition(card_utgst);
            card_utgst.setVisibility(View.VISIBLE);
        }
        else
        {
            TransitionManager.beginDelayedTransition(card_utgst);
            card_utgst.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toggle_shipping)
    public void toggleShipping()
    {
        if (toggle_shipping.isChecked())
        {
            //TransitionManager.beginDelayedTransition(edt_shippingCharges);
            edt_shippingCharges.setVisibility(View.VISIBLE);
        }
        else
        {
            //TransitionManager.beginDelayedTransition(edt_shippingCharges);
            edt_shippingCharges.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toggle_discount)
    public void toggleDiscount()
    {
        if (toggle_discount.isChecked())
        {
            //TransitionManager.beginDelayedTransition(edt_shippingCharges);
            edt_discount.setVisibility(View.VISIBLE);
        }
        else
        {
            //TransitionManager.beginDelayedTransition(edt_shippingCharges);
            edt_discount.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.cgst_five)
    public void changeCgstFiveStroke()
    {

        cgstTwentyEight = false;
        cgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        cgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        cgstEighteen = false;
        cgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        cgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        cgstTwelve = false;
        cgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        cgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        if (cgstFive)
        {
            cgstFive = false;
            cgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
            cgst_five.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            cgstFive = true;
            cgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent_blue));
            cgst_five.setTextColor(getResources().getColor(R.color.white));

        }

    }

    @OnClick(R.id.cgst_twelve)
    public void changeCgstTwelveStroke()
    {
        cgstFive = false;
        cgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        cgst_five.setTextColor(getResources().getColor(R.color.grey));

        cgstTwentyEight = false;
        cgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        cgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        cgstEighteen = false;
        cgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        cgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        if (cgstTwelve)
        {
            cgstTwelve = false;
            cgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
            cgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            cgstTwelve = true;
            cgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent_blue));
            cgst_twelve.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.cgst_eighteen)
    public void changeCgstEighteenStroke()
    {
        cgstTwelve = false;
        cgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        cgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        cgstFive = false;
        cgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        cgst_five.setTextColor(getResources().getColor(R.color.grey));

        cgstTwentyEight = false;
        cgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        cgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        if (cgstEighteen)
        {
            cgstEighteen = false;
            cgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
            cgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            cgstEighteen = true;
            cgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent_blue));
            cgst_eighteen.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.cgst_twentyEight)
    public void changeCgstTwentyEightStroke()
    {

        cgstEighteen = false;
        cgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        cgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        cgstTwelve = false;
        cgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        cgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        cgstFive = false;
        cgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        cgst_five.setTextColor(getResources().getColor(R.color.grey));

        if (cgstTwentyEight)
        {
            cgstTwentyEight = false;
            cgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
            cgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            cgstTwentyEight = true;
            cgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent_blue));
            cgst_twentyEight.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.back_arrow_extras)
    public void finishActivity()
    {
        finish();
    }
}
