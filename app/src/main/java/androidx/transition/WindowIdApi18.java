package androidx.transition;

import android.view.View;
import android.view.WindowId;

/* compiled from: WindowIdApi18.java */
/* renamed from: androidx.transition.l0, reason: use source file name */
/* loaded from: classes.dex */
class WindowIdApi18 implements WindowIdImpl {

    /* renamed from: a, reason: collision with root package name */
    private final WindowId f4122a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowIdApi18(View view) {
        this.f4122a = view.getWindowId();
    }

    public boolean equals(Object obj) {
        return (obj instanceof WindowIdApi18) && ((WindowIdApi18) obj).f4122a.equals(this.f4122a);
    }

    public int hashCode() {
        return this.f4122a.hashCode();
    }
}
