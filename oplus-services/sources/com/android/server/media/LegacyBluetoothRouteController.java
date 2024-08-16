package com.android.server.media;

import android.R;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHearingAid;
import android.bluetooth.BluetoothLeAudio;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRoute2Info;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.server.media.BluetoothRouteController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LegacyBluetoothRouteController implements BluetoothRouteController {
    private static final String HEARING_AID_ROUTE_ID_PREFIX = "HEARING_AID_";
    private static final String LE_AUDIO_ROUTE_ID_PREFIX = "LE_AUDIO_";
    private BluetoothA2dp mA2dpProfile;
    private final AudioManager mAudioManager;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Context mContext;
    private BluetoothHearingAid mHearingAidProfile;
    private BluetoothLeAudio mLeAudioProfile;
    private final BluetoothRouteController.BluetoothRoutesUpdatedListener mListener;
    private static final String TAG = "LBtRouteProvider";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final Map<String, BluetoothRouteInfo> mBluetoothRoutes = new HashMap();
    private final List<BluetoothRouteInfo> mActiveRoutes = new ArrayList();
    private final SparseIntArray mVolumeMap = new SparseIntArray();
    private final BluetoothProfileListener mProfileListener = new BluetoothProfileListener();
    final Object mBtRoutesLock = new Object();
    private final AdapterStateChangedReceiver mAdapterStateChangedReceiver = new AdapterStateChangedReceiver();
    private final DeviceStateChangedReceiver mDeviceStateChangedReceiver = new DeviceStateChangedReceiver();

    @Override // com.android.server.media.BluetoothRouteController
    public boolean selectRoute(String str) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LegacyBluetoothRouteController(Context context, BluetoothAdapter bluetoothAdapter, BluetoothRouteController.BluetoothRoutesUpdatedListener bluetoothRoutesUpdatedListener) {
        this.mContext = context;
        this.mBluetoothAdapter = bluetoothAdapter;
        this.mListener = bluetoothRoutesUpdatedListener;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        buildBluetoothRoutes();
    }

    @Override // com.android.server.media.BluetoothRouteController
    public void start(UserHandle userHandle) {
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 2);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 21);
        this.mBluetoothAdapter.getProfileProxy(this.mContext, this.mProfileListener, 22);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mAdapterStateChangedReceiver, userHandle, intentFilter, null, null);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED");
        intentFilter2.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter2.addAction("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED");
        intentFilter2.addAction("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter2.addAction("android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED");
        intentFilter2.addAction("android.bluetooth.action.LE_AUDIO_ACTIVE_DEVICE_CHANGED");
        this.mContext.registerReceiverAsUser(this.mDeviceStateChangedReceiver, userHandle, intentFilter2, null, null);
    }

    @Override // com.android.server.media.BluetoothRouteController
    public void stop() {
        this.mContext.unregisterReceiver(this.mAdapterStateChangedReceiver);
        this.mContext.unregisterReceiver(this.mDeviceStateChangedReceiver);
    }

    @Override // com.android.server.media.BluetoothRouteController
    public void transferTo(String str) {
        if (str == null) {
            clearActiveDevices();
            return;
        }
        BluetoothRouteInfo findBluetoothRouteWithRouteId = findBluetoothRouteWithRouteId(str);
        if (findBluetoothRouteWithRouteId == null) {
            Slog.w(TAG, "transferTo: Unknown route. ID=" + str);
            return;
        }
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.setActiveDevice(findBluetoothRouteWithRouteId.mBtDevice, 0);
        }
    }

    private BluetoothRouteInfo findBluetoothRouteWithRouteId(String str) {
        if (str == null) {
            return null;
        }
        for (BluetoothRouteInfo bluetoothRouteInfo : this.mBluetoothRoutes.values()) {
            if (TextUtils.equals(bluetoothRouteInfo.mRoute.getId(), str)) {
                return bluetoothRouteInfo;
            }
        }
        return null;
    }

    private void clearActiveDevices() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.removeActiveDevice(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildBluetoothRoutes() {
        this.mBluetoothRoutes.clear();
        Set<BluetoothDevice> bondedDevices = this.mBluetoothAdapter.getBondedDevices();
        if (bondedDevices != null) {
            for (BluetoothDevice bluetoothDevice : bondedDevices) {
                if (bluetoothDevice.isConnected()) {
                    BluetoothRouteInfo createBluetoothRoute = createBluetoothRoute(bluetoothDevice);
                    if (createBluetoothRoute.mConnectedProfiles.size() > 0) {
                        this.mBluetoothRoutes.put(bluetoothDevice.getAddress(), createBluetoothRoute);
                    }
                }
            }
        }
    }

    @Override // com.android.server.media.BluetoothRouteController
    public MediaRoute2Info getSelectedRoute() {
        if (this.mActiveRoutes.isEmpty()) {
            return null;
        }
        return this.mActiveRoutes.get(0).mRoute;
    }

    @Override // com.android.server.media.BluetoothRouteController
    public List<MediaRoute2Info> getTransferableRoutes() {
        List<MediaRoute2Info> allBluetoothRoutes = getAllBluetoothRoutes();
        Iterator<BluetoothRouteInfo> it = this.mActiveRoutes.iterator();
        while (it.hasNext()) {
            allBluetoothRoutes.remove(it.next().mRoute);
        }
        return allBluetoothRoutes;
    }

    @Override // com.android.server.media.BluetoothRouteController
    public List<MediaRoute2Info> getAllBluetoothRoutes() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        MediaRoute2Info selectedRoute = getSelectedRoute();
        if (selectedRoute != null) {
            arrayList.add(selectedRoute);
            arrayList2.add(selectedRoute.getId());
        }
        for (BluetoothRouteInfo bluetoothRouteInfo : this.mBluetoothRoutes.values()) {
            if (!arrayList2.contains(bluetoothRouteInfo.mRoute.getId())) {
                arrayList.add(bluetoothRouteInfo.mRoute);
                arrayList2.add(bluetoothRouteInfo.mRoute.getId());
            }
        }
        return arrayList;
    }

    @Override // com.android.server.media.BluetoothRouteController
    public boolean updateVolumeForDevices(int i, int i2) {
        int i3;
        boolean z = false;
        if ((134217728 & i) != 0) {
            i3 = 23;
        } else if ((i & 896) != 0) {
            i3 = 8;
        } else {
            if ((i & 536870912) == 0) {
                return false;
            }
            i3 = 26;
        }
        this.mVolumeMap.put(i3, i2);
        for (BluetoothRouteInfo bluetoothRouteInfo : this.mActiveRoutes) {
            if (bluetoothRouteInfo.mRoute.getType() == i3) {
                bluetoothRouteInfo.mRoute = new MediaRoute2Info.Builder(bluetoothRouteInfo.mRoute).setVolume(i2).build();
                z = true;
            }
        }
        if (z) {
            notifyBluetoothRoutesUpdated();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBluetoothRoutesUpdated() {
        BluetoothRouteController.BluetoothRoutesUpdatedListener bluetoothRoutesUpdatedListener = this.mListener;
        if (bluetoothRoutesUpdatedListener != null) {
            bluetoothRoutesUpdatedListener.onBluetoothRoutesUpdated(getAllBluetoothRoutes());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BluetoothRouteInfo createBluetoothRoute(BluetoothDevice bluetoothDevice) {
        int i;
        BluetoothRouteInfo bluetoothRouteInfo = new BluetoothRouteInfo();
        bluetoothRouteInfo.mBtDevice = bluetoothDevice;
        String address = bluetoothDevice.getAddress();
        String name = bluetoothDevice.getName();
        if (TextUtils.isEmpty(name)) {
            name = this.mContext.getResources().getText(R.string.unknownName).toString();
        }
        bluetoothRouteInfo.mConnectedProfiles = new SparseBooleanArray();
        BluetoothA2dp bluetoothA2dp = this.mA2dpProfile;
        if (bluetoothA2dp != null && bluetoothA2dp.getConnectedDevices().contains(bluetoothDevice)) {
            bluetoothRouteInfo.mConnectedProfiles.put(2, true);
        }
        BluetoothHearingAid bluetoothHearingAid = this.mHearingAidProfile;
        if (bluetoothHearingAid == null || !bluetoothHearingAid.getConnectedDevices().contains(bluetoothDevice)) {
            i = 8;
        } else {
            bluetoothRouteInfo.mConnectedProfiles.put(21, true);
            address = HEARING_AID_ROUTE_ID_PREFIX + this.mHearingAidProfile.getHiSyncId(bluetoothDevice);
            i = 23;
        }
        BluetoothLeAudio bluetoothLeAudio = this.mLeAudioProfile;
        if (bluetoothLeAudio != null && bluetoothLeAudio.getConnectedDevices().contains(bluetoothDevice)) {
            bluetoothRouteInfo.mConnectedProfiles.put(22, true);
            address = LE_AUDIO_ROUTE_ID_PREFIX + this.mLeAudioProfile.getGroupId(bluetoothDevice);
            i = 26;
        }
        bluetoothRouteInfo.mRoute = new MediaRoute2Info.Builder(address, name).addFeature("android.media.route.feature.LIVE_AUDIO").addFeature("android.media.route.feature.LOCAL_PLAYBACK").setConnectionState(0).setDescription(this.mContext.getResources().getText(R.string.capability_title_canTakeScreenshot).toString()).setType(i).setVolumeHandling(1).setVolumeMax(this.mAudioManager.getStreamMaxVolume(3)).setAddress(bluetoothDevice.getAddress()).build();
        return bluetoothRouteInfo;
    }

    private void setRouteConnectionState(BluetoothRouteInfo bluetoothRouteInfo, int i) {
        if (bluetoothRouteInfo == null) {
            Slog.w(TAG, "setRouteConnectionState: route shouldn't be null");
            return;
        }
        if (bluetoothRouteInfo.mRoute.getConnectionState() == i) {
            return;
        }
        MediaRoute2Info.Builder connectionState = new MediaRoute2Info.Builder(bluetoothRouteInfo.mRoute).setConnectionState(i);
        connectionState.setType(bluetoothRouteInfo.getRouteType());
        if (i == 2) {
            connectionState.setVolume(this.mVolumeMap.get(bluetoothRouteInfo.getRouteType(), 0));
        }
        bluetoothRouteInfo.mRoute = connectionState.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveRoute(BluetoothRouteInfo bluetoothRouteInfo) {
        if (bluetoothRouteInfo == null) {
            Slog.w(TAG, "addActiveRoute: btRoute is null");
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "Adding active route: " + bluetoothRouteInfo.mRoute);
        }
        if (this.mActiveRoutes.contains(bluetoothRouteInfo)) {
            Slog.w(TAG, "addActiveRoute: btRoute is already added.");
        } else {
            setRouteConnectionState(bluetoothRouteInfo, 2);
            this.mActiveRoutes.add(bluetoothRouteInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeActiveRoute(BluetoothRouteInfo bluetoothRouteInfo) {
        if (DEBUG) {
            Log.d(TAG, "Removing active route: " + bluetoothRouteInfo.mRoute);
        }
        if (this.mActiveRoutes.remove(bluetoothRouteInfo)) {
            setRouteConnectionState(bluetoothRouteInfo, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearActiveRoutesWithType(int i) {
        if (DEBUG) {
            Log.d(TAG, "Clearing active routes with type. type=" + i);
        }
        Iterator<BluetoothRouteInfo> it = this.mActiveRoutes.iterator();
        while (it.hasNext()) {
            BluetoothRouteInfo next = it.next();
            if (next.mRoute.getType() == i) {
                it.remove();
                setRouteConnectionState(next, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addActiveDevices(BluetoothDevice bluetoothDevice) {
        BluetoothRouteInfo bluetoothRouteInfo = this.mBluetoothRoutes.get(bluetoothDevice.getAddress());
        if (bluetoothRouteInfo == null) {
            bluetoothRouteInfo = createBluetoothRoute(bluetoothDevice);
            this.mBluetoothRoutes.put(bluetoothDevice.getAddress(), bluetoothRouteInfo);
        }
        addActiveRoute(bluetoothRouteInfo);
        for (BluetoothRouteInfo bluetoothRouteInfo2 : this.mBluetoothRoutes.values()) {
            if (TextUtils.equals(bluetoothRouteInfo2.mRoute.getId(), bluetoothRouteInfo.mRoute.getId()) && !TextUtils.equals(bluetoothRouteInfo2.mBtDevice.getAddress(), bluetoothRouteInfo.mBtDevice.getAddress())) {
                addActiveRoute(bluetoothRouteInfo2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class BluetoothRouteInfo {
        private BluetoothDevice mBtDevice;
        private SparseBooleanArray mConnectedProfiles;
        private MediaRoute2Info mRoute;

        private BluetoothRouteInfo() {
        }

        int getRouteType() {
            if (this.mConnectedProfiles.get(21, false)) {
                return 23;
            }
            return this.mConnectedProfiles.get(22, false) ? 26 : 8;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BluetoothProfileListener implements BluetoothProfile.ServiceListener {
        private BluetoothProfileListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            List activeDevices;
            if (i == 2) {
                LegacyBluetoothRouteController.this.mA2dpProfile = (BluetoothA2dp) bluetoothProfile;
                activeDevices = LegacyBluetoothRouteController.this.mBluetoothAdapter.getActiveDevices(2);
            } else if (i == 21) {
                LegacyBluetoothRouteController.this.mHearingAidProfile = (BluetoothHearingAid) bluetoothProfile;
                activeDevices = LegacyBluetoothRouteController.this.mBluetoothAdapter.getActiveDevices(21);
            } else {
                if (i != 22) {
                    return;
                }
                LegacyBluetoothRouteController.this.mLeAudioProfile = (BluetoothLeAudio) bluetoothProfile;
                activeDevices = LegacyBluetoothRouteController.this.mBluetoothAdapter.getActiveDevices(22);
            }
            synchronized (LegacyBluetoothRouteController.this.mBtRoutesLock) {
                for (BluetoothDevice bluetoothDevice : bluetoothProfile.getConnectedDevices()) {
                    BluetoothRouteInfo bluetoothRouteInfo = (BluetoothRouteInfo) LegacyBluetoothRouteController.this.mBluetoothRoutes.get(bluetoothDevice.getAddress());
                    if (bluetoothRouteInfo == null) {
                        bluetoothRouteInfo = LegacyBluetoothRouteController.this.createBluetoothRoute(bluetoothDevice);
                        LegacyBluetoothRouteController.this.mBluetoothRoutes.put(bluetoothDevice.getAddress(), bluetoothRouteInfo);
                    }
                    if (activeDevices.contains(bluetoothDevice)) {
                        LegacyBluetoothRouteController.this.addActiveRoute(bluetoothRouteInfo);
                    }
                }
                LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            if (i == 2) {
                LegacyBluetoothRouteController.this.mA2dpProfile = null;
            } else if (i == 21) {
                LegacyBluetoothRouteController.this.mHearingAidProfile = null;
            } else {
                if (i != 22) {
                    return;
                }
                LegacyBluetoothRouteController.this.mLeAudioProfile = null;
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class AdapterStateChangedReceiver extends BroadcastReceiver {
        private AdapterStateChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            if (intExtra == 10 || intExtra == 13) {
                LegacyBluetoothRouteController.this.mBluetoothRoutes.clear();
                LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
            } else if (intExtra == 12) {
                LegacyBluetoothRouteController.this.buildBluetoothRoutes();
                if (LegacyBluetoothRouteController.this.mBluetoothRoutes.isEmpty()) {
                    return;
                }
                LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class DeviceStateChangedReceiver extends BroadcastReceiver {
        private DeviceStateChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class);
            String action = intent.getAction();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1765714821:
                    if (action.equals("android.bluetooth.action.LE_AUDIO_CONNECTION_STATE_CHANGED")) {
                        c = 0;
                        break;
                    }
                    break;
                case -749511570:
                    if (action.equals("android.bluetooth.action.LE_AUDIO_ACTIVE_DEVICE_CHANGED")) {
                        c = 1;
                        break;
                    }
                    break;
                case -612790895:
                    if (action.equals("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED")) {
                        c = 2;
                        break;
                    }
                    break;
                case 487423555:
                    if (action.equals("android.bluetooth.a2dp.profile.action.ACTIVE_DEVICE_CHANGED")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1176349464:
                    if (action.equals("android.bluetooth.hearingaid.profile.action.ACTIVE_DEVICE_CHANGED")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1244161670:
                    if (action.equals("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    handleConnectionStateChanged(22, intent, bluetoothDevice);
                    return;
                case 1:
                    LegacyBluetoothRouteController.this.clearActiveRoutesWithType(26);
                    LegacyBluetoothRouteController.this.clearActiveRoutesWithType(8);
                    if (bluetoothDevice != null) {
                        if (LegacyBluetoothRouteController.DEBUG) {
                            Log.d(LegacyBluetoothRouteController.TAG, "Setting active le audio devices. device=" + bluetoothDevice);
                        }
                        LegacyBluetoothRouteController.this.addActiveDevices(bluetoothDevice);
                    }
                    LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                    return;
                case 2:
                    handleConnectionStateChanged(21, intent, bluetoothDevice);
                    return;
                case 3:
                    LegacyBluetoothRouteController.this.clearActiveRoutesWithType(8);
                    LegacyBluetoothRouteController.this.clearActiveRoutesWithType(26);
                    if (bluetoothDevice != null) {
                        LegacyBluetoothRouteController legacyBluetoothRouteController = LegacyBluetoothRouteController.this;
                        legacyBluetoothRouteController.addActiveRoute((BluetoothRouteInfo) legacyBluetoothRouteController.mBluetoothRoutes.get(bluetoothDevice.getAddress()));
                    }
                    LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                    return;
                case 4:
                    LegacyBluetoothRouteController.this.clearActiveRoutesWithType(23);
                    if (bluetoothDevice != null) {
                        if (LegacyBluetoothRouteController.DEBUG) {
                            Log.d(LegacyBluetoothRouteController.TAG, "Setting active hearing aid devices. device=" + bluetoothDevice);
                        }
                        LegacyBluetoothRouteController.this.addActiveDevices(bluetoothDevice);
                    }
                    LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                    return;
                case 5:
                    handleConnectionStateChanged(2, intent, bluetoothDevice);
                    return;
                default:
                    return;
            }
        }

        private void handleConnectionStateChanged(int i, Intent intent, BluetoothDevice bluetoothDevice) {
            int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
            BluetoothRouteInfo bluetoothRouteInfo = (BluetoothRouteInfo) LegacyBluetoothRouteController.this.mBluetoothRoutes.get(bluetoothDevice.getAddress());
            if (intExtra == 2) {
                if (bluetoothRouteInfo == null) {
                    BluetoothRouteInfo createBluetoothRoute = LegacyBluetoothRouteController.this.createBluetoothRoute(bluetoothDevice);
                    if (createBluetoothRoute.mConnectedProfiles.size() > 0) {
                        LegacyBluetoothRouteController.this.mBluetoothRoutes.put(bluetoothDevice.getAddress(), createBluetoothRoute);
                        LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                        return;
                    }
                    return;
                }
                bluetoothRouteInfo.mConnectedProfiles.put(i, true);
                return;
            }
            if ((intExtra == 3 || intExtra == 0) && bluetoothRouteInfo != null) {
                bluetoothRouteInfo.mConnectedProfiles.delete(i);
                if (bluetoothRouteInfo.mConnectedProfiles.size() == 0) {
                    LegacyBluetoothRouteController legacyBluetoothRouteController = LegacyBluetoothRouteController.this;
                    legacyBluetoothRouteController.removeActiveRoute((BluetoothRouteInfo) legacyBluetoothRouteController.mBluetoothRoutes.remove(bluetoothDevice.getAddress()));
                    LegacyBluetoothRouteController.this.notifyBluetoothRoutesUpdated();
                }
            }
        }
    }
}
