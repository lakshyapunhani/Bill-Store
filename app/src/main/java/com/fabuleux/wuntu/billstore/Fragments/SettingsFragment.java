package com.fabuleux.wuntu.billstore.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Activity.ContactsActivity;
import com.fabuleux.wuntu.billstore.Activity.EditProfileActivity;
import com.fabuleux.wuntu.billstore.Activity.FaqActivity;
import com.fabuleux.wuntu.billstore.Activity.LanguageSelectionActivity;
import com.fabuleux.wuntu.billstore.Manager.RealmManager;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Activity.ProductsActivity;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Activity.SignInActivity;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatNotificationConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.freshchat.consumer.sdk.exception.MethodNotAllowedException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    @BindView(R.id.layout_changeLanguage)
    LinearLayout layout_changeLanguage;

    @BindView(R.id.layout_logOut)
    Button layout_logOut;

    @BindView(R.id.layout_freshchat)
    LinearLayout layout_freshChat;

    @BindView(R.id.layout_profile)
    LinearLayout layout_profile;

    @BindView(R.id.mobile_number)
    TextView mobile_number;

    @BindView(R.id.layout_invite)
    LinearLayout layout_invite;

    @BindView(R.id.layout_products)
            LinearLayout layout_products;

    @BindView(R.id.layout_contacts)
    LinearLayout layout_contacts;

    @BindView(R.id.layout_faq) LinearLayout layout_faq;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    private Context context;

    private SessionManager sessionManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this,view);

        sessionManager = new SessionManager(context);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait...");

        mobile_number.setText(firebaseUser.getPhoneNumber());


        return view;
    }

    @OnClick(R.id.layout_changeLanguage)
    public void changeLanguage()
    {
        Intent intent = new Intent(context,LanguageSelectionActivity.class);
        intent.putExtra("flag",1);
        startActivity(intent);
    }

    @OnClick(R.id.layout_logOut)
    public void logOut()
    {
        showLogOutAlert();
    }

    @OnClick(R.id.layout_freshchat)
    public void startChat()
    {
        FreshchatConfig freshchatConfig = new FreshchatConfig("d462331e-d19a-46e1-9f72-696564b6c514", "ac2788ab-197d-4108-8a21-a6415be5a1f6");
        Freshchat.getInstance(context.getApplicationContext()).init(freshchatConfig);

        FreshchatUser user = Freshchat.getInstance(context.getApplicationContext()).getUser();
        user.setFirstName(firebaseUser.getUid()).setPhone("+91",firebaseUser.getPhoneNumber());
        try {
            Freshchat.getInstance(context.getApplicationContext()).setUser(user);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }

        Freshchat.showConversations(context.getApplicationContext());

        FreshchatNotificationConfig notificationConfig = new FreshchatNotificationConfig()
                .setNotificationSoundEnabled(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(R.mipmap.ic_launcher)
                .launchActivityOnFinish(SettingsFragment.class.getName())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Freshchat.getInstance(context.getApplicationContext()).setNotificationConfig(notificationConfig);
    }

    @OnClick(R.id.layout_profile)
    public void profileLayout()
    {
        startActivity(new Intent(context, EditProfileActivity.class));
    }

    @OnClick(R.id.layout_invite)
    public void invite()
    {
        String shareBody = getString(R.string.invite_link);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I love using Bill Store");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }


    private void showLogOutAlert()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(R.string.log_out);
        builder1.setMessage(R.string.are_you_sure);
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.alert_btn_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        RealmManager.deleteAllRealm();
                        sessionManager.clearSharedPref();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(context, SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
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

    @OnClick(R.id.layout_products)
    public void productLayoutClick()
    {
        startActivity(new Intent(context, ProductsActivity.class));
    }

    @OnClick(R.id.layout_faq)
    public void faqLayoutClick()
    {
        startActivity(new Intent(context, FaqActivity.class));
    }

    @OnClick(R.id.layout_contacts)
    public void contactsLayoutClick()
    {
        startActivity(new Intent(context,ContactsActivity.class));
    }

}
