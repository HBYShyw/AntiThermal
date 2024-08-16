package com.oplus.wrapper.media;

import android.media.Image;

/* loaded from: classes.dex */
public class ImageReader {
    private final android.media.ImageReader mImageReader;

    public ImageReader(android.media.ImageReader imageReader) {
        this.mImageReader = imageReader;
    }

    public void detachImage(Image image) {
        this.mImageReader.detachImage(image);
    }
}
