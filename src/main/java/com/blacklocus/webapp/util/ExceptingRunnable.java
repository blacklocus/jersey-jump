package com.blacklocus.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
public abstract class ExceptingRunnable implements Runnable {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        try {
            go();
        } catch (Exception e) {
            LOG.error("Exception in Runnable " + this.toString(), e);
        }
    }

    public abstract void go() throws Exception;
}
