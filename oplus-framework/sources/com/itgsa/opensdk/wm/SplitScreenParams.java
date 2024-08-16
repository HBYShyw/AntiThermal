package com.itgsa.opensdk.wm;

import android.content.Intent;

/* loaded from: classes.dex */
public final class SplitScreenParams {
    private Intent mLaunchIntent;
    private int mPosition;
    private boolean mSelfSplit;

    private SplitScreenParams() {
    }

    private SplitScreenParams(boolean selfSplit, Intent intent, int position) {
        this.mSelfSplit = selfSplit;
        this.mLaunchIntent = intent;
        this.mPosition = position;
    }

    public boolean isSelfSplit() {
        return this.mSelfSplit;
    }

    public Intent getLaunchIntent() {
        return this.mLaunchIntent;
    }

    public int getLaunchPosition() {
        return this.mPosition;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private Intent mLaunchIntent;
        private int mPosition;
        private boolean mSelfSplit;

        public Builder() {
        }

        public Builder(SplitScreenParams original) {
            this.mSelfSplit = original.mSelfSplit;
            this.mLaunchIntent = original.mLaunchIntent;
            this.mPosition = original.mPosition;
        }

        public Builder setSelfSplit() {
            this.mSelfSplit = true;
            return this;
        }

        public Builder setLaunchIntent(Intent intent) {
            this.mLaunchIntent = intent;
            return this;
        }

        public Builder setLaunchPosition(int position) {
            this.mPosition = position;
            return this;
        }

        public SplitScreenParams build() {
            return new SplitScreenParams(this.mSelfSplit, this.mLaunchIntent, this.mPosition);
        }
    }
}
