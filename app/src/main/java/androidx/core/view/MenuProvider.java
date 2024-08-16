package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/* compiled from: MenuProvider.java */
/* renamed from: androidx.core.view.m, reason: use source file name */
/* loaded from: classes.dex */
public interface MenuProvider {
    boolean a(MenuItem menuItem);

    default void b(Menu menu) {
    }

    void c(Menu menu, MenuInflater menuInflater);

    default void d(Menu menu) {
    }
}
