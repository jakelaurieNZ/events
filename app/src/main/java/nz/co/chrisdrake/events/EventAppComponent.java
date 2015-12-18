package nz.co.chrisdrake.events;

import android.content.Context;
import dagger.Component;
import javax.inject.Singleton;
import nz.co.chrisdrake.events.data.DataModule;
import nz.co.chrisdrake.events.data.api.ApiModule;
import nz.co.chrisdrake.events.data.realm.RealmModule;
import nz.co.chrisdrake.events.ui.explore.ExploreFragment;

@Singleton @Component(
    modules = { EventAppModule.class, DataModule.class, ApiModule.class, RealmModule.class })
public interface EventAppComponent {
    Context context();

    void inject(ExploreFragment fragment);
}
