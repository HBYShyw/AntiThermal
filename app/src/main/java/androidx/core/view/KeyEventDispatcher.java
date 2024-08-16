package androidx.core.view;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/* compiled from: KeyEventDispatcher.java */
/* renamed from: androidx.core.view.f, reason: use source file name */
/* loaded from: classes.dex */
public class KeyEventDispatcher {

    /* compiled from: KeyEventDispatcher.java */
    /* renamed from: androidx.core.view.f$a */
    /* loaded from: classes.dex */
    public interface a {
        boolean superDispatchKeyEvent(KeyEvent keyEvent);
    }

    public static boolean a(View view, KeyEvent keyEvent) {
        return ViewCompat.g(view, keyEvent);
    }

    @SuppressLint({"LambdaLast"})
    public static boolean b(a aVar, View view, Window.Callback callback, KeyEvent keyEvent) {
        if (aVar == null) {
            return false;
        }
        return aVar.superDispatchKeyEvent(keyEvent);
    }
}
