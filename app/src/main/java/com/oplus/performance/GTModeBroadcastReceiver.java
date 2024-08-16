package com.oplus.performance;

import android.annotation.SuppressLint;
import android.app.OplusActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.PowerManager;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;
import b6.LocalLog;
import com.oplus.battery.R;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.os.WaveformEffect;
import f6.f;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class GTModeBroadcastReceiver extends BroadcastReceiver {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Context f9985e;

        a(Context context) {
            this.f9985e = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            GTModeBroadcastReceiver.a(this.f9985e, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private Context f9986e;

        public b(Context context) {
            this.f9986e = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            int parseInt;
            try {
                LocalLog.l("GTModeBroadcastReceiver", "CircleLightRunnale run");
                Class<?> cls = Integer.TYPE;
                Class<?>[] clsArr = {cls, cls, cls, cls, cls};
                PowerManager powerManager = (PowerManager) this.f9986e.getSystemService("power");
                Method declaredMethod = Class.forName("android.os.PowerManager").getDeclaredMethod("setFlashing", clsArr);
                declaredMethod.setAccessible(true);
                String string = Settings.Global.getString(this.f9986e.getContentResolver(), "breathing_light_action_gt");
                String string2 = Settings.Global.getString(this.f9986e.getContentResolver(), "breathing_light_color_gt");
                LocalLog.l("GTModeBroadcastReceiver", "CircleLightRunnale color =" + string2 + " action =" + string);
                if (!TextUtils.isEmpty(string2) && string2.startsWith("#")) {
                    if (!TextUtils.isEmpty(string) && string.startsWith("#7")) {
                        LocalLog.l("GTModeBroadcastReceiver", "CircleLightRunnale has color #7 action =" + string.substring(1));
                        declaredMethod.invoke(powerManager, 5, Integer.valueOf(Integer.parseInt(string.substring(1), 16) + Integer.parseInt(string2.substring(1), 16)), 0, 0, 2);
                        Thread.sleep(4000L);
                        declaredMethod.invoke(powerManager, 5, 0, 0, 0, 2);
                        return;
                    }
                } else if (!TextUtils.isEmpty(string) && string.startsWith("#7")) {
                    LocalLog.l("GTModeBroadcastReceiver", "CircleLightRunnale no color #7 action =" + string.substring(1));
                    declaredMethod.invoke(powerManager, 5, Integer.valueOf(Integer.parseInt(string.substring(1), 16) + 7453695), 0, 0, 2);
                    Thread.sleep(4000L);
                    declaredMethod.invoke(powerManager, 5, 0, 0, 0, 2);
                    return;
                }
                int i10 = 2054274047;
                if ((TextUtils.isEmpty(string) || string.startsWith("#8")) && !TextUtils.isEmpty(string2) && string2.startsWith("#")) {
                    parseInt = (-1962934272) + Integer.parseInt(string2.substring(1), 16);
                    i10 = Integer.parseInt(string2.substring(1), 16) + 2046820352;
                } else {
                    parseInt = -1955480577;
                }
                LocalLog.l("GTModeBroadcastReceiver", "CircleLightRunnale circle mode");
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 15, 100, 5);
                Thread.sleep(200L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 7, 100, 5);
                Thread.sleep(90L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 3, 100, 5);
                Thread.sleep(90L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 1, 100, 5);
                Thread.sleep(90L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 8, 100, 5);
                Thread.sleep(90L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 4, 100, 5);
                Thread.sleep(90L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 2, 100, 5);
                Thread.sleep(90L);
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 1, 100, 5);
                Thread.sleep(90L);
                for (int i11 = 0; i11 < 3; i11++) {
                    declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 8, 100, 5);
                    Thread.sleep(60L);
                    declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 4, 100, 5);
                    Thread.sleep(60L);
                    declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 2, 100, 5);
                    Thread.sleep(60L);
                    declaredMethod.invoke(powerManager, 5, Integer.valueOf(parseInt), 1, 100, 5);
                    Thread.sleep(60L);
                }
                declaredMethod.invoke(powerManager, 5, Integer.valueOf(i10), 0, 0, 2);
                Thread.sleep(520L);
                declaredMethod.invoke(powerManager, 5, 0, 3, 100, 2);
            } catch (ClassNotFoundException e10) {
                LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod ClassNotFoundException caught : " + e10.getMessage());
            } catch (IllegalAccessException e11) {
                LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod IllegalAccessException caught : " + e11.getMessage());
            } catch (IllegalArgumentException e12) {
                LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod IllegalArgumentException caught : " + e12.getMessage());
            } catch (NoSuchMethodException e13) {
                LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod NoSuchMethodException caught : " + e13.getMessage());
            } catch (Exception e14) {
                LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod exception caught : " + e14.getMessage());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class c extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        private Context f9987a;

        /* renamed from: b, reason: collision with root package name */
        private final int f9988b;

        /* renamed from: c, reason: collision with root package name */
        private final int f9989c;

        public c(Handler handler, Context context) {
            super(handler);
            this.f9988b = 0;
            this.f9989c = 1;
            this.f9987a = context;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (Settings.System.getIntForUser(this.f9987a.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1) {
                int i10 = Settings.Global.getInt(this.f9987a.getContentResolver(), "low_power", 0);
                if (i10 >= 0 && i10 <= 1) {
                    if (i10 == 1) {
                        GTModeBroadcastReceiver.c(this.f9987a, 1);
                    }
                    LocalLog.a("GTModeBroadcastReceiver", "mPowerSaveObserver:  onChange state=" + i10);
                    return;
                }
                LocalLog.a("GTModeBroadcastReceiver", "mPowerSaveObserver:  invalid state=" + i10);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class d extends ContentObserver {

        /* renamed from: a, reason: collision with root package name */
        private Context f9990a;

        public d(Handler handler, Context context) {
            super(handler);
            this.f9990a = context;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (Settings.System.getIntForUser(this.f9990a.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1 && f.a1(this.f9990a)) {
                GTModeBroadcastReceiver.c(this.f9990a, 2);
            }
        }
    }

    public static void a(Context context, int i10) {
        Handler handler;
        a aVar;
        if ((!y5.b.o() && !y5.b.r()) || Settings.Global.getInt(context.getContentResolver(), "customize_breath_light_gt", 1) == 0 || Settings.Global.getInt(context.getContentResolver(), "customize_breath_light_master_switch", 1) == 0) {
            return;
        }
        Class<?> cls = Integer.TYPE;
        Class<?>[] clsArr = {cls, cls, cls, cls, cls};
        if (y5.b.r()) {
            new Thread(new b(context), "multiLights").start();
            return;
        }
        Object[] objArr = y5.b.o() ? new Object[]{5, Integer.valueOf(i10), 3, 100, 2} : null;
        try {
            try {
                try {
                    try {
                        PowerManager powerManager = (PowerManager) context.getSystemService("power");
                        Method declaredMethod = Class.forName("android.os.PowerManager").getDeclaredMethod("setFlashing", clsArr);
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(powerManager, objArr);
                    } catch (ClassNotFoundException e10) {
                        LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod ClassNotFoundException caught : " + e10.getMessage());
                        if (i10 == 0) {
                            return;
                        }
                        handler = new Handler();
                        aVar = new a(context);
                    } catch (NoSuchMethodException e11) {
                        LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod NoSuchMethodException caught : " + e11.getMessage());
                        if (i10 == 0) {
                            return;
                        }
                        handler = new Handler();
                        aVar = new a(context);
                    }
                } catch (IllegalArgumentException e12) {
                    LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod IllegalArgumentException caught : " + e12.getMessage());
                    if (i10 == 0) {
                        return;
                    }
                    handler = new Handler();
                    aVar = new a(context);
                } catch (Exception e13) {
                    LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod exception caught : " + e13.getMessage());
                    if (i10 == 0) {
                        return;
                    }
                    handler = new Handler();
                    aVar = new a(context);
                }
            } catch (IllegalAccessException e14) {
                LocalLog.b("GTModeBroadcastReceiver", "callDeclaredMethod IllegalAccessException caught : " + e14.getMessage());
                if (i10 == 0) {
                    return;
                }
                handler = new Handler();
                aVar = new a(context);
            }
            if (i10 != 0) {
                handler = new Handler();
                aVar = new a(context);
                handler.postDelayed(aVar, 4000L);
            }
        } catch (Throwable th) {
            if (i10 != 0) {
                new Handler().postDelayed(new a(context), 4000L);
            }
            throw th;
        }
    }

    public static void b(Context context, boolean z10) {
        LocalLog.a("GTModeBroadcastReceiver", "changeScreenRefresh state= " + z10);
        if (z10) {
            int i10 = Settings.Global.getInt(context.getContentResolver(), "low_power", 0);
            LocalLog.l("GTModeBroadcastReceiver", "changeScreenRefresh  powerMode = " + i10);
            if (i10 == 1) {
                Settings.Secure.putIntForUser(context.getContentResolver(), "oplus_customize_screen_refresh_rate", Settings.System.getIntForUser(context.getContentResolver(), "power_save_pre_refresh_state", 0, 0), 0);
                d(context, 0);
            }
            e(context, -100);
            return;
        }
        e(context, -200);
    }

    public static void c(Context context, int i10) {
        m(context, false);
        LocalLog.l("GTModeBroadcastReceiver", "close GT mode reason = " + i10);
        Settings.System.putIntForUser(context.getContentResolver(), "gt_mode_state_setting", 0, 0);
        if (i10 != 4) {
            Toast.makeText(context, R.string.rm_gt_mode_toast_content, 0).show();
        }
        l(context, 0);
    }

    private static void d(Context context, int i10) {
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (i10 == 1) {
            powerManager.setPowerSaveModeEnabled(true);
        } else if (i10 == 0) {
            powerManager.setPowerSaveModeEnabled(false);
        }
        LocalLog.a("GTModeBroadcastReceiver", "gt open power save mode = " + i10);
    }

    @SuppressLint({"WrongConstant"})
    public static void e(Context context, int i10) {
        Intent intent = new Intent();
        intent.setAction("rm.gt.screen.rate.change.action");
        if (i10 == -100) {
            intent.putExtra("gt_screen_rate_change_to_max_key", true);
        } else if (i10 == -200) {
            intent.putExtra("gt_screen_rate_change_back_key", true);
        }
        intent.addFlags(16777216);
        context.sendBroadcastAsUser(intent, UserHandle.SYSTEM);
    }

    public static String f() {
        try {
            ComponentName topActivityComponentName = new OplusActivityManager().getTopActivityComponentName();
            if (topActivityComponentName == null) {
                return null;
            }
            String packageName = topActivityComponentName.getPackageName();
            LocalLog.d("GTModeBroadcastReceiver", "getTopApp packageName =" + packageName);
            return packageName;
        } catch (Exception unused) {
            LocalLog.b("GTModeBroadcastReceiver", "getTopApp Exception");
            return null;
        }
    }

    public static boolean g(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "gt_mode_block_message_setting", 0, 0) == 1;
    }

    private static boolean h() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.vibrator_xlinear_type");
    }

    private static boolean i(Context context) {
        int i10 = context.getResources().getConfiguration().orientation;
        LocalLog.d("GTModeBroadcastReceiver", "ori =" + i10);
        return i10 == 2;
    }

    public static boolean j(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setClassName("com.oplus.gtmode", "com.oplus.gtmode.GtmodeActivity");
        if (packageManager.resolveActivity(intent, 0) != null) {
            LocalLog.b("GTModeBroadcastReceiver", "isSupportGTapp true");
            return true;
        }
        LocalLog.l("GTModeBroadcastReceiver", "isSupportGTapp false");
        return false;
    }

    public static void k(Context context) {
        l(context, 1);
        Settings.System.putIntForUser(context.getContentResolver(), "gt_mode_state_setting", 1, 0);
        LocalLog.l("GTModeBroadcastReceiver", "open GT mode");
    }

    public static void l(Context context, int i10) {
        Settings.System.putIntForUser(context.getContentResolver(), "gt_mode_block_message_setting", i10, 0);
    }

    @SuppressLint({"WrongConstant"})
    private static void m(Context context, boolean z10) {
        String f10;
        String[] stringArray;
        if (j(context)) {
            Intent intent = new Intent();
            if (z10) {
                intent.setAction("oplus.intent.action.GT_OPEN");
            } else {
                intent.setAction("oplus.intent.action.GT_CLOSE");
            }
            if (z10 && y5.b.w() && y5.b.v() && (f10 = f()) != null && (stringArray = context.getResources().getStringArray(R.array.gt_video_app_list)) != null) {
                for (String str : stringArray) {
                    if (TextUtils.equals(str, f10)) {
                        if (i(context)) {
                            intent.putExtra("show_video_anim", "landscape");
                        } else {
                            intent.putExtra("show_video_anim", "portrait");
                        }
                    }
                }
            }
            intent.putExtra("source_pkg", "battery");
            intent.setComponent(new ComponentName("com.oplus.gtmode", "com.oplus.gtmode.receiver.GtmodeBatteryReceiver"));
            intent.addFlags(16777216);
            context.sendBroadcastAsUser(intent, UserHandle.SYSTEM, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    @SuppressLint({"WrongConstant"})
    public static void n(Context context, String str) {
        if ("com.android.launcher".equals(str) && Settings.System.getIntForUser(context.getContentResolver(), "gt_mode_state_setting", 0, 0) != 0 && j(context)) {
            Intent intent = new Intent();
            intent.setAction("oplus.intent.action.LAUNCHER_VISIBLE");
            intent.setComponent(new ComponentName("com.oplus.gtmode", "com.oplus.gtmode.receiver.GtmodeLauncherStateReceiver"));
            intent.addFlags(16777216);
            context.sendBroadcastAsUser(intent, UserHandle.SYSTEM, "oplus.permission.OPLUS_COMPONENT_SAFE");
        }
    }

    public static void o(Context context) {
        if (h()) {
            LocalLog.a("GTModeBroadcastReceiver", "isLinearmotoSupport");
            ((LinearmotorVibrator) context.getSystemService("linearmotor")).vibrate(new WaveformEffect.Builder().setEffectType(DataLinkConstants.GENERATOR_ID_MUTE_PREFER).build());
        } else {
            ((Vibrator) context.getSystemService("vibrator")).vibrate(2000L);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (y5.b.E()) {
            if ("gt_mode_broadcast_intent_close_action".equals(intent.getAction())) {
                c(context, 0);
            }
            if ("gt_mode_broadcast_intent_open_action".equals(intent.getAction())) {
                m(context, true);
                k(context);
            }
            if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction()) && Settings.System.getIntForUser(context.getContentResolver(), "gt_mode_state_setting", 0, 0) == 1) {
                c(context, 4);
            }
            if ("gtmode_intent_open_action".equals(intent.getAction())) {
                k(context);
            }
        }
    }
}
