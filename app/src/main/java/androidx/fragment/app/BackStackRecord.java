package androidx.fragment.app;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.h;
import java.io.PrintWriter;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BackStackRecord.java */
/* renamed from: androidx.fragment.app.a, reason: use source file name */
/* loaded from: classes.dex */
public final class BackStackRecord extends FragmentTransaction implements FragmentManager.n {

    /* renamed from: t, reason: collision with root package name */
    final FragmentManager f2813t;

    /* renamed from: u, reason: collision with root package name */
    boolean f2814u;

    /* renamed from: v, reason: collision with root package name */
    int f2815v;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackStackRecord(FragmentManager fragmentManager) {
        super(fragmentManager.s0(), fragmentManager.v0() != null ? fragmentManager.v0().f().getClassLoader() : null);
        this.f2815v = -1;
        this.f2813t = fragmentManager;
    }

    private static boolean F(FragmentTransaction.a aVar) {
        Fragment fragment = aVar.f2958b;
        return (fragment == null || !fragment.mAdded || fragment.mView == null || fragment.mDetached || fragment.mHidden || !fragment.isPostponed()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(boolean z10) {
        for (int size = this.f2940c.size() - 1; size >= 0; size--) {
            FragmentTransaction.a aVar = this.f2940c.get(size);
            Fragment fragment = aVar.f2958b;
            if (fragment != null) {
                fragment.setPopDirection(true);
                fragment.setNextTransition(FragmentManager.l1(this.f2945h));
                fragment.setSharedElementNames(this.f2954q, this.f2953p);
            }
            switch (aVar.f2957a) {
                case 1:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.p1(fragment, true);
                    this.f2813t.g1(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + aVar.f2957a);
                case 3:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.g(fragment);
                    break;
                case 4:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.t1(fragment);
                    break;
                case 5:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.p1(fragment, true);
                    this.f2813t.E0(fragment);
                    break;
                case 6:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.l(fragment);
                    break;
                case 7:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.p1(fragment, true);
                    this.f2813t.y(fragment);
                    break;
                case 8:
                    this.f2813t.r1(null);
                    break;
                case 9:
                    this.f2813t.r1(fragment);
                    break;
                case 10:
                    this.f2813t.q1(fragment, aVar.f2963g);
                    break;
            }
            if (!this.f2955r && aVar.f2957a != 3 && fragment != null && !FragmentManager.P) {
                this.f2813t.R0(fragment);
            }
        }
        if (this.f2955r || !z10 || FragmentManager.P) {
            return;
        }
        FragmentManager fragmentManager = this.f2813t;
        fragmentManager.S0(fragmentManager.f2743q, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment B(ArrayList<Fragment> arrayList, Fragment fragment) {
        Fragment fragment2 = fragment;
        int i10 = 0;
        while (i10 < this.f2940c.size()) {
            FragmentTransaction.a aVar = this.f2940c.get(i10);
            int i11 = aVar.f2957a;
            if (i11 != 1) {
                if (i11 == 2) {
                    Fragment fragment3 = aVar.f2958b;
                    int i12 = fragment3.mContainerId;
                    boolean z10 = false;
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        Fragment fragment4 = arrayList.get(size);
                        if (fragment4.mContainerId == i12) {
                            if (fragment4 == fragment3) {
                                z10 = true;
                            } else {
                                if (fragment4 == fragment2) {
                                    this.f2940c.add(i10, new FragmentTransaction.a(9, fragment4));
                                    i10++;
                                    fragment2 = null;
                                }
                                FragmentTransaction.a aVar2 = new FragmentTransaction.a(3, fragment4);
                                aVar2.f2959c = aVar.f2959c;
                                aVar2.f2961e = aVar.f2961e;
                                aVar2.f2960d = aVar.f2960d;
                                aVar2.f2962f = aVar.f2962f;
                                this.f2940c.add(i10, aVar2);
                                arrayList.remove(fragment4);
                                i10++;
                            }
                        }
                    }
                    if (z10) {
                        this.f2940c.remove(i10);
                        i10--;
                    } else {
                        aVar.f2957a = 1;
                        arrayList.add(fragment3);
                    }
                } else if (i11 == 3 || i11 == 6) {
                    arrayList.remove(aVar.f2958b);
                    Fragment fragment5 = aVar.f2958b;
                    if (fragment5 == fragment2) {
                        this.f2940c.add(i10, new FragmentTransaction.a(9, fragment5));
                        i10++;
                        fragment2 = null;
                    }
                } else if (i11 != 7) {
                    if (i11 == 8) {
                        this.f2940c.add(i10, new FragmentTransaction.a(9, fragment2));
                        i10++;
                        fragment2 = aVar.f2958b;
                    }
                }
                i10++;
            }
            arrayList.add(aVar.f2958b);
            i10++;
        }
        return fragment2;
    }

    public String C() {
        return this.f2948k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean D(int i10) {
        int size = this.f2940c.size();
        for (int i11 = 0; i11 < size; i11++) {
            Fragment fragment = this.f2940c.get(i11).f2958b;
            int i12 = fragment != null ? fragment.mContainerId : 0;
            if (i12 != 0 && i12 == i10) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean E(ArrayList<BackStackRecord> arrayList, int i10, int i11) {
        if (i11 == i10) {
            return false;
        }
        int size = this.f2940c.size();
        int i12 = -1;
        for (int i13 = 0; i13 < size; i13++) {
            Fragment fragment = this.f2940c.get(i13).f2958b;
            int i14 = fragment != null ? fragment.mContainerId : 0;
            if (i14 != 0 && i14 != i12) {
                for (int i15 = i10; i15 < i11; i15++) {
                    BackStackRecord backStackRecord = arrayList.get(i15);
                    int size2 = backStackRecord.f2940c.size();
                    for (int i16 = 0; i16 < size2; i16++) {
                        Fragment fragment2 = backStackRecord.f2940c.get(i16).f2958b;
                        if ((fragment2 != null ? fragment2.mContainerId : 0) == i14) {
                            return true;
                        }
                    }
                }
                i12 = i14;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean G() {
        for (int i10 = 0; i10 < this.f2940c.size(); i10++) {
            if (F(this.f2940c.get(i10))) {
                return true;
            }
        }
        return false;
    }

    public void H() {
        if (this.f2956s != null) {
            for (int i10 = 0; i10 < this.f2956s.size(); i10++) {
                this.f2956s.get(i10).run();
            }
            this.f2956s = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(Fragment.l lVar) {
        for (int i10 = 0; i10 < this.f2940c.size(); i10++) {
            FragmentTransaction.a aVar = this.f2940c.get(i10);
            if (F(aVar)) {
                aVar.f2958b.setOnStartEnterTransitionListener(lVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment J(ArrayList<Fragment> arrayList, Fragment fragment) {
        for (int size = this.f2940c.size() - 1; size >= 0; size--) {
            FragmentTransaction.a aVar = this.f2940c.get(size);
            int i10 = aVar.f2957a;
            if (i10 != 1) {
                if (i10 != 3) {
                    switch (i10) {
                        case 8:
                            fragment = null;
                            break;
                        case 9:
                            fragment = aVar.f2958b;
                            break;
                        case 10:
                            aVar.f2964h = aVar.f2963g;
                            break;
                    }
                }
                arrayList.add(aVar.f2958b);
            }
            arrayList.remove(aVar.f2958b);
        }
        return fragment;
    }

    @Override // androidx.fragment.app.FragmentManager.n
    public boolean a(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "Run: " + this);
        }
        arrayList.add(this);
        arrayList2.add(Boolean.FALSE);
        if (!this.f2946i) {
            return true;
        }
        this.f2813t.e(this);
        return true;
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public int i() {
        return w(false);
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public int j() {
        return w(true);
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public void k() {
        n();
        this.f2813t.c0(this, false);
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public void l() {
        n();
        this.f2813t.c0(this, true);
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public FragmentTransaction m(Fragment fragment) {
        FragmentManager fragmentManager = fragment.mFragmentManager;
        if (fragmentManager != null && fragmentManager != this.f2813t) {
            throw new IllegalStateException("Cannot detach Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
        }
        return super.m(fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.fragment.app.FragmentTransaction
    public void o(int i10, Fragment fragment, String str, int i11) {
        super.o(i10, fragment, str, i11);
        fragment.mFragmentManager = this.f2813t;
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public boolean p() {
        return this.f2940c.isEmpty();
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public FragmentTransaction q(Fragment fragment) {
        FragmentManager fragmentManager = fragment.mFragmentManager;
        if (fragmentManager != null && fragmentManager != this.f2813t) {
            throw new IllegalStateException("Cannot remove Fragment attached to a different FragmentManager. Fragment " + fragment.toString() + " is already attached to a FragmentManager.");
        }
        return super.q(fragment);
    }

    @Override // androidx.fragment.app.FragmentTransaction
    public FragmentTransaction t(Fragment fragment, h.c cVar) {
        if (fragment.mFragmentManager == this.f2813t) {
            if (cVar == h.c.INITIALIZED && fragment.mState > -1) {
                throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + cVar + " after the Fragment has been created");
            }
            if (cVar != h.c.DESTROYED) {
                return super.t(fragment, cVar);
            }
            throw new IllegalArgumentException("Cannot set maximum Lifecycle to " + cVar + ". Use remove() to remove the fragment from the FragmentManager and trigger its destruction.");
        }
        throw new IllegalArgumentException("Cannot setMaxLifecycle for Fragment not attached to FragmentManager " + this.f2813t);
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("BackStackEntry{");
        sb2.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.f2815v >= 0) {
            sb2.append(" #");
            sb2.append(this.f2815v);
        }
        if (this.f2948k != null) {
            sb2.append(" ");
            sb2.append(this.f2948k);
        }
        sb2.append("}");
        return sb2.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(int i10) {
        if (this.f2946i) {
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Bump nesting in " + this + " by " + i10);
            }
            int size = this.f2940c.size();
            for (int i11 = 0; i11 < size; i11++) {
                FragmentTransaction.a aVar = this.f2940c.get(i11);
                Fragment fragment = aVar.f2958b;
                if (fragment != null) {
                    fragment.mBackStackNesting += i10;
                    if (FragmentManager.H0(2)) {
                        Log.v("FragmentManager", "Bump nesting of " + aVar.f2958b + " to " + aVar.f2958b.mBackStackNesting);
                    }
                }
            }
        }
    }

    int w(boolean z10) {
        if (!this.f2814u) {
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Commit: " + this);
                PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                x("  ", printWriter);
                printWriter.close();
            }
            this.f2814u = true;
            if (this.f2946i) {
                this.f2815v = this.f2813t.j();
            } else {
                this.f2815v = -1;
            }
            this.f2813t.Z(this, z10);
            return this.f2815v;
        }
        throw new IllegalStateException("commit already called");
    }

    public void x(String str, PrintWriter printWriter) {
        y(str, printWriter, true);
    }

    public void y(String str, PrintWriter printWriter, boolean z10) {
        String str2;
        if (z10) {
            printWriter.print(str);
            printWriter.print("mName=");
            printWriter.print(this.f2948k);
            printWriter.print(" mIndex=");
            printWriter.print(this.f2815v);
            printWriter.print(" mCommitted=");
            printWriter.println(this.f2814u);
            if (this.f2945h != 0) {
                printWriter.print(str);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.f2945h));
            }
            if (this.f2941d != 0 || this.f2942e != 0) {
                printWriter.print(str);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.f2941d));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.f2942e));
            }
            if (this.f2943f != 0 || this.f2944g != 0) {
                printWriter.print(str);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.f2943f));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.f2944g));
            }
            if (this.f2949l != 0 || this.f2950m != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.f2949l));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.f2950m);
            }
            if (this.f2951n != 0 || this.f2952o != null) {
                printWriter.print(str);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.f2951n));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.f2952o);
            }
        }
        if (this.f2940c.isEmpty()) {
            return;
        }
        printWriter.print(str);
        printWriter.println("Operations:");
        int size = this.f2940c.size();
        for (int i10 = 0; i10 < size; i10++) {
            FragmentTransaction.a aVar = this.f2940c.get(i10);
            switch (aVar.f2957a) {
                case 0:
                    str2 = "NULL";
                    break;
                case 1:
                    str2 = "ADD";
                    break;
                case 2:
                    str2 = "REPLACE";
                    break;
                case 3:
                    str2 = "REMOVE";
                    break;
                case 4:
                    str2 = "HIDE";
                    break;
                case 5:
                    str2 = "SHOW";
                    break;
                case 6:
                    str2 = "DETACH";
                    break;
                case 7:
                    str2 = "ATTACH";
                    break;
                case 8:
                    str2 = "SET_PRIMARY_NAV";
                    break;
                case 9:
                    str2 = "UNSET_PRIMARY_NAV";
                    break;
                case 10:
                    str2 = "OP_SET_MAX_LIFECYCLE";
                    break;
                default:
                    str2 = "cmd=" + aVar.f2957a;
                    break;
            }
            printWriter.print(str);
            printWriter.print("  Op #");
            printWriter.print(i10);
            printWriter.print(": ");
            printWriter.print(str2);
            printWriter.print(" ");
            printWriter.println(aVar.f2958b);
            if (z10) {
                if (aVar.f2959c != 0 || aVar.f2960d != 0) {
                    printWriter.print(str);
                    printWriter.print("enterAnim=#");
                    printWriter.print(Integer.toHexString(aVar.f2959c));
                    printWriter.print(" exitAnim=#");
                    printWriter.println(Integer.toHexString(aVar.f2960d));
                }
                if (aVar.f2961e != 0 || aVar.f2962f != 0) {
                    printWriter.print(str);
                    printWriter.print("popEnterAnim=#");
                    printWriter.print(Integer.toHexString(aVar.f2961e));
                    printWriter.print(" popExitAnim=#");
                    printWriter.println(Integer.toHexString(aVar.f2962f));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z() {
        int size = this.f2940c.size();
        for (int i10 = 0; i10 < size; i10++) {
            FragmentTransaction.a aVar = this.f2940c.get(i10);
            Fragment fragment = aVar.f2958b;
            if (fragment != null) {
                fragment.setPopDirection(false);
                fragment.setNextTransition(this.f2945h);
                fragment.setSharedElementNames(this.f2953p, this.f2954q);
            }
            switch (aVar.f2957a) {
                case 1:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.p1(fragment, false);
                    this.f2813t.g(fragment);
                    break;
                case 2:
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + aVar.f2957a);
                case 3:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.g1(fragment);
                    break;
                case 4:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.E0(fragment);
                    break;
                case 5:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.p1(fragment, false);
                    this.f2813t.t1(fragment);
                    break;
                case 6:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.y(fragment);
                    break;
                case 7:
                    fragment.setAnimations(aVar.f2959c, aVar.f2960d, aVar.f2961e, aVar.f2962f);
                    this.f2813t.p1(fragment, false);
                    this.f2813t.l(fragment);
                    break;
                case 8:
                    this.f2813t.r1(fragment);
                    break;
                case 9:
                    this.f2813t.r1(null);
                    break;
                case 10:
                    this.f2813t.q1(fragment, aVar.f2964h);
                    break;
            }
            if (!this.f2955r && aVar.f2957a != 1 && fragment != null && !FragmentManager.P) {
                this.f2813t.R0(fragment);
            }
        }
        if (this.f2955r || FragmentManager.P) {
            return;
        }
        FragmentManager fragmentManager = this.f2813t;
        fragmentManager.S0(fragmentManager.f2743q, true);
    }
}
