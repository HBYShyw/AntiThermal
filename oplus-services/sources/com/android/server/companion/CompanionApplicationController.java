package com.android.server.companion;

import android.annotation.SuppressLint;
import android.companion.AssociationInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.PerUser;
import com.android.server.companion.CompanionDeviceServiceConnector;
import com.android.server.companion.presence.CompanionDevicePresenceMonitor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressLint({"LongLogTag"})
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class CompanionApplicationController {
    static final boolean DEBUG = false;
    private static final long REBIND_TIMEOUT = 10000;
    private static final String TAG = "CDM_CompanionApplicationController";
    private final AssociationStore mAssociationStore;
    private final Context mContext;
    private final CompanionDevicePresenceMonitor mDevicePresenceMonitor;
    private final CompanionServicesRegister mCompanionServicesRegister = new CompanionServicesRegister();

    @GuardedBy({"mBoundCompanionApplications"})
    private final AndroidPackageMap<List<CompanionDeviceServiceConnector>> mBoundCompanionApplications = new AndroidPackageMap<>();

    @GuardedBy({"mScheduledForRebindingCompanionApplications"})
    private final AndroidPackageMap<Boolean> mScheduledForRebindingCompanionApplications = new AndroidPackageMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompanionApplicationController(Context context, AssociationStore associationStore, CompanionDevicePresenceMonitor companionDevicePresenceMonitor) {
        this.mContext = context;
        this.mAssociationStore = associationStore;
        this.mDevicePresenceMonitor = companionDevicePresenceMonitor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackagesChanged(int i) {
        this.mCompanionServicesRegister.invalidate(i);
    }

    public void bindCompanionApplication(int i, String str, boolean z) {
        List<ComponentName> forPackage = this.mCompanionServicesRegister.forPackage(i, str);
        if (forPackage.isEmpty()) {
            Slog.w(TAG, "Can not bind companion applications u" + i + "/" + str + ": eligible CompanionDeviceService not found.\nA CompanionDeviceService should declare an intent-filter for \"android.companion.CompanionDeviceService\" action and require \"android.permission.BIND_COMPANION_DEVICE_SERVICE\" permission.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this.mBoundCompanionApplications) {
            if (this.mBoundCompanionApplications.containsValueForPackage(i, str)) {
                return;
            }
            int i2 = 0;
            while (i2 < forPackage.size()) {
                arrayList.add(CompanionDeviceServiceConnector.newInstance(this.mContext, i, forPackage.get(i2), z, i2 == 0));
                i2++;
            }
            this.mBoundCompanionApplications.setValueForPackage(i, str, arrayList);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ((CompanionDeviceServiceConnector) it.next()).setListener(new CompanionDeviceServiceConnector.Listener() { // from class: com.android.server.companion.CompanionApplicationController$$ExternalSyntheticLambda1
                    @Override // com.android.server.companion.CompanionDeviceServiceConnector.Listener
                    public final void onBindingDied(int i3, String str2, CompanionDeviceServiceConnector companionDeviceServiceConnector) {
                        CompanionApplicationController.this.onBinderDied(i3, str2, companionDeviceServiceConnector);
                    }
                });
            }
            Iterator it2 = arrayList.iterator();
            while (it2.hasNext()) {
                ((CompanionDeviceServiceConnector) it2.next()).connect();
            }
        }
    }

    public void unbindCompanionApplication(int i, String str) {
        List<CompanionDeviceServiceConnector> removePackage;
        synchronized (this.mBoundCompanionApplications) {
            removePackage = this.mBoundCompanionApplications.removePackage(i, str);
        }
        synchronized (this.mScheduledForRebindingCompanionApplications) {
            this.mScheduledForRebindingCompanionApplications.removePackage(i, str);
        }
        if (removePackage == null) {
            return;
        }
        Iterator<CompanionDeviceServiceConnector> it = removePackage.iterator();
        while (it.hasNext()) {
            it.next().postUnbind();
        }
    }

    public boolean isCompanionApplicationBound(int i, String str) {
        boolean containsValueForPackage;
        synchronized (this.mBoundCompanionApplications) {
            containsValueForPackage = this.mBoundCompanionApplications.containsValueForPackage(i, str);
        }
        return containsValueForPackage;
    }

    private void scheduleRebinding(final int i, final String str, final CompanionDeviceServiceConnector companionDeviceServiceConnector) {
        Slog.i(TAG, "scheduleRebinding() " + i + "/" + str);
        if (isRebindingCompanionApplicationScheduled(i, str)) {
            return;
        }
        if (companionDeviceServiceConnector.isPrimary()) {
            synchronized (this.mScheduledForRebindingCompanionApplications) {
                this.mScheduledForRebindingCompanionApplications.setValueForPackage(i, str, Boolean.TRUE);
            }
        }
        Handler.getMain().postDelayed(new Runnable() { // from class: com.android.server.companion.CompanionApplicationController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CompanionApplicationController.this.lambda$scheduleRebinding$0(i, str, companionDeviceServiceConnector);
            }
        }, 10000L);
    }

    private boolean isRebindingCompanionApplicationScheduled(int i, String str) {
        boolean containsValueForPackage;
        synchronized (this.mScheduledForRebindingCompanionApplications) {
            containsValueForPackage = this.mScheduledForRebindingCompanionApplications.containsValueForPackage(i, str);
        }
        return containsValueForPackage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onRebindingCompanionApplicationTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleRebinding$0(int i, String str, CompanionDeviceServiceConnector companionDeviceServiceConnector) {
        if (companionDeviceServiceConnector.isPrimary()) {
            synchronized (this.mBoundCompanionApplications) {
                if (!this.mBoundCompanionApplications.containsValueForPackage(i, str)) {
                    this.mBoundCompanionApplications.setValueForPackage(i, str, Collections.singletonList(companionDeviceServiceConnector));
                }
            }
            synchronized (this.mScheduledForRebindingCompanionApplications) {
                this.mScheduledForRebindingCompanionApplications.removePackage(i, str);
            }
        }
        companionDeviceServiceConnector.connect();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyCompanionApplicationDeviceAppeared(AssociationInfo associationInfo) {
        int userId = associationInfo.getUserId();
        String packageName = associationInfo.getPackageName();
        CompanionDeviceServiceConnector primaryServiceConnector = getPrimaryServiceConnector(userId, packageName);
        if (primaryServiceConnector == null) {
            return;
        }
        Log.i(TAG, "Calling onDeviceAppeared to userId=[" + userId + "] package=[" + packageName + "] associationId=[" + associationInfo.getId() + "]");
        primaryServiceConnector.postOnDeviceAppeared(associationInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyCompanionApplicationDeviceDisappeared(AssociationInfo associationInfo) {
        int userId = associationInfo.getUserId();
        String packageName = associationInfo.getPackageName();
        CompanionDeviceServiceConnector primaryServiceConnector = getPrimaryServiceConnector(userId, packageName);
        if (primaryServiceConnector == null) {
            return;
        }
        Log.i(TAG, "Calling onDeviceDisappeared to userId=[" + userId + "] package=[" + packageName + "] associationId=[" + associationInfo.getId() + "]");
        primaryServiceConnector.postOnDeviceDisappeared(associationInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.append("Companion Device Application Controller: \n");
        synchronized (this.mBoundCompanionApplications) {
            printWriter.append("  Bound Companion Applications: ");
            if (this.mBoundCompanionApplications.size() == 0) {
                printWriter.append("<empty>\n");
            } else {
                printWriter.append("\n");
                this.mBoundCompanionApplications.dump(printWriter);
            }
        }
        printWriter.append("  Companion Applications Scheduled For Rebinding: ");
        if (this.mScheduledForRebindingCompanionApplications.size() == 0) {
            printWriter.append("<empty>\n");
        } else {
            printWriter.append("\n");
            this.mScheduledForRebindingCompanionApplications.dump(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBinderDied(int i, String str, CompanionDeviceServiceConnector companionDeviceServiceConnector) {
        boolean isPrimary = companionDeviceServiceConnector.isPrimary();
        Slog.i(TAG, "onBinderDied() u" + i + "/" + str + " isPrimary: " + isPrimary);
        synchronized (this.mBoundCompanionApplications) {
            if (companionDeviceServiceConnector.isPrimary()) {
                this.mBoundCompanionApplications.removePackage(i, str);
            }
        }
        if (shouldScheduleRebind(i, str, isPrimary)) {
            scheduleRebinding(i, str, companionDeviceServiceConnector);
        }
    }

    private CompanionDeviceServiceConnector getPrimaryServiceConnector(int i, String str) {
        List<CompanionDeviceServiceConnector> valueForPackage;
        synchronized (this.mBoundCompanionApplications) {
            valueForPackage = this.mBoundCompanionApplications.getValueForPackage(i, str);
        }
        if (valueForPackage != null) {
            return valueForPackage.get(0);
        }
        return null;
    }

    private boolean shouldScheduleRebind(int i, String str, boolean z) {
        boolean z2 = false;
        boolean z3 = false;
        for (AssociationInfo associationInfo : this.mAssociationStore.getAssociationsForPackage(i, str)) {
            int id = associationInfo.getId();
            if (associationInfo.isSelfManaged()) {
                if (z && this.mDevicePresenceMonitor.isDevicePresent(id)) {
                    this.mDevicePresenceMonitor.onSelfManagedDeviceReporterBinderDied(id);
                }
                z3 = isCompanionApplicationBound(i, str);
            } else if (associationInfo.isNotifyOnDeviceNearby()) {
                z3 = true;
            }
            z2 = true;
        }
        return z2 && z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class CompanionServicesRegister extends PerUser<Map<String, List<ComponentName>>> {
        private CompanionServicesRegister() {
        }

        public synchronized Map<String, List<ComponentName>> forUser(int i) {
            return (Map) super.forUser(i);
        }

        synchronized List<ComponentName> forPackage(int i, String str) {
            return forUser(i).getOrDefault(str, Collections.emptyList());
        }

        synchronized void invalidate(int i) {
            remove(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final Map<String, List<ComponentName>> create(int i) {
            return PackageUtils.getCompanionServicesForUser(CompanionApplicationController.this.mContext, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class AndroidPackageMap<T> extends SparseArray<Map<String, T>> {
        private AndroidPackageMap() {
        }

        void setValueForPackage(int i, String str, T t) {
            Map<String, T> map = get(i);
            if (map == null) {
                map = new HashMap<>();
                put(i, map);
            }
            map.put(str, t);
        }

        boolean containsValueForPackage(int i, String str) {
            Map<String, T> map = get(i);
            return map != null && map.containsKey(str);
        }

        T getValueForPackage(int i, String str) {
            Map<String, T> map = get(i);
            if (map != null) {
                return map.get(str);
            }
            return null;
        }

        T removePackage(int i, String str) {
            Map<String, T> map = get(i);
            if (map == null) {
                return null;
            }
            return map.remove(str);
        }

        void dump() {
            if (size() == 0) {
                Log.d(CompanionApplicationController.TAG, "<empty>");
                return;
            }
            for (int i = 0; i < size(); i++) {
                int keyAt = keyAt(i);
                Map<String, T> map = get(keyAt);
                if (map.isEmpty()) {
                    Log.d(CompanionApplicationController.TAG, "u" + keyAt + ": <empty>");
                }
                for (Map.Entry<String, T> entry : map.entrySet()) {
                    Log.d(CompanionApplicationController.TAG, "u" + keyAt + "\\" + entry.getKey() + " -> " + entry.getValue());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter) {
            for (int i = 0; i < size(); i++) {
                int keyAt = keyAt(i);
                Map<String, T> map = get(keyAt);
                if (map.isEmpty()) {
                    printWriter.append("    u").append((CharSequence) String.valueOf(keyAt)).append(": <empty>\n");
                }
                for (Map.Entry<String, T> entry : map.entrySet()) {
                    printWriter.append("    u").append((CharSequence) String.valueOf(keyAt)).append("\\").append((CharSequence) entry.getKey()).append(" -> ").append((CharSequence) entry.getValue().toString()).append('\n');
                }
            }
        }
    }
}
