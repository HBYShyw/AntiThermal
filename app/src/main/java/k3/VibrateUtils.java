package k3;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.DynamicEffect;
import android.os.Handler;
import android.os.HapticPlayer;
import android.provider.Settings;
import android.util.Log;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.os.LinearmotorVibrator;
import com.oplus.os.WaveformEffect;
import com.oplus.sceneservice.sdk.dataprovider.bean.scene.SceneFlightData;

/* compiled from: VibrateUtils.java */
/* renamed from: k3.a, reason: use source file name */
/* loaded from: classes.dex */
public class VibrateUtils {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f14031a = false;

    /* renamed from: b, reason: collision with root package name */
    private static Context f14032b = null;

    /* renamed from: c, reason: collision with root package name */
    private static long f14033c = -1;

    /* renamed from: d, reason: collision with root package name */
    private static final ContentObserver f14034d = new a(null);

    /* compiled from: VibrateUtils.java */
    /* renamed from: k3.a$a */
    /* loaded from: classes.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            super.onChange(z10);
            boolean unused = VibrateUtils.f14031a = Settings.System.getInt(VibrateUtils.f14032b.getContentResolver(), "haptic_feedback_enabled", 0) == 1;
        }
    }

    private static boolean c() {
        if (f14033c == -1) {
            f14033c = System.currentTimeMillis();
            return false;
        }
        if (System.currentTimeMillis() - f14033c < 25) {
            return true;
        }
        f14033c = System.currentTimeMillis();
        return false;
    }

    private static DynamicEffect d(int i10, int i11) {
        return DynamicEffect.create("{\n    \"Metadata\": {\n        \"Version\": 2,\n        \"Created\": \"2023-05-12\",\n        \"Description\": \"Exported from RichTap Creator Pro\"\n    },\n    \"PatternList\": [\n        {\n            \"AbsoluteTime\": 0,\n            \"Pattern\": [\n                {\n                    \"Event\": {\n                        \"Type\": \"transient\",\n                        \"RelativeTime\": 0,\n                        \"Parameters\": {\n                            \"Intensity\": " + i11 + ",\n                            \"Frequency\": " + i10 + "\n                        },\n                        \"Index\": 0\n                    }\n                }\n            ]\n        }\n    ]\n}");
    }

    @SuppressLint({"WrongConstant"})
    public static LinearmotorVibrator e(Context context) {
        try {
            if (OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.vibrator_luxunvibrator")) {
                return (LinearmotorVibrator) context.getSystemService("linearmotor");
            }
            return null;
        } catch (Exception e10) {
            Log.e("VibrateUtils", "get linear motor vibrator failed. error = " + e10.getMessage());
            return null;
        }
    }

    private static int f(int i10, int i11, int i12, int i13) {
        int i14 = (int) ((((i10 * 1.0d) / i11) * (i13 - i12)) + i12);
        if (i12 < i13) {
            return Math.max(i12, Math.min(i14, i13));
        }
        return Math.max(i13, Math.min(i14, i12));
    }

    private static int g(int i10, int i11, int i12, int i13) {
        int i14 = (int) ((((i10 * 1.0d) / i11) * (i13 - i12)) + i12);
        if (i12 < i13) {
            return Math.max(i12, Math.min(i14, i13));
        }
        return Math.max(i13, Math.min(i14, i12));
    }

    public static boolean h(Context context) {
        try {
            return OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.vibrator_lmvibrator");
        } catch (Throwable th) {
            Log.e("VibrateUtils", "get isLinearMotorVersion failed. error = " + th.getMessage());
            return false;
        }
    }

    public static void i(Context context) {
        if (f14032b != null || context == null) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        f14032b = applicationContext;
        ContentResolver contentResolver = applicationContext.getContentResolver();
        f14031a = Settings.System.getInt(contentResolver, "haptic_feedback_enabled", 0) == 1;
        contentResolver.registerContentObserver(Settings.System.getUriFor("haptic_feedback_enabled"), false, f14034d);
    }

    public static void j(LinearmotorVibrator linearmotorVibrator, int i10, int i11, int i12, int i13, int i14) {
        if (linearmotorVibrator == null || !f14031a) {
            return;
        }
        int f10 = f(i11, i12, i13, i14);
        if (i10 == 0) {
            f10 += SceneFlightData.INVALID_LATITUDE_LONGITUDE;
        }
        linearmotorVibrator.vibrate(new WaveformEffect.Builder().setStrengthSettingEnabled(false).setEffectStrength(f10).setEffectType(i10).setAsynchronous(true).build());
    }

    public static void k(LinearmotorVibrator linearmotorVibrator, int i10, int i11, int i12, int i13, int i14, int i15, float f10) {
        if (linearmotorVibrator == null || !f14031a || c()) {
            return;
        }
        try {
            DynamicEffect d10 = d(g(i11, i12, i15 == 0 ? 75 : 48, i15 == 0 ? 90 : 55), Math.round(g(i11, i12, i15 == 0 ? 50 : 52, i15 == 0 ? 100 : 68) * f10));
            if (d10 != null) {
                HapticPlayer hapticPlayer = new HapticPlayer(d10);
                if (HapticPlayer.isAvailable()) {
                    hapticPlayer.start(1);
                    return;
                }
            }
        } catch (Exception e10) {
            Log.e("VibrateUtils", "get haptic player failed. error = " + e10.getMessage());
        }
        j(linearmotorVibrator, i10, i11, i12, i13, i14);
    }

    public static void l() {
        Context context = f14032b;
        if (context != null) {
            context.getContentResolver().unregisterContentObserver(f14034d);
            f14032b = null;
        }
    }
}
