package android.view.autolayout;

import android.widget.ImageView;

/* loaded from: classes.dex */
public class AutoLayoutViewInfo {
    public static final int FILL_PATTERN_ALL = 3;
    public static final int FILL_PATTERN_NONE = 1;
    public static final int FILL_PATTERN_SCALE_ONLY = 2;
    public static final int TYPE_FULL = 3;
    public static final int TYPE_LARGE = 2;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SMALL = 4;
    private boolean mIsFlatView;
    private boolean mIsImageType;
    private boolean mMeasureChanged;
    private boolean mNeedStretchItems;
    private int mNewMeasuredHeight;
    private int mOldMeasuredHeight;
    private ImageView.ScaleType mScaleType;
    private int mWidthType = 1;
    private int mHeightType = 1;
    private int mFillBackgroundPattern = 1;
    private StringBuilder mExtraInfo = new StringBuilder();

    public void setNeedStretchItems(boolean needStretch) {
        this.mNeedStretchItems = needStretch;
    }

    public boolean getNeedStretchItems() {
        return this.mNeedStretchItems;
    }

    public void setMeasureChanged(boolean changed) {
        this.mMeasureChanged = changed;
    }

    public boolean getMeasureChanged() {
        return this.mMeasureChanged;
    }

    public void setNewMeasuredHeight(int newMeasuredHeight) {
        this.mNewMeasuredHeight = newMeasuredHeight;
    }

    public int getNewMeasuredHeight() {
        return this.mNewMeasuredHeight;
    }

    public void setOldMeasuredHeight(int oldMeasuredHeight) {
        this.mOldMeasuredHeight = oldMeasuredHeight;
    }

    public int getOldMeasuredHeight() {
        return this.mOldMeasuredHeight;
    }

    public void setIsImageType(boolean isImageType) {
        this.mIsImageType = isImageType;
    }

    public boolean getIsImageType() {
        return this.mIsImageType;
    }

    public void setWidthType(int type) {
        this.mWidthType = type;
    }

    public int getWidthType() {
        return this.mWidthType;
    }

    public void setHeightType(int type) {
        this.mHeightType = type;
    }

    public int getHeightType() {
        return this.mHeightType;
    }

    public void setFillBackgroundPattern(int pattern) {
        this.mFillBackgroundPattern = pattern;
    }

    public int getFillBackgroundPattern() {
        return this.mFillBackgroundPattern;
    }

    public void setIsFlatView(boolean isFlatView) {
        this.mIsFlatView = isFlatView;
    }

    public boolean getIsFlatView() {
        return this.mIsFlatView;
    }

    public void setOriginScaleType(ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    public ImageView.ScaleType getOriginScaleType() {
        return this.mScaleType;
    }

    public void setExtraInfo(String content) {
        this.mExtraInfo.append("<");
        this.mExtraInfo.append(content);
        this.mExtraInfo.append(">");
    }

    public StringBuilder getExtraInfo() {
        return this.mExtraInfo;
    }

    public void reset() {
        setNeedStretchItems(false);
        setIsFlatView(false);
        setWidthType(1);
        setHeightType(1);
        setIsImageType(false);
        setFillBackgroundPattern(1);
        this.mExtraInfo = new StringBuilder();
        setMeasureChanged(false);
        setNewMeasuredHeight(0);
        setOldMeasuredHeight(0);
    }

    public String toString() {
        return "AutoLayoutViewInfo{mIsImageType=" + this.mIsImageType + ", mIsFlatView=" + this.mIsFlatView + ", mWidthType=" + this.mWidthType + ", mHeightType=" + this.mHeightType + ", mScaleType=" + this.mScaleType + ", mFillBackgroundPattern=" + this.mFillBackgroundPattern + ", mNeedStretchItems=" + this.mNeedStretchItems + ", mMeasureChanged=" + this.mMeasureChanged + ", mNewMeasuredHeight=" + this.mNewMeasuredHeight + ", mOldMeasuredHeight=" + this.mOldMeasuredHeight + ", mExtraInfo=" + ((Object) this.mExtraInfo) + '}';
    }
}
