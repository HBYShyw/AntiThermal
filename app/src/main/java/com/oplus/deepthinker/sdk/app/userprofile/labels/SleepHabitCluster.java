package com.oplus.deepthinker.sdk.app.userprofile.labels;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.m0;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SleepHabitLabel.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0010\u0006\n\u0002\b$\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001Bo\u0012\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0004\u0012\b\b\u0002\u0010\t\u001a\u00020\u0004\u0012\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0004¢\u0006\u0002\u0010\fJ\u0015\u0010!\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003HÆ\u0003J\u0015\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0005HÆ\u0003J\t\u0010$\u001a\u00020\u0004HÆ\u0003J\t\u0010%\u001a\u00020\u0004HÆ\u0003J\u0015\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0004HÆ\u0003Js\u0010(\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u00032\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00042\b\b\u0002\u0010\t\u001a\u00020\u00042\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0004HÆ\u0001J\u0013\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010,\u001a\u00020\u0004HÖ\u0001J\t\u0010-\u001a\u00020.HÖ\u0001R&\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\b\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0012\"\u0004\b\u0016\u0010\u0014R\u001a\u0010\u000b\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0012\"\u0004\b\u0018\u0010\u0014R\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR&\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u000e\"\u0004\b\u001e\u0010\u0010R&\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u000e\"\u0004\b \u0010\u0010¨\u0006/"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/userprofile/labels/SleepHabitCluster;", "", "sleepTimePercentiles", "", "", "", "wakeTimePercentiles", "maxDistance", "clusterId", "clusterNum", "clusterDayDistribution", "clusterPercent", "(Ljava/util/Map;Ljava/util/Map;DIILjava/util/Map;I)V", "getClusterDayDistribution", "()Ljava/util/Map;", "setClusterDayDistribution", "(Ljava/util/Map;)V", "getClusterId", "()I", "setClusterId", "(I)V", "getClusterNum", "setClusterNum", "getClusterPercent", "setClusterPercent", "getMaxDistance", "()D", "setMaxDistance", "(D)V", "getSleepTimePercentiles", "setSleepTimePercentiles", "getWakeTimePercentiles", "setWakeTimePercentiles", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class SleepHabitCluster {
    private Map<Integer, Integer> clusterDayDistribution;
    private int clusterId;
    private int clusterNum;
    private int clusterPercent;
    private double maxDistance;
    private Map<Integer, Double> sleepTimePercentiles;
    private Map<Integer, Double> wakeTimePercentiles;

    public SleepHabitCluster() {
        this(null, null, UserProfileInfo.Constant.NA_LAT_LON, 0, 0, null, 0, 127, null);
    }

    public SleepHabitCluster(Map<Integer, Double> map, Map<Integer, Double> map2, double d10, int i10, int i11, Map<Integer, Integer> map3, int i12) {
        k.e(map, "sleepTimePercentiles");
        k.e(map2, "wakeTimePercentiles");
        k.e(map3, "clusterDayDistribution");
        this.sleepTimePercentiles = map;
        this.wakeTimePercentiles = map2;
        this.maxDistance = d10;
        this.clusterId = i10;
        this.clusterNum = i11;
        this.clusterDayDistribution = map3;
        this.clusterPercent = i12;
    }

    public final Map<Integer, Double> component1() {
        return this.sleepTimePercentiles;
    }

    public final Map<Integer, Double> component2() {
        return this.wakeTimePercentiles;
    }

    /* renamed from: component3, reason: from getter */
    public final double getMaxDistance() {
        return this.maxDistance;
    }

    /* renamed from: component4, reason: from getter */
    public final int getClusterId() {
        return this.clusterId;
    }

    /* renamed from: component5, reason: from getter */
    public final int getClusterNum() {
        return this.clusterNum;
    }

    public final Map<Integer, Integer> component6() {
        return this.clusterDayDistribution;
    }

    /* renamed from: component7, reason: from getter */
    public final int getClusterPercent() {
        return this.clusterPercent;
    }

    public final SleepHabitCluster copy(Map<Integer, Double> sleepTimePercentiles, Map<Integer, Double> wakeTimePercentiles, double maxDistance, int clusterId, int clusterNum, Map<Integer, Integer> clusterDayDistribution, int clusterPercent) {
        k.e(sleepTimePercentiles, "sleepTimePercentiles");
        k.e(wakeTimePercentiles, "wakeTimePercentiles");
        k.e(clusterDayDistribution, "clusterDayDistribution");
        return new SleepHabitCluster(sleepTimePercentiles, wakeTimePercentiles, maxDistance, clusterId, clusterNum, clusterDayDistribution, clusterPercent);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SleepHabitCluster)) {
            return false;
        }
        SleepHabitCluster sleepHabitCluster = (SleepHabitCluster) other;
        return k.a(this.sleepTimePercentiles, sleepHabitCluster.sleepTimePercentiles) && k.a(this.wakeTimePercentiles, sleepHabitCluster.wakeTimePercentiles) && k.a(Double.valueOf(this.maxDistance), Double.valueOf(sleepHabitCluster.maxDistance)) && this.clusterId == sleepHabitCluster.clusterId && this.clusterNum == sleepHabitCluster.clusterNum && k.a(this.clusterDayDistribution, sleepHabitCluster.clusterDayDistribution) && this.clusterPercent == sleepHabitCluster.clusterPercent;
    }

    public final Map<Integer, Integer> getClusterDayDistribution() {
        return this.clusterDayDistribution;
    }

    public final int getClusterId() {
        return this.clusterId;
    }

    public final int getClusterNum() {
        return this.clusterNum;
    }

    public final int getClusterPercent() {
        return this.clusterPercent;
    }

    public final double getMaxDistance() {
        return this.maxDistance;
    }

    public final Map<Integer, Double> getSleepTimePercentiles() {
        return this.sleepTimePercentiles;
    }

    public final Map<Integer, Double> getWakeTimePercentiles() {
        return this.wakeTimePercentiles;
    }

    public int hashCode() {
        return (((((((((((this.sleepTimePercentiles.hashCode() * 31) + this.wakeTimePercentiles.hashCode()) * 31) + Double.hashCode(this.maxDistance)) * 31) + Integer.hashCode(this.clusterId)) * 31) + Integer.hashCode(this.clusterNum)) * 31) + this.clusterDayDistribution.hashCode()) * 31) + Integer.hashCode(this.clusterPercent);
    }

    public final void setClusterDayDistribution(Map<Integer, Integer> map) {
        k.e(map, "<set-?>");
        this.clusterDayDistribution = map;
    }

    public final void setClusterId(int i10) {
        this.clusterId = i10;
    }

    public final void setClusterNum(int i10) {
        this.clusterNum = i10;
    }

    public final void setClusterPercent(int i10) {
        this.clusterPercent = i10;
    }

    public final void setMaxDistance(double d10) {
        this.maxDistance = d10;
    }

    public final void setSleepTimePercentiles(Map<Integer, Double> map) {
        k.e(map, "<set-?>");
        this.sleepTimePercentiles = map;
    }

    public final void setWakeTimePercentiles(Map<Integer, Double> map) {
        k.e(map, "<set-?>");
        this.wakeTimePercentiles = map;
    }

    public String toString() {
        return "SleepHabitCluster(sleepTimePercentiles=" + this.sleepTimePercentiles + ", wakeTimePercentiles=" + this.wakeTimePercentiles + ", maxDistance=" + this.maxDistance + ", clusterId=" + this.clusterId + ", clusterNum=" + this.clusterNum + ", clusterDayDistribution=" + this.clusterDayDistribution + ", clusterPercent=" + this.clusterPercent + ')';
    }

    public /* synthetic */ SleepHabitCluster(Map map, Map map2, double d10, int i10, int i11, Map map3, int i12, int i13, DefaultConstructorMarker defaultConstructorMarker) {
        this((i13 & 1) != 0 ? m0.i() : map, (i13 & 2) != 0 ? m0.i() : map2, (i13 & 4) != 0 ? UserProfileInfo.Constant.NA_LAT_LON : d10, (i13 & 8) != 0 ? 0 : i10, (i13 & 16) != 0 ? 0 : i11, (i13 & 32) != 0 ? m0.i() : map3, (i13 & 64) == 0 ? i12 : 0);
    }
}
