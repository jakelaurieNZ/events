package nz.co.chrisdrake.events.data.realm;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import javax.inject.Singleton;
import nz.co.chrisdrake.events.BuildConfig;

@Module public class RealmModule {

    @Provides @Singleton RealmConfiguration provideRealmConfiguration(Context context) {
        return new RealmConfiguration.Builder(context).schemaVersion(
            BuildConfig.REALM_SCHEMA_VERSION).build();
    }

    @Provides @Singleton Realm provideRealm(RealmConfiguration configuration) {
        Realm.setDefaultConfiguration(configuration);
        return Realm.getDefaultInstance();
    }

    @Provides @Singleton RealmHelper provideRealmHelper(Realm realm) {
        return new RealmHelperImpl(realm);
    }
}
