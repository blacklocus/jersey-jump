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
package com.blacklocus.webapp;

import com.blacklocus.webapp.app.StaticResourceUTF8CharEncodingFilterHolder;
import com.blacklocus.webapp.base.BaseConfig;
import com.blacklocus.webapp.base.BasePackagesResourceConfig;
import com.blacklocus.webapp.util.ExceptingRunnable;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.server.impl.resource.SingletonFactory;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import static com.blacklocus.webapp.base.BaseConfig.$;
import static com.blacklocus.webapp.base.BaseConfig.PROP_JETTY_HOST;
import static com.blacklocus.webapp.base.BaseConfig.PROP_JETTY_PORT;
import static com.blacklocus.webapp.base.BaseConfig.PROP_JETTY_PORT_SSL;
import static com.blacklocus.webapp.base.BaseConfig.PROP_KEYSTORE_PW;
import static com.blacklocus.webapp.base.BaseConfig.PROP_STATIC_CONTENT_REGEX;
import static com.blacklocus.webapp.base.BaseConfig.PROP_STATIC_DIRS;

/**
 * Sets up Jetty and Jersey with some sane defaults. This is the primary {@link #main(String[])} entry point that
 * typically starts up a jersey-jump -based application.
 *
 * @author Jason Dunkelberger (dirkraft)
 */
public class RunServer extends ExceptingRunnable {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public static Server SERVER;

    private final Configuration propSet = BaseConfig.$;

    private final Class<? extends BasePackagesResourceConfig> prcCls;

    public RunServer() {
        this(BasePackagesResourceConfig.class);
    }

    /**
     * Initializes a new Jetty instance running the specified Jersey application class.
     */
    public RunServer(Class<? extends BasePackagesResourceConfig> prcCls) {
        this.prcCls = prcCls;
    }

    @Override
    public void go() throws Exception {

        WebAppContext webApp = new WebAppContext();
        // Effectively removes the "no-JSP support" warning, because we don't want dirty, stinkin' JSP support ever.
        webApp.setDefaultsDescriptor("webdefault-nojsp.xml");

        // Fixes serving static resources correctly. Without this, no charset is set in the Content-Type header and
        // 'good' browsers do a terrible job at guessing. Does not affect Java resources, which must specify
        // their own @Produces (usually by extending BaseJsonResource).
        webApp.addFilter(new StaticResourceUTF8CharEncodingFilterHolder(), "/*", EnumSet.allOf(DispatcherType.class));


        ////////////////////
        // Prepare jersey

        // as a filter-specifically because that can support falling back to static content when no java resource matches
        FilterHolder jerseyFilter = new FilterHolder(ServletContainer.class);
        jerseyFilter.setName(prcCls.getName());
        jerseyFilter.setInitParameter(ServletContainer.APPLICATION_CONFIG_CLASS, prcCls.getCanonicalName());
        jerseyFilter.setInitParameter(ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX, $.getString(PROP_STATIC_CONTENT_REGEX));
        jerseyFilter.setInitParameter(ResourceConfig.PROPERTY_DEFAULT_RESOURCE_COMPONENT_PROVIDER_FACTORY_CLASS, SingletonFactory.class.getCanonicalName());
        jerseyFilter.setInitParameter(ResourceConfig.FEATURE_DISABLE_WADL, "true");
        jerseyFilter.setInitParameter(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

        Map<String, String> addtionalInitParams = getAdditionalJerseyInitParameters();
        for (Map.Entry<String, String> initParam : addtionalInitParams.entrySet()) {
            // must iterate because jerseyFilter.setInitParameters replaces everything
            jerseyFilter.setInitParameter(initParam.getKey(), initParam.getValue());
        }

        webApp.addFilter(jerseyFilter, "/*", EnumSet.allOf(DispatcherType.class));


        ////////////////////////////////////////
        // prepare static resources locations

        final Resource staticResources;
        String explicitStaticDirs = propSet.getString(PROP_STATIC_DIRS);
        if (explicitStaticDirs == null) {
            // Single static resource directory in the classpath:static/ folder.
            //noinspection ConstantConditions
            URL staticDir = prcCls.getClassLoader().getResource("static");
            staticResources = staticDir == null ? Resource.newResource("") : Resource.newResource(staticDir.toExternalForm());
        } else {
            // Explicitly named static resource directories. There may be multiple. First match wins.
            staticResources = new ResourceCollection(explicitStaticDirs.split(";"));
        }

        webApp.setBaseResource(staticResources);


        ///////////////////////////////////
        // initialize network listeners

        SERVER = new Server(new InetSocketAddress(
                propSet.getString(PROP_JETTY_HOST),
                propSet.getInt(PROP_JETTY_PORT)
        ));
        SslSelectChannelConnector ssl = createSsl();
        if (ssl != null) {
            SERVER.addConnector(ssl);
        }
        SERVER.setHandler(webApp);
        SERVER.start();
        SERVER.join(); // blocks this thread

    }

    private SslSelectChannelConnector createSsl() throws IOException, KeyStoreException, CertificateException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnrecoverableKeyException, URISyntaxException {
        SslSelectChannelConnector sslChannel = null;

        URL keyStoreURL = RunServer.class.getClassLoader().getResource("keystore/keystore.jks");
        if (keyStoreURL == null) {
            LOG.warn("Could not find keystore");

        } else {
            File tempDir = FileUtils.getTempDirectory();
            File keyStoreFile = new File(tempDir, "keystore.jks");
            LOG.info("Copying keystore to filesystem: " + keyStoreFile.getAbsolutePath());
            FileUtils.copyInputStreamToFile(keyStoreURL.openStream(), keyStoreFile);

            SslContextFactory sslContextFactory = new SslContextFactory(keyStoreFile.getAbsolutePath());
            sslContextFactory.setKeyStorePassword($.getString(PROP_KEYSTORE_PW));

            // plug it into the SSL channel
            sslChannel = new SslSelectChannelConnector(sslContextFactory);
            sslChannel.setHost(propSet.getString(PROP_JETTY_HOST));
            sslChannel.setPort(propSet.getInt(PROP_JETTY_PORT_SSL));
        }

        return sslChannel;
    }

    /**
     * Override to provide additional jersey initialization parameters
     */
    protected Map<String, String> getAdditionalJerseyInitParameters() {
        return Collections.emptyMap();
    }

    public static void main(String[] args) throws Exception {
        new RunServer().run();
    }
}
