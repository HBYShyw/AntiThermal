package androidx.core.view;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;
import androidx.core.R$id;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import j.SimpleArrayMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: ViewCompat.java */
@SuppressLint({"PrivateConstructorForUtilityClass"})
/* renamed from: androidx.core.view.y, reason: use source file name */
/* loaded from: classes.dex */
public class ViewCompat {

    /* renamed from: a, reason: collision with root package name */
    private static final AtomicInteger f2417a = new AtomicInteger(1);

    /* renamed from: b, reason: collision with root package name */
    private static WeakHashMap<View, ViewPropertyAnimatorCompat> f2418b = null;

    /* renamed from: c, reason: collision with root package name */
    private static boolean f2419c = false;

    /* renamed from: d, reason: collision with root package name */
    private static final int[] f2420d = {R$id.accessibility_custom_action_0, R$id.accessibility_custom_action_1, R$id.accessibility_custom_action_2, R$id.accessibility_custom_action_3, R$id.accessibility_custom_action_4, R$id.accessibility_custom_action_5, R$id.accessibility_custom_action_6, R$id.accessibility_custom_action_7, R$id.accessibility_custom_action_8, R$id.accessibility_custom_action_9, R$id.accessibility_custom_action_10, R$id.accessibility_custom_action_11, R$id.accessibility_custom_action_12, R$id.accessibility_custom_action_13, R$id.accessibility_custom_action_14, R$id.accessibility_custom_action_15, R$id.accessibility_custom_action_16, R$id.accessibility_custom_action_17, R$id.accessibility_custom_action_18, R$id.accessibility_custom_action_19, R$id.accessibility_custom_action_20, R$id.accessibility_custom_action_21, R$id.accessibility_custom_action_22, R$id.accessibility_custom_action_23, R$id.accessibility_custom_action_24, R$id.accessibility_custom_action_25, R$id.accessibility_custom_action_26, R$id.accessibility_custom_action_27, R$id.accessibility_custom_action_28, R$id.accessibility_custom_action_29, R$id.accessibility_custom_action_30, R$id.accessibility_custom_action_31};

    /* renamed from: e, reason: collision with root package name */
    private static final OnReceiveContentViewBehavior f2421e = new OnReceiveContentViewBehavior() { // from class: androidx.core.view.x
    };

    /* renamed from: f, reason: collision with root package name */
    private static final e f2422f = new e();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$a */
    /* loaded from: classes.dex */
    public class a extends f<Boolean> {
        a(int i10, Class cls, int i11) {
            super(i10, cls, i11);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public Boolean d(View view) {
            return Boolean.valueOf(q.d(view));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: j, reason: merged with bridge method [inline-methods] */
        public void e(View view, Boolean bool) {
            q.i(view, bool.booleanValue());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public boolean h(Boolean bool, Boolean bool2) {
            return !a(bool, bool2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$b */
    /* loaded from: classes.dex */
    public class b extends f<CharSequence> {
        b(int i10, Class cls, int i11, int i12) {
            super(i10, cls, i11, i12);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public CharSequence d(View view) {
            return q.b(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: j, reason: merged with bridge method [inline-methods] */
        public void e(View view, CharSequence charSequence) {
            q.h(view, charSequence);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public boolean h(CharSequence charSequence, CharSequence charSequence2) {
            return !TextUtils.equals(charSequence, charSequence2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$c */
    /* loaded from: classes.dex */
    public class c extends f<CharSequence> {
        c(int i10, Class cls, int i11, int i12) {
            super(i10, cls, i11, i12);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public CharSequence d(View view) {
            return s.a(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: j, reason: merged with bridge method [inline-methods] */
        public void e(View view, CharSequence charSequence) {
            s.b(view, charSequence);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public boolean h(CharSequence charSequence, CharSequence charSequence2) {
            return !TextUtils.equals(charSequence, charSequence2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$d */
    /* loaded from: classes.dex */
    public class d extends f<Boolean> {
        d(int i10, Class cls, int i11) {
            super(i10, cls, i11);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: i, reason: merged with bridge method [inline-methods] */
        public Boolean d(View view) {
            return Boolean.valueOf(q.c(view));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: j, reason: merged with bridge method [inline-methods] */
        public void e(View view, Boolean bool) {
            q.g(view, bool.booleanValue());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // androidx.core.view.ViewCompat.f
        /* renamed from: k, reason: merged with bridge method [inline-methods] */
        public boolean h(Boolean bool, Boolean bool2) {
            return !a(bool, bool2);
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$e */
    /* loaded from: classes.dex */
    static class e implements ViewTreeObserver.OnGlobalLayoutListener, View.OnAttachStateChangeListener {

        /* renamed from: e, reason: collision with root package name */
        private final WeakHashMap<View, Boolean> f2423e = new WeakHashMap<>();

        e() {
        }

        private void b(View view) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        private void d(View view) {
            h.o(view.getViewTreeObserver(), this);
        }

        void a(View view) {
            this.f2423e.put(view, Boolean.valueOf(view.isShown() && view.getWindowVisibility() == 0));
            view.addOnAttachStateChangeListener(this);
            if (k.b(view)) {
                b(view);
            }
        }

        void c(View view) {
            this.f2423e.remove(view);
            view.removeOnAttachStateChangeListener(this);
            d(view);
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            b(view);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$f */
    /* loaded from: classes.dex */
    public static abstract class f<T> {

        /* renamed from: a, reason: collision with root package name */
        private final int f2424a;

        /* renamed from: b, reason: collision with root package name */
        private final Class<T> f2425b;

        /* renamed from: c, reason: collision with root package name */
        private final int f2426c;

        /* renamed from: d, reason: collision with root package name */
        private final int f2427d;

        f(int i10, Class<T> cls, int i11) {
            this(i10, cls, 0, i11);
        }

        private boolean b() {
            return true;
        }

        private boolean c() {
            return Build.VERSION.SDK_INT >= this.f2426c;
        }

        boolean a(Boolean bool, Boolean bool2) {
            return (bool != null && bool.booleanValue()) == (bool2 != null && bool2.booleanValue());
        }

        abstract T d(View view);

        abstract void e(View view, T t7);

        T f(View view) {
            if (c()) {
                return d(view);
            }
            if (!b()) {
                return null;
            }
            T t7 = (T) view.getTag(this.f2424a);
            if (this.f2425b.isInstance(t7)) {
                return t7;
            }
            return null;
        }

        void g(View view, T t7) {
            if (c()) {
                e(view, t7);
            } else if (b() && h(f(view), t7)) {
                ViewCompat.h(view);
                view.setTag(this.f2424a, t7);
                ViewCompat.U(view, this.f2427d);
            }
        }

        abstract boolean h(T t7, T t10);

        f(int i10, Class<T> cls, int i11, int i12) {
            this.f2424a = i10;
            this.f2425b = cls;
            this.f2427d = i11;
            this.f2426c = i12;
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$g */
    /* loaded from: classes.dex */
    static class g {
        static boolean a(View view) {
            return view.hasOnClickListeners();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$h */
    /* loaded from: classes.dex */
    public static class h {
        static AccessibilityNodeProvider a(View view) {
            return view.getAccessibilityNodeProvider();
        }

        static boolean b(View view) {
            return view.getFitsSystemWindows();
        }

        static int c(View view) {
            return view.getImportantForAccessibility();
        }

        static int d(View view) {
            return view.getMinimumHeight();
        }

        static int e(View view) {
            return view.getMinimumWidth();
        }

        static ViewParent f(View view) {
            return view.getParentForAccessibility();
        }

        static int g(View view) {
            return view.getWindowSystemUiVisibility();
        }

        static boolean h(View view) {
            return view.hasOverlappingRendering();
        }

        static boolean i(View view) {
            return view.hasTransientState();
        }

        static boolean j(View view, int i10, Bundle bundle) {
            return view.performAccessibilityAction(i10, bundle);
        }

        static void k(View view) {
            view.postInvalidateOnAnimation();
        }

        static void l(View view, int i10, int i11, int i12, int i13) {
            view.postInvalidateOnAnimation(i10, i11, i12, i13);
        }

        static void m(View view, Runnable runnable) {
            view.postOnAnimation(runnable);
        }

        static void n(View view, Runnable runnable, long j10) {
            view.postOnAnimationDelayed(runnable, j10);
        }

        static void o(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        static void p(View view) {
            view.requestFitSystemWindows();
        }

        static void q(View view, Drawable drawable) {
            view.setBackground(drawable);
        }

        static void r(View view, boolean z10) {
            view.setHasTransientState(z10);
        }

        static void s(View view, int i10) {
            view.setImportantForAccessibility(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$i */
    /* loaded from: classes.dex */
    public static class i {
        static int a() {
            return View.generateViewId();
        }

        static Display b(View view) {
            return view.getDisplay();
        }

        static int c(View view) {
            return view.getLabelFor();
        }

        static int d(View view) {
            return view.getLayoutDirection();
        }

        static int e(View view) {
            return view.getPaddingEnd();
        }

        static int f(View view) {
            return view.getPaddingStart();
        }

        static boolean g(View view) {
            return view.isPaddingRelative();
        }

        static void h(View view, int i10) {
            view.setLabelFor(i10);
        }

        static void i(View view, Paint paint) {
            view.setLayerPaint(paint);
        }

        static void j(View view, int i10) {
            view.setLayoutDirection(i10);
        }

        static void k(View view, int i10, int i11, int i12, int i13) {
            view.setPaddingRelative(i10, i11, i12, i13);
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$j */
    /* loaded from: classes.dex */
    static class j {
        static Rect a(View view) {
            return view.getClipBounds();
        }

        static boolean b(View view) {
            return view.isInLayout();
        }

        static void c(View view, Rect rect) {
            view.setClipBounds(rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$k */
    /* loaded from: classes.dex */
    public static class k {
        static int a(View view) {
            return view.getAccessibilityLiveRegion();
        }

        static boolean b(View view) {
            return view.isAttachedToWindow();
        }

        static boolean c(View view) {
            return view.isLaidOut();
        }

        static boolean d(View view) {
            return view.isLayoutDirectionResolved();
        }

        static void e(ViewParent viewParent, View view, View view2, int i10) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, i10);
        }

        static void f(View view, int i10) {
            view.setAccessibilityLiveRegion(i10);
        }

        static void g(AccessibilityEvent accessibilityEvent, int i10) {
            accessibilityEvent.setContentChangeTypes(i10);
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$l */
    /* loaded from: classes.dex */
    static class l {
        static WindowInsets a(View view, WindowInsets windowInsets) {
            return view.dispatchApplyWindowInsets(windowInsets);
        }

        static WindowInsets b(View view, WindowInsets windowInsets) {
            return view.onApplyWindowInsets(windowInsets);
        }

        static void c(View view) {
            view.requestApplyInsets();
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$m */
    /* loaded from: classes.dex */
    private static class m {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: ViewCompat.java */
        /* renamed from: androidx.core.view.y$m$a */
        /* loaded from: classes.dex */
        public class a implements View.OnApplyWindowInsetsListener {

            /* renamed from: a, reason: collision with root package name */
            WindowInsetsCompat f2428a = null;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ View f2429b;

            /* renamed from: c, reason: collision with root package name */
            final /* synthetic */ androidx.core.view.t f2430c;

            a(View view, androidx.core.view.t tVar) {
                this.f2429b = view;
                this.f2430c = tVar;
            }

            @Override // android.view.View.OnApplyWindowInsetsListener
            public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                WindowInsetsCompat w10 = WindowInsetsCompat.w(windowInsets, view);
                this.f2428a = w10;
                return this.f2430c.onApplyWindowInsets(view, w10).u();
            }
        }

        static void a(WindowInsets windowInsets, View view) {
            View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = (View.OnApplyWindowInsetsListener) view.getTag(R$id.tag_window_insets_animation_callback);
            if (onApplyWindowInsetsListener != null) {
                onApplyWindowInsetsListener.onApplyWindowInsets(view, windowInsets);
            }
        }

        static WindowInsetsCompat b(View view, WindowInsetsCompat windowInsetsCompat, Rect rect) {
            WindowInsets u7 = windowInsetsCompat.u();
            if (u7 != null) {
                return WindowInsetsCompat.w(view.computeSystemWindowInsets(u7, rect), view);
            }
            rect.setEmpty();
            return windowInsetsCompat;
        }

        static boolean c(View view, float f10, float f11, boolean z10) {
            return view.dispatchNestedFling(f10, f11, z10);
        }

        static boolean d(View view, float f10, float f11) {
            return view.dispatchNestedPreFling(f10, f11);
        }

        static boolean e(View view, int i10, int i11, int[] iArr, int[] iArr2) {
            return view.dispatchNestedPreScroll(i10, i11, iArr, iArr2);
        }

        static boolean f(View view, int i10, int i11, int i12, int i13, int[] iArr) {
            return view.dispatchNestedScroll(i10, i11, i12, i13, iArr);
        }

        static ColorStateList g(View view) {
            return view.getBackgroundTintList();
        }

        static PorterDuff.Mode h(View view) {
            return view.getBackgroundTintMode();
        }

        static float i(View view) {
            return view.getElevation();
        }

        public static WindowInsetsCompat j(View view) {
            return WindowInsetsCompat.a.a(view);
        }

        static String k(View view) {
            return view.getTransitionName();
        }

        static float l(View view) {
            return view.getTranslationZ();
        }

        static float m(View view) {
            return view.getZ();
        }

        static boolean n(View view) {
            return view.hasNestedScrollingParent();
        }

        static boolean o(View view) {
            return view.isImportantForAccessibility();
        }

        static boolean p(View view) {
            return view.isNestedScrollingEnabled();
        }

        static void q(View view, ColorStateList colorStateList) {
            view.setBackgroundTintList(colorStateList);
        }

        static void r(View view, PorterDuff.Mode mode) {
            view.setBackgroundTintMode(mode);
        }

        static void s(View view, float f10) {
            view.setElevation(f10);
        }

        static void t(View view, boolean z10) {
            view.setNestedScrollingEnabled(z10);
        }

        static void u(View view, androidx.core.view.t tVar) {
            if (tVar == null) {
                view.setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) view.getTag(R$id.tag_window_insets_animation_callback));
            } else {
                view.setOnApplyWindowInsetsListener(new a(view, tVar));
            }
        }

        static void v(View view, String str) {
            view.setTransitionName(str);
        }

        static void w(View view, float f10) {
            view.setTranslationZ(f10);
        }

        static void x(View view, float f10) {
            view.setZ(f10);
        }

        static boolean y(View view, int i10) {
            return view.startNestedScroll(i10);
        }

        static void z(View view) {
            view.stopNestedScroll();
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$n */
    /* loaded from: classes.dex */
    private static class n {
        public static WindowInsetsCompat a(View view) {
            WindowInsets rootWindowInsets = view.getRootWindowInsets();
            if (rootWindowInsets == null) {
                return null;
            }
            WindowInsetsCompat v7 = WindowInsetsCompat.v(rootWindowInsets);
            v7.t(v7);
            v7.d(view.getRootView());
            return v7;
        }

        static int b(View view) {
            return view.getScrollIndicators();
        }

        static void c(View view, int i10) {
            view.setScrollIndicators(i10);
        }

        static void d(View view, int i10, int i11) {
            view.setScrollIndicators(i10, i11);
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$o */
    /* loaded from: classes.dex */
    static class o {
        static void a(View view) {
            view.cancelDragAndDrop();
        }

        static void b(View view) {
            view.dispatchFinishTemporaryDetach();
        }

        static void c(View view) {
            view.dispatchStartTemporaryDetach();
        }

        static void d(View view, PointerIcon pointerIcon) {
            view.setPointerIcon(pointerIcon);
        }

        static boolean e(View view, ClipData clipData, View.DragShadowBuilder dragShadowBuilder, Object obj, int i10) {
            return view.startDragAndDrop(clipData, dragShadowBuilder, obj, i10);
        }

        static void f(View view, View.DragShadowBuilder dragShadowBuilder) {
            view.updateDragShadow(dragShadowBuilder);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$p */
    /* loaded from: classes.dex */
    public static class p {
        static void a(View view, Collection<View> collection, int i10) {
            view.addKeyboardNavigationClusters(collection, i10);
        }

        static int b(View view) {
            return view.getImportantForAutofill();
        }

        static int c(View view) {
            return view.getNextClusterForwardId();
        }

        static boolean d(View view) {
            return view.hasExplicitFocusable();
        }

        static boolean e(View view) {
            return view.isFocusedByDefault();
        }

        static boolean f(View view) {
            return view.isImportantForAutofill();
        }

        static boolean g(View view) {
            return view.isKeyboardNavigationCluster();
        }

        static View h(View view, View view2, int i10) {
            return view.keyboardNavigationClusterSearch(view2, i10);
        }

        static boolean i(View view) {
            return view.restoreDefaultFocus();
        }

        static void j(View view, String... strArr) {
            view.setAutofillHints(strArr);
        }

        static void k(View view, boolean z10) {
            view.setFocusedByDefault(z10);
        }

        static void l(View view, int i10) {
            view.setImportantForAutofill(i10);
        }

        static void m(View view, boolean z10) {
            view.setKeyboardNavigationCluster(z10);
        }

        static void n(View view, int i10) {
            view.setNextClusterForwardId(i10);
        }

        static void o(View view, CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$q */
    /* loaded from: classes.dex */
    public static class q {
        static void a(View view, final t tVar) {
            int i10 = R$id.tag_unhandled_key_listeners;
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) view.getTag(i10);
            if (simpleArrayMap == null) {
                simpleArrayMap = new SimpleArrayMap();
                view.setTag(i10, simpleArrayMap);
            }
            Objects.requireNonNull(tVar);
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener = new View.OnUnhandledKeyEventListener() { // from class: androidx.core.view.z
                @Override // android.view.View.OnUnhandledKeyEventListener
                public final boolean onUnhandledKeyEvent(View view2, KeyEvent keyEvent) {
                    return ViewCompat.t.this.onUnhandledKeyEvent(view2, keyEvent);
                }
            };
            simpleArrayMap.put(tVar, onUnhandledKeyEventListener);
            view.addOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
        }

        static CharSequence b(View view) {
            return view.getAccessibilityPaneTitle();
        }

        static boolean c(View view) {
            return view.isAccessibilityHeading();
        }

        static boolean d(View view) {
            return view.isScreenReaderFocusable();
        }

        static void e(View view, t tVar) {
            View.OnUnhandledKeyEventListener onUnhandledKeyEventListener;
            SimpleArrayMap simpleArrayMap = (SimpleArrayMap) view.getTag(R$id.tag_unhandled_key_listeners);
            if (simpleArrayMap == null || (onUnhandledKeyEventListener = (View.OnUnhandledKeyEventListener) simpleArrayMap.get(tVar)) == null) {
                return;
            }
            view.removeOnUnhandledKeyEventListener(onUnhandledKeyEventListener);
        }

        static <T> T f(View view, int i10) {
            return (T) view.requireViewById(i10);
        }

        static void g(View view, boolean z10) {
            view.setAccessibilityHeading(z10);
        }

        static void h(View view, CharSequence charSequence) {
            view.setAccessibilityPaneTitle(charSequence);
        }

        static void i(View view, boolean z10) {
            view.setScreenReaderFocusable(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$r */
    /* loaded from: classes.dex */
    public static class r {
        static View.AccessibilityDelegate a(View view) {
            return view.getAccessibilityDelegate();
        }

        static List<Rect> b(View view) {
            return view.getSystemGestureExclusionRects();
        }

        static void c(View view, Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i10, int i11) {
            view.saveAttributeDataForStyleable(context, iArr, attributeSet, typedArray, i10, i11);
        }

        static void d(View view, List<Rect> list) {
            view.setSystemGestureExclusionRects(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$s */
    /* loaded from: classes.dex */
    public static class s {
        static CharSequence a(View view) {
            return view.getStateDescription();
        }

        static void b(View view, CharSequence charSequence) {
            view.setStateDescription(charSequence);
        }
    }

    /* compiled from: ViewCompat.java */
    /* renamed from: androidx.core.view.y$t */
    /* loaded from: classes.dex */
    public interface t {
        boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent);
    }

    public static int A(View view) {
        return h.e(view);
    }

    public static void A0(View view, int i10, int i11, int i12, int i13) {
        i.k(view, i10, i11, i12, i13);
    }

    public static int B(View view) {
        return i.e(view);
    }

    public static void B0(View view, PointerIconCompat pointerIconCompat) {
        o.d(view, (PointerIcon) (pointerIconCompat != null ? pointerIconCompat.a() : null));
    }

    public static int C(View view) {
        return i.f(view);
    }

    public static void C0(View view, int i10, int i11) {
        n.d(view, i10, i11);
    }

    public static ViewParent D(View view) {
        return h.f(view);
    }

    public static void D0(View view, CharSequence charSequence) {
        I0().g(view, charSequence);
    }

    public static WindowInsetsCompat E(View view) {
        return n.a(view);
    }

    public static void E0(View view, String str) {
        m.v(view, str);
    }

    public static CharSequence F(View view) {
        return I0().f(view);
    }

    public static void F0(View view, float f10) {
        m.w(view, f10);
    }

    public static String G(View view) {
        return m.k(view);
    }

    private static void G0(View view) {
        if (v(view) == 0) {
            w0(view, 1);
        }
        for (ViewParent parent = view.getParent(); parent instanceof View; parent = parent.getParent()) {
            if (v((View) parent) == 4) {
                w0(view, 2);
                return;
            }
        }
    }

    public static float H(View view) {
        return m.l(view);
    }

    public static void H0(View view, float f10) {
        m.x(view, f10);
    }

    @Deprecated
    public static int I(View view) {
        return h.g(view);
    }

    private static f<CharSequence> I0() {
        return new c(R$id.tag_state_description, CharSequence.class, 64, 30);
    }

    public static float J(View view) {
        return m.m(view);
    }

    public static void J0(View view) {
        m.z(view);
    }

    public static boolean K(View view) {
        return k(view) != null;
    }

    public static boolean L(View view) {
        return g.a(view);
    }

    public static boolean M(View view) {
        return h.h(view);
    }

    public static boolean N(View view) {
        return h.i(view);
    }

    public static boolean O(View view) {
        Boolean f10 = a().f(view);
        return f10 != null && f10.booleanValue();
    }

    public static boolean P(View view) {
        return k.b(view);
    }

    public static boolean Q(View view) {
        return k.c(view);
    }

    public static boolean R(View view) {
        return m.p(view);
    }

    public static boolean S(View view) {
        return i.g(view);
    }

    public static boolean T(View view) {
        Boolean f10 = k0().f(view);
        return f10 != null && f10.booleanValue();
    }

    static void U(View view, int i10) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled()) {
            boolean z10 = m(view) != null && view.isShown() && view.getWindowVisibility() == 0;
            if (l(view) != 0 || z10) {
                AccessibilityEvent obtain = AccessibilityEvent.obtain();
                obtain.setEventType(z10 ? 32 : 2048);
                k.g(obtain, i10);
                if (z10) {
                    obtain.getText().add(m(view));
                    G0(view);
                }
                view.sendAccessibilityEventUnchecked(obtain);
                return;
            }
            if (i10 == 32) {
                AccessibilityEvent obtain2 = AccessibilityEvent.obtain();
                view.onInitializeAccessibilityEvent(obtain2);
                obtain2.setEventType(32);
                k.g(obtain2, i10);
                obtain2.setSource(view);
                view.onPopulateAccessibilityEvent(obtain2);
                obtain2.getText().add(m(view));
                accessibilityManager.sendAccessibilityEvent(obtain2);
                return;
            }
            if (view.getParent() != null) {
                try {
                    k.e(view.getParent(), view, view, i10);
                } catch (AbstractMethodError e10) {
                    Log.e("ViewCompat", view.getParent().getClass().getSimpleName() + " does not fully implement ViewParent", e10);
                }
            }
        }
    }

    public static void V(View view, int i10) {
        view.offsetLeftAndRight(i10);
    }

    public static void W(View view, int i10) {
        view.offsetTopAndBottom(i10);
    }

    public static WindowInsetsCompat X(View view, WindowInsetsCompat windowInsetsCompat) {
        WindowInsets u7 = windowInsetsCompat.u();
        if (u7 != null) {
            WindowInsets b10 = l.b(view, u7);
            if (!b10.equals(u7)) {
                return WindowInsetsCompat.w(b10, view);
            }
        }
        return windowInsetsCompat;
    }

    public static void Y(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.B0());
    }

    private static f<CharSequence> Z() {
        return new b(R$id.tag_accessibility_pane_title, CharSequence.class, 8, 28);
    }

    private static f<Boolean> a() {
        return new d(R$id.tag_accessibility_heading, Boolean.class, 28);
    }

    public static boolean a0(View view, int i10, Bundle bundle) {
        return h.j(view, i10, bundle);
    }

    public static int b(View view, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
        int o10 = o(view, charSequence);
        if (o10 != -1) {
            c(view, new AccessibilityNodeInfoCompat.a(o10, charSequence, accessibilityViewCommand));
        }
        return o10;
    }

    public static void b0(View view) {
        h.k(view);
    }

    private static void c(View view, AccessibilityNodeInfoCompat.a aVar) {
        h(view);
        f0(aVar.b(), view);
        n(view).add(aVar);
        U(view, 0);
    }

    public static void c0(View view, Runnable runnable) {
        h.m(view, runnable);
    }

    public static ViewPropertyAnimatorCompat d(View view) {
        if (f2418b == null) {
            f2418b = new WeakHashMap<>();
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = f2418b.get(view);
        if (viewPropertyAnimatorCompat != null) {
            return viewPropertyAnimatorCompat;
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat2 = new ViewPropertyAnimatorCompat(view);
        f2418b.put(view, viewPropertyAnimatorCompat2);
        return viewPropertyAnimatorCompat2;
    }

    @SuppressLint({"LambdaLast"})
    public static void d0(View view, Runnable runnable, long j10) {
        h.n(view, runnable, j10);
    }

    public static WindowInsetsCompat e(View view, WindowInsetsCompat windowInsetsCompat, Rect rect) {
        return m.b(view, windowInsetsCompat, rect);
    }

    public static void e0(View view, int i10) {
        f0(i10, view);
        U(view, 0);
    }

    public static WindowInsetsCompat f(View view, WindowInsetsCompat windowInsetsCompat) {
        WindowInsets u7 = windowInsetsCompat.u();
        if (u7 != null) {
            WindowInsets a10 = l.a(view, u7);
            if (!a10.equals(u7)) {
                return WindowInsetsCompat.w(a10, view);
            }
        }
        return windowInsetsCompat;
    }

    private static void f0(int i10, View view) {
        List<AccessibilityNodeInfoCompat.a> n10 = n(view);
        for (int i11 = 0; i11 < n10.size(); i11++) {
            if (n10.get(i11).b() == i10) {
                n10.remove(i11);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean g(View view, KeyEvent keyEvent) {
        return false;
    }

    public static void g0(View view, AccessibilityNodeInfoCompat.a aVar, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
        if (accessibilityViewCommand == null && charSequence == null) {
            e0(view, aVar.b());
        } else {
            c(view, aVar.a(charSequence, accessibilityViewCommand));
        }
    }

    static void h(View view) {
        AccessibilityDelegateCompat j10 = j(view);
        if (j10 == null) {
            j10 = new AccessibilityDelegateCompat();
        }
        l0(view, j10);
    }

    public static void h0(View view) {
        l.c(view);
    }

    public static int i() {
        return i.a();
    }

    @Deprecated
    public static int i0(int i10, int i11, int i12) {
        return View.resolveSizeAndState(i10, i11, i12);
    }

    public static AccessibilityDelegateCompat j(View view) {
        View.AccessibilityDelegate k10 = k(view);
        if (k10 == null) {
            return null;
        }
        if (k10 instanceof AccessibilityDelegateCompat.a) {
            return ((AccessibilityDelegateCompat.a) k10).f2310a;
        }
        return new AccessibilityDelegateCompat(k10);
    }

    public static void j0(View view, @SuppressLint({"ContextFirst"}) Context context, int[] iArr, AttributeSet attributeSet, TypedArray typedArray, int i10, int i11) {
        r.c(view, context, iArr, attributeSet, typedArray, i10, i11);
    }

    private static View.AccessibilityDelegate k(View view) {
        return r.a(view);
    }

    private static f<Boolean> k0() {
        return new a(R$id.tag_screen_reader_focusable, Boolean.class, 28);
    }

    public static int l(View view) {
        return k.a(view);
    }

    public static void l0(View view, AccessibilityDelegateCompat accessibilityDelegateCompat) {
        if (accessibilityDelegateCompat == null && (k(view) instanceof AccessibilityDelegateCompat.a)) {
            accessibilityDelegateCompat = new AccessibilityDelegateCompat();
        }
        view.setAccessibilityDelegate(accessibilityDelegateCompat == null ? null : accessibilityDelegateCompat.getBridge());
    }

    public static CharSequence m(View view) {
        return Z().f(view);
    }

    public static void m0(View view, boolean z10) {
        a().g(view, Boolean.valueOf(z10));
    }

    private static List<AccessibilityNodeInfoCompat.a> n(View view) {
        int i10 = R$id.tag_accessibility_actions;
        ArrayList arrayList = (ArrayList) view.getTag(i10);
        if (arrayList != null) {
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList();
        view.setTag(i10, arrayList2);
        return arrayList2;
    }

    public static void n0(View view, int i10) {
        k.f(view, i10);
    }

    private static int o(View view, CharSequence charSequence) {
        List<AccessibilityNodeInfoCompat.a> n10 = n(view);
        for (int i10 = 0; i10 < n10.size(); i10++) {
            if (TextUtils.equals(charSequence, n10.get(i10).c())) {
                return n10.get(i10).b();
            }
        }
        int i11 = -1;
        int i12 = 0;
        while (true) {
            int[] iArr = f2420d;
            if (i12 >= iArr.length || i11 != -1) {
                break;
            }
            int i13 = iArr[i12];
            boolean z10 = true;
            for (int i14 = 0; i14 < n10.size(); i14++) {
                z10 &= n10.get(i14).b() != i13;
            }
            if (z10) {
                i11 = i13;
            }
            i12++;
        }
        return i11;
    }

    public static void o0(View view, CharSequence charSequence) {
        Z().g(view, charSequence);
        if (charSequence != null) {
            f2422f.a(view);
        } else {
            f2422f.c(view);
        }
    }

    public static ColorStateList p(View view) {
        return m.g(view);
    }

    public static void p0(View view, Drawable drawable) {
        h.q(view, drawable);
    }

    public static PorterDuff.Mode q(View view) {
        return m.h(view);
    }

    public static void q0(View view, ColorStateList colorStateList) {
        m.q(view, colorStateList);
    }

    public static Rect r(View view) {
        return j.a(view);
    }

    public static void r0(View view, PorterDuff.Mode mode) {
        m.r(view, mode);
    }

    public static Display s(View view) {
        return i.b(view);
    }

    public static void s0(View view, Rect rect) {
        j.c(view, rect);
    }

    public static float t(View view) {
        return m.i(view);
    }

    public static void t0(View view, float f10) {
        m.s(view, f10);
    }

    public static boolean u(View view) {
        return h.b(view);
    }

    @Deprecated
    public static void u0(View view, boolean z10) {
        view.setFitsSystemWindows(z10);
    }

    public static int v(View view) {
        return h.c(view);
    }

    public static void v0(View view, boolean z10) {
        h.r(view, z10);
    }

    @SuppressLint({"InlinedApi"})
    public static int w(View view) {
        return p.b(view);
    }

    public static void w0(View view, int i10) {
        h.s(view, i10);
    }

    public static int x(View view) {
        return i.d(view);
    }

    public static void x0(View view, int i10) {
        p.l(view, i10);
    }

    @Deprecated
    public static int y(View view) {
        return view.getMeasuredState();
    }

    public static void y0(View view, boolean z10) {
        m.t(view, z10);
    }

    public static int z(View view) {
        return h.d(view);
    }

    public static void z0(View view, androidx.core.view.t tVar) {
        m.u(view, tVar);
    }
}
