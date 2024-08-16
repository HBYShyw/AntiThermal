package com.oplus.deepthinker.sdk.app.userprofile.labels;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.r;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: LeaveHomeLabel.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005J\u000f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u0019\u0010\n\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0001J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R \u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\u0005¨\u0006\u0012"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/userprofile/labels/LeaveHomeLabel;", "", "leaveHomeClusters", "", "Lcom/oplus/deepthinker/sdk/app/userprofile/labels/LeaveHomeCluster;", "(Ljava/util/List;)V", "getLeaveHomeClusters", "()Ljava/util/List;", "setLeaveHomeClusters", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class LeaveHomeLabel {
    private List<LeaveHomeCluster> leaveHomeClusters;

    public LeaveHomeLabel() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    public LeaveHomeLabel(List<LeaveHomeCluster> list) {
        k.e(list, "leaveHomeClusters");
        this.leaveHomeClusters = list;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ LeaveHomeLabel copy$default(LeaveHomeLabel leaveHomeLabel, List list, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            list = leaveHomeLabel.leaveHomeClusters;
        }
        return leaveHomeLabel.copy(list);
    }

    public final List<LeaveHomeCluster> component1() {
        return this.leaveHomeClusters;
    }

    public final LeaveHomeLabel copy(List<LeaveHomeCluster> leaveHomeClusters) {
        k.e(leaveHomeClusters, "leaveHomeClusters");
        return new LeaveHomeLabel(leaveHomeClusters);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof LeaveHomeLabel) && k.a(this.leaveHomeClusters, ((LeaveHomeLabel) other).leaveHomeClusters);
    }

    public final List<LeaveHomeCluster> getLeaveHomeClusters() {
        return this.leaveHomeClusters;
    }

    public int hashCode() {
        return this.leaveHomeClusters.hashCode();
    }

    public final void setLeaveHomeClusters(List<LeaveHomeCluster> list) {
        k.e(list, "<set-?>");
        this.leaveHomeClusters = list;
    }

    public String toString() {
        return "LeaveHomeLabel(leaveHomeClusters=" + this.leaveHomeClusters + ')';
    }

    public /* synthetic */ LeaveHomeLabel(List list, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? r.j() : list);
    }
}
