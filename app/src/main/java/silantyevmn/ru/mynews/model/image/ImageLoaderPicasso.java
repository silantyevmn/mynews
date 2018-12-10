package silantyevmn.ru.mynews.model.image;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import silantyevmn.ru.mynews.R;

public class ImageLoaderPicasso implements ImageLoader<ImageView> {

    @Override
    public void loadIconTitle(@Nullable String url, ImageView imageSmall) {
        String baseUrl = Uri.parse(url).getHost();
        String path = String.format("https://besticon-demo.herokuapp.com/icon?url=%s&size=32..64..64", baseUrl);
        show(path, imageSmall);
    }

    @Override
    public void loadIcon(@Nullable String url, String UrlToImage, ImageView image) {
        String baseUrl = Uri.parse(url).getHost();
        String path = "";
        if (TextUtils.isEmpty(UrlToImage)) {
            path = String.format("https://besticon-demo.herokuapp.com/icon?url=%s&size=64..100..120", baseUrl);
        } else {
            path = UrlToImage;
        }
        show(path, image);
    }

    private void show(String url, ImageView container) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_autorenew_black_24dp)
                .error(R.drawable.ic_crop_original_black_24dp)
                .centerCrop(0)
                .resize(88, 88)
                .into(container);
    }

}
