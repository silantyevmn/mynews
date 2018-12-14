package silantyevmn.ru.mynews.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import silantyevmn.ru.mynews.ui.image.ImageLoader;
import silantyevmn.ru.mynews.ui.image.ImageLoaderPicasso;

@Module
public class ImageLoaderModule {
    @Singleton
    @Provides
    public ImageLoader imageLoader() {
        return new ImageLoaderPicasso();
    }
}
