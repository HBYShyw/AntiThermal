package com.android.server;

import android.os.FileUtils;
import android.os.UEventObserver;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.server.ExtconUEventObserver;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public abstract class ExtconUEventObserver extends UEventObserver {
    private static final boolean LOG = false;
    private static final String SELINUX_POLICIES_NEED_TO_BE_CHANGED = "This probably means the selinux policies need to be changed.";
    private static final String TAG = "ExtconUEventObserver";
    private final Map<String, ExtconInfo> mExtconInfos = new ArrayMap();

    protected abstract void onUEvent(ExtconInfo extconInfo, UEventObserver.UEvent uEvent);

    public final void onUEvent(UEventObserver.UEvent uEvent) {
        ExtconInfo extconInfo = this.mExtconInfos.get(uEvent.get("DEVPATH"));
        if (extconInfo != null) {
            onUEvent(extconInfo, uEvent);
            return;
        }
        Slog.w(TAG, "No match found for DEVPATH of " + uEvent + " in " + this.mExtconInfos);
    }

    public void startObserving(ExtconInfo extconInfo) {
        String devicePath = extconInfo.getDevicePath();
        if (devicePath == null) {
            Slog.wtf(TAG, "Unable to start observing  " + extconInfo.getName() + " because the device path is null. " + SELINUX_POLICIES_NEED_TO_BE_CHANGED);
            return;
        }
        this.mExtconInfos.put(devicePath, extconInfo);
        startObserving("DEVPATH=" + devicePath);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ExtconInfo {
        public static final String EXTCON_CHARGE_DOWNSTREAM = "CHARGE-DOWNSTREAM";
        public static final String EXTCON_DOCK = "DOCK";
        public static final String EXTCON_DVI = "DVI";
        public static final String EXTCON_FAST_CHARGER = "FAST-CHARGER";
        public static final String EXTCON_HDMI = "HDMI";
        public static final String EXTCON_HEADPHONE = "HEADPHONE";
        public static final String EXTCON_JIG = "JIG";
        public static final String EXTCON_LINE_IN = "LINE-IN";
        public static final String EXTCON_LINE_OUT = "LINE-OUT";
        public static final String EXTCON_MECHANICAL = "MECHANICAL";
        public static final String EXTCON_MHL = "MHL";
        public static final String EXTCON_MICROPHONE = "MICROPHONE";
        public static final String EXTCON_SLOW_CHARGER = "SLOW-CHARGER";
        public static final String EXTCON_SPDIF_IN = "SPDIF-IN";
        public static final String EXTCON_SPDIF_OUT = "SPDIF-OUT";
        public static final String EXTCON_TA = "TA";
        public static final String EXTCON_USB = "USB";
        public static final String EXTCON_USB_HOST = "USB-HOST";
        public static final String EXTCON_VGA = "VGA";
        public static final String EXTCON_VIDEO_IN = "VIDEO-IN";
        public static final String EXTCON_VIDEO_OUT = "VIDEO-OUT";
        private static ExtconInfo[] sExtconInfos;
        private static final Object sLock = new Object();

        @ExtconDeviceType
        private final HashSet<String> mDeviceTypes = new HashSet<>();
        private final String mName;

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public @interface ExtconDeviceType {
        }

        @GuardedBy({"sLock"})
        private static void initExtconInfos() {
            if (sExtconInfos != null) {
                return;
            }
            File file = new File("/sys/class/extcon");
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                Slog.w(ExtconUEventObserver.TAG, file + " exists " + file.exists() + " isDir " + file.isDirectory() + " but listFiles returns null." + ExtconUEventObserver.SELINUX_POLICIES_NEED_TO_BE_CHANGED);
                sExtconInfos = new ExtconInfo[0];
                return;
            }
            ArrayList arrayList = new ArrayList(listFiles.length);
            for (File file2 : listFiles) {
                arrayList.add(new ExtconInfo(file2.getName()));
            }
            sExtconInfos = (ExtconInfo[]) arrayList.toArray(new ExtconInfo[0]);
        }

        public static List<ExtconInfo> getExtconInfoForTypes(@ExtconDeviceType String[] strArr) {
            synchronized (sLock) {
                initExtconInfos();
            }
            ArrayList arrayList = new ArrayList();
            for (ExtconInfo extconInfo : sExtconInfos) {
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    if (extconInfo.hasCableType(strArr[i])) {
                        arrayList.add(extconInfo);
                        break;
                    }
                    i++;
                }
            }
            return arrayList;
        }

        public boolean hasCableType(@ExtconDeviceType String str) {
            return this.mDeviceTypes.contains(str);
        }

        private ExtconInfo(String str) {
            this.mName = str;
            File[] listFilesOrEmpty = FileUtils.listFilesOrEmpty(new File("/sys/class/extcon", str), new FilenameFilter() { // from class: com.android.server.ExtconUEventObserver$ExtconInfo$$ExternalSyntheticLambda0
                @Override // java.io.FilenameFilter
                public final boolean accept(File file, String str2) {
                    boolean lambda$new$0;
                    lambda$new$0 = ExtconUEventObserver.ExtconInfo.lambda$new$0(file, str2);
                    return lambda$new$0;
                }
            });
            if (listFilesOrEmpty.length == 0) {
                Slog.d(ExtconUEventObserver.TAG, "Unable to list cables in /sys/class/extcon/" + str + ". " + ExtconUEventObserver.SELINUX_POLICIES_NEED_TO_BE_CHANGED);
            }
            for (File file : listFilesOrEmpty) {
                String str2 = null;
                try {
                    String canonicalPath = file.getCanonicalPath();
                    try {
                        this.mDeviceTypes.add(FileUtils.readTextFile(new File(file, "name"), 0, null).replace("\n", "").replace("\r", ""));
                    } catch (IOException e) {
                        e = e;
                        str2 = canonicalPath;
                        Slog.w(ExtconUEventObserver.TAG, "Unable to read " + str2 + "/name. " + ExtconUEventObserver.SELINUX_POLICIES_NEED_TO_BE_CHANGED, e);
                    }
                } catch (IOException e2) {
                    e = e2;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$new$0(File file, String str) {
            return str.startsWith("cable.");
        }

        public String getName() {
            return this.mName;
        }

        public String getDevicePath() {
            try {
                File file = new File(TextUtils.formatSimple("/sys/class/extcon/%s", new Object[]{this.mName}));
                if (!file.exists()) {
                    return null;
                }
                String canonicalPath = file.getCanonicalPath();
                return canonicalPath.substring(canonicalPath.indexOf("/devices"));
            } catch (IOException e) {
                Slog.e(ExtconUEventObserver.TAG, "Could not get the extcon device path for " + this.mName, e);
                return null;
            }
        }

        public String getStatePath() {
            return TextUtils.formatSimple("/sys/class/extcon/%s/state", new Object[]{this.mName});
        }
    }

    public static boolean extconExists() {
        File file = new File("/sys/class/extcon");
        return file.exists() && file.isDirectory();
    }
}
