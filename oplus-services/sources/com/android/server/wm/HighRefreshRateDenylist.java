package com.android.server.wm;

import android.R;
import android.content.res.Resources;
import android.provider.DeviceConfig;
import android.provider.DeviceConfigInterface;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;
import java.io.PrintWriter;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class HighRefreshRateDenylist {
    private final String[] mDefaultDenylist;
    private final ArraySet<String> mDenylistedPackages = new ArraySet<>();
    private final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HighRefreshRateDenylist create(Resources resources) {
        return new HighRefreshRateDenylist(resources, DeviceConfigInterface.REAL);
    }

    @VisibleForTesting
    HighRefreshRateDenylist(Resources resources, DeviceConfigInterface deviceConfigInterface) {
        this.mDefaultDenylist = resources.getStringArray(R.array.config_tether_upstream_types);
        deviceConfigInterface.addOnPropertiesChangedListener("display_manager", BackgroundThread.getExecutor(), new OnPropertiesChangedListener());
        updateDenylist(deviceConfigInterface.getProperty("display_manager", "high_refresh_rate_blacklist"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDenylist(String str) {
        synchronized (this.mLock) {
            this.mDenylistedPackages.clear();
            int i = 0;
            if (str != null) {
                String[] split = str.split(",");
                int length = split.length;
                while (i < length) {
                    String trim = split[i].trim();
                    if (!trim.isEmpty()) {
                        this.mDenylistedPackages.add(trim);
                    }
                    i++;
                }
            } else {
                String[] strArr = this.mDefaultDenylist;
                int length2 = strArr.length;
                while (i < length2) {
                    this.mDenylistedPackages.add(strArr[i]);
                    i++;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDenylisted(String str) {
        boolean contains;
        synchronized (this.mLock) {
            contains = this.mDenylistedPackages.contains(str);
        }
        return contains;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("High Refresh Rate Denylist");
        printWriter.println("  Packages:");
        synchronized (this.mLock) {
            Iterator<String> it = this.mDenylistedPackages.iterator();
            while (it.hasNext()) {
                printWriter.println("    " + it.next());
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class OnPropertiesChangedListener implements DeviceConfig.OnPropertiesChangedListener {
        private OnPropertiesChangedListener() {
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains("high_refresh_rate_blacklist")) {
                HighRefreshRateDenylist.this.updateDenylist(properties.getString("high_refresh_rate_blacklist", (String) null));
            }
        }
    }
}
