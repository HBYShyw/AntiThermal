package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: EventAssociationConfig.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005¢\u0006\u0002\u0010\tJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\u0011\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005HÆ\u0003J\u0011\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005HÆ\u0003J7\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00052\u0010\b\u0002\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001J\b\u0010\u001c\u001a\u00020\bH\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\"\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\"\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000f\"\u0004\b\u0013\u0010\u0011¨\u0006\u001d"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationConfig;", "", EventType.EventAssociationExtra.BUSINESS_ID, "", "conditionConfigs", "", "Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationConditionConfig;", "targetList", "", "(ILjava/util/List;Ljava/util/List;)V", "getBusinessId", "()I", "setBusinessId", "(I)V", "getConditionConfigs", "()Ljava/util/List;", "setConditionConfigs", "(Ljava/util/List;)V", "getTargetList", "setTargetList", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class EventAssociationConfig {
    private int businessId;
    private List<EventAssociationConditionConfig> conditionConfigs;
    private List<String> targetList;

    public EventAssociationConfig(int i10, List<EventAssociationConditionConfig> list, List<String> list2) {
        this.businessId = i10;
        this.conditionConfigs = list;
        this.targetList = list2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ EventAssociationConfig copy$default(EventAssociationConfig eventAssociationConfig, int i10, List list, List list2, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = eventAssociationConfig.businessId;
        }
        if ((i11 & 2) != 0) {
            list = eventAssociationConfig.conditionConfigs;
        }
        if ((i11 & 4) != 0) {
            list2 = eventAssociationConfig.targetList;
        }
        return eventAssociationConfig.copy(i10, list, list2);
    }

    /* renamed from: component1, reason: from getter */
    public final int getBusinessId() {
        return this.businessId;
    }

    public final List<EventAssociationConditionConfig> component2() {
        return this.conditionConfigs;
    }

    public final List<String> component3() {
        return this.targetList;
    }

    public final EventAssociationConfig copy(int businessId, List<EventAssociationConditionConfig> conditionConfigs, List<String> targetList) {
        return new EventAssociationConfig(businessId, conditionConfigs, targetList);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EventAssociationConfig)) {
            return false;
        }
        EventAssociationConfig eventAssociationConfig = (EventAssociationConfig) other;
        return this.businessId == eventAssociationConfig.businessId && k.a(this.conditionConfigs, eventAssociationConfig.conditionConfigs) && k.a(this.targetList, eventAssociationConfig.targetList);
    }

    public final int getBusinessId() {
        return this.businessId;
    }

    public final List<EventAssociationConditionConfig> getConditionConfigs() {
        return this.conditionConfigs;
    }

    public final List<String> getTargetList() {
        return this.targetList;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.businessId) * 31;
        List<EventAssociationConditionConfig> list = this.conditionConfigs;
        int hashCode2 = (hashCode + (list == null ? 0 : list.hashCode())) * 31;
        List<String> list2 = this.targetList;
        return hashCode2 + (list2 != null ? list2.hashCode() : 0);
    }

    public final void setBusinessId(int i10) {
        this.businessId = i10;
    }

    public final void setConditionConfigs(List<EventAssociationConditionConfig> list) {
        this.conditionConfigs = list;
    }

    public final void setTargetList(List<String> list) {
        this.targetList = list;
    }

    public String toString() {
        Unit unit;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("business:" + this.businessId + '\n');
        sb2.append("conditionConfig:");
        List<EventAssociationConditionConfig> list = this.conditionConfigs;
        if (list == null) {
            unit = null;
        } else {
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                sb2.append(((EventAssociationConditionConfig) it.next()) + " \n");
            }
            unit = Unit.f15173a;
        }
        if (unit == null) {
            sb2.append("empty \n");
        }
        sb2.append(k.l("targetList : ", this.targetList));
        String sb3 = sb2.toString();
        k.d(sb3, "sb.toString()");
        return sb3;
    }
}
