package silantyevmn.ru.mynews.model.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import silantyevmn.ru.mynews.model.entity.News;

public interface ApiService {
    @GET("v2/top-headlines/")
    Observable<News> getTopNews(
            @Query("apiKey") String keyApi,
            @Query("pageSize") int pageSize,
            @Query("country") String country

    );

    @GET("v2/everything")
    Observable<News> getNewsSearch(
            @Query("apiKey") String keyApi,
            @Query("q") String text
    );

    @GET("v2/top-headlines/")
    Observable<News> getCategoryNews(
            @Query("apiKey") String keyApi,
            @Query("pageSize") int pageSize,
            @Query("country") String country,
            @Query("category") String category
    );
}
