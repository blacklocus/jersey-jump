package com.blacklocus.webapp.app;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.ws.rs.ext.Provider;

/**
 * @author jason
 */
@Provider
public class DefaultObjectMapper extends ObjectMapper {
    public DefaultObjectMapper() {
        configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setVisibility(JsonMethod.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
    }
}
