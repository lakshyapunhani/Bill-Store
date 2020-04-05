package com.fabuleux.wuntu.billstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.fabuleux.wuntu.billstore.Adapters.NotificationsAdapter;
import com.fabuleux.wuntu.billstore.Pojos.NotificationPojo;
import com.fabuleux.wuntu.billstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity {

    @BindView(R.id.notificationsRecyclerView)
    RecyclerView recyclerView;

    LinearLayoutManager mLayoutManager;

    ArrayList<NotificationPojo> notificationList;

    NotificationsAdapter notificationsAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        notificationList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        notificationsAdapter = new NotificationsAdapter(notificationList);

        mLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(notificationsAdapter);

        CollectionReference collectionReference= db.collection("Users").
                document(firebaseUser.getUid()).collection("Notifications");


        collectionReference.orderBy("time",Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null)
                {
                    Toast.makeText(NotificationsActivity.this, "Some error occured. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                notificationList.clear();
                for (DocumentSnapshot doc : documentSnapshots)
                {
                    NotificationPojo notificationPojo = doc.toObject(NotificationPojo.class);
                    notificationList.add(notificationPojo);
                }
                notificationsAdapter.notifyDataSetChanged();
            }
        });


    }
}
