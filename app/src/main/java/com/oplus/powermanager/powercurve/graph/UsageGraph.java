package com.oplus.powermanager.powercurve.graph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.icu.text.NumberFormat;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Range;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import b6.LocalLog;
import b9.BatteryPowerHelper;
import com.oplus.battery.R;
import com.oplus.powermanager.powercurve.graph.UsageGraph;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import l8.IPowerRankingUpdate;
import q8.CirclePaint;
import q8.LinePaintYLabel;
import q8.TextPaintTips;
import q8.TextPaintXLabel;
import q8.TextPaintYLabel;
import r8.LSLinkedHashMap;
import r8.StatusBean;
import r8.TimeLabelBean;
import t8.PowerUsageManager;
import x5.UploadDataUtil;

/* loaded from: classes2.dex */
public class UsageGraph extends View {
    HashMap<Float, Range<Float>> A;
    private String A0;
    private final int B;
    private float B0;
    private final int[] C;
    private boolean C0;
    List<String> D;
    private boolean D0;
    List<String> E;
    private IPowerRankingUpdate E0;
    private float F;
    private boolean F0;
    private float G;
    private Xfermode G0;
    private float H;
    private int H0;
    private float I;
    private float J;
    private float K;
    private float L;
    private float M;
    private float N;
    private boolean O;
    private float P;
    private boolean Q;
    private boolean R;
    private boolean S;
    private boolean T;
    private long U;
    private long V;
    private float W;

    /* renamed from: a0, reason: collision with root package name */
    private float f10347a0;

    /* renamed from: b0, reason: collision with root package name */
    private final float f10348b0;

    /* renamed from: c0, reason: collision with root package name */
    private final float f10349c0;

    /* renamed from: d0, reason: collision with root package name */
    private final float f10350d0;

    /* renamed from: e, reason: collision with root package name */
    private final String f10351e;

    /* renamed from: e0, reason: collision with root package name */
    private final float f10352e0;

    /* renamed from: f, reason: collision with root package name */
    private final String f10353f;

    /* renamed from: f0, reason: collision with root package name */
    private final float f10354f0;

    /* renamed from: g, reason: collision with root package name */
    private final String f10355g;

    /* renamed from: g0, reason: collision with root package name */
    private final float f10356g0;

    /* renamed from: h, reason: collision with root package name */
    private final String f10357h;

    /* renamed from: h0, reason: collision with root package name */
    private float f10358h0;

    /* renamed from: i, reason: collision with root package name */
    private final String f10359i;

    /* renamed from: i0, reason: collision with root package name */
    private final float f10360i0;

    /* renamed from: j, reason: collision with root package name */
    private final Paint f10361j;

    /* renamed from: j0, reason: collision with root package name */
    private float f10362j0;

    /* renamed from: k, reason: collision with root package name */
    private final Paint f10363k;

    /* renamed from: k0, reason: collision with root package name */
    private final float f10364k0;

    /* renamed from: l, reason: collision with root package name */
    private final Paint f10365l;

    /* renamed from: l0, reason: collision with root package name */
    private final float f10366l0;

    /* renamed from: m, reason: collision with root package name */
    private final Paint f10367m;

    /* renamed from: m0, reason: collision with root package name */
    private final float f10368m0;

    /* renamed from: n, reason: collision with root package name */
    private final Paint f10369n;

    /* renamed from: n0, reason: collision with root package name */
    private final float f10370n0;

    /* renamed from: o, reason: collision with root package name */
    private final Paint f10371o;

    /* renamed from: o0, reason: collision with root package name */
    private final float f10372o0;

    /* renamed from: p, reason: collision with root package name */
    private final Paint f10373p;

    /* renamed from: p0, reason: collision with root package name */
    private final float f10374p0;

    /* renamed from: q, reason: collision with root package name */
    private final Paint f10375q;

    /* renamed from: q0, reason: collision with root package name */
    private final float f10376q0;

    /* renamed from: r, reason: collision with root package name */
    private final Paint f10377r;

    /* renamed from: r0, reason: collision with root package name */
    private final float f10378r0;

    /* renamed from: s, reason: collision with root package name */
    private final Paint f10379s;

    /* renamed from: s0, reason: collision with root package name */
    private final float f10380s0;

    /* renamed from: t, reason: collision with root package name */
    private final Paint f10381t;

    /* renamed from: t0, reason: collision with root package name */
    private final float f10382t0;

    /* renamed from: u, reason: collision with root package name */
    private final int f10383u;

    /* renamed from: u0, reason: collision with root package name */
    private final float f10384u0;

    /* renamed from: v, reason: collision with root package name */
    private final Path f10385v;

    /* renamed from: v0, reason: collision with root package name */
    private final float f10386v0;

    /* renamed from: w, reason: collision with root package name */
    LSLinkedHashMap<String, StatusBean> f10387w;

    /* renamed from: w0, reason: collision with root package name */
    private final float f10388w0;

    /* renamed from: x, reason: collision with root package name */
    LinkedHashMap<Float, StatusBean> f10389x;

    /* renamed from: x0, reason: collision with root package name */
    private final float f10390x0;

    /* renamed from: y, reason: collision with root package name */
    LinkedHashMap<Float, String> f10391y;

    /* renamed from: y0, reason: collision with root package name */
    private float f10392y0;

    /* renamed from: z, reason: collision with root package name */
    LinkedHashMap<Float, TimeLabelBean> f10393z;

    /* renamed from: z0, reason: collision with root package name */
    private String f10394z0;

    public UsageGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f10385v = new Path();
        this.f10387w = new LSLinkedHashMap<>();
        this.f10389x = new LinkedHashMap<>();
        this.f10391y = new LinkedHashMap<>();
        this.f10393z = new LinkedHashMap<>();
        this.A = new HashMap<>();
        this.C = new int[]{100, 75, 50, 25, 0};
        this.D = new ArrayList(Arrays.asList("00:00", "04:00", "08:00", "12:00", "16:00", "20:00"));
        this.E = new ArrayList(Arrays.asList("00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"));
        this.F = 100.0f;
        this.G = 100.0f;
        this.H = -1.0f;
        this.I = -1.0f;
        this.J = -1.0f;
        this.K = -1.0f;
        this.L = -1.0f;
        this.M = -1.0f;
        this.N = -1.0f;
        this.O = true;
        this.P = -1.0f;
        this.Q = true;
        this.R = true;
        this.S = true;
        this.T = true;
        this.U = 0L;
        this.V = 0L;
        this.W = 0.0f;
        this.f10347a0 = 0.0f;
        this.C0 = false;
        this.D0 = true;
        this.E0 = null;
        this.F0 = true;
        Resources resources = context.getResources();
        this.S = DateFormat.is24HourFormat(getContext());
        this.f10351e = resources.getString(R.string.battery_ui_now);
        this.f10353f = resources.getString(R.string.battery_ui_am);
        this.f10355g = resources.getString(R.string.battery_ui_pm);
        this.f10357h = resources.getString(R.string.battery_ui_normal_use);
        this.f10359i = resources.getString(R.string.battery_detail_view_high_powerconsum);
        this.f10348b0 = getResources().getDimension(R.dimen.usage_graph_margin_left);
        this.f10349c0 = getResources().getDimension(R.dimen.usage_graph_margin_right);
        this.f10350d0 = getResources().getDimension(R.dimen.usage_graph_margin_right_offset);
        this.f10352e0 = getResources().getDimension(R.dimen.usage_graph_margin_right_level_text_offset);
        this.f10354f0 = getResources().getDimension(R.dimen.usage_graph_margin_top);
        this.f10356g0 = getResources().getDimension(R.dimen.usage_graph_margin_top_from_title);
        this.f10358h0 = getResources().getDimension(R.dimen.usage_graph_margin_bottom);
        this.f10360i0 = getResources().getDimension(R.dimen.usage_graph_time_text_margin_top);
        this.f10362j0 = getResources().getDimension(R.dimen.usage_graph_tips_margin_top);
        this.f10364k0 = getResources().getDimension(R.dimen.usage_graph_tips_offset1);
        this.f10366l0 = getResources().getDimension(R.dimen.usage_graph_tips_offset2);
        this.f10368m0 = getResources().getDimension(R.dimen.usage_graph_tips_offset3);
        this.f10370n0 = getResources().getDimension(R.dimen.usage_graph_tips_circle_radius);
        this.f10372o0 = getResources().getDimension(R.dimen.usage_graph_tips_circle_ring_width);
        float dimension = getResources().getDimension(R.dimen.usage_graph_height_increase);
        this.f10374p0 = dimension;
        this.G0 = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        this.f10376q0 = getResources().getDimension(R.dimen.usage_graph_superscript_offset);
        this.f10378r0 = getResources().getDimension(R.dimen.usage_graph_superscript_rect_width);
        this.f10380s0 = getResources().getDimension(R.dimen.usage_graph_superscript_rect_height);
        this.f10382t0 = getResources().getDimension(R.dimen.usage_graph_time_label_rect_height);
        this.f10390x0 = getResources().getDimension(R.dimen.usage_graph_curve_x_offset);
        this.f10384u0 = getResources().getDimension(R.dimen.usage_graph_superscript_arrow_width);
        this.f10386v0 = getResources().getDimension(R.dimen.usage_graph_superscript_arrow_height);
        this.f10388w0 = getResources().getDimension(R.dimen.usage_graph_superscript_rect_radius);
        if (!this.S) {
            this.f10362j0 += dimension;
            this.f10358h0 += dimension;
        }
        Paint paint = new Paint();
        this.f10361j = paint;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.usage_graph_line_corner_radius);
        this.B = dimensionPixelSize;
        paint.setPathEffect(new CornerPathEffect(dimensionPixelSize));
        paint.setStrokeWidth(resources.getDimensionPixelSize(R.dimen.usage_graph_line_width));
        paint.setColor(context.getColor(R.color.curve_theme_green_color));
        this.f10367m = new LinePaintYLabel(context);
        this.f10369n = new TextPaintYLabel(context);
        this.f10371o = new TextPaintXLabel(context);
        this.f10373p = new TextPaintTips(context);
        this.f10375q = new CirclePaint(context);
        CirclePaint circlePaint = new CirclePaint(context);
        this.f10377r = circlePaint;
        TextPaintYLabel textPaintYLabel = new TextPaintYLabel(context);
        this.f10381t = textPaintYLabel;
        circlePaint.setColor(context.getColor(R.color.curve_theme_green_color));
        textPaintYLabel.setColor(context.getColor(R.color.curve_superscript_text_color));
        CirclePaint circlePaint2 = new CirclePaint(context);
        this.f10379s = circlePaint2;
        circlePaint2.setColor(context.getColor(R.color.curve_theme_green_color));
        circlePaint2.setPathEffect(new CornerPathEffect(resources.getDimensionPixelSize(R.dimen.usage_graph_superscript_arrow_radius)));
        Paint paint2 = new Paint(paint);
        this.f10363k = paint2;
        paint2.setStyle(Paint.Style.FILL);
        paint2.setStrokeCap(Paint.Cap.SQUARE);
        paint2.setStrokeJoin(Paint.Join.MITER);
        paint2.setPathEffect(null);
        Paint paint3 = new Paint(paint);
        this.f10365l = paint3;
        paint3.setStyle(Paint.Style.STROKE);
        float dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.usage_graph_dot_size);
        float dimensionPixelSize3 = resources.getDimensionPixelSize(R.dimen.usage_graph_dot_interval);
        paint3.setStrokeWidth(3.0f * dimensionPixelSize2);
        paint3.setPathEffect(new DashPathEffect(new float[]{dimensionPixelSize2, dimensionPixelSize3}, 0.0f));
        paint3.setColor(context.getColor(R.color.usage_graph_dots));
        this.f10383u = resources.getDimensionPixelSize(R.dimen.usage_graph_divider_size);
        o(this.D);
        o(this.E);
        setForceDarkAllowed(false);
    }

    private void d() {
        this.f10387w.clear();
        this.f10389x.clear();
        this.f10391y.clear();
        this.f10393z.clear();
    }

    private float e(float f10) {
        return (getContext().getResources().getDisplayMetrics().density * f10) + ((f10 >= 0.0f ? 1 : -1) * 0.5f);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x017f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void f(Canvas canvas) {
        LocalLog.a("UsageGraph", "isSwipeRight:" + this.O + " mFilledStartLeftX:" + this.K + " mFilledStartRightX:" + this.L);
        float f10 = this.I;
        if (f10 != -1.0f) {
            float f11 = this.J;
            if (f11 != -1.0f) {
                if (f10 > f11) {
                    this.I = f11;
                    this.J = f10;
                }
                this.f10385v.reset();
                if (this.f10389x.size() <= 1) {
                    Map.Entry<Float, StatusBean> next = this.f10389x.entrySet().iterator().next();
                    this.f10385v.moveTo(next.getKey().floatValue(), next.getValue().c());
                    int a10 = next.getValue().a();
                    float floatValue = next.getKey().floatValue();
                    next.getValue().c();
                    for (Map.Entry<Float, StatusBean> entry : this.f10389x.entrySet()) {
                        if (a10 - entry.getValue().a() >= 10) {
                            this.f10361j.setColor(((View) this).mContext.getColor(R.color.curve_theme_yellow_color));
                            this.f10375q.setColor(((View) this).mContext.getColor(R.color.curve_theme_yellow_color));
                            Range<Float> range = new Range<>(Float.valueOf(floatValue), entry.getKey());
                            Range<Float> range2 = this.A.get(Float.valueOf(floatValue));
                            if (range2 == null || !range2.contains(range)) {
                                this.A.put(Float.valueOf(floatValue), range);
                            }
                        } else {
                            this.f10361j.setColor(((View) this).mContext.getColor(R.color.curve_theme_green_color));
                            this.f10375q.setColor(((View) this).mContext.getColor(R.color.curve_theme_green_color));
                        }
                        floatValue = entry.getKey().floatValue();
                        float f12 = this.I;
                        if (f12 != -1.0f && (floatValue <= f12 || floatValue > this.J)) {
                            this.f10361j.setColor(((View) this).mContext.getColor(R.color.usage_graph_dots));
                            this.f10375q.setColor(((View) this).mContext.getColor(R.color.usage_graph_dots));
                        }
                        float c10 = entry.getValue().c();
                        this.f10385v.lineTo(floatValue, c10);
                        canvas.drawPath(this.f10385v, this.f10361j);
                        this.f10385v.reset();
                        this.f10385v.moveTo(floatValue, c10);
                        a10 = entry.getValue().a();
                    }
                    return;
                }
                this.f10385v.moveTo(k(0.0f), m(PowerUsageManager.x(((View) this).mContext).r()));
                this.f10385v.lineTo(k(-1.0f), m(PowerUsageManager.x(((View) this).mContext).r()));
                canvas.drawPath(this.f10385v, this.f10361j);
                this.f10375q.setColor(((View) this).mContext.getColor(R.color.curve_theme_green_color));
                return;
            }
        }
        this.I = -1.0f;
        this.J = -1.0f;
        this.f10385v.reset();
        if (this.f10389x.size() <= 1) {
        }
    }

    private void g(Canvas canvas) {
        float f10 = this.I;
        float f11 = -1.0f;
        if (f10 == -1.0f) {
            LocalLog.a("UsageGraph", "drawFilledPath invalid");
            return;
        }
        if (f10 != this.J && this.f10389x.size() > 1) {
            if (this.f10389x.size() > 1) {
                Map.Entry<Float, StatusBean> next = this.f10389x.entrySet().iterator().next();
                int a10 = next.getValue().a();
                float c10 = next.getValue().c();
                float f12 = -1.0f;
                float f13 = -1.0f;
                for (Map.Entry<Float, StatusBean> entry : this.f10389x.entrySet()) {
                    f12 = entry.getKey().floatValue();
                    float c11 = entry.getValue().c();
                    if (this.I <= entry.getKey().floatValue() && this.J >= entry.getKey().floatValue()) {
                        Float key = entry.getKey();
                        if (a10 - entry.getValue().a() >= 10) {
                            this.f10363k.setColor(((View) this).mContext.getColor(R.color.curve_theme_yellow_color));
                            u(((View) this).mContext.getColor(R.color.curve_theme_yellow_color), key.floatValue());
                        } else {
                            this.f10363k.setColor(((View) this).mContext.getColor(R.color.curve_theme_green_color));
                            u(((View) this).mContext.getColor(R.color.curve_theme_green_color), key.floatValue());
                        }
                        float c12 = entry.getValue().c();
                        if (f13 != f11) {
                            this.f10385v.moveTo(key.floatValue(), c12);
                            this.f10385v.lineTo(key.floatValue(), c12);
                            this.f10385v.lineTo(key.floatValue(), m(0.0f));
                            this.f10385v.lineTo(f13, m(0.0f));
                            this.f10385v.lineTo(f13, this.f10389x.get(Float.valueOf(f13)).c());
                            this.f10385v.lineTo(key.floatValue(), c12);
                        }
                        f13 = key.floatValue();
                        a10 = entry.getValue().a();
                        canvas.drawPath(this.f10385v, this.f10363k);
                        this.f10385v.reset();
                    }
                    c10 = c11;
                    f11 = -1.0f;
                }
                canvas.drawCircle(f12, c10, this.f10370n0, this.f10375q);
                this.f10375q.setXfermode(this.G0);
                canvas.drawCircle(f12, c10, this.f10370n0 - this.f10372o0, this.f10375q);
                this.f10375q.setXfermode(null);
            } else {
                this.f10363k.setColor(((View) this).mContext.getColor(R.color.curve_theme_green_color));
                u(((View) this).mContext.getColor(R.color.curve_theme_green_color), l(0.0f));
                this.f10385v.moveTo(k(0.0f), m(PowerUsageManager.x(((View) this).mContext).r()));
                this.f10385v.lineTo(k(-1.0f), m(PowerUsageManager.x(((View) this).mContext).r()));
                this.f10385v.lineTo(k(-1.0f), m(0.0f));
                this.f10385v.lineTo(k(0.0f), m(0.0f));
                this.f10385v.lineTo(k(0.0f), m(PowerUsageManager.x(((View) this).mContext).r()));
                canvas.drawPath(this.f10385v, this.f10363k);
                canvas.drawCircle(k(-1.0f), m(PowerUsageManager.x(((View) this).mContext).r()), this.f10370n0, this.f10375q);
                this.f10375q.setXfermode(this.G0);
                canvas.drawCircle(k(-1.0f), m(PowerUsageManager.x(((View) this).mContext).r()), this.f10370n0 - this.f10372o0, this.f10375q);
                this.f10375q.setXfermode(null);
            }
            canvas.restoreToCount(this.H0);
            this.f10385v.reset();
            this.V = w(this.J);
            this.U = w(this.I);
            if (this.f10393z.get(Float.valueOf(this.I)) == null || this.f10393z.get(Float.valueOf(this.J)) == null || !this.T) {
                return;
            }
            this.f10394z0 = this.f10393z.get(Float.valueOf(this.I)).a();
            this.A0 = this.f10393z.get(Float.valueOf(this.J)).a();
            this.B0 = (this.I + this.J) / 2.0f;
            String str = this.f10394z0 + "-" + this.A0;
            this.f10381t.getTextBounds(str, 0, str.length(), new Rect());
            float min = Math.min(l(-1.0f) - r4.width(), Math.max(this.f10348b0 / 2.0f, (this.B0 - (r4.width() / 2.0f)) - this.f10388w0));
            float min2 = Math.min(this.f10392y0, this.f10389x.get(Float.valueOf(this.I)).c()) - this.f10376q0;
            RectF rectF = new RectF(min, min2 - this.f10380s0, r4.width() + (this.f10388w0 * 2.0f) + min, min2);
            float f14 = this.f10380s0;
            canvas.drawRoundRect(rectF, f14 / 2.0f, f14 / 2.0f, this.f10377r);
            canvas.drawText(str, min + this.f10388w0, (min2 - (this.f10380s0 / 2.0f)) + (r4.height() / 2.0f), this.f10381t);
            int i10 = (int) this.B0;
            LocalLog.a("UsageGraph", "drawFilledPath: arrowCenterX=" + i10 + " mSuperscriptRadius=" + this.f10388w0 + " y=" + min2);
            if (i10 <= this.f10388w0 * 2.0f) {
                LocalLog.a("UsageGraph", "drawFilledPath: <");
                i10 = (int) (this.B + (this.f10384u0 / 2.0f));
            } else if (i10 >= ((int) (l(-1.0f) - (this.f10388w0 * 2.0f)))) {
                LocalLog.a("UsageGraph", "drawFilledPath: >");
                i10 = (int) ((l(-1.0f) - (this.f10388w0 / 2.0f)) - (this.f10384u0 / 1.5d));
            }
            float f15 = i10;
            int i11 = (int) min2;
            Point point = new Point((int) (f15 - (this.f10384u0 / 2.0f)), i11);
            Point point2 = new Point((int) (f15 + (this.f10384u0 / 2.0f)), i11);
            Point point3 = new Point(i10, (int) (min2 + this.f10386v0));
            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(point2.x, point2.y);
            path.lineTo(point3.x, point3.y);
            path.lineTo(point.x, point.y);
            path.close();
            canvas.drawPath(path, this.f10379s);
            return;
        }
        canvas.drawCircle(l(-1.0f), m(PowerUsageManager.x(((View) this).mContext).r()), this.f10370n0, this.f10375q);
        this.f10375q.setXfermode(this.G0);
        canvas.drawCircle(l(-1.0f), m(PowerUsageManager.x(((View) this).mContext).r()), this.f10370n0 - this.f10372o0, this.f10375q);
        this.f10375q.setXfermode(null);
    }

    private float getCurveWidth() {
        return (this.f10347a0 - this.f10350d0) - this.f10348b0;
    }

    private ArrayList<String> getTimeLabelList() {
        SimpleDateFormat simpleDateFormat;
        ArrayList<String> arrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(12, 0);
        if (DateFormat.is24HourFormat(getContext())) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
        }
        calendar.add(11, 1);
        for (int i10 = 0; i10 < 24; i10++) {
            arrayList.add(simpleDateFormat.format(new Date(calendar.getTimeInMillis())).substring(r5.length() - 5));
            calendar.add(11, 1);
        }
        return arrayList;
    }

    private ArrayList<TimeLabelBean> getTimeLabelListForSuperscript() {
        SimpleDateFormat simpleDateFormat;
        ArrayList<TimeLabelBean> arrayList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(13, 0);
        calendar.set(14, 0);
        int i10 = calendar.get(12);
        calendar.set(12, 0);
        if (i10 >= 30) {
            calendar.add(11, 1);
        }
        if (DateFormat.is24HourFormat(getContext())) {
            simpleDateFormat = new SimpleDateFormat("HH:mm", ((View) this).mContext.getResources().getConfiguration().getLocales().get(0));
        } else {
            simpleDateFormat = new SimpleDateFormat("hh:mm", ((View) this).mContext.getResources().getConfiguration().getLocales().get(0));
        }
        calendar.add(11, -1);
        for (int i11 = 0; i11 < 26; i11++) {
            arrayList.add(new TimeLabelBean(simpleDateFormat.format(new Date(calendar.getTimeInMillis())), calendar.get(11) < 12));
            calendar.add(11, 1);
        }
        return arrayList;
    }

    private void h(Canvas canvas) {
        char c10;
        String str;
        char c11;
        String str2;
        LocalLog.a("UsageGraph", "drawTimeLabel");
        String str3 = this.f10351e;
        this.f10371o.getTextBounds(str3, 0, str3.length(), new Rect());
        float height = this.f10360i0 + r4.height();
        float l10 = l(-1.0f) - r4.width();
        if (this.f10389x.size() != 49) {
            canvas.drawText(str3, l10, height, this.f10371o);
        }
        char c12 = 0;
        char c13 = 65535;
        int i10 = 4;
        if (this.f10389x.size() > 1 && this.f10389x.size() < 49) {
            int integer = getResources().getInteger(R.integer.usage_graph_show_time_intervals);
            if (integer != 4) {
                i10 = integer;
            } else if (this.f10389x.size() <= 9) {
                i10 = 2;
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap(this.f10393z);
            Iterator it = linkedHashMap.entrySet().iterator();
            Map.Entry entry = (Map.Entry) it.next();
            if (((Float) ((Map.Entry) it.next()).getKey()).floatValue() - ((Float) entry.getKey()).floatValue() <= l(1.0f) - this.f10348b0 && linkedHashMap.size() > 2) {
                linkedHashMap.remove(entry.getKey());
            }
            int i11 = 0;
            for (Map.Entry entry2 : linkedHashMap.entrySet()) {
                if (i11 % i10 == 0) {
                    this.f10371o.getTextBounds(((TimeLabelBean) entry2.getValue()).a(), 0, ((TimeLabelBean) entry2.getValue()).a().length(), new Rect());
                    float max = Math.max(((Float) entry2.getKey()).floatValue(), this.f10348b0 + (r9.width() / 2.0f));
                    if (Math.abs(l10 - max) > r9.width()) {
                        if (c13 != ((TimeLabelBean) entry2.getValue()).b() && !this.S) {
                            if (((TimeLabelBean) entry2.getValue()).b()) {
                                str2 = this.f10353f;
                                c13 = 1;
                            } else {
                                str2 = this.f10355g;
                                c13 = 0;
                            }
                            canvas.drawText(str2, max, this.f10382t0 + height, this.f10371o);
                        }
                        canvas.drawText(((TimeLabelBean) entry2.getValue()).a(), max, height, this.f10371o);
                    }
                }
                i11++;
            }
            return;
        }
        if (this.f10389x.size() == 49) {
            int integer2 = getResources().getInteger(R.integer.usage_graph_show_time_intervals);
            List<String> list = integer2 == 4 ? this.D : this.E;
            ArrayList arrayList = new ArrayList(this.f10393z.entrySet());
            int i12 = 0;
            while (i12 < arrayList.size()) {
                Map.Entry entry3 = (Map.Entry) arrayList.get(i12);
                if (list.contains(((TimeLabelBean) entry3.getValue()).a())) {
                    float floatValue = ((Float) entry3.getKey()).floatValue();
                    String a10 = ((TimeLabelBean) entry3.getValue()).a();
                    this.f10371o.getTextBounds(a10, 0, a10.length(), new Rect());
                    c10 = 0;
                    float max2 = Math.max(floatValue, this.f10348b0 + (r15.width() / 2.0f));
                    int i13 = i12 + integer2;
                    if (i13 < arrayList.size() && Math.abs(max2 - ((Float) ((Map.Entry) arrayList.get(i13)).getKey()).floatValue()) <= r15.width() + e(5.0f)) {
                        LocalLog.a("UsageGraph", "not show time label: " + a10 + " key=" + max2 + " nextKey=" + ((Map.Entry) arrayList.get(i12 + 1)).getKey());
                    } else {
                        if (c13 != ((TimeLabelBean) entry3.getValue()).b() && !this.S) {
                            if (((TimeLabelBean) entry3.getValue()).b()) {
                                str = this.f10353f;
                                c11 = 1;
                            } else {
                                str = this.f10355g;
                                c11 = 0;
                            }
                            canvas.drawText(str, max2, this.f10382t0 + height, this.f10371o);
                            c13 = c11;
                        }
                        canvas.drawText(((TimeLabelBean) entry3.getValue()).a(), max2, height, this.f10371o);
                    }
                } else {
                    c10 = c12;
                }
                i12++;
                c12 = c10;
            }
        }
    }

    private void i(Canvas canvas) {
        LocalLog.a("UsageGraph", "drawYLabelLine");
        this.f10385v.reset();
        int integer = getResources().getInteger(R.integer.usage_graph_y_label_line_intervals);
        int i10 = 0;
        while (true) {
            int[] iArr = this.C;
            if (i10 >= iArr.length) {
                return;
            }
            float f10 = iArr[i10];
            this.f10385v.moveTo(this.f10348b0, m(f10));
            this.f10385v.lineTo(this.f10348b0 + getCurveWidth(), m(f10));
            canvas.drawPath(this.f10385v, this.f10367m);
            this.f10385v.reset();
            i10 += integer;
        }
    }

    private void j(Canvas canvas) {
        LocalLog.a("UsageGraph", "drawYLabelText");
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        int i10 = 0;
        while (true) {
            int[] iArr = this.C;
            if (i10 >= iArr.length) {
                return;
            }
            float f10 = iArr[i10];
            String format = percentInstance.format(f10 / 100.0f);
            this.f10369n.getTextBounds(format, 0, format.length(), new Rect());
            canvas.drawText(format, this.f10348b0 + getCurveWidth() + this.f10352e0, m(f10) + (r5.height() / 2.0f), this.f10369n);
            i10 += 2;
        }
    }

    private float k(float f10) {
        if (f10 <= -1.0f) {
            return (this.f10348b0 + getCurveWidth()) - this.f10390x0;
        }
        if (Math.min(this.f10387w.size(), 49) <= 1) {
            return this.f10348b0;
        }
        float f11 = this.f10348b0;
        float curveWidth = getCurveWidth();
        float f12 = this.f10390x0;
        return f11 + (((curveWidth - (2.0f * f12)) * f10) / (r0 - 1)) + f12;
    }

    private float l(float f10) {
        LocalLog.a("UsageGraph", "x:" + f10);
        if (Math.min(this.f10387w.size(), 49) != 1 && f10 > -1.0f) {
            float f11 = this.f10348b0;
            float curveWidth = getCurveWidth();
            float f12 = this.f10390x0;
            return f11 + (((curveWidth - (2.0f * f12)) * f10) / (r0 - 1)) + f12;
        }
        return (this.f10348b0 + getCurveWidth()) - this.f10390x0;
    }

    private float m(float f10) {
        LocalLog.a("UsageGraph", "y:" + f10);
        float f11 = this.W;
        float f12 = this.f10354f0;
        return (((f11 - f12) - this.f10358h0) * (1.0f - (f10 / 100.0f))) + f12;
    }

    private float n(final float f10, final boolean z10) {
        final float[] fArr = {0.0f};
        final float[] fArr2 = {Float.MAX_VALUE};
        this.f10391y.forEach(new BiConsumer() { // from class: p8.e
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                UsageGraph.q(z10, f10, fArr2, fArr, (Float) obj, (String) obj2);
            }
        });
        if (fArr[0] == 0.0f) {
            fArr[0] = f10 > l(-1.0f) ? l(-1.0f) : l(0.0f);
        }
        LocalLog.a("UsageGraph", "getUsableX xCoordinate:" + f10 + " isLeft:" + z10 + " result:" + fArr[0]);
        return fArr[0];
    }

    private void p() {
        BatteryPowerHelper.a(((View) this).mContext, this.f10387w);
        LocalLog.a("UsageGraph", "list size:" + this.f10387w.size());
        final int[] iArr = {0};
        this.f10387w.forEach(new BiConsumer() { // from class: p8.d
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                UsageGraph.this.r(iArr, (String) obj, (StatusBean) obj2);
            }
        });
        final ArrayList<TimeLabelBean> timeLabelListForSuperscript = getTimeLabelListForSuperscript();
        iArr[0] = 0;
        LocalLog.a("UsageGraph", "initPointsMap timeLabelList.size():" + timeLabelListForSuperscript.size() + " mXCoordinate2DateMap.size():" + this.f10391y.size());
        this.f10391y.forEach(new BiConsumer() { // from class: p8.c
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                UsageGraph.this.s(timeLabelListForSuperscript, iArr, (Float) obj, (String) obj2);
            }
        });
        IPowerRankingUpdate iPowerRankingUpdate = this.E0;
        if (iPowerRankingUpdate != null) {
            iPowerRankingUpdate.O(BatteryPowerHelper.d(true, w(l(0.0f))), BatteryPowerHelper.d(false, w(l(this.f10389x.size() - 1))));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void q(boolean z10, float f10, float[] fArr, float[] fArr2, Float f11, String str) {
        if (z10) {
            if (f10 - f11.floatValue() < 0.0f || f10 - f11.floatValue() >= fArr[0]) {
                return;
            }
            fArr[0] = f10 - f11.floatValue();
            fArr2[0] = f11.floatValue();
            return;
        }
        if (f10 - f11.floatValue() >= 0.0f || Math.abs(f10 - f11.floatValue()) >= fArr[0]) {
            return;
        }
        fArr[0] = Math.abs(f10 - f11.floatValue());
        fArr2[0] = f11.floatValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r(int[] iArr, String str, StatusBean statusBean) {
        this.f10389x.put(Float.valueOf(l(iArr[0])), new StatusBean(statusBean.a(), statusBean.b(), m(statusBean.a())));
        LocalLog.a("UsageGraph", "initPointsMap date:" + str);
        if (iArr[0] == 0 || iArr[0] == this.f10387w.size() - 1 || '0' == str.charAt(14)) {
            this.f10391y.put(Float.valueOf(l(iArr[0])), str);
        }
        iArr[0] = iArr[0] + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void s(ArrayList arrayList, int[] iArr, Float f10, String str) {
        LocalLog.a("UsageGraph", "init mXCoordinate2TimeMap k:" + f10 + " v:" + ((TimeLabelBean) arrayList.get(Math.max((arrayList.size() - this.f10391y.size()) + iArr[0], 0))).a());
        this.f10393z.put(f10, (TimeLabelBean) arrayList.get(Math.max((arrayList.size() - this.f10391y.size()) + iArr[0], 0)));
        iArr[0] = iArr[0] + 1;
    }

    private void t() {
        this.T = false;
        this.I = l(0.0f);
        this.J = l(this.f10387w.size() - 1);
    }

    private void u(int i10, float f10) {
        int i11 = i10 & 16777215;
        this.f10363k.setShader(new LinearGradient(f10, m(100.0f), f10, m(0.0f), new int[]{1291845632 | i11, i11 | 167772160}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
    }

    private void v() {
        boolean z10;
        Range range = new Range(Float.valueOf(Math.min(this.I, this.J)), Float.valueOf(Math.max(this.I, this.J)));
        Iterator<Range<Float>> it = this.A.values().iterator();
        while (true) {
            if (!it.hasNext()) {
                z10 = false;
                break;
            } else if (range.contains(it.next())) {
                z10 = true;
                break;
            }
        }
        UploadDataUtil.S0(((View) this).mContext).n0(1, z10);
    }

    private long w(float f10) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(this.f10391y.get(Float.valueOf(f10))).getTime();
        } catch (Exception e10) {
            e10.printStackTrace();
            return 0L;
        }
    }

    public void o(List<String> list) {
        Locale locale = ((View) this).mContext.getResources().getConfiguration().getLocales().get(0);
        DateTimeFormatterBuilder appendPattern = new DateTimeFormatterBuilder().appendPattern("HH:mm");
        Locale locale2 = Locale.US;
        DateTimeFormatter withDecimalStyle = appendPattern.toFormatter(locale2).withDecimalStyle(DecimalStyle.of(locale2));
        DateTimeFormatter withDecimalStyle2 = appendPattern.toFormatter(locale).withDecimalStyle(DecimalStyle.of(locale));
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            try {
                LocalTime parse = LocalTime.parse(str, withDecimalStyle);
                if (parse != null) {
                    LocalLog.a("UsageGraph", "s:" + str + " parse:" + parse + " transform:" + parse.format(withDecimalStyle2));
                    arrayList.add(parse.format(withDecimalStyle2));
                }
            } catch (DateTimeParseException e10) {
                LocalLog.b("UsageGraph", str + " parse fail:" + e10);
            }
        }
        list.addAll(arrayList);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        LocalLog.a("UsageGraph", "onDraw");
        super.onDraw(canvas);
        if (!this.S && this.Q) {
            LocalLog.a("UsageGraph", "change height");
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.usage_graph_12_hour_format_height);
            setLayoutParams(layoutParams);
        }
        this.W = getMeasuredHeight();
        this.f10347a0 = getRight() - getLeft();
        if (this.R) {
            d();
            p();
            t();
        }
        this.R = false;
        this.Q = false;
        i(canvas);
        j(canvas);
        h(canvas);
        this.H0 = canvas.saveLayer(0.0f, 0.0f, canvas.getWidth(), canvas.getHeight(), null);
        f(canvas);
        g(canvas);
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        LocalLog.a("UsageGraph", "onMeasure");
        super.onMeasure(i10, i11);
        this.R = true;
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z10;
        LocalLog.a("UsageGraph", "onTouchEvent:" + motionEvent.getAction());
        if (motionEvent.getY() <= m(100.0f) - this.f10356g0) {
            return false;
        }
        if (!this.F0) {
            return true;
        }
        int action = motionEvent.getAction();
        if (this.f10389x.size() < 2) {
            return true;
        }
        if (motionEvent.getY() > m(100.0f) && motionEvent.getY() < m(0.0f) && motionEvent.getX() <= l(-1.0f) && motionEvent.getX() > l(0.0f)) {
            this.T = true;
            if (action == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
                this.H = motionEvent.getX();
                this.K = n(motionEvent.getX(), true);
                this.L = n(motionEvent.getX(), false);
                this.O = true;
                float f10 = this.K;
                this.I = f10;
                this.U = w(f10);
                this.J = n(motionEvent.getX(), false);
                this.f10392y0 = Math.min(this.f10389x.get(Float.valueOf(this.K)).c(), this.f10389x.get(Float.valueOf(this.L)).c());
                invalidate();
                this.D0 = true;
            } else if (action == 1) {
                long j10 = this.U;
                if (j10 != 0) {
                    this.E0.B(BatteryPowerHelper.d(true, j10), BatteryPowerHelper.d(false, this.V), false);
                }
                this.P = -1.0f;
                getParent().requestDisallowInterceptTouchEvent(false);
                v();
            } else if (action == 2 && motionEvent.getX() <= l(-1.0f)) {
                boolean z11 = motionEvent.getX() > this.H;
                this.O = z11;
                this.I = z11 ? this.K : this.L;
                float n10 = n(motionEvent.getX(), !this.O);
                this.J = n10;
                float f11 = this.P;
                if (f11 == -1.0f || Math.abs(n10 - f11) >= l(1.0f) - l(0.0f)) {
                    LocalLog.a("UsageGraph", "invalidate");
                    LocalLog.a("UsageGraph", "mFilledEndX: " + this.J + " getmYLabel:" + this.f10389x.get(Float.valueOf(this.J)).c());
                    this.f10392y0 = Math.min(this.f10392y0, this.f10389x.get(Float.valueOf(this.J)).c());
                    invalidate();
                    this.P = this.J + (this.O ? -0.1f : 0.1f);
                }
            }
            return true;
        }
        if (motionEvent.getY() <= m(100.0f) || motionEvent.getY() >= m(0.0f)) {
            t();
            z10 = true;
        } else {
            z10 = false;
        }
        if (this.D0 && 1 == action) {
            getParent().requestDisallowInterceptTouchEvent(true);
            invalidate();
            if (z10) {
                this.E0.B(BatteryPowerHelper.d(true, w(l(0.0f))), BatteryPowerHelper.d(false, w(l(this.f10389x.size() - 1))), false);
                this.D0 = false;
            } else {
                this.E0.B(BatteryPowerHelper.d(true, this.U), BatteryPowerHelper.d(false, this.V), false);
                v();
            }
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }

    public void setPowerRankingUpdate(IPowerRankingUpdate iPowerRankingUpdate) {
        this.E0 = iPowerRankingUpdate;
    }

    public void setTouchEnable(boolean z10) {
        this.F0 = z10;
    }
}
