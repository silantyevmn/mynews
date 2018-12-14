package silantyevmn.ru.mynews.ui.image;

import android.support.annotation.Nullable;


public interface ImageLoader<T> {
    void loadIcon(@Nullable String url,String urlImage, T container);

    void loadIconTitle(@Nullable String urlToImage, T container);
}