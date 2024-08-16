package com.android.server.credentials.metrics;

import com.android.server.credentials.metrics.shared.ResponseCollective;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BrowsedAuthenticationMetric {
    private static final String TAG = "AuthenticationMetric";
    private final int mSessionIdProvider;
    private int mProviderUid = -1;
    private ResponseCollective mAuthEntryCollective = new ResponseCollective(Map.of(), Map.of());
    private boolean mHasException = false;
    private String mFrameworkException = "";
    private int mProviderStatus = -1;
    private boolean mAuthReturned = false;

    public BrowsedAuthenticationMetric(int i) {
        this.mSessionIdProvider = i;
    }

    public int getSessionIdProvider() {
        return this.mSessionIdProvider;
    }

    public void setProviderUid(int i) {
        this.mProviderUid = i;
    }

    public int getProviderUid() {
        return this.mProviderUid;
    }

    public void setAuthEntryCollective(ResponseCollective responseCollective) {
        this.mAuthEntryCollective = responseCollective;
    }

    public ResponseCollective getAuthEntryCollective() {
        return this.mAuthEntryCollective;
    }

    public void setHasException(boolean z) {
        this.mHasException = z;
    }

    public void setFrameworkException(String str) {
        this.mFrameworkException = str;
    }

    public void setProviderStatus(int i) {
        this.mProviderStatus = i;
    }

    public void setAuthReturned(boolean z) {
        this.mAuthReturned = z;
    }

    public boolean isAuthReturned() {
        return this.mAuthReturned;
    }

    public int getProviderStatus() {
        return this.mProviderStatus;
    }

    public String getFrameworkException() {
        return this.mFrameworkException;
    }

    public boolean isHasException() {
        return this.mHasException;
    }
}
