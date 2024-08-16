package com.oplus.wrapper.window;

import android.graphics.Rect;
import android.view.SurfaceControl;
import android.window.PictureInPictureSurfaceTransaction;

/* loaded from: classes.dex */
public class PictureInPictureSurfaceTransaction {
    private android.window.PictureInPictureSurfaceTransaction mPictureInPictureSurfaceTransaction;

    public PictureInPictureSurfaceTransaction(android.window.PictureInPictureSurfaceTransaction pictureInPictureSurfaceTransaction) {
        this.mPictureInPictureSurfaceTransaction = pictureInPictureSurfaceTransaction;
    }

    public static void apply(PictureInPictureSurfaceTransaction surfaceTransaction, SurfaceControl surfaceControl, SurfaceControl.Transaction tx) {
        android.window.PictureInPictureSurfaceTransaction.apply(surfaceTransaction.mPictureInPictureSurfaceTransaction, surfaceControl, tx);
    }

    public android.window.PictureInPictureSurfaceTransaction getPictureInPictureSurfaceTransaction() {
        return this.mPictureInPictureSurfaceTransaction;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private PictureInPictureSurfaceTransaction.Builder mBuilder = new PictureInPictureSurfaceTransaction.Builder();

        public Builder setAlpha(float alpha) {
            this.mBuilder.setAlpha(alpha);
            return this;
        }

        public PictureInPictureSurfaceTransaction build() {
            return new PictureInPictureSurfaceTransaction(this.mBuilder.build());
        }

        public Builder setCornerRadius(float cornerRadius) {
            this.mBuilder.setCornerRadius(cornerRadius);
            return this;
        }

        public Builder setPosition(float x, float y) {
            this.mBuilder.setPosition(x, y);
            return this;
        }

        public Builder setTransform(float[] float9, float rotation) {
            this.mBuilder.setTransform(float9, rotation);
            return this;
        }

        public Builder setWindowCrop(Rect windowCrop) {
            this.mBuilder.setWindowCrop(windowCrop);
            return this;
        }
    }
}
