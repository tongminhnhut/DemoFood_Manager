package com.tongminhnhut.orderfood_manager.Remote;



import com.tongminhnhut.orderfood_manager.model.MyResponse;
import com.tongminhnhut.orderfood_manager.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by tongminhnhut on 05/03/2018.
 */

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAyM5EHx8:APA91bH9pCXs2zE2eaHLyt9dWrbqzKw_c2D0b3QfKe1jaQVsaYbZ_xRARSTPc5N_J1gG4VgL_-FhHDmCfopAp4mHluYTs_5YrkfDKf4mwZlQsKAfBE7sKznDiwV5sQu-Ts2oOPzmEN7S"
            }

    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
