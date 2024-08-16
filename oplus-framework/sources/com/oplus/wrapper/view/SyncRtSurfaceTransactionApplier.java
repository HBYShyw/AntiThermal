package com.oplus.wrapper.view;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.SyncRtSurfaceTransactionApplier;

/* loaded from: classes.dex */
public class SyncRtSurfaceTransactionApplier {
    private final android.view.SyncRtSurfaceTransactionApplier mSyncRtSurfaceTransactionApplier;

    public SyncRtSurfaceTransactionApplier(android.view.View targetView) {
        this.mSyncRtSurfaceTransactionApplier = new android.view.SyncRtSurfaceTransactionApplier(targetView);
    }

    public void scheduleApply(SurfaceParams... params) {
        if (params == null || params.length == 0) {
            return;
        }
        SyncRtSurfaceTransactionApplier.SurfaceParams[] surfaceParams = new SyncRtSurfaceTransactionApplier.SurfaceParams[params.length];
        for (int i = 0; i < params.length; i++) {
            surfaceParams[i] = params[i].getSurfaceParams();
        }
        this.mSyncRtSurfaceTransactionApplier.scheduleApply(surfaceParams);
    }

    /* loaded from: classes.dex */
    public static class SurfaceParams {
        private final SyncRtSurfaceTransactionApplier.SurfaceParams mSurfaceParams;

        private SurfaceParams(SyncRtSurfaceTransactionApplier.SurfaceParams surfaceParams) {
            this.mSurfaceParams = surfaceParams;
        }

        SyncRtSurfaceTransactionApplier.SurfaceParams getSurfaceParams() {
            return this.mSurfaceParams;
        }

        /* loaded from: classes.dex */
        public static class Builder {
            private final SyncRtSurfaceTransactionApplier.SurfaceParams.Builder mBuilder;

            public Builder(android.view.SurfaceControl surface) {
                this.mBuilder = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surface);
            }

            public Builder withAlpha(float alpha) {
                this.mBuilder.withAlpha(alpha);
                return this;
            }

            public Builder withMatrix(Matrix matrix) {
                this.mBuilder.withMatrix(matrix);
                return this;
            }

            public Builder withWindowCrop(Rect windowCrop) {
                this.mBuilder.withWindowCrop(windowCrop);
                return this;
            }

            public Builder withLayer(int layer) {
                this.mBuilder.withLayer(layer);
                return this;
            }

            public Builder withCornerRadius(float radius) {
                this.mBuilder.withCornerRadius(radius);
                return this;
            }

            public Builder withBackgroundBlur(int radius) {
                this.mBuilder.withBackgroundBlur(radius);
                return this;
            }

            public Builder withVisibility(boolean visible) {
                this.mBuilder.withVisibility(visible);
                return this;
            }

            public SurfaceParams build() {
                return new SurfaceParams(this.mBuilder.build());
            }
        }
    }
}
