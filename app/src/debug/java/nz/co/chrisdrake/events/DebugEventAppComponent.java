package nz.co.chrisdrake.events;

import dagger.Component;
import javax.inject.Singleton;
import nz.co.chrisdrake.events.data.DebugDataModule;
import nz.co.chrisdrake.events.data.api.DebugApiModule;
import nz.co.chrisdrake.events.data.realm.DebugRealmModule;

@Singleton @Component(
    modules = {
        EventAppModule.class, DebugDataModule.class, DebugApiModule.class, DebugRealmModule.class
    }) //
public interface DebugEventAppComponent extends EventAppComponent {
}
