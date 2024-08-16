package android.view;

import android.os.Bundle;
import android.os.RemoteException;
import com.oplus.screenshot.IOplusScrollCaptureResponseListener;
import com.oplus.view.analysis.OplusWindowNode;
import java.io.FileDescriptor;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusLongshotWindow {
    OplusWindowNode longshotCollectWindow(boolean z, boolean z2) throws RemoteException;

    void longshotDump(FileDescriptor fileDescriptor, List<OplusWindowNode> list, List<OplusWindowNode> list2, String[] strArr) throws RemoteException;

    void longshotInjectInput(InputEvent inputEvent, int i) throws RemoteException;

    void longshotInjectInputBegin() throws RemoteException;

    void longshotInjectInputEnd() throws RemoteException;

    void longshotNotifyConnected(boolean z) throws RemoteException;

    void requestScrollCapture(IOplusScrollCaptureResponseListener iOplusScrollCaptureResponseListener, Bundle bundle) throws RemoteException;

    void screenshotDump(FileDescriptor fileDescriptor) throws RemoteException;
}
