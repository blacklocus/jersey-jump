package com.blacklocus.sample.time;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.concurrent.TimeUnit;

public class HttpClientFactory {

    public static final int POOL_MAX_PER_ROUTE = 10000;
    public static final int POOL_MAX_TOTAL = 10000;

    public static final PoolingClientConnectionManager POOL_MGR = new PoolingClientConnectionManager(
            SchemeRegistryFactory.createDefault(), 5, TimeUnit.MINUTES
    ) {{
        setDefaultMaxPerRoute(POOL_MAX_PER_ROUTE);
        setMaxTotal(POOL_MAX_TOTAL);
    }};

    /**
     * Construct a new HttpClient which uses the {@link #POOL_MGR default connection pool}.
     *
     * @param connectionTimeout highly sensitive to application so must be specified
     * @param socketTimeout highly sensitive to application so must be specified
     */
    public static HttpClient createHttpClient(final int connectionTimeout, final int socketTimeout) {
        return new DefaultHttpClient(POOL_MGR) {{
            HttpParams httpParams = getParams();

            // prevent seg fault JVM bug, see https://code.google.com/p/crawler4j/issues/detail?id=136
            httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

            HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout);
            HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
            HttpConnectionParams.setStaleCheckingEnabled(httpParams, true);

        }};
    }

}
