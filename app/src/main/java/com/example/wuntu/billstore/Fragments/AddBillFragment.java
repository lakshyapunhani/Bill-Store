package com.example.wuntu.billstore.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Adapters.AddDocumentsAdapter;
import com.example.wuntu.billstore.Dialogs.SearchableSpinner;
import com.example.wuntu.billstore.EventBus.SetCurrentFragmentEvent;
import com.example.wuntu.billstore.Pojos.AddBillDetails;
import com.example.wuntu.billstore.Pojos.VendorDetails;
import com.example.wuntu.billstore.R;
import com.example.wuntu.billstore.Utils.MarshMallowPermission;
import com.example.wuntu.billstore.Utils.RecyclerViewListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBillFragment extends Fragment {

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

    private Context context;
    long timestamp;

    String timestampString;

    ArrayList<VendorDetails> vendorsList;

    ArrayList<String> vendorNameList;

    ArrayList<String> imagesList;

    private int REQUEST_CAMERA = 0;

    private int MAX_ATTACHMENT_COUNT = 6;

    Uri selectedImageURI;

    int spinnerValue;

    boolean vendorView, statusView;

    String newVendorName,newVendorAddress;

    String billAmount,billDescription,billDate;
    String billStatus = "Due";

    MarshMallowPermission marshMallowPermission;

    Map<String,String> billImages;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;
    private StorageReference mStorageRef;

    ArrayAdapter<String> spinnerAdapter;

    ProgressDialog progressDialog;

    String billNumber = "";
    SimpleDateFormat convertDf = new SimpleDateFormat("MMMM dd, yyyy");

    public AddBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_bill, container, false);

        ButterKnife.bind(this,view);

        marshMallowPermission = new MarshMallowPermission(getActivity());
        billImages = new HashMap<>();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Saving");

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        imagesList = new ArrayList<>();
        vendorsList = new ArrayList<>();

        vendorNameList = new ArrayList<>();

        long date = System.currentTimeMillis();

        String dateString = convertDf.format(date);
        text_pickDate.setText(dateString);

        addDocumentsAdapter = new AddDocumentsAdapter(imagesList);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setAdapter(addDocumentsAdapter);

        recycler_view.addOnItemTouchListener(
                new RecyclerViewListener(context, recycler_view, new RecyclerViewListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {

                        if (!(imagesList.size() >= 6))
                        {
                            if (position == imagesList.size() )
                            {
                                final CharSequence[] options = {"Take Photo", "Gallery", "Document" ,  "Cancel"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Add Document!");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (options[item].equals("Take Photo")) {


                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                                                cameraIntent();

                                            } else {

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
                                            } else {

                                                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                                                    marshMallowPermission.requestPermissionForExternalStorage();
                                                } else {
                                                    documentPicker();
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
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, vendorNameList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSpinner.setTitle("Select Vendor");
        vendorSpinner.setAdapter(spinnerAdapter);

        getVendorsList();

        return view;
    }

    private void getVendorsList()
    {
        CollectionReference billsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Vendors");

        billsReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null) {
                    Toast.makeText(context, "Bills Request Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                vendorsList.clear();
                vendorNameList.clear();

                if (documentSnapshots.isEmpty())
                {
                    newVendorViewClick();
                    return;
                }
                else
                {
                    existingVendorViewClick();
                }

                for (DocumentSnapshot doc : documentSnapshots) {
                    if (doc.exists())
                    {
                        VendorDetails vendorDetails = doc.toObject(VendorDetails.class);
                        vendorsList.add(vendorDetails);
                        vendorNameList.add(vendorDetails.getVendorName());
                    }
                }

                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), thumbnail, "Title", null);

                selectedImageURI = Uri.parse(path);
                String selectedImage = selectedImageURI.toString();
                imagesList.add(selectedImage);

                if (imagesList.size() >= 6)
                {
                    addDocumentsAdapter.hideFooter();
                }
                addDocumentsAdapter.notifyDataSetChanged();


            }

            else if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO)
            {
                if(data!=null)
                {
                    imagesList.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    if (imagesList.size() >= 6)
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
                    imagesList.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

                    if (imagesList.size() >= 6)
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
        DatePickerDialog datePickerDialog = new  DatePickerDialog(context, R.style.DatePickerTheme, mDateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }


    @OnClick(R.id.radio_due)
    public void onDueRadioButtonClicked()
    {
        statusView = false;
        billStatus = "Due";
    }

    @OnClick(R.id.radio_paid)
    public void onPaidRadioButtonClicked()
    {
        statusView = true;
        billStatus = "Paid";
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryPicker()
    {
        int maxCount = MAX_ATTACHMENT_COUNT - imagesList.size();

        if((imagesList.size())==MAX_ATTACHMENT_COUNT)
            Toast.makeText(context, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();
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
        int maxCount = MAX_ATTACHMENT_COUNT - imagesList.size();

        if ((imagesList.size()) == MAX_ATTACHMENT_COUNT)
            Toast.makeText(context, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " items", Toast.LENGTH_SHORT).show();

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

        if (!progressDialog.isShowing() && AddBillFragment.this.isVisible())
        {
            progressDialog.show();
        }

        timestamp = System.currentTimeMillis();
        timestampString = String.valueOf(timestamp);
        if (vendorView)
        {
            if (edt_newVendorName.getText().toString().trim().isEmpty())
            {
                if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
                {
                    progressDialog.hide();
                }
                Toast.makeText(context, "Fill Vendor Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edt_newVendorAddress.getText().toString().trim().isEmpty())
            {
                if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
                {
                    progressDialog.hide();
                }
                Toast.makeText(context, "Fill Vendor Address", Toast.LENGTH_SHORT).show();
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
            if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
            {
                progressDialog.hide();
            }
            Toast.makeText(context, getString(R.string.toast_please_select_date), Toast.LENGTH_SHORT).show();
            return;
        }

        if (edt_billAmount.getText().toString().trim().isEmpty())
        {
            if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
            {
                progressDialog.hide();
            }
            Toast.makeText(context, "Enter the bill amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!edt_billDescription.getText().toString().trim().isEmpty())
        {
            billDescription = edt_billDescription.getText().toString();
        }
        else billDescription = "";

        if (imagesList.isEmpty())
        {
            if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
            {
                progressDialog.hide();
            }
            Toast.makeText(context, "Please add atleast one document", Toast.LENGTH_SHORT).show();
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




            for (int i = 0; i< imagesList.size(); i++)
            {
                String fileName = imagesList.get(i);
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
                                if (downloadUrl != null) {
                                    billImages.put(uri.getLastPathSegment(),downloadUrl.toString());
                                }
                                writeDataToFirebase();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
                                {
                                    progressDialog.hide();
                                }
                                Log.d("TAG Storage Failed", "Storage Failed" + exception);
                                Toast.makeText(context, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void writeDataToFirebase()
    {
        VendorDetails vendorDetails = new VendorDetails(newVendorName,newVendorAddress);

        billNumber = autoGenerateInvoiceNumber();

        final AddBillDetails addBillDetails = new AddBillDetails(vendorDetails,billAmount,billDescription,billDate,billStatus,billImages,timestampString,billNumber);

        final CollectionReference vendorReference = db.collection("Users").document(firebaseUser.getUid()).collection("Vendors");

            vendorReference.document(newVendorName).set(vendorDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid)
                {
                    vendorReference.document(newVendorName)
                            .collection(firebaseUser.getUid()).document(billDate + "&&" + timestampString).set(addBillDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
                                    {
                                        progressDialog.hide();
                                    }
                                    Toast.makeText(context, "Bill Added", Toast.LENGTH_SHORT).show();
                                    EventBus.getDefault().post(new SetCurrentFragmentEvent("home","add_bill","make_bill","profile"));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
                            {
                                progressDialog.hide();
                            }
                            Toast.makeText(context, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (progressDialog.isShowing() && AddBillFragment.this.isVisible())
                    {
                        progressDialog.hide();
                    }
                    Toast.makeText(context, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private String autoGenerateInvoiceNumber()
    {
        double doublea = (Math.random() * 46656);
        String a = String.valueOf(doublea);
        String firstPart = "000" + a;
        firstPart = firstPart.substring(a.length() - 4,a.length());
        return firstPart;
    }

}
