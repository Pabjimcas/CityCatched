package com.pabji.citycatched.domain.executor;

import javax.inject.Inject;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Pablo Jiménez Casado on 19/10/2016.
 */

public class UIThread implements PostExecutionThread{
    @Inject
    public UIThread() {}

    @Override public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
