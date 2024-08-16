package android.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;

/* loaded from: classes.dex */
public class OplusLongshotWindowManager implements IOplusLongshotWindowManager {
    private static final String TAG = "OplusLongshotWindowManager";

    @Override // android.view.IOplusLongshotWindowManager
    public void getFocusedWindowFrame(Rect frame) throws RemoteException {
        OplusWindowManager.getInstance().getFocusedWindowFrame(frame);
    }

    @Override // android.view.IOplusLongshotWindowManager
    public int getLongshotSurfaceLayer() throws RemoteException {
        return OplusWindowManager.getInstance().getLongshotSurfaceLayer();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public int getLongshotSurfaceLayerByType(int type) throws RemoteException {
        return OplusWindowManager.getInstance().getLongshotSurfaceLayerByType(type);
    }

    @Override // android.view.IOplusLongshotWindowManager
    public void longshotInjectInput(InputEvent event, int mode) throws RemoteException {
        OplusWindowManager.getInstance().longshotInjectInput(event, mode);
    }

    @Override // android.view.IOplusLongshotWindowManager
    public void longshotNotifyConnected(boolean isConnected) throws RemoteException {
        OplusWindowManager.getInstance().longshotNotifyConnected(isConnected);
    }

    @Override // android.view.IOplusLongshotWindowManager
    public boolean isNavigationBarVisible() throws RemoteException {
        return OplusWindowManager.getInstance().isNavigationBarVisible();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public boolean isVolumeShow() throws RemoteException {
        return OplusWindowManager.getInstance().isVolumeShow();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public boolean isShortcutsPanelShow() throws RemoteException {
        return OplusWindowManager.getInstance().isShortcutsPanelShow();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public void longshotInjectInputBegin() throws RemoteException {
        OplusWindowManager.getInstance().longshotInjectInputBegin();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public boolean isKeyguardShowingAndNotOccluded() throws RemoteException {
        return OplusWindowManager.getInstance().isKeyguardShowingAndNotOccluded();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public void longshotInjectInputEnd() throws RemoteException {
        OplusWindowManager.getInstance().longshotInjectInputEnd();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public IBinder getLongshotWindowByType(int type) throws RemoteException {
        return OplusWindowManager.getInstance().getLongshotWindowByType(type);
    }

    @Override // android.view.IOplusLongshotWindowManager
    public SurfaceControl getLongshotWindowByTypeForR(int type) throws RemoteException {
        return OplusWindowManager.getInstance().getLongshotWindowByTypeForR(type);
    }

    @Override // android.view.IOplusLongshotWindowManager
    public boolean isFloatAssistExpand() throws RemoteException {
        return OplusWindowManager.getInstance().isFloatAssistExpand();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public boolean isEdgePanelExpand() throws RemoteException {
        return OplusWindowManager.getInstance().isEdgePanelExpand();
    }

    @Override // android.view.IOplusLongshotWindowManager
    public void requestScrollCapture(int displayId, IBinder behindClient, int taskId, IOplusScrollCaptureResponseListener listener, Bundle extras) throws RemoteException {
        OplusWindowManager.getInstance().requestScrollCapture(displayId, behindClient, taskId, listener, extras);
    }
}
