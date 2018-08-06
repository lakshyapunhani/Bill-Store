package com.fabuleux.wuntu.billstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fabuleux.wuntu.billstore.Adapters.FaqAdapter;
import com.fabuleux.wuntu.billstore.Adapters.ProductAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaqActivity extends AppCompatActivity
{

    @BindView(R.id.faq_recycler_view)
    RecyclerView faq_recycler_view;

    FaqAdapter faqAdapter;

    String[] questions = {"How to generate invoice?"
            ,"How to add products?"
            ,"How to edit/delete product?"
            ,"How to change language?"
            ,"How to update personal details?"
            ,"What is Add Bill?"};
    String[] answers = {"Select make bill from bottom menu and select a customer or add new customer if no customer added yet by adding customer name, customer address and customer gst number if any. Select items to add in invoice by tapping on Invoiced Items and then preview the bill. Just print the bill. You can even see that bill generated under Customers on Home screen.",
            "Select Settings from bottom menu and select Products. Tap the plus button in the bottom right corner and add product with product name,unit amount and description."
            ,"Select Settings from bottom menu and select Products. Tap on the product and select edit or delete in the menu opened."
            ,"Select Settings from bottom menu and select Change Language. Select the language and tap done."
            ,"Select Settings from bottom menu and select Profile. Edit the fields and tap on update."
            ,"Suppose you got a printed bill and you want to digitize it so add that bill with full information like bill amount,bill date, bill paid or due with picture of bill. Now you can access your bill in Bill Store under Vendors on Home screen."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);

        faqAdapter = new FaqAdapter(questions,answers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FaqActivity.this);
        faq_recycler_view.setLayoutManager(linearLayoutManager);
        faq_recycler_view.setAdapter(faqAdapter);
    }

    @OnClick(R.id.back_arrow_faq)
    public void backClick()
    {
        finish();
    }
}
