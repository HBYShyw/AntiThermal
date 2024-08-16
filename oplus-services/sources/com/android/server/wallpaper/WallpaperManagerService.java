package com.android.server.wallpaper;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.app.ILocalWallpaperColorConsumer;
import android.app.IWallpaperManager;
import android.app.IWallpaperManagerCallback;
import android.app.IWallpaperManagerExt;
import android.app.PendingIntent;
import android.app.UserSwitchObserver;
import android.app.WallpaperColors;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SELinux;
import android.os.ShellCallback;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.service.wallpaper.IWallpaperConnection;
import android.service.wallpaper.IWallpaperEngine;
import android.service.wallpaper.IWallpaperService;
import android.system.ErrnoException;
import android.system.Os;
import android.util.EventLog;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Display;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.pm.UserManagerInternal;
import com.android.server.utils.TimingsTraceAndSlog;
import com.android.server.wallpaper.WallpaperDataParser;
import com.android.server.wallpaper.WallpaperDisplayHelper;
import com.android.server.wallpaper.WallpaperManagerService;
import com.android.server.wm.WindowManagerInternal;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperManagerService extends IWallpaperManager.Stub implements IWallpaperManagerService {
    private static final boolean DEBUG_LIVE = true;
    private static final int MAX_WALLPAPER_COMPONENT_LOG_LENGTH = 128;
    private static final long MIN_WALLPAPER_CRASH_TIME = 10000;
    private static final String TAG = "WallpaperManagerService";
    private final ActivityManager mActivityManager;
    private final AppOpsManager mAppOpsManager;
    private WallpaperColors mCacheDefaultImageWallpaperColors;
    private final SparseArray<SparseArray<RemoteCallbackList<IWallpaperManagerCallback>>> mColorsChangedListeners;
    private final Context mContext;
    private int mCurrentUserId;
    private ComponentName mDefaultWallpaperComponent;
    private final DisplayManager.DisplayListener mDisplayListener;
    protected WallpaperData mFallbackWallpaper;
    private boolean mHomeWallpaperWaitingForUnlock;
    private final IPackageManager mIPackageManager;
    private final ComponentName mImageWallpaper;
    private boolean mInAmbientMode;
    private final boolean mIsLockscreenLiveWallpaperEnabled;
    private final boolean mIsMultiCropEnabled;
    private IWallpaperManagerCallback mKeyguardListener;
    protected WallpaperData mLastLockWallpaper;
    protected WallpaperData mLastWallpaper;
    private LocalColorRepository mLocalColorRepo;
    private final Object mLock = new Object();
    private final SparseArray<WallpaperData> mLockWallpaperMap;
    private boolean mLockWallpaperWaitingForUnlock;
    private final MyPackageMonitor mMonitor;
    private final PackageManagerInternal mPackageManagerInternal;
    WallpaperDestinationChangeHandler mPendingMigrationViaStatic;
    private boolean mShuttingDown;
    private final SparseBooleanArray mUserRestorecon;
    private boolean mWaitingForUnlock;
    final WallpaperCropper mWallpaperCropper;

    @VisibleForTesting
    final WallpaperDataParser mWallpaperDataParser;

    @VisibleForTesting
    final WallpaperDisplayHelper mWallpaperDisplayHelper;
    private IWallpaperManagerExt mWallpaperManagerExt;
    private IWallpaperManagerServiceExt mWallpaperManagerServiceExt;
    private WallpaperManagerServiceWrapperImpl mWallpaperManagerServiceWrapper;
    private final SparseArray<WallpaperData> mWallpaperMap;
    private final WindowManagerInternal mWindowManagerInternal;
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final RectF LOCAL_COLOR_BOUNDS = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    private static final Map<Integer, String> sWallpaperType = Map.of(1, "decode_record", 2, "decode_lock_record");

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private IWallpaperManagerService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        public void onStart() {
            try {
                IWallpaperManagerService iWallpaperManagerService = (IWallpaperManagerService) Class.forName(getContext().getResources().getString(R.string.dynamic_mode_notification_channel_name)).getConstructor(Context.class).newInstance(getContext());
                this.mService = iWallpaperManagerService;
                publishBinderService("wallpaper", iWallpaperManagerService);
            } catch (Exception e) {
                Slog.wtf(WallpaperManagerService.TAG, "Failed to instantiate WallpaperManagerService", e);
            }
        }

        public void onBootPhase(int i) {
            IWallpaperManagerService iWallpaperManagerService = this.mService;
            if (iWallpaperManagerService != null) {
                iWallpaperManagerService.onBootPhase(i);
            }
        }

        public void onUserUnlocking(SystemService.TargetUser targetUser) {
            IWallpaperManagerService iWallpaperManagerService = this.mService;
            if (iWallpaperManagerService != null) {
                iWallpaperManagerService.onUnlockUser(targetUser.getUserIdentifier());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WallpaperObserver extends FileObserver {
        final int mUserId;
        final WallpaperData mWallpaper;
        final File mWallpaperDir;
        final File mWallpaperFile;
        final File mWallpaperLockFile;

        public WallpaperObserver(WallpaperData wallpaperData) {
            super(WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperDir(wallpaperData.userId, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId(), WallpaperUtils.getWallpaperDir(wallpaperData.userId)).getAbsolutePath(), 1672);
            this.mUserId = wallpaperData.userId;
            File wallpaperDir = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperDir(wallpaperData.userId, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId(), WallpaperUtils.getWallpaperDir(wallpaperData.userId));
            this.mWallpaperDir = wallpaperDir;
            this.mWallpaper = wallpaperData;
            this.mWallpaperFile = new File(wallpaperDir, "wallpaper_orig");
            this.mWallpaperLockFile = new File(wallpaperDir, "wallpaper_lock_orig");
        }

        WallpaperData dataForEvent(boolean z, boolean z2) {
            WallpaperData wallpaperData;
            synchronized (WallpaperManagerService.this.mLock) {
                if (z2) {
                    try {
                        wallpaperData = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, this.mWallpaper.mWallpaperDataExt.getPhysicalDisplayId(), WallpaperManagerService.this.mLockWallpaperMap).get(this.mUserId);
                    } catch (Throwable th) {
                        throw th;
                    }
                } else {
                    wallpaperData = null;
                }
                if (wallpaperData == null) {
                    wallpaperData = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, this.mWallpaper.mWallpaperDataExt.getPhysicalDisplayId(), WallpaperManagerService.this.mWallpaperMap).get(this.mUserId);
                }
            }
            return wallpaperData != null ? wallpaperData : this.mWallpaper;
        }

        /* JADX WARN: Removed duplicated region for block: B:72:0x01cf  */
        /* JADX WARN: Removed duplicated region for block: B:74:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void updateWallpapers(int i, String str) {
            File file = new File(this.mWallpaperDir, str);
            boolean equals = this.mWallpaperFile.equals(file);
            boolean equals2 = this.mWallpaperLockFile.equals(file);
            final WallpaperData dataForEvent = dataForEvent(equals, equals2);
            int i2 = 1;
            boolean z = i == 128;
            boolean z2 = i == 8 || z;
            boolean z3 = z && equals2;
            boolean z4 = z && !z3;
            boolean z5 = (dataForEvent.mWhich & 2) != 0;
            boolean z6 = dataForEvent.wallpaperComponent == null || i != 8 || dataForEvent.imageWallpaperPending;
            if (WallpaperManagerService.DEBUG) {
                Slog.v(WallpaperManagerService.TAG, "Wallpaper file change: evt=" + i + " path=" + str + " sys=" + equals + " lock=" + equals2 + " imagePending=" + dataForEvent.imageWallpaperPending + " mWhich=0x" + Integer.toHexString(dataForEvent.mWhich) + " written=" + z2 + " isMigration=" + z3 + " isRestore=" + z4 + " isAppliedToLock=" + z5 + " needsUpdate=" + z6);
            }
            if (z3) {
                return;
            }
            if (equals || equals2) {
                synchronized (WallpaperManagerService.this.mLock) {
                    WallpaperManagerService.this.notifyCallbacksLocked(dataForEvent);
                    if (z2 && z6) {
                        if (WallpaperManagerService.DEBUG) {
                            Slog.v(WallpaperManagerService.TAG, "Setting new static wallpaper: which=" + dataForEvent.mWhich);
                        }
                        WallpaperManagerService wallpaperManagerService = WallpaperManagerService.this;
                        final WallpaperDestinationChangeHandler wallpaperDestinationChangeHandler = wallpaperManagerService.mPendingMigrationViaStatic;
                        wallpaperManagerService.mPendingMigrationViaStatic = null;
                        SELinux.restorecon(file);
                        if (z4) {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.v(WallpaperManagerService.TAG, "Wallpaper restore; reloading metadata");
                            }
                            WallpaperManagerService.this.loadSettingsLocked(dataForEvent.userId, true, 3);
                        }
                        if (WallpaperManagerService.DEBUG) {
                            Slog.v(WallpaperManagerService.TAG, "Wallpaper written; generating crop");
                        }
                        WallpaperManagerService.this.mWallpaperCropper.generateCrop(dataForEvent);
                        if (WallpaperManagerService.DEBUG) {
                            Slog.v(WallpaperManagerService.TAG, "Crop done; invoking completion callback");
                        }
                        dataForEvent.imageWallpaperPending = false;
                        if (equals) {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.v(WallpaperManagerService.TAG, "Home screen wallpaper changed");
                            }
                            IRemoteCallback iRemoteCallback = new IRemoteCallback.Stub() { // from class: com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver.1
                                public void sendResult(Bundle bundle) throws RemoteException {
                                    if (WallpaperManagerService.DEBUG) {
                                        Slog.d(WallpaperManagerService.TAG, "publish system wallpaper changed!");
                                    }
                                    WallpaperDestinationChangeHandler wallpaperDestinationChangeHandler2 = wallpaperDestinationChangeHandler;
                                    if (wallpaperDestinationChangeHandler2 != null) {
                                        wallpaperDestinationChangeHandler2.complete();
                                    }
                                    WallpaperManagerService.this.notifyWallpaperChanged(dataForEvent);
                                }
                            };
                            WallpaperManagerService wallpaperManagerService2 = WallpaperManagerService.this;
                            wallpaperManagerService2.bindWallpaperComponentLocked(wallpaperManagerService2.mImageWallpaper, true, false, dataForEvent, iRemoteCallback);
                        } else {
                            i2 = 0;
                        }
                        if (equals2) {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.v(WallpaperManagerService.TAG, "Lock screen wallpaper changed");
                            }
                            IRemoteCallback iRemoteCallback2 = new IRemoteCallback.Stub() { // from class: com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver.2
                                public void sendResult(Bundle bundle) throws RemoteException {
                                    if (WallpaperManagerService.DEBUG) {
                                        Slog.d(WallpaperManagerService.TAG, "publish lock wallpaper changed!");
                                    }
                                    WallpaperDestinationChangeHandler wallpaperDestinationChangeHandler2 = wallpaperDestinationChangeHandler;
                                    if (wallpaperDestinationChangeHandler2 != null) {
                                        wallpaperDestinationChangeHandler2.complete();
                                    }
                                    WallpaperManagerService.this.notifyWallpaperChanged(dataForEvent);
                                }
                            };
                            WallpaperManagerService wallpaperManagerService3 = WallpaperManagerService.this;
                            wallpaperManagerService3.bindWallpaperComponentLocked(wallpaperManagerService3.mImageWallpaper, true, false, dataForEvent, iRemoteCallback2);
                        } else {
                            if (z5) {
                                if (WallpaperManagerService.DEBUG) {
                                    Slog.v(WallpaperManagerService.TAG, "Lock screen wallpaper changed to same as home");
                                }
                                WallpaperData wallpaperData = (WallpaperData) WallpaperManagerService.this.mLockWallpaperMap.get(this.mWallpaper.userId);
                                if (wallpaperData != null) {
                                    WallpaperManagerService.this.detachWallpaperLocked(wallpaperData);
                                }
                                WallpaperManagerService.this.mLockWallpaperMap.remove(dataForEvent.userId);
                            }
                            WallpaperManagerService.this.saveSettingsLocked(dataForEvent.userId);
                            if (equals2 && !equals) {
                                WallpaperManagerService.this.notifyWallpaperChanged(dataForEvent);
                            }
                            if (i2 == 0) {
                                WallpaperManagerService.this.notifyWallpaperColorsChanged(dataForEvent, i2);
                                return;
                            }
                            return;
                        }
                        i2 |= 2;
                        WallpaperManagerService.this.saveSettingsLocked(dataForEvent.userId);
                        if (equals2) {
                            WallpaperManagerService.this.notifyWallpaperChanged(dataForEvent);
                        }
                        if (i2 == 0) {
                        }
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x026d  */
        /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void updateWallpapersLegacy(int i, String str) {
            WallpaperData wallpaperData;
            int i2 = 0;
            boolean z = i == 128;
            boolean z2 = i == 8 || z;
            File file = new File(this.mWallpaperDir, str);
            boolean equals = this.mWallpaperFile.equals(file);
            boolean equals2 = this.mWallpaperLockFile.equals(file);
            final WallpaperData dataForEvent = dataForEvent(equals, equals2);
            if (WallpaperManagerService.DEBUG) {
                Slog.v(WallpaperManagerService.TAG, "Wallpaper file change: evt=" + i + " path=" + str + " sys=" + equals + " lock=" + equals2 + " imagePending=" + dataForEvent.imageWallpaperPending + " mWhich=0x" + Integer.toHexString(dataForEvent.mWhich) + " written=" + z2);
            }
            if (z && equals2) {
                if (WallpaperManagerService.DEBUG) {
                    Slog.i(WallpaperManagerService.TAG, "Sys -> lock MOVED_TO");
                }
                SELinux.restorecon(file);
                WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().setIsMainDisplayWallpaperChangeLocked(dataForEvent, dataForEvent.mWhich);
                WallpaperManagerService.this.notifyLockWallpaperChanged();
                WallpaperManagerService.this.notifyWallpaperColorsChanged(dataForEvent, 2);
                return;
            }
            synchronized (WallpaperManagerService.this.mLock) {
                if (equals || equals2) {
                    if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().ignoreFileEventForCopyLocked(dataForEvent, i)) {
                        return;
                    }
                    if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().canNotifySysChangedLocked(dataForEvent, equals)) {
                        WallpaperManagerService.this.notifyCallbacksLocked(dataForEvent);
                    }
                    if ((dataForEvent.wallpaperComponent == null || i != 8 || dataForEvent.imageWallpaperPending) && z2) {
                        if (WallpaperManagerService.DEBUG) {
                            Slog.v(WallpaperManagerService.TAG, "Wallpaper written; generating crop");
                        }
                        SELinux.restorecon(file);
                        WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWrittenEventStart(dataForEvent);
                        if (z) {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.v(WallpaperManagerService.TAG, "moved-to, therefore restore; reloading metadata");
                            }
                            if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().loadSettingsLocked(dataForEvent.userId, true, dataForEvent.mWallpaperDataExt.getPhysicalDisplayId(), 3)) {
                                WallpaperManagerService.this.loadSettingsLocked(dataForEvent.userId, true, 3);
                            }
                        }
                        WallpaperManagerService.this.mWallpaperCropper.generateCrop(dataForEvent);
                        if (WallpaperManagerService.DEBUG) {
                            Slog.v(WallpaperManagerService.TAG, "Crop done; invoking completion callback");
                        }
                        dataForEvent.imageWallpaperPending = false;
                        if (equals) {
                            WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().checkSysChangedWhenSysAndLockIsLive(dataForEvent, WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, dataForEvent.mWallpaperDataExt.getPhysicalDisplayId(), WallpaperManagerService.this.mLockWallpaperMap));
                            IRemoteCallback iRemoteCallback = new IRemoteCallback.Stub() { // from class: com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver.3
                                public void sendResult(Bundle bundle) throws RemoteException {
                                    Slog.d(WallpaperManagerService.TAG, "publish system wallpaper changed!");
                                    WallpaperManagerService.this.notifyWallpaperChanged(dataForEvent);
                                }
                            };
                            WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().updateWallpaperBitmap();
                            WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().copySysWallpaperToOtherPhysicalDisplaysLocked(dataForEvent);
                            if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().bindWallpaperComponentOnImageChangedLocked(dataForEvent, iRemoteCallback)) {
                                wallpaperData = dataForEvent;
                            } else {
                                WallpaperManagerService wallpaperManagerService = WallpaperManagerService.this;
                                wallpaperData = dataForEvent;
                                wallpaperManagerService.bindWallpaperComponentLocked(wallpaperManagerService.mImageWallpaper, true, false, dataForEvent, iRemoteCallback);
                            }
                            if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().isCurPhysicalDisplayWallpaperChangeLocked(wallpaperData)) {
                                i2 = 1;
                            }
                        } else {
                            wallpaperData = dataForEvent;
                        }
                        if (equals2 || (wallpaperData.mWhich & 2) != 0) {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.i(WallpaperManagerService.TAG, "Lock-relevant wallpaper changed");
                            }
                            if (!equals2) {
                                WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().copySysWallpaperToLockLocked(wallpaperData, true, WallpaperManagerService.this.mLockWallpaperMap);
                            }
                            WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().copyLockWallpaperToOtherPhysicalDisplaysLocked(wallpaperData, equals2);
                            if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().isCurPhysicalDisplayWallpaperChangeLocked(wallpaperData)) {
                                Slog.i(WallpaperManagerService.TAG, "onEvent notifyLockWallpaperChanged");
                                WallpaperManagerService.this.notifyLockWallpaperChanged();
                                i2 |= 2;
                            } else {
                                WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().setIsMainDisplayWallpaperChangeLocked(wallpaperData, wallpaperData.mWhich);
                            }
                        }
                        if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLockedForAffectedPhysicalDisplays(wallpaperData.userId, wallpaperData.mWhich)) {
                            WallpaperManagerService.this.saveSettingsLocked(wallpaperData.userId);
                        }
                        if (equals2 && !equals) {
                            WallpaperManagerService.this.notifyWallpaperChanged(wallpaperData);
                        }
                        WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWrittenEventEnd(wallpaperData);
                        if (i2 == 0) {
                            WallpaperManagerService.this.notifyWallpaperColorsChanged(wallpaperData, i2);
                            return;
                        }
                        return;
                    }
                }
                wallpaperData = dataForEvent;
                if (i2 == 0) {
                }
            }
        }

        @Override // android.os.FileObserver
        public void onEvent(int i, String str) {
            if (str == null) {
                return;
            }
            if (WallpaperManagerService.this.mIsLockscreenLiveWallpaperEnabled) {
                updateWallpapers(i, str);
            } else {
                updateWallpapersLegacy(i, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWallpaperChanged(WallpaperData wallpaperData) {
        IWallpaperManagerCallback iWallpaperManagerCallback = wallpaperData.setComplete;
        if (iWallpaperManagerCallback != null) {
            try {
                iWallpaperManagerCallback.onWallpaperChanged();
            } catch (RemoteException e) {
                Slog.w(TAG, "onWallpaperChanged threw an exception", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyLockWallpaperChanged() {
        this.mWallpaperManagerServiceWrapper.getExtImpl().onLockWallpaperChanged(this.mContext, this.mCurrentUserId);
        IWallpaperManagerCallback iWallpaperManagerCallback = this.mKeyguardListener;
        if (iWallpaperManagerCallback != null) {
            try {
                iWallpaperManagerCallback.onWallpaperChanged();
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to notify keyguard callback about wallpaper changes", e);
            }
        }
    }

    void notifyWallpaperColorsChanged(final WallpaperData wallpaperData, final int i) {
        if (DEBUG) {
            Slog.i(TAG, "Notifying wallpaper colors changed");
        }
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().notifyWallpaperColorsChanged(wallpaperData, i)) {
            return;
        }
        WallpaperConnection wallpaperConnection = wallpaperData.connection;
        if (wallpaperConnection != null) {
            wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WallpaperManagerService.this.lambda$notifyWallpaperColorsChanged$0(wallpaperData, i, (WallpaperManagerService.DisplayConnector) obj);
                }
            });
        } else {
            notifyWallpaperColorsChangedOnDisplay(wallpaperData, i, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyWallpaperColorsChanged$0(WallpaperData wallpaperData, int i, DisplayConnector displayConnector) {
        notifyWallpaperColorsChangedOnDisplay(wallpaperData, i, displayConnector.mDisplayId);
    }

    private RemoteCallbackList<IWallpaperManagerCallback> getWallpaperCallbacks(int i, int i2) {
        SparseArray<RemoteCallbackList<IWallpaperManagerCallback>> sparseArray = this.mColorsChangedListeners.get(i);
        if (sparseArray != null) {
            return sparseArray.get(i2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void notifyWallpaperColorsChangedOnDisplay(WallpaperData wallpaperData, int i, int i2) {
        boolean z;
        synchronized (this.mLock) {
            RemoteCallbackList<IWallpaperManagerCallback> wallpaperCallbacks = getWallpaperCallbacks(wallpaperData.userId, i2);
            RemoteCallbackList<IWallpaperManagerCallback> wallpaperCallbacks2 = getWallpaperCallbacks(-1, i2);
            if (emptyCallbackList(wallpaperCallbacks) && emptyCallbackList(wallpaperCallbacks2)) {
                return;
            }
            if (DEBUG) {
                Slog.v(TAG, "notifyWallpaperColorsChangedOnDisplay " + i);
            }
            if (wallpaperData.primaryColors != null && !wallpaperData.mIsColorExtractedFromDim) {
                z = false;
                if (z) {
                    extractColors(wallpaperData);
                }
                notifyColorListeners(getAdjustedWallpaperColorsOnDimming(wallpaperData), i, wallpaperData.userId, i2);
            }
            z = true;
            if (z) {
            }
            notifyColorListeners(getAdjustedWallpaperColorsOnDimming(wallpaperData), i, wallpaperData.userId, i2);
        }
    }

    private static <T extends IInterface> boolean emptyCallbackList(RemoteCallbackList<T> remoteCallbackList) {
        return remoteCallbackList == null || remoteCallbackList.getRegisteredCallbackCount() == 0;
    }

    private void notifyColorListeners(WallpaperColors wallpaperColors, int i, int i2, int i3) {
        IWallpaperManagerCallback iWallpaperManagerCallback;
        int i4;
        ArrayList arrayList = new ArrayList();
        synchronized (this.mLock) {
            RemoteCallbackList<IWallpaperManagerCallback> wallpaperCallbacks = getWallpaperCallbacks(i2, i3);
            RemoteCallbackList<IWallpaperManagerCallback> wallpaperCallbacks2 = getWallpaperCallbacks(-1, i3);
            iWallpaperManagerCallback = this.mKeyguardListener;
            if (wallpaperCallbacks != null) {
                int beginBroadcast = wallpaperCallbacks.beginBroadcast();
                for (int i5 = 0; i5 < beginBroadcast; i5++) {
                    arrayList.add(wallpaperCallbacks.getBroadcastItem(i5));
                }
                wallpaperCallbacks.finishBroadcast();
            }
            if (wallpaperCallbacks2 != null) {
                int beginBroadcast2 = wallpaperCallbacks2.beginBroadcast();
                for (int i6 = 0; i6 < beginBroadcast2; i6++) {
                    arrayList.add(wallpaperCallbacks2.getBroadcastItem(i6));
                }
                wallpaperCallbacks2.finishBroadcast();
            }
        }
        int size = arrayList.size();
        for (i4 = 0; i4 < size; i4++) {
            try {
                ((IWallpaperManagerCallback) arrayList.get(i4)).onWallpaperColorsChanged(wallpaperColors, i, i2);
            } catch (RemoteException e) {
                Slog.w(TAG, "onWallpaperColorsChanged() threw an exception", e);
            }
        }
        if (iWallpaperManagerCallback == null || i3 != 0) {
            return;
        }
        try {
            iWallpaperManagerCallback.onWallpaperColorsChanged(wallpaperColors, i, i2);
        } catch (RemoteException e2) {
            Slog.w(TAG, "keyguardListener.onWallpaperColorsChanged threw an exception", e2);
        }
    }

    private void extractColors(WallpaperData wallpaperData) {
        boolean z;
        boolean z2;
        WallpaperColors wallpaperColors;
        String str;
        int i;
        float f;
        File file;
        synchronized (this.mLock) {
            z = false;
            wallpaperData.mIsColorExtractedFromDim = false;
        }
        if (wallpaperData.equals(this.mFallbackWallpaper)) {
            synchronized (this.mLock) {
                if (this.mFallbackWallpaper.primaryColors != null) {
                    return;
                }
                WallpaperColors extractDefaultImageWallpaperColors = extractDefaultImageWallpaperColors(wallpaperData);
                synchronized (this.mLock) {
                    this.mFallbackWallpaper.primaryColors = extractDefaultImageWallpaperColors;
                }
                return;
            }
        }
        synchronized (this.mLock) {
            if (!this.mImageWallpaper.equals(wallpaperData.wallpaperComponent) && wallpaperData.wallpaperComponent != null) {
                z2 = false;
                wallpaperColors = null;
                if (!z2 && (file = wallpaperData.cropFile) != null && file.exists()) {
                    str = wallpaperData.cropFile.getAbsolutePath();
                } else {
                    if (z2 && !wallpaperData.cropExists() && !wallpaperData.sourceExists()) {
                        z = true;
                    }
                    str = null;
                }
                i = wallpaperData.wallpaperId;
                f = wallpaperData.mWallpaperDimAmount;
            }
            z2 = true;
            wallpaperColors = null;
            if (!z2) {
            }
            if (z2) {
                z = true;
            }
            str = null;
            i = wallpaperData.wallpaperId;
            f = wallpaperData.mWallpaperDimAmount;
        }
        if (str != null) {
            Bitmap decodeFile = BitmapFactory.decodeFile(str);
            if (decodeFile != null) {
                WallpaperColors fromBitmap = WallpaperColors.fromBitmap(decodeFile, f);
                decodeFile.recycle();
                wallpaperColors = fromBitmap;
            }
        } else if (z) {
            wallpaperColors = extractDefaultImageWallpaperColors(wallpaperData);
        }
        if (wallpaperColors == null) {
            Slog.w(TAG, "Cannot extract colors because wallpaper could not be read.");
            return;
        }
        synchronized (this.mLock) {
            if (wallpaperData.wallpaperId == i) {
                wallpaperData.primaryColors = wallpaperColors;
                this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLocked(wallpaperData.userId, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId());
            } else {
                Slog.w(TAG, "Not setting primary colors since wallpaper changed");
            }
        }
    }

    private WallpaperColors extractDefaultImageWallpaperColors(WallpaperData wallpaperData) {
        InputStream openDefaultWallpaper;
        if (DEBUG) {
            Slog.d(TAG, "Extract default image wallpaper colors");
        }
        synchronized (this.mLock) {
            WallpaperColors wallpaperColors = this.mCacheDefaultImageWallpaperColors;
            if (wallpaperColors != null) {
                return wallpaperColors;
            }
            float f = wallpaperData.mWallpaperDimAmount;
            WallpaperColors wallpaperColors2 = null;
            try {
                openDefaultWallpaper = WallpaperManager.openDefaultWallpaper(this.mContext, 1);
            } catch (IOException e) {
                Slog.w(TAG, "Can't close default wallpaper stream", e);
            } catch (OutOfMemoryError e2) {
                Slog.w(TAG, "Can't decode default wallpaper stream", e2);
            }
            try {
                if (openDefaultWallpaper == null) {
                    Slog.w(TAG, "Can't open default wallpaper stream");
                    if (openDefaultWallpaper != null) {
                        openDefaultWallpaper.close();
                    }
                    return null;
                }
                Bitmap decodeStream = BitmapFactory.decodeStream(openDefaultWallpaper, null, new BitmapFactory.Options());
                if (decodeStream != null) {
                    wallpaperColors2 = WallpaperColors.fromBitmap(decodeStream, f);
                    decodeStream.recycle();
                }
                openDefaultWallpaper.close();
                if (wallpaperColors2 == null) {
                    Slog.e(TAG, "Extract default image wallpaper colors failed");
                } else {
                    synchronized (this.mLock) {
                        this.mCacheDefaultImageWallpaperColors = wallpaperColors2;
                    }
                }
                return wallpaperColors2;
            } catch (Throwable th) {
                if (openDefaultWallpaper != null) {
                    try {
                        openDefaultWallpaper.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean supportsMultiDisplay(WallpaperConnection wallpaperConnection) {
        if (wallpaperConnection == null) {
            return false;
        }
        WallpaperInfo wallpaperInfo = wallpaperConnection.mInfo;
        return wallpaperInfo == null || wallpaperInfo.supportsMultipleDisplays();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFallbackConnection() {
        WallpaperData wallpaperData;
        WallpaperData wallpaperData2 = this.mLastWallpaper;
        if (wallpaperData2 == null || (wallpaperData = this.mFallbackWallpaper) == null) {
            return;
        }
        WallpaperConnection wallpaperConnection = wallpaperData2.connection;
        final WallpaperConnection wallpaperConnection2 = wallpaperData.connection;
        if (wallpaperConnection2 == null) {
            Slog.w(TAG, "Fallback wallpaper connection has not been created yet!!");
            return;
        }
        if (supportsMultiDisplay(wallpaperConnection)) {
            if (wallpaperConnection2.mDisplayConnector.size() != 0) {
                wallpaperConnection2.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda26
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WallpaperManagerService.lambda$updateFallbackConnection$1(WallpaperManagerService.WallpaperConnection.this, (WallpaperManagerService.DisplayConnector) obj);
                    }
                });
                wallpaperConnection2.mDisplayConnector.clear();
                return;
            }
            return;
        }
        wallpaperConnection2.appendConnectorWithCondition(new Predicate() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda27
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$updateFallbackConnection$2;
                lambda$updateFallbackConnection$2 = WallpaperManagerService.this.lambda$updateFallbackConnection$2(wallpaperConnection2, (Display) obj);
                return lambda$updateFallbackConnection$2;
            }
        });
        wallpaperConnection2.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WallpaperManagerService.this.lambda$updateFallbackConnection$3(wallpaperConnection2, (WallpaperManagerService.DisplayConnector) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateFallbackConnection$1(WallpaperConnection wallpaperConnection, DisplayConnector displayConnector) {
        if (displayConnector.mEngine != null) {
            displayConnector.disconnectLocked(wallpaperConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateFallbackConnection$2(WallpaperConnection wallpaperConnection, Display display) {
        return this.mWallpaperDisplayHelper.isUsableDisplay(display, wallpaperConnection.mClientUid) && display.getDisplayId() != 0 && this.mWallpaperManagerServiceWrapper.getExtImpl().isAvailableFallbackDisplay(display) && !wallpaperConnection.containsDisplay(display.getDisplayId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFallbackConnection$3(WallpaperConnection wallpaperConnection, DisplayConnector displayConnector) {
        if (displayConnector.mEngine == null) {
            displayConnector.connectLocked(wallpaperConnection, this.mFallbackWallpaper);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class DisplayConnector {
        boolean mDimensionsChanged;
        final int mDisplayId;
        IWallpaperEngine mEngine;
        boolean mPaddingChanged;
        final Binder mToken = new Binder();

        DisplayConnector(int i) {
            this.mDisplayId = i;
        }

        void ensureStatusHandled() {
            WallpaperDisplayHelper.DisplayData displayDataOrCreate = WallpaperManagerService.this.mWallpaperDisplayHelper.getDisplayDataOrCreate(this.mDisplayId);
            if (this.mDimensionsChanged) {
                try {
                    this.mEngine.setDesiredSize(displayDataOrCreate.mWidth, displayDataOrCreate.mHeight);
                } catch (RemoteException e) {
                    Slog.w(WallpaperManagerService.TAG, "Failed to set wallpaper dimensions", e);
                }
                this.mDimensionsChanged = false;
            }
            if (this.mPaddingChanged) {
                try {
                    this.mEngine.setDisplayPadding(displayDataOrCreate.mPadding);
                } catch (RemoteException e2) {
                    Slog.w(WallpaperManagerService.TAG, "Failed to set wallpaper padding", e2);
                }
                this.mPaddingChanged = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v12 */
        /* JADX WARN: Type inference failed for: r2v13 */
        /* JADX WARN: Type inference failed for: r2v14 */
        /* JADX WARN: Type inference failed for: r2v5 */
        /* JADX WARN: Type inference failed for: r2v6, types: [boolean, int] */
        public void connectLocked(WallpaperConnection wallpaperConnection, WallpaperData wallpaperData) {
            ?? r2;
            ComponentName componentName;
            if (wallpaperConnection.mService == null) {
                Slog.w(WallpaperManagerService.TAG, "WallpaperService is not connected yet");
                return;
            }
            TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(WallpaperManagerService.TAG);
            timingsTraceAndSlog.traceBegin("WPMS.connectLocked-" + wallpaperData.wallpaperComponent);
            if (WallpaperManagerService.DEBUG) {
                Slog.v(WallpaperManagerService.TAG, "Adding window token: " + this.mToken);
            }
            WallpaperManagerService.this.mWindowManagerInternal.addWindowToken(this.mToken, 2013, this.mDisplayId, (Bundle) null);
            WallpaperManagerService.this.mWindowManagerInternal.setWallpaperShowWhenLocked(this.mToken, (wallpaperData.mWhich & 2) != 0);
            WallpaperDisplayHelper.DisplayData displayDataOrCreate = WallpaperManagerService.this.mWallpaperDisplayHelper.getDisplayDataOrCreate(this.mDisplayId);
            try {
                try {
                    componentName = null;
                    try {
                        wallpaperConnection.mService.attach(wallpaperConnection, this.mToken, 2013, false, displayDataOrCreate.mWidth, displayDataOrCreate.mHeight, displayDataOrCreate.mPadding, this.mDisplayId, wallpaperData.mWhich);
                        r2 = 0;
                    } catch (RemoteException e) {
                        e = e;
                        r2 = 0;
                    }
                    try {
                        WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceAttachedLocked(false, 0, null);
                    } catch (RemoteException e2) {
                        e = e2;
                        Slog.w(WallpaperManagerService.TAG, "Failed attaching wallpaper on display", e);
                        if (!wallpaperData.wallpaperUpdating && wallpaperConnection.getConnectedEngineSize() == 0) {
                            WallpaperManagerService.this.bindWallpaperComponentLocked(WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceAttachedLocked(true, wallpaperData.userId, wallpaperData.wallpaperComponent) ? WallpaperManagerService.this.mImageWallpaper : componentName, false, false, wallpaperData, null);
                        } else {
                            WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceAttachedLocked(r2, r2, componentName);
                        }
                        timingsTraceAndSlog.traceEnd();
                    }
                } catch (RemoteException e3) {
                    e = e3;
                    r2 = 0;
                    componentName = null;
                }
            } catch (RemoteException e4) {
                e = e4;
                r2 = 0;
                componentName = null;
            }
            timingsTraceAndSlog.traceEnd();
        }

        void disconnectLocked(WallpaperConnection wallpaperConnection) {
            if (WallpaperManagerService.DEBUG) {
                Slog.v(WallpaperManagerService.TAG, "Removing window token: " + this.mToken);
            }
            WallpaperManagerService.this.mWindowManagerInternal.removeWindowToken(this.mToken, false, this.mDisplayId);
            try {
                IWallpaperService iWallpaperService = wallpaperConnection.mService;
                if (iWallpaperService != null) {
                    iWallpaperService.detach(this.mToken);
                }
            } catch (RemoteException e) {
                Slog.w(WallpaperManagerService.TAG, "connection.mService.destroy() threw a RemoteException", e);
            }
            this.mEngine = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WallpaperConnection extends IWallpaperConnection.Stub implements ServiceConnection {
        private static final long WALLPAPER_RECONNECT_TIMEOUT_MS = 10000;
        final int mClientUid;
        final WallpaperInfo mInfo;
        IRemoteCallback mReply;
        IWallpaperService mService;
        WallpaperData mWallpaper;
        private final SparseArray<DisplayConnector> mDisplayConnector = new SparseArray<>();
        private Runnable mResetRunnable = new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WallpaperManagerService.WallpaperConnection.this.lambda$new$0();
            }
        };
        private Runnable mTryToRebindRunnable = new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                WallpaperManagerService.WallpaperConnection.this.tryToRebind();
            }
        };
        private Runnable mDisconnectRunnable = new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                WallpaperManagerService.WallpaperConnection.this.lambda$new$5();
            }
        };

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            synchronized (WallpaperManagerService.this.mLock) {
                if (WallpaperManagerService.this.mShuttingDown) {
                    Slog.i(WallpaperManagerService.TAG, "Ignoring relaunch timeout during shutdown");
                    return;
                }
                WallpaperData wallpaperData = this.mWallpaper;
                if (!wallpaperData.wallpaperUpdating && wallpaperData.userId == WallpaperManagerService.this.mCurrentUserId) {
                    Slog.w(WallpaperManagerService.TAG, "Wallpaper reconnect timed out for " + this.mWallpaper.wallpaperComponent + ", reverting to built-in wallpaper!");
                    IWallpaperManagerServiceExt extImpl = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl();
                    WallpaperData wallpaperData2 = this.mWallpaper;
                    if (!extImpl.clearWallpaperLockedForComponent(true, 1, wallpaperData2.userId, null, wallpaperData2.wallpaperComponent)) {
                        WallpaperManagerService.this.clearWallpaperLocked(true, 1, this.mWallpaper.userId, null);
                    }
                }
            }
        }

        WallpaperConnection(WallpaperInfo wallpaperInfo, WallpaperData wallpaperData, int i) {
            this.mInfo = wallpaperInfo;
            this.mWallpaper = wallpaperData;
            this.mClientUid = i;
            initDisplayState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initDisplayState() {
            if (this.mWallpaper.equals(WallpaperManagerService.this.mFallbackWallpaper)) {
                return;
            }
            if (WallpaperManagerService.this.supportsMultiDisplay(this)) {
                appendConnectorWithCondition(new Predicate() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda6
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$initDisplayState$1;
                        lambda$initDisplayState$1 = WallpaperManagerService.WallpaperConnection.this.lambda$initDisplayState$1((Display) obj);
                        return lambda$initDisplayState$1;
                    }
                });
            } else {
                this.mDisplayConnector.append(0, new DisplayConnector(0));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$initDisplayState$1(Display display) {
            return WallpaperManagerService.this.mWallpaperDisplayHelper.isUsableDisplay(display, this.mClientUid);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void appendConnectorWithCondition(Predicate<Display> predicate) {
            for (Display display : WallpaperManagerService.this.mWallpaperDisplayHelper.getDisplays(this.mWallpaper)) {
                if (predicate.test(display)) {
                    int displayId = display.getDisplayId();
                    if (this.mDisplayConnector.get(displayId) == null) {
                        this.mDisplayConnector.append(displayId, new DisplayConnector(displayId));
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void forEachDisplayConnector(Consumer<DisplayConnector> consumer) {
            if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().forEachAvailableDisplayConnector(this.mDisplayConnector, this, consumer)) {
                return;
            }
            for (int size = this.mDisplayConnector.size() - 1; size >= 0; size--) {
                consumer.accept(this.mDisplayConnector.valueAt(size));
            }
        }

        int getConnectedEngineSize() {
            int i = 0;
            for (int size = this.mDisplayConnector.size() - 1; size >= 0; size--) {
                if (this.mDisplayConnector.valueAt(size).mEngine != null) {
                    i++;
                }
            }
            return i;
        }

        DisplayConnector getDisplayConnectorOrCreate(int i) {
            DisplayConnector displayConnector = this.mDisplayConnector.get(i);
            if (displayConnector != null || !WallpaperManagerService.this.mWallpaperDisplayHelper.isUsableDisplay(i, this.mClientUid)) {
                return displayConnector;
            }
            DisplayConnector displayConnector2 = new DisplayConnector(i);
            this.mDisplayConnector.append(i, displayConnector2);
            return displayConnector2;
        }

        boolean containsDisplay(int i) {
            return this.mDisplayConnector.get(i) != null;
        }

        void removeDisplayConnector(int i) {
            if (this.mDisplayConnector.get(i) != null) {
                this.mDisplayConnector.remove(i);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(WallpaperManagerService.TAG);
            timingsTraceAndSlog.traceBegin("WPMS.onServiceConnected-" + componentName);
            synchronized (WallpaperManagerService.this.mLock) {
                if (this.mWallpaper.connection == this) {
                    this.mService = IWallpaperService.Stub.asInterface(iBinder);
                    if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().needAttachService(this.mWallpaper)) {
                        WallpaperManagerService.this.attachServiceLocked(this, this.mWallpaper);
                    }
                    if (!this.mWallpaper.equals(WallpaperManagerService.this.mFallbackWallpaper)) {
                        IWallpaperManagerServiceExt extImpl = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl();
                        WallpaperData wallpaperData = this.mWallpaper;
                        if (!extImpl.saveSettingsLockedOnServiceConnected(wallpaperData.userId, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId())) {
                            WallpaperManagerService.this.saveSettingsLocked(this.mWallpaper.userId);
                        }
                    }
                    FgThread.getHandler().removeCallbacks(this.mResetRunnable);
                    WallpaperManagerService.this.mContext.getMainThreadHandler().removeCallbacks(this.mTryToRebindRunnable);
                    WallpaperManagerService.this.mContext.getMainThreadHandler().removeCallbacks(this.mDisconnectRunnable);
                }
            }
            timingsTraceAndSlog.traceEnd();
        }

        public void onLocalWallpaperColorsChanged(final RectF rectF, final WallpaperColors wallpaperColors, final int i) {
            forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WallpaperManagerService.WallpaperConnection.this.lambda$onLocalWallpaperColorsChanged$3(rectF, wallpaperColors, i, (WallpaperManagerService.DisplayConnector) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocalWallpaperColorsChanged$3(final RectF rectF, final WallpaperColors wallpaperColors, int i, DisplayConnector displayConnector) {
            Consumer<ILocalWallpaperColorConsumer> consumer = new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WallpaperManagerService.WallpaperConnection.lambda$onLocalWallpaperColorsChanged$2(rectF, wallpaperColors, (ILocalWallpaperColorConsumer) obj);
                }
            };
            synchronized (WallpaperManagerService.this.mLock) {
                WallpaperManagerService.this.mLocalColorRepo.forEachCallback(consumer, rectF, i);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onLocalWallpaperColorsChanged$2(RectF rectF, WallpaperColors wallpaperColors, ILocalWallpaperColorConsumer iLocalWallpaperColorConsumer) {
            try {
                iLocalWallpaperColorConsumer.onColorsChanged(rectF, wallpaperColors);
            } catch (RemoteException e) {
                Slog.w(WallpaperManagerService.TAG, "Failed to notify local color callbacks", e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (WallpaperManagerService.this.mLock) {
                Slog.w(WallpaperManagerService.TAG, "Wallpaper service gone: " + componentName);
                if (!Objects.equals(componentName, this.mWallpaper.wallpaperComponent)) {
                    Slog.e(WallpaperManagerService.TAG, "Does not match expected wallpaper component " + this.mWallpaper.wallpaperComponent);
                }
                this.mService = null;
                forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WallpaperManagerService.WallpaperConnection.this.lambda$onServiceDisconnected$4((WallpaperManagerService.DisplayConnector) obj);
                    }
                });
                if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceDisconnected(this.mWallpaper)) {
                    WallpaperData wallpaperData = this.mWallpaper;
                    if (wallpaperData.connection == this && !wallpaperData.wallpaperUpdating) {
                        WallpaperManagerService.this.mContext.getMainThreadHandler().postDelayed(this.mDisconnectRunnable, 1000L);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$4(DisplayConnector displayConnector) {
            WallpaperManagerService.this.mWallpaperManagerServiceExt.removeEngineIfDisconnected(displayConnector);
        }

        private void scheduleTimeoutLocked() {
            Handler handler = FgThread.getHandler();
            handler.removeCallbacks(this.mResetRunnable);
            handler.postDelayed(this.mResetRunnable, 10000L);
            Slog.i(WallpaperManagerService.TAG, "Started wallpaper reconnect timeout for " + this.mWallpaper.wallpaperComponent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void tryToRebind() {
            synchronized (WallpaperManagerService.this.mLock) {
                WallpaperData wallpaperData = this.mWallpaper;
                if (wallpaperData.wallpaperUpdating) {
                    return;
                }
                ComponentName componentName = wallpaperData.wallpaperComponent;
                if (WallpaperManagerService.this.bindWallpaperComponentLocked(componentName, true, false, wallpaperData, null)) {
                    this.mWallpaper.connection.scheduleTimeoutLocked();
                } else if (SystemClock.uptimeMillis() - this.mWallpaper.lastDiedTime < 10000) {
                    Slog.w(WallpaperManagerService.TAG, "Rebind fail! Try again later");
                    WallpaperManagerService.this.mContext.getMainThreadHandler().postDelayed(this.mTryToRebindRunnable, 1000L);
                } else {
                    Slog.w(WallpaperManagerService.TAG, "Reverting to built-in wallpaper!");
                    IWallpaperManagerServiceExt extImpl = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl();
                    WallpaperData wallpaperData2 = this.mWallpaper;
                    if (!extImpl.clearWallpaperLockedForComponent(true, 1, wallpaperData2.userId, null, wallpaperData2.wallpaperComponent)) {
                        WallpaperManagerService.this.clearWallpaperLocked(true, 1, this.mWallpaper.userId, null);
                    }
                    String flattenToString = componentName.flattenToString();
                    EventLog.writeEvent(33000, flattenToString.substring(0, Math.min(flattenToString.length(), 128)));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5() {
            synchronized (WallpaperManagerService.this.mLock) {
                WallpaperData wallpaperData = this.mWallpaper;
                if (this == wallpaperData.connection) {
                    ComponentName componentName = wallpaperData.wallpaperComponent;
                    if (!wallpaperData.wallpaperUpdating && wallpaperData.userId == WallpaperManagerService.this.mCurrentUserId && !Objects.equals(WallpaperManagerService.this.mImageWallpaper, componentName)) {
                        long j = this.mWallpaper.lastDiedTime;
                        if (j != 0 && j + 10000 > SystemClock.uptimeMillis()) {
                            if (WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().delayRebindOnCrashTimeout(this.mWallpaper, this)) {
                                return;
                            }
                            Slog.w(WallpaperManagerService.TAG, "Reverting to built-in wallpaper!");
                            IWallpaperManagerServiceExt extImpl = WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl();
                            WallpaperData wallpaperData2 = this.mWallpaper;
                            if (!extImpl.clearWallpaperLockedForComponent(true, 1, wallpaperData2.userId, null, wallpaperData2.wallpaperComponent)) {
                                WallpaperManagerService.this.clearWallpaperLocked(true, 1, this.mWallpaper.userId, null);
                            }
                        } else {
                            this.mWallpaper.lastDiedTime = SystemClock.uptimeMillis();
                            WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().updateWallpaperBeforeTryingToRebind(this.mWallpaper);
                            tryToRebind();
                        }
                    }
                } else {
                    Slog.i(WallpaperManagerService.TAG, "Wallpaper changed during disconnect tracking; ignoring");
                }
            }
        }

        public void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i) {
            synchronized (WallpaperManagerService.this.mLock) {
                if (WallpaperManagerService.this.mImageWallpaper.equals(this.mWallpaper.wallpaperComponent)) {
                    return;
                }
                int i2 = WallpaperManagerService.this.mIsLockscreenLiveWallpaperEnabled ? this.mWallpaper.mWhich : 1;
                this.mWallpaper.primaryColors = wallpaperColors;
                WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().copyWallpaperColorsToOtherPhysicalDisplaysLocked(this.mWallpaper);
                if (i == 0 && WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, WallpaperManagerService.this.mLockWallpaperMap).get(this.mWallpaper.userId) == null) {
                    i2 |= 2;
                }
                if (i2 != 0) {
                    WallpaperManagerService.this.notifyWallpaperColorsChangedOnDisplay(this.mWallpaper, i2, i);
                }
            }
        }

        public void attachEngine(IWallpaperEngine iWallpaperEngine, int i) {
            synchronized (WallpaperManagerService.this.mLock) {
                DisplayConnector displayConnectorOrCreate = getDisplayConnectorOrCreate(i);
                if (displayConnectorOrCreate == null) {
                    throw new IllegalStateException("Connector has already been destroyed");
                }
                displayConnectorOrCreate.mEngine = iWallpaperEngine;
                displayConnectorOrCreate.ensureStatusHandled();
                WallpaperInfo wallpaperInfo = this.mInfo;
                if (wallpaperInfo != null && wallpaperInfo.supportsAmbientMode() && i == 0) {
                    try {
                        displayConnectorOrCreate.mEngine.setInAmbientMode(WallpaperManagerService.this.mInAmbientMode, 0L);
                    } catch (RemoteException e) {
                        Slog.w(WallpaperManagerService.TAG, "Failed to set ambient mode state", e);
                    }
                }
                try {
                    displayConnectorOrCreate.mEngine.requestWallpaperColors();
                } catch (RemoteException e2) {
                    Slog.w(WallpaperManagerService.TAG, "Failed to request wallpaper colors", e2);
                }
                List<RectF> areasByDisplayId = WallpaperManagerService.this.mLocalColorRepo.getAreasByDisplayId(i);
                if (areasByDisplayId != null && areasByDisplayId.size() != 0) {
                    try {
                        displayConnectorOrCreate.mEngine.addLocalColorsAreas(areasByDisplayId);
                    } catch (RemoteException e3) {
                        Slog.w(WallpaperManagerService.TAG, "Failed to register local colors areas", e3);
                    }
                }
                float f = this.mWallpaper.mWallpaperDimAmount;
                if (f != 0.0f) {
                    try {
                        displayConnectorOrCreate.mEngine.applyDimming(f);
                    } catch (RemoteException e4) {
                        Slog.w(WallpaperManagerService.TAG, "Failed to dim wallpaper", e4);
                    }
                }
            }
        }

        public void engineShown(IWallpaperEngine iWallpaperEngine) {
            synchronized (WallpaperManagerService.this.mLock) {
                if (this.mReply != null) {
                    TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(WallpaperManagerService.TAG);
                    timingsTraceAndSlog.traceBegin("WPMS.mReply.sendResult");
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        try {
                            this.mReply.sendResult((Bundle) null);
                        } catch (RemoteException e) {
                            Slog.d(WallpaperManagerService.TAG, "Failed to send callback!", e);
                        }
                        timingsTraceAndSlog.traceEnd();
                        this.mReply = null;
                    } finally {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                    }
                }
            }
        }

        public ParcelFileDescriptor setWallpaper(String str) {
            synchronized (WallpaperManagerService.this.mLock) {
                WallpaperData wallpaperData = this.mWallpaper;
                if (wallpaperData.connection != this) {
                    return null;
                }
                return WallpaperManagerService.this.updateWallpaperBitmapLocked(str, wallpaperData, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WallpaperDestinationChangeHandler {
        final WallpaperData mNewWallpaper;
        final WallpaperData mOriginalSystem;

        WallpaperDestinationChangeHandler(WallpaperData wallpaperData) {
            this.mNewWallpaper = wallpaperData;
            this.mOriginalSystem = new WallpaperData((WallpaperData) WallpaperManagerService.this.mWallpaperMap.get(wallpaperData.userId));
        }

        void complete() {
            if (this.mNewWallpaper.mSystemWasBoth) {
                if (WallpaperManagerService.DEBUG) {
                    Slog.v(WallpaperManagerService.TAG, "Handling change from system+lock wallpaper");
                }
                int i = this.mNewWallpaper.mWhich;
                if (i == 1) {
                    if (WallpaperManagerService.this.mImageWallpaper.equals(this.mOriginalSystem.wallpaperComponent)) {
                        WallpaperData wallpaperData = (WallpaperData) WallpaperManagerService.this.mLockWallpaperMap.get(this.mNewWallpaper.userId);
                        if (wallpaperData != null) {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.v(WallpaperManagerService.TAG, "static system+lock to system success");
                            }
                            WallpaperData wallpaperData2 = this.mOriginalSystem;
                            wallpaperData.wallpaperComponent = wallpaperData2.wallpaperComponent;
                            WallpaperConnection wallpaperConnection = wallpaperData2.connection;
                            wallpaperData.connection = wallpaperConnection;
                            wallpaperConnection.mWallpaper = wallpaperData;
                            wallpaperData2.mWhich = 2;
                            WallpaperManagerService.this.updateEngineFlags(wallpaperData2);
                            WallpaperManagerService.this.notifyWallpaperColorsChanged(wallpaperData, 2);
                        } else {
                            if (WallpaperManagerService.DEBUG) {
                                Slog.v(WallpaperManagerService.TAG, "static system+lock to system failure");
                            }
                            WallpaperData wallpaperData3 = (WallpaperData) WallpaperManagerService.this.mWallpaperMap.get(this.mNewWallpaper.userId);
                            wallpaperData3.mWhich = 3;
                            WallpaperManagerService.this.updateEngineFlags(wallpaperData3);
                            WallpaperManagerService.this.mLockWallpaperMap.remove(this.mNewWallpaper.userId);
                        }
                    } else {
                        if (WallpaperManagerService.DEBUG) {
                            Slog.v(WallpaperManagerService.TAG, "live system+lock to system success");
                        }
                        WallpaperData wallpaperData4 = this.mOriginalSystem;
                        wallpaperData4.mWhich = 2;
                        WallpaperManagerService.this.updateEngineFlags(wallpaperData4);
                        WallpaperManagerService.this.mLockWallpaperMap.put(this.mNewWallpaper.userId, this.mOriginalSystem);
                        WallpaperManagerService wallpaperManagerService = WallpaperManagerService.this;
                        WallpaperData wallpaperData5 = this.mOriginalSystem;
                        wallpaperManagerService.mLastLockWallpaper = wallpaperData5;
                        wallpaperManagerService.notifyWallpaperColorsChanged(wallpaperData5, 2);
                    }
                } else if (i == 2) {
                    if (WallpaperManagerService.DEBUG) {
                        Slog.v(WallpaperManagerService.TAG, "system+lock to lock");
                    }
                    WallpaperData wallpaperData6 = (WallpaperData) WallpaperManagerService.this.mWallpaperMap.get(this.mNewWallpaper.userId);
                    if (wallpaperData6.wallpaperId == this.mOriginalSystem.wallpaperId) {
                        wallpaperData6.mWhich = 1;
                        WallpaperManagerService.this.updateEngineFlags(wallpaperData6);
                    }
                }
            }
            if (WallpaperManagerService.DEBUG) {
                Slog.v(WallpaperManagerService.TAG, "--- wallpaper changed --");
                Slog.v(WallpaperManagerService.TAG, "new sysWp: " + WallpaperManagerService.this.mWallpaperMap.get(WallpaperManagerService.this.mCurrentUserId));
                Slog.v(WallpaperManagerService.TAG, "new lockWp: " + WallpaperManagerService.this.mLockWallpaperMap.get(WallpaperManagerService.this.mCurrentUserId));
                Slog.v(WallpaperManagerService.TAG, "new lastWp: " + WallpaperManagerService.this.mLastWallpaper);
                Slog.v(WallpaperManagerService.TAG, "new lastLockWp: " + WallpaperManagerService.this.mLastLockWallpaper);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class MyPackageMonitor extends PackageMonitor {
        MyPackageMonitor() {
        }

        public void onPackageUpdateFinished(String str, int i) {
            ComponentName componentName;
            synchronized (WallpaperManagerService.this.mLock) {
                if (WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (WallpaperData wallpaperData : WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(WallpaperManagerService.this.mCurrentUserId, 1)) {
                    if (wallpaperData != null && (componentName = wallpaperData.wallpaperComponent) != null && componentName.getPackageName().equals(str)) {
                        Slog.i(WallpaperManagerService.TAG, "Wallpaper " + componentName + " update has finished");
                        wallpaperData.wallpaperUpdating = false;
                        if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperComponentLockedOnPackageUpdated(wallpaperData)) {
                            WallpaperManagerService.this.clearWallpaperComponentLocked(wallpaperData);
                        }
                        if (!WallpaperManagerService.this.bindWallpaperComponentLocked(componentName, false, false, wallpaperData, null)) {
                            Slog.w(WallpaperManagerService.TAG, "Wallpaper " + componentName + " no longer available; reverting to default");
                            if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(false, 1, wallpaperData.userId, null, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId())) {
                                WallpaperManagerService.this.clearWallpaperLocked(false, 1, wallpaperData.userId, null);
                            }
                        }
                    }
                }
                WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().resetOnPackageUpdatedLocked();
            }
        }

        public void onPackageModified(String str) {
            synchronized (WallpaperManagerService.this.mLock) {
                if (WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (WallpaperData wallpaperData : WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(WallpaperManagerService.this.mCurrentUserId, 1)) {
                    if (wallpaperData != null) {
                        ComponentName componentName = wallpaperData.wallpaperComponent;
                        if (componentName != null && componentName.getPackageName().equals(str)) {
                            doPackagesChangedLocked(true, wallpaperData);
                        }
                        return;
                    }
                }
            }
        }

        public void onPackageUpdateStarted(String str, int i) {
            ComponentName componentName;
            synchronized (WallpaperManagerService.this.mLock) {
                if (WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (WallpaperData wallpaperData : WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(WallpaperManagerService.this.mCurrentUserId, 1)) {
                    if (wallpaperData != null && (componentName = wallpaperData.wallpaperComponent) != null && componentName.getPackageName().equals(str)) {
                        Slog.i(WallpaperManagerService.TAG, "Wallpaper service " + wallpaperData.wallpaperComponent + " is updating");
                        wallpaperData.wallpaperUpdating = true;
                        if (wallpaperData.connection != null) {
                            FgThread.getHandler().removeCallbacks(wallpaperData.connection.mResetRunnable);
                        }
                    }
                }
            }
        }

        public boolean onHandleForceStop(Intent intent, String[] strArr, int i, boolean z) {
            synchronized (WallpaperManagerService.this.mLock) {
                boolean z2 = false;
                if (WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return false;
                }
                for (WallpaperData wallpaperData : WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(WallpaperManagerService.this.mCurrentUserId, 1)) {
                    if (wallpaperData != null) {
                        z2 = doPackagesChangedLocked(z, wallpaperData) | z2;
                    }
                }
                return z2;
            }
        }

        public void onSomePackagesChanged() {
            synchronized (WallpaperManagerService.this.mLock) {
                if (WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (WallpaperData wallpaperData : WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(WallpaperManagerService.this.mCurrentUserId, 1)) {
                    if (wallpaperData != null) {
                        doPackagesChangedLocked(true, wallpaperData);
                    }
                }
            }
        }

        boolean doPackagesChangedLocked(boolean z, WallpaperData wallpaperData) {
            boolean z2;
            int isPackageDisappearing;
            int isPackageDisappearing2;
            ComponentName componentName = wallpaperData.wallpaperComponent;
            if (componentName == null || !((isPackageDisappearing2 = isPackageDisappearing(componentName.getPackageName())) == 3 || isPackageDisappearing2 == 2)) {
                z2 = false;
            } else {
                if (z) {
                    Slog.w(WallpaperManagerService.TAG, "Wallpaper uninstalled, removing: " + wallpaperData.wallpaperComponent);
                    if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(false, 1, wallpaperData.userId, null, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId())) {
                        WallpaperManagerService.this.clearWallpaperLocked(false, 1, wallpaperData.userId, null);
                    }
                }
                z2 = true;
            }
            ComponentName componentName2 = wallpaperData.nextWallpaperComponent;
            if (componentName2 != null && ((isPackageDisappearing = isPackageDisappearing(componentName2.getPackageName())) == 3 || isPackageDisappearing == 2)) {
                wallpaperData.nextWallpaperComponent = null;
            }
            ComponentName componentName3 = wallpaperData.wallpaperComponent;
            if (componentName3 != null && isPackageModified(componentName3.getPackageName())) {
                try {
                    if (WallpaperManagerService.this.mIPackageManager != null && WallpaperManagerService.this.mIPackageManager.getServiceInfo(wallpaperData.wallpaperComponent, 786432L, wallpaperData.userId) == null) {
                        Slog.w(WallpaperManagerService.TAG, "Wallpaper component gone, removing: " + wallpaperData.wallpaperComponent);
                        if (!WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(false, 1, wallpaperData.userId, null, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId())) {
                            WallpaperManagerService.this.clearWallpaperLocked(false, 1, wallpaperData.userId, null);
                        }
                    }
                } catch (RemoteException unused) {
                }
            }
            ComponentName componentName4 = wallpaperData.nextWallpaperComponent;
            if (componentName4 != null && isPackageModified(componentName4.getPackageName())) {
                try {
                    if (WallpaperManagerService.this.mIPackageManager != null && WallpaperManagerService.this.mIPackageManager.getServiceInfo(wallpaperData.nextWallpaperComponent, 786432L, wallpaperData.userId) == null) {
                        wallpaperData.nextWallpaperComponent = null;
                    }
                } catch (RemoteException unused2) {
                }
            }
            return z2;
        }
    }

    @VisibleForTesting
    WallpaperData getCurrentWallpaperData(int i, int i2) {
        WallpaperData wallpaperData;
        synchronized (this.mLock) {
            int wallpaperType = this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i);
            wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(wallpaperType, wallpaperType == 1 ? this.mWallpaperMap : this.mLockWallpaperMap).get(i2);
        }
        return wallpaperData;
    }

    public WallpaperManagerService(Context context) {
        DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() { // from class: com.android.server.wallpaper.WallpaperManagerService.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
                WallpaperData wallpaperData;
                WallpaperConnection wallpaperConnection;
                synchronized (WallpaperManagerService.this.mLock) {
                    WallpaperManagerService.this.mWallpaperDisplayHelper.removeDisplayData(i);
                    WallpaperData wallpaperData2 = WallpaperManagerService.this.mLastWallpaper;
                    if (wallpaperData2 != null) {
                        if (wallpaperData2.connection.containsDisplay(i)) {
                            wallpaperData = WallpaperManagerService.this.mLastWallpaper;
                        } else {
                            WallpaperData wallpaperData3 = WallpaperManagerService.this.mFallbackWallpaper;
                            wallpaperData = (wallpaperData3 == null || (wallpaperConnection = wallpaperData3.connection) == null || !wallpaperConnection.containsDisplay(i)) ? null : WallpaperManagerService.this.mFallbackWallpaper;
                        }
                        if (wallpaperData == null) {
                            return;
                        }
                        DisplayConnector displayConnectorOrCreate = wallpaperData.connection.getDisplayConnectorOrCreate(i);
                        if (displayConnectorOrCreate == null) {
                            return;
                        }
                        displayConnectorOrCreate.disconnectLocked(wallpaperData.connection);
                        wallpaperData.connection.removeDisplayConnector(i);
                        WallpaperManagerService.this.mWallpaperDisplayHelper.removeDisplayData(i);
                    }
                    for (int size = WallpaperManagerService.this.mColorsChangedListeners.size() - 1; size >= 0; size--) {
                        ((SparseArray) WallpaperManagerService.this.mColorsChangedListeners.valueAt(size)).delete(i);
                    }
                }
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                synchronized (WallpaperManagerService.this.mLock) {
                    WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().removeDisplayData(i, WallpaperManagerService.this);
                }
            }
        };
        this.mDisplayListener = displayListener;
        this.mWallpaperMap = new SparseArray<>();
        this.mLockWallpaperMap = new SparseArray<>();
        this.mUserRestorecon = new SparseBooleanArray();
        this.mCurrentUserId = -10000;
        this.mLocalColorRepo = new LocalColorRepository();
        byte b = 0;
        this.mWallpaperManagerServiceWrapper = new WallpaperManagerServiceWrapperImpl();
        this.mWallpaperManagerServiceExt = (IWallpaperManagerServiceExt) ExtLoader.type(IWallpaperManagerServiceExt.class).base(this).create();
        this.mWallpaperManagerExt = (IWallpaperManagerExt) ExtLoader.type(IWallpaperManagerExt.class).create();
        if (DEBUG) {
            Slog.v(TAG, "WallpaperService startup");
        }
        this.mContext = context;
        this.mShuttingDown = false;
        this.mImageWallpaper = ComponentName.unflattenFromString(context.getResources().getString(R.string.mediasize_chinese_prc_1));
        WindowManagerInternal windowManagerInternal = (WindowManagerInternal) LocalServices.getService(WindowManagerInternal.class);
        this.mWindowManagerInternal = windowManagerInternal;
        this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        this.mIPackageManager = AppGlobals.getPackageManager();
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        displayManager.registerDisplayListener(displayListener, null);
        WallpaperDisplayHelper wallpaperDisplayHelper = new WallpaperDisplayHelper(displayManager, windowManagerInternal, this.mWallpaperManagerServiceExt);
        this.mWallpaperDisplayHelper = wallpaperDisplayHelper;
        WallpaperCropper wallpaperCropper = new WallpaperCropper(wallpaperDisplayHelper, this.mWallpaperManagerServiceExt);
        this.mWallpaperCropper = wallpaperCropper;
        this.mActivityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
        this.mMonitor = new MyPackageMonitor();
        this.mColorsChangedListeners = new SparseArray<>();
        this.mWallpaperDataParser = new WallpaperDataParser(context, wallpaperDisplayHelper, wallpaperCropper, this.mWallpaperManagerServiceExt);
        this.mIsLockscreenLiveWallpaperEnabled = SystemProperties.getBoolean("persist.wm.debug.lockscreen_live_wallpaper", false);
        this.mIsMultiCropEnabled = SystemProperties.getBoolean("persist.wm.debug.wallpaper_multi_crop", false);
        LocalServices.addService(WallpaperManagerInternal.class, new LocalService());
        this.mWallpaperManagerServiceWrapper.getExtImpl().initExt();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class LocalService extends WallpaperManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onDisplayReady(int i) {
            WallpaperManagerService.this.onDisplayReadyInternal(i);
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onScreenTurnedOn(int i) {
            WallpaperManagerService.this.notifyScreenTurnedOn(i);
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onScreenTurningOn(int i) {
            WallpaperManagerService.this.notifyScreenTurningOn(i);
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onKeyguardGoingAway() {
            WallpaperManagerService.this.notifyKeyguardGoingAway();
        }
    }

    void initialize() {
        this.mDefaultWallpaperComponent = WallpaperManager.getDefaultWallpaperComponent(this.mContext);
        this.mMonitor.getWrapper().getExtImpl().register(this.mContext, (Looper) null, UserHandle.ALL, true, new int[]{3, 5, 6, 12, 19});
        WallpaperUtils.getWallpaperDir(0).mkdirs();
        loadSettingsLocked(0, false, 3);
        getWallpaperSafeLocked(0, 1);
        this.mWallpaperManagerServiceWrapper.getExtImpl().initSeparateWallpaperForMultiDisplay(this.mContext);
        this.mWallpaperManagerServiceExt.registerLogSwitchObserver(this.mContext);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        for (int i = 0; i < this.mWallpaperMap.size(); i++) {
            this.mWallpaperMap.valueAt(i).wallpaperObserver.stopWatching();
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().finalizeSubDisplay();
    }

    void systemReady() {
        if (DEBUG) {
            Slog.v(TAG, "systemReady");
        }
        initialize();
        for (WallpaperData wallpaperData : this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(0, 1)) {
            if (this.mImageWallpaper.equals(wallpaperData.nextWallpaperComponent)) {
                if (!wallpaperData.cropExists()) {
                    if (DEBUG) {
                        Slog.i(TAG, "No crop; regenerating from source");
                    }
                    this.mWallpaperCropper.generateCrop(wallpaperData);
                }
                if (!wallpaperData.cropExists()) {
                    if (DEBUG) {
                        Slog.i(TAG, "Unable to regenerate crop; resetting");
                    }
                    if (!this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(false, 1, 0, null, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId())) {
                        clearWallpaperLocked(false, 1, 0, null);
                    }
                }
            } else if (DEBUG) {
                Slog.i(TAG, "Nondefault wallpaper component; gracefully ignoring");
            }
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.wallpaper.WallpaperManagerService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.USER_REMOVED".equals(intent.getAction())) {
                    WallpaperManagerService.this.onRemoveUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
                }
            }
        }, intentFilter);
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.wallpaper.WallpaperManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    if (WallpaperManagerService.DEBUG) {
                        Slog.i(WallpaperManagerService.TAG, "Shutting down");
                    }
                    synchronized (WallpaperManagerService.this.mLock) {
                        WallpaperManagerService.this.mShuttingDown = true;
                    }
                }
            }
        }, new IntentFilter("android.intent.action.ACTION_SHUTDOWN"));
        try {
            ActivityManager.getService().registerUserSwitchObserver(new UserSwitchObserver() { // from class: com.android.server.wallpaper.WallpaperManagerService.4
                public void onUserSwitching(int i, IRemoteCallback iRemoteCallback) {
                    try {
                        Trace.traceBegin(8L, "WallpaperManagerService.switchUser");
                        WallpaperManagerService.this.errorCheck(i);
                        WallpaperManagerService.this.switchUser(i, iRemoteCallback);
                    } finally {
                        Trace.traceEnd(8L);
                    }
                }
            }, TAG);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public String getName() {
        String str;
        if (Binder.getCallingUid() != 1000) {
            throw new RuntimeException("getName() can only be called from the system process");
        }
        synchronized (this.mLock) {
            str = this.mWallpaperMap.get(0).name;
        }
        return str;
    }

    void stopObserver(WallpaperData wallpaperData) {
        WallpaperObserver wallpaperObserver;
        if (wallpaperData == null || (wallpaperObserver = wallpaperData.wallpaperObserver) == null) {
            return;
        }
        wallpaperObserver.stopWatching();
        wallpaperData.wallpaperObserver = null;
    }

    void stopObserversLocked(int i) {
        stopObserver(this.mWallpaperMap.get(i));
        stopObserver(this.mLockWallpaperMap.get(i));
        this.mWallpaperMap.remove(i);
        this.mLockWallpaperMap.remove(i);
        this.mWallpaperManagerServiceWrapper.getExtImpl().stopSubDisplayObserversLocked(i);
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerService
    public void onBootPhase(int i) {
        errorCheck(0);
        if (i == 550) {
            systemReady();
        } else if (i == 600) {
            switchUser(0, null);
            this.mWallpaperManagerServiceWrapper.getExtImpl().initCustomizeWallpaper(this.mContext);
            this.mWallpaperManagerServiceWrapper.getExtImpl().initWallpaperBitmap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void errorCheck(final int i) {
        sWallpaperType.forEach(new BiConsumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda13
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                WallpaperManagerService.this.lambda$errorCheck$4(i, (Integer) obj, (String) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$errorCheck$4(int i, Integer num, String str) {
        File file = new File(WallpaperUtils.getWallpaperDir(i), str);
        if (file.exists()) {
            Slog.w(TAG, "User:" + i + ", wallpaper tyep = " + num + ", wallpaper fail detect!! reset to default wallpaper");
            clearWallpaperData(i, num.intValue());
            file.delete();
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().subDisplayErrorCheck(i, num.intValue(), str);
    }

    private void clearWallpaperData(int i, int i2) {
        WallpaperData wallpaperData = new WallpaperData(i, i2);
        if (wallpaperData.sourceExists()) {
            wallpaperData.wallpaperFile.delete();
        }
        if (wallpaperData.cropExists()) {
            wallpaperData.cropFile.delete();
        }
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerService
    public void onUnlockUser(final int i) {
        synchronized (this.mLock) {
            if (this.mCurrentUserId == i) {
                if (this.mIsLockscreenLiveWallpaperEnabled) {
                    if (this.mHomeWallpaperWaitingForUnlock) {
                        WallpaperData wallpaperSafeLocked = getWallpaperSafeLocked(i, 1);
                        switchWallpaper(wallpaperSafeLocked, null);
                        notifyCallbacksLocked(wallpaperSafeLocked);
                    }
                    if (this.mLockWallpaperWaitingForUnlock) {
                        WallpaperData wallpaperSafeLocked2 = getWallpaperSafeLocked(i, 2);
                        switchWallpaper(wallpaperSafeLocked2, null);
                        notifyCallbacksLocked(wallpaperSafeLocked2);
                    }
                }
                if (this.mWaitingForUnlock && !this.mIsLockscreenLiveWallpaperEnabled) {
                    WallpaperData wallpaperSafeLocked3 = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(i, 1, this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentPhysicalDisplayIdLocked());
                    if (wallpaperSafeLocked3 == null) {
                        wallpaperSafeLocked3 = getWallpaperSafeLocked(i, 1);
                    }
                    switchWallpaper(wallpaperSafeLocked3, null);
                    notifyCallbacksLocked(wallpaperSafeLocked3);
                }
                this.mWallpaperManagerServiceWrapper.getExtImpl().switchWallpaperForOtherPhysicalDisplay(i, true);
                if (!this.mUserRestorecon.get(i)) {
                    this.mUserRestorecon.put(i, true);
                    BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            WallpaperManagerService.this.lambda$onUnlockUser$5(i);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUnlockUser$5(int i) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG);
        timingsTraceAndSlog.traceBegin("Wallpaper_selinux_restorecon-" + i);
        try {
            for (File file : WallpaperUtils.getWallpaperFiles(i)) {
                if (file.exists()) {
                    SELinux.restorecon(file);
                }
            }
            this.mWallpaperManagerServiceWrapper.getExtImpl().restoreconSubDisplayFiles(i);
        } finally {
            timingsTraceAndSlog.traceEnd();
        }
    }

    void onRemoveUser(int i) {
        if (i < 1) {
            return;
        }
        synchronized (this.mLock) {
            stopObserversLocked(i);
            WallpaperUtils.getWallpaperFiles(i).forEach(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda29
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((File) obj).delete();
                }
            });
            this.mUserRestorecon.delete(i);
            this.mWallpaperManagerServiceWrapper.getExtImpl().deleteSubDisplayFiles(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x007d A[Catch: all -> 0x00b8, Merged into TryCatch #1 {all -> 0x00bb, blocks: (B:3:0x001b, B:4:0x001d, B:29:0x00a8, B:38:0x00ba, B:6:0x001e, B:8:0x0022, B:12:0x0027, B:14:0x0049, B:15:0x004d, B:17:0x0052, B:20:0x0079, B:22:0x007d, B:23:0x0087, B:26:0x0096, B:27:0x009a, B:28:0x00a7, B:32:0x0059, B:33:0x0064), top: B:2:0x001b }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0094 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void switchUser(int i, IRemoteCallback iRemoteCallback) {
        final WallpaperData wallpaperData;
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG);
        timingsTraceAndSlog.traceBegin("Wallpaper_switch-user-" + i);
        try {
            synchronized (this.mLock) {
                if (this.mCurrentUserId == i) {
                    return;
                }
                this.mCurrentUserId = i;
                this.mWallpaperManagerServiceWrapper.getExtImpl().initOnUserSwitch(i);
                int currentPhysicalDisplayIdLocked = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentPhysicalDisplayIdLocked();
                final WallpaperData wallpaperSafeLocked = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(i, 1, currentPhysicalDisplayIdLocked);
                if (wallpaperSafeLocked == null) {
                    wallpaperSafeLocked = getWallpaperSafeLocked(i, 1);
                }
                if (this.mIsLockscreenLiveWallpaperEnabled) {
                    if (wallpaperSafeLocked.mWhich != 3) {
                        wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(i, 2, currentPhysicalDisplayIdLocked);
                        if (wallpaperSafeLocked.wallpaperObserver == null) {
                            WallpaperObserver wallpaperObserver = new WallpaperObserver(wallpaperSafeLocked);
                            wallpaperSafeLocked.wallpaperObserver = wallpaperObserver;
                            wallpaperObserver.startWatching();
                        }
                        this.mWallpaperManagerServiceWrapper.getExtImpl().detachOtherPhysicalDisplaysWallpaper(i, wallpaperSafeLocked);
                        if (this.mIsLockscreenLiveWallpaperEnabled && wallpaperData != wallpaperSafeLocked) {
                            switchWallpaper(wallpaperData, null);
                        }
                        switchWallpaper(wallpaperSafeLocked, iRemoteCallback);
                        this.mWallpaperManagerServiceWrapper.getExtImpl().switchWallpaperForOtherPhysicalDisplay(i, false);
                        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda31
                            @Override // java.lang.Runnable
                            public final void run() {
                                WallpaperManagerService.this.lambda$switchUser$6(wallpaperSafeLocked, wallpaperData);
                            }
                        });
                    }
                    wallpaperData = wallpaperSafeLocked;
                    if (wallpaperSafeLocked.wallpaperObserver == null) {
                    }
                    this.mWallpaperManagerServiceWrapper.getExtImpl().detachOtherPhysicalDisplaysWallpaper(i, wallpaperSafeLocked);
                    if (this.mIsLockscreenLiveWallpaperEnabled) {
                        switchWallpaper(wallpaperData, null);
                    }
                    switchWallpaper(wallpaperSafeLocked, iRemoteCallback);
                    this.mWallpaperManagerServiceWrapper.getExtImpl().switchWallpaperForOtherPhysicalDisplay(i, false);
                    FgThread.getHandler().post(new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda31
                        @Override // java.lang.Runnable
                        public final void run() {
                            WallpaperManagerService.this.lambda$switchUser$6(wallpaperSafeLocked, wallpaperData);
                        }
                    });
                }
                wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, currentPhysicalDisplayIdLocked, this.mLockWallpaperMap).get(i);
                if (wallpaperData == null) {
                    wallpaperData = wallpaperSafeLocked;
                }
                if (wallpaperSafeLocked.wallpaperObserver == null) {
                }
                this.mWallpaperManagerServiceWrapper.getExtImpl().detachOtherPhysicalDisplaysWallpaper(i, wallpaperSafeLocked);
                if (this.mIsLockscreenLiveWallpaperEnabled) {
                }
                switchWallpaper(wallpaperSafeLocked, iRemoteCallback);
                this.mWallpaperManagerServiceWrapper.getExtImpl().switchWallpaperForOtherPhysicalDisplay(i, false);
                FgThread.getHandler().post(new Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda31
                    @Override // java.lang.Runnable
                    public final void run() {
                        WallpaperManagerService.this.lambda$switchUser$6(wallpaperSafeLocked, wallpaperData);
                    }
                });
            }
        } finally {
            timingsTraceAndSlog.traceEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchUser$6(WallpaperData wallpaperData, WallpaperData wallpaperData2) {
        notifyWallpaperColorsChanged(wallpaperData, 1);
        notifyWallpaperColorsChanged(wallpaperData2, 2);
        notifyWallpaperColorsChanged(this.mFallbackWallpaper, 1);
    }

    void switchWallpaper(final WallpaperData wallpaperData, IRemoteCallback iRemoteCallback) {
        ServiceInfo serviceInfo;
        synchronized (this.mLock) {
            this.mWaitingForUnlock = false;
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                int i = wallpaperData.mWhich;
                if ((i & 1) != 0) {
                    this.mHomeWallpaperWaitingForUnlock = false;
                }
                if ((i & 2) != 0) {
                    this.mLockWallpaperWaitingForUnlock = false;
                }
            }
            ComponentName componentName = wallpaperData.wallpaperComponent;
            if (componentName == null) {
                componentName = wallpaperData.nextWallpaperComponent;
            }
            if (!bindWallpaperComponentLocked(componentName, true, false, wallpaperData, iRemoteCallback)) {
                try {
                    serviceInfo = this.mIPackageManager.getServiceInfo(componentName, 262144L, wallpaperData.userId);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failure starting previous wallpaper; clearing", e);
                    serviceInfo = null;
                }
                if (this.mIsLockscreenLiveWallpaperEnabled) {
                    onSwitchWallpaperFailLocked(wallpaperData, iRemoteCallback, serviceInfo);
                    return;
                }
                if (serviceInfo == null) {
                    if (!this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(false, 1, wallpaperData.userId, null, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId())) {
                        clearWallpaperLocked(false, 1, wallpaperData.userId, null);
                    }
                } else {
                    Slog.w(TAG, "Wallpaper isn't direct boot aware; using fallback until unlocked");
                    wallpaperData.wallpaperComponent = wallpaperData.nextWallpaperComponent;
                    bindWallpaperComponentLocked(this.mImageWallpaper, true, false, this.mWallpaperManagerServiceWrapper.getExtImpl().newDirectBootAwareFallbackWallpaper(wallpaperData, new Supplier() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda19
                        @Override // java.util.function.Supplier
                        public final Object get() {
                            WallpaperData lambda$switchWallpaper$7;
                            lambda$switchWallpaper$7 = WallpaperManagerService.lambda$switchWallpaper$7(WallpaperData.this);
                            return lambda$switchWallpaper$7;
                        }
                    }), iRemoteCallback);
                    this.mWaitingForUnlock = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WallpaperData lambda$switchWallpaper$7(WallpaperData wallpaperData) {
        return new WallpaperData(wallpaperData.userId, 2);
    }

    private void onSwitchWallpaperFailLocked(WallpaperData wallpaperData, IRemoteCallback iRemoteCallback, ServiceInfo serviceInfo) {
        if (serviceInfo == null) {
            int i = wallpaperData.mWhich;
            if (i == 3) {
                clearWallpaperLocked(false, 1, wallpaperData.userId, null);
                clearWallpaperLocked(false, 2, wallpaperData.userId, iRemoteCallback);
                return;
            } else {
                clearWallpaperLocked(false, i, wallpaperData.userId, iRemoteCallback);
                return;
            }
        }
        Slog.w(TAG, "Wallpaper isn't direct boot aware; using fallback until unlocked");
        wallpaperData.wallpaperComponent = wallpaperData.nextWallpaperComponent;
        WallpaperData wallpaperData2 = new WallpaperData(wallpaperData.userId, wallpaperData.mWhich);
        if (wallpaperData.wallpaperFile.exists()) {
            wallpaperData.wallpaperFile.delete();
            wallpaperData.cropFile.delete();
        }
        bindWallpaperComponentLocked(this.mImageWallpaper, true, false, wallpaperData2, iRemoteCallback);
        int i2 = wallpaperData.mWhich;
        if ((i2 & 1) != 0) {
            this.mHomeWallpaperWaitingForUnlock = true;
        }
        if ((i2 & 2) != 0) {
            this.mLockWallpaperWaitingForUnlock = true;
        }
    }

    public void clearWallpaper(String str, int i, int i2) {
        Slog.v(TAG, "clearWallpaper callPackage=" + str);
        checkPermission("android.permission.SET_WALLPAPER");
        if (isWallpaperSupported(str) && isSetWallpaperAllowed(str)) {
            int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i2, false, true, "clearWallpaper", null);
            synchronized (this.mLock) {
                int formatWhichPending = this.mWallpaperManagerServiceWrapper.getExtImpl().formatWhichPending(i);
                if (!this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLockedForMultiPhysicalDisplays(false, formatWhichPending, handleIncomingUser, null)) {
                    clearWallpaperLocked(false, i, handleIncomingUser, null);
                }
                if (this.mWallpaperManagerServiceWrapper.getExtImpl().isCurrentPhysicalDisplayWallpaperChangedLocked(formatWhichPending)) {
                    WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 2 ? this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(handleIncomingUser) : null;
                    if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 2 || wallpaperData == null) {
                        wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(handleIncomingUser);
                    }
                    if (wallpaperData != null) {
                        notifyWallpaperColorsChanged(wallpaperData, this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i));
                        notifyWallpaperColorsChanged(this.mFallbackWallpaper, 1);
                    }
                }
            }
        }
    }

    void clearWallpaperLocked(boolean z, int i, int i2, IRemoteCallback iRemoteCallback) {
        WallpaperData wallpaperData;
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(i)) {
            throw new IllegalArgumentException("Must specify exactly one kind of wallpaper to clear");
        }
        if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 2) {
            wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, this.mLockWallpaperMap).get(i2);
            if (wallpaperData == null) {
                if (DEBUG) {
                    Slog.i(TAG, "Lock wallpaper already cleared");
                }
                this.mWallpaperManagerServiceWrapper.getExtImpl().setIsMainDisplayWallpaperChangeLocked(null, i);
                notifyLockWallpaperChanged();
                return;
            }
        } else {
            wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, this.mWallpaperMap).get(i2);
            if (wallpaperData == null) {
                this.mWallpaperManagerServiceWrapper.getExtImpl().loadSettingsLocked(i2, false, this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdLocked(i), 1);
                wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, this.mWallpaperMap).get(i2);
            }
        }
        WallpaperData wallpaperData2 = wallpaperData;
        if (wallpaperData2 == null) {
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mWallpaperManagerServiceExt.clearFlipClockTextColorIfNeed(this.mContext, i, i2);
            this.mWallpaperManagerServiceExt.clearFlipClockTextStyleIfNeed(this.mContext, i, i2);
            if (wallpaperData2.wallpaperFile.exists()) {
                wallpaperData2.wallpaperFile.delete();
                wallpaperData2.cropFile.delete();
                if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 2) {
                    this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, this.mLockWallpaperMap).remove(i2);
                    IWallpaperManagerCallback iWallpaperManagerCallback = this.mKeyguardListener;
                    if (iWallpaperManagerCallback != null && this.mWallpaperManagerServiceWrapper.getExtImpl().isCurrentPhysicalDisplayWallpaper(wallpaperData2)) {
                        if (DEBUG) {
                            Slog.i(TAG, "Notifying keyguard of lock wallpaper clear");
                        }
                        try {
                            iWallpaperManagerCallback.onWallpaperChanged();
                        } catch (RemoteException e) {
                            Slog.w(TAG, "Failed to notify keyguard after wallpaper clear", e);
                        }
                    }
                    this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLocked(i2, this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdLocked(i));
                    return;
                }
            }
            if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 1 && this.mWallpaperManagerServiceWrapper.getExtImpl().isCurrentPhysicalDisplayWallpaper(wallpaperData2)) {
                notifyCallbacksLocked(wallpaperData2);
            }
            try {
                wallpaperData2.primaryColors = null;
                wallpaperData2.imageWallpaperPending = false;
            } catch (IllegalArgumentException e2) {
                e = e2;
            }
            if (i2 != this.mCurrentUserId) {
                return;
            }
            if (bindWallpaperComponentLocked(z ? this.mImageWallpaper : null, true, false, wallpaperData2, iRemoteCallback)) {
                return;
            }
            e = null;
            Slog.e(TAG, "Default wallpaper component not found!", e);
            clearWallpaperComponentLocked(wallpaperData2);
            if (iRemoteCallback != null) {
                try {
                    iRemoteCallback.sendResult((Bundle) null);
                } catch (RemoteException e3) {
                    Slog.w(TAG, "Failed to notify callback after wallpaper clear", e3);
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private boolean hasCrossUserPermission() {
        return this.mContext.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0;
    }

    public boolean hasNamedWallpaper(String str) {
        int callingUserId = UserHandle.getCallingUserId();
        boolean hasCrossUserPermission = hasCrossUserPermission();
        if (DEBUG) {
            Slog.d(TAG, "hasNamedWallpaper() caller " + Binder.getCallingUid() + " cross-user?: " + hasCrossUserPermission);
        }
        synchronized (this.mLock) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                List<UserInfo> users = ((UserManager) this.mContext.getSystemService("user")).getUsers();
                Binder.restoreCallingIdentity(clearCallingIdentity);
                for (UserInfo userInfo : users) {
                    if (hasCrossUserPermission || callingUserId == userInfo.id) {
                        if (!userInfo.isManagedProfile()) {
                            WallpaperData wallpaperData = this.mWallpaperMap.get(userInfo.id);
                            if (wallpaperData == null) {
                                loadSettingsLocked(userInfo.id, false, 3);
                                wallpaperData = this.mWallpaperMap.get(userInfo.id);
                            }
                            if (wallpaperData != null && str.equals(wallpaperData.name)) {
                                return true;
                            }
                            if (this.mWallpaperManagerServiceWrapper.getExtImpl().hasNamedSubWallpaperForUser(userInfo.id, str)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(clearCallingIdentity);
                throw th;
            }
        }
    }

    public void setDimensionHints(int i, int i2, String str, int i3) throws RemoteException {
        checkPermission("android.permission.SET_WALLPAPER_HINTS");
        if (isWallpaperSupported(str)) {
            int min = Math.min(i, GLHelper.getMaxTextureSize());
            int min2 = Math.min(i2, GLHelper.getMaxTextureSize());
            synchronized (this.mLock) {
                int callingUserId = UserHandle.getCallingUserId();
                WallpaperData wallpaperSafeLocked = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(callingUserId, 1, this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdFromDisplayIdLocked(i3));
                if (min <= 0 || min2 <= 0) {
                    throw new IllegalArgumentException("width and height must be > 0");
                }
                if (!this.mWallpaperDisplayHelper.isValidDisplay(i3)) {
                    throw new IllegalArgumentException("Cannot find display with id=" + i3);
                }
                WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(i3);
                if (min != displayDataOrCreate.mWidth || min2 != displayDataOrCreate.mHeight) {
                    displayDataOrCreate.mWidth = min;
                    displayDataOrCreate.mHeight = min2;
                    if (i3 == 0) {
                        saveSettingsLocked(callingUserId);
                    }
                    if (this.mCurrentUserId != callingUserId) {
                        return;
                    }
                    WallpaperConnection wallpaperConnection = wallpaperSafeLocked.connection;
                    if (wallpaperConnection != null) {
                        DisplayConnector displayConnectorOrCreate = wallpaperConnection.getDisplayConnectorOrCreate(i3);
                        IWallpaperEngine iWallpaperEngine = displayConnectorOrCreate != null ? displayConnectorOrCreate.mEngine : null;
                        if (iWallpaperEngine != null) {
                            try {
                                iWallpaperEngine.setDesiredSize(min, min2);
                            } catch (RemoteException e) {
                                Slog.w(TAG, "Failed to set desired size", e);
                            }
                            notifyCallbacksLocked(wallpaperSafeLocked);
                        } else if (wallpaperSafeLocked.connection.mService != null && displayConnectorOrCreate != null) {
                            displayConnectorOrCreate.mDimensionsChanged = true;
                        }
                    }
                }
            }
        }
    }

    public int getWidthHint(int i) throws RemoteException {
        synchronized (this.mLock) {
            if (!this.mWallpaperDisplayHelper.isValidDisplay(i)) {
                throw new IllegalArgumentException("Cannot find display with id=" + i);
            }
            if (this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(UserHandle.getCallingUserId()) == null) {
                return 0;
            }
            return this.mWallpaperDisplayHelper.getDisplayDataOrCreate(i).mWidth;
        }
    }

    public int getHeightHint(int i) throws RemoteException {
        synchronized (this.mLock) {
            if (!this.mWallpaperDisplayHelper.isValidDisplay(i)) {
                throw new IllegalArgumentException("Cannot find display with id=" + i);
            }
            if (this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(UserHandle.getCallingUserId()) == null) {
                return 0;
            }
            return this.mWallpaperDisplayHelper.getDisplayDataOrCreate(i).mHeight;
        }
    }

    public void setDisplayPadding(Rect rect, String str, int i) {
        checkPermission("android.permission.SET_WALLPAPER_HINTS");
        if (isWallpaperSupported(str)) {
            synchronized (this.mLock) {
                if (!this.mWallpaperDisplayHelper.isValidDisplay(i)) {
                    throw new IllegalArgumentException("Cannot find display with id=" + i);
                }
                int callingUserId = UserHandle.getCallingUserId();
                WallpaperData wallpaperSafeLocked = getWallpaperSafeLocked(callingUserId, 1);
                if (rect.left < 0 || rect.top < 0 || rect.right < 0 || rect.bottom < 0) {
                    throw new IllegalArgumentException("padding must be positive: " + rect);
                }
                int maximumSizeDimension = this.mWallpaperDisplayHelper.getMaximumSizeDimension(i);
                int i2 = rect.left + rect.right;
                int i3 = rect.top + rect.bottom;
                if (i2 > maximumSizeDimension) {
                    throw new IllegalArgumentException("padding width " + i2 + " exceeds max width " + maximumSizeDimension);
                }
                if (i3 > maximumSizeDimension) {
                    throw new IllegalArgumentException("padding height " + i3 + " exceeds max height " + maximumSizeDimension);
                }
                WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(i);
                if (!rect.equals(displayDataOrCreate.mPadding)) {
                    displayDataOrCreate.mPadding.set(rect);
                    if (i == 0) {
                        saveSettingsLocked(callingUserId);
                    }
                    if (this.mCurrentUserId != callingUserId) {
                        return;
                    }
                    WallpaperConnection wallpaperConnection = wallpaperSafeLocked.connection;
                    if (wallpaperConnection != null) {
                        DisplayConnector displayConnectorOrCreate = wallpaperConnection.getDisplayConnectorOrCreate(i);
                        IWallpaperEngine iWallpaperEngine = displayConnectorOrCreate != null ? displayConnectorOrCreate.mEngine : null;
                        if (iWallpaperEngine != null) {
                            try {
                                iWallpaperEngine.setDisplayPadding(rect);
                            } catch (RemoteException e) {
                                Slog.w(TAG, "Failed to set display padding", e);
                            }
                            notifyCallbacksLocked(wallpaperSafeLocked);
                        } else if (wallpaperSafeLocked.connection.mService != null && displayConnectorOrCreate != null) {
                            displayConnectorOrCreate.mPaddingChanged = true;
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    public ParcelFileDescriptor getWallpaper(String str, IWallpaperManagerCallback iWallpaperManagerCallback, int i, Bundle bundle, int i2) {
        return getWallpaperWithFeature(str, null, iWallpaperManagerCallback, i, bundle, i2, true);
    }

    public ParcelFileDescriptor getWallpaperWithFeature(String str, String str2, IWallpaperManagerCallback iWallpaperManagerCallback, int i, Bundle bundle, int i2, boolean z) {
        if (!hasPermission("android.permission.READ_WALLPAPER_INTERNAL")) {
            try {
                if (!isCtsTest() && this.mContext.getPackageManager().getApplicationInfo(str, 0).targetSdkVersion >= 33) {
                    if (this.mContext.checkPermission("android.permission.READ_MEDIA_IMAGES", Binder.getCallingPid(), Binder.getCallingUid()) != 0) {
                        ((StorageManager) this.mContext.getSystemService(StorageManager.class)).checkPermissionReadImages(true, Binder.getCallingPid(), Binder.getCallingUid(), str, str2);
                    }
                }
                ((StorageManager) this.mContext.getSystemService(StorageManager.class)).checkPermissionReadImages(true, Binder.getCallingPid(), Binder.getCallingUid(), str, str2);
            } catch (PackageManager.NameNotFoundException e) {
                Slog.e(TAG, "getApplicationInfo error ", e);
            }
        }
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i2, false, true, "getWallpaper", null);
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(i)) {
            throw new IllegalArgumentException("Must specify exactly one kind of wallpaper to read");
        }
        synchronized (this.mLock) {
            WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 2 ? this.mLockWallpaperMap : this.mWallpaperMap).get(handleIncomingUser);
            if (wallpaperData == null) {
                return null;
            }
            WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
            if (bundle != null) {
                try {
                    bundle.putInt("width", displayDataOrCreate.mWidth);
                    bundle.putInt("height", displayDataOrCreate.mHeight);
                } catch (FileNotFoundException e2) {
                    Slog.w(TAG, "Error getting wallpaper", e2);
                    return null;
                }
            }
            if (iWallpaperManagerCallback != null) {
                wallpaperData.callbacks.register(iWallpaperManagerCallback);
                this.mWallpaperManagerServiceWrapper.getExtImpl().registerWallpaperCallbacksToOtherPhysicalDisplays(i, iWallpaperManagerCallback, wallpaperData);
            }
            File file = z ? wallpaperData.cropFile : wallpaperData.wallpaperFile;
            if (file.exists()) {
                return ParcelFileDescriptor.open(file, 268435456);
            }
            return null;
        }
    }

    private boolean isCtsTest() {
        return !SystemProperties.getBoolean("persist.sys.permission.enable", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasPermission(String str) {
        return this.mContext.checkCallingOrSelfPermission(str) == 0;
    }

    public WallpaperInfo getWallpaperInfo(int i) {
        return getWallpaperInfoWithFlags(1, i);
    }

    public WallpaperInfo getWallpaperInfoWithFlags(int i, int i2) {
        WallpaperData wallpaperData;
        WallpaperConnection wallpaperConnection;
        WallpaperInfo wallpaperInfo;
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i2, false, true, "getWallpaperInfo", null);
        synchronized (this.mLock) {
            if (i == 2) {
                wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(handleIncomingUser);
            } else {
                wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(handleIncomingUser);
            }
            if (wallpaperData != null && (wallpaperConnection = wallpaperData.connection) != null && (wallpaperInfo = wallpaperConnection.mInfo) != null) {
                if (!hasPermission("android.permission.READ_WALLPAPER_INTERNAL") && !this.mPackageManagerInternal.canQueryPackage(Binder.getCallingUid(), wallpaperInfo.getComponent().getPackageName())) {
                    return null;
                }
                return wallpaperInfo;
            }
            return null;
        }
    }

    public ParcelFileDescriptor getWallpaperInfoFile(int i) {
        synchronized (this.mLock) {
            try {
                try {
                    File file = new File(WallpaperUtils.getWallpaperDir(i), "wallpaper_info.xml");
                    if (!file.exists()) {
                        return null;
                    }
                    return ParcelFileDescriptor.open(file, 268435456);
                } catch (FileNotFoundException e) {
                    Slog.w(TAG, "Error getting wallpaper info file", e);
                    return null;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getWallpaperIdForUser(int i, int i2) {
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i2, false, true, "getWallpaperIdForUser", null);
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(i)) {
            throw new IllegalArgumentException("Must specify exactly one kind of wallpaper");
        }
        synchronized (this.mLock) {
            WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i) == 2 ? this.mLockWallpaperMap : this.mWallpaperMap).get(handleIncomingUser);
            if (wallpaperData != null) {
                return wallpaperData.wallpaperId;
            }
            return -1;
        }
    }

    public void registerWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i, int i2) {
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, true, true, "registerWallpaperColorsCallback", null);
        synchronized (this.mLock) {
            SparseArray<RemoteCallbackList<IWallpaperManagerCallback>> sparseArray = this.mColorsChangedListeners.get(handleIncomingUser);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                this.mColorsChangedListeners.put(handleIncomingUser, sparseArray);
            }
            RemoteCallbackList<IWallpaperManagerCallback> remoteCallbackList = sparseArray.get(i2);
            if (remoteCallbackList == null) {
                remoteCallbackList = new RemoteCallbackList<>();
                sparseArray.put(i2, remoteCallbackList);
            }
            remoteCallbackList.register(iWallpaperManagerCallback);
        }
    }

    public void unregisterWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i, int i2) {
        RemoteCallbackList<IWallpaperManagerCallback> remoteCallbackList;
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, true, true, "unregisterWallpaperColorsCallback", null);
        synchronized (this.mLock) {
            SparseArray<RemoteCallbackList<IWallpaperManagerCallback>> sparseArray = this.mColorsChangedListeners.get(handleIncomingUser);
            if (sparseArray != null && (remoteCallbackList = sparseArray.get(i2)) != null) {
                remoteCallbackList.unregister(iWallpaperManagerCallback);
            }
        }
    }

    public void setInAmbientMode(boolean z, long j) {
        IWallpaperEngine iWallpaperEngine;
        WallpaperConnection wallpaperConnection;
        WallpaperInfo wallpaperInfo;
        IWallpaperEngine iWallpaperEngine2;
        if (this.mIsLockscreenLiveWallpaperEnabled) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.mLock) {
                this.mInAmbientMode = z;
                for (WallpaperData wallpaperData : getActiveWallpapers()) {
                    WallpaperInfo wallpaperInfo2 = wallpaperData.connection.mInfo;
                    if ((wallpaperInfo2 == null || wallpaperInfo2.supportsAmbientMode()) && (iWallpaperEngine2 = wallpaperData.connection.getDisplayConnectorOrCreate(0).mEngine) != null) {
                        arrayList.add(iWallpaperEngine2);
                    }
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((IWallpaperEngine) it.next()).setInAmbientMode(z, j);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Failed to set ambient mode", e);
                }
            }
            return;
        }
        synchronized (this.mLock) {
            this.mInAmbientMode = z;
            WallpaperData wallpaperData2 = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(this.mCurrentUserId);
            iWallpaperEngine = (wallpaperData2 == null || (wallpaperConnection = wallpaperData2.connection) == null || !((wallpaperInfo = wallpaperConnection.mInfo) == null || wallpaperInfo.supportsAmbientMode())) ? null : wallpaperData2.connection.getDisplayConnectorOrCreate(0).mEngine;
        }
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.setInAmbientMode(z, j);
            } catch (RemoteException e2) {
                Slog.w(TAG, "Failed to set ambient mode", e2);
            }
        }
    }

    public void notifyWakingUp(final int i, final int i2, final Bundle bundle) {
        WallpaperConnection wallpaperConnection;
        checkCallerIsSystemOrSystemUi();
        synchronized (this.mLock) {
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                for (WallpaperData wallpaperData : getActiveWallpapers()) {
                    wallpaperData.connection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda23
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WallpaperManagerService.lambda$notifyWakingUp$8(i, i2, bundle, (WallpaperManagerService.DisplayConnector) obj);
                        }
                    });
                }
                return;
            }
            WallpaperData wallpaperData2 = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(this.mCurrentUserId);
            if (wallpaperData2 != null && (wallpaperConnection = wallpaperData2.connection) != null) {
                wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda24
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WallpaperManagerService.lambda$notifyWakingUp$9(i, i2, bundle, (WallpaperManagerService.DisplayConnector) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyWakingUp$8(int i, int i2, Bundle bundle, DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.wakingup", i, i2, -1, bundle);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to dispatch COMMAND_WAKING_UP", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyWakingUp$9(int i, int i2, Bundle bundle, DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.wakingup", i, i2, -1, bundle);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to dispatch COMMAND_WAKING_UP", e);
            }
        }
    }

    public void notifyGoingToSleep(final int i, final int i2, final Bundle bundle) {
        WallpaperConnection wallpaperConnection;
        checkCallerIsSystemOrSystemUi();
        synchronized (this.mLock) {
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                for (WallpaperData wallpaperData : getActiveWallpapers()) {
                    wallpaperData.connection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda14
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WallpaperManagerService.lambda$notifyGoingToSleep$10(i, i2, bundle, (WallpaperManagerService.DisplayConnector) obj);
                        }
                    });
                }
                return;
            }
            WallpaperData wallpaperData2 = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(this.mCurrentUserId);
            if (wallpaperData2 != null && (wallpaperConnection = wallpaperData2.connection) != null) {
                wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda15
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WallpaperManagerService.lambda$notifyGoingToSleep$11(i, i2, bundle, (WallpaperManagerService.DisplayConnector) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyGoingToSleep$10(int i, int i2, Bundle bundle, DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.goingtosleep", i, i2, -1, bundle);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to dispatch COMMAND_GOING_TO_SLEEP", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyGoingToSleep$11(int i, int i2, Bundle bundle, DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.goingtosleep", i, i2, -1, bundle);
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to dispatch COMMAND_GOING_TO_SLEEP", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScreenTurnedOn(int i) {
        WallpaperConnection wallpaperConnection;
        IWallpaperEngine iWallpaperEngine;
        IWallpaperEngine iWallpaperEngine2;
        synchronized (this.mLock) {
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                for (WallpaperData wallpaperData : getActiveWallpapers()) {
                    if (wallpaperData.connection.containsDisplay(i) && (iWallpaperEngine2 = wallpaperData.connection.getDisplayConnectorOrCreate(i).mEngine) != null) {
                        try {
                            iWallpaperEngine2.onScreenTurnedOn();
                        } catch (RemoteException e) {
                            Slog.w(TAG, "Failed to notify that the screen turned on", e);
                        }
                    }
                }
                return;
            }
            WallpaperData wallpaperData2 = this.mWallpaperMap.get(this.mCurrentUserId);
            if (wallpaperData2 != null && (wallpaperConnection = wallpaperData2.connection) != null && wallpaperConnection.containsDisplay(i) && (iWallpaperEngine = wallpaperData2.connection.getDisplayConnectorOrCreate(i).mEngine) != null) {
                try {
                    iWallpaperEngine.onScreenTurnedOn();
                } catch (RemoteException e2) {
                    Slog.w(TAG, "Failed to notify that the screen turned on", e2);
                }
            }
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScreenTurningOn(int i) {
        WallpaperConnection wallpaperConnection;
        IWallpaperEngine iWallpaperEngine;
        IWallpaperEngine iWallpaperEngine2;
        synchronized (this.mLock) {
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                for (WallpaperData wallpaperData : getActiveWallpapers()) {
                    if (wallpaperData.connection.containsDisplay(i) && (iWallpaperEngine2 = wallpaperData.connection.getDisplayConnectorOrCreate(i).mEngine) != null) {
                        try {
                            iWallpaperEngine2.onScreenTurningOn();
                        } catch (RemoteException e) {
                            Slog.w(TAG, "Failed to notify that the screen is turning on", e);
                        }
                    }
                }
                return;
            }
            WallpaperData wallpaperData2 = this.mWallpaperMap.get(this.mCurrentUserId);
            if (wallpaperData2 != null && (wallpaperConnection = wallpaperData2.connection) != null && wallpaperConnection.containsDisplay(i) && (iWallpaperEngine = wallpaperData2.connection.getDisplayConnectorOrCreate(i).mEngine) != null) {
                try {
                    iWallpaperEngine.onScreenTurningOn();
                } catch (RemoteException e2) {
                    Slog.w(TAG, "Failed to notify that the screen is turning on", e2);
                }
            }
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyKeyguardGoingAway() {
        WallpaperConnection wallpaperConnection;
        synchronized (this.mLock) {
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                for (WallpaperData wallpaperData : getActiveWallpapers()) {
                    wallpaperData.connection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda21
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WallpaperManagerService.lambda$notifyKeyguardGoingAway$12((WallpaperManagerService.DisplayConnector) obj);
                        }
                    });
                }
                return;
            }
            WallpaperData wallpaperData2 = this.mWallpaperMap.get(this.mCurrentUserId);
            if (wallpaperData2 != null && (wallpaperConnection = wallpaperData2.connection) != null) {
                wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda22
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        WallpaperManagerService.lambda$notifyKeyguardGoingAway$13((WallpaperManagerService.DisplayConnector) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyKeyguardGoingAway$12(DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.keyguardgoingaway", -1, -1, -1, new Bundle());
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to notify that the keyguard is going away", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyKeyguardGoingAway$13(DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.keyguardgoingaway", -1, -1, -1, new Bundle());
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to notify that the keyguard is going away", e);
            }
        }
    }

    public boolean setLockWallpaperCallback(IWallpaperManagerCallback iWallpaperManagerCallback) {
        checkPermission("android.permission.INTERNAL_SYSTEM_WINDOW");
        synchronized (this.mLock) {
            this.mKeyguardListener = iWallpaperManagerCallback;
        }
        return true;
    }

    private WallpaperData[] getActiveWallpapers() {
        WallpaperData wallpaperData = this.mWallpaperMap.get(this.mCurrentUserId);
        WallpaperData wallpaperData2 = this.mLockWallpaperMap.get(this.mCurrentUserId);
        boolean z = (wallpaperData == null || wallpaperData.connection == null) ? false : true;
        boolean z2 = (wallpaperData2 == null || wallpaperData2.connection == null) ? false : true;
        if (z && z2) {
            return new WallpaperData[]{wallpaperData, wallpaperData2};
        }
        if (z) {
            return new WallpaperData[]{wallpaperData};
        }
        return z2 ? new WallpaperData[]{wallpaperData2} : new WallpaperData[0];
    }

    private IWallpaperEngine getEngine(int i, int i2, int i3) {
        WallpaperConnection wallpaperConnection;
        WallpaperData findWallpaperAtDisplay = findWallpaperAtDisplay(i2, i3);
        IWallpaperEngine iWallpaperEngine = null;
        if (findWallpaperAtDisplay == null || (wallpaperConnection = findWallpaperAtDisplay.connection) == null) {
            return null;
        }
        synchronized (this.mLock) {
            for (int i4 = 0; i4 < wallpaperConnection.mDisplayConnector.size(); i4++) {
                int i5 = ((DisplayConnector) wallpaperConnection.mDisplayConnector.get(i4)).mDisplayId;
                int i6 = ((DisplayConnector) wallpaperConnection.mDisplayConnector.get(i4)).mDisplayId;
                if (i5 == i3 || i6 == i) {
                    iWallpaperEngine = ((DisplayConnector) wallpaperConnection.mDisplayConnector.get(i4)).mEngine;
                    break;
                }
            }
        }
        return iWallpaperEngine;
    }

    public void addOnLocalColorsChangedListener(ILocalWallpaperColorConsumer iLocalWallpaperColorConsumer, List<RectF> list, int i, int i2, int i3) throws RemoteException {
        if (i != 2 && i != 1) {
            throw new IllegalArgumentException("which should be either FLAG_LOCK or FLAG_SYSTEM");
        }
        IWallpaperEngine engine = getEngine(i, i2, i3);
        if (engine == null) {
            return;
        }
        synchronized (this.mLock) {
            this.mLocalColorRepo.addAreas(iLocalWallpaperColorConsumer, list, i3);
        }
        engine.addLocalColorsAreas(list);
    }

    public void removeOnLocalColorsChangedListener(ILocalWallpaperColorConsumer iLocalWallpaperColorConsumer, List<RectF> list, int i, int i2, int i3) throws RemoteException {
        if (i != 2 && i != 1) {
            throw new IllegalArgumentException("which should be either FLAG_LOCK or FLAG_SYSTEM");
        }
        if (Binder.getCallingUserHandle().getIdentifier() != i2) {
            throw new SecurityException("calling user id does not match");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        List<RectF> list2 = null;
        try {
            synchronized (this.mLock) {
                list2 = this.mLocalColorRepo.removeAreas(iLocalWallpaperColorConsumer, list, i3);
            }
        } catch (Exception unused) {
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        IWallpaperEngine engine = getEngine(i, i2, i3);
        if (engine == null || list2 == null || list2.size() <= 0) {
            return;
        }
        engine.removeLocalColorsAreas(list2);
    }

    public boolean lockScreenWallpaperExists() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(this.mCurrentUserId) != null;
        }
        return z;
    }

    public boolean isStaticWallpaper(int i) {
        synchronized (this.mLock) {
            WallpaperData wallpaperData = (i == 2 ? this.mLockWallpaperMap : this.mWallpaperMap).get(this.mCurrentUserId);
            if (wallpaperData == null) {
                return false;
            }
            return this.mImageWallpaper.equals(wallpaperData.wallpaperComponent);
        }
    }

    public void setWallpaperDimAmount(float f) throws RemoteException {
        setWallpaperDimAmountForUid(Binder.getCallingUid(), f);
    }

    public void setWallpaperDimAmountForUid(final int i, final float f) {
        WallpaperConnection wallpaperConnection;
        checkPermission("android.permission.SET_WALLPAPER_DIM_AMOUNT");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(this.mCurrentUserId);
                WallpaperData wallpaperData2 = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(this.mCurrentUserId);
                if (f == 0.0f) {
                    wallpaperData.mUidToDimAmount.remove(i);
                    this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(this.mCurrentUserId, 1, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda4
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WallpaperManagerService.lambda$setWallpaperDimAmountForUid$14(i, (WallpaperData) obj);
                        }
                    });
                } else {
                    wallpaperData.mUidToDimAmount.put(i, Float.valueOf(f));
                    this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(this.mCurrentUserId, 1, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda5
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            WallpaperManagerService.lambda$setWallpaperDimAmountForUid$15(i, f, (WallpaperData) obj);
                        }
                    });
                }
                final float highestDimAmountFromMap = getHighestDimAmountFromMap(wallpaperData.mUidToDimAmount);
                wallpaperData.mWallpaperDimAmount = highestDimAmountFromMap;
                this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(this.mCurrentUserId, 1, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((WallpaperData) obj).mWallpaperDimAmount = highestDimAmountFromMap;
                    }
                });
                this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(this.mCurrentUserId, 2, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((WallpaperData) obj).mWallpaperDimAmount = highestDimAmountFromMap;
                    }
                });
                if (wallpaperData2 != null) {
                    wallpaperData2.mWallpaperDimAmount = highestDimAmountFromMap;
                }
                if (this.mIsLockscreenLiveWallpaperEnabled) {
                    boolean z = false;
                    for (WallpaperData wallpaperData3 : getActiveWallpapers()) {
                        if (wallpaperData3 != null && (wallpaperConnection = wallpaperData3.connection) != null) {
                            wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda8
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    WallpaperManagerService.lambda$setWallpaperDimAmountForUid$18(highestDimAmountFromMap, (WallpaperManagerService.DisplayConnector) obj);
                                }
                            });
                            wallpaperData3.mIsColorExtractedFromDim = true;
                            notifyWallpaperColorsChanged(wallpaperData3, wallpaperData3.mWhich);
                            z = true;
                        }
                    }
                    if (z) {
                        saveSettingsLocked(wallpaperData.userId);
                    }
                } else {
                    WallpaperConnection wallpaperConnection2 = wallpaperData.connection;
                    if (wallpaperConnection2 != null) {
                        wallpaperConnection2.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda9
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                WallpaperManagerService.lambda$setWallpaperDimAmountForUid$19(highestDimAmountFromMap, (WallpaperManagerService.DisplayConnector) obj);
                            }
                        });
                        wallpaperData.mIsColorExtractedFromDim = true;
                        this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(this.mCurrentUserId, 1, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda10
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((WallpaperData) obj).mIsColorExtractedFromDim = true;
                            }
                        });
                        this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(this.mCurrentUserId, 2, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda11
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ((WallpaperData) obj).mIsColorExtractedFromDim = true;
                            }
                        });
                        notifyWallpaperColorsChanged(wallpaperData, 1);
                        if (wallpaperData2 != null) {
                            wallpaperData2.mIsColorExtractedFromDim = true;
                            notifyWallpaperColorsChanged(wallpaperData2, 2);
                        }
                        this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLockedForAffectedPhysicalDisplays(this.mCurrentUserId, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(1, IWallpaperManagerServiceExt.ALL_PHYSICAL_DISPLAY_IDS));
                    }
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setWallpaperDimAmountForUid$14(int i, WallpaperData wallpaperData) {
        wallpaperData.mUidToDimAmount.remove(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setWallpaperDimAmountForUid$15(int i, float f, WallpaperData wallpaperData) {
        wallpaperData.mUidToDimAmount.put(i, Float.valueOf(f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setWallpaperDimAmountForUid$18(float f, DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.applyDimming(f);
            } catch (RemoteException e) {
                Slog.w(TAG, "Can't apply dimming on wallpaper display connector", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setWallpaperDimAmountForUid$19(float f, DisplayConnector displayConnector) {
        IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
        if (iWallpaperEngine != null) {
            try {
                iWallpaperEngine.applyDimming(f);
            } catch (RemoteException e) {
                Slog.w(TAG, "Can't apply dimming on wallpaper display connector", e);
            }
        }
    }

    public float getWallpaperDimAmount() {
        checkPermission("android.permission.SET_WALLPAPER_DIM_AMOUNT");
        synchronized (this.mLock) {
            WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(this.mCurrentUserId);
            if (wallpaperData == null && (wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(0)) == null) {
                Slog.e(TAG, "getWallpaperDimAmount: wallpaperData is null");
                return 0.0f;
            }
            return wallpaperData.mWallpaperDimAmount;
        }
    }

    private float getHighestDimAmountFromMap(SparseArray<Float> sparseArray) {
        float f = 0.0f;
        for (int i = 0; i < sparseArray.size(); i++) {
            f = Math.max(f, sparseArray.valueAt(i).floatValue());
        }
        return f;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0053  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public WallpaperColors getWallpaperColors(int i, int i2, int i3) throws RemoteException {
        boolean z;
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(i)) {
            throw new IllegalArgumentException("which should be either FLAG_LOCK or FLAG_SYSTEM");
        }
        int handleIncomingUser = ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i2, false, true, "getWallpaperColors", null);
        synchronized (this.mLock) {
            WallpaperData wallpaperForWallpaperColors = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForWallpaperColors(i, handleIncomingUser, i3);
            if (wallpaperForWallpaperColors == null) {
                if (i == 2) {
                    wallpaperForWallpaperColors = this.mLockWallpaperMap.get(handleIncomingUser);
                }
                if (wallpaperForWallpaperColors == null) {
                    wallpaperForWallpaperColors = findWallpaperAtDisplay(handleIncomingUser, i3);
                }
            }
            if (wallpaperForWallpaperColors == null) {
                return null;
            }
            if (wallpaperForWallpaperColors.primaryColors != null && !wallpaperForWallpaperColors.mIsColorExtractedFromDim) {
                z = false;
                if (z) {
                    extractColors(wallpaperForWallpaperColors);
                }
                return getAdjustedWallpaperColorsOnDimming(wallpaperForWallpaperColors);
            }
            z = true;
            if (z) {
            }
            return getAdjustedWallpaperColorsOnDimming(wallpaperForWallpaperColors);
        }
    }

    WallpaperColors getAdjustedWallpaperColorsOnDimming(WallpaperData wallpaperData) {
        synchronized (this.mLock) {
            WallpaperColors wallpaperColors = wallpaperData.primaryColors;
            if (wallpaperColors == null || (wallpaperColors.getColorHints() & 4) != 0 || wallpaperData.mWallpaperDimAmount == 0.0f) {
                return wallpaperColors;
            }
            return new WallpaperColors(wallpaperColors.getPrimaryColor(), wallpaperColors.getSecondaryColor(), wallpaperColors.getTertiaryColor(), wallpaperColors.getColorHints() & (-2) & (-3));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WallpaperData findWallpaperAtDisplay(int i, int i2) {
        WallpaperConnection wallpaperConnection;
        WallpaperData wallpaperData = this.mFallbackWallpaper;
        if (wallpaperData != null && (wallpaperConnection = wallpaperData.connection) != null && wallpaperConnection.containsDisplay(i2)) {
            return this.mFallbackWallpaper;
        }
        return this.mWallpaperManagerServiceWrapper.getExtImpl().findWallpaperAtDisplay(i, i2, 1, this.mWallpaperMap.get(i));
    }

    public ParcelFileDescriptor setWallpaper(String str, String str2, Rect rect, boolean z, Bundle bundle, int i, IWallpaperManagerCallback iWallpaperManagerCallback, int i2) {
        ParcelFileDescriptor updateWallpaperBitmapLocked;
        int handleIncomingUser = ActivityManager.handleIncomingUser(IWallpaperManager.Stub.getCallingPid(), IWallpaperManager.Stub.getCallingUid(), i2, false, true, "changing wallpaper", null);
        checkPermission("android.permission.SET_WALLPAPER");
        if ((i & 3) == 0) {
            Slog.e(TAG, "Must specify a valid wallpaper category to set");
            throw new IllegalArgumentException("Must specify a valid wallpaper category to set");
        }
        if (!isWallpaperSupported(str2) || !isSetWallpaperAllowed(str2)) {
            return null;
        }
        if (rect == null) {
            rect = new Rect(0, 0, 0, 0);
        } else if (rect.width() < 0 || rect.height() < 0 || rect.left < 0 || rect.top < 0) {
            throw new IllegalArgumentException("Invalid crop rect supplied: " + rect);
        }
        synchronized (this.mLock) {
            if (DEBUG) {
                Slog.v(TAG, "setWallpaper which=0x" + Integer.toHexString(i));
            }
            WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, this.mWallpaperMap).get(handleIncomingUser);
            boolean z2 = wallpaperData != null && this.mImageWallpaper.equals(wallpaperData.wallpaperComponent);
            boolean z3 = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, this.mLockWallpaperMap).get(handleIncomingUser) == null;
            if (!this.mWallpaperManagerServiceWrapper.getExtImpl().shouldCommetOriginCode() && i == 1 && z2 && z3) {
                Slog.i(TAG, "Migrating current wallpaper to be lock-only before updating system wallpaper");
                if (!migrateStaticSystemToLockWallpaperLocked(handleIncomingUser) && !isLockscreenLiveWallpaperEnabled()) {
                    i |= 2;
                }
            }
            WallpaperData wallpaperSafeLocked = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(handleIncomingUser, i);
            if (wallpaperSafeLocked == null) {
                wallpaperSafeLocked = getWallpaperSafeLocked(handleIncomingUser, i);
            }
            if (this.mPendingMigrationViaStatic != null) {
                Slog.w(TAG, "Starting new static wp migration before previous migration finished");
            }
            this.mPendingMigrationViaStatic = new WallpaperDestinationChangeHandler(wallpaperSafeLocked);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                updateWallpaperBitmapLocked = updateWallpaperBitmapLocked(str, wallpaperSafeLocked, bundle);
                if (updateWallpaperBitmapLocked != null) {
                    wallpaperSafeLocked.imageWallpaperPending = true;
                    wallpaperSafeLocked.mSystemWasBoth = z3;
                    wallpaperSafeLocked.mWhich = this.mWallpaperManagerServiceWrapper.getExtImpl().formatWhichPending(i);
                    wallpaperSafeLocked.setComplete = iWallpaperManagerCallback;
                    wallpaperSafeLocked.fromForegroundApp = isFromForegroundApp(str2);
                    wallpaperSafeLocked.cropHint.set(rect);
                    wallpaperSafeLocked.allowBackup = z;
                    wallpaperSafeLocked.mWallpaperDimAmount = getWallpaperDimAmount();
                    this.mWallpaperManagerServiceExt.clearFlipClockTextColorIfNeed(this.mContext, wallpaperSafeLocked.mWhich, handleIncomingUser);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return updateWallpaperBitmapLocked;
    }

    private boolean migrateStaticSystemToLockWallpaperLocked(int i) {
        WallpaperData wallpaperData = this.mWallpaperMap.get(i);
        if (wallpaperData == null) {
            if (DEBUG) {
                Slog.i(TAG, "No system wallpaper?  Not tracking for lock-only");
            }
            return true;
        }
        int currentPhysicalDisplayIdLocked = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentPhysicalDisplayIdLocked();
        WallpaperData wallpaperData2 = new WallpaperData(i, this.mWallpaperManagerServiceExt.getWhichValue(2, currentPhysicalDisplayIdLocked));
        wallpaperData2.wallpaperId = wallpaperData.wallpaperId;
        wallpaperData2.cropHint.set(wallpaperData.cropHint);
        wallpaperData2.allowBackup = wallpaperData.allowBackup;
        wallpaperData2.primaryColors = wallpaperData.primaryColors;
        wallpaperData2.mWallpaperDimAmount = wallpaperData.mWallpaperDimAmount;
        wallpaperData2.mWhich = 2;
        try {
            Os.rename(wallpaperData.wallpaperFile.getAbsolutePath(), wallpaperData2.wallpaperFile.getAbsolutePath());
            Os.rename(wallpaperData.cropFile.getAbsolutePath(), wallpaperData2.cropFile.getAbsolutePath());
            this.mWallpaperManagerServiceExt.getWallpaperMap(2, currentPhysicalDisplayIdLocked, this.mLockWallpaperMap).put(i, wallpaperData2);
            if (this.mIsLockscreenLiveWallpaperEnabled) {
                SELinux.restorecon(wallpaperData2.wallpaperFile);
                this.mLastLockWallpaper = wallpaperData2;
            }
            return true;
        } catch (ErrnoException e) {
            Slog.w(TAG, "Couldn't migrate system wallpaper: " + e.getMessage());
            wallpaperData2.wallpaperFile.delete();
            wallpaperData2.cropFile.delete();
            return false;
        }
    }

    ParcelFileDescriptor updateWallpaperBitmapLocked(String str, WallpaperData wallpaperData, Bundle bundle) {
        if (str == null) {
            str = "";
        }
        try {
            File wallpaperDir = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperDir(wallpaperData.userId, wallpaperData.mWallpaperDataExt.getPhysicalDisplayId(), WallpaperUtils.getWallpaperDir(wallpaperData.userId));
            if (!wallpaperDir.exists()) {
                wallpaperDir.mkdir();
                FileUtils.setPermissions(wallpaperDir.getPath(), 505, -1, -1);
            }
            ParcelFileDescriptor open = ParcelFileDescriptor.open(wallpaperData.wallpaperFile, 1006632960);
            if (!SELinux.restorecon(wallpaperData.wallpaperFile)) {
                Slog.w(TAG, "restorecon failed for wallpaper file: " + wallpaperData.wallpaperFile.getPath());
                return null;
            }
            wallpaperData.name = str;
            int makeWallpaperIdLocked = WallpaperUtils.makeWallpaperIdLocked();
            wallpaperData.wallpaperId = makeWallpaperIdLocked;
            if (bundle != null) {
                bundle.putInt("android.service.wallpaper.extra.ID", makeWallpaperIdLocked);
            }
            wallpaperData.primaryColors = null;
            Slog.v(TAG, "updateWallpaperBitmapLocked() : id=" + wallpaperData.wallpaperId + " name=" + str + " file=" + wallpaperData.wallpaperFile.getName());
            return open;
        } catch (FileNotFoundException e) {
            Slog.w(TAG, "Error setting wallpaper", e);
            return null;
        }
    }

    public void setWallpaperComponentChecked(ComponentName componentName, String str, int i, int i2) {
        if (isWallpaperSupported(str) && isSetWallpaperAllowed(str) && !this.mWallpaperManagerServiceWrapper.getExtImpl().setWallpaperComponent(componentName, i2, str, this.mActivityManager)) {
            setWallpaperComponent(componentName, str, i, i2);
        }
    }

    public void setWallpaperComponent(ComponentName componentName) {
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().setWallpaperComponent(componentName, UserHandle.getCallingUserId())) {
            return;
        }
        setWallpaperComponent(componentName, "", UserHandle.getCallingUserId(), 1);
    }

    @VisibleForTesting
    void setWallpaperComponent(ComponentName componentName, String str, int i, int i2) {
        if (this.mIsLockscreenLiveWallpaperEnabled) {
            setWallpaperComponentInternal(componentName, str, i, i2);
        } else {
            setWallpaperComponentInternalLegacy(componentName, str, i, i2);
        }
    }

    private void setWallpaperComponentInternal(ComponentName componentName, String str, int i, int i2) {
        boolean z;
        WallpaperData wallpaperSafeLocked;
        if (DEBUG) {
            Slog.v(TAG, "Setting new live wallpaper: which=" + i + ", component: " + componentName);
        }
        int handleIncomingUser = ActivityManager.handleIncomingUser(IWallpaperManager.Stub.getCallingPid(), IWallpaperManager.Stub.getCallingUid(), i2, false, true, "changing live wallpaper", null);
        checkPermission("android.permission.SET_WALLPAPER_COMPONENT");
        synchronized (this.mLock) {
            Slog.v(TAG, "setWallpaperComponent name=" + componentName);
            WallpaperData wallpaperData = this.mWallpaperMap.get(handleIncomingUser);
            if (wallpaperData == null) {
                throw new IllegalStateException("Wallpaper not yet initialized for user " + handleIncomingUser);
            }
            boolean equals = this.mImageWallpaper.equals(wallpaperData.wallpaperComponent);
            z = false;
            boolean z2 = this.mLockWallpaperMap.get(handleIncomingUser) == null;
            if (i == 1 && z2 && equals) {
                Slog.i(TAG, "Migrating current wallpaper to be lock-only beforeupdating system wallpaper");
                migrateStaticSystemToLockWallpaperLocked(handleIncomingUser);
            }
            wallpaperSafeLocked = getWallpaperSafeLocked(handleIncomingUser, i);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                wallpaperSafeLocked.imageWallpaperPending = false;
                wallpaperSafeLocked.mWhich = i;
                wallpaperSafeLocked.mSystemWasBoth = z2;
                wallpaperSafeLocked.fromForegroundApp = isFromForegroundApp(str);
                final WallpaperDestinationChangeHandler wallpaperDestinationChangeHandler = new WallpaperDestinationChangeHandler(wallpaperSafeLocked);
                boolean changingToSame = changingToSame(componentName, wallpaperSafeLocked);
                if (bindWallpaperComponentLocked(componentName, changingToSame && z2 && i == 1, true, wallpaperSafeLocked, new IRemoteCallback.Stub() { // from class: com.android.server.wallpaper.WallpaperManagerService.5
                    public void sendResult(Bundle bundle) throws RemoteException {
                        if (WallpaperManagerService.DEBUG) {
                            Slog.d(WallpaperManagerService.TAG, "publish system wallpaper changed!");
                        }
                        wallpaperDestinationChangeHandler.complete();
                    }
                })) {
                    if (!changingToSame) {
                        wallpaperSafeLocked.primaryColors = null;
                    } else {
                        WallpaperConnection wallpaperConnection = wallpaperSafeLocked.connection;
                        if (wallpaperConnection != null) {
                            wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda20
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    WallpaperManagerService.lambda$setWallpaperComponentInternal$22((WallpaperManagerService.DisplayConnector) obj);
                                }
                            });
                        }
                    }
                    wallpaperSafeLocked.wallpaperId = WallpaperUtils.makeWallpaperIdLocked();
                    notifyCallbacksLocked(wallpaperSafeLocked);
                    if (i == 3) {
                        if (DEBUG) {
                            Slog.v(TAG, "Lock screen wallpaper changed to same as home");
                        }
                        WallpaperData wallpaperData2 = this.mLockWallpaperMap.get(wallpaperSafeLocked.userId);
                        if (wallpaperData2 != null) {
                            detachWallpaperLocked(wallpaperData2);
                            if (changingToSame) {
                                updateEngineFlags(wallpaperSafeLocked);
                            }
                        }
                        this.mLockWallpaperMap.remove(wallpaperSafeLocked.userId);
                    }
                    z = true;
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        if (z) {
            notifyWallpaperColorsChanged(wallpaperSafeLocked, i);
            notifyWallpaperColorsChanged(this.mFallbackWallpaper, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setWallpaperComponentInternal$22(DisplayConnector displayConnector) {
        try {
            IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
            if (iWallpaperEngine != null) {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.reapply", 0, 0, 0, (Bundle) null);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Error sending apply message to wallpaper", e);
        }
    }

    private void setWallpaperComponentInternalLegacy(ComponentName componentName, String str, int i, int i2) {
        WallpaperData wallpaperData;
        int i3;
        boolean z;
        int handleIncomingUser = ActivityManager.handleIncomingUser(IWallpaperManager.Stub.getCallingPid(), IWallpaperManager.Stub.getCallingUid(), i2, false, true, "changing live wallpaper", null);
        checkPermission("android.permission.SET_WALLPAPER_COMPONENT");
        synchronized (this.mLock) {
            Slog.v(TAG, "setWallpaperComponent name=" + componentName + ", which=" + i);
            int cachePhysicalDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
            wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, cachePhysicalDisplayId, this.mWallpaperMap).get(handleIncomingUser);
            if (wallpaperData == null) {
                throw new IllegalStateException("Wallpaper not yet initialized for user " + handleIncomingUser);
            }
            Slog.i(TAG, "display id: " + cachePhysicalDisplayId + " wallpaper displayID: " + wallpaperData.mWallpaperDataExt.getPhysicalDisplayId() + " wallpaper name: " + wallpaperData.wallpaperComponent);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            if (!this.mWallpaperManagerServiceWrapper.getExtImpl().shouldCommetOriginCode() && this.mImageWallpaper.equals(wallpaperData.wallpaperComponent) && this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, this.mLockWallpaperMap).get(handleIncomingUser) == null) {
                Slog.i(TAG, "Migrating current wallpaper to be lock-only beforeupdating system wallpaper");
                if (!migrateStaticSystemToLockWallpaperLocked(handleIncomingUser)) {
                    i |= 2;
                }
            }
            i3 = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, cachePhysicalDisplayId, this.mLockWallpaperMap).get(handleIncomingUser) == null ? 3 : 1;
            z = false;
            try {
                wallpaperData.imageWallpaperPending = false;
                wallpaperData.mWhich = i;
                this.mWallpaperManagerServiceWrapper.getExtImpl().forEachPhysicalDisplayWallpaperLocked(handleIncomingUser, 1, new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((WallpaperData) obj).imageWallpaperPending = false;
                    }
                });
                wallpaperData.fromForegroundApp = isFromForegroundApp(str);
                boolean changingToSame = changingToSame(componentName, wallpaperData);
                if (this.mWallpaperManagerServiceWrapper.getExtImpl().bindWallpaperComponentCheck(componentName, wallpaperData, changingToSame && this.mLockWallpaperMap.get(handleIncomingUser) != null && i == 3)) {
                    if (!changingToSame) {
                        wallpaperData.primaryColors = null;
                    } else {
                        WallpaperConnection wallpaperConnection = wallpaperData.connection;
                        if (wallpaperConnection != null) {
                            wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda3
                                @Override // java.util.function.Consumer
                                public final void accept(Object obj) {
                                    WallpaperManagerService.lambda$setWallpaperComponentInternalLegacy$24((WallpaperManagerService.DisplayConnector) obj);
                                }
                            });
                        }
                    }
                    wallpaperData.wallpaperId = WallpaperUtils.makeWallpaperIdLocked();
                    this.mWallpaperManagerServiceWrapper.getExtImpl().setFromForegroundAppLocked(wallpaperData);
                    if (this.mWallpaperManagerServiceWrapper.getExtImpl().shouldNotifyCallbacks(wallpaperData)) {
                        notifyCallbacksLocked(wallpaperData);
                        z = true;
                    }
                    this.mWallpaperManagerServiceWrapper.getExtImpl().updateWallpaperBitmap();
                    IWallpaperManagerServiceExt iWallpaperManagerServiceExt = this.mWallpaperManagerServiceExt;
                    iWallpaperManagerServiceExt.clearFlipClockTextColorIfNeed(this.mContext, iWallpaperManagerServiceExt.getWhichValue(1, cachePhysicalDisplayId), handleIncomingUser);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        if (z) {
            notifyWallpaperColorsChanged(wallpaperData, i3);
            notifyWallpaperColorsChanged(this.mFallbackWallpaper, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setWallpaperComponentInternalLegacy$24(DisplayConnector displayConnector) {
        try {
            IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
            if (iWallpaperEngine != null) {
                iWallpaperEngine.dispatchWallpaperCommand("android.wallpaper.reapply", 0, 0, 0, (Bundle) null);
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Error sending apply message to wallpaper", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean changingToSame(ComponentName componentName, WallpaperData wallpaperData) {
        if (wallpaperData.connection == null) {
            return false;
        }
        ComponentName componentName2 = wallpaperData.wallpaperComponent;
        if (componentName2 == null) {
            if (componentName != null) {
                return false;
            }
            if (DEBUG) {
                Slog.v(TAG, "changingToSame: still using default");
            }
            return true;
        }
        if (!componentName2.equals(componentName)) {
            return false;
        }
        if (DEBUG) {
            Slog.v(TAG, "same wallpaper");
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x0335 A[Catch: all -> 0x0342, TRY_LEAVE, TryCatch #2 {all -> 0x0342, blocks: (B:81:0x0238, B:83:0x0253, B:84:0x0258, B:86:0x0284, B:88:0x0297, B:91:0x029e, B:92:0x02a3, B:93:0x02a4, B:96:0x02d4, B:98:0x02e7, B:99:0x030b, B:102:0x02eb, B:104:0x02fb, B:106:0x0301, B:108:0x0309, B:109:0x02b5, B:111:0x02b9, B:112:0x02bd, B:114:0x02c3, B:116:0x02c7, B:118:0x02cf, B:120:0x031a, B:122:0x0335, B:125:0x033c, B:126:0x0341), top: B:10:0x0047 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x033c A[Catch: all -> 0x0342, TRY_ENTER, TryCatch #2 {all -> 0x0342, blocks: (B:81:0x0238, B:83:0x0253, B:84:0x0258, B:86:0x0284, B:88:0x0297, B:91:0x029e, B:92:0x02a3, B:93:0x02a4, B:96:0x02d4, B:98:0x02e7, B:99:0x030b, B:102:0x02eb, B:104:0x02fb, B:106:0x0301, B:108:0x0309, B:109:0x02b5, B:111:0x02b9, B:112:0x02bd, B:114:0x02c3, B:116:0x02c7, B:118:0x02cf, B:120:0x031a, B:122:0x0335, B:125:0x033c, B:126:0x0341), top: B:10:0x0047 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x009e A[Catch: all -> 0x0315, RemoteException -> 0x0318, TRY_ENTER, TryCatch #4 {all -> 0x0315, blocks: (B:152:0x0049, B:154:0x0059, B:158:0x006c, B:159:0x006e, B:14:0x007f, B:19:0x008d, B:23:0x009e, B:25:0x00b6, B:28:0x00d3, B:30:0x00dd, B:32:0x00f0, B:35:0x00f7, B:36:0x00fc, B:37:0x00fd, B:39:0x0106, B:41:0x010e, B:42:0x0129, B:44:0x012f, B:46:0x0141, B:49:0x014b, B:52:0x0180, B:54:0x0193, B:57:0x019a, B:58:0x019f, B:60:0x01a3, B:62:0x01a9, B:64:0x01b7, B:66:0x01ca, B:69:0x01d1, B:70:0x01d6, B:71:0x01d7, B:75:0x01e7, B:77:0x021a, B:78:0x022e, B:130:0x015d, B:133:0x0164, B:134:0x0169, B:137:0x016d, B:140:0x0174, B:141:0x0179, B:143:0x017a), top: B:151:0x0049 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    boolean bindWallpaperComponentLocked(ComponentName componentName, boolean z, boolean z2, WallpaperData wallpaperData, IRemoteCallback iRemoteCallback) {
        ComponentName componentName2;
        TimingsTraceAndSlog timingsTraceAndSlog;
        IWallpaperManagerServiceExt extImpl;
        boolean z3;
        WallpaperInfo wallpaperInfo;
        ComponentName componentName3 = componentName;
        Slog.v(TAG, "bindWallpaperComponentLocked: componentName=" + componentName3);
        if (!z && changingToSame(componentName3, wallpaperData)) {
            return true;
        }
        TimingsTraceAndSlog timingsTraceAndSlog2 = new TimingsTraceAndSlog(TAG);
        timingsTraceAndSlog2.traceBegin("WPMS.bindWallpaperComponentLocked-" + componentName3);
        boolean z4 = componentName3 == null;
        try {
            try {
                if (componentName3 == null) {
                    try {
                        try {
                            ComponentName defaultWallpaperComponent = this.mWallpaperManagerServiceWrapper.getExtImpl().getDefaultWallpaperComponent(this.mContext, wallpaperData.userId);
                            this.mDefaultWallpaperComponent = defaultWallpaperComponent;
                            try {
                                if (this.mWallpaperManagerServiceWrapper.getExtImpl().needDefaultImageWallpaper(this.mContext, wallpaperData.userId)) {
                                    defaultWallpaperComponent = null;
                                }
                                if (defaultWallpaperComponent == null) {
                                    componentName3 = this.mImageWallpaper;
                                    Slog.v(TAG, "No default component; using image wallpaper");
                                } else {
                                    componentName2 = defaultWallpaperComponent;
                                    extImpl = this.mWallpaperManagerServiceWrapper.getExtImpl();
                                    if (!z && !z4) {
                                        z3 = false;
                                        if (!extImpl.bindSharedWallpaperComponentLocked(componentName2, z3, z2, wallpaperData, iRemoteCallback)) {
                                            timingsTraceAndSlog2.traceEnd();
                                            return true;
                                        }
                                        int serviceUserId = this.mWallpaperManagerServiceWrapper.getExtImpl().getServiceUserId(wallpaperData.userId, this.mImageWallpaper, componentName2);
                                        ServiceInfo serviceInfo = this.mIPackageManager.getServiceInfo(componentName2, 4224L, serviceUserId);
                                        if (serviceInfo == null) {
                                            Slog.w(TAG, "Attempted wallpaper " + componentName2 + " is unavailable");
                                            timingsTraceAndSlog2.traceEnd();
                                            return false;
                                        }
                                        if (!"android.permission.BIND_WALLPAPER".equals(serviceInfo.permission)) {
                                            String str = "Selected service does not have android.permission.BIND_WALLPAPER: " + componentName2;
                                            if (z2) {
                                                throw new SecurityException(str);
                                            }
                                            Slog.w(TAG, str);
                                            timingsTraceAndSlog2.traceEnd();
                                            return false;
                                        }
                                        Intent intent = new Intent("android.service.wallpaper.WallpaperService");
                                        if (componentName2 == null || componentName2.equals(this.mImageWallpaper)) {
                                            wallpaperInfo = null;
                                        } else {
                                            List list = this.mIPackageManager.queryIntentServices(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), 128L, serviceUserId).getList();
                                            int i = 0;
                                            while (true) {
                                                if (i >= list.size()) {
                                                    wallpaperInfo = null;
                                                    break;
                                                }
                                                ServiceInfo serviceInfo2 = ((ResolveInfo) list.get(i)).serviceInfo;
                                                if (serviceInfo2.name.equals(serviceInfo.name) && serviceInfo2.packageName.equals(serviceInfo.packageName)) {
                                                    try {
                                                        wallpaperInfo = new WallpaperInfo(this.mContext, (ResolveInfo) list.get(i));
                                                        break;
                                                    } catch (IOException e) {
                                                        if (z2) {
                                                            throw new IllegalArgumentException(e);
                                                        }
                                                        Slog.w(TAG, e);
                                                        timingsTraceAndSlog2.traceEnd();
                                                        return false;
                                                    } catch (XmlPullParserException e2) {
                                                        if (z2) {
                                                            throw new IllegalArgumentException(e2);
                                                        }
                                                        Slog.w(TAG, e2);
                                                        timingsTraceAndSlog2.traceEnd();
                                                        return false;
                                                    }
                                                }
                                                i++;
                                            }
                                            if (wallpaperInfo == null) {
                                                String str2 = "Selected service is not a wallpaper: " + componentName2;
                                                if (z2) {
                                                    throw new SecurityException(str2);
                                                }
                                                Slog.w(TAG, str2);
                                                timingsTraceAndSlog2.traceEnd();
                                                return false;
                                            }
                                        }
                                        if (wallpaperInfo != null && wallpaperInfo.supportsAmbientMode() && this.mIPackageManager.checkPermission("android.permission.AMBIENT_WALLPAPER", wallpaperInfo.getPackageName(), serviceUserId) != 0) {
                                            String str3 = "Selected service does not have android.permission.AMBIENT_WALLPAPER: " + componentName2;
                                            if (z2) {
                                                throw new SecurityException(str3);
                                            }
                                            Slog.w(TAG, str3);
                                            timingsTraceAndSlog2.traceEnd();
                                            return false;
                                        }
                                        if (!this.mWallpaperManagerServiceWrapper.getExtImpl().isAvailableSetWallpaperFlagOnBind(wallpaperData, wallpaperInfo)) {
                                            timingsTraceAndSlog2.traceEnd();
                                            return false;
                                        }
                                        PendingIntent activityAsUser = PendingIntent.getActivityAsUser(this.mContext, 0, Intent.createChooser(new Intent("android.intent.action.SET_WALLPAPER"), this.mContext.getText(R.string.config_customAdbWifiNetworkConfirmationSecondaryUserComponent)), 67108864, ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2).toBundle(), UserHandle.of(serviceUserId));
                                        if (DEBUG) {
                                            Slog.v(TAG, "Binding to:" + componentName2);
                                        }
                                        timingsTraceAndSlog = timingsTraceAndSlog2;
                                        try {
                                            WallpaperConnection wallpaperConnection = new WallpaperConnection(wallpaperInfo, wallpaperData, this.mIPackageManager.getPackageUid(componentName2.getPackageName(), 268435456L, serviceUserId));
                                            intent.setComponent(componentName2);
                                            intent.putExtra("android.intent.extra.client_label", 17041851);
                                            intent.putExtra("android.intent.extra.client_intent", activityAsUser);
                                            if (iRemoteCallback != null) {
                                                intent.setIdentifier("fromSwitchingUser");
                                            }
                                            if (!this.mContext.bindServiceAsUser(intent, wallpaperConnection, this.mWallpaperManagerServiceWrapper.getExtImpl().getBindWallpaperServiceFlag(570429441, wallpaperData), this.mWallpaperManagerServiceWrapper.getExtImpl().getEventHandler(this.mContext), new UserHandle(serviceUserId))) {
                                                String str4 = "Unable to bind service: " + componentName2;
                                                if (z2) {
                                                    throw new IllegalArgumentException(str4);
                                                }
                                                Slog.w(TAG, str4);
                                                timingsTraceAndSlog.traceEnd();
                                                return false;
                                            }
                                            if (!this.mWallpaperManagerServiceWrapper.getExtImpl().detachLastWallpaperLocked(this.mCurrentUserId, wallpaperData, this.mFallbackWallpaper)) {
                                                if (this.mIsLockscreenLiveWallpaperEnabled) {
                                                    maybeDetachLastWallpapers(wallpaperData);
                                                } else if (wallpaperData.userId == this.mCurrentUserId && this.mLastWallpaper != null && !wallpaperData.equals(this.mFallbackWallpaper)) {
                                                    detachWallpaperLocked(this.mLastWallpaper);
                                                }
                                            }
                                            wallpaperData.wallpaperComponent = componentName2;
                                            wallpaperData.connection = wallpaperConnection;
                                            this.mWallpaperManagerServiceWrapper.getExtImpl().initWallpaperOnBindWallpaperComponentLocked(wallpaperData);
                                            wallpaperConnection.mReply = iRemoteCallback;
                                            if (this.mIsLockscreenLiveWallpaperEnabled) {
                                                updateCurrentWallpapers(wallpaperData);
                                            } else if (!this.mWallpaperManagerServiceWrapper.getExtImpl().setLastWallpaper(this.mCurrentUserId, wallpaperData, this.mFallbackWallpaper) && wallpaperData.userId == this.mCurrentUserId && !wallpaperData.equals(this.mFallbackWallpaper)) {
                                                this.mLastWallpaper = wallpaperData;
                                            }
                                            updateFallbackConnection();
                                            timingsTraceAndSlog.traceEnd();
                                            return true;
                                        } catch (RemoteException e3) {
                                            e = e3;
                                            String str5 = "Remote exception for " + componentName2 + "\n" + e;
                                            if (z2) {
                                                throw new IllegalArgumentException(str5);
                                            }
                                            Slog.w(TAG, str5);
                                            timingsTraceAndSlog.traceEnd();
                                            return false;
                                        }
                                    }
                                    z3 = true;
                                    if (!extImpl.bindSharedWallpaperComponentLocked(componentName2, z3, z2, wallpaperData, iRemoteCallback)) {
                                    }
                                }
                            } catch (RemoteException e4) {
                                e = e4;
                                componentName2 = defaultWallpaperComponent;
                                timingsTraceAndSlog = timingsTraceAndSlog2;
                                String str52 = "Remote exception for " + componentName2 + "\n" + e;
                                if (z2) {
                                }
                            }
                        } catch (RemoteException e5) {
                            e = e5;
                            componentName2 = componentName3;
                        }
                    } catch (Throwable th) {
                        th = th;
                        timingsTraceAndSlog2.traceEnd();
                        throw th;
                    }
                }
                extImpl = this.mWallpaperManagerServiceWrapper.getExtImpl();
                if (!z) {
                    z3 = false;
                    if (!extImpl.bindSharedWallpaperComponentLocked(componentName2, z3, z2, wallpaperData, iRemoteCallback)) {
                    }
                }
                z3 = true;
                if (!extImpl.bindSharedWallpaperComponentLocked(componentName2, z3, z2, wallpaperData, iRemoteCallback)) {
                }
            } catch (RemoteException e6) {
                e = e6;
                timingsTraceAndSlog = timingsTraceAndSlog2;
                String str522 = "Remote exception for " + componentName2 + "\n" + e;
                if (z2) {
                }
            }
            componentName2 = componentName3;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void updateCurrentWallpapers(WallpaperData wallpaperData) {
        if (wallpaperData.userId != this.mCurrentUserId || wallpaperData.equals(this.mFallbackWallpaper)) {
            return;
        }
        int i = wallpaperData.mWhich;
        if (i == 3) {
            this.mLastWallpaper = wallpaperData;
        } else if (i == 1) {
            this.mLastWallpaper = wallpaperData;
        } else if (i == 2) {
            this.mLastLockWallpaper = wallpaperData;
        }
    }

    private void maybeDetachLastWallpapers(WallpaperData wallpaperData) {
        if (wallpaperData.userId != this.mCurrentUserId || wallpaperData.equals(this.mFallbackWallpaper)) {
            return;
        }
        int i = wallpaperData.mWhich;
        boolean z = false;
        boolean z2 = (i & 1) != 0;
        boolean z3 = (i & 2) != 0;
        if (wallpaperData.mSystemWasBoth && !z3) {
            z = true;
        }
        WallpaperData wallpaperData2 = this.mLastWallpaper;
        if (wallpaperData2 != null && z2 && !z) {
            detachWallpaperLocked(wallpaperData2);
        }
        WallpaperData wallpaperData3 = this.mLastLockWallpaper;
        if (wallpaperData3 == null || !z3) {
            return;
        }
        detachWallpaperLocked(wallpaperData3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detachWallpaperLocked(final WallpaperData wallpaperData) {
        if (wallpaperData.connection != null) {
            if (DEBUG) {
                Slog.v(TAG, "Detaching wallpaper: " + wallpaperData);
            }
            if (this.mWallpaperManagerServiceWrapper.getExtImpl().detachSharedWallpaperLocked(wallpaperData)) {
                return;
            }
            IRemoteCallback iRemoteCallback = wallpaperData.connection.mReply;
            if (iRemoteCallback != null) {
                try {
                    iRemoteCallback.sendResult((Bundle) null);
                } catch (RemoteException e) {
                    Slog.w(TAG, "Error sending reply to wallpaper before disconnect", e);
                }
                wallpaperData.connection.mReply = null;
            }
            wallpaperData.connection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda18
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WallpaperManagerService.lambda$detachWallpaperLocked$25(WallpaperData.this, (WallpaperManagerService.DisplayConnector) obj);
                }
            });
            WallpaperConnection wallpaperConnection = wallpaperData.connection;
            wallpaperConnection.mService = null;
            wallpaperConnection.mDisplayConnector.clear();
            FgThread.getHandler().removeCallbacks(wallpaperData.connection.mResetRunnable);
            this.mContext.getMainThreadHandler().removeCallbacks(wallpaperData.connection.mDisconnectRunnable);
            this.mContext.getMainThreadHandler().removeCallbacks(wallpaperData.connection.mTryToRebindRunnable);
            if (!this.mWallpaperManagerServiceExt.unBindServiceForSeparateWallpaper(this.mContext, wallpaperData)) {
                this.mContext.unbindService(wallpaperData.connection);
            }
            wallpaperData.connection = null;
            if (wallpaperData == this.mLastWallpaper) {
                this.mLastWallpaper = null;
            }
            if (wallpaperData == this.mLastLockWallpaper) {
                this.mLastLockWallpaper = null;
            }
            this.mWallpaperManagerServiceWrapper.getExtImpl().removeLastWallpaperLocked(wallpaperData);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$detachWallpaperLocked$25(WallpaperData wallpaperData, DisplayConnector displayConnector) {
        displayConnector.disconnectLocked(wallpaperData.connection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEngineFlags(final WallpaperData wallpaperData) {
        WallpaperConnection wallpaperConnection = wallpaperData.connection;
        if (wallpaperConnection == null) {
            return;
        }
        wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda12
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WallpaperManagerService.this.lambda$updateEngineFlags$26(wallpaperData, (WallpaperManagerService.DisplayConnector) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEngineFlags$26(WallpaperData wallpaperData, DisplayConnector displayConnector) {
        try {
            IWallpaperEngine iWallpaperEngine = displayConnector.mEngine;
            if (iWallpaperEngine != null) {
                iWallpaperEngine.setWallpaperFlags(wallpaperData.mWhich);
                this.mWindowManagerInternal.setWallpaperShowWhenLocked(displayConnector.mToken, (wallpaperData.mWhich & 2) != 0);
            }
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to update wallpaper engine flags", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearWallpaperComponentLocked(WallpaperData wallpaperData) {
        wallpaperData.wallpaperComponent = null;
        detachWallpaperLocked(wallpaperData);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachServiceLocked(final WallpaperConnection wallpaperConnection, final WallpaperData wallpaperData) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG);
        timingsTraceAndSlog.traceBegin("WPMS.attachServiceLocked");
        wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((WallpaperManagerService.DisplayConnector) obj).connectLocked(WallpaperManagerService.WallpaperConnection.this, wallpaperData);
            }
        });
        timingsTraceAndSlog.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCallbacksLocked(WallpaperData wallpaperData) {
        int beginBroadcast = wallpaperData.callbacks.beginBroadcast();
        for (int i = 0; i < beginBroadcast; i++) {
            try {
                wallpaperData.callbacks.getBroadcastItem(i).onWallpaperChanged();
            } catch (RemoteException e) {
                Slog.w(TAG, "Failed to notify callbacks about wallpaper changes", e);
            }
        }
        wallpaperData.callbacks.finishBroadcast();
        Intent intent = new Intent("android.intent.action.WALLPAPER_CHANGED");
        intent.putExtra("android.service.wallpaper.extra.FROM_FOREGROUND_APP", wallpaperData.fromForegroundApp);
        this.mContext.sendBroadcastAsUser(intent, new UserHandle(this.mCurrentUserId));
    }

    private void checkPermission(String str) {
        if (hasPermission(str)) {
            return;
        }
        throw new SecurityException("Access denied to process: " + Binder.getCallingPid() + ", must have permission " + str);
    }

    private boolean packageBelongsToUid(String str, int i) {
        try {
            return this.mContext.getPackageManager().getPackageUidAsUser(str, UserHandle.getUserId(i)) == i;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private void enforcePackageBelongsToUid(String str, int i) {
        if (packageBelongsToUid(str, i)) {
            return;
        }
        throw new IllegalArgumentException("Invalid package or package does not belong to uid:" + i);
    }

    private boolean isFromForegroundApp(final String str) {
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda25
            public final Object getOrThrow() {
                Boolean lambda$isFromForegroundApp$28;
                lambda$isFromForegroundApp$28 = WallpaperManagerService.this.lambda$isFromForegroundApp$28(str);
                return lambda$isFromForegroundApp$28;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isFromForegroundApp$28(String str) throws Exception {
        return Boolean.valueOf(this.mActivityManager.getPackageImportance(str) == 100);
    }

    private void checkCallerIsSystemOrSystemUi() {
        if (Binder.getCallingUid() != Process.myUid() && this.mContext.checkCallingPermission("android.permission.STATUS_BAR_SERVICE") != 0) {
            throw new SecurityException("Access denied: only system processes can call this");
        }
    }

    public boolean isWallpaperSupported(String str) {
        int callingUid = Binder.getCallingUid();
        enforcePackageBelongsToUid(str, callingUid);
        return this.mAppOpsManager.checkOpNoThrow(48, callingUid, str) == 0;
    }

    public boolean isSetWallpaperAllowed(String str) {
        if (!Arrays.asList(this.mContext.getPackageManager().getPackagesForUid(Binder.getCallingUid())).contains(str)) {
            return false;
        }
        DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        if (devicePolicyManagerInternal != null && devicePolicyManagerInternal.isDeviceOrProfileOwnerInCallingUser(str)) {
            return true;
        }
        if (!this.mWallpaperManagerServiceWrapper.getExtImpl().isSetWallpaperAllowed(str, this.mContext)) {
            return false;
        }
        int callingUserId = UserHandle.getCallingUserId();
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return !((UserManagerInternal) LocalServices.getService(UserManagerInternal.class)).hasUserRestriction("no_set_wallpaper", callingUserId);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isWallpaperBackupEligible(int i, int i2) {
        WallpaperData wallpaperData;
        if (i == 2) {
            wallpaperData = this.mLockWallpaperMap.get(i2);
        } else {
            wallpaperData = this.mWallpaperMap.get(i2);
        }
        if (wallpaperData != null) {
            return wallpaperData.allowBackup;
        }
        return false;
    }

    public boolean isLockscreenLiveWallpaperEnabled() {
        return this.mIsLockscreenLiveWallpaperEnabled;
    }

    public boolean isMultiCropEnabled() {
        return this.mIsMultiCropEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDisplayReadyInternal(int i) {
        synchronized (this.mLock) {
            WallpaperData wallpaperData = this.mLastWallpaper;
            if (wallpaperData == null) {
                return;
            }
            if (supportsMultiDisplay(wallpaperData.connection)) {
                DisplayConnector displayConnectorOrCreate = this.mLastWallpaper.connection.getDisplayConnectorOrCreate(i);
                if (displayConnectorOrCreate == null) {
                    return;
                }
                WallpaperData wallpaperData2 = this.mLastWallpaper;
                displayConnectorOrCreate.connectLocked(wallpaperData2.connection, wallpaperData2);
                return;
            }
            WallpaperData wallpaperData3 = this.mFallbackWallpaper;
            if (wallpaperData3 != null) {
                DisplayConnector displayConnectorOrCreate2 = wallpaperData3.connection.getDisplayConnectorOrCreate(i);
                if (displayConnectorOrCreate2 == null) {
                    return;
                }
                WallpaperData wallpaperData4 = this.mFallbackWallpaper;
                displayConnectorOrCreate2.connectLocked(wallpaperData4.connection, wallpaperData4);
            } else {
                Slog.w(TAG, "No wallpaper can be added to the new display");
            }
        }
    }

    void saveSettingsLocked(int i) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG);
        timingsTraceAndSlog.traceBegin("WPMS.saveSettingsLocked-" + i);
        int cachePhysicalDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
        this.mWallpaperDataParser.saveSettingsLocked(i, this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, cachePhysicalDisplayId, this.mWallpaperMap).get(i), this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, cachePhysicalDisplayId, this.mLockWallpaperMap).get(i));
        timingsTraceAndSlog.traceEnd();
    }

    WallpaperData getWallpaperSafeLocked(int i, int i2) {
        SparseArray<WallpaperData> wallpaperMap;
        int cachePhysicalDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
        int wallpaperType = this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(i2);
        if (wallpaperType == 2) {
            wallpaperMap = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, cachePhysicalDisplayId, this.mLockWallpaperMap);
        } else {
            wallpaperMap = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, cachePhysicalDisplayId, this.mWallpaperMap);
        }
        WallpaperData wallpaperData = wallpaperMap.get(i);
        if (wallpaperData != null) {
            return wallpaperData;
        }
        loadSettingsLocked(i, false, wallpaperType == 2 ? 2 : 1);
        WallpaperData wallpaperData2 = wallpaperMap.get(i);
        if (wallpaperData2 != null) {
            return wallpaperData2;
        }
        if (wallpaperType == 2) {
            WallpaperData wallpaperData3 = new WallpaperData(i, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(2, cachePhysicalDisplayId));
            wallpaperData3.mWallpaperDataExt.setPhysicalDisplayId(cachePhysicalDisplayId);
            wallpaperMap.put(i, wallpaperData3);
            return wallpaperData3;
        }
        Slog.wtf(TAG, "Didn't find wallpaper in non-lock case!");
        WallpaperData wallpaperData4 = new WallpaperData(i, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(1, cachePhysicalDisplayId));
        wallpaperData4.mWallpaperDataExt.setPhysicalDisplayId(cachePhysicalDisplayId);
        wallpaperMap.put(i, wallpaperData4);
        return wallpaperData4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSettingsLocked(int i, boolean z, int i2) {
        initializeFallbackWallpaper();
        this.mWallpaperManagerServiceWrapper.getExtImpl().initSubDisplayOnLoadSettingsLocked(i);
        int cachePhysicalDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
        WallpaperDataParser.WallpaperLoadingResult loadSettingsLocked = this.mWallpaperDataParser.loadSettingsLocked(i, z, this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, cachePhysicalDisplayId, this.mWallpaperMap).get(i), this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, cachePhysicalDisplayId, this.mLockWallpaperMap).get(i), i2);
        boolean z2 = this.mIsLockscreenLiveWallpaperEnabled;
        boolean z3 = (z2 && (i2 & 1) == 0) ? false : true;
        boolean z4 = (z2 && (i2 & 2) == 0) ? false : true;
        if (z3) {
            this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, cachePhysicalDisplayId, this.mWallpaperMap).put(i, loadSettingsLocked.getSystemWallpaperData());
        }
        if (z4) {
            if (loadSettingsLocked.success()) {
                this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, cachePhysicalDisplayId, this.mLockWallpaperMap).put(i, loadSettingsLocked.getLockWallpaperData());
            } else {
                this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, cachePhysicalDisplayId, this.mLockWallpaperMap).remove(i);
            }
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().onLoadSettingsEnd(loadSettingsLocked.getSystemWallpaperData());
    }

    private void initializeFallbackWallpaper() {
        if (this.mFallbackWallpaper == null) {
            if (DEBUG) {
                Slog.d(TAG, "Initialize fallback wallpaper");
            }
            WallpaperData wallpaperData = new WallpaperData(0, 1);
            this.mFallbackWallpaper = wallpaperData;
            wallpaperData.allowBackup = false;
            wallpaperData.wallpaperId = WallpaperUtils.makeWallpaperIdLocked();
            bindWallpaperComponentLocked(this.mImageWallpaper, true, false, this.mFallbackWallpaper, null);
        }
    }

    public void settingsRestored() {
        WallpaperData wallpaperData;
        boolean z;
        if (Binder.getCallingUid() != 1000) {
            throw new RuntimeException("settingsRestored() can only be called from the system process");
        }
        if (DEBUG) {
            Slog.v(TAG, "settingsRestored");
        }
        synchronized (this.mLock) {
            loadSettingsLocked(0, false, 3);
            wallpaperData = this.mWallpaperMap.get(0);
            wallpaperData.wallpaperId = WallpaperUtils.makeWallpaperIdLocked();
            z = true;
            wallpaperData.allowBackup = true;
            ComponentName componentName = wallpaperData.nextWallpaperComponent;
            if (componentName != null && !componentName.equals(this.mImageWallpaper)) {
                if (!bindWallpaperComponentLocked(wallpaperData.nextWallpaperComponent, false, false, wallpaperData, null)) {
                    bindWallpaperComponentLocked(null, false, false, wallpaperData, null);
                }
            } else {
                if ("".equals(wallpaperData.name)) {
                    if (DEBUG) {
                        Slog.v(TAG, "settingsRestored: name is empty");
                    }
                } else {
                    if (DEBUG) {
                        Slog.v(TAG, "settingsRestored: attempting to restore named resource");
                    }
                    z = this.mWallpaperDataParser.restoreNamedResourceLocked(wallpaperData);
                }
                if (DEBUG) {
                    Slog.v(TAG, "settingsRestored: success=" + z + " id=" + wallpaperData.wallpaperId);
                }
                if (z) {
                    this.mWallpaperCropper.generateCrop(wallpaperData);
                    bindWallpaperComponentLocked(wallpaperData.nextWallpaperComponent, true, false, wallpaperData, null);
                }
            }
        }
        if (!z) {
            Slog.e(TAG, "Failed to restore wallpaper: '" + wallpaperData.name + "'");
            wallpaperData.name = "";
            WallpaperUtils.getWallpaperDir(0).delete();
        }
        synchronized (this.mLock) {
            saveSettingsLocked(0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
        new WallpaperManagerShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
    }

    private void dumpWallpaper(WallpaperData wallpaperData, final PrintWriter printWriter) {
        if (wallpaperData == null) {
            printWriter.println(" (null entry)");
            return;
        }
        printWriter.print(" User ");
        printWriter.print(wallpaperData.userId);
        printWriter.print(": id=");
        printWriter.print(wallpaperData.wallpaperId);
        printWriter.print(": mWhich=");
        printWriter.print(wallpaperData.mWhich);
        printWriter.print(": mSystemWasBoth=");
        printWriter.println(wallpaperData.mSystemWasBoth);
        printWriter.println(" Display state:");
        printWriter.println(" setting display:" + wallpaperData.mWallpaperDataExt.getPhysicalDisplayId());
        this.mWallpaperDisplayHelper.forEachDisplayData(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WallpaperManagerService.lambda$dumpWallpaper$29(printWriter, (WallpaperDisplayHelper.DisplayData) obj);
            }
        });
        printWriter.print("  mCropHint=");
        printWriter.println(wallpaperData.cropHint);
        printWriter.print("  mName=");
        printWriter.println(wallpaperData.name);
        printWriter.print("  mAllowBackup=");
        printWriter.println(wallpaperData.allowBackup);
        printWriter.print("  mWallpaperComponent=");
        printWriter.println(wallpaperData.wallpaperComponent);
        printWriter.print("  mWallpaperDimAmount=");
        printWriter.println(wallpaperData.mWallpaperDimAmount);
        printWriter.print("  isColorExtracted=");
        printWriter.println(wallpaperData.mIsColorExtractedFromDim);
        printWriter.println("  mUidToDimAmount:");
        for (int i = 0; i < wallpaperData.mUidToDimAmount.size(); i++) {
            printWriter.print("    UID=");
            printWriter.print(wallpaperData.mUidToDimAmount.keyAt(i));
            printWriter.print(" dimAmount=");
            printWriter.println(wallpaperData.mUidToDimAmount.valueAt(i));
        }
        WallpaperConnection wallpaperConnection = wallpaperData.connection;
        if (wallpaperConnection != null) {
            printWriter.print("  Wallpaper connection ");
            printWriter.print(wallpaperConnection);
            printWriter.println(":");
            if (wallpaperConnection.mInfo != null) {
                printWriter.print("    mInfo.component=");
                printWriter.println(wallpaperConnection.mInfo.getComponent());
            }
            wallpaperConnection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda17
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WallpaperManagerService.lambda$dumpWallpaper$30(printWriter, (WallpaperManagerService.DisplayConnector) obj);
                }
            });
            printWriter.print("    mService=");
            printWriter.println(wallpaperConnection.mService);
            printWriter.print("    mLastDiedTime=");
            printWriter.println(wallpaperData.lastDiedTime - SystemClock.uptimeMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWallpaper$29(PrintWriter printWriter, WallpaperDisplayHelper.DisplayData displayData) {
        printWriter.print("  displayId=");
        printWriter.println(displayData.mDisplayId);
        printWriter.print("  mWidth=");
        printWriter.print(displayData.mWidth);
        printWriter.print("  mHeight=");
        printWriter.println(displayData.mHeight);
        printWriter.print("  mPadding=");
        printWriter.println(displayData.mPadding);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dumpWallpaper$30(PrintWriter printWriter, DisplayConnector displayConnector) {
        printWriter.print("     mDisplayId=");
        printWriter.println(displayConnector.mDisplayId);
        printWriter.print("     mToken=");
        printWriter.println(displayConnector.mToken);
        printWriter.print("     mEngine=");
        printWriter.println(displayConnector.mEngine);
    }

    protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            printWriter.print("mDefaultWallpaperComponent=");
            printWriter.println(this.mDefaultWallpaperComponent);
            printWriter.print("mImageWallpaper=");
            printWriter.println(this.mImageWallpaper);
            synchronized (this.mLock) {
                printWriter.println("System wallpaper state:");
                Iterator<WallpaperData> it = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpapersLocked(1, this.mWallpaperMap).iterator();
                while (it.hasNext()) {
                    dumpWallpaper(it.next(), printWriter);
                }
                printWriter.println("Lock wallpaper state:");
                Iterator<WallpaperData> it2 = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpapersLocked(2, this.mLockWallpaperMap).iterator();
                while (it2.hasNext()) {
                    dumpWallpaper(it2.next(), printWriter);
                }
                printWriter.println("Fallback wallpaper state:");
                WallpaperData wallpaperData = this.mFallbackWallpaper;
                if (wallpaperData != null) {
                    dumpWallpaper(wallpaperData, printWriter);
                }
                printWriter.print("mIsLockscreenLiveWallpaperEnabled=");
                printWriter.println(this.mIsLockscreenLiveWallpaperEnabled);
            }
        }
    }

    public IWallpaperManagerServiceWrapper getWrapper() {
        return this.mWallpaperManagerServiceWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class WallpaperManagerServiceWrapperImpl implements IWallpaperManagerServiceWrapper {
        private WallpaperManagerServiceWrapperImpl() {
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public IWallpaperManagerServiceExt getExtImpl() {
            return WallpaperManagerService.this.mWallpaperManagerServiceExt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IWallpaperManagerExt getManagerExtImpl() {
            return WallpaperManagerService.this.mWallpaperManagerExt;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void removeDisplayData(int i) {
            WallpaperManagerService.this.mWallpaperDisplayHelper.removeDisplayData(i);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public Context getContext() {
            return WallpaperManagerService.this.mContext;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public Object getLock() {
            return WallpaperManagerService.this.mLock;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public ComponentName getImageWallpaper() {
            return WallpaperManagerService.this.mImageWallpaper;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void notifyCallbacksLocked(WallpaperData wallpaperData) {
            WallpaperManagerService.this.notifyCallbacksLocked(wallpaperData);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public int getCurrentUserId() {
            return WallpaperManagerService.this.mCurrentUserId;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public SparseArray<WallpaperData> getWallpaperMap() {
            return WallpaperManagerService.this.mWallpaperMap;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public SparseArray<WallpaperData> getLockWallpaperMap() {
            return WallpaperManagerService.this.mLockWallpaperMap;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void loadSettingsLocked(int i, boolean z, int i2) {
            WallpaperManagerService.this.loadSettingsLocked(i, z, i2);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public WallpaperData getFallbackWallpaper() {
            return WallpaperManagerService.this.mFallbackWallpaper;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void notifyWallpaperColorsChangedOnDisplay(WallpaperData wallpaperData, int i, int i2) {
            WallpaperManagerService.this.notifyWallpaperColorsChangedOnDisplay(wallpaperData, i, i2);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public String[] getPerUserFiles() {
            return new String[]{"wallpaper_orig", "wallpaper", "wallpaper_lock_orig", "wallpaper_lock", "wallpaper_info.xml"};
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void clearWallpaperComponentLocked(WallpaperData wallpaperData) {
            WallpaperManagerService.this.clearWallpaperComponentLocked(wallpaperData);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void notifyLockWallpaperChanged() {
            WallpaperManagerService.this.notifyLockWallpaperChanged();
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void detachWallpaperLocked(WallpaperData wallpaperData) {
            WallpaperManagerService.this.detachWallpaperLocked(wallpaperData);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void updateFallbackConnection() {
            WallpaperManagerService.this.updateFallbackConnection();
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public RemoteCallbackList<IWallpaperManagerCallback> getWallpaperCallbacks(WallpaperData wallpaperData) {
            return wallpaperData.callbacks;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void setWallpaperComponent(ComponentName componentName, int i) {
            WallpaperManagerService.this.setWallpaperComponent(componentName, "", 3, i);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public ComponentName getDefaultWallpaperComponent() {
            return WallpaperManagerService.this.mDefaultWallpaperComponent;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public boolean hasPermission(String str) {
            return WallpaperManagerService.this.hasPermission(str);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public WallpaperData findWallpaperAtDisplay(int i, int i2) {
            return WallpaperManagerService.this.findWallpaperAtDisplay(i, i2);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public boolean changingToSame(ComponentName componentName, WallpaperData wallpaperData) {
            return WallpaperManagerService.this.changingToSame(componentName, wallpaperData);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public SparseArray<DisplayConnector> getDisplayConnectors(WallpaperConnection wallpaperConnection) {
            return wallpaperConnection.mDisplayConnector;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void initDisplayState(WallpaperConnection wallpaperConnection) {
            wallpaperConnection.initDisplayState();
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public ActivityManager getActivityManager() {
            return WallpaperManagerService.this.mActivityManager;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public DisplayManager getDisplayManager() {
            return (DisplayManager) WallpaperManagerService.this.mContext.getSystemService(DisplayManager.class);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void updateLogState(boolean z) {
            Slog.i(WallpaperManagerService.TAG, "updateLogState: on = " + z);
            WallpaperManagerService.DEBUG = z;
        }
    }
}
