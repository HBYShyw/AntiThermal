package com.oplus.wrapper.view;

import android.view.ViewGroup;

/* loaded from: classes.dex */
public class ViewGroup {

    /* loaded from: classes.dex */
    public static class MarginLayoutParams {
        private final ViewGroup.MarginLayoutParams mMarginLayoutParams;

        public MarginLayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            this.mMarginLayoutParams = marginLayoutParams;
        }

        public void setMarginsRelative(int start, int top, int end, int bottom) {
            this.mMarginLayoutParams.setMarginsRelative(start, top, end, bottom);
        }
    }
}
