package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.lifecycle.ViewTreeViewModelStoreOwner;
import b0.ViewTreeSavedStateRegistryOwner;

/* compiled from: DialogFragment.java */
/* renamed from: androidx.fragment.app.c, reason: use source file name */
/* loaded from: classes.dex */
public class DialogFragment extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    /* renamed from: e, reason: collision with root package name */
    private Handler f2858e;

    /* renamed from: n, reason: collision with root package name */
    private boolean f2867n;

    /* renamed from: p, reason: collision with root package name */
    private Dialog f2869p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f2870q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f2871r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f2872s;

    /* renamed from: f, reason: collision with root package name */
    private Runnable f2859f = new a();

    /* renamed from: g, reason: collision with root package name */
    private DialogInterface.OnCancelListener f2860g = new b();

    /* renamed from: h, reason: collision with root package name */
    private DialogInterface.OnDismissListener f2861h = new c();

    /* renamed from: i, reason: collision with root package name */
    private int f2862i = 0;

    /* renamed from: j, reason: collision with root package name */
    private int f2863j = 0;

    /* renamed from: k, reason: collision with root package name */
    private boolean f2864k = true;

    /* renamed from: l, reason: collision with root package name */
    private boolean f2865l = true;

    /* renamed from: m, reason: collision with root package name */
    private int f2866m = -1;

    /* renamed from: o, reason: collision with root package name */
    private Observer<androidx.lifecycle.o> f2868o = new d();

    /* renamed from: t, reason: collision with root package name */
    private boolean f2873t = false;

    /* compiled from: DialogFragment.java */
    /* renamed from: androidx.fragment.app.c$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        @SuppressLint({"SyntheticAccessor"})
        public void run() {
            DialogFragment.this.f2861h.onDismiss(DialogFragment.this.f2869p);
        }
    }

    /* compiled from: DialogFragment.java */
    /* renamed from: androidx.fragment.app.c$b */
    /* loaded from: classes.dex */
    class b implements DialogInterface.OnCancelListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnCancelListener
        @SuppressLint({"SyntheticAccessor"})
        public void onCancel(DialogInterface dialogInterface) {
            if (DialogFragment.this.f2869p != null) {
                DialogFragment dialogFragment = DialogFragment.this;
                dialogFragment.onCancel(dialogFragment.f2869p);
            }
        }
    }

    /* compiled from: DialogFragment.java */
    /* renamed from: androidx.fragment.app.c$c */
    /* loaded from: classes.dex */
    class c implements DialogInterface.OnDismissListener {
        c() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        @SuppressLint({"SyntheticAccessor"})
        public void onDismiss(DialogInterface dialogInterface) {
            if (DialogFragment.this.f2869p != null) {
                DialogFragment dialogFragment = DialogFragment.this;
                dialogFragment.onDismiss(dialogFragment.f2869p);
            }
        }
    }

    /* compiled from: DialogFragment.java */
    /* renamed from: androidx.fragment.app.c$d */
    /* loaded from: classes.dex */
    class d implements Observer<androidx.lifecycle.o> {
        d() {
        }

        @Override // androidx.lifecycle.Observer
        @SuppressLint({"SyntheticAccessor"})
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(androidx.lifecycle.o oVar) {
            if (oVar == null || !DialogFragment.this.f2865l) {
                return;
            }
            View requireView = DialogFragment.this.requireView();
            if (requireView.getParent() == null) {
                if (DialogFragment.this.f2869p != null) {
                    if (FragmentManager.H0(3)) {
                        Log.d("FragmentManager", "DialogFragment " + this + " setting the content view on " + DialogFragment.this.f2869p);
                    }
                    DialogFragment.this.f2869p.setContentView(requireView);
                    return;
                }
                return;
            }
            throw new IllegalStateException("DialogFragment can not be attached to a container view");
        }
    }

    /* compiled from: DialogFragment.java */
    /* renamed from: androidx.fragment.app.c$e */
    /* loaded from: classes.dex */
    class e extends FragmentContainer {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ FragmentContainer f2878a;

        e(FragmentContainer fragmentContainer) {
            this.f2878a = fragmentContainer;
        }

        @Override // androidx.fragment.app.FragmentContainer
        public View c(int i10) {
            if (this.f2878a.d()) {
                return this.f2878a.c(i10);
            }
            return DialogFragment.this.l0(i10);
        }

        @Override // androidx.fragment.app.FragmentContainer
        public boolean d() {
            return this.f2878a.d() || DialogFragment.this.m0();
        }
    }

    private void h0(boolean z10, boolean z11) {
        if (this.f2871r) {
            return;
        }
        this.f2871r = true;
        this.f2872s = false;
        Dialog dialog = this.f2869p;
        if (dialog != null) {
            dialog.setOnDismissListener(null);
            this.f2869p.dismiss();
            if (!z11) {
                if (Looper.myLooper() == this.f2858e.getLooper()) {
                    onDismiss(this.f2869p);
                } else {
                    this.f2858e.post(this.f2859f);
                }
            }
        }
        this.f2870q = true;
        if (this.f2866m >= 0) {
            getParentFragmentManager().Y0(this.f2866m, 1);
            this.f2866m = -1;
            return;
        }
        FragmentTransaction m10 = getParentFragmentManager().m();
        m10.q(this);
        if (z10) {
            m10.j();
        } else {
            m10.i();
        }
    }

    private void n0(Bundle bundle) {
        if (this.f2865l && !this.f2873t) {
            try {
                this.f2867n = true;
                Dialog k02 = k0(bundle);
                this.f2869p = k02;
                if (this.f2865l) {
                    p0(k02, this.f2862i);
                    Context context = getContext();
                    if (context instanceof Activity) {
                        this.f2869p.setOwnerActivity((Activity) context);
                    }
                    this.f2869p.setCancelable(this.f2864k);
                    this.f2869p.setOnCancelListener(this.f2860g);
                    this.f2869p.setOnDismissListener(this.f2861h);
                    this.f2873t = true;
                } else {
                    this.f2869p = null;
                }
            } finally {
                this.f2867n = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.fragment.app.Fragment
    public FragmentContainer createFragmentContainer() {
        return new e(super.createFragmentContainer());
    }

    public void g0() {
        h0(false, false);
    }

    public Dialog i0() {
        return this.f2869p;
    }

    public int j0() {
        return this.f2863j;
    }

    public Dialog k0(Bundle bundle) {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "onCreateDialog called for DialogFragment " + this);
        }
        return new Dialog(requireContext(), j0());
    }

    View l0(int i10) {
        Dialog dialog = this.f2869p;
        if (dialog != null) {
            return dialog.findViewById(i10);
        }
        return null;
    }

    boolean m0() {
        return this.f2873t;
    }

    public final Dialog o0() {
        Dialog i02 = i0();
        if (i02 != null) {
            return i02;
        }
        throw new IllegalStateException("DialogFragment " + this + " does not have a Dialog.");
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        getViewLifecycleOwnerLiveData().h(this.f2868o);
        if (this.f2872s) {
            return;
        }
        this.f2871r = false;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f2858e = new Handler();
        this.f2865l = this.mContainerId == 0;
        if (bundle != null) {
            this.f2862i = bundle.getInt("android:style", 0);
            this.f2863j = bundle.getInt("android:theme", 0);
            this.f2864k = bundle.getBoolean("android:cancelable", true);
            this.f2865l = bundle.getBoolean("android:showsDialog", this.f2865l);
            this.f2866m = bundle.getInt("android:backStackId", -1);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        Dialog dialog = this.f2869p;
        if (dialog != null) {
            this.f2870q = true;
            dialog.setOnDismissListener(null);
            this.f2869p.dismiss();
            if (!this.f2871r) {
                onDismiss(this.f2869p);
            }
            this.f2869p = null;
            this.f2873t = false;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (!this.f2872s && !this.f2871r) {
            this.f2871r = true;
        }
        getViewLifecycleOwnerLiveData().l(this.f2868o);
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        if (this.f2870q) {
            return;
        }
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "onDismiss called for DialogFragment " + this);
        }
        h0(true, true);
    }

    @Override // androidx.fragment.app.Fragment
    public LayoutInflater onGetLayoutInflater(Bundle bundle) {
        LayoutInflater onGetLayoutInflater = super.onGetLayoutInflater(bundle);
        if (this.f2865l && !this.f2867n) {
            n0(bundle);
            if (FragmentManager.H0(2)) {
                Log.d("FragmentManager", "get layout inflater for DialogFragment " + this + " from dialog context");
            }
            Dialog dialog = this.f2869p;
            return dialog != null ? onGetLayoutInflater.cloneInContext(dialog.getContext()) : onGetLayoutInflater;
        }
        if (FragmentManager.H0(2)) {
            String str = "getting layout inflater for DialogFragment " + this;
            if (!this.f2865l) {
                Log.d("FragmentManager", "mShowsDialog = false: " + str);
            } else {
                Log.d("FragmentManager", "mCreatingDialog = true: " + str);
            }
        }
        return onGetLayoutInflater;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Dialog dialog = this.f2869p;
        if (dialog != null) {
            Bundle onSaveInstanceState = dialog.onSaveInstanceState();
            onSaveInstanceState.putBoolean("android:dialogShowing", false);
            bundle.putBundle("android:savedDialogState", onSaveInstanceState);
        }
        int i10 = this.f2862i;
        if (i10 != 0) {
            bundle.putInt("android:style", i10);
        }
        int i11 = this.f2863j;
        if (i11 != 0) {
            bundle.putInt("android:theme", i11);
        }
        boolean z10 = this.f2864k;
        if (!z10) {
            bundle.putBoolean("android:cancelable", z10);
        }
        boolean z11 = this.f2865l;
        if (!z11) {
            bundle.putBoolean("android:showsDialog", z11);
        }
        int i12 = this.f2866m;
        if (i12 != -1) {
            bundle.putInt("android:backStackId", i12);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Dialog dialog = this.f2869p;
        if (dialog != null) {
            this.f2870q = false;
            dialog.show();
            View decorView = this.f2869p.getWindow().getDecorView();
            ViewTreeLifecycleOwner.a(decorView, this);
            ViewTreeViewModelStoreOwner.a(decorView, this);
            ViewTreeSavedStateRegistryOwner.a(decorView, this);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        Dialog dialog = this.f2869p;
        if (dialog != null) {
            dialog.hide();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        Bundle bundle2;
        super.onViewStateRestored(bundle);
        if (this.f2869p == null || bundle == null || (bundle2 = bundle.getBundle("android:savedDialogState")) == null) {
            return;
        }
        this.f2869p.onRestoreInstanceState(bundle2);
    }

    public void p0(Dialog dialog, int i10) {
        if (i10 != 1 && i10 != 2) {
            if (i10 != 3) {
                return;
            }
            Window window = dialog.getWindow();
            if (window != null) {
                window.addFlags(24);
            }
        }
        dialog.requestWindowFeature(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.fragment.app.Fragment
    public void performCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle bundle2;
        super.performCreateView(layoutInflater, viewGroup, bundle);
        if (this.mView != null || this.f2869p == null || bundle == null || (bundle2 = bundle.getBundle("android:savedDialogState")) == null) {
            return;
        }
        this.f2869p.onRestoreInstanceState(bundle2);
    }

    public void q0(FragmentManager fragmentManager, String str) {
        this.f2871r = false;
        this.f2872s = true;
        FragmentTransaction m10 = fragmentManager.m();
        m10.e(this, str);
        m10.i();
    }
}
