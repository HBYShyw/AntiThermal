package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* compiled from: FragmentHostCallback.java */
/* renamed from: androidx.fragment.app.h, reason: use source file name */
/* loaded from: classes.dex */
public abstract class FragmentHostCallback<E> extends FragmentContainer {

    /* renamed from: e, reason: collision with root package name */
    private final Activity f2900e;

    /* renamed from: f, reason: collision with root package name */
    private final Context f2901f;

    /* renamed from: g, reason: collision with root package name */
    private final Handler f2902g;

    /* renamed from: h, reason: collision with root package name */
    private final int f2903h;

    /* renamed from: i, reason: collision with root package name */
    final FragmentManager f2904i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentHostCallback(FragmentActivity fragmentActivity) {
        this(fragmentActivity, fragmentActivity, new Handler(), 0);
    }

    @Override // androidx.fragment.app.FragmentContainer
    public View c(int i10) {
        return null;
    }

    @Override // androidx.fragment.app.FragmentContainer
    public boolean d() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Activity e() {
        return this.f2900e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context f() {
        return this.f2901f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Handler g() {
        return this.f2902g;
    }

    public void h(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public abstract E i();

    public LayoutInflater j() {
        return LayoutInflater.from(this.f2901f);
    }

    @Deprecated
    public void k(Fragment fragment, String[] strArr, int i10) {
    }

    public boolean l(Fragment fragment) {
        return true;
    }

    public boolean m(String str) {
        return false;
    }

    public void n(Fragment fragment, @SuppressLint({"UnknownNullness"}) Intent intent, int i10, Bundle bundle) {
        if (i10 == -1) {
            ContextCompat.i(this.f2901f, intent, bundle);
            return;
        }
        throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
    }

    @Deprecated
    public void o(Fragment fragment, @SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i10, Intent intent, int i11, int i12, int i13, Bundle bundle) {
        if (i10 == -1) {
            ActivityCompat.s(this.f2900e, intentSender, i10, intent, i11, i12, i13, bundle);
            return;
        }
        throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
    }

    public void p() {
    }

    FragmentHostCallback(Activity activity, Context context, Handler handler, int i10) {
        this.f2904i = new FragmentManagerImpl();
        this.f2900e = activity;
        this.f2901f = (Context) Preconditions.e(context, "context == null");
        this.f2902g = (Handler) Preconditions.e(handler, "handler == null");
        this.f2903h = i10;
    }
}
