package android.debug;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

/* loaded from: classes.dex */
public class ThirdPartLogManager extends IDebugLogManager {
    private static final String MM_MOVE_XLOG_ACTION = "wechat.shell.MOVE_XLOG";
    private static final String MM_PKG = "com.tencent.mm";
    private static final String TAG = TAG_BASE + ThirdPartLogManager.class.getSimpleName();
    private static volatile ThirdPartLogManager sInstance = null;
    private Context mContext;

    public ThirdPartLogManager(Context context) {
        this.mContext = context;
    }

    public static ThirdPartLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ThirdPartLogManager.class) {
                if (sInstance == null) {
                    sInstance = new ThirdPartLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        Log.v(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param + ", context = " + this.mContext);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.v(TAG, "setLogOff: ");
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        Log.v(TAG, "setLogDump: ");
        if (this.mContext == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setPackage(MM_PKG);
        intent.setAction(MM_MOVE_XLOG_ACTION);
        this.mContext.sendBroadcast(intent);
    }
}
