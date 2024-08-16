package com.android.server.location.provider;

import android.location.provider.ProviderRequest;
import com.android.server.location.provider.AbstractLocationProvider;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IAbstractLocationProviderWrapper {
    default void flush(Runnable runnable) {
    }

    default boolean isStarted() {
        return false;
    }

    default AbstractLocationProvider.State setListener(AbstractLocationProvider.Listener listener) {
        return null;
    }

    default void setRequest(ProviderRequest providerRequest) {
    }

    default void start() {
    }

    default void stop() {
    }
}
