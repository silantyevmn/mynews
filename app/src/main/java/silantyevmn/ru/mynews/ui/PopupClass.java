package silantyevmn.ru.mynews.ui;

import android.content.Intent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.ui.adapter.IBookmark;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

public class PopupClass {
    @Inject
    Repo repo;

    public PopupClass() {
        App.getInstance().getComponent().inject(this);
    }


    public void getStatusBookmark(Articles articles, IBookmark bookmarkStatus) {
        repo.findBookmark(articles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isBookmark -> {
                    bookmarkStatus.onSuccess(isBookmark);
                }, throwable -> bookmarkStatus.onError(Messages.getErrorStatusBookmark()));
    }

    public void share(Articles articles) {
        if (NetworkStatus.checkInternetConnection()) {
            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("text/html");
            intentShare.putExtra(Intent.EXTRA_TEXT, articles.getUrl());

            Intent shareIntent = Intent.createChooser(intentShare, App.getInstance().getString(R.string.title_share));
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            App.getInstance().startActivity(shareIntent);
        }
    }

    public void updateBookmark(Articles articles, IBookmark bookmarkUpdate) {
        repo.updateBookmark(articles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isBookmarks -> bookmarkUpdate.onSuccess(isBookmarks)
                        , throwable -> bookmarkUpdate.onError(Messages.getErrorUpdateBookmark()));
    }

}
