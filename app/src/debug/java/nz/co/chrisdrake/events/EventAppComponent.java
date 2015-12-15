package nz.co.chrisdrake.events;

import android.content.Context;
import dagger.Component;
import javax.inject.Singleton;
import nz.co.chrisdrake.events.data.DebugDataModule;
import nz.co.chrisdrake.events.data.api.DebugApiModule;
import nz.co.chrisdrake.events.data.realm.DebugRealmModule;
import nz.co.chrisdrake.events.ui.explore.ExploreFragment;

@Singleton @Component(
    modules = {
        EventAppModule.class, DebugDataModule.class, DebugApiModule.class, DebugRealmModule.class
    }) //
public interface EventAppComponent {
    Context context();

    void inject(ExploreFragment fragment);
}
