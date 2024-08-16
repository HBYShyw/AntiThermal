package androidx.customview.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat;
import androidx.core.view.accessibility.AccessibilityRecordCompat;
import androidx.customview.widget.FocusStrategy;
import j.SparseArrayCompat;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ExploreByTouchHelper.java */
/* renamed from: androidx.customview.widget.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
    private static final String DEFAULT_CLASS_NAME = "android.view.View";
    public static final int HOST_ID = -1;
    public static final int INVALID_ID = Integer.MIN_VALUE;
    private static final Rect INVALID_PARENT_BOUNDS = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    private static final FocusStrategy.a<AccessibilityNodeInfoCompat> NODE_ADAPTER = new a();
    private static final FocusStrategy.b<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> SPARSE_VALUES_ADAPTER = new b();
    private final View mHost;
    private final AccessibilityManager mManager;
    private c mNodeProvider;
    private final Rect mTempScreenRect = new Rect();
    private final Rect mTempParentRect = new Rect();
    private final Rect mTempVisibleRect = new Rect();
    private final int[] mTempGlobalRect = new int[2];
    int mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
    int mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
    private int mHoveredVirtualViewId = Integer.MIN_VALUE;

    /* compiled from: ExploreByTouchHelper.java */
    /* renamed from: androidx.customview.widget.a$a */
    /* loaded from: classes.dex */
    class a implements FocusStrategy.a<AccessibilityNodeInfoCompat> {
        a() {
        }

        @Override // androidx.customview.widget.FocusStrategy.a
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, Rect rect) {
            accessibilityNodeInfoCompat.j(rect);
        }
    }

    /* compiled from: ExploreByTouchHelper.java */
    /* renamed from: androidx.customview.widget.a$b */
    /* loaded from: classes.dex */
    class b implements FocusStrategy.b<SparseArrayCompat<AccessibilityNodeInfoCompat>, AccessibilityNodeInfoCompat> {
        b() {
        }

        @Override // androidx.customview.widget.FocusStrategy.b
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public AccessibilityNodeInfoCompat a(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat, int i10) {
            return sparseArrayCompat.l(i10);
        }

        @Override // androidx.customview.widget.FocusStrategy.b
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public int b(SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat) {
            return sparseArrayCompat.k();
        }
    }

    /* compiled from: ExploreByTouchHelper.java */
    /* renamed from: androidx.customview.widget.a$c */
    /* loaded from: classes.dex */
    private class c extends AccessibilityNodeProviderCompat {
        c() {
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public AccessibilityNodeInfoCompat b(int i10) {
            return AccessibilityNodeInfoCompat.L(ExploreByTouchHelper.this.obtainAccessibilityNodeInfo(i10));
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public AccessibilityNodeInfoCompat d(int i10) {
            int i11 = i10 == 2 ? ExploreByTouchHelper.this.mAccessibilityFocusedVirtualViewId : ExploreByTouchHelper.this.mKeyboardFocusedVirtualViewId;
            if (i11 == Integer.MIN_VALUE) {
                return null;
            }
            return b(i11);
        }

        @Override // androidx.core.view.accessibility.AccessibilityNodeProviderCompat
        public boolean f(int i10, int i11, Bundle bundle) {
            return ExploreByTouchHelper.this.performAction(i10, i11, bundle);
        }
    }

    public ExploreByTouchHelper(View view) {
        if (view != null) {
            this.mHost = view;
            this.mManager = (AccessibilityManager) view.getContext().getSystemService("accessibility");
            view.setFocusable(true);
            if (ViewCompat.v(view) == 0) {
                ViewCompat.w0(view, 1);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("View may not be null");
    }

    private boolean clearAccessibilityFocus(int i10) {
        if (this.mAccessibilityFocusedVirtualViewId != i10) {
            return false;
        }
        this.mAccessibilityFocusedVirtualViewId = Integer.MIN_VALUE;
        this.mHost.invalidate();
        sendEventForVirtualView(i10, 65536);
        return true;
    }

    private boolean clickKeyboardFocusedVirtualView() {
        int i10 = this.mKeyboardFocusedVirtualViewId;
        return i10 != Integer.MIN_VALUE && onPerformActionForVirtualView(i10, 16, null);
    }

    private AccessibilityEvent createEvent(int i10, int i11) {
        if (i10 != -1) {
            return createEventForChild(i10, i11);
        }
        return createEventForHost(i11);
    }

    private AccessibilityEvent createEventForChild(int i10, int i11) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i11);
        AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo = obtainAccessibilityNodeInfo(i10);
        obtain.getText().add(obtainAccessibilityNodeInfo.s());
        obtain.setContentDescription(obtainAccessibilityNodeInfo.o());
        obtain.setScrollable(obtainAccessibilityNodeInfo.F());
        obtain.setPassword(obtainAccessibilityNodeInfo.E());
        obtain.setEnabled(obtainAccessibilityNodeInfo.A());
        obtain.setChecked(obtainAccessibilityNodeInfo.y());
        onPopulateEventForVirtualView(i10, obtain);
        if (obtain.getText().isEmpty() && obtain.getContentDescription() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()");
        }
        obtain.setClassName(obtainAccessibilityNodeInfo.m());
        AccessibilityRecordCompat.c(obtain, this.mHost, i10);
        obtain.setPackageName(this.mHost.getContext().getPackageName());
        return obtain;
    }

    private AccessibilityEvent createEventForHost(int i10) {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i10);
        this.mHost.onInitializeAccessibilityEvent(obtain);
        return obtain;
    }

    private AccessibilityNodeInfoCompat createNodeForChild(int i10) {
        AccessibilityNodeInfoCompat J = AccessibilityNodeInfoCompat.J();
        J.b0(true);
        J.d0(true);
        J.V(DEFAULT_CLASS_NAME);
        Rect rect = INVALID_PARENT_BOUNDS;
        J.Q(rect);
        J.R(rect);
        J.n0(this.mHost);
        onPopulateNodeForVirtualView(i10, J);
        if (J.s() == null && J.o() == null) {
            throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()");
        }
        J.j(this.mTempParentRect);
        if (!this.mTempParentRect.equals(rect)) {
            int i11 = J.i();
            if ((i11 & 64) != 0) {
                throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
            }
            if ((i11 & 128) == 0) {
                J.l0(this.mHost.getContext().getPackageName());
                J.w0(this.mHost, i10);
                if (this.mAccessibilityFocusedVirtualViewId == i10) {
                    J.P(true);
                    J.a(128);
                } else {
                    J.P(false);
                    J.a(64);
                }
                boolean z10 = this.mKeyboardFocusedVirtualViewId == i10;
                if (z10) {
                    J.a(2);
                } else if (J.B()) {
                    J.a(1);
                }
                J.e0(z10);
                this.mHost.getLocationOnScreen(this.mTempGlobalRect);
                J.k(this.mTempScreenRect);
                if (this.mTempScreenRect.equals(rect)) {
                    J.j(this.mTempScreenRect);
                    if (J.f2316b != -1) {
                        AccessibilityNodeInfoCompat J2 = AccessibilityNodeInfoCompat.J();
                        for (int i12 = J.f2316b; i12 != -1; i12 = J2.f2316b) {
                            J2.o0(this.mHost, -1);
                            J2.Q(INVALID_PARENT_BOUNDS);
                            onPopulateNodeForVirtualView(i12, J2);
                            J2.j(this.mTempParentRect);
                            Rect rect2 = this.mTempScreenRect;
                            Rect rect3 = this.mTempParentRect;
                            rect2.offset(rect3.left, rect3.top);
                        }
                        J2.N();
                    }
                    this.mTempScreenRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                }
                if (this.mHost.getLocalVisibleRect(this.mTempVisibleRect)) {
                    this.mTempVisibleRect.offset(this.mTempGlobalRect[0] - this.mHost.getScrollX(), this.mTempGlobalRect[1] - this.mHost.getScrollY());
                    if (this.mTempScreenRect.intersect(this.mTempVisibleRect)) {
                        J.R(this.mTempScreenRect);
                        if (isVisibleToUser(this.mTempScreenRect)) {
                            J.A0(true);
                        }
                    }
                }
                return J;
            }
            throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()");
        }
        throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()");
    }

    private AccessibilityNodeInfoCompat createNodeForHost() {
        AccessibilityNodeInfoCompat K = AccessibilityNodeInfoCompat.K(this.mHost);
        ViewCompat.Y(this.mHost, K);
        ArrayList arrayList = new ArrayList();
        getVisibleVirtualViews(arrayList);
        if (K.l() > 0 && arrayList.size() > 0) {
            throw new RuntimeException("Views cannot have both real and virtual children");
        }
        int size = arrayList.size();
        for (int i10 = 0; i10 < size; i10++) {
            K.d(this.mHost, ((Integer) arrayList.get(i10)).intValue());
        }
        return K;
    }

    private SparseArrayCompat<AccessibilityNodeInfoCompat> getAllNodes() {
        ArrayList arrayList = new ArrayList();
        getVisibleVirtualViews(arrayList);
        SparseArrayCompat<AccessibilityNodeInfoCompat> sparseArrayCompat = new SparseArrayCompat<>();
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            sparseArrayCompat.i(arrayList.get(i10).intValue(), createNodeForChild(arrayList.get(i10).intValue()));
        }
        return sparseArrayCompat;
    }

    private void getBoundsInParent(int i10, Rect rect) {
        obtainAccessibilityNodeInfo(i10).j(rect);
    }

    private static Rect guessPreviouslyFocusedRect(View view, int i10, Rect rect) {
        int width = view.getWidth();
        int height = view.getHeight();
        if (i10 == 17) {
            rect.set(width, 0, width, height);
        } else if (i10 == 33) {
            rect.set(0, height, width, height);
        } else if (i10 == 66) {
            rect.set(-1, 0, -1, height);
        } else if (i10 == 130) {
            rect.set(0, -1, width, -1);
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        return rect;
    }

    private boolean isVisibleToUser(Rect rect) {
        if (rect == null || rect.isEmpty() || this.mHost.getWindowVisibility() != 0) {
            return false;
        }
        Object parent = this.mHost.getParent();
        while (parent instanceof View) {
            View view = (View) parent;
            if (view.getAlpha() <= 0.0f || view.getVisibility() != 0) {
                return false;
            }
            parent = view.getParent();
        }
        return parent != null;
    }

    private static int keyToDirection(int i10) {
        if (i10 == 19) {
            return 33;
        }
        if (i10 != 21) {
            return i10 != 22 ? 130 : 66;
        }
        return 17;
    }

    private boolean moveFocus(int i10, Rect rect) {
        AccessibilityNodeInfoCompat accessibilityNodeInfoCompat;
        SparseArrayCompat<AccessibilityNodeInfoCompat> allNodes = getAllNodes();
        int i11 = this.mKeyboardFocusedVirtualViewId;
        AccessibilityNodeInfoCompat e10 = i11 == Integer.MIN_VALUE ? null : allNodes.e(i11);
        if (i10 == 1 || i10 == 2) {
            accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat) FocusStrategy.d(allNodes, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, e10, i10, ViewCompat.x(this.mHost) == 1, false);
        } else {
            if (i10 != 17 && i10 != 33 && i10 != 66 && i10 != 130) {
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD, FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            Rect rect2 = new Rect();
            int i12 = this.mKeyboardFocusedVirtualViewId;
            if (i12 != Integer.MIN_VALUE) {
                getBoundsInParent(i12, rect2);
            } else if (rect != null) {
                rect2.set(rect);
            } else {
                guessPreviouslyFocusedRect(this.mHost, i10, rect2);
            }
            accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat) FocusStrategy.c(allNodes, SPARSE_VALUES_ADAPTER, NODE_ADAPTER, e10, rect2, i10);
        }
        return requestKeyboardFocusForVirtualView(accessibilityNodeInfoCompat != null ? allNodes.h(allNodes.g(accessibilityNodeInfoCompat)) : Integer.MIN_VALUE);
    }

    private boolean performActionForChild(int i10, int i11, Bundle bundle) {
        if (i11 == 1) {
            return requestKeyboardFocusForVirtualView(i10);
        }
        if (i11 == 2) {
            return clearKeyboardFocusForVirtualView(i10);
        }
        if (i11 == 64) {
            return requestAccessibilityFocus(i10);
        }
        if (i11 != 128) {
            return onPerformActionForVirtualView(i10, i11, bundle);
        }
        return clearAccessibilityFocus(i10);
    }

    private boolean performActionForHost(int i10, Bundle bundle) {
        return ViewCompat.a0(this.mHost, i10, bundle);
    }

    private boolean requestAccessibilityFocus(int i10) {
        int i11;
        if (!this.mManager.isEnabled() || !this.mManager.isTouchExplorationEnabled() || (i11 = this.mAccessibilityFocusedVirtualViewId) == i10) {
            return false;
        }
        if (i11 != Integer.MIN_VALUE) {
            clearAccessibilityFocus(i11);
        }
        this.mAccessibilityFocusedVirtualViewId = i10;
        this.mHost.invalidate();
        sendEventForVirtualView(i10, 32768);
        return true;
    }

    private void updateHoveredVirtualView(int i10) {
        int i11 = this.mHoveredVirtualViewId;
        if (i11 == i10) {
            return;
        }
        this.mHoveredVirtualViewId = i10;
        sendEventForVirtualView(i10, 128);
        sendEventForVirtualView(i11, 256);
    }

    public final boolean clearKeyboardFocusForVirtualView(int i10) {
        if (this.mKeyboardFocusedVirtualViewId != i10) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = Integer.MIN_VALUE;
        onVirtualViewKeyboardFocusChanged(i10, false);
        sendEventForVirtualView(i10, 8);
        return true;
    }

    public final boolean dispatchHoverEvent(MotionEvent motionEvent) {
        if (!this.mManager.isEnabled() || !this.mManager.isTouchExplorationEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action != 7 && action != 9) {
            if (action != 10 || this.mHoveredVirtualViewId == Integer.MIN_VALUE) {
                return false;
            }
            updateHoveredVirtualView(Integer.MIN_VALUE);
            return true;
        }
        int virtualViewAt = getVirtualViewAt(motionEvent.getX(), motionEvent.getY());
        updateHoveredVirtualView(virtualViewAt);
        return virtualViewAt != Integer.MIN_VALUE;
    }

    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int i10 = 0;
        if (keyEvent.getAction() == 1) {
            return false;
        }
        int keyCode = keyEvent.getKeyCode();
        if (keyCode != 61) {
            if (keyCode != 66) {
                switch (keyCode) {
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                        if (!keyEvent.hasNoModifiers()) {
                            return false;
                        }
                        int keyToDirection = keyToDirection(keyCode);
                        int repeatCount = keyEvent.getRepeatCount() + 1;
                        boolean z10 = false;
                        while (i10 < repeatCount && moveFocus(keyToDirection, null)) {
                            i10++;
                            z10 = true;
                        }
                        return z10;
                    case 23:
                        break;
                    default:
                        return false;
                }
            }
            if (!keyEvent.hasNoModifiers() || keyEvent.getRepeatCount() != 0) {
                return false;
            }
            clickKeyboardFocusedVirtualView();
            return true;
        }
        if (keyEvent.hasNoModifiers()) {
            return moveFocus(2, null);
        }
        if (keyEvent.hasModifiers(1)) {
            return moveFocus(1, null);
        }
        return false;
    }

    public final int getAccessibilityFocusedVirtualViewId() {
        return this.mAccessibilityFocusedVirtualViewId;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        if (this.mNodeProvider == null) {
            this.mNodeProvider = new c();
        }
        return this.mNodeProvider;
    }

    @Deprecated
    public int getFocusedVirtualView() {
        return getAccessibilityFocusedVirtualViewId();
    }

    public final int getKeyboardFocusedVirtualViewId() {
        return this.mKeyboardFocusedVirtualViewId;
    }

    protected abstract int getVirtualViewAt(float f10, float f11);

    protected abstract void getVisibleVirtualViews(List<Integer> list);

    public final void invalidateRoot() {
        invalidateVirtualView(-1, 1);
    }

    public final void invalidateVirtualView(int i10) {
        invalidateVirtualView(i10, 0);
    }

    AccessibilityNodeInfoCompat obtainAccessibilityNodeInfo(int i10) {
        if (i10 == -1) {
            return createNodeForHost();
        }
        return createNodeForChild(i10);
    }

    public final void onFocusChanged(boolean z10, int i10, Rect rect) {
        int i11 = this.mKeyboardFocusedVirtualViewId;
        if (i11 != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i11);
        }
        if (z10) {
            moveFocus(i10, rect);
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(view, accessibilityEvent);
        onPopulateEventForHost(accessibilityEvent);
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        onPopulateNodeForHost(accessibilityNodeInfoCompat);
    }

    protected abstract boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle);

    protected void onPopulateEventForHost(AccessibilityEvent accessibilityEvent) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
    }

    protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
    }

    protected abstract void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

    protected void onVirtualViewKeyboardFocusChanged(int i10, boolean z10) {
    }

    boolean performAction(int i10, int i11, Bundle bundle) {
        if (i10 != -1) {
            return performActionForChild(i10, i11, bundle);
        }
        return performActionForHost(i11, bundle);
    }

    public final boolean requestKeyboardFocusForVirtualView(int i10) {
        int i11;
        if ((!this.mHost.isFocused() && !this.mHost.requestFocus()) || (i11 = this.mKeyboardFocusedVirtualViewId) == i10) {
            return false;
        }
        if (i11 != Integer.MIN_VALUE) {
            clearKeyboardFocusForVirtualView(i11);
        }
        if (i10 == Integer.MIN_VALUE) {
            return false;
        }
        this.mKeyboardFocusedVirtualViewId = i10;
        onVirtualViewKeyboardFocusChanged(i10, true);
        sendEventForVirtualView(i10, 8);
        return true;
    }

    public final boolean sendEventForVirtualView(int i10, int i11) {
        ViewParent parent;
        if (i10 == Integer.MIN_VALUE || !this.mManager.isEnabled() || (parent = this.mHost.getParent()) == null) {
            return false;
        }
        return parent.requestSendAccessibilityEvent(this.mHost, createEvent(i10, i11));
    }

    public final void invalidateVirtualView(int i10, int i11) {
        ViewParent parent;
        if (i10 == Integer.MIN_VALUE || !this.mManager.isEnabled() || (parent = this.mHost.getParent()) == null) {
            return;
        }
        AccessibilityEvent createEvent = createEvent(i10, 2048);
        AccessibilityEventCompat.b(createEvent, i11);
        parent.requestSendAccessibilityEvent(this.mHost, createEvent);
    }
}
