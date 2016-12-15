package com.pabji.citycatched.presentation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class BorderedCircleTransformation implements Transformation<Bitmap> {

    //Default values.
    private int resolvedColor = Color.WHITE;
    private int strokeWidth = 4;

    private BitmapPool mBitmapPool;

    public BorderedCircleTransformation(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public BorderedCircleTransformation(Context context, @ColorInt int resolvedColor, int strokeWidth) {
        this(Glide.get(context).getBitmapPool());
        this.resolvedColor = resolvedColor;
        this.strokeWidth = strokeWidth;
    }

    public BorderedCircleTransformation(BitmapPool pool) {
        this.mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;

        Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        //draw border.
        BitmapResource obtain = BitmapResource.obtain(bitmap, mBitmapPool);
        Bitmap borderedBitmap = addFrameBorder(obtain.get());

        return BitmapResource.obtain(borderedBitmap, mBitmapPool);
    }

    private Bitmap addFrameBorder(Bitmap bitmap) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + (strokeWidth * 2), h + (strokeWidth * 2), Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + strokeWidth, (h / 2) + strokeWidth, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, strokeWidth, strokeWidth, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(resolvedColor);
        p.setStrokeWidth(strokeWidth);
        c.drawCircle((w / 2) + strokeWidth, (h / 2) + strokeWidth, radius, p);

        return output;
    }

    @Override
    public String getId() {
        return "CropCircleTransformation()";
    }
}