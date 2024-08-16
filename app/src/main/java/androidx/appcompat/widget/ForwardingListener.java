package androidx.appcompat.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import androidx.appcompat.view.menu.ShowableListMenu;

/* compiled from: ForwardingListener.java */
/* renamed from: androidx.appcompat.widget.w, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ForwardingListener implements View.OnTouchListener, View.OnAttachStateChangeListener {

    /* renamed from: e, reason: collision with root package name */
    private final float f1333e;

    /* renamed from: f, reason: collision with root package name */
    private final int f1334f;

    /* renamed from: g, reason: collision with root package name */
    private final int f1335g;

    /* renamed from: h, reason: collision with root package name */
    final View f1336h;

    /* renamed from: i, reason: collision with root package name */
    private Runnable f1337i;

    /* renamed from: j, reason: collision with root package name */
    private Runnable f1338j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f1339k;

    /* renamed from: l, reason: collision with root package name */
    private int f1340l;

    /* renamed from: m, reason: collision with root package name */
    private final int[] f1341m = new int[2];

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ForwardingListener.java */
    /* renamed from: androidx.appcompat.widget.w$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewParent parent = ForwardingListener.this.f1336h.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ForwardingListener.java */
    /* renamed from: androidx.appcompat.widget.w$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ForwardingListener.this.e();
        }
    }

    public ForwardingListener(View view) {
        this.f1336h = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener(this);
        this.f1333e = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        int tapTimeout = ViewConfiguration.getTapTimeout();
        this.f1334f = tapTimeout;
        this.f1335g = (tapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void a() {
        Runnable runnable = this.f1338j;
        if (runnable != null) {
            this.f1336h.removeCallbacks(runnable);
        }
        Runnable runnable2 = this.f1337i;
        if (runnable2 != null) {
            this.f1336h.removeCallbacks(runnable2);
        }
    }

    private boolean f(MotionEvent motionEvent) {
        DropDownListView dropDownListView;
        View view = this.f1336h;
        ShowableListMenu b10 = b();
        if (b10 == null || !b10.a() || (dropDownListView = (DropDownListView) b10.j()) == null || !dropDownListView.isShown()) {
            return false;
        }
        MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
        i(view, obtainNoHistory);
        j(dropDownListView, obtainNoHistory);
        boolean e10 = dropDownListView.e(obtainNoHistory, this.f1340l);
        obtainNoHistory.recycle();
        int actionMasked = motionEvent.getActionMasked();
        return e10 && (actionMasked != 1 && actionMasked != 3);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0017, code lost:
    
        if (r1 != 3) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean g(MotionEvent motionEvent) {
        View view = this.f1336h;
        if (!view.isEnabled()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.f1340l);
                    if (findPointerIndex >= 0 && !h(view, motionEvent.getX(findPointerIndex), motionEvent.getY(findPointerIndex), this.f1333e)) {
                        a();
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                }
            }
            a();
        } else {
            this.f1340l = motionEvent.getPointerId(0);
            if (this.f1337i == null) {
                this.f1337i = new a();
            }
            view.postDelayed(this.f1337i, this.f1334f);
            if (this.f1338j == null) {
                this.f1338j = new b();
            }
            view.postDelayed(this.f1338j, this.f1335g);
        }
        return false;
    }

    private static boolean h(View view, float f10, float f11, float f12) {
        float f13 = -f12;
        return f10 >= f13 && f11 >= f13 && f10 < ((float) (view.getRight() - view.getLeft())) + f12 && f11 < ((float) (view.getBottom() - view.getTop())) + f12;
    }

    private boolean i(View view, MotionEvent motionEvent) {
        view.getLocationOnScreen(this.f1341m);
        motionEvent.offsetLocation(r1[0], r1[1]);
        return true;
    }

    private boolean j(View view, MotionEvent motionEvent) {
        view.getLocationOnScreen(this.f1341m);
        motionEvent.offsetLocation(-r1[0], -r1[1]);
        return true;
    }

    public abstract ShowableListMenu b();

    protected abstract boolean c();

    protected boolean d() {
        ShowableListMenu b10 = b();
        if (b10 == null || !b10.a()) {
            return true;
        }
        b10.dismiss();
        return true;
    }

    void e() {
        a();
        View view = this.f1336h;
        if (view.isEnabled() && !view.isLongClickable() && c()) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            view.onTouchEvent(obtain);
            obtain.recycle();
            this.f1339k = true;
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z10;
        boolean z11 = this.f1339k;
        if (z11) {
            z10 = f(motionEvent) || !d();
        } else {
            z10 = g(motionEvent) && c();
            if (z10) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                this.f1336h.onTouchEvent(obtain);
                obtain.recycle();
            }
        }
        this.f1339k = z10;
        return z10 || z11;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        this.f1339k = false;
        this.f1340l = -1;
        Runnable runnable = this.f1337i;
        if (runnable != null) {
            this.f1336h.removeCallbacks(runnable);
        }
    }
}
