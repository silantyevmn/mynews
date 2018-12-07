package silantyevmn.ru.mynews.di;

import javax.inject.Singleton;

import dagger.Component;
import silantyevmn.ru.mynews.di.modules.ImageLoaderModule;
import silantyevmn.ru.mynews.di.modules.NavigationModule;
import silantyevmn.ru.mynews.di.modules.PopupWindowModule;
import silantyevmn.ru.mynews.di.modules.RepoModule;
import silantyevmn.ru.mynews.ui.activity.StartActivity;
import silantyevmn.ru.mynews.ui.fragment.HomeFragment;
import silantyevmn.ru.mynews.ui.fragment.SearchFragment;
import silantyevmn.ru.mynews.ui.fragment.WebFragment;

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

    void inject(WebFragment webFragment);

}
