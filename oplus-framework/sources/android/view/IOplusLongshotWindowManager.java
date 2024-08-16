package android.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;

/* loaded from: classes.dex */
public interface IOplusLongshotWindowManager extends IOplusBaseWindowManager {
    public static final String LONGSHOT_SURFACECONTROL = "longshotSurfaceControl";

    void getFocusedWindowFrame(Rect rect) throws RemoteException;

    int getLongshotSurfaceLayer() throws RemoteException;

    int getLongshotSurfaceLayerByType(int i) throws RemoteException;

    IBinder getLongshotWindowByType(int i) throws RemoteException;

    SurfaceControl getLongshotWindowByTypeForR(int i) throws RemoteException;

    boolean isEdgePanelExpand() throws RemoteException;

    boolean isFloatAssistExpand() throws RemoteException;

    boolean isKeyguardShowingAndNotOccluded() throws RemoteException;

    boolean isNavigationBarVisible() throws RemoteException;

    boolean isShortcutsPanelShow() throws RemoteException;

    boolean isVolumeShow() throws RemoteException;

    void longshotInjectInput(InputEvent inputEvent, int i) throws RemoteException;

    void longshotInjectInputBegin() throws RemoteException;

    void longshotInjectInputEnd() throws RemoteException;

    void longshotNotifyConnected(boolean z) throws RemoteException;

    void requestScrollCapture(int i, IBinder iBinder, int i2, IOplusScrollCaptureResponseListener iOplusScrollCaptureResponseListener, Bundle bundle) throws RemoteException;
}
