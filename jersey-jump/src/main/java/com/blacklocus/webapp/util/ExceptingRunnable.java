package com.blacklocus.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Is sure to log any exception as an error if the extending {@link Runnable} crashes, since plain Runnables that
 * crash in an {@link ExecutorService} by jvm-default produce no output (like the main thread does).
 *
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
