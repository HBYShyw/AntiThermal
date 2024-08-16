package android.net.wifi.oplus.p2p;

import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusWifiP2pManager {
    void saveExternalPeerAddress(String str) throws RemoteException;

    void setCastContent(int i, int i2, int i3, String str) throws RemoteException;

    void setCastStatus(int i, int i2, int i3, String str) throws RemoteException;

    boolean setNfcTriggered(boolean z) throws RemoteException;

    boolean setPcAutonomousGo(boolean z) throws RemoteException;
}
