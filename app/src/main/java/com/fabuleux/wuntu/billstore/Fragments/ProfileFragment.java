package com.fabuleux.wuntu.billstore.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.BillViewActivity;
import com.fabuleux.wuntu.billstore.EditProfileActivity;
import com.fabuleux.wuntu.billstore.LanguageSelectionActivity;
import com.fabuleux.wuntu.billstore.MainActivity;
import com.fabuleux.wuntu.billstore.Pojos.User;
import com.fabuleux.wuntu.billstore.PreviewActivity;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.RegisterActivity;
import com.fabuleux.wuntu.billstore.SignInActivity;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.freshchat.consumer.sdk.FreshchatNotificationConfig;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

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

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_new, container, false);
        ButterKnife.bind(this,view);

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
        startActivity(new Intent(context, LanguageSelectionActivity.class));
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
        //user.setFirstName(mSessionManager.getName()).setPhone(mSessionManager.getCountryCode(), mSessionManager.getUsername());
        Freshchat.getInstance(context.getApplicationContext()).setUser(user);

        Freshchat.showConversations(context.getApplicationContext());

        FreshchatNotificationConfig notificationConfig = new FreshchatNotificationConfig()
                .setNotificationSoundEnabled(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(R.mipmap.ic_launcher)
                .launchActivityOnFinish(ProfileFragment.class.getName())
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
        builder1.setTitle("Log Out");
        builder1.setMessage("Are you sure You want to Log out?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.alert_btn_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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

}
