package com.blacklocus.webapp.base;

import org.apache.commons.lang.CharEncoding;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author jason
 */
@Produces(MediaType.APPLICATION_JSON + ";charset=" + CharEncoding.UTF_8)
public abstract class BaseJsonResource {
}
