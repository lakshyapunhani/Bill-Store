package com.example.wuntu.billstore.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.BillViewActivity;
import com.example.wuntu.billstore.MainActivity;
import com.example.wuntu.billstore.Pojos.User;
import com.example.wuntu.billstore.PreviewActivity;
import com.example.wuntu.billstore.R;
import com.example.wuntu.billstore.RegisterActivity;
import com.example.wuntu.billstore.SignInActivity;
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

    @BindView(R.id.mobile_number)
    TextView mobile_number;

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

    @BindView(R.id.menu_dots)
    ImageView menu_dots;

    PopupMenu popup;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    private Context context;

    String _name ="",_shopName = "",_shopPanNumber ="",_shopGstNumber = "",_shopAddress = "";

    ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,view);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait...");

        mobile_number.setText(firebaseUser.getPhoneNumber());


        popup = new PopupMenu(context, menu_dots);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.logoutmenu, popup.getMenu());

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

        return view;
    }

    @OnClick(R.id.menu_dots)
    public void menuClick()
    {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                showLogOutAlert();
                return true;
            }
        });

        popup.show();
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

    @OnClick(R.id.btn_profileUpdate)
    public void updateClick()
    {
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

    public void writeDataToFirebase()
    {
        User user = new User(_name, _shopName,_shopAddress,_shopGstNumber,_shopPanNumber);
        progressDialog.show();

        if (firebaseUser != null ) {

            db.collection("Users")
                    .document(firebaseUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
