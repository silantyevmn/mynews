package silantyevmn.ru.mynews.utils;

import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;

public class Messages {

    public static String getErrorNoInternetConnection() {
        return App.getInstance().getString(R.string.no_internet_connection) + "\n" + App.getInstance().getString(R.string.check_connection_settings);
    }

    public static String getErrorLoadNetwork(){
        return App.getInstance().getString(R.string.error_load_network);
    }

    public static String getErrorLoadCache() {
        return App.getInstance().getString(R.string.error_load_cache);
    }

    public static String getBookmarkAdd() {
        return App.getInstance().getString(R.string.bookmark_add);
    }

    public static String getBookmarkRemove() {
        return App.getInstance().getString(R.string.bookmark_remove);
    }

    public static String getErrorUpdateBookmark() {
        return App.getInstance().getString(R.string.error_update_bookmark);
    }

    public static String getBookmarkSuccessAdd() {
        return App.getInstance().getString(R.string.bookmark_success_add);
    }

    public static String getBookmarkSuccessRemove() {
        return App.getInstance().getString(R.string.bookmark_success_remove);
    }

    public static String getErrorStatusBookmark() {
        return App.getInstance().getString(R.string.error_find_status_bookmark);
    }


    public static String getTitleCategory(int position) {
        return App.getInstance().getResources().getStringArray(R.array.category_list)[position];
    }

    public static String getNoNewsFound() {
        return App.getInstance().getResources().getString(R.string.no_news_found);
    }

    public static String getTextLinkCopied() {
        return App.getInstance().getResources().getString(R.string.link_copied);
    }
}
