package android.app;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusApplicationThread {
    public static final int APPLICATION_INFO_CHANGED_FOR_SWITCH_USER = 10004;
    public static final String DESCRIPTOR = "android.app.IApplicationThread";
    public static final int OPLUS_CALL_TRANSACTION_INDEX = 10000;
    public static final int OPLUS_FIRST_CALL_TRANSACTION = 10001;
    public static final int SEND_BRACKET_MODE_UNIT_DATA = 10007;
    public static final int SEND_EXTRACT_VIEW_DATA = 10008;
    public static final int SEND_GFX_TRIM = 10006;
    public static final int SET_DYNAMICAL_LOG_CONFIG = 10003;
    public static final int SET_DYNAMICAL_LOG_ENABLE = 10002;
    public static final int SET_ZOOM_SENSOR_STATE = 10005;

    void scheduleApplicationInfoChangedForSwitchUser(ApplicationInfo applicationInfo, int i) throws RemoteException;

    void sendBracketModeUnitData(OplusBracketModeUnit oplusBracketModeUnit) throws RemoteException;

    void sendGfxTrim(int i) throws RemoteException;

    void setDynamicalLogConfig(List<String> list) throws RemoteException;

    void setDynamicalLogEnable(boolean z) throws RemoteException;

    void setViewExtractData(Bundle bundle, IBinder iBinder) throws RemoteException;

    void setZoomSensorState(boolean z) throws RemoteException;
}
