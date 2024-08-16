package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.FragmentTransitionSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentTransition.java */
/* renamed from: androidx.fragment.app.s, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentTransition {

    /* renamed from: a, reason: collision with root package name */
    private static final int[] f2965a = {0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};

    /* renamed from: b, reason: collision with root package name */
    static final FragmentTransitionImpl f2966b = new FragmentTransitionCompat21();

    /* renamed from: c, reason: collision with root package name */
    static final FragmentTransitionImpl f2967c = w();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ g f2968e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Fragment f2969f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ CancellationSignal f2970g;

        a(g gVar, Fragment fragment, CancellationSignal cancellationSignal) {
            this.f2968e = gVar;
            this.f2969f = fragment;
            this.f2970g = cancellationSignal;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f2968e.a(this.f2969f, this.f2970g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ArrayList f2971e;

        b(ArrayList arrayList) {
            this.f2971e = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentTransition.A(this.f2971e, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$c */
    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ g f2972e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Fragment f2973f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ CancellationSignal f2974g;

        c(g gVar, Fragment fragment, CancellationSignal cancellationSignal) {
            this.f2972e = gVar;
            this.f2973f = fragment;
            this.f2974g = cancellationSignal;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f2972e.a(this.f2973f, this.f2974g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$d */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Object f2975e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ FragmentTransitionImpl f2976f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ View f2977g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ Fragment f2978h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ ArrayList f2979i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ ArrayList f2980j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ ArrayList f2981k;

        /* renamed from: l, reason: collision with root package name */
        final /* synthetic */ Object f2982l;

        d(Object obj, FragmentTransitionImpl fragmentTransitionImpl, View view, Fragment fragment, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, Object obj2) {
            this.f2975e = obj;
            this.f2976f = fragmentTransitionImpl;
            this.f2977g = view;
            this.f2978h = fragment;
            this.f2979i = arrayList;
            this.f2980j = arrayList2;
            this.f2981k = arrayList3;
            this.f2982l = obj2;
        }

        @Override // java.lang.Runnable
        public void run() {
            Object obj = this.f2975e;
            if (obj != null) {
                this.f2976f.p(obj, this.f2977g);
                this.f2980j.addAll(FragmentTransition.k(this.f2976f, this.f2975e, this.f2978h, this.f2979i, this.f2977g));
            }
            if (this.f2981k != null) {
                if (this.f2982l != null) {
                    ArrayList<View> arrayList = new ArrayList<>();
                    arrayList.add(this.f2977g);
                    this.f2976f.q(this.f2982l, this.f2981k, arrayList);
                }
                this.f2981k.clear();
                this.f2981k.add(this.f2977g);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$e */
    /* loaded from: classes.dex */
    public class e implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Fragment f2983e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Fragment f2984f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ boolean f2985g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ j.a f2986h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ View f2987i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ FragmentTransitionImpl f2988j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ Rect f2989k;

        e(Fragment fragment, Fragment fragment2, boolean z10, j.a aVar, View view, FragmentTransitionImpl fragmentTransitionImpl, Rect rect) {
            this.f2983e = fragment;
            this.f2984f = fragment2;
            this.f2985g = z10;
            this.f2986h = aVar;
            this.f2987i = view;
            this.f2988j = fragmentTransitionImpl;
            this.f2989k = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentTransition.f(this.f2983e, this.f2984f, this.f2985g, this.f2986h, false);
            View view = this.f2987i;
            if (view != null) {
                this.f2988j.k(view, this.f2989k);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$f */
    /* loaded from: classes.dex */
    public class f implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ FragmentTransitionImpl f2990e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ j.a f2991f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ Object f2992g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ h f2993h;

        /* renamed from: i, reason: collision with root package name */
        final /* synthetic */ ArrayList f2994i;

        /* renamed from: j, reason: collision with root package name */
        final /* synthetic */ View f2995j;

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ Fragment f2996k;

        /* renamed from: l, reason: collision with root package name */
        final /* synthetic */ Fragment f2997l;

        /* renamed from: m, reason: collision with root package name */
        final /* synthetic */ boolean f2998m;

        /* renamed from: n, reason: collision with root package name */
        final /* synthetic */ ArrayList f2999n;

        /* renamed from: o, reason: collision with root package name */
        final /* synthetic */ Object f3000o;

        /* renamed from: p, reason: collision with root package name */
        final /* synthetic */ Rect f3001p;

        f(FragmentTransitionImpl fragmentTransitionImpl, j.a aVar, Object obj, h hVar, ArrayList arrayList, View view, Fragment fragment, Fragment fragment2, boolean z10, ArrayList arrayList2, Object obj2, Rect rect) {
            this.f2990e = fragmentTransitionImpl;
            this.f2991f = aVar;
            this.f2992g = obj;
            this.f2993h = hVar;
            this.f2994i = arrayList;
            this.f2995j = view;
            this.f2996k = fragment;
            this.f2997l = fragment2;
            this.f2998m = z10;
            this.f2999n = arrayList2;
            this.f3000o = obj2;
            this.f3001p = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            j.a<String, View> h10 = FragmentTransition.h(this.f2990e, this.f2991f, this.f2992g, this.f2993h);
            if (h10 != null) {
                this.f2994i.addAll(h10.values());
                this.f2994i.add(this.f2995j);
            }
            FragmentTransition.f(this.f2996k, this.f2997l, this.f2998m, h10, false);
            Object obj = this.f2992g;
            if (obj != null) {
                this.f2990e.A(obj, this.f2999n, this.f2994i);
                View s7 = FragmentTransition.s(h10, this.f2993h, this.f3000o, this.f2998m);
                if (s7 != null) {
                    this.f2990e.k(s7, this.f3001p);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$g */
    /* loaded from: classes.dex */
    public interface g {
        void a(Fragment fragment, CancellationSignal cancellationSignal);

        void b(Fragment fragment, CancellationSignal cancellationSignal);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentTransition.java */
    /* renamed from: androidx.fragment.app.s$h */
    /* loaded from: classes.dex */
    public static class h {

        /* renamed from: a, reason: collision with root package name */
        public Fragment f3002a;

        /* renamed from: b, reason: collision with root package name */
        public boolean f3003b;

        /* renamed from: c, reason: collision with root package name */
        public BackStackRecord f3004c;

        /* renamed from: d, reason: collision with root package name */
        public Fragment f3005d;

        /* renamed from: e, reason: collision with root package name */
        public boolean f3006e;

        /* renamed from: f, reason: collision with root package name */
        public BackStackRecord f3007f;

        h() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void A(ArrayList<View> arrayList, int i10) {
        if (arrayList == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            arrayList.get(size).setVisibility(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void B(Context context, FragmentContainer fragmentContainer, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i10, int i11, boolean z10, g gVar) {
        ViewGroup viewGroup;
        SparseArray sparseArray = new SparseArray();
        for (int i12 = i10; i12 < i11; i12++) {
            BackStackRecord backStackRecord = arrayList.get(i12);
            if (arrayList2.get(i12).booleanValue()) {
                e(backStackRecord, sparseArray, z10);
            } else {
                c(backStackRecord, sparseArray, z10);
            }
        }
        if (sparseArray.size() != 0) {
            View view = new View(context);
            int size = sparseArray.size();
            for (int i13 = 0; i13 < size; i13++) {
                int keyAt = sparseArray.keyAt(i13);
                j.a<String, String> d10 = d(keyAt, arrayList, arrayList2, i10, i11);
                h hVar = (h) sparseArray.valueAt(i13);
                if (fragmentContainer.d() && (viewGroup = (ViewGroup) fragmentContainer.c(keyAt)) != null) {
                    if (z10) {
                        o(viewGroup, hVar, view, d10, gVar);
                    } else {
                        n(viewGroup, hVar, view, d10, gVar);
                    }
                }
            }
        }
    }

    private static void a(ArrayList<View> arrayList, j.a<String, View> aVar, Collection<String> collection) {
        for (int size = aVar.size() - 1; size >= 0; size--) {
            View n10 = aVar.n(size);
            if (collection.contains(ViewCompat.G(n10))) {
                arrayList.add(n10);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:58:0x0039, code lost:
    
        if (r0.mAdded != false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x008d, code lost:
    
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x006f, code lost:
    
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x008b, code lost:
    
        if (r0.mHidden == false) goto L69;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00c8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00da A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void b(BackStackRecord backStackRecord, FragmentTransaction.a aVar, SparseArray<h> sparseArray, boolean z10, boolean z11) {
        int i10;
        boolean z12;
        boolean z13;
        boolean z14;
        h hVar;
        Fragment fragment = aVar.f2958b;
        if (fragment == null || (i10 = fragment.mContainerId) == 0) {
            return;
        }
        int i11 = z10 ? f2965a[aVar.f2957a] : aVar.f2957a;
        boolean z15 = false;
        boolean z16 = true;
        if (i11 != 1) {
            if (i11 != 3) {
                if (i11 == 4) {
                    boolean z17 = !z11 ? false : false;
                    z13 = z17;
                    z14 = true;
                    z16 = false;
                    hVar = sparseArray.get(i10);
                    if (z15) {
                    }
                    if (!z11) {
                    }
                    if (z13) {
                    }
                    if (z11) {
                    }
                } else if (i11 != 5) {
                    if (i11 != 6) {
                        if (i11 != 7) {
                            z14 = false;
                            z16 = false;
                            z13 = false;
                            hVar = sparseArray.get(i10);
                            if (z15) {
                                hVar = p(hVar, sparseArray, i10);
                                hVar.f3002a = fragment;
                                hVar.f3003b = z10;
                                hVar.f3004c = backStackRecord;
                            }
                            if (!z11 && z16) {
                                if (hVar != null && hVar.f3005d == fragment) {
                                    hVar.f3005d = null;
                                }
                                if (!backStackRecord.f2955r) {
                                    FragmentManager fragmentManager = backStackRecord.f2813t;
                                    fragmentManager.t0().p(fragmentManager.w(fragment));
                                    fragmentManager.T0(fragment);
                                }
                            }
                            if (z13 && (hVar == null || hVar.f3005d == null)) {
                                hVar = p(hVar, sparseArray, i10);
                                hVar.f3005d = fragment;
                                hVar.f3006e = z10;
                                hVar.f3007f = backStackRecord;
                            }
                            if (z11 || !z14 || hVar == null || hVar.f3002a != fragment) {
                                return;
                            }
                            hVar.f3002a = null;
                            return;
                        }
                    }
                } else {
                    if (z11) {
                        if (fragment.mHiddenChanged) {
                            if (!fragment.mHidden) {
                            }
                        }
                        z12 = false;
                        z13 = false;
                        z15 = z12;
                        z14 = false;
                        hVar = sparseArray.get(i10);
                        if (z15) {
                        }
                        if (!z11) {
                            if (hVar != null) {
                                hVar.f3005d = null;
                            }
                            if (!backStackRecord.f2955r) {
                            }
                        }
                        if (z13) {
                            hVar = p(hVar, sparseArray, i10);
                            hVar.f3005d = fragment;
                            hVar.f3006e = z10;
                            hVar.f3007f = backStackRecord;
                        }
                        if (z11) {
                            return;
                        } else {
                            return;
                        }
                    }
                    z12 = fragment.mHidden;
                    z13 = false;
                    z15 = z12;
                    z14 = false;
                    hVar = sparseArray.get(i10);
                    if (z15) {
                    }
                    if (!z11) {
                    }
                    if (z13) {
                    }
                    if (z11) {
                    }
                }
            }
            if (!z11) {
            }
            z13 = z17;
            z14 = true;
            z16 = false;
            hVar = sparseArray.get(i10);
            if (z15) {
            }
            if (!z11) {
            }
            if (z13) {
            }
            if (z11) {
            }
        }
        if (z11) {
            z12 = fragment.mIsNewlyAdded;
            z13 = false;
            z15 = z12;
            z14 = false;
            hVar = sparseArray.get(i10);
            if (z15) {
            }
            if (!z11) {
            }
            if (z13) {
            }
            if (z11) {
            }
        } else {
            if (!fragment.mAdded) {
            }
            z12 = false;
            z13 = false;
            z15 = z12;
            z14 = false;
            hVar = sparseArray.get(i10);
            if (z15) {
            }
            if (!z11) {
            }
            if (z13) {
            }
            if (z11) {
            }
        }
    }

    public static void c(BackStackRecord backStackRecord, SparseArray<h> sparseArray, boolean z10) {
        int size = backStackRecord.f2940c.size();
        for (int i10 = 0; i10 < size; i10++) {
            b(backStackRecord, backStackRecord.f2940c.get(i10), sparseArray, false, z10);
        }
    }

    private static j.a<String, String> d(int i10, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i11, int i12) {
        ArrayList<String> arrayList3;
        ArrayList<String> arrayList4;
        j.a<String, String> aVar = new j.a<>();
        for (int i13 = i12 - 1; i13 >= i11; i13--) {
            BackStackRecord backStackRecord = arrayList.get(i13);
            if (backStackRecord.D(i10)) {
                boolean booleanValue = arrayList2.get(i13).booleanValue();
                ArrayList<String> arrayList5 = backStackRecord.f2953p;
                if (arrayList5 != null) {
                    int size = arrayList5.size();
                    if (booleanValue) {
                        arrayList3 = backStackRecord.f2953p;
                        arrayList4 = backStackRecord.f2954q;
                    } else {
                        ArrayList<String> arrayList6 = backStackRecord.f2953p;
                        arrayList3 = backStackRecord.f2954q;
                        arrayList4 = arrayList6;
                    }
                    for (int i14 = 0; i14 < size; i14++) {
                        String str = arrayList4.get(i14);
                        String str2 = arrayList3.get(i14);
                        String remove = aVar.remove(str2);
                        if (remove != null) {
                            aVar.put(str, remove);
                        } else {
                            aVar.put(str, str2);
                        }
                    }
                }
            }
        }
        return aVar;
    }

    public static void e(BackStackRecord backStackRecord, SparseArray<h> sparseArray, boolean z10) {
        if (backStackRecord.f2813t.p0().d()) {
            for (int size = backStackRecord.f2940c.size() - 1; size >= 0; size--) {
                b(backStackRecord, backStackRecord.f2940c.get(size), sparseArray, true, z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void f(Fragment fragment, Fragment fragment2, boolean z10, j.a<String, View> aVar, boolean z11) {
        if (z10) {
            fragment2.getEnterTransitionCallback();
        } else {
            fragment.getEnterTransitionCallback();
        }
    }

    private static boolean g(FragmentTransitionImpl fragmentTransitionImpl, List<Object> list) {
        int size = list.size();
        for (int i10 = 0; i10 < size; i10++) {
            if (!fragmentTransitionImpl.e(list.get(i10))) {
                return false;
            }
        }
        return true;
    }

    static j.a<String, View> h(FragmentTransitionImpl fragmentTransitionImpl, j.a<String, String> aVar, Object obj, h hVar) {
        ArrayList<String> arrayList;
        Fragment fragment = hVar.f3002a;
        View view = fragment.getView();
        if (!aVar.isEmpty() && obj != null && view != null) {
            j.a<String, View> aVar2 = new j.a<>();
            fragmentTransitionImpl.j(aVar2, view);
            BackStackRecord backStackRecord = hVar.f3004c;
            if (hVar.f3003b) {
                fragment.getExitTransitionCallback();
                arrayList = backStackRecord.f2953p;
            } else {
                fragment.getEnterTransitionCallback();
                arrayList = backStackRecord.f2954q;
            }
            if (arrayList != null) {
                aVar2.p(arrayList);
                aVar2.p(aVar.values());
            }
            x(aVar, aVar2);
            return aVar2;
        }
        aVar.clear();
        return null;
    }

    private static j.a<String, View> i(FragmentTransitionImpl fragmentTransitionImpl, j.a<String, String> aVar, Object obj, h hVar) {
        ArrayList<String> arrayList;
        if (!aVar.isEmpty() && obj != null) {
            Fragment fragment = hVar.f3005d;
            j.a<String, View> aVar2 = new j.a<>();
            fragmentTransitionImpl.j(aVar2, fragment.requireView());
            BackStackRecord backStackRecord = hVar.f3007f;
            if (hVar.f3006e) {
                fragment.getEnterTransitionCallback();
                arrayList = backStackRecord.f2954q;
            } else {
                fragment.getExitTransitionCallback();
                arrayList = backStackRecord.f2953p;
            }
            if (arrayList != null) {
                aVar2.p(arrayList);
            }
            aVar.p(aVar2.keySet());
            return aVar2;
        }
        aVar.clear();
        return null;
    }

    private static FragmentTransitionImpl j(Fragment fragment, Fragment fragment2) {
        ArrayList arrayList = new ArrayList();
        if (fragment != null) {
            Object exitTransition = fragment.getExitTransition();
            if (exitTransition != null) {
                arrayList.add(exitTransition);
            }
            Object returnTransition = fragment.getReturnTransition();
            if (returnTransition != null) {
                arrayList.add(returnTransition);
            }
            Object sharedElementReturnTransition = fragment.getSharedElementReturnTransition();
            if (sharedElementReturnTransition != null) {
                arrayList.add(sharedElementReturnTransition);
            }
        }
        if (fragment2 != null) {
            Object enterTransition = fragment2.getEnterTransition();
            if (enterTransition != null) {
                arrayList.add(enterTransition);
            }
            Object reenterTransition = fragment2.getReenterTransition();
            if (reenterTransition != null) {
                arrayList.add(reenterTransition);
            }
            Object sharedElementEnterTransition = fragment2.getSharedElementEnterTransition();
            if (sharedElementEnterTransition != null) {
                arrayList.add(sharedElementEnterTransition);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        FragmentTransitionImpl fragmentTransitionImpl = f2966b;
        if (fragmentTransitionImpl != null && g(fragmentTransitionImpl, arrayList)) {
            return fragmentTransitionImpl;
        }
        FragmentTransitionImpl fragmentTransitionImpl2 = f2967c;
        if (fragmentTransitionImpl2 != null && g(fragmentTransitionImpl2, arrayList)) {
            return fragmentTransitionImpl2;
        }
        if (fragmentTransitionImpl == null && fragmentTransitionImpl2 == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }

    static ArrayList<View> k(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Fragment fragment, ArrayList<View> arrayList, View view) {
        if (obj == null) {
            return null;
        }
        ArrayList<View> arrayList2 = new ArrayList<>();
        View view2 = fragment.getView();
        if (view2 != null) {
            fragmentTransitionImpl.f(arrayList2, view2);
        }
        if (arrayList != null) {
            arrayList2.removeAll(arrayList);
        }
        if (arrayList2.isEmpty()) {
            return arrayList2;
        }
        arrayList2.add(view);
        fragmentTransitionImpl.b(obj, arrayList2);
        return arrayList2;
    }

    private static Object l(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, j.a<String, String> aVar, h hVar, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object t7;
        j.a<String, String> aVar2;
        Object obj3;
        Rect rect;
        Fragment fragment = hVar.f3002a;
        Fragment fragment2 = hVar.f3005d;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z10 = hVar.f3003b;
        if (aVar.isEmpty()) {
            aVar2 = aVar;
            t7 = null;
        } else {
            t7 = t(fragmentTransitionImpl, fragment, fragment2, z10);
            aVar2 = aVar;
        }
        j.a<String, View> i10 = i(fragmentTransitionImpl, aVar2, t7, hVar);
        if (aVar.isEmpty()) {
            obj3 = null;
        } else {
            arrayList.addAll(i10.values());
            obj3 = t7;
        }
        if (obj == null && obj2 == null && obj3 == null) {
            return null;
        }
        f(fragment, fragment2, z10, i10, true);
        if (obj3 != null) {
            rect = new Rect();
            fragmentTransitionImpl.z(obj3, view, arrayList);
            z(fragmentTransitionImpl, obj3, obj2, i10, hVar.f3006e, hVar.f3007f);
            if (obj != null) {
                fragmentTransitionImpl.u(obj, rect);
            }
        } else {
            rect = null;
        }
        OneShotPreDrawListener.a(viewGroup, new f(fragmentTransitionImpl, aVar, obj3, hVar, arrayList2, view, fragment, fragment2, z10, arrayList, obj, rect));
        return obj3;
    }

    private static Object m(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, j.a<String, String> aVar, h hVar, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object obj, Object obj2) {
        Object obj3;
        View view2;
        Rect rect;
        Fragment fragment = hVar.f3002a;
        Fragment fragment2 = hVar.f3005d;
        if (fragment != null) {
            fragment.requireView().setVisibility(0);
        }
        if (fragment == null || fragment2 == null) {
            return null;
        }
        boolean z10 = hVar.f3003b;
        Object t7 = aVar.isEmpty() ? null : t(fragmentTransitionImpl, fragment, fragment2, z10);
        j.a<String, View> i10 = i(fragmentTransitionImpl, aVar, t7, hVar);
        j.a<String, View> h10 = h(fragmentTransitionImpl, aVar, t7, hVar);
        if (aVar.isEmpty()) {
            if (i10 != null) {
                i10.clear();
            }
            if (h10 != null) {
                h10.clear();
            }
            obj3 = null;
        } else {
            a(arrayList, i10, aVar.keySet());
            a(arrayList2, h10, aVar.values());
            obj3 = t7;
        }
        if (obj == null && obj2 == null && obj3 == null) {
            return null;
        }
        f(fragment, fragment2, z10, i10, true);
        if (obj3 != null) {
            arrayList2.add(view);
            fragmentTransitionImpl.z(obj3, view, arrayList);
            z(fragmentTransitionImpl, obj3, obj2, i10, hVar.f3006e, hVar.f3007f);
            Rect rect2 = new Rect();
            View s7 = s(h10, hVar, obj, z10);
            if (s7 != null) {
                fragmentTransitionImpl.u(obj, rect2);
            }
            rect = rect2;
            view2 = s7;
        } else {
            view2 = null;
            rect = null;
        }
        OneShotPreDrawListener.a(viewGroup, new e(fragment, fragment2, z10, h10, view2, fragmentTransitionImpl, rect));
        return obj3;
    }

    private static void n(ViewGroup viewGroup, h hVar, View view, j.a<String, String> aVar, g gVar) {
        Object obj;
        Fragment fragment = hVar.f3002a;
        Fragment fragment2 = hVar.f3005d;
        FragmentTransitionImpl j10 = j(fragment2, fragment);
        if (j10 == null) {
            return;
        }
        boolean z10 = hVar.f3003b;
        boolean z11 = hVar.f3006e;
        Object q10 = q(j10, fragment, z10);
        Object r10 = r(j10, fragment2, z11);
        ArrayList arrayList = new ArrayList();
        ArrayList<View> arrayList2 = new ArrayList<>();
        Object l10 = l(j10, viewGroup, view, aVar, hVar, arrayList, arrayList2, q10, r10);
        if (q10 == null && l10 == null) {
            obj = r10;
            if (obj == null) {
                return;
            }
        } else {
            obj = r10;
        }
        ArrayList<View> k10 = k(j10, obj, fragment2, arrayList, view);
        if (k10 == null || k10.isEmpty()) {
            obj = null;
        }
        Object obj2 = obj;
        j10.a(q10, view);
        Object u7 = u(j10, q10, obj2, l10, fragment, hVar.f3003b);
        if (fragment2 != null && k10 != null && (k10.size() > 0 || arrayList.size() > 0)) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            gVar.b(fragment2, cancellationSignal);
            j10.w(fragment2, u7, cancellationSignal, new c(gVar, fragment2, cancellationSignal));
        }
        if (u7 != null) {
            ArrayList<View> arrayList3 = new ArrayList<>();
            j10.t(u7, q10, arrayList3, obj2, k10, l10, arrayList2);
            y(j10, viewGroup, fragment, view, arrayList2, q10, arrayList3, obj2, k10);
            j10.x(viewGroup, arrayList2, aVar);
            j10.c(viewGroup, u7);
            j10.s(viewGroup, arrayList2, aVar);
        }
    }

    private static void o(ViewGroup viewGroup, h hVar, View view, j.a<String, String> aVar, g gVar) {
        Object obj;
        Fragment fragment = hVar.f3002a;
        Fragment fragment2 = hVar.f3005d;
        FragmentTransitionImpl j10 = j(fragment2, fragment);
        if (j10 == null) {
            return;
        }
        boolean z10 = hVar.f3003b;
        boolean z11 = hVar.f3006e;
        ArrayList<View> arrayList = new ArrayList<>();
        ArrayList<View> arrayList2 = new ArrayList<>();
        Object q10 = q(j10, fragment, z10);
        Object r10 = r(j10, fragment2, z11);
        Object m10 = m(j10, viewGroup, view, aVar, hVar, arrayList2, arrayList, q10, r10);
        if (q10 == null && m10 == null) {
            obj = r10;
            if (obj == null) {
                return;
            }
        } else {
            obj = r10;
        }
        ArrayList<View> k10 = k(j10, obj, fragment2, arrayList2, view);
        ArrayList<View> k11 = k(j10, q10, fragment, arrayList, view);
        A(k11, 4);
        Object u7 = u(j10, q10, obj, m10, fragment, z10);
        if (fragment2 != null && k10 != null && (k10.size() > 0 || arrayList2.size() > 0)) {
            CancellationSignal cancellationSignal = new CancellationSignal();
            gVar.b(fragment2, cancellationSignal);
            j10.w(fragment2, u7, cancellationSignal, new a(gVar, fragment2, cancellationSignal));
        }
        if (u7 != null) {
            v(j10, obj, fragment2, k10);
            ArrayList<String> o10 = j10.o(arrayList);
            j10.t(u7, q10, k11, obj, k10, m10, arrayList);
            j10.c(viewGroup, u7);
            j10.y(viewGroup, arrayList2, arrayList, o10, aVar);
            A(k11, 0);
            j10.A(m10, arrayList2, arrayList);
        }
    }

    private static h p(h hVar, SparseArray<h> sparseArray, int i10) {
        if (hVar != null) {
            return hVar;
        }
        h hVar2 = new h();
        sparseArray.put(i10, hVar2);
        return hVar2;
    }

    private static Object q(FragmentTransitionImpl fragmentTransitionImpl, Fragment fragment, boolean z10) {
        Object enterTransition;
        if (fragment == null) {
            return null;
        }
        if (z10) {
            enterTransition = fragment.getReenterTransition();
        } else {
            enterTransition = fragment.getEnterTransition();
        }
        return fragmentTransitionImpl.g(enterTransition);
    }

    private static Object r(FragmentTransitionImpl fragmentTransitionImpl, Fragment fragment, boolean z10) {
        Object exitTransition;
        if (fragment == null) {
            return null;
        }
        if (z10) {
            exitTransition = fragment.getReturnTransition();
        } else {
            exitTransition = fragment.getExitTransition();
        }
        return fragmentTransitionImpl.g(exitTransition);
    }

    static View s(j.a<String, View> aVar, h hVar, Object obj, boolean z10) {
        ArrayList<String> arrayList;
        String str;
        BackStackRecord backStackRecord = hVar.f3004c;
        if (obj == null || aVar == null || (arrayList = backStackRecord.f2953p) == null || arrayList.isEmpty()) {
            return null;
        }
        if (z10) {
            str = backStackRecord.f2953p.get(0);
        } else {
            str = backStackRecord.f2954q.get(0);
        }
        return aVar.get(str);
    }

    private static Object t(FragmentTransitionImpl fragmentTransitionImpl, Fragment fragment, Fragment fragment2, boolean z10) {
        Object sharedElementEnterTransition;
        if (fragment == null || fragment2 == null) {
            return null;
        }
        if (z10) {
            sharedElementEnterTransition = fragment2.getSharedElementReturnTransition();
        } else {
            sharedElementEnterTransition = fragment.getSharedElementEnterTransition();
        }
        return fragmentTransitionImpl.B(fragmentTransitionImpl.g(sharedElementEnterTransition));
    }

    private static Object u(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Object obj2, Object obj3, Fragment fragment, boolean z10) {
        boolean z11;
        if (obj == null || obj2 == null || fragment == null) {
            z11 = true;
        } else if (z10) {
            z11 = fragment.getAllowReturnTransitionOverlap();
        } else {
            z11 = fragment.getAllowEnterTransitionOverlap();
        }
        if (z11) {
            return fragmentTransitionImpl.n(obj2, obj, obj3);
        }
        return fragmentTransitionImpl.m(obj2, obj, obj3);
    }

    private static void v(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Fragment fragment, ArrayList<View> arrayList) {
        if (fragment != null && obj != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            fragmentTransitionImpl.r(obj, fragment.getView(), arrayList);
            OneShotPreDrawListener.a(fragment.mContainer, new b(arrayList));
        }
    }

    private static FragmentTransitionImpl w() {
        try {
            return (FragmentTransitionImpl) FragmentTransitionSupport.class.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void x(j.a<String, String> aVar, j.a<String, View> aVar2) {
        for (int size = aVar.size() - 1; size >= 0; size--) {
            if (!aVar2.containsKey(aVar.n(size))) {
                aVar.l(size);
            }
        }
    }

    private static void y(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, Fragment fragment, View view, ArrayList<View> arrayList, Object obj, ArrayList<View> arrayList2, Object obj2, ArrayList<View> arrayList3) {
        OneShotPreDrawListener.a(viewGroup, new d(obj, fragmentTransitionImpl, view, fragment, arrayList, arrayList2, arrayList3, obj2));
    }

    private static void z(FragmentTransitionImpl fragmentTransitionImpl, Object obj, Object obj2, j.a<String, View> aVar, boolean z10, BackStackRecord backStackRecord) {
        String str;
        ArrayList<String> arrayList = backStackRecord.f2953p;
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        if (z10) {
            str = backStackRecord.f2954q.get(0);
        } else {
            str = backStackRecord.f2953p.get(0);
        }
        View view = aVar.get(str);
        fragmentTransitionImpl.v(obj, view);
        if (obj2 != null) {
            fragmentTransitionImpl.v(obj2, view);
        }
    }
}
