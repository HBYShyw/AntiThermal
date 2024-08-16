package com.android.server.media;

import android.R;
import android.annotation.RequiresPermission;
import android.app.ActivityManager;
import android.app.UserSwitchObserver;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioPlaybackConfiguration;
import android.media.AudioRoutesInfo;
import android.media.IAudioRoutesObserver;
import android.media.IAudioService;
import android.media.IMediaRouter2;
import android.media.IMediaRouter2Manager;
import android.media.IMediaRouterClient;
import android.media.IMediaRouterService;
import android.media.MediaRoute2Info;
import android.media.MediaRouterClientState;
import android.media.RemoteDisplayState;
import android.media.RouteDiscoveryPreference;
import android.media.RouteListingPreference;
import android.media.RoutingSessionInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.DumpUtils;
import com.android.server.LocalServices;
import com.android.server.Watchdog;
import com.android.server.media.AudioPlayerStateMonitor;
import com.android.server.media.RemoteDisplayProviderProxy;
import com.android.server.media.RemoteDisplayProviderWatcher;
import com.android.server.pm.UserManagerInternal;
import com.android.server.statusbar.StatusBarManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class MediaRouterService extends IMediaRouterService.Stub implements Watchdog.Monitor {
    private static final long CONNECTED_TIMEOUT = 60000;
    private static final long CONNECTING_TIMEOUT = 5000;
    BluetoothDevice mActiveBluetoothDevice;
    private final IntArray mActivePlayerMinPriorityQueue;
    private final IntArray mActivePlayerUidMinPriorityQueue;
    private final AudioPlayerStateMonitor mAudioPlayerStateMonitor;
    int mAudioRouteMainType;
    private final IAudioService mAudioService;
    private final String mBluetoothA2dpRouteId;
    private final Context mContext;
    private final String mDefaultAudioRouteId;
    boolean mGlobalBluetoothA2dpOn;
    private final Handler mHandler;
    private final BroadcastReceiver mReceiver;
    private final MediaRouter2ServiceImpl mService2;
    private final UserManagerInternal mUserManagerInternal;
    private static final String TAG = "MediaRouterService";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final SparseArray<UserRecord> mUserRecords = new SparseArray<>();

    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, ClientRecord> mAllClientRecords = new ArrayMap<>();

    @GuardedBy({"mLock"})
    private int mCurrentActiveUserId = -1;

    @RequiresPermission("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS")
    public MediaRouterService(Context context) {
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mActivePlayerMinPriorityQueue = new IntArray();
        this.mActivePlayerUidMinPriorityQueue = new IntArray();
        this.mReceiver = new MediaRouterServiceBroadcastReceiver();
        this.mAudioRouteMainType = 0;
        this.mGlobalBluetoothA2dpOn = false;
        this.mService2 = new MediaRouter2ServiceImpl(context);
        this.mContext = context;
        Watchdog.getInstance().addMonitor(this);
        Resources resources = context.getResources();
        this.mDefaultAudioRouteId = resources.getString(R.string.face_acquired_too_far);
        this.mBluetoothA2dpRouteId = resources.getString(R.string.capability_title_canRetrieveWindowContent);
        this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
        IAudioService asInterface = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
        this.mAudioService = asInterface;
        AudioPlayerStateMonitor audioPlayerStateMonitor = AudioPlayerStateMonitor.getInstance(context);
        this.mAudioPlayerStateMonitor = audioPlayerStateMonitor;
        byte b = 0;
        audioPlayerStateMonitor.registerListener(new AudioPlayerActiveStateChangedListenerImpl(), handler);
        try {
            asInterface.startWatchingRoutes(new AudioRoutesObserverImpl());
        } catch (RemoteException unused) {
            Slog.w(TAG, "RemoteException in the audio service.");
        }
        context.registerReceiverAsUser(this.mReceiver, UserHandle.ALL, new IntentFilter("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED"), null, null);
    }

    @RequiresPermission(anyOf = {"android.permission.INTERACT_ACROSS_USERS", "android.permission.INTERACT_ACROSS_USERS_FULL"})
    public void systemRunning() throws RemoteException {
        ActivityManager.getService().registerUserSwitchObserver(new UserSwitchObserver() { // from class: com.android.server.media.MediaRouterService.1
            public void onUserSwitchComplete(int i) {
                MediaRouterService.this.updateRunningUserAndProfiles(i);
            }
        }, TAG);
        updateRunningUserAndProfiles(ActivityManager.getCurrentUser());
    }

    public void monitor() {
        synchronized (this.mLock) {
        }
    }

    public void registerClientAsUser(IMediaRouterClient iMediaRouterClient, String str, int i) {
        int callingUid = Binder.getCallingUid();
        if (!validatePackageName(callingUid, str)) {
            throw new SecurityException("packageName must match the calling uid");
        }
        int callingPid = Binder.getCallingPid();
        int handleIncomingUser = ActivityManager.handleIncomingUser(callingPid, callingUid, i, false, true, "registerClientAsUser", str);
        boolean z = this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY") == 0;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerClientLocked(iMediaRouterClient, callingUid, callingPid, str, handleIncomingUser, z);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void registerClientGroupId(IMediaRouterClient iMediaRouterClient, String str) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.CONFIGURE_WIFI_DISPLAY") != 0) {
            Log.w(TAG, "Ignoring client group request because the client doesn't have the CONFIGURE_WIFI_DISPLAY permission.");
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                registerClientGroupIdLocked(iMediaRouterClient, str);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void unregisterClient(IMediaRouterClient iMediaRouterClient) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                unregisterClientLocked(iMediaRouterClient, false);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean showMediaOutputSwitcher(String str) {
        if (!validatePackageName(Binder.getCallingUid(), str)) {
            throw new SecurityException("packageName must match the calling identity");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).getPackageImportance(str) > 100) {
                Slog.w(TAG, "showMediaOutputSwitcher only works when called from foreground");
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            }
            synchronized (this.mLock) {
                ((StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class)).showMediaOutputSwitcher(str);
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return true;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    public MediaRouterClientState getState(IMediaRouterClient iMediaRouterClient) {
        MediaRouterClientState stateLocked;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                stateLocked = getStateLocked(iMediaRouterClient);
            }
            return stateLocked;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public boolean isPlaybackActive(IMediaRouterClient iMediaRouterClient) {
        ClientRecord clientRecord;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
            }
            if (clientRecord != null) {
                return this.mAudioPlayerStateMonitor.isPlaybackActive(clientRecord.mUid);
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setBluetoothA2dpOn(IMediaRouterClient iMediaRouterClient, boolean z) {
        if (iMediaRouterClient == null) {
            throw new IllegalArgumentException("client must not be null");
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                this.mAudioService.setBluetoothA2dpOn(z);
            } catch (RemoteException unused) {
                Slog.w(TAG, "RemoteException while calling setBluetoothA2dpOn. on=" + z);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setDiscoveryRequest(IMediaRouterClient iMediaRouterClient, int i, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setDiscoveryRequestLocked(iMediaRouterClient, i, z);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void setSelectedRoute(IMediaRouterClient iMediaRouterClient, String str, boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                setSelectedRouteLocked(iMediaRouterClient, str, z);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void requestSetVolume(IMediaRouterClient iMediaRouterClient, String str, int i) {
        Objects.requireNonNull(str, "routeId must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestSetVolumeLocked(iMediaRouterClient, str, i);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void requestUpdateVolume(IMediaRouterClient iMediaRouterClient, String str, int i) {
        Objects.requireNonNull(str, "routeId must not be null");
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                requestUpdateVolumeLocked(iMediaRouterClient, str, i);
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            printWriter.println("MEDIA ROUTER SERVICE (dumpsys media_router)");
            printWriter.println();
            printWriter.println("Global state");
            printWriter.println("  mCurrentUserId=" + this.mCurrentActiveUserId);
            synchronized (this.mLock) {
                int size = this.mUserRecords.size();
                for (int i = 0; i < size; i++) {
                    UserRecord valueAt = this.mUserRecords.valueAt(i);
                    printWriter.println();
                    valueAt.dump(printWriter, "");
                }
            }
            printWriter.println();
            this.mService2.dump(printWriter, "");
        }
    }

    public boolean verifyPackageExists(String str) {
        return this.mService2.verifyPackageExists(str);
    }

    public List<MediaRoute2Info> getSystemRoutes() {
        return this.mService2.getSystemRoutes();
    }

    public RoutingSessionInfo getSystemSessionInfo() {
        return this.mService2.getSystemSessionInfo(null, false);
    }

    public void registerRouter2(IMediaRouter2 iMediaRouter2, String str) {
        if (!validatePackageName(Binder.getCallingUid(), str)) {
            throw new SecurityException("packageName must match the calling uid");
        }
        this.mService2.registerRouter2(iMediaRouter2, str);
    }

    public void unregisterRouter2(IMediaRouter2 iMediaRouter2) {
        this.mService2.unregisterRouter2(iMediaRouter2);
    }

    public void setDiscoveryRequestWithRouter2(IMediaRouter2 iMediaRouter2, RouteDiscoveryPreference routeDiscoveryPreference) {
        this.mService2.setDiscoveryRequestWithRouter2(iMediaRouter2, routeDiscoveryPreference);
    }

    public void setRouteListingPreference(IMediaRouter2 iMediaRouter2, RouteListingPreference routeListingPreference) {
        this.mService2.setRouteListingPreference(iMediaRouter2, routeListingPreference);
    }

    public void setRouteVolumeWithRouter2(IMediaRouter2 iMediaRouter2, MediaRoute2Info mediaRoute2Info, int i) {
        this.mService2.setRouteVolumeWithRouter2(iMediaRouter2, mediaRoute2Info, i);
    }

    public void requestCreateSessionWithRouter2(IMediaRouter2 iMediaRouter2, int i, long j, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info, Bundle bundle) {
        this.mService2.requestCreateSessionWithRouter2(iMediaRouter2, i, j, routingSessionInfo, mediaRoute2Info, bundle);
    }

    public void selectRouteWithRouter2(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        this.mService2.selectRouteWithRouter2(iMediaRouter2, str, mediaRoute2Info);
    }

    public void deselectRouteWithRouter2(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        this.mService2.deselectRouteWithRouter2(iMediaRouter2, str, mediaRoute2Info);
    }

    public void transferToRouteWithRouter2(IMediaRouter2 iMediaRouter2, String str, MediaRoute2Info mediaRoute2Info) {
        this.mService2.transferToRouteWithRouter2(iMediaRouter2, str, mediaRoute2Info);
    }

    public void setSessionVolumeWithRouter2(IMediaRouter2 iMediaRouter2, String str, int i) {
        this.mService2.setSessionVolumeWithRouter2(iMediaRouter2, str, i);
    }

    public void releaseSessionWithRouter2(IMediaRouter2 iMediaRouter2, String str) {
        this.mService2.releaseSessionWithRouter2(iMediaRouter2, str);
    }

    public List<RoutingSessionInfo> getRemoteSessions(IMediaRouter2Manager iMediaRouter2Manager) {
        return this.mService2.getRemoteSessions(iMediaRouter2Manager);
    }

    public RoutingSessionInfo getSystemSessionInfoForPackage(IMediaRouter2Manager iMediaRouter2Manager, String str) {
        boolean z;
        int identifier = UserHandle.getUserHandleForUid(Binder.getCallingUid()).getIdentifier();
        synchronized (this.mLock) {
            UserRecord userRecord = this.mUserRecords.get(identifier);
            Iterator it = (userRecord != null ? userRecord.mClientRecords : Collections.emptyList()).iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                ClientRecord clientRecord = (ClientRecord) it.next();
                if (TextUtils.equals(clientRecord.mPackageName, str) && this.mDefaultAudioRouteId.equals(clientRecord.mSelectedRouteId)) {
                    z = true;
                    break;
                }
            }
        }
        return this.mService2.getSystemSessionInfo(str, z);
    }

    public void registerManager(IMediaRouter2Manager iMediaRouter2Manager, String str) {
        if (!validatePackageName(Binder.getCallingUid(), str)) {
            throw new SecurityException("packageName must match the calling uid");
        }
        this.mService2.registerManager(iMediaRouter2Manager, str);
    }

    public void unregisterManager(IMediaRouter2Manager iMediaRouter2Manager) {
        this.mService2.unregisterManager(iMediaRouter2Manager);
    }

    public void startScan(IMediaRouter2Manager iMediaRouter2Manager) {
        this.mService2.startScan(iMediaRouter2Manager);
    }

    public void stopScan(IMediaRouter2Manager iMediaRouter2Manager) {
        this.mService2.stopScan(iMediaRouter2Manager);
    }

    public void setRouteVolumeWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, MediaRoute2Info mediaRoute2Info, int i2) {
        this.mService2.setRouteVolumeWithManager(iMediaRouter2Manager, i, mediaRoute2Info, i2);
    }

    public void requestCreateSessionWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
        this.mService2.requestCreateSessionWithManager(iMediaRouter2Manager, i, routingSessionInfo, mediaRoute2Info);
    }

    public void selectRouteWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, MediaRoute2Info mediaRoute2Info) {
        this.mService2.selectRouteWithManager(iMediaRouter2Manager, i, str, mediaRoute2Info);
    }

    public void deselectRouteWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, MediaRoute2Info mediaRoute2Info) {
        this.mService2.deselectRouteWithManager(iMediaRouter2Manager, i, str, mediaRoute2Info);
    }

    public void transferToRouteWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, MediaRoute2Info mediaRoute2Info) {
        this.mService2.transferToRouteWithManager(iMediaRouter2Manager, i, str, mediaRoute2Info);
    }

    public void setSessionVolumeWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str, int i2) {
        this.mService2.setSessionVolumeWithManager(iMediaRouter2Manager, i, str, i2);
    }

    public void releaseSessionWithManager(IMediaRouter2Manager iMediaRouter2Manager, int i, String str) {
        this.mService2.releaseSessionWithManager(iMediaRouter2Manager, i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreBluetoothA2dp() {
        boolean z;
        BluetoothDevice bluetoothDevice;
        try {
            synchronized (this.mLock) {
                z = this.mGlobalBluetoothA2dpOn;
                bluetoothDevice = this.mActiveBluetoothDevice;
            }
            if (bluetoothDevice != null) {
                if (DEBUG) {
                    Slog.d(TAG, "restoreBluetoothA2dp(" + z + ")");
                }
                this.mAudioService.setBluetoothA2dpOn(z);
            }
        } catch (RemoteException unused) {
            Slog.w(TAG, "RemoteException while calling setBluetoothA2dpOn.");
        }
    }

    void restoreRoute(int i) {
        ClientRecord clientRecord;
        ArrayList<ClientRecord> arrayList;
        synchronized (this.mLock) {
            UserRecord userRecord = this.mUserRecords.get(UserHandle.getUserHandleForUid(i).getIdentifier());
            if (userRecord != null && (arrayList = userRecord.mClientRecords) != null) {
                Iterator<ClientRecord> it = arrayList.iterator();
                while (it.hasNext()) {
                    clientRecord = it.next();
                    if (validatePackageName(i, clientRecord.mPackageName)) {
                        break;
                    }
                }
            }
            clientRecord = null;
        }
        if (clientRecord != null) {
            try {
                clientRecord.mClient.onRestoreRoute();
                return;
            } catch (RemoteException unused) {
                Slog.w(TAG, "Failed to call onRestoreRoute. Client probably died.");
                return;
            }
        }
        restoreBluetoothA2dp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRunningUserAndProfiles(int i) {
        synchronized (this.mLock) {
            if (this.mCurrentActiveUserId != i) {
                this.mCurrentActiveUserId = i;
                SparseArray<UserRecord> clone = this.mUserRecords.clone();
                for (int i2 = 0; i2 < clone.size(); i2++) {
                    int keyAt = clone.keyAt(i2);
                    UserRecord valueAt = clone.valueAt(i2);
                    if (isUserActiveLocked(keyAt)) {
                        valueAt.mHandler.sendEmptyMessage(1);
                    } else {
                        valueAt.mHandler.sendEmptyMessage(2);
                        disposeUserIfNeededLocked(valueAt);
                    }
                }
            }
        }
        this.mService2.updateRunningUserAndProfiles(i);
    }

    void clientDied(ClientRecord clientRecord) {
        synchronized (this.mLock) {
            unregisterClientLocked(clientRecord.mClient, true);
        }
    }

    @GuardedBy({"mLock"})
    private void registerClientLocked(IMediaRouterClient iMediaRouterClient, int i, int i2, String str, int i3, boolean z) {
        UserRecord userRecord;
        boolean z2;
        IBinder asBinder = iMediaRouterClient.asBinder();
        if (this.mAllClientRecords.get(asBinder) == null) {
            UserRecord userRecord2 = this.mUserRecords.get(i3);
            if (userRecord2 == null) {
                userRecord = new UserRecord(i3);
                z2 = true;
            } else {
                userRecord = userRecord2;
                z2 = false;
            }
            ClientRecord clientRecord = new ClientRecord(userRecord, iMediaRouterClient, i, i2, str, z);
            try {
                asBinder.linkToDeath(clientRecord, 0);
                if (z2) {
                    this.mUserRecords.put(i3, userRecord);
                    initializeUserLocked(userRecord);
                }
                userRecord.mClientRecords.add(clientRecord);
                this.mAllClientRecords.put(asBinder, clientRecord);
                initializeClientLocked(clientRecord);
            } catch (RemoteException e) {
                throw new RuntimeException("Media router client died prematurely.", e);
            }
        }
    }

    @GuardedBy({"mLock"})
    private void registerClientGroupIdLocked(IMediaRouterClient iMediaRouterClient, String str) {
        ClientRecord clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
        if (clientRecord == null) {
            Log.w(TAG, "Ignoring group id register request of a unregistered client.");
            return;
        }
        if (TextUtils.equals(clientRecord.mGroupId, str)) {
            return;
        }
        UserRecord userRecord = clientRecord.mUserRecord;
        String str2 = clientRecord.mGroupId;
        if (str2 != null) {
            userRecord.removeFromGroup(str2, clientRecord);
        }
        clientRecord.mGroupId = str;
        if (str != null) {
            userRecord.addToGroup(str, clientRecord);
            userRecord.mHandler.obtainMessage(10, str).sendToTarget();
        }
    }

    @GuardedBy({"mLock"})
    private void unregisterClientLocked(IMediaRouterClient iMediaRouterClient, boolean z) {
        ClientRecord remove = this.mAllClientRecords.remove(iMediaRouterClient.asBinder());
        if (remove != null) {
            UserRecord userRecord = remove.mUserRecord;
            userRecord.mClientRecords.remove(remove);
            String str = remove.mGroupId;
            if (str != null) {
                userRecord.removeFromGroup(str, remove);
                remove.mGroupId = null;
            }
            disposeClientLocked(remove, z);
            disposeUserIfNeededLocked(userRecord);
        }
    }

    @GuardedBy({"mLock"})
    private MediaRouterClientState getStateLocked(IMediaRouterClient iMediaRouterClient) {
        ClientRecord clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
        if (clientRecord != null) {
            return clientRecord.getState();
        }
        return null;
    }

    @GuardedBy({"mLock"})
    private void setDiscoveryRequestLocked(IMediaRouterClient iMediaRouterClient, int i, boolean z) {
        ClientRecord clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
        if (clientRecord != null) {
            if (!clientRecord.mTrusted) {
                i &= -5;
            }
            if (clientRecord.mRouteTypes == i && clientRecord.mActiveScan == z) {
                return;
            }
            if (DEBUG) {
                Slog.d(TAG, clientRecord + ": Set discovery request, routeTypes=0x" + Integer.toHexString(i) + ", activeScan=" + z);
            }
            clientRecord.mRouteTypes = i;
            clientRecord.mActiveScan = z;
            clientRecord.mUserRecord.mHandler.sendEmptyMessage(3);
        }
    }

    @GuardedBy({"mLock"})
    private void setSelectedRouteLocked(IMediaRouterClient iMediaRouterClient, String str, boolean z) {
        ClientGroup clientGroup;
        ClientRecord clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
        if (clientRecord != null) {
            String str2 = (this.mDefaultAudioRouteId.equals(clientRecord.mSelectedRouteId) || this.mBluetoothA2dpRouteId.equals(clientRecord.mSelectedRouteId)) ? null : clientRecord.mSelectedRouteId;
            clientRecord.mSelectedRouteId = str;
            if (this.mDefaultAudioRouteId.equals(str) || this.mBluetoothA2dpRouteId.equals(str)) {
                str = null;
            }
            if (Objects.equals(str, str2)) {
                return;
            }
            if (DEBUG) {
                Slog.d(TAG, clientRecord + ": Set selected route, routeId=" + str + ", oldRouteId=" + str2 + ", explicit=" + z);
            }
            if (z && clientRecord.mTrusted) {
                if (str2 != null) {
                    clientRecord.mUserRecord.mHandler.obtainMessage(5, str2).sendToTarget();
                }
                if (str != null) {
                    clientRecord.mUserRecord.mHandler.obtainMessage(4, str).sendToTarget();
                }
                if (clientRecord.mGroupId == null || (clientGroup = (ClientGroup) clientRecord.mUserRecord.mClientGroupMap.get(clientRecord.mGroupId)) == null) {
                    return;
                }
                clientGroup.mSelectedRouteId = str;
                clientRecord.mUserRecord.mHandler.obtainMessage(10, clientRecord.mGroupId).sendToTarget();
            }
        }
    }

    @GuardedBy({"mLock"})
    private void requestSetVolumeLocked(IMediaRouterClient iMediaRouterClient, String str, int i) {
        ClientRecord clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
        if (clientRecord != null) {
            clientRecord.mUserRecord.mHandler.obtainMessage(6, i, 0, str).sendToTarget();
        }
    }

    @GuardedBy({"mLock"})
    private void requestUpdateVolumeLocked(IMediaRouterClient iMediaRouterClient, String str, int i) {
        ClientRecord clientRecord = this.mAllClientRecords.get(iMediaRouterClient.asBinder());
        if (clientRecord != null) {
            clientRecord.mUserRecord.mHandler.obtainMessage(7, i, 0, str).sendToTarget();
        }
    }

    @GuardedBy({"mLock"})
    private void initializeUserLocked(UserRecord userRecord) {
        if (DEBUG) {
            Slog.d(TAG, userRecord + ": Initialized");
        }
        if (isUserActiveLocked(userRecord.mUserId)) {
            userRecord.mHandler.sendEmptyMessage(1);
        }
    }

    @GuardedBy({"mLock"})
    private void disposeUserIfNeededLocked(UserRecord userRecord) {
        if (isUserActiveLocked(userRecord.mUserId) || !userRecord.mClientRecords.isEmpty()) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, userRecord + ": Disposed");
        }
        this.mUserRecords.remove(userRecord.mUserId);
    }

    @GuardedBy({"mLock"})
    private boolean isUserActiveLocked(int i) {
        return this.mUserManagerInternal.getProfileParentId(i) == this.mCurrentActiveUserId;
    }

    @GuardedBy({"mLock"})
    private void initializeClientLocked(ClientRecord clientRecord) {
        if (DEBUG) {
            Slog.d(TAG, clientRecord + ": Registered");
        }
    }

    @GuardedBy({"mLock"})
    private void disposeClientLocked(ClientRecord clientRecord, boolean z) {
        if (DEBUG) {
            if (z) {
                Slog.d(TAG, clientRecord + ": Died!");
            } else {
                Slog.d(TAG, clientRecord + ": Unregistered");
            }
        }
        if (clientRecord.mRouteTypes != 0 || clientRecord.mActiveScan) {
            clientRecord.mUserRecord.mHandler.sendEmptyMessage(3);
        }
        clientRecord.dispose();
    }

    private boolean validatePackageName(int i, String str) {
        String[] packagesForUid;
        if (str != null && (packagesForUid = this.mContext.getPackageManager().getPackagesForUid(i)) != null) {
            for (String str2 : packagesForUid) {
                if (str2.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    final class MediaRouterServiceBroadcastReceiver extends BroadcastReceiver {
        MediaRouterServiceBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED")) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class);
                synchronized (MediaRouterService.this.mLock) {
                    MediaRouterService mediaRouterService = MediaRouterService.this;
                    mediaRouterService.mActiveBluetoothDevice = bluetoothDevice;
                    mediaRouterService.mGlobalBluetoothA2dpOn = bluetoothDevice != null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ClientRecord implements IBinder.DeathRecipient {
        public boolean mActiveScan;
        public final IMediaRouterClient mClient;
        public List<String> mControlCategories;
        public String mGroupId;
        public final String mPackageName;
        public final int mPid;
        public int mRouteTypes;
        public String mSelectedRouteId;
        public final boolean mTrusted;
        public final int mUid;
        public final UserRecord mUserRecord;

        public ClientRecord(UserRecord userRecord, IMediaRouterClient iMediaRouterClient, int i, int i2, String str, boolean z) {
            this.mUserRecord = userRecord;
            this.mClient = iMediaRouterClient;
            this.mUid = i;
            this.mPid = i2;
            this.mPackageName = str;
            this.mTrusted = z;
        }

        public void dispose() {
            this.mClient.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            MediaRouterService.this.clientDied(this);
        }

        MediaRouterClientState getState() {
            if (this.mTrusted) {
                return this.mUserRecord.mRouterState;
            }
            return null;
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + this);
            String str2 = str + "  ";
            printWriter.println(str2 + "mTrusted=" + this.mTrusted);
            printWriter.println(str2 + "mRouteTypes=0x" + Integer.toHexString(this.mRouteTypes));
            printWriter.println(str2 + "mActiveScan=" + this.mActiveScan);
            printWriter.println(str2 + "mSelectedRouteId=" + this.mSelectedRouteId);
        }

        public String toString() {
            return "Client " + this.mPackageName + " (pid " + this.mPid + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class ClientGroup {
        public final List<ClientRecord> mClientRecords = new ArrayList();
        public String mSelectedRouteId;

        ClientGroup() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class UserRecord {
        public final UserHandler mHandler;
        public MediaRouterClientState mRouterState;
        public final int mUserId;
        public final ArrayList<ClientRecord> mClientRecords = new ArrayList<>();
        private final ArrayMap<String, ClientGroup> mClientGroupMap = new ArrayMap<>();

        public UserRecord(int i) {
            this.mUserId = i;
            this.mHandler = new UserHandler(MediaRouterService.this, this);
        }

        public void dump(final PrintWriter printWriter, String str) {
            printWriter.println(str + this);
            final String str2 = str + "  ";
            int size = this.mClientRecords.size();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    this.mClientRecords.get(i).dump(printWriter, str2);
                }
            } else {
                printWriter.println(str2 + "<no clients>");
            }
            printWriter.println(str2 + "State");
            printWriter.println(str2 + "mRouterState=" + this.mRouterState);
            if (this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.media.MediaRouterService.UserRecord.1
                @Override // java.lang.Runnable
                public void run() {
                    UserRecord.this.mHandler.dump(printWriter, str2);
                }
            }, 1000L)) {
                return;
            }
            printWriter.println(str2 + "<could not dump handler state>");
        }

        public void addToGroup(String str, ClientRecord clientRecord) {
            ClientGroup clientGroup = this.mClientGroupMap.get(str);
            if (clientGroup == null) {
                clientGroup = new ClientGroup();
                this.mClientGroupMap.put(str, clientGroup);
            }
            clientGroup.mClientRecords.add(clientRecord);
        }

        public void removeFromGroup(String str, ClientRecord clientRecord) {
            ClientGroup clientGroup = this.mClientGroupMap.get(str);
            if (clientGroup != null) {
                clientGroup.mClientRecords.remove(clientRecord);
                if (clientGroup.mClientRecords.size() == 0) {
                    this.mClientGroupMap.remove(str);
                }
            }
        }

        public String toString() {
            return "User " + this.mUserId;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class UserHandler extends Handler implements RemoteDisplayProviderWatcher.Callback, RemoteDisplayProviderProxy.Callback {
        private static final int MSG_CONNECTION_TIMED_OUT = 9;
        private static final int MSG_NOTIFY_GROUP_ROUTE_SELECTED = 10;
        public static final int MSG_REQUEST_SET_VOLUME = 6;
        public static final int MSG_REQUEST_UPDATE_VOLUME = 7;
        public static final int MSG_SELECT_ROUTE = 4;
        public static final int MSG_START = 1;
        public static final int MSG_STOP = 2;
        public static final int MSG_UNSELECT_ROUTE = 5;
        private static final int MSG_UPDATE_CLIENT_STATE = 8;
        public static final int MSG_UPDATE_DISCOVERY_REQUEST = 3;
        private static final int PHASE_CONNECTED = 2;
        private static final int PHASE_CONNECTING = 1;
        private static final int PHASE_NOT_AVAILABLE = -1;
        private static final int PHASE_NOT_CONNECTED = 0;
        private static final int TIMEOUT_REASON_CONNECTION_LOST = 2;
        private static final int TIMEOUT_REASON_NOT_AVAILABLE = 1;
        private static final int TIMEOUT_REASON_WAITING_FOR_CONNECTED = 4;
        private static final int TIMEOUT_REASON_WAITING_FOR_CONNECTING = 3;
        private boolean mClientStateUpdateScheduled;
        private int mConnectionPhase;
        private int mConnectionTimeoutReason;
        private long mConnectionTimeoutStartTime;
        private int mDiscoveryMode;
        private final ArrayList<ProviderRecord> mProviderRecords;
        private boolean mRunning;
        private RouteRecord mSelectedRouteRecord;
        private final MediaRouterService mService;
        private final ArrayList<IMediaRouterClient> mTempClients;
        private final UserRecord mUserRecord;
        private final RemoteDisplayProviderWatcher mWatcher;

        private static int getConnectionPhase(int i) {
            if (i != 0) {
                if (i == 1) {
                    return 0;
                }
                if (i == 2) {
                    return 1;
                }
                if (i == 3) {
                    return 0;
                }
                if (i != 6) {
                    return -1;
                }
            }
            return 2;
        }

        public UserHandler(MediaRouterService mediaRouterService, UserRecord userRecord) {
            super(Looper.getMainLooper(), null, true);
            this.mProviderRecords = new ArrayList<>();
            this.mTempClients = new ArrayList<>();
            this.mDiscoveryMode = 0;
            this.mConnectionPhase = -1;
            this.mService = mediaRouterService;
            this.mUserRecord = userRecord;
            this.mWatcher = new RemoteDisplayProviderWatcher(mediaRouterService.mContext, this, this, userRecord.mUserId);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    start();
                    return;
                case 2:
                    stop();
                    return;
                case 3:
                    updateDiscoveryRequest();
                    return;
                case 4:
                    selectRoute((String) message.obj);
                    return;
                case 5:
                    unselectRoute((String) message.obj);
                    return;
                case 6:
                    requestSetVolume((String) message.obj, message.arg1);
                    return;
                case 7:
                    requestUpdateVolume((String) message.obj, message.arg1);
                    return;
                case 8:
                    updateClientState();
                    return;
                case 9:
                    connectionTimedOut();
                    return;
                case 10:
                    notifyGroupRouteSelected((String) message.obj);
                    return;
                default:
                    return;
            }
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(str + "Handler");
            String str2 = str + "  ";
            printWriter.println(str2 + "mRunning=" + this.mRunning);
            printWriter.println(str2 + "mDiscoveryMode=" + this.mDiscoveryMode);
            printWriter.println(str2 + "mSelectedRouteRecord=" + this.mSelectedRouteRecord);
            printWriter.println(str2 + "mConnectionPhase=" + this.mConnectionPhase);
            printWriter.println(str2 + "mConnectionTimeoutReason=" + this.mConnectionTimeoutReason);
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append("mConnectionTimeoutStartTime=");
            sb.append(this.mConnectionTimeoutReason != 0 ? TimeUtils.formatUptime(this.mConnectionTimeoutStartTime) : "<n/a>");
            printWriter.println(sb.toString());
            this.mWatcher.dump(printWriter, str);
            int size = this.mProviderRecords.size();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    this.mProviderRecords.get(i).dump(printWriter, str);
                }
                return;
            }
            printWriter.println(str2 + "<no providers>");
        }

        private void start() {
            if (this.mRunning) {
                return;
            }
            this.mRunning = true;
            this.mWatcher.start();
        }

        private void stop() {
            if (this.mRunning) {
                this.mRunning = false;
                unselectSelectedRoute();
                this.mWatcher.stop();
            }
        }

        private void updateDiscoveryRequest() {
            int i;
            int i2;
            boolean z;
            synchronized (this.mService.mLock) {
                int size = this.mUserRecord.mClientRecords.size();
                i2 = 0;
                z = false;
                for (int i3 = 0; i3 < size; i3++) {
                    ClientRecord clientRecord = this.mUserRecord.mClientRecords.get(i3);
                    i2 |= clientRecord.mRouteTypes;
                    z |= clientRecord.mActiveScan;
                }
            }
            int i4 = (i2 & 4) != 0 ? z ? 2 : 1 : 0;
            if (this.mDiscoveryMode != i4) {
                this.mDiscoveryMode = i4;
                int size2 = this.mProviderRecords.size();
                for (i = 0; i < size2; i++) {
                    this.mProviderRecords.get(i).getProvider().setDiscoveryMode(this.mDiscoveryMode);
                }
            }
        }

        private void selectRoute(String str) {
            RouteRecord findRouteRecord;
            if (str != null) {
                RouteRecord routeRecord = this.mSelectedRouteRecord;
                if ((routeRecord == null || !str.equals(routeRecord.getUniqueId())) && (findRouteRecord = findRouteRecord(str)) != null) {
                    unselectSelectedRoute();
                    Slog.i(MediaRouterService.TAG, "Selected route:" + findRouteRecord);
                    this.mSelectedRouteRecord = findRouteRecord;
                    checkSelectedRouteState();
                    findRouteRecord.getProvider().setSelectedDisplay(findRouteRecord.getDescriptorId());
                    scheduleUpdateClientState();
                }
            }
        }

        private void unselectRoute(String str) {
            RouteRecord routeRecord;
            if (str == null || (routeRecord = this.mSelectedRouteRecord) == null || !str.equals(routeRecord.getUniqueId())) {
                return;
            }
            unselectSelectedRoute();
        }

        private void unselectSelectedRoute() {
            if (this.mSelectedRouteRecord != null) {
                Slog.i(MediaRouterService.TAG, "Unselected route:" + this.mSelectedRouteRecord);
                this.mSelectedRouteRecord.getProvider().setSelectedDisplay(null);
                this.mSelectedRouteRecord = null;
                checkSelectedRouteState();
                scheduleUpdateClientState();
            }
        }

        private void requestSetVolume(String str, int i) {
            RouteRecord routeRecord = this.mSelectedRouteRecord;
            if (routeRecord == null || !str.equals(routeRecord.getUniqueId())) {
                return;
            }
            this.mSelectedRouteRecord.getProvider().setDisplayVolume(i);
        }

        private void requestUpdateVolume(String str, int i) {
            RouteRecord routeRecord = this.mSelectedRouteRecord;
            if (routeRecord == null || !str.equals(routeRecord.getUniqueId())) {
                return;
            }
            this.mSelectedRouteRecord.getProvider().adjustDisplayVolume(i);
        }

        @Override // com.android.server.media.RemoteDisplayProviderWatcher.Callback
        public void addProvider(RemoteDisplayProviderProxy remoteDisplayProviderProxy) {
            remoteDisplayProviderProxy.setCallback(this);
            remoteDisplayProviderProxy.setDiscoveryMode(this.mDiscoveryMode);
            remoteDisplayProviderProxy.setSelectedDisplay(null);
            ProviderRecord providerRecord = new ProviderRecord(remoteDisplayProviderProxy);
            this.mProviderRecords.add(providerRecord);
            providerRecord.updateDescriptor(remoteDisplayProviderProxy.getDisplayState());
            scheduleUpdateClientState();
        }

        @Override // com.android.server.media.RemoteDisplayProviderWatcher.Callback
        public void removeProvider(RemoteDisplayProviderProxy remoteDisplayProviderProxy) {
            int findProviderRecord = findProviderRecord(remoteDisplayProviderProxy);
            if (findProviderRecord >= 0) {
                this.mProviderRecords.remove(findProviderRecord).updateDescriptor(null);
                remoteDisplayProviderProxy.setCallback(null);
                remoteDisplayProviderProxy.setDiscoveryMode(0);
                checkSelectedRouteState();
                scheduleUpdateClientState();
            }
        }

        @Override // com.android.server.media.RemoteDisplayProviderProxy.Callback
        public void onDisplayStateChanged(RemoteDisplayProviderProxy remoteDisplayProviderProxy, RemoteDisplayState remoteDisplayState) {
            updateProvider(remoteDisplayProviderProxy, remoteDisplayState);
        }

        private void updateProvider(RemoteDisplayProviderProxy remoteDisplayProviderProxy, RemoteDisplayState remoteDisplayState) {
            int findProviderRecord = findProviderRecord(remoteDisplayProviderProxy);
            if (findProviderRecord < 0 || !this.mProviderRecords.get(findProviderRecord).updateDescriptor(remoteDisplayState)) {
                return;
            }
            checkSelectedRouteState();
            scheduleUpdateClientState();
        }

        private void checkSelectedRouteState() {
            RouteRecord routeRecord = this.mSelectedRouteRecord;
            if (routeRecord == null) {
                this.mConnectionPhase = -1;
                updateConnectionTimeout(0);
                return;
            }
            if (!routeRecord.isValid() || !this.mSelectedRouteRecord.isEnabled()) {
                updateConnectionTimeout(1);
                return;
            }
            int i = this.mConnectionPhase;
            int connectionPhase = getConnectionPhase(this.mSelectedRouteRecord.getStatus());
            this.mConnectionPhase = connectionPhase;
            if (i >= 1 && connectionPhase < 1) {
                updateConnectionTimeout(2);
                return;
            }
            if (connectionPhase == 0) {
                updateConnectionTimeout(3);
                return;
            }
            if (connectionPhase == 1) {
                if (i != 1) {
                    Slog.i(MediaRouterService.TAG, "Connecting to route: " + this.mSelectedRouteRecord);
                }
                updateConnectionTimeout(4);
                return;
            }
            if (connectionPhase == 2) {
                if (i != 2) {
                    Slog.i(MediaRouterService.TAG, "Connected to route: " + this.mSelectedRouteRecord);
                }
                updateConnectionTimeout(0);
                return;
            }
            updateConnectionTimeout(1);
        }

        private void updateConnectionTimeout(int i) {
            int i2 = this.mConnectionTimeoutReason;
            if (i != i2) {
                if (i2 != 0) {
                    removeMessages(9);
                }
                this.mConnectionTimeoutReason = i;
                this.mConnectionTimeoutStartTime = SystemClock.uptimeMillis();
                if (i == 1 || i == 2) {
                    sendEmptyMessage(9);
                } else if (i == 3) {
                    sendEmptyMessageDelayed(9, MediaRouterService.CONNECTING_TIMEOUT);
                } else {
                    if (i != 4) {
                        return;
                    }
                    sendEmptyMessageDelayed(9, MediaRouterService.CONNECTED_TIMEOUT);
                }
            }
        }

        private void connectionTimedOut() {
            int i = this.mConnectionTimeoutReason;
            if (i == 0 || this.mSelectedRouteRecord == null) {
                Log.wtf(MediaRouterService.TAG, "Handled connection timeout for no reason.");
                return;
            }
            if (i == 1) {
                Slog.i(MediaRouterService.TAG, "Selected route no longer available: " + this.mSelectedRouteRecord);
            } else if (i == 2) {
                Slog.i(MediaRouterService.TAG, "Selected route connection lost: " + this.mSelectedRouteRecord);
            } else if (i == 3) {
                Slog.i(MediaRouterService.TAG, "Selected route timed out while waiting for connection attempt to begin after " + (SystemClock.uptimeMillis() - this.mConnectionTimeoutStartTime) + " ms: " + this.mSelectedRouteRecord);
            } else if (i == 4) {
                Slog.i(MediaRouterService.TAG, "Selected route timed out while connecting after " + (SystemClock.uptimeMillis() - this.mConnectionTimeoutStartTime) + " ms: " + this.mSelectedRouteRecord);
            }
            this.mConnectionTimeoutReason = 0;
            unselectSelectedRoute();
        }

        private void scheduleUpdateClientState() {
            if (this.mClientStateUpdateScheduled) {
                return;
            }
            this.mClientStateUpdateScheduled = true;
            sendEmptyMessage(8);
        }

        private void updateClientState() {
            this.mClientStateUpdateScheduled = false;
            MediaRouterClientState mediaRouterClientState = new MediaRouterClientState();
            int size = this.mProviderRecords.size();
            for (int i = 0; i < size; i++) {
                this.mProviderRecords.get(i).appendClientState(mediaRouterClientState);
            }
            try {
                synchronized (this.mService.mLock) {
                    UserRecord userRecord = this.mUserRecord;
                    userRecord.mRouterState = mediaRouterClientState;
                    int size2 = userRecord.mClientRecords.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        this.mTempClients.add(this.mUserRecord.mClientRecords.get(i2).mClient);
                    }
                }
                int size3 = this.mTempClients.size();
                for (int i3 = 0; i3 < size3; i3++) {
                    try {
                        this.mTempClients.get(i3).onStateChanged();
                    } catch (RemoteException unused) {
                        Slog.w(MediaRouterService.TAG, "Failed to call onStateChanged. Client probably died.");
                    }
                }
            } finally {
                this.mTempClients.clear();
            }
        }

        private void notifyGroupRouteSelected(String str) {
            try {
                synchronized (this.mService.mLock) {
                    ClientGroup clientGroup = (ClientGroup) this.mUserRecord.mClientGroupMap.get(str);
                    if (clientGroup == null) {
                        return;
                    }
                    String str2 = clientGroup.mSelectedRouteId;
                    int size = clientGroup.mClientRecords.size();
                    for (int i = 0; i < size; i++) {
                        ClientRecord clientRecord = clientGroup.mClientRecords.get(i);
                        if (!TextUtils.equals(str2, clientRecord.mSelectedRouteId)) {
                            this.mTempClients.add(clientRecord.mClient);
                        }
                    }
                    int size2 = this.mTempClients.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        try {
                            this.mTempClients.get(i2).onGroupRouteSelected(str2);
                        } catch (RemoteException unused) {
                            Slog.w(MediaRouterService.TAG, "Failed to call onSelectedRouteChanged. Client probably died.");
                        }
                    }
                }
            } finally {
                this.mTempClients.clear();
            }
        }

        private int findProviderRecord(RemoteDisplayProviderProxy remoteDisplayProviderProxy) {
            int size = this.mProviderRecords.size();
            for (int i = 0; i < size; i++) {
                if (this.mProviderRecords.get(i).getProvider() == remoteDisplayProviderProxy) {
                    return i;
                }
            }
            return -1;
        }

        private RouteRecord findRouteRecord(String str) {
            int size = this.mProviderRecords.size();
            for (int i = 0; i < size; i++) {
                RouteRecord findRouteByUniqueId = this.mProviderRecords.get(i).findRouteByUniqueId(str);
                if (findRouteByUniqueId != null) {
                    return findRouteByUniqueId;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class ProviderRecord {
            private RemoteDisplayState mDescriptor;
            private final RemoteDisplayProviderProxy mProvider;
            private final ArrayList<RouteRecord> mRoutes = new ArrayList<>();
            private final String mUniquePrefix;

            public ProviderRecord(RemoteDisplayProviderProxy remoteDisplayProviderProxy) {
                this.mProvider = remoteDisplayProviderProxy;
                this.mUniquePrefix = remoteDisplayProviderProxy.getFlattenedComponentName() + ":";
            }

            public RemoteDisplayProviderProxy getProvider() {
                return this.mProvider;
            }

            public String getUniquePrefix() {
                return this.mUniquePrefix;
            }

            /* JADX WARN: Removed duplicated region for block: B:23:0x0095 A[LOOP:1: B:22:0x0093->B:23:0x0095, LOOP_END] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean updateDescriptor(RemoteDisplayState remoteDisplayState) {
                int i;
                int size;
                boolean z = false;
                z = false;
                if (this.mDescriptor != remoteDisplayState) {
                    this.mDescriptor = remoteDisplayState;
                    if (remoteDisplayState != null) {
                        if (remoteDisplayState.isValid()) {
                            ArrayList arrayList = remoteDisplayState.displays;
                            int size2 = arrayList.size();
                            boolean z2 = false;
                            i = 0;
                            for (int i2 = 0; i2 < size2; i2++) {
                                RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo = (RemoteDisplayState.RemoteDisplayInfo) arrayList.get(i2);
                                String str = remoteDisplayInfo.id;
                                int findRouteByDescriptorId = findRouteByDescriptorId(str);
                                if (findRouteByDescriptorId < 0) {
                                    RouteRecord routeRecord = new RouteRecord(this, str, assignRouteUniqueId(str));
                                    this.mRoutes.add(i, routeRecord);
                                    routeRecord.updateDescriptor(remoteDisplayInfo);
                                    z2 = true;
                                    i++;
                                } else if (findRouteByDescriptorId < i) {
                                    Slog.w(MediaRouterService.TAG, "Ignoring route descriptor with duplicate id: " + remoteDisplayInfo);
                                } else {
                                    RouteRecord routeRecord2 = this.mRoutes.get(findRouteByDescriptorId);
                                    Collections.swap(this.mRoutes, findRouteByDescriptorId, i);
                                    z2 |= routeRecord2.updateDescriptor(remoteDisplayInfo);
                                    i++;
                                }
                            }
                            z = z2;
                            size = this.mRoutes.size() - 1;
                            while (size >= i) {
                                this.mRoutes.remove(size).updateDescriptor(null);
                                size--;
                                z = true;
                            }
                        } else {
                            Slog.w(MediaRouterService.TAG, "Ignoring invalid descriptor from media route provider: " + this.mProvider.getFlattenedComponentName());
                        }
                    }
                    i = 0;
                    size = this.mRoutes.size() - 1;
                    while (size >= i) {
                    }
                }
                return z;
            }

            public void appendClientState(MediaRouterClientState mediaRouterClientState) {
                int size = this.mRoutes.size();
                for (int i = 0; i < size; i++) {
                    mediaRouterClientState.routes.add(this.mRoutes.get(i).getInfo());
                }
            }

            public RouteRecord findRouteByUniqueId(String str) {
                int size = this.mRoutes.size();
                for (int i = 0; i < size; i++) {
                    RouteRecord routeRecord = this.mRoutes.get(i);
                    if (routeRecord.getUniqueId().equals(str)) {
                        return routeRecord;
                    }
                }
                return null;
            }

            private int findRouteByDescriptorId(String str) {
                int size = this.mRoutes.size();
                for (int i = 0; i < size; i++) {
                    if (this.mRoutes.get(i).getDescriptorId().equals(str)) {
                        return i;
                    }
                }
                return -1;
            }

            public void dump(PrintWriter printWriter, String str) {
                printWriter.println(str + this);
                String str2 = str + "  ";
                this.mProvider.dump(printWriter, str2);
                int size = this.mRoutes.size();
                if (size != 0) {
                    for (int i = 0; i < size; i++) {
                        this.mRoutes.get(i).dump(printWriter, str2);
                    }
                    return;
                }
                printWriter.println(str2 + "<no routes>");
            }

            public String toString() {
                return "Provider " + this.mProvider.getFlattenedComponentName();
            }

            private String assignRouteUniqueId(String str) {
                return this.mUniquePrefix + str;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class RouteRecord {
            private RemoteDisplayState.RemoteDisplayInfo mDescriptor;
            private final String mDescriptorId;
            private MediaRouterClientState.RouteInfo mImmutableInfo;
            private final MediaRouterClientState.RouteInfo mMutableInfo;
            private final ProviderRecord mProviderRecord;

            private static int computePlaybackStream(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                return 3;
            }

            private static int computePlaybackType(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                return 1;
            }

            private static int computeSupportedTypes(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                return 7;
            }

            public RouteRecord(ProviderRecord providerRecord, String str, String str2) {
                this.mProviderRecord = providerRecord;
                this.mDescriptorId = str;
                this.mMutableInfo = new MediaRouterClientState.RouteInfo(str2);
            }

            public RemoteDisplayProviderProxy getProvider() {
                return this.mProviderRecord.getProvider();
            }

            public ProviderRecord getProviderRecord() {
                return this.mProviderRecord;
            }

            public String getDescriptorId() {
                return this.mDescriptorId;
            }

            public String getUniqueId() {
                return this.mMutableInfo.id;
            }

            public MediaRouterClientState.RouteInfo getInfo() {
                if (this.mImmutableInfo == null) {
                    this.mImmutableInfo = new MediaRouterClientState.RouteInfo(this.mMutableInfo);
                }
                return this.mImmutableInfo;
            }

            public boolean isValid() {
                return this.mDescriptor != null;
            }

            public boolean isEnabled() {
                return this.mMutableInfo.enabled;
            }

            public int getStatus() {
                return this.mMutableInfo.statusCode;
            }

            public boolean updateDescriptor(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                boolean z = false;
                if (this.mDescriptor != remoteDisplayInfo) {
                    this.mDescriptor = remoteDisplayInfo;
                    if (remoteDisplayInfo != null) {
                        String computeName = computeName(remoteDisplayInfo);
                        if (!Objects.equals(this.mMutableInfo.name, computeName)) {
                            this.mMutableInfo.name = computeName;
                            z = true;
                        }
                        String computeDescription = computeDescription(remoteDisplayInfo);
                        if (!Objects.equals(this.mMutableInfo.description, computeDescription)) {
                            this.mMutableInfo.description = computeDescription;
                            z = true;
                        }
                        int computeSupportedTypes = computeSupportedTypes(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo = this.mMutableInfo;
                        if (routeInfo.supportedTypes != computeSupportedTypes) {
                            routeInfo.supportedTypes = computeSupportedTypes;
                            z = true;
                        }
                        boolean computeEnabled = computeEnabled(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo2 = this.mMutableInfo;
                        if (routeInfo2.enabled != computeEnabled) {
                            routeInfo2.enabled = computeEnabled;
                            z = true;
                        }
                        int computeStatusCode = computeStatusCode(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo3 = this.mMutableInfo;
                        if (routeInfo3.statusCode != computeStatusCode) {
                            routeInfo3.statusCode = computeStatusCode;
                            z = true;
                        }
                        int computePlaybackType = computePlaybackType(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo4 = this.mMutableInfo;
                        if (routeInfo4.playbackType != computePlaybackType) {
                            routeInfo4.playbackType = computePlaybackType;
                            z = true;
                        }
                        int computePlaybackStream = computePlaybackStream(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo5 = this.mMutableInfo;
                        if (routeInfo5.playbackStream != computePlaybackStream) {
                            routeInfo5.playbackStream = computePlaybackStream;
                            z = true;
                        }
                        int computeVolume = computeVolume(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo6 = this.mMutableInfo;
                        if (routeInfo6.volume != computeVolume) {
                            routeInfo6.volume = computeVolume;
                            z = true;
                        }
                        int computeVolumeMax = computeVolumeMax(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo7 = this.mMutableInfo;
                        if (routeInfo7.volumeMax != computeVolumeMax) {
                            routeInfo7.volumeMax = computeVolumeMax;
                            z = true;
                        }
                        int computeVolumeHandling = computeVolumeHandling(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo8 = this.mMutableInfo;
                        if (routeInfo8.volumeHandling != computeVolumeHandling) {
                            routeInfo8.volumeHandling = computeVolumeHandling;
                            z = true;
                        }
                        int computePresentationDisplayId = computePresentationDisplayId(remoteDisplayInfo);
                        MediaRouterClientState.RouteInfo routeInfo9 = this.mMutableInfo;
                        if (routeInfo9.presentationDisplayId != computePresentationDisplayId) {
                            routeInfo9.presentationDisplayId = computePresentationDisplayId;
                            z = true;
                        }
                    }
                }
                if (z) {
                    this.mImmutableInfo = null;
                }
                return z;
            }

            public void dump(PrintWriter printWriter, String str) {
                printWriter.println(str + this);
                String str2 = str + "  ";
                printWriter.println(str2 + "mMutableInfo=" + this.mMutableInfo);
                printWriter.println(str2 + "mDescriptorId=" + this.mDescriptorId);
                printWriter.println(str2 + "mDescriptor=" + this.mDescriptor);
            }

            public String toString() {
                return "Route " + this.mMutableInfo.name + " (" + this.mMutableInfo.id + ")";
            }

            private static String computeName(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                return remoteDisplayInfo.name;
            }

            private static String computeDescription(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                String str = remoteDisplayInfo.description;
                if (TextUtils.isEmpty(str)) {
                    return null;
                }
                return str;
            }

            private static boolean computeEnabled(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                int i = remoteDisplayInfo.status;
                return i == 2 || i == 3 || i == 4;
            }

            private static int computeStatusCode(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                int i = remoteDisplayInfo.status;
                if (i == 0) {
                    return 4;
                }
                if (i == 1) {
                    return 5;
                }
                if (i == 2) {
                    return 3;
                }
                if (i != 3) {
                    return i != 4 ? 0 : 6;
                }
                return 2;
            }

            private static int computeVolume(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                int i = remoteDisplayInfo.volume;
                int i2 = remoteDisplayInfo.volumeMax;
                if (i < 0) {
                    return 0;
                }
                return i > i2 ? i2 : i;
            }

            private static int computeVolumeMax(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                int i = remoteDisplayInfo.volumeMax;
                if (i > 0) {
                    return i;
                }
                return 0;
            }

            private static int computeVolumeHandling(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                return remoteDisplayInfo.volumeHandling != 1 ? 0 : 1;
            }

            private static int computePresentationDisplayId(RemoteDisplayState.RemoteDisplayInfo remoteDisplayInfo) {
                int i = remoteDisplayInfo.presentationDisplayId;
                if (i < 0) {
                    return -1;
                }
                return i;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AudioPlayerActiveStateChangedListenerImpl implements AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener {
        private static final long WAIT_MS = 500;
        private final Runnable mRestoreBluetoothA2dpRunnable;

        private AudioPlayerActiveStateChangedListenerImpl() {
            this.mRestoreBluetoothA2dpRunnable = new Runnable() { // from class: com.android.server.media.MediaRouterService$AudioPlayerActiveStateChangedListenerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaRouterService.this.restoreBluetoothA2dp();
                }
            };
        }

        @Override // com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener
        public void onAudioPlayerActiveStateChanged(AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z) {
            int i;
            boolean z2 = !z && audioPlaybackConfiguration.isActive();
            int clientUid = audioPlaybackConfiguration.getClientUid();
            int indexOf = MediaRouterService.this.mActivePlayerMinPriorityQueue.indexOf(audioPlaybackConfiguration.getPlayerInterfaceId());
            if (indexOf >= 0) {
                MediaRouterService.this.mActivePlayerMinPriorityQueue.remove(indexOf);
                MediaRouterService.this.mActivePlayerUidMinPriorityQueue.remove(indexOf);
            }
            if (z2) {
                MediaRouterService.this.mActivePlayerMinPriorityQueue.add(audioPlaybackConfiguration.getPlayerInterfaceId());
                MediaRouterService.this.mActivePlayerUidMinPriorityQueue.add(clientUid);
                i = clientUid;
            } else {
                i = MediaRouterService.this.mActivePlayerUidMinPriorityQueue.size() > 0 ? MediaRouterService.this.mActivePlayerUidMinPriorityQueue.get(MediaRouterService.this.mActivePlayerUidMinPriorityQueue.size() - 1) : -1;
            }
            MediaRouterService.this.mHandler.removeCallbacks(this.mRestoreBluetoothA2dpRunnable);
            if (i >= 0) {
                MediaRouterService.this.restoreRoute(i);
                if (MediaRouterService.DEBUG) {
                    Slog.d(MediaRouterService.TAG, "onAudioPlayerActiveStateChanged: uid=" + clientUid + ", active=" + z2 + ", restoreUid=" + i);
                    return;
                }
                return;
            }
            MediaRouterService.this.mHandler.postDelayed(this.mRestoreBluetoothA2dpRunnable, 500L);
            if (MediaRouterService.DEBUG) {
                Slog.d(MediaRouterService.TAG, "onAudioPlayerActiveStateChanged: uid=" + clientUid + ", active=" + z2 + ", delaying");
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AudioRoutesObserverImpl extends IAudioRoutesObserver.Stub {
        private static final int HEADSET_FLAGS = 19;

        private AudioRoutesObserverImpl() {
        }

        public void dispatchAudioRoutesChanged(AudioRoutesInfo audioRoutesInfo) {
            synchronized (MediaRouterService.this.mLock) {
                int i = audioRoutesInfo.mainType;
                MediaRouterService mediaRouterService = MediaRouterService.this;
                if (i != mediaRouterService.mAudioRouteMainType) {
                    if ((i & 19) == 0) {
                        mediaRouterService.mGlobalBluetoothA2dpOn = (audioRoutesInfo.bluetoothName == null && mediaRouterService.mActiveBluetoothDevice == null) ? false : true;
                    } else {
                        mediaRouterService.mGlobalBluetoothA2dpOn = false;
                    }
                    mediaRouterService.mAudioRouteMainType = i;
                }
            }
        }
    }
}
