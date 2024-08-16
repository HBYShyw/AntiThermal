package com.oplus.powermanager.wirelesscharg;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;
import b6.LocalLog;
import c9.WirelessChargingController;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.WirelessReverseChargingReminderActivity;
import d6.ConfigUpdateUtil;
import f6.f;
import x5.UploadDataUtil;
import y5.b;

/* loaded from: classes2.dex */
public class WirelessReverseControService extends TileService {

    /* renamed from: e, reason: collision with root package name */
    private Context f10425e;

    /* renamed from: f, reason: collision with root package name */
    private WirelessChargingController f10426f;

    /* renamed from: g, reason: collision with root package name */
    private UploadDataUtil f10427g = null;

    /* renamed from: h, reason: collision with root package name */
    private StatusBarManager f10428h = null;

    /* renamed from: i, reason: collision with root package name */
    private int f10429i = 25;

    /* renamed from: j, reason: collision with root package name */
    private int f10430j = 100;

    /* renamed from: k, reason: collision with root package name */
    private int f10431k = 0;

    /* renamed from: l, reason: collision with root package name */
    private int f10432l = 1;

    /* renamed from: m, reason: collision with root package name */
    private int f10433m = 25;

    /* renamed from: n, reason: collision with root package name */
    private int f10434n = 0;

    /* renamed from: o, reason: collision with root package name */
    private boolean f10435o = false;

    /* renamed from: p, reason: collision with root package name */
    private ContentObserver f10436p = new a(new Handler());

    /* loaded from: classes2.dex */
    class a extends ContentObserver {
        a(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            WirelessReverseControService.this.f();
            LocalLog.a("WirelessReverseControService", "onChange: mReverseState = " + WirelessReverseControService.this.f10435o);
            boolean unused = WirelessReverseControService.this.f10435o;
        }
    }

    private boolean c() {
        return getQsTile().getState() == 2;
    }

    private void d() {
        this.f10430j = this.f10426f.E();
        this.f10431k = this.f10426f.I();
        this.f10432l = this.f10426f.F();
        this.f10433m = this.f10426f.G();
        this.f10429i = f.c1(this.f10425e);
        this.f10434n = ConfigUpdateUtil.n(this.f10425e).E();
        this.f10435o = f.d1(this.f10425e);
    }

    @SuppressLint({"NewApi"})
    private void e() {
        Intent intent = new Intent(this.f10425e, (Class<?>) WirelessReverseChargingReminderActivity.class);
        intent.addFlags(268435456);
        startActivityAndCollapse(PendingIntent.getActivity(this.f10425e, 0, intent, 335544320));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        Tile qsTile = getQsTile();
        d();
        if (qsTile != null) {
            int i10 = this.f10432l;
            if (i10 == 2 || i10 == 5) {
                qsTile.setState(0);
            }
            if (this.f10435o) {
                qsTile.setState(2);
            } else {
                qsTile.setState(1);
            }
            qsTile.setLabel(getString(R.string.wireless_reverse_charging_title));
            qsTile.updateTile();
        }
    }

    @Override // android.service.quicksettings.TileService
    public void onClick() {
        super.onClick();
        if (!c()) {
            d();
            if (this.f10430j < this.f10429i) {
                this.f10428h.collapsePanels();
                Context context = this.f10425e;
                Toast.makeText(context, context.getString(R.string.below_battery_level_disable, Integer.valueOf(this.f10429i)), 0).show();
                return;
            }
            int i10 = this.f10432l;
            if ((i10 == 2 || i10 == 5) && this.f10431k == 4) {
                this.f10428h.collapsePanels();
                Context context2 = this.f10425e;
                Toast.makeText(context2, context2.getString(R.string.reverse_disabled_on_wireless_charging_toast), 0).show();
                return;
            }
            int i11 = this.f10431k;
            if ((i11 == 1 || i11 == 2) && ((i10 == 2 || i10 == 5) && !b.J())) {
                this.f10428h.collapsePanels();
                Context context3 = this.f10425e;
                Toast.makeText(context3, context3.getString(R.string.reverse_charge_forbbiden_by_wired_charge), 0).show();
                return;
            } else if (this.f10426f.K() == 1 && !b.J()) {
                this.f10428h.collapsePanels();
                Context context4 = this.f10425e;
                Toast.makeText(context4, context4.getString(R.string.reverse_charge_forbbiden_by_wired_otg), 0).show();
                return;
            } else if (this.f10433m >= this.f10434n) {
                this.f10428h.collapsePanels();
                Context context5 = this.f10425e;
                Toast.makeText(context5, context5.getString(R.string.reverse_disable_on_high_temp), 0).show();
                return;
            } else {
                f.y3(this.f10425e, true);
                this.f10427g.R0(true, "contro_center_click");
                e();
                return;
            }
        }
        f.y3(this.f10425e, false);
        this.f10427g.R0(false, "contro_center_click");
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f10425e = getApplicationContext();
    }

    @Override // android.service.quicksettings.TileService
    @SuppressLint({"WrongConstant"})
    public void onStartListening() {
        super.onStartListening();
        this.f10426f = WirelessChargingController.H(this.f10425e);
        this.f10425e.getContentResolver().registerContentObserver(Settings.System.getUriFor("wireless_reverse_charging_state"), false, this.f10436p, 0);
        this.f10427g = UploadDataUtil.S0(this.f10425e);
        this.f10428h = (StatusBarManager) this.f10425e.getSystemService("statusbar");
        f();
    }

    @Override // android.service.quicksettings.TileService
    public void onStopListening() {
        super.onStopListening();
        this.f10425e.getContentResolver().unregisterContentObserver(this.f10436p);
    }

    @Override // android.service.quicksettings.TileService
    public void onTileAdded() {
        super.onTileAdded();
    }
}
