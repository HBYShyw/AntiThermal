package com.android.server.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.ZenModeConfig;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import com.android.server.notification.CalendarTracker;
import com.android.server.notification.NotificationManagerService;
import com.android.server.pm.PackageManagerService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class EventConditionProvider extends SystemConditionProviderService {
    private static final String ACTION_EVALUATE;
    private static final long CHANGE_DELAY = 2000;
    private static final String EXTRA_TIME = "time";
    private static final String NOT_SHOWN = "...";
    private static final int REQUEST_CODE_EVALUATE = 1;
    private static final String SIMPLE_NAME;
    private static final String TAG = "ConditionProviders.ECP";
    private boolean mBootComplete;
    private boolean mConnected;
    private long mNextAlarmTime;
    private boolean mRegistered;
    private final HandlerThread mThread;
    private final Handler mWorker;
    private static final boolean DEBUG = Log.isLoggable("ConditionProviders", 3);
    public static final ComponentName COMPONENT = new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, EventConditionProvider.class.getName());
    private final Context mContext = this;
    private final ArraySet<Uri> mSubscriptions = new ArraySet<>();
    private final SparseArray<CalendarTracker> mTrackers = new SparseArray<>();
    private final CalendarTracker.Callback mTrackerCallback = new CalendarTracker.Callback() { // from class: com.android.server.notification.EventConditionProvider.2
        @Override // com.android.server.notification.CalendarTracker.Callback
        public void onChanged() {
            if (EventConditionProvider.DEBUG) {
                Slog.d(EventConditionProvider.TAG, "mTrackerCallback.onChanged");
            }
            EventConditionProvider.this.mWorker.removeCallbacks(EventConditionProvider.this.mEvaluateSubscriptionsW);
            EventConditionProvider.this.mWorker.postDelayed(EventConditionProvider.this.mEvaluateSubscriptionsW, EventConditionProvider.CHANGE_DELAY);
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.server.notification.EventConditionProvider.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (EventConditionProvider.DEBUG) {
                Slog.d(EventConditionProvider.TAG, "onReceive " + intent.getAction());
            }
            EventConditionProvider.this.evaluateSubscriptions();
        }
    };
    private final Runnable mEvaluateSubscriptionsW = new Runnable() { // from class: com.android.server.notification.EventConditionProvider.4
        @Override // java.lang.Runnable
        public void run() {
            EventConditionProvider.this.evaluateSubscriptionsW();
        }
    };

    static {
        String simpleName = EventConditionProvider.class.getSimpleName();
        SIMPLE_NAME = simpleName;
        ACTION_EVALUATE = simpleName + ".EVALUATE";
    }

    public EventConditionProvider() {
        if (DEBUG) {
            Slog.d(TAG, "new " + SIMPLE_NAME + "()");
        }
        HandlerThread handlerThread = new HandlerThread(TAG, 10);
        this.mThread = handlerThread;
        handlerThread.start();
        this.mWorker = new Handler(handlerThread.getLooper());
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public ComponentName getComponent() {
        return COMPONENT;
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public boolean isValidConditionId(Uri uri) {
        return ZenModeConfig.isValidEventConditionId(uri);
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
        printWriter.print("      mBootComplete=");
        printWriter.println(this.mBootComplete);
        SystemConditionProviderService.dumpUpcomingTime(printWriter, "mNextAlarmTime", this.mNextAlarmTime, System.currentTimeMillis());
        synchronized (this.mSubscriptions) {
            printWriter.println("      mSubscriptions=");
            Iterator<Uri> it = this.mSubscriptions.iterator();
            while (it.hasNext()) {
                Uri next = it.next();
                printWriter.print("        ");
                printWriter.println(next);
            }
        }
        printWriter.println("      mTrackers=");
        for (int i = 0; i < this.mTrackers.size(); i++) {
            printWriter.print("        user=");
            printWriter.println(this.mTrackers.keyAt(i));
            this.mTrackers.valueAt(i).dump("          ", printWriter);
        }
    }

    @Override // com.android.server.notification.SystemConditionProviderService
    public void onBootComplete() {
        if (DEBUG) {
            Slog.d(TAG, "onBootComplete");
        }
        if (this.mBootComplete) {
            return;
        }
        this.mBootComplete = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_REMOVED");
        this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.notification.EventConditionProvider.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                EventConditionProvider.this.reloadTrackers();
            }
        }, intentFilter);
        reloadTrackers();
    }

    @Override // android.service.notification.ConditionProviderService
    public void onConnected() {
        if (DEBUG) {
            Slog.d(TAG, "onConnected");
        }
        this.mConnected = true;
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
        if (!ZenModeConfig.isValidEventConditionId(uri)) {
            notifyCondition(createCondition(uri, 0));
            return;
        }
        synchronized (this.mSubscriptions) {
            if (this.mSubscriptions.add(uri)) {
                evaluateSubscriptions();
            }
        }
    }

    @Override // android.service.notification.ConditionProviderService
    public void onUnsubscribe(Uri uri) {
        if (DEBUG) {
            Slog.d(TAG, "onUnsubscribe " + uri);
        }
        synchronized (this.mSubscriptions) {
            if (this.mSubscriptions.remove(uri)) {
                evaluateSubscriptions();
            }
        }
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
    public void reloadTrackers() {
        if (DEBUG) {
            Slog.d(TAG, "reloadTrackers");
        }
        for (int i = 0; i < this.mTrackers.size(); i++) {
            this.mTrackers.valueAt(i).setCallback(null);
        }
        this.mTrackers.clear();
        for (UserHandle userHandle : UserManager.get(this.mContext).getUserProfiles()) {
            Context contextForUser = userHandle.isSystem() ? this.mContext : getContextForUser(this.mContext, userHandle);
            if (contextForUser == null) {
                Slog.w(TAG, "Unable to create context for user " + userHandle.getIdentifier());
            } else {
                this.mTrackers.put(userHandle.getIdentifier(), new CalendarTracker(this.mContext, contextForUser));
            }
        }
        evaluateSubscriptions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSubscriptions() {
        if (this.mWorker.hasCallbacks(this.mEvaluateSubscriptionsW)) {
            return;
        }
        this.mWorker.post(this.mEvaluateSubscriptionsW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void evaluateSubscriptionsW() {
        Iterator<Uri> it;
        CalendarTracker.CheckEventResult checkEventResult;
        Iterator<Uri> it2;
        boolean z = DEBUG;
        if (z) {
            Slog.d(TAG, "evaluateSubscriptions");
        }
        if (!this.mBootComplete) {
            if (z) {
                Slog.d(TAG, "Skipping evaluate before boot complete");
                return;
            }
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList<Condition> arrayList = new ArrayList();
        synchronized (this.mSubscriptions) {
            int i = 0;
            for (int i2 = 0; i2 < this.mTrackers.size(); i2++) {
                this.mTrackers.valueAt(i2).setCallback(this.mSubscriptions.isEmpty() ? null : this.mTrackerCallback);
            }
            setRegistered(!this.mSubscriptions.isEmpty());
            Iterator<Uri> it3 = this.mSubscriptions.iterator();
            long j = 0;
            while (it3.hasNext()) {
                Uri next = it3.next();
                ZenModeConfig.EventInfo tryParseEventConditionId = ZenModeConfig.tryParseEventConditionId(next);
                if (tryParseEventConditionId == null) {
                    arrayList.add(createCondition(next, i));
                    it = it3;
                } else {
                    if (tryParseEventConditionId.calName == null) {
                        int i3 = i;
                        checkEventResult = null;
                        while (i3 < this.mTrackers.size()) {
                            CalendarTracker.CheckEventResult checkEvent = this.mTrackers.valueAt(i3).checkEvent(tryParseEventConditionId, currentTimeMillis);
                            if (checkEventResult == null) {
                                it2 = it3;
                                checkEventResult = checkEvent;
                            } else {
                                checkEventResult.inEvent |= checkEvent.inEvent;
                                it2 = it3;
                                checkEventResult.recheckAt = Math.min(checkEventResult.recheckAt, checkEvent.recheckAt);
                            }
                            i3++;
                            it3 = it2;
                        }
                        it = it3;
                    } else {
                        it = it3;
                        int resolveUserId = ZenModeConfig.EventInfo.resolveUserId(tryParseEventConditionId.userId);
                        CalendarTracker calendarTracker = this.mTrackers.get(resolveUserId);
                        if (calendarTracker == null) {
                            Slog.w(TAG, "No calendar tracker found for user " + resolveUserId);
                            arrayList.add(createCondition(next, 0));
                        } else {
                            checkEventResult = calendarTracker.checkEvent(tryParseEventConditionId, currentTimeMillis);
                        }
                    }
                    long j2 = checkEventResult.recheckAt;
                    if (j2 != 0 && (j == 0 || j2 < j)) {
                        j = j2;
                    }
                    if (!checkEventResult.inEvent) {
                        i = 0;
                        arrayList.add(createCondition(next, 0));
                    } else {
                        i = 0;
                        arrayList.add(createCondition(next, 1));
                    }
                    it3 = it;
                }
                it3 = it;
                i = 0;
            }
            rescheduleAlarm(currentTimeMillis, j);
        }
        for (Condition condition : arrayList) {
            if (condition != null) {
                notifyCondition(condition);
            }
        }
        if (DEBUG) {
            Slog.d(TAG, "evaluateSubscriptions took " + (System.currentTimeMillis() - currentTimeMillis));
        }
    }

    private void rescheduleAlarm(long j, long j2) {
        this.mNextAlarmTime = j2;
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        PendingIntent pendingIntent = getPendingIntent(j2);
        alarmManager.cancel(pendingIntent);
        if (j2 == 0 || j2 < j) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not scheduling evaluate: ");
                sb.append(j2 == 0 ? "no time specified" : "specified time in the past");
                Slog.d(TAG, sb.toString());
                return;
            }
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, String.format("Scheduling evaluate for %s, in %s, now=%s", SystemConditionProviderService.ts(j2), SystemConditionProviderService.formatDuration(j2 - j), SystemConditionProviderService.ts(j)));
        }
        alarmManager.setExact(0, j2, pendingIntent);
    }

    PendingIntent getPendingIntent(long j) {
        return PendingIntent.getBroadcast(this.mContext, 1, new Intent(ACTION_EVALUATE).addFlags(268435456).setPackage(PackageManagerService.PLATFORM_PACKAGE_NAME).putExtra(EXTRA_TIME, j), 201326592);
    }

    private Condition createCondition(Uri uri, int i) {
        return new Condition(uri, NOT_SHOWN, NOT_SHOWN, NOT_SHOWN, 0, i, 2);
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
            registerReceiver(this.mReceiver, intentFilter, 2);
            return;
        }
        unregisterReceiver(this.mReceiver);
    }

    private static Context getContextForUser(Context context, UserHandle userHandle) {
        try {
            return context.createPackageContextAsUser(context.getPackageName(), 0, userHandle);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }
}
