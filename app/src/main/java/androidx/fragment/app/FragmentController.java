package androidx.fragment.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.core.util.Preconditions;
import androidx.lifecycle.ViewModelStoreOwner;

/* compiled from: FragmentController.java */
/* renamed from: androidx.fragment.app.f, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentController {

    /* renamed from: a, reason: collision with root package name */
    private final FragmentHostCallback<?> f2898a;

    private FragmentController(FragmentHostCallback<?> fragmentHostCallback) {
        this.f2898a = fragmentHostCallback;
    }

    public static FragmentController b(FragmentHostCallback<?> fragmentHostCallback) {
        return new FragmentController((FragmentHostCallback) Preconditions.e(fragmentHostCallback, "callbacks == null"));
    }

    public void a(Fragment fragment) {
        FragmentHostCallback<?> fragmentHostCallback = this.f2898a;
        fragmentHostCallback.f2904i.k(fragmentHostCallback, fragmentHostCallback, fragment);
    }

    public void c() {
        this.f2898a.f2904i.z();
    }

    public void d(Configuration configuration) {
        this.f2898a.f2904i.B(configuration);
    }

    public boolean e(MenuItem menuItem) {
        return this.f2898a.f2904i.C(menuItem);
    }

    public void f() {
        this.f2898a.f2904i.D();
    }

    public boolean g(Menu menu, MenuInflater menuInflater) {
        return this.f2898a.f2904i.E(menu, menuInflater);
    }

    public void h() {
        this.f2898a.f2904i.F();
    }

    public void i() {
        this.f2898a.f2904i.H();
    }

    public void j(boolean z10) {
        this.f2898a.f2904i.I(z10);
    }

    public boolean k(MenuItem menuItem) {
        return this.f2898a.f2904i.K(menuItem);
    }

    public void l(Menu menu) {
        this.f2898a.f2904i.L(menu);
    }

    public void m() {
        this.f2898a.f2904i.N();
    }

    public void n(boolean z10) {
        this.f2898a.f2904i.O(z10);
    }

    public boolean o(Menu menu) {
        return this.f2898a.f2904i.P(menu);
    }

    public void p() {
        this.f2898a.f2904i.R();
    }

    public void q() {
        this.f2898a.f2904i.S();
    }

    public void r() {
        this.f2898a.f2904i.U();
    }

    public boolean s() {
        return this.f2898a.f2904i.b0(true);
    }

    public FragmentManager t() {
        return this.f2898a.f2904i;
    }

    public void u() {
        this.f2898a.f2904i.V0();
    }

    public View v(View view, String str, Context context, AttributeSet attributeSet) {
        return this.f2898a.f2904i.w0().onCreateView(view, str, context, attributeSet);
    }

    public void w(Parcelable parcelable) {
        FragmentHostCallback<?> fragmentHostCallback = this.f2898a;
        if (fragmentHostCallback instanceof ViewModelStoreOwner) {
            fragmentHostCallback.f2904i.k1(parcelable);
            return;
        }
        throw new IllegalStateException("Your FragmentHostCallback must implement ViewModelStoreOwner to call restoreSaveState(). Call restoreAllState()  if you're still using retainNestedNonConfig().");
    }

    public Parcelable x() {
        return this.f2898a.f2904i.m1();
    }
}
