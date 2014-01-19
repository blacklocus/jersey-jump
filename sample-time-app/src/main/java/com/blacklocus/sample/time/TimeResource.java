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

import com.blacklocus.webapp.base.BaseJsonResource;
import com.google.common.collect.ImmutableMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Map;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
@Path("time")
public class TimeResource extends BaseJsonResource {

    @GET
    public Map<String, Object> getTime() {
        return ImmutableMap.<String, Object>of("current", System.currentTimeMillis());
    }
}
