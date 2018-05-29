package com.fabuleux.wuntu.billstore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Pojos.User;
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

public class EditProfileActivity extends AppCompatActivity
{
    @BindView(R.id.btn_profileUpdate)
    TextView btn_profileUpdate;

    @BindView(R.id.edt_updateName)
    EditText edt_updateName;

    @BindView(R.id.edt_updatesShopName)
    EditText edt_updatesShopName;

    @BindView(R.id.edt_updateShopAddress)
    EditText edt_updateShopAddress;

    @BindView(R.id.edt_updateGstNumber)
    EditText edt_updateGstNumber;

    @BindView(R.id.edt_updatePanNumber)
    EditText edt_updatePanNumber;

    String _name ="",_shopName = "",_shopPanNumber ="",_shopGstNumber = "",_shopAddress = "";

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait...");

        DocumentReference profileReference = db.collection("Users").document(firebaseUser.getUid());

        profileReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    return;
                }
                if (documentSnapshot.contains("shop_name"))
                {
                    edt_updatesShopName.setText(documentSnapshot.get("shop_name").toString());
                }
                if (documentSnapshot.contains("shop_address"))
                {
                    edt_updateShopAddress.setText(documentSnapshot.get("shop_address").toString());
                }
                if (documentSnapshot.contains("shop_gst"))
                {
                    edt_updateGstNumber.setText(documentSnapshot.get("shop_gst").toString());
                }

                if (documentSnapshot.contains("name"))
                {
                    edt_updateName.setText(documentSnapshot.get("name").toString());
                }
                if (documentSnapshot.contains("shop_pan"))
                {
                    edt_updatePanNumber.setText(documentSnapshot.get("shop_pan").toString());
                }
            }
        });
    }

    public void updateClick()
    {

        if (!progressDialog.isShowing())
        {
            progressDialog.show();
        }

        if (!edt_updateName.getText().toString().trim().isEmpty())
        {
            _name = edt_updateName.getText().toString();
        }

        if (!edt_updatesShopName.getText().toString().trim().isEmpty())
        {
            _shopName = edt_updatesShopName.getText().toString();
        }
        if (!edt_updateShopAddress.getText().toString().trim().isEmpty())
        {
            _shopAddress = edt_updateShopAddress.getText().toString();
        }
        if (!edt_updateGstNumber.getText().toString().trim().isEmpty())
        {
            _shopGstNumber = edt_updateShopAddress.getText().toString();
        }
        if (!edt_updatePanNumber.getText().toString().trim().isEmpty())
        {
            _shopPanNumber = edt_updatePanNumber.getText().toString();
        }
        writeDataToFirebase();
    }

    @OnClick(R.id.btn_profileUpdate)
    public void showUpdateAlert()
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Update Details");
        builder1.setMessage("Are you sure?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.alert_btn_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        updateClick();
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

    public void writeDataToFirebase()
    {
        User user = new User(_name, _shopName,_shopAddress,_shopGstNumber,_shopPanNumber);

        if (firebaseUser != null )
        {
            db.collection("Users")
                    .document(firebaseUser.getUid())
                    .set(user);
            Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            finish();

        }
    }

    @OnClick(R.id.back_arrow)
    public void backClick()
    {
        finish();
    }
}
