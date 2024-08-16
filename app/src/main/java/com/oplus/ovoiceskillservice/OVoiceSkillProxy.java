package com.oplus.ovoiceskillservice;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.ovoicemanager.service.ActionRequest;
import com.oplus.ovoicemanager.service.IOVoiceSkillService;
import com.oplus.ovoiceskillservice.utils.ThreadTask;
import com.oplus.ovoiceskillservice.utils.ThreadTaskPool;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import k7.BinderPool;
import k7.IBinderCallback;
import k7.a;

/* loaded from: classes.dex */
public class OVoiceSkillProxy {
    private static final int MAX_SESSION_COUNT = 30;
    private static final String SEPARATOR = ";";
    private static final String TAG = "OVSS.OVoiceSkillProxy";
    private static final OVoiceSkillProxy sInstance = new OVoiceSkillProxy();
    private BinderPool mBinderPool;
    private Context mContext;
    private IOVoiceSkillService mOVoiceSkillService = null;
    private Map<String, SkillSession> mSessionMap = new ConcurrentHashMap();
    private List<String> mSessionList = new ArrayList();
    private ThreadLocal<OVoiceConnectionCallback> localConnectionCallbacks = new ThreadLocal<>();
    private ConnectStatus mStatus = ConnectStatus.NONE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum ConnectStatus {
        NONE,
        INIT,
        CONNECTED,
        DISCONNECTED
    }

    /* loaded from: classes.dex */
    abstract class ConnectedTask extends ThreadTask {
        ConnectedTask() {
        }

        @Override // com.oplus.ovoiceskillservice.utils.ThreadTask
        public ThreadTask.TaskGuard guard() {
            Log.d(OVoiceSkillProxy.TAG, "guard, status:" + OVoiceSkillProxy.this.mStatus);
            if (OVoiceSkillProxy.this.mStatus == ConnectStatus.CONNECTED) {
                return ThreadTask.TaskGuard.ENTER;
            }
            if (OVoiceSkillProxy.this.mStatus == ConnectStatus.DISCONNECTED) {
                return ThreadTask.TaskGuard.DISCARD;
            }
            return ThreadTask.TaskGuard.RETRY;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class InnerBinderCallback implements IBinderCallback {
        InnerBinderCallback() {
        }

        @Override // k7.IBinderCallback
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(OVoiceSkillProxy.TAG, "onServiceConnected");
            try {
                if (OVoiceSkillProxy.getInstance().mOVoiceSkillService == null && OVoiceSkillProxy.getInstance().mBinderPool.f("skill_provider") != null) {
                    OVoiceSkillProxy.getInstance().mOVoiceSkillService = IOVoiceSkillService.a.z(OVoiceSkillProxy.getInstance().mBinderPool.f("skill_provider"));
                }
                if (OVoiceSkillProxy.getInstance().mOVoiceSkillService == null) {
                    Log.d(OVoiceSkillProxy.TAG, "mOVoiceSkillService is null");
                    OVoiceSkillProxy.getInstance().mStatus = ConnectStatus.NONE;
                    OVoiceSkillProxy.sInstance.notifyAll();
                    return;
                }
                OVoiceSkillProxy.getInstance().mStatus = ConnectStatus.CONNECTED;
                OVoiceConnectionCallback oVoiceConnectionCallback = (OVoiceConnectionCallback) OVoiceSkillProxy.getInstance().localConnectionCallbacks.get();
                if (oVoiceConnectionCallback != null) {
                    oVoiceConnectionCallback.onServiceConnected();
                }
            } catch (Exception e10) {
                Log.e(OVoiceSkillProxy.TAG, "onServiceConnected error", e10);
            }
        }

        @Override // k7.IBinderCallback
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(OVoiceSkillProxy.TAG, "onServiceDisconnected");
            try {
                OVoiceConnectionCallback oVoiceConnectionCallback = (OVoiceConnectionCallback) OVoiceSkillProxy.getInstance().localConnectionCallbacks.get();
                if (oVoiceConnectionCallback != null) {
                    oVoiceConnectionCallback.onServiceDisconnected();
                }
                OVoiceSkillProxy.getInstance().mStatus = ConnectStatus.DISCONNECTED;
                OVoiceSkillProxy.getInstance().mBinderPool = null;
                OVoiceSkillProxy.getInstance().mOVoiceSkillService = null;
            } catch (Exception e10) {
                Log.e(OVoiceSkillProxy.TAG, "onServiceDisconnected error", e10);
            }
        }
    }

    private void afterNewSkillSession(final SkillSession skillSession, boolean z10) {
        if (skillSession == null) {
            Log.e(TAG, "session is null");
            return;
        }
        Log.e(TAG, "notifyNewSkillSession, processing");
        putSession(skillSession.mSessionCode, skillSession);
        Log.e(TAG, "before thread process: " + skillSession);
        ConnectedTask connectedTask = new ConnectedTask() { // from class: com.oplus.ovoiceskillservice.OVoiceSkillProxy.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.lang.Runnable
            public void run() {
                Log.d(OVoiceSkillProxy.TAG, "thread begin -> registerPendingIntent");
                Log.e(OVoiceSkillProxy.TAG, "thread processing: " + skillSession);
                if (OVoiceSkillProxy.this.isStatusValid()) {
                    SkillSession skillSession2 = skillSession;
                    if (skillSession2.mSkillActionListener != null) {
                        String str = skillSession2.mCommand;
                        if (str != null && str.equals("set_value")) {
                            Log.e(OVoiceSkillProxy.TAG, ">>> onValueChanged");
                            Log.e(OVoiceSkillProxy.TAG, "thread processing mSkillActionListener: " + skillSession.mSkillActionListener);
                            SkillSession skillSession3 = skillSession;
                            skillSession3.mSkillActionListener.onValueChanged(skillSession3, skillSession3.mCmdData);
                            return;
                        }
                        Log.e(OVoiceSkillProxy.TAG, ">>> onSessionCreated");
                        Log.e(OVoiceSkillProxy.TAG, ">>> session.mSkillActionListener: " + skillSession.mSkillActionListener);
                        SkillSession skillSession4 = skillSession;
                        skillSession4.mSkillActionListener.onSessionCreated(skillSession4);
                        Log.e(OVoiceSkillProxy.TAG, "session.mUri: " + skillSession.mUri);
                    }
                }
            }
        };
        if (z10) {
            ThreadTaskPool.add(connectedTask);
        } else {
            connectedTask.run();
        }
    }

    private synchronized boolean bindService(Context context) {
        ConnectStatus connectStatus = this.mStatus;
        if (connectStatus == ConnectStatus.CONNECTED) {
            Log.d(TAG, "already connected, return");
            return true;
        }
        ConnectStatus connectStatus2 = ConnectStatus.INIT;
        if (connectStatus == connectStatus2) {
            Log.d(TAG, "under initializing, waiting");
            return true;
        }
        this.mStatus = connectStatus2;
        this.mContext = context;
        BinderPool e10 = BinderPool.e(context, new InnerBinderCallback());
        this.mBinderPool = e10;
        if (e10 == null) {
            Log.e(TAG, "mBinderPool is null");
            return false;
        }
        e10.b(sInstance);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OVoiceSkillProxy getInstance() {
        return sInstance;
    }

    private String getSessionCodeByIntent(Intent intent) {
        Log.d(TAG, "getSessionCodeByIntent");
        if (intent != null) {
            return intent.getStringExtra("ovms_session_code");
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getVersionCode(Context context) {
        PackageInfo d10 = a.d(context);
        if (d10 == null) {
            return -1L;
        }
        Log.d(TAG, String.format("mVersionCode[%d]", Long.valueOf(d10.getLongVersionCode())));
        return d10.getLongVersionCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getVersionName(Context context) {
        PackageInfo d10 = a.d(context);
        if (d10 == null) {
            return null;
        }
        Log.d(TAG, String.format("mVersionName[%s]", d10.versionName));
        return d10.versionName;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isStatusValid() {
        if (this.mStatus != ConnectStatus.CONNECTED) {
            Log.e(TAG, "not valid status." + this.mStatus);
            return false;
        }
        if (this.mOVoiceSkillService == null) {
            Log.e(TAG, "mOVoiceSkillService is null.");
            return false;
        }
        if (this.mContext != null) {
            return true;
        }
        Log.d(TAG, "mContext is null.");
        return false;
    }

    private boolean newSkillSessionByActionRequest(ActionRequest actionRequest, SkillActionListener skillActionListener) {
        SkillSession skillSession = new SkillSession(actionRequest.l(), actionRequest.j(), skillActionListener);
        Log.d(TAG, "newSkillSessionByActionRequest, mContext: " + this.mContext);
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        if (Activity.class.isAssignableFrom(context.getClass())) {
            skillSession.mContextType = "activity";
        } else if (Service.class.isAssignableFrom(this.mContext.getClass())) {
            skillSession.mContextType = "service";
        } else {
            skillSession.mContextType = "activity";
        }
        afterNewSkillSession(skillSession, false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean newSkillSessionById(String str, String str2, SkillActionListener skillActionListener) {
        Log.d(TAG, String.format("newSkillSession[%s] actionID[%s]", str, str2));
        if (this.mContext != null && !isEmpty(str)) {
            SkillSession skillSession = new SkillSession(str, str2, skillActionListener);
            Log.d(TAG, "newSkillSessionById, mContext: " + this.mContext);
            if (Activity.class.isAssignableFrom(this.mContext.getClass())) {
                skillSession.mContextType = "activity";
            } else if (Service.class.isAssignableFrom(this.mContext.getClass())) {
                skillSession.mContextType = "service";
            } else {
                skillSession.mContextType = "activity";
            }
            afterNewSkillSession(skillSession, true);
            return true;
        }
        Log.d(TAG, "mContext or newSkillSession is null");
        return false;
    }

    private boolean newSkillSessionByIntent(Intent intent, SkillActionListener skillActionListener) {
        Log.d(TAG, "newSkillSessionByIntent");
        SkillSession skillSession = new SkillSession(getSessionCodeByIntent(intent), null, skillActionListener);
        Log.d(TAG, "newSkillSessionByIntent, skillActionListener: " + skillActionListener);
        Log.d(TAG, "newSkillSessionByIntent, mContext: " + this.mContext);
        Context context = this.mContext;
        if (context == null) {
            return false;
        }
        if (Activity.class.isAssignableFrom(context.getClass())) {
            skillSession.mContextType = "activity";
        } else if (Service.class.isAssignableFrom(this.mContext.getClass())) {
            skillSession.mContextType = "service";
        } else {
            skillSession.mContextType = "activity";
        }
        if (intent.getData() != null) {
            skillSession.mUri = intent.getData().toString();
            Log.d(TAG, "mUri: " + skillSession.mUri);
        }
        if (intent.getStringExtra("ovms_skill_cmd") != null) {
            skillSession.mCommand = intent.getStringExtra("ovms_skill_cmd");
            Log.d(TAG, "mCommand: " + skillSession.mCommand);
        }
        if (intent.getStringExtra("ovms_skill_data") != null) {
            skillSession.mCmdData = intent.getStringExtra("ovms_skill_data");
            Log.d(TAG, "mCmdData: " + skillSession.mCmdData);
        }
        afterNewSkillSession(skillSession, true);
        return true;
    }

    private void putSession(String str, final SkillSession skillSession) {
        if (str == null || skillSession == null) {
            return;
        }
        if (!this.mSessionList.contains(str)) {
            this.mSessionList.add(str);
        }
        if (this.mSessionList.size() > 30) {
            removeSession(this.mSessionList.get(0));
        }
        this.mSessionMap.put(str, skillSession);
        ThreadTaskPool.add(new ConnectedTask() { // from class: com.oplus.ovoiceskillservice.OVoiceSkillProxy.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // java.lang.Runnable
            public void run() {
                Log.d(OVoiceSkillProxy.TAG, "thread begin -> registerListener");
                if (OVoiceSkillProxy.this.mOVoiceSkillService != null && skillSession.mSkillActionListener != null) {
                    try {
                        OVoiceSkillProxy.this.mOVoiceSkillService.k(skillSession.mSessionCode, new SkillListener() { // from class: com.oplus.ovoiceskillservice.OVoiceSkillProxy.1.1
                            @Override // com.oplus.ovoiceskillservice.SkillListener, com.oplus.ovoicemanager.service.ISkillListener
                            public void onCancel(String str2) {
                                Log.d(OVoiceSkillProxy.TAG, "onCancel");
                                skillSession.cancel();
                            }

                            @Override // com.oplus.ovoiceskillservice.SkillListener, com.oplus.ovoicemanager.service.ISkillListener
                            public void onValueChanged(String str2, String str3) {
                                Log.d(OVoiceSkillProxy.TAG, "onValueChanged, sessionCode:" + str2 + ", json:" + str3);
                                skillSession.changeValue(str3);
                            }
                        });
                    } catch (RemoteException e10) {
                        e10.printStackTrace();
                    }
                    Log.d(OVoiceSkillProxy.TAG, "thread end   -> registerListener");
                    return;
                }
                Log.d(OVoiceSkillProxy.TAG, "mOVoiceSkillService: " + OVoiceSkillProxy.this.mOVoiceSkillService + "; skillActionListener: " + skillSession.mSkillActionListener);
            }
        });
    }

    private void removeSession(String str) {
        if (str == null) {
            return;
        }
        this.mSessionMap.get(str).finish();
        this.mSessionList.remove(str);
        this.mSessionMap.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deinitialize() {
        Log.d(TAG, "deinitialize");
        try {
            ThreadTaskPool.add(new ThreadTask() { // from class: com.oplus.ovoiceskillservice.OVoiceSkillProxy.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Log.d(OVoiceSkillProxy.TAG, "start deinitialize thread");
                        if (OVoiceSkillProxy.this.mOVoiceSkillService != null && OVoiceSkillProxy.this.mContext != null) {
                            Log.d(OVoiceSkillProxy.TAG, "mOVoiceSkillService.unregisterActionExecutionListenerByAppID, mContext: " + OVoiceSkillProxy.this.mContext);
                            OVoiceSkillProxy.this.mOVoiceSkillService.j(a.a(OVoiceSkillProxy.this.mContext));
                            OVoiceSkillProxy.this.mOVoiceSkillService = null;
                        }
                        if (OVoiceSkillProxy.this.mBinderPool != null) {
                            Log.d(OVoiceSkillProxy.TAG, "mBinderPool.disconnect");
                            OVoiceSkillProxy.this.mBinderPool.d();
                            OVoiceSkillProxy.this.mBinderPool = null;
                        }
                        OVoiceSkillProxy.this.mStatus = ConnectStatus.NONE;
                        Iterator it = OVoiceSkillProxy.this.mSessionMap.values().iterator();
                        while (it.hasNext()) {
                            ((SkillSession) it.next()).finish();
                        }
                        OVoiceSkillProxy.this.mSessionList.clear();
                        OVoiceSkillProxy.this.mSessionMap.clear();
                        OVoiceSkillProxy.this.mContext = null;
                    } catch (Exception e10) {
                        Log.e(OVoiceSkillProxy.TAG, "deinitialize thread process error", e10);
                    }
                }
            });
            ThreadTaskPool.shutdownAndWait(500L, sInstance);
            this.localConnectionCallbacks.remove();
        } catch (Exception e10) {
            Log.e(TAG, "ThreadTaskPool error", e10);
            this.localConnectionCallbacks.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IOVoiceSkillService getOVoiceSkillService() {
        return sInstance.mOVoiceSkillService;
    }

    public ISkillSession getSession(String str) {
        if (str == null) {
            return null;
        }
        return this.mSessionMap.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean initialize(Context context, OVoiceConnectionCallback oVoiceConnectionCallback) {
        Log.d(TAG, "initialize");
        this.localConnectionCallbacks.set(oVoiceConnectionCallback);
        ThreadTaskPool.start();
        return bindService(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean initializeByOVoiceSkillService(Context context, IOVoiceSkillService iOVoiceSkillService, OVoiceConnectionCallback oVoiceConnectionCallback) {
        Log.d(TAG, "initializeByOVoiceSkillService");
        IOVoiceSkillService iOVoiceSkillService2 = this.mOVoiceSkillService;
        if (iOVoiceSkillService2 == null || !iOVoiceSkillService2.asBinder().isBinderAlive() || !this.mOVoiceSkillService.asBinder().pingBinder()) {
            Log.d(TAG, " last oVoiceSkillService is not alive");
            this.mStatus = ConnectStatus.NONE;
        }
        ConnectStatus connectStatus = this.mStatus;
        ConnectStatus connectStatus2 = ConnectStatus.CONNECTED;
        if (connectStatus == connectStatus2) {
            return true;
        }
        this.mContext = context;
        this.mStatus = connectStatus2;
        this.mOVoiceSkillService = iOVoiceSkillService;
        if (oVoiceConnectionCallback != null) {
            this.localConnectionCallbacks.set(oVoiceConnectionCallback);
            oVoiceConnectionCallback.onServiceConnected();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean newSkillSession(Intent intent, SkillActionListener skillActionListener) {
        return newSkillSessionByIntent(intent, skillActionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean registerActionExecutionCallback(List<String> list, SkillActionListener skillActionListener) {
        String str = (String) list.stream().collect(Collectors.joining(";"));
        Log.d(TAG, String.format("registerActionExecutionCallback actionIDs[%s]", str));
        return registerActionExecutionCallback(str, skillActionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean unregisterActionExecutionCallback(List<String> list) {
        String str = (String) list.stream().collect(Collectors.joining(";"));
        Log.d(TAG, String.format("unregisterActionExecutionCallback, actionIDs[%s]", str));
        return unregisterActionExecutionCallback(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean newSkillSession(ActionRequest actionRequest, SkillActionListener skillActionListener) {
        return newSkillSessionByActionRequest(actionRequest, skillActionListener);
    }

    boolean newSkillSession(String str, String str2, SkillActionListener skillActionListener) {
        return newSkillSessionById(str, str2, skillActionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean registerActionExecutionCallback(String str, final SkillActionListener skillActionListener) {
        Log.d(TAG, String.format("registerActionExecutionCallback actionIDs[%s]", str));
        if (isEmpty(str)) {
            Log.e(TAG, "actionIDs is null or empty");
            return false;
        }
        if (skillActionListener == null) {
            Log.e(TAG, "skillActionListener is null");
            return false;
        }
        if (!isStatusValid()) {
            return false;
        }
        String a10 = a.a(this.mContext);
        Log.d(TAG, String.format("registerActionExecutionCallback, packageName[%s]appName[%s]", a.e(this.mContext), a10));
        try {
            this.mOVoiceSkillService.u(a10, str, new SkillListener() { // from class: com.oplus.ovoiceskillservice.OVoiceSkillProxy.4
                @Override // com.oplus.ovoiceskillservice.SkillListener, com.oplus.ovoicemanager.service.ISkillListener
                public void onActionExecution(String str2, String str3, final String str4) {
                    Log.d(OVoiceSkillProxy.TAG, String.format("onActionExecution: [%s][%s][%s]", str2, str3, str4));
                    if (OVoiceSkillProxy.this.mStatus != ConnectStatus.CONNECTED) {
                        Log.d(OVoiceSkillProxy.TAG, String.format("mStatus != ConnectStatus.CONNECTED, reconnect", new Object[0]));
                        OVoiceSkillProxy oVoiceSkillProxy = OVoiceSkillProxy.this;
                        oVoiceSkillProxy.initialize(oVoiceSkillProxy.mContext, (OVoiceConnectionCallback) OVoiceSkillProxy.this.localConnectionCallbacks.get());
                    }
                    if (OVoiceSkillProxy.this.newSkillSessionById(str2, str3, new SkillActionListener() { // from class: com.oplus.ovoiceskillservice.OVoiceSkillProxy.4.1
                        @Override // com.oplus.ovoiceskillservice.SkillActionListener
                        public void onActionExecution(ISkillSession iSkillSession, String str5) {
                            skillActionListener.onActionExecution(iSkillSession, str5);
                        }

                        @Override // com.oplus.ovoiceskillservice.SkillActionListener
                        public void onCancel(ISkillSession iSkillSession) {
                            skillActionListener.onCancel(iSkillSession);
                        }

                        @Override // com.oplus.ovoiceskillservice.SkillActionListener
                        public void onSessionCreated(ISkillSession iSkillSession) {
                            skillActionListener.onActionExecution(iSkillSession, str4);
                        }

                        @Override // com.oplus.ovoiceskillservice.SkillActionListener
                        public void onValueChanged(ISkillSession iSkillSession, String str5) {
                            skillActionListener.onValueChanged(iSkillSession, str5);
                        }
                    })) {
                        Log.e(OVoiceSkillProxy.TAG, "newSkillSessionById succeed.");
                    }
                }

                @Override // com.oplus.ovoiceskillservice.SkillListener, com.oplus.ovoicemanager.service.ISkillListener
                public void onCancel(String str2) {
                    Log.d(OVoiceSkillProxy.TAG, "onCancel");
                    ISkillSession session = OVoiceSkillProxy.this.getSession(str2);
                    if (session != null) {
                        session.cancel();
                    }
                }

                @Override // com.oplus.ovoiceskillservice.SkillListener, com.oplus.ovoicemanager.service.ISkillListener
                public void onValueChanged(String str2, String str3) {
                    Log.d(OVoiceSkillProxy.TAG, String.format("onValueChanged: [%s][%s][%s]", str2, str3));
                    ISkillSession session = OVoiceSkillProxy.this.getSession(str2);
                    if (session == null) {
                        Log.e(OVoiceSkillProxy.TAG, String.format("invalid result, ignore. sessionCode[%s]", str2));
                    } else {
                        skillActionListener.onValueChanged(session, str3);
                    }
                }
            });
            return true;
        } catch (RemoteException e10) {
            Log.e(TAG, String.format("RemoteException,appID[%s]", a10));
            e10.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean unregisterActionExecutionCallback(String str) {
        Log.d(TAG, String.format("unregisterActionExecutionCallback, actionIDs[%s]", str));
        if (isEmpty(str)) {
            Log.e(TAG, "actionIDs is null or empty");
            return false;
        }
        if (!isStatusValid()) {
            return false;
        }
        String a10 = a.a(this.mContext);
        Log.d(TAG, String.format("unregisterActionExecutionCallback,packageName[%s]appName[%s]", a.e(this.mContext), a10));
        try {
            this.mOVoiceSkillService.b(a10, str);
            return true;
        } catch (RemoteException e10) {
            Log.e(TAG, String.format("RemoteException,appID[%s]", a10));
            e10.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean unregisterActionExecutionCallback() {
        Log.d(TAG, "unregisterAllActionExecutionCallback");
        if (!isStatusValid()) {
            return false;
        }
        String a10 = a.a(this.mContext);
        Log.d(TAG, String.format("unregisterAllActionExecutionCallback,packageName[%s]appName[%s]", a.e(this.mContext), a10));
        try {
            this.mOVoiceSkillService.j(a10);
            return true;
        } catch (RemoteException e10) {
            Log.e(TAG, String.format("RemoteException,appID[%s]", a10));
            e10.printStackTrace();
            return false;
        }
    }
}
