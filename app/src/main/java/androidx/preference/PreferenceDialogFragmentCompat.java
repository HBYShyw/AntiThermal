package androidx.preference;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.DialogPreference;

/* compiled from: PreferenceDialogFragmentCompat.java */
/* renamed from: androidx.preference.f, reason: use source file name */
/* loaded from: classes.dex */
public abstract class PreferenceDialogFragmentCompat extends DialogFragment implements DialogInterface.OnClickListener {
    private BitmapDrawable A;
    private int B;

    /* renamed from: u, reason: collision with root package name */
    private DialogPreference f3312u;

    /* renamed from: v, reason: collision with root package name */
    private CharSequence f3313v;

    /* renamed from: w, reason: collision with root package name */
    private CharSequence f3314w;

    /* renamed from: x, reason: collision with root package name */
    private CharSequence f3315x;

    /* renamed from: y, reason: collision with root package name */
    private CharSequence f3316y;

    /* renamed from: z, reason: collision with root package name */
    private int f3317z;

    private void x0(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(5);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog k0(Bundle bundle) {
        FragmentActivity activity = getActivity();
        this.B = -2;
        AlertDialog.a k10 = new AlertDialog.a(activity).t(this.f3313v).f(this.A).p(this.f3314w, this).k(this.f3315x, this);
        View u02 = u0(activity);
        if (u02 != null) {
            t0(u02);
            k10.v(u02);
        } else {
            k10.h(this.f3316y);
        }
        w0(k10);
        AlertDialog a10 = k10.a();
        if (s0()) {
            x0(a10);
        }
        return a10;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i10) {
        this.B = i10;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ViewModelStoreOwner targetFragment = getTargetFragment();
        if (targetFragment instanceof DialogPreference.a) {
            DialogPreference.a aVar = (DialogPreference.a) targetFragment;
            String string = getArguments().getString("key");
            if (bundle == null) {
                DialogPreference dialogPreference = (DialogPreference) aVar.findPreference(string);
                this.f3312u = dialogPreference;
                this.f3313v = dialogPreference.f();
                this.f3314w = this.f3312u.h();
                this.f3315x = this.f3312u.g();
                this.f3316y = this.f3312u.e();
                this.f3317z = this.f3312u.d();
                Drawable c10 = this.f3312u.c();
                if (c10 != null && !(c10 instanceof BitmapDrawable)) {
                    Bitmap createBitmap = Bitmap.createBitmap(c10.getIntrinsicWidth(), c10.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    c10.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    c10.draw(canvas);
                    this.A = new BitmapDrawable(getResources(), createBitmap);
                    return;
                }
                this.A = (BitmapDrawable) c10;
                return;
            }
            this.f3313v = bundle.getCharSequence("PreferenceDialogFragment.title");
            this.f3314w = bundle.getCharSequence("PreferenceDialogFragment.positiveText");
            this.f3315x = bundle.getCharSequence("PreferenceDialogFragment.negativeText");
            this.f3316y = bundle.getCharSequence("PreferenceDialogFragment.message");
            this.f3317z = bundle.getInt("PreferenceDialogFragment.layout", 0);
            Bitmap bitmap = (Bitmap) bundle.getParcelable("PreferenceDialogFragment.icon");
            if (bitmap != null) {
                this.A = new BitmapDrawable(getResources(), bitmap);
                return;
            }
            return;
        }
        throw new IllegalStateException("Target fragment must implement TargetFragment interface");
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        v0(this.B == -1);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence("PreferenceDialogFragment.title", this.f3313v);
        bundle.putCharSequence("PreferenceDialogFragment.positiveText", this.f3314w);
        bundle.putCharSequence("PreferenceDialogFragment.negativeText", this.f3315x);
        bundle.putCharSequence("PreferenceDialogFragment.message", this.f3316y);
        bundle.putInt("PreferenceDialogFragment.layout", this.f3317z);
        BitmapDrawable bitmapDrawable = this.A;
        if (bitmapDrawable != null) {
            bundle.putParcelable("PreferenceDialogFragment.icon", bitmapDrawable.getBitmap());
        }
    }

    public DialogPreference r0() {
        if (this.f3312u == null) {
            this.f3312u = (DialogPreference) ((DialogPreference.a) getTargetFragment()).findPreference(getArguments().getString("key"));
        }
        return this.f3312u;
    }

    protected boolean s0() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void t0(View view) {
        View findViewById = view.findViewById(R.id.message);
        if (findViewById != null) {
            CharSequence charSequence = this.f3316y;
            int i10 = 8;
            if (!TextUtils.isEmpty(charSequence)) {
                if (findViewById instanceof TextView) {
                    ((TextView) findViewById).setText(charSequence);
                }
                i10 = 0;
            }
            if (findViewById.getVisibility() != i10) {
                findViewById.setVisibility(i10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View u0(Context context) {
        int i10 = this.f3317z;
        if (i10 == 0) {
            return null;
        }
        return getLayoutInflater().inflate(i10, (ViewGroup) null);
    }

    public abstract void v0(boolean z10);

    /* JADX INFO: Access modifiers changed from: protected */
    public void w0(AlertDialog.a aVar) {
    }
}
