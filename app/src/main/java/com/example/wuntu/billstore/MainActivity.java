package com.example.wuntu.billstore;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wuntu.billstore.Fragments.AddBillFragment;
import com.example.wuntu.billstore.Fragments.HomeFragment;
import com.example.wuntu.billstore.Fragments.MakeBillFragment;
import com.example.wuntu.billstore.Fragments.ProfileFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layout_home)
    LinearLayout layout_home;

    @BindView(R.id.img_homePage)
    AppCompatImageView img_homePage;

    @BindView(R.id.text_homePage)
    TextView text_homePage;

    @BindView(R.id.layout_addBill)
    LinearLayout layout_addBill;

    @BindView(R.id.img_addBill)
    AppCompatImageView img_addBill;

    @BindView(R.id.text_addBill)
    TextView text_addBill;

    @BindView(R.id.layout_makeBill)
    LinearLayout layout_makeBill;

    @BindView(R.id.img_makeBill)
    AppCompatImageView img_makeBill;

    @BindView(R.id.text_makeBill)
    TextView text_makeBill;

    @BindView(R.id.layout_profile)
    LinearLayout layout_profile;

    @BindView(R.id.img_profile)
    AppCompatImageView img_profile;

    @BindView(R.id.text_profile)
    TextView text_profile;

    private FragmentManager fragmentManager;

    Fragment homeFragment;
    Fragment addBillFragment;
    Fragment makeBillFragment;
    Fragment profileFragment;
    private String selectedFragmentTag = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("fragmentFlag") != null){
            selectedFragmentTag = getIntent().getStringExtra("fragmentFlag");
        }

        homeFragment = new HomeFragment();
        addBillFragment = new AddBillFragment();
        makeBillFragment = new MakeBillFragment();
        profileFragment = new ProfileFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add( R.id.layout_mainFrame,homeFragment,"home");
        transaction.add( R.id.layout_mainFrame,addBillFragment,"add_bill");
        transaction.add( R.id.layout_mainFrame,makeBillFragment,"make_bill");
        transaction.add( R.id.layout_mainFrame,profileFragment,"profile");

        switch (selectedFragmentTag) {
            case "home":
                transaction.attach(homeFragment);
                transaction.detach(addBillFragment);
                transaction.detach(makeBillFragment);
                transaction.detach(profileFragment);
                break;
            case "add_bill":
                transaction.detach(homeFragment);
                transaction.attach(addBillFragment);
                transaction.detach(makeBillFragment);
                transaction.detach(profileFragment);
                break;
            case "make_bill":
                transaction.detach(homeFragment);
                transaction.detach(addBillFragment);
                transaction.attach(makeBillFragment);
                transaction.detach(profileFragment);
                break;
            case "profile":
                transaction.detach(homeFragment);
                transaction.detach(addBillFragment);
                transaction.detach(makeBillFragment);
                transaction.attach(profileFragment);
                break;
        }
        transaction.commitAllowingStateLoss();

    }

    private void setCurrentFragment(String show, String hide, String hide2, String hide3) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!fragmentManager.findFragmentByTag(show).isVisible() ) {
            transaction.attach(fragmentManager.findFragmentByTag(show));
            transaction.detach(fragmentManager.findFragmentByTag(hide));
            transaction.detach(fragmentManager.findFragmentByTag(hide2));
            transaction.detach(fragmentManager.findFragmentByTag(hide3));
            transaction.commitAllowingStateLoss();
        }
    }

    @OnClick(R.id.layout_home)
    public void homeClick()
    {
        setCurrentFragment("home","add_bill","make_bill","profile");
        img_homePage.setImageResource(R.drawable.ic_home_blue);
        img_addBill.setImageResource(R.drawable.ic_add);
        img_makeBill.setImageResource(R.drawable.ic_invoice_white);
        img_profile.setImageResource(R.drawable.ic_profile_white);
        text_homePage.setTextColor(getResources().getColor(R.color.blue));
        text_addBill.setTextColor(getResources().getColor(R.color.grey));
        text_makeBill.setTextColor(getResources().getColor(R.color.grey));
        text_profile.setTextColor(getResources().getColor(R.color.grey));
    }

    @OnClick(R.id.layout_addBill)
    public void addBillClick()
    {
        setCurrentFragment("add_bill","home","make_bill","profile");
        img_homePage.setImageResource(R.drawable.ic_home_white);
        img_addBill.setImageResource(R.drawable.ic_add_blue);
        img_makeBill.setImageResource(R.drawable.ic_invoice_white);
        img_profile.setImageResource(R.drawable.ic_profile_white);
        text_homePage.setTextColor(getResources().getColor(R.color.grey));
        text_addBill.setTextColor(getResources().getColor(R.color.blue));
        text_makeBill.setTextColor(getResources().getColor(R.color.grey));
        text_profile.setTextColor(getResources().getColor(R.color.grey));
    }

    @OnClick(R.id.layout_makeBill)
    public void makeBillClick()
    {
        setCurrentFragment("make_bill","home","add_bill","profile");
        img_homePage.setImageResource(R.drawable.ic_home_white);
        img_addBill.setImageResource(R.drawable.ic_add);
        img_makeBill.setImageResource(R.drawable.ic_invoice_blue);
        img_profile.setImageResource(R.drawable.ic_profile_white);
        text_homePage.setTextColor(getResources().getColor(R.color.grey));
        text_addBill.setTextColor(getResources().getColor(R.color.grey));
        text_makeBill.setTextColor(getResources().getColor(R.color.blue));
        text_profile.setTextColor(getResources().getColor(R.color.grey));
    }

    @OnClick(R.id.layout_profile)
    public void profileClick()
    {
        setCurrentFragment("profile","home","add_bill","make_bill");
        img_homePage.setImageResource(R.drawable.ic_home_white);
        img_addBill.setImageResource(R.drawable.ic_add);
        img_makeBill.setImageResource(R.drawable.ic_invoice_white);
        img_profile.setImageResource(R.drawable.ic_profile_blue);
        text_homePage.setTextColor(getResources().getColor(R.color.grey));
        text_addBill.setTextColor(getResources().getColor(R.color.grey));
        text_makeBill.setTextColor(getResources().getColor(R.color.grey));
        text_profile.setTextColor(getResources().getColor(R.color.blue));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
