package androidx.core.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;

/* compiled from: AccessibilityNodeProviderCompat.java */
/* renamed from: androidx.core.view.accessibility.e, reason: use source file name */
/* loaded from: classes.dex */
public class AccessibilityNodeProviderCompat {

    /* renamed from: a, reason: collision with root package name */
    private final Object f2347a;

    /* compiled from: AccessibilityNodeProviderCompat.java */
    /* renamed from: androidx.core.view.accessibility.e$a */
    /* loaded from: classes.dex */
    static class a extends AccessibilityNodeProvider {

        /* renamed from: a, reason: collision with root package name */
        final AccessibilityNodeProviderCompat f2348a;

        a(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            this.f2348a = accessibilityNodeProviderCompat;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i10) {
            AccessibilityNodeInfoCompat b10 = this.f2348a.b(i10);
            if (b10 == null) {
                return null;
            }
            return b10.B0();
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i10) {
            List<AccessibilityNodeInfoCompat> c10 = this.f2348a.c(str, i10);
            if (c10 == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            int size = c10.size();
            for (int i11 = 0; i11 < size; i11++) {
                arrayList.add(c10.get(i11).B0());
            }
            return arrayList;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public boolean performAction(int i10, int i11, Bundle bundle) {
            return this.f2348a.f(i10, i11, bundle);
        }
    }

    /* compiled from: AccessibilityNodeProviderCompat.java */
    /* renamed from: androidx.core.view.accessibility.e$b */
    /* loaded from: classes.dex */
    static class b extends a {
        b(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            super(accessibilityNodeProviderCompat);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo findFocus(int i10) {
            AccessibilityNodeInfoCompat d10 = this.f2348a.d(i10);
            if (d10 == null) {
                return null;
            }
            return d10.B0();
        }
    }

    /* compiled from: AccessibilityNodeProviderCompat.java */
    /* renamed from: androidx.core.view.accessibility.e$c */
    /* loaded from: classes.dex */
    static class c extends b {
        c(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            super(accessibilityNodeProviderCompat);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public void addExtraDataToAccessibilityNodeInfo(int i10, AccessibilityNodeInfo accessibilityNodeInfo, String str, Bundle bundle) {
            this.f2348a.a(i10, AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo), str, bundle);
        }
    }

    public AccessibilityNodeProviderCompat() {
        this.f2347a = new c(this);
    }

    public void a(int i10, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, String str, Bundle bundle) {
    }

    public AccessibilityNodeInfoCompat b(int i10) {
        return null;
    }

    public List<AccessibilityNodeInfoCompat> c(String str, int i10) {
        return null;
    }

    public AccessibilityNodeInfoCompat d(int i10) {
        return null;
    }

    public Object e() {
        return this.f2347a;
    }

    public boolean f(int i10, int i11, Bundle bundle) {
        return false;
    }

    public AccessibilityNodeProviderCompat(Object obj) {
        this.f2347a = obj;
    }
}
