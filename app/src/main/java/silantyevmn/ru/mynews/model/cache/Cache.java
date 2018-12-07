package silantyevmn.ru.mynews.model.cache;

import java.util.List;

import io.reactivex.Observable;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.entity.News;

public interface Cache{
    void putAll(News list);

    Observable<News> getNews();
}
