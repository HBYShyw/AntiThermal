package android.bluetooth;

import android.content.AttributionSource;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import com.android.modules.utils.SynchronousResultReceiver;
import java.util.concurrent.TimeoutException;

/* loaded from: classes.dex */
public final class BluetoothLeAudioSocExtImpl implements IBluetoothLeAudioSocExt {
    private static final boolean DBG;
    public static final String TAG = "BluetoothLeAudioSocExtImpl";
    private BluetoothLeAudio mBluetoothLeAudio;

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
    }

    public BluetoothLeAudioSocExtImpl(Object bluetoothLeAudio) {
        this.mBluetoothLeAudio = (BluetoothLeAudio) bluetoothLeAudio;
    }

    public boolean isFallbackDeviceUsable(IBluetoothLeAudio service, AttributionSource attributionSource) {
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                SynchronousResultReceiver<Boolean> recv = SynchronousResultReceiver.get();
                service.isFallbackDeviceUsable(attributionSource, recv);
                return ((Boolean) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(false)).booleanValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public int getAudioLocationOfSrc(BluetoothDevice device, IBluetoothLeAudio service, AttributionSource attributionSource) {
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                SynchronousResultReceiver<Integer> recv = SynchronousResultReceiver.get();
                service.getAudioLocationOfSrc(device, attributionSource, recv);
                return ((Integer) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(0)).intValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return 0;
    }

    public void pauseBroadcastAndActiveUnicast(IBluetoothLeAudio service, AttributionSource attributionSource) {
        Log.d(TAG, "pauseBroadcastAndActiveUnicast");
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            SynchronousResultReceiver<Integer> recv = SynchronousResultReceiver.get();
            service.pauseBroadcastAndActiveUnicast(attributionSource, recv);
            recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue((Object) null);
        } catch (RemoteException | TimeoutException e) {
            Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
        }
    }

    public void inactiveUnicastProfile(IBluetoothLeAudio service, AttributionSource attributionSource) {
        Log.d(TAG, "inactiveUnicastProfile");
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            SynchronousResultReceiver<Integer> recv = SynchronousResultReceiver.get();
            service.inactiveUnicastProfile(attributionSource, recv);
            recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue((Object) null);
        } catch (RemoteException | TimeoutException e) {
            Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
        }
    }

    public boolean isBroadcastEnable(IBluetoothLeAudio service, AttributionSource attributionSource) {
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                SynchronousResultReceiver<Boolean> recv = SynchronousResultReceiver.get();
                service.isBroadcastEnable(attributionSource, recv);
                return ((Boolean) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(false)).booleanValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public boolean isPreventUnicastProfile(IBluetoothLeAudio service, AttributionSource attributionSource) {
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        } else {
            try {
                SynchronousResultReceiver<Boolean> recv = SynchronousResultReceiver.get();
                service.isPreventUnicastProfile(attributionSource, recv);
                return ((Boolean) recv.awaitResultNoInterrupt(BluetoothUtils.getSyncTimeout()).getValue(false)).booleanValue();
            } catch (RemoteException | TimeoutException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        }
        return false;
    }

    public void setCallState(IBluetoothLeAudio service, AttributionSource attributionSource, boolean isCalling) {
        Log.d(TAG, "setCallState(" + isCalling + ")");
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.setCallState(isCalling, attributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            e.rethrowFromSystemServer();
        }
    }
}
