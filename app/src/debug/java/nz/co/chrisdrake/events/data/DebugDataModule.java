package nz.co.chrisdrake.events.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import javax.inject.Singleton;
import timber.log.Timber;

@Module public class DebugDataModule {
    private final boolean useMockMode;

    public DebugDataModule(boolean useMockMode) {
        this.useMockMode = useMockMode;
    }

    @Provides @Singleton @IsMockMode boolean provideMockMode() {
        return useMockMode;
    }

    @Provides @Singleton SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton Picasso providePicasso(Context context, OkHttpClient client) {
        return new Picasso.Builder(context).downloader(new OkHttpDownloader(client)).build();
    }

    @Provides @Singleton OkHttpClient provideHttpClient(Context context) {
        OkHttpClient client = DataModule.createOkHttpClient(context);
        client.interceptors().add(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Timber.i("Request: %s", request.url());
                return chain.proceed(request);
            }
        });
        return client;
    }
}