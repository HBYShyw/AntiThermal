package com.oplus.deepthinker.sdk.app.feature.eventassociation;

import com.oplus.deepthinker.sdk.app.SDKLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: EventAssociationResult.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0017\u0012\u000e\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u0006¢\u0006\u0004\b\u0011\u0010\u0010B\t\b\u0016¢\u0006\u0004\b\u0011\u0010\u0012J\u0010\u0010\u0005\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002J\u0016\u0010\b\u001a\u00020\u00042\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u0006J\b\u0010\n\u001a\u00020\tH\u0016R*\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u00068\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010¨\u0006\u0014"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationResult;", "", "Lcom/oplus/deepthinker/sdk/app/feature/eventassociation/EventAssociationResultDetail;", "result", "Lma/f0;", "appendResult", "", "resultList", "appendResultList", "", "toString", "results", "Ljava/util/List;", "getResults", "()Ljava/util/List;", "setResults", "(Ljava/util/List;)V", "<init>", "()V", "Companion", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class EventAssociationResult {
    public static final String TAG = "EventAssociationResult";
    private List<EventAssociationResultDetail> results;

    public EventAssociationResult(List<EventAssociationResultDetail> list) {
        this.results = list;
    }

    public final void appendResult(EventAssociationResultDetail eventAssociationResultDetail) {
        Unit unit;
        if (eventAssociationResultDetail == null) {
            unit = null;
        } else {
            List<EventAssociationResultDetail> results = getResults();
            if (results != null) {
                results.add(eventAssociationResultDetail);
            }
            SDKLog.v(TAG, "appendResult result : " + eventAssociationResultDetail + " successfully");
            unit = Unit.f15173a;
        }
        if (unit == null) {
            SDKLog.w(TAG, "appendResult result is empty");
        }
    }

    public final void appendResultList(List<EventAssociationResultDetail> list) {
        Unit unit;
        if (list == null) {
            unit = null;
        } else {
            List<EventAssociationResultDetail> results = getResults();
            if (results != null) {
                results.addAll(list);
            }
            SDKLog.v(TAG, "appendResult result list : " + list + " successfully");
            unit = Unit.f15173a;
        }
        if (unit == null) {
            SDKLog.w(TAG, "appendResult result list is null");
        }
    }

    public final List<EventAssociationResultDetail> getResults() {
        return this.results;
    }

    public final void setResults(List<EventAssociationResultDetail> list) {
        this.results = list;
    }

    public String toString() {
        Unit unit;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("resultDetail:\n");
        List<EventAssociationResultDetail> list = this.results;
        if (list == null) {
            unit = null;
        } else {
            Iterator<T> it = list.iterator();
            while (it.hasNext()) {
                sb2.append(' ' + ((EventAssociationResultDetail) it.next()) + " \n");
            }
            unit = Unit.f15173a;
        }
        if (unit == null) {
            sb2.append("is empty \n");
        }
        String sb3 = sb2.toString();
        k.d(sb3, "sb.toString()");
        return sb3;
    }

    public EventAssociationResult() {
        this(new ArrayList());
    }
}
