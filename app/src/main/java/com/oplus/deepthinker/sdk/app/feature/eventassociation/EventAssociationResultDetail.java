package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import java.util.Map;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: EventAssociationResultDetail.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u0000 &2\u00020\u0001:\u0001&B/\b\u0016\u0012\u0014\u0010\u0002\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bB7\u0012\u0014\u0010\u0002\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\u000bJ\u0017\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\nHÆ\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0007HÆ\u0003JA\u0010 \u001a\u00020\u00002\u0016\b\u0002\u0010\u0002\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010$\u001a\u00020\nHÖ\u0001J\b\u0010%\u001a\u00020\u0004H\u0016R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR(\u0010\u0002\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b¨\u0006'"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationResultDetail;", "", "condition", "", "", "candidate", "weight", "", "(Ljava/util/Map;Ljava/lang/String;F)V", "validDay", "", "(Ljava/util/Map;ILjava/lang/String;F)V", "getCandidate", "()Ljava/lang/String;", "setCandidate", "(Ljava/lang/String;)V", "getCondition", "()Ljava/util/Map;", "setCondition", "(Ljava/util/Map;)V", "getValidDay", "()I", "setValidDay", "(I)V", "getWeight", "()F", "setWeight", "(F)V", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class EventAssociationResultDetail {
    public static final int DEFAULT_VALID_DAY = 30;
    private String candidate;
    private Map<String, String> condition;
    private int validDay;
    private float weight;

    public EventAssociationResultDetail(Map<String, String> map, int i10, String str, float f10) {
        this.condition = map;
        this.validDay = i10;
        this.candidate = str;
        this.weight = f10;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ EventAssociationResultDetail copy$default(EventAssociationResultDetail eventAssociationResultDetail, Map map, int i10, String str, float f10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            map = eventAssociationResultDetail.condition;
        }
        if ((i11 & 2) != 0) {
            i10 = eventAssociationResultDetail.validDay;
        }
        if ((i11 & 4) != 0) {
            str = eventAssociationResultDetail.candidate;
        }
        if ((i11 & 8) != 0) {
            f10 = eventAssociationResultDetail.weight;
        }
        return eventAssociationResultDetail.copy(map, i10, str, f10);
    }

    public final Map<String, String> component1() {
        return this.condition;
    }

    /* renamed from: component2, reason: from getter */
    public final int getValidDay() {
        return this.validDay;
    }

    /* renamed from: component3, reason: from getter */
    public final String getCandidate() {
        return this.candidate;
    }

    /* renamed from: component4, reason: from getter */
    public final float getWeight() {
        return this.weight;
    }

    public final EventAssociationResultDetail copy(Map<String, String> condition, int validDay, String candidate, float weight) {
        return new EventAssociationResultDetail(condition, validDay, candidate, weight);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EventAssociationResultDetail)) {
            return false;
        }
        EventAssociationResultDetail eventAssociationResultDetail = (EventAssociationResultDetail) other;
        return k.a(this.condition, eventAssociationResultDetail.condition) && this.validDay == eventAssociationResultDetail.validDay && k.a(this.candidate, eventAssociationResultDetail.candidate) && k.a(Float.valueOf(this.weight), Float.valueOf(eventAssociationResultDetail.weight));
    }

    public final String getCandidate() {
        return this.candidate;
    }

    public final Map<String, String> getCondition() {
        return this.condition;
    }

    public final int getValidDay() {
        return this.validDay;
    }

    public final float getWeight() {
        return this.weight;
    }

    public int hashCode() {
        Map<String, String> map = this.condition;
        int hashCode = (((map == null ? 0 : map.hashCode()) * 31) + Integer.hashCode(this.validDay)) * 31;
        String str = this.candidate;
        return ((hashCode + (str != null ? str.hashCode() : 0)) * 31) + Float.hashCode(this.weight);
    }

    public final void setCandidate(String str) {
        this.candidate = str;
    }

    public final void setCondition(Map<String, String> map) {
        this.condition = map;
    }

    public final void setValidDay(int i10) {
        this.validDay = i10;
    }

    public final void setWeight(float f10) {
        this.weight = f10;
    }

    public String toString() {
        return "condition: " + this.condition + " validDay : " + this.validDay + ",candidate : " + ((Object) this.candidate) + ",weight : " + this.weight;
    }

    public /* synthetic */ EventAssociationResultDetail(Map map, int i10, String str, float f10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(map, (i11 & 2) != 0 ? 30 : i10, str, f10);
    }

    public EventAssociationResultDetail(Map<String, String> map, String str, float f10) {
        this(map, 30, str, f10);
    }
}
