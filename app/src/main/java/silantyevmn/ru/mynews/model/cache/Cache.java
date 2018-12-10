package silantyevmn.ru.mynews.model.cache;

import java.util.List;

import io.reactivex.Observable;
import silantyevmn.ru.mynews.model.entity.Articles;

public interface Cache {

    Observable<List<Articles>> getBookmarksList();

    Observable<Boolean> updateBookmark(Articles articles);

    Observable<Boolean> findBookmark(Articles articles);
}
