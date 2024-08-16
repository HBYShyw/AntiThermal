package android.bluetooth;

import android.app.ActivityThread;
import android.app.IActivityManager;
import android.bluetooth.IBluetoothManagerCallback;
import android.bluetooth.IOplusBluetooth;
import android.bluetooth.IOplusBluetoothManager;
import android.bluetooth.IOplusBluetoothManagerCallback;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.IOplusRssiDetectCallback;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.AttributionSource;
import android.content.pm.IPackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.bluetooth.OplusBluetoothClass;
import com.oplus.bluetooth.OplusBluetoothQoSData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public final class BluetoothAdapterExtImpl implements IBluetoothAdapterExt {
    private static final int ADDRESS_TYPE_SENSELESS_RANDOM = 254;
    private static final List<String> BACKUP_RESTORE_PKG_NAMES;
    private static final boolean DBG;
    public static final String DESCRIPTOR = "android.bluetooth.IBluetooth";
    public static final int OPLUS_CALL_TRANSACTION_INDEX = 10000;
    private static final String PERMISSION = "com.oplus.permission.safe.SECURITY";
    public static final String TAG = "BluetoothAdapterExtImpl";
    private static IOplusBluetooth mOplusBluetooth;
    private static BluetoothAdapterExtImpl sAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Map<OplusBluetoothMonitorCallback, OplusBtMonitorCallbackWrapper> mBluetoothMonitorClients;
    private IBluetooth mService;
    private List<OplusServiceLifecycleCallback> mServiceLifecycleCallbacks;
    private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
    private IBluetoothManagerCallback mStateChangeCallback = new IBluetoothManagerCallback.Stub() { // from class: android.bluetooth.BluetoothAdapterExtImpl.1
        public void onBluetoothServiceUp(IBluetooth bluetoothService) throws RemoteException {
            BluetoothAdapterExtImpl.this.mServiceLock.writeLock().lock();
            try {
                try {
                    if (BluetoothAdapterExtImpl.this.mService == null) {
                        Log.w(BluetoothAdapterExtImpl.TAG, "mService is NULL");
                        BluetoothAdapterExtImpl.this.mService = bluetoothService;
                        BluetoothAdapterExtImpl.mOplusBluetooth = IOplusBluetooth.Stub.asInterface(BluetoothAdapterExtImpl.this.mService.asBinder().getExtension());
                    }
                    if (BluetoothAdapterExtImpl.mOplusBluetooth == null) {
                        BluetoothAdapterExtImpl.this.debugLog("onBluetoothServiceUp mOplusBluetooth null");
                    }
                } catch (RemoteException e) {
                    Log.e(BluetoothAdapterExtImpl.TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
                }
                synchronized (BluetoothAdapterExtImpl.this.mServiceLifecycleCallbacks) {
                    for (OplusServiceLifecycleCallback cb : BluetoothAdapterExtImpl.this.mServiceLifecycleCallbacks) {
                        if (cb != null) {
                            cb.onBluetoothServiceUp();
                        }
                    }
                }
            } finally {
                BluetoothAdapterExtImpl.this.mServiceLock.writeLock().unlock();
            }
        }

        public void onBluetoothServiceDown() throws RemoteException {
            synchronized (BluetoothAdapterExtImpl.this.mServiceLifecycleCallbacks) {
                for (OplusServiceLifecycleCallback cb : BluetoothAdapterExtImpl.this.mServiceLifecycleCallbacks) {
                    if (cb != null) {
                        cb.onBluetoothServiceDown();
                    }
                }
            }
            BluetoothAdapterExtImpl.this.mServiceLock.writeLock().lock();
            try {
                BluetoothAdapterExtImpl.mOplusBluetooth = null;
                BluetoothAdapterExtImpl.this.mService = null;
            } finally {
                BluetoothAdapterExtImpl.this.mServiceLock.writeLock().unlock();
            }
        }

        public void onBrEdrDown() throws RemoteException {
            Log.d(BluetoothAdapterExtImpl.TAG, "onBrEdrDown: reached BLE ON state");
        }
    };

    /* loaded from: classes.dex */
    public static abstract class OplusServiceLifecycleCallback {
        public abstract void onBluetoothServiceDown();

        public abstract void onBluetoothServiceUp();
    }

    static {
        DBG = !SystemProperties.getBoolean("ro.build.release_type", false) || SystemProperties.getBoolean("persist.sys.assert.panic", false);
        BACKUP_RESTORE_PKG_NAMES = Arrays.asList("com.coloros.backuprestore", "com.oneplus.backuprestore");
        sAdapter = null;
    }

    public static synchronized BluetoothAdapterExtImpl getInstance() {
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl;
        synchronized (BluetoothAdapterExtImpl.class) {
            if (sAdapter == null) {
                sAdapter = createBluetoothAdapterExtImpl();
            }
            bluetoothAdapterExtImpl = sAdapter;
        }
        return bluetoothAdapterExtImpl;
    }

    public static synchronized BluetoothAdapterExtImpl getInstance(Object base) {
        BluetoothAdapterExtImpl bluetoothAdapterExtImpl;
        synchronized (BluetoothAdapterExtImpl.class) {
            bluetoothAdapterExtImpl = getInstance();
        }
        return bluetoothAdapterExtImpl;
    }

    private BluetoothAdapterExtImpl(BluetoothAdapter adapter) {
        this.mServiceLifecycleCallbacks = null;
        this.mBluetoothAdapter = null;
        this.mBluetoothMonitorClients = null;
        this.mBluetoothAdapter = (BluetoothAdapter) Objects.requireNonNull(adapter);
        this.mServiceLifecycleCallbacks = new ArrayList();
        this.mBluetoothMonitorClients = new HashMap();
        getService();
    }

    private static BluetoothAdapterExtImpl createBluetoothAdapterExtImpl() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && sAdapter == null) {
            return new BluetoothAdapterExtImpl(adapter);
        }
        Log.e(TAG, "Bluetooth Adapter is null");
        return sAdapter;
    }

    private void getService() {
        IBluetooth iBluetooth;
        this.mServiceLock.writeLock().lock();
        try {
            try {
                IBluetooth tService = this.mBluetoothAdapter.getBluetoothService(this.mStateChangeCallback);
                if (this.mService == null) {
                    this.mService = tService;
                }
                if (mOplusBluetooth == null && (iBluetooth = this.mService) != null) {
                    IOplusBluetooth asInterface = IOplusBluetooth.Stub.asInterface(iBluetooth.asBinder().getExtension());
                    mOplusBluetooth = asInterface;
                    if (asInterface == null) {
                        debugLog("getService mOplusBluetooth null");
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.writeLock().unlock();
        }
    }

    public boolean oplusRegisterServiceLifecycleCallback(OplusServiceLifecycleCallback callback) {
        if (callback == null) {
            return false;
        }
        synchronized (this.mServiceLifecycleCallbacks) {
            this.mServiceLifecycleCallbacks.add(callback);
        }
        return true;
    }

    public void oplusUnregisterServiceLifecycleCallback(OplusServiceLifecycleCallback callback) {
        if (callback == null) {
            return;
        }
        synchronized (this.mServiceLifecycleCallbacks) {
            this.mServiceLifecycleCallbacks.remove(callback);
        }
    }

    public void setBLBlackOrWhiteList(List<String> addressList, int btCustomizeMode, boolean enable) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "setBLBlackOrWhiteList：mOplusBluetooth null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.setBLBlackOrWhiteList(addressList, btCustomizeMode, enable);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isBluetoothScoAvailableOffCall() {
        boolean result = true;
        if (mOplusBluetooth == null) {
            Log.w(TAG, "isBluetoothScoAvailableOffCall：mOplusBluetooth null!");
            return true;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.isBluetoothScoAvailableOffCall();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getBluetoothConnectionCount() {
        int result = 0;
        if (mOplusBluetooth == null) {
            Log.w(TAG, "getBluetoothConnectionCount：mOplusBluetooth null!");
            return 0;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.getBluetoothConnectionCount();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int[] getBluetoothConnectedAppPID() {
        int[] result = null;
        if (mOplusBluetooth == null) {
            Log.w(TAG, "getBluetoothConnectedAppPID：mOplusBluetooth null!");
            return null;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.getBluetoothConnectedAppPID();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void enableAutoConnectPolicy(BluetoothDevice device) {
        if (mOplusBluetooth == null || device == null) {
            Log.w(TAG, "enableAutoConnectPolicy：mOplusBluetooth null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.enableAutoConnectPolicy(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void disableAutoConnectPolicy(BluetoothDevice device) {
        if (mOplusBluetooth == null || device == null) {
            Log.w(TAG, "disableAutoConnectPolicy null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.disableAutoConnectPolicy(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void triggerFirmwareCrash() {
        Log.d(TAG, "triggerFirmwareCrash() call by:" + ActivityThread.currentPackageName());
        if (mOplusBluetooth == null) {
            Log.w(TAG, "triggerFirmwareCrash：mOplusBluetooth null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.triggerFirmwareCrash(ActivityThread.currentPackageName());
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void oplusEnableVerboseLogging(boolean verbose) {
        Log.d(TAG, "oplusEnableVerboseLogging v: " + verbose);
        if (mOplusBluetooth == null) {
            Log.e(TAG, "cannot set verbose because oplusbluetooth is null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.oplusEnableVerboseLogging(verbose);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isCarkit(BluetoothDevice device) {
        if (mOplusBluetooth == null || device == null) {
            Log.w(TAG, "oplusBluetoothAdapter isCarkit null!");
            return false;
        }
        boolean result = false;
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.isCarkit(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void addCarkit(BluetoothDevice device) {
        if (mOplusBluetooth == null || device == null) {
            Log.w(TAG, "oplusBluetoothAdapter addCarkit null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.addCarkit(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void removeCarkit(BluetoothDevice device) {
        if (mOplusBluetooth == null || device == null) {
            Log.w(TAG, "oplusBluetoothAdapter removeCarkit null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.removeCarkit(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public Map<String, String> getBluetoothMonitorReport(int monitorId, boolean reset) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "oplusBluetoothAdapter getBluetoothMonitorReport null!");
            return null;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.getBluetoothMonitorReport(monitorId, reset);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return null;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void onVoipCallStateChange(int callState, String packageName) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "oplusBluetoothAdapter onVoipCallStateChange null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.onVoipCallStateChange(callState, packageName);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean registerBluetoothCallback(IOplusBluetoothCallback iCallback) {
        if (mOplusBluetooth == null || iCallback == null) {
            Log.w(TAG, "registerBluetoothCallback register null!");
            return false;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                if (mOplusBluetooth != null) {
                    debugLog("registerBluetoothCallback");
                    mOplusBluetooth.registerBluetoothCallback(iCallback);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return true;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean unRegisterBluetoothCallback(IOplusBluetoothCallback iCallback) {
        if (mOplusBluetooth == null || iCallback == null) {
            Log.w(TAG, "unRegisterBluetoothCallback register null!");
            return false;
        }
        boolean result = false;
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.unRegisterBluetoothCallback(iCallback);
                    result = true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean registerBluetoothRssiDetectCallback(OplusBluetoothRssiDetectCallback callback) {
        if (callback == null) {
            Log.w(TAG, "callback null!");
            return false;
        }
        String packageName = ActivityThread.currentPackageName();
        BleRssiDetectCallback bleRssiDetectCallback = new BleRssiDetectCallback(callback);
        IBluetoothManager managerService = this.mBluetoothAdapter.getBluetoothManager();
        try {
            IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null) {
                Log.w(TAG, "iGatt null!");
                return false;
            }
            debugLog("registerBluetoothRssiDetectCallback");
            OplusBluetoothGatt.registerBluetoothRssiDetectCallback(iGatt, bleRssiDetectCallback, packageName);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    public boolean unregisterBluetoothRssiDetectCallback(OplusBluetoothRssiDetectCallback callback) {
        if (callback == null) {
            Log.w(TAG, "callback null!");
            return false;
        }
        String packageName = ActivityThread.currentPackageName();
        BleRssiDetectCallback bleRssiDetectCallback = new BleRssiDetectCallback(callback);
        IBluetoothManager managerService = this.mBluetoothAdapter.getBluetoothManager();
        try {
            IBluetoothGatt iGatt = managerService.getBluetoothGatt();
            if (iGatt == null) {
                Log.w(TAG, "iGatt null!");
                return false;
            }
            return OplusBluetoothGatt.unregisterBluetoothRssiDetectCallback(iGatt, bleRssiDetectCallback, packageName);
        } catch (RemoteException e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    /* loaded from: classes.dex */
    public class BleRssiDetectCallback extends IOplusRssiDetectCallback.Stub {
        private OplusBluetoothRssiDetectCallback mCallback;

        public BleRssiDetectCallback(OplusBluetoothRssiDetectCallback callback) {
            this.mCallback = callback;
        }

        @Override // android.bluetooth.le.IOplusRssiDetectCallback
        public void onRssiDetectResultCallback(ScanResult result, float modifRssi) {
            OplusBluetoothRssiDetectCallback oplusBluetoothRssiDetectCallback = this.mCallback;
            if (oplusBluetoothRssiDetectCallback != null) {
                oplusBluetoothRssiDetectCallback.onRssiDetectResultCallback(result, modifRssi);
            }
        }

        public OplusBluetoothRssiDetectCallback getCallback() {
            return this.mCallback;
        }
    }

    public void setMode(int mode, int callingPid, String callingPackage) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "oplusSetMode register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.setMode(mode, callingPid, callingPackage);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getBluetoothRecordConnectedType() {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "isBluetoothRecordConnected register null!");
            return 0;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.getBluetoothRecordConnectedType();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return 0;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isBluetoothRecordConnected() {
        boolean result = false;
        if (mOplusBluetooth == null) {
            Log.w(TAG, "isBluetoothRecordConnected register null!");
            return false;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.isBluetoothRecordConnected();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void setSpeakerphoneOn(boolean on, int callingPid, String callingPackage) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "setSpeakerphoneOn register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.setSpeakerphoneOn(on, callingPid, callingPackage);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void startBluetoothSco(int callingPid, String callingPackage) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "startBluetoothSco register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.startBluetoothSco(callingPid, callingPackage);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void stopBluetoothSco(int callingPid, String callingPackage) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "stopBluetoothSco register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.stopBluetoothSco(callingPid, callingPackage);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void setCommunicationDevice(int callingPid, String callingPackage, int deviceType) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "setCommunicationDevice register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.setCommunicationDevice(callingPid, callingPackage, deviceType);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void clearCommunicationDevice(int callingPid, String callingPackage) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "clearCommunicationDevice register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.clearCommunicationDevice(callingPid, callingPackage);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public String getRandomAddress() throws RemoteException {
        String address = null;
        if (mOplusBluetooth == null) {
            Log.w(TAG, "getRandomAddress register null!");
            return null;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    address = iOplusBluetooth.getRandomAddress();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return address;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void rejectScoState(boolean scostate, boolean isforeground, boolean isSetMode, boolean isRecordingActive, boolean isPlaybackActive, boolean isPackageInFocus, String callingPackage, boolean detectResult) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "rejectScoState register null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.rejectScoState(scostate, isforeground, isSetMode, isRecordingActive, isPlaybackActive, isPackageInFocus, callingPackage, detectResult);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean openBtAbnomalMonitor(String str, List<String> list, OplusBluetoothMonitorCallback oplusBluetoothMonitorCallback) {
        int size = list.size();
        String str2 = TAG;
        if (size == 0) {
            Log.w(TAG, "params flags is null");
            return false;
        }
        boolean z = false;
        this.mServiceLock.readLock().lock();
        try {
            try {
                if (this.mBluetoothMonitorClients.containsKey(oplusBluetoothMonitorCallback)) {
                    Log.w(TAG, "callback has been registered");
                    str2 = str2;
                } else {
                    IOplusBluetoothManager asInterface = IOplusBluetoothManager.Stub.asInterface(this.mBluetoothAdapter.getBluetoothManager().asBinder().getExtension());
                    if (asInterface != null) {
                        OplusBtMonitorCallbackWrapper oplusBtMonitorCallbackWrapper = new OplusBtMonitorCallbackWrapper(oplusBluetoothMonitorCallback);
                        oplusBtMonitorCallbackWrapper.startRegistration();
                        boolean registerBtMonitStateCallback = asInterface.registerBtMonitStateCallback(str, list, oplusBtMonitorCallbackWrapper);
                        z = registerBtMonitStateCallback;
                        str2 = registerBtMonitStateCallback;
                    } else {
                        Log.w(TAG, "iOplusBluetoothManager null");
                        str2 = str2;
                    }
                }
            } catch (RemoteException e) {
                Log.e(str2, "", e);
            }
            return z;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean closeBtAbnomalMonitor(String str, List<String> list, OplusBluetoothMonitorCallback oplusBluetoothMonitorCallback) {
        debugLog("enter closeBtAbnomalMonitor");
        int size = list.size();
        String str2 = TAG;
        if (size == 0) {
            Log.w(TAG, "params flags is null");
            return false;
        }
        boolean z = false;
        this.mServiceLock.readLock().lock();
        try {
            try {
                OplusBtMonitorCallbackWrapper remove = this.mBluetoothMonitorClients.remove(oplusBluetoothMonitorCallback);
                if (remove == null) {
                    debugLog("could not find callback wrapper");
                    str2 = str2;
                } else {
                    IOplusBluetoothManager asInterface = IOplusBluetoothManager.Stub.asInterface(this.mBluetoothAdapter.getBluetoothManager().asBinder().getExtension());
                    str2 = str2;
                    if (asInterface != null) {
                        boolean unregisterBtMonitStateCallback = asInterface.unregisterBtMonitStateCallback(str, list, remove);
                        z = unregisterBtMonitStateCallback;
                        str2 = unregisterBtMonitStateCallback;
                    }
                }
            } catch (RemoteException e) {
                Log.e(str2, "", e);
            }
            return z;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean oplusSetBTCTddMode(int tddMode) {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "oplusSetBTCTddMode register null!");
            return false;
        }
        boolean result = false;
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.oplusSetBTCTddMode(tddMode);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean allowToEnableFddMode() {
        if (mOplusBluetooth == null) {
            Log.w(TAG, "allowToEnableFddMode register null!");
            return true;
        }
        boolean result = true;
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    result = iOplusBluetooth.allowToEnableFddMode();
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return result;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void senselessConnectionStartLeAdvertising(AdvertiseSettings settings, AdvertiseData advertiseData, AdvertiseData scanResponse, AdvertiseCallback appCallback) {
        if (!checkPermissions()) {
            return;
        }
        AdvertiseSettings.Builder newSettingsBuilder = new AdvertiseSettings.Builder();
        newSettingsBuilder.setAdvertiseMode(settings.getMode());
        newSettingsBuilder.setTxPowerLevel(settings.getTxPowerLevel());
        newSettingsBuilder.setConnectable(settings.isConnectable());
        newSettingsBuilder.setDiscoverable(settings.isDiscoverable());
        newSettingsBuilder.setTimeout(settings.getTimeout());
        if (BACKUP_RESTORE_PKG_NAMES.contains(getCallerPkgName())) {
            newSettingsBuilder.setAdvertiseMode(3);
            newSettingsBuilder.setOwnAddressType(settings.getOwnAddressType());
        } else {
            newSettingsBuilder.setOwnAddressType(254);
        }
        AdvertiseSettings newSettings = newSettingsBuilder.build();
        newSettings.setIsSenselessAdv(true);
        BluetoothLeAdvertiser advertiser = this.mBluetoothAdapter.getBluetoothLeAdvertiser();
        advertiser.startAdvertising(newSettings, advertiseData, scanResponse, appCallback);
    }

    public void senselessConnectionStartLeAdvertisingSet(AdvertisingSetParameters parameters, AdvertiseData advertiseData, AdvertiseData scanResponse, PeriodicAdvertisingParameters periodicParameters, AdvertiseData periodicData, AdvertisingSetCallback callback) {
        if (!checkPermissions()) {
            return;
        }
        parameters.setIsSenselessAdv(true);
        BluetoothLeAdvertiser advertiser = this.mBluetoothAdapter.getBluetoothLeAdvertiser();
        advertiser.startAdvertisingSet(parameters, advertiseData, scanResponse, periodicParameters, periodicData, callback);
    }

    public void senselessConnectionStartLeScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback callback) {
        if (!checkPermissions()) {
            return;
        }
        ScanSettings.Builder newScanSettingsBuilder = new ScanSettings.Builder();
        newScanSettingsBuilder.setIsSenselessScan(true);
        newScanSettingsBuilder.setScanMode(6);
        newScanSettingsBuilder.setCallbackType(settings.getCallbackType());
        newScanSettingsBuilder.setScanResultType(settings.getScanResultType());
        newScanSettingsBuilder.setReportDelay(settings.getReportDelayMillis());
        newScanSettingsBuilder.setNumOfMatches(settings.getNumOfMatches());
        newScanSettingsBuilder.setMatchMode(settings.getMatchMode());
        newScanSettingsBuilder.setLegacy(settings.getLegacy());
        newScanSettingsBuilder.setPhy(settings.getPhy());
        newScanSettingsBuilder.setRssiThreshold(settings.getRssiThreshold());
        ScanSettings newScanSettings = newScanSettingsBuilder.build();
        newScanSettings.setIsSenselessScan(true);
        BluetoothLeScanner scanner = this.mBluetoothAdapter.getBluetoothLeScanner();
        scanner.startScan(filters, newScanSettings, callback);
    }

    public boolean isHDTSupported(BluetoothDevice device) {
        return false;
    }

    public int getA2dpTransmissionPath() {
        return 1;
    }

    public void setHDTEnable(boolean enable) {
    }

    public void sendDataToController(byte[] data) {
    }

    /* loaded from: classes.dex */
    private class OplusBtMonitorCallbackWrapper extends IOplusBluetoothManagerCallback.Stub {
        private OplusBluetoothMonitorCallback mCallback;

        public OplusBtMonitorCallbackWrapper(OplusBluetoothMonitorCallback callback) {
            this.mCallback = callback;
        }

        public void startRegistration() {
            BluetoothAdapterExtImpl.this.mBluetoothMonitorClients.put(this.mCallback, this);
        }

        @Override // android.bluetooth.IOplusBluetoothManagerCallback
        public void onBluetoothManagerMonitor(String monitorEvent, Map monitResult) {
            BluetoothAdapterExtImpl.this.debugLog("enter onBluetoothManagerMonitor");
            OplusBluetoothMonitorCallback oplusBluetoothMonitorCallback = this.mCallback;
            if (oplusBluetoothMonitorCallback != null) {
                oplusBluetoothMonitorCallback.onBluetoothMonitor(monitorEvent, monitResult);
            }
        }

        public OplusBluetoothMonitorCallback getCallback() {
            return this.mCallback;
        }
    }

    public void setInsecureRfcommEnhanceMode(String address, String uuid, int mode) {
        debugLog("set insecure rfcomm mode " + mode);
        if (mOplusBluetooth == null) {
            Log.w(TAG, "setInsecureRfcommEnhanceMode mOplusBluetooth null!");
            return;
        }
        try {
            try {
                this.mServiceLock.readLock().lock();
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.setInsecureRfcommEnhanceMode(address, uuid, mode);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean createBondOutOfBand(int transport, BluetoothDevice device, OplusBluetoothOobData remoteP192Data, OplusBluetoothOobData remoteP256Data, AttributionSource attributionSource) {
        debugLog("createBondOutOfBand transport is " + transport);
        if (mOplusBluetooth == null) {
            Log.e(TAG, "createBondOutOfBand mOplusBluetooth null!");
            return false;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                if (mOplusBluetooth != null) {
                    Log.e(TAG, "mOplusBluetooth.createBondOutOfBand");
                    return mOplusBluetooth.createBondOutOfBand(transport, device, remoteP192Data, remoteP256Data, attributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void generateLocalOobData(int transport, IOplusBluetoothOobDataCallback callback, AttributionSource attributionSource) {
        debugLog("generateLocalOobData transport is " + transport);
        if (mOplusBluetooth == null) {
            Log.e(TAG, "createBondOutOfBand mOplusBluetooth null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                if (mOplusBluetooth != null) {
                    Log.e(TAG, "mOplusBluetooth.generateLocalOobData");
                    mOplusBluetooth.generateLocalOobData(transport, callback, attributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getRemoteClass(BluetoothDevice device) {
        if (!checkPermissions() || mOplusBluetooth == null) {
            return OplusBluetoothClass.Device.UNKNOWN;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.getRemoteClass(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return OplusBluetoothClass.Device.UNKNOWN;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isSupportAbsoluteVolume(BluetoothDevice device) {
        debugLog("isSupportAbsoluteVolume()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "isSupportAbsoluteVolume mOplusBluetooth null!");
            return false;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.isSupportAbsoluteVolume(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isAbsoluteVolumeOn(BluetoothDevice device) {
        debugLog("isAbsoluteVolumeOn()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "isAbsoluteVolumeOn mOplusBluetooth null!");
            return false;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.isAbsoluteVolumeOn(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean setAbsoluteVolumeOn(BluetoothDevice device, boolean on) {
        debugLog("setAbsoluteVolumeOn()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "setAbsoluteVolumeOn mOplusBluetooth null!");
            return false;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.setAbsoluteVolumeOn(device, on);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public OplusBluetoothQoSData getLinkStatus() {
        if (!checkPermissions()) {
            debugLog("caller has insufficient permission to get link status");
            return null;
        }
        if (mOplusBluetooth == null) {
            debugLog("mOplusBluetooth null");
            return null;
        }
        this.mServiceLock.readLock().lock();
        try {
            return mOplusBluetooth.getLinkStatus();
        } catch (RemoteException e) {
            Log.e(TAG, "getLinkStatus remote exception");
            return null;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public void setConfigDelayReport(BluetoothDevice device, String avPlayer, int delayReport) {
        debugLog("setConfigDelayReport()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "setConfigDelayReport mOplusBluetooth null!");
            return;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    iOplusBluetooth.setConfigDelayReport(device, avPlayer, delayReport);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getRemoteDelayReport(BluetoothDevice device) {
        debugLog("getRemoteDelayReport()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "getRemoteDelayReport mOplusBluetooth null!");
            return -1;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.getRemoteDelayReport(device);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return -1;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public int getConfigDelayReport(BluetoothDevice device, String avPlayer) {
        debugLog("getConfigDelayReport()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "getConfigDelayReport mOplusBluetooth null!");
            return -1;
        }
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    return iOplusBluetooth.getConfigDelayReport(device, avPlayer);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return -1;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public boolean isCallSwitchSupported(BluetoothDevice device, AttributionSource attributionSource) {
        debugLog("isCallSwitchSupported()");
        if (mOplusBluetooth == null) {
            Log.e(TAG, "isCallSwitchSupported mOplusBluetooth null!");
            return false;
        }
        long features = 0;
        this.mServiceLock.readLock().lock();
        try {
            try {
                IOplusBluetooth iOplusBluetooth = mOplusBluetooth;
                if (iOplusBluetooth != null) {
                    features = iOplusBluetooth.getRemoteOplusFeatures(device, attributionSource);
                }
            } catch (RemoteException e) {
                Log.e(TAG, e.toString() + "\n" + Log.getStackTraceString(new Throwable()));
            }
            return (features & 1) == 1;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debugLog(String msg) {
        if (DBG) {
            Log.d(TAG, msg);
        }
    }

    private boolean checkPermissions() {
        int ret;
        IBinder b = ServiceManager.getService("activity");
        IActivityManager am = IActivityManager.Stub.asInterface(b);
        try {
            ret = am.checkPermission("com.oplus.permission.safe.SECURITY", Binder.getCallingPid(), Binder.getCallingUid());
        } catch (RemoteException e) {
            Log.e(TAG, "am remote exception");
            ret = -1;
        }
        return ret == 0;
    }

    private String getCallerPkgName() {
        int uid = Binder.getCallingUid();
        Log.w(TAG, "uid is " + uid);
        IBinder b = ServiceManager.getService("package");
        IPackageManager iPackageManager = IPackageManager.Stub.asInterface(b);
        if (iPackageManager == null) {
            return "null";
        }
        try {
            String packageName = iPackageManager.getNameForUid(uid);
            return packageName;
        } catch (RemoteException e) {
            Log.w(TAG, "remote exception");
            return "null";
        }
    }
}
