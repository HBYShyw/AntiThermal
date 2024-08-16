package g3;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import java.util.List;

/* compiled from: COUIViewExplorerByTouchHelper.java */
/* renamed from: g3.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIViewExplorerByTouchHelper extends ExploreByTouchHelper {

    /* renamed from: a, reason: collision with root package name */
    private final Rect f11569a;

    /* renamed from: b, reason: collision with root package name */
    private View f11570b;

    /* renamed from: c, reason: collision with root package name */
    private a f11571c;

    /* compiled from: COUIViewExplorerByTouchHelper.java */
    /* renamed from: g3.a$a */
    /* loaded from: classes.dex */
    public interface a {
        void a(int i10, Rect rect);

        CharSequence b(int i10);

        CharSequence c();

        int d();

        int e();

        void f(int i10, int i11, boolean z10);

        int g(float f10, float f11);

        int h();
    }

    public COUIViewExplorerByTouchHelper(View view) {
        super(view);
        this.f11569a = new Rect();
        this.f11571c = null;
        this.f11570b = view;
    }

    private void b(int i10, Rect rect) {
        if (i10 < 0 || i10 >= this.f11571c.e()) {
            return;
        }
        this.f11571c.a(i10, rect);
    }

    public void a() {
        int focusedVirtualView = getFocusedVirtualView();
        if (focusedVirtualView != Integer.MIN_VALUE) {
            getAccessibilityNodeProvider(this.f11570b).f(focusedVirtualView, 128, null);
        }
    }

    public void c(a aVar) {
        this.f11571c = aVar;
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected int getVirtualViewAt(float f10, float f11) {
        int g6 = this.f11571c.g(f10, f11);
        if (g6 >= 0) {
            return g6;
        }
        return Integer.MIN_VALUE;
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected void getVisibleVirtualViews(List<Integer> list) {
        for (int i10 = 0; i10 < this.f11571c.e(); i10++) {
            list.add(Integer.valueOf(i10));
        }
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
        if (i11 != 16) {
            return false;
        }
        this.f11571c.f(i10, 16, false);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.customview.widget.ExploreByTouchHelper
    public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
        accessibilityEvent.setContentDescription(this.f11571c.b(i10));
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        b(i10, this.f11569a);
        accessibilityNodeInfoCompat.Z(this.f11571c.b(i10));
        accessibilityNodeInfoCompat.Q(this.f11569a);
        if (this.f11571c.c() != null) {
            accessibilityNodeInfoCompat.V(this.f11571c.c());
        }
        accessibilityNodeInfoCompat.a(16);
        if (i10 == this.f11571c.h()) {
            accessibilityNodeInfoCompat.t0(true);
        }
        if (i10 == this.f11571c.d()) {
            accessibilityNodeInfoCompat.b0(false);
        }
    }
}
