package com.android.server.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.notification.ManagedServices;
import com.android.server.notification.NotificationManagerService;
import com.android.server.pm.PackageManagerService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class SnoozeHelper {
    static final int CONCURRENT_SNOOZE_LIMIT = 500;
    static final String EXTRA_KEY = "key";
    private static final String EXTRA_USER_ID = "userId";
    private static final String INDENT = "    ";
    static final int MAX_STRING_LENGTH = 1000;
    private static final String REPOST_SCHEME = "repost";
    private static final int REQUEST_CODE_REPOST = 1;
    private static final String XML_SNOOZED_NOTIFICATION = "notification";
    private static final String XML_SNOOZED_NOTIFICATION_CONTEXT = "context";
    private static final String XML_SNOOZED_NOTIFICATION_CONTEXT_ID = "id";
    private static final String XML_SNOOZED_NOTIFICATION_KEY = "key";
    private static final String XML_SNOOZED_NOTIFICATION_TIME = "time";
    public static final int XML_SNOOZED_NOTIFICATION_VERSION = 1;
    private static final String XML_SNOOZED_NOTIFICATION_VERSION_LABEL = "version";
    protected static final String XML_TAG_NAME = "snoozed-notifications";
    private AlarmManager mAm;
    private final BroadcastReceiver mBroadcastReceiver;
    private Callback mCallback;
    private final Context mContext;
    private final ManagedServices.UserProfiles mUserProfiles;
    private static final String TAG = "SnoozeHelper";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String REPOST_ACTION = SnoozeHelper.class.getSimpleName() + ".EVALUATE";
    private ArrayMap<String, NotificationRecord> mSnoozedNotifications = new ArrayMap<>();
    private final ArrayMap<String, Long> mPersistedSnoozedNotifications = new ArrayMap<>();
    private final ArrayMap<String, String> mPersistedSnoozedNotificationsWithContext = new ArrayMap<>();
    private final Object mLock = new Object();

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        void repost(int i, NotificationRecord notificationRecord, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Inserter<T> {
        void insert(T t) throws IOException;
    }

    public SnoozeHelper(Context context, Callback callback, ManagedServices.UserProfiles userProfiles) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.SnoozeHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (SnoozeHelper.DEBUG) {
                    Slog.d(SnoozeHelper.TAG, "Reposting notification");
                }
                if (SnoozeHelper.REPOST_ACTION.equals(intent.getAction())) {
                    SnoozeHelper.this.repost(intent.getStringExtra("key"), intent.getIntExtra("userId", 0), false);
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter(REPOST_ACTION);
        intentFilter.addDataScheme(REPOST_SCHEME);
        context.registerReceiver(broadcastReceiver, intentFilter, 2);
        this.mAm = (AlarmManager) context.getSystemService("alarm");
        this.mCallback = callback;
        this.mUserProfiles = userProfiles;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canSnooze(int i) {
        synchronized (this.mLock) {
            if (this.mSnoozedNotifications.size() + i <= 500 && this.mPersistedSnoozedNotifications.size() + this.mPersistedSnoozedNotificationsWithContext.size() + i <= 500) {
                return true;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Long getSnoozeTimeForUnpostedNotification(int i, String str, String str2) {
        Long l;
        synchronized (this.mLock) {
            l = this.mPersistedSnoozedNotifications.get(getTrimmedString(str2));
        }
        if (l == null) {
            return 0L;
        }
        return l;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSnoozeContextForUnpostedNotification(int i, String str, String str2) {
        String str3;
        synchronized (this.mLock) {
            str3 = this.mPersistedSnoozedNotificationsWithContext.get(getTrimmedString(str2));
        }
        return str3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isSnoozed(int i, String str, String str2) {
        boolean containsKey;
        synchronized (this.mLock) {
            containsKey = this.mSnoozedNotifications.containsKey(str2);
        }
        return containsKey;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Collection<NotificationRecord> getSnoozed(int i, String str) {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList();
            for (NotificationRecord notificationRecord : this.mSnoozedNotifications.values()) {
                if (notificationRecord.getUserId() == i && notificationRecord.getSbn().getPackageName().equals(str)) {
                    arrayList.add(notificationRecord);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<NotificationRecord> getNotifications(String str, String str2, Integer num) {
        ArrayList<NotificationRecord> arrayList = new ArrayList<>();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mSnoozedNotifications.size(); i++) {
                NotificationRecord valueAt = this.mSnoozedNotifications.valueAt(i);
                if (valueAt.getSbn().getPackageName().equals(str) && valueAt.getUserId() == num.intValue() && Objects.equals(valueAt.getSbn().getGroup(), str2)) {
                    arrayList.add(valueAt);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public NotificationRecord getNotification(String str) {
        NotificationRecord notificationRecord;
        synchronized (this.mLock) {
            notificationRecord = this.mSnoozedNotifications.get(str);
        }
        return notificationRecord;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<NotificationRecord> getSnoozed() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList();
            arrayList.addAll(this.mSnoozedNotifications.values());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void snooze(NotificationRecord notificationRecord, long j) {
        String key = notificationRecord.getKey();
        snooze(notificationRecord);
        scheduleRepost(key, j);
        Long valueOf = Long.valueOf(System.currentTimeMillis() + j);
        synchronized (this.mLock) {
            this.mPersistedSnoozedNotifications.put(getTrimmedString(key), valueOf);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void snooze(NotificationRecord notificationRecord, String str) {
        if (str != null) {
            synchronized (this.mLock) {
                this.mPersistedSnoozedNotificationsWithContext.put(getTrimmedString(notificationRecord.getKey()), getTrimmedString(str));
            }
        }
        snooze(notificationRecord);
    }

    private void snooze(NotificationRecord notificationRecord) {
        if (DEBUG) {
            Slog.d(TAG, "Snoozing " + notificationRecord.getKey());
        }
        synchronized (this.mLock) {
            this.mSnoozedNotifications.put(notificationRecord.getKey(), notificationRecord);
        }
    }

    private String getTrimmedString(String str) {
        return (str == null || str.length() <= 1000) ? str : str.substring(0, 1000);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean cancel(int i, String str, String str2, int i2) {
        synchronized (this.mLock) {
            for (Map.Entry<String, NotificationRecord> entry : this.mSnoozedNotifications.entrySet()) {
                StatusBarNotification sbn = entry.getValue().getSbn();
                if (sbn.getPackageName().equals(str) && sbn.getUserId() == i && Objects.equals(sbn.getTag(), str2) && sbn.getId() == i2) {
                    entry.getValue().isCanceled = true;
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancel(int i, boolean z) {
        synchronized (this.mLock) {
            if (this.mSnoozedNotifications.size() == 0) {
                return;
            }
            IntArray intArray = new IntArray();
            intArray.add(i);
            if (z) {
                intArray = this.mUserProfiles.getCurrentProfileIds();
            }
            for (NotificationRecord notificationRecord : this.mSnoozedNotifications.values()) {
                if (intArray.binarySearch(notificationRecord.getUserId()) >= 0) {
                    notificationRecord.isCanceled = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean cancel(int i, String str) {
        synchronized (this.mLock) {
            int size = this.mSnoozedNotifications.size();
            for (int i2 = 0; i2 < size; i2++) {
                NotificationRecord valueAt = this.mSnoozedNotifications.valueAt(i2);
                if (valueAt.getSbn().getPackageName().equals(str) && valueAt.getUserId() == i) {
                    valueAt.isCanceled = true;
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void update(int i, NotificationRecord notificationRecord) {
        synchronized (this.mLock) {
            if (this.mSnoozedNotifications.containsKey(notificationRecord.getKey())) {
                this.mSnoozedNotifications.put(notificationRecord.getKey(), notificationRecord);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void repost(String str, boolean z) {
        synchronized (this.mLock) {
            NotificationRecord notificationRecord = this.mSnoozedNotifications.get(str);
            if (notificationRecord != null) {
                repost(str, notificationRecord.getUserId(), z);
            }
        }
    }

    protected void repost(String str, int i, boolean z) {
        NotificationRecord remove;
        String trimmedString = getTrimmedString(str);
        synchronized (this.mLock) {
            this.mPersistedSnoozedNotifications.remove(trimmedString);
            this.mPersistedSnoozedNotificationsWithContext.remove(trimmedString);
            remove = this.mSnoozedNotifications.remove(str);
        }
        if (remove == null || remove.isCanceled) {
            return;
        }
        this.mAm.cancel(createPendingIntent(remove.getKey()));
        MetricsLogger.action(remove.getLogMaker().setCategory(831).setType(1));
        this.mCallback.repost(remove.getUserId(), remove, z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void repostGroupSummary(String str, int i, String str2) {
        String str3;
        synchronized (this.mLock) {
            int size = this.mSnoozedNotifications.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    str3 = null;
                    break;
                }
                NotificationRecord valueAt = this.mSnoozedNotifications.valueAt(i2);
                if (valueAt.getSbn().getPackageName().equals(str) && valueAt.getUserId() == i && valueAt.getSbn().isGroup() && valueAt.getNotification().isGroupSummary() && str2.equals(valueAt.getGroupKey())) {
                    str3 = valueAt.getKey();
                    break;
                }
                i2++;
            }
            if (str3 != null) {
                final NotificationRecord remove = this.mSnoozedNotifications.remove(str3);
                String trimmedString = getTrimmedString(str3);
                this.mPersistedSnoozedNotificationsWithContext.remove(trimmedString);
                this.mPersistedSnoozedNotifications.remove(trimmedString);
                if (remove != null && !remove.isCanceled) {
                    new Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            SnoozeHelper.this.lambda$repostGroupSummary$0(remove);
                        }
                    }.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repostGroupSummary$0(NotificationRecord notificationRecord) {
        MetricsLogger.action(notificationRecord.getLogMaker().setCategory(831).setType(1));
        this.mCallback.repost(notificationRecord.getUserId(), notificationRecord, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearData(int i, String str) {
        synchronized (this.mLock) {
            for (int size = this.mSnoozedNotifications.size() - 1; size >= 0; size--) {
                final NotificationRecord valueAt = this.mSnoozedNotifications.valueAt(size);
                if (valueAt.getUserId() == i && valueAt.getSbn().getPackageName().equals(str)) {
                    this.mSnoozedNotifications.removeAt(size);
                    String trimmedString = getTrimmedString(valueAt.getKey());
                    this.mPersistedSnoozedNotificationsWithContext.remove(trimmedString);
                    this.mPersistedSnoozedNotifications.remove(trimmedString);
                    new Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            SnoozeHelper.this.lambda$clearData$1(valueAt);
                        }
                    }.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearData$1(NotificationRecord notificationRecord) {
        this.mAm.cancel(createPendingIntent(notificationRecord.getKey()));
        MetricsLogger.action(notificationRecord.getLogMaker().setCategory(831).setType(5));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearData(int i) {
        synchronized (this.mLock) {
            for (int size = this.mSnoozedNotifications.size() - 1; size >= 0; size--) {
                final NotificationRecord valueAt = this.mSnoozedNotifications.valueAt(size);
                if (valueAt.getUserId() == i) {
                    this.mSnoozedNotifications.removeAt(size);
                    String trimmedString = getTrimmedString(valueAt.getKey());
                    this.mPersistedSnoozedNotificationsWithContext.remove(trimmedString);
                    this.mPersistedSnoozedNotifications.remove(trimmedString);
                    new Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            SnoozeHelper.this.lambda$clearData$2(valueAt);
                        }
                    }.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearData$2(NotificationRecord notificationRecord) {
        this.mAm.cancel(createPendingIntent(notificationRecord.getKey()));
        MetricsLogger.action(notificationRecord.getLogMaker().setCategory(831).setType(5));
    }

    private PendingIntent createPendingIntent(String str) {
        return PendingIntent.getBroadcast(this.mContext, 1, new Intent(REPOST_ACTION).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME).setData(new Uri.Builder().scheme(REPOST_SCHEME).appendPath(str).build()).addFlags(268435456).putExtra("key", str), 201326592);
    }

    public void scheduleRepostsForPersistedNotifications(long j) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mPersistedSnoozedNotifications.size(); i++) {
                String keyAt = this.mPersistedSnoozedNotifications.keyAt(i);
                Long valueAt = this.mPersistedSnoozedNotifications.valueAt(i);
                if (valueAt != null && valueAt.longValue() > j) {
                    scheduleRepostAtTime(keyAt, valueAt.longValue());
                }
            }
        }
    }

    private void scheduleRepost(String str, long j) {
        scheduleRepostAtTime(str, System.currentTimeMillis() + j);
    }

    private void scheduleRepostAtTime(final String str, final long j) {
        new Runnable() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                SnoozeHelper.this.lambda$scheduleRepostAtTime$3(str, j);
            }
        }.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRepostAtTime$3(String str, long j) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            PendingIntent createPendingIntent = createPendingIntent(str);
            this.mAm.cancel(createPendingIntent);
            if (DEBUG) {
                Slog.d(TAG, "Scheduling evaluate for " + new Date(j));
            }
            this.mAm.setExactAndAllowWhileIdle(0, j, createPendingIntent);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void dump(PrintWriter printWriter, NotificationManagerService.DumpFilter dumpFilter) {
        synchronized (this.mLock) {
            printWriter.println("\n  Snoozed notifications:");
            for (String str : this.mSnoozedNotifications.keySet()) {
                printWriter.print(INDENT);
                printWriter.println("key: " + str);
            }
            printWriter.println("\n Pending snoozed notifications");
            for (String str2 : this.mPersistedSnoozedNotifications.keySet()) {
                printWriter.print(INDENT);
                printWriter.println("key: " + str2 + " until: " + this.mPersistedSnoozedNotifications.get(str2));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void writeXml(final TypedXmlSerializer typedXmlSerializer) throws IOException {
        synchronized (this.mLock) {
            final long currentTimeMillis = System.currentTimeMillis();
            typedXmlSerializer.startTag((String) null, XML_TAG_NAME);
            writeXml(typedXmlSerializer, this.mPersistedSnoozedNotifications, XML_SNOOZED_NOTIFICATION, new Inserter() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda4
                @Override // com.android.server.notification.SnoozeHelper.Inserter
                public final void insert(Object obj) {
                    SnoozeHelper.lambda$writeXml$4(currentTimeMillis, typedXmlSerializer, (Long) obj);
                }
            });
            writeXml(typedXmlSerializer, this.mPersistedSnoozedNotificationsWithContext, XML_SNOOZED_NOTIFICATION_CONTEXT, new Inserter() { // from class: com.android.server.notification.SnoozeHelper$$ExternalSyntheticLambda5
                @Override // com.android.server.notification.SnoozeHelper.Inserter
                public final void insert(Object obj) {
                    typedXmlSerializer.attribute(null, SnoozeHelper.XML_SNOOZED_NOTIFICATION_CONTEXT_ID, (String) obj);
                }
            });
            typedXmlSerializer.endTag((String) null, XML_TAG_NAME);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$writeXml$4(long j, TypedXmlSerializer typedXmlSerializer, Long l) throws IOException {
        if (l.longValue() < j) {
            return;
        }
        typedXmlSerializer.attributeLong((String) null, XML_SNOOZED_NOTIFICATION_TIME, l.longValue());
    }

    private <T> void writeXml(TypedXmlSerializer typedXmlSerializer, ArrayMap<String, T> arrayMap, String str, Inserter<T> inserter) throws IOException {
        for (int i = 0; i < arrayMap.size(); i++) {
            String keyAt = arrayMap.keyAt(i);
            T valueAt = arrayMap.valueAt(i);
            typedXmlSerializer.startTag((String) null, str);
            inserter.insert(valueAt);
            typedXmlSerializer.attributeInt((String) null, XML_SNOOZED_NOTIFICATION_VERSION_LABEL, 1);
            typedXmlSerializer.attribute((String) null, "key", keyAt);
            typedXmlSerializer.endTag((String) null, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void readXml(TypedXmlPullParser typedXmlPullParser, long j) throws XmlPullParserException, IOException {
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            String name = typedXmlPullParser.getName();
            if (next == 3 && XML_TAG_NAME.equals(name)) {
                return;
            }
            if (next == 2 && (XML_SNOOZED_NOTIFICATION.equals(name) || name.equals(XML_SNOOZED_NOTIFICATION_CONTEXT))) {
                if (typedXmlPullParser.getAttributeInt((String) null, XML_SNOOZED_NOTIFICATION_VERSION_LABEL, -1) == 1) {
                    try {
                        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, "key");
                        if (name.equals(XML_SNOOZED_NOTIFICATION)) {
                            Long valueOf = Long.valueOf(typedXmlPullParser.getAttributeLong((String) null, XML_SNOOZED_NOTIFICATION_TIME, 0L));
                            if (valueOf.longValue() > j) {
                                synchronized (this.mLock) {
                                    this.mPersistedSnoozedNotifications.put(attributeValue, valueOf);
                                }
                            }
                        }
                        if (name.equals(XML_SNOOZED_NOTIFICATION_CONTEXT)) {
                            String attributeValue2 = typedXmlPullParser.getAttributeValue((String) null, XML_SNOOZED_NOTIFICATION_CONTEXT_ID);
                            synchronized (this.mLock) {
                                this.mPersistedSnoozedNotificationsWithContext.put(attributeValue, attributeValue2);
                            }
                        }
                    } catch (Exception e) {
                        Slog.e(TAG, "Exception in reading snooze data from policy xml", e);
                    }
                }
            }
        }
    }

    @VisibleForTesting
    void setAlarmManager(AlarmManager alarmManager) {
        this.mAm = alarmManager;
    }
}
