package com.fabuleux.wuntu.billstore.Network;

import android.content.Context;
import com.fabuleux.wuntu.billstore.Manager.SessionManager;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonRequest
{
    private static volatile CommonRequest commonRequest;
    private SessionManager sessionManager;
    private Context context;

    private CommonRequest(Context context) {
        sessionManager= new SessionManager(context);
        this.context = context;
    }

    public static CommonRequest getInstance(Context context){
        if (commonRequest == null) {
            synchronized (CommonRequest.class) {
                commonRequest = new CommonRequest(context);
            }
        }
        return commonRequest;
    }

    public void sendDeviceToken(String UID)
    {

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("deviceId",UID);
        hashMap.put("deviceToken",sessionManager.getDeviceToken());

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiService.registerDevice(hashMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                /*if (response.code() == ApiInterface.RESPONSE_SUCCESS || response.code() == ApiInterface.NO_DATA_ON_SERVER)
                {
                }
                else
                {
                }*/
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void sendNotification(HashMap<String,Object> hashMap)
    {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.sendNotification(hashMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                /*if (response.code() == ApiInterface.RESPONSE_SUCCESS || response.code() == ApiInterface.NO_DATA_ON_SERVER)
                {
                    Toast.makeText(context, "Send notification Working correctly", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Send notification Not working", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(context, "Send notification Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
