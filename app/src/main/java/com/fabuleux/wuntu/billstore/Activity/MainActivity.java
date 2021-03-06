package com.fabuleux.wuntu.billstore.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.EventBus.SetCurrentFragmentEvent;
import com.fabuleux.wuntu.billstore.Fragments.AddBillFragment;
import com.fabuleux.wuntu.billstore.Fragments.InvoicesFragment;
import com.fabuleux.wuntu.billstore.Fragments.MakeBillFragment;
import com.fabuleux.wuntu.billstore.Fragments.SettingsFragment;
import com.fabuleux.wuntu.billstore.Manager.RealmManager;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Network.CommonRequest;
import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;
import com.fabuleux.wuntu.billstore.Pojos.ProductModel;
import com.fabuleux.wuntu.billstore.Pojos.User;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;
import com.freshchat.consumer.sdk.Freshchat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    Fragment customerFragment;
    Fragment addBillFragment;
    Fragment makeBillFragment;
    Fragment profileFragment;
    private String selectedFragmentTag = "home";

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    ArrayList<String> gstRateList;
    ArrayList<String> unitList;

    CollectionReference productReference;

    private List<ItemSelectionPojo> itemList;

    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;
    Gson gson;

    String shopName = "",shopAddress = "",shopGstNumber = "",name = "",shopPanNumber="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        if (sessionManager.isDarkThemeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("fragmentFlag") != null){
            selectedFragmentTag = getIntent().getStringExtra("fragmentFlag");
        }


        networkReceiver = new NetworkReceiver();
        gson = new Gson();

        RealmManager.deleteAllRealm();

        customerFragment = new InvoicesFragment();
        addBillFragment = new AddBillFragment();
        makeBillFragment = new MakeBillFragment();
        profileFragment = new SettingsFragment();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //Log.d("Firebase", "token "+ FirebaseInstanceId.getInstance().getToken());

        if (!sessionManager.getDeviceToken().isEmpty())
        {
            CommonRequest.getInstance(this).sendDeviceToken(firebaseUser.getUid());
            Freshchat.getInstance(this).setPushRegistrationToken(sessionManager.getDeviceToken());
        }

        Freshchat.getInstance(this).setPushRegistrationToken(sessionManager.getDeviceToken());

        /////////////////////////////////////// Just for saving user mobile number to db
        DocumentReference profileReference = db.collection("Users").document(firebaseUser.getUid());

        profileReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    return;
                }
                if (documentSnapshot.exists())
                {
                    if(!documentSnapshot.contains("mobileNumber"))
                    {
                        if (documentSnapshot.contains("shop_name"))
                        {
                            shopName = documentSnapshot.get("shop_name").toString();
                        }
                        if (documentSnapshot.contains("shop_address"))
                        {
                            shopAddress = documentSnapshot.get("shop_address").toString();
                        }
                        if (documentSnapshot.contains("shop_gst"))
                        {
                            shopGstNumber = documentSnapshot.get("shop_gst").toString();
                        }

                        if (documentSnapshot.contains("name"))
                        {
                            name = documentSnapshot.get("name").toString();
                        }
                        if (documentSnapshot.contains("shop_pan"))
                        {
                            shopPanNumber = documentSnapshot.get("shop_pan").toString();
                        }

                        User user = new User(name, shopName,shopAddress,shopGstNumber,shopPanNumber,
                                firebaseUser.getUid(),firebaseUser.getPhoneNumber());

                        if (firebaseUser != null )
                        {
                            db.collection("Users")
                                    .document(firebaseUser.getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }
                    }

                }

            }
        });



        /////////////////////////////////////////////////////////////////////////////////


        unitList = new ArrayList<>();
        gstRateList = new ArrayList<>();
        itemList = new ArrayList<>();
        saveDataToSessionManager();

        productReference = db.collection("Users").document(firebaseUser.getUid()).collection("Products");

        productReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    Toast.makeText(MainActivity.this, "Not able to show products. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                itemList.clear();

                for (DocumentSnapshot doc : documentSnapshots)
                {
                    ProductModel productModel = doc.toObject(ProductModel.class);
                    ItemSelectionPojo itemSelectionPojo = new ItemSelectionPojo(productModel.getProductId(),productModel.getProductName(),productModel.getProductRate(),productModel.getProductDescription(),0);
                    itemList.add(itemSelectionPojo);
                }
                RealmManager.addItemsInRealm(itemList);
            }
        });

        RealmManager.resetItemRealm();


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add( R.id.layout_mainFrame,customerFragment,"home");
        transaction.add( R.id.layout_mainFrame,makeBillFragment,"make_bill");
        transaction.add( R.id.layout_mainFrame,addBillFragment,"add_bill");
        transaction.add( R.id.layout_mainFrame,profileFragment,"profile");

        switch (selectedFragmentTag) {
            case "home":
                transaction.show(customerFragment);
                transaction.hide(addBillFragment);
                transaction.hide(makeBillFragment);
                transaction.hide(profileFragment);
                break;
            case "add_bill":
                transaction.hide(customerFragment);
                transaction.show(addBillFragment);
                transaction.hide(makeBillFragment);
                transaction.hide(profileFragment);
                break;
            case "make_bill":
                transaction.hide(customerFragment);
                transaction.hide(addBillFragment);
                transaction.show(makeBillFragment);
                transaction.hide(profileFragment);
                break;
            case "profile":
                transaction.hide(customerFragment);
                transaction.hide(addBillFragment);
                transaction.hide(makeBillFragment);
                transaction.show(profileFragment);
                break;
        }
        transaction.commitAllowingStateLoss();

    }

    public void setCurrentFragment(String show, String hide, String hide2, String hide3) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!fragmentManager.findFragmentByTag(show).isVisible() ) {
            transaction.show(fragmentManager.findFragmentByTag(show));
            transaction.hide(fragmentManager.findFragmentByTag(hide));
            transaction.hide(fragmentManager.findFragmentByTag(hide2));
            transaction.hide(fragmentManager.findFragmentByTag(hide3));
            transaction.commitAllowingStateLoss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFragment(SetCurrentFragmentEvent event)
    {
        //setCurrentFragment(event.getShow(),event.getHide(),event.getHide2(),event.getHide3());
        if (event.getShow().matches("home"))
        {
            homeClick();
        }
        else if (event.getShow().matches("add_bill"))
        {
            addBillClick();
        }
        else if (event.getShow().matches("make_bill"))
        {
            makeBillClick();
        }
        else if (event.getShow().matches("profile"))
        {
            profileClick();
        }
    }

    @OnClick(R.id.layout_home)
    public void homeClick()
    {
        setCurrentFragment("home","add_bill","make_bill","profile");
        img_homePage.setImageResource(R.drawable.ic_home_blue);
        img_addBill.setImageResource(R.drawable.ic_add);
        img_makeBill.setImageResource(R.drawable.ic_invoice_white);
        img_profile.setImageResource(R.drawable.ic_settings_white);
        text_homePage.setTextColor(getResources().getColor(R.color.bg_header));
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
        img_profile.setImageResource(R.drawable.ic_settings_white);
        text_homePage.setTextColor(getResources().getColor(R.color.grey));
        text_addBill.setTextColor(getResources().getColor(R.color.bg_header));
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
        img_profile.setImageResource(R.drawable.ic_settings_white);
        text_homePage.setTextColor(getResources().getColor(R.color.grey));
        text_addBill.setTextColor(getResources().getColor(R.color.grey));
        text_makeBill.setTextColor(getResources().getColor(R.color.bg_header));
        text_profile.setTextColor(getResources().getColor(R.color.grey));
    }

    @OnClick(R.id.layout_profile)
    public void profileClick()
    {
        setCurrentFragment("profile","home","add_bill","make_bill");
        img_homePage.setImageResource(R.drawable.ic_home_white);
        img_addBill.setImageResource(R.drawable.ic_add);
        img_makeBill.setImageResource(R.drawable.ic_invoice_white);
        img_profile.setImageResource(R.drawable.ic_settings_blue);
        text_homePage.setTextColor(getResources().getColor(R.color.grey));
        text_addBill.setTextColor(getResources().getColor(R.color.grey));
        text_makeBill.setTextColor(getResources().getColor(R.color.grey));
        text_profile.setTextColor(getResources().getColor(R.color.bg_header));
    }


    @Override
    public void onBackPressed() {
        showBackAlert();
    }

    private void showBackAlert()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setTitle(getString(R.string.exit));
        builder1.setMessage(getString(R.string.alert_sure_exit_app));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.alert_btn_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        }
                        else
                        {
                            finishAffinity();
                            System.exit(0);
                        }*/
                    }
                });
        builder1.setNegativeButton(getString(R.string.alert_btn_no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        this.unregisterReceiver(networkReceiver);
    }

    private void saveDataToSessionManager()
    {
        /*CollectionReference gstReference = db.collection("GstSlabs");

        gstReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                gstRateList.add(document.getId());
                            }

                            String s = gson.toJson(gstRateList);
                            sessionManager.saveGstSlabLists(s);

                        } else
                        {
                            Toast.makeText(MainActivity.this, "Error in GST document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        CollectionReference unitReference = db.collection("Units");

        unitReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                unitList.add(document.getId());
                            }
                            String s = gson.toJson(unitList);
                            sessionManager.saveUnitList(s);
                        } else
                        {
                            Toast.makeText(MainActivity.this, "Error in unit document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        DocumentReference userDetails = db.collection("Users").document(firebaseUser.getUid());
        userDetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists())
                    {
                        if (documentSnapshot.contains("name"))
                        {
                            sessionManager.setName(documentSnapshot.get("name").toString());
                        }

                        if (documentSnapshot.contains("shop_address"))
                        {
                            sessionManager.setShop_address(documentSnapshot.get("shop_address").toString());
                        }

                        if (documentSnapshot.contains("shop_gst"))
                        {
                            sessionManager.setShop_gst(documentSnapshot.get("shop_gst").toString());
                        }

                        if (documentSnapshot.contains("shop_name"))
                        {
                            sessionManager.setShop_name(documentSnapshot.get("shop_name").toString());
                        }

                        if (documentSnapshot.contains("shop_pan"))
                        {
                            sessionManager.setShop_pan(documentSnapshot.get("shop_pan").toString());
                        }
                    }

                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InternetStatus event) {
        if (event.getStatus()) {
            sessionManager.setInternetAvailable(true);
        }
        else
        {
            sessionManager.setInternetAvailable(false);
        }
    }



}
