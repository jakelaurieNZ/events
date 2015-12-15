package nz.co.chrisdrake.events.data.api;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import nz.co.chrisdrake.events.data.IsMockMode;
import retrofit.Retrofit;

@Module public class DebugApiModule {

    @Provides @Singleton @Named("ApiClient") OkHttpClient provideHttpClient(OkHttpClient client,
        ApiAuthenticator authenticator) {
        return ApiModule.createApiClient(client, authenticator);
    }

    @Provides @Singleton Gson provideGson() {
        return ApiModule.createGson();
    }

    @Provides @Singleton Retrofit provideRetrofit(@Named("ApiClient") OkHttpClient client,
        Gson gson) {
        return ApiModule.createRetrofit(client, gson);
    }

    @Provides @Singleton EventFinderService provideApi(Retrofit retrofit,
        @IsMockMode boolean isMockMode) {
        return isMockMode ? new MockEventFinderService()
            : retrofit.create(EventFinderService.class);
    }
}
