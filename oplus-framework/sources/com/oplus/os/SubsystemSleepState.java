package com.oplus.os;

import java.util.Objects;

/* loaded from: classes.dex */
public class SubsystemSleepState {
    public String name = new String();
    public long version = 0;
    public long residencyInMsecSinceBoot = 0;
    public long totalTransitions = 0;
    public long lastEntryTimestampMs = 0;
    public boolean supportedOnlyInSuspend = false;

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(Objects.hashCode(this.name)), Integer.valueOf(Objects.hashCode(Long.valueOf(this.version))), Integer.valueOf(Objects.hashCode(Long.valueOf(this.residencyInMsecSinceBoot))), Integer.valueOf(Objects.hashCode(Long.valueOf(this.totalTransitions))), Integer.valueOf(Objects.hashCode(Long.valueOf(this.lastEntryTimestampMs))), Integer.valueOf(Objects.hashCode(Boolean.valueOf(this.supportedOnlyInSuspend))));
    }

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != SubsystemSleepState.class) {
            return false;
        }
        SubsystemSleepState other = (SubsystemSleepState) otherObject;
        if (this.version == other.version && this.residencyInMsecSinceBoot == other.residencyInMsecSinceBoot && this.totalTransitions == other.totalTransitions && this.lastEntryTimestampMs == other.lastEntryTimestampMs && this.supportedOnlyInSuspend == other.supportedOnlyInSuspend) {
            return true;
        }
        return false;
    }

    public final String toString() {
        return "{.name = " + this.name + ", .version = " + this.version + ", .residencyInMsecSinceBoot = " + this.residencyInMsecSinceBoot + ", .totalTransitions = " + this.totalTransitions + ", .lastEntryTimestampMs = " + this.lastEntryTimestampMs + ", .supportedOnlyInSuspend = " + this.supportedOnlyInSuspend + "}";
    }
}
