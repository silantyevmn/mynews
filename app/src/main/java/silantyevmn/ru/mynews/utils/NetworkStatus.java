package silantyevmn.ru.mynews.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import silantyevmn.ru.mynews.App;

public class NetworkStatus {

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
