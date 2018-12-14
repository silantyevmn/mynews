package silantyevmn.ru.mynews.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;

public class NetworkStatus {

    public static boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
