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
package com.blacklocus.webapp.util;

import com.blacklocus.webapp.base.BaseWebApplicationException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Static methods to help validate request parameters. An json-serialization-friendly exception will be thrown
 * if a validation condition fails.
 *
 * @author Jason Dunkelberger (dirkraft)
 */
public class Valid {

    /**
     * @param condition which if failed will trigger a 400 exception
     * @param failureMessage detailing the fault
     * @throws WebApplicationException if the condition fails
     */
    public static void $400(boolean condition, String failureMessage) throws WebApplicationException {
        if (!condition) throw new BaseWebApplicationException(Response.Status.BAD_REQUEST, failureMessage);
    }
}

