package nz.co.chrisdrake.events.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module public class ApiModule {

    @Provides @Singleton @Named("ApiClient") OkHttpClient provideHttpClient(OkHttpClient client,
        ApiAuthenticator authenticator) {
        return createApiClient(client, authenticator);
    }

    @Provides @Singleton Gson provideGson() {
        return createGson();
    }

    @Provides @Singleton Retrofit provideRetrofit(@Named("ApiClient") OkHttpClient client,
        Gson gson) {
        return createRetrofit(client, gson);
    }

    @Provides @Singleton EventFinderService provideApi(Retrofit retrofit) {
        return retrofit.create(EventFinderService.class);
    }

    static Gson createGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    static OkHttpClient createApiClient(OkHttpClient client, ApiAuthenticator authenticator) {
        client = client.clone();
        client.setAuthenticator(authenticator);
        return client;
    }

    static Retrofit createRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder().baseUrl("http://api.eventfinda.co.nz/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }
}
