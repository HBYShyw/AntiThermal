package androidx.appcompat.widget;

import android.view.Menu;
import android.view.Window;
import androidx.appcompat.view.menu.MenuPresenter;

/* compiled from: DecorContentParent.java */
/* renamed from: androidx.appcompat.widget.r, reason: use source file name */
/* loaded from: classes.dex */
public interface DecorContentParent {
    void a(Menu menu, MenuPresenter.a aVar);

    boolean b();

    void c();

    boolean d();

    boolean e();

    boolean f();

    boolean g();

    void h(int i10);

    void i();

    void setWindowCallback(Window.Callback callback);

    void setWindowTitle(CharSequence charSequence);
}
