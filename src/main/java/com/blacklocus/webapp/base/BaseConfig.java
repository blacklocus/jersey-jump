/*
 * Copyright 2013 BlackLocus
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
 *
 */

package com.blacklocus.webapp.base;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author jason
 */
public class BaseConfig {

    /** Sort of a registry of default values */
    public static final Configuration DEFAULTS = new MapConfiguration(new HashMap<String, Object>());
    /**
     * The primary property lookup source. First checks system properties. If not found falls back to registered
     * {@link #DEFAULTS}.
     */
    public static final Configuration $ = new CompositeConfiguration(Arrays.asList(
            new SystemConfiguration(), // first check sys props
            DEFAULTS // fall back to coded defaults
    ));

    /**
     * Base package that will be scanned for instantiable components ({@link Path}, {@link Provider}, ...)
     */
    public static final String PROP_BASE_PKG = "base.pkg";
    private static final String DEF_BASE_PKG = ""; // all of the things
    static {
        DEFAULTS.setProperty(PROP_BASE_PKG, DEF_BASE_PKG);
    }

    public static final String PROP_JETTY_HOST = "jetty.host";
    private static final String DEF_JETTY_HOST = "0.0.0.0";
    static {
        DEFAULTS.setProperty(PROP_JETTY_HOST, DEF_JETTY_HOST);
    }

    /**
     * Same as jetty property to use a custom port. Read by BasePackagesResourceConfig when starting embedded Jetty.
     * Defaults to 8080.
     */
    public static final String PROP_JETTY_PORT = "jetty.port";
    private static final int DEF_JETTY_PORT = 8080;
    static {
        DEFAULTS.setProperty(PROP_JETTY_PORT, DEF_JETTY_PORT);
    }

    public static final String PROP_JETTY_PORT_SSL = "jetty.port.ssl";
    private static final int DEF_JETTY_PORT_SSL = 8443;
    static {
        DEFAULTS.setProperty(PROP_JETTY_PORT_SSL, DEF_JETTY_PORT_SSL);
    }

    /**
     * A counter that when incremented (or changed at all) can trigger a jetty restart. Useful if the application was
     * reconfigured on the fly and should be totally reinitialized. Note that certain other props also trigger jetty
     * restarts. Defaults to a restart count of 0.
     */
    public static final String PROP_RESTART_TRIGGER = "jetty.restart";
    private static final int DEF_RESTART_TRIGGER = 0;
    static {
        DEFAULTS.setProperty(PROP_RESTART_TRIGGER, DEF_RESTART_TRIGGER);
    }

    /**
     * <h2>single-module web app project structure</h2>
     *
     * If all of your static files are in a single module project and in src/main/webapp/static/, then it may useful
     * to set this default JVM arg when starting the web app. This ensures that during development all static resources
     * are served directly from project source, so that any changes you make are instantly visible in the browser.
     *
     * <pre>
     * -Dbase.static_dirs=src/main/webapp/static/
     * </pre>
     *
     * <h2>multi-module web app project structure</h2>
     *
     * Example
     *
     * <pre>
     * project/
     *   ├── base-webapp/
     *   │     └── src/main/webapp/static/  -- common static files, e.g. jQuery, YUI, library files, base.css, ...
     *   ├── concrete-app1/  ----------------- has dependency on base-webapp/
     *   │     └── src/main/webapp/static/  -- additional static files
     *   └── concrete-app2/  ----------------- has dependency on base-webapp/
     *         └── src/main/webapp/static/  -- additional static files
     * </pre>
     *
     * <h3>fat jar build</h3>
     *
     * When the fat jar is built for any concrete app, this property is not necessary, as all these static files should
     * be merged into the built artifact, which should work as long as you have sourceSets correctly set up across your
     * build.gradle's.
     *
     * <h3>during development</h3>
     *
     * Separate multiple locations with ';'. When serving these resources, first matching path wins.
     *
     * <pre>
     * -Dbase.static_dirs=src/main/webapp/static/;../my-base-webapp/src/main/webapp/static/
     * </pre>
     *
     * <h2>fat jar build</h2>
     *
     * The Gradle fat jar build will actually merge static resources from base-webapp into the concrete webapp's
     * static files in the Fat Jar. So this property should not be necessary at runtime, though if for some reason
     * you want to serve files external to the fat jar, I don't see why you couldn't set it.
     *
     * <p/>
     *
     * All modules with static resources should be sure to have this in their build.gradle's, in order for the fat jar
     * roll up to mash them all into the same location in the jar.
     *
     * <pre>
     * sourceSets {
     *     main.resources.srcDir 'src/main/webapp'
     * }
     * </pre>
     *
     * If the static files are to be served directly from the fat jar, they MUST reside in a subdirectory of this
     * source dir, e.g. if sourceSets=src/main/webapp/ then -Dbase.static_dirs=src/main/webapp/static/ . Inside the fat
     * jar then they will be located in static/ .
     *
     * <hr/>
     *
     * Gradle note: This property was chosen over an alternate approach, this build.gradle closure:
     * <pre>
     * idea {
     *     module {
     *         sourceDirs += file('../base-webapp/src/main/webapp')
     *     }
     * }
     * </pre>
     * This approach unfortunately has some negative effects on IntelliJ functionality, particularly in the
     * Project Structure dialogs.
     */
    public static final String PROP_STATIC_DIRS = "base.static_dirs";

}
