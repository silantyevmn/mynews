package silantyevmn.ru.mynews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import silantyevmn.ru.mynews.App;

public class SharedManager {
    private static final String KEY_CATEGORY_POSITION = "key_category_position";
    private static final int DEFAULT_CATEGORY_POSITION = 0;
    private static SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences("myShared", Context.MODE_PRIVATE);

    public static void setCategoryPosition(int position) {
        sharedPreferences.edit().putInt(KEY_CATEGORY_POSITION, position).commit();
    }

    public static int getCategoryPosition() {
        return sharedPreferences.getInt(KEY_CATEGORY_POSITION, DEFAULT_CATEGORY_POSITION);
    }
}
