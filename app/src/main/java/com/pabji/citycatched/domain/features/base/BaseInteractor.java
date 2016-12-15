package com.pabji.citycatched.domain.features.base;

import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;

import rx.Subscriber;

/**
 * Created by Pablo Jiménez Casado on 18/10/2016.
 */

public interface BaseInteractor<T> {
    void execute(Subscriber<T> subscriber);

    void executeInMainThread(Subscriber<T> subscriber);
}
