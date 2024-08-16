package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.R$attr;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.widget.ListViewAutoScrollHelper;
import d.DrawableWrapperCompat;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DropDownListView.java */
/* renamed from: androidx.appcompat.widget.u, reason: use source file name */
/* loaded from: classes.dex */
public class DropDownListView extends ListView {

    /* renamed from: e, reason: collision with root package name */
    private final Rect f1313e;

    /* renamed from: f, reason: collision with root package name */
    private int f1314f;

    /* renamed from: g, reason: collision with root package name */
    private int f1315g;

    /* renamed from: h, reason: collision with root package name */
    private int f1316h;

    /* renamed from: i, reason: collision with root package name */
    private int f1317i;

    /* renamed from: j, reason: collision with root package name */
    private int f1318j;

    /* renamed from: k, reason: collision with root package name */
    private d f1319k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f1320l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f1321m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1322n;

    /* renamed from: o, reason: collision with root package name */
    private ViewPropertyAnimatorCompat f1323o;

    /* renamed from: p, reason: collision with root package name */
    private ListViewAutoScrollHelper f1324p;

    /* renamed from: q, reason: collision with root package name */
    f f1325q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropDownListView.java */
    /* renamed from: androidx.appcompat.widget.u$a */
    /* loaded from: classes.dex */
    public static class a {
        static void a(View view, float f10, float f11) {
            view.drawableHotspotChanged(f10, f11);
        }
    }

    /* compiled from: DropDownListView.java */
    /* renamed from: androidx.appcompat.widget.u$b */
    /* loaded from: classes.dex */
    static class b {

        /* renamed from: a, reason: collision with root package name */
        private static Method f1326a;

        /* renamed from: b, reason: collision with root package name */
        private static Method f1327b;

        /* renamed from: c, reason: collision with root package name */
        private static Method f1328c;

        /* renamed from: d, reason: collision with root package name */
        private static boolean f1329d;

        static {
            try {
                Class cls = Integer.TYPE;
                Class cls2 = Float.TYPE;
                Method declaredMethod = AbsListView.class.getDeclaredMethod("positionSelector", cls, View.class, Boolean.TYPE, cls2, cls2);
                f1326a = declaredMethod;
                declaredMethod.setAccessible(true);
                Method declaredMethod2 = AdapterView.class.getDeclaredMethod("setSelectedPositionInt", cls);
                f1327b = declaredMethod2;
                declaredMethod2.setAccessible(true);
                Method declaredMethod3 = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt", cls);
                f1328c = declaredMethod3;
                declaredMethod3.setAccessible(true);
                f1329d = true;
            } catch (NoSuchMethodException e10) {
                e10.printStackTrace();
            }
        }

        static boolean a() {
            return f1329d;
        }

        @SuppressLint({"BanUncheckedReflection"})
        static void b(DropDownListView dropDownListView, int i10, View view) {
            try {
                f1326a.invoke(dropDownListView, Integer.valueOf(i10), view, Boolean.FALSE, -1, -1);
                f1327b.invoke(dropDownListView, Integer.valueOf(i10));
                f1328c.invoke(dropDownListView, Integer.valueOf(i10));
            } catch (IllegalAccessException e10) {
                e10.printStackTrace();
            } catch (InvocationTargetException e11) {
                e11.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropDownListView.java */
    /* renamed from: androidx.appcompat.widget.u$c */
    /* loaded from: classes.dex */
    public static class c {
        static boolean a(AbsListView absListView) {
            return absListView.isSelectedChildViewEnabled();
        }

        static void b(AbsListView absListView, boolean z10) {
            absListView.setSelectedChildViewEnabled(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DropDownListView.java */
    /* renamed from: androidx.appcompat.widget.u$d */
    /* loaded from: classes.dex */
    public static class d extends DrawableWrapperCompat {

        /* renamed from: f, reason: collision with root package name */
        private boolean f1330f;

        d(Drawable drawable) {
            super(drawable);
            this.f1330f = true;
        }

        void b(boolean z10) {
            this.f1330f = z10;
        }

        @Override // d.DrawableWrapperCompat, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.f1330f) {
                super.draw(canvas);
            }
        }

        @Override // d.DrawableWrapperCompat, android.graphics.drawable.Drawable
        public void setHotspot(float f10, float f11) {
            if (this.f1330f) {
                super.setHotspot(f10, f11);
            }
        }

        @Override // d.DrawableWrapperCompat, android.graphics.drawable.Drawable
        public void setHotspotBounds(int i10, int i11, int i12, int i13) {
            if (this.f1330f) {
                super.setHotspotBounds(i10, i11, i12, i13);
            }
        }

        @Override // d.DrawableWrapperCompat, android.graphics.drawable.Drawable
        public boolean setState(int[] iArr) {
            if (this.f1330f) {
                return super.setState(iArr);
            }
            return false;
        }

        @Override // d.DrawableWrapperCompat, android.graphics.drawable.Drawable
        public boolean setVisible(boolean z10, boolean z11) {
            if (this.f1330f) {
                return super.setVisible(z10, z11);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DropDownListView.java */
    /* renamed from: androidx.appcompat.widget.u$e */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        private static final Field f1331a;

        static {
            Field field = null;
            try {
                field = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
                field.setAccessible(true);
            } catch (NoSuchFieldException e10) {
                e10.printStackTrace();
            }
            f1331a = field;
        }

        static boolean a(AbsListView absListView) {
            Field field = f1331a;
            if (field == null) {
                return false;
            }
            try {
                return field.getBoolean(absListView);
            } catch (IllegalAccessException e10) {
                e10.printStackTrace();
                return false;
            }
        }

        static void b(AbsListView absListView, boolean z10) {
            Field field = f1331a;
            if (field != null) {
                try {
                    field.set(absListView, Boolean.valueOf(z10));
                } catch (IllegalAccessException e10) {
                    e10.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: DropDownListView.java */
    /* renamed from: androidx.appcompat.widget.u$f */
    /* loaded from: classes.dex */
    public class f implements Runnable {
        f() {
        }

        public void a() {
            DropDownListView dropDownListView = DropDownListView.this;
            dropDownListView.f1325q = null;
            dropDownListView.removeCallbacks(this);
        }

        public void b() {
            DropDownListView.this.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            DropDownListView dropDownListView = DropDownListView.this;
            dropDownListView.f1325q = null;
            dropDownListView.drawableStateChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DropDownListView(Context context, boolean z10) {
        super(context, null, R$attr.dropDownListViewStyle);
        this.f1313e = new Rect();
        this.f1314f = 0;
        this.f1315g = 0;
        this.f1316h = 0;
        this.f1317i = 0;
        this.f1321m = z10;
        setCacheColorHint(0);
    }

    private void a() {
        this.f1322n = false;
        setPressed(false);
        drawableStateChanged();
        View childAt = getChildAt(this.f1318j - getFirstVisiblePosition());
        if (childAt != null) {
            childAt.setPressed(false);
        }
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = this.f1323o;
        if (viewPropertyAnimatorCompat != null) {
            viewPropertyAnimatorCompat.c();
            this.f1323o = null;
        }
    }

    private void b(View view, int i10) {
        performItemClick(view, i10, getItemIdAtPosition(i10));
    }

    private void c(Canvas canvas) {
        Drawable selector;
        if (this.f1313e.isEmpty() || (selector = getSelector()) == null) {
            return;
        }
        selector.setBounds(this.f1313e);
        selector.draw(canvas);
    }

    private void f(int i10, View view) {
        Rect rect = this.f1313e;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        rect.left -= this.f1314f;
        rect.top -= this.f1315g;
        rect.right += this.f1316h;
        rect.bottom += this.f1317i;
        boolean j10 = j();
        if (view.isEnabled() != j10) {
            k(!j10);
            if (i10 != -1) {
                refreshDrawableState();
            }
        }
    }

    private void g(int i10, View view) {
        Drawable selector = getSelector();
        boolean z10 = (selector == null || i10 == -1) ? false : true;
        if (z10) {
            selector.setVisible(false, false);
        }
        f(i10, view);
        if (z10) {
            Rect rect = this.f1313e;
            float exactCenterX = rect.exactCenterX();
            float exactCenterY = rect.exactCenterY();
            selector.setVisible(getVisibility() == 0, false);
            DrawableCompat.e(selector, exactCenterX, exactCenterY);
        }
    }

    private void h(int i10, View view, float f10, float f11) {
        g(i10, view);
        Drawable selector = getSelector();
        if (selector == null || i10 == -1) {
            return;
        }
        DrawableCompat.e(selector, f10, f11);
    }

    private void i(View view, int i10, float f10, float f11) {
        View childAt;
        this.f1322n = true;
        a.a(this, f10, f11);
        if (!isPressed()) {
            setPressed(true);
        }
        layoutChildren();
        int i11 = this.f1318j;
        if (i11 != -1 && (childAt = getChildAt(i11 - getFirstVisiblePosition())) != null && childAt != view && childAt.isPressed()) {
            childAt.setPressed(false);
        }
        this.f1318j = i10;
        a.a(view, f10 - view.getLeft(), f11 - view.getTop());
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        h(i10, view, f10, f11);
        setSelectorEnabled(false);
        refreshDrawableState();
    }

    private boolean j() {
        if (BuildCompat.c()) {
            return c.a(this);
        }
        return e.a(this);
    }

    private void k(boolean z10) {
        if (BuildCompat.c()) {
            c.b(this, z10);
        } else {
            e.b(this, z10);
        }
    }

    private boolean l() {
        return this.f1322n;
    }

    private void m() {
        Drawable selector = getSelector();
        if (selector != null && l() && isPressed()) {
            selector.setState(getDrawableState());
        }
    }

    private void setSelectorEnabled(boolean z10) {
        d dVar = this.f1319k;
        if (dVar != null) {
            dVar.b(z10);
        }
    }

    public int d(int i10, int i11, int i12, int i13, int i14) {
        int makeMeasureSpec;
        int listPaddingTop = getListPaddingTop();
        int listPaddingBottom = getListPaddingBottom();
        int dividerHeight = getDividerHeight();
        Drawable divider = getDivider();
        ListAdapter adapter = getAdapter();
        if (adapter == null) {
            return listPaddingTop + listPaddingBottom;
        }
        int i15 = listPaddingTop + listPaddingBottom;
        if (dividerHeight <= 0 || divider == null) {
            dividerHeight = 0;
        }
        int count = adapter.getCount();
        int i16 = 0;
        int i17 = 0;
        int i18 = 0;
        View view = null;
        while (i16 < count) {
            int itemViewType = adapter.getItemViewType(i16);
            if (itemViewType != i17) {
                view = null;
                i17 = itemViewType;
            }
            view = adapter.getView(i16, view, this);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
                view.setLayoutParams(layoutParams);
            }
            int i19 = layoutParams.height;
            if (i19 > 0) {
                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i19, 1073741824);
            } else {
                makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
            }
            view.measure(i10, makeMeasureSpec);
            view.forceLayout();
            if (i16 > 0) {
                i15 += dividerHeight;
            }
            i15 += view.getMeasuredHeight();
            if (i15 >= i13) {
                return (i14 < 0 || i16 <= i14 || i18 <= 0 || i15 == i13) ? i13 : i18;
            }
            if (i14 >= 0 && i16 >= i14) {
                i18 = i15;
            }
            i16++;
        }
        return i15;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        c(canvas);
        super.dispatchDraw(canvas);
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        if (this.f1325q != null) {
            return;
        }
        super.drawableStateChanged();
        setSelectorEnabled(true);
        m();
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x000c, code lost:
    
        if (r0 != 3) goto L8;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0048 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean e(MotionEvent motionEvent, int i10) {
        boolean z10;
        boolean z11;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1) {
            z10 = false;
        } else if (actionMasked == 2) {
            z10 = true;
        }
        int findPointerIndex = motionEvent.findPointerIndex(i10);
        if (findPointerIndex >= 0) {
            int x10 = (int) motionEvent.getX(findPointerIndex);
            int y4 = (int) motionEvent.getY(findPointerIndex);
            int pointToPosition = pointToPosition(x10, y4);
            if (pointToPosition != -1) {
                View childAt = getChildAt(pointToPosition - getFirstVisiblePosition());
                i(childAt, pointToPosition, x10, y4);
                if (actionMasked == 1) {
                    b(childAt, pointToPosition);
                }
                z11 = false;
                z10 = true;
                if (z10) {
                }
                a();
                if (z10) {
                }
                return z10;
            }
            z11 = true;
            if (z10 || z11) {
                a();
            }
            if (z10) {
                if (this.f1324p == null) {
                    this.f1324p = new ListViewAutoScrollHelper(this);
                }
                this.f1324p.m(true);
                this.f1324p.onTouch(this, motionEvent);
            } else {
                ListViewAutoScrollHelper listViewAutoScrollHelper = this.f1324p;
                if (listViewAutoScrollHelper != null) {
                    listViewAutoScrollHelper.m(false);
                }
            }
            return z10;
        }
        z11 = false;
        z10 = false;
        if (z10) {
        }
        a();
        if (z10) {
        }
        return z10;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean hasFocus() {
        return this.f1321m || super.hasFocus();
    }

    @Override // android.view.View
    public boolean hasWindowFocus() {
        return this.f1321m || super.hasWindowFocus();
    }

    @Override // android.view.View
    public boolean isFocused() {
        return this.f1321m || super.isFocused();
    }

    @Override // android.view.View
    public boolean isInTouchMode() {
        return (this.f1321m && this.f1320l) || super.isInTouchMode();
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.f1325q = null;
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 10 && this.f1325q == null) {
            f fVar = new f();
            this.f1325q = fVar;
            fVar.b();
        }
        boolean onHoverEvent = super.onHoverEvent(motionEvent);
        if (actionMasked != 9 && actionMasked != 7) {
            setSelection(-1);
        } else {
            int pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
            if (pointToPosition != -1 && pointToPosition != getSelectedItemPosition()) {
                View childAt = getChildAt(pointToPosition - getFirstVisiblePosition());
                if (childAt.isEnabled()) {
                    requestFocus();
                    if (b.a()) {
                        b.b(this, pointToPosition, childAt);
                    } else {
                        setSelectionFromTop(pointToPosition, childAt.getTop() - getTop());
                    }
                }
                m();
            }
        }
        return onHoverEvent;
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.f1318j = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
        }
        f fVar = this.f1325q;
        if (fVar != null) {
            fVar.a();
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setListSelectionHidden(boolean z10) {
        this.f1320l = z10;
    }

    @Override // android.widget.AbsListView
    public void setSelector(Drawable drawable) {
        d dVar = drawable != null ? new d(drawable) : null;
        this.f1319k = dVar;
        super.setSelector(dVar);
        Rect rect = new Rect();
        if (drawable != null) {
            drawable.getPadding(rect);
        }
        this.f1314f = rect.left;
        this.f1315g = rect.top;
        this.f1316h = rect.right;
        this.f1317i = rect.bottom;
    }
}
