package com.blacklocus.webapp.base;

import org.apache.commons.lang.CharEncoding;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
public class BaseWebApplicationException extends WebApplicationException {
    public BaseWebApplicationException(Response.Status status, Object entity) {
        super(Response
                .status(status)
                .type(MediaType.APPLICATION_JSON + ";charset=" + CharEncoding.UTF_8)
                .entity(entity)
                .build());
    }
    public BaseWebApplicationException(Response.Status status, String message) {
        super(Response
                .status(status)
                .type(MediaType.APPLICATION_JSON + ";charset=" + CharEncoding.UTF_8)
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