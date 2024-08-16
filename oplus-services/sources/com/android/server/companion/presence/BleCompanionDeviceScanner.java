package com.android.server.companion.presence;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.companion.AssociationInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Slog;
import com.android.server.companion.AssociationStore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BleCompanionDeviceScanner implements AssociationStore.OnChangeListener {
    private static final int NOTIFY_DEVICE_LOST_DELAY = 120000;
    private static final ScanSettings SCAN_SETTINGS = new ScanSettings.Builder().setCallbackType(6).setScanMode(0).build();
    private static final String TAG = "CDM_BleCompanionDeviceScanner";
    private final AssociationStore mAssociationStore;
    private BluetoothLeScanner mBleScanner;
    private BluetoothAdapter mBtAdapter;
    private final Callback mCallback;
    private boolean mScanning = false;
    private final ScanCallback mScanCallback = new ScanCallback() { // from class: com.android.server.companion.presence.BleCompanionDeviceScanner.2
        @Override // android.bluetooth.le.ScanCallback
        public void onScanResult(int i, ScanResult scanResult) {
            BluetoothDevice device = scanResult.getDevice();
            if (i == 2) {
                if (BleCompanionDeviceScanner.this.mMainThreadHandler.hasNotifyDeviceLostMessages(device)) {
                    BleCompanionDeviceScanner.this.mMainThreadHandler.removeNotifyDeviceLostMessages(device);
                    return;
                } else {
                    BleCompanionDeviceScanner.this.notifyDeviceFound(device);
                    return;
                }
            }
            if (i == 4) {
                BleCompanionDeviceScanner.this.mMainThreadHandler.sendNotifyDeviceLostDelayedMessage(device);
                return;
            }
            Slog.wtf(BleCompanionDeviceScanner.TAG, "Unexpected callback " + BleCompanionDeviceScanner.nameForBleScanCallbackType(i));
        }

        @Override // android.bluetooth.le.ScanCallback
        public void onScanFailed(int i) {
            BleCompanionDeviceScanner.this.mScanning = false;
        }
    };
    private final MainThreadHandler mMainThreadHandler = new MainThreadHandler();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Callback {
        void onBleCompanionDeviceFound(int i);

        void onBleCompanionDeviceLost(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BleCompanionDeviceScanner(AssociationStore associationStore, Callback callback) {
        this.mAssociationStore = associationStore;
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(Context context, BluetoothAdapter bluetoothAdapter) {
        if (this.mBtAdapter != null) {
            throw new IllegalStateException(getClass().getSimpleName() + " is already initialized");
        }
        Objects.requireNonNull(bluetoothAdapter);
        this.mBtAdapter = bluetoothAdapter;
        checkBleState();
        registerBluetoothStateBroadcastReceiver(context);
        this.mAssociationStore.registerListener(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void restartScan() {
        enforceInitialized();
        if (this.mBleScanner == null) {
            return;
        }
        stopScanIfNeeded();
        startScan();
    }

    @Override // com.android.server.companion.AssociationStore.OnChangeListener
    public void onAssociationChanged(int i, AssociationInfo associationInfo) {
        if (Looper.getMainLooper().isCurrentThread()) {
            restartScan();
        } else {
            this.mMainThreadHandler.post(new Runnable() { // from class: com.android.server.companion.presence.BleCompanionDeviceScanner$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BleCompanionDeviceScanner.this.restartScan();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkBleState() {
        enforceInitialized();
        boolean isLeEnabled = this.mBtAdapter.isLeEnabled();
        if (!isLeEnabled || this.mBleScanner == null) {
            if (isLeEnabled || this.mBleScanner != null) {
                if (isLeEnabled) {
                    BluetoothLeScanner bluetoothLeScanner = this.mBtAdapter.getBluetoothLeScanner();
                    this.mBleScanner = bluetoothLeScanner;
                    if (bluetoothLeScanner == null) {
                        return;
                    }
                    startScan();
                    return;
                }
                stopScanIfNeeded();
                this.mBleScanner = null;
            }
        }
    }

    private void startScan() {
        String deviceMacAddressAsString;
        enforceInitialized();
        if (this.mScanning) {
            Slog.w(TAG, "Scan is already in progress.");
            return;
        }
        if (this.mBleScanner == null) {
            Slog.w(TAG, "BLE is not available.");
            return;
        }
        HashSet hashSet = new HashSet();
        for (AssociationInfo associationInfo : this.mAssociationStore.getAssociations()) {
            if (associationInfo.isNotifyOnDeviceNearby() && (deviceMacAddressAsString = associationInfo.getDeviceMacAddressAsString()) != null) {
                hashSet.add(deviceMacAddressAsString);
            }
        }
        if (hashSet.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList(hashSet.size());
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            arrayList.add(new ScanFilter.Builder().setDeviceAddress((String) it.next()).build());
        }
        if (this.mBtAdapter.isLeEnabled()) {
            try {
                this.mBleScanner.startScan(arrayList, SCAN_SETTINGS, this.mScanCallback);
                this.mScanning = true;
                return;
            } catch (IllegalStateException e) {
                Slog.w(TAG, "Exception while starting BLE scanning", e);
                return;
            }
        }
        Slog.w(TAG, "BLE scanning is not turned on");
    }

    private void stopScanIfNeeded() {
        enforceInitialized();
        if (this.mScanning) {
            if (this.mBtAdapter.isLeEnabled()) {
                try {
                    this.mBleScanner.stopScan(this.mScanCallback);
                } catch (IllegalStateException e) {
                    Slog.w(TAG, "Exception while stopping BLE scanning", e);
                }
            } else {
                Slog.w(TAG, "BLE scanning is not turned on");
            }
            this.mScanning = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceFound(BluetoothDevice bluetoothDevice) {
        Iterator<AssociationInfo> it = this.mAssociationStore.getAssociationsByAddress(bluetoothDevice.getAddress()).iterator();
        while (it.hasNext()) {
            this.mCallback.onBleCompanionDeviceFound(it.next().getId());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDeviceLost(BluetoothDevice bluetoothDevice) {
        Iterator<AssociationInfo> it = this.mAssociationStore.getAssociationsByAddress(bluetoothDevice.getAddress()).iterator();
        while (it.hasNext()) {
            this.mCallback.onBleCompanionDeviceLost(it.next().getId());
        }
    }

    private void registerBluetoothStateBroadcastReceiver(Context context) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.companion.presence.BleCompanionDeviceScanner.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", -1);
                intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
                BleCompanionDeviceScanner.this.checkBleState();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.BLE_STATE_CHANGED");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void enforceInitialized() {
        if (this.mBtAdapter != null) {
            return;
        }
        throw new IllegalStateException(getClass().getSimpleName() + " is not initialized");
    }

    @SuppressLint({"HandlerLeak"})
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class MainThreadHandler extends Handler {
        private static final int NOTIFY_DEVICE_LOST = 1;

        MainThreadHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            BleCompanionDeviceScanner.this.notifyDeviceLost((BluetoothDevice) message.obj);
        }

        void sendNotifyDeviceLostDelayedMessage(BluetoothDevice bluetoothDevice) {
            sendMessageDelayed(obtainMessage(1, bluetoothDevice), 120000L);
        }

        boolean hasNotifyDeviceLostMessages(BluetoothDevice bluetoothDevice) {
            return hasEqualMessages(1, bluetoothDevice);
        }

        void removeNotifyDeviceLostMessages(BluetoothDevice bluetoothDevice) {
            removeEqualMessages(1, bluetoothDevice);
        }
    }

    private static String nameForBtState(int i) {
        return BluetoothAdapter.nameForState(i) + "(" + i + ")";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String nameForBleScanCallbackType(int i) {
        return (i != 1 ? i != 2 ? i != 4 ? "Unknown" : "MATCH_LOST" : "FIRST_MATCH" : "ALL_MATCHES") + "(" + i + ")";
    }

    private static String nameForBleScanErrorCode(int i) {
        String str;
        switch (i) {
            case 1:
                str = "ALREADY_STARTED";
                break;
            case 2:
                str = "APPLICATION_REGISTRATION_FAILED";
                break;
            case 3:
                str = "INTERNAL_ERROR";
                break;
            case 4:
                str = "FEATURE_UNSUPPORTED";
                break;
            case 5:
                str = "OUT_OF_HARDWARE_RESOURCES";
                break;
            case 6:
                str = "SCANNING_TOO_FREQUENTLY";
                break;
            default:
                str = "Unknown";
                break;
        }
        return str + "(" + i + ")";
    }
}
