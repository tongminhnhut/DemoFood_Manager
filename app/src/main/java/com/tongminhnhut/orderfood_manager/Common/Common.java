package com.tongminhnhut.orderfood_manager.Common;

import com.tongminhnhut.orderfood_manager.Remote.APIService;
import com.tongminhnhut.orderfood_manager.Remote.RetrofitClient;
import com.tongminhnhut.orderfood_manager.model.Requests;
import com.tongminhnhut.orderfood_manager.model.User;

/**
 * Created by nhut on 2/22/2018.
 */

public class Common {
    public static User curentUser ;
    public static Requests currentRequest ;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static String convertCodeStatus (String code){
        if (code.equals("0"))
            return "Đang làm" ;
        if (code.equals("1"))
            return "Hoàn thành" ;
        else
            return "Giao tận bàn" ;
    }

    private static final String BASE_URL = "https://fcm.googleapis.com/";
    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }


}
