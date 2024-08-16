package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import java.util.List;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: EventAssociationConditionConfig.kt */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u0015\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005B)\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0011\u0010\u0016\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0018\u001a\u00020\tHÆ\u0003J/\u0010\u0019\u001a\u00020\u00002\u0010\b\u0002\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0007HÖ\u0001J\b\u0010\u001e\u001a\u00020\u0004H\u0016R\"\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u0005R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015¨\u0006 "}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationConditionConfig;", "", "conditionTypes", "", "", "(Ljava/util/List;)V", "validDay", "", "minWeight", "", "(Ljava/util/List;IF)V", "getConditionTypes", "()Ljava/util/List;", "setConditionTypes", "getMinWeight", "()F", "setMinWeight", "(F)V", "getValidDay", "()I", "setValidDay", "(I)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class EventAssociationConditionConfig {
    public static final int DEFAULT_VALID_DAY = 30;
    private List<String> conditionTypes;
    private float minWeight;
    private int validDay;

    public EventAssociationConditionConfig(List<String> list, int i10, float f10) {
        this.conditionTypes = list;
        this.validDay = i10;
        this.minWeight = f10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ EventAssociationConditionConfig copy$default(EventAssociationConditionConfig eventAssociationConditionConfig, List list, int i10, float f10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            list = eventAssociationConditionConfig.conditionTypes;
        }
        if ((i11 & 2) != 0) {
            i10 = eventAssociationConditionConfig.validDay;
        }
        if ((i11 & 4) != 0) {
            f10 = eventAssociationConditionConfig.minWeight;
        }
        return eventAssociationConditionConfig.copy(list, i10, f10);
    }

    public final List<String> component1() {
        return this.conditionTypes;
    }

    /* renamed from: component2, reason: from getter */
    public final int getValidDay() {
        return this.validDay;
    }

    /* renamed from: component3, reason: from getter */
    public final float getMinWeight() {
        return this.minWeight;
    }

    public final EventAssociationConditionConfig copy(List<String> conditionTypes, int validDay, float minWeight) {
        return new EventAssociationConditionConfig(conditionTypes, validDay, minWeight);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EventAssociationConditionConfig)) {
            return false;
        }
        EventAssociationConditionConfig eventAssociationConditionConfig = (EventAssociationConditionConfig) other;
        return k.a(this.conditionTypes, eventAssociationConditionConfig.conditionTypes) && this.validDay == eventAssociationConditionConfig.validDay && k.a(Float.valueOf(this.minWeight), Float.valueOf(eventAssociationConditionConfig.minWeight));
    }

    public final List<String> getConditionTypes() {
        return this.conditionTypes;
    }

    public final float getMinWeight() {
        return this.minWeight;
    }

    public final int getValidDay() {
        return this.validDay;
    }

    public int hashCode() {
        List<String> list = this.conditionTypes;
        return ((((list == null ? 0 : list.hashCode()) * 31) + Integer.hashCode(this.validDay)) * 31) + Float.hashCode(this.minWeight);
    }

    public final void setConditionTypes(List<String> list) {
        this.conditionTypes = list;
    }

    public final void setMinWeight(float f10) {
        this.minWeight = f10;
    }

    public final void setValidDay(int i10) {
        this.validDay = i10;
    }

    public String toString() {
        return "conditionTypes: " + this.conditionTypes + ", weightUnderConditions: " + this.minWeight + " validDay: " + this.validDay;
    }

    public /* synthetic */ EventAssociationConditionConfig(List list, int i10, float f10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, (i11 & 2) != 0 ? 30 : i10, (i11 & 4) != 0 ? 0.0f : f10);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public EventAssociationConditionConfig(List<String> list) {
        this(list, 30, 0.0f);
        k.e(list, "conditionTypes");
    }
}
