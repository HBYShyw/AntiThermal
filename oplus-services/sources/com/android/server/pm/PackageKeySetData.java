package com.android.server.pm;

import android.util.ArrayMap;
import com.android.internal.util.ArrayUtils;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageKeySetData {
    static final long KEYSET_UNASSIGNED = -1;
    private final ArrayMap<String, Long> mKeySetAliases;
    private long mProperSigningKeySet;
    private long[] mUpgradeKeySets;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageKeySetData() {
        this.mKeySetAliases = new ArrayMap<>();
        this.mProperSigningKeySet = -1L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageKeySetData(PackageKeySetData packageKeySetData) {
        ArrayMap<String, Long> arrayMap = new ArrayMap<>();
        this.mKeySetAliases = arrayMap;
        this.mProperSigningKeySet = packageKeySetData.mProperSigningKeySet;
        this.mUpgradeKeySets = ArrayUtils.cloneOrNull(packageKeySetData.mUpgradeKeySets);
        arrayMap.putAll((ArrayMap<? extends String, ? extends Long>) packageKeySetData.mKeySetAliases);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setProperSigningKeySet(long j) {
        this.mProperSigningKeySet = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getProperSigningKeySet() {
        return this.mProperSigningKeySet;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addUpgradeKeySet(String str) {
        if (str == null) {
            return;
        }
        Long l = this.mKeySetAliases.get(str);
        if (l != null) {
            this.mUpgradeKeySets = ArrayUtils.appendLong(this.mUpgradeKeySets, l.longValue());
            return;
        }
        throw new IllegalArgumentException("Upgrade keyset alias " + str + "does not refer to a defined keyset alias!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addUpgradeKeySetById(long j) {
        this.mUpgradeKeySets = ArrayUtils.appendLong(this.mUpgradeKeySets, j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeAllUpgradeKeySets() {
        this.mUpgradeKeySets = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long[] getUpgradeKeySets() {
        return this.mUpgradeKeySets;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ArrayMap<String, Long> getAliases() {
        return this.mKeySetAliases;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAliases(Map<String, Long> map) {
        removeAllDefinedKeySets();
        this.mKeySetAliases.putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addDefinedKeySet(long j, String str) {
        this.mKeySetAliases.put(str, Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeAllDefinedKeySets() {
        this.mKeySetAliases.erase();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUsingDefinedKeySets() {
        return this.mKeySetAliases.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isUsingUpgradeKeySets() {
        long[] jArr = this.mUpgradeKeySets;
        return jArr != null && jArr.length > 0;
    }
}
