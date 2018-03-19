package com.tongminhnhut.orderfood_manager.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    public static final String USER_KEY = "User";
    public static final String PMW_KEY = "Password";

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

    public static boolean isConnectedInternet (Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager !=null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo() ;
            if (infos !=null){
                for (int i = 0; i<infos.length;i++)
                {
                    if (infos[i].getState()== NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false ;
    }



}
