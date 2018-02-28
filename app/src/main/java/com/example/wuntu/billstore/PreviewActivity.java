package com.example.wuntu.billstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuntu.billstore.Pojos.ItemPojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_generate;
    TextView tv_link;
    ImageView iv_image;

    @BindView(R.id.pdflayout)
    NestedScrollView scrollView;

    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    ProgressDialog progressDialog;

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

    @BindView(R.id.invoice_subtotal)
    TextView invoice_subtotal;

    @BindView(R.id.invoice_total)
    TextView invoice_total;

    private ArrayList<ItemPojo> itemList;
    private String customerName = "";
    private String customerAddress = "";
    private String customerGstNumber = "";
    private String invoiceDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        getIntentItems();
        setViews();
        init();
        fn_permission();
        listener();
        initTable();
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
        }
    }

    private void setViews()
    {
        txt_custName.setText(customerName);
        txt_custAddress.setText(customerAddress);
        txt_custGstNumber.setText(customerGstNumber);
        txt_invoiceDate.setText(invoiceDate);
    }

    private void init()
    {
        btn_generate = (Button)findViewById(R.id.btn_generate);
    }

    public void initTable()
    {
        for (int i = 0; i < itemList.size(); i++) {

            ItemPojo itemPojo = itemList.get(i);
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.invoice_row, tableLayoutItems, false);
            TextView itemName = (TextView) row.findViewById(R.id.tv0);
            TextView costPerItem= (TextView) row.findViewById(R.id.tv1);
            TextView quantity = (TextView) row.findViewById(R.id.tv2);
            TextView totalAmount = row.findViewById(R.id.tv3);

            //txtDeviceName.setText("Cello Bottle");
            itemName.setText(itemPojo.getItemName());
            //txtLastLoggedIn.setText("$200");
            costPerItem.setText(getResources().getString(R.string.rupee_sign) + itemPojo.getCostPerItem());
            //txtLocation.setText("3");
            quantity.setText(itemPojo.getQuantity());
            //textView.setText("$200");
            totalAmount.setText(getResources().getString(R.string.rupee_sign) + itemPojo.getTotalAmount());

            tableLayoutItems.addView(row);
        }
    }

    private void listener()
    {
        btn_generate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_generate:

                if (boolean_save) {
                    /*Intent intent = new Intent(getApplicationContext(), PDFViewActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(this, "Rukja bhai,abhi ye part krna baaki h", Toast.LENGTH_SHORT).show();

                } else {
                    if (boolean_permission) {
                        progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Please wait");
                        bitmap = loadBitmapFromView(scrollView, scrollView.getWidth(), scrollView.getChildAt(0).getHeight());
                        createPdf();
//                        saveBitmap(bitmap);
                    } else {

                    }

                    createPdf();
                    break;
                }
        }
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
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

        //paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/"+"test.pdf";
        String target = Environment.getExternalStorageDirectory().getPath() + "test.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            btn_generate.setText("Check PDF");
            boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
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

            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);

            }
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


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }
}
