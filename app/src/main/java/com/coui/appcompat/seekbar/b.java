package com.coui.appcompat.seekbar;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import java.lang.ref.WeakReference;
import java.util.List;

/* compiled from: PatternExploreByTouchHelper.java */
/* loaded from: classes.dex */
public class b extends ExploreByTouchHelper {

    /* renamed from: a, reason: collision with root package name */
    private WeakReference<COUIIconSeekBar> f7611a;

    public b(COUIIconSeekBar cOUIIconSeekBar) {
        super(cOUIIconSeekBar);
        this.f7611a = new WeakReference<>(cOUIIconSeekBar);
    }

    private COUIIconSeekBar a() {
        return this.f7611a.get();
    }

    private Rect b(int i10) {
        Rect rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = a().getWidth();
        rect.bottom = a().getHeight();
        return rect;
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected int getVirtualViewAt(float f10, float f11) {
        return (f10 < 0.0f || f10 > ((float) a().getWidth()) || f11 < 0.0f || f11 > ((float) a().getHeight())) ? -1 : 0;
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected void getVisibleVirtualViews(List<Integer> list) {
        for (int i10 = 0; i10 < 1; i10++) {
            list.add(Integer.valueOf(i10));
        }
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper, androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.b(AccessibilityNodeInfoCompat.a.L);
        accessibilityNodeInfoCompat.p0(AccessibilityNodeInfoCompat.d.a(1, 0.0f, a().getMax(), a().getProgress()));
        if (this.f7611a.get().isEnabled()) {
            int progress = a().getProgress();
            if (progress > 0) {
                accessibilityNodeInfoCompat.a(8192);
            }
            if (progress < a().getMax()) {
                accessibilityNodeInfoCompat.a(4096);
            }
        }
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected boolean onPerformActionForVirtualView(int i10, int i11, Bundle bundle) {
        sendEventForVirtualView(i10, 4);
        return false;
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.customview.widget.ExploreByTouchHelper
    public void onPopulateEventForVirtualView(int i10, AccessibilityEvent accessibilityEvent) {
        accessibilityEvent.getText().add(getClass().getSimpleName());
        accessibilityEvent.setItemCount(a().getMax());
        accessibilityEvent.setCurrentItemIndex(a().getProgress());
    }

    @Override // androidx.customview.widget.ExploreByTouchHelper
    protected void onPopulateNodeForVirtualView(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        accessibilityNodeInfoCompat.Z("");
        accessibilityNodeInfoCompat.V(COUIIconSeekBar.class.getName());
        accessibilityNodeInfoCompat.Q(b(i10));
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public boolean performAccessibilityAction(View view, int i10, Bundle bundle) {
        if (super.performAccessibilityAction(view, i10, bundle)) {
            return true;
        }
        if (!a().isEnabled()) {
            return false;
        }
        if (i10 == 4096) {
            a().x(a().getProgress() + a().getIncrement(), false, true);
            a().announceForAccessibility(a().getProgressContentDescription());
            return true;
        }
        if (i10 != 8192) {
            return false;
        }
        a().x(a().getProgress() - a().getIncrement(), false, true);
        a().announceForAccessibility(a().getProgressContentDescription());
        return true;
    }
}
