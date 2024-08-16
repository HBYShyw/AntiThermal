package com.android.server.companion.presence;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.companion.AssociationInfo;
import android.net.MacAddress;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.companion.AssociationStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothCompanionDeviceConnectionListener extends BluetoothAdapter.BluetoothConnectionCallback implements AssociationStore.OnChangeListener {
    private static final String TAG = "CDM_BluetoothCompanionDeviceConnectionListener";
    private final AssociationStore mAssociationStore;
    private final Callback mCallback;
    private final UserManager mUserManager;
    private final Map<MacAddress, BluetoothDevice> mAllConnectedDevices = new HashMap();

    @GuardedBy({"mPendingConnectedDevices"})
    final SparseArray<Set<BluetoothDevice>> mPendingConnectedDevices = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Callback {
        void onBluetoothCompanionDeviceConnected(int i);

        void onBluetoothCompanionDeviceDisconnected(int i);
    }

    @Override // com.android.server.companion.AssociationStore.OnChangeListener
    public void onAssociationRemoved(AssociationInfo associationInfo) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothCompanionDeviceConnectionListener(UserManager userManager, AssociationStore associationStore, Callback callback) {
        this.mAssociationStore = associationStore;
        this.mCallback = callback;
        this.mUserManager = userManager;
    }

    public void init(BluetoothAdapter bluetoothAdapter) {
        bluetoothAdapter.registerBluetoothConnectionCallback(new HandlerExecutor(Handler.getMain()), this);
        this.mAssociationStore.registerListener(this);
    }

    public void onDeviceConnected(BluetoothDevice bluetoothDevice) {
        MacAddress fromString = MacAddress.fromString(bluetoothDevice.getAddress());
        int myUserId = UserHandle.myUserId();
        if (this.mAllConnectedDevices.put(fromString, bluetoothDevice) != null) {
            return;
        }
        if (!this.mUserManager.isUserUnlockingOrUnlocked(UserHandle.myUserId())) {
            Slog.i(TAG, "Current user is not in unlocking or unlocked stage yet. Notify the application when the phone is unlocked");
            synchronized (this.mPendingConnectedDevices) {
                Set<BluetoothDevice> set = this.mPendingConnectedDevices.get(myUserId, new HashSet());
                set.add(bluetoothDevice);
                this.mPendingConnectedDevices.put(myUserId, set);
            }
            return;
        }
        onDeviceConnectivityChanged(bluetoothDevice, true);
    }

    public void onDeviceDisconnected(BluetoothDevice bluetoothDevice, int i) {
        MacAddress fromString = MacAddress.fromString(bluetoothDevice.getAddress());
        int myUserId = UserHandle.myUserId();
        if (this.mAllConnectedDevices.remove(fromString) == null) {
            return;
        }
        if (!this.mUserManager.isUserUnlockingOrUnlocked(myUserId)) {
            synchronized (this.mPendingConnectedDevices) {
                Set<BluetoothDevice> set = this.mPendingConnectedDevices.get(myUserId);
                if (set != null) {
                    set.remove(bluetoothDevice);
                }
            }
            return;
        }
        onDeviceConnectivityChanged(bluetoothDevice, false);
    }

    private void onDeviceConnectivityChanged(BluetoothDevice bluetoothDevice, boolean z) {
        Iterator<AssociationInfo> it = this.mAssociationStore.getAssociationsByAddress(bluetoothDevice.getAddress()).iterator();
        while (it.hasNext()) {
            int id = it.next().getId();
            if (z) {
                this.mCallback.onBluetoothCompanionDeviceConnected(id);
            } else {
                this.mCallback.onBluetoothCompanionDeviceDisconnected(id);
            }
        }
    }

    @Override // com.android.server.companion.AssociationStore.OnChangeListener
    public void onAssociationAdded(AssociationInfo associationInfo) {
        if (this.mAllConnectedDevices.containsKey(associationInfo.getDeviceMacAddress())) {
            this.mCallback.onBluetoothCompanionDeviceConnected(associationInfo.getId());
        }
    }

    @Override // com.android.server.companion.AssociationStore.OnChangeListener
    public void onAssociationUpdated(AssociationInfo associationInfo, boolean z) {
        if (z) {
            throw new IllegalArgumentException("Address changes are not supported.");
        }
    }
}
