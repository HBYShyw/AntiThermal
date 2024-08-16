package com.oplus.deepthinker.sdk.app.deepthinkermanager;

import android.os.Bundle;
import android.os.Handler;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventQueryListener;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.TriggerEvent;
import com.oplus.deepthinker.sdk.app.userprofile.labels.AppTypePreferenceLabel;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface IDeviceDomainManager {
    default Map<String, Integer> getAllAppImportance(Map<String, Integer> map) {
        return null;
    }

    default Map<String, Integer> getAllAppImportanceLocked(Map<String, Integer> map) {
        return null;
    }

    default int getAppType(String str) {
        return -1;
    }

    default Map getAppTypeMap(List<String> list) {
        return null;
    }

    default List<Event> getAvailableEvent() {
        return null;
    }

    default AppTypePreferenceLabel getRecentAppTypePreference() {
        return null;
    }

    default boolean isAvailableEvent(Event event) {
        return false;
    }

    default void queryEvent(Event event, IEventQueryListener iEventQueryListener) {
    }

    default void queryEvents(EventConfig eventConfig, IEventQueryListener iEventQueryListener) {
    }

    default int registerEventCallback(IEventCallback iEventCallback, EventConfig eventConfig) {
        return 0;
    }

    default void triggerHookEvent(int i10, int i11, String str, Bundle bundle) {
    }

    default void triggerHookEvent(TriggerEvent triggerEvent) {
    }

    default void triggerHookEventAsync(Handler handler, int i10, int i11, String str, Bundle bundle) {
    }

    default int unregisterEventCallback(IEventCallback iEventCallback) {
        return 0;
    }

    default int unregisterEventCallbackWithArgs(IEventCallback iEventCallback, EventConfig eventConfig) {
        return 0;
    }
}
