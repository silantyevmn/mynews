package silantyevmn.ru.mynews.model.image;

import android.support.annotation.Nullable;
import android.widget.ImageView;


public interface ImageLoader<T> {
    void loadIcon(@Nullable String url,String urlImage, T container);

    void loadIconTitle(@Nullable String urlToImage, T container);
}