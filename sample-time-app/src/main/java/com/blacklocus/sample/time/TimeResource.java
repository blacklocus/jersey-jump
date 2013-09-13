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
