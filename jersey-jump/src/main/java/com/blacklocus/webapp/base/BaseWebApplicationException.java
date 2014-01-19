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
package com.blacklocus.webapp.base;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Provides a little more control over the response when exceptions of this type that bubble out of web resources
 * ({@link Path @Path}-annotated).
 *
 * @author Jason Dunkelberger (dirkraft)
 */
public class BaseWebApplicationException extends WebApplicationException {
    public BaseWebApplicationException(Response.Status status, Object entity) {
        super(Response
                .status(status)
                .type(BaseJsonResource.JSON_CONTENT_TYPE)
                .entity(entity)
                .build());
    }
    public BaseWebApplicationException(Response.Status status, String message) {
        super(Response
                .status(status)
                .type(BaseJsonResource.JSON_CONTENT_TYPE)
                .entity(new MessageHolder(message))
                .build());
    }
}

class MessageHolder {
    public final String message;

    MessageHolder(String message) {
        this.message = message;
    }
}