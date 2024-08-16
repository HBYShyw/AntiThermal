package x1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.content.ContextCompat;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$id;
import com.support.appcompat.R$style;
import h3.FollowHandManager;
import h3.UIUtil;

/* compiled from: COUIBottomAlertDialogAdjustUtil.java */
/* renamed from: x1.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUIBottomAlertDialogAdjustUtil {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomAlertDialogAdjustUtil.java */
    /* renamed from: x1.c$a */
    /* loaded from: classes.dex */
    public class a implements c {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Window f19492a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f19493b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ Point f19494c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ Point f19495d;

        /* compiled from: COUIBottomAlertDialogAdjustUtil.java */
        /* renamed from: x1.c$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class C0116a implements COUIAlertDialogMaxLinearLayout.a {
            C0116a() {
            }

            @Override // com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout.a
            public void a(int i10, int i11, int i12, int i13) {
                a aVar = a.this;
                COUIBottomAlertDialogAdjustUtil.m(aVar.f19492a, aVar.f19493b, aVar.f19494c, aVar.f19495d);
                a.this.f19492a.getDecorView().setVisibility(0);
                COUIBottomAlertDialogAdjustUtil.j(a.this.f19492a, null);
            }
        }

        a(Window window, View view, Point point, Point point2) {
            this.f19492a = window;
            this.f19493b = view;
            this.f19494c = point;
            this.f19495d = point2;
        }

        @Override // x1.COUIBottomAlertDialogAdjustUtil.c
        public void a() {
            COUIBottomAlertDialogAdjustUtil.l(this.f19492a, true);
            COUIBottomAlertDialogAdjustUtil.m(this.f19492a, this.f19493b, this.f19494c, this.f19495d);
            COUIBottomAlertDialogAdjustUtil.j(this.f19492a, new C0116a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIBottomAlertDialogAdjustUtil.java */
    /* renamed from: x1.c$b */
    /* loaded from: classes.dex */
    public class b implements ViewTreeObserver.OnGlobalLayoutListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Window f19497e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ c f19498f;

        b(Window window, c cVar) {
            this.f19497e = window;
            this.f19498f = cVar;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            this.f19497e.getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            this.f19498f.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: COUIBottomAlertDialogAdjustUtil.java */
    /* renamed from: x1.c$c */
    /* loaded from: classes.dex */
    public interface c {
        void a();
    }

    public static void d(Window window, View view, Point point, Point point2) {
        k(window, -2);
        window.clearFlags(2);
        window.setGravity(51);
        window.setWindowAnimations(R$style.Animation_COUI_PopupListWindow);
        i(window, new a(window, view, point, point2));
    }

    private static int e(Context context, float f10) {
        return Math.round(TypedValue.applyDimension(1, f10, context.getResources().getDisplayMetrics()));
    }

    private static int f(Window window, int i10, int i11) {
        Resources resources = window.getDecorView().getResources();
        return (resources == null || i10 == 0) ? i11 : resources.getDimensionPixelOffset(i10);
    }

    private static Drawable g(Window window, int i10) {
        Context context = window.getDecorView().getContext();
        if (context == null || i10 == 0) {
            return null;
        }
        return context.getDrawable(i10);
    }

    private static void h(Window window, int i10, int i11) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = i10;
        attributes.y = i11;
        window.setAttributes(attributes);
    }

    private static void i(Window window, c cVar) {
        if (cVar == null) {
            return;
        }
        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new b(window, cVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void j(Window window, COUIAlertDialogMaxLinearLayout.a aVar) {
        View findViewById = window.findViewById(R$id.parentPanel);
        if (findViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) findViewById).setOnSizeChangeListener(aVar);
        }
    }

    private static void k(Window window, int i10) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = i10;
        window.setAttributes(attributes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void l(Window window, boolean z10) {
        View findViewById = window.findViewById(R$id.parentPanel);
        if (findViewById instanceof COUIAlertDialogMaxLinearLayout) {
            if (z10) {
                ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
                layoutParams.width = f(window, R$dimen.coui_dialog_max_width_in_bottom_free, 0);
                int f10 = f(window, R$dimen.support_shadow_size_level_four, 0);
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(f10, f10, f10, f10 * 2);
                }
                findViewById.setLayoutParams(layoutParams);
                UIUtil.j(findViewById, f10, ContextCompat.c(window.getContext(), R$color.coui_dialog_follow_hand_spot_shadow_color));
            } else {
                ((COUIAlertDialogMaxLinearLayout) findViewById).setMaxWidth(f(window, R$dimen.coui_dialog_max_width, 0));
            }
            findViewById.setBackground(g(window, R$drawable.coui_alert_dialog_builder_background));
            findViewById.requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void m(Window window, View view, Point point, Point point2) {
        Point a10;
        if (view == null && point != null) {
            h(window, point.x, point.y);
            return;
        }
        if (point == null) {
            FollowHandManager.n(view);
            a10 = FollowHandManager.a(view.getContext(), window.getDecorView().getMeasuredWidth(), window.getDecorView().getMeasuredHeight(), true);
            if (a10.y < FollowHandManager.h()) {
                a10.y += e(view.getContext(), 8.0f);
            }
        } else {
            FollowHandManager.o(view, point.x, point.y);
            a10 = FollowHandManager.a(view.getContext(), window.getDecorView().getMeasuredWidth(), window.getDecorView().getMeasuredHeight(), true);
        }
        int i10 = a10.y - FollowHandManager.i().top;
        a10.y = i10;
        if (point2 != null) {
            a10.x += point2.x;
            a10.y = i10 + point2.y;
        }
        h(window, a10.x, a10.y);
    }
}
