package com.oplus.atlas;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import com.oplus.atlas.IOplusAtlasService;
import com.oplus.atlas.IOplusMMAtlasService;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMMAtlasService extends IOplusMMAtlasService.Stub {
    public static final String ACTION_MMATLAS_SERVICE_CONNECTED = "com.oplus.atlas.OplusMMAtlasService.CONNECTED";
    private static final int SENDMSG_NOOP = 1;
    private static final int SENDMSG_QUEUE = 2;
    private static final int SENDMSG_REPLACE = 0;
    public static final String SERVICE_NAME = "multimediaDaemon";
    private static final String TAG = "OplusMMAtlasService";
    private AtlasServiceHandler mAtlasHandler;
    private AtlasServiceThread mAtlasThread;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private IOplusAtlasService mOplusAtlasService;
    private boolean mBindServiceFlag = false;
    private BroadcastReceiver mCommonReceiver = new BroadcastReceiver() { // from class: com.oplus.atlas.OplusMMAtlasService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DebugLog.d(OplusMMAtlasService.TAG, "mCommonReceiver action = " + action);
            if (action.equals(OplusAtlasManagerDefine.ACTION_ATLAS_SERVICE_STARTED)) {
                OplusMMAtlasService.this.bindAtlasService();
            }
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.oplus.atlas.OplusMMAtlasService.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            OplusMMAtlasService.this.mOplusAtlasService = IOplusAtlasService.Stub.asInterface(service);
            if (OplusMMAtlasService.this.mContext != null) {
                try {
                    OplusMMAtlasService.this.mContext.sendBroadcastAsUser(new Intent(OplusMMAtlasService.ACTION_MMATLAS_SERVICE_CONNECTED).setFlags(1073741824), UserHandle.ALL);
                } catch (Exception e) {
                    Log.w(OplusMMAtlasService.TAG, "failed to broadcast broadcast atlasservice connected: " + e);
                }
            }
            DebugLog.d(OplusMMAtlasService.TAG, "onServiceConnected mOplusAtlasService = " + OplusMMAtlasService.this.mOplusAtlasService);
            OplusMMAtlasService.this.mBindServiceFlag = true;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            OplusMMAtlasService.this.mOplusAtlasService = null;
            OplusMMAtlasService.this.mBindServiceFlag = false;
            DebugLog.d(OplusMMAtlasService.TAG, "onServiceDisconnected");
        }
    };

    public OplusMMAtlasService(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        readSettings();
        createMediaWatchThread();
        registerCommonReceiver();
        systemRunning();
        Log.d(TAG, "-OplusMMAtlasService");
    }

    public void readSettings() {
    }

    public void systemRunning() {
        Log.d(TAG, "systemRunning");
        synchronized (this) {
            sendMsg(this.mAtlasHandler, 0, 0, 0, 0, null, 6000);
        }
    }

    private void createMediaWatchThread() {
        AtlasServiceThread atlasServiceThread = new AtlasServiceThread();
        this.mAtlasThread = atlasServiceThread;
        atlasServiceThread.start();
        waitForAtlasHandlerCreation();
    }

    private void waitForAtlasHandlerCreation() {
        synchronized (this) {
            while (this.mAtlasHandler == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while waiting on other handler.");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AtlasServiceThread extends Thread {
        AtlasServiceThread() {
            super("AtlasServiceThread");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            synchronized (OplusMMAtlasService.this) {
                OplusMMAtlasService.this.mAtlasHandler = new AtlasServiceHandler();
                OplusMMAtlasService.this.notify();
            }
            Looper.loop();
        }
    }

    private static void sendMsg(Handler handler, int msg, int existingMsgPolicy, int arg1, int arg2, Object obj, int delay) {
        if (handler == null) {
            DebugLog.e(TAG, "atlas handle is null");
            return;
        }
        if (existingMsgPolicy == 0) {
            handler.removeMessages(msg);
        } else if (existingMsgPolicy == 1 && handler.hasMessages(msg)) {
            DebugLog.d(TAG, "sendMsg: Msg " + msg + " existed!");
            return;
        }
        long time = SystemClock.uptimeMillis() + delay;
        handler.sendMessageAtTime(handler.obtainMessage(msg, arg1, arg2, obj), time);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AtlasServiceHandler extends Handler {
        private AtlasServiceHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    OplusMMAtlasService.this.atlasServiceinit();
                    return;
                default:
                    return;
            }
        }
    }

    private void registerCommonReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OplusAtlasManagerDefine.ACTION_ATLAS_SERVICE_STARTED);
        this.mContext.registerReceiver(this.mCommonReceiver, intentFilter);
    }

    public void atlasServiceinit() {
        bindAtlasService();
    }

    private IOplusAtlasService getService() {
        if (this.mOplusAtlasService == null) {
            int uid = Binder.getCallingUid();
            DebugLog.d(TAG, "getService() uid:" + uid);
            if (uid == 1000) {
                bindAtlasService();
            }
        }
        return this.mOplusAtlasService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindAtlasService() {
        if (this.mContext == null) {
            DebugLog.d(TAG, "context is null");
            return;
        }
        if (this.mOplusAtlasService == null) {
            DebugLog.d(TAG, "bind atlas Service start");
            PackageManager pm = this.mContext.getPackageManager();
            Intent implicitIntent = new Intent("com.oplus.atlas.OplusAtlasService.START");
            List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, 0);
            if (resolveInfos == null || resolveInfos.size() != 1) {
                DebugLog.d(TAG, "bindService, return not found resolveInfos.");
                sendMsg(this.mAtlasHandler, 0, 0, 0, 0, null, 2000);
                return;
            }
            ResolveInfo serviceInfo = resolveInfos.get(0);
            String packageName = serviceInfo.serviceInfo.packageName;
            String className = serviceInfo.serviceInfo.name;
            DebugLog.d(TAG, "packageName = " + packageName + " className = " + className);
            ComponentName component = new ComponentName(packageName, className);
            Intent iapIntent = new Intent();
            iapIntent.setComponent(component);
            this.mContext.startService(iapIntent);
            this.mContext.bindService(iapIntent, this.mServiceConnection, 1);
            DebugLog.d(TAG, "bindService end");
        }
    }

    private void unBindAtlasService() {
        if (this.mBindServiceFlag) {
            this.mContext.unbindService(this.mServiceConnection);
            this.mBindServiceFlag = false;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void setEvent(String event, String info) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.setEvent(event, info);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void setEventToNative(String event, String info) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.setEventToNative(event, info);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void setParameters(String keyValuePairs) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.setParameters(keyValuePairs);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getParameters(String keys) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            String result = service.getParameters(keys);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public boolean interfaceCallPermissionCheck(String function, String callPackageName) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return true;
        }
        try {
            boolean result = service.interfaceCallPermissionCheck(function, callPackageName);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return true;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void registerCallback(IOplusAtlasServiceCallback callback) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.registerCallback(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void unRegisterCallback(IOplusAtlasServiceCallback callback) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.unRegisterCallback(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void registerAudioCallback(IOplusAtlasAudioCallback callback) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.registerAudioCallback(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public void unRegisterAudioCallback(IOplusAtlasAudioCallback callback) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return;
        }
        try {
            service.unRegisterAudioCallback(callback);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public boolean checkIsInDaemonlistByName(String module, String packageName) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return false;
        }
        try {
            return service.checkIsInDaemonlistByName(module, packageName);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return false;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public boolean checkIsInDaemonlistByUid(String module, int uid) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return false;
        }
        try {
            return service.checkIsInDaemonlistByUid(module, uid);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return false;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getPackageNameByUid(int uid) {
        int callingUid = Binder.getCallingUid();
        DebugLog.d(TAG, "getPackageNameByUid() callingUid:" + callingUid);
        if (callingUid >= 10000) {
            DebugLog.d(TAG, "no permission to getPackageName, callinguid " + callingUid);
            return null;
        }
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getPackageNameByUid(uid);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getPackageNameByPid(int pid) {
        int callingUid = Binder.getCallingUid();
        DebugLog.d(TAG, "getPackageNameByPid() callingUid:" + callingUid);
        if (callingUid >= 10000) {
            DebugLog.d(TAG, "no permission to getPackageName, callinguid " + callingUid);
            return null;
        }
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getPackageNameByPid(pid);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getAttributeByAppName(String module, String packageName) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getAttributeByAppName(module, packageName);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getAttributeByAppUid(String module, int uid) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getAttributeByAppUid(module, uid);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getListInfoByAppUid(String module, int uid) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getListInfoByAppUid(module, uid);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public String getListInfoByAppName(String module, String packageName) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getListInfoByAppName(module, packageName);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    @Override // com.oplus.atlas.IOplusMMAtlasService
    public List<String> getConfigAppList(String module) {
        IOplusAtlasService service = getService();
        if (service == null) {
            return null;
        }
        try {
            return service.getConfigAppList(module);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in info", e);
            return null;
        }
    }

    /* loaded from: classes.dex */
    private final class BinderService extends IOplusMMAtlasService.Stub {
        private BinderService() {
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void setEvent(String event, String value) throws RemoteException {
            OplusMMAtlasService.this.setEvent(event, value);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void setEventToNative(String event, String value) throws RemoteException {
            OplusMMAtlasService.this.setEventToNative(event, value);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void setParameters(String keyValuePairs) throws RemoteException {
            OplusMMAtlasService.this.setParameters(keyValuePairs);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getParameters(String keyValuePairs) throws RemoteException {
            return OplusMMAtlasService.this.getParameters(keyValuePairs);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public boolean interfaceCallPermissionCheck(String function, String callPackageName) {
            return OplusMMAtlasService.this.interfaceCallPermissionCheck(function, callPackageName);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void registerCallback(IOplusAtlasServiceCallback callback) throws RemoteException {
            OplusMMAtlasService.this.registerCallback(callback);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void unRegisterCallback(IOplusAtlasServiceCallback callback) throws RemoteException {
            OplusMMAtlasService.this.unRegisterCallback(callback);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void registerAudioCallback(IOplusAtlasAudioCallback callback) throws RemoteException {
            OplusMMAtlasService.this.registerAudioCallback(callback);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public void unRegisterAudioCallback(IOplusAtlasAudioCallback callback) throws RemoteException {
            OplusMMAtlasService.this.unRegisterAudioCallback(callback);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public boolean checkIsInDaemonlistByName(String module, String packageName) {
            return OplusMMAtlasService.this.checkIsInDaemonlistByName(module, packageName);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public boolean checkIsInDaemonlistByUid(String module, int uid) {
            return OplusMMAtlasService.this.checkIsInDaemonlistByUid(module, uid);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getPackageNameByUid(int uid) {
            return OplusMMAtlasService.this.getPackageNameByUid(uid);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getPackageNameByPid(int pid) {
            return OplusMMAtlasService.this.getPackageNameByPid(pid);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getAttributeByAppName(String module, String packageName) {
            return OplusMMAtlasService.this.getAttributeByAppName(module, packageName);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getAttributeByAppUid(String module, int uid) {
            return OplusMMAtlasService.this.getAttributeByAppUid(module, uid);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getListInfoByAppUid(String module, int uid) {
            return OplusMMAtlasService.this.getListInfoByAppUid(module, uid);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public String getListInfoByAppName(String module, String packageName) {
            return OplusMMAtlasService.this.getListInfoByAppName(module, packageName);
        }

        @Override // com.oplus.atlas.IOplusMMAtlasService
        public List<String> getConfigAppList(String module) {
            return OplusMMAtlasService.this.getConfigAppList(module);
        }
    }
}
