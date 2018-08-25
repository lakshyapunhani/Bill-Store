package com.fabuleux.wuntu.billstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.EventBus.EventClearBill;
import com.fabuleux.wuntu.billstore.EventBus.InternetStatus;
import com.fabuleux.wuntu.billstore.Manager.RealmManager;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.CustomerDetails;
import com.fabuleux.wuntu.billstore.Pojos.ItemPojo;
import com.fabuleux.wuntu.billstore.Pojos.MakeBillDetails;
import com.fabuleux.wuntu.billstore.R;
import com.fabuleux.wuntu.billstore.Utils.NetworkReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.pdflayout)
    NestedScrollView scrollView;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    Bitmap bitmap;

    PopupMenu popup;

    @BindView(R.id.txt_shopName)
    TextView txt_shopName;

    @BindView(R.id.txt_shopAddress)
    TextView txt_shopAddress;

    @BindView(R.id.txt_gstNumber)
    TextView txt_gstNumber;

    @BindView(R.id.txt_invoiceDate)
    TextView txt_invoiceDate;

    @BindView(R.id.txt_custName)
    TextView txt_custName;

    @BindView(R.id.txt_custAddress)
    TextView txt_custAddress;

    @BindView(R.id.txt_custGstNumber)
    TextView txt_custGstNumber;

    @BindView(R.id.tableLayoutItems)
    TableLayout tableLayoutItems;

    @BindView(R.id.invoice_total)
    TextView invoice_total;


    @BindView(R.id.invoice_subTotal)
    TextView invoice_subTotal;

    @BindView(R.id.btn_print)
    TextView btn_print;


    @BindView(R.id.menu_dots_preview)
    ImageView menu_dots;

    @BindView(R.id.layout_cgst)
    LinearLayout layout_cgst;

    @BindView(R.id.layout_sgst)
    LinearLayout layout_sgst;

    @BindView(R.id.layout_igst)
    LinearLayout layout_igst;

    @BindView(R.id.invoice_cgst)
    TextView invoice_cgst;

    @BindView(R.id.invoice_sgst)
    TextView invoice_sgst;

    @BindView(R.id.invoice_igst)
    TextView invoice_igst;

    private ArrayList<ItemPojo> itemList;
    private String customerName = "";
    private String customerAddress = "";
    private String customerGstNumber = "";
    private String invoiceDate = "";

    HashMap<String,ItemPojo> billItems;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    long timestamp;
    String timestampString;

    double totalAmount = 0;

    double gstRate = 0,cgst=0,sgst = 0,igst = 0;

    File filePath;

    String invoiceNumber= "";

    boolean printClicked = false;
    boolean showSave = true;
    ProgressDialog progressDialog;

    private SessionManager sessionManager;
    private NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        networkReceiver = new NetworkReceiver();

        billItems = new HashMap<>();
        itemList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving");
        progressDialog.setMessage("Please wait...");

        popup = new PopupMenu(this, menu_dots);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.preview_menu, popup.getMenu());

        firebaseUser = firebaseAuth.getCurrentUser();

        timestamp = System.currentTimeMillis();
        timestampString = String.valueOf(timestamp);

        billItems.clear();
        itemList.clear();

        getIntentItems();
        getShopDetails();
        fn_permission();
        initTable();
        setViews();
    }

    private void getShopDetails()
    {
        DocumentReference profileReference = db.collection("Users").document(firebaseUser.getUid());
        profileReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (!documentSnapshot.exists())
                {
                    Toast.makeText(PreviewActivity.this, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.contains("shop_name"))
                {
                    txt_shopName.setText(documentSnapshot.get("shop_name").toString());
                }
                if (documentSnapshot.contains("shop_address"))
                {
                    txt_shopAddress.setText(documentSnapshot.get("shop_address").toString());
                }
                if (documentSnapshot.contains("shop_gst"))
                {
                    txt_gstNumber.setText(documentSnapshot.get("shop_gst").toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PreviewActivity.this, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIntentItems()
    {
        if (getIntent() != null)
        {
            itemList = getIntent().getParcelableArrayListExtra("ItemList");
            customerName = getIntent().getStringExtra("Customer Name");
            customerAddress = getIntent().getStringExtra("Customer Address");
            customerGstNumber = getIntent().getStringExtra("Customer GST Number");
            invoiceDate = getIntent().getStringExtra("Invoice Date");
            showSave = getIntent().getBooleanExtra("showSave",false);
            gstRate = getIntent().getDoubleExtra("gstValue",0.0);
            cgst = getIntent().getDoubleExtra("cgst",0.0);
            sgst = getIntent().getDoubleExtra("sgst",0.0);
            igst = getIntent().getDoubleExtra("igst",0.0);
        }
    }

    private void setViews()
    {
        String amount = String.valueOf(totalAmount);
        double subtotalGst = totalAmount * gstRate;
        double subtotal = totalAmount + subtotalGst;
        String subtotalAmount = String.valueOf(subtotal);
        invoice_subTotal.setText(getResources().getString(R.string.rupee_sign) +subtotalAmount);
        txt_custName.setText(customerName);
        txt_custAddress.setText(customerAddress);
        txt_custGstNumber.setText(customerGstNumber);
        txt_invoiceDate.setText(invoiceDate);
        invoice_total.setText(getResources().getString(R.string.rupee_sign) + amount);
        if (showSave)
        {
            menu_dots.setVisibility(View.VISIBLE);
        }
        else
        {
            menu_dots.setVisibility(View.GONE);
        }

        if (cgst != 0.0)
        {
            layout_cgst.setVisibility(View.VISIBLE);
            invoice_cgst.setText(String.valueOf(cgst)+ " %");
        }
        if (sgst != 0.0)
        {
            layout_sgst.setVisibility(View.VISIBLE);
            invoice_sgst.setText(String.valueOf(sgst)+ " %");
        }
        if (igst != 0.0)
        {
            layout_igst.setVisibility(View.VISIBLE);
            invoice_igst.setText(String.valueOf(igst) + " %");
        }

        if (progressDialog.isShowing() && !PreviewActivity.this.isDestroyed())
        {
            progressDialog.dismiss();
        }


    }

    public void initTable()
    {
        for (int i = 0; i < itemList.size(); i++)
        {
            totalAmount = totalAmount + Double.parseDouble(itemList.get(i).getTotalAmount());

            ItemPojo itemPojo = itemList.get(i);
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.invoice_row, tableLayoutItems, false);
            TextView itemName = row.findViewById(R.id.tv0);
            TextView costPerItem= row.findViewById(R.id.tv1);
            TextView quantity = row.findViewById(R.id.tv2);
            TextView totalAmount = row.findViewById(R.id.tv3);

            itemName.setText(itemPojo.getItemName());
            costPerItem.setText(getResources().getString(R.string.rupee_sign) + itemPojo.getCostPerItem());
            quantity.setText(itemPojo.getQuantity());
            totalAmount.setText(getResources().getString(R.string.rupee_sign) + itemPojo.getTotalAmount());

            tableLayoutItems.addView(row);
        }
    }

    public void saveButton()
    {

        if (!progressDialog.isShowing() && !PreviewActivity.this.isDestroyed())
        {
            progressDialog.show();
        }
        for (int i = 0;i<itemList.size();i++)
        {
            ItemPojo itemPojo = new ItemPojo(itemList.get(i).getProductId(),itemList.get(i).getItemName(),itemList.get(i).getCostPerItem(),itemList.get(i).getQuantity(),itemList.get(i).getTotalAmount());
            billItems.put(itemList.get(i).getItemName(),itemPojo);
        }

        invoiceNumber = autoGenerateInvoiceNumber();
        final CollectionReference customerReference = db.collection("Users").document(firebaseUser.getUid()).collection("Customers");
        CustomerDetails customerDetails = new CustomerDetails(customerName,customerAddress,customerGstNumber);
        final MakeBillDetails makeBillDetails = new MakeBillDetails(customerDetails, invoiceDate,cgst,sgst,igst,gstRate,billItems,totalAmount,invoiceNumber);

        customerReference.document(customerName).set(customerDetails);
        customerReference.document(customerName).collection(firebaseUser.getUid())
                .document(invoiceDate + " && " + timestampString).set(makeBillDetails);

        Toast.makeText(this, "Invoice Saved", Toast.LENGTH_SHORT).show();
        if (printClicked)
        {
            if (progressDialog.isShowing() && !PreviewActivity.this.isDestroyed())
            {
                progressDialog.dismiss();
            }
            openPdf();
        }
        else
        {
            if (progressDialog.isShowing() && !PreviewActivity.this.isDestroyed())
            {
                progressDialog.dismiss();
            }
            EventBus.getDefault().postSticky(new EventClearBill());
            finish();
        }

    }

    @OnClick(R.id.btn_print)
    public void printButton()
    {
        printClicked = true;
        if (boolean_permission) {
            bitmap = loadBitmapFromView(scrollView, scrollView.getWidth(), scrollView.getChildAt(0).getHeight());
            createPdf();
        } else
        {
            fn_permission();
        }
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height = displaymetrics.heightPixels ;
        float scrollHeight = scrollView.getChildAt(0).getHeight();
        float width = displaymetrics.widthPixels ;

        int convertHeight = (int) scrollHeight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/"+"Invoice.pdf";
        String target = Environment.getExternalStorageDirectory().getPath() + "test.pdf";
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            if (showSave)
            {
                saveButton();
            }
            else
            {
                openPdf();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    private void openPdf()
    {
        Intent intent;
        File file=new File(Environment.getExternalStorageDirectory()
                + File.separator + "Invoice.pdf");

        RealmManager.resetItemRealm();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri uri = FileProvider.getUriForFile(PreviewActivity.this, getPackageName() + ".provider", file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

            EventBus.getDefault().postSticky(new EventClearBill());
            finish();
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent = Intent.createChooser(intent, "Open File");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            EventBus.getDefault().postSticky(new EventClearBill());
            finish();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS);

        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;

            } else if (permissions.length > 0)
            {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String autoGenerateInvoiceNumber()
    {
        double doublea = (Math.random() * 46656);
        String a = String.valueOf(doublea);
        String firstPart = "000" + a;
        firstPart = firstPart.substring(a.length() - 4,a.length());
        return firstPart;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.menu_dots_preview)
    public void menuClick()
    {
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.save_bill)
                {
                    saveButton();
                }
                else if (item.getItemId() == R.id.discard_bill)
                {
                    EventBus.getDefault().postSticky(new EventClearBill());
                    finish();
                }
                return true;
            }
        });

        popup.show();
    }
}