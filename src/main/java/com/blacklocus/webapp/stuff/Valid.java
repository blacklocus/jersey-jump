package com.blacklocus.webapp.stuff;

import com.blacklocus.webapp.base.BaseWebApplicationException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * static methods to help validate request parameters
 *
 * @author Jason Dunkelberger (dirkraft)
 */
public class Valid {

    /**
     * @param condition which if failed will trigger a 404 exception
     * @param failureMessage detailing the fault
     * @throws WebApplicationException if the condition fails
     */
    public static void $404(boolean condition, String failureMessage) throws WebApplicationException {
        if (!condition) throw new BaseWebApplicationException(Response.Status.BAD_REQUEST, failureMessage);
    }
}

