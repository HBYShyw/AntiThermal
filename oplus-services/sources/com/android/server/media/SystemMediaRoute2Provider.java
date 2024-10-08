package com.android.server.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import android.media.MediaRoute2Info;
import android.media.MediaRoute2ProviderInfo;
import android.media.RouteDiscoveryPreference;
import android.media.RoutingSessionInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.media.BluetoothRouteController;
import com.android.server.media.DeviceRouteController;
import com.android.server.media.MediaRoute2Provider;
import com.android.server.media.SystemMediaRoute2Provider;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SystemMediaRoute2Provider extends MediaRoute2Provider {
    static final String SYSTEM_SESSION_ID = "SYSTEM_SESSION";
    private final AudioManager mAudioManager;
    private final AudioManagerBroadcastReceiver mAudioReceiver;
    private final BluetoothRouteController mBluetoothRouteController;
    private final Context mContext;
    MediaRoute2Info mDefaultRoute;
    RoutingSessionInfo mDefaultSessionInfo;
    private final DeviceRouteController mDeviceRouteController;
    private final Handler mHandler;
    private final AudioManager.OnDevicesForAttributesChangedListener mOnDevicesForAttributesChangedListener;

    @GuardedBy({"mRequestLock"})
    private volatile SessionCreationRequest mPendingSessionCreationRequest;
    private final Object mRequestLock;
    private String mSelectedRouteId;
    private final UserHandle mUser;
    private static final String TAG = "MR2SystemProvider";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final ComponentName COMPONENT_NAME = new ComponentName(SystemMediaRoute2Provider.class.getPackage().getName(), SystemMediaRoute2Provider.class.getName());

    @Override // com.android.server.media.MediaRoute2Provider
    public void deselectRoute(long j, String str, String str2) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void prepareReleaseSession(String str) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void releaseSession(long j, String str) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void selectRoute(long j, String str, String str2) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setSessionVolume(long j, String str, int i) {
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void updateDiscoveryPreference(Set<String> set, RouteDiscoveryPreference routeDiscoveryPreference) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.media.SystemMediaRoute2Provider$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 implements AudioManager.OnDevicesForAttributesChangedListener {
        AnonymousClass1() {
        }

        public void onDevicesForAttributesChanged(AudioAttributes audioAttributes, final List<AudioDeviceAttributes> list) {
            if (audioAttributes.getUsage() != 1) {
                return;
            }
            SystemMediaRoute2Provider.this.mHandler.post(new Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SystemMediaRoute2Provider.AnonymousClass1.this.lambda$onDevicesForAttributesChanged$0(list);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDevicesForAttributesChanged$0(List list) {
            SystemMediaRoute2Provider.this.updateSelectedAudioDevice(list);
            SystemMediaRoute2Provider.this.notifyProviderState();
            if (SystemMediaRoute2Provider.this.updateSessionInfosIfNeeded()) {
                SystemMediaRoute2Provider.this.notifySessionInfoUpdated();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemMediaRoute2Provider(Context context, UserHandle userHandle) {
        super(COMPONENT_NAME);
        this.mAudioReceiver = new AudioManagerBroadcastReceiver();
        AudioManager.OnDevicesForAttributesChangedListener anonymousClass1 = new AnonymousClass1();
        this.mOnDevicesForAttributesChangedListener = anonymousClass1;
        this.mRequestLock = new Object();
        this.mIsSystemRouteProvider = true;
        this.mContext = context;
        this.mUser = userHandle;
        this.mHandler = new Handler(Looper.getMainLooper());
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        this.mAudioManager = audioManager;
        this.mBluetoothRouteController = BluetoothRouteController.createInstance(context, new BluetoothRouteController.BluetoothRoutesUpdatedListener() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda3
            @Override // com.android.server.media.BluetoothRouteController.BluetoothRoutesUpdatedListener
            public final void onBluetoothRoutesUpdated(List list) {
                SystemMediaRoute2Provider.this.lambda$new$0(list);
            }
        });
        this.mDeviceRouteController = DeviceRouteController.createInstance(context, new DeviceRouteController.OnDeviceRouteChangedListener() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda4
            @Override // com.android.server.media.DeviceRouteController.OnDeviceRouteChangedListener
            public final void onDeviceRouteChanged(MediaRoute2Info mediaRoute2Info) {
                SystemMediaRoute2Provider.this.lambda$new$2(mediaRoute2Info);
            }
        });
        AudioAttributes audioAttributes = AudioAttributesUtils.ATTRIBUTES_MEDIA;
        audioManager.addOnDevicesForAttributesChangedListener(audioAttributes, context.getMainExecutor(), anonymousClass1);
        updateSelectedAudioDevice(audioManager.getDevicesForAttributes(audioAttributes));
        updateProviderState();
        updateSessionInfosIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(List list) {
        publishProviderState();
        if (updateSessionInfosIfNeeded()) {
            notifySessionInfoUpdated();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(MediaRoute2Info mediaRoute2Info) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SystemMediaRoute2Provider.this.lambda$new$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        publishProviderState();
        if (updateSessionInfosIfNeeded()) {
            notifySessionInfoUpdated();
        }
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        intentFilter.addAction("android.media.STREAM_DEVICES_CHANGED_ACTION");
        this.mContext.registerReceiverAsUser(this.mAudioReceiver, this.mUser, intentFilter, null, null);
        this.mHandler.post(new Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SystemMediaRoute2Provider.this.lambda$start$3();
            }
        });
        updateVolume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$3() {
        this.mBluetoothRouteController.start(this.mUser);
        notifyProviderState();
    }

    public void stop() {
        this.mContext.unregisterReceiver(this.mAudioReceiver);
        this.mHandler.post(new Runnable() { // from class: com.android.server.media.SystemMediaRoute2Provider$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SystemMediaRoute2Provider.this.lambda$stop$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$stop$4() {
        this.mBluetoothRouteController.stop();
        notifyProviderState();
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setCallback(MediaRoute2Provider.Callback callback) {
        super.setCallback(callback);
        notifyProviderState();
        notifySessionInfoUpdated();
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void requestCreateSession(long j, String str, String str2, Bundle bundle) {
        if (TextUtils.equals(str2, "DEFAULT_ROUTE")) {
            this.mCallback.onSessionCreated(this, j, this.mDefaultSessionInfo);
            return;
        }
        if (TextUtils.equals(str2, this.mSelectedRouteId)) {
            this.mCallback.onSessionCreated(this, j, this.mSessionInfos.get(0));
            return;
        }
        synchronized (this.mRequestLock) {
            if (this.mPendingSessionCreationRequest != null) {
                this.mCallback.onRequestFailed(this, this.mPendingSessionCreationRequest.mRequestId, 0);
            }
            this.mPendingSessionCreationRequest = new SessionCreationRequest(j, str2);
        }
        transferToRoute(j, SYSTEM_SESSION_ID, str2);
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void transferToRoute(long j, String str, String str2) {
        if (TextUtils.equals(str2, "DEFAULT_ROUTE")) {
            return;
        }
        if (TextUtils.equals(str2, this.mDeviceRouteController.getDeviceRoute().getId())) {
            this.mBluetoothRouteController.transferTo(null);
        } else {
            this.mBluetoothRouteController.transferTo(str2);
        }
    }

    @Override // com.android.server.media.MediaRoute2Provider
    public void setRouteVolume(long j, String str, int i) {
        if (TextUtils.equals(str, this.mSelectedRouteId)) {
            this.mAudioManager.setStreamVolume(3, i, 0);
        }
    }

    public MediaRoute2Info getDefaultRoute() {
        return this.mDefaultRoute;
    }

    public RoutingSessionInfo getDefaultSessionInfo() {
        return this.mDefaultSessionInfo;
    }

    public RoutingSessionInfo generateDeviceRouteSelectedSessionInfo(String str) {
        synchronized (this.mLock) {
            if (this.mSessionInfos.isEmpty()) {
                return null;
            }
            MediaRoute2Info deviceRoute = this.mDeviceRouteController.getDeviceRoute();
            RoutingSessionInfo.Builder systemSession = new RoutingSessionInfo.Builder(SYSTEM_SESSION_ID, str).setSystemSession(true);
            systemSession.addSelectedRoute(deviceRoute.getId());
            Iterator<MediaRoute2Info> it = this.mBluetoothRouteController.getAllBluetoothRoutes().iterator();
            while (it.hasNext()) {
                systemSession.addTransferableRoute(it.next().getId());
            }
            return systemSession.setProviderId(this.mUniqueId).build();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelectedAudioDevice(List<AudioDeviceAttributes> list) {
        if (list.isEmpty()) {
            Slog.w(TAG, "The list of preferred devices was empty.");
            return;
        }
        AudioDeviceAttributes audioDeviceAttributes = list.get(0);
        if (AudioAttributesUtils.isDeviceOutputAttributes(audioDeviceAttributes)) {
            this.mDeviceRouteController.selectRoute(Integer.valueOf(AudioAttributesUtils.mapToMediaRouteType(audioDeviceAttributes)));
            this.mBluetoothRouteController.selectRoute(null);
        } else if (AudioAttributesUtils.isBluetoothOutputAttributes(audioDeviceAttributes)) {
            this.mDeviceRouteController.selectRoute(null);
            this.mBluetoothRouteController.selectRoute(audioDeviceAttributes.getAddress());
        } else {
            Slog.w(TAG, "Unknown audio attributes: " + audioDeviceAttributes);
        }
    }

    private void updateProviderState() {
        MediaRoute2ProviderInfo.Builder builder = new MediaRoute2ProviderInfo.Builder();
        builder.addRoute(this.mDeviceRouteController.getDeviceRoute());
        Iterator<MediaRoute2Info> it = this.mBluetoothRouteController.getAllBluetoothRoutes().iterator();
        while (it.hasNext()) {
            builder.addRoute(it.next());
        }
        MediaRoute2ProviderInfo build = builder.build();
        setProviderState(build);
        if (DEBUG) {
            Slog.d(TAG, "Updating system provider info : " + build);
        }
    }

    boolean updateSessionInfosIfNeeded() {
        SessionCreationRequest sessionCreationRequest;
        synchronized (this.mLock) {
            RoutingSessionInfo routingSessionInfo = this.mSessionInfos.isEmpty() ? null : this.mSessionInfos.get(0);
            RoutingSessionInfo.Builder systemSession = new RoutingSessionInfo.Builder(SYSTEM_SESSION_ID, "").setSystemSession(true);
            MediaRoute2Info deviceRoute = this.mDeviceRouteController.getDeviceRoute();
            MediaRoute2Info selectedRoute = this.mBluetoothRouteController.getSelectedRoute();
            if (selectedRoute != null) {
                systemSession.addTransferableRoute(deviceRoute.getId());
                deviceRoute = selectedRoute;
            }
            this.mSelectedRouteId = deviceRoute.getId();
            this.mDefaultRoute = new MediaRoute2Info.Builder("DEFAULT_ROUTE", deviceRoute).setSystemRoute(true).setProviderId(this.mUniqueId).build();
            systemSession.addSelectedRoute(this.mSelectedRouteId);
            Iterator<MediaRoute2Info> it = this.mBluetoothRouteController.getTransferableRoutes().iterator();
            while (it.hasNext()) {
                systemSession.addTransferableRoute(it.next().getId());
            }
            RoutingSessionInfo build = systemSession.setProviderId(this.mUniqueId).build();
            if (this.mPendingSessionCreationRequest != null) {
                synchronized (this.mRequestLock) {
                    sessionCreationRequest = this.mPendingSessionCreationRequest;
                    this.mPendingSessionCreationRequest = null;
                }
                if (sessionCreationRequest != null) {
                    if (TextUtils.equals(this.mSelectedRouteId, sessionCreationRequest.mRouteId)) {
                        this.mCallback.onSessionCreated(this, sessionCreationRequest.mRequestId, build);
                    } else {
                        this.mCallback.onRequestFailed(this, sessionCreationRequest.mRequestId, 0);
                    }
                }
            }
            if (Objects.equals(routingSessionInfo, build)) {
                return false;
            }
            if (DEBUG) {
                Slog.d(TAG, "Updating system routing session info : " + build);
            }
            this.mSessionInfos.clear();
            this.mSessionInfos.add(build);
            this.mDefaultSessionInfo = new RoutingSessionInfo.Builder(SYSTEM_SESSION_ID, "").setProviderId(this.mUniqueId).setSystemSession(true).addSelectedRoute("DEFAULT_ROUTE").build();
            return true;
        }
    }

    void publishProviderState() {
        updateProviderState();
        notifyProviderState();
    }

    void notifySessionInfoUpdated() {
        RoutingSessionInfo routingSessionInfo;
        if (this.mCallback == null) {
            return;
        }
        synchronized (this.mLock) {
            routingSessionInfo = this.mSessionInfos.get(0);
        }
        this.mCallback.onSessionUpdated(this, routingSessionInfo);
    }

    @Override // com.android.server.media.MediaRoute2Provider
    protected String getDebugString() {
        return TextUtils.formatSimple("SystemMR2Provider - package: %s, selected route id: %s, bluetooth impl: %s", new Object[]{this.mComponentName.getPackageName(), this.mSelectedRouteId, this.mBluetoothRouteController.getClass().getSimpleName()});
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SessionCreationRequest {
        final long mRequestId;
        final String mRouteId;

        SessionCreationRequest(long j, String str) {
            this.mRequestId = j;
            this.mRouteId = str;
        }
    }

    void updateVolume() {
        int devicesForStream = this.mAudioManager.getDevicesForStream(3);
        int streamVolume = this.mAudioManager.getStreamVolume(3);
        if (this.mDefaultRoute.getVolume() != streamVolume) {
            this.mDefaultRoute = new MediaRoute2Info.Builder(this.mDefaultRoute).setVolume(streamVolume).build();
        }
        if (this.mBluetoothRouteController.updateVolumeForDevices(devicesForStream, streamVolume)) {
            return;
        }
        this.mDeviceRouteController.updateVolume(streamVolume);
        publishProviderState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AudioManagerBroadcastReceiver extends BroadcastReceiver {
        private AudioManagerBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ((intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION") || intent.getAction().equals("android.media.STREAM_DEVICES_CHANGED_ACTION")) && intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1) == 3) {
                SystemMediaRoute2Provider.this.updateVolume();
            }
        }
    }
}
