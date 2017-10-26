package com.example.wuntu.billstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Utils.CodeSentDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignInActivity extends AppCompatActivity {

    CodeSentDialog codeSentDialog;

    //For Facebook CallbackManager
    CallbackManager callbackManager;

    @BindView(R.id.bill_book_text)
    TextView bill_book_text;

    @BindView(R.id.resend_otp)
    TextView resend_otp;

    @BindView(R.id.phone_number_edittext)
    AppCompatEditText phone_number_edittext;

    @BindView(R.id.verification_code_edittext)
    AppCompatEditText verification_code_editText;

    @BindView(R.id.send_otp_button)
    AppCompatButton send_otp_button;

    @BindView(R.id.verify_button)
    AppCompatButton verify_button;

    @BindView(R.id.login_with_text)
    TextView login_with_text;

    @BindView(R.id.facebook_icon)
    ImageView facebook_icon;

    @BindView(R.id.google_icon)
    ImageView google_icon;

    @BindView(R.id.send_otp_layout)
    LinearLayout send_otp_layout ;

    @BindView(R.id.verification_layout)
    LinearLayout verification_layout;


    String phone_number;

    // For Firebase Integration
    FirebaseAuth firebaseAuth;
    String mverificationCode, mcode;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    //For Google Integration
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient googleApiClient;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        codeSentDialog = new CodeSentDialog(this);

        progressDialog = new ProgressDialog(this);

        //For Google Integration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //For Facebook Integration
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();



        //For Changing Fonts
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/terminator.ttf");

        bill_book_text.setTypeface(typeface);
        bill_book_text.requestFocus();

        login_with_text.setTypeface(typeface);


        //For Firebase Integration
        firebaseAuth = FirebaseAuth.getInstance();


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
               // Toast.makeText(SignInActivity.this, "onVerificationCompleted", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.hide();
                phone_number_edittext.setError("Please fill the Correct Mobile Number");
                //Toast.makeText(SignInActivity.this, "onVerificationFailed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verification_id, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification_id, forceResendingToken);
                mverificationCode = verification_id;
                token = forceResendingToken;
                //Toast.makeText(SignInActivity.this, "onCodeSent", Toast.LENGTH_SHORT).show();
                send_otp_layout.setVisibility(View.GONE);
                verification_layout.setVisibility(View.VISIBLE);
                progressDialog.hide();
                codeSentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                codeSentDialog.show();
            }
        };

    }

    @OnClick(R.id.facebook_icon)
    public void facebook_click()
    {
        progressDialog.setMessage("Verifying");
        progressDialog.show();
        LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                Log.d("SUCCESS", loginResult.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                progressDialog.hide();
                //Toast.makeText(SignInActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                progressDialog.hide();
                Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.google_icon)
    public void google_click()
    {
        progressDialog.setMessage("Verifying");
        progressDialog.show();
        signIn();
    }

    @OnClick(R.id.send_otp_button)
    public void send_otp()
    {
        progressDialog.setMessage("Verifying");
        progressDialog.show();

        if (phone_number_edittext.getText().length() == 0)
        {
            progressDialog.hide();
            phone_number_edittext.setError("Please fill your Mobile Number");
            //Toast.makeText(this, "Please fill the phone number", Toast.LENGTH_SHORT).show();

            return;
        }

        if (phone_number_edittext.getText().length() < 10)
        {
            progressDialog.hide();
            phone_number_edittext.setError("Please fill the Correct Mobile Number");

            return;
        }

        phone_number = phone_number_edittext.getText().toString();

        String country_code = "+91";
        String phone_number1 = country_code + phone_number;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number1, 60, TimeUnit.SECONDS, SignInActivity.this, callbacks);

    }

    @OnClick(R.id.verify_button)
    public void verify_button()
    {
        progressDialog.show();

        if (verification_code_editText.getText().length() == 0)
        {
            verification_code_editText.setError("Please fill the Correct Verification Code");
            progressDialog.hide();
            return;
        }
        mcode = verification_code_editText.getText().toString();
        verifyPhoneNumberWithCode(mverificationCode, mcode);
    }

    @OnClick(R.id.resend_otp)
    public void resend_otp()
    {
        progressDialog.setMessage("Verifying");
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number_edittext.getText().toString(), 60, TimeUnit.SECONDS, SignInActivity.this, callbacks, token);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.hide();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            /*FirebaseUser user = firebaseAuth.getCurrentUser();*/
                            Intent intent = new Intent(SignInActivity.this,Main2Activity.class);
                            intent.putExtra("LOGIN_METHOD","FACEBOOK");
                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                           // Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.hide();
                            //Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignInActivity.this, Main2Activity.class).putExtra("LOGIN_METHOD","OTP"));
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

        } else {

            Log.d("TAG","Signed Out");
            // Signed out, show unauthenticated UI.
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                //Toast.makeText(SignInActivity.this, user.getDisplayName() + "", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                                startActivity(new Intent(SignInActivity.this,Main2Activity.class));
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.hide();
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed. Try Again",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}