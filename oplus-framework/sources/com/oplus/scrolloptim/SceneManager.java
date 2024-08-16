package com.oplus.scrolloptim;

import android.view.MotionEvent;

/* loaded from: classes.dex */
public class SceneManager {
    public static final int SCROLL_STATE_FLING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    private static final String TAG = SceneManager.class.getSimpleName();
    private OptimConfig mOptimConfig;
    private int mScrollState = 0;
    private int mScrollChangedState = 0;
    private String mCurrentActivityName = null;
    private boolean mHasInput = false;
    private boolean mIsFling = false;
    private boolean mIsScrollOptDebugEnable = OplusDebugUtil.DEBUG_SWITCH;
    private ConfigManager mConfigManager = new ConfigManager();

    public SceneManager() {
        OptimConfig optimConfig = new OptimConfig();
        this.mOptimConfig = optimConfig;
        optimConfig.mOptEnable = this.mConfigManager.isScrollOptEnabled();
        this.mOptimConfig.mAnimAheadEnable = this.mConfigManager.isAnimAheadEnabled();
        this.mOptimConfig.mFrameInsertEnable = this.mConfigManager.isFrameInsertEnabled();
        this.mOptimConfig.mInsertNum = this.mConfigManager.getInsertNum(null);
        this.mOptimConfig.mIsScrollChangedEnable = this.mConfigManager.isScrollChangedEnabled(null);
    }

    public OptimConfig getOptimConfig() {
        return this.mOptimConfig;
    }

    public void onDoFrameFinished() {
        if (!this.mHasInput) {
            boolean z = true;
            if (this.mScrollState != 1 && this.mScrollChangedState != 1) {
                z = false;
            }
            this.mIsFling = z;
        } else {
            this.mIsFling = false;
        }
        if (this.mIsScrollOptDebugEnable) {
            OplusDebugUtil.trace("SceneManager: mHasInput=" + this.mHasInput + ", ScrollState=" + this.mScrollState + ", scrollChangedState=" + this.mScrollChangedState);
        }
        this.mHasInput = false;
        this.mScrollState = 0;
        this.mScrollChangedState = 0;
    }

    public boolean isFling() {
        return this.mIsFling;
    }

    public void onFlingStart() {
        onFlingPositionUpdate();
    }

    public void onFlingPositionUpdate() {
        this.mScrollState = 1;
    }

    public void onFlingFinish() {
        this.mScrollState = 0;
    }

    public void onEventHandled(MotionEvent ev) {
        this.mHasInput = true;
    }

    public void onScrollChanged() {
        if (this.mOptimConfig.mIsScrollChangedEnable) {
            this.mScrollChangedState = 1;
        }
    }

    public void updateCurrentActivity(String activityName) {
        this.mCurrentActivityName = activityName;
        if (this.mOptimConfig.checkFrameInsertEnable()) {
            this.mOptimConfig.mInsertNum = this.mConfigManager.getInsertNum(this.mCurrentActivityName);
        }
        this.mOptimConfig.mIsScrollChangedEnable = this.mConfigManager.isScrollChangedEnabled(this.mCurrentActivityName);
        OplusDebugUtil.d(TAG, "updateCurrentActivity: mCurrentActivityName=" + this.mCurrentActivityName + ", isOptEnable=" + this.mOptimConfig.checkOptEnable() + ", isAnimAheadEnable=" + this.mOptimConfig.checkAnimAheadEnable() + ", isFrameInsertEnable=" + this.mOptimConfig.checkFrameInsertEnable() + ", InsertNum=" + this.mOptimConfig.checkInsertNum() + ", isEnabledForScrollChanged=" + this.mOptimConfig.checkIsEnabledForScrollChanged());
    }

    /* loaded from: classes.dex */
    public class OptimConfig {
        boolean mAnimAheadEnable;
        boolean mFrameInsertEnable;
        int mInsertNum;
        boolean mIsScrollChangedEnable = false;
        boolean mOptEnable;

        public OptimConfig() {
        }

        public boolean checkOptEnable() {
            return this.mOptEnable;
        }

        public boolean checkAnimAheadEnable() {
            return this.mAnimAheadEnable;
        }

        public boolean checkFrameInsertEnable() {
            return this.mFrameInsertEnable;
        }

        public int checkInsertNum() {
            return this.mInsertNum;
        }

        public boolean checkIsEnabledForScrollChanged() {
            return this.mIsScrollChangedEnable;
        }
    }
}
