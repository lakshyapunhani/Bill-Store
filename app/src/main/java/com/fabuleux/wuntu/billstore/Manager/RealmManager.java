package com.fabuleux.wuntu.billstore.Manager;

import android.util.Log;

import com.fabuleux.wuntu.billstore.Pojos.ItemRealm;
import com.fabuleux.wuntu.billstore.Pojos.ItemSelectionPojo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmManager {

    public static void deleteAllRealm() {
        try(Realm realm= Realm.getDefaultInstance()) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    RealmResults<ItemRealm> list = realm.where(ItemRealm.class).findAll();


                    list.deleteAllFromRealm();

                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("aaaaaaaaa", "aaaaaaaaaaaa");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.d("aaaaaaaaa", "aaaaaaaaaaaa");
                }
            });
        }
    }


    public static void addItemsInRealm(final List<ItemSelectionPojo> response)
    {
        try(Realm realm = Realm.getDefaultInstance())
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm)
                {
                    for (int i = 0;i<response.size();i++) {
                        if (!getItem(response.get(i).getProductId()))
                        {
                        ItemRealm itemRealm = new ItemRealm(response.get(i).getProductId(), response.get(i).getProductName(), response.get(i).getProductRate()
                                , response.get(i).getProductDescription(), 0, "N");
                        bgrealm.insertOrUpdate(itemRealm);
                    }
                    }
                }
            });
        }
    }

    public static void updateNumItem(final String id, final int numProducts)
    {
        try(Realm realm = Realm.getDefaultInstance())
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm) {
                    ItemRealm itemRealm = bgrealm.where(ItemRealm.class).equalTo("productId",id).findFirst();
                    if (itemRealm != null)
                    {
                        itemRealm.setNumProducts(numProducts);
                    }
                }
            });
        }
    }

    public static void updateSavedItem(final String id)
    {
        try(Realm realm = Realm.getDefaultInstance())
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm) {
                    ItemRealm itemRealm = bgrealm.where(ItemRealm.class).equalTo("productId",id).findFirst();
                    if (itemRealm != null)
                    {
                        itemRealm.setIsSaved("S");
                        bgrealm.insertOrUpdate(itemRealm);
                    }
                }
            });
        }
    }

    public static void updateSavedItems(final ArrayList<ItemSelectionPojo> arrayList)
    {
        try(Realm realm = Realm.getDefaultInstance())
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm) {
                    for (int i = 0;i<arrayList.size();i++) {
                        ItemRealm itemRealm = bgrealm.where(ItemRealm.class).equalTo("productId", arrayList.get(i).getProductId()).findFirst();
                        if (itemRealm != null) {
                            itemRealm.setNumProducts(arrayList.get(i).getNumProducts());
                            itemRealm.setIsSaved("S");
                            bgrealm.insertOrUpdate(itemRealm);
                        }
                    }
                }
            });
        }
    }

    public static ArrayList<ItemSelectionPojo> getItemsList()
    {
        try (Realm realm = Realm.getDefaultInstance())
        {
            ArrayList<ItemSelectionPojo> itemList = new ArrayList<>();

            RealmResults<ItemRealm> itemRealms = realm.where(ItemRealm.class).findAllSorted("productName", Sort.DESCENDING);
            for (int i = 0;i<itemRealms.size();i++)
            {
                if (itemRealms.get(i).isValid())
                {
                    ItemSelectionPojo itemListPojo = new ItemSelectionPojo();
                    itemListPojo.setProductDescription(itemRealms.get(i).getProductDescription());
                    itemListPojo.setProductId(itemRealms.get(i).getProductId());
                    itemListPojo.setProductName(itemRealms.get(i).getProductName());
                    itemListPojo.setProductRate(itemRealms.get(i).getProductRate());
                    itemListPojo.setNumProducts(itemRealms.get(i).getNumProducts());
                    itemList.add(itemListPojo);
                }
            }
            return itemList;

        }
    }

    public static ArrayList<ItemSelectionPojo> getSelecteditems()
    {
        try (Realm realm = Realm.getDefaultInstance())
        {
            ArrayList<ItemSelectionPojo> itemList = new ArrayList<>();

            RealmResults<ItemRealm> itemRealms = realm.where(ItemRealm.class).greaterThan("numProducts",0).findAll();
            for (int i = 0;i<itemRealms.size();i++)
            {
                if (itemRealms.get(i).isValid())
                {
                    ItemSelectionPojo itemListPojo = new ItemSelectionPojo();
                    itemListPojo.setProductDescription(itemRealms.get(i).getProductDescription());
                    itemListPojo.setProductId(itemRealms.get(i).getProductId());
                    itemListPojo.setProductName(itemRealms.get(i).getProductName());
                    itemListPojo.setProductRate(itemRealms.get(i).getProductRate());
                    itemListPojo.setNumProducts(itemRealms.get(i).getNumProducts());
                    itemList.add(itemListPojo);
                }
            }
            return itemList;

        }
    }

    public static ArrayList<ItemSelectionPojo> getSavedItems()
    {
        try (Realm realm = Realm.getDefaultInstance())
        {
            ArrayList<ItemSelectionPojo> itemList = new ArrayList<>();

            RealmResults<ItemRealm> itemRealms = realm.where(ItemRealm.class).equalTo("isSaved","S").greaterThan("numProducts",0).findAll();
            for (int i = 0;i<itemRealms.size();i++)
            {
                if (itemRealms.get(i).isValid())
                {
                    ItemSelectionPojo itemListPojo = new ItemSelectionPojo();
                    itemListPojo.setProductDescription(itemRealms.get(i).getProductDescription());
                    itemListPojo.setProductId(itemRealms.get(i).getProductId());
                    itemListPojo.setProductName(itemRealms.get(i).getProductName());
                    itemListPojo.setProductRate(itemRealms.get(i).getProductRate());
                    itemListPojo.setNumProducts(itemRealms.get(i).getNumProducts());
                    itemList.add(itemListPojo);
                }
            }
            return itemList;

        }
    }

    public static void deleteItemsRealm(final List<ItemSelectionPojo> response)
    {
        try(Realm realm = Realm.getDefaultInstance())
        {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgrealm) {
                    RealmResults<ItemRealm> itemRealms = bgrealm.where(ItemRealm.class).findAll();
                    itemRealms.deleteAllFromRealm();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess()
                {
                    if (response != null)
                    {
                        addItemsInRealm(response);
                    }


                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {

                }
            });
        }
    }





    public static boolean getItem(final String id)
    {
        try(final Realm realm = Realm.getDefaultInstance())
        {
            ItemRealm itemRealm= realm.where(ItemRealm.class).equalTo("productId", id).findFirst();
            if (itemRealm != null && itemRealm.isValid())
            {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static ItemSelectionPojo getSalesItemPojoById(final String id) {
        try (Realm realm = Realm.getDefaultInstance()){
            ItemRealm itemRealm = realm.where(ItemRealm.class).equalTo("productId", id).findFirst();
            if (itemRealm != null && itemRealm.isValid()) {
                return new ItemSelectionPojo(itemRealm);
            } else {
                return null;
            }
        }
    }

}
