package c6;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import b6.LocalLog;
import com.android.internal.notification.SystemNotificationChannels;
import com.oplus.battery.R;
import com.oplus.deepsleep.SuperSleepModeActivity;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.performance.GTModeBroadcastReceiver;
import com.oplus.powermanager.fuelgaue.BatteryHealthActivity;
import com.oplus.powermanager.fuelgaue.IntellPowerSaveScence;
import com.oplus.powermanager.fuelgaue.PowerConsumptionActivity;
import com.oplus.powermanager.fuelgaue.PowerConsumptionOptimizationActivity;
import com.oplus.powermanager.fuelgaue.PowerControlActivity;
import com.oplus.powermanager.fuelgaue.PowerSaveActivity;
import com.oplus.powermanager.fuelgaue.SmartChargeActivity;
import com.oplus.powermanager.fuelgaue.base.HighlightPreferenceGroupAdapter;
import f6.ChargeUtil;
import f6.CommonUtil;
import f6.d;
import f6.f;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import r9.SimplePowerMonitorUtils;
import w4.Affair;
import x5.UploadDataUtil;
import y5.AppFeature;
import z5.GuardElfDataManager;

/* compiled from: NotifyUtil.java */
/* renamed from: c6.c, reason: use source file name */
/* loaded from: classes.dex */
public class NotifyUtil {

    /* renamed from: n, reason: collision with root package name */
    private static volatile NotifyUtil f4888n;

    /* renamed from: a, reason: collision with root package name */
    private Context f4889a;

    /* renamed from: b, reason: collision with root package name */
    private GuardElfDataManager f4890b;

    /* renamed from: c, reason: collision with root package name */
    private NotificationManager f4891c;

    /* renamed from: d, reason: collision with root package name */
    private PowerManager f4892d;

    /* renamed from: g, reason: collision with root package name */
    private Handler f4895g;

    /* renamed from: h, reason: collision with root package name */
    private HandlerThread f4896h;

    /* renamed from: e, reason: collision with root package name */
    private boolean f4893e = true;

    /* renamed from: f, reason: collision with root package name */
    private int f4894f = 0;

    /* renamed from: i, reason: collision with root package name */
    private ConcurrentHashMap<NotificationChannel, Integer> f4897i = new ConcurrentHashMap<>();

    /* renamed from: j, reason: collision with root package name */
    private HashMap<String, Boolean> f4898j = new HashMap<>();

    /* renamed from: k, reason: collision with root package name */
    private boolean f4899k = false;

    /* renamed from: l, reason: collision with root package name */
    private boolean f4900l = false;

    /* renamed from: m, reason: collision with root package name */
    private boolean f4901m = false;

    /* compiled from: NotifyUtil.java */
    /* renamed from: c6.c$a */
    /* loaded from: classes.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("POWER_SAVE_RECOVER")) {
                NotifyUtil.this.f4892d.setPowerSaveModeEnabled(true);
                UploadDataUtil.S0(NotifyUtil.this.f4889a).m0();
                NotifyUtil.this.f4891c.cancel(6);
            }
        }
    }

    private NotifyUtil(Context context) {
        this.f4889a = null;
        this.f4890b = null;
        this.f4891c = null;
        this.f4892d = null;
        this.f4889a = context;
        this.f4890b = GuardElfDataManager.d(context);
        this.f4891c = (NotificationManager) this.f4889a.getSystemService("notification");
        this.f4892d = (PowerManager) this.f4889a.getSystemService("power");
        HandlerThread handlerThread = new HandlerThread("NotifyUtil");
        this.f4896h = handlerThread;
        handlerThread.start();
        this.f4895g = new Handler(this.f4896h.getLooper());
    }

    private void S(String str, int i10) {
        String str2;
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        String string = this.f4889a.getString(R.string.notify_title_restrict);
        String o10 = CommonUtil.o(this.f4889a, str);
        if (o10 != null && !"".equals(o10)) {
            str2 = o10 + this.f4889a.getString(R.string.toast_text_startinfo);
        } else {
            str2 = str + this.f4889a.getString(R.string.toast_text_startinfo);
        }
        LocalLog.a("NotifyUtil", "notifySreenoffRestrict: pkg=" + str + ", appLabel=" + o10);
        Notification.Builder builder = new Notification.Builder(this.f4889a);
        builder.setSmallIcon(R.drawable.ic_small).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(string).setContentText(str2).setLargeIcon(decodeResource);
        this.f4891c.notify(i10 + 2, builder.build());
    }

    public static NotifyUtil v(Context context) {
        if (f4888n == null) {
            synchronized (NotifyUtil.class) {
                if (f4888n == null) {
                    f4888n = new NotifyUtil(context);
                }
            }
        }
        return f4888n;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void x(Intent intent) {
        this.f4889a.sendBroadcastAsUser(intent, UserHandle.ALL, "oplus.permission.OPLUS_COMPONENT_SAFE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void y() {
        LocalLog.a("NotifyUtil", "deleteUnusedChannels");
        this.f4891c.deleteNotificationChannel("charge_protectoin_getin_channel_id");
        this.f4891c.deleteNotificationChannel("charge_protectoin_connect_power_channel_id");
        this.f4891c.deleteNotificationChannel("charge_protectoin_power_full");
    }

    public void A() {
        NotificationChannel notificationChannel = new NotificationChannel("regular_charge_protectoin_getin_channel_id", this.f4889a.getString(R.string.regular_charge_protection_notification_title), 4);
        notificationChannel.setDescription("ChargeProtectionMode");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.regular_charge_protection_notification_title);
        Notification.Builder builder = new Notification.Builder(this.f4889a, SystemNotificationChannels.ALERTS);
        String string = this.f4889a.getString(R.string.regular_charge_protection_notification_title);
        String string2 = this.f4889a.getString(R.string.regular_charge_protection_notification_content);
        BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent = new Intent(this.f4889a, (Class<?>) (AppFeature.d() ? BatteryHealthActivity.class : PowerConsumptionActivity.class));
        intent.setPackage("com.oplus.battery");
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 335544320);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("regular_charge_protectoin_getin_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(true).setAutoCancel(false);
        this.f4891c.notify(28, builder.build());
    }

    public void B() {
        if (!this.f4899k && !this.f4900l) {
            NotificationChannel notificationChannel = new NotificationChannel("smart_charge_protectoin_getin_channel_id", this.f4889a.getString(R.string.smart_charge_protection_notification_title), 4);
            notificationChannel.setDescription("ChargeProtectionMode");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(-16711936);
            notificationChannel.enableVibration(true);
            this.f4891c.createNotificationChannel(notificationChannel);
            W(notificationChannel, R.string.smart_charge_protection_notification_title);
            Intent intent = new Intent();
            intent.setAction("oplus.intent.action.smartchargeprotection.end");
            intent.setPackage("com.oplus.battery");
            PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 67108864);
            Notification.Builder builder = new Notification.Builder(this.f4889a, SystemNotificationChannels.ALERTS);
            String string = this.f4889a.getString(R.string.smart_charge_protection_notification_title);
            String string2 = this.f4889a.getString(R.string.smart_charge_protection_notification_content);
            BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
            Intent intent2 = new Intent(this.f4889a, (Class<?>) (AppFeature.d() ? BatteryHealthActivity.class : PowerConsumptionActivity.class));
            intent2.setPackage("com.oplus.battery");
            PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent2, 335544320);
            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.setBigContentTitle(string);
            bigTextStyle.bigText(string2);
            builder.setContentText(string2).setContentTitle(string).setChannelId("smart_charge_protectoin_getin_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(true).addAction(R.drawable.ic_small, this.f4889a.getString(R.string.smart_charge_protection_notification_exit), broadcast).setAutoCancel(false);
            this.f4891c.notify(24, builder.build());
            this.f4899k = true;
            this.f4901m = false;
            f.c2(this.f4889a, true);
            return;
        }
        LocalLog.a("NotifyUtil", "SmartChargeProtectionRightNow || mIsNotifyBmsHeatActiveStatus is on, return! ");
        if (this.f4900l) {
            this.f4901m = true;
        }
    }

    public void C() {
        if (GTModeBroadcastReceiver.g(this.f4889a)) {
            j();
            return;
        }
        NotificationChannel notificationChannel = new NotificationChannel("high_performance_channel_id", this.f4889a.getString(R.string.high_performance_notify_channel_name), 4);
        notificationChannel.setDescription("High Performance Mode");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "high_performance_channel_id");
        String string = this.f4889a.getString(R.string.high_performance_notification_title);
        String string2 = this.f4889a.getString(R.string.high_performance_notification_message_new);
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.ACTION_HIGH_PERFORMANCE");
        intent.setPackage("com.oplus.battery");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 67108864);
        Intent intent2 = new Intent(this.f4889a, (Class<?>) IntellPowerSaveScence.class);
        intent2.setAction("android.intent.action.MAIN");
        intent2.setFlags(603979776);
        intent2.putExtra("enter_from_notify", false);
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent2, 335544320);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("high_performance_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).addAction(new Notification.Action(0, this.f4889a.getString(R.string.high_performance_notification_close), broadcast)).setAutoCancel(false);
        Notification build = builder.build();
        if (this.f4893e) {
            this.f4891c.notify(5, build);
            LocalLog.a("NotifyUtil", "notify high perform " + this.f4889a.getUserId());
            return;
        }
        if (this.f4889a.getUserId() == ActivityManager.getCurrentUser()) {
            this.f4893e = true;
            this.f4891c.notify(5, build);
            LocalLog.a("NotifyUtil", "notify high perform " + this.f4889a.getUserId() + " and change foreground");
            return;
        }
        LocalLog.a("NotifyUtil", "user " + this.f4889a.getUserId() + " try notify but is not foreground");
    }

    public void D(String str, boolean z10) {
        String str2;
        PackageManager packageManager = this.f4889a.getPackageManager();
        try {
            String charSequence = packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)).toString();
            if (z10) {
                NotificationChannel notificationChannel = new NotificationChannel("PowerConsumptionOptimizationChannel", this.f4889a.getString(R.string.power_consumption_optimization_title), 4);
                notificationChannel.setDescription("Power Consumption Optimization");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(-16711936);
                LocalLog.l("NotifyUtil", "impor   " + notificationChannel.getImportance());
                this.f4891c.createNotificationChannel(notificationChannel);
                W(notificationChannel, R.string.power_consumption_optimization_title);
                Notification.Builder builder = new Notification.Builder(this.f4889a, "PowerConsumptionOptimizationChannel");
                String string = this.f4889a.getString(R.string.power_consumption_optimization_title);
                String string2 = this.f4889a.getString(R.string.pco_notification_text, charSequence);
                Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
                new Intent().setPackage("com.oplus.battery");
                Intent intent = new Intent(this.f4889a, (Class<?>) PowerControlActivity.class);
                intent.addFlags(335544320);
                intent.putExtra("pkgName", str);
                intent.putExtra("title", charSequence);
                intent.putExtra("target", "notify");
                intent.putExtra("drainType", "APP");
                intent.putExtra("light", true);
                intent.putExtra(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY, "background_act_switch");
                PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 201326592);
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
                bigTextStyle.setBigContentTitle(string);
                bigTextStyle.bigText(string2);
                builder.setContentIntent(activity).setContentTitle(string).setContentText(string2).addAction(new Notification.Action(R.drawable.button, this.f4889a.getString(R.string.pco_notification_optimization_immediately), activity)).setChannelId("PowerConsumptionOptimizationChannel").setWhen(System.currentTimeMillis()).setShowWhen(true).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).setAutoCancel(true);
                this.f4891c.notify(str, 17, builder.build());
                str2 = "NotifyUtil";
            } else {
                NotificationChannel notificationChannel2 = new NotificationChannel("PowerConsumptionOptimizationChannelLow", this.f4889a.getString(R.string.power_consumption_optimization_title), 2);
                notificationChannel2.setDescription("Power Consumption Optimization");
                notificationChannel2.enableLights(true);
                notificationChannel2.setLightColor(-16711936);
                LocalLog.l("NotifyUtil", "impor   " + notificationChannel2.getImportance());
                this.f4891c.createNotificationChannel(notificationChannel2);
                W(notificationChannel2, R.string.power_consumption_optimization_title);
                Notification.Builder builder2 = new Notification.Builder(this.f4889a, "PowerConsumptionOptimizationChannelLow");
                String string3 = this.f4889a.getString(R.string.power_consumption_optimization_title);
                String string4 = this.f4889a.getString(R.string.pco_notification_text, charSequence);
                Bitmap decodeResource2 = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
                new Intent().setPackage("com.oplus.battery");
                str2 = "NotifyUtil";
                Intent intent2 = new Intent(this.f4889a, (Class<?>) PowerControlActivity.class);
                intent2.addFlags(335544320);
                intent2.putExtra("pkgName", str);
                intent2.putExtra("title", charSequence);
                intent2.putExtra("target", "notify");
                intent2.putExtra("drainType", "APP");
                intent2.putExtra("light", true);
                intent2.putExtra(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY, "background_act_switch");
                PendingIntent activity2 = PendingIntent.getActivity(this.f4889a, 0, intent2, 201326592);
                Intent intent3 = new Intent();
                intent3.setAction("oplus.intent.action.high_power_consumption_notification.nomore");
                intent3.setPackage("com.oplus.battery");
                intent3.putExtra("pkgName", str);
                intent3.putExtra("isNotificate", true);
                PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent3, 67108864);
                Notification.BigTextStyle bigTextStyle2 = new Notification.BigTextStyle();
                bigTextStyle2.setBigContentTitle(string3);
                bigTextStyle2.bigText(string4);
                builder2.setContentIntent(activity2).setContentTitle(string3).setContentText(string4).addAction(new Notification.Action(R.drawable.button, this.f4889a.getString(R.string.pco_notification_optimization_immediately), activity2)).addAction(new Notification.Action(R.drawable.button, this.f4889a.getString(R.string.pco_notification_no_more), broadcast)).setChannelId("PowerConsumptionOptimizationChannelLow").setWhen(System.currentTimeMillis()).setShowWhen(true).setLargeIcon(decodeResource2).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle2).setOngoing(false).setAutoCancel(true);
                Notification build = builder2.build();
                if (!f.O(this.f4889a, str)) {
                    this.f4891c.notify(str, 17, build);
                }
            }
            if (this.f4898j.containsKey(str)) {
                this.f4898j.replace(str, Boolean.TRUE);
            } else {
                this.f4898j.put(str, Boolean.TRUE);
            }
            LocalLog.l(str2, "notification pco");
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b("NotifyUtil", e10.toString());
        }
    }

    public void E(String str, boolean z10) {
        PackageManager packageManager = this.f4889a.getPackageManager();
        try {
            String charSequence = packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)).toString();
            NotificationChannel notificationChannel = new NotificationChannel("PowerConsumptionOptimizationChannel", this.f4889a.getString(R.string.power_consumption_optimization_title), 4);
            notificationChannel.setDescription("Power Consumption Optimization");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(-16711936);
            notificationChannel.enableVibration(true);
            this.f4891c.createNotificationChannel(notificationChannel);
            W(notificationChannel, R.string.power_consumption_optimization_title);
            Notification.Builder builder = new Notification.Builder(this.f4889a, "PowerConsumptionOptimizationChannel");
            String string = this.f4889a.getString(R.string.power_consumption_optimization_title);
            String string2 = this.f4889a.getString(R.string.pco_notification_text, charSequence);
            Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
            Intent intent = new Intent();
            if (z10) {
                intent.setAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_F");
            } else {
                intent.setAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_NF");
            }
            intent.setPackage("com.oplus.battery");
            intent.putExtra("pkgName", str);
            PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 201326592);
            Intent intent2 = new Intent(this.f4889a, (Class<?>) PowerConsumptionOptimizationActivity.class);
            intent2.addFlags(335544320);
            intent2.putExtra("pkgName", str);
            intent2.putExtra("top", charSequence);
            intent2.putExtra("target", "notify");
            PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent2, 201326592);
            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.setBigContentTitle(string);
            bigTextStyle.bigText(string2);
            builder.setContentIntent(activity).setContentTitle(string).setContentText(string2).addAction(new Notification.Action(R.drawable.button, this.f4889a.getString(R.string.pco_notification_optimization_immediately), broadcast)).setChannelId("PowerConsumptionOptimizationChannel").setWhen(System.currentTimeMillis()).setShowWhen(true).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).setAutoCancel(false);
            this.f4891c.notify(str, 17, builder.build());
            if (this.f4898j.containsKey(str)) {
                this.f4898j.replace(str, Boolean.TRUE);
            } else {
                this.f4898j.put(str, Boolean.TRUE);
            }
            LocalLog.a("NotifyUtil", "notification pco");
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b("NotifyUtil", e10.toString());
        }
    }

    public void F() {
        NotificationChannel notificationChannel = new NotificationChannel("hightemp_protect_channel_id", "HighTemp Protect", 4);
        notificationChannel.setDescription("hightemp_protect");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "hightemp_protect_channel_id");
        String string = this.f4889a.getString(R.string.hightemp_protect_notification_text);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(string);
        builder.setContentText(string).setChannelId("hightemp_protect_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(false);
        Notification build = builder.build();
        if (this.f4893e) {
            this.f4891c.notify(18, build);
        }
    }

    public void G(String str, boolean z10) {
        String str2;
        PackageManager packageManager = this.f4889a.getPackageManager();
        try {
            str2 = packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)).toString();
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b("NotifyUtil", e10.toString());
            str2 = "";
        }
        NotificationChannel notificationChannel = new NotificationChannel("PowerConsumptionOptimizationChannel", this.f4889a.getString(R.string.power_consumption_optimization_title), 4);
        notificationChannel.setDescription("Power Consumption Optimization");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.power_consumption_optimization_title);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "PowerConsumptionOptimizationChannel");
        String string = this.f4889a.getString(R.string.power_consumption_optimization_title);
        String string2 = this.f4889a.getString(R.string.pco_notification_text, str2);
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent = new Intent();
        if (z10) {
            intent.setAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_F");
        } else {
            intent.setAction("oplus.intent.action.ACTION_OPTIMZATION_IMMEDIATELY_NF");
        }
        intent.setPackage("com.oplus.battery");
        intent.putExtra("pkgName", str);
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 201326592);
        Intent intent2 = new Intent(this.f4889a, (Class<?>) PowerConsumptionOptimizationActivity.class);
        intent2.addFlags(335544320);
        intent2.putExtra("pkgName", str);
        intent2.putExtra("top", str2);
        intent2.putExtra("target", "notify");
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent2, 201326592);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentIntent(activity).setContentTitle(string).setContentText(string2).addAction(new Notification.Action(R.drawable.button, this.f4889a.getString(R.string.pco_notification_optimization_immediately), broadcast)).setChannelId("PowerConsumptionOptimizationChannel").setWhen(System.currentTimeMillis()).setShowWhen(true).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).setAutoCancel(false);
        this.f4891c.notifyAsUser(str, 17, builder.build(), UserHandle.ALL);
        if (this.f4898j.containsKey(str)) {
            this.f4898j.replace(str, Boolean.TRUE);
        } else {
            this.f4898j.put(str, Boolean.TRUE);
        }
        LocalLog.a("NotifyUtil", "notification pco");
    }

    public void H(int i10) {
        this.f4891c.createNotificationChannel(new NotificationChannel("AbnormalPowerConsumption", this.f4889a.getString(R.string.notify_screenoff_unrestrict_title), 4));
        Notification.Builder builder = new Notification.Builder(this.f4889a, "AbnormalPowerConsumption");
        String string = this.f4889a.getString(R.string.power_save_auto_close);
        this.f4889a.getString(R.string.notify_power_save_mode_content, Integer.valueOf(i10));
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent = new Intent(this.f4889a, (Class<?>) PowerSaveActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.setFlags(603979776);
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 335544320);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        a aVar = new a();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("POWER_SAVE_RECOVER");
        this.f4889a.registerReceiver(aVar, intentFilter, 4);
        Intent intent2 = new Intent("POWER_SAVE_RECOVER");
        intent2.setPackage("com.oplus.battery");
        builder.setContentIntent(activity).setContentTitle(string).setStyle(bigTextStyle).setChannelId("AbnormalPowerConsumption").setWhen(System.currentTimeMillis()).setShowWhen(true).setAutoCancel(true).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).addAction(R.drawable.ic_small, this.f4889a.getString(R.string.power_save_recover), PendingIntent.getBroadcast(this.f4889a, 0, intent2, 67108864));
        this.f4891c.notify(6, builder.build());
    }

    public void I(int i10) {
        NotificationChannel notificationChannel = new NotificationChannel("reverse_func_off_channel_id", this.f4889a.getString(R.string.wireless_reverse_charging_title), 4);
        notificationChannel.setDescription("Reverse_Func_Off_Notify");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "reverse_func_off_channel_id");
        String string = this.f4889a.getString(R.string.reverse_charge_low_battery_close, Integer.valueOf(i10));
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(string);
        builder.setContentText(string).setChannelId("reverse_func_off_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
        this.f4891c.notify(10, builder.build());
    }

    public void J() {
        NotificationChannel notificationChannel = new NotificationChannel("reverse_func_off_channel_id", this.f4889a.getString(R.string.wireless_reverse_charging_title), 4);
        notificationChannel.setDescription("Reverse_Func_Off_Notify");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "reverse_func_off_channel_id");
        String string = this.f4889a.getString(R.string.reverse_disable_note_on_notify);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(string);
        builder.setContentText(string).setChannelId("reverse_func_off_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(false);
        this.f4891c.notify(11, builder.build());
    }

    public void K() {
        NotificationChannel notificationChannel = new NotificationChannel("reverse_func_off_channel_id", this.f4889a.getString(R.string.wireless_reverse_charging_title), 4);
        notificationChannel.setDescription("Reverse_Func_Off_Notify");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "reverse_func_off_channel_id");
        String string = this.f4889a.getString(R.string.reverse_charge_special_close_alert);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(string);
        builder.setContentText(string).setChannelId("reverse_func_off_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
        this.f4891c.notify(8, builder.build());
    }

    public void L() {
        NotificationChannel notificationChannel = new NotificationChannel("reverse_func_off_channel_id", this.f4889a.getString(R.string.wireless_reverse_charging_title), 4);
        notificationChannel.setDescription("Reverse_Func_Off_Notify");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "reverse_func_off_channel_id");
        String string = this.f4889a.getString(R.string.reverse_charge_close_by_normal_charge);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.bigText(string);
        builder.setContentText(string).setChannelId("reverse_func_off_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
        this.f4891c.notify(9, builder.build());
    }

    public void M() {
        NotificationChannel notificationChannel = new NotificationChannel("smart_charge_be_running_channel_id", this.f4889a.getString(R.string.smart_charge_being_notification_title), 4);
        notificationChannel.setDescription(this.f4889a.getString(R.string.smart_charge_being_notification_content));
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.smart_charge_being_notification_title);
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.smartcharge.berunning.nomore");
        intent.setPackage("com.oplus.battery");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 67108864);
        Intent intent2 = new Intent();
        intent2.setAction("oplus.intent.action.smartcharge.berunning.knowmore");
        intent2.setPackage("com.oplus.battery");
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this.f4889a, 0, intent2, 67108864);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "smart_charge_be_running_channel_id");
        String string = this.f4889a.getString(R.string.smart_charge_being_notification_title);
        String string2 = this.f4889a.getString(R.string.smart_charge_being_notification_content);
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent3 = new Intent(this.f4889a, (Class<?>) SmartChargeActivity.class);
        intent3.addFlags(335544320);
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent3, 201326592);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("smart_charge_be_running_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).addAction(R.drawable.ic_small, this.f4889a.getString(R.string.smart_charge_being_notification_no_more), broadcast).addAction(R.drawable.ic_small, this.f4889a.getString(R.string.smart_charge_being_notification_know_more), broadcast2).setAutoCancel(true);
        this.f4891c.notify(21, builder.build());
    }

    public void N() {
        NotificationChannel notificationChannel = new NotificationChannel("smart_charge_channel_id", this.f4889a.getString(R.string.smart_charge_notification_title), 4);
        notificationChannel.setDescription(this.f4889a.getString(R.string.smart_charge_notification_content));
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.smart_charge_notification_title);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "smart_charge_channel_id");
        String string = this.f4889a.getString(R.string.smart_charge_notification_title);
        String string2 = this.f4889a.getString(R.string.smart_charge_notification_content);
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent = new Intent(this.f4889a, (Class<?>) SmartChargeActivity.class);
        intent.addFlags(335544320);
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 201326592);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("smart_charge_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(false).setAutoCancel(true);
        this.f4891c.notify(20, builder.build());
        UploadDataUtil.S0(this.f4889a).D(ChargeUtil.g());
    }

    public void O() {
        NotificationChannel notificationChannel = new NotificationChannel("smart_charge_protectoin_guide_channel_id", this.f4889a.getString(R.string.smart_charge_protection_title), 3);
        notificationChannel.setDescription("ChargeProtectionMode");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.smart_charge_protection_title);
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.smartchargeprotection.close");
        intent.setPackage("com.oplus.battery");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 335544320);
        Notification.Builder builder = new Notification.Builder(this.f4889a, SystemNotificationChannels.ALERTS);
        String string = this.f4889a.getString(R.string.smart_charge_protection_recommend_close_title);
        String string2 = this.f4889a.getString(R.string.smart_charge_protection_recommend_close_content);
        BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent2 = new Intent(this.f4889a, (Class<?>) (AppFeature.d() ? BatteryHealthActivity.class : PowerConsumptionActivity.class));
        intent2.setPackage("com.oplus.battery");
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent2, 335544320);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("smart_charge_protectoin_guide_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).addAction(R.drawable.ic_small, this.f4889a.getString(R.string.smart_charge_protection_recommend_button), broadcast).setAutoCancel(true);
        this.f4891c.notify(23, builder.build());
    }

    public void P() {
        NotificationChannel notificationChannel = new NotificationChannel("smart_charge_protectoin_guide_channel_id", this.f4889a.getString(R.string.smart_charge_protection_title), 4);
        notificationChannel.setDescription("ChargeProtectionMode");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.smart_charge_protection_title);
        Intent intent = new Intent();
        intent.setAction("oplus.intent.action.smartchargeprotection.open");
        intent.setPackage("com.oplus.battery");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.f4889a, 0, intent, 67108864);
        Notification.Builder builder = new Notification.Builder(this.f4889a, SystemNotificationChannels.ALERTS);
        String string = this.f4889a.getString(R.string.smart_charge_protection_guide_title);
        String string2 = this.f4889a.getString(R.string.smart_charge_protection_guide_content);
        BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent2 = new Intent(this.f4889a, (Class<?>) (AppFeature.d() ? BatteryHealthActivity.class : PowerConsumptionActivity.class));
        intent2.setPackage("com.oplus.battery");
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent2, 335544320);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("smart_charge_protectoin_guide_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).addAction(R.drawable.ic_small, this.f4889a.getString(R.string.smart_charge_protection_guide_open), broadcast).setAutoCancel(true);
        this.f4891c.notify(22, builder.build());
    }

    public void Q(String str, boolean z10) {
        String str2;
        if (str == null) {
            return;
        }
        String string = this.f4889a.getString(R.string.power_consumption_optimization_title);
        PackageManager packageManager = this.f4889a.getPackageManager();
        try {
            str2 = packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)).toString();
        } catch (PackageManager.NameNotFoundException e10) {
            LocalLog.b("NotifyUtil", e10.toString());
            str2 = "";
        }
        String str3 = this.f4889a.getString(R.string.pco_notification_text, str2) + "\n" + this.f4889a.getString(R.string.pco_notification_optimization_immediately);
        Intent intent = new Intent(this.f4889a.getApplicationContext(), (Class<?>) PowerControlActivity.class);
        intent.addFlags(335544320);
        intent.putExtra("title", str2);
        intent.putExtra("pkgName", str);
        intent.putExtra("drainType", "APP");
        intent.putExtra(HighlightPreferenceGroupAdapter.EXTRA_FRAGMENT_ARG_KEY, "background_act_switch");
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 201326592);
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(str3);
        if (z10) {
            NotificationChannel notificationChannel = new NotificationChannel("AbnormalPowerConsumption", this.f4889a.getString(R.string.notify_screenoff_unrestrict_title), 4);
            notificationChannel.setDescription("AbnormalPowerConsumption");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(-16711936);
            notificationChannel.enableVibration(true);
            this.f4891c.createNotificationChannel(notificationChannel);
            Notification.Builder builder = new Notification.Builder(this.f4889a, SystemNotificationChannels.ALERTS);
            builder.setContentText(str3).setContentTitle(string).setChannelId(notificationChannel.getId()).setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
            this.f4891c.notify(3, builder.build());
            return;
        }
        NotificationChannel notificationChannel2 = new NotificationChannel("AbnormalPowerConsumptionLow", this.f4889a.getString(R.string.notify_screenoff_unrestrict_title), 2);
        notificationChannel2.setDescription("AbnormalPowerConsumptionLow");
        this.f4891c.createNotificationChannel(notificationChannel2);
        Notification.Builder builder2 = new Notification.Builder(this.f4889a, SystemNotificationChannels.DO_NOT_DISTURB);
        builder2.setContentText(str3).setContentTitle(string).setChannelId(notificationChannel2.getId()).setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
        this.f4891c.notify(3, builder2.build());
    }

    public void R() {
        List<String> e10 = this.f4890b.e("waste_locked_app_thistime.xml");
        for (int i10 = 0; i10 < e10.size(); i10++) {
            String str = e10.get(i10);
            Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
            String string = this.f4889a.getString(R.string.notify_screenoff_unrestrict_title);
            String o10 = CommonUtil.o(this.f4889a, str);
            String str2 = (o10 != null && !"".equals(o10)) ? o10 + this.f4889a.getString(R.string.notify_screenoff_battery_save_text_tail) : str + this.f4889a.getString(R.string.notify_screenoff_battery_save_text_tail);
            LocalLog.a("NotifyUtil", "notifySreenoffNotRestrictDueToLocded: pkg=" + str + ", appLabel=" + o10);
            Notification.Builder builder = new Notification.Builder(this.f4889a);
            builder.setSmallIcon(R.drawable.ic_small).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(string).setContentText(str2).setLargeIcon(decodeResource);
            this.f4891c.notify(i10 + 4, builder.build());
        }
    }

    public void T() {
        List<String> e10 = this.f4890b.e("forcestop_system_app_thistime.xml");
        List<String> e11 = this.f4890b.e("forcestop_third_app_thisTime.xml");
        Iterator<String> it = e10.iterator();
        int i10 = 0;
        while (it.hasNext()) {
            S(it.next(), i10);
            i10++;
        }
        Iterator<String> it2 = e11.iterator();
        while (it2.hasNext()) {
            S(it2.next(), i10);
            i10++;
        }
    }

    public void U() {
        NotificationChannel notificationChannel = new NotificationChannel("sleepMode_optimize_channel_id", this.f4889a.getString(R.string.intelligent_sleep_mode), 4);
        notificationChannel.setDescription("SleepMode NetOff Notify");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "sleepMode_optimize_channel_id");
        String string = this.f4889a.getString(R.string.intelligent_sleep_mode);
        String string2 = this.f4889a.getString(R.string.intelligent_sleep_super_notification_message);
        Intent intent = new Intent(this.f4889a, (Class<?>) SuperSleepModeActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.setFlags(603979776);
        intent.putExtra("enter_from_notify", true);
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 335544320);
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("sleepMode_optimize_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
        this.f4891c.notify(7, builder.build());
    }

    public void V() {
        NotificationChannel notificationChannel = new NotificationChannel("sleepMode_optimize_channel_id", this.f4889a.getString(R.string.intelligent_deep_sleep_mode_tiltle), 4);
        notificationChannel.setDescription("SleepMode NetOff Notify");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(this.f4889a, "sleepMode_optimize_channel_id");
        String string = this.f4889a.getString(R.string.intelligent_deep_sleep_mode_tiltle);
        String string2 = this.f4889a.getString(R.string.sleepmode_optimize_notification_message);
        Intent intent = new Intent(this.f4889a, (Class<?>) IntellPowerSaveScence.class);
        intent.setAction("android.intent.action.MAIN");
        intent.setFlags(603979776);
        intent.putExtra("enter_from_notify", true);
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 335544320);
        Bitmap decodeResource = BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("sleepMode_optimize_channel_id").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setLargeIcon(decodeResource).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setAutoCancel(true);
        this.f4891c.notify(7, builder.build());
    }

    public void W(NotificationChannel notificationChannel, int i10) {
        this.f4897i.put(notificationChannel, Integer.valueOf(i10));
    }

    public void X(boolean z10) {
        LocalLog.a("NotifyUtil", "user " + this.f4889a.getUserId() + " set foreground " + z10);
        this.f4893e = z10;
    }

    public void Y() {
        boolean z10;
        LocalLog.a("NotifyUtil", "Update Notification Channel!");
        Iterator<Map.Entry<NotificationChannel, Integer>> it = this.f4897i.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                z10 = false;
                break;
            }
            Map.Entry<NotificationChannel, Integer> next = it.next();
            if (next.getKey().getName().equals(this.f4889a.getString(next.getValue().intValue())) && this.f4894f < 10) {
                LocalLog.b("NotifyUtil", "language not ready!");
                z10 = true;
                break;
            } else {
                next.getKey().setName(this.f4889a.getString(next.getValue().intValue()));
                this.f4891c.createNotificationChannel(next.getKey());
            }
        }
        if (z10 && this.f4894f < 10) {
            try {
                Thread.sleep(300L);
            } catch (InterruptedException e10) {
                LocalLog.b("NotifyUtil", "updateNotificationChannel " + e10.toString());
            }
            this.f4894f++;
            Affair.f().d(EventType.SCENE_SHORT_VIDEO, new Intent());
            return;
        }
        this.f4894f = 0;
        NotificationChannel notificationChannel = new NotificationChannel("high_performance_channel_id", this.f4889a.getString(R.string.high_performance_notify_channel_name), 4);
        notificationChannel.setDescription("High Performance Mode");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
    }

    public void f() {
        this.f4891c.cancel(27);
        this.f4900l = false;
        if (this.f4901m) {
            LocalLog.a("NotifyUtil", "cancelBmsHeatActiveStatus notifyEnterSmartChargeProtectionRightNow : mIsBmsHeatStopSmartChargeProtectNotify = " + this.f4901m);
            B();
        }
    }

    public void g() {
        this.f4891c.cancel(28);
    }

    public void h() {
        this.f4899k = false;
        this.f4901m = false;
        this.f4891c.cancel(24);
        f.c2(this.f4889a, false);
    }

    public void i() {
        this.f4891c.cancel(3);
    }

    public void j() {
        if (this.f4893e) {
            this.f4891c.cancel(5);
            LocalLog.a("NotifyUtil", "cancel notify high perform " + this.f4889a.getUserId());
            return;
        }
        if (this.f4889a.getUserId() == ActivityManager.getCurrentUser()) {
            this.f4893e = true;
            this.f4891c.cancel(5);
            LocalLog.a("NotifyUtil", "cancel notify high perform " + this.f4889a.getUserId() + " and change foreground");
            return;
        }
        LocalLog.a("NotifyUtil", "user " + this.f4889a.getUserId() + " try cancel but is not foreground");
    }

    public void k(String str) {
        for (StatusBarNotification statusBarNotification : this.f4891c.getActiveNotifications()) {
            if (statusBarNotification.getId() == 17 && str.equals(statusBarNotification.getTag())) {
                if (SimplePowerMonitorUtils.f17652c != 0) {
                    final Intent intent = new Intent();
                    intent.setAction("oplus.intent.action.OPLUS_SUB_USER_NOTIFY");
                    intent.putExtra("pkgname", str);
                    intent.putExtra("type", 4);
                    Log.d("NotifyUtil", "mContext.sendBroadcastAsUser4");
                    this.f4895g.post(new Runnable() { // from class: c6.b
                        @Override // java.lang.Runnable
                        public final void run() {
                            NotifyUtil.this.x(intent);
                        }
                    });
                } else {
                    this.f4891c.cancel(str, 17);
                }
            }
        }
        if (this.f4898j.containsKey(str)) {
            this.f4898j.replace(str, Boolean.FALSE);
        }
    }

    public void l() {
        this.f4891c.cancelAsUser(null, 18, UserHandle.ALL);
    }

    public void m(String str) {
        try {
            d.a().b("android.app.NotificationManager", "cancelAsUser", String.class, Integer.TYPE, UserHandle.class).invoke(this.f4891c, str, 17, UserHandle.ALL);
        } catch (Exception e10) {
            Log.e("NotifyUtil", "Fail invoke method e=" + e10);
            this.f4891c.cancel(17);
        }
    }

    public void n() {
        this.f4891c.cancelAsUser(null, 21, UserHandle.ALL);
    }

    public void o() {
        this.f4891c.cancel(10);
    }

    public void p() {
        this.f4891c.cancel(11);
    }

    public void q() {
        this.f4891c.cancel(8);
    }

    public void r() {
        this.f4891c.cancel(9);
    }

    public void s() {
        this.f4891c.cancel(23);
    }

    public void t() {
        this.f4891c.cancel(22);
    }

    public void u() {
        this.f4895g.post(new Runnable() { // from class: c6.a
            @Override // java.lang.Runnable
            public final void run() {
                NotifyUtil.this.y();
            }
        });
    }

    public HashMap<String, Boolean> w() {
        return this.f4898j;
    }

    public void z() {
        if (this.f4899k) {
            h();
            this.f4901m = true;
        }
        NotificationChannel notificationChannel = new NotificationChannel("bms_heat_active", this.f4889a.getString(R.string.extremely_cold_mode_notification_title), 4);
        notificationChannel.setDescription("BmsHeatActiveMode");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(-16711936);
        notificationChannel.enableVibration(true);
        this.f4891c.createNotificationChannel(notificationChannel);
        W(notificationChannel, R.string.extremely_cold_mode_notification_title);
        Notification.Builder builder = new Notification.Builder(this.f4889a, SystemNotificationChannels.ALERTS);
        String string = this.f4889a.getString(R.string.extremely_cold_mode_notification_title);
        String string2 = this.f4889a.getString(R.string.extremely_cold_mode_notification_content);
        BitmapFactory.decodeResource(this.f4889a.getResources(), R.drawable.ic_launcher_pwrmgr);
        Intent intent = new Intent(this.f4889a, (Class<?>) (AppFeature.d() ? BatteryHealthActivity.class : PowerConsumptionActivity.class));
        intent.setPackage("com.oplus.battery");
        PendingIntent activity = PendingIntent.getActivity(this.f4889a, 0, intent, 335544320);
        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(string);
        bigTextStyle.bigText(string2);
        builder.setContentText(string2).setContentTitle(string).setChannelId("bms_heat_active").setWhen(System.currentTimeMillis()).setShowWhen(true).setContentIntent(activity).setSmallIcon(R.drawable.ic_small).setStyle(bigTextStyle).setOngoing(true).setAutoCancel(false);
        this.f4891c.notify(27, builder.build());
        this.f4900l = true;
    }
}
