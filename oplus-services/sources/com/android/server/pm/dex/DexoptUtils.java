package com.android.server.pm.dex;

import android.content.pm.SharedLibraryInfo;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.os.ClassLoaderFactory;
import com.android.internal.util.ArrayUtils;
import com.android.server.pm.dex.IDexoptUtilsExt;
import com.android.server.pm.pkg.AndroidPackage;
import java.io.File;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DexoptUtils {
    private static final String TAG = "DexoptUtils";
    private static final String SHARED_LIBRARY_LOADER_TYPE = ClassLoaderFactory.getPathClassLoaderName();
    private static IDexoptUtilsExt.IStaticExt mIDexoptUtilsExt = (IDexoptUtilsExt.IStaticExt) ExtLoader.type(IDexoptUtilsExt.IStaticExt.class).create();

    private DexoptUtils() {
    }

    public static String[] getClassLoaderContexts(AndroidPackage androidPackage, List<SharedLibraryInfo> list, boolean[] zArr) {
        String encodeSharedLibraries = list != null ? encodeSharedLibraries(list) : "";
        String encodeClassLoader = encodeClassLoader(androidPackage != null ? mIDexoptUtilsExt.getClassLoaderContext(androidPackage.getPackageName(), androidPackage.getBaseApkPath()) : "", androidPackage.getClassLoaderName(), encodeSharedLibraries);
        if (ArrayUtils.isEmpty(androidPackage.getSplitCodePaths())) {
            return new String[]{encodeClassLoader};
        }
        String[] splitRelativeCodePaths = getSplitRelativeCodePaths(androidPackage);
        String name = new File(androidPackage.getBaseApkPath()).getName();
        int i = 1;
        int length = splitRelativeCodePaths.length + 1;
        String[] strArr = new String[length];
        if (!zArr[0]) {
            encodeClassLoader = null;
        }
        strArr[0] = encodeClassLoader;
        SparseArray<int[]> splitDependencies = androidPackage.getSplitDependencies();
        if (!androidPackage.isIsolatedSplitLoading() || splitDependencies == null || splitDependencies.size() == 0) {
            while (i < length) {
                if (zArr[i]) {
                    strArr[i] = encodeClassLoader(name, androidPackage.getClassLoaderName(), encodeSharedLibraries);
                } else {
                    strArr[i] = null;
                }
                name = encodeClasspath(name, splitRelativeCodePaths[i - 1]);
                i++;
            }
        } else {
            String[] strArr2 = new String[splitRelativeCodePaths.length];
            for (int i2 = 0; i2 < splitRelativeCodePaths.length; i2++) {
                strArr2[i2] = encodeClassLoader(splitRelativeCodePaths[i2], androidPackage.getSplitClassLoaderNames()[i2]);
            }
            String encodeClassLoader2 = encodeClassLoader(name, androidPackage.getClassLoaderName());
            for (int i3 = 1; i3 < splitDependencies.size(); i3++) {
                int keyAt = splitDependencies.keyAt(i3);
                if (zArr[keyAt]) {
                    getParentDependencies(keyAt, strArr2, splitDependencies, strArr, encodeClassLoader2);
                }
            }
            while (i < length) {
                String encodeClassLoader3 = encodeClassLoader("", androidPackage.getSplitClassLoaderNames()[i - 1]);
                if (zArr[i]) {
                    if (strArr[i] != null) {
                        encodeClassLoader3 = encodeClassLoaderChain(encodeClassLoader3, strArr[i]) + encodeSharedLibraries;
                    }
                    strArr[i] = encodeClassLoader3;
                } else {
                    strArr[i] = null;
                }
                i++;
            }
        }
        return strArr;
    }

    public static String getClassLoaderContext(SharedLibraryInfo sharedLibraryInfo) {
        return encodeClassLoader("", SHARED_LIBRARY_LOADER_TYPE, sharedLibraryInfo.getDependencies() != null ? encodeSharedLibraries(sharedLibraryInfo.getDependencies()) : "");
    }

    private static String getParentDependencies(int i, String[] strArr, SparseArray<int[]> sparseArray, String[] strArr2, String str) {
        if (i == 0) {
            return str;
        }
        String str2 = strArr2[i];
        if (str2 != null) {
            return str2;
        }
        int i2 = sparseArray.get(i)[0];
        String parentDependencies = getParentDependencies(i2, strArr, sparseArray, strArr2, str);
        if (i2 != 0) {
            parentDependencies = encodeClassLoaderChain(strArr[i2 - 1], parentDependencies);
        }
        strArr2[i] = parentDependencies;
        return parentDependencies;
    }

    private static String encodeSharedLibrary(SharedLibraryInfo sharedLibraryInfo) {
        List allCodePaths = sharedLibraryInfo.getAllCodePaths();
        String encodeClassLoader = encodeClassLoader(encodeClasspath((String[]) allCodePaths.toArray(new String[allCodePaths.size()])), SHARED_LIBRARY_LOADER_TYPE);
        if (sharedLibraryInfo.getDependencies() == null) {
            return encodeClassLoader;
        }
        return encodeClassLoader + encodeSharedLibraries(sharedLibraryInfo.getDependencies());
    }

    private static String encodeSharedLibraries(List<SharedLibraryInfo> list) {
        String str = "{";
        boolean z = true;
        for (SharedLibraryInfo sharedLibraryInfo : list) {
            if (!z) {
                str = str + "#";
            }
            str = str + encodeSharedLibrary(sharedLibraryInfo);
            z = false;
        }
        return str + "}";
    }

    private static String encodeClasspath(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            if (sb.length() != 0) {
                sb.append(":");
            }
            sb.append(str);
        }
        return sb.toString();
    }

    private static String encodeClasspath(String str, String str2) {
        if (str.isEmpty()) {
            return str2;
        }
        return str + ":" + str2;
    }

    static String encodeClassLoader(String str, String str2) {
        str.getClass();
        if (ClassLoaderFactory.isPathClassLoaderName(str2)) {
            str2 = "PCL";
        } else if (ClassLoaderFactory.isDelegateLastClassLoaderName(str2)) {
            str2 = "DLC";
        } else {
            Slog.wtf(TAG, "Unsupported classLoaderName: " + str2);
        }
        return str2 + "[" + str + "]";
    }

    private static String encodeClassLoader(String str, String str2, String str3) {
        return encodeClassLoader(str, str2) + str3;
    }

    static String encodeClassLoaderChain(String str, String str2) {
        if (str.isEmpty()) {
            return str2;
        }
        if (str2.isEmpty()) {
            return str;
        }
        return str + ";" + str2;
    }

    static String[] processContextForDexLoad(List<String> list, List<String> list2) {
        if (list.size() != list2.size()) {
            throw new IllegalArgumentException("The size of the class loader names and the dex paths do not match.");
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty classLoadersNames");
        }
        String str = "";
        String str2 = "";
        for (int i = 1; i < list.size(); i++) {
            if (!ClassLoaderFactory.isValidClassLoaderName(list.get(i)) || list2.get(i) == null) {
                return null;
            }
            str2 = encodeClassLoaderChain(str2, encodeClassLoader(encodeClasspath(list2.get(i).split(File.pathSeparator)), list.get(i)));
        }
        String str3 = list.get(0);
        if (!ClassLoaderFactory.isValidClassLoaderName(str3)) {
            return null;
        }
        String[] split = list2.get(0).split(File.pathSeparator);
        String[] strArr = new String[split.length];
        for (int i2 = 0; i2 < split.length; i2++) {
            String str4 = split[i2];
            strArr[i2] = encodeClassLoaderChain(encodeClassLoader(str, str3), str2);
            str = encodeClasspath(str, str4);
        }
        return strArr;
    }

    private static String[] getSplitRelativeCodePaths(AndroidPackage androidPackage) {
        String parent = new File(androidPackage.getBaseApkPath()).getParent();
        String[] splitCodePaths = androidPackage.getSplitCodePaths();
        int size = ArrayUtils.size(splitCodePaths);
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            File file = new File(splitCodePaths[i]);
            strArr[i] = file.getName();
            String parent2 = file.getParent();
            if (!parent2.equals(parent)) {
                Slog.wtf(TAG, "Split paths have different base paths: " + parent2 + " and " + parent);
            }
        }
        return strArr;
    }
}
