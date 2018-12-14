package silantyevmn.ru.mynews.di;

import javax.inject.Singleton;

import dagger.Component;
import silantyevmn.ru.mynews.di.modules.ImageLoaderModule;
import silantyevmn.ru.mynews.di.modules.NavigationModule;
import silantyevmn.ru.mynews.di.modules.PopupWindowModule;
import silantyevmn.ru.mynews.di.modules.RepoModule;
import silantyevmn.ru.mynews.utils.PopupClass;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.activity.WebActivity;
import silantyevmn.ru.mynews.ui.fragment.BookmarksFragment;
import silantyevmn.ru.mynews.ui.fragment.CategoryFragment;
import silantyevmn.ru.mynews.ui.fragment.HomeFragment;
import silantyevmn.ru.mynews.ui.fragment.SearchFragment;

@Singleton
@Component(modules = {
        NavigationModule.class,
        RepoModule.class,
        ImageLoaderModule.class,
        PopupWindowModule.class
})
public interface AppComponent {
    void inject(StartActivity startActivity);

    void inject(HomeFragment mainFragment);

    void inject(SearchFragment searchFragment);

    void inject(WebActivity webActivity);

    void inject(BookmarksFragment bookmarksFragment);

    void inject(CategoryFragment categoryFragment);

    void inject(PopupClass popupClass);

}
