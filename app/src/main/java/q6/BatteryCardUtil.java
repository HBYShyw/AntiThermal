package q6;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import b6.LocalLog;
import f5.CardWidgetAction;
import s5.CardDataTranslater;

/* compiled from: BatteryCardUtil.java */
/* renamed from: q6.c, reason: use source file name */
/* loaded from: classes.dex */
public class BatteryCardUtil {

    /* renamed from: b, reason: collision with root package name */
    private static BatteryCardUtil f16904b;

    /* renamed from: a, reason: collision with root package name */
    private Context f16905a;

    private BatteryCardUtil(Context context) {
        this.f16905a = context;
    }

    public static synchronized BatteryCardUtil a(Context context) {
        BatteryCardUtil batteryCardUtil;
        synchronized (BatteryCardUtil.class) {
            if (f16904b == null) {
                f16904b = new BatteryCardUtil(context);
            }
            batteryCardUtil = f16904b;
        }
        return batteryCardUtil;
    }

    public void b(int i10, int i11, int i12) {
        String stringForUser = Settings.System.getStringForUser(this.f16905a.getContentResolver(), "battery_widget_code", 0);
        if (stringForUser == null) {
            LocalLog.b("BatteryCardUtil", "widgetCodes = null !");
            return;
        }
        for (String str : stringForUser.split(",")) {
            Log.d("BatteryCardUtil", "code = " + str);
            if (str.contains("188")) {
                try {
                    CardWidgetAction.f11356a.a(this.f16905a, new BatteryCardDataPack(new BatteryCardData(i10, i11, i12), CardDataTranslater.a(str)), str);
                } catch (IllegalStateException e10) {
                    Log.e("BatteryCardUtil", "updateBatteryCard error, e=" + e10);
                }
            }
        }
    }
}
