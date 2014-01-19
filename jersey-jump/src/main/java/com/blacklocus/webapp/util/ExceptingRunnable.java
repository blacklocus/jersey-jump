/**
 * Copyright 2014 BlackLocus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
