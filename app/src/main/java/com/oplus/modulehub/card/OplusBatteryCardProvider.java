package com.oplus.modulehub.card;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import b6.LocalLog;
import com.oplus.cardwidget.serviceLayer.AppCardWidgetProvider;
import f5.CardWidgetAction;
import java.util.Iterator;
import java.util.List;
import q6.BatteryCardData;
import q6.BatteryCardDataPack;
import s5.CardDataTranslater;
import x5.UploadDataUtil;

/* loaded from: classes.dex */
public class OplusBatteryCardProvider extends AppCardWidgetProvider {

    /* renamed from: s, reason: collision with root package name */
    private SharedPreferences f9911s;

    /* renamed from: t, reason: collision with root package name */
    private SharedPreferences.Editor f9912t;

    private void B(List<String> list) {
        StringBuilder sb2 = new StringBuilder();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            sb2.append(it.next());
            sb2.append(",");
        }
        Log.d("OplusBatteryCardProvider", "code list = " + sb2.toString());
        Settings.System.putStringForUser(getContext().getContentResolver(), "battery_widget_code", sb2.toString(), 0);
    }

    @Override // com.oplus.cardwidget.serviceLayer.AppCardWidgetProvider, p5.ICardState
    public void f(Context context, List<String> list) {
        Log.d("OplusBatteryCardProvider", "onCardsObserve");
        super.f(context, list);
        B(list);
    }

    @Override // c5.ICardLayout
    public String i(String str) {
        String stringForUser = Settings.System.getStringForUser(getContext().getContentResolver(), "battery_widget_code", 0);
        int length = stringForUser != null ? stringForUser.split(",").length : 0;
        LocalLog.a("OplusBatteryCardProvider", "getCardLayoutName " + str + ", list " + length + ", showed " + z().size());
        if (z().size() <= length) {
            return "batteryCard.json";
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("battery_ui", 0);
        this.f9911s = sharedPreferences;
        this.f9912t = sharedPreferences.edit();
        if (CardDataTranslater.a(str) == 1) {
            int i10 = this.f9911s.getInt("battery_card_create_desktop", 0) + 1;
            this.f9912t.putInt("battery_card_create_desktop", i10);
            this.f9912t.apply();
            UploadDataUtil.S0(getContext()).m(String.valueOf(i10));
            return "batteryCard.json";
        }
        int i11 = this.f9911s.getInt("battery_card_create_assistant", 0) + 1;
        this.f9912t.putInt("battery_card_create_assistant", i11);
        this.f9912t.apply();
        UploadDataUtil.S0(getContext()).l(String.valueOf(i11));
        return "batteryCard.json";
    }

    @Override // com.oplus.cardwidget.serviceLayer.AppCardWidgetProvider, p5.ICardState
    public void k(Context context, String str) {
        Log.d("OplusBatteryCardProvider", "onDestroy " + str);
        super.k(context, str);
    }

    @Override // p5.ICardState
    public void m(Context context, String str) {
        Log.d("OplusBatteryCardProvider", "onResume " + str);
        CardWidgetAction.f11356a.a(context, new BatteryCardDataPack(new BatteryCardData(-1, -1, -1), CardDataTranslater.a(str)), str);
    }
}
