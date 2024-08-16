package androidx.fragment.app;

import android.util.Log;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.h0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentManagerViewModel.java */
/* renamed from: androidx.fragment.app.l, reason: use source file name */
/* loaded from: classes.dex */
public final class FragmentManagerViewModel extends ViewModel {

    /* renamed from: k, reason: collision with root package name */
    private static final h0.b f2912k = new a();

    /* renamed from: g, reason: collision with root package name */
    private final boolean f2916g;

    /* renamed from: d, reason: collision with root package name */
    private final HashMap<String, Fragment> f2913d = new HashMap<>();

    /* renamed from: e, reason: collision with root package name */
    private final HashMap<String, FragmentManagerViewModel> f2914e = new HashMap<>();

    /* renamed from: f, reason: collision with root package name */
    private final HashMap<String, ViewModelStore> f2915f = new HashMap<>();

    /* renamed from: h, reason: collision with root package name */
    private boolean f2917h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f2918i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f2919j = false;

    /* compiled from: FragmentManagerViewModel.java */
    /* renamed from: androidx.fragment.app.l$a */
    /* loaded from: classes.dex */
    class a implements h0.b {
        a() {
        }

        @Override // androidx.lifecycle.h0.b
        public <T extends ViewModel> T a(Class<T> cls) {
            return new FragmentManagerViewModel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentManagerViewModel(boolean z10) {
        this.f2916g = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FragmentManagerViewModel j(ViewModelStore viewModelStore) {
        return (FragmentManagerViewModel) new h0(viewModelStore, f2912k).a(FragmentManagerViewModel.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.ViewModel
    public void d() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "onCleared called for " + this);
        }
        this.f2917h = true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || FragmentManagerViewModel.class != obj.getClass()) {
            return false;
        }
        FragmentManagerViewModel fragmentManagerViewModel = (FragmentManagerViewModel) obj;
        return this.f2913d.equals(fragmentManagerViewModel.f2913d) && this.f2914e.equals(fragmentManagerViewModel.f2914e) && this.f2915f.equals(fragmentManagerViewModel.f2915f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Fragment fragment) {
        if (this.f2919j) {
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved");
            }
        } else {
            if (this.f2913d.containsKey(fragment.mWho)) {
                return;
            }
            this.f2913d.put(fragment.mWho, fragment);
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Updating retained Fragments: Added " + fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Fragment fragment) {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "Clearing non-config state for " + fragment);
        }
        FragmentManagerViewModel fragmentManagerViewModel = this.f2914e.get(fragment.mWho);
        if (fragmentManagerViewModel != null) {
            fragmentManagerViewModel.d();
            this.f2914e.remove(fragment.mWho);
        }
        ViewModelStore viewModelStore = this.f2915f.get(fragment.mWho);
        if (viewModelStore != null) {
            viewModelStore.a();
            this.f2915f.remove(fragment.mWho);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment h(String str) {
        return this.f2913d.get(str);
    }

    public int hashCode() {
        return (((this.f2913d.hashCode() * 31) + this.f2914e.hashCode()) * 31) + this.f2915f.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentManagerViewModel i(Fragment fragment) {
        FragmentManagerViewModel fragmentManagerViewModel = this.f2914e.get(fragment.mWho);
        if (fragmentManagerViewModel != null) {
            return fragmentManagerViewModel;
        }
        FragmentManagerViewModel fragmentManagerViewModel2 = new FragmentManagerViewModel(this.f2916g);
        this.f2914e.put(fragment.mWho, fragmentManagerViewModel2);
        return fragmentManagerViewModel2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Collection<Fragment> k() {
        return new ArrayList(this.f2913d.values());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewModelStore l(Fragment fragment) {
        ViewModelStore viewModelStore = this.f2915f.get(fragment.mWho);
        if (viewModelStore != null) {
            return viewModelStore;
        }
        ViewModelStore viewModelStore2 = new ViewModelStore();
        this.f2915f.put(fragment.mWho, viewModelStore2);
        return viewModelStore2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean m() {
        return this.f2917h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(Fragment fragment) {
        if (this.f2919j) {
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved");
                return;
            }
            return;
        }
        if ((this.f2913d.remove(fragment.mWho) != null) && FragmentManager.H0(2)) {
            Log.v("FragmentManager", "Updating retained Fragments: Removed " + fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(boolean z10) {
        this.f2919j = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean p(Fragment fragment) {
        if (!this.f2913d.containsKey(fragment.mWho)) {
            return true;
        }
        if (this.f2916g) {
            return this.f2917h;
        }
        return !this.f2918i;
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder("FragmentManagerViewModel{");
        sb2.append(Integer.toHexString(System.identityHashCode(this)));
        sb2.append("} Fragments (");
        Iterator<Fragment> it = this.f2913d.values().iterator();
        while (it.hasNext()) {
            sb2.append(it.next());
            if (it.hasNext()) {
                sb2.append(", ");
            }
        }
        sb2.append(") Child Non Config (");
        Iterator<String> it2 = this.f2914e.keySet().iterator();
        while (it2.hasNext()) {
            sb2.append(it2.next());
            if (it2.hasNext()) {
                sb2.append(", ");
            }
        }
        sb2.append(") ViewModelStores (");
        Iterator<String> it3 = this.f2915f.keySet().iterator();
        while (it3.hasNext()) {
            sb2.append(it3.next());
            if (it3.hasNext()) {
                sb2.append(", ");
            }
        }
        sb2.append(')');
        return sb2.toString();
    }
}
