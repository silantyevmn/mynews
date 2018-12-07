package silantyevmn.ru.mynews.utils;

import java.util.Locale;


public class LocaleManager {
    public static String getCountry() {
        return Locale.getDefault().getCountry().toLowerCase();
    }
}
