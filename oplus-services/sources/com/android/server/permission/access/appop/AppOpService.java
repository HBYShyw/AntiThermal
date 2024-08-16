package com.android.server.permission.access.appop;

import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.appop.AppOpsCheckingServiceInterface;
import com.android.server.appop.OnOpModeChangedListener;
import com.android.server.permission.access.AccessCheckingService;
import com.android.server.permission.access.AccessState;
import com.android.server.permission.access.GetStateScope;
import com.android.server.permission.access.MutateStateScope;
import com.android.server.permission.access.SchemePolicy;
import com.android.server.permission.access.util.IntExtensions;
import com.android.server.permission.jarjar.kotlin.Unit;
import com.android.server.permission.jarjar.kotlin.collections.MutableCollections;
import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Ref$BooleanRef;
import java.io.PrintWriter;
import libcore.util.EmptyArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AppOpService.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class AppOpService implements AppOpsCheckingServiceInterface {

    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = AppOpService.class.getSimpleName();
    private final Context context;
    private Handler handler;
    private Object lock;

    @NotNull
    private final SparseArray<ArraySet<OnOpModeChangedListener>> opModeWatchers;

    @NotNull
    private final ArrayMap<String, ArraySet<OnOpModeChangedListener>> packageModeWatchers;

    @NotNull
    private final PackageAppOpPolicy packagePolicy;

    @NotNull
    private final AccessCheckingService service;
    private SparseArray<int[]> switchedOps;

    @NotNull
    private final UidAppOpPolicy uidPolicy;

    public void clearAllModes() {
    }

    public void readState() {
    }

    @VisibleForTesting
    public void shutdown() {
    }

    public void systemReady() {
    }

    @VisibleForTesting
    public void writeState() {
    }

    public AppOpService(@NotNull AccessCheckingService accessCheckingService) {
        this.service = accessCheckingService;
        SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission = accessCheckingService.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission("package", "app-op");
        Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission, "null cannot be cast to non-null type com.android.server.permission.access.appop.PackageAppOpPolicy");
        this.packagePolicy = (PackageAppOpPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission;
        SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission2 = accessCheckingService.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission("uid", "app-op");
        Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission2, "null cannot be cast to non-null type com.android.server.permission.access.appop.UidAppOpPolicy");
        this.uidPolicy = (UidAppOpPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission2;
        this.context = accessCheckingService.getContext();
        this.opModeWatchers = new SparseArray<>();
        this.packageModeWatchers = new ArrayMap<>();
    }

    private final SparseBooleanArray evalForegroundOps(ArrayMap<String, Integer> arrayMap, SparseBooleanArray sparseBooleanArray) {
        if (arrayMap != null) {
            int size = arrayMap.size();
            for (int i = 0; i < size; i++) {
                String keyAt = arrayMap.keyAt(i);
                if (arrayMap.valueAt(i).intValue() == 4) {
                    if (sparseBooleanArray == null) {
                        sparseBooleanArray = new SparseBooleanArray();
                    }
                    evalForegroundWatchers(keyAt, sparseBooleanArray);
                }
            }
        }
        return sparseBooleanArray;
    }

    public final void initialize() {
        this.handler = new Handler(this.context.getMainLooper());
        this.lock = new Object();
        this.switchedOps = new SparseArray<>();
        for (int i = 0; i < 136; i++) {
            int opToSwitch = AppOpsManager.opToSwitch(i);
            SparseArray<int[]> sparseArray = this.switchedOps;
            SparseArray<int[]> sparseArray2 = null;
            if (sparseArray == null) {
                Intrinsics.throwUninitializedPropertyAccessException("switchedOps");
                sparseArray = null;
            }
            SparseArray<int[]> sparseArray3 = this.switchedOps;
            if (sparseArray3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("switchedOps");
            } else {
                sparseArray2 = sparseArray3;
            }
            sparseArray.put(opToSwitch, ArrayUtils.appendInt(sparseArray2.get(opToSwitch), i));
        }
    }

    @NotNull
    public SparseIntArray getNonDefaultUidModes(int i) {
        return opNameMapToOpIntMap(getUidModes(i));
    }

    @NotNull
    public SparseIntArray getNonDefaultPackageModes(@NotNull String str, int i) {
        return opNameMapToOpIntMap(getPackageModes(str, i));
    }

    public int getUidMode(int i, int i2) {
        int appId = UserHandle.getAppId(i);
        int userId = UserHandle.getUserId(i);
        String opToPublicName = AppOpsManager.opToPublicName(i2);
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        return this.uidPolicy.getAppOpMode(new GetStateScope(accessState), appId, userId, opToPublicName);
    }

    private final ArrayMap<String, Integer> getUidModes(int i) {
        int appId = UserHandle.getAppId(i);
        int userId = UserHandle.getUserId(i);
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        return this.uidPolicy.getAppOpModes(new GetStateScope(accessState), appId, userId);
    }

    public boolean setUidMode(int i, int i2, int i3) {
        int appId = UserHandle.getAppId(i);
        int userId = UserHandle.getUserId(i);
        String opToPublicName = AppOpsManager.opToPublicName(i2);
        Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
        AccessCheckingService accessCheckingService = this.service;
        synchronized (accessCheckingService.stateLock) {
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            AccessState copy = accessState.copy();
            ref$BooleanRef.element = Boolean.valueOf(this.uidPolicy.setAppOpMode(new MutateStateScope(accessState, copy), appId, userId, opToPublicName, i3)).booleanValue();
            accessCheckingService.persistence.write(copy);
            accessCheckingService.state = copy;
            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
            Unit unit = Unit.INSTANCE;
        }
        return ref$BooleanRef.element;
    }

    public int getPackageMode(@NotNull String str, int i, int i2) {
        String opToPublicName = AppOpsManager.opToPublicName(i);
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        return this.packagePolicy.getAppOpMode(new GetStateScope(accessState), str, i2, opToPublicName);
    }

    private final ArrayMap<String, Integer> getPackageModes(String str, int i) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        return this.packagePolicy.getAppOpModes(new GetStateScope(accessState), str, i);
    }

    public void setPackageMode(@NotNull String str, int i, int i2, int i3) {
        String opToPublicName = AppOpsManager.opToPublicName(i);
        AccessCheckingService accessCheckingService = this.service;
        synchronized (accessCheckingService.stateLock) {
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            AccessState copy = accessState.copy();
            this.packagePolicy.setAppOpMode(new MutateStateScope(accessState, copy), str, i3, opToPublicName, i2);
            accessCheckingService.persistence.write(copy);
            accessCheckingService.state = copy;
            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
            Unit unit = Unit.INSTANCE;
        }
    }

    public void removeUid(int i) {
        int appId = UserHandle.getAppId(i);
        int userId = UserHandle.getUserId(i);
        AccessCheckingService accessCheckingService = this.service;
        synchronized (accessCheckingService.stateLock) {
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            AccessState copy = accessState.copy();
            this.uidPolicy.removeAppOpModes(new MutateStateScope(accessState, copy), appId, userId);
            accessCheckingService.persistence.write(copy);
            accessCheckingService.state = copy;
            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
            Unit unit = Unit.INSTANCE;
        }
    }

    public boolean removePackage(@NotNull String str, int i) {
        Ref$BooleanRef ref$BooleanRef = new Ref$BooleanRef();
        AccessCheckingService accessCheckingService = this.service;
        synchronized (accessCheckingService.stateLock) {
            AccessState accessState = accessCheckingService.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            AccessState copy = accessState.copy();
            ref$BooleanRef.element = Boolean.valueOf(this.packagePolicy.removeAppOpModes(new MutateStateScope(accessState, copy), str, i)).booleanValue();
            accessCheckingService.persistence.write(copy);
            accessCheckingService.state = copy;
            accessCheckingService.policy.onStateMutated(new GetStateScope(copy));
            Unit unit = Unit.INSTANCE;
        }
        return ref$BooleanRef.element;
    }

    private final SparseIntArray opNameMapToOpIntMap(ArrayMap<String, Integer> arrayMap) {
        if (arrayMap == null) {
            return new SparseIntArray();
        }
        SparseIntArray sparseIntArray = new SparseIntArray(arrayMap.size());
        int size = arrayMap.size();
        for (int i = 0; i < size; i++) {
            String keyAt = arrayMap.keyAt(i);
            sparseIntArray.put(AppOpsManager.strOpToOp(keyAt), arrayMap.valueAt(i).intValue());
        }
        return sparseIntArray;
    }

    public boolean areUidModesDefault(int i) {
        ArrayMap<String, Integer> uidModes = getUidModes(i);
        return uidModes == null || uidModes.isEmpty();
    }

    public boolean arePackageModesDefault(@NotNull String str, int i) {
        AccessState accessState = this.service.state;
        if (accessState == null) {
            Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        new GetStateScope(accessState);
        ArrayMap<String, Integer> packageModes = getPackageModes(str, i);
        return packageModes == null || packageModes.isEmpty();
    }

    public void startWatchingOpModeChanged(@NotNull OnOpModeChangedListener onOpModeChangedListener, int i) {
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            SparseArray<ArraySet<OnOpModeChangedListener>> sparseArray = this.opModeWatchers;
            ArraySet<OnOpModeChangedListener> arraySet = sparseArray.get(i);
            if (arraySet == null) {
                arraySet = new ArraySet<>();
                sparseArray.put(i, arraySet);
            }
            arraySet.add(onOpModeChangedListener);
            Unit unit = Unit.INSTANCE;
        }
    }

    public void startWatchingPackageModeChanged(@NotNull OnOpModeChangedListener onOpModeChangedListener, @NotNull String str) {
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            ArrayMap<String, ArraySet<OnOpModeChangedListener>> arrayMap = this.packageModeWatchers;
            ArraySet<OnOpModeChangedListener> arraySet = arrayMap.get(str);
            if (arraySet == null) {
                arraySet = new ArraySet<>();
                arrayMap.put(str, arraySet);
            }
            arraySet.add(onOpModeChangedListener);
            Unit unit = Unit.INSTANCE;
        }
    }

    public void removeListener(@NotNull OnOpModeChangedListener onOpModeChangedListener) {
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            SparseArray<ArraySet<OnOpModeChangedListener>> sparseArray = this.opModeWatchers;
            for (int size = sparseArray.size() - 1; -1 < size; size--) {
                sparseArray.keyAt(size);
                ArraySet<OnOpModeChangedListener> valueAt = sparseArray.valueAt(size);
                valueAt.remove(onOpModeChangedListener);
                if (valueAt.isEmpty()) {
                    sparseArray.removeAt(size);
                }
            }
            ArrayMap<String, ArraySet<OnOpModeChangedListener>> arrayMap = this.packageModeWatchers;
            for (int size2 = arrayMap.size() - 1; -1 < size2; size2--) {
                String keyAt = arrayMap.keyAt(size2);
                ArraySet<OnOpModeChangedListener> valueAt2 = arrayMap.valueAt(size2);
                String str = keyAt;
                valueAt2.remove(onOpModeChangedListener);
                if (valueAt2.isEmpty()) {
                    arrayMap.removeAt(size2);
                }
            }
        }
    }

    @NotNull
    public ArraySet<OnOpModeChangedListener> getOpModeChangedListeners(int i) {
        ArraySet<OnOpModeChangedListener> arraySet;
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            ArraySet<OnOpModeChangedListener> arraySet2 = this.opModeWatchers.get(i);
            if (arraySet2 == null) {
                arraySet = new ArraySet<>();
            } else {
                arraySet = new ArraySet<>(arraySet2);
            }
        }
        return arraySet;
    }

    @NotNull
    public ArraySet<OnOpModeChangedListener> getPackageModeChangedListeners(@NotNull String str) {
        ArraySet<OnOpModeChangedListener> arraySet;
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            ArraySet<OnOpModeChangedListener> arraySet2 = this.packageModeWatchers.get(str);
            if (arraySet2 == null) {
                arraySet = new ArraySet<>();
            } else {
                arraySet = new ArraySet<>(arraySet2);
            }
        }
        return arraySet;
    }

    public void notifyWatchersOfChange(int i, int i2) {
        ArraySet<OnOpModeChangedListener> opModeChangedListeners = getOpModeChangedListeners(i);
        int size = opModeChangedListeners.size();
        for (int i3 = 0; i3 < size; i3++) {
            notifyOpChanged(opModeChangedListeners.valueAt(i3), i, i2, null);
        }
    }

    public void notifyOpChanged(@NotNull OnOpModeChangedListener onOpModeChangedListener, int i, int i2, @Nullable String str) {
        int[] iArr;
        if (i2 == -2 || onOpModeChangedListener.getWatchingUid() < 0 || onOpModeChangedListener.getWatchingUid() == i2) {
            int watchedOpCode = onOpModeChangedListener.getWatchedOpCode();
            if (watchedOpCode == -2) {
                SparseArray<int[]> sparseArray = this.switchedOps;
                if (sparseArray == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("switchedOps");
                    sparseArray = null;
                }
                iArr = sparseArray.get(i);
            } else if (watchedOpCode == -1) {
                iArr = new int[]{i};
            } else {
                iArr = new int[]{onOpModeChangedListener.getWatchedOpCode()};
            }
            for (int i3 : iArr) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (!shouldIgnoreCallback(i3, onOpModeChangedListener)) {
                        onOpModeChangedListener.onOpModeChanged(i3, i2, str);
                    }
                } catch (RemoteException unused) {
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    throw th;
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    private final boolean shouldIgnoreCallback(int i, OnOpModeChangedListener onOpModeChangedListener) {
        return AppOpsManager.opRestrictsRead(i) && this.context.checkPermission("android.permission.MANAGE_APPOPS", onOpModeChangedListener.getCallingPid(), onOpModeChangedListener.getCallingUid()) != 0;
    }

    public void notifyOpChangedForAllPkgsInUid(int i, int i2, boolean z, @Nullable OnOpModeChangedListener onOpModeChangedListener) {
        AppOpService appOpService = this;
        String[] packagesForUid = appOpService.getPackagesForUid(i2);
        ArrayMap arrayMap = new ArrayMap();
        Object obj = appOpService.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            ArraySet<OnOpModeChangedListener> arraySet = appOpService.opModeWatchers.get(i);
            if (arraySet != null) {
                int size = arraySet.size();
                for (int i3 = 0; i3 < size; i3++) {
                    notifyOpChangedForAllPkgsInUid$associateListenerWithPackageNames(z, arrayMap, arraySet.valueAt(i3), packagesForUid);
                }
            }
            for (String str : packagesForUid) {
                ArraySet<OnOpModeChangedListener> arraySet2 = appOpService.packageModeWatchers.get(str);
                if (arraySet2 != null) {
                    int size2 = arraySet2.size();
                    for (int i4 = 0; i4 < size2; i4++) {
                        notifyOpChangedForAllPkgsInUid$associateListenerWithPackageNames(z, arrayMap, arraySet2.valueAt(i4), new String[]{str});
                    }
                }
            }
            if (onOpModeChangedListener != null) {
                arrayMap.remove(onOpModeChangedListener);
            }
            Unit unit = Unit.INSTANCE;
        }
        int size3 = arrayMap.size();
        int i5 = 0;
        while (i5 < size3) {
            Object keyAt = arrayMap.keyAt(i5);
            ArraySet arraySet3 = (ArraySet) arrayMap.valueAt(i5);
            OnOpModeChangedListener onOpModeChangedListener2 = (OnOpModeChangedListener) keyAt;
            int size4 = arraySet3.size();
            int i6 = 0;
            while (i6 < size4) {
                String str2 = (String) arraySet3.valueAt(i6);
                Handler handler = appOpService.handler;
                if (handler == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("handler");
                    handler = null;
                }
                handler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: com.android.server.permission.access.appop.AppOpService$notifyOpChangedForAllPkgsInUid$2$1$1
                    public final void accept(@NotNull AppOpService appOpService2, @NotNull OnOpModeChangedListener onOpModeChangedListener3, int i7, int i8, @Nullable String str3) {
                        appOpService2.notifyOpChanged(onOpModeChangedListener3, i7, i8, str3);
                    }

                    public /* bridge */ /* synthetic */ void accept(Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                        accept((AppOpService) obj2, (OnOpModeChangedListener) obj3, ((Number) obj4).intValue(), ((Number) obj5).intValue(), (String) obj6);
                    }
                }, this, onOpModeChangedListener2, Integer.valueOf(i), Integer.valueOf(i2), str2));
                i6++;
                appOpService = this;
            }
            i5++;
            appOpService = this;
        }
    }

    private static final void notifyOpChangedForAllPkgsInUid$associateListenerWithPackageNames(boolean z, ArrayMap<OnOpModeChangedListener, ArraySet<String>> arrayMap, OnOpModeChangedListener onOpModeChangedListener, String[] strArr) {
        boolean hasBits = IntExtensions.hasBits(onOpModeChangedListener.getFlags(), 1);
        if (!z || hasBits) {
            ArraySet<String> arraySet = arrayMap.get(onOpModeChangedListener);
            if (arraySet == null) {
                arraySet = new ArraySet<>();
                arrayMap.put(onOpModeChangedListener, arraySet);
            }
            MutableCollections.addAll(arraySet, strArr);
        }
    }

    private final String[] getPackagesForUid(int i) {
        String[] packagesForUid;
        try {
            IPackageManager packageManager = AppGlobals.getPackageManager();
            return (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(i)) == null) ? EmptyArray.STRING : packagesForUid;
        } catch (RemoteException unused) {
            return EmptyArray.STRING;
        }
    }

    @Nullable
    public SparseBooleanArray evalForegroundUidOps(int i, @Nullable SparseBooleanArray sparseBooleanArray) {
        SparseBooleanArray evalForegroundOps;
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            evalForegroundOps = evalForegroundOps(getUidModes(i), sparseBooleanArray);
        }
        return evalForegroundOps;
    }

    @Nullable
    public SparseBooleanArray evalForegroundPackageOps(@NotNull String str, @Nullable SparseBooleanArray sparseBooleanArray, int i) {
        SparseBooleanArray evalForegroundOps;
        Object obj = this.lock;
        if (obj == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lock");
            obj = Unit.INSTANCE;
        }
        synchronized (obj) {
            AccessState accessState = this.service.state;
            if (accessState == null) {
                Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            new GetStateScope(accessState);
            evalForegroundOps = evalForegroundOps(getPackageModes(str, i), sparseBooleanArray);
        }
        return evalForegroundOps;
    }

    private final void evalForegroundWatchers(String str, SparseBooleanArray sparseBooleanArray) {
        boolean z;
        int strOpToOp = AppOpsManager.strOpToOp(str);
        ArraySet<OnOpModeChangedListener> arraySet = this.opModeWatchers.get(strOpToOp);
        boolean z2 = true;
        if (!sparseBooleanArray.get(strOpToOp)) {
            if (arraySet != null) {
                int size = arraySet.size();
                for (int i = 0; i < size; i++) {
                    if (IntExtensions.hasBits(arraySet.valueAt(i).getFlags(), 1)) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (!z) {
                z2 = false;
            }
        }
        sparseBooleanArray.put(strOpToOp, z2);
    }

    public boolean dumpListeners(int i, int i2, @Nullable String str, @NotNull PrintWriter printWriter) {
        boolean z;
        SparseArray<ArraySet<OnOpModeChangedListener>> sparseArray;
        int i3;
        if (this.opModeWatchers.size() > 0) {
            SparseArray<ArraySet<OnOpModeChangedListener>> sparseArray2 = this.opModeWatchers;
            int size = sparseArray2.size();
            int i4 = 0;
            z = false;
            boolean z2 = false;
            while (i4 < size) {
                int keyAt = sparseArray2.keyAt(i4);
                ArraySet<OnOpModeChangedListener> valueAt = sparseArray2.valueAt(i4);
                if (i < 0 || i == keyAt) {
                    String opToName = AppOpsManager.opToName(keyAt);
                    int size2 = valueAt.size();
                    int i5 = 0;
                    boolean z3 = false;
                    while (i5 < size2) {
                        OnOpModeChangedListener valueAt2 = valueAt.valueAt(i5);
                        if (str != null) {
                            sparseArray = sparseArray2;
                            i3 = size2;
                            if (i2 != UserHandle.getAppId(valueAt2.getWatchingUid())) {
                                i5++;
                                size2 = i3;
                                sparseArray2 = sparseArray;
                            }
                        } else {
                            sparseArray = sparseArray2;
                            i3 = size2;
                        }
                        if (!z2) {
                            printWriter.println("  Op mode watchers:");
                            z2 = true;
                        }
                        if (!z3) {
                            printWriter.print("    Op ");
                            printWriter.print(opToName);
                            printWriter.println(":");
                            z3 = true;
                        }
                        printWriter.print("      #");
                        printWriter.print(i5);
                        printWriter.print(opToName);
                        printWriter.print(": ");
                        printWriter.println(valueAt2.toString());
                        z = true;
                        i5++;
                        size2 = i3;
                        sparseArray2 = sparseArray;
                    }
                }
                i4++;
                sparseArray2 = sparseArray2;
            }
        } else {
            z = false;
        }
        if (this.packageModeWatchers.size() > 0 && i < 0) {
            ArrayMap<String, ArraySet<OnOpModeChangedListener>> arrayMap = this.packageModeWatchers;
            int size3 = arrayMap.size();
            boolean z4 = false;
            for (int i6 = 0; i6 < size3; i6++) {
                String keyAt2 = arrayMap.keyAt(i6);
                ArraySet<OnOpModeChangedListener> valueAt3 = arrayMap.valueAt(i6);
                String str2 = keyAt2;
                if (str == null || Intrinsics.areEqual(str, str2)) {
                    if (!z4) {
                        printWriter.println("  Package mode watchers:");
                        z4 = true;
                    }
                    printWriter.print("    Pkg ");
                    printWriter.print(str2);
                    printWriter.println(":");
                    int size4 = valueAt3.size();
                    for (int i7 = 0; i7 < size4; i7++) {
                        OnOpModeChangedListener valueAt4 = valueAt3.valueAt(i7);
                        printWriter.print("      #");
                        printWriter.print(i7);
                        printWriter.print(": ");
                        printWriter.println(valueAt4.toString());
                    }
                    z = true;
                }
            }
        }
        return z;
    }

    /* compiled from: AppOpService.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
