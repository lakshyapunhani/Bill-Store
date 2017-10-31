package com.example.wuntu.billstore.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.view.Window;
import android.widget.Toast;

import com.example.wuntu.billstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by Wuntu on 28-10-2017.
 */

public class ProfileInformationDialog extends Dialog
{
    private Context context;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @BindView(R.id.user_name)
    AppCompatEditText user_name;

    @BindView(R.id.shop_name)
    AppCompatEditText shop_name;

    public ProfileInformationDialog(Context context)
    {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.profile_information_dialog);

        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Optional @OnClick(R.id.profile_submit_button)
    void profile_submit()
    {

        String user_name_string;
        if (user_name.getText().length() == 0)
        {
            user_name.setError("Please fill your name");
            return;
        }
        else
        {
            user_name_string = user_name.getText().toString();
        }
        String shop_name_string;
        if (shop_name.getText().length() == 0)
        {
            shop_name.setError("Please fill your Shop Name");
            return;
        }
        else
        {
            shop_name_string = shop_name.getText().toString();
        }

        User user = new User(user_name_string, shop_name_string);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            db.collection("Users")
                    .document(firebaseUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
                }
            });
        }
        dismiss();
    }
}
