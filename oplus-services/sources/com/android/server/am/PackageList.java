package com.android.server.am;

import android.content.pm.VersionedPackage;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.procstats.ProcessStats;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PackageList {
    private final ArrayMap<String, ProcessStats.ProcessStateHolder> mPkgList = new ArrayMap<>();
    private final ProcessRecord mProcess;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageList(ProcessRecord processRecord) {
        this.mProcess = processRecord;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessStats.ProcessStateHolder put(String str, ProcessStats.ProcessStateHolder processStateHolder) {
        ProcessStats.ProcessStateHolder put;
        synchronized (this) {
            this.mProcess.getWindowProcessController().addPackage(str);
            put = this.mPkgList.put(str, processStateHolder);
        }
        return put;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        synchronized (this) {
            this.mPkgList.clear();
            this.mProcess.getWindowProcessController().clearPackageList();
        }
    }

    public int size() {
        int size;
        synchronized (this) {
            size = this.mPkgList.size();
        }
        return size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsKey(Object obj) {
        boolean containsKey;
        synchronized (this) {
            containsKey = this.mPkgList.containsKey(obj);
        }
        return containsKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProcessStats.ProcessStateHolder get(String str) {
        ProcessStats.ProcessStateHolder processStateHolder;
        synchronized (this) {
            processStateHolder = this.mPkgList.get(str);
        }
        return processStateHolder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachPackage(Consumer<String> consumer) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                consumer.accept(this.mPkgList.keyAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachPackage(BiConsumer<String, ProcessStats.ProcessStateHolder> biConsumer) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                biConsumer.accept(this.mPkgList.keyAt(i), this.mPkgList.valueAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <R> R searchEachPackage(Function<String, R> function) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                R apply = function.apply(this.mPkgList.keyAt(i));
                if (apply != null) {
                    return apply;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachPackageProcessStats(Consumer<ProcessStats.ProcessStateHolder> consumer) {
        synchronized (this) {
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                consumer.accept(this.mPkgList.valueAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"this"})
    public ArrayMap<String, ProcessStats.ProcessStateHolder> getPackageListLocked() {
        return this.mPkgList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getPackageList() {
        synchronized (this) {
            int size = this.mPkgList.size();
            if (size == 0) {
                return null;
            }
            String[] strArr = new String[size];
            for (int i = 0; i < size; i++) {
                strArr[i] = this.mPkgList.keyAt(i);
            }
            return strArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<VersionedPackage> getPackageListWithVersionCode() {
        synchronized (this) {
            int size = this.mPkgList.size();
            if (size == 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < size; i++) {
                arrayList.add(new VersionedPackage(this.mPkgList.keyAt(i), this.mPkgList.valueAt(i).appVersion));
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        synchronized (this) {
            printWriter.print(str);
            printWriter.print("packageList={");
            int size = this.mPkgList.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    printWriter.print(", ");
                }
                printWriter.print(this.mPkgList.keyAt(i));
            }
            printWriter.println("}");
        }
    }

    public String keyAt(int i) {
        String keyAt;
        synchronized (this) {
            keyAt = this.mPkgList.keyAt(i);
        }
        return keyAt;
    }
}
