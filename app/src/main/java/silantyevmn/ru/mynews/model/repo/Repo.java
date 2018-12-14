package silantyevmn.ru.mynews.model.repo;

import java.util.List;

import io.reactivex.Observable;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.entity.News;

public interface Repo {
    Observable<News> getTopNews();

    Observable<News> getSearchNews(String newText);

    Observable<List<Articles>> getBookmarksList();

    Observable<Boolean> updateBookmark(Articles articles);

    Observable<Boolean> findBookmark(Articles articles);

    Observable<News> getCategoryNews(String category);
}
