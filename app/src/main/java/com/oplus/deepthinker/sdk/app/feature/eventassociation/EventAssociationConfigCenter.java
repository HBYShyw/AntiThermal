package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import com.oplus.deepthinker.sdk.app.SDKLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: EventAssociationConfigCenter.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\b\n\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0017\u0012\u000e\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\b¢\u0006\u0004\b\u000f\u0010\u000eB\t\b\u0016¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016R*\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\b8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u0012"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationConfigCenter;", "", "Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationConfig;", "config", "Lma/f0;", "appendConfig", "", "toString", "", "configs", "Ljava/util/List;", "getConfigs", "()Ljava/util/List;", "setConfigs", "(Ljava/util/List;)V", "<init>", "()V", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class EventAssociationConfigCenter {
    public static final String TAG = "EventAssociationConfigs";
    private List<EventAssociationConfig> configs;

    public EventAssociationConfigCenter(List<EventAssociationConfig> list) {
        this.configs = list;
    }

    public final void appendConfig(EventAssociationConfig eventAssociationConfig) {
        Unit unit;
        if (eventAssociationConfig == null) {
            unit = null;
        } else {
            List<EventAssociationConfig> configs = getConfigs();
            if (configs != null) {
                configs.add(eventAssociationConfig);
            }
            SDKLog.v(TAG, "appendConfig config : " + eventAssociationConfig + " successfully");
            unit = Unit.f15173a;
        }
        if (unit == null) {
            SDKLog.w(TAG, "appendConfig config is empty");
        }
    }

    public final List<EventAssociationConfig> getConfigs() {
        return this.configs;
    }

    public final void setConfigs(List<EventAssociationConfig> list) {
        this.configs = list;
    }

    public String toString() {
        Unit unit;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("config:");
        List<EventAssociationConfig> list = this.configs;
        if (list == null) {
            unit = null;
        } else {
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                sb2.append(' ' + ((EventAssociationConfig) it.next()) + " \n");
            }
            unit = Unit.f15173a;
        }
        if (unit == null) {
            sb2.append("empty \n");
        }
        String sb3 = sb2.toString();
        k.d(sb3, "sb.toString()");
        return sb3;
    }

    public EventAssociationConfigCenter() {
        this(new ArrayList());
    }
}
