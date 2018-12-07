package silantyevmn.ru.mynews.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import silantyevmn.ru.mynews.model.cache.Cache;
import silantyevmn.ru.mynews.model.cache.PaperCache;
import silantyevmn.ru.mynews.model.image.ImageLoader;
import silantyevmn.ru.mynews.model.image.ImageLoaderPicasso;

@Module
public class ImageLoaderModule {
    @Singleton
    @Provides
    public ImageLoader imageLoader() {
        return new ImageLoaderPicasso();
    }
}
