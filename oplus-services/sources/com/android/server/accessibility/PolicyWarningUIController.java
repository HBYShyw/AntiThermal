package com.android.server.accessibility;

import android.R;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.accessibility.util.AccessibilityStatsLogUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.ImageUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.accessibility.PolicyWarningUIController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PolicyWarningUIController {

    @VisibleForTesting
    protected static final String ACTION_A11Y_SETTINGS;

    @VisibleForTesting
    protected static final String ACTION_DISMISS_NOTIFICATION;

    @VisibleForTesting
    protected static final String ACTION_SEND_NOTIFICATION;
    private static final String EXTRA_TIME_FOR_LOGGING = "start_time_to_log_a11y_tool";
    private static final int SEND_NOTIFICATION_DELAY_HOURS = 24;
    private static final String TAG = "PolicyWarningUIController";
    private final AlarmManager mAlarmManager;
    private final Context mContext;
    private final ArraySet<ComponentName> mEnabledA11yServices = new ArraySet<>();
    private final Handler mMainHandler;
    private final NotificationController mNotificationController;

    static {
        String simpleName = PolicyWarningUIController.class.getSimpleName();
        ACTION_SEND_NOTIFICATION = simpleName + ".ACTION_SEND_NOTIFICATION";
        ACTION_A11Y_SETTINGS = simpleName + ".ACTION_A11Y_SETTINGS";
        ACTION_DISMISS_NOTIFICATION = simpleName + ".ACTION_DISMISS_NOTIFICATION";
    }

    public PolicyWarningUIController(Handler handler, Context context, NotificationController notificationController) {
        this.mMainHandler = handler;
        this.mContext = context;
        this.mNotificationController = notificationController;
        this.mAlarmManager = (AlarmManager) context.getSystemService(AlarmManager.class);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SEND_NOTIFICATION);
        intentFilter.addAction(ACTION_A11Y_SETTINGS);
        intentFilter.addAction(ACTION_DISMISS_NOTIFICATION);
        context.registerReceiver(notificationController, intentFilter, "android.permission.MANAGE_ACCESSIBILITY", handler, 2);
    }

    public void onSwitchUser(int i, Set<ComponentName> set) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PolicyWarningUIController.this.onSwitchUserInternal(((Integer) obj).intValue(), (Set) obj2);
            }
        }, Integer.valueOf(i), set));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSwitchUserInternal(int i, Set<ComponentName> set) {
        this.mEnabledA11yServices.clear();
        this.mEnabledA11yServices.addAll(set);
        this.mNotificationController.onSwitchUser(i);
    }

    public void onEnabledServicesChanged(int i, Set<ComponentName> set) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda5
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PolicyWarningUIController.this.onEnabledServicesChangedInternal(((Integer) obj).intValue(), (Set) obj2);
            }
        }, Integer.valueOf(i), set));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnabledServicesChangedInternal(int i, Set<ComponentName> set) {
        ArraySet arraySet = new ArraySet((ArraySet) this.mEnabledA11yServices);
        arraySet.removeAll(set);
        this.mEnabledA11yServices.clear();
        this.mEnabledA11yServices.addAll(set);
        Handler handler = this.mMainHandler;
        final NotificationController notificationController = this.mNotificationController;
        Objects.requireNonNull(notificationController);
        handler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PolicyWarningUIController.NotificationController.this.onServicesDisabled(((Integer) obj).intValue(), (ArraySet) obj2);
            }
        }, Integer.valueOf(i), arraySet));
    }

    public void onNonA11yCategoryServiceBound(int i, ComponentName componentName) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PolicyWarningUIController.this.setAlarm(((Integer) obj).intValue(), (ComponentName) obj2);
            }
        }, Integer.valueOf(i), componentName));
    }

    public void onNonA11yCategoryServiceUnbound(int i, ComponentName componentName) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                PolicyWarningUIController.this.cancelAlarm(((Integer) obj).intValue(), (ComponentName) obj2);
            }
        }, Integer.valueOf(i), componentName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAlarm(int i, ComponentName componentName) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(10, 24);
        this.mAlarmManager.set(0, calendar.getTimeInMillis(), createPendingIntent(this.mContext, i, ACTION_SEND_NOTIFICATION, componentName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAlarm(int i, ComponentName componentName) {
        this.mAlarmManager.cancel(createPendingIntent(this.mContext, i, ACTION_SEND_NOTIFICATION, componentName));
    }

    protected static PendingIntent createPendingIntent(Context context, int i, String str, ComponentName componentName) {
        return PendingIntent.getBroadcast(context, 0, createIntent(context, i, str, componentName), 67108864);
    }

    protected static Intent createIntent(Context context, int i, String str, ComponentName componentName) {
        Intent intent = new Intent(str);
        intent.setPackage(context.getPackageName()).setIdentifier(componentName.flattenToShortString()).putExtra("android.intent.extra.COMPONENT_NAME", componentName).putExtra("android.intent.extra.USER_ID", i).putExtra("android.intent.extra.TIME", SystemClock.elapsedRealtime());
        return intent;
    }

    public void enableSendingNonA11yToolNotification(boolean z) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PolicyWarningUIController.this.enableSendingNonA11yToolNotificationInternal(((Boolean) obj).booleanValue());
            }
        }, Boolean.valueOf(z)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableSendingNonA11yToolNotificationInternal(boolean z) {
        this.mNotificationController.setSendingNotification(z);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class NotificationController extends BroadcastReceiver {
        private static final char RECORD_SEPARATOR = ':';
        private final Context mContext;
        private int mCurrentUserId;
        private final NotificationManager mNotificationManager;
        private boolean mSendNotification;
        private final ArraySet<ComponentName> mNotifiedA11yServices = new ArraySet<>();
        private final List<ComponentName> mSentA11yServiceNotification = new ArrayList();

        public NotificationController(Context context) {
            this.mContext = context;
            this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ComponentName componentName = (ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME", ComponentName.class);
            if (TextUtils.isEmpty(action) || componentName == null) {
                return;
            }
            long longExtra = intent.getLongExtra("android.intent.extra.TIME", 0L);
            long elapsedRealtime = longExtra > 0 ? SystemClock.elapsedRealtime() - longExtra : 0L;
            int intExtra = intent.getIntExtra("android.intent.extra.USER_ID", 0);
            if (PolicyWarningUIController.ACTION_SEND_NOTIFICATION.equals(action)) {
                if (trySendNotification(intExtra, componentName)) {
                    AccessibilityStatsLogUtils.logNonA11yToolServiceWarningReported(componentName.getPackageName(), AccessibilityStatsLogUtils.ACCESSIBILITY_PRIVACY_WARNING_STATUS_SHOWN, elapsedRealtime);
                }
            } else {
                if (PolicyWarningUIController.ACTION_A11Y_SETTINGS.equals(action)) {
                    if (tryLaunchSettings(intExtra, componentName)) {
                        AccessibilityStatsLogUtils.logNonA11yToolServiceWarningReported(componentName.getPackageName(), AccessibilityStatsLogUtils.ACCESSIBILITY_PRIVACY_WARNING_STATUS_CLICKED, elapsedRealtime);
                    }
                    this.mNotificationManager.cancel(componentName.flattenToShortString(), 1005);
                    this.mSentA11yServiceNotification.remove(componentName);
                    onNotificationCanceled(intExtra, componentName);
                    return;
                }
                if (PolicyWarningUIController.ACTION_DISMISS_NOTIFICATION.equals(action)) {
                    this.mSentA11yServiceNotification.remove(componentName);
                    onNotificationCanceled(intExtra, componentName);
                }
            }
        }

        protected void onSwitchUser(int i) {
            cancelSentNotifications();
            this.mNotifiedA11yServices.clear();
            this.mCurrentUserId = i;
            this.mNotifiedA11yServices.addAll((ArraySet<? extends ComponentName>) readNotifiedServiceList(i));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onServicesDisabled(int i, ArraySet<ComponentName> arraySet) {
            if (this.mNotifiedA11yServices.removeAll((ArraySet<? extends ComponentName>) arraySet)) {
                writeNotifiedServiceList(i, this.mNotifiedA11yServices);
            }
        }

        private boolean trySendNotification(int i, ComponentName componentName) {
            if (i != this.mCurrentUserId || !this.mSendNotification) {
                return false;
            }
            List<AccessibilityServiceInfo> enabledServiceInfos = getEnabledServiceInfos();
            int i2 = 0;
            while (true) {
                if (i2 >= enabledServiceInfos.size()) {
                    break;
                }
                AccessibilityServiceInfo accessibilityServiceInfo = enabledServiceInfos.get(i2);
                if (!componentName.flattenToShortString().equals(accessibilityServiceInfo.getComponentName().flattenToShortString())) {
                    i2++;
                } else if (!accessibilityServiceInfo.isAccessibilityTool() && !this.mNotifiedA11yServices.contains(componentName)) {
                    CharSequence loadLabel = accessibilityServiceInfo.getResolveInfo().serviceInfo.loadLabel(this.mContext.getPackageManager());
                    Drawable loadIcon = accessibilityServiceInfo.getResolveInfo().loadIcon(this.mContext.getPackageManager());
                    int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
                    sendNotification(i, componentName, loadLabel, ImageUtils.buildScaledBitmap(loadIcon, dimensionPixelSize, dimensionPixelSize));
                    return true;
                }
            }
            return false;
        }

        private boolean tryLaunchSettings(int i, ComponentName componentName) {
            if (i != this.mCurrentUserId) {
                return false;
            }
            Intent intent = new Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS");
            intent.addFlags(268468224);
            intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName.flattenToShortString());
            intent.putExtra(PolicyWarningUIController.EXTRA_TIME_FOR_LOGGING, SystemClock.elapsedRealtime());
            this.mContext.startActivityAsUser(intent, ActivityOptions.makeBasic().setLaunchDisplayId(this.mContext.getDisplayId()).toBundle(), UserHandle.of(i));
            ((StatusBarManager) this.mContext.getSystemService(StatusBarManager.class)).collapsePanels();
            return true;
        }

        protected void onNotificationCanceled(int i, ComponentName componentName) {
            if (i == this.mCurrentUserId && this.mNotifiedA11yServices.add(componentName)) {
                writeNotifiedServiceList(i, this.mNotifiedA11yServices);
            }
        }

        private void sendNotification(int i, ComponentName componentName, CharSequence charSequence, Bitmap bitmap) {
            Notification.Builder builder = new Notification.Builder(this.mContext, SystemNotificationChannels.ACCESSIBILITY_SECURITY_POLICY);
            builder.setSmallIcon(R.drawable.ic_audio_media).setContentTitle(this.mContext.getString(17041819)).setContentText(this.mContext.getString(17041818, charSequence)).setStyle(new Notification.BigTextStyle().bigText(this.mContext.getString(17041818, charSequence))).setTicker(this.mContext.getString(17041819)).setOnlyAlertOnce(true).setDeleteIntent(PolicyWarningUIController.createPendingIntent(this.mContext, i, PolicyWarningUIController.ACTION_DISMISS_NOTIFICATION, componentName)).setContentIntent(PolicyWarningUIController.createPendingIntent(this.mContext, i, PolicyWarningUIController.ACTION_A11Y_SETTINGS, componentName));
            if (bitmap != null) {
                builder.setLargeIcon(bitmap);
            }
            this.mNotificationManager.notify(componentName.flattenToShortString(), 1005, builder.build());
            this.mSentA11yServiceNotification.add(componentName);
        }

        private ArraySet<ComponentName> readNotifiedServiceList(int i) {
            String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "notified_non_accessibility_category_services", i);
            if (TextUtils.isEmpty(stringForUser)) {
                return new ArraySet<>();
            }
            TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(RECORD_SEPARATOR);
            simpleStringSplitter.setString(stringForUser);
            ArraySet<ComponentName> arraySet = new ArraySet<>();
            Iterator it = simpleStringSplitter.iterator();
            while (it.hasNext()) {
                ComponentName unflattenFromString = ComponentName.unflattenFromString((String) it.next());
                if (unflattenFromString != null) {
                    arraySet.add(unflattenFromString);
                }
            }
            return arraySet;
        }

        private void writeNotifiedServiceList(int i, ArraySet<ComponentName> arraySet) {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < arraySet.size(); i2++) {
                if (i2 > 0) {
                    sb.append(RECORD_SEPARATOR);
                }
                sb.append(arraySet.valueAt(i2).flattenToShortString());
            }
            Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "notified_non_accessibility_category_services", sb.toString(), i);
        }

        @VisibleForTesting
        protected List<AccessibilityServiceInfo> getEnabledServiceInfos() {
            return AccessibilityManager.getInstance(this.mContext).getEnabledAccessibilityServiceList(-1);
        }

        private void cancelSentNotifications() {
            this.mSentA11yServiceNotification.forEach(new Consumer() { // from class: com.android.server.accessibility.PolicyWarningUIController$NotificationController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PolicyWarningUIController.NotificationController.this.lambda$cancelSentNotifications$0((ComponentName) obj);
                }
            });
            this.mSentA11yServiceNotification.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cancelSentNotifications$0(ComponentName componentName) {
            this.mNotificationManager.cancel(componentName.flattenToShortString(), 1005);
        }

        void setSendingNotification(boolean z) {
            this.mSendNotification = z;
        }
    }
}
