package com.pabji.citycatched.presentation.mvp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Pablo Jiménez Casado on 22/10/2016.
 */

public class RecognizedObject implements Parcelable {
    public Descriptor descriptor;
    public String contentDescriptorDetected;

    public RecognizedObject(Descriptor descriptorRecognized, String contentDescriptorDetected) {
        this.descriptor = descriptorRecognized;
        this.contentDescriptorDetected = contentDescriptorDetected;
    }


    protected RecognizedObject(Parcel in) {
        descriptor = in.readParcelable(Descriptor.class.getClassLoader());
        contentDescriptorDetected = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(descriptor, flags);
        dest.writeString(contentDescriptorDetected);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecognizedObject> CREATOR = new Creator<RecognizedObject>() {
        @Override
        public RecognizedObject createFromParcel(Parcel in) {
            return new RecognizedObject(in);
        }

        @Override
        public RecognizedObject[] newArray(int size) {
            return new RecognizedObject[size];
        }
    };

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public String getContentDescriptorDetected() {
        return contentDescriptorDetected;
    }

    public void setContentDescriptorDetected(String contentDescriptorDetected) {
        this.contentDescriptorDetected = contentDescriptorDetected;
    }
}
