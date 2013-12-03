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

package com.blacklocus.webapp.app;

import com.sun.jersey.api.core.InjectParam;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Registers {@link DefaultObjectMapper} as the default {@link ContextResolver} which supplies the
 * {@link DefaultObjectMapper}. TODO what does this really do? Thanks enterprise java.
 *
 * @author Jason Dunkelberger (dirkraft)
 */
@Provider
public class DefaultContextResolver implements ContextResolver<ObjectMapper> {

    final ObjectMapper om;

    public DefaultContextResolver(@InjectParam DefaultObjectMapper objectMapper) {
        om = objectMapper;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return om;
    }
}