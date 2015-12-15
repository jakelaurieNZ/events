package nz.co.chrisdrake.events;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class EventAppModule {
    private final EventApp app;

    public EventAppModule(EventApp app) {
        this.app = app;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return app;
    }
}
