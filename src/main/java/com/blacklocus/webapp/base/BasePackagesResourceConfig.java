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

import com.sun.jersey.api.core.PackagesResourceConfig;

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

    public BasePackagesResourceConfig() {
        this($.getString(PROP_BASE_PKG));
    }

    public BasePackagesResourceConfig(String... basePkgs) {
        super(combine(new String[]{
                "org.codehaus.jackson.jaxrs" // json serialization
        }, basePkgs));
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

