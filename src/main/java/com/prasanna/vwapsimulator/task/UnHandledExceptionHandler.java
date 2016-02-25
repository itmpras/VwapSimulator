package com.prasanna.vwapsimulator.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by prasniths on 25/02/16.
 */
public class UnHandledExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnHandledExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Thread {} has failed will exception {}", t.getName(), e);
    }
}
