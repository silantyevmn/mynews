package silantyevmn.ru.mynews;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;
import silantyevmn.ru.mynews.di.AppComponent;
import silantyevmn.ru.mynews.di.DaggerAppComponent;

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
        Fabric.with(this, new Crashlytics());
        instance = this;
        Paper.init(this);
        FirebaseAnalytics.getInstance(this);
        Stetho.initializeWithDefaults(this);
    }
}
