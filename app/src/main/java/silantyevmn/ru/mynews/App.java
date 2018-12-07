package silantyevmn.ru.mynews;

import android.app.Application;

import com.facebook.stetho.Stetho;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;
import silantyevmn.ru.mynews.di.*;

public class App extends Application {
    
    private static App instance;
    private AppComponent component;

    public static App getInstance() {
        return instance;
    }

    public AppComponent getComponent() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                    .build();
        }
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Paper.init(this);
        Stetho.initializeWithDefaults(this);
    }
}
