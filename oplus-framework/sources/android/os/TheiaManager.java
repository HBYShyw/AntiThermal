package android.os;

import android.util.Slog;
import java.util.Map;

/* loaded from: classes.dex */
public class TheiaManager {
    private static final int CODE_THEIA_SEND_FRAMEWORK_EVENT = 1;
    private static final int CODE_THEIA_UPDATE_EVENT_STATE = 2;
    public static final String TAG = "TheiaManager";
    private static final String THEIA_DEAMON_SERVICE = "TheiaBinderService";
    private static final String THEIA_DEAMON_TOKEN = "TheiaBinder";
    private static final int THEIA_EVENT_CLASS_AMS = 0;
    private static final int THEIA_EVENT_CLASS_LAUNCHER = 6;
    private static final int THEIA_EVENT_CLASS_POWER = 2;
    private static final int THEIA_EVENT_CLASS_SYSTEMUI = 7;
    private static final int THEIA_EVENT_CLASS_SYSTEM_SERVER = 3;
    private static final int THEIA_EVENT_CLASS_TRIGGER_ERROR_UPLOAD = 14;
    private static final int THEIA_EVENT_CLASS_USER_PANIC = 15;
    private static final int THEIA_EVENT_CLASS_WMS = 1;
    private static volatile TheiaManager sInstance;
    private volatile IBinder mTheiaDeamon = null;

    private TheiaManager() {
        checkServiceStatus();
    }

    public static TheiaManager getInstance() {
        if (sInstance == null) {
            synchronized (TheiaManager.class) {
                if (sInstance == null) {
                    sInstance = new TheiaManager();
                }
            }
        }
        return sInstance;
    }

    private boolean checkServiceStatus() {
        if (this.mTheiaDeamon != null) {
            return true;
        }
        synchronized (TheiaManager.class) {
            if (this.mTheiaDeamon == null) {
                this.mTheiaDeamon = ServiceManager.getService(THEIA_DEAMON_SERVICE);
            }
        }
        if (this.mTheiaDeamon != null) {
            return true;
        }
        Slog.d(TAG, "get TheiaBinderService Service failed");
        return false;
    }

    public void updateNativeEventState(Map<Long, TheiaEventState> eventStateMap, long versionId) {
        if (checkServiceStatus() && eventStateMap != null) {
            for (Map.Entry<Long, TheiaEventState> eventStateEnty : eventStateMap.entrySet()) {
                long id = eventStateEnty.getKey().longValue();
                TheiaEventState eventState = eventStateEnty.getValue();
                Parcel data = Parcel.obtain();
                data.writeInterfaceToken(THEIA_DEAMON_TOKEN);
                data.writeLong(id);
                data.writeString(eventState.getName());
                data.writeBoolean(eventState.isEnable());
                data.writeLong(eventState.getReportFreq());
                data.writeLong(eventState.getLogType());
                data.writeLong(eventState.getDelay());
                data.writeLong(eventState.getRecoveryId());
                data.writeString(eventState.getExtraInfo());
                data.writeLong(versionId);
                Slog.d(TAG, "[updateNativeEventState] " + eventState);
                try {
                    try {
                        this.mTheiaDeamon.transact(2, data, null, 1);
                    } catch (RemoteException ex) {
                        Slog.e(TAG, "sendEvent failed", ex);
                    }
                } finally {
                    data.recycle();
                }
            }
        }
    }

    public void sendEvent(TheiaEventInfo te) {
        Trace.traceBegin(64L, "Theia-sendEvent");
        sendEventInner(te);
        Trace.traceEnd(64L);
    }

    private void sendEventInner(TheiaEventInfo te) {
        if (checkServiceStatus() && te != null) {
            Parcel data = Parcel.obtain();
            data.writeInterfaceToken(THEIA_DEAMON_TOKEN);
            data.writeLong(te.mTheiaID);
            data.writeLong(te.mTimeStamp);
            data.writeLong(te.mLogInfo.getValue());
            data.writeInt(te.mPid);
            data.writeInt(te.mUid);
            if (te.mExtraInfo != null) {
                data.writeString(te.mExtraInfo);
            }
            Slog.d(TAG, "sendEvent " + te.toString());
            try {
                try {
                    this.mTheiaDeamon.transact(1, data, null, 1);
                } catch (RemoteException ex) {
                    Slog.e(TAG, "sendEvent failed", ex);
                }
            } finally {
                data.recycle();
            }
        }
    }
}
