package com.fabuleux.wuntu.billstore.Fcm;

import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Dell on 6/8/16.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        assert refreshedToken != null;
        Freshchat.getInstance(this).setPushRegistrationToken(refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
    }



}
