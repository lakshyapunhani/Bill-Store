package com.fabuleux.wuntu.billstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.ProductSelectionAdapter;
import com.fabuleux.wuntu.billstore.EventBus.SendItemsEvent;
import com.fabuleux.wuntu.billstore.Manager.RealmManager;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;
import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;
import com.fabuleux.wuntu.billstore.Pojos.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class ProductSelectionActivity extends AppCompatActivity {

    @BindView(R.id.fast_scroller_recycler)
    IndexFastScrollRecyclerView mRecyclerView;

    private List<ItemSelectionPojo> itemList;

    @BindView(R.id.edt_searchProduct)
    EditText edt_searchProduct;

    @BindView(R.id.btn_viewAdded)
    Button viewAdded;

    SessionManager sessionManager;

    ProgressDialog progressDialog;

    int view_added = 0;

    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    CollectionReference productReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        ButterKnife.bind(this);

        sessionManager= new SessionManager(this);

        progressDialog = new ProgressDialog(ProductSelectionActivity.this);
        progressDialog.setMessage("Please wait");

        itemList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        View view1 = this.getCurrentFocus();

        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }

        //itemList = RealmManager.getItemsList();

        initialiseUI();


        productReference = db.collection("Users").document(firebaseUser.getUid()).collection("Products");

        productReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if (e != null)
                {
                    Toast.makeText(ProductSelectionActivity.this, "Not able to show products. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                itemList.clear();

                for (DocumentSnapshot doc : documentSnapshots)
                {
                    ProductModel productModel = doc.toObject(ProductModel.class);
                    ItemSelectionPojo itemSelectionPojo = new ItemSelectionPojo(productModel.getProductId(),productModel.getProductName(),productModel.getProductRate(),productModel.getProductDescription(),0);
                    itemList.add(itemSelectionPojo);
                }
                RealmManager.addItemsInRealm(itemList);
                initialiseUI();
            }
        });


    }

    protected void initialiseUI()
    {

        Collections.sort(itemList, new Comparator<ItemSelectionPojo>() {
            @Override
            public int compare(ItemSelectionPojo s1, ItemSelectionPojo s2) {
                return (s1.getProductName()).compareToIgnoreCase(s2.getProductName());
            }
        });

        ProductSelectionAdapter recyclerViewAdapter = new ProductSelectionAdapter(itemList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(recyclerViewAdapter);

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#33334c");
        mRecyclerView.setIndexBarCornerRadius(0);
        mRecyclerView.setIndexBarTransparentValue((float) 0.4);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(40);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#FFFFFF");
        mRecyclerView.setIndexBarVisibility(true);
        mRecyclerView.setIndexbarHighLateTextColor("#33334c");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);
    }


    @OnTextChanged(value = R.id.edt_searchProduct, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onTextChanged(CharSequence cs)
    {
        updateList(filterList(cs));
    }

    private ArrayList<ItemSelectionPojo> filterList(CharSequence cs)
    {
        mRecyclerView.removeAllViewsInLayout();
        ArrayList<ItemSelectionPojo> filteredList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++)
        {
            if (itemList.get(i).getProductName().toLowerCase().contains(cs.toString().toLowerCase())) {
                filteredList.add(itemList.get(i));
            }
        }
        return filteredList;

    }

    private void updateList(ArrayList<ItemSelectionPojo> arrayList)
    {
        mRecyclerView.setAdapter(new ProductSelectionAdapter(arrayList));
    }

    private ArrayList<ItemSelectionPojo> addedList()
    {
        mRecyclerView.removeAllViewsInLayout();
        itemList = RealmManager.getSelecteditems();
        ArrayList<ItemSelectionPojo> filteredList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++)
        {
            if (itemList.get(i).getNumProducts() > 0)
            {
                filteredList.add(itemList.get(i));
            }
        }
        return filteredList;
    }

    @OnClick(R.id.btn_viewAdded)
    public void viewAddedClick()
    {
        if (view_added == 0)
        {
            view_added = 1;
            viewAdded.setText("View all");
            updateList(addedList());
        }
        else
        {
            mRecyclerView.removeAllViewsInLayout();
            view_added = 0;
            viewAdded.setText("View added");
            itemList = RealmManager.getItemsList();
            initialiseUI();
        }
    }

    @OnClick(R.id.btn_save)
    public void saveButton()
    {
        ArrayList<ItemSelectionPojo> savedList = new ArrayList<>();

        itemList = RealmManager.getSelecteditems();

        for (int i =0;i<itemList.size();i++)
        {
            RealmManager.updateSavedItem(itemList.get(i).getProductId());
        }
        EventBus.getDefault().postSticky(new SendItemsEvent(savedList,"1"));
        finish();
    }
}
