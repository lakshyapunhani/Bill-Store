package com.fabuleux.wuntu.billstore.Network;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface
{

    int INTERNAL_SERVER_ERROR = 500;
    int UNAUTHORIZED = 401;
    int INTERNET_FAILURE = 600;
    int RESPONSE_SUCCESS = 200;
    int NO_DATA_ON_SERVER = 204;
    int BAD_REQUEST = 400;
    int SERVER_DOWN = 503;

    @POST("registerDevice")
    Call<ResponseBody> registerDevice(@Body HashMap<String,String> hashMap);

    @POST("deviceNotification")
    Call<ResponseBody> sendNotification(@Body HashMap<String,Object> hashMap);
}
