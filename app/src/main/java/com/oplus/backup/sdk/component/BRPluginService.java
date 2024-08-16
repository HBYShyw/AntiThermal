package com.oplus.backup.sdk.component;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import com.oplus.backup.sdk.common.host.BREngineConfig;
import com.oplus.backup.sdk.common.load.BRPluginLoader;
import com.oplus.backup.sdk.common.plugin.BRPluginConfig;
import com.oplus.backup.sdk.common.utils.BRLog;
import com.oplus.backup.sdk.common.utils.Constants;
import com.oplus.backup.sdk.common.utils.ReflectUtils;
import com.oplus.backup.sdk.common.utils.StaticHandler;
import com.oplus.backup.sdk.host.BRPluginServiceInfo;
import com.oplus.backup.sdk.host.BRPluginSource;
import java.lang.Thread;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* loaded from: classes.dex */
public class BRPluginService extends Service {
    public static final boolean DEBUG_SYNC_THREAD = true;
    public static final String TAG = "BRPluginService";
    private Handler mAsyncHandler;
    private HandlerThread mAsyncThread;
    private BREngineConfig mBREngineConfig;
    private Object mBRPlugin;
    private BRPluginHandlerImpl mBRPluginHandler;
    private IBinder mBinder;
    private ClassLoader mClassLoader;
    private Handler mHandler;
    private InvocationBRPluginHandlerImpl mInvocationBRPluginHandler;
    private BRPluginLoader mLoader;
    private Messenger mMessenger;
    private Class<?> mPluginClass;
    private Messenger mSendMessenger;
    private Handler mSyncHandler;
    private HandlerThread mSyncThread;

    /* loaded from: classes.dex */
    private class BRPluginHandlerImpl implements BRPluginHandler {
        public BRPluginHandlerImpl() {
        }

        @Override // com.oplus.backup.sdk.component.BRPluginHandler
        public void error(Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putBundle(Constants.MessagerConstants.ARGS_KEY, bundle);
            BRPluginService.this.sendMsg(4, bundle2, true);
        }

        @Override // com.oplus.backup.sdk.component.BRPluginHandler
        public void sendMsg(Intent intent, boolean z10) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.MessagerConstants.ARGS_KEY, intent);
            BRPluginService.this.sendMsg(2, bundle, z10);
        }

        @Override // com.oplus.backup.sdk.component.BRPluginHandler
        public void updateProgress(Bundle bundle) {
            Bundle bundle2 = new Bundle();
            bundle2.putBundle(Constants.MessagerConstants.ARGS_KEY, bundle);
            BRPluginService.this.sendMsg(3, bundle2, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InvocationBRPluginHandlerImpl implements InvocationHandler {
        private InvocationBRPluginHandlerImpl() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            if ("updateProgress".equals(method.getName())) {
                BRPluginService.this.mBRPluginHandler.updateProgress((Bundle) objArr[0]);
                return null;
            }
            if ("error".equals(method.getName())) {
                BRPluginService.this.mBRPluginHandler.error((Bundle) objArr[0]);
                return null;
            }
            if (!"sendMsg".equals(method.getName())) {
                return null;
            }
            BRPluginService.this.mBRPluginHandler.sendMsg((Intent) objArr[0], ((Boolean) objArr[1]).booleanValue());
            return null;
        }
    }

    /* loaded from: classes.dex */
    private static class MessengerHandler extends StaticHandler<BRPluginService> {
        private Handler mAsyncHandler;
        private Handler mSyncHandler;

        public MessengerHandler(BRPluginService bRPluginService, Looper looper, Handler handler, Handler handler2) {
            super(bRPluginService, looper);
            this.mAsyncHandler = handler;
            this.mSyncHandler = handler2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.oplus.backup.sdk.common.utils.StaticHandler
        public void handleMessage(Message message, BRPluginService bRPluginService) {
            BRLog.d(BRPluginService.TAG, "MessagerHandler handleMessage " + message.what + ", msg.arg1:" + message.arg1);
            int i10 = message.arg1;
            if (i10 == 0) {
                this.mAsyncHandler.sendMessage(Message.obtain(message));
            } else {
                if (i10 != 1) {
                    return;
                }
                this.mSyncHandler.sendMessage(Message.obtain(message));
            }
        }
    }

    /* loaded from: classes.dex */
    private static class WorkHandler extends StaticHandler<BRPluginService> {
        public WorkHandler(BRPluginService bRPluginService, Looper looper) {
            super(bRPluginService, looper);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.oplus.backup.sdk.common.utils.StaticHandler
        public void handleMessage(Message message, BRPluginService bRPluginService) {
            String str;
            Messenger messenger = message.replyTo;
            if (messenger != null) {
                bRPluginService.mSendMessenger = messenger;
                Object obj = message.obj;
                Bundle bundle = null;
                if (obj != null) {
                    Bundle bundle2 = (Bundle) obj;
                    bundle = bundle2.getBundle(Constants.MessagerConstants.ARGS_KEY);
                    str = bundle2.getString(Constants.MessagerConstants.METHOD_KEY);
                } else {
                    str = null;
                }
                Bundle bundle3 = new Bundle();
                BRLog.d(BRPluginService.TAG, "WorkHandler handleMessage " + message.what + ", msg.arg1:" + message.arg1 + ", " + bundle + ", method:" + str);
                Object obj2 = bRPluginService.mBRPlugin;
                Class cls = bRPluginService.mPluginClass;
                int i10 = message.what;
                if (i10 == 0) {
                    Bundle bundle4 = bundle.getBundle("config");
                    if (bundle4 != null) {
                        bRPluginService.mBREngineConfig = new BREngineConfig(bundle4);
                        BRLog.setLogLevel(bRPluginService.mBREngineConfig.getLogLevel());
                    }
                    bundle3.putBundle(Constants.MessagerConstants.RETURN_KEY, bRPluginService.load(bundle));
                } else if (i10 != 1) {
                    if (i10 != 2) {
                        switch (i10) {
                            case 6:
                                BRLog.d(BRPluginService.TAG, "" + new BREngineConfig(bundle.getBundle("config")));
                                if (obj2 != null) {
                                    bundle3.putBundle(Constants.MessagerConstants.RETURN_KEY, (Bundle) bRPluginService.invoke(obj2, cls, "onPrepare", bundle));
                                    break;
                                }
                                break;
                            case 7:
                                if (obj2 != null) {
                                    bRPluginService.invoke(obj2, cls, "onBackup", bundle);
                                    break;
                                }
                                break;
                            case 8:
                                if (obj2 != null) {
                                    bRPluginService.invoke(obj2, cls, "onRestore", bundle);
                                    break;
                                }
                                break;
                            case 9:
                                if (obj2 != null) {
                                    bRPluginService.invoke(obj2, cls, "onPause", bundle);
                                    break;
                                }
                                break;
                            case 10:
                                if (obj2 != null) {
                                    bRPluginService.invoke(obj2, cls, "onContinue", bundle);
                                    break;
                                }
                                break;
                            case 11:
                                HandlerThread handlerThread = bRPluginService.mSyncThread;
                                if (handlerThread != null) {
                                    BRLog.i(BRPluginService.TAG, "before onCancel, Thread state = " + handlerThread.getState());
                                }
                                if (obj2 != null) {
                                    bRPluginService.invoke(obj2, cls, "onCancel", bundle);
                                }
                                HandlerThread handlerThread2 = bRPluginService.mSyncThread;
                                if (handlerThread2 != null) {
                                    Thread.State state = handlerThread2.getState();
                                    BRLog.i(BRPluginService.TAG, "after onCancel, Thread state = " + state);
                                    if (Thread.State.BLOCKED == state || Thread.State.WAITING == state) {
                                        BRLog.w(BRPluginService.TAG, "getStackTrace:\n" + BRPluginService.getStackMsg(handlerThread2.getStackTrace()));
                                        break;
                                    }
                                }
                                break;
                            case 12:
                                if (obj2 != null) {
                                    bundle3.putBundle(Constants.MessagerConstants.RETURN_KEY, (Bundle) bRPluginService.invoke(obj2, cls, "onDestroy", bundle));
                                    break;
                                }
                                break;
                            case 13:
                                if (obj2 != null) {
                                    bundle3.putBundle(Constants.MessagerConstants.RETURN_KEY, (Bundle) bRPluginService.invoke(obj2, cls, "onPreview", bundle));
                                    break;
                                }
                                break;
                        }
                    } else {
                        bRPluginService.receive(bundle, message.arg1 == 0);
                    }
                } else if (obj2 != null) {
                    bundle3.putBundle(Constants.MessagerConstants.RETURN_KEY, (Bundle) bRPluginService.invoke(obj2, cls, str, bundle));
                }
                if (message.arg1 == 1) {
                    bRPluginService.returnMsg(messenger, message.what, bundle3, false);
                    return;
                }
                return;
            }
            BRLog.w(BRPluginService.TAG, "handleMessage, messenger is null, return");
        }
    }

    private Class<?> findClass(BRPluginConfig bRPluginConfig, int i10) {
        BRPluginLoader bRPluginLoader = new BRPluginLoader();
        this.mLoader = bRPluginLoader;
        ClassLoader load = bRPluginLoader.load(this, bRPluginConfig);
        this.mClassLoader = load;
        if (i10 == 0) {
            return this.mLoader.findClass(bRPluginConfig, load, 0);
        }
        if (i10 == 1) {
            return this.mLoader.findClass(bRPluginConfig, load, 1);
        }
        return null;
    }

    private BRPluginConfig getLocalPluginConfig() {
        ServiceInfo serviceInfo;
        BRPluginConfig[] localBRPlugins = BRPluginSource.getLocalBRPlugins(getApplicationContext(), BRPluginSource.LOCAL_PLUGINS_DIR);
        try {
            serviceInfo = getPackageManager().getServiceInfo(new ComponentName(this, getClass()), 128);
        } catch (PackageManager.NameNotFoundException unused) {
            BRLog.e(TAG, "imposible! no this service? " + this + ", " + this.getClass());
            serviceInfo = null;
        }
        if (serviceInfo != null) {
            BRPluginServiceInfo bRPluginServiceInfo = new BRPluginServiceInfo();
            bRPluginServiceInfo.setClass(serviceInfo.packageName, serviceInfo.name);
            bRPluginServiceInfo.setMetaData(serviceInfo.metaData);
            if (localBRPlugins != null && localBRPlugins.length > 0) {
                for (BRPluginConfig bRPluginConfig : localBRPlugins) {
                    if (bRPluginServiceInfo.getUniqueID().equals(bRPluginConfig.getUniqueID())) {
                        return bRPluginConfig;
                    }
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getStackMsg(StackTraceElement[] stackTraceElementArr) {
        StringBuilder sb2 = new StringBuilder();
        if (stackTraceElementArr != null) {
            for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                sb2.append(stackTraceElement.toString() + "\n");
            }
        }
        return sb2.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object invoke(Object obj, Class<?> cls, String str, Bundle bundle) {
        if (obj == null || cls == null) {
            return null;
        }
        return ReflectUtils.invoke(obj, cls, str, new Class[]{Bundle.class}, new Object[]{bundle});
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0162 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0163  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Bundle load(Bundle bundle) {
        BRPluginConfig[] bRPluginConfigArr;
        BRPluginConfig bRPluginConfig;
        Class<?> cls;
        int length;
        Parcelable[] parcelableArray = bundle.getParcelableArray(Constants.LoadBundle.PLUGINS);
        int i10 = bundle.getInt(Constants.LoadBundle.BR_TYPE);
        if (parcelableArray == null || (length = parcelableArray.length) <= 0) {
            bRPluginConfigArr = null;
        } else {
            bRPluginConfigArr = new BRPluginConfig[length];
            for (int i11 = 0; i11 < length; i11++) {
                bRPluginConfigArr[i11] = new BRPluginConfig((Bundle) parcelableArray[i11]);
            }
        }
        try {
            bRPluginConfig = getLocalPluginConfig();
            try {
                if (bRPluginConfig != null && bRPluginConfig.isPreferLocalClass()) {
                    BRLog.d(TAG, "use local plugin config:" + bRPluginConfig + ", isPreferLocalClass true");
                } else {
                    BRPluginConfig onLoad = onLoad(bRPluginConfigArr, i10);
                    if (onLoad != null) {
                        try {
                            BRLog.d(TAG, "use host's plugin config:" + onLoad);
                            bRPluginConfig = onLoad;
                        } catch (ClassCastException e10) {
                            e = e10;
                            bRPluginConfig = onLoad;
                            BRLog.e(TAG, "load, e =" + e.getMessage());
                            cls = this.mClassLoader.loadClass(BRPluginHandler.class.getName());
                            ReflectUtils.invoke(this.mBRPlugin, this.mPluginClass, "onCreate", new Class[]{Context.class, cls, BREngineConfig.class}, new Object[]{this, Proxy.newProxyInstance(this.mClassLoader, new Class[]{cls}, this.mInvocationBRPluginHandler), this.mBREngineConfig});
                            if (bRPluginConfig != null) {
                            }
                        } catch (IllegalAccessException e11) {
                            e = e11;
                            bRPluginConfig = onLoad;
                            BRLog.e(TAG, "load, e =" + e.getMessage());
                            cls = this.mClassLoader.loadClass(BRPluginHandler.class.getName());
                            ReflectUtils.invoke(this.mBRPlugin, this.mPluginClass, "onCreate", new Class[]{Context.class, cls, BREngineConfig.class}, new Object[]{this, Proxy.newProxyInstance(this.mClassLoader, new Class[]{cls}, this.mInvocationBRPluginHandler), this.mBREngineConfig});
                            if (bRPluginConfig != null) {
                            }
                        } catch (InstantiationException e12) {
                            e = e12;
                            bRPluginConfig = onLoad;
                            BRLog.e(TAG, "load, e =" + e.getMessage());
                            cls = this.mClassLoader.loadClass(BRPluginHandler.class.getName());
                            ReflectUtils.invoke(this.mBRPlugin, this.mPluginClass, "onCreate", new Class[]{Context.class, cls, BREngineConfig.class}, new Object[]{this, Proxy.newProxyInstance(this.mClassLoader, new Class[]{cls}, this.mInvocationBRPluginHandler), this.mBREngineConfig});
                            if (bRPluginConfig != null) {
                            }
                        }
                    } else {
                        if (bRPluginConfig == null) {
                            BRLog.w(TAG, "load localPlugin is null! pls check your .config file");
                            return null;
                        }
                        BRLog.d(TAG, "use local plugin config:" + bRPluginConfig + ", no plugin in host");
                    }
                }
                Class<?> findClass = findClass(bRPluginConfig, i10);
                this.mPluginClass = findClass;
                if (findClass != null) {
                    this.mBRPlugin = findClass.newInstance();
                } else {
                    BRLog.e(TAG, "Didn't find plugin class. " + bRPluginConfig);
                }
            } catch (ClassCastException e13) {
                e = e13;
            } catch (IllegalAccessException e14) {
                e = e14;
            } catch (InstantiationException e15) {
                e = e15;
            }
        } catch (ClassCastException e16) {
            e = e16;
            bRPluginConfig = null;
        } catch (IllegalAccessException e17) {
            e = e17;
            bRPluginConfig = null;
        } catch (InstantiationException e18) {
            e = e18;
            bRPluginConfig = null;
        }
        try {
            cls = this.mClassLoader.loadClass(BRPluginHandler.class.getName());
        } catch (ClassNotFoundException e19) {
            BRLog.e(TAG, "load, e =" + e19.getMessage());
            cls = null;
        }
        ReflectUtils.invoke(this.mBRPlugin, this.mPluginClass, "onCreate", new Class[]{Context.class, cls, BREngineConfig.class}, new Object[]{this, Proxy.newProxyInstance(this.mClassLoader, new Class[]{cls}, this.mInvocationBRPluginHandler), this.mBREngineConfig});
        if (bRPluginConfig != null) {
            return null;
        }
        return bRPluginConfig.getBundle();
    }

    private void onReveiveMsg(Object obj, Class<?> cls, Intent intent, boolean z10) {
        if (obj == null || cls == null) {
            return;
        }
        ReflectUtils.invoke(obj, cls, "onReveiveMsg", new Class[]{Intent.class, Boolean.TYPE}, new Object[]{intent, Boolean.valueOf(z10)});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void receive(Bundle bundle, boolean z10) {
        onReveiveMsg(this.mBRPlugin, this.mPluginClass, (Intent) bundle.get(Constants.MessagerConstants.INTENT_KEY), z10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnMsg(Messenger messenger, int i10, Bundle bundle, boolean z10) {
        try {
            messenger.send(Message.obtain(null, i10, !z10 ? 1 : 0, 0, bundle));
        } catch (RemoteException e10) {
            BRLog.e(TAG, "returnMsg, e =" + e10.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMsg(int i10, Bundle bundle, boolean z10) {
        Message obtain = Message.obtain(null, i10, !z10 ? 1 : 0, 0, bundle);
        Messenger messenger = this.mSendMessenger;
        if (messenger != null) {
            try {
                messenger.send(obtain);
                return;
            } catch (RemoteException e10) {
                BRLog.e(TAG, "sendMsg, e =" + e10.getMessage());
                return;
            }
        }
        BRLog.e(TAG, "mSendMessenger == null");
    }

    protected BRPluginLoader getBRPluginLoader() {
        return this.mLoader;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (this.mBinder == null) {
            BRLog.i(TAG, "onBind ");
            this.mBinder = this.mMessenger.getBinder();
        }
        return this.mBinder;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        BRLog.d(TAG, "onCreate ");
        HandlerThread handlerThread = new HandlerThread("br_plugin_messager_thread");
        this.mSyncThread = handlerThread;
        handlerThread.start();
        HandlerThread handlerThread2 = new HandlerThread("br_plugin_async_thread");
        this.mAsyncThread = handlerThread2;
        handlerThread2.start();
        this.mAsyncHandler = new WorkHandler(this, this.mAsyncThread.getLooper());
        this.mSyncHandler = new WorkHandler(this, this.mSyncThread.getLooper());
        this.mHandler = new MessengerHandler(this, getMainLooper(), this.mAsyncHandler, this.mSyncHandler);
        this.mMessenger = new Messenger(this.mHandler);
        this.mBRPluginHandler = new BRPluginHandlerImpl();
        this.mInvocationBRPluginHandler = new InvocationBRPluginHandlerImpl();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        BRLog.i(TAG, "onDestroy");
        HandlerThread handlerThread = this.mSyncThread;
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
        HandlerThread handlerThread2 = this.mAsyncThread;
        if (handlerThread2 != null) {
            handlerThread2.quitSafely();
        }
    }

    public BRPluginConfig onLoad(BRPluginConfig[] bRPluginConfigArr, int i10) {
        if (bRPluginConfigArr != null && bRPluginConfigArr.length > 0) {
            for (BRPluginConfig bRPluginConfig : bRPluginConfigArr) {
                if (bRPluginConfig != null) {
                    return bRPluginConfig;
                }
            }
        }
        return null;
    }
}
