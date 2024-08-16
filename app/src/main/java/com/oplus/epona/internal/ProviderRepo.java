package com.oplus.epona.internal;

import android.text.TextUtils;
import android.util.Log;
import com.oplus.epona.DynamicProvider;
import com.oplus.epona.Epona;
import com.oplus.epona.Repo;
import com.oplus.epona.ipc.local.RemoteTransfer;
import com.oplus.epona.provider.ProviderInfo;
import com.oplus.epona.provider.ProviderMethodInfo;
import com.oplus.epona.route.RouteInfo;
import com.oplus.epona.utils.Logger;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class ProviderRepo implements Repo {
    private static final String TAG = "ProviderRepo";
    private final ConcurrentHashMap<String, DynamicProvider> mDynamicProviders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ProviderInfo> mProviders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RouteInfo> mRoutes = new ConcurrentHashMap<>();

    private Map<String, ProviderMethodInfo> getProviderMethodInfo(ProviderInfo providerInfo) {
        if (providerInfo == null) {
            return null;
        }
        try {
            Field declaredField = providerInfo.getClass().getDeclaredField("mMethods");
            declaredField.setAccessible(true);
            return (Map) declaredField.get(providerInfo);
        } catch (Exception e10) {
            Log.e(TAG, e10.toString());
            return null;
        }
    }

    private boolean isValid(DynamicProvider dynamicProvider) {
        return (dynamicProvider == null || TextUtils.isEmpty(dynamicProvider.getName())) ? false : true;
    }

    private void printDynamicProviderInfo(PrintWriter printWriter) {
        printWriter.println("dynamic:");
        for (Map.Entry<String, DynamicProvider> entry : this.mDynamicProviders.entrySet()) {
            if (entry.getValue().getName() != null) {
                printWriter.println(entry.getValue().getName());
            }
        }
        printWriter.println("");
    }

    private void printStaticProviderInfo(PrintWriter printWriter) {
        printWriter.println("static:");
        Iterator<Map.Entry<String, ProviderInfo>> it = this.mProviders.entrySet().iterator();
        while (it.hasNext()) {
            ProviderInfo value = it.next().getValue();
            String name = value.getName();
            if (name != null) {
                printWriter.println(name + " : ");
            }
            Map<String, ProviderMethodInfo> providerMethodInfo = getProviderMethodInfo(value);
            if (providerMethodInfo != null) {
                for (Map.Entry<String, ProviderMethodInfo> entry : providerMethodInfo.entrySet()) {
                    if (entry != null) {
                        printWriter.println("    -> " + entry.getValue().getMethodName());
                    }
                }
            }
            printWriter.println("");
        }
    }

    private void registerProviderToRemote(String str, String str2) {
        Epona.getTransferController().register(str, str2, RemoteTransfer.getInstance());
    }

    @Override // com.oplus.epona.Repo
    public void dump(PrintWriter printWriter) {
        printWriter.println("---------start dump epona register info---------");
        printDynamicProviderInfo(printWriter);
        printStaticProviderInfo(printWriter);
        printWriter.println("-------------------- end -----------------------");
    }

    @Override // com.oplus.epona.Repo
    public DynamicProvider findProvider(String str) {
        return this.mDynamicProviders.get(str);
    }

    @Override // com.oplus.epona.Repo
    public ProviderInfo findProviderProviderInfo(String str) {
        return this.mProviders.get(str);
    }

    @Override // com.oplus.epona.Repo
    public RouteInfo findRouteInfo(String str) {
        return this.mRoutes.get(str);
    }

    @Override // com.oplus.epona.Repo
    public void registerProvider(DynamicProvider dynamicProvider) {
        if (isValid(dynamicProvider)) {
            Logger.d(TAG, "register dynamic provider %s needIPC = %s", dynamicProvider.getName(), Boolean.valueOf(dynamicProvider.needIPC()));
            this.mDynamicProviders.put(dynamicProvider.getName(), dynamicProvider);
            if (dynamicProvider.needIPC()) {
                registerProviderToRemote(dynamicProvider.getName(), dynamicProvider.getClass().getCanonicalName());
            }
        }
    }

    @Override // com.oplus.epona.Repo
    public void registerProviderInfo(ProviderInfo providerInfo) {
        if (isValid(providerInfo)) {
            Logger.d(TAG, "register static provider %s needIPC = %s", providerInfo.getName(), Boolean.valueOf(providerInfo.needIPC()));
            this.mProviders.put(providerInfo.getName(), providerInfo);
            if (providerInfo.needIPC()) {
                registerProviderToRemote(providerInfo.getName(), providerInfo.getClassName());
            }
        }
    }

    @Override // com.oplus.epona.Repo
    public void registerRouteInfo(RouteInfo routeInfo) {
        if (isValid(routeInfo)) {
            Logger.d(TAG, "register static route %s", routeInfo.getPath());
            this.mRoutes.put(routeInfo.getPath(), routeInfo);
        }
    }

    @Override // com.oplus.epona.Repo
    public void unRegisterProvider(DynamicProvider dynamicProvider) {
        if (isValid(dynamicProvider)) {
            Logger.d(TAG, "unregister dynamic provider %s", dynamicProvider.getName());
            this.mDynamicProviders.remove(dynamicProvider.getName());
        }
    }

    @Override // com.oplus.epona.Repo
    public void unRegisterProviderInfo(ProviderInfo providerInfo) {
        if (isValid(providerInfo)) {
            Logger.d(TAG, "unregister static provider %s", providerInfo.getName());
            this.mProviders.remove(providerInfo.getName());
        }
    }

    @Override // com.oplus.epona.Repo
    public void unRegisterRouteInfo(RouteInfo routeInfo) {
        if (isValid(routeInfo)) {
            Logger.d(TAG, "unregister static route %s", routeInfo.getPath());
            this.mRoutes.remove(routeInfo.getPath());
        }
    }

    private boolean isValid(ProviderInfo providerInfo) {
        return (providerInfo == null || TextUtils.isEmpty(providerInfo.getName())) ? false : true;
    }

    private boolean isValid(RouteInfo routeInfo) {
        return (routeInfo == null || TextUtils.isEmpty(routeInfo.getPath())) ? false : true;
    }
}
