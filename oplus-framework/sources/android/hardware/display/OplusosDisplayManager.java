package android.hardware.display;

import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Slog;

/* loaded from: classes.dex */
public class OplusosDisplayManager extends OplusBaseDisplayManager implements IOplusosDisplayManager {
    public static final String KEY_GAME_LOCK_SWITCH = "game_lock_switch";
    public static final int MSG_GAME_SPACE = 0;
    private static final String TAG = "OplusosDisplayManager";

    @Override // android.hardware.display.IOplusosDisplayManager
    public boolean setStateChanged(int msgId, Bundle extraData) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        Slog.d(TAG, "setStateChanged +++ msgId=" + msgId + " data=" + extraData);
        try {
            data.writeInterfaceToken(IOplusBaseDisplayManager.DESCRIPTOR);
            data.writeInt(msgId);
            data.writeBundle(extraData);
            this.mRemote.transact(IOplusosDisplayManager.SET_AI_BRIGHT_SCENE_STATE_CHANGED_, data, reply, 0);
            reply.readException();
            boolean success = Boolean.valueOf(reply.readString()).booleanValue();
            data.recycle();
            reply.recycle();
            Slog.d(TAG, "setStateChanged --- msgId=" + msgId + " data=" + extraData + " success=" + success);
            return success;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
            throw th;
        }
    }
}
