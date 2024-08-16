package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.core.view.ViewPropertyAnimatorCompat;

/* compiled from: DecorToolbar.java */
/* renamed from: androidx.appcompat.widget.s, reason: use source file name */
/* loaded from: classes.dex */
public interface DecorToolbar {
    void a(Menu menu, MenuPresenter.a aVar);

    boolean b();

    void c();

    void collapseActionView();

    boolean d();

    boolean e();

    boolean f();

    boolean g();

    Context getContext();

    CharSequence getTitle();

    void h();

    void i(ScrollingTabContainerView scrollingTabContainerView);

    boolean j();

    void k(int i10);

    Menu l();

    void m(int i10);

    int n();

    ViewPropertyAnimatorCompat o(int i10, long j10);

    void p(MenuPresenter.a aVar, MenuBuilder.a aVar2);

    ViewGroup q();

    void r(boolean z10);

    int s();

    void setIcon(int i10);

    void setIcon(Drawable drawable);

    void setVisibility(int i10);

    void setWindowCallback(Window.Callback callback);

    void setWindowTitle(CharSequence charSequence);

    void t();

    void u();

    void v(boolean z10);
}
