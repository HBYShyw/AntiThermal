package com.android.server.pm;

import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.ActivityThread;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManagerInternal;
import android.media.AudioFocusInfo;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import android.media.IAudioService;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.ArraySet;
import com.android.internal.util.ArrayUtils;
import com.android.server.LocalServices;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageStateInternal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppStateHelper {
    private static final long ACTIVE_NETWORK_DURATION_MILLIS = TimeUnit.MINUTES.toMillis(10);
    private final Context mContext;

    public AppStateHelper(Context context) {
        this.mContext = context;
    }

    private static boolean isPackageLoaded(ActivityManager.RunningAppProcessInfo runningAppProcessInfo, String str) {
        return ArrayUtils.contains(runningAppProcessInfo.pkgList, str) || ArrayUtils.contains(runningAppProcessInfo.pkgDeps, str);
    }

    private int getImportance(String str) {
        return ((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).getPackageImportance(str);
    }

    private boolean hasAudioFocus(String str) {
        try {
            List focusStack = IAudioService.Stub.asInterface(ServiceManager.getService("audio")).getFocusStack();
            int size = focusStack.size();
            return TextUtils.equals(str, size > 0 ? ((AudioFocusInfo) focusStack.get(size - 1)).getPackageName() : null);
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean hasVoiceCall() {
        try {
            int mode = ((AudioManager) this.mContext.getSystemService(AudioManager.class)).getMode();
            return mode == 2 || mode == 3;
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean isRecordingAudio(String str) {
        try {
            Iterator<AudioRecordingConfiguration> it = ((AudioManager) this.mContext.getSystemService(AudioManager.class)).getActiveRecordingConfigurations().iterator();
            while (it.hasNext()) {
                if (TextUtils.equals(it.next().getClientPackageName(), str)) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean isAppForeground(String str) {
        return getImportance(str) <= 125;
    }

    public boolean isAppTopVisible(String str) {
        return getImportance(str) <= 100;
    }

    private boolean hasActiveAudio(String str) {
        return hasAudioFocus(str) || isRecordingAudio(str);
    }

    private boolean hasActiveNetwork(List<String> list, int i) {
        IPackageManager packageManager = ActivityThread.getPackageManager();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.mContext.getSystemService(NetworkStatsManager.class);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            NetworkStats querySummary = networkStatsManager.querySummary(i, null, currentTimeMillis - ACTIVE_NETWORK_DURATION_MILLIS, currentTimeMillis);
            try {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                while (querySummary.hasNextBucket()) {
                    querySummary.getNextBucket(bucket);
                    if (list.contains(packageManager.getNameForUid(bucket.getUid())) && (bucket.getRxPackets() > 0 || bucket.getTxPackets() > 0)) {
                        querySummary.close();
                        return true;
                    }
                }
                querySummary.close();
                return false;
            } finally {
            }
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean containsAny(String[] strArr, List<String> list) {
        int length = strArr.length;
        int size = list.size();
        int i = 0;
        int i2 = 0;
        while (i < length && i2 < size) {
            int compareTo = strArr[i].compareTo(list.get(i2));
            if (compareTo == 0) {
                return true;
            }
            if (compareTo < 0) {
                i++;
            } else {
                i2++;
            }
        }
        return false;
    }

    private void addLibraryDependency(final ArraySet<String> arraySet, List<String> list) {
        PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            AndroidPackage androidPackage = packageManagerInternal.getAndroidPackage(it.next());
            if (androidPackage != null) {
                arrayList.addAll(androidPackage.getLibraryNames());
                String staticSharedLibraryName = androidPackage.getStaticSharedLibraryName();
                if (staticSharedLibraryName != null) {
                    arrayList2.add(staticSharedLibraryName);
                }
                String sdkLibraryName = androidPackage.getSdkLibraryName();
                if (sdkLibraryName != null) {
                    arrayList3.add(sdkLibraryName);
                }
            }
        }
        if (arrayList.isEmpty() && arrayList2.isEmpty() && arrayList3.isEmpty()) {
            return;
        }
        Collections.sort(arrayList);
        Collections.sort(arrayList3);
        Collections.sort(arrayList2);
        packageManagerInternal.forEachPackageState(new Consumer() { // from class: com.android.server.pm.AppStateHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                AppStateHelper.lambda$addLibraryDependency$0(arrayList, arrayList2, arrayList3, arraySet, (PackageStateInternal) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addLibraryDependency$0(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArraySet arraySet, PackageStateInternal packageStateInternal) {
        AndroidPackageInternal pkg = packageStateInternal.getPkg();
        if (pkg == null) {
            return;
        }
        if (containsAny(pkg.getUsesLibrariesSorted(), arrayList) || containsAny(pkg.getUsesOptionalLibrariesSorted(), arrayList) || containsAny(pkg.getUsesStaticLibrariesSorted(), arrayList2) || containsAny(pkg.getUsesSdkLibrariesSorted(), arrayList3)) {
            arraySet.add(pkg.getPackageName());
        }
    }

    private boolean hasActiveNetwork(List<String> list) {
        return hasActiveNetwork(list, 1) || hasActiveNetwork(list, 0);
    }

    public boolean hasInteractingApp(List<String> list) {
        for (String str : list) {
            if (hasActiveAudio(str) || isAppTopVisible(str)) {
                return true;
            }
        }
        return hasActiveNetwork(list);
    }

    public boolean hasForegroundApp(List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (isAppForeground(it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasTopVisibleApp(List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (isAppTopVisible(it.next())) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCall() {
        if (SystemProperties.getBoolean("debug.pm.gentle_update_test.is_in_call", false)) {
            return true;
        }
        return ((TelecomManager) this.mContext.getSystemService(TelecomManager.class)).isInCall() || hasVoiceCall();
    }

    public List<String> getDependencyPackages(List<String> list) {
        ArraySet<String> arraySet = new ArraySet<>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : ((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).getRunningAppProcesses()) {
            Iterator<String> it = list.iterator();
            while (it.hasNext()) {
                if (isPackageLoaded(runningAppProcessInfo, it.next())) {
                    for (String str : runningAppProcessInfo.pkgList) {
                        arraySet.add(str);
                    }
                }
            }
        }
        ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        Iterator<String> it2 = list.iterator();
        while (it2.hasNext()) {
            arraySet.addAll(activityManagerInternal.getClientPackages(it2.next()));
        }
        addLibraryDependency(arraySet, list);
        return new ArrayList(arraySet);
    }
}
