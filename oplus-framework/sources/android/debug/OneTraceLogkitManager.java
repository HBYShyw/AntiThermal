package android.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Debug;
import android.os.IBinder;
import android.os.SharedMemory;
import android.system.ErrnoException;
import android.util.Log;
import java.io.FileDescriptor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class OneTraceLogkitManager extends IDebugLogManager {
    private static final String ACTION_TRACE_SERVER = "com.oplus.onetrace.intent.TRACE";
    private static final int MAX_BUFFER_SIZE = 8192;
    private static final int MAX_RETRY_CONNECT_TIMES = 5;
    private static final String SERVICE_NAME = "StatsManagerService";
    private static final String TAG = "OneTraceLogkitManager";
    private static final String TRACE_REMOTE_PACKAGE_NAME = "com.oplus.onetrace";
    private static volatile OneTraceLogkitManager sInstance = null;
    private Context mContext;
    private final AtomicInteger mRetryCount = new AtomicInteger(0);
    private final AtomicBoolean mServiceConnected = new AtomicBoolean(false);
    private final ServiceConnection mOTraceConnection = new ServiceConnection() { // from class: android.debug.OneTraceLogkitManager.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(OneTraceLogkitManager.TAG, "onServiceConnected");
            OneTraceLogkitManager.this.mServiceConnected.set(true);
            OneTraceLogkitManager.this.mRetryCount.set(0);
            OneTraceLogkitManager.this.startOneTraceCommand();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            Log.d(OneTraceLogkitManager.TAG, "onServiceDisconnected retry to binder service");
            OneTraceLogkitManager.this.mServiceConnected.set(false);
            if (OneTraceLogkitManager.this.mRetryCount.getAndIncrement() > 5) {
                Log.e(OneTraceLogkitManager.TAG, "try 5 times to bind onetrace Service is failed");
            } else {
                OneTraceLogkitManager.this.bindService();
            }
        }
    };

    private OneTraceLogkitManager(Context context) {
        this.mContext = context;
    }

    public static OneTraceLogkitManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (OneTraceLogkitManager.class) {
                if (sInstance == null) {
                    sInstance = new OneTraceLogkitManager(context);
                }
            }
        }
        return sInstance;
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOn(long maxSize, String param) {
        Log.d(TAG, "startOneTraceLog auto");
        bindService();
    }

    @Override // android.debug.IDebugLogManager
    public void setLogOff() {
        Log.d(TAG, "stopOneTraceLog");
        stopOneTraceCommand();
        unbindService();
    }

    @Override // android.debug.IDebugLogManager
    public void setLogDump() {
        Log.d(TAG, "flushOneTraceLog");
        stopOneTraceCommand();
        startOneTraceCommand();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOneTraceCommand() {
        String[] startRecordArgs = {"log", "--start", "auto"};
        if (!dumpAppService(SERVICE_NAME, startRecordArgs)) {
            Log.i(TAG, "Failed to start recording OT_TRACE");
        }
    }

    private void stopOneTraceCommand() {
        String[] stopRecordArgs = {"log", "--stop"};
        if (!dumpAppService(SERVICE_NAME, stopRecordArgs)) {
            Log.i(TAG, "Failed to stop recording OT_TRACE");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bindService() {
        Intent intent = new Intent(ACTION_TRACE_SERVER);
        intent.setPackage(TRACE_REMOTE_PACKAGE_NAME);
        return this.mContext.bindService(intent, this.mOTraceConnection, 1);
    }

    private void unbindService() {
        if (this.mServiceConnected.get()) {
            this.mContext.unbindService(this.mOTraceConnection);
            this.mServiceConnected.set(false);
        }
    }

    private static boolean dumpAppService(String serviceName, String[] args) {
        try {
            SharedMemory sharedMem = SharedMemory.create(TAG, 8192);
            try {
                boolean dumpAppService = dumpAppService(sharedMem.getFileDescriptor(), serviceName, args);
                if (sharedMem != null) {
                    sharedMem.close();
                }
                return dumpAppService;
            } finally {
            }
        } catch (ErrnoException e) {
            Log.e(TAG, "ErrnoException " + e.getMessage());
            return false;
        }
    }

    private static boolean dumpAppService(FileDescriptor fd, String serviceName, String[] args) {
        String[] baseArgs = {"service", serviceName};
        String[] cmds = new String[baseArgs.length + args.length];
        System.arraycopy(baseArgs, 0, cmds, 0, baseArgs.length);
        System.arraycopy(args, 0, cmds, baseArgs.length, args.length);
        return Debug.dumpService("activity", fd, cmds);
    }
}
