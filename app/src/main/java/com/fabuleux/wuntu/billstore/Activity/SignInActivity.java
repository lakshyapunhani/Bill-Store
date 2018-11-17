package com.fabuleux.wuntu.billstore.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Dialogs.DialogOTP;
import com.fabuleux.wuntu.billstore.EventBus.EventOTP;
import com.fabuleux.wuntu.billstore.EventBus.EventPrintOtp;
import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.EventBus.ResendOTPEvent;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.phone_number_edittext)
    AppCompatEditText phone_number_edittext;

    DialogOTP otpDialog;

    String phone_number;

    // For Firebase Integration
    FirebaseAuth firebaseAuth;

    String mVerificationCode;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseFirestore db;
    DocumentReference documentReference;

    ProgressDialog progressDialog;

    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;
    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();
        smsReceiver = new SMSReceiver();

        // Generate a filter object
        IntentFilter intentFilter = new IntentFilter();
        // Add filter an action
        intentFilter.addAction(SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, intentFilter);

        askForPermissions();


        otpDialog = new DialogOTP(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Verifying");
        progressDialog.setMessage("Please wait...");


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                    //User is signed in
                    documentReference = db.collection("Users").document(firebaseUser.getUid());

                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot)
                        {
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                                {
                                    progressDialog.dismiss();
                                }
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();

                            }
                            else
                            {
                                if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                                {
                                    progressDialog.dismiss();
                                }

                                Intent intent = new Intent(SignInActivity.this,LanguageSelectionActivity.class);
                                intent.putExtra("flag",0);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(SignInActivity.this, "Exception " + e, Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                }
                else
                {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        //For Firebase Integration
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                if (!progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                {
                    progressDialog.show();
                }
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                {
                    progressDialog.dismiss();
                }
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MESSAGE",e.getMessage());
            }

            @Override
            public void onCodeSent(String verification_id, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                {
                    progressDialog.dismiss();
                }
                super.onCodeSent(verification_id, forceResendingToken);

                //otpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               /* otpDialog.changeMessage(phone_number);
                if (!otpDialog.isShowing()  && !SignInActivity.this.isDestroyed())
                {
                    otpDialog.show();
                }*/


                mVerificationCode = verification_id;
                token = forceResendingToken;
            }
        };
    }

    private void askForPermissions()
    {
        int requestCode = 0;

        ArrayList<String> permissionsToRequestFor = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsToRequestFor.add(android.Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsToRequestFor.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsToRequestFor.add(android.Manifest.permission.CALL_PHONE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsToRequestFor.add(android.Manifest.permission.SEND_SMS);
        }

        if (permissionsToRequestFor.size() > 0) {
            String[] requestArray = new String[permissionsToRequestFor.size()];
            permissionsToRequestFor.toArray(requestArray);

            ActivityCompat.requestPermissions(this, requestArray, requestCode);
        }
    }

    @OnClick(R.id.btn_signIn)
    public void BtnSignIn()
    {
        if (phone_number_edittext.getText().length() == 0)
        {
            Toast.makeText(this, "Please fill the Mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(phone_number_edittext.getText().length() > 9 && phone_number_edittext.getText().length() < 12))
        {
            Toast.makeText(this, "Please fill the Correct Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }

        phone_number = phone_number_edittext.getText().toString();

        String country_code = "+91";
        String phone_number1 = country_code + phone_number;

        if (!progressDialog.isShowing() && !SignInActivity.this.isDestroyed()) {
            progressDialog.show();
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number1, 60, TimeUnit.SECONDS, SignInActivity.this, callbacks);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
       /* if (otpDialog.isShowing() && !SignInActivity.this.isDestroyed())
        {
            otpDialog.dismiss();
        }*/

        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                            {
                                progressDialog.dismiss();
                            }
                        }
                        else
                        {
                            if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed())
                            {
                                progressDialog.dismiss();
                            }
                            Log.d("MESSAGE",task.getException().getMessage());
                            Toast.makeText(SignInActivity.this,"Wrong OTP. Please Enter Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendOTPEvent(EventOTP event)
    {
        if (!progressDialog.isShowing() && !SignInActivity.this.isDestroyed()) {
            progressDialog.show();
        }
        verifyPhoneNumberWithCode(mVerificationCode, event.getCode());
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResendOTPEvent(ResendOTPEvent event)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number_edittext.getText().toString(), 60, TimeUnit.SECONDS, SignInActivity.this, callbacks, token);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        EventBus.getDefault().unregister(this);

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

    public class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
                Bundle extras = intent.getExtras();

                String strMessage = "";

                if (extras != null) {
                    Object[] smsextras = (Object[]) extras.get("pdus");

                    assert smsextras != null;
                    for (Object smsextra : smsextras) {
                        SmsMessage smsmsg = SmsMessage.createFromPdu((byte[]) smsextra);

                        String strMsgBody = smsmsg.getMessageBody();
                        strMessage += strMsgBody;
                    }

                    //012345 is your verification code

                    strMessage = strMessage.toLowerCase();
                    if (strMessage.contains("verification")) {
                        String otpSms;
                        try {
                            if (!progressDialog.isShowing() && !SignInActivity.this.isDestroyed()) {
                                progressDialog.show();
                            }
                            otpSms = strMessage.substring(0, 6);
                            String otpSms1 = strMessage.substring(0,1);
                            String otpSms2 = strMessage.substring(1,2);
                            String otpSms3 = strMessage.substring(2,3);
                            String otpSms4 = strMessage.substring(3,4);
                            String otpSms5 = strMessage.substring(4,5);
                            String otpSms6 = strMessage.substring(5,6);

                            EventPrintOtp eventPrintOtp = new EventPrintOtp(otpSms1,otpSms2,otpSms3,otpSms4,otpSms5,otpSms6);
                            EventBus.getDefault().postSticky(eventPrintOtp);
                        } catch (Exception e) {
                            return;
                        }
                        if (ContextCompat.checkSelfPermission(SignInActivity.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(SignInActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        } else {
                            if (progressDialog.isShowing() && !SignInActivity.this.isDestroyed()) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
        if (networkReceiver != null)
        {
            this.unregisterReceiver(networkReceiver);
        }
    }
}