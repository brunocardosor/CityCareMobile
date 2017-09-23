package com.example.administrador.citycaremobile.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Administrador on 23/09/2017.
 */

public final class  SystemUtils {

    public  boolean verificaConexao(Context context) {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
