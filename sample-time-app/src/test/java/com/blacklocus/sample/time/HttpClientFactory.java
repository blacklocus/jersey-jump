/**
 * Copyright 2014 BlackLocus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
