package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentStore.java */
/* renamed from: androidx.fragment.app.q, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentStore {

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList<Fragment> f2935a = new ArrayList<>();

    /* renamed from: b, reason: collision with root package name */
    private final HashMap<String, FragmentStateManager> f2936b = new HashMap<>();

    /* renamed from: c, reason: collision with root package name */
    private FragmentManagerViewModel f2937c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Fragment fragment) {
        if (!this.f2935a.contains(fragment)) {
            synchronized (this.f2935a) {
                this.f2935a.add(fragment);
            }
            fragment.mAdded = true;
            return;
        }
        throw new IllegalStateException("Fragment already added: " + fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        this.f2936b.values().removeAll(Collections.singleton(null));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c(String str) {
        return this.f2936b.get(str) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(int i10) {
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null) {
                fragmentStateManager.u(i10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str2 = str + "    ";
        if (!this.f2936b.isEmpty()) {
            printWriter.print(str);
            printWriter.println("Active Fragments:");
            for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
                printWriter.print(str);
                if (fragmentStateManager != null) {
                    Fragment k10 = fragmentStateManager.k();
                    printWriter.println(k10);
                    k10.dump(str2, fileDescriptor, printWriter, strArr);
                } else {
                    printWriter.println("null");
                }
            }
        }
        int size = this.f2935a.size();
        if (size > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i10 = 0; i10 < size; i10++) {
                Fragment fragment = this.f2935a.get(i10);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i10);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment f(String str) {
        FragmentStateManager fragmentStateManager = this.f2936b.get(str);
        if (fragmentStateManager != null) {
            return fragmentStateManager.k();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment g(int i10) {
        for (int size = this.f2935a.size() - 1; size >= 0; size--) {
            Fragment fragment = this.f2935a.get(size);
            if (fragment != null && fragment.mFragmentId == i10) {
                return fragment;
            }
        }
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null) {
                Fragment k10 = fragmentStateManager.k();
                if (k10.mFragmentId == i10) {
                    return k10;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment h(String str) {
        if (str != null) {
            for (int size = this.f2935a.size() - 1; size >= 0; size--) {
                Fragment fragment = this.f2935a.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (str == null) {
            return null;
        }
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null) {
                Fragment k10 = fragmentStateManager.k();
                if (str.equals(k10.mTag)) {
                    return k10;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment i(String str) {
        Fragment findFragmentByWho;
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null && (findFragmentByWho = fragmentStateManager.k().findFragmentByWho(str)) != null) {
                return findFragmentByWho;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j(Fragment fragment) {
        View view;
        View view2;
        ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup == null) {
            return -1;
        }
        int indexOf = this.f2935a.indexOf(fragment);
        for (int i10 = indexOf - 1; i10 >= 0; i10--) {
            Fragment fragment2 = this.f2935a.get(i10);
            if (fragment2.mContainer == viewGroup && (view2 = fragment2.mView) != null) {
                return viewGroup.indexOfChild(view2) + 1;
            }
        }
        while (true) {
            indexOf++;
            if (indexOf >= this.f2935a.size()) {
                return -1;
            }
            Fragment fragment3 = this.f2935a.get(indexOf);
            if (fragment3.mContainer == viewGroup && (view = fragment3.mView) != null) {
                return viewGroup.indexOfChild(view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<FragmentStateManager> k() {
        ArrayList arrayList = new ArrayList();
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null) {
                arrayList.add(fragmentStateManager);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Fragment> l() {
        ArrayList arrayList = new ArrayList();
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null) {
                arrayList.add(fragmentStateManager.k());
            } else {
                arrayList.add(null);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStateManager m(String str) {
        return this.f2936b.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Fragment> n() {
        ArrayList arrayList;
        if (this.f2935a.isEmpty()) {
            return Collections.emptyList();
        }
        synchronized (this.f2935a) {
            arrayList = new ArrayList(this.f2935a);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentManagerViewModel o() {
        return this.f2937c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(FragmentStateManager fragmentStateManager) {
        Fragment k10 = fragmentStateManager.k();
        if (c(k10.mWho)) {
            return;
        }
        this.f2936b.put(k10.mWho, fragmentStateManager);
        if (k10.mRetainInstanceChangedWhileDetached) {
            if (k10.mRetainInstance) {
                this.f2937c.f(k10);
            } else {
                this.f2937c.n(k10);
            }
            k10.mRetainInstanceChangedWhileDetached = false;
        }
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "Added fragment to active set " + k10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(FragmentStateManager fragmentStateManager) {
        Fragment k10 = fragmentStateManager.k();
        if (k10.mRetainInstance) {
            this.f2937c.n(k10);
        }
        if (this.f2936b.put(k10.mWho, null) != null && FragmentManager.H0(2)) {
            Log.v("FragmentManager", "Removed fragment from active set " + k10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        Iterator<Fragment> it = this.f2935a.iterator();
        while (it.hasNext()) {
            FragmentStateManager fragmentStateManager = this.f2936b.get(it.next().mWho);
            if (fragmentStateManager != null) {
                fragmentStateManager.m();
            }
        }
        for (FragmentStateManager fragmentStateManager2 : this.f2936b.values()) {
            if (fragmentStateManager2 != null) {
                fragmentStateManager2.m();
                Fragment k10 = fragmentStateManager2.k();
                if (k10.mRemoving && !k10.isInBackStack()) {
                    q(fragmentStateManager2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(Fragment fragment) {
        synchronized (this.f2935a) {
            this.f2935a.remove(fragment);
        }
        fragment.mAdded = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t() {
        this.f2936b.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(List<String> list) {
        this.f2935a.clear();
        if (list != null) {
            for (String str : list) {
                Fragment f10 = f(str);
                if (f10 != null) {
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "restoreSaveState: added (" + str + "): " + f10);
                    }
                    a(f10);
                } else {
                    throw new IllegalStateException("No instantiated fragment for (" + str + ")");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<FragmentState> v() {
        ArrayList<FragmentState> arrayList = new ArrayList<>(this.f2936b.size());
        for (FragmentStateManager fragmentStateManager : this.f2936b.values()) {
            if (fragmentStateManager != null) {
                Fragment k10 = fragmentStateManager.k();
                FragmentState s7 = fragmentStateManager.s();
                arrayList.add(s7);
                if (FragmentManager.H0(2)) {
                    Log.v("FragmentManager", "Saved state of " + k10 + ": " + s7.f2800q);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> w() {
        synchronized (this.f2935a) {
            if (this.f2935a.isEmpty()) {
                return null;
            }
            ArrayList<String> arrayList = new ArrayList<>(this.f2935a.size());
            Iterator<Fragment> it = this.f2935a.iterator();
            while (it.hasNext()) {
                Fragment next = it.next();
                arrayList.add(next.mWho);
                if (FragmentManager.H0(2)) {
                    Log.v("FragmentManager", "saveAllState: adding fragment (" + next.mWho + "): " + next);
                }
            }
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(FragmentManagerViewModel fragmentManagerViewModel) {
        this.f2937c = fragmentManagerViewModel;
    }
}
