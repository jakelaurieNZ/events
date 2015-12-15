package nz.co.chrisdrake.events.data.api;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.Proxy;
import javax.inject.Inject;
import nz.co.chrisdrake.events.BuildConfig;

public class ApiAuthenticator implements Authenticator {

    @Inject ApiAuthenticator() {

    }

    @Override public Request authenticate(Proxy proxy, Response response) throws IOException {
        String credential = Credentials.basic(BuildConfig.EVENTFINDER_API_USERNAME,
            BuildConfig.EVENTFINDER_API_PASSWORD);
        return response.request().newBuilder().header("Authorization", credential).build();
    }

    @Override public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}
