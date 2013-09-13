package com.blacklocus.sample.time;

import com.blacklocus.webapp.base.BaseJsonResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
@Path("wait")
public class WaitResource extends BaseJsonResource {

    @GET
    @Path("{ms}")
    public String waitFor(@PathParam("ms") int ms) throws InterruptedException {
        Thread.sleep(ms);
        return "Slept for " + ms + "ms.";
    }
}
