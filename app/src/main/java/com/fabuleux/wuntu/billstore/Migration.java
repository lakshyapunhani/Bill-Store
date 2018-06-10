package com.fabuleux.wuntu.billstore;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;
import io.realm.annotations.PrimaryKey;

/**
 * Created by saksham on 30/4/17.
 */

public class Migration implements RealmMigration {
    private int i = 0;
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {

            schema.create("ItemRealm")
                    .addField("productId",String.class,FieldAttribute.PRIMARY_KEY)
                    .addField("productName",String.class)
                    .addField("productRate",String.class)
                    .addField("productDescription",String.class)
                    .addField("numProducts",int.class)
                    .addField("isSaved",String.class);

            oldVersion++;

        }

    }
}