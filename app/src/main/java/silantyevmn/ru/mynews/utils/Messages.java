package silantyevmn.ru.mynews.utils;

import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;

/**
 * Created by silan on 04.12.2018.
 */

public class Messages {

    public static String getErrorNoInternetConnection() {
        return App.getInstance().getString(R.string.no_internet_connection) + "\n" + App.getInstance().getString(R.string.check_connection_settings);
    }

    public static String getErrorLoadNetwork(){
        return App.getInstance().getString(R.string.error_load_network);
    }
}
