package com.android.server.am;

import android.app.IApplicationThread;
import android.content.ComponentName;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.SparseArray;
import com.android.internal.os.TransferPipe;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.DumpUtils;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ProviderMap {
    private static final boolean DBG = false;
    private static final String TAG = "ProviderMap";
    private final ActivityManagerService mAm;
    private final HashMap<String, ContentProviderRecord> mSingletonByName = new HashMap<>();
    private final HashMap<ComponentName, ContentProviderRecord> mSingletonByClass = new HashMap<>();
    private final SparseArray<HashMap<String, ContentProviderRecord>> mProvidersByNamePerUser = new SparseArray<>();
    private final SparseArray<HashMap<ComponentName, ContentProviderRecord>> mProvidersByClassPerUser = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProviderMap(ActivityManagerService activityManagerService) {
        this.mAm = activityManagerService;
    }

    ContentProviderRecord getProviderByName(String str) {
        return getProviderByName(str, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderRecord getProviderByName(String str, int i) {
        ContentProviderRecord contentProviderRecord = this.mSingletonByName.get(str);
        return contentProviderRecord != null ? contentProviderRecord : getProvidersByName(i).get(str);
    }

    ContentProviderRecord getProviderByClass(ComponentName componentName) {
        return getProviderByClass(componentName, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentProviderRecord getProviderByClass(ComponentName componentName, int i) {
        ContentProviderRecord contentProviderRecord = this.mSingletonByClass.get(componentName);
        return contentProviderRecord != null ? contentProviderRecord : getProvidersByClass(i).get(componentName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void putProviderByName(String str, ContentProviderRecord contentProviderRecord) {
        if (contentProviderRecord.singleton) {
            this.mSingletonByName.put(str, contentProviderRecord);
        } else {
            getProvidersByName(UserHandle.getUserId(contentProviderRecord.appInfo.uid)).put(str, contentProviderRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void putProviderByClass(ComponentName componentName, ContentProviderRecord contentProviderRecord) {
        if (contentProviderRecord.singleton) {
            this.mSingletonByClass.put(componentName, contentProviderRecord);
        } else {
            getProvidersByClass(UserHandle.getUserId(contentProviderRecord.appInfo.uid)).put(componentName, contentProviderRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeProviderByName(String str, int i) {
        if (this.mSingletonByName.containsKey(str)) {
            this.mSingletonByName.remove(str);
            return;
        }
        if (i < 0) {
            throw new IllegalArgumentException("Bad user " + i);
        }
        HashMap<String, ContentProviderRecord> providersByName = getProvidersByName(i);
        providersByName.remove(str);
        if (providersByName.size() == 0) {
            this.mProvidersByNamePerUser.remove(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeProviderByClass(ComponentName componentName, int i) {
        if (this.mSingletonByClass.containsKey(componentName)) {
            this.mSingletonByClass.remove(componentName);
            return;
        }
        if (i < 0) {
            throw new IllegalArgumentException("Bad user " + i);
        }
        HashMap<ComponentName, ContentProviderRecord> providersByClass = getProvidersByClass(i);
        providersByClass.remove(componentName);
        if (providersByClass.size() == 0) {
            this.mProvidersByClassPerUser.remove(i);
        }
    }

    private HashMap<String, ContentProviderRecord> getProvidersByName(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Bad user " + i);
        }
        HashMap<String, ContentProviderRecord> hashMap = this.mProvidersByNamePerUser.get(i);
        if (hashMap != null) {
            return hashMap;
        }
        HashMap<String, ContentProviderRecord> hashMap2 = new HashMap<>();
        this.mProvidersByNamePerUser.put(i, hashMap2);
        return hashMap2;
    }

    HashMap<ComponentName, ContentProviderRecord> getProvidersByClass(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Bad user " + i);
        }
        HashMap<ComponentName, ContentProviderRecord> hashMap = this.mProvidersByClassPerUser.get(i);
        if (hashMap != null) {
            return hashMap;
        }
        HashMap<ComponentName, ContentProviderRecord> hashMap2 = new HashMap<>();
        this.mProvidersByClassPerUser.put(i, hashMap2);
        return hashMap2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0045, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean collectPackageProvidersLocked(String str, Set<String> set, boolean z, boolean z2, HashMap<ComponentName, ContentProviderRecord> hashMap, ArrayList<ContentProviderRecord> arrayList) {
        ProcessRecord processRecord;
        boolean z3 = false;
        for (ContentProviderRecord contentProviderRecord : hashMap.values()) {
            if ((str == null || (contentProviderRecord.info.packageName.equals(str) && (set == null || set.contains(contentProviderRecord.name.getClassName())))) && ((processRecord = contentProviderRecord.proc) == null || z2 || !processRecord.isPersistent())) {
                arrayList.add(contentProviderRecord);
                z3 = true;
            }
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean collectPackageProvidersLocked(String str, Set<String> set, boolean z, boolean z2, int i, ArrayList<ContentProviderRecord> arrayList) {
        boolean collectPackageProvidersLocked = (i == -1 || i == 0) ? collectPackageProvidersLocked(str, set, z, z2, this.mSingletonByClass, arrayList) : false;
        if (!z && collectPackageProvidersLocked) {
            return true;
        }
        if (i == -1) {
            for (int i2 = 0; i2 < this.mProvidersByClassPerUser.size(); i2++) {
                if (collectPackageProvidersLocked(str, set, z, z2, this.mProvidersByClassPerUser.valueAt(i2), arrayList)) {
                    if (!z) {
                        return true;
                    }
                    collectPackageProvidersLocked = true;
                }
            }
            return collectPackageProvidersLocked;
        }
        HashMap<ComponentName, ContentProviderRecord> providersByClass = getProvidersByClass(i);
        return providersByClass != null ? collectPackageProvidersLocked | collectPackageProvidersLocked(str, set, z, z2, providersByClass, arrayList) : collectPackageProvidersLocked;
    }

    private boolean dumpProvidersByClassLocked(PrintWriter printWriter, boolean z, String str, String str2, boolean z2, HashMap<ComponentName, ContentProviderRecord> hashMap) {
        Iterator<Map.Entry<ComponentName, ContentProviderRecord>> it = hashMap.entrySet().iterator();
        boolean z3 = false;
        while (it.hasNext()) {
            ContentProviderRecord value = it.next().getValue();
            if (str == null || str.equals(value.appInfo.packageName)) {
                if (z2) {
                    printWriter.println("");
                    z2 = false;
                }
                if (str2 != null) {
                    printWriter.println(str2);
                    str2 = null;
                }
                printWriter.print("  * ");
                printWriter.println(value);
                value.dump(printWriter, "    ", z);
                z3 = true;
            }
        }
        return z3;
    }

    private boolean dumpProvidersByNameLocked(PrintWriter printWriter, String str, String str2, boolean z, HashMap<String, ContentProviderRecord> hashMap) {
        boolean z2 = false;
        for (Map.Entry<String, ContentProviderRecord> entry : hashMap.entrySet()) {
            ContentProviderRecord value = entry.getValue();
            if (str == null || str.equals(value.appInfo.packageName)) {
                if (z) {
                    printWriter.println("");
                    z = false;
                }
                if (str2 != null) {
                    printWriter.println(str2);
                    str2 = null;
                }
                printWriter.print("  ");
                printWriter.print(entry.getKey());
                printWriter.print(": ");
                printWriter.println(value.toShortString());
                z2 = true;
            }
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean dumpProvidersLocked(PrintWriter printWriter, boolean z, String str) {
        boolean dumpProvidersByClassLocked = this.mSingletonByClass.size() > 0 ? dumpProvidersByClassLocked(printWriter, z, str, "  Published single-user content providers (by class):", false, this.mSingletonByClass) | false : false;
        for (int i = 0; i < this.mProvidersByClassPerUser.size(); i++) {
            dumpProvidersByClassLocked |= dumpProvidersByClassLocked(printWriter, z, str, "  Published user " + this.mProvidersByClassPerUser.keyAt(i) + " content providers (by class):", dumpProvidersByClassLocked, this.mProvidersByClassPerUser.valueAt(i));
        }
        if (z) {
            dumpProvidersByClassLocked = dumpProvidersByNameLocked(printWriter, str, "  Single-user authority to provider mappings:", dumpProvidersByClassLocked, this.mSingletonByName) | dumpProvidersByClassLocked;
            for (int i2 = 0; i2 < this.mProvidersByNamePerUser.size(); i2++) {
                dumpProvidersByClassLocked |= dumpProvidersByNameLocked(printWriter, str, "  User " + this.mProvidersByNamePerUser.keyAt(i2) + " authority to provider mappings:", dumpProvidersByClassLocked, this.mProvidersByNamePerUser.valueAt(i2));
            }
        }
        return dumpProvidersByClassLocked;
    }

    private ArrayList<ContentProviderRecord> getProvidersForName(String str) {
        ArrayList arrayList = new ArrayList();
        ArrayList<ContentProviderRecord> arrayList2 = new ArrayList<>();
        Predicate filterRecord = DumpUtils.filterRecord(str);
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                arrayList.addAll(this.mSingletonByClass.values());
                for (int i = 0; i < this.mProvidersByClassPerUser.size(); i++) {
                    arrayList.addAll(this.mProvidersByClassPerUser.valueAt(i).values());
                }
                CollectionUtils.addIf(arrayList, arrayList2, filterRecord);
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
        arrayList2.sort(Comparator.comparing(new Function() { // from class: com.android.server.am.ProviderMap$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((ContentProviderRecord) obj).getComponentName();
            }
        }));
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dumpProvider(FileDescriptor fileDescriptor, PrintWriter printWriter, String str, String[] strArr, int i, boolean z) {
        try {
            int i2 = 0;
            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(false);
            ArrayList<ContentProviderRecord> providersForName = getProvidersForName(str);
            if (providersForName.size() <= 0) {
                return false;
            }
            boolean z2 = false;
            while (i2 < providersForName.size()) {
                if (z2) {
                    printWriter.println();
                }
                dumpProvider("", fileDescriptor, printWriter, providersForName.get(i2), strArr, z);
                i2++;
                z2 = true;
            }
            return true;
        } finally {
            this.mAm.mOomAdjuster.mCachedAppOptimizer.enableFreezer(true);
        }
    }

    private void dumpProvider(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, ContentProviderRecord contentProviderRecord, String[] strArr, boolean z) {
        ProcessRecord processRecord = contentProviderRecord.proc;
        IApplicationThread thread = processRecord != null ? processRecord.getThread() : null;
        for (String str2 : strArr) {
            if (!z && str2.contains("--proto")) {
                if (thread != null) {
                    dumpToTransferPipe(null, fileDescriptor, printWriter, contentProviderRecord, thread, strArr);
                    return;
                }
                return;
            }
        }
        String str3 = str + "  ";
        ActivityManagerService activityManagerService = this.mAm;
        ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                printWriter.print(str);
                printWriter.print("PROVIDER ");
                printWriter.print(contentProviderRecord);
                printWriter.print(" pid=");
                ProcessRecord processRecord2 = contentProviderRecord.proc;
                if (processRecord2 != null) {
                    printWriter.println(processRecord2.getPid());
                } else {
                    printWriter.println("(not running)");
                }
                if (z) {
                    contentProviderRecord.dump(printWriter, str3, true);
                }
            } catch (Throwable th) {
                ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        ActivityManagerService.resetPriorityAfterLockedSection();
        if (thread != null) {
            printWriter.println("    Client:");
            printWriter.flush();
            dumpToTransferPipe("      ", fileDescriptor, printWriter, contentProviderRecord, thread, strArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean dumpProviderProto(FileDescriptor fileDescriptor, PrintWriter printWriter, String str, String[] strArr) {
        IApplicationThread thread;
        String[] strArr2 = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
        strArr2[strArr.length] = "--proto";
        ArrayList<ContentProviderRecord> providersForName = getProvidersForName(str);
        if (providersForName.size() <= 0) {
            return false;
        }
        for (int i = 0; i < providersForName.size(); i++) {
            ContentProviderRecord contentProviderRecord = providersForName.get(i);
            ProcessRecord processRecord = contentProviderRecord.proc;
            if (processRecord != null && (thread = processRecord.getThread()) != null) {
                dumpToTransferPipe(null, fileDescriptor, printWriter, contentProviderRecord, thread, strArr2);
                return true;
            }
        }
        return false;
    }

    private void dumpToTransferPipe(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, ContentProviderRecord contentProviderRecord, IApplicationThread iApplicationThread, String[] strArr) {
        try {
            TransferPipe transferPipe = new TransferPipe();
            try {
                iApplicationThread.dumpProvider(transferPipe.getWriteFd(), contentProviderRecord.provider.asBinder(), strArr);
                transferPipe.setBufferPrefix(str);
                transferPipe.go(fileDescriptor, 2000L);
                transferPipe.kill();
            } catch (Throwable th) {
                transferPipe.kill();
                throw th;
            }
        } catch (RemoteException unused) {
            printWriter.println("      Got a RemoteException while dumping the service");
        } catch (IOException e) {
            printWriter.println("      Failure while dumping the provider: " + e);
        }
    }
}
