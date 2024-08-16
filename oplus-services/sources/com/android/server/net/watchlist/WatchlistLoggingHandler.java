package com.android.server.net.watchlist;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.incremental.IncrementalManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.HexDump;
import com.android.server.net.watchlist.WatchlistReportDbHelper;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WatchlistLoggingHandler extends Handler {
    private static final boolean DEBUG = false;
    private static final String DROPBOX_TAG = "network_watchlist_report";

    @VisibleForTesting
    static final int FORCE_REPORT_RECORDS_NOW_FOR_TEST_MSG = 3;

    @VisibleForTesting
    static final int LOG_WATCHLIST_EVENT_MSG = 1;

    @VisibleForTesting
    static final int REPORT_RECORDS_IF_NECESSARY_MSG = 2;
    private final ConcurrentHashMap<Integer, byte[]> mCachedUidDigestMap;
    private final WatchlistConfig mConfig;
    private final Context mContext;
    private final WatchlistReportDbHelper mDbHelper;
    private final DropBoxManager mDropBoxManager;
    private final PackageManager mPm;
    private int mPrimaryUserId;
    private final ContentResolver mResolver;
    private final WatchlistSettings mSettings;
    private static final String TAG = WatchlistLoggingHandler.class.getSimpleName();
    private static final long ONE_DAY_MS = TimeUnit.DAYS.toMillis(1);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private interface WatchlistEventKeys {
        public static final String HOST = "host";
        public static final String IP_ADDRESSES = "ipAddresses";
        public static final String TIMESTAMP = "timestamp";
        public static final String UID = "uid";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WatchlistLoggingHandler(Context context, Looper looper) {
        super(looper);
        this.mPrimaryUserId = -1;
        this.mCachedUidDigestMap = new ConcurrentHashMap<>();
        this.mContext = context;
        this.mPm = context.getPackageManager();
        this.mResolver = context.getContentResolver();
        this.mDbHelper = WatchlistReportDbHelper.getInstance(context);
        this.mConfig = WatchlistConfig.getInstance();
        this.mSettings = WatchlistSettings.getInstance();
        this.mDropBoxManager = (DropBoxManager) context.getSystemService(DropBoxManager.class);
        this.mPrimaryUserId = getPrimaryUserId();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            Bundle data = message.getData();
            handleNetworkEvent(data.getString("host"), data.getStringArray(WatchlistEventKeys.IP_ADDRESSES), data.getInt("uid"), data.getLong(WatchlistEventKeys.TIMESTAMP));
            return;
        }
        if (i == 2) {
            tryAggregateRecords(getLastMidnightTime());
            return;
        }
        if (i == 3) {
            Object obj = message.obj;
            if (obj instanceof Long) {
                tryAggregateRecords(((Long) obj).longValue());
                return;
            } else {
                Slog.e(TAG, "Msg.obj needs to be a Long object.");
                return;
            }
        }
        Slog.d(TAG, "WatchlistLoggingHandler received an unknown of message.");
    }

    private int getPrimaryUserId() {
        UserInfo primaryUser = ((UserManager) this.mContext.getSystemService("user")).getPrimaryUser();
        if (primaryUser != null) {
            return primaryUser.id;
        }
        return -1;
    }

    private boolean isPackageTestOnly(int i) {
        try {
            String[] packagesForUid = this.mPm.getPackagesForUid(i);
            if (packagesForUid != null && packagesForUid.length != 0) {
                return (this.mPm.getApplicationInfo(packagesForUid[0], 0).flags & 256) != 0;
            }
            Slog.e(TAG, "Couldn't find package: " + Arrays.toString(packagesForUid));
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }

    public void reportWatchlistIfNecessary() {
        sendMessage(obtainMessage(2));
    }

    public void forceReportWatchlistForTest(long j) {
        Message obtainMessage = obtainMessage(3);
        obtainMessage.obj = Long.valueOf(j);
        sendMessage(obtainMessage);
    }

    public void asyncNetworkEvent(String str, String[] strArr, int i) {
        Message obtainMessage = obtainMessage(1);
        Bundle bundle = new Bundle();
        bundle.putString("host", str);
        bundle.putStringArray(WatchlistEventKeys.IP_ADDRESSES, strArr);
        bundle.putInt("uid", i);
        bundle.putLong(WatchlistEventKeys.TIMESTAMP, System.currentTimeMillis());
        obtainMessage.setData(bundle);
        sendMessage(obtainMessage);
    }

    private void handleNetworkEvent(String str, String[] strArr, int i, long j) {
        if (this.mPrimaryUserId == -1) {
            this.mPrimaryUserId = getPrimaryUserId();
        }
        if (UserHandle.getUserId(i) != this.mPrimaryUserId) {
            return;
        }
        String searchAllSubDomainsInWatchlist = searchAllSubDomainsInWatchlist(str);
        if (searchAllSubDomainsInWatchlist != null) {
            insertRecord(i, searchAllSubDomainsInWatchlist, j);
            return;
        }
        String searchIpInWatchlist = searchIpInWatchlist(strArr);
        if (searchIpInWatchlist != null) {
            insertRecord(i, searchIpInWatchlist, j);
        }
    }

    private void insertRecord(int i, String str, long j) {
        byte[] digestFromUid;
        if ((this.mConfig.isConfigSecure() || isPackageTestOnly(i)) && (digestFromUid = getDigestFromUid(i)) != null && this.mDbHelper.insertNewRecord(digestFromUid, str, j)) {
            Slog.w(TAG, "Unable to insert record for uid: " + i);
        }
    }

    private boolean shouldReportNetworkWatchlist(long j) {
        long j2 = Settings.Global.getLong(this.mResolver, "network_watchlist_last_report_time", 0L);
        if (j >= j2) {
            return j >= j2 + ONE_DAY_MS;
        }
        Slog.i(TAG, "Last report time is larger than current time, reset report");
        this.mDbHelper.cleanup(j2);
        return false;
    }

    private void tryAggregateRecords(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                if (!shouldReportNetworkWatchlist(j)) {
                    String str = TAG;
                    Slog.i(str, "No need to aggregate record yet.");
                    Slog.i(str, "Milliseconds spent on tryAggregateRecords(): " + (System.currentTimeMillis() - currentTimeMillis));
                    return;
                }
                String str2 = TAG;
                Slog.i(str2, "Start aggregating watchlist records.");
                DropBoxManager dropBoxManager = this.mDropBoxManager;
                if (dropBoxManager == null || !dropBoxManager.isTagEnabled(DROPBOX_TAG)) {
                    Slog.w(str2, "Network Watchlist dropbox tag is not enabled");
                } else {
                    Settings.Global.putLong(this.mResolver, "network_watchlist_last_report_time", j);
                    WatchlistReportDbHelper.AggregatedResult aggregatedRecords = this.mDbHelper.getAggregatedRecords(j);
                    if (aggregatedRecords == null) {
                        Slog.i(str2, "Cannot get result from database");
                        Slog.i(str2, "Milliseconds spent on tryAggregateRecords(): " + (System.currentTimeMillis() - currentTimeMillis));
                        return;
                    }
                    byte[] encodeWatchlistReport = ReportEncoder.encodeWatchlistReport(this.mConfig, this.mSettings.getPrivacySecretKey(), getAllDigestsForReport(aggregatedRecords), aggregatedRecords);
                    if (encodeWatchlistReport != null) {
                        addEncodedReportToDropBox(encodeWatchlistReport);
                    }
                }
                this.mDbHelper.cleanup(j);
                Slog.i(str2, "Milliseconds spent on tryAggregateRecords(): " + (System.currentTimeMillis() - currentTimeMillis));
            } catch (SQLiteDatabaseCorruptException e) {
                String str3 = TAG;
                Slog.w(str3, "Database exception", e);
                Slog.i(str3, "Milliseconds spent on tryAggregateRecords(): " + (System.currentTimeMillis() - currentTimeMillis));
            }
        } catch (Throwable th) {
            long currentTimeMillis2 = System.currentTimeMillis();
            Slog.i(TAG, "Milliseconds spent on tryAggregateRecords(): " + (currentTimeMillis2 - currentTimeMillis));
            throw th;
        }
    }

    @VisibleForTesting
    List<String> getAllDigestsForReport(WatchlistReportDbHelper.AggregatedResult aggregatedResult) {
        List<ApplicationInfo> installedApplications = this.mContext.getPackageManager().getInstalledApplications(131072);
        HashSet hashSet = new HashSet(installedApplications.size() + aggregatedResult.appDigestCNCList.size());
        int size = installedApplications.size();
        for (int i = 0; i < size; i++) {
            byte[] digestFromUid = getDigestFromUid(installedApplications.get(i).uid);
            if (digestFromUid != null) {
                hashSet.add(HexDump.toHexString(digestFromUid));
            }
        }
        hashSet.addAll(aggregatedResult.appDigestCNCList.keySet());
        return new ArrayList(hashSet);
    }

    private void addEncodedReportToDropBox(byte[] bArr) {
        this.mDropBoxManager.addData(DROPBOX_TAG, bArr, 0);
    }

    private byte[] getDigestFromUid(final int i) {
        return this.mCachedUidDigestMap.computeIfAbsent(Integer.valueOf(i), new Function() { // from class: com.android.server.net.watchlist.WatchlistLoggingHandler$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                byte[] lambda$getDigestFromUid$0;
                lambda$getDigestFromUid$0 = WatchlistLoggingHandler.this.lambda$getDigestFromUid$0(i, (Integer) obj);
                return lambda$getDigestFromUid$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ byte[] lambda$getDigestFromUid$0(int i, Integer num) {
        String[] packagesForUid = this.mPm.getPackagesForUid(num.intValue());
        int userId = UserHandle.getUserId(i);
        if (!ArrayUtils.isEmpty(packagesForUid)) {
            for (String str : packagesForUid) {
                try {
                    String str2 = this.mPm.getPackageInfoAsUser(str, 786432, userId).applicationInfo.publicSourceDir;
                    if (TextUtils.isEmpty(str2)) {
                        Slog.w(TAG, "Cannot find apkPath for " + str);
                    } else if (IncrementalManager.isIncrementalPath(str2)) {
                        Slog.i(TAG, "Skipping incremental path: " + str);
                    } else {
                        return DigestUtils.getSha256Hash(new File(str2));
                    }
                } catch (PackageManager.NameNotFoundException | IOException | NoSuchAlgorithmException e) {
                    Slog.e(TAG, "Cannot get digest from uid: " + num + ",pkg: " + str, e);
                }
            }
        }
        return null;
    }

    private String searchIpInWatchlist(String[] strArr) {
        for (String str : strArr) {
            if (isIpInWatchlist(str)) {
                return str;
            }
        }
        return null;
    }

    private boolean isIpInWatchlist(String str) {
        if (str == null) {
            return false;
        }
        return this.mConfig.containsIp(str);
    }

    private boolean isHostInWatchlist(String str) {
        if (str == null) {
            return false;
        }
        return this.mConfig.containsDomain(str);
    }

    private String searchAllSubDomainsInWatchlist(String str) {
        if (str == null) {
            return null;
        }
        for (String str2 : getAllSubDomains(str)) {
            if (isHostInWatchlist(str2)) {
                return str2;
            }
        }
        return null;
    }

    @VisibleForTesting
    static String[] getAllSubDomains(String str) {
        if (str == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        int indexOf = str.indexOf(".");
        while (indexOf != -1) {
            str = str.substring(indexOf + 1);
            if (!TextUtils.isEmpty(str)) {
                arrayList.add(str);
            }
            indexOf = str.indexOf(".");
        }
        return (String[]) arrayList.toArray(new String[0]);
    }

    static long getLastMidnightTime() {
        return getMidnightTimestamp(0);
    }

    static long getMidnightTimestamp(int i) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(11, 0);
        gregorianCalendar.set(12, 0);
        gregorianCalendar.set(13, 0);
        gregorianCalendar.set(14, 0);
        gregorianCalendar.add(5, -i);
        return gregorianCalendar.getTimeInMillis();
    }
}
