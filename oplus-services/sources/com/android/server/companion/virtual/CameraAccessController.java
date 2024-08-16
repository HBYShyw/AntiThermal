package com.android.server.companion.virtual;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraInjectionSession;
import android.hardware.camera2.CameraManager;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class CameraAccessController extends CameraManager.AvailabilityCallback implements AutoCloseable {
    private static final String TAG = "CameraAccessController";
    private final CameraAccessBlockedCallback mBlockedCallback;
    private final CameraManager mCameraManager;
    private final Context mContext;
    private final PackageManager mPackageManager;
    private final UserManager mUserManager;
    private final VirtualDeviceManagerInternal mVirtualDeviceManagerInternal;
    private final Object mLock = new Object();
    private final Object mObserverLock = new Object();

    @GuardedBy({"mObserverLock"})
    private int mObserverCount = 0;

    @GuardedBy({"mLock"})
    private ArrayMap<String, InjectionSessionData> mPackageToSessionData = new ArrayMap<>();

    @GuardedBy({"mLock"})
    private ArrayMap<String, OpenCameraInfo> mAppsToBlockOnVirtualDevice = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface CameraAccessBlockedCallback {
        void onCameraAccessBlocked(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class InjectionSessionData {
        public int appUid;
        public ArrayMap<String, CameraInjectionSession> cameraIdToSession = new ArrayMap<>();

        InjectionSessionData() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    static class OpenCameraInfo {
        public String packageName;
        public Set<Integer> packageUids;

        OpenCameraInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraAccessController(Context context, VirtualDeviceManagerInternal virtualDeviceManagerInternal, CameraAccessBlockedCallback cameraAccessBlockedCallback) {
        this.mContext = context;
        this.mVirtualDeviceManagerInternal = virtualDeviceManagerInternal;
        this.mBlockedCallback = cameraAccessBlockedCallback;
        this.mCameraManager = (CameraManager) context.getSystemService(CameraManager.class);
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
    }

    public int getUserId() {
        return this.mContext.getUserId();
    }

    public int getObserverCount() {
        int i;
        synchronized (this.mObserverLock) {
            i = this.mObserverCount;
        }
        return i;
    }

    public void startObservingIfNeeded() {
        synchronized (this.mObserverLock) {
            if (this.mObserverCount == 0) {
                this.mCameraManager.registerAvailabilityCallback(this.mContext.getMainExecutor(), this);
            }
            this.mObserverCount++;
        }
    }

    public void stopObservingIfNeeded() {
        synchronized (this.mObserverLock) {
            int i = this.mObserverCount - 1;
            this.mObserverCount = i;
            if (i <= 0) {
                close();
            }
        }
    }

    public void blockCameraAccessIfNeeded(Set<Integer> set) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mAppsToBlockOnVirtualDevice.size(); i++) {
                String keyAt = this.mAppsToBlockOnVirtualDevice.keyAt(i);
                OpenCameraInfo openCameraInfo = this.mAppsToBlockOnVirtualDevice.get(keyAt);
                String str = openCameraInfo.packageName;
                Iterator<Integer> it = openCameraInfo.packageUids.iterator();
                while (true) {
                    if (it.hasNext()) {
                        int intValue = it.next().intValue();
                        if (set.contains(Integer.valueOf(intValue))) {
                            if (this.mPackageToSessionData.get(str) == null) {
                                InjectionSessionData injectionSessionData = new InjectionSessionData();
                                injectionSessionData.appUid = intValue;
                                this.mPackageToSessionData.put(str, injectionSessionData);
                            }
                            startBlocking(str, keyAt);
                        }
                    }
                }
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.mObserverLock) {
            int i = this.mObserverCount;
            if (i < 0) {
                Slog.wtf(TAG, "Unexpected negative mObserverCount: " + this.mObserverCount);
            } else if (i > 0) {
                Slog.w(TAG, "Unexpected close with observers remaining: " + this.mObserverCount);
            }
        }
        this.mCameraManager.unregisterAvailabilityCallback(this);
    }

    public void onCameraOpened(String str, String str2) {
        synchronized (this.mLock) {
            InjectionSessionData injectionSessionData = this.mPackageToSessionData.get(str2);
            List aliveUsers = this.mUserManager.getAliveUsers();
            ArraySet arraySet = new ArraySet();
            Iterator it = aliveUsers.iterator();
            while (it.hasNext()) {
                int queryUidFromPackageName = queryUidFromPackageName(((UserInfo) it.next()).getUserHandle().getIdentifier(), str2);
                if (this.mVirtualDeviceManagerInternal.isAppRunningOnAnyVirtualDevice(queryUidFromPackageName)) {
                    if (injectionSessionData == null) {
                        injectionSessionData = new InjectionSessionData();
                        injectionSessionData.appUid = queryUidFromPackageName;
                        this.mPackageToSessionData.put(str2, injectionSessionData);
                    }
                    if (injectionSessionData.cameraIdToSession.containsKey(str)) {
                        return;
                    }
                    startBlocking(str2, str);
                    return;
                }
                if (queryUidFromPackageName != -1) {
                    arraySet.add(Integer.valueOf(queryUidFromPackageName));
                }
            }
            OpenCameraInfo openCameraInfo = new OpenCameraInfo();
            openCameraInfo.packageName = str2;
            openCameraInfo.packageUids = arraySet;
            this.mAppsToBlockOnVirtualDevice.put(str, openCameraInfo);
            CameraInjectionSession cameraInjectionSession = injectionSessionData != null ? injectionSessionData.cameraIdToSession.get(str) : null;
            if (cameraInjectionSession != null) {
                cameraInjectionSession.close();
                injectionSessionData.cameraIdToSession.remove(str);
                if (injectionSessionData.cameraIdToSession.isEmpty()) {
                    this.mPackageToSessionData.remove(str2);
                }
            }
        }
    }

    public void onCameraClosed(String str) {
        synchronized (this.mLock) {
            this.mAppsToBlockOnVirtualDevice.remove(str);
            for (int size = this.mPackageToSessionData.size() - 1; size >= 0; size--) {
                InjectionSessionData valueAt = this.mPackageToSessionData.valueAt(size);
                CameraInjectionSession cameraInjectionSession = valueAt.cameraIdToSession.get(str);
                if (cameraInjectionSession != null) {
                    cameraInjectionSession.close();
                    valueAt.cameraIdToSession.remove(str);
                    if (valueAt.cameraIdToSession.isEmpty()) {
                        this.mPackageToSessionData.removeAt(size);
                    }
                }
            }
        }
    }

    private void startBlocking(final String str, final String str2) {
        try {
            Slog.d(TAG, "startBlocking() cameraId: " + str2 + " packageName: " + str);
            this.mCameraManager.injectCamera(str, str2, "", this.mContext.getMainExecutor(), new CameraInjectionSession.InjectionStatusCallback() { // from class: com.android.server.companion.virtual.CameraAccessController.1
                public void onInjectionSucceeded(CameraInjectionSession cameraInjectionSession) {
                    CameraAccessController.this.onInjectionSucceeded(str2, str, cameraInjectionSession);
                }

                public void onInjectionError(int i) {
                    CameraAccessController.this.onInjectionError(str2, str, i);
                }
            });
        } catch (CameraAccessException e) {
            Slog.e(TAG, "Failed to injectCamera for cameraId:" + str2 + " package:" + str, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInjectionSucceeded(String str, String str2, CameraInjectionSession cameraInjectionSession) {
        synchronized (this.mLock) {
            InjectionSessionData injectionSessionData = this.mPackageToSessionData.get(str2);
            if (injectionSessionData == null) {
                Slog.e(TAG, "onInjectionSucceeded didn't find expected entry for package " + str2);
                cameraInjectionSession.close();
                return;
            }
            CameraInjectionSession put = injectionSessionData.cameraIdToSession.put(str, cameraInjectionSession);
            if (put != null) {
                Slog.e(TAG, "onInjectionSucceeded found unexpected existing session for camera " + str);
                put.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onInjectionError(String str, String str2, int i) {
        if (i != 2) {
            Slog.e(TAG, "Unexpected injection error code:" + i + " for camera:" + str + " and package:" + str2);
            return;
        }
        synchronized (this.mLock) {
            InjectionSessionData injectionSessionData = this.mPackageToSessionData.get(str2);
            if (injectionSessionData != null) {
                this.mBlockedCallback.onCameraAccessBlocked(injectionSessionData.appUid);
            }
        }
    }

    private int queryUidFromPackageName(int i, String str) {
        try {
            return this.mPackageManager.getApplicationInfoAsUser(str, 1, i).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Slog.w(TAG, "queryUidFromPackageName - unknown package " + str, e);
            return -1;
        }
    }
}
