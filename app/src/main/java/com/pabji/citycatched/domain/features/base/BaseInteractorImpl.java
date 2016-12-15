package com.pabji.citycatched.domain.features.base;


import com.pabji.citycatched.domain.executor.PostExecutionThread;
import com.pabji.citycatched.domain.executor.ThreadExecutor;

import java.util.concurrent.Executor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by Pablo Jiménez Casado on 18/10/2016.
 */

public abstract class BaseInteractorImpl<T> implements BaseInteractor<T>{

    protected ThreadExecutor threadExecutor;
    protected PostExecutionThread postExecutionThread;

    private Subscription subscription = Subscriptions.empty();

    protected abstract Observable<T> buildFeatureObservable();

    public BaseInteractorImpl(ThreadExecutor threadExecutor,PostExecutionThread postExecutionThread){
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Subscriber<T> subscriber) {
        this.subscription = this.buildFeatureObservable()
            .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    @Override
    public void executeInMainThread(Subscriber<T> subscriber) {
        this.subscription = this.buildFeatureObservable()
                .subscribe(subscriber);
    }

    public Boolean haveSubscription(){
        return !subscription.isUnsubscribed();
    }

    public void unsubscribe() {
        if (haveSubscription()) {
            subscription.unsubscribe();
        }
    }
}
