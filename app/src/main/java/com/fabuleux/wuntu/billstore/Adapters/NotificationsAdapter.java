package com.fabuleux.wuntu.billstore.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.NotificationPojo;
import com.fabuleux.wuntu.billstore.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<NotificationPojo> notificationsList= new ArrayList<>();

    NotificationPojo notificationPojo;

    public NotificationsAdapter(ArrayList<NotificationPojo> notificationsList)
    {
        this.notificationsList = notificationsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.notification_message)
        TextView notification_message;

        @BindView(R.id.notification_date)
        TextView notification_date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        notificationPojo = notificationsList.get(position);
        ((ViewHolder)holder).notification_date.setText(getDate(notificationPojo.getTime()));
        ((ViewHolder)holder).notification_message.setText(notificationPojo.getMessage());
    }

    private String getDate(String timestamp)
    {
        long time = Long.parseLong(timestamp);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return this.notificationsList.size();
    }
}
