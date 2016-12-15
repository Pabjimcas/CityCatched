package com.pabji.citycatched.data.exceptions;

/**
 * Created by Pablo Jiménez Casado on 22/10/2016.
 */

public class CustomOpenCvException extends Exception {
    private final int error;

    public CustomOpenCvException(int error){
        this.error = error;
    }
}
