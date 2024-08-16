package com.oplus.util;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.Log;
import com.oplus.thermalcontrol.IThermalStatusListener;
import com.oplus.util.OplusHoraeThermalHelper;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class OplusHoraeThermalHelper {
    private static final int AMBIENTTHERMAL_TIME = 6;
    private static final int BIND_PROCESS_TASKS = 101;
    private static final String DESCRIPTOR = "com.oplus.horae.IHoraeService";
    private static final int GET_ALL_SHELL_TEMPS = 21;
    private static final int GET_AMBIENT_TEMP = 25;
    private static final int GET_SAFETY_TYPE = 23;
    private static final int GET_SHELL_TEMP_TYPE = 22;
    private static final int GET_SPECIFIED_TEMPS = 20;
    private static final long GET_TEMP_INTERVAL = 5000;
    private static final int HORAE_ON = 1;
    private static final int OLDSAFETY_TYPE_TIME = 4;
    private static final int SHELLTEMPS_TIME = 2;
    private static final int SHELLTEMP_TYPE_TIME = 3;
    private static final int SKINTHERMAL_TIME = 1;
    private static final int SPECTEMPS_TIME = 5;
    private static final String TAG = "HoraeHelper";
    private static final int THERMALSTATUS_TIME = 0;
    private static final int TIME_SIZE = 7;
    private static final int TRANSACTION_GET_THERMAL_STATUS = 4;
    private static final int TRANSACTION_REGISTER_CLIENT = 1;
    private static final int TRANSACTION_UNREGISTER_CLIENT = 2;
    private int[] mAllShellTemps;
    private int mAmbientThermal;
    private IBinder.DeathRecipient mDeathRecipient;
    private int mIsOldSafetyType;
    private final ArrayMap<IThermalListener, IThermalStatusListener> mListenerMap;
    private IBinder mRemote;
    private int[] mShellTempAndType;
    private float mSkinThermal;
    private int[] mSpecifiedTemps;
    private int mThermalStatus;
    private long[] mTimeDiff;
    private static int sHoraeProp = SystemProperties.getInt("persist.sys.horae.enable", 1);
    private static final boolean sDebug = SystemProperties.getBoolean("persist.sys.assert.panic", true);

    /* loaded from: classes.dex */
    public interface IThermalListener {
        void notifyThermalBroadCast(int i, int i2);

        void thermalLevel(int i);
    }

    /* loaded from: classes.dex */
    private static class SingletonHolder {
        private static OplusHoraeThermalHelper instance = new OplusHoraeThermalHelper();

        private SingletonHolder() {
        }
    }

    public static OplusHoraeThermalHelper getInstance() {
        return SingletonHolder.instance;
    }

    private OplusHoraeThermalHelper() {
        this.mTimeDiff = new long[7];
        this.mThermalStatus = Integer.MIN_VALUE;
        this.mSkinThermal = -2.14748365E9f;
        this.mAmbientThermal = Integer.MIN_VALUE;
        this.mIsOldSafetyType = Integer.MIN_VALUE;
        this.mAllShellTemps = null;
        this.mShellTempAndType = null;
        this.mSpecifiedTemps = null;
        this.mListenerMap = new ArrayMap<>();
        this.mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.oplus.util.OplusHoraeThermalHelper.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                Log.d(OplusHoraeThermalHelper.TAG, "HoraeProxyUtils binderDied");
                OplusHoraeThermalHelper.this.mRemote = null;
                OplusHoraeThermalHelper.this.mListenerMap.clear();
            }
        };
        connHoraeService();
        for (int i = 0; i < 7; i++) {
            this.mTimeDiff[i] = SystemClock.elapsedRealtime();
        }
    }

    private boolean needConnect(int num) {
        long now = SystemClock.elapsedRealtime();
        long[] jArr = this.mTimeDiff;
        long diff = now - jArr[num];
        if (diff <= GET_TEMP_INTERVAL) {
            return false;
        }
        jArr[num] = now;
        return true;
    }

    private synchronized IBinder connHoraeService() {
        IBinder checkService = ServiceManager.checkService("horae");
        this.mRemote = checkService;
        if (checkService != null) {
            try {
                checkService.linkToDeath(this.mDeathRecipient, 0);
            } catch (RemoteException e) {
                this.mRemote = null;
            }
        }
        return this.mRemote;
    }

    public int getThermalStatus() {
        int i;
        if (!horaeEnable()) {
            return -1;
        }
        if (!needConnect(0) && (i = this.mThermalStatus) != Integer.MIN_VALUE) {
            return i;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(4, _data, _reply, 0);
            _reply.readException();
            int thermalStatus = _reply.readInt();
            if (sDebug) {
                Log.d(TAG, "thermalStatus:" + thermalStatus);
            }
            this.mThermalStatus = thermalStatus;
            return thermalStatus;
        } catch (Exception e) {
            Log.d(TAG, "getThermalStatus has Exception : " + e.getMessage());
            return -1;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public float getCurrentThermal() {
        if (!horaeEnable()) {
            return -1.0f;
        }
        if (!needConnect(1)) {
            float f = this.mSkinThermal;
            if (f != -2.14748365E9f) {
                return f;
            }
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(17, _data, _reply, 0);
            float skinThermal = _reply.readFloat();
            if (sDebug) {
                Log.d(TAG, "skinThermal:" + skinThermal);
            }
            this.mSkinThermal = skinThermal;
            return skinThermal;
        } catch (Exception e) {
            Log.d(TAG, "get SkinThermal has Exception : " + e);
            return -1.0f;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int[] getAllShellTemps() {
        int[] iArr;
        if (!horaeEnable()) {
            return null;
        }
        if (!needConnect(2) && (iArr = this.mAllShellTemps) != null) {
            return iArr;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(21, _data, _reply, 0);
            int tSize = _reply.readInt();
            if (sDebug) {
                Log.d(TAG, "getAllShellTemps tSize : " + tSize);
            }
            int[] thermalArray = new int[tSize];
            _reply.readIntArray(thermalArray);
            this.mAllShellTemps = thermalArray;
            return thermalArray;
        } catch (Exception e) {
            Log.d(TAG, "getAllShellTemps has Exception : " + e);
            return null;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int[] getShellTempAndType() {
        int[] iArr;
        if (!horaeEnable()) {
            return null;
        }
        if (!needConnect(3) && (iArr = this.mShellTempAndType) != null) {
            return iArr;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(22, _data, _reply, 0);
            int currentTemperature = _reply.readInt();
            int shellType = _reply.readInt();
            int[] result = {shellType, currentTemperature};
            if (sDebug) {
                Log.d(TAG, "currentTemperature:" + currentTemperature + " shellType:" + shellType);
            }
            this.mShellTempAndType = result;
            return result;
        } catch (Exception e) {
            Log.d(TAG, "getShellTempAndType has Exception : " + e);
            return null;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int isOldSafetyType() {
        int i;
        if (!horaeEnable()) {
            return -2;
        }
        if (!needConnect(4) && (i = this.mIsOldSafetyType) != Integer.MIN_VALUE) {
            return i;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(23, _data, _reply, 0);
            int currentType = _reply.readInt();
            this.mIsOldSafetyType = currentType;
            return currentType;
        } catch (Exception e) {
            Log.d(TAG, "getShellTempAndType has Exception : " + e);
            return -2;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int[] getSpecifiedThermals() {
        int[] iArr;
        if (!horaeEnable()) {
            return null;
        }
        if (!needConnect(5) && (iArr = this.mSpecifiedTemps) != null) {
            return iArr;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(20, _data, _reply, 0);
            int length = _reply.readInt();
            if (sDebug) {
                Log.d(TAG, "Thermal length:" + length);
            }
            if (length <= 0) {
                return null;
            }
            int[] thermalArray = new int[length];
            _reply.readIntArray(thermalArray);
            this.mSpecifiedTemps = thermalArray;
            return thermalArray;
        } catch (Exception e) {
            Log.d(TAG, "get SkinThermal has Exception : " + e);
            return null;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public String[] getBindProcessInfo() {
        if (!horaeEnable()) {
            return null;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(101, data, reply, 0);
            int length = reply.readInt();
            if (sDebug) {
                Log.d(TAG, "Thermal length:" + length);
            }
            if (length <= 0) {
                return null;
            }
            String[] processesInfoArray = new String[length];
            reply.readString16Array(processesInfoArray);
            return processesInfoArray;
        } catch (Exception e) {
            Log.d(TAG, "get SkinThermal has Exception : " + e);
            return null;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean addHoraeThermalStatusListener(Context context, IThermalListener thermalListener) {
        return addHoraeThermalStatusListener(context.getMainExecutor(), thermalListener);
    }

    public boolean addHoraeThermalStatusListener(Executor executor, IThermalListener thermalListener) {
        int _result;
        if (!horaeEnable() || executor == null || thermalListener == null) {
            return false;
        }
        IThermalStatusListener IThermalStatusListener = new AnonymousClass2(executor, thermalListener);
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeStrongBinder(IThermalStatusListener.asBinder());
            this.mRemote.transact(1, _data, _reply, 0);
            _result = _reply.readInt();
        } catch (Exception e) {
            Log.d(TAG, "registerClient has Exception : " + e);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        if (_result != 1) {
            return false;
        }
        this.mListenerMap.put(thermalListener, IThermalStatusListener);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.util.OplusHoraeThermalHelper$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends IThermalStatusListener.Stub {
        final /* synthetic */ Executor val$executor;
        final /* synthetic */ IThermalListener val$thermalListener;

        AnonymousClass2(Executor executor, IThermalListener iThermalListener) {
            this.val$executor = executor;
            this.val$thermalListener = iThermalListener;
        }

        public void empty1() throws RemoteException {
        }

        public void empty2() throws RemoteException {
        }

        public void notifyThermalStatus(final int level) {
            Log.d(OplusHoraeThermalHelper.TAG, "mIThermalStatusListener info is:" + level);
            long token = Binder.clearCallingIdentity();
            try {
                Executor executor = this.val$executor;
                final IThermalListener iThermalListener = this.val$thermalListener;
                executor.execute(new Runnable() { // from class: com.oplus.util.OplusHoraeThermalHelper$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusHoraeThermalHelper.IThermalListener.this.thermalLevel(level);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyThermalBroadCast(final int level, final int temp) {
            long token = Binder.clearCallingIdentity();
            try {
                Executor executor = this.val$executor;
                final IThermalListener iThermalListener = this.val$thermalListener;
                executor.execute(new Runnable() { // from class: com.oplus.util.OplusHoraeThermalHelper$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        OplusHoraeThermalHelper.IThermalListener.this.notifyThermalBroadCast(level, temp);
                    }
                });
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public void notifyThermalSource(int level, int temp, String heatInfo) {
        }

        public void notifyTsensorTemp(int temp) {
        }
    }

    public boolean removeHoraeThermalStatusListener(IThermalListener thermalListener) {
        IThermalStatusListener IThermalStatusListener = this.mListenerMap.get(thermalListener);
        if (IThermalStatusListener == null || !horaeEnable()) {
            return false;
        }
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            try {
                _data.writeInterfaceToken(DESCRIPTOR);
                _data.writeStrongBinder(IThermalStatusListener.asBinder());
                this.mRemote.transact(2, _data, _reply, 0);
                int _result = _reply.readInt();
                if (_result == 1) {
                    this.mListenerMap.remove(thermalListener);
                    return true;
                }
            } catch (Exception e) {
                Log.d(TAG, "removeClient has Exception : " + e);
            }
            return false;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int getAmbientThermal() {
        int i;
        if (!horaeEnable()) {
            return -1;
        }
        if (!needConnect(6) && (i = this.mAmbientThermal) != Integer.MIN_VALUE) {
            return i;
        }
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(25, data, reply, 0);
            int ambientThermal = reply.readInt();
            if (sDebug) {
                Log.d(TAG, "ambientThermal:" + ambientThermal);
            }
            this.mAmbientThermal = ambientThermal;
            return ambientThermal;
        } catch (Exception e) {
            Log.d(TAG, "get ambientThermal has Exception : " + e);
            return -1;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    private boolean horaeEnable() {
        if (sHoraeProp == 0) {
            Log.e(TAG, "horae is not open");
            return false;
        }
        if (this.mRemote == null && connHoraeService() == null) {
            Log.e(TAG, "Cannot connect to HoraeService");
            return false;
        }
        return true;
    }
}
