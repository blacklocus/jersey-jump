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

import org.apache.commons.lang.CharEncoding;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * {@link Path @Path}-annotated resources that extend this class will be guaranteed to produce as <code>Content-Type:
 * {@value #JSON_CONTENT_TYPE}</code>
 *
 * @author Jason Dunkelberger (dirkraft)
 */
@Produces(BaseJsonResource.JSON_CONTENT_TYPE)
public abstract class BaseJsonResource {

    public static final String JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON + ";charset=" + CharEncoding.UTF_8;

}
