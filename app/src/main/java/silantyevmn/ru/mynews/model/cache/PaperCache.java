package silantyevmn.ru.mynews.model.cache;


import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.Observable;
import silantyevmn.ru.mynews.model.entity.Articles;


public class PaperCache implements Cache {
    private final String FAVORITES_KEY = "favorites_key";

    @Override
    public Observable<List<Articles>> getBookmarksList() {
        return Observable.create(emitter -> {
            List<Articles> bookmarksList = Paper.book().read(FAVORITES_KEY);
            if (bookmarksList == null) {
                bookmarksList = new ArrayList<>();
            }
            emitter.onNext(bookmarksList);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Boolean> updateBookmark(Articles articles) {
        return Observable.create(emitter -> {
            List<Articles> bookmarksList = Paper.book().read(FAVORITES_KEY);
            if (bookmarksList == null) {
                bookmarksList = new ArrayList<>();
            }
            if (bookmarksList.contains(articles)) {
                bookmarksList.remove(articles);
                emitter.onNext(false);
            } else {
                bookmarksList.add(articles);
                emitter.onNext(true);
            }
            Paper.book().write(FAVORITES_KEY, bookmarksList);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Boolean> findBookmark(Articles articles) {
        return Observable.create(emitter -> {
            List<Articles> bookmarksList = Paper.book().read(FAVORITES_KEY);
            if (bookmarksList == null || bookmarksList.size() == 0) {
                emitter.onNext(false);
            } else {
                emitter.onNext(bookmarksList.contains(articles));
            }
            emitter.onComplete();
        });

    }

}
