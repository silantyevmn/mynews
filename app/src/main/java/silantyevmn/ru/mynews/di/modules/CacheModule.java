package silantyevmn.ru.mynews.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import silantyevmn.ru.mynews.model.cache.Cache;
import silantyevmn.ru.mynews.model.cache.PaperCache;

@Module
public class CacheModule {
    @Provides
    public Cache cache() {
        return new PaperCache();
    }
}
