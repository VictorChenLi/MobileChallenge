package com.victorchen.mycurrency.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.URI;

public final class NetworkUtil {

    @SuppressWarnings("deprecation")
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo info : infos) {
                    if ((info.getState() == NetworkInfo.State.CONNECTING) || (info.getState() == NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getNormalizedBaseUrl(String urlString) {
        String url = "";
        if (urlString != null && (urlString = urlString.trim()).length() > 0) {
            if (urlString.charAt(urlString.length() - 1) != '/') {
                urlString += "/";
            }

            int domainStarts = urlString.indexOf("//") + 2;
            if (domainStarts < urlString.length()) {
                int domainEnds = urlString.indexOf("/", domainStarts);
                if (domainEnds < urlString.length()) {
                    return (urlString.substring(0, domainEnds).toLowerCase() + urlString.substring(domainEnds)).trim();
                }
            }

            url = urlString.toLowerCase().trim();
        }

        return url;
    }

    /**
     * Handle Capital letters in scheme and host name
     *
     * @return Normalized url. Input url when url syntax exception
     */
    public static String normalizeUrl(String url) {
        URI norm;
        if (url == null || url.length() == 0) return "";
        try {
            URI u = new URI(url.trim());

            norm = new URI(u.getScheme().toLowerCase(),
                    u.getUserInfo(),
                    u.getHost().toLowerCase(),
                    u.getPort(),
                    u.getPath(),
                    u.getQuery(),
                    null);
        } catch (Exception e) {
            return url;
        }

        return norm.toString();
    }

    /**
     * Check if app is debuggable without using BuildConfig
     *
     * @param context app context
     * @return true if app is debuggable
     */
    public static boolean isDebug(Context context) {
        return (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
    }
}
