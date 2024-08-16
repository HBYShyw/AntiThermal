package com.android.server.notification;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.provider.Settings;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.ScheduleCalendar;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.notification.NotificationManagerService;
import com.android.server.pm.PackageManagerService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ScheduleConditionProvider extends SystemConditionProviderService {
    private static final String ACTION_EVALUATE;
    public static final ComponentName COMPONENT = new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, ScheduleConditionProvider.class.getName());
    static final boolean DEBUG = true;
    private static final String EXTRA_TIME = "time";
    private static final String NOT_SHOWN = "...";
    private static final int REQUEST_CODE_EVALUATE = 1;
    private static final String SCP_SETTING = "snoozed_schedule_condition_provider";
    private static final String SEPARATOR = ";";
    private static final String SIMPLE_NAME;
    static final String TAG = "ConditionProviders.SCP";
    private AlarmManager mAlarmManager;
    private boolean mConnected;
    private long mNextAlarmTime;
    private boolean mRegistered;
    private final Context mContext = this;
    private final ArrayMap<Uri, ScheduleCalendar> mSubscriptions = new ArrayMap<>();
    private ArraySet<Uri> mSnoozedForAlarm = new ArraySet<>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.ScheduleConditionProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (ScheduleConditionProvider.DEBUG) {
                Slog.d(ScheduleConditionProvider.TAG, "onReceive " + intent.getAction());
            }
            if ("android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction())) {
                synchronized (ScheduleConditionProvider.this.mSubscriptions) {
                    Iterator it = ScheduleConditionProvider.this.mSubscriptions.keySet().iterator();
                    while (it.hasNext()) {
                        ScheduleCalendar scheduleCalendar = (ScheduleCalendar) ScheduleConditionProvider.this.mSubscriptions.get((Uri) it.next());
                        if (scheduleCalendar != null) {
                            scheduleCalendar.setTimeZone(Calendar.getInstance().getTimeZone());
                        }
                    }
                }
            }
            ScheduleConditionProvider.this.evaluateSubscriptions();
        }
    };

    @Override // com.android.server.notification.SystemConditionProviderService
    public void onBootComplete() {
    }

    static {
        String simpleName = ScheduleConditionProvider.class.getSimpleName();
        SIMPLE_NAME = simpleName;
        ACTION_EVALUATE = simpleName + ".EVALUATE";
    }

    public ScheduleConditionProvider() {
        if (DEBUG) {
            Slog.d(TAG, "new " + SIMPLE_NAME + "()");
        }
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public ComponentName getComponent() {
        return COMPONENT;
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public boolean isValidConditionId(Uri uri) {
        return ZenModeConfig.isValidScheduleConditionId(uri);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void dump(PrintWriter printWriter, NotificationManagerService.DumpFilter dumpFilter) {
        printWriter.print("    ");
        printWriter.print(SIMPLE_NAME);
        printWriter.println(":");
        printWriter.print("      mConnected=");
        printWriter.println(this.mConnected);
        printWriter.print("      mRegistered=");
        printWriter.println(this.mRegistered);
        printWriter.println("      mSubscriptions=");
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mSubscriptions) {
            for (Uri uri : this.mSubscriptions.keySet()) {
                printWriter.print("        ");
                printWriter.print(meetsSchedule(this.mSubscriptions.get(uri), currentTimeMillis) ? "* " : "  ");
                printWriter.println(uri);
                printWriter.print("            ");
                printWriter.println(this.mSubscriptions.get(uri).toString());
            }
        }
        printWriter.println("      snoozed due to alarm: " + TextUtils.join(SEPARATOR, this.mSnoozedForAlarm));
        SystemConditionProviderService.dumpUpcomingTime(printWriter, "mNextAlarmTime", this.mNextAlarmTime, currentTimeMillis);
    }

    @Override // android.service.notification.ConditionProviderService
    public void onConnected() {
        if (DEBUG) {
            Slog.d(TAG, "onConnected");
        }
        this.mConnected = true;
        readSnoozed();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            Slog.d(TAG, "onDestroy");
        }
        this.mConnected = false;
    }

    @Override // android.service.notification.ConditionProviderService
    public void onSubscribe(Uri uri) {
        if (DEBUG) {
            Slog.d(TAG, "onSubscribe " + uri);
        }
        if (!ZenModeConfig.isValidScheduleConditionId(uri)) {
            notifyCondition(createCondition(uri, 3, "invalidId"));
            return;
        }
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.put(uri, ZenModeConfig.toScheduleCalendar(uri));
        }
        evaluateSubscriptions();
    }

    @Override // android.service.notification.ConditionProviderService
    public void onUnsubscribe(Uri uri) {
        if (DEBUG) {
            Slog.d(TAG, "onUnsubscribe " + uri);
        }
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.remove(uri);
        }
        removeSnoozed(uri);
        evaluateSubscriptions();
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void attachBase(Context context) {
        attachBaseContext(context);
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public IConditionProvider asInterface() {
        return onBind(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSubscriptions() {
        if (this.mAlarmManager == null) {
            this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        }
        long currentTimeMillis = System.currentTimeMillis();
        this.mNextAlarmTime = 0L;
        long nextAlarm = getNextAlarm();
        ArrayList arrayList = new ArrayList();
        synchronized (this.mSubscriptions) {
            setRegistered(!this.mSubscriptions.isEmpty());
            for (Uri uri : this.mSubscriptions.keySet()) {
                Condition evaluateSubscriptionLocked = evaluateSubscriptionLocked(uri, this.mSubscriptions.get(uri), currentTimeMillis, nextAlarm);
                if (evaluateSubscriptionLocked != null) {
                    arrayList.add(evaluateSubscriptionLocked);
                }
            }
        }
        notifyConditions((Condition[]) arrayList.toArray(new Condition[arrayList.size()]));
        updateAlarm(currentTimeMillis, this.mNextAlarmTime);
    }

    @GuardedBy({"mSubscriptions"})
    @VisibleForTesting
    Condition evaluateSubscriptionLocked(Uri uri, ScheduleCalendar scheduleCalendar, long j, long j2) {
        Condition createCondition;
        Condition condition;
        long nextChangeTime;
        long j3;
        if (DEBUG) {
            Slog.d(TAG, String.format("evaluateSubscriptionLocked cal=%s, now=%s, nextUserAlarmTime=%s", scheduleCalendar, SystemConditionProviderService.ts(j), SystemConditionProviderService.ts(j2)));
        }
        if (scheduleCalendar == null) {
            Condition createCondition2 = createCondition(uri, 3, "!invalidId");
            removeSnoozed(uri);
            return createCondition2;
        }
        if (scheduleCalendar.isInSchedule(j)) {
            if (conditionSnoozed(uri)) {
                condition = createCondition(uri, 0, "snoozed");
            } else if (scheduleCalendar.shouldExitForAlarm(j)) {
                createCondition = createCondition(uri, 0, "alarmCanceled");
                addSnoozed(uri);
            } else {
                condition = createCondition(uri, 1, "meetsSchedule");
            }
            scheduleCalendar.maybeSetNextAlarm(j, j2);
            nextChangeTime = scheduleCalendar.getNextChangeTime(j);
            if (nextChangeTime > 0 && nextChangeTime > j) {
                j3 = this.mNextAlarmTime;
                if (j3 != 0 || nextChangeTime < j3) {
                    this.mNextAlarmTime = nextChangeTime;
                }
            }
            return condition;
        }
        createCondition = createCondition(uri, 0, "!meetsSchedule");
        removeSnoozed(uri);
        condition = createCondition;
        scheduleCalendar.maybeSetNextAlarm(j, j2);
        nextChangeTime = scheduleCalendar.getNextChangeTime(j);
        if (nextChangeTime > 0) {
            j3 = this.mNextAlarmTime;
            if (j3 != 0) {
            }
            this.mNextAlarmTime = nextChangeTime;
        }
        return condition;
    }

    private void updateAlarm(long j, long j2) {
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        PendingIntent pendingIntent = getPendingIntent(j2);
        alarmManager.cancel(pendingIntent);
        if (j2 > j) {
            if (DEBUG) {
                Slog.d(TAG, String.format("Scheduling evaluate for %s, in %s, now=%s", SystemConditionProviderService.ts(j2), SystemConditionProviderService.formatDuration(j2 - j), SystemConditionProviderService.ts(j)));
            }
            alarmManager.setExact(0, j2, pendingIntent);
        } else if (DEBUG) {
            Slog.d(TAG, "Not scheduling evaluate");
        }
    }

    @VisibleForTesting
    PendingIntent getPendingIntent(long j) {
        return PendingIntent.getBroadcast(this.mContext, 1, new Intent(ACTION_EVALUATE).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME).addFlags(268435456).putExtra(EXTRA_TIME, j), 201326592);
    }

    public long getNextAlarm() {
        AlarmManager.AlarmClockInfo nextAlarmClock = this.mAlarmManager.getNextAlarmClock(ActivityManager.getCurrentUser());
        if (nextAlarmClock != null) {
            return nextAlarmClock.getTriggerTime();
        }
        return 0L;
    }

    private boolean meetsSchedule(ScheduleCalendar scheduleCalendar, long j) {
        return scheduleCalendar != null && scheduleCalendar.isInSchedule(j);
    }

    private void setRegistered(boolean z) {
        if (this.mRegistered == z) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, "setRegistered " + z);
        }
        this.mRegistered = z;
        if (z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction(ACTION_EVALUATE);
            intentFilter.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
            registerReceiver(this.mReceiver, intentFilter, 2);
            return;
        }
        unregisterReceiver(this.mReceiver);
    }

    private Condition createCondition(Uri uri, int i, String str) {
        if (DEBUG) {
            Slog.d(TAG, "notifyCondition " + uri + " " + Condition.stateToString(i) + " reason=" + str);
        }
        return new Condition(uri, NOT_SHOWN, NOT_SHOWN, NOT_SHOWN, 0, i, 2);
    }

    private boolean conditionSnoozed(Uri uri) {
        boolean contains;
        synchronized (this.mSnoozedForAlarm) {
            contains = this.mSnoozedForAlarm.contains(uri);
        }
        return contains;
    }

    @VisibleForTesting
    void addSnoozed(Uri uri) {
        synchronized (this.mSnoozedForAlarm) {
            this.mSnoozedForAlarm.add(uri);
            saveSnoozedLocked();
        }
    }

    private void removeSnoozed(Uri uri) {
        synchronized (this.mSnoozedForAlarm) {
            this.mSnoozedForAlarm.remove(uri);
            saveSnoozedLocked();
        }
    }

    private void saveSnoozedLocked() {
        Settings.Secure.putStringForUser(this.mContext.getContentResolver(), SCP_SETTING, TextUtils.join(SEPARATOR, this.mSnoozedForAlarm), ActivityManager.getCurrentUser());
    }

    private void readSnoozed() {
        synchronized (this.mSnoozedForAlarm) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), SCP_SETTING, ActivityManager.getCurrentUser());
                if (stringForUser != null) {
                    String[] split = stringForUser.split(SEPARATOR);
                    for (int i = 0; i < split.length; i++) {
                        String str = split[i];
                        if (str != null) {
                            str = str.trim();
                        }
                        if (!TextUtils.isEmpty(str)) {
                            this.mSnoozedForAlarm.add(Uri.parse(str));
                        }
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
}
