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
package com.blacklocus.webapp.app;

import com.blacklocus.webapp.base.BaseJsonResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.lang.Override;import java.lang.Throwable;import static javax.ws.rs.core.Response.ResponseBuilder;

/**
 * Ensures that all exceptions are converted to json. {@link WebApplicationException}s will be left alone except for
 * ensuring that they come out as json. All other exceptions will be serialized per {@link DefaultObjectMapper}.
 *
 * @author Jason Dunkelberger (dirkraft)
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        ResponseBuilder builder;
        if (exception instanceof WebApplicationException) {
            WebApplicationException webAppExcept = (WebApplicationException) exception;
            builder = Response.fromResponse(webAppExcept.getResponse());
        } else {
            LOG.warn("Internal server error (500)", exception);
            builder = Response.status(500).entity(exception);
        }
        return builder.type(BaseJsonResource.JSON_CONTENT_TYPE).build();
    }
}
