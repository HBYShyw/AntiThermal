package android.content.pm;

import android.os.Bundle;
import android.os.Parcel;
import android.util.proto.ProtoOutputStream;

/* loaded from: classes.dex */
public class ApplicationInfoExtImpl implements IApplicationInfoExt {
    private static final int OPLUS_PRIVATE_FLAG_CHECK_DISPLAY_COMPAT = 16;
    private static final int OPLUS_PRIVATE_FLAG_ENABLE_DISPLAY_COMPAT = 8;
    public static final int OPLUS_PRIVATE_FLAG_IGNORE_OPENNDK = 4;
    private Bundle mAutoLayoutBundle;
    private boolean mIsInScrollToTopWhiteList;
    private boolean mIsNeedShowScrollToTopGuidePopup;
    private Bundle mRGBNormalizeBundle;
    private ApplicationInfo mRefApplicationInfo;
    private Bundle mResourcesManagerExtraBundle;
    private int oplusFreezeState;
    private int oplusPrivateFlags;
    private String[] specialNativeLibraryDirs;
    private float mAppscale = 1.0f;
    private float mNewappscale = 1.0f;
    private float mAappInvscale = 1.0f;
    private int mOverrideDensity = 0;
    private int mCompatDensity = 0;
    private int mLaunchingDisplayId = -1;
    private int mCmpactModeFlag = 0;
    private int mNormalModeFlag = 0;
    private int mTaskBarFlag = 0;
    private int mIsInAppHeapWhiteList = -1;

    public ApplicationInfoExtImpl(Object base) {
        this.mRefApplicationInfo = (ApplicationInfo) base;
    }

    public void init(ApplicationInfo refApplicationInfo) {
        this.mRefApplicationInfo = refApplicationInfo;
    }

    public void init(ApplicationInfo refApplicationInfo, ApplicationInfo origin) {
        this.mRefApplicationInfo = refApplicationInfo;
        refApplicationInfo.maxAspectRatio = origin.maxAspectRatio;
        IApplicationInfoExt originExt = origin.mApplicationInfoExt;
        if (originExt != null) {
            this.specialNativeLibraryDirs = originExt.getSpecialNativeLibraryDirs();
            this.mAppscale = originExt.getAppScale();
            this.mNewappscale = originExt.getNewAppScale();
            this.oplusFreezeState = originExt.getOplusFreezeState();
            this.mOverrideDensity = originExt.getOverrideDensity();
            this.mAappInvscale = originExt.getAppInvScale();
            this.mCompatDensity = originExt.getCompatDensity();
            this.oplusPrivateFlags = originExt.getPrivateFlags();
            this.mLaunchingDisplayId = originExt.getLaunchingDisplayId();
            this.mCmpactModeFlag = originExt.getCompatModeFlag();
            this.mNormalModeFlag = originExt.getNormalModeFlag();
            this.mAutoLayoutBundle = originExt.getAutoLayoutExtraBundle();
            this.mRGBNormalizeBundle = originExt.getRGBNormalizeExtraBundle();
            this.mTaskBarFlag = originExt.getTaskBarFlag();
            this.mResourcesManagerExtraBundle = originExt.getResourcesManagerExtraBundle();
        }
    }

    public void init(ApplicationInfo refApplicationInfo, Parcel source) {
        this.mRefApplicationInfo = refApplicationInfo;
        refApplicationInfo.maxAspectRatio = source.readFloat();
        this.specialNativeLibraryDirs = source.readStringArray();
        this.mAppscale = source.readFloat();
        this.mNewappscale = source.readFloat();
        this.oplusFreezeState = source.readInt();
        this.mOverrideDensity = source.readInt();
        this.mAappInvscale = source.readFloat();
        this.mCompatDensity = source.readInt();
        this.oplusPrivateFlags = source.readInt();
        this.mLaunchingDisplayId = source.readInt();
        this.mCmpactModeFlag = source.readInt();
        this.mNormalModeFlag = source.readInt();
        this.mAutoLayoutBundle = source.readBundle(getClass().getClassLoader());
        this.mRGBNormalizeBundle = source.readBundle(getClass().getClassLoader());
        this.mIsInScrollToTopWhiteList = source.readBoolean();
        this.mIsNeedShowScrollToTopGuidePopup = source.readBoolean();
        this.mTaskBarFlag = source.readInt();
        this.mIsInAppHeapWhiteList = source.readInt();
        this.mResourcesManagerExtraBundle = source.readBundle(getClass().getClassLoader());
    }

    public void setAppScale(float scale) {
        this.mAppscale = scale;
    }

    public float getAppScale() {
        return this.mAppscale;
    }

    public void setNewAppScale(float scale) {
        this.mNewappscale = scale;
    }

    public float getNewAppScale() {
        return this.mNewappscale;
    }

    public void setAppInvScale(float scale) {
        this.mAappInvscale = scale;
    }

    public float getAppInvScale() {
        return this.mAappInvscale;
    }

    public int getOverrideDensity() {
        return this.mOverrideDensity;
    }

    public void setOverrideDensity(int newValue) {
        this.mOverrideDensity = newValue;
        this.mRefApplicationInfo.overrideDensity = newValue;
    }

    public int getCompatDensity() {
        return this.mCompatDensity;
    }

    public void setCompatDensity(int newValue) {
        this.mCompatDensity = newValue;
    }

    public int getPrivateFlags() {
        return this.oplusPrivateFlags;
    }

    public void addPrivateFlags(int flags) {
        setPrivateFlags(flags, flags);
    }

    public void clearPrivateFlags(int flags) {
        setPrivateFlags(0, flags);
    }

    public void setPrivateFlags(int flags, int mask) {
        this.oplusPrivateFlags = (this.oplusPrivateFlags & (~mask)) | (flags & mask);
    }

    public boolean hasPrivateFlags(int flags) {
        return (this.oplusPrivateFlags & flags) == flags;
    }

    public boolean firstCheckSupportLowResolution() {
        if (hasPrivateFlags(16)) {
            return false;
        }
        addPrivateFlags(16);
        return true;
    }

    public boolean enableLowResolution() {
        return (this.oplusPrivateFlags & 8) == 8;
    }

    public void setEnableLowResolution(boolean enable) {
        if (enable) {
            addPrivateFlags(8);
        } else {
            clearPrivateFlags(8);
        }
    }

    public String[] getSpecialNativeLibraryDirs() {
        return this.specialNativeLibraryDirs;
    }

    public void setOplusFreezeState(int state) {
        this.oplusFreezeState = state;
    }

    public int getOplusFreezeState() {
        return this.oplusFreezeState;
    }

    public int getLaunchingDisplayId() {
        return this.mLaunchingDisplayId;
    }

    public void setLaunchingDisplayId(int displayId) {
        this.mLaunchingDisplayId = displayId;
    }

    public void setCmpactModeFlag(int flag) {
        this.mCmpactModeFlag = flag;
    }

    public int getCompatModeFlag() {
        return this.mCmpactModeFlag;
    }

    public void setNormalModeFlag(int flag) {
        this.mNormalModeFlag = flag;
    }

    public int getNormalModeFlag() {
        return this.mNormalModeFlag;
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, int dumpFlags) {
    }

    public void setAutoLayoutExtraBundle(Bundle bundle) {
        this.mAutoLayoutBundle = bundle;
    }

    public void setRGBNormalizeExtraBundle(Bundle bundle) {
        this.mRGBNormalizeBundle = bundle;
    }

    public Bundle getAutoLayoutExtraBundle() {
        return this.mAutoLayoutBundle;
    }

    public Bundle getRGBNormalizeExtraBundle() {
        return this.mRGBNormalizeBundle;
    }

    public boolean isInScrollToTopWhiteList() {
        return this.mIsInScrollToTopWhiteList;
    }

    public void setIsInScrollToTopWhiteList(boolean isInWhiteList) {
        this.mIsInScrollToTopWhiteList = isInWhiteList;
    }

    public int isInAppHeapWhiteList() {
        return this.mIsInAppHeapWhiteList;
    }

    public void setIsInAppHeapWhiteList(int isInWhiteList) {
        this.mIsInAppHeapWhiteList = isInWhiteList;
    }

    public boolean isNeedShowScrollToTopGuidePopup() {
        return this.mIsNeedShowScrollToTopGuidePopup;
    }

    public void setIsNeedShowScrollToTopGuidePopup(boolean needShowGuidPopup) {
        this.mIsNeedShowScrollToTopGuidePopup = needShowGuidPopup;
    }

    public int getTaskBarFlag() {
        return this.mTaskBarFlag;
    }

    public void setTaskBarFlag(int flag) {
        this.mTaskBarFlag = flag;
    }

    public void setResourcesManagerExtraBundle(Bundle bundle) {
        this.mResourcesManagerExtraBundle = bundle;
    }

    public Bundle getResourcesManagerExtraBundle() {
        return this.mResourcesManagerExtraBundle;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mRefApplicationInfo.maxAspectRatio);
        dest.writeStringArray(this.specialNativeLibraryDirs);
        dest.writeFloat(this.mAppscale);
        dest.writeFloat(this.mNewappscale);
        dest.writeInt(this.oplusFreezeState);
        dest.writeInt(this.mOverrideDensity);
        dest.writeFloat(this.mAappInvscale);
        dest.writeInt(this.mCompatDensity);
        dest.writeInt(this.oplusPrivateFlags);
        dest.writeInt(this.mLaunchingDisplayId);
        dest.writeInt(this.mCmpactModeFlag);
        dest.writeInt(this.mNormalModeFlag);
        dest.writeBundle(this.mAutoLayoutBundle);
        dest.writeBundle(this.mRGBNormalizeBundle);
        dest.writeBoolean(this.mIsInScrollToTopWhiteList);
        dest.writeBoolean(this.mIsNeedShowScrollToTopGuidePopup);
        dest.writeInt(this.mTaskBarFlag);
        dest.writeInt(this.mIsInAppHeapWhiteList);
        dest.writeBundle(this.mResourcesManagerExtraBundle);
    }
}
