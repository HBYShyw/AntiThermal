package com.android.server.credentials.metrics;

import com.android.server.audio.AudioService$$ExternalSyntheticLambda3;
import com.android.server.credentials.ProviderSession;
import com.android.server.credentials.metrics.shared.ResponseCollective;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CandidateAggregateMetric {
    private static final String TAG = "CandidateTotalMetric";
    private final int mSessionIdProvider;
    private boolean mQueryReturned = false;
    private long mServiceBeganTimeNanoseconds = -1;
    private int mNumProviders = 0;
    private boolean mAuthReturned = false;
    private int mNumAuthEntriesTapped = 0;
    private ResponseCollective mAggregateCollectiveQuery = new ResponseCollective(Map.of(), Map.of());
    private ResponseCollective mAggregateCollectiveAuth = new ResponseCollective(Map.of(), Map.of());
    private long mMinProviderTimestampNanoseconds = -1;
    private long mMaxProviderTimestampNanoseconds = -1;
    private int mTotalQueryFailures = 0;
    private Map<String, Integer> mExceptionCountQuery = new LinkedHashMap();
    private int mTotalAuthFailures = 0;
    private Map<String, Integer> mExceptionCountAuth = new LinkedHashMap();

    public CandidateAggregateMetric(int i) {
        this.mSessionIdProvider = i;
    }

    public int getSessionIdProvider() {
        return this.mSessionIdProvider;
    }

    public void collectAverages(Map<String, ProviderSession> map) {
        collectQueryAggregates(map);
        collectAuthAggregates(map);
    }

    private void collectQueryAggregates(Map<String, ProviderSession> map) {
        this.mNumProviders = map.size();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        Iterator<ProviderSession> it = map.values().iterator();
        long j = Long.MAX_VALUE;
        long j2 = Long.MIN_VALUE;
        while (it.hasNext()) {
            CandidatePhaseMetric candidatePhasePerProviderMetric = it.next().getProviderSessionMetric().getCandidatePhasePerProviderMetric();
            if (candidatePhasePerProviderMetric.getCandidateUid() == -1) {
                this.mNumProviders--;
            } else {
                if (this.mServiceBeganTimeNanoseconds == -1) {
                    this.mServiceBeganTimeNanoseconds = candidatePhasePerProviderMetric.getServiceBeganTimeNanoseconds();
                }
                this.mQueryReturned = this.mQueryReturned || candidatePhasePerProviderMetric.isQueryReturned();
                ResponseCollective responseCollective = candidatePhasePerProviderMetric.getResponseCollective();
                ResponseCollective.combineTypeCountMaps(linkedHashMap, responseCollective.getResponseCountsMap());
                ResponseCollective.combineTypeCountMaps(linkedHashMap2, responseCollective.getEntryCountsMap());
                j = Math.min(j, candidatePhasePerProviderMetric.getStartQueryTimeNanoseconds());
                j2 = Math.max(j2, candidatePhasePerProviderMetric.getQueryFinishTimeNanoseconds());
                this.mTotalQueryFailures += candidatePhasePerProviderMetric.isHasException() ? 1 : 0;
                if (!candidatePhasePerProviderMetric.getFrameworkException().isEmpty()) {
                    this.mExceptionCountQuery.put(candidatePhasePerProviderMetric.getFrameworkException(), Integer.valueOf(this.mExceptionCountQuery.getOrDefault(candidatePhasePerProviderMetric.getFrameworkException(), 0).intValue() + 1));
                }
            }
        }
        this.mMinProviderTimestampNanoseconds = j;
        this.mMaxProviderTimestampNanoseconds = j2;
        this.mAggregateCollectiveQuery = new ResponseCollective(linkedHashMap, linkedHashMap2);
    }

    private void collectAuthAggregates(Map<String, ProviderSession> map) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        Iterator<ProviderSession> it = map.values().iterator();
        while (it.hasNext()) {
            for (BrowsedAuthenticationMetric browsedAuthenticationMetric : it.next().getProviderSessionMetric().getBrowsedAuthenticationMetric()) {
                if (browsedAuthenticationMetric.getProviderUid() != -1) {
                    this.mNumAuthEntriesTapped++;
                    this.mAuthReturned = this.mAuthReturned || browsedAuthenticationMetric.isAuthReturned();
                    ResponseCollective authEntryCollective = browsedAuthenticationMetric.getAuthEntryCollective();
                    ResponseCollective.combineTypeCountMaps(linkedHashMap, authEntryCollective.getResponseCountsMap());
                    ResponseCollective.combineTypeCountMaps(linkedHashMap2, authEntryCollective.getEntryCountsMap());
                    this.mTotalQueryFailures += browsedAuthenticationMetric.isHasException() ? 1 : 0;
                    if (!browsedAuthenticationMetric.getFrameworkException().isEmpty()) {
                        this.mExceptionCountQuery.put(browsedAuthenticationMetric.getFrameworkException(), Integer.valueOf(this.mExceptionCountQuery.getOrDefault(browsedAuthenticationMetric.getFrameworkException(), 0).intValue() + 1));
                    }
                }
            }
        }
        this.mAggregateCollectiveAuth = new ResponseCollective(linkedHashMap, linkedHashMap2);
    }

    public int getNumProviders() {
        return this.mNumProviders;
    }

    public boolean isQueryReturned() {
        return this.mQueryReturned;
    }

    public int getNumAuthEntriesTapped() {
        return this.mNumAuthEntriesTapped;
    }

    public ResponseCollective getAggregateCollectiveQuery() {
        return this.mAggregateCollectiveQuery;
    }

    public ResponseCollective getAggregateCollectiveAuth() {
        return this.mAggregateCollectiveAuth;
    }

    public boolean isAuthReturned() {
        return this.mAuthReturned;
    }

    public long getMaxProviderTimestampNanoseconds() {
        return this.mMaxProviderTimestampNanoseconds;
    }

    public long getMinProviderTimestampNanoseconds() {
        return this.mMinProviderTimestampNanoseconds;
    }

    public int getTotalQueryFailures() {
        return this.mTotalQueryFailures;
    }

    public String[] getUniqueExceptionStringsQuery() {
        String[] strArr = new String[this.mExceptionCountQuery.keySet().size()];
        this.mExceptionCountQuery.keySet().toArray(strArr);
        return strArr;
    }

    public int[] getUniqueExceptionCountsQuery() {
        return this.mExceptionCountQuery.values().stream().mapToInt(new AudioService$$ExternalSyntheticLambda3()).toArray();
    }

    public String[] getUniqueExceptionStringsAuth() {
        String[] strArr = new String[this.mExceptionCountAuth.keySet().size()];
        this.mExceptionCountAuth.keySet().toArray(strArr);
        return strArr;
    }

    public int[] getUniqueExceptionCountsAuth() {
        return this.mExceptionCountAuth.values().stream().mapToInt(new AudioService$$ExternalSyntheticLambda3()).toArray();
    }

    public long getServiceBeganTimeNanoseconds() {
        return this.mServiceBeganTimeNanoseconds;
    }

    public int getTotalAuthFailures() {
        return this.mTotalAuthFailures;
    }
}
