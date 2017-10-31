package com.example.wuntu.billstore;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wuntu.billstore.Utils.DrawerUtil;
import com.example.wuntu.billstore.Utils.ProfileInformationDialog;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {

    ProfileInformationDialog profileInformationDialog;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    Button button;
    GoogleApiClient googleApiClient;
    String LOGIN_METHOD;

    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        profileInformationDialog = new ProfileInformationDialog(this);
        profileInformationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profileInformationDialog.show();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        DrawerUtil.getDrawer(this,toolbar);






        LOGIN_METHOD = getIntent().getStringExtra("LOGIN_METHOD");


        button = (Button) findViewById(R.id.log_out);
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    if (LOGIN_METHOD != null)
                    {
                        if (LOGIN_METHOD.equals("OTP"))
                        {
                           // Toast.makeText(Main2Activity.this, user.getPhoneNumber() + "User Phone Number", Toast.LENGTH_SHORT).show();
                        }
                        else if (LOGIN_METHOD.equals("FACEBOOK"))
                        {
                            Profile profile = Profile.getCurrentProfile();
                           // Toast.makeText(Main2Activity.this, profile.getName() +"", Toast.LENGTH_SHORT).show();
                        }
                    }
                   // Toast.makeText(Main2Activity.this, user.getDisplayName() + "Google Name", Toast.LENGTH_SHORT).show();

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Toast.makeText(Main2Activity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status)
                            {
                                //Toast.makeText(getApplicationContext(),"Google Logged Out",Toast.LENGTH_SHORT).show();
                            }
                        });

                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();

                startActivity(new Intent(Main2Activity.this,SignInActivity.class));
                finish();
            }
        });


    }





    @Override
    public void onStart()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleApiClient.connect();
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
