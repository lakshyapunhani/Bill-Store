package com.example.wuntu.billstore;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.AddDocumentsAdapter;
import com.example.wuntu.billstore.Pojos.BillDetails;
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.Utils.MarshMallowPermission;
import com.example.wuntu.billstore.Utils.RecyclerViewListener;
import com.example.wuntu.billstore.Utils.SearchableSpinner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class AddNewBillActivity extends AppCompatActivity {

    AddDocumentsAdapter addDocumentsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.vendorSpinner)
    SearchableSpinner vendorSpinner;

    @BindView(R.id.radio_existingVendor)
    RadioButton radio_existingVendor;

    @BindView(R.id.radio_newVendor)
    RadioButton radio_newVendor;

    @BindView(R.id.innerView_existingVendor)
    LinearLayout innerView_existingVendor;

    @BindView(R.id.innerView_newVendor)
    LinearLayout innerView_newVendor;

    @BindView(R.id.outerView_newVendor)
    LinearLayout outerView_newVendor;

    @BindView(R.id.outerView_existingVendor)
    LinearLayout outerView_existingVendor;

    @BindView(R.id.text_pickDate)
    TextView text_pickDate;

    @BindView(R.id.edt_newVendorName)
    EditText edt_newVendorName;

    @BindView(R.id.edt_newVendorAddress) EditText edt_newVendorAddress;

    @BindView(R.id.edt_billAmount) EditText edt_billAmount;

    @BindView(R.id.edt_billDescription) EditText edt_billDescription;

    Calendar myCalendar = Calendar.getInstance();

    long timestamp;

    String timestampString;

    ArrayList<VendorDetails> vendorsList;

    ArrayList<String> vendorNameList;

    ArrayList<String> recyclerViewList;

    private int REQUEST_CAMERA = 0;

    private int MAX_ATTACHMENT_COUNT = 6;

    Uri selectedImageURI;

    int spinnerValue;
    String spinnerValueName,spinnerValueAddress;

    boolean vendorView, statusView;

    String newVendorName,newVendorAddress;

    String billAmount,billDescription,billDate;
    String billStatus = "Due";

    Map<String,String> billImages;


    private FirebaseFirestore db;
    FirebaseUser firebaseUser;
    private StorageReference mStorageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_add_new_bill);

        ButterKnife.bind(this);


        billImages = new HashMap<>();

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        recyclerViewList = new ArrayList<>();

        vendorNameList = new ArrayList<>();



        vendorsList = getIntent().getParcelableArrayListExtra("VENDOR_NAME_LIST");

        for (int i = 0;i<vendorsList.size();i++)
        {
            vendorNameList.add(vendorsList.get(i).getVendorName());
        }

        addDocumentsAdapter = new AddDocumentsAdapter(recyclerViewList);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setAdapter(addDocumentsAdapter);

        recycler_view.addOnItemTouchListener(
                new RecyclerViewListener(this, recycler_view, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {

                        if (!(recyclerViewList.size() >= 6))
                        {
                            if (position == recyclerViewList.size() )
                            {
                                final CharSequence[] options = {"Take Photo", "Gallery", "Document" ,  "Cancel"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewBillActivity.this);
                                builder.setTitle("Add Document!");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (options[item].equals("Take Photo")) {


                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                                                cameraIntent();

                                            } else {

                                                MarshMallowPermission marshMallowPermission = new MarshMallowPermission(AddNewBillActivity.this);
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


                                        } else if (options[item].equals("Gallery")) {

                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                                galleryPicker();
                                            } else {

                                                MarshMallowPermission marshMallowPermission = new MarshMallowPermission(AddNewBillActivity.this);

                                                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                                                    marshMallowPermission.requestPermissionForExternalStorage();
                                                } else {
                                                    galleryPicker();

                                                }


                                            }
                                        }
                                        else if (options[item].equals("Document")) {
                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                                documentPicker();
                                                Toast.makeText(AddNewBillActivity.this, "Document Clicked", Toast.LENGTH_SHORT).show();
                                            } else {

                                                MarshMallowPermission marshMallowPermission = new MarshMallowPermission(AddNewBillActivity.this);

                                                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                                                    marshMallowPermission.requestPermissionForExternalStorage();
                                                } else {
                                                    documentPicker();
                                                    Toast.makeText(AddNewBillActivity.this, "Document Clicked", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        else if (options[item].equals("Cancel")) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        }


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vendorNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setAdapter(spinnerAdapter);

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
                String selectedImage = selectedImageURI.toString();
                recyclerViewList.add(selectedImage);

                if (recyclerViewList.size() >= 6)
                {
                    addDocumentsAdapter.hideFooter();
                }
                addDocumentsAdapter.notifyDataSetChanged();


            }

            else if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO)
            {
                if(data!=null)
                {
                    recyclerViewList.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    if (recyclerViewList.size() >= 6)
                    {
                        addDocumentsAdapter.hideFooter();
                    }
                    addDocumentsAdapter.notifyDataSetChanged();
                }
            }

            else if (requestCode == FilePickerConst.REQUEST_CODE_DOC)
            {
                if (data!= null)
                {
                    recyclerViewList.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                    if (recyclerViewList.size() >= 6)
                    {
                        addDocumentsAdapter.hideFooter();
                    }
                    addDocumentsAdapter.notifyDataSetChanged();
                }
            }
        }
    }






    DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
                              int selectedDay) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat convertDf = new SimpleDateFormat("MMMM dd, yyyy");
            try {
                Date reportDate = df.parse(selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay);
                text_pickDate.setText((convertDf.format(reportDate)));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

    };

    @OnClick({R.id.view_existingVendor,R.id.radio_existingVendor})
    public void existingVendorViewClick()
    {
        vendorView = false;
        radio_existingVendor.setChecked(true);
        radio_newVendor.setChecked(false);
        TransitionManager.beginDelayedTransition(outerView_newVendor);
        innerView_newVendor.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(outerView_existingVendor);
        innerView_existingVendor.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.view_newVendor,R.id.radio_newVendor})
    public void newVendorViewClick()
    {
        vendorView = true;
        radio_newVendor.setChecked(true);
        radio_existingVendor.setChecked(false);
        TransitionManager.beginDelayedTransition(outerView_newVendor);
        innerView_newVendor.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(outerView_existingVendor);
        innerView_existingVendor.setVisibility(View.GONE);
    }

    @OnClick(R.id.text_pickDate)
    public void onClickPickDate()
    {
        DatePickerDialog datePickerDialog = new  DatePickerDialog(AddNewBillActivity.this, R.style.DatePickerTheme, mDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    @OnClick(R.id.radio_due)
    public void onDueRadioButtonClicked()
    {
        statusView = false;
        billStatus = "Due";
        Toast.makeText(this, "Radio Due Checked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.radio_paid)
    public void onPaidRadioButtonClicked()
    {
        statusView = true;
        billStatus = "Paid";
        Toast.makeText(this, "Radio Paid Checked", Toast.LENGTH_SHORT).show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryPicker()
    {
        int maxCount = MAX_ATTACHMENT_COUNT - recyclerViewList.size();

        if((recyclerViewList.size())==MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();
        else
        {
            FilePickerBuilder.getInstance()
                    .setMaxCount(maxCount)
                    .enableCameraSupport(false)
                    .setActivityTheme(R.style.FilePickerTheme)
                    .pickPhoto(this);
        }
    }

    private void documentPicker()
    {
        int maxCount = MAX_ATTACHMENT_COUNT - recyclerViewList.size();

        if ((recyclerViewList.size()) == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();

        else
        {
            FilePickerBuilder.getInstance().setMaxCount(maxCount)
                    .setActivityTheme(R.style.FilePickerTheme)
                    .pickFile(this);
        }

    }

    @OnItemSelected(value = R.id.vendorSpinner, callback = OnItemSelected.Callback.ITEM_SELECTED)
    void selectVehicle(AdapterView<?> adapterView, int newVal) {
        if (vendorsList.size()>0)
        {
            spinnerValue = newVal;
        }
    }

    @OnClick(R.id.btn_submitBill)
    public void submitBill()
    {
        timestamp = System.currentTimeMillis();
        timestampString = String.valueOf(timestamp);
        if (vendorView)
        {
            if (edt_newVendorName.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "Fill Vendor Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edt_newVendorAddress.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "Fill Vendor Address", Toast.LENGTH_SHORT).show();
                return;
            }

            newVendorName = edt_newVendorName.getText().toString();
            newVendorAddress = edt_newVendorAddress.getText().toString();
        }
        else
        {
            newVendorName = vendorNameList.get(spinnerValue);
            newVendorAddress = vendorsList.get(spinnerValue).getVendorAddress();
        }


        if (!text_pickDate.getText().toString().trim().equals("Select Bill Date"))
        {
            billDate = text_pickDate.getText().toString().trim();
            Log.d("tag",text_pickDate.getText().toString() + "");
        }
        else
        {
            Toast.makeText(AddNewBillActivity.this, getString(R.string.toast_please_select_date), Toast.LENGTH_SHORT).show();
            return;
        }

        if (edt_billAmount.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Enter the bill amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!edt_billDescription.getText().toString().trim().isEmpty())
        {
            billDescription = edt_billDescription.getText().toString();
        }
        else billDescription = "";

        if (recyclerViewList.isEmpty())
        {
            Toast.makeText(this, "Please add atleast one document", Toast.LENGTH_SHORT).show();
            return;
        }


        billAmount = edt_billAmount.getText().toString().trim();

        writeImageToCloud();

    }

    private void writeImageToCloud()
    {
        StorageReference riversRef;
        if (firebaseUser != null)
        {
            if (vendorView)
            {
                riversRef = mStorageRef.child(firebaseUser.getUid()).child(newVendorName);
            }
            else
            {
                riversRef = mStorageRef.child(firebaseUser.getUid()).child(newVendorName);
            }




            for (int i = 0;i<recyclerViewList.size();i++)
            {
                String fileName = recyclerViewList.get(i);
                String fileName1;
                final Uri uri;
                if (fileName.startsWith("content"))
                {
                    fileName1 = fileName;
                    uri = Uri.parse(fileName1);
                }
                else
                {
                    fileName1 = "file://" + fileName;
                    uri = Uri.parse(fileName1);
                }

                Log.d("TAG",fileName1);
                riversRef.child(billDate + "&&" + timestampString + "/" + uri.getLastPathSegment()).putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.d("TAG Download URL : ", downloadUrl.toString());
                                billImages.put(uri.getLastPathSegment(),downloadUrl.toString());
                                writeDataToFirebase();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Log.d("TAG Storage Failed", "Storage Failed" + exception);
                                Toast.makeText(AddNewBillActivity.this, "Storage Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        }
    }

    private void writeDataToFirebase()
    {
        VendorDetails vendorDetails = new VendorDetails(newVendorName,newVendorAddress);

        final BillDetails billDetails = new BillDetails(newVendorName,newVendorAddress,billAmount,billDescription,billDate,billStatus,billImages,timestampString);

        final CollectionReference vendorReference = db.collection("Users").document(firebaseUser.getUid()).collection("Bills");

        if (vendorView)
        {
            vendorReference.document(newVendorName).set(vendorDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {
                    vendorReference.document(newVendorName)
                            .collection(firebaseUser.getUid()).document(billDate + "&&" + timestampString).set(billDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddNewBillActivity.this, "Bill Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_out_up, R.anim.slide_out_down);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewBillActivity.this, "Bill not added", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(AddNewBillActivity.this, "Trader Request Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewBillActivity.this, "Trader Request Failure", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            vendorReference.document(newVendorName).collection(firebaseUser.getUid()).document(billDate + "&&" + timestampString).set(billDetails)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddNewBillActivity.this, "Bill Added", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_out_down);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewBillActivity.this, "Bill not Added", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_out_down);
    }
}
