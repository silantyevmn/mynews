package silantyevmn.ru.mynews.utils;

import android.content.Intent;

import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;

/**
 * Created by silan on 03.12.2018.
 */

public class PopurManager {

    public static void share(Articles articles) {
        if(NetworkStatus.checkInternetConnection()){
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/html");
            intentShare.putExtra(Intent.EXTRA_TEXT, articles.getUrl());

            Intent share= Intent.createChooser(intentShare, App.getInstance().getString(R.string.title_share));
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            App.getInstance().startActivity(share);

            /*if (intentShare.resolveActivity(App.getInstance().getPackageManager()) != null) {
                App.getInstance().startActivity(share);
            }*/
            /*shareIntent.setType("text/html");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Launcher");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(shareIntent, "Share Deal"));*/
        }
    }
}
