package com.pabji.citycatched.domain.executor;

import rx.Scheduler;

/**
 * Created by carlos on 3/12/15.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
