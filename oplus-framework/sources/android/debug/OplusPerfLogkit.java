package android.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusPerfLogkit extends IDebugLogManager {
    private static final String ACTION_SYSTRACE_SCREEN_SHOT = "oplus.intent.action.SCREEN_SHOT";
    private static final String ACTION_TRACEUR_PARAM_PACKAGENAME = "from";
    private static final String ACTION_TRACEUR_PARAM_RESTART_KEY = "restart";
    private static final String ACTION_TRACEUR_START = "oplus.intent.action.TRACEUR_START_TRACING";
    private static final String ACTION_TRACEUR_STOP = "oplus.intent.action.TRACEUR_STOP_TRACING";
    private static final boolean OPEN_LOGKIT = true;
    private static final String PACKAGE_FROM_LOGKIT = "com.oplus.olc";
    private static final String PACKAGE_FROM_SCREEN_SHOT = "finger.screenshot";
    private static final String PACKAGE_NAME_TRACEUR = "com.android.traceur";
    private static final String PROP_PERFETTO_ENABLE = "sys.oplus.perfetto.enable";
    private static final String TAG = OplusPerfLogkit.class.getSimpleName();
    private static volatile OplusPerfLogkit sInstance = null;
    private Context mContext;
    private boolean mTraceIsOn = false;
    private BroadcastReceiver mScreenshotReceiver = new BroadcastReceiver() { // from class: android.debug.OplusPerfLogkit.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            OplusPerfLogkit.this.dumpTrace(OplusPerfLogkit.PACKAGE_FROM_SCREEN_SHOT);
        }
    };

    public OplusPerfLogkit(Context context) {
        this.mContext = context;
    }

    public static OplusPerfLogkit getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OplusPerfLogkit.class) {
                if (sInstance == null) {
                    sInstance = new OplusPerfLogkit(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) {
        BroadcastReceiver broadcastReceiver;
        String str = TAG;
        Log.v(str, "setLogOn: maxSize = " + maxSize + ", param = " + param);
        if (this.mTraceIsOn) {
            Log.w(str, "trace is on, no need start again");
            return;
        }
        startTracing(PACKAGE_FROM_LOGKIT);
        this.mTraceIsOn = true;
        Context context = this.mContext;
        if (context != null && (broadcastReceiver = this.mScreenshotReceiver) != null) {
            context.registerReceiver(broadcastReceiver, new IntentFilter("oplus.intent.action.SCREEN_SHOT"));
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() {
        BroadcastReceiver broadcastReceiver;
        Log.v(TAG, "setLogOff ...");
        stopTracing(PACKAGE_FROM_LOGKIT);
        this.mTraceIsOn = false;
        Context context = this.mContext;
        if (context != null && (broadcastReceiver = this.mScreenshotReceiver) != null) {
            context.unregisterReceiver(broadcastReceiver);
            this.mScreenshotReceiver = null;
        }
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() {
        Log.v(TAG, "dump trace, and Open the next round of logging");
        dumpTrace(PACKAGE_FROM_LOGKIT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpTrace(String owner) {
        if (this.mContext != null) {
            Intent intent = new Intent(ACTION_TRACEUR_STOP);
            intent.setPackage(PACKAGE_NAME_TRACEUR);
            intent.putExtra(ACTION_TRACEUR_PARAM_RESTART_KEY, true);
            intent.putExtra(ACTION_TRACEUR_PARAM_PACKAGENAME, owner);
            this.mContext.startService(intent);
        }
    }

    private void startTracing(String owner) {
        SystemProperties.set(PROP_PERFETTO_ENABLE, "true");
        if (this.mContext != null) {
            Intent intent = new Intent(ACTION_TRACEUR_START);
            intent.setPackage(PACKAGE_NAME_TRACEUR);
            intent.putExtra(ACTION_TRACEUR_PARAM_PACKAGENAME, owner);
            this.mContext.startService(intent);
        }
    }

    private void stopTracing(String owner) {
        SystemProperties.set(PROP_PERFETTO_ENABLE, "false");
        if (this.mContext != null) {
            Intent intent = new Intent(ACTION_TRACEUR_STOP);
            intent.setPackage(PACKAGE_NAME_TRACEUR);
            intent.putExtra(ACTION_TRACEUR_PARAM_RESTART_KEY, false);
            intent.putExtra(ACTION_TRACEUR_PARAM_PACKAGENAME, owner);
            this.mContext.startService(intent);
        }
    }
}
