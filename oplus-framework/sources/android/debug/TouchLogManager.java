package android.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.touchnode.OplusTouchNodeManager;

/* loaded from: classes.dex */
public class TouchLogManager extends IDebugLogManager {
    private static final String ACTION_SYSTRACE_SCREEN_SHOT = "oplus.intent.action.SCREEN_SHOT";
    private static final int DEFAULT_DID = 0;
    private static final int TP_DEBUG_MAIN_REGISTER_NODE = 161;
    private Context mContext;
    private BroadcastReceiver mScreenshotReceiver = new BroadcastReceiver() { // from class: android.debug.TouchLogManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            TouchLogManager.this.dumpTpInfo();
        }
    };
    private static final String TAG = TAG_BASE + TouchLogManager.class.getSimpleName();
    private static volatile TouchLogManager sInstance = null;

    public TouchLogManager(Context context) {
        this.mContext = context;
    }

    public static TouchLogManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (TouchLogManager.class) {
                if (sInstance == null) {
                    sInstance = new TouchLogManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) throws RemoteException {
        BroadcastReceiver broadcastReceiver;
        Context context = this.mContext;
        if (context != null && (broadcastReceiver = this.mScreenshotReceiver) != null) {
            context.registerReceiver(broadcastReceiver, new IntentFilter("oplus.intent.action.SCREEN_SHOT"));
        }
        Log.v(TAG, "setLogOn: maxSize = " + maxSize + ", param = " + param);
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() throws RemoteException {
        Log.v(TAG, "setLogOff: ");
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() throws RemoteException {
        dumpTpInfo();
        Log.v(TAG, "setLogDump: save touch diff data.");
    }

    public void dumpTpInfo() {
        String str = TAG;
        Log.d(str, "dumpTpInfo: ");
        if (OplusTouchNodeManager.getInstance().isTouchNodeSupport(0, 161)) {
            Log.d(str, "dump Tp main register info.");
            OplusTouchNodeManager.getInstance().writeNodeFile(161, "1");
        }
    }
}
