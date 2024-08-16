package com.oplus.deepthinker.sdk.app.geofence;

import android.content.Context;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.IOplusDeepThinkerManager;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.Event;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventConfig;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.IEventCallback;
import java.util.ArrayList;
import java.util.HashSet;
import kotlin.Metadata;
import za.k;

/* compiled from: GeofencingClient.kt */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010J&\u0010\u0011\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0016\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u0015R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0016"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/GeofencingClient;", "", "context", "Landroid/content/Context;", "manager", "Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "(Landroid/content/Context;Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;)V", "getContext", "()Landroid/content/Context;", "getManager", "()Lcom/oplus/deepthinker/sdk/app/IOplusDeepThinkerManager;", "registerGeofences", "", "geofenceRequest", "Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest;", "callback", "Lcom/oplus/deepthinker/sdk/app/aidl/eventfountain/IEventCallback;", "unregisterGeofences", "ids", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class GeofencingClient {
    private final Context context;
    private final IOplusDeepThinkerManager manager;

    public GeofencingClient(Context context, IOplusDeepThinkerManager iOplusDeepThinkerManager) {
        k.e(context, "context");
        k.e(iOplusDeepThinkerManager, "manager");
        this.context = context;
        this.manager = iOplusDeepThinkerManager;
    }

    public final Context getContext() {
        return this.context;
    }

    public final IOplusDeepThinkerManager getManager() {
        return this.manager;
    }

    public final int registerGeofences(GeofenceRequest geofenceRequest, IEventCallback callback) {
        k.e(geofenceRequest, "geofenceRequest");
        k.e(callback, "callback");
        Bundle bundle = new Bundle();
        bundle.putParcelable(EventType.GeoFenceExtra.BUNDLE_KEY_GEOFENCE_REQUEST, geofenceRequest);
        HashSet hashSet = new HashSet();
        hashSet.add(new Event(EventType.GEOFENCE, bundle));
        return this.manager.registerEventCallback(callback, new EventConfig((HashSet<Event>) hashSet));
    }

    public final int unregisterGeofences(IEventCallback callback) {
        k.e(callback, "callback");
        return this.manager.unregisterEventCallback(callback);
    }

    public final int unregisterGeofences(IEventCallback callback, ArrayList<String> ids) {
        k.e(callback, "callback");
        k.e(ids, "ids");
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EventType.GeoFenceExtra.BUNDLE_KEY_GEOFENCE_IDS, ids);
        HashSet hashSet = new HashSet();
        hashSet.add(new Event(EventType.GEOFENCE, bundle));
        return this.manager.unregisterEventCallbackWithArgs(callback, new EventConfig((HashSet<Event>) hashSet));
    }
}
