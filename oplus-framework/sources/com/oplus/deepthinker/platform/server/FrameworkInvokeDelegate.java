package com.oplus.deepthinker.platform.server;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.oplus.deepthinker.sdk.aidl.proton.appactionpredict.PredictAABResult;
import com.oplus.deepthinker.sdk.aidl.proton.appactionpredict.PredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.DeepSleepPredictResult;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.SleepRecord;
import com.oplus.deepthinker.sdk.aidl.proton.deepsleep.TotalPredictResult;
import com.oplus.eventhub.sdk.aidl.Event;
import com.oplus.eventhub.sdk.aidl.EventConfig;
import com.oplus.eventhub.sdk.aidl.IEventCallback;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class FrameworkInvokeDelegate {
    private static final String DESCRIPTOR = "com.oplus.deepthinker.platform.server.IDeepThinkerBridge";
    private static final String TAG = "FrameworkInvokeDelegate";
    private static final int TEMP_HOLDER = -1;
    private static final int TRANSACTION_availableState = 4;
    private static final int TRANSACTION_call = 7;
    private static final int TRANSACTION_capability = 5;
    private static final int TRANSACTION_exchange = 8;
    private static final int TRANSACTION_getAllPeriodDurationTopApps = 1007;
    private static final int TRANSACTION_getAllPeriodFrequencyTopApps = 1006;
    private static final int TRANSACTION_getAppPredictResult = 1010;
    private static final int TRANSACTION_getAppPredictResultMap = 1009;
    private static final int TRANSACTION_getAppQueueSortedByComplex = 1003;
    private static final int TRANSACTION_getAppQueueSortedByCount = 1002;
    private static final int TRANSACTION_getAppQueueSortedByTime = 1001;
    private static final int TRANSACTION_getAppType = 1011;
    private static final int TRANSACTION_getAppTypeMap = 1012;
    private static final int TRANSACTION_getCertainPeriodDurationTopApps = 1005;
    private static final int TRANSACTION_getCertainPeriodFrequencyTopApps = 1004;
    private static final int TRANSACTION_getDeepSleepPredictResult = 3001;
    private static final int TRANSACTION_getDeepSleepTotalPredictResult = 3003;
    private static final int TRANSACTION_getIdleScreenResultInLongTime = 3007;
    private static final int TRANSACTION_getIdleScreenResultInMiddleTime = 3006;
    private static final int TRANSACTION_getIdleScreenResultInShortTime = 3005;
    private static final int TRANSACTION_getLastDeepSleepRecord = 3002;
    private static final int TRANSACTION_getPlatformVersion = 2;
    private static final int TRANSACTION_getPredictAABResult = 1008;
    private static final int TRANSACTION_getPredictResultWithFeedBack = 3004;
    private static final int TRANSACTION_getSmartGpsBssidList = 4001;
    private static final int TRANSACTION_registerEventCallback = 2001;
    private static final int TRANSACTION_requestGrantPermission = 6;
    private static final int TRANSACTION_unregisterEventCallback = 2002;
    private static final int VAL_PARCELABLE = 4;
    private Map<String, EventCallbackDelegate> mHolder = new ConcurrentHashMap();
    private IBinder mRemote;

    public FrameworkInvokeDelegate(IBinder server) {
        this.mRemote = server;
    }

    public void setRemote(IBinder remote) {
        this.mRemote = remote;
    }

    public IBinder getRemote() {
        return this.mRemote;
    }

    public int getPlatformVersion() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(2, _data, _reply, 0);
            _reply.readException();
            int _result = _reply.readInt();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int availableState(int code, String tag) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeInt(code);
            _data.writeString(tag);
            this.mRemote.transact(4, _data, _reply, 0);
            _reply.readException();
            int _result = _reply.readInt();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public List capability() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(5, _data, _reply, 0);
            _reply.readException();
            ClassLoader cl = getClass().getClassLoader();
            List _result = _reply.readArrayList(cl);
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public void requestGrantPermission(String permission) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(permission);
            this.mRemote.transact(6, _data, _reply, 0);
            _reply.readException();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public Bundle call(String target, String method, Bundle extra) throws RemoteException {
        Bundle _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(target);
            _data.writeString(method);
            if (extra != null) {
                _data.writeInt(1);
                extra.writeToParcel(_data, 0);
            } else {
                _data.writeInt(0);
            }
            this.mRemote.transact(7, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public IBinder exchange(String target, String method, IBinder binder) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(target);
            _data.writeString(method);
            _data.writeStrongBinder(binder);
            this.mRemote.transact(8, _data, _reply, 0);
            _reply.readException();
            IBinder _result = _reply.readStrongBinder();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public List<String> getAppQueueSortedByTime() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(1001, _data, _reply, 0);
            _reply.readException();
            List<String> _result = _reply.createStringArrayList();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public List<String> getAppQueueSortedByCount() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(1002, _data, _reply, 0);
            _reply.readException();
            List<String> _result = _reply.createStringArrayList();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public List<String> getAppQueueSortedByComplex() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(1003, _data, _reply, 0);
            _reply.readException();
            List<String> _result = _reply.createStringArrayList();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public PredictAABResult getPredictAABResult() throws RemoteException {
        PredictAABResult _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(1008, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = PredictAABResult.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public List<PredictResult> getAppPredictResultMap(String callerName) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(callerName);
            this.mRemote.transact(1009, _data, _reply, 0);
            _reply.readException();
            List<PredictResult> _result = _reply.createTypedArrayList(PredictResult.CREATOR);
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public PredictResult getAppPredictResult(String callerName) throws RemoteException {
        PredictResult _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(callerName);
            this.mRemote.transact(1010, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = PredictResult.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int getAppType(String pkgName) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(pkgName);
            this.mRemote.transact(1011, _data, _reply, 0);
            _reply.readException();
            int _result = _reply.readInt();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public Map getAppTypeMap(List<String> pkgNameList) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeStringList(pkgNameList);
            this.mRemote.transact(TRANSACTION_getAppTypeMap, _data, _reply, 0);
            _reply.readException();
            ClassLoader cl = getClass().getClassLoader();
            Map _result = _reply.readHashMap(cl);
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int registerEventCallback(String fingerPrint, IEventCallback callback, EventConfig config) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        EventCallbackDelegate delegate = null;
        if (callback != null) {
            delegate = new EventCallbackDelegate(callback);
        }
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(fingerPrint);
            _data.writeStrongBinder(callback != null ? delegate : null);
            if (config != null) {
                _data.writeInt(1);
                writeEventConfig(_data, config);
            } else {
                _data.writeInt(0);
            }
            this.mRemote.transact(2001, _data, _reply, 0);
            _reply.readException();
            int _result = _reply.readInt();
            if (_result == 1) {
                this.mHolder.put(fingerPrint, delegate);
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public int unregisterEventCallback(String fingerprint) throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(fingerprint);
            this.mRemote.transact(2002, _data, _reply, 0);
            _reply.readException();
            int _result = _reply.readInt();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
            this.mHolder.remove(fingerprint);
        }
    }

    public DeepSleepPredictResult getDeepSleepPredictResult() throws RemoteException {
        DeepSleepPredictResult _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(3001, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = DeepSleepPredictResult.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public SleepRecord getLastDeepSleepRecord() throws RemoteException {
        SleepRecord _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(3002, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = SleepRecord.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public TotalPredictResult getDeepSleepTotalPredictResult() throws RemoteException {
        TotalPredictResult _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(3003, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = TotalPredictResult.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public DeepSleepPredictResult getPredictResultWithFeedBack() throws RemoteException {
        DeepSleepPredictResult _result;
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(3004, _data, _reply, 0);
            _reply.readException();
            if (_reply.readInt() != 0) {
                _result = DeepSleepPredictResult.CREATOR.createFromParcel(_reply);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public List<String> getSmartGpsBssidList() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            this.mRemote.transact(4001, _data, _reply, 0);
            _reply.readException();
            List<String> _result = _reply.createStringArrayList();
            return _result;
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    private void writeEventConfig(Parcel parcel, EventConfig config) {
        Set<Event> eventSet = config.getEventSet();
        if (eventSet == null) {
            return;
        }
        parcel.writeInt(eventSet.size());
        for (Event event : eventSet) {
            writeEventAboveT(parcel, event);
        }
    }

    private static void writeEvent(Parcel parcel, Event event) {
        parcel.writeInt(4);
        if (event == null) {
            parcel.writeString(null);
        } else {
            parcel.writeString("com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event");
            event.writeToParcel(parcel, 0);
        }
    }

    private static void writeEventAboveT(Parcel parcel, Event event) {
        parcel.writeInt(4);
        int tempLengthPos = parcel.dataPosition();
        parcel.writeInt(-1);
        int startPos = parcel.dataPosition();
        if (event == null) {
            parcel.writeString(null);
        } else {
            parcel.writeString("com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event");
            event.writeToParcel(parcel, 0);
        }
        int endPos = parcel.dataPosition();
        int eventObjectLength = endPos - startPos;
        parcel.setDataPosition(tempLengthPos);
        parcel.writeInt(eventObjectLength);
        parcel.setDataPosition(endPos);
    }
}
