package com.oplus.powermanager.fuelgaue.view.batteryUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import b6.LocalLog;
import com.oplus.battery.R;
import t8.PowerUsageManager;
import w1.COUIDarkModeUtil;

/* loaded from: classes2.dex */
public class BatteryLevelView extends View {
    private static int A;
    private static int B;
    private static int C;
    private static int D;

    /* renamed from: u, reason: collision with root package name */
    private static int f10280u;

    /* renamed from: v, reason: collision with root package name */
    private static int f10281v;

    /* renamed from: w, reason: collision with root package name */
    private static int f10282w;

    /* renamed from: x, reason: collision with root package name */
    private static int f10283x;

    /* renamed from: y, reason: collision with root package name */
    private static int f10284y;

    /* renamed from: z, reason: collision with root package name */
    private static int f10285z;

    /* renamed from: e, reason: collision with root package name */
    private float f10286e;

    /* renamed from: f, reason: collision with root package name */
    private float f10287f;

    /* renamed from: g, reason: collision with root package name */
    private Context f10288g;

    /* renamed from: h, reason: collision with root package name */
    private PaintFlagsDrawFilter f10289h;

    /* renamed from: i, reason: collision with root package name */
    private int f10290i;

    /* renamed from: j, reason: collision with root package name */
    private int f10291j;

    /* renamed from: k, reason: collision with root package name */
    private int f10292k;

    /* renamed from: l, reason: collision with root package name */
    private int f10293l;

    /* renamed from: m, reason: collision with root package name */
    private float f10294m;

    /* renamed from: n, reason: collision with root package name */
    private Paint f10295n;

    /* renamed from: o, reason: collision with root package name */
    private Paint f10296o;

    /* renamed from: p, reason: collision with root package name */
    private Paint f10297p;

    /* renamed from: q, reason: collision with root package name */
    private Path f10298q;

    /* renamed from: r, reason: collision with root package name */
    private float f10299r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f10300s;

    /* renamed from: t, reason: collision with root package name */
    private Xfermode f10301t;

    public BatteryLevelView(Context context) {
        super(context);
        this.f10286e = a(312.0f);
        this.f10287f = a(40.0f);
        this.f10288g = null;
        this.f10289h = null;
        this.f10290i = -1;
        this.f10291j = -1;
        this.f10292k = -1;
        this.f10294m = -1.0f;
        this.f10300s = true;
    }

    private float a(float f10) {
        return (getContext().getResources().getDisplayMetrics().density * f10) + ((f10 >= 0.0f ? 1 : -1) * 0.5f);
    }

    private int b(Canvas canvas, float f10, float f11) {
        int i10;
        boolean a10 = COUIDarkModeUtil.a(this.f10288g);
        int i11 = a10 ? f10281v : f10280u;
        boolean z10 = Settings.Global.getInt(this.f10288g.getContentResolver(), "low_power", 0) == 1;
        if (Settings.System.getIntForUser(this.f10288g.getContentResolver(), "oplus_battery_settings_bms_heat_status", 0, 0) == 1) {
            i10 = -295866;
        } else {
            if (z10) {
                i11 = a10 ? B : A;
            } else if (this.f10290i <= 20) {
                i11 = a10 ? D : C;
            }
            int i12 = this.f10292k;
            if (i12 == -2) {
                i10 = a10 ? f10281v : f10280u;
                this.f10291j = i12;
            } else if (i12 != -1) {
                int chargeWattage = getChargeWattage();
                int i13 = this.f10292k;
                if (i13 == 0 && this.f10291j > 0) {
                    LocalLog.l("BatteryLevelView", "ignore charge type change from vooc to normal");
                    chargeWattage = this.f10293l;
                } else {
                    this.f10293l = chargeWattage;
                    this.f10291j = i13;
                }
                int i14 = a10 ? f10281v : f10280u;
                if (chargeWattage >= 100) {
                    i10 = a10 ? f10284y : f10285z;
                } else if (chargeWattage >= 33) {
                    i10 = a10 ? f10282w : f10283x;
                } else {
                    i10 = i14;
                }
            } else {
                this.f10291j = -1;
                this.f10293l = 0;
                i10 = i11;
            }
        }
        Paint paint = new Paint(1);
        Paint paint2 = new Paint(1);
        Paint paint3 = new Paint(1);
        RectF rectF = new RectF(f10, f11, this.f10286e + f10, this.f10287f + f11);
        Path path = new Path();
        path.reset();
        path.addRoundRect(rectF, a(10.0f), a(10.0f), Path.Direction.CW);
        float f12 = (this.f10290i * 1.0f) / 100.0f;
        paint.setColor(i10);
        paint.setStyle(Paint.Style.FILL);
        paint3.setColor(520093696);
        paint3.setStyle(Paint.Style.FILL);
        paint2.setAlpha(43);
        canvas.clipPath(path);
        float f13 = this.f10286e;
        canvas.drawRect(f10 + (f12 * f13), f11, f10 + f13, f11 + this.f10287f, paint3);
        int saveLayer = canvas.saveLayer(null, null, 31);
        canvas.drawRect(f10, f11, f10 + (this.f10286e * f12), f11 + this.f10287f, paint);
        this.f10298q = path;
        this.f10295n = paint;
        this.f10296o = paint2;
        this.f10297p = paint3;
        this.f10299r = f12;
        return saveLayer;
    }

    private int getChargeWattage() {
        return Settings.System.getIntForUser(getContext().getContentResolver(), "oplus_battery_settings_charge_wattage", 0, 0);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(this.f10289h);
        int right = getRight() - getLeft();
        int bottom = getBottom() - getTop();
        int i10 = right / 2;
        this.f10286e = right;
        float a10 = a(40.0f);
        this.f10287f = a10;
        float f10 = i10 - (this.f10286e / 2.0f);
        float f11 = (bottom / 2) - (a10 / 2.0f);
        int r10 = PowerUsageManager.x(this.f10288g).r();
        int i11 = this.f10290i;
        if (i11 == -1 || i11 != r10) {
            this.f10290i = r10;
        }
        canvas.restoreToCount(b(canvas, f10, f11));
        this.f10300s = false;
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        setMeasuredDimension(View.MeasureSpec.getSize(i10), (int) this.f10287f);
        setForceDarkAllowed(false);
    }

    public void setChargeType(int i10) {
        this.f10292k = i10;
    }

    public BatteryLevelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10286e = a(312.0f);
        this.f10287f = a(40.0f);
        this.f10289h = null;
        this.f10290i = -1;
        this.f10291j = -1;
        this.f10292k = -1;
        this.f10294m = -1.0f;
        this.f10300s = true;
        this.f10288g = context;
        this.f10289h = new PaintFlagsDrawFilter(0, 3);
        this.f10301t = new PorterDuffXfermode(PorterDuff.Mode.XOR);
        setLayerType(1, null);
        f10280u = getResources().getColor(R.color.coui_color_primary_green);
        f10281v = getResources().getColor(R.color.coui_color_primary_green_dark);
        f10282w = getResources().getColor(R.color.coui_color_primary_blue);
        f10283x = getResources().getColor(R.color.coui_color_primary_blue_dark);
        f10284y = getResources().getColor(R.color.coui_color_primary_purple);
        f10285z = getResources().getColor(R.color.coui_color_primary_purple_dark);
        A = getResources().getColor(R.color.coui_color_primary_yellow);
        B = getResources().getColor(R.color.coui_color_primary_yellow);
        C = getResources().getColor(R.color.coui_color_primary_red);
        D = getResources().getColor(R.color.coui_color_primary_red);
    }
}
