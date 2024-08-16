package com.oplus.miragewindow;

import android.os.Bundle;

/* loaded from: classes.dex */
public class OplusMirageOptions {
    public static final int INVALID_DISPLAY = -1;
    private static final String KEY_MIRAGE_CAR_MULTI_LAUNCH_LIST = "mirage:car_multi_white_list";
    public static final String KEY_MIRAGE_CAST_MODE = "mirage:castmode";
    public static final String KEY_MIRAGE_DENSITY_DPI = "mirage:densitydpi";
    public static final String KEY_MIRAGE_DISPLAY_HEIGHT = "mirage:displayheight";
    public static final String KEY_MIRAGE_DISPLAY_ID = "mirage:displayid";
    public static final String KEY_MIRAGE_DISPLAY_WIDTH = "mirage:dislaywidth";
    private static final String KEY_MIRAGE_MOVE_TO_FRONT = "mirage:movetofront";
    private static final String KEY_MIRAGE_OLD_VERSION = "mirage:oldversion";
    private static final String KEY_MIRAGE_OWNER_PACKAGE_NAME = "mirage:ownerPackageName";
    private static final String KEY_MIRAGE_REFRESH_RATE = "mirage:refreshrate";
    private static final String KEY_MIRAGE_ROTATION = "mirage:rotation";
    private static final String KEY_MIRAGE_SESSION_ID = "mirage:sessionid";
    public static final String KEY_MIRAGE_X_DPI = "mirage:xdpi";
    public static final String KEY_MIRAGE_Y_DPI = "mirage:ydpi";
    public static final String KEY_TASK_DISPLAYID = "task:displayid";
    public static final String KEY_TASK_ID = "task:taskid";
    public static final String KEY_TASK_IS_FOCUSED = "task:isfocused";
    public static final String KEY_TASK_IS_RUNNING = "task:isrunning";
    public static final String KEY_TASK_IS_SLEEPING = "task:issleeping";
    public static final String KEY_TASK_IS_VISIBLE = "task:isvisible";
    private String mCarMultiLaunchWhiteList;
    private int mCastMode;
    private int mDensityDpi;
    private int mDisplayHeight;
    private int mDisplayId;
    private int mDisplayWidth;
    private boolean mMoveToFront;
    private boolean mOldVersion;
    private String mOwnerPackageName;
    private int mRefreshRate;
    private int mRotation;
    private int mSessionId;
    private float mXDpi;
    private float mYDpi;

    private OplusMirageOptions() {
        this.mDisplayId = -1;
    }

    public OplusMirageOptions(Bundle opts) {
        this.mDisplayId = -1;
        this.mDisplayId = opts.getInt(KEY_MIRAGE_DISPLAY_ID, -1);
        this.mCastMode = opts.getInt(KEY_MIRAGE_CAST_MODE, 0);
        this.mMoveToFront = opts.getBoolean(KEY_MIRAGE_MOVE_TO_FRONT, true);
        this.mDisplayWidth = opts.getInt(KEY_MIRAGE_DISPLAY_WIDTH, 0);
        this.mDisplayHeight = opts.getInt(KEY_MIRAGE_DISPLAY_HEIGHT, 0);
        this.mDensityDpi = opts.getInt(KEY_MIRAGE_DENSITY_DPI, 0);
        this.mXDpi = opts.getFloat(KEY_MIRAGE_X_DPI, 0.0f);
        this.mYDpi = opts.getFloat(KEY_MIRAGE_Y_DPI, 0.0f);
        this.mRefreshRate = opts.getInt(KEY_MIRAGE_REFRESH_RATE, 0);
        this.mSessionId = opts.getInt(KEY_MIRAGE_SESSION_ID, -1);
        this.mRotation = opts.getInt(KEY_MIRAGE_ROTATION, -1);
        this.mOldVersion = opts.getBoolean(KEY_MIRAGE_OLD_VERSION, false);
        this.mOwnerPackageName = (String) opts.getCharSequence(KEY_MIRAGE_OWNER_PACKAGE_NAME);
        this.mCarMultiLaunchWhiteList = opts.getString(KEY_MIRAGE_CAR_MULTI_LAUNCH_LIST, null);
    }

    public static OplusMirageOptions makeBasic() {
        OplusMirageOptions opts = new OplusMirageOptions();
        return opts;
    }

    public static OplusMirageOptions fromBundle(Bundle bOptions) {
        if (bOptions != null) {
            return new OplusMirageOptions(bOptions);
        }
        return null;
    }

    public static OplusMirageOptions makeBackgroundStreamModeOptions() {
        OplusMirageOptions opts = new OplusMirageOptions();
        opts.setCastMode(4);
        return opts;
    }

    public static OplusMirageOptions makeAppKeepModeOptions() {
        OplusMirageOptions opts = new OplusMirageOptions();
        opts.setCastMode(7);
        return opts;
    }

    public static OplusMirageOptions makeShareModeOptions(int displayId) {
        OplusMirageOptions opts = new OplusMirageOptions();
        opts.setCastMode(3);
        opts.setDisplayId(displayId);
        return opts;
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public int getCastMode() {
        return this.mCastMode;
    }

    public boolean getMoveToFront() {
        return this.mMoveToFront;
    }

    public int getDisplayWidth() {
        return this.mDisplayWidth;
    }

    public int getDisplayHeight() {
        return this.mDisplayHeight;
    }

    public int getDensityDpi() {
        return this.mDensityDpi;
    }

    public float getXDpi() {
        return this.mXDpi;
    }

    public float getYDpi() {
        return this.mYDpi;
    }

    public int getRefreshRate() {
        return this.mRefreshRate;
    }

    public int getSessionId() {
        return this.mSessionId;
    }

    public int getRotation() {
        return this.mRotation;
    }

    public boolean getOldVersion() {
        return this.mOldVersion;
    }

    public String getOwnerPackageName() {
        return this.mOwnerPackageName;
    }

    public String getCarMultiWhiteList() {
        return this.mCarMultiLaunchWhiteList;
    }

    public OplusMirageOptions setDisplayId(int displayId) {
        this.mDisplayId = displayId;
        return this;
    }

    public OplusMirageOptions setCastMode(int castMode) {
        this.mCastMode = castMode;
        return this;
    }

    public OplusMirageOptions setMoveToFront(boolean moveToFront) {
        this.mMoveToFront = moveToFront;
        return this;
    }

    public OplusMirageOptions setDisplayWidth(int displayWidth) {
        this.mDisplayWidth = displayWidth;
        return this;
    }

    public OplusMirageOptions setDisplayHeight(int displayHeight) {
        this.mDisplayHeight = displayHeight;
        return this;
    }

    public OplusMirageOptions setDensityDpi(int densityDpi) {
        this.mDensityDpi = densityDpi;
        return this;
    }

    public OplusMirageOptions setXDpi(float xDpi) {
        this.mXDpi = xDpi;
        return this;
    }

    public OplusMirageOptions setYDpi(float yDpi) {
        this.mYDpi = yDpi;
        return this;
    }

    public OplusMirageOptions setRefreshRate(int refreshRate) {
        this.mRefreshRate = refreshRate;
        return this;
    }

    public OplusMirageOptions setSession(int sessionId) {
        this.mSessionId = sessionId;
        return this;
    }

    public OplusMirageOptions setRotation(int rotation) {
        this.mRotation = rotation;
        return this;
    }

    public OplusMirageOptions setOldVersion(boolean old) {
        this.mOldVersion = old;
        return this;
    }

    public OplusMirageOptions setOwnerPackageName(String packageName) {
        this.mOwnerPackageName = packageName;
        return this;
    }

    public OplusMirageOptions setCarMultiLaunchWhiteList(String carMultiLaunchWhiteList) {
        this.mCarMultiLaunchWhiteList = carMultiLaunchWhiteList;
        return this;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        int i = this.mCastMode;
        if (i != 0) {
            b.putInt(KEY_MIRAGE_CAST_MODE, i);
        }
        int i2 = this.mDisplayId;
        if (i2 != -1) {
            b.putInt(KEY_MIRAGE_DISPLAY_ID, i2);
        }
        b.putBoolean(KEY_MIRAGE_MOVE_TO_FRONT, this.mMoveToFront);
        int i3 = this.mDisplayWidth;
        if (i3 > 0) {
            b.putInt(KEY_MIRAGE_DISPLAY_WIDTH, i3);
        }
        int i4 = this.mDisplayHeight;
        if (i4 > 0) {
            b.putInt(KEY_MIRAGE_DISPLAY_HEIGHT, i4);
        }
        int i5 = this.mDensityDpi;
        if (i5 > 0) {
            b.putInt(KEY_MIRAGE_DENSITY_DPI, i5);
        }
        float f = this.mXDpi;
        if (f > 0.0f) {
            b.putFloat(KEY_MIRAGE_X_DPI, f);
        }
        float f2 = this.mYDpi;
        if (f2 > 0.0f) {
            b.putFloat(KEY_MIRAGE_Y_DPI, f2);
        }
        int i6 = this.mRefreshRate;
        if (i6 > 0) {
            b.putInt(KEY_MIRAGE_REFRESH_RATE, i6);
        }
        int i7 = this.mSessionId;
        if (i7 > 0) {
            b.putInt(KEY_MIRAGE_SESSION_ID, i7);
        }
        int i8 = this.mRotation;
        if (i8 >= 0) {
            b.putInt(KEY_MIRAGE_ROTATION, i8);
        }
        b.putBoolean(KEY_MIRAGE_OLD_VERSION, this.mOldVersion);
        String str = this.mOwnerPackageName;
        if (str != null) {
            b.putCharSequence(KEY_MIRAGE_OWNER_PACKAGE_NAME, str);
        }
        String str2 = this.mCarMultiLaunchWhiteList;
        if (str2 != null) {
            b.putString(KEY_MIRAGE_CAR_MULTI_LAUNCH_LIST, str2);
        }
        return b;
    }
}
