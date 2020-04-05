package com.fabuleux.wuntu.billstore.Activity;

import androidx.transition.TransitionManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.fabuleux.wuntu.billstore.EventBus.SendExtraDetails;
import com.fabuleux.wuntu.billstore.Pojos.ExtraDetailsPojo;
import com.fabuleux.wuntu.billstore.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtraTaxesActivity extends AppCompatActivity
{

    //////////////CGST + SGST
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

    //////////////CGST + UTGST
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

    //boolean cgstFive,cgstTwelve,cgstEighteen,cgstTwentyEight;

    boolean sgstFive,sgstTwelve,sgstEighteen,sgstTwentyEight;

    boolean igstFive,igstTwelve,igstEighteen,igstTwentyEight;

    boolean utgstFive,utgstTwelve,utgstEighteen,utgstTwentyEight;

    ExtraDetailsPojo extraDetailsPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_taxes);
        ButterKnife.bind(this);
        sgstFive = true;
        igstFive = true;
        utgstFive = true;
        if (getIntent().getParcelableExtra("ExtraTaxes") != null)
        {
            extraDetailsPojo = getIntent().getParcelableExtra("ExtraTaxes");
            if (extraDetailsPojo.getSgst() != 0)
            {
                toggle_sgst.setChecked(true);
                sgstFive = false;
                card_sgst.setVisibility(View.VISIBLE);
                if (extraDetailsPojo.getSgst() == 5)
                {
                    changeSgstFiveStroke();
                }
                else if (extraDetailsPojo.getSgst() == 12)
                {
                    changeSgstTwelveStroke();
                }
                else if (extraDetailsPojo.getSgst() == 18)
                {
                    changeSgstEighteenStroke();
                }
                else if (extraDetailsPojo.getSgst() == 28)
                {
                    changeSgstTwentyEightStroke();
                }
                toggle_igst.setChecked(false);
                card_igst.setVisibility(View.GONE);
                toggle_utgst.setChecked(false);
                card_utgst.setVisibility(View.GONE);
            }
            else if (extraDetailsPojo.getIgst() != 0)
            {
                igstFive = false;
                if (extraDetailsPojo.getIgst() == 5)
                {
                    changeIgstFiveStroke();
                }
                else if (extraDetailsPojo.getIgst() == 12)
                {
                    changeIgstTwelveStroke();
                }
                else if (extraDetailsPojo.getIgst() == 18)
                {
                    changeIgstEighteenStroke();
                }
                else if (extraDetailsPojo.getIgst() == 28)
                {
                    changeIgstTwentyEightStroke();
                }

                toggle_igst.setChecked(true);
                card_igst.setVisibility(View.VISIBLE);
                toggle_sgst.setChecked(false);
                card_sgst.setVisibility(View.GONE);
                toggle_utgst.setChecked(false);
                card_utgst.setVisibility(View.GONE);
            }
            else if (extraDetailsPojo.getUtgst() != 0)
            {
                toggle_utgst.setChecked(true);
                card_utgst.setVisibility(View.VISIBLE);
                utgstFive = false;
                if (extraDetailsPojo.getUtgst() == 5)
                {
                    changeUTgstFiveStroke();
                }
                else if (extraDetailsPojo.getUtgst() == 12)
                {
                    changeUTgstTwelveStroke();
                }
                else if (extraDetailsPojo.getUtgst() == 18)
                {
                    changeSgstEighteenStroke();
                }
                else if (extraDetailsPojo.getUtgst() == 28)
                {
                    changeUTgstTwentyEightStroke();
                }
                toggle_sgst.setChecked(false);
                card_sgst.setVisibility(View.GONE);
                toggle_igst.setChecked(false);
                card_igst.setVisibility(View.GONE);
            }
            else
            {
                toggle_utgst.setChecked(false);
                toggle_igst.setChecked(false);
                toggle_sgst.setChecked(false);
            }

            if (extraDetailsPojo.getShipping_charges() != 0)
            {
                toggle_shipping.setChecked(true);
                edt_shippingCharges.setVisibility(View.VISIBLE);
                edt_shippingCharges.setText("" +extraDetailsPojo.getShipping_charges());
            }
            else
            {
                toggle_shipping.setChecked(false);
                edt_shippingCharges.setVisibility(View.GONE);
            }

            if (extraDetailsPojo.getDiscount() != 0)
            {
                toggle_discount.setChecked(true);
                edt_discount.setVisibility(View.VISIBLE);
                edt_discount.setText("" +extraDetailsPojo.getDiscount());
            }
            else
            {
                toggle_discount.setChecked(false);
                edt_discount.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.toggle_sgst)
    public void toggleSGST()
    {
        toggle_utgst.setChecked(false);
        TransitionManager.beginDelayedTransition(card_utgst);
        card_utgst.setVisibility(View.GONE);

        toggle_igst.setChecked(false);
        TransitionManager.beginDelayedTransition(card_igst);
        card_igst.setVisibility(View.GONE);


        if (toggle_sgst.isChecked())
        {
            TransitionManager.beginDelayedTransition(card_sgst);
            card_sgst.setVisibility(View.VISIBLE);
        }
        else
        {
            TransitionManager.beginDelayedTransition(card_sgst);
            card_sgst.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.toggle_igst)
    public void toggleIGST()
    {
        toggle_utgst.setChecked(false);
        TransitionManager.beginDelayedTransition(card_utgst);
        card_utgst.setVisibility(View.GONE);

        toggle_sgst.setChecked(false);
        TransitionManager.beginDelayedTransition(card_sgst);
        card_sgst.setVisibility(View.GONE);

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
        toggle_sgst.setChecked(false);
        TransitionManager.beginDelayedTransition(card_sgst);
        card_sgst.setVisibility(View.GONE);

        toggle_igst.setChecked(false);
        TransitionManager.beginDelayedTransition(card_igst);
        card_igst.setVisibility(View.GONE);

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

    @OnClick(R.id.sgst_five)
    public void changeSgstFiveStroke()
    {

        sgstTwentyEight = false;
        sgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        sgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        sgstEighteen = false;
        sgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        sgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        sgstTwelve = false;
        sgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        sgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        if (sgstFive)
        {
            sgstFive = false;
            sgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
            sgst_five.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            sgstFive = true;
            sgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent_blue));
            sgst_five.setTextColor(getResources().getColor(R.color.white));
        }

    }

    @OnClick(R.id.sgst_twelve)
    public void changeSgstTwelveStroke()
    {
        sgstFive = false;
        sgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        sgst_five.setTextColor(getResources().getColor(R.color.grey));

        sgstTwentyEight = false;
        sgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        sgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        sgstEighteen = false;
        sgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        sgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        if (sgstTwelve)
        {
            sgstTwelve = false;
            sgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
            sgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            sgstTwelve = true;
            sgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent_blue));
            sgst_twelve.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.sgst_eighteen)
    public void changeSgstEighteenStroke()
    {
        sgstTwelve = false;
        sgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        sgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        sgstFive = false;
        sgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        sgst_five.setTextColor(getResources().getColor(R.color.grey));

        sgstTwentyEight = false;
        sgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        sgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        if (sgstEighteen)
        {
            sgstEighteen = false;
            sgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
            sgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            sgstEighteen = true;
            sgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent_blue));
            sgst_eighteen.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.sgst_twentyEight)
    public void changeSgstTwentyEightStroke()
    {

        sgstEighteen = false;
        sgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        sgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        sgstTwelve = false;
        sgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        sgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        sgstFive = false;
        sgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        sgst_five.setTextColor(getResources().getColor(R.color.grey));

        if (sgstTwentyEight)
        {
            sgstTwentyEight = false;
            sgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
            sgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));
        }
        else
        {
            sgstTwentyEight = true;
            sgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent_blue));
            sgst_twentyEight.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.igst_five)
    public void changeIgstFiveStroke()
    {

        igstTwentyEight = false;
        igst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        igst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        igstEighteen = false;
        igst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        igst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        igstTwelve = false;
        igst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        igst_twelve.setTextColor(getResources().getColor(R.color.grey));

        if (igstFive)
        {
            igstFive = false;
            igst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
            igst_five.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            igstFive = true;
            igst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent_blue));
            igst_five.setTextColor(getResources().getColor(R.color.white));

        }

    }

    @OnClick(R.id.igst_twelve)
    public void changeIgstTwelveStroke()
    {
        igstFive = false;
        igst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        igst_five.setTextColor(getResources().getColor(R.color.grey));

        igstTwentyEight = false;
        igst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        igst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        igstEighteen = false;
        igst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        igst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        if (igstTwelve)
        {
            igstTwelve = false;
            igst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
            igst_twelve.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            igstTwelve = true;
            igst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent_blue));
            igst_twelve.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.igst_eighteen)
    public void changeIgstEighteenStroke()
    {
        igstTwelve = false;
        igst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        igst_twelve.setTextColor(getResources().getColor(R.color.grey));

        igstFive = false;
        igst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        igst_five.setTextColor(getResources().getColor(R.color.grey));

        igstTwentyEight = false;
        igst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        igst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        if (igstEighteen)
        {
            igstEighteen = false;
            igst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
            igst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            igstEighteen = true;
            igst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent_blue));
            igst_eighteen.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.igst_twentyEight)
    public void changeIgstTwentyEightStroke()
    {

        igstEighteen = false;
        igst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        igst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        igstTwelve = false;
        igst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        igst_twelve.setTextColor(getResources().getColor(R.color.grey));

        igstFive = false;
        igst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        igst_five.setTextColor(getResources().getColor(R.color.grey));

        if (igstTwentyEight)
        {
            igstTwentyEight = false;
            igst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
            igst_twentyEight.setTextColor(getResources().getColor(R.color.grey));
        }
        else
        {
            igstTwentyEight = true;
            igst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent_blue));
            igst_twentyEight.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.utgst_five)
    public void changeUTgstFiveStroke()
    {

        utgstTwentyEight = false;
        utgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        utgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        utgstEighteen = false;
        utgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        utgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        utgstTwelve = false;
        utgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        utgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        if (utgstFive)
        {
            utgstFive = false;
            utgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
            utgst_five.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            utgstFive = true;
            utgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent_blue));
            utgst_five.setTextColor(getResources().getColor(R.color.white));

        }

    }

    @OnClick(R.id.utgst_twelve)
    public void changeUTgstTwelveStroke()
    {
        utgstFive = false;
        utgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        utgst_five.setTextColor(getResources().getColor(R.color.grey));

        utgstTwentyEight = false;
        utgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        utgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        utgstEighteen = false;
        utgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        utgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        if (utgstTwelve)
        {
            utgstTwelve = false;
            utgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
            utgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            utgstTwelve = true;
            utgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent_blue));
            utgst_twelve.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.utgst_eighteen)
    public void changeUTgstEighteenStroke()
    {
        utgstTwelve = false;
        utgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        utgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        utgstFive = false;
        utgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        utgst_five.setTextColor(getResources().getColor(R.color.grey));

        utgstTwentyEight = false;
        utgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
        utgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));

        if (utgstEighteen)
        {
            utgstEighteen = false;
            utgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
            utgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        }
        else
        {
            utgstEighteen = true;
            utgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent_blue));
            utgst_eighteen.setTextColor(getResources().getColor(R.color.white));

        }
    }

    @OnClick(R.id.utgst_twentyEight)
    public void changeUTgstTwentyEightStroke()
    {

        utgstEighteen = false;
        utgst_eighteen.setBackground(getResources().getDrawable(R.drawable.textview_border_eighteen_percent));
        utgst_eighteen.setTextColor(getResources().getColor(R.color.grey));

        utgstTwelve = false;
        utgst_twelve.setBackground(getResources().getDrawable(R.drawable.textview_border_twelve_percent));
        utgst_twelve.setTextColor(getResources().getColor(R.color.grey));

        utgstFive = false;
        utgst_five.setBackground(getResources().getDrawable(R.drawable.textview_border_five_percent));
        utgst_five.setTextColor(getResources().getColor(R.color.grey));

        if (utgstTwentyEight)
        {
            utgstTwentyEight = false;
            utgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent));
            utgst_twentyEight.setTextColor(getResources().getColor(R.color.grey));
        }
        else
        {
            utgstTwentyEight = true;
            utgst_twentyEight.setBackground(getResources().getDrawable(R.drawable.textview_border_twentyeight_percent_blue));
            utgst_twentyEight.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.back_arrow_extras)
    public void finishActivity()
    {
        finish();
    }

    @OnClick(R.id.btn_extra_done)
    public void addTaxes()
    {
        ExtraDetailsPojo extraDetailsPojo = new ExtraDetailsPojo();

        if (toggle_sgst.isChecked())
        {
            if (sgstFive)
            {
                extraDetailsPojo.setSgst(5);
            }
            else if (sgstTwelve)
            {
                extraDetailsPojo.setSgst(12);
            }
            else if (sgstEighteen)
            {
                extraDetailsPojo.setSgst(18);
            }
            else
            {
                extraDetailsPojo.setSgst(28);
            }
        }

        if (toggle_igst.isChecked())
        {
            if (igstFive)
            {
                extraDetailsPojo.setIgst(5);
            }
            else if (igstTwelve)
            {
                extraDetailsPojo.setIgst(12);
            }
            else if (igstEighteen)
            {
                extraDetailsPojo.setIgst(18);
            }
            else
            {
                extraDetailsPojo.setIgst(28);
            }
        }

        if (toggle_utgst.isChecked())
        {
            if (utgstFive)
            {
                extraDetailsPojo.setUtgst(5);
            }
            else if (utgstTwelve)
            {
                extraDetailsPojo.setUtgst(12);
            }
            else if (utgstEighteen)
            {
                extraDetailsPojo.setUtgst(18);
            }
            else
            {
                extraDetailsPojo.setUtgst(28);
            }
        }

        if (toggle_shipping.isChecked())
        {
            if (edt_shippingCharges.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "Please fill shipping charges", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                extraDetailsPojo.setShipping_charges(Double.parseDouble(edt_shippingCharges.getText().toString().trim()));
            }
        }

        if (toggle_discount.isChecked())
        {
            if (edt_discount.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "Please fill discount", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                extraDetailsPojo.setDiscount(Double.parseDouble(edt_discount.getText().toString().trim()));
            }
        }

        if (toggle_roundOff.isChecked())
        {
            extraDetailsPojo.setRoundOff(true);
        }
        else
        {
            extraDetailsPojo.setRoundOff(false);
        }

        EventBus.getDefault().postSticky(new SendExtraDetails(extraDetailsPojo,"1"));
        finish();
    }
}
