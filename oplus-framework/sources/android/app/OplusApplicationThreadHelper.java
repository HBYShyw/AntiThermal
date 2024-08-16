package android.app;

import android.content.pm.ApplicationInfo;
import android.hardware.SystemSensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusApplicationThreadHelper implements IOplusApplicationThread {
    private static final String TAG = "OplusApplicationThreadH";
    private OplusBaseApplicationThread mOplusApplicationThread;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OplusApplicationThreadHelper(OplusBaseApplicationThread oplusApplicationThread) {
        this.mOplusApplicationThread = oplusApplicationThread;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        switch (code) {
            case 10002:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                boolean on = 1 == data.readInt();
                setDynamicalLogEnable(on);
                return true;
            case 10003:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                List<String> configs = new ArrayList<>();
                if (data.readInt() != 0) {
                    data.readStringList(configs);
                }
                setDynamicalLogConfig(configs);
                return true;
            case 10004:
                data.enforceInterface(IOplusApplicationThread.DESCRIPTOR);
                ApplicationInfo ai = (ApplicationInfo) data.readParcelable(ApplicationInfo.class.getClassLoader());
                int updateFrameworkRes = data.readInt();
                scheduleApplicationInfoChangedForSwitchUser(ai, updateFrameworkRes);
                return true;
            default:
                return false;
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void setDynamicalLogEnable(boolean on) {
        Slog.v(TAG, "setDynamicalLogEnable on " + on);
        ActivityDynamicLogHelper.setDynamicalLogEnable(on);
    }

    @Override // android.app.IOplusApplicationThread
    public void setDynamicalLogConfig(List<String> configs) {
        Log.d(TAG, "setDynamicalLogConfig: server: on " + configs);
        AppDynamicalLogEnabler.getInstance().setDynamicalLogConfig(configs);
    }

    @Override // android.app.IOplusApplicationThread
    public void scheduleApplicationInfoChangedForSwitchUser(ApplicationInfo ai, int updateFrameworkRes) {
        OplusBaseApplicationThread oplusBaseApplicationThread = this.mOplusApplicationThread;
        if (oplusBaseApplicationThread != null) {
            oplusBaseApplicationThread.scheduleApplicationInfoChangedForSwitchUser(ai, updateFrameworkRes);
        } else {
            Slog.w(TAG, "applicationInfoChangedForSwitchUser  thread was NULL ai=" + ai);
        }
    }

    @Override // android.app.IOplusApplicationThread
    public void setZoomSensorState(boolean isZoom) {
        SystemSensorManager.mInMirage = isZoom;
    }

    @Override // android.app.IOplusApplicationThread
    public void sendGfxTrim(int level) throws RemoteException {
        Log.d(TAG, "sendGfxTrim in OplusApplicationThreadHelper");
    }

    @Override // android.app.IOplusApplicationThread
    public void sendBracketModeUnitData(OplusBracketModeUnit bracketModeUnit) throws RemoteException {
        Log.d(TAG, "sendBracketModeUnitData do nothing");
    }

    @Override // android.app.IOplusApplicationThread
    public void setViewExtractData(Bundle bundle, IBinder activityToken) throws RemoteException {
        Log.d(TAG, "setViewExtractData do nothing");
    }
}
