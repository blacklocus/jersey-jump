package com.blacklocus.webapp.app;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.Provider;

@Provider
public class DefaultMessageBodyWriter extends JacksonJsonProvider {
}
