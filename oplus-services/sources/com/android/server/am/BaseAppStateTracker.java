package com.android.server.am;

import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.media.session.MediaSessionManager;
import android.os.BatteryManagerInternal;
import android.os.BatteryStatsInternal;
import android.os.Handler;
import android.os.ServiceManager;
import android.permission.PermissionManager;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.IAppOpsService;
import com.android.server.DeviceIdleInternal;
import com.android.server.LocalServices;
import com.android.server.am.AppRestrictionController;
import com.android.server.am.BaseAppStatePolicy;
import com.android.server.notification.NotificationManagerInternal;
import com.android.server.pm.UserManagerInternal;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class BaseAppStateTracker<T extends BaseAppStatePolicy> {
    static final long ONE_DAY = 86400000;
    static final long ONE_HOUR = 3600000;
    static final long ONE_MINUTE = 60000;
    static final int STATE_TYPE_FGS_LOCATION = 4;
    static final int STATE_TYPE_FGS_MEDIA_PLAYBACK = 2;
    static final int STATE_TYPE_FGS_WITH_NOTIFICATION = 8;
    static final int STATE_TYPE_INDEX_FGS_LOCATION = 2;
    static final int STATE_TYPE_INDEX_FGS_MEDIA_PLAYBACK = 1;
    static final int STATE_TYPE_INDEX_FGS_WITH_NOTIFICATION = 3;
    static final int STATE_TYPE_INDEX_MEDIA_SESSION = 0;
    static final int STATE_TYPE_INDEX_PERMISSION = 4;
    static final int STATE_TYPE_MEDIA_SESSION = 1;
    static final int STATE_TYPE_NUM = 5;
    static final int STATE_TYPE_PERMISSION = 16;
    protected static final String TAG = "ActivityManager";
    protected final AppRestrictionController mAppRestrictionController;
    protected final Handler mBgHandler;
    protected final Context mContext;
    protected final Injector<T> mInjector;
    protected final Object mLock;
    protected final ArrayList<StateListener> mStateListeners = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface StateListener {
        void onStateChange(int i, String str, boolean z, long j, int i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int stateIndexToType(int i) {
        return 1 << i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpAsProto(ProtoOutputStream protoOutputStream, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getTrackerInfoForStatsd(int i) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @AppRestrictionController.TrackerType
    public int getType() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBackgroundRestrictionChanged(int i, String str, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLockedBootCompleted() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUidAdded(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUidGone(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUidProcStateChanged(int i, int i2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUidRemoved(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserAdded(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserInteractionStarted(String str, int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserRemoved(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserStarted(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserStopped(int i) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAppStateTracker(Context context, AppRestrictionController appRestrictionController, Constructor<? extends Injector<T>> constructor, Object obj) {
        Injector<T> injector;
        this.mContext = context;
        this.mAppRestrictionController = appRestrictionController;
        this.mBgHandler = appRestrictionController.getBackgroundHandler();
        this.mLock = appRestrictionController.getLock();
        if (constructor == null) {
            this.mInjector = new Injector<>();
            return;
        }
        try {
            injector = constructor.newInstance(obj);
        } catch (Exception e) {
            Slog.w("ActivityManager", "Unable to instantiate " + constructor, e);
            injector = null;
        }
        this.mInjector = injector == null ? new Injector<>() : injector;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int stateTypeToIndex(int i) {
        return Integer.numberOfTrailingZeros(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String stateTypesToString(int i) {
        StringBuilder sb = new StringBuilder("[");
        int highestOneBit = Integer.highestOneBit(i);
        boolean z = false;
        while (highestOneBit != 0) {
            if (z) {
                sb.append('|');
            }
            z = true;
            if (highestOneBit == 1) {
                sb.append("MEDIA_SESSION");
            } else if (highestOneBit == 2) {
                sb.append("FGS_MEDIA_PLAYBACK");
            } else if (highestOneBit == 4) {
                sb.append("FGS_LOCATION");
            } else if (highestOneBit == 8) {
                sb.append("FGS_NOTIFICATION");
            } else if (highestOneBit == 16) {
                sb.append("PERMISSION");
            } else {
                return "[UNKNOWN(" + Integer.toHexString(i) + ")]";
            }
            i &= ~highestOneBit;
            highestOneBit = Integer.highestOneBit(i);
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerStateListener(StateListener stateListener) {
        synchronized (this.mLock) {
            this.mStateListeners.add(stateListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyListenersOnStateChange(int i, String str, boolean z, long j, int i2) {
        synchronized (this.mLock) {
            int size = this.mStateListeners.size();
            for (int i3 = 0; i3 < size; i3++) {
                this.mStateListeners.get(i3).onStateChange(i, str, z, j, i2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public T getPolicy() {
        return this.mInjector.getPolicy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        this.mInjector.onSystemReady();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPropertiesChanged(String str) {
        getPolicy().onPropertiesChanged(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        this.mInjector.getPolicy().dump(printWriter, "  " + str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector<T extends BaseAppStatePolicy> {
        ActivityManagerInternal mActivityManagerInternal;
        AppOpsManager mAppOpsManager;
        T mAppStatePolicy;
        BatteryManagerInternal mBatteryManagerInternal;
        BatteryStatsInternal mBatteryStatsInternal;
        DeviceIdleInternal mDeviceIdleInternal;
        IAppOpsService mIAppOpsService;
        MediaSessionManager mMediaSessionManager;
        NotificationManagerInternal mNotificationManagerInternal;
        PackageManager mPackageManager;
        PackageManagerInternal mPackageManagerInternal;
        PermissionManager mPermissionManager;
        PermissionManagerServiceInternal mPermissionManagerServiceInternal;
        RoleManager mRoleManager;
        UserManagerInternal mUserManagerInternal;

        Injector() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setPolicy(T t) {
            this.mAppStatePolicy = t;
        }

        void onSystemReady() {
            this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            this.mBatteryManagerInternal = (BatteryManagerInternal) LocalServices.getService(BatteryManagerInternal.class);
            this.mBatteryStatsInternal = (BatteryStatsInternal) LocalServices.getService(BatteryStatsInternal.class);
            this.mDeviceIdleInternal = (DeviceIdleInternal) LocalServices.getService(DeviceIdleInternal.class);
            this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
            this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            this.mPermissionManagerServiceInternal = (PermissionManagerServiceInternal) LocalServices.getService(PermissionManagerServiceInternal.class);
            Context context = this.mAppStatePolicy.mTracker.mContext;
            this.mPackageManager = context.getPackageManager();
            this.mAppOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
            this.mMediaSessionManager = (MediaSessionManager) context.getSystemService(MediaSessionManager.class);
            this.mPermissionManager = (PermissionManager) context.getSystemService(PermissionManager.class);
            this.mRoleManager = (RoleManager) context.getSystemService(RoleManager.class);
            this.mNotificationManagerInternal = (NotificationManagerInternal) LocalServices.getService(NotificationManagerInternal.class);
            this.mIAppOpsService = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));
            getPolicy().onSystemReady();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ActivityManagerInternal getActivityManagerInternal() {
            return this.mActivityManagerInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BatteryManagerInternal getBatteryManagerInternal() {
            return this.mBatteryManagerInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BatteryStatsInternal getBatteryStatsInternal() {
            return this.mBatteryStatsInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public T getPolicy() {
            return this.mAppStatePolicy;
        }

        DeviceIdleInternal getDeviceIdleInternal() {
            return this.mDeviceIdleInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public UserManagerInternal getUserManagerInternal() {
            return this.mUserManagerInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PackageManager getPackageManager() {
            return this.mPackageManager;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PackageManagerInternal getPackageManagerInternal() {
            return this.mPackageManagerInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PermissionManager getPermissionManager() {
            return this.mPermissionManager;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PermissionManagerServiceInternal getPermissionManagerServiceInternal() {
            return this.mPermissionManagerServiceInternal;
        }

        AppOpsManager getAppOpsManager() {
            return this.mAppOpsManager;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public MediaSessionManager getMediaSessionManager() {
            return this.mMediaSessionManager;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getServiceStartForegroundTimeout() {
            return this.mActivityManagerInternal.getServiceStartForegroundTimeout();
        }

        RoleManager getRoleManager() {
            return this.mRoleManager;
        }

        NotificationManagerInternal getNotificationManagerInternal() {
            return this.mNotificationManagerInternal;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public IAppOpsService getIAppOpsService() {
            return this.mIAppOpsService;
        }
    }
}
