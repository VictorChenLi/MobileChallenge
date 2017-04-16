package com.victorchen.mycurrency.network.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.victorchen.mycurrency.network.NetworkUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Abstract api factory that holds a map of Retrofit API instances.
 * <p>
 * Concrete class is strongly recommended to use Singleton pattern
 * <p>
 * To instantiate this factory, override {@code getSessionToken} method to return current session
 * token for API authentication.
 * <p>
 * OPTIONAL: to add additional Retrofit converters/adapters/interceptors, override
 * {@code getRetrofitConverters},
 * {@code getRetrofitCallAdapters}
 * {@code getRetrofitInterceptors}
 * For example, you can set a custom GsonConverter to customize response parsing
 * <p>
 * To use this class, get an instance of the factory and call
 * {@code getApiInstance} or
 * {@code getCentralLoginApiInstance}
 */
public abstract class BaseApiFactory {

    // in this case we only have one end point base url
    // if we have multi end point, we'll create instance with
    // different base url at execution time by using interceptors
    private static final String PLACEHOLDER_BASE_URL = "http://baseURL/";
    private static Map<Class, Object> apiMap = new HashMap<>();
    private static Retrofit retrofit;

    public static Converter<ResponseBody, ?> getResponseConverter(Type type) {
        return retrofit.responseBodyConverter(type, new Annotation[0]);
    }

    /**
     * Override this method to return a list of custom converts for Retrofit
     *
     * @return a list of converters to be used with Retrofit
     */
    public
    @NonNull
    List<Converter.Factory> getRetrofitConverters() {
        return new ArrayList<>();
    }

    /**
     * Override this method to return a list of call adapters to handle response
     * other than {@link retrofit2.Call}
     *
     * @return a list of call adapters to be used with Retrofit
     */
    public
    @NonNull
    List<CallAdapter.Factory> getRetrofitCallAdapters() {
        return new ArrayList<>();
    }

    /**
     * Override this method to add additional okhttp interceptors to Retrofit
     *
     * @return a list of {@link Interceptor} to be added to Retrofit
     */
    public
    @NonNull
    List<Interceptor> getRetrofitInterceptors() {
        return new ArrayList<>();
    }

    /**
     * Override this method to customize the OkHttpClient, such as read/write/connect timeout
     */
    public
    @NonNull
    OkHttpClient.Builder getOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    private synchronized Retrofit getRetrofitInstance(final Context appContext, final boolean isCentralLogin) {
        if (retrofit != null) {
            return retrofit;
        }

        final OkHttpClient.Builder builder = getOkHttpBuilder();

        // setup custom host name verifier for https connection
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("pointclickcare.com", session);
            }
        });

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc;
        try {
            sc = SSLContext.getInstance("SSL");
            if (sc != null) {
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                builder.sslSocketFactory(sc.getSocketFactory());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        // add log in debug mode
        if (NetworkUtil.isDebug(appContext)) {
            //add logging interceptor
            HttpLoggingInterceptor loginInterceptor = new HttpLoggingInterceptor();
            loginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.interceptors().add(loginInterceptor);
        }

        //Add additional interceptors
        for (Interceptor interceptor : getRetrofitInterceptors()) {
            builder.interceptors().add(interceptor);
        }

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(builder.build());

        //Add additional converters
        for (Converter.Factory factory : getRetrofitConverters()) {
            retroBuilder.addConverterFactory(factory);
        }

        //Add additional call adapters
        for (CallAdapter.Factory factory : getRetrofitCallAdapters()) {
            retroBuilder.addCallAdapterFactory(factory);
        }

        Retrofit retro = retroBuilder.build();
        retrofit = retro;
        return retro;
    }

    /**
     * Retrieve API instance for all APIs other than central login
     *
     * @param context app context
     * @param clazz   API interface class
     * @return Retrofit API instance
     */
    public <T> T getApiInstance(Context context, Class<T> clazz) {
        return getApiInstance(context, clazz, false);
    }

    @SuppressWarnings("unchecked")
    private <T> T getApiInstance(Context context, Class<T> clazz, boolean isCentralLogin) {
        if (!apiMap.containsKey(clazz)) {
            synchronized (clazz) {
                if (!apiMap.containsKey(clazz)) {
                    apiMap.put(clazz, getRetrofitInstance(context, isCentralLogin).create(clazz));
                }
            }
        }

        return (T) apiMap.get(clazz);
    }

    public String getBaseUrl() {
        return PLACEHOLDER_BASE_URL;
    }
}
