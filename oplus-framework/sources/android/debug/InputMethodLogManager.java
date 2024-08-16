package android.debug;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.inputmethod.OplusInputMethodManagerInternal;

/* loaded from: classes.dex */
public class InputMethodLogManager extends IDebugLogManager {
    private static final String TAG = TAG_BASE + InputMethodLogManager.class.getSimpleName();
    private static volatile InputMethodLogManager sInstance = null;
    private Context mContext;

    public InputMethodLogManager(Context context) {
        this.mContext = context;
    }

    public static InputMethodLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (InputMethodLogManager.class) {
                if (sInstance == null) {
                    sInstance = new InputMethodLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.d(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        OplusInputMethodManagerInternal.getInstance().setAlwaysLogOn(maxSize, param);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.d(TAG, "setLogOff: ");
        OplusInputMethodManagerInternal.getInstance().setAlwaysLogOff();
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.d(TAG, "setLogDump: ");
    }
}
