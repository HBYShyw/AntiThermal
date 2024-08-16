package android.app.uxicons;

import android.content.res.Resources;
import android.graphics.Path;
import com.oplus.util.UxScreenUtil;

/* loaded from: classes.dex */
public class CustomAdaptiveIconConfig {
    public static final int OPLUS_ADAPTIVE_MASK_SIZE = 150;
    private int mCustomIconFgSize;
    private int mCustomIconSize;
    private Path mCustomMask;
    private int mDefaultIconSize;
    private float mForegroundScalePercent;
    private boolean mIsAdaptiveIconDrawable;
    private boolean mIsPlatformDrawable;
    private float mScalePercent;

    /* loaded from: classes.dex */
    public static class Builder {
        private CustomAdaptiveIconConfig mConfig;

        public Builder(Resources res) {
            this.mConfig = new CustomAdaptiveIconConfig(res);
        }

        public Builder setCustomIconSize(int customIconSize) {
            this.mConfig.mCustomIconSize = customIconSize;
            this.mConfig.mScalePercent = (customIconSize * 1.0f) / r0.getDefaultIconSize();
            return this;
        }

        public Builder setCustomIconFgSize(int customIconFgSize) {
            this.mConfig.mCustomIconFgSize = customIconFgSize;
            this.mConfig.mForegroundScalePercent = (customIconFgSize * 1.0f) / r0.getDefaultIconSize();
            return this;
        }

        public Builder setCustomMask(Path customMask) {
            this.mConfig.mCustomMask = customMask;
            return this;
        }

        public Builder setIsPlatformDrawable(boolean isPlatformDrawable) {
            this.mConfig.mIsPlatformDrawable = isPlatformDrawable;
            return this;
        }

        public Builder setIsAdaptiveIconDrawable(boolean isAdaptiveIconDrawable) {
            this.mConfig.mIsAdaptiveIconDrawable = isAdaptiveIconDrawable;
            return this;
        }

        public CustomAdaptiveIconConfig create() {
            return this.mConfig;
        }
    }

    private CustomAdaptiveIconConfig(Resources resources) {
        if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
            this.mDefaultIconSize = resources.getDimensionPixelSize(201654492);
        } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
            this.mDefaultIconSize = resources.getDimensionPixelSize(201654491);
        } else if (UxScreenUtil.isTabletDevices()) {
            this.mDefaultIconSize = resources.getDimensionPixelSize(201654492);
        } else {
            this.mDefaultIconSize = resources.getDimensionPixelSize(201654414);
        }
        int i = this.mDefaultIconSize;
        this.mCustomIconSize = i;
        this.mCustomIconFgSize = i;
        this.mCustomMask = null;
        this.mScalePercent = 1.0f;
        this.mForegroundScalePercent = 1.0f;
        this.mIsPlatformDrawable = false;
        this.mIsAdaptiveIconDrawable = false;
    }

    public int getDefaultIconSize() {
        return this.mDefaultIconSize;
    }

    public Path getCustomMask() {
        return this.mCustomMask;
    }

    public int getCustomIconSize() {
        return this.mCustomIconSize;
    }

    public int getCustomIconFgSize() {
        return this.mCustomIconFgSize;
    }

    public float getScalePercent() {
        return this.mScalePercent;
    }

    public float getForegroundScalePercent() {
        return this.mForegroundScalePercent;
    }

    public boolean getIsPlatformDrawable() {
        return this.mIsPlatformDrawable;
    }

    public boolean getIsAdaptiveIconDrawable() {
        return this.mIsAdaptiveIconDrawable;
    }

    public String toString() {
        return "CustomIconConfig:DefaultIconSize = " + this.mDefaultIconSize + ";CustomIconSize = " + this.mCustomIconSize + ";CustomIconFgSize = " + this.mCustomIconFgSize + ";ScalePercent" + this.mScalePercent + ";ForegroundScalePercent = " + this.mForegroundScalePercent + ";IsPlatformDrawable = " + this.mIsPlatformDrawable + ";IsAdaptiveIconDrawable" + this.mIsAdaptiveIconDrawable;
    }
}
