package android.hardware.display;

import android.os.Bundle;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IOplusosDisplayManager extends IOplusBaseDisplayManager {
    public static final int SET_AI_BRIGHT_SCENE_STATE_CHANGED_ = 10111;

    boolean setStateChanged(int i, Bundle bundle) throws RemoteException;
}
