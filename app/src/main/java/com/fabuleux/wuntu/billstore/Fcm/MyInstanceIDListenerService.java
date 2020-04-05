//package com.fabuleux.wuntu.billstore.Fcm;
//
//import android.widget.Toast;
//
//import com.fabuleux.wuntu.billstore.Manager.SessionManager;
//import com.fabuleux.wuntu.billstore.Network.ApiClient;
//import com.fabuleux.wuntu.billstore.Network.ApiInterface;
//import com.fabuleux.wuntu.billstore.Network.CommonRequest;
//import com.freshchat.consumer.sdk.Freshchat;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.iid.FirebaseInstanceId;
//
//import java.util.HashMap;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by Dell on 6/8/16.
// */
//public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. This call is initiated by the
//     * InstanceID provider.
//     */
//    // [START refresh_token]
//    @Override
//    public void onTokenRefresh() {
//        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        assert refreshedToken != null;
//        Freshchat.getInstance(this).setPushRegistrationToken(refreshedToken);
//        // TODO: Implement this method to send any registration to your app's servers.
//        registerDevice(refreshedToken);
//    }
//
//    private void registerDevice(String token)
//    {
//        FirebaseUser firebaseUser;
//        SessionManager sessionManager = new SessionManager(MyInstanceIDListenerService.this);
//        sessionManager.setDeviceToken(token);
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        Freshchat.getInstance(this).setPushRegistrationToken(token);
//        if (firebaseUser != null)
//        {
//            CommonRequest.getInstance(this).sendDeviceToken(firebaseUser.getUid());
//        }
//    }
//
//
//}
