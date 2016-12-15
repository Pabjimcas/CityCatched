package com.pabji.citycatched.presentation.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Pablo Jiménez Casado on 03/12/2016.
 */

public class ImageUtils {

    public interface BitmapListener {
        void onBitmapReady(Bitmap bitmap);
    }

    public static void loadCircledMarker(Context context,String url, final BitmapListener listener) {
        int diameter = dpToPx(context, 26);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .transform(new BorderedCircleTransformation(context))
                .override(diameter, diameter)
                .into(new SimpleTarget<Bitmap>() {
                    @Override public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        listener.onBitmapReady(resource);
                    }
                });
    }

    public static int dpToPx(Context mContext, int dipValue) {
        return (int) (dipValue * mContext.getResources().getDisplayMetrics().density);
    }
}
