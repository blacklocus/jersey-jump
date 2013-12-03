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

import com.blacklocus.webapp.RunServer;
import com.blacklocus.webapp.app.StartupListener;
import com.blacklocus.webapp.app.JerseyScannerHelper;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.core.PackagesResourceConfig;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;

import javax.ws.rs.ApplicationPath;

import static com.blacklocus.webapp.base.BaseConfig.$;
import static com.blacklocus.webapp.base.BaseConfig.PROP_BASE_PKG;

/**
 * See {@link PackagesResourceConfig}
 *
 * @author Jason Dunkelberger (dirkraft)
 */
@ApplicationPath("/*")
public class BasePackagesResourceConfig extends PackagesResourceConfig {

    public BasePackagesResourceConfig(@InjectParam JerseyScannerHelper scannerHelper) {
        this(scannerHelper, $.getString(PROP_BASE_PKG));
    }

    public BasePackagesResourceConfig(final JerseyScannerHelper scannerHelper, String... basePkgs) {
        super(combine(new String[] {
                "org.codehaus.jackson.jaxrs" // json serialization
        }, basePkgs));
        // Support for initialization after context building but before serving requests.
        RunServer.SERVER.getHandler().addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
            @Override
            public void lifeCycleStarted(LifeCycle event) {
                for (StartupListener startupListener : scannerHelper.findImplementing(StartupListener.class)) {
                    startupListener.onStartup();
                }
            }
        });
    }

    protected static String[] combine(String pkg, String... moar) {
        return combine(new String[]{pkg}, moar);
    }

    protected static String[] combine(String[] pkgs, String... moar) {
        if (pkgs == null || moar == null) {
            return pkgs != null ? pkgs : (moar != null ? moar : null);
        }
        String[] combined = new String[pkgs.length + moar.length];
        System.arraycopy(pkgs, 0, combined, 0, pkgs.length);
        System.arraycopy(moar, 0, combined, pkgs.length, moar.length);
        return combined;
    }
}

