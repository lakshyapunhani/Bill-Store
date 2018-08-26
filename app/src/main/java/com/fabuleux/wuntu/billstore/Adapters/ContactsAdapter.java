package com.fabuleux.wuntu.billstore.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fabuleux.wuntu.billstore.Pojos.ContactPojo;
import com.fabuleux.wuntu.billstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;

    private ArrayList<ContactPojo> contactsList;
    CollectionReference conntactsReference;
    private FirebaseFirestore db;
    FirebaseUser firebaseUser;

    public ContactsAdapter(ArrayList<ContactPojo> contactsList)
    {
        db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        this.contactsList = contactsList;
        conntactsReference = db.collection("Users").document(firebaseUser.getUid()).collection("Contacts");
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        @BindView(R.id.iv_contactImage)
        ImageView iv_contactImage;

        @BindView(R.id.tv_contactName)
        TextView tv_contactName;

        @BindView(R.id.tv_contactPhoneNumber)
        TextView tv_contactPhoneNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {

        String name = contactsList.get(position).getContactName();

        String phoneNumber = contactsList.get(position).getContactPhoneNumber();

        ((ViewHolder)holder).tv_contactName.setText(name.trim());
        ((ViewHolder)holder).tv_contactPhoneNumber.setText(phoneNumber.trim());

        selectLetterImage(name,((ViewHolder) holder).iv_contactImage);

        ((ViewHolder)holder).mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                contactDescriptionDialog(contactsList.get(position));
            }
        });


    }

    public void contactDescriptionDialog(ContactPojo contactPojo)
    {
        final Dialog dialog=new Dialog(context,R.style.ThemeWithCorners);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view1=layoutInflater.inflate(R.layout.dialog_contact_view,null);

        ImageView iv_dialogContactImage = view1.findViewById(R.id.iv_dialogContactImage);

        TextView tv_dialogContactName = view1.findViewById(R.id.tv_dialogContactName);
        TextView tv_dialogContactPhoneNumber = view1.findViewById(R.id.tv_dialogContactPhoneNumber);
        TextView tv_dialogContactAddress = view1.findViewById(R.id.tv_dialogContactAddress);
        TextView tv_dialogContactGSTNumber = view1.findViewById(R.id.tv_dialogContactGSTNumber);

        ImageView img_callContact = view1.findViewById(R.id.img_callContact);
        ImageView img_messageContact = view1.findViewById(R.id.img_messageContact);
        ImageView img_editContact= view1.findViewById(R.id.img_editContact);
        ImageView img_deleteContact= view1.findViewById(R.id.img_deleteContact);

        selectLetterImage(contactPojo.getContactName(),iv_dialogContactImage);

        tv_dialogContactName.setText(contactPojo.getContactName());
        tv_dialogContactAddress.setText(contactPojo.getContactAddress());
        tv_dialogContactGSTNumber.setText(contactPojo.getContactGstNumber());
        tv_dialogContactPhoneNumber.setText(contactPojo.getContactPhoneNumber());


        dialog.setContentView(view1);
        dialog.show();
    }

    public void selectLetterImage(String name,ImageView imageView)
    {
        if (name.startsWith("a") || name.startsWith("A"))
        {
            imageView.setImageResource(R.drawable.ic_letter_a);

        }
        else if (name.startsWith("b") || name.startsWith("B"))
        {
            imageView.setImageResource(R.drawable.ic_letter_b);
        }
        else if (name.startsWith("c") || name.startsWith("C"))
        {
            imageView.setImageResource(R.drawable.ic_letter_c);
        }
        else if (name.startsWith("d") || name.startsWith("D"))
        {
            imageView.setImageResource(R.drawable.ic_letter_d);
        }
        else if (name.startsWith("e") || name.startsWith("E"))
        {
            imageView.setImageResource(R.drawable.ic_letter_e);
        }
        else if (name.startsWith("f") || name.startsWith("F"))
        {
            imageView.setImageResource(R.drawable.ic_letter_f);
        }
        else if (name.startsWith("g") || name.startsWith("G"))
        {
            imageView.setImageResource(R.drawable.ic_letter_g);
        }
        else if (name.startsWith("h") || name.startsWith("H"))
        {
            imageView.setImageResource(R.drawable.ic_letter_h);
        }
        else if (name.startsWith("i") || name.startsWith("I"))
        {
            imageView.setImageResource(R.drawable.ic_letter_i);
        }
        else if (name.startsWith("j") || name.startsWith("J"))
        {
            imageView.setImageResource(R.drawable.ic_letter_j);
        }
        else if (name.startsWith("k") || name.startsWith("K"))
        {
            imageView.setImageResource(R.drawable.ic_letter_k);
        }
        else if (name.startsWith("l") || name.startsWith("L"))
        {
            imageView.setImageResource(R.drawable.ic_letter_l);
        }
        else if (name.startsWith("m") || name.startsWith("M"))
        {
            imageView.setImageResource(R.drawable.ic_letter_m);
        }
        else if (name.startsWith("n") || name.startsWith("N"))
        {
            imageView.setImageResource(R.drawable.ic_letter_n);
        }
        else if (name.startsWith("o") || name.startsWith("O"))
        {
            imageView.setImageResource(R.drawable.ic_letter_o);
        }
        else if (name.startsWith("p") || name.startsWith("P"))
        {
            imageView.setImageResource(R.drawable.ic_letter_p);
        }
        else if (name.startsWith("q") || name.startsWith("Q"))
        {
            imageView.setImageResource(R.drawable.ic_letter_q);
        }
        else if (name.startsWith("r") || name.startsWith("R"))
        {
            imageView.setImageResource(R.drawable.ic_letter_r);
        }
        else if (name.startsWith("s") || name.startsWith("S"))
        {
            imageView.setImageResource(R.drawable.ic_letter_s);
        }
        else if (name.startsWith("t") || name.startsWith("T"))
        {
            imageView.setImageResource(R.drawable.ic_letter_t);
        }
        else if (name.startsWith("u") || name.startsWith("U"))
        {
            imageView.setImageResource(R.drawable.ic_letter_u);
        }
        else if (name.startsWith("v") || name.startsWith("V"))
        {
            imageView.setImageResource(R.drawable.ic_letter_v);
        }
        else if (name.startsWith("w") || name.startsWith("W"))
        {
            imageView.setImageResource(R.drawable.ic_letter_w);
        }
        else if (name.startsWith("x") || name.startsWith("X"))
        {
            imageView.setImageResource(R.drawable.ic_letter_x);
        }
        else if (name.startsWith("y") || name.startsWith("Y"))
        {
            imageView.setImageResource(R.drawable.ic_letter_y);
        }
        else if (name.startsWith("z") || name.startsWith("Z"))
        {
            imageView.setImageResource(R.drawable.ic_letter_z);
        }
    }


    @Override
    public int getItemCount()
    {
        return contactsList.size();
    }
}
