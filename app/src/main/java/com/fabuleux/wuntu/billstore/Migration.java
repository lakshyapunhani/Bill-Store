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

            RealmObjectSchema cashInHand = schema.get("CashInHandRealm");
            cashInHand.addField("sentStatus", String.class)
                    .addField("submitStatus", String.class)
                    .addField("orderNumber", String.class)
                    .removeField("status");


            RealmObjectSchema orderRealm = schema.get("OrderRealm");
            orderRealm.removeField("isExpress")
                    .addField("isExpress", int.class);


            schema.create("NotificationRealm")
                    .addField("message", String.class)
                    .addField("timestamp", long.class)
                    .addField("id", long.class);


            schema.create("ChatPojo")
                    .addField("text", String.class)
                    .addField("senderName", String.class)
                    .addField("senderID", String.class)
                    .addField("time", String.class);

            oldVersion++;

        }

    }
}