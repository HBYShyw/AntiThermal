package android.app;

import android.os.Bundle;

/* loaded from: classes.dex */
public class ActivityOptionsExtImpl implements IActivityOptionsExt {
    private static final String KEY_LAUNCHED_FROM_MULTI_SEARCH = "android.activity.launchTypeMultiSearch";
    private static final String KEY_LAUNCHED_IN_SPLIT_POSITION = "android.activity.launchInSplitPosition";
    private static final String KEY_MULTI_APP_FLAGS = "android.activity.OplusMultiAppFlags";
    public static final String KEY_RP_LAUNCH_HINT = "android:activity.isRPLaunch";
    public static final String KEY_START_RECENT_REASON = "start_reason";
    public static final String KEY_ZOOM_LAUNCH_FLAGS = "android:activity.mZoomLaunchFlags";
    public static final int VALUE_START_RECENT_FROM_CLICK = 1;
    public static final int VALUE_START_RECENT_FROM_SLIDE = 2;
    private ActivityOptions mBase;
    private boolean mIsRPLaunch;
    private boolean mLaunchedFromMultiSearch;
    private int mMultiAppFlags;
    private String mRemoteTaskSecurityToken;
    private String mRemoteTaskUUID;
    private int mZoomLaunchFlags;
    private int mLaunchedInSplitPosition = -1;
    private int mRemoteTaskFlag = -1;

    public ActivityOptionsExtImpl(Object base) {
        this.mBase = (ActivityOptions) base;
    }

    public void setData(Bundle b) {
        setRPLaunch(b.getBoolean(KEY_RP_LAUNCH_HINT, false));
        setZoomLaunchFlags(b.getInt("android:activity.mZoomLaunchFlags", -1));
        setLaunchedFromMultiSearch(b.getBoolean(KEY_LAUNCHED_FROM_MULTI_SEARCH, false));
        setLaunchedInSplitPosition(b.getInt(KEY_LAUNCHED_IN_SPLIT_POSITION, -1));
        setRemoteTaskFlag(b.getInt(RemoteTaskConstants.KEY_REMOTE_TASK_LAUNCH_OPTION, 0));
        setRemoteUuid(b.getString(RemoteTaskConstants.KEY_REMOTE_TASK_UUID, null));
        setRemoteSecurityToken(b.getString(RemoteTaskConstants.KEY_REMOTE_TASK_SECURITY_TOKEN, null));
        this.mMultiAppFlags = b.getInt(KEY_MULTI_APP_FLAGS, 0);
    }

    public void getData(Bundle b) {
        if (isRPLaunch()) {
            b.putBoolean(KEY_RP_LAUNCH_HINT, true);
        }
        if (getZoomLaunchFlags() != -1) {
            b.putInt("android:activity.mZoomLaunchFlags", getZoomLaunchFlags());
        }
        boolean z = this.mLaunchedFromMultiSearch;
        if (z) {
            b.putBoolean(KEY_LAUNCHED_FROM_MULTI_SEARCH, z);
        }
        if (getLaunchedInSplitPosition() != -1) {
            b.putInt(KEY_LAUNCHED_IN_SPLIT_POSITION, getLaunchedInSplitPosition());
        }
        int i = this.mRemoteTaskFlag;
        if (i != 0) {
            b.putInt(RemoteTaskConstants.KEY_REMOTE_TASK_LAUNCH_OPTION, i);
        }
        String str = this.mRemoteTaskUUID;
        if (str != null) {
            b.putString(RemoteTaskConstants.KEY_REMOTE_TASK_UUID, str);
        }
        String str2 = this.mRemoteTaskSecurityToken;
        if (str2 != null) {
            b.putString(RemoteTaskConstants.KEY_REMOTE_TASK_SECURITY_TOKEN, str2);
        }
        int i2 = this.mMultiAppFlags;
        if (i2 > 0) {
            b.putInt(KEY_MULTI_APP_FLAGS, i2);
        }
    }

    public boolean isRPLaunch() {
        return this.mIsRPLaunch;
    }

    public void setRPLaunch(boolean rpLaunch) {
        this.mIsRPLaunch = rpLaunch;
    }

    public int getZoomLaunchFlags() {
        return this.mZoomLaunchFlags;
    }

    public void setZoomLaunchFlags(int zoomLaunchFlags) {
        this.mZoomLaunchFlags = zoomLaunchFlags;
    }

    public void setLaunchedFromMultiSearch(boolean fromMultiSearch) {
        this.mLaunchedFromMultiSearch = fromMultiSearch;
    }

    public boolean getLaunchedFromMultiSearch() {
        return this.mLaunchedFromMultiSearch;
    }

    public void setLaunchedInSplitPosition(int launchedInSplitPosition) {
        this.mLaunchedInSplitPosition = launchedInSplitPosition;
    }

    public int getLaunchedInSplitPosition() {
        return this.mLaunchedInSplitPosition;
    }

    public void setRemoteTaskFlag(int remoteTaskFlag) {
        this.mRemoteTaskFlag = remoteTaskFlag;
    }

    public int getRemoteTaskFlag() {
        return this.mRemoteTaskFlag;
    }

    public void setRemoteUuid(String remoteTaskUUID) {
        this.mRemoteTaskUUID = remoteTaskUUID;
    }

    public String getRemoteUuid() {
        return this.mRemoteTaskUUID;
    }

    public void setRemoteSecurityToken(String remoteSecurityToken) {
        this.mRemoteTaskSecurityToken = remoteSecurityToken;
    }

    public String getRemoteSecurityToken() {
        return this.mRemoteTaskSecurityToken;
    }

    public int getMultiAppFlags() {
        return this.mMultiAppFlags;
    }

    public void setStartRecentReason(Bundle extraBundle, Bundle opts) {
        int startReason = opts.getInt(KEY_START_RECENT_REASON, -1);
        if (startReason != -1) {
            extraBundle.putInt(KEY_START_RECENT_REASON, startReason);
        }
    }
}
