package com.android.server.pm.dex;

import android.os.Build;
import android.util.AtomicFile;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FastPrintWriter;
import com.android.server.pm.AbstractStatsBase;
import com.android.server.pm.PackageManagerServiceUtils;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageDexUsage extends AbstractStatsBase<Void> {
    private static final String CODE_PATH_LINE_CHAR = "+";
    private static final String DEX_LINE_CHAR = "#";
    private static final String LOADING_PACKAGE_CHAR = "@";

    @VisibleForTesting
    static final int MAX_SECONDARY_FILES_PER_OWNER = 100;
    private static final int PACKAGE_DEX_USAGE_VERSION = 2;
    private static final String PACKAGE_DEX_USAGE_VERSION_HEADER = "PACKAGE_MANAGER__PACKAGE_DEX_USAGE__";
    private static final String SPLIT_CHAR = ",";
    private static final String TAG = "PackageDexUsage";
    static final String UNSUPPORTED_CLASS_LOADER_CONTEXT = "=UnsupportedClassLoaderContext=";
    static final String VARIABLE_CLASS_LOADER_CONTEXT = "=VariableClassLoaderContext=";

    @GuardedBy({"mPackageUseInfoMap"})
    private final Map<String, PackageUseInfo> mPackageUseInfoMap;

    private boolean isSupportedVersion(int i) {
        return i == 2;
    }

    private String writeBoolean(boolean z) {
        return z ? "1" : "0";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageDexUsage() {
        super("package-dex-usage.list", "PackageDexUsage_DiskWriter", false);
        this.mPackageUseInfoMap = new HashMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean record(String str, String str2, int i, String str3, boolean z, String str4, String str5, boolean z2) {
        if (!PackageManagerServiceUtils.checkISA(str3)) {
            throw new IllegalArgumentException("loaderIsa " + str3 + " is unsupported");
        }
        if (str5 == null) {
            throw new IllegalArgumentException("Null classLoaderContext");
        }
        if (str5.equals(UNSUPPORTED_CLASS_LOADER_CONTEXT)) {
            Slog.e(TAG, "Unsupported context?");
            return false;
        }
        boolean z3 = !str.equals(str4);
        synchronized (this.mPackageUseInfoMap) {
            PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(str);
            if (packageUseInfo == null) {
                PackageUseInfo packageUseInfo2 = new PackageUseInfo(str);
                if (z) {
                    packageUseInfo2.mergePrimaryCodePaths(str2, str4);
                } else {
                    DexUseInfo dexUseInfo = new DexUseInfo(z3, i, str5, str3);
                    packageUseInfo2.mDexUseInfoMap.put(str2, dexUseInfo);
                    maybeAddLoadingPackage(str, str4, dexUseInfo.mLoadingPackages);
                }
                this.mPackageUseInfoMap.put(str, packageUseInfo2);
                return true;
            }
            if (z) {
                return packageUseInfo.mergePrimaryCodePaths(str2, str4);
            }
            DexUseInfo dexUseInfo2 = new DexUseInfo(z3, i, str5, str3);
            boolean maybeAddLoadingPackage = maybeAddLoadingPackage(str, str4, dexUseInfo2.mLoadingPackages);
            DexUseInfo dexUseInfo3 = (DexUseInfo) packageUseInfo.mDexUseInfoMap.get(str2);
            if (dexUseInfo3 == null) {
                if (packageUseInfo.mDexUseInfoMap.size() >= 100) {
                    return maybeAddLoadingPackage;
                }
                packageUseInfo.mDexUseInfoMap.put(str2, dexUseInfo2);
                return true;
            }
            if (i != dexUseInfo3.mOwnerUserId) {
                throw new IllegalArgumentException("Trying to change ownerUserId for  dex path " + str2 + " from " + dexUseInfo3.mOwnerUserId + " to " + i);
            }
            return dexUseInfo3.merge(dexUseInfo2, z2) || maybeAddLoadingPackage;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void read() {
        read((PackageDexUsage) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void maybeWriteAsync() {
        maybeWriteAsync(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeNow() {
        writeInternal((Void) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void writeInternal(Void r3) {
        FileOutputStream fileOutputStream;
        AtomicFile file = getFile();
        try {
            fileOutputStream = file.startWrite();
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                write(outputStreamWriter);
                outputStreamWriter.flush();
                file.finishWrite(fileOutputStream);
            } catch (IOException e) {
                e = e;
                if (fileOutputStream != null) {
                    file.failWrite(fileOutputStream);
                }
                Slog.e(TAG, "Failed to write usage for dex files", e);
            }
        } catch (IOException e2) {
            e = e2;
            fileOutputStream = null;
        }
    }

    void write(Writer writer) {
        Map<String, PackageUseInfo> clonePackageUseInfoMap = clonePackageUseInfoMap();
        FastPrintWriter fastPrintWriter = new FastPrintWriter(writer);
        fastPrintWriter.print(PACKAGE_DEX_USAGE_VERSION_HEADER);
        fastPrintWriter.println(2);
        for (Map.Entry<String, PackageUseInfo> entry : clonePackageUseInfoMap.entrySet()) {
            String key = entry.getKey();
            PackageUseInfo value = entry.getValue();
            fastPrintWriter.println(key);
            for (Map.Entry entry2 : value.mPrimaryCodePaths.entrySet()) {
                String str = (String) entry2.getKey();
                Set set = (Set) entry2.getValue();
                fastPrintWriter.println(CODE_PATH_LINE_CHAR + str);
                fastPrintWriter.println(LOADING_PACKAGE_CHAR + String.join(SPLIT_CHAR, set));
            }
            for (Map.Entry entry3 : value.mDexUseInfoMap.entrySet()) {
                String str2 = (String) entry3.getKey();
                DexUseInfo dexUseInfo = (DexUseInfo) entry3.getValue();
                fastPrintWriter.println(DEX_LINE_CHAR + str2);
                fastPrintWriter.print(String.join(SPLIT_CHAR, Integer.toString(dexUseInfo.mOwnerUserId), writeBoolean(dexUseInfo.mIsUsedByOtherApps)));
                Iterator it = dexUseInfo.mLoaderIsas.iterator();
                while (it.hasNext()) {
                    fastPrintWriter.print(SPLIT_CHAR + ((String) it.next()));
                }
                fastPrintWriter.println();
                fastPrintWriter.println(LOADING_PACKAGE_CHAR + String.join(SPLIT_CHAR, dexUseInfo.mLoadingPackages));
                fastPrintWriter.println(dexUseInfo.getClassLoaderContext());
            }
        }
        fastPrintWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.pm.AbstractStatsBase
    public void readInternal(Void r4) {
        BufferedReader bufferedReader = null;
        try {
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(getFile().openRead()));
                try {
                    read((Reader) bufferedReader2);
                    IoUtils.closeQuietly(bufferedReader2);
                } catch (FileNotFoundException unused) {
                    bufferedReader = bufferedReader2;
                    IoUtils.closeQuietly(bufferedReader);
                } catch (IOException e) {
                    e = e;
                    bufferedReader = bufferedReader2;
                    Slog.w(TAG, "Failed to parse package dex usage.", e);
                    IoUtils.closeQuietly(bufferedReader);
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    IoUtils.closeQuietly(bufferedReader);
                    throw th;
                }
            } catch (FileNotFoundException unused2) {
                IoUtils.closeQuietly(bufferedReader);
            } catch (IOException e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    void read(Reader reader) throws IOException {
        HashMap hashMap = new HashMap();
        BufferedReader bufferedReader = new BufferedReader(reader);
        String readLine = bufferedReader.readLine();
        if (readLine == null) {
            throw new IllegalStateException("No version line found.");
        }
        if (!readLine.startsWith(PACKAGE_DEX_USAGE_VERSION_HEADER)) {
            throw new IllegalStateException("Invalid version line: " + readLine);
        }
        int parseInt = Integer.parseInt(readLine.substring(36));
        if (!isSupportedVersion(parseInt)) {
            Slog.w(TAG, "Unexpected package-dex-use version: " + parseInt + ". Not reading from it");
            return;
        }
        HashSet hashSet = new HashSet();
        for (String str : Build.SUPPORTED_ABIS) {
            hashSet.add(VMRuntime.getInstructionSet(str));
        }
        String str2 = null;
        String str3 = null;
        PackageUseInfo packageUseInfo = null;
        while (true) {
            String readLine2 = bufferedReader.readLine();
            if (readLine2 != null) {
                if (readLine2.startsWith(DEX_LINE_CHAR)) {
                    if (str3 == null) {
                        throw new IllegalStateException("Malformed PackageDexUsage file. Expected package line before dex line.");
                    }
                    String substring = readLine2.substring(1);
                    String readLine3 = bufferedReader.readLine();
                    if (readLine3 == null) {
                        throw new IllegalStateException("Could not find dexUseInfo line");
                    }
                    String[] split = readLine3.split(SPLIT_CHAR);
                    if (split.length < 3) {
                        throw new IllegalStateException("Invalid PackageDexUsage line: " + readLine3);
                    }
                    Set<String> readLoadingPackages = readLoadingPackages(bufferedReader, parseInt);
                    String readClassLoaderContext = readClassLoaderContext(bufferedReader, parseInt);
                    if (!UNSUPPORTED_CLASS_LOADER_CONTEXT.equals(readClassLoaderContext)) {
                        DexUseInfo dexUseInfo = new DexUseInfo(readBoolean(split[1]), Integer.parseInt(split[0]), readClassLoaderContext, str2);
                        dexUseInfo.mLoadingPackages.addAll(readLoadingPackages);
                        for (int i = 2; i < split.length; i++) {
                            String str4 = split[i];
                            if (hashSet.contains(str4)) {
                                dexUseInfo.mLoaderIsas.add(split[i]);
                            } else {
                                Slog.wtf(TAG, "Unsupported ISA when parsing PackageDexUsage: " + str4);
                            }
                        }
                        if (hashSet.isEmpty()) {
                            Slog.wtf(TAG, "Ignore dexPath when parsing PackageDexUsage because of unsupported isas. dexPath=" + substring);
                        } else {
                            packageUseInfo.mDexUseInfoMap.put(substring, dexUseInfo);
                        }
                    }
                } else if (readLine2.startsWith(CODE_PATH_LINE_CHAR)) {
                    packageUseInfo.mPrimaryCodePaths.put(readLine2.substring(1), readLoadingPackages(bufferedReader, parseInt));
                } else {
                    packageUseInfo = new PackageUseInfo(readLine2);
                    hashMap.put(readLine2, packageUseInfo);
                    str3 = readLine2;
                }
                str2 = null;
            } else {
                synchronized (this.mPackageUseInfoMap) {
                    this.mPackageUseInfoMap.clear();
                    this.mPackageUseInfoMap.putAll(hashMap);
                }
                return;
            }
        }
    }

    private String readClassLoaderContext(BufferedReader bufferedReader, int i) throws IOException {
        String readLine = bufferedReader.readLine();
        if (readLine != null) {
            return readLine;
        }
        throw new IllegalStateException("Could not find the classLoaderContext line.");
    }

    private Set<String> readLoadingPackages(BufferedReader bufferedReader, int i) throws IOException {
        String readLine = bufferedReader.readLine();
        if (readLine == null) {
            throw new IllegalStateException("Could not find the loadingPackages line.");
        }
        HashSet hashSet = new HashSet();
        if (readLine.length() != 1) {
            Collections.addAll(hashSet, readLine.substring(1).split(SPLIT_CHAR));
        }
        return hashSet;
    }

    private boolean maybeAddLoadingPackage(String str, String str2, Set<String> set) {
        return !str.equals(str2) && set.add(str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void syncData(Map<String, Set<Integer>> map, Map<String, Set<String>> map2, List<String> list) {
        synchronized (this.mPackageUseInfoMap) {
            Iterator<Map.Entry<String, PackageUseInfo>> it = this.mPackageUseInfoMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, PackageUseInfo> next = it.next();
                String key = next.getKey();
                if (!list.contains(key)) {
                    PackageUseInfo value = next.getValue();
                    Set<Integer> set = map.get(key);
                    if (set == null) {
                        it.remove();
                    } else {
                        Iterator it2 = value.mDexUseInfoMap.entrySet().iterator();
                        while (it2.hasNext()) {
                            if (!set.contains(Integer.valueOf(((DexUseInfo) ((Map.Entry) it2.next()).getValue()).mOwnerUserId))) {
                                it2.remove();
                            }
                        }
                        Set<String> set2 = map2.get(key);
                        Iterator it3 = value.mPrimaryCodePaths.entrySet().iterator();
                        while (it3.hasNext()) {
                            Map.Entry entry = (Map.Entry) it3.next();
                            if (!set2.contains((String) entry.getKey())) {
                                it3.remove();
                            } else {
                                Iterator it4 = ((Set) entry.getValue()).iterator();
                                while (it4.hasNext()) {
                                    String str = (String) it4.next();
                                    if (!list.contains(str) && !map.containsKey(str)) {
                                        it4.remove();
                                    }
                                }
                            }
                        }
                        if (!value.isAnyCodePathUsedByOtherApps() && value.mDexUseInfoMap.isEmpty()) {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean clearUsedByOtherApps(String str) {
        synchronized (this.mPackageUseInfoMap) {
            PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(str);
            if (packageUseInfo == null) {
                return false;
            }
            return packageUseInfo.clearCodePathUsedByOtherApps();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removePackage(String str) {
        boolean z;
        synchronized (this.mPackageUseInfoMap) {
            z = this.mPackageUseInfoMap.remove(str) != null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeUserPackage(String str, int i) {
        synchronized (this.mPackageUseInfoMap) {
            PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(str);
            boolean z = false;
            if (packageUseInfo == null) {
                return false;
            }
            Iterator it = packageUseInfo.mDexUseInfoMap.entrySet().iterator();
            while (it.hasNext()) {
                if (((DexUseInfo) ((Map.Entry) it.next()).getValue()).mOwnerUserId == i) {
                    it.remove();
                    z = true;
                }
            }
            if (packageUseInfo.mDexUseInfoMap.isEmpty() && !packageUseInfo.isAnyCodePathUsedByOtherApps()) {
                this.mPackageUseInfoMap.remove(str);
                z = true;
            }
            return z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeDexFile(String str, String str2, int i) {
        synchronized (this.mPackageUseInfoMap) {
            PackageUseInfo packageUseInfo = this.mPackageUseInfoMap.get(str);
            if (packageUseInfo == null) {
                return false;
            }
            return removeDexFile(packageUseInfo, str2, i);
        }
    }

    private boolean removeDexFile(PackageUseInfo packageUseInfo, String str, int i) {
        DexUseInfo dexUseInfo = (DexUseInfo) packageUseInfo.mDexUseInfoMap.get(str);
        if (dexUseInfo == null || dexUseInfo.mOwnerUserId != i) {
            return false;
        }
        packageUseInfo.mDexUseInfoMap.remove(str);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PackageUseInfo getPackageUseInfo(String str) {
        PackageUseInfo packageUseInfo;
        synchronized (this.mPackageUseInfoMap) {
            PackageUseInfo packageUseInfo2 = this.mPackageUseInfoMap.get(str);
            packageUseInfo = null;
            byte b = 0;
            if (packageUseInfo2 != null) {
                packageUseInfo = new PackageUseInfo(packageUseInfo2);
            }
        }
        return packageUseInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getAllPackagesWithSecondaryDexFiles() {
        HashSet hashSet = new HashSet();
        synchronized (this.mPackageUseInfoMap) {
            for (Map.Entry<String, PackageUseInfo> entry : this.mPackageUseInfoMap.entrySet()) {
                if (!entry.getValue().mDexUseInfoMap.isEmpty()) {
                    hashSet.add(entry.getKey());
                }
            }
        }
        return hashSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        synchronized (this.mPackageUseInfoMap) {
            this.mPackageUseInfoMap.clear();
        }
    }

    private Map<String, PackageUseInfo> clonePackageUseInfoMap() {
        HashMap hashMap = new HashMap();
        synchronized (this.mPackageUseInfoMap) {
            for (Map.Entry<String, PackageUseInfo> entry : this.mPackageUseInfoMap.entrySet()) {
                hashMap.put(entry.getKey(), new PackageUseInfo(entry.getValue()));
            }
        }
        return hashMap;
    }

    private boolean readBoolean(String str) {
        if ("0".equals(str)) {
            return false;
        }
        if ("1".equals(str)) {
            return true;
        }
        throw new IllegalArgumentException("Unknown bool encoding: " + str);
    }

    String dump() {
        StringWriter stringWriter = new StringWriter();
        write(stringWriter);
        return stringWriter.toString();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PackageUseInfo {
        private final Map<String, DexUseInfo> mDexUseInfoMap;
        private final String mPackageName;
        private final Map<String, Set<String>> mPrimaryCodePaths;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PackageUseInfo(String str) {
            this.mPrimaryCodePaths = new HashMap();
            this.mDexUseInfoMap = new HashMap();
            this.mPackageName = str;
        }

        private PackageUseInfo(PackageUseInfo packageUseInfo) {
            this.mPackageName = packageUseInfo.mPackageName;
            this.mPrimaryCodePaths = new HashMap();
            for (Map.Entry<String, Set<String>> entry : packageUseInfo.mPrimaryCodePaths.entrySet()) {
                this.mPrimaryCodePaths.put(entry.getKey(), new HashSet(entry.getValue()));
            }
            this.mDexUseInfoMap = new HashMap();
            for (Map.Entry<String, DexUseInfo> entry2 : packageUseInfo.mDexUseInfoMap.entrySet()) {
                this.mDexUseInfoMap.put(entry2.getKey(), new DexUseInfo(entry2.getValue()));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean mergePrimaryCodePaths(String str, String str2) {
            Set<String> set = this.mPrimaryCodePaths.get(str);
            if (set == null) {
                set = new HashSet<>();
                this.mPrimaryCodePaths.put(str, set);
            }
            return set.add(str2);
        }

        public boolean isUsedByOtherApps(String str) {
            if (!this.mPrimaryCodePaths.containsKey(str)) {
                return false;
            }
            Set<String> set = this.mPrimaryCodePaths.get(str);
            if (set.contains(this.mPackageName)) {
                return set.size() > 1;
            }
            return !set.isEmpty();
        }

        public Map<String, DexUseInfo> getDexUseInfoMap() {
            return this.mDexUseInfoMap;
        }

        public Set<String> getLoadingPackages(String str) {
            return this.mPrimaryCodePaths.getOrDefault(str, null);
        }

        public boolean isAnyCodePathUsedByOtherApps() {
            return !this.mPrimaryCodePaths.isEmpty();
        }

        boolean clearCodePathUsedByOtherApps() {
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(this.mPackageName);
            Iterator<Map.Entry<String, Set<String>>> it = this.mPrimaryCodePaths.entrySet().iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (it.next().getValue().retainAll(arrayList)) {
                    z = true;
                }
            }
            return z;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class DexUseInfo {
        private String mClassLoaderContext;
        private boolean mIsUsedByOtherApps;
        private final Set<String> mLoaderIsas;
        private final Set<String> mLoadingPackages;
        private final int mOwnerUserId;

        @VisibleForTesting
        DexUseInfo(boolean z, int i, String str, String str2) {
            this.mIsUsedByOtherApps = z;
            this.mOwnerUserId = i;
            this.mClassLoaderContext = str;
            HashSet hashSet = new HashSet();
            this.mLoaderIsas = hashSet;
            if (str2 != null) {
                hashSet.add(str2);
            }
            this.mLoadingPackages = new HashSet();
        }

        private DexUseInfo(DexUseInfo dexUseInfo) {
            this.mIsUsedByOtherApps = dexUseInfo.mIsUsedByOtherApps;
            this.mOwnerUserId = dexUseInfo.mOwnerUserId;
            this.mClassLoaderContext = dexUseInfo.mClassLoaderContext;
            this.mLoaderIsas = new HashSet(dexUseInfo.mLoaderIsas);
            this.mLoadingPackages = new HashSet(dexUseInfo.mLoadingPackages);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean merge(DexUseInfo dexUseInfo, boolean z) {
            boolean z2 = this.mIsUsedByOtherApps;
            this.mIsUsedByOtherApps = z2 || dexUseInfo.mIsUsedByOtherApps;
            boolean addAll = this.mLoaderIsas.addAll(dexUseInfo.mLoaderIsas);
            boolean addAll2 = this.mLoadingPackages.addAll(dexUseInfo.mLoadingPackages);
            String str = this.mClassLoaderContext;
            if (z) {
                this.mClassLoaderContext = dexUseInfo.mClassLoaderContext;
            } else if (isUnsupportedContext(str)) {
                this.mClassLoaderContext = dexUseInfo.mClassLoaderContext;
            } else if (!Objects.equals(this.mClassLoaderContext, dexUseInfo.mClassLoaderContext)) {
                this.mClassLoaderContext = PackageDexUsage.VARIABLE_CLASS_LOADER_CONTEXT;
            }
            return addAll || z2 != this.mIsUsedByOtherApps || addAll2 || !Objects.equals(str, this.mClassLoaderContext);
        }

        private static boolean isUnsupportedContext(String str) {
            return PackageDexUsage.UNSUPPORTED_CLASS_LOADER_CONTEXT.equals(str);
        }

        public boolean isUsedByOtherApps() {
            return this.mIsUsedByOtherApps;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getOwnerUserId() {
            return this.mOwnerUserId;
        }

        public Set<String> getLoaderIsas() {
            return this.mLoaderIsas;
        }

        public Set<String> getLoadingPackages() {
            return this.mLoadingPackages;
        }

        public String getClassLoaderContext() {
            return this.mClassLoaderContext;
        }

        public boolean isUnsupportedClassLoaderContext() {
            return isUnsupportedContext(this.mClassLoaderContext);
        }

        public boolean isVariableClassLoaderContext() {
            return PackageDexUsage.VARIABLE_CLASS_LOADER_CONTEXT.equals(this.mClassLoaderContext);
        }
    }
}
