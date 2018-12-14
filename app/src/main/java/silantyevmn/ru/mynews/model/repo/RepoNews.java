package silantyevmn.ru.mynews.model.repo;

import java.util.List;

import io.reactivex.Observable;
import silantyevmn.ru.mynews.BuildConfig;
import silantyevmn.ru.mynews.model.api.ApiService;
import silantyevmn.ru.mynews.model.cache.Cache;
import silantyevmn.ru.mynews.model.entity.Articles;
import silantyevmn.ru.mynews.model.entity.News;
import silantyevmn.ru.mynews.utils.LocaleManager;

public class RepoNews implements Repo {
    private final String KEY_API = BuildConfig.ApiKey;
    private final String localeToCountry;
    private final int PAGE_SIZE = 100;
    private ApiService api;
    private Cache cache;

    public RepoNews(ApiService api, Cache cache) {
        this.api = api;
        this.cache = cache;
        localeToCountry = LocaleManager.getCountry();
    }

    @Override
    public Observable<News> getTopNews() {
        return api.getTopNews(KEY_API, PAGE_SIZE, localeToCountry);
    }

    @Override
    public Observable<News> getSearchNews(String newText) {
        return api.getNewsSearch(KEY_API, newText);
    }

    @Override
    public Observable<List<Articles>> getBookmarksList() {
        return cache.getBookmarksList();
    }

    @Override
    public Observable<Boolean> updateBookmark(Articles articles) {
        return cache.updateBookmark(articles);
    }

    @Override
    public Observable<Boolean> findBookmark(Articles articles) {
        return cache.findBookmark(articles);
    }

    @Override
    public Observable<News> getCategoryNews(String category) {
        return api.getCategoryNews(KEY_API, PAGE_SIZE, localeToCountry, category);
    }
}
