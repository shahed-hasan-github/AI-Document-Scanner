package com.scan.android.documentscanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Check_Network {

    private static final String TAG = Check_Network.class.getSimpleName();



    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {

            return false;
        }
        else
        {
            if(info.isConnected())
            {

                return true;
            }
            else
            {

                return true;
            }

        }
    }





}
