package com.android.server.notification;

import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class GroupHelper {
    private static final int ALL_CHILDREN_FLAG = 16;
    private static final int ANY_CHILDREN_FLAGS = 34;
    protected static final String AUTOGROUP_KEY = "ranker_group";
    protected static final int BASE_FLAGS = 1792;
    private static final String TAG = "GroupHelper";
    private final int mAutoGroupAtCount;
    private final Callback mCallback;

    @GuardedBy({"mUngroupedNotifications"})
    private final ArrayMap<String, ArrayMap<String, Integer>> mUngroupedNotifications = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        void addAutoGroup(String str);

        void addAutoGroupSummary(int i, String str, String str2, int i2);

        void removeAutoGroup(String str);

        void removeAutoGroupSummary(int i, String str);

        void updateAutogroupSummary(int i, String str, int i2);
    }

    private boolean hasAnyFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public GroupHelper(int i, Callback callback) {
        this.mAutoGroupAtCount = i;
        this.mCallback = callback;
    }

    private String generatePackageKey(int i, String str) {
        return i + "|" + str;
    }

    @GuardedBy({"mUngroupedNotifications"})
    @VisibleForTesting
    protected int getAutogroupSummaryFlags(ArrayMap<String, Integer> arrayMap) {
        boolean z = arrayMap.size() > 0;
        int i = 0;
        for (int i2 = 0; i2 < arrayMap.size(); i2++) {
            if (!hasAnyFlag(arrayMap.valueAt(i2).intValue(), 16)) {
                z = false;
            }
            if (hasAnyFlag(arrayMap.valueAt(i2).intValue(), 34)) {
                i |= arrayMap.valueAt(i2).intValue() & 34;
            }
        }
        return (z ? 16 : 0) | 1792 | i;
    }

    public void onNotificationPosted(StatusBarNotification statusBarNotification, boolean z) {
        try {
            if (!statusBarNotification.isAppGroup()) {
                maybeGroup(statusBarNotification, z);
            } else {
                maybeUngroup(statusBarNotification, false, statusBarNotification.getUserId());
            }
        } catch (Exception e) {
            Slog.e(TAG, "Failure processing new notification", e);
        }
    }

    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        try {
            maybeUngroup(statusBarNotification, true, statusBarNotification.getUserId());
        } catch (Exception e) {
            Slog.e(TAG, "Error processing canceled notification", e);
        }
    }

    private void maybeGroup(StatusBarNotification statusBarNotification, boolean z) {
        int autogroupSummaryFlags;
        ArrayList arrayList = new ArrayList();
        synchronized (this.mUngroupedNotifications) {
            String generatePackageKey = generatePackageKey(statusBarNotification.getUserId(), statusBarNotification.getPackageName());
            ArrayMap<String, Integer> orDefault = this.mUngroupedNotifications.getOrDefault(generatePackageKey, new ArrayMap<>());
            orDefault.put(statusBarNotification.getKey(), Integer.valueOf(statusBarNotification.getNotification().flags));
            this.mUngroupedNotifications.put(generatePackageKey, orDefault);
            if (orDefault.size() < this.mAutoGroupAtCount && !z) {
                autogroupSummaryFlags = 0;
            }
            autogroupSummaryFlags = getAutogroupSummaryFlags(orDefault);
            arrayList.addAll(orDefault.keySet());
        }
        if (arrayList.size() > 0) {
            if (z) {
                this.mCallback.updateAutogroupSummary(statusBarNotification.getUserId(), statusBarNotification.getPackageName(), autogroupSummaryFlags);
            } else {
                this.mCallback.addAutoGroupSummary(statusBarNotification.getUserId(), statusBarNotification.getPackageName(), statusBarNotification.getKey(), autogroupSummaryFlags);
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mCallback.addAutoGroup((String) it.next());
            }
        }
    }

    private void maybeUngroup(StatusBarNotification statusBarNotification, boolean z, int i) {
        boolean z2;
        int i2;
        boolean z3;
        synchronized (this.mUngroupedNotifications) {
            ArrayMap<String, Integer> orDefault = this.mUngroupedNotifications.getOrDefault(generatePackageKey(statusBarNotification.getUserId(), statusBarNotification.getPackageName()), new ArrayMap<>());
            if (orDefault.size() == 0) {
                return;
            }
            boolean z4 = false;
            if (orDefault.containsKey(statusBarNotification.getKey())) {
                if (hasAnyFlag(orDefault.remove(statusBarNotification.getKey()).intValue(), 34)) {
                    i2 = getAutogroupSummaryFlags(orDefault);
                    z3 = true;
                } else {
                    i2 = 0;
                    z3 = false;
                }
                z2 = (z || statusBarNotification.getOverrideGroupKey() == null) ? false : true;
                if (orDefault.size() == 0) {
                    z4 = true;
                }
            } else {
                z2 = false;
                i2 = 0;
                z3 = false;
            }
            if (z4) {
                this.mCallback.removeAutoGroupSummary(i, statusBarNotification.getPackageName());
            } else if (z3) {
                this.mCallback.updateAutogroupSummary(i, statusBarNotification.getPackageName(), i2);
            }
            if (z2) {
                this.mCallback.removeAutoGroup(statusBarNotification.getKey());
            }
        }
    }

    @VisibleForTesting
    int getNotGroupedByAppCount(int i, String str) {
        int size;
        synchronized (this.mUngroupedNotifications) {
            size = this.mUngroupedNotifications.getOrDefault(generatePackageKey(i, str), new ArrayMap<>()).size();
        }
        return size;
    }
}
