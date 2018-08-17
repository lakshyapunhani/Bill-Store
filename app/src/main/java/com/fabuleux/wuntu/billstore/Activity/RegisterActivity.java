package com.fabuleux.wuntu.billstore.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.User;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edt_name)
    EditText name;

    @BindView(R.id.edt_shopName)
    EditText shopName;

    @BindView(R.id.edt_panNumber) EditText panNumber;

    @BindView(R.id.edt_gstNumber) EditText gstNumber;

    @BindView(R.id.edt_shopAddress) EditText shopAddress;

    @BindView(R.id.text_skip)
    TextView text_skip;

    String _name ="",_shopName = "",_shopPanNumber ="",_shopGstNumber = "",_shopAddress = "";

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;
    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();

        HashMap<String,String> map = new HashMap<>();
        map.put("UID",firebaseUser.getUid());
        db.collection("Users").document(firebaseUser.getUid()).set(map);
    }

    @OnClick(R.id.btn_profileSubmit)
    public void profileSubmitClick()
    {
        if (!progressDialog.isShowing() && RegisterActivity.this.isDestroyed())
        {
            progressDialog.show();
        }

        if (name.getText().toString().trim().isEmpty())
        {
            if (progressDialog.isShowing() && RegisterActivity.this.isDestroyed())
            {
                progressDialog.dismiss();
            }
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            _name = name.getText().toString();
        }

        if (shopName.getText().toString().trim().isEmpty())
        {
            if (progressDialog.isShowing() && RegisterActivity.this.isDestroyed())
            {
                progressDialog.dismiss();
            }
            Toast.makeText(this, "Please enter Shop name", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            _shopName = shopName.getText().toString();
        }
        if (shopAddress.getText().toString().trim().isEmpty())
        {
            if (progressDialog.isShowing() && RegisterActivity.this.isDestroyed())
            {
                progressDialog.dismiss();
            }
            Toast.makeText(this, "Please enter Shop Address", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            _shopAddress = shopAddress.getText().toString();
        }
        if (!gstNumber.getText().toString().trim().isEmpty())
        {
            _shopGstNumber = gstNumber.getText().toString();
        }
        if (!panNumber.getText().toString().trim().isEmpty())
        {
            _shopPanNumber = panNumber.getText().toString();
        }


        writeDataToFirebase();

    }

    public void writeDataToFirebase()
    {
        User user = new User(_name, _shopName,_shopAddress,_shopGstNumber,_shopPanNumber);

        if (firebaseUser != null ) {

            db.collection("Users")
                    .document(firebaseUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (progressDialog.isShowing() && RegisterActivity.this.isDestroyed())
                            {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(RegisterActivity.this, "Profile Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.text_skip)
    public void skipClick()
    {
        showAlert();
    }

    private void showAlert()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
        builder1.setTitle(getString(R.string.skip_profile_setup));
        builder1.setMessage(getString(R.string.malfunction));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.alert_btn_continue),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        finish();
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
    public void onBackPressed()
    {}

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
