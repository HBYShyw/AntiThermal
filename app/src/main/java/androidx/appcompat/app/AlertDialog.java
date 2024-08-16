package androidx.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.R$attr;
import androidx.appcompat.app.AlertController;

/* compiled from: AlertDialog.java */
/* renamed from: androidx.appcompat.app.b, reason: use source file name */
/* loaded from: classes.dex */
public class AlertDialog extends AppCompatDialog {

    /* renamed from: i, reason: collision with root package name */
    final AlertController f469i;

    /* compiled from: AlertDialog.java */
    /* renamed from: androidx.appcompat.app.b$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private final AlertController.b f470a;

        /* renamed from: b, reason: collision with root package name */
        private final int f471b;

        public a(Context context) {
            this(context, AlertDialog.k(context, 0));
        }

        public AlertDialog a() {
            AlertDialog alertDialog = new AlertDialog(this.f470a.f350a, this.f471b);
            this.f470a.a(alertDialog.f469i);
            alertDialog.setCancelable(this.f470a.f367r);
            if (this.f470a.f367r) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.f470a.f368s);
            alertDialog.setOnDismissListener(this.f470a.f369t);
            DialogInterface.OnKeyListener onKeyListener = this.f470a.f370u;
            if (onKeyListener != null) {
                alertDialog.setOnKeyListener(onKeyListener);
            }
            return alertDialog;
        }

        public Context b() {
            return this.f470a.f350a;
        }

        public a c(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f372w = listAdapter;
            bVar.f373x = onClickListener;
            return this;
        }

        public a d(boolean z10) {
            this.f470a.f367r = z10;
            return this;
        }

        public a e(View view) {
            this.f470a.f356g = view;
            return this;
        }

        public a f(Drawable drawable) {
            this.f470a.f353d = drawable;
            return this;
        }

        public a g(int i10) {
            AlertController.b bVar = this.f470a;
            bVar.f357h = bVar.f350a.getText(i10);
            return this;
        }

        public a h(CharSequence charSequence) {
            this.f470a.f357h = charSequence;
            return this;
        }

        public a i(CharSequence[] charSequenceArr, boolean[] zArr, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f371v = charSequenceArr;
            bVar.J = onMultiChoiceClickListener;
            bVar.F = zArr;
            bVar.G = true;
            return this;
        }

        public a j(int i10, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f361l = bVar.f350a.getText(i10);
            this.f470a.f363n = onClickListener;
            return this;
        }

        public a k(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f361l = charSequence;
            bVar.f363n = onClickListener;
            return this;
        }

        public a l(int i10, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f364o = bVar.f350a.getText(i10);
            this.f470a.f366q = onClickListener;
            return this;
        }

        public a m(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f364o = charSequence;
            bVar.f366q = onClickListener;
            return this;
        }

        public a n(DialogInterface.OnKeyListener onKeyListener) {
            this.f470a.f370u = onKeyListener;
            return this;
        }

        public a o(int i10, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f358i = bVar.f350a.getText(i10);
            this.f470a.f360k = onClickListener;
            return this;
        }

        public a p(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f358i = charSequence;
            bVar.f360k = onClickListener;
            return this;
        }

        public a q(ListAdapter listAdapter, int i10, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f372w = listAdapter;
            bVar.f373x = onClickListener;
            bVar.I = i10;
            bVar.H = true;
            return this;
        }

        public a r(CharSequence[] charSequenceArr, int i10, DialogInterface.OnClickListener onClickListener) {
            AlertController.b bVar = this.f470a;
            bVar.f371v = charSequenceArr;
            bVar.f373x = onClickListener;
            bVar.I = i10;
            bVar.H = true;
            return this;
        }

        public a s(int i10) {
            AlertController.b bVar = this.f470a;
            bVar.f355f = bVar.f350a.getText(i10);
            return this;
        }

        public a t(CharSequence charSequence) {
            this.f470a.f355f = charSequence;
            return this;
        }

        public a u(int i10) {
            AlertController.b bVar = this.f470a;
            bVar.f375z = null;
            bVar.f374y = i10;
            bVar.E = false;
            return this;
        }

        public a v(View view) {
            AlertController.b bVar = this.f470a;
            bVar.f375z = view;
            bVar.f374y = 0;
            bVar.E = false;
            return this;
        }

        public AlertDialog w() {
            AlertDialog a10 = a();
            a10.show();
            return a10;
        }

        public a(Context context, int i10) {
            this.f470a = new AlertController.b(new ContextThemeWrapper(context, AlertDialog.k(context, i10)));
            this.f471b = i10;
        }
    }

    protected AlertDialog(Context context, int i10) {
        super(context, k(context, i10));
        this.f469i = new AlertController(getContext(), this, getWindow());
    }

    static int k(Context context, int i10) {
        if (((i10 >>> 24) & 255) >= 1) {
            return i10;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R$attr.alertDialogTheme, typedValue, true);
        return typedValue.resourceId;
    }

    public Button i(int i10) {
        return this.f469i.c(i10);
    }

    public ListView j() {
        return this.f469i.e();
    }

    public void l(CharSequence charSequence) {
        this.f469i.o(charSequence);
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.view.ComponentDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f469i.f();
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i10, KeyEvent keyEvent) {
        if (this.f469i.g(i10, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i10, keyEvent);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i10, KeyEvent keyEvent) {
        if (this.f469i.h(i10, keyEvent)) {
            return true;
        }
        return super.onKeyUp(i10, keyEvent);
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.f469i.q(charSequence);
    }
}
