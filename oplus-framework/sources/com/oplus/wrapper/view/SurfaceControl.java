package com.oplus.wrapper.view;

import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class SurfaceControl {
    private final android.view.SurfaceControl mSurfaceControl;

    public SurfaceControl(android.view.SurfaceControl surfaceControl) {
        this.mSurfaceControl = surfaceControl;
    }

    public int getWidth() {
        return this.mSurfaceControl.getWidth();
    }

    public int getHeight() {
        return this.mSurfaceControl.getHeight();
    }

    /* loaded from: classes.dex */
    public static class Transaction {
        private final SurfaceControl.Transaction mTransaction;

        public Transaction(SurfaceControl.Transaction transaction) {
            this.mTransaction = transaction;
        }

        @Deprecated
        public SurfaceControl.Transaction setBackgroundBlurRadius(android.view.SurfaceControl sc, int radius) {
            return this.mTransaction.setBackgroundBlurRadius(sc, radius);
        }

        public Transaction setBackgroundBlurRadiusNew(android.view.SurfaceControl sc, int radius) {
            this.mTransaction.setBackgroundBlurRadius(sc, radius);
            return this;
        }

        public Transaction hide(android.view.SurfaceControl sc) {
            this.mTransaction.hide(sc);
            return this;
        }

        public Transaction remove(android.view.SurfaceControl sc) {
            this.mTransaction.remove(sc);
            return this;
        }

        public Transaction show(android.view.SurfaceControl sc) {
            this.mTransaction.show(sc);
            return this;
        }

        public SurfaceControl.Transaction getTransaction() {
            return this.mTransaction;
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final SurfaceControl.Builder mBuilder;

        public Builder(SurfaceSession surfaceSession) {
            this.mBuilder = new SurfaceControl.Builder(surfaceSession.getSurfaceSession());
        }

        public Builder setCallsite(String callsite) {
            this.mBuilder.setCallsite(callsite);
            return this;
        }

        public Builder setContainerLayer() {
            this.mBuilder.setContainerLayer();
            return this;
        }

        public SurfaceControl.Builder getBuilder() {
            return this.mBuilder;
        }
    }
}
