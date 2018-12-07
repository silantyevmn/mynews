package silantyevmn.ru.mynews.model.repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.entity.News;

public interface Repo {
    Observable<News> getTopNews();

    Observable<News> getSearchNews(String newText);

}
