package com.oplus.epona;

import com.oplus.epona.provider.ProviderInfo;
import com.oplus.epona.route.RouteInfo;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public interface Repo {
    void dump(PrintWriter printWriter);

    DynamicProvider findProvider(String str);

    ProviderInfo findProviderProviderInfo(String str);

    RouteInfo findRouteInfo(String str);

    void registerProvider(DynamicProvider dynamicProvider);

    void registerProviderInfo(ProviderInfo providerInfo);

    void registerRouteInfo(RouteInfo routeInfo);

    void unRegisterProvider(DynamicProvider dynamicProvider);

    void unRegisterProviderInfo(ProviderInfo providerInfo);

    void unRegisterRouteInfo(RouteInfo routeInfo);
}
