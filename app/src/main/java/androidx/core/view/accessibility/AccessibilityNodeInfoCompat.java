package androidx.core.view.accessibility;

import android.R;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.os.BuildCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* compiled from: AccessibilityNodeInfoCompat.java */
/* renamed from: androidx.core.view.accessibility.d, reason: use source file name */
/* loaded from: classes.dex */
public class AccessibilityNodeInfoCompat {

    /* renamed from: a, reason: collision with root package name */
    private final AccessibilityNodeInfo f2315a;

    /* renamed from: b, reason: collision with root package name */
    public int f2316b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f2317c = -1;

    /* compiled from: AccessibilityNodeInfoCompat.java */
    /* renamed from: androidx.core.view.accessibility.d$a */
    /* loaded from: classes.dex */
    public static class a {
        public static final a A;
        public static final a B;
        public static final a C;
        public static final a D;
        public static final a E;
        public static final a F;
        public static final a G;
        public static final a H;
        public static final a I;
        public static final a J;
        public static final a K;
        public static final a L;
        public static final a M;
        public static final a N;
        public static final a O;
        public static final a P;
        public static final a Q;
        public static final a R;
        public static final a S;
        public static final a T;
        public static final a U;

        /* renamed from: e, reason: collision with root package name */
        public static final a f2318e = new a(1, null);

        /* renamed from: f, reason: collision with root package name */
        public static final a f2319f = new a(2, null);

        /* renamed from: g, reason: collision with root package name */
        public static final a f2320g = new a(4, null);

        /* renamed from: h, reason: collision with root package name */
        public static final a f2321h = new a(8, null);

        /* renamed from: i, reason: collision with root package name */
        public static final a f2322i = new a(16, null);

        /* renamed from: j, reason: collision with root package name */
        public static final a f2323j = new a(32, null);

        /* renamed from: k, reason: collision with root package name */
        public static final a f2324k = new a(64, null);

        /* renamed from: l, reason: collision with root package name */
        public static final a f2325l = new a(128, null);

        /* renamed from: m, reason: collision with root package name */
        public static final a f2326m = new a(256, (CharSequence) null, (Class<? extends AccessibilityViewCommand.a>) AccessibilityViewCommand.b.class);

        /* renamed from: n, reason: collision with root package name */
        public static final a f2327n = new a(512, (CharSequence) null, (Class<? extends AccessibilityViewCommand.a>) AccessibilityViewCommand.b.class);

        /* renamed from: o, reason: collision with root package name */
        public static final a f2328o = new a(1024, (CharSequence) null, (Class<? extends AccessibilityViewCommand.a>) AccessibilityViewCommand.c.class);

        /* renamed from: p, reason: collision with root package name */
        public static final a f2329p = new a(2048, (CharSequence) null, (Class<? extends AccessibilityViewCommand.a>) AccessibilityViewCommand.c.class);

        /* renamed from: q, reason: collision with root package name */
        public static final a f2330q = new a(4096, null);

        /* renamed from: r, reason: collision with root package name */
        public static final a f2331r = new a(8192, null);

        /* renamed from: s, reason: collision with root package name */
        public static final a f2332s = new a(16384, null);

        /* renamed from: t, reason: collision with root package name */
        public static final a f2333t = new a(32768, null);

        /* renamed from: u, reason: collision with root package name */
        public static final a f2334u = new a(65536, null);

        /* renamed from: v, reason: collision with root package name */
        public static final a f2335v = new a(131072, (CharSequence) null, (Class<? extends AccessibilityViewCommand.a>) AccessibilityViewCommand.g.class);

        /* renamed from: w, reason: collision with root package name */
        public static final a f2336w = new a(262144, null);

        /* renamed from: x, reason: collision with root package name */
        public static final a f2337x = new a(524288, null);

        /* renamed from: y, reason: collision with root package name */
        public static final a f2338y = new a(1048576, null);

        /* renamed from: z, reason: collision with root package name */
        public static final a f2339z = new a(2097152, (CharSequence) null, (Class<? extends AccessibilityViewCommand.a>) AccessibilityViewCommand.h.class);

        /* renamed from: a, reason: collision with root package name */
        final Object f2340a;

        /* renamed from: b, reason: collision with root package name */
        private final int f2341b;

        /* renamed from: c, reason: collision with root package name */
        private final Class<? extends AccessibilityViewCommand.a> f2342c;

        /* renamed from: d, reason: collision with root package name */
        protected final AccessibilityViewCommand f2343d;

        static {
            int i10 = Build.VERSION.SDK_INT;
            A = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN, R.id.accessibilityActionShowOnScreen, null, null, null);
            B = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION, R.id.accessibilityActionScrollToPosition, null, null, AccessibilityViewCommand.e.class);
            C = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP, R.id.accessibilityActionScrollUp, null, null, null);
            D = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT, R.id.accessibilityActionScrollLeft, null, null, null);
            E = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN, R.id.accessibilityActionScrollDown, null, null, null);
            F = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT, R.id.accessibilityActionScrollRight, null, null, null);
            G = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_UP, R.id.accessibilityActionPageUp, null, null, null);
            H = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_DOWN, R.id.accessibilityActionPageDown, null, null, null);
            I = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT, R.id.accessibilityActionPageLeft, null, null, null);
            J = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT, R.id.accessibilityActionPageRight, null, null, null);
            K = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK, R.id.accessibilityActionContextClick, null, null, null);
            L = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS, R.id.accessibilityActionSetProgress, null, null, AccessibilityViewCommand.f.class);
            M = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW, R.id.accessibilityActionMoveWindow, null, null, AccessibilityViewCommand.d.class);
            N = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TOOLTIP, R.id.accessibilityActionShowTooltip, null, null, null);
            O = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_HIDE_TOOLTIP, R.id.accessibilityActionHideTooltip, null, null, null);
            P = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_PRESS_AND_HOLD, R.id.accessibilityActionPressAndHold, null, null, null);
            Q = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_IME_ENTER, R.id.accessibilityActionImeEnter, null, null, null);
            R = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_START, R.id.ALT, null, null, null);
            S = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_DROP, R.id.CTRL, null, null, null);
            T = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_CANCEL, R.id.FUNCTION, null, null, null);
            U = new a(i10 >= 33 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TEXT_SUGGESTIONS : null, R.id.KEYCODE_0, null, null, null);
        }

        public a(int i10, CharSequence charSequence) {
            this(null, i10, charSequence, null, null);
        }

        public a a(CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
            return new a(null, this.f2341b, charSequence, accessibilityViewCommand, this.f2342c);
        }

        public int b() {
            return ((AccessibilityNodeInfo.AccessibilityAction) this.f2340a).getId();
        }

        public CharSequence c() {
            return ((AccessibilityNodeInfo.AccessibilityAction) this.f2340a).getLabel();
        }

        public boolean d(View view, Bundle bundle) {
            if (this.f2343d == null) {
                return false;
            }
            AccessibilityViewCommand.a aVar = null;
            Class<? extends AccessibilityViewCommand.a> cls = this.f2342c;
            if (cls != null) {
                try {
                    AccessibilityViewCommand.a newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    try {
                        newInstance.a(bundle);
                        aVar = newInstance;
                    } catch (Exception e10) {
                        e = e10;
                        aVar = newInstance;
                        Class<? extends AccessibilityViewCommand.a> cls2 = this.f2342c;
                        Log.e("A11yActionCompat", "Failed to execute command with argument class ViewCommandArgument: " + (cls2 == null ? "null" : cls2.getName()), e);
                        return this.f2343d.perform(view, aVar);
                    }
                } catch (Exception e11) {
                    e = e11;
                }
            }
            return this.f2343d.perform(view, aVar);
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            Object obj2 = this.f2340a;
            return obj2 == null ? aVar.f2340a == null : obj2.equals(aVar.f2340a);
        }

        public int hashCode() {
            Object obj = this.f2340a;
            if (obj != null) {
                return obj.hashCode();
            }
            return 0;
        }

        public a(int i10, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand) {
            this(null, i10, charSequence, accessibilityViewCommand, null);
        }

        a(Object obj) {
            this(obj, 0, null, null, null);
        }

        private a(int i10, CharSequence charSequence, Class<? extends AccessibilityViewCommand.a> cls) {
            this(null, i10, charSequence, null, cls);
        }

        a(Object obj, int i10, CharSequence charSequence, AccessibilityViewCommand accessibilityViewCommand, Class<? extends AccessibilityViewCommand.a> cls) {
            this.f2341b = i10;
            this.f2343d = accessibilityViewCommand;
            if (obj == null) {
                this.f2340a = new AccessibilityNodeInfo.AccessibilityAction(i10, charSequence);
            } else {
                this.f2340a = obj;
            }
            this.f2342c = cls;
        }
    }

    /* compiled from: AccessibilityNodeInfoCompat.java */
    /* renamed from: androidx.core.view.accessibility.d$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        final Object f2344a;

        b(Object obj) {
            this.f2344a = obj;
        }

        public static b a(int i10, int i11, boolean z10) {
            return new b(AccessibilityNodeInfo.CollectionInfo.obtain(i10, i11, z10));
        }

        public static b b(int i10, int i11, boolean z10, int i12) {
            return new b(AccessibilityNodeInfo.CollectionInfo.obtain(i10, i11, z10, i12));
        }
    }

    /* compiled from: AccessibilityNodeInfoCompat.java */
    /* renamed from: androidx.core.view.accessibility.d$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        final Object f2345a;

        c(Object obj) {
            this.f2345a = obj;
        }

        public static c a(int i10, int i11, int i12, int i13, boolean z10, boolean z11) {
            return new c(AccessibilityNodeInfo.CollectionItemInfo.obtain(i10, i11, i12, i13, z10, z11));
        }
    }

    /* compiled from: AccessibilityNodeInfoCompat.java */
    /* renamed from: androidx.core.view.accessibility.d$d */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        final Object f2346a;

        d(Object obj) {
            this.f2346a = obj;
        }

        public static d a(int i10, float f10, float f11, float f12) {
            return new d(AccessibilityNodeInfo.RangeInfo.obtain(i10, f10, f11, f12));
        }
    }

    private AccessibilityNodeInfoCompat(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.f2315a = accessibilityNodeInfo;
    }

    public static AccessibilityNodeInfoCompat C0(AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }

    public static AccessibilityNodeInfoCompat J() {
        return C0(AccessibilityNodeInfo.obtain());
    }

    public static AccessibilityNodeInfoCompat K(View view) {
        return C0(AccessibilityNodeInfo.obtain(view));
    }

    public static AccessibilityNodeInfoCompat L(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return C0(AccessibilityNodeInfo.obtain(accessibilityNodeInfoCompat.f2315a));
    }

    private List<Integer> f(String str) {
        ArrayList<Integer> integerArrayList = this.f2315a.getExtras().getIntegerArrayList(str);
        if (integerArrayList != null) {
            return integerArrayList;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        this.f2315a.getExtras().putIntegerArrayList(str, arrayList);
        return arrayList;
    }

    private static String h(int i10) {
        if (i10 == 1) {
            return "ACTION_FOCUS";
        }
        if (i10 == 2) {
            return "ACTION_CLEAR_FOCUS";
        }
        switch (i10) {
            case 4:
                return "ACTION_SELECT";
            case 8:
                return "ACTION_CLEAR_SELECTION";
            case 16:
                return "ACTION_CLICK";
            case 32:
                return "ACTION_LONG_CLICK";
            case 64:
                return "ACTION_ACCESSIBILITY_FOCUS";
            case 128:
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case 256:
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case 512:
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case 1024:
                return "ACTION_NEXT_HTML_ELEMENT";
            case 2048:
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case 4096:
                return "ACTION_SCROLL_FORWARD";
            case 8192:
                return "ACTION_SCROLL_BACKWARD";
            case 16384:
                return "ACTION_COPY";
            case 32768:
                return "ACTION_PASTE";
            case 65536:
                return "ACTION_CUT";
            case 131072:
                return "ACTION_SET_SELECTION";
            case 262144:
                return "ACTION_EXPAND";
            case 524288:
                return "ACTION_COLLAPSE";
            case 2097152:
                return "ACTION_SET_TEXT";
            case R.id.accessibilityActionMoveWindow:
                return "ACTION_MOVE_WINDOW";
            default:
                switch (i10) {
                    case R.id.accessibilityActionShowOnScreen:
                        return "ACTION_SHOW_ON_SCREEN";
                    case R.id.accessibilityActionScrollToPosition:
                        return "ACTION_SCROLL_TO_POSITION";
                    case R.id.accessibilityActionScrollUp:
                        return "ACTION_SCROLL_UP";
                    case R.id.accessibilityActionScrollLeft:
                        return "ACTION_SCROLL_LEFT";
                    case R.id.accessibilityActionScrollDown:
                        return "ACTION_SCROLL_DOWN";
                    case R.id.accessibilityActionScrollRight:
                        return "ACTION_SCROLL_RIGHT";
                    case R.id.accessibilityActionContextClick:
                        return "ACTION_CONTEXT_CLICK";
                    case R.id.accessibilityActionSetProgress:
                        return "ACTION_SET_PROGRESS";
                    default:
                        switch (i10) {
                            case R.id.accessibilityActionShowTooltip:
                                return "ACTION_SHOW_TOOLTIP";
                            case R.id.accessibilityActionHideTooltip:
                                return "ACTION_HIDE_TOOLTIP";
                            case R.id.accessibilityActionPageUp:
                                return "ACTION_PAGE_UP";
                            case R.id.accessibilityActionPageDown:
                                return "ACTION_PAGE_DOWN";
                            case R.id.accessibilityActionPageLeft:
                                return "ACTION_PAGE_LEFT";
                            case R.id.accessibilityActionPageRight:
                                return "ACTION_PAGE_RIGHT";
                            case R.id.accessibilityActionPressAndHold:
                                return "ACTION_PRESS_AND_HOLD";
                            default:
                                switch (i10) {
                                    case R.id.accessibilityActionImeEnter:
                                        return "ACTION_IME_ENTER";
                                    case R.id.ALT:
                                        return "ACTION_DRAG_START";
                                    case R.id.CTRL:
                                        return "ACTION_DRAG_DROP";
                                    case R.id.FUNCTION:
                                        return "ACTION_DRAG_CANCEL";
                                    default:
                                        return "ACTION_UNKNOWN";
                                }
                        }
                }
        }
    }

    public static ClickableSpan[] n(CharSequence charSequence) {
        if (charSequence instanceof Spanned) {
            return (ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class);
        }
        return null;
    }

    private boolean v() {
        return !f("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").isEmpty();
    }

    public boolean A() {
        return this.f2315a.isEnabled();
    }

    public void A0(boolean z10) {
        this.f2315a.setVisibleToUser(z10);
    }

    public boolean B() {
        return this.f2315a.isFocusable();
    }

    public AccessibilityNodeInfo B0() {
        return this.f2315a;
    }

    public boolean C() {
        return this.f2315a.isFocused();
    }

    public boolean D() {
        return this.f2315a.isLongClickable();
    }

    public boolean E() {
        return this.f2315a.isPassword();
    }

    public boolean F() {
        return this.f2315a.isScrollable();
    }

    public boolean G() {
        return this.f2315a.isSelected();
    }

    public boolean H() {
        return this.f2315a.isShowingHintText();
    }

    public boolean I() {
        return this.f2315a.isVisibleToUser();
    }

    public boolean M(int i10, Bundle bundle) {
        return this.f2315a.performAction(i10, bundle);
    }

    public void N() {
        this.f2315a.recycle();
    }

    public boolean O(a aVar) {
        return this.f2315a.removeAction((AccessibilityNodeInfo.AccessibilityAction) aVar.f2340a);
    }

    public void P(boolean z10) {
        this.f2315a.setAccessibilityFocused(z10);
    }

    @Deprecated
    public void Q(Rect rect) {
        this.f2315a.setBoundsInParent(rect);
    }

    public void R(Rect rect) {
        this.f2315a.setBoundsInScreen(rect);
    }

    public void S(boolean z10) {
        this.f2315a.setCanOpenPopup(z10);
    }

    public void T(boolean z10) {
        this.f2315a.setCheckable(z10);
    }

    public void U(boolean z10) {
        this.f2315a.setChecked(z10);
    }

    public void V(CharSequence charSequence) {
        this.f2315a.setClassName(charSequence);
    }

    public void W(boolean z10) {
        this.f2315a.setClickable(z10);
    }

    public void X(Object obj) {
        this.f2315a.setCollectionInfo(obj == null ? null : (AccessibilityNodeInfo.CollectionInfo) ((b) obj).f2344a);
    }

    public void Y(Object obj) {
        this.f2315a.setCollectionItemInfo(obj == null ? null : (AccessibilityNodeInfo.CollectionItemInfo) ((c) obj).f2345a);
    }

    public void Z(CharSequence charSequence) {
        this.f2315a.setContentDescription(charSequence);
    }

    public void a(int i10) {
        this.f2315a.addAction(i10);
    }

    public void a0(boolean z10) {
        this.f2315a.setDismissable(z10);
    }

    public void b(a aVar) {
        this.f2315a.addAction((AccessibilityNodeInfo.AccessibilityAction) aVar.f2340a);
    }

    public void b0(boolean z10) {
        this.f2315a.setEnabled(z10);
    }

    public void c(View view) {
        this.f2315a.addChild(view);
    }

    public void c0(CharSequence charSequence) {
        this.f2315a.setError(charSequence);
    }

    public void d(View view, int i10) {
        this.f2315a.addChild(view, i10);
    }

    public void d0(boolean z10) {
        this.f2315a.setFocusable(z10);
    }

    public void e(CharSequence charSequence, View view) {
    }

    public void e0(boolean z10) {
        this.f2315a.setFocused(z10);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AccessibilityNodeInfoCompat)) {
            return false;
        }
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat) obj;
        AccessibilityNodeInfo accessibilityNodeInfo = this.f2315a;
        if (accessibilityNodeInfo == null) {
            if (accessibilityNodeInfoCompat.f2315a != null) {
                return false;
            }
        } else if (!accessibilityNodeInfo.equals(accessibilityNodeInfoCompat.f2315a)) {
            return false;
        }
        return this.f2317c == accessibilityNodeInfoCompat.f2317c && this.f2316b == accessibilityNodeInfoCompat.f2316b;
    }

    public void f0(boolean z10) {
        this.f2315a.setHeading(z10);
    }

    public List<a> g() {
        List<AccessibilityNodeInfo.AccessibilityAction> actionList = this.f2315a.getActionList();
        if (actionList != null) {
            ArrayList arrayList = new ArrayList();
            int size = actionList.size();
            for (int i10 = 0; i10 < size; i10++) {
                arrayList.add(new a(actionList.get(i10)));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    public void g0(CharSequence charSequence) {
        this.f2315a.setHintText(charSequence);
    }

    public void h0(View view) {
        this.f2315a.setLabelFor(view);
    }

    public int hashCode() {
        AccessibilityNodeInfo accessibilityNodeInfo = this.f2315a;
        if (accessibilityNodeInfo == null) {
            return 0;
        }
        return accessibilityNodeInfo.hashCode();
    }

    @Deprecated
    public int i() {
        return this.f2315a.getActions();
    }

    public void i0(boolean z10) {
        this.f2315a.setLongClickable(z10);
    }

    @Deprecated
    public void j(Rect rect) {
        this.f2315a.getBoundsInParent(rect);
    }

    public void j0(int i10) {
        this.f2315a.setMaxTextLength(i10);
    }

    public void k(Rect rect) {
        this.f2315a.getBoundsInScreen(rect);
    }

    public void k0(int i10) {
        this.f2315a.setMovementGranularities(i10);
    }

    public int l() {
        return this.f2315a.getChildCount();
    }

    public void l0(CharSequence charSequence) {
        this.f2315a.setPackageName(charSequence);
    }

    public CharSequence m() {
        return this.f2315a.getClassName();
    }

    public void m0(CharSequence charSequence) {
        this.f2315a.setPaneTitle(charSequence);
    }

    public void n0(View view) {
        this.f2316b = -1;
        this.f2315a.setParent(view);
    }

    public CharSequence o() {
        return this.f2315a.getContentDescription();
    }

    public void o0(View view, int i10) {
        this.f2316b = i10;
        this.f2315a.setParent(view, i10);
    }

    public Bundle p() {
        return this.f2315a.getExtras();
    }

    public void p0(d dVar) {
        this.f2315a.setRangeInfo((AccessibilityNodeInfo.RangeInfo) dVar.f2346a);
    }

    public int q() {
        return this.f2315a.getMovementGranularities();
    }

    public void q0(CharSequence charSequence) {
        this.f2315a.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", charSequence);
    }

    public CharSequence r() {
        return this.f2315a.getPackageName();
    }

    public void r0(boolean z10) {
        this.f2315a.setScreenReaderFocusable(z10);
    }

    public CharSequence s() {
        if (v()) {
            List<Integer> f10 = f("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
            List<Integer> f11 = f("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
            List<Integer> f12 = f("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
            List<Integer> f13 = f("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
            SpannableString spannableString = new SpannableString(TextUtils.substring(this.f2315a.getText(), 0, this.f2315a.getText().length()));
            for (int i10 = 0; i10 < f10.size(); i10++) {
                spannableString.setSpan(new AccessibilityClickableSpanCompat(f13.get(i10).intValue(), this, p().getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY")), f10.get(i10).intValue(), f11.get(i10).intValue(), f12.get(i10).intValue());
            }
            return spannableString;
        }
        return this.f2315a.getText();
    }

    public void s0(boolean z10) {
        this.f2315a.setScrollable(z10);
    }

    public String t() {
        if (BuildCompat.c()) {
            return this.f2315a.getUniqueId();
        }
        return this.f2315a.getExtras().getString("androidx.view.accessibility.AccessibilityNodeInfoCompat.UNIQUE_ID_KEY");
    }

    public void t0(boolean z10) {
        this.f2315a.setSelected(z10);
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        sb2.append(super.toString());
        Rect rect = new Rect();
        j(rect);
        sb2.append("; boundsInParent: " + rect);
        k(rect);
        sb2.append("; boundsInScreen: " + rect);
        sb2.append("; packageName: ");
        sb2.append(r());
        sb2.append("; className: ");
        sb2.append(m());
        sb2.append("; text: ");
        sb2.append(s());
        sb2.append("; contentDescription: ");
        sb2.append(o());
        sb2.append("; viewId: ");
        sb2.append(u());
        sb2.append("; uniqueId: ");
        sb2.append(t());
        sb2.append("; checkable: ");
        sb2.append(x());
        sb2.append("; checked: ");
        sb2.append(y());
        sb2.append("; focusable: ");
        sb2.append(B());
        sb2.append("; focused: ");
        sb2.append(C());
        sb2.append("; selected: ");
        sb2.append(G());
        sb2.append("; clickable: ");
        sb2.append(z());
        sb2.append("; longClickable: ");
        sb2.append(D());
        sb2.append("; enabled: ");
        sb2.append(A());
        sb2.append("; password: ");
        sb2.append(E());
        sb2.append("; scrollable: " + F());
        sb2.append("; [");
        List<a> g6 = g();
        for (int i10 = 0; i10 < g6.size(); i10++) {
            a aVar = g6.get(i10);
            String h10 = h(aVar.b());
            if (h10.equals("ACTION_UNKNOWN") && aVar.c() != null) {
                h10 = aVar.c().toString();
            }
            sb2.append(h10);
            if (i10 != g6.size() - 1) {
                sb2.append(", ");
            }
        }
        sb2.append("]");
        return sb2.toString();
    }

    public String u() {
        return this.f2315a.getViewIdResourceName();
    }

    public void u0(boolean z10) {
        this.f2315a.setShowingHintText(z10);
    }

    public void v0(View view) {
        this.f2317c = -1;
        this.f2315a.setSource(view);
    }

    public boolean w() {
        return this.f2315a.isAccessibilityFocused();
    }

    public void w0(View view, int i10) {
        this.f2317c = i10;
        this.f2315a.setSource(view, i10);
    }

    public boolean x() {
        return this.f2315a.isCheckable();
    }

    public void x0(CharSequence charSequence) {
        if (BuildCompat.b()) {
            this.f2315a.setStateDescription(charSequence);
        } else {
            this.f2315a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.STATE_DESCRIPTION_KEY", charSequence);
        }
    }

    public boolean y() {
        return this.f2315a.isChecked();
    }

    public void y0(CharSequence charSequence) {
        this.f2315a.setText(charSequence);
    }

    public boolean z() {
        return this.f2315a.isClickable();
    }

    public void z0(View view) {
        this.f2315a.setTraversalAfter(view);
    }
}
