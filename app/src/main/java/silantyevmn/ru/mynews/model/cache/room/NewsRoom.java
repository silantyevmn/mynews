package silantyevmn.ru.mynews.model.cache.room;

import android.arch.persistence.room.Room;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import silantyevmn.ru.mynews.App;
import silantyevmn.ru.mynews.model.cache.Cache;
import silantyevmn.ru.mynews.model.entity.Articles;


public class NewsRoom implements Cache {
    private final String DATABASE_NAME = "bookmarksNews.db";
    private NewsRoomAbs database;

    public NewsRoom() {
        this.database = Room.databaseBuilder(App.getInstance(), NewsRoomAbs.class, DATABASE_NAME).allowMainThreadQueries().build();
    }

    @Override
    public Observable<List<Articles>> getBookmarksList() {
        return Observable.just(database.photoDao().getList());
    }

    @Override
    public Observable<Boolean> updateBookmark(Articles articles) {
        return Observable.just(database.photoDao().getList())
                .map(bookmarksList -> {
                    if (bookmarksList == null) {
                        bookmarksList = new ArrayList<>();
                    }
                    if (bookmarksList.contains(articles)) {
                        database.photoDao().delete(articles);
                        return false;
                    } else {
                        database.photoDao().insert(articles);
                        return true;
                    }
                });
    }

    @Override
    public Observable<Boolean> findBookmark(Articles articles) {
        return Observable.just(database.photoDao().getList())
                .map(bookmarksList -> {
                    return bookmarksList.contains(articles);
                });
    }
}
