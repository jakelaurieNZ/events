package nz.co.chrisdrake.events;

import android.app.Application;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class EventApp extends Application {
    protected EventAppComponent appComponent;

    @Override public void onCreate() {
        super.onCreate();
        createComponent();

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }
    }

    protected void createComponent() {
        this.appComponent = DaggerEventAppComponent.builder() //
            .eventAppModule(new EventAppModule(this)) //
            .build();
    }

    public EventAppComponent getApplicationComponent() {
        return appComponent;
    }
}
