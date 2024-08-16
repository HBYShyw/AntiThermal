package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.appcompat.R$dimen;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

/* compiled from: MenuPopupHelper.java */
/* renamed from: androidx.appcompat.view.menu.l, reason: use source file name */
/* loaded from: classes.dex */
public class MenuPopupHelper {

    /* renamed from: a, reason: collision with root package name */
    private final Context f780a;

    /* renamed from: b, reason: collision with root package name */
    private final MenuBuilder f781b;

    /* renamed from: c, reason: collision with root package name */
    private final boolean f782c;

    /* renamed from: d, reason: collision with root package name */
    private final int f783d;

    /* renamed from: e, reason: collision with root package name */
    private final int f784e;

    /* renamed from: f, reason: collision with root package name */
    private View f785f;

    /* renamed from: g, reason: collision with root package name */
    private int f786g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f787h;

    /* renamed from: i, reason: collision with root package name */
    private MenuPresenter.a f788i;

    /* renamed from: j, reason: collision with root package name */
    private MenuPopup f789j;

    /* renamed from: k, reason: collision with root package name */
    private PopupWindow.OnDismissListener f790k;

    /* renamed from: l, reason: collision with root package name */
    private final PopupWindow.OnDismissListener f791l;

    /* compiled from: MenuPopupHelper.java */
    /* renamed from: androidx.appcompat.view.menu.l$a */
    /* loaded from: classes.dex */
    class a implements PopupWindow.OnDismissListener {
        a() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            MenuPopupHelper.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MenuPopupHelper.java */
    /* renamed from: androidx.appcompat.view.menu.l$b */
    /* loaded from: classes.dex */
    public static class b {
        static void a(Display display, Point point) {
            display.getRealSize(point);
        }
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean z10, int i10) {
        this(context, menuBuilder, view, z10, i10, 0);
    }

    private MenuPopup a() {
        MenuPopup standardMenuPopup;
        Display defaultDisplay = ((WindowManager) this.f780a.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        b.a(defaultDisplay, point);
        if (Math.min(point.x, point.y) >= this.f780a.getResources().getDimensionPixelSize(R$dimen.abc_cascading_menus_min_smallest_width)) {
            standardMenuPopup = new CascadingMenuPopup(this.f780a, this.f785f, this.f783d, this.f784e, this.f782c);
        } else {
            standardMenuPopup = new StandardMenuPopup(this.f780a, this.f781b, this.f785f, this.f783d, this.f784e, this.f782c);
        }
        standardMenuPopup.c(this.f781b);
        standardMenuPopup.m(this.f791l);
        standardMenuPopup.g(this.f785f);
        standardMenuPopup.setCallback(this.f788i);
        standardMenuPopup.i(this.f787h);
        standardMenuPopup.k(this.f786g);
        return standardMenuPopup;
    }

    private void l(int i10, int i11, boolean z10, boolean z11) {
        MenuPopup c10 = c();
        c10.n(z11);
        if (z10) {
            if ((GravityCompat.b(this.f786g, ViewCompat.x(this.f785f)) & 7) == 5) {
                i10 -= this.f785f.getWidth();
            }
            c10.l(i10);
            c10.o(i11);
            int i12 = (int) ((this.f780a.getResources().getDisplayMetrics().density * 48.0f) / 2.0f);
            c10.h(new Rect(i10 - i12, i11 - i12, i10 + i12, i11 + i12));
        }
        c10.b();
    }

    public void b() {
        if (d()) {
            this.f789j.dismiss();
        }
    }

    public MenuPopup c() {
        if (this.f789j == null) {
            this.f789j = a();
        }
        return this.f789j;
    }

    public boolean d() {
        MenuPopup menuPopup = this.f789j;
        return menuPopup != null && menuPopup.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void e() {
        this.f789j = null;
        PopupWindow.OnDismissListener onDismissListener = this.f790k;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void f(View view) {
        this.f785f = view;
    }

    public void g(boolean z10) {
        this.f787h = z10;
        MenuPopup menuPopup = this.f789j;
        if (menuPopup != null) {
            menuPopup.i(z10);
        }
    }

    public void h(int i10) {
        this.f786g = i10;
    }

    public void i(PopupWindow.OnDismissListener onDismissListener) {
        this.f790k = onDismissListener;
    }

    public void j(MenuPresenter.a aVar) {
        this.f788i = aVar;
        MenuPopup menuPopup = this.f789j;
        if (menuPopup != null) {
            menuPopup.setCallback(aVar);
        }
    }

    public void k() {
        if (!m()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public boolean m() {
        if (d()) {
            return true;
        }
        if (this.f785f == null) {
            return false;
        }
        l(0, 0, false, false);
        return true;
    }

    public boolean n(int i10, int i11) {
        if (d()) {
            return true;
        }
        if (this.f785f == null) {
            return false;
        }
        l(i10, i11, true, true);
        return true;
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean z10, int i10, int i11) {
        this.f786g = 8388611;
        this.f791l = new a();
        this.f780a = context;
        this.f781b = menuBuilder;
        this.f785f = view;
        this.f782c = z10;
        this.f783d = i10;
        this.f784e = i11;
    }
}
