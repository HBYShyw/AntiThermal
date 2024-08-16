package h3;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/* compiled from: FollowHandManager.java */
/* renamed from: h3.a, reason: use source file name */
/* loaded from: classes.dex */
public class FollowHandManager {

    /* renamed from: a, reason: collision with root package name */
    private static Rect f11987a;

    /* renamed from: b, reason: collision with root package name */
    private static Rect f11988b;

    /* renamed from: f, reason: collision with root package name */
    private static Rect f11992f;

    /* renamed from: i, reason: collision with root package name */
    private static int f11995i;

    /* renamed from: j, reason: collision with root package name */
    private static int f11996j;

    /* renamed from: c, reason: collision with root package name */
    private static int[] f11989c = new int[2];

    /* renamed from: d, reason: collision with root package name */
    private static int[] f11990d = new int[4];

    /* renamed from: e, reason: collision with root package name */
    private static Point f11991e = new Point();

    /* renamed from: g, reason: collision with root package name */
    private static Rect f11993g = new Rect();

    /* renamed from: h, reason: collision with root package name */
    private static Rect f11994h = new Rect();

    public static Point a(Context context, int i10, int i11, boolean z10) {
        Point point = new Point();
        int i12 = f11991e.x - (i10 / 2);
        int i13 = l() ? f11991e.y : f11988b.bottom;
        int i14 = l() ? f11991e.y : f11988b.top;
        int c10 = c() - i13;
        Rect rect = f11994h;
        int i15 = rect.top;
        int i16 = rect.bottom;
        if (c10 <= i11 + i15 + i16) {
            i13 = (i14 - i11) - i16;
        } else if (i15 + i13 + i11 < c()) {
            i13 += f11994h.top;
        }
        int max = Math.max(d() + f11994h.left, Math.min(i12, (e() - f11994h.right) - i10));
        if (z10 && m(context)) {
            int[] iArr = f11989c;
            if (iArr[0] > 0) {
                max += iArr[0];
            }
        }
        point.set(max, Math.max(f() + f11994h.top, i13));
        return point;
    }

    public static Rect b() {
        return f11988b;
    }

    public static int c() {
        int i10;
        Rect rect = f11992f;
        if (rect != null) {
            i10 = rect.bottom;
        } else {
            i10 = f11987a.bottom;
        }
        return i10 - f11993g.bottom;
    }

    public static int d() {
        int i10;
        Rect rect = f11992f;
        if (rect != null) {
            i10 = rect.left;
        } else {
            i10 = f11987a.left;
        }
        return i10 + f11993g.left;
    }

    public static int e() {
        int i10;
        Rect rect = f11992f;
        if (rect != null) {
            i10 = rect.right;
        } else {
            i10 = f11987a.right;
        }
        return i10 - f11993g.right;
    }

    public static int f() {
        int i10;
        Rect rect = f11992f;
        if (rect != null) {
            i10 = rect.top;
        } else {
            i10 = f11987a.top;
        }
        return i10 + f11993g.top;
    }

    public static int g() {
        if (l()) {
            int i10 = f11988b.left;
            int i11 = f11996j;
            return i11 < 0 ? i10 + i11 : i10;
        }
        return f11988b.centerX();
    }

    public static int h() {
        if (l()) {
            int i10 = f11988b.top;
            int i11 = f11995i;
            return i11 < 0 ? i10 + i11 : i10;
        }
        return f11988b.centerY();
    }

    public static Rect i() {
        return f11987a;
    }

    public static Rect j() {
        return f11994h;
    }

    public static int[] k() {
        return f11989c;
    }

    public static boolean l() {
        int[] iArr = f11990d;
        return (iArr[0] == 0 && iArr[1] == 0 && iArr[2] == 0 && iArr[3] == 0) ? false : true;
    }

    public static boolean m(Context context) {
        double d10 = context.getResources().getConfiguration().screenWidthDp;
        double e10 = UIUtil.e(context) / context.getResources().getDisplayMetrics().density;
        return d10 == Math.floor(e10) || d10 == Math.ceil(e10);
    }

    public static void n(View view) {
        o(view, 0, 0);
    }

    public static void o(View view, int i10, int i11) {
        p();
        if (i10 != 0 || i11 != 0) {
            s(-i10, -i11, i10 - view.getWidth(), i11 - view.getHeight());
        }
        int[] iArr = new int[2];
        f11987a = new Rect();
        f11988b = new Rect();
        view.getWindowVisibleDisplayFrame(f11987a);
        view.getGlobalVisibleRect(f11988b);
        Rect rect = f11988b;
        int i12 = rect.left;
        int[] iArr2 = f11990d;
        rect.left = i12 - iArr2[0];
        rect.top -= iArr2[1];
        rect.right += iArr2[2];
        rect.bottom += iArr2[3];
        Rect rect2 = new Rect();
        view.getRootView().getGlobalVisibleRect(rect2);
        view.getRootView().getLocationOnScreen(iArr);
        rect2.offset(iArr[0], iArr[1]);
        Rect rect3 = f11987a;
        rect3.left = Math.max(rect3.left, rect2.left);
        Rect rect4 = f11987a;
        rect4.top = Math.max(rect4.top, rect2.top);
        Rect rect5 = f11987a;
        rect5.right = Math.min(rect5.right, rect2.right);
        Rect rect6 = f11987a;
        rect6.bottom = Math.min(rect6.bottom, rect2.bottom);
        view.getRootView().getLocationOnScreen(iArr);
        int i13 = iArr[0];
        int i14 = iArr[1];
        view.getRootView().getLocationInWindow(iArr);
        int i15 = iArr[0];
        int i16 = iArr[1];
        int[] iArr3 = f11989c;
        iArr3[0] = i13 - i15;
        iArr3[1] = i14 - i16;
        f11987a.offset(-iArr3[0], -iArr3[1]);
        f11995i = view.getTop();
        f11996j = view.getLeft();
        f11991e.x = g();
        f11991e.y = h();
    }

    private static void p() {
        s(0, 0, 0, 0);
        q(null);
        f11993g.set(0, 0, 0, 0);
        f11994h.set(0, 0, 0, 0);
    }

    public static void q(Rect rect) {
        f11992f = rect;
    }

    public static void r(Rect rect) {
        f11994h = rect;
    }

    public static void s(int i10, int i11, int i12, int i13) {
        int[] iArr = f11990d;
        iArr[0] = i10;
        iArr[1] = i11;
        iArr[2] = i12;
        iArr[3] = i13;
    }
}
