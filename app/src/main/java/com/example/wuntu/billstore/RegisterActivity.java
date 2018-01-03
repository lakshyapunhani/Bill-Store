package com.example.wuntu.billstore;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wuntu.billstore.Pojos.User;
import com.example.wuntu.billstore.Utils.MarshMallowPermission;
import com.example.wuntu.billstore.Utils.Roundedimageview;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.cameraimageview)
    Roundedimageview roundedimageview;

    @BindView(R.id.edt_name)
    EditText name;

    @BindView(R.id.edt_shopName)
    EditText shopName;

    Bitmap bitmap_thumbnail;

    String _name,_shopName;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private StorageReference mStorageRef;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    Uri selectedImageURI;
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();


    }

    @OnClick(R.id.cameraimageview)
    public void roundedImageClick()
    {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {


                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        cameraIntent();

                    } else {

                        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(RegisterActivity.this);
                        if (!marshMallowPermission.checkPermissionForCamera()) {
                            marshMallowPermission.requestPermissionForCamera();
                        } else {
                            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                                marshMallowPermission.requestPermissionForExternalStorage();
                            } else {
                                cameraIntent();

                            }
                        }
                    }


                } else if (options[item].equals("Choose from Gallery")) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        galleryIntent();
                    } else {

                        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(RegisterActivity.this);

                        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                            marshMallowPermission.requestPermissionForExternalStorage();
                        } else {
                            galleryIntent();

                        }


                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @OnClick(R.id.btn_profileSubmit)
    public void profileSubmitClick()
    {
        if (name.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            _name = name.getText().toString();
        }

        if (shopName.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter shop name", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            _shopName = shopName.getText().toString();
        }

        writeImageToCloud();

    }

    private void writeImageToCloud()
    {
        if (firebaseUser != null)
        {
            StorageReference riversRef = mStorageRef.child(firebaseUser.getUid() + "/profile");

            if (selectedImageURI != null) {


                riversRef.putFile(selectedImageURI)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.d("TAG Download URL : ", downloadUrl.toString());

                                writeDataToFirebase();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {


                                // Handle unsuccessful uploads
                                Log.d("TAG Storage Failed", "Storage Failed");
                                Toast.makeText(RegisterActivity.this, "Storage Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void writeDataToFirebase()
    {
        User user = new User(_name, _shopName,downloadUrl.toString());


        if (firebaseUser != null ) {


            db.collection("Users")
                    .document(firebaseUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "Request Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);



                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), thumbnail, "Title", null);

                selectedImageURI = Uri.parse(path);
                Log.d("URI",selectedImageURI.toString());
                roundedimageview.setImageBitmap(thumbnail);


            } else if (requestCode == SELECT_FILE) {

                selectedImageURI= data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImageURI, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bitmap_thumbnail = (BitmapFactory.decodeFile(picturePath));

                Log.d("URI 2",selectedImageURI.toString());

                roundedimageview.setImageBitmap(bitmap_thumbnail);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //firebaseAuth.signOut();
    }

    @Override
    public void onBackPressed()
    {}
}
