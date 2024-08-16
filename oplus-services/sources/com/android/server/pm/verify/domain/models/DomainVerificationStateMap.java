package com.android.server.pm.verify.domain.models;

import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DomainVerificationStateMap<ValueType> {
    private static final String TAG = "DomainVerificationStateMap";
    private final ArrayMap<String, ValueType> mPackageNameMap = new ArrayMap<>();
    private final ArrayMap<UUID, ValueType> mDomainSetIdMap = new ArrayMap<>();

    public int size() {
        return this.mPackageNameMap.size();
    }

    public ValueType valueAt(int i) {
        return this.mPackageNameMap.valueAt(i);
    }

    public ValueType get(String str) {
        return this.mPackageNameMap.get(str);
    }

    public ValueType get(UUID uuid) {
        return this.mDomainSetIdMap.get(uuid);
    }

    public void put(String str, UUID uuid, ValueType valuetype) {
        if (this.mPackageNameMap.containsKey(str)) {
            remove(str);
        }
        this.mPackageNameMap.put(str, valuetype);
        this.mDomainSetIdMap.put(uuid, valuetype);
    }

    public ValueType remove(String str) {
        int indexOfValue;
        ValueType remove = this.mPackageNameMap.remove(str);
        if (remove != null && (indexOfValue = this.mDomainSetIdMap.indexOfValue(remove)) >= 0) {
            this.mDomainSetIdMap.removeAt(indexOfValue);
        }
        return remove;
    }

    public ValueType remove(UUID uuid) {
        int indexOfValue;
        ValueType remove = this.mDomainSetIdMap.remove(uuid);
        if (remove != null && (indexOfValue = this.mPackageNameMap.indexOfValue(remove)) >= 0) {
            this.mPackageNameMap.removeAt(indexOfValue);
        }
        return remove;
    }

    public List<String> getPackageNames() {
        return new ArrayList(this.mPackageNameMap.keySet());
    }

    @VisibleForTesting
    public Collection<ValueType> values() {
        return new ArrayList(this.mPackageNameMap.values());
    }

    public String toString() {
        return "DomainVerificationStateMap{packageNameMap=" + this.mPackageNameMap + ", domainSetIdMap=" + this.mDomainSetIdMap + '}';
    }
}
