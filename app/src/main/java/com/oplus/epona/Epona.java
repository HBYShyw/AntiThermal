package com.oplus.epona;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.oplus.epona.interceptor.IPCInterceptor;
import com.oplus.epona.internal.ActivityStackManager;
import com.oplus.epona.internal.ProviderRepo;
import com.oplus.epona.internal.RealCall;
import com.oplus.epona.ipc.local.DefaultTransferController;
import com.oplus.epona.ipc.local.RemoteTransferController;
import com.oplus.epona.ipc.remote.Dispatcher;
import com.oplus.epona.provider.ProviderInfo;
import com.oplus.epona.route.RouteInfo;
import com.oplus.epona.utils.Logger;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class Epona {
    private static final String TAG = "Epona";
    private static Epona sInstance;
    private Application mApp;
    private Context mContext;
    public static final RemoteTransferController DEFAULT_CONTROLLER = new DefaultTransferController();
    private static final Object mLock = new Object();
    private static AtomicBoolean sInitialized = new AtomicBoolean(false);
    private Map<String, Dumper> mDumpMap = new ConcurrentHashMap();
    private final List<Interceptor> mInterceptors = new ArrayList();
    private RemoteTransferController mController = DEFAULT_CONTROLLER;
    private Interceptor mIPCInterceptor = new IPCInterceptor();
    private Repo mRepo = new ProviderRepo();
    private Route mRoute = new Route();
    private ActivityStackManager mActivityStackManager = new ActivityStackManager();

    private Epona() {
    }

    public static boolean addDumper(Dumper dumper) {
        Objects.requireNonNull(dumper);
        getDumper().put(dumper.key(), dumper);
        return true;
    }

    public static boolean addInterceptor(Interceptor interceptor) {
        Objects.requireNonNull(interceptor, "interceptor cannot be null");
        List<Interceptor> list = getInstance().mInterceptors;
        if (list.contains(interceptor)) {
            Logger.e(TAG, "interceptor has been add twice", new Object[0]);
            return false;
        }
        return list.add(interceptor);
    }

    private void attach(Context context) {
        this.mContext = context;
        if (context instanceof Application) {
            this.mApp = (Application) context;
        } else {
            this.mApp = (Application) context.getApplicationContext();
        }
        this.mActivityStackManager.attach(this.mApp);
    }

    private static void autoRegister() {
    }

    public static void dump(PrintWriter printWriter) {
        getInstance().mRepo.dump(printWriter);
    }

    public static DynamicProvider findComponent(String str) {
        return getInstance().mRepo.findProvider(str);
    }

    public static ProviderInfo findProviderComponent(String str) {
        return getInstance().mRepo.findProviderProviderInfo(str);
    }

    public static RouteInfo findRoute(String str) {
        return getInstance().mRepo.findRouteInfo(str);
    }

    public static Application getApplication() {
        return getInstance().mApp;
    }

    public static Context getContext() {
        return getInstance().mContext;
    }

    public static Activity getCurrentActivity() {
        return getInstance().mActivityStackManager.getCurrentActivity();
    }

    public static Map<String, Dumper> getDumper() {
        return getInstance().mDumpMap;
    }

    public static Interceptor getIPCInterceptor() {
        return getInstance().mIPCInterceptor;
    }

    private static Epona getInstance() {
        synchronized (mLock) {
            if (sInstance == null) {
                sInstance = new Epona();
            }
        }
        return sInstance;
    }

    public static List<Interceptor> getInterceptors() {
        return getInstance().mInterceptors;
    }

    public static RemoteTransferController getTransferController() {
        return getInstance().mController;
    }

    public static void init(Context context) {
        if (sInitialized.getAndSet(true)) {
            return;
        }
        getInstance().attach(context);
        addDumper(Dispatcher.getInstance());
        Logger.init(context);
        autoRegister();
    }

    public static RealCall newCall(Request request) {
        return getInstance().mRoute.newCall(request);
    }

    public static void register(DynamicProvider dynamicProvider) {
        getInstance().mRepo.registerProvider(dynamicProvider);
    }

    public static void registerProvider(ProviderInfo providerInfo) {
        getInstance().mRepo.registerProviderInfo(providerInfo);
    }

    public static void registerRoute(RouteInfo routeInfo) {
        getInstance().mRepo.registerRouteInfo(routeInfo);
    }

    public static void setIPCInterceptor(Interceptor interceptor) {
        getInstance().mIPCInterceptor = interceptor;
    }

    public static void setTransferController(RemoteTransferController remoteTransferController) {
        getInstance().mController = remoteTransferController;
    }

    public static void unRegister(DynamicProvider dynamicProvider) {
        getInstance().mRepo.unRegisterProvider(dynamicProvider);
    }

    public static void unRegisterProvider(ProviderInfo providerInfo) {
        getInstance().mRepo.unRegisterProviderInfo(providerInfo);
    }

    public static void unRegisterRoute(RouteInfo routeInfo) {
        getInstance().mRepo.unRegisterRouteInfo(routeInfo);
    }
}
