package com.android.server.companion;

import android.annotation.SuppressLint;
import android.companion.AssociationInfo;
import android.net.MacAddress;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.CollectionUtils;
import com.android.server.companion.AssociationStore;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AssociationStoreImpl implements AssociationStore {
    private static final boolean DEBUG = false;
    private static final String TAG = "CDM_AssociationStore";
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private final Map<Integer, AssociationInfo> mIdMap = new HashMap();

    @GuardedBy({"mLock"})
    private final Map<MacAddress, Set<Integer>> mAddressMap = new HashMap();

    @GuardedBy({"mLock"})
    private final SparseArray<List<AssociationInfo>> mCachedPerUser = new SparseArray<>();

    @GuardedBy({"mListeners"})
    private final Set<AssociationStore.OnChangeListener> mListeners = new LinkedHashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAssociation(AssociationInfo associationInfo) {
        checkNotRevoked(associationInfo);
        int id = associationInfo.getId();
        synchronized (this.mLock) {
            if (this.mIdMap.containsKey(Integer.valueOf(id))) {
                Slog.e(TAG, "Association with id " + id + " already exists.");
                return;
            }
            this.mIdMap.put(Integer.valueOf(id), associationInfo);
            MacAddress deviceMacAddress = associationInfo.getDeviceMacAddress();
            if (deviceMacAddress != null) {
                this.mAddressMap.computeIfAbsent(deviceMacAddress, new Function() { // from class: com.android.server.companion.AssociationStoreImpl$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        Set lambda$addAssociation$0;
                        lambda$addAssociation$0 = AssociationStoreImpl.lambda$addAssociation$0((MacAddress) obj);
                        return lambda$addAssociation$0;
                    }
                }).add(Integer.valueOf(id));
            }
            invalidateCacheForUserLocked(associationInfo.getUserId());
            broadcastChange(0, associationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Set lambda$addAssociation$0(MacAddress macAddress) {
        return new HashSet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAssociation(AssociationInfo associationInfo) {
        checkNotRevoked(associationInfo);
        int id = associationInfo.getId();
        synchronized (this.mLock) {
            AssociationInfo associationInfo2 = this.mIdMap.get(Integer.valueOf(id));
            if (associationInfo2 == null) {
                return;
            }
            if (associationInfo2.equals(associationInfo)) {
                return;
            }
            this.mIdMap.put(Integer.valueOf(id), associationInfo);
            invalidateCacheForUserLocked(associationInfo2.getUserId());
            MacAddress deviceMacAddress = associationInfo.getDeviceMacAddress();
            MacAddress deviceMacAddress2 = associationInfo2.getDeviceMacAddress();
            boolean z = !Objects.equals(deviceMacAddress2, deviceMacAddress);
            if (z) {
                if (deviceMacAddress2 != null) {
                    this.mAddressMap.get(deviceMacAddress2).remove(Integer.valueOf(id));
                }
                if (deviceMacAddress != null) {
                    this.mAddressMap.computeIfAbsent(deviceMacAddress, new Function() { // from class: com.android.server.companion.AssociationStoreImpl$$ExternalSyntheticLambda5
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Set lambda$updateAssociation$1;
                            lambda$updateAssociation$1 = AssociationStoreImpl.lambda$updateAssociation$1((MacAddress) obj);
                            return lambda$updateAssociation$1;
                        }
                    }).add(Integer.valueOf(id));
                }
            }
            broadcastChange(z ? 2 : 3, associationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Set lambda$updateAssociation$1(MacAddress macAddress) {
        return new HashSet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAssociation(int i) {
        synchronized (this.mLock) {
            AssociationInfo remove = this.mIdMap.remove(Integer.valueOf(i));
            if (remove == null) {
                return;
            }
            MacAddress deviceMacAddress = remove.getDeviceMacAddress();
            if (deviceMacAddress != null) {
                this.mAddressMap.get(deviceMacAddress).remove(Integer.valueOf(i));
            }
            invalidateCacheForUserLocked(remove.getUserId());
            broadcastChange(1, remove);
        }
    }

    @Override // com.android.server.companion.AssociationStore
    public Collection<AssociationInfo> getAssociations() {
        List copyOf;
        synchronized (this.mLock) {
            copyOf = List.copyOf(this.mIdMap.values());
        }
        return copyOf;
    }

    @Override // com.android.server.companion.AssociationStore
    public List<AssociationInfo> getAssociationsForUser(int i) {
        List<AssociationInfo> associationsForUserLocked;
        synchronized (this.mLock) {
            associationsForUserLocked = getAssociationsForUserLocked(i);
        }
        return associationsForUserLocked;
    }

    @Override // com.android.server.companion.AssociationStore
    public List<AssociationInfo> getAssociationsForPackage(int i, final String str) {
        return Collections.unmodifiableList(CollectionUtils.filter(getAssociationsForUser(i), new Predicate() { // from class: com.android.server.companion.AssociationStoreImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getAssociationsForPackage$2;
                lambda$getAssociationsForPackage$2 = AssociationStoreImpl.lambda$getAssociationsForPackage$2(str, (AssociationInfo) obj);
                return lambda$getAssociationsForPackage$2;
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getAssociationsForPackage$2(String str, AssociationInfo associationInfo) {
        return associationInfo.getPackageName().equals(str);
    }

    @Override // com.android.server.companion.AssociationStore
    public AssociationInfo getAssociationsForPackageWithAddress(final int i, final String str, String str2) {
        return (AssociationInfo) CollectionUtils.find(getAssociationsByAddress(str2), new Predicate() { // from class: com.android.server.companion.AssociationStoreImpl$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getAssociationsForPackageWithAddress$3;
                lambda$getAssociationsForPackageWithAddress$3 = AssociationStoreImpl.lambda$getAssociationsForPackageWithAddress$3(i, str, (AssociationInfo) obj);
                return lambda$getAssociationsForPackageWithAddress$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getAssociationsForPackageWithAddress$3(int i, String str, AssociationInfo associationInfo) {
        return associationInfo.belongsToPackage(i, str);
    }

    @Override // com.android.server.companion.AssociationStore
    public AssociationInfo getAssociationById(int i) {
        AssociationInfo associationInfo;
        synchronized (this.mLock) {
            associationInfo = this.mIdMap.get(Integer.valueOf(i));
        }
        return associationInfo;
    }

    @Override // com.android.server.companion.AssociationStore
    public List<AssociationInfo> getAssociationsByAddress(String str) {
        MacAddress fromString = MacAddress.fromString(str);
        synchronized (this.mLock) {
            Set<Integer> set = this.mAddressMap.get(fromString);
            if (set == null) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList(set.size());
            Iterator<Integer> it = set.iterator();
            while (it.hasNext()) {
                arrayList.add(this.mIdMap.get(it.next()));
            }
            return Collections.unmodifiableList(arrayList);
        }
    }

    @GuardedBy({"mLock"})
    private List<AssociationInfo> getAssociationsForUserLocked(int i) {
        List<AssociationInfo> list = this.mCachedPerUser.get(i);
        if (list != null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (AssociationInfo associationInfo : this.mIdMap.values()) {
            if (associationInfo.getUserId() == i) {
                arrayList.add(associationInfo);
            }
        }
        List<AssociationInfo> unmodifiableList = Collections.unmodifiableList(arrayList);
        this.mCachedPerUser.set(i, unmodifiableList);
        return unmodifiableList;
    }

    @GuardedBy({"mLock"})
    private void invalidateCacheForUserLocked(int i) {
        this.mCachedPerUser.delete(i);
    }

    @Override // com.android.server.companion.AssociationStore
    public void registerListener(AssociationStore.OnChangeListener onChangeListener) {
        synchronized (this.mListeners) {
            this.mListeners.add(onChangeListener);
        }
    }

    @Override // com.android.server.companion.AssociationStore
    public void unregisterListener(AssociationStore.OnChangeListener onChangeListener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(onChangeListener);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.append("Companion Device Associations: ");
        if (getAssociations().isEmpty()) {
            printWriter.append("<empty>\n");
            return;
        }
        printWriter.append("\n");
        Iterator<AssociationInfo> it = getAssociations().iterator();
        while (it.hasNext()) {
            printWriter.append("  ").append((CharSequence) it.next().toString()).append('\n');
        }
    }

    private void broadcastChange(int i, AssociationInfo associationInfo) {
        synchronized (this.mListeners) {
            Iterator<AssociationStore.OnChangeListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                it.next().onAssociationChanged(i, associationInfo);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAssociations(Collection<AssociationInfo> collection) {
        collection.forEach(new Consumer() { // from class: com.android.server.companion.AssociationStoreImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AssociationStoreImpl.checkNotRevoked((AssociationInfo) obj);
            }
        });
        synchronized (this.mLock) {
            setAssociationsLocked(collection);
        }
    }

    @GuardedBy({"mLock"})
    private void setAssociationsLocked(Collection<AssociationInfo> collection) {
        clearLocked();
        for (AssociationInfo associationInfo : collection) {
            int id = associationInfo.getId();
            this.mIdMap.put(Integer.valueOf(id), associationInfo);
            MacAddress deviceMacAddress = associationInfo.getDeviceMacAddress();
            if (deviceMacAddress != null) {
                this.mAddressMap.computeIfAbsent(deviceMacAddress, new Function() { // from class: com.android.server.companion.AssociationStoreImpl$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        Set lambda$setAssociationsLocked$5;
                        lambda$setAssociationsLocked$5 = AssociationStoreImpl.lambda$setAssociationsLocked$5((MacAddress) obj);
                        return lambda$setAssociationsLocked$5;
                    }
                }).add(Integer.valueOf(id));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Set lambda$setAssociationsLocked$5(MacAddress macAddress) {
        return new HashSet();
    }

    @GuardedBy({"mLock"})
    private void clearLocked() {
        this.mIdMap.clear();
        this.mAddressMap.clear();
        this.mCachedPerUser.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkNotRevoked(AssociationInfo associationInfo) {
        if (associationInfo.isRevoked()) {
            throw new IllegalArgumentException("Revoked (removed) associations MUST NOT appear in the AssociationStore");
        }
    }
}
