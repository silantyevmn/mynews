package silantyevmn.ru.mynews.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import silantyevmn.ru.mynews.ui.popup.PopupDialogMessage;
import silantyevmn.ru.mynews.ui.popup.PopupDialogToasty;

@Module
public class PopupWindowModule {
    @Singleton
    @Provides
    public PopupDialogMessage cache() {
        return new PopupDialogToasty();
    }
}
