package com.android.server.credentials.metrics;

import com.android.server.audio.AudioService$$ExternalSyntheticLambda3;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class InitialPhaseMetric {
    private static final String TAG = "InitialPhaseMetric";
    private final int mSessionIdCaller;
    private int mApiName = ApiName.UNKNOWN.getMetricCode();
    private int mCallerUid = -1;
    private long mCredentialServiceStartedTimeNanoseconds = -1;
    private long mCredentialServiceBeginQueryTimeNanoseconds = -1;
    private boolean mOriginSpecified = false;
    private Map<String, Integer> mRequestCounts = new LinkedHashMap();

    public InitialPhaseMetric(int i) {
        this.mSessionIdCaller = i;
    }

    public int getServiceStartToQueryLatencyMicroseconds() {
        return (int) ((this.mCredentialServiceStartedTimeNanoseconds - this.mCredentialServiceBeginQueryTimeNanoseconds) / 1000);
    }

    public void setCredentialServiceStartedTimeNanoseconds(long j) {
        this.mCredentialServiceStartedTimeNanoseconds = j;
    }

    public void setCredentialServiceBeginQueryTimeNanoseconds(long j) {
        this.mCredentialServiceBeginQueryTimeNanoseconds = j;
    }

    public long getCredentialServiceStartedTimeNanoseconds() {
        return this.mCredentialServiceStartedTimeNanoseconds;
    }

    public long getCredentialServiceBeginQueryTimeNanoseconds() {
        return this.mCredentialServiceBeginQueryTimeNanoseconds;
    }

    public void setApiName(int i) {
        this.mApiName = i;
    }

    public int getApiName() {
        return this.mApiName;
    }

    public void setCallerUid(int i) {
        this.mCallerUid = i;
    }

    public int getCallerUid() {
        return this.mCallerUid;
    }

    public int getSessionIdCaller() {
        return this.mSessionIdCaller;
    }

    public int getCountRequestClassType() {
        return this.mRequestCounts.size();
    }

    public void setOriginSpecified(boolean z) {
        this.mOriginSpecified = z;
    }

    public boolean isOriginSpecified() {
        return this.mOriginSpecified;
    }

    public void setRequestCounts(Map<String, Integer> map) {
        this.mRequestCounts = map;
    }

    public String[] getUniqueRequestStrings() {
        String[] strArr = new String[this.mRequestCounts.keySet().size()];
        this.mRequestCounts.keySet().toArray(strArr);
        return strArr;
    }

    public int[] getUniqueRequestCounts() {
        return this.mRequestCounts.values().stream().mapToInt(new AudioService$$ExternalSyntheticLambda3()).toArray();
    }
}
