package com.pabji.citycatched.domain.scopes;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by pabji on 16/06/2016.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
