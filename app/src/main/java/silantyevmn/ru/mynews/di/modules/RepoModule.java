package silantyevmn.ru.mynews.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import silantyevmn.ru.mynews.model.api.ApiService;
import silantyevmn.ru.mynews.model.cache.Cache;
import silantyevmn.ru.mynews.model.repo.Repo;
import silantyevmn.ru.mynews.model.repo.RepoNews;

@Module(includes = {ApiModule.class, CacheModule.class})
public class RepoModule {
    @Provides
    public Repo generalRepository(ApiService api, Cache cache) {
        return new RepoNews(api, cache);
    }
}

