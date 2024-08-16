package com.oplus.wrapper.graphics;

import android.graphics.BitmapFactory;

/* loaded from: classes.dex */
public class BitmapFactory {
    private BitmapFactory() {
    }

    /* loaded from: classes.dex */
    public static class Options {
        private BitmapFactory.Options mOptions;

        public Options(BitmapFactory.Options options) {
            this.mOptions = options;
        }

        public boolean getInPostProc() {
            return this.mOptions.inPostProc;
        }

        public void setInPostProc(boolean value) {
            this.mOptions.inPostProc = value;
        }
    }
}
