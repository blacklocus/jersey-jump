package com.blacklocus.webapp.app;

/**
* @author jason
*/
public interface StartupListener {
    /**
     * Initialization that should occur after singleton instantiation.
     */
    void onStartup();
}
