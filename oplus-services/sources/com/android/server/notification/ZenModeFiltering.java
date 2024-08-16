package com.android.server.notification;

import android.R;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.telecom.TelecomManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.server.pm.PackageManagerService;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ZenModeFiltering {
    private static final boolean DEBUG = ZenModeHelper.DEBUG;
    static final RepeatCallers REPEAT_CALLERS = new RepeatCallers();
    private static final String TAG = "ZenModeHelper";
    private final Context mContext;
    private ComponentName mDefaultPhoneApp;
    private final NotificationMessagingUtil mMessagingUtil;

    public ZenModeFiltering(Context context) {
        this.mContext = context;
        this.mMessagingUtil = new NotificationMessagingUtil(context);
    }

    public ZenModeFiltering(Context context, NotificationMessagingUtil notificationMessagingUtil) {
        this.mContext = context;
        this.mMessagingUtil = notificationMessagingUtil;
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("mDefaultPhoneApp=");
        printWriter.println(this.mDefaultPhoneApp);
        printWriter.print(str);
        printWriter.print("RepeatCallers.mThresholdMinutes=");
        RepeatCallers repeatCallers = REPEAT_CALLERS;
        printWriter.println(repeatCallers.mThresholdMinutes);
        synchronized (repeatCallers) {
            int i = 0;
            if (!repeatCallers.mTelCalls.isEmpty()) {
                printWriter.print(str);
                printWriter.println("RepeatCallers.mTelCalls=");
                int i2 = 0;
                while (true) {
                    RepeatCallers repeatCallers2 = REPEAT_CALLERS;
                    if (i2 >= repeatCallers2.mTelCalls.size()) {
                        break;
                    }
                    printWriter.print(str);
                    printWriter.print("  ");
                    printWriter.print((String) repeatCallers2.mTelCalls.keyAt(i2));
                    printWriter.print(" at ");
                    printWriter.println(ts(((Long) repeatCallers2.mTelCalls.valueAt(i2)).longValue()));
                    i2++;
                }
            }
            if (!REPEAT_CALLERS.mOtherCalls.isEmpty()) {
                printWriter.print(str);
                printWriter.println("RepeatCallers.mOtherCalls=");
                while (true) {
                    RepeatCallers repeatCallers3 = REPEAT_CALLERS;
                    if (i >= repeatCallers3.mOtherCalls.size()) {
                        break;
                    }
                    printWriter.print(str);
                    printWriter.print("  ");
                    printWriter.print((String) repeatCallers3.mOtherCalls.keyAt(i));
                    printWriter.print(" at ");
                    printWriter.println(ts(((Long) repeatCallers3.mOtherCalls.valueAt(i)).longValue()));
                    i++;
                }
            }
        }
    }

    private static String ts(long j) {
        return new Date(j) + " (" + j + ")";
    }

    public static boolean matchesCallFilter(Context context, int i, NotificationManager.Policy policy, UserHandle userHandle, Bundle bundle, ValidateNotificationPeople validateNotificationPeople, int i2, float f, int i3) {
        if (i == 2) {
            ZenLog.traceMatchesCallFilter(false, "no interruptions", i3);
            return false;
        }
        if (i == 3) {
            ZenLog.traceMatchesCallFilter(false, "alarms only", i3);
            return false;
        }
        if (i == 1) {
            if (policy.allowRepeatCallers() && REPEAT_CALLERS.isRepeat(context, bundle, null)) {
                ZenLog.traceMatchesCallFilter(true, "repeat caller", i3);
                return true;
            }
            if (!policy.allowCalls()) {
                ZenLog.traceMatchesCallFilter(false, "calls not allowed", i3);
                return false;
            }
            if (validateNotificationPeople != null) {
                float contactAffinity = validateNotificationPeople.getContactAffinity(userHandle, bundle, i2, f);
                boolean audienceMatches = audienceMatches(policy.allowCallsFrom(), contactAffinity);
                ZenLog.traceMatchesCallFilter(audienceMatches, "contact affinity " + contactAffinity, i3);
                return audienceMatches;
            }
        }
        ZenLog.traceMatchesCallFilter(true, "no restrictions", i3);
        return true;
    }

    private static Bundle extras(NotificationRecord notificationRecord) {
        if (notificationRecord == null || notificationRecord.getSbn() == null || notificationRecord.getSbn().getNotification() == null) {
            return null;
        }
        return notificationRecord.getSbn().getNotification().extras;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void recordCall(NotificationRecord notificationRecord) {
        REPEAT_CALLERS.recordCall(this.mContext, extras(notificationRecord), notificationRecord.getPhoneNumbers());
    }

    public boolean shouldIntercept(int i, NotificationManager.Policy policy, NotificationRecord notificationRecord) {
        if (i == 0) {
            return false;
        }
        if (isCritical(notificationRecord)) {
            maybeLogInterceptDecision(notificationRecord, false, "criticalNotification");
            return false;
        }
        if (NotificationManager.Policy.areAllVisualEffectsSuppressed(policy.suppressedVisualEffects) && PackageManagerService.PLATFORM_PACKAGE_NAME.equals(notificationRecord.getSbn().getPackageName()) && 48 == notificationRecord.getSbn().getId()) {
            maybeLogInterceptDecision(notificationRecord, false, "systemDndChangedNotification");
            return false;
        }
        if (i != 1) {
            if (i == 2) {
                maybeLogInterceptDecision(notificationRecord, true, "none");
                return true;
            }
            if (i == 3) {
                if (isAlarm(notificationRecord)) {
                    maybeLogInterceptDecision(notificationRecord, false, "alarm");
                    return false;
                }
                maybeLogInterceptDecision(notificationRecord, true, "alarmsOnly");
                return true;
            }
            maybeLogInterceptDecision(notificationRecord, false, "unknownZenMode");
            return false;
        }
        if (notificationRecord.getPackagePriority() == 2) {
            maybeLogInterceptDecision(notificationRecord, false, "priorityApp");
            return false;
        }
        if (isAlarm(notificationRecord)) {
            if (!policy.allowAlarms()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowAlarms");
                return true;
            }
            maybeLogInterceptDecision(notificationRecord, false, "allowedAlarm");
            return false;
        }
        if (isEvent(notificationRecord)) {
            if (!policy.allowEvents()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowEvents");
                return true;
            }
            maybeLogInterceptDecision(notificationRecord, false, "allowedEvent");
            return false;
        }
        if (isReminder(notificationRecord)) {
            if (!policy.allowReminders()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowReminders");
                return true;
            }
            maybeLogInterceptDecision(notificationRecord, false, "allowedReminder");
            return false;
        }
        if (isMedia(notificationRecord)) {
            if (!policy.allowMedia()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowMedia");
                return true;
            }
            maybeLogInterceptDecision(notificationRecord, false, "allowedMedia");
            return false;
        }
        if (isSystem(notificationRecord)) {
            if (!policy.allowSystem()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowSystem");
                return true;
            }
            maybeLogInterceptDecision(notificationRecord, false, "allowedSystem");
            return false;
        }
        if (isConversation(notificationRecord) && policy.allowConversations()) {
            int i2 = policy.priorityConversationSenders;
            if (i2 == 1) {
                maybeLogInterceptDecision(notificationRecord, false, "conversationAnyone");
                return false;
            }
            if (i2 == 2 && notificationRecord.getChannel().isImportantConversation()) {
                maybeLogInterceptDecision(notificationRecord, false, "conversationMatches");
                return false;
            }
        }
        if (isCall(notificationRecord)) {
            if (policy.allowRepeatCallers() && REPEAT_CALLERS.isRepeat(this.mContext, extras(notificationRecord), notificationRecord.getPhoneNumbers())) {
                maybeLogInterceptDecision(notificationRecord, false, "repeatCaller");
                return false;
            }
            if (!policy.allowCalls()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowCalls");
                return true;
            }
            return shouldInterceptAudience(policy.allowCallsFrom(), notificationRecord);
        }
        if (isMessage(notificationRecord)) {
            if (!policy.allowMessages()) {
                maybeLogInterceptDecision(notificationRecord, true, "!allowMessages");
                return true;
            }
            return shouldInterceptAudience(policy.allowMessagesFrom(), notificationRecord);
        }
        maybeLogInterceptDecision(notificationRecord, true, "!priority");
        return true;
    }

    private static void maybeLogInterceptDecision(NotificationRecord notificationRecord, boolean z, String str) {
        boolean isIntercepted = notificationRecord.isIntercepted();
        if (notificationRecord.hasInterceptBeenSet() && isIntercepted == z) {
            return;
        }
        if (!notificationRecord.hasInterceptBeenSet()) {
            str = "new:" + str;
        } else if (isIntercepted != z) {
            str = "updated:" + str;
        }
        if (z) {
            ZenLog.traceIntercepted(notificationRecord, str);
        } else {
            ZenLog.traceNotIntercepted(notificationRecord, str);
        }
    }

    private boolean isCritical(NotificationRecord notificationRecord) {
        return notificationRecord.getCriticality() < 2;
    }

    private static boolean shouldInterceptAudience(int i, NotificationRecord notificationRecord) {
        float contactAffinity = notificationRecord.getContactAffinity();
        if (!audienceMatches(i, contactAffinity)) {
            maybeLogInterceptDecision(notificationRecord, true, "!audienceMatches,affinity=" + contactAffinity);
            return true;
        }
        maybeLogInterceptDecision(notificationRecord, false, "affinity=" + contactAffinity);
        return false;
    }

    protected static boolean isAlarm(NotificationRecord notificationRecord) {
        return notificationRecord.isCategory("alarm") || notificationRecord.isAudioAttributesUsage(4);
    }

    private static boolean isEvent(NotificationRecord notificationRecord) {
        return notificationRecord.isCategory("event");
    }

    private static boolean isReminder(NotificationRecord notificationRecord) {
        return notificationRecord.isCategory("reminder");
    }

    public boolean isCall(NotificationRecord notificationRecord) {
        return notificationRecord != null && (isDefaultPhoneApp(notificationRecord.getSbn().getPackageName()) || notificationRecord.isCategory("call"));
    }

    public boolean isMedia(NotificationRecord notificationRecord) {
        AudioAttributes audioAttributes = notificationRecord.getAudioAttributes();
        return audioAttributes != null && AudioAttributes.SUPPRESSIBLE_USAGES.get(audioAttributes.getUsage()) == 5;
    }

    public boolean isSystem(NotificationRecord notificationRecord) {
        AudioAttributes audioAttributes = notificationRecord.getAudioAttributes();
        return audioAttributes != null && AudioAttributes.SUPPRESSIBLE_USAGES.get(audioAttributes.getUsage()) == 6;
    }

    private boolean isDefaultPhoneApp(String str) {
        ComponentName componentName;
        if (this.mDefaultPhoneApp == null) {
            TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService("telecom");
            this.mDefaultPhoneApp = telecomManager != null ? telecomManager.getDefaultPhoneApp() : null;
            if (DEBUG) {
                Slog.d(TAG, "Default phone app: " + this.mDefaultPhoneApp);
            }
        }
        return (str == null || (componentName = this.mDefaultPhoneApp) == null || !str.equals(componentName.getPackageName())) ? false : true;
    }

    protected boolean isMessage(NotificationRecord notificationRecord) {
        return this.mMessagingUtil.isMessaging(notificationRecord.getSbn());
    }

    protected boolean isConversation(NotificationRecord notificationRecord) {
        return notificationRecord.isConversation();
    }

    private static boolean audienceMatches(int i, float f) {
        if (i == 0) {
            return true;
        }
        if (i == 1) {
            return f >= 0.5f;
        }
        if (i == 2) {
            return f >= 1.0f;
        }
        Slog.w(TAG, "Encountered unknown source: " + i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cleanUpCallersAfter(long j) {
        REPEAT_CALLERS.cleanUpCallsAfter(j);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class RepeatCallers {
        private final ArrayMap<String, Long> mOtherCalls;
        private final ArrayMap<String, Long> mTelCalls;
        private int mThresholdMinutes;

        private RepeatCallers() {
            this.mTelCalls = new ArrayMap<>();
            this.mOtherCalls = new ArrayMap<>();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void recordCall(Context context, Bundle bundle, ArraySet<String> arraySet) {
            setThresholdMinutes(context);
            if (this.mThresholdMinutes > 0 && bundle != null) {
                String[] extraPeople = ValidateNotificationPeople.getExtraPeople(bundle);
                if (extraPeople != null && extraPeople.length != 0) {
                    long currentTimeMillis = System.currentTimeMillis();
                    cleanUp(this.mTelCalls, currentTimeMillis);
                    cleanUp(this.mOtherCalls, currentTimeMillis);
                    recordCallers(extraPeople, arraySet, currentTimeMillis);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isRepeat(Context context, Bundle bundle, ArraySet<String> arraySet) {
            setThresholdMinutes(context);
            if (this.mThresholdMinutes > 0 && bundle != null) {
                String[] extraPeople = ValidateNotificationPeople.getExtraPeople(bundle);
                if (extraPeople != null && extraPeople.length != 0) {
                    long currentTimeMillis = System.currentTimeMillis();
                    cleanUp(this.mTelCalls, currentTimeMillis);
                    cleanUp(this.mOtherCalls, currentTimeMillis);
                    return checkCallers(context, extraPeople, arraySet);
                }
                return false;
            }
            return false;
        }

        private synchronized void cleanUp(ArrayMap<String, Long> arrayMap, long j) {
            for (int size = arrayMap.size() - 1; size >= 0; size--) {
                long longValue = arrayMap.valueAt(size).longValue();
                if (longValue > j || j - longValue > this.mThresholdMinutes * 1000 * 60) {
                    arrayMap.removeAt(size);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void cleanUpCallsAfter(long j) {
            for (int size = this.mTelCalls.size() - 1; size >= 0; size--) {
                if (this.mTelCalls.valueAt(size).longValue() > j) {
                    this.mTelCalls.removeAt(size);
                }
            }
            for (int size2 = this.mOtherCalls.size() - 1; size2 >= 0; size2--) {
                if (this.mOtherCalls.valueAt(size2).longValue() > j) {
                    this.mOtherCalls.removeAt(size2);
                }
            }
        }

        private void setThresholdMinutes(Context context) {
            if (this.mThresholdMinutes <= 0) {
                this.mThresholdMinutes = context.getResources().getInteger(R.integer.timepicker_title_visibility);
            }
        }

        private synchronized void recordCallers(String[] strArr, ArraySet<String> arraySet, long j) {
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            for (String str : strArr) {
                if (str != null) {
                    Uri parse = Uri.parse(str);
                    if ("tel".equals(parse.getScheme())) {
                        String decode = Uri.decode(parse.getSchemeSpecificPart());
                        if (decode != null) {
                            this.mTelCalls.put(decode, Long.valueOf(j));
                            z = true;
                            z2 = true;
                        }
                    } else {
                        this.mOtherCalls.put(str, Long.valueOf(j));
                        z = true;
                        z3 = true;
                    }
                }
            }
            if (arraySet != null) {
                Iterator<String> it = arraySet.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (next != null) {
                        this.mTelCalls.put(next, Long.valueOf(j));
                        z = true;
                        z2 = true;
                    }
                }
            }
            if (z) {
                ZenLog.traceRecordCaller(z2, z3);
            }
        }

        private synchronized boolean checkForNumber(String str, String str2) {
            if (this.mTelCalls.containsKey(str)) {
                return true;
            }
            String decode = Uri.decode(str);
            if (decode != null) {
                Iterator<String> it = this.mTelCalls.keySet().iterator();
                while (it.hasNext()) {
                    if (PhoneNumberUtils.areSamePhoneNumber(decode, it.next(), str2)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private synchronized boolean checkCallers(Context context, String[] strArr, ArraySet<String> arraySet) {
            boolean z;
            String networkCountryIso = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).getNetworkCountryIso();
            z = false;
            boolean z2 = false;
            boolean z3 = false;
            for (String str : strArr) {
                if (str != null) {
                    Uri parse = Uri.parse(str);
                    if ("tel".equals(parse.getScheme())) {
                        if (checkForNumber(parse.getSchemeSpecificPart(), networkCountryIso)) {
                            z = true;
                        }
                        z2 = true;
                    } else if (this.mOtherCalls.containsKey(str)) {
                        z = true;
                        z3 = true;
                    } else {
                        z3 = true;
                    }
                }
            }
            if (arraySet != null) {
                Iterator<String> it = arraySet.iterator();
                while (it.hasNext()) {
                    if (checkForNumber(it.next(), networkCountryIso)) {
                        z = true;
                    }
                    z2 = true;
                }
            }
            ZenLog.traceCheckRepeatCaller(z, z2, z3);
            return z;
        }
    }
}
