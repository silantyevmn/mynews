package silantyevmn.ru.mynews.model.repo;

import io.reactivex.Observable;
import silantyevmn.ru.mynews.model.api.ApiService;
import silantyevmn.ru.mynews.model.cache.Cache;
import silantyevmn.ru.mynews.model.entity.News;
import silantyevmn.ru.mynews.utils.LocaleManager;
import silantyevmn.ru.mynews.utils.Messages;
import silantyevmn.ru.mynews.utils.NetworkStatus;

public class RepoNews implements Repo {
    private final String KEY_API = "e355d575a04c43fb9076a3ee6be5253d";
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
}
