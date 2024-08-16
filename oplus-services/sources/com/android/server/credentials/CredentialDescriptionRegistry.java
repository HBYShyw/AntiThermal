package com.android.server.credentials;

import android.credentials.CredentialDescription;
import android.credentials.RegisterCredentialDescriptionRequest;
import android.credentials.UnregisterCredentialDescriptionRequest;
import android.service.credentials.CredentialEntry;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CredentialDescriptionRegistry {
    private static final int MAX_ALLOWED_CREDENTIAL_DESCRIPTIONS = 128;
    private static final int MAX_ALLOWED_ENTRIES_PER_PROVIDER = 16;

    @GuardedBy({"sLock"})
    private static final SparseArray<CredentialDescriptionRegistry> sCredentialDescriptionSessionPerUser = new SparseArray<>();
    private static final ReentrantLock sLock = new ReentrantLock();
    private Map<String, Set<CredentialDescription>> mCredentialDescriptions = new HashMap();
    private int mTotalDescriptionCount = 0;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class FilterResult {
        final List<CredentialEntry> mCredentialEntries;
        final Set<String> mElementKeys;
        final String mPackageName;

        @VisibleForTesting
        FilterResult(String str, Set<String> set, List<CredentialEntry> list) {
            this.mPackageName = str;
            this.mElementKeys = set;
            this.mCredentialEntries = list;
        }
    }

    @GuardedBy({"sLock"})
    public static CredentialDescriptionRegistry forUser(int i) {
        ReentrantLock reentrantLock = sLock;
        reentrantLock.lock();
        try {
            SparseArray<CredentialDescriptionRegistry> sparseArray = sCredentialDescriptionSessionPerUser;
            CredentialDescriptionRegistry credentialDescriptionRegistry = sparseArray.get(i, null);
            if (credentialDescriptionRegistry == null) {
                credentialDescriptionRegistry = new CredentialDescriptionRegistry();
                sparseArray.put(i, credentialDescriptionRegistry);
            }
            reentrantLock.unlock();
            return credentialDescriptionRegistry;
        } catch (Throwable th) {
            sLock.unlock();
            throw th;
        }
    }

    @GuardedBy({"sLock"})
    public static void clearUserSession(int i) {
        ReentrantLock reentrantLock = sLock;
        reentrantLock.lock();
        try {
            sCredentialDescriptionSessionPerUser.remove(i);
            reentrantLock.unlock();
        } catch (Throwable th) {
            sLock.unlock();
            throw th;
        }
    }

    @GuardedBy({"sLock"})
    @VisibleForTesting
    static void clearAllSessions() {
        ReentrantLock reentrantLock = sLock;
        reentrantLock.lock();
        try {
            sCredentialDescriptionSessionPerUser.clear();
            reentrantLock.unlock();
        } catch (Throwable th) {
            sLock.unlock();
            throw th;
        }
    }

    @GuardedBy({"sLock"})
    @VisibleForTesting
    static void setSession(int i, CredentialDescriptionRegistry credentialDescriptionRegistry) {
        ReentrantLock reentrantLock = sLock;
        reentrantLock.lock();
        try {
            sCredentialDescriptionSessionPerUser.put(i, credentialDescriptionRegistry);
            reentrantLock.unlock();
        } catch (Throwable th) {
            sLock.unlock();
            throw th;
        }
    }

    private CredentialDescriptionRegistry() {
    }

    public void executeRegisterRequest(RegisterCredentialDescriptionRequest registerCredentialDescriptionRequest, String str) {
        if (!this.mCredentialDescriptions.containsKey(str)) {
            this.mCredentialDescriptions.put(str, new HashSet());
        }
        if (this.mTotalDescriptionCount > 128 || this.mCredentialDescriptions.get(str).size() > 16) {
            return;
        }
        Set<CredentialDescription> credentialDescriptions = registerCredentialDescriptionRequest.getCredentialDescriptions();
        int size = this.mCredentialDescriptions.get(str).size();
        this.mCredentialDescriptions.get(str).addAll(credentialDescriptions);
        this.mTotalDescriptionCount += this.mCredentialDescriptions.get(str).size() - size;
    }

    public void executeUnregisterRequest(UnregisterCredentialDescriptionRequest unregisterCredentialDescriptionRequest, String str) {
        if (this.mCredentialDescriptions.containsKey(str)) {
            int size = this.mCredentialDescriptions.get(str).size();
            this.mCredentialDescriptions.get(str).removeAll(unregisterCredentialDescriptionRequest.getCredentialDescriptions());
            this.mTotalDescriptionCount -= size - this.mCredentialDescriptions.get(str).size();
        }
    }

    public Set<FilterResult> getFilteredResultForProvider(String str, Set<String> set) {
        HashSet hashSet = new HashSet();
        if (!this.mCredentialDescriptions.containsKey(str)) {
            return hashSet;
        }
        for (CredentialDescription credentialDescription : this.mCredentialDescriptions.get(str)) {
            if (checkForMatch(credentialDescription.getSupportedElementKeys(), set)) {
                hashSet.add(new FilterResult(str, credentialDescription.getSupportedElementKeys(), credentialDescription.getCredentialEntries()));
            }
        }
        return hashSet;
    }

    public Set<FilterResult> getMatchingProviders(Set<Set<String>> set) {
        HashSet hashSet = new HashSet();
        for (String str : this.mCredentialDescriptions.keySet()) {
            for (CredentialDescription credentialDescription : this.mCredentialDescriptions.get(str)) {
                if (canProviderSatisfyAny(credentialDescription.getSupportedElementKeys(), set)) {
                    hashSet.add(new FilterResult(str, credentialDescription.getSupportedElementKeys(), credentialDescription.getCredentialEntries()));
                }
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void evictProviderWithPackageName(String str) {
        if (this.mCredentialDescriptions.containsKey(str)) {
            this.mCredentialDescriptions.remove(str);
        }
    }

    private static boolean canProviderSatisfyAny(Set<String> set, Set<Set<String>> set2) {
        Iterator<Set<String>> it = set2.iterator();
        while (it.hasNext()) {
            if (set.containsAll(it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean checkForMatch(Set<String> set, Set<String> set2) {
        return set.containsAll(set2);
    }
}
