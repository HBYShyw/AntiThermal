package androidx.preference;

import android.os.Bundle;
import android.view.View;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

/* compiled from: PreferenceRecyclerViewAccessibilityDelegate.java */
@Deprecated
/* renamed from: androidx.preference.k, reason: use source file name */
/* loaded from: classes.dex */
public class PreferenceRecyclerViewAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {

    /* renamed from: a, reason: collision with root package name */
    final RecyclerView f3357a;

    /* renamed from: b, reason: collision with root package name */
    final AccessibilityDelegateCompat f3358b;

    /* renamed from: c, reason: collision with root package name */
    final AccessibilityDelegateCompat f3359c;

    /* compiled from: PreferenceRecyclerViewAccessibilityDelegate.java */
    /* renamed from: androidx.preference.k$a */
    /* loaded from: classes.dex */
    class a extends AccessibilityDelegateCompat {
        a() {
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            Preference item;
            PreferenceRecyclerViewAccessibilityDelegate.this.f3358b.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            int childAdapterPosition = PreferenceRecyclerViewAccessibilityDelegate.this.f3357a.getChildAdapterPosition(view);
            RecyclerView.h adapter = PreferenceRecyclerViewAccessibilityDelegate.this.f3357a.getAdapter();
            if ((adapter instanceof PreferenceGroupAdapter) && (item = ((PreferenceGroupAdapter) adapter).getItem(childAdapterPosition)) != null) {
                item.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public boolean performAccessibilityAction(View view, int i10, Bundle bundle) {
            return PreferenceRecyclerViewAccessibilityDelegate.this.f3358b.performAccessibilityAction(view, i10, bundle);
        }
    }

    public PreferenceRecyclerViewAccessibilityDelegate(RecyclerView recyclerView) {
        super(recyclerView);
        this.f3358b = super.getItemDelegate();
        this.f3359c = new a();
        this.f3357a = recyclerView;
    }

    @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
    public AccessibilityDelegateCompat getItemDelegate() {
        return this.f3359c;
    }
}
