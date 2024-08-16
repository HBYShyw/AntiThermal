package androidx.appcompat.app;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ComponentDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R$attr;
import androidx.appcompat.view.ActionMode;
import androidx.core.view.KeyEventDispatcher;

/* compiled from: AppCompatDialog.java */
/* renamed from: androidx.appcompat.app.h, reason: use source file name */
/* loaded from: classes.dex */
public class AppCompatDialog extends ComponentDialog implements AppCompatCallback {

    /* renamed from: g, reason: collision with root package name */
    private AppCompatDelegate f484g;

    /* renamed from: h, reason: collision with root package name */
    private final KeyEventDispatcher.a f485h;

    public AppCompatDialog(Context context, int i10) {
        super(context, f(context, i10));
        this.f485h = new KeyEventDispatcher.a() { // from class: androidx.appcompat.app.g
            @Override // androidx.core.view.KeyEventDispatcher.a
            public final boolean superDispatchKeyEvent(KeyEvent keyEvent) {
                return AppCompatDialog.this.g(keyEvent);
            }
        };
        AppCompatDelegate e10 = e();
        e10.N(f(context, i10));
        e10.y(null);
    }

    private static int f(Context context, int i10) {
        if (i10 != 0) {
            return i10;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R$attr.dialogTheme, typedValue, true);
        return typedValue.resourceId;
    }

    @Override // android.app.Dialog
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        e().e(view, layoutParams);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        e().z();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return KeyEventDispatcher.b(this.f485h, getWindow().getDecorView(), this, keyEvent);
    }

    public AppCompatDelegate e() {
        if (this.f484g == null) {
            this.f484g = AppCompatDelegate.i(this, this);
        }
        return this.f484g;
    }

    @Override // android.app.Dialog
    public <T extends View> T findViewById(int i10) {
        return (T) e().j(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean h(int i10) {
        return e().H(i10);
    }

    @Override // android.app.Dialog
    public void invalidateOptionsMenu() {
        e().u();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ComponentDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        e().t();
        super.onCreate(bundle);
        e().y(bundle);
    }

    @Override // android.view.ComponentDialog, android.app.Dialog
    protected void onStop() {
        super.onStop();
        e().E();
    }

    @Override // androidx.appcompat.app.AppCompatCallback
    public void onSupportActionModeFinished(ActionMode actionMode) {
    }

    @Override // androidx.appcompat.app.AppCompatCallback
    public void onSupportActionModeStarted(ActionMode actionMode) {
    }

    @Override // androidx.appcompat.app.AppCompatCallback
    public ActionMode onWindowStartingSupportActionMode(ActionMode.a aVar) {
        return null;
    }

    @Override // android.app.Dialog
    public void setContentView(int i10) {
        e().I(i10);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        e().O(charSequence);
    }

    @Override // android.app.Dialog
    public void setContentView(View view) {
        e().J(view);
    }

    @Override // android.app.Dialog
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        e().K(view, layoutParams);
    }

    @Override // android.app.Dialog
    public void setTitle(int i10) {
        super.setTitle(i10);
        e().O(getContext().getString(i10));
    }
}
