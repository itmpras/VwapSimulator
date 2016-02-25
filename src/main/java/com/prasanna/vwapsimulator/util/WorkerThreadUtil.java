package com.prasanna.vwapsimulator.util;

import com.prasanna.vwapsimulator.task.UnHandledExceptionHandler;

/**
 * WorkerThreadUtil
 */
public class WorkerThreadUtil {
    public static void enrichWorkerThread(String threadName) {
        Thread.currentThread().setName(threadName);
        Thread.currentThread().setUncaughtExceptionHandler(new UnHandledExceptionHandler());
    }
}

