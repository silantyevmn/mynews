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

    public static boolean checkInternetConnection() {
        boolean check = isInternetAvailable();
        if (!check) {
            Toast.makeText(App.getInstance(), App.getInstance().getString(R.string.no_internet_connection) +
                    "\n" + App.getInstance().getString(R.string.check_connection_settings), Toast.LENGTH_LONG).show();
        }
        return check;
    }
}
