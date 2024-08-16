package android.bluetooth;

import android.content.AttributionSource;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.android.modules.utils.SynchronousResultReceiver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/* loaded from: classes.dex */
public final class BluetoothDeviceSocExtImpl implements IBluetoothDeviceSocExt {
    private static final boolean DBG;
    public static final String TAG = "BluetoothDeviceSocExtImpl";

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public BluetoothDeviceSocExtImpl(Object base) {
    }

    public void setBondingInitiatedLocally(IBluetooth service, AttributionSource attributionSource, BluetoothDevice device, boolean localInitiated) {
        Log.w(TAG, "mtk not support api setBondingInitiatedLocally");
    }

    public int getDeviceType(IBluetooth service, AttributionSource attributionSource, BluetoothDevice device) {
        Log.w(TAG, "mtk not support api getDeviceType");
        return -1;
    }

    public boolean setLeAudioStatus(IBluetooth service, AttributionSource attributionSource, BluetoothDevice device, int value) {
        if (service == null || !isBluetoothEnabled()) {
            Log.w(TAG, "Bluetooth disabled,setLeAudioStatus failed");
        } else {
            try {
                SynchronousResultReceiver<Boolean> recv = SynchronousResultReceiver.get();
                service.setLeAudioStatus(device, value, attributionSource, recv);
                return ((Boolean) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(false)).booleanValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public int getLeAudioStatus(IBluetooth service, AttributionSource attributionSource, BluetoothDevice device) {
        if (service == null || !isBluetoothEnabled()) {
            Log.w(TAG, "Bluetooth disabled,getLeAudioStatus failed");
            if (DBG) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                SynchronousResultReceiver<Integer> recv = SynchronousResultReceiver.get();
                service.getLeAudioStatus(device, attributionSource, recv);
                return ((Integer) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(0)).intValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return 0;
    }

    public boolean isLeAudioDevice(IBluetooth service, AttributionSource attributionSource, String addr) {
        if (service == null || !isBluetoothEnabled()) {
            Log.w(TAG, "BT not enabled,isLeAudioDevice failed");
        } else {
            try {
                SynchronousResultReceiver<Boolean> recv = SynchronousResultReceiver.get();
                service.isLeAudioDevice(addr, attributionSource, recv);
                return ((Boolean) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(false)).booleanValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public boolean isConnectableDevice(IBluetooth service, AttributionSource attributionSource, String addr) {
        if (service == null || !isBluetoothEnabled()) {
            Log.w(TAG, "BT not enabled,isConnectableDevice failed");
        } else {
            try {
                SynchronousResultReceiver<Boolean> recv = SynchronousResultReceiver.get();
                service.isConnectableDevice(addr, attributionSource, recv);
                return ((Boolean) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(false)).booleanValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public BluetoothDevice findBrDevice(IBluetooth service, AttributionSource attributionSource, String addr) {
        if (service == null || !isBluetoothEnabled()) {
            Log.w(TAG, "Bluetooth disabled,findBrDevice failed");
            if (DBG) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                SynchronousResultReceiver<BluetoothDevice> recv = SynchronousResultReceiver.get();
                service.findBrDevice(addr, attributionSource, recv);
                return Attributable.setAttributionSource((BluetoothDevice) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue((Object) null), attributionSource);
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return null;
    }

    public List<BluetoothDevice> findLeAudioDevices(IBluetooth service, AttributionSource attributionSource, String addr) {
        List<BluetoothDevice> defaultValue = new ArrayList<>();
        if (service == null || !isBluetoothEnabled()) {
            Log.w(TAG, "Bluetooth disabled,findLeAudioDevices failed");
            if (DBG) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        } else {
            try {
                SynchronousResultReceiver<List<BluetoothDevice>> recv = SynchronousResultReceiver.get();
                service.findLeAudioDevices(addr, attributionSource, recv);
                return Attributable.setAttributionSource((List) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(defaultValue), attributionSource);
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return defaultValue;
    }

    private boolean isBluetoothEnabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            return false;
        }
        return true;
    }
}
