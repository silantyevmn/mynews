package silantyevmn.ru.mynews.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.R;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.ui.adapter.IBookmark;

public class PopupClass {
    private static final String SHARE_TYPE = "text/html";

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
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType(SHARE_TYPE);
        intentShare.putExtra(Intent.EXTRA_TEXT, articles.getUrl());

        Intent shareIntent = Intent.createChooser(intentShare, App.getInstance().getString(R.string.title_share));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        App.getInstance().startActivity(shareIntent);
    }

    public void updateBookmark(Articles articles, IBookmark bookmarkUpdate) {
        repo.updateBookmark(articles)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isBookmarks -> bookmarkUpdate.onSuccess(isBookmarks)
                        , throwable -> bookmarkUpdate.onError(Messages.getErrorUpdateBookmark()));
    }

    public void openInBrowser(Articles articles) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articles.getUrl()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().startActivity(intent);
    }

    public void copy(Articles articles) {
        ClipboardManager clipManager = (ClipboardManager) App.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(SHARE_TYPE, articles.getUrl());
        clipManager.setPrimaryClip(clipData);
    }
}
