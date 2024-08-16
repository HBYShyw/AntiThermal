package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.OnBackPressedCallback;
import android.view.OnBackPressedDispatcher;
import android.view.OnBackPressedDispatcherOwner;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.result.ActivityResult;
import android.view.result.ActivityResultCallback;
import android.view.result.ActivityResultLauncher;
import android.view.result.ActivityResultRegistry;
import android.view.result.ActivityResultRegistryOwner;
import android.view.result.IntentSenderRequest;
import androidx.core.os.CancellationSignal;
import androidx.fragment.R$id;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransition;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.h;
import b.ActivityResultContract;
import j.ArraySet;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public abstract class FragmentManager {
    private static boolean O = false;
    static boolean P = true;
    private ActivityResultLauncher<IntentSenderRequest> A;
    private ActivityResultLauncher<String[]> B;
    private boolean D;
    private boolean E;
    private boolean F;
    private boolean G;
    private boolean H;
    private ArrayList<BackStackRecord> I;
    private ArrayList<Boolean> J;
    private ArrayList<Fragment> K;
    private ArrayList<p> L;
    private FragmentManagerViewModel M;

    /* renamed from: b, reason: collision with root package name */
    private boolean f2728b;

    /* renamed from: d, reason: collision with root package name */
    ArrayList<BackStackRecord> f2730d;

    /* renamed from: e, reason: collision with root package name */
    private ArrayList<Fragment> f2731e;

    /* renamed from: g, reason: collision with root package name */
    private OnBackPressedDispatcher f2733g;

    /* renamed from: l, reason: collision with root package name */
    private ArrayList<m> f2738l;

    /* renamed from: r, reason: collision with root package name */
    private FragmentHostCallback<?> f2744r;

    /* renamed from: s, reason: collision with root package name */
    private FragmentContainer f2745s;

    /* renamed from: t, reason: collision with root package name */
    private Fragment f2746t;

    /* renamed from: u, reason: collision with root package name */
    Fragment f2747u;

    /* renamed from: z, reason: collision with root package name */
    private ActivityResultLauncher<Intent> f2752z;

    /* renamed from: a, reason: collision with root package name */
    private final ArrayList<n> f2727a = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    private final FragmentStore f2729c = new FragmentStore();

    /* renamed from: f, reason: collision with root package name */
    private final FragmentLayoutInflaterFactory f2732f = new FragmentLayoutInflaterFactory(this);

    /* renamed from: h, reason: collision with root package name */
    private final OnBackPressedCallback f2734h = new c(false);

    /* renamed from: i, reason: collision with root package name */
    private final AtomicInteger f2735i = new AtomicInteger();

    /* renamed from: j, reason: collision with root package name */
    private final Map<String, Bundle> f2736j = Collections.synchronizedMap(new HashMap());

    /* renamed from: k, reason: collision with root package name */
    private final Map<String, Object> f2737k = Collections.synchronizedMap(new HashMap());

    /* renamed from: m, reason: collision with root package name */
    private Map<Fragment, HashSet<CancellationSignal>> f2739m = Collections.synchronizedMap(new HashMap());

    /* renamed from: n, reason: collision with root package name */
    private final FragmentTransition.g f2740n = new d();

    /* renamed from: o, reason: collision with root package name */
    private final FragmentLifecycleCallbacksDispatcher f2741o = new FragmentLifecycleCallbacksDispatcher(this);

    /* renamed from: p, reason: collision with root package name */
    private final CopyOnWriteArrayList<FragmentOnAttachListener> f2742p = new CopyOnWriteArrayList<>();

    /* renamed from: q, reason: collision with root package name */
    int f2743q = -1;

    /* renamed from: v, reason: collision with root package name */
    private FragmentFactory f2748v = null;

    /* renamed from: w, reason: collision with root package name */
    private FragmentFactory f2749w = new e();

    /* renamed from: x, reason: collision with root package name */
    private SpecialEffectsControllerFactory f2750x = null;

    /* renamed from: y, reason: collision with root package name */
    private SpecialEffectsControllerFactory f2751y = new f();
    ArrayDeque<LaunchedFragmentInfo> C = new ArrayDeque<>();
    private Runnable N = new g();

    /* renamed from: androidx.fragment.app.FragmentManager$6, reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass6 implements LifecycleEventObserver {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f2753e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ FragmentResultListener f2754f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ androidx.lifecycle.h f2755g;

        /* renamed from: h, reason: collision with root package name */
        final /* synthetic */ FragmentManager f2756h;

        @Override // androidx.lifecycle.LifecycleEventObserver
        public void a(androidx.lifecycle.o oVar, h.b bVar) {
            Bundle bundle;
            if (bVar == h.b.ON_START && (bundle = (Bundle) this.f2756h.f2736j.get(this.f2753e)) != null) {
                this.f2754f.a(this.f2753e, bundle);
                this.f2756h.r(this.f2753e);
            }
            if (bVar == h.b.ON_DESTROY) {
                this.f2755g.c(this);
                this.f2756h.f2737k.remove(this.f2753e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ActivityResultCallback<ActivityResult> {
        a() {
        }

        @Override // android.view.result.ActivityResultCallback
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(ActivityResult activityResult) {
            LaunchedFragmentInfo pollFirst = FragmentManager.this.C.pollFirst();
            if (pollFirst == null) {
                Log.w("FragmentManager", "No IntentSenders were started for " + this);
                return;
            }
            String str = pollFirst.f2757e;
            int i10 = pollFirst.f2758f;
            Fragment i11 = FragmentManager.this.f2729c.i(str);
            if (i11 == null) {
                Log.w("FragmentManager", "Intent Sender result delivered for unknown Fragment " + str);
                return;
            }
            i11.onActivityResult(i10, activityResult.k(), activityResult.j());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ActivityResultCallback<Map<String, Boolean>> {
        b() {
        }

        @Override // android.view.result.ActivityResultCallback
        @SuppressLint({"SyntheticAccessor"})
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(Map<String, Boolean> map) {
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            ArrayList arrayList = new ArrayList(map.values());
            int[] iArr = new int[arrayList.size()];
            for (int i10 = 0; i10 < arrayList.size(); i10++) {
                iArr[i10] = ((Boolean) arrayList.get(i10)).booleanValue() ? 0 : -1;
            }
            LaunchedFragmentInfo pollFirst = FragmentManager.this.C.pollFirst();
            if (pollFirst == null) {
                Log.w("FragmentManager", "No permissions were requested for " + this);
                return;
            }
            String str = pollFirst.f2757e;
            int i11 = pollFirst.f2758f;
            Fragment i12 = FragmentManager.this.f2729c.i(str);
            if (i12 == null) {
                Log.w("FragmentManager", "Permission request result delivered for unknown Fragment " + str);
                return;
            }
            i12.onRequestPermissionsResult(i11, strArr, iArr);
        }
    }

    /* loaded from: classes.dex */
    class c extends OnBackPressedCallback {
        c(boolean z10) {
            super(z10);
        }

        @Override // android.view.OnBackPressedCallback
        public void b() {
            FragmentManager.this.D0();
        }
    }

    /* loaded from: classes.dex */
    class d implements FragmentTransition.g {
        d() {
        }

        @Override // androidx.fragment.app.FragmentTransition.g
        public void a(Fragment fragment, CancellationSignal cancellationSignal) {
            if (cancellationSignal.b()) {
                return;
            }
            FragmentManager.this.f1(fragment, cancellationSignal);
        }

        @Override // androidx.fragment.app.FragmentTransition.g
        public void b(Fragment fragment, CancellationSignal cancellationSignal) {
            FragmentManager.this.f(fragment, cancellationSignal);
        }
    }

    /* loaded from: classes.dex */
    class e extends FragmentFactory {
        e() {
        }

        @Override // androidx.fragment.app.FragmentFactory
        public Fragment a(ClassLoader classLoader, String str) {
            return FragmentManager.this.v0().b(FragmentManager.this.v0().f(), str, null);
        }
    }

    /* loaded from: classes.dex */
    class f implements SpecialEffectsControllerFactory {
        f() {
        }

        @Override // androidx.fragment.app.SpecialEffectsControllerFactory
        public SpecialEffectsController a(ViewGroup viewGroup) {
            return new DefaultSpecialEffectsController(viewGroup);
        }
    }

    /* loaded from: classes.dex */
    class g implements Runnable {
        g() {
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentManager.this.b0(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f2766a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f2767b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ Fragment f2768c;

        h(ViewGroup viewGroup, View view, Fragment fragment) {
            this.f2766a = viewGroup;
            this.f2767b = view;
            this.f2768c = fragment;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f2766a.endViewTransition(this.f2767b);
            animator.removeListener(this);
            Fragment fragment = this.f2768c;
            View view = fragment.mView;
            if (view == null || !fragment.mHidden) {
                return;
            }
            view.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i implements FragmentOnAttachListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Fragment f2770e;

        i(Fragment fragment) {
            this.f2770e = fragment;
        }

        @Override // androidx.fragment.app.FragmentOnAttachListener
        public void a(FragmentManager fragmentManager, Fragment fragment) {
            this.f2770e.onAttachFragment(fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class j implements ActivityResultCallback<ActivityResult> {
        j() {
        }

        @Override // android.view.result.ActivityResultCallback
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void a(ActivityResult activityResult) {
            LaunchedFragmentInfo pollFirst = FragmentManager.this.C.pollFirst();
            if (pollFirst == null) {
                Log.w("FragmentManager", "No Activities were started for result for " + this);
                return;
            }
            String str = pollFirst.f2757e;
            int i10 = pollFirst.f2758f;
            Fragment i11 = FragmentManager.this.f2729c.i(str);
            if (i11 == null) {
                Log.w("FragmentManager", "Activity result delivered for unknown Fragment " + str);
                return;
            }
            i11.onActivityResult(i10, activityResult.k(), activityResult.j());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class k extends ActivityResultContract<IntentSenderRequest, ActivityResult> {
        k() {
        }

        @Override // b.ActivityResultContract
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public Intent a(Context context, IntentSenderRequest intentSenderRequest) {
            Bundle bundleExtra;
            Intent intent = new Intent("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST");
            Intent j10 = intentSenderRequest.j();
            if (j10 != null && (bundleExtra = j10.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE")) != null) {
                intent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundleExtra);
                j10.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                if (j10.getBooleanExtra("androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE", false)) {
                    intentSenderRequest = new IntentSenderRequest.b(intentSenderRequest.m()).b(null).c(intentSenderRequest.l(), intentSenderRequest.k()).a();
                }
            }
            intent.putExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST", intentSenderRequest);
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "CreateIntent created the following intent: " + intent);
            }
            return intent;
        }

        @Override // b.ActivityResultContract
        /* renamed from: e, reason: merged with bridge method [inline-methods] */
        public ActivityResult c(int i10, Intent intent) {
            return new ActivityResult(i10, intent);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class l {
        @Deprecated
        public void a(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void b(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void c(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void d(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void e(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void f(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void g(FragmentManager fragmentManager, Fragment fragment, Context context) {
        }

        public void h(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void i(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void j(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        }

        public void k(FragmentManager fragmentManager, Fragment fragment) {
        }

        public void l(FragmentManager fragmentManager, Fragment fragment) {
        }

        public abstract void m(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle);

        public void n(FragmentManager fragmentManager, Fragment fragment) {
        }
    }

    /* loaded from: classes.dex */
    public interface m {
        void a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface n {
        boolean a(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* loaded from: classes.dex */
    private class o implements n {

        /* renamed from: a, reason: collision with root package name */
        final String f2773a;

        /* renamed from: b, reason: collision with root package name */
        final int f2774b;

        /* renamed from: c, reason: collision with root package name */
        final int f2775c;

        o(String str, int i10, int i11) {
            this.f2773a = str;
            this.f2774b = i10;
            this.f2775c = i11;
        }

        @Override // androidx.fragment.app.FragmentManager.n
        public boolean a(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
            Fragment fragment = FragmentManager.this.f2747u;
            if (fragment == null || this.f2774b >= 0 || this.f2773a != null || !fragment.getChildFragmentManager().Z0()) {
                return FragmentManager.this.b1(arrayList, arrayList2, this.f2773a, this.f2774b, this.f2775c);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class p implements Fragment.l {

        /* renamed from: a, reason: collision with root package name */
        final boolean f2777a;

        /* renamed from: b, reason: collision with root package name */
        final BackStackRecord f2778b;

        /* renamed from: c, reason: collision with root package name */
        private int f2779c;

        p(BackStackRecord backStackRecord, boolean z10) {
            this.f2777a = z10;
            this.f2778b = backStackRecord;
        }

        @Override // androidx.fragment.app.Fragment.l
        public void a() {
            this.f2779c++;
        }

        @Override // androidx.fragment.app.Fragment.l
        public void b() {
            int i10 = this.f2779c - 1;
            this.f2779c = i10;
            if (i10 != 0) {
                return;
            }
            this.f2778b.f2813t.o1();
        }

        void c() {
            BackStackRecord backStackRecord = this.f2778b;
            backStackRecord.f2813t.u(backStackRecord, this.f2777a, false, false);
        }

        void d() {
            boolean z10 = this.f2779c > 0;
            for (Fragment fragment : this.f2778b.f2813t.u0()) {
                fragment.setOnStartEnterTransitionListener(null);
                if (z10 && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            BackStackRecord backStackRecord = this.f2778b;
            backStackRecord.f2813t.u(backStackRecord, this.f2777a, !z10, true);
        }

        public boolean e() {
            return this.f2779c == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Fragment B0(View view) {
        Object tag = view.getTag(R$id.fragment_container_view_tag);
        if (tag instanceof Fragment) {
            return (Fragment) tag;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean H0(int i10) {
        return O || Log.isLoggable("FragmentManager", i10);
    }

    private boolean I0(Fragment fragment) {
        return (fragment.mHasMenu && fragment.mMenuVisible) || fragment.mChildFragmentManager.o();
    }

    private void M(Fragment fragment) {
        if (fragment == null || !fragment.equals(h0(fragment.mWho))) {
            return;
        }
        fragment.performPrimaryNavigationFragmentChanged();
    }

    private void Q0(ArraySet<Fragment> arraySet) {
        int size = arraySet.size();
        for (int i10 = 0; i10 < size; i10++) {
            Fragment k10 = arraySet.k(i10);
            if (!k10.mAdded) {
                View requireView = k10.requireView();
                k10.mPostponedAlpha = requireView.getAlpha();
                requireView.setAlpha(0.0f);
            }
        }
    }

    private void T(int i10) {
        try {
            this.f2728b = true;
            this.f2729c.d(i10);
            S0(i10, false);
            if (P) {
                Iterator<SpecialEffectsController> it = s().iterator();
                while (it.hasNext()) {
                    it.next().j();
                }
            }
            this.f2728b = false;
            b0(true);
        } catch (Throwable th) {
            this.f2728b = false;
            throw th;
        }
    }

    private void W() {
        if (this.H) {
            this.H = false;
            u1();
        }
    }

    private void Y() {
        if (P) {
            Iterator<SpecialEffectsController> it = s().iterator();
            while (it.hasNext()) {
                it.next().j();
            }
        } else {
            if (this.f2739m.isEmpty()) {
                return;
            }
            for (Fragment fragment : this.f2739m.keySet()) {
                n(fragment);
                T0(fragment);
            }
        }
    }

    private void a0(boolean z10) {
        if (!this.f2728b) {
            if (this.f2744r == null) {
                if (this.G) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            if (Looper.myLooper() == this.f2744r.g().getLooper()) {
                if (!z10) {
                    p();
                }
                if (this.I == null) {
                    this.I = new ArrayList<>();
                    this.J = new ArrayList<>();
                }
                this.f2728b = true;
                try {
                    g0(null, null);
                    return;
                } finally {
                    this.f2728b = false;
                }
            }
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        throw new IllegalStateException("FragmentManager is already executing transactions");
    }

    private boolean a1(String str, int i10, int i11) {
        b0(false);
        a0(true);
        Fragment fragment = this.f2747u;
        if (fragment != null && i10 < 0 && str == null && fragment.getChildFragmentManager().Z0()) {
            return true;
        }
        boolean b12 = b1(this.I, this.J, str, i10, i11);
        if (b12) {
            this.f2728b = true;
            try {
                h1(this.I, this.J);
            } finally {
                q();
            }
        }
        x1();
        W();
        this.f2729c.b();
        return b12;
    }

    private int c1(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i10, int i11, ArraySet<Fragment> arraySet) {
        int i12 = i11;
        for (int i13 = i11 - 1; i13 >= i10; i13--) {
            BackStackRecord backStackRecord = arrayList.get(i13);
            boolean booleanValue = arrayList2.get(i13).booleanValue();
            if (backStackRecord.G() && !backStackRecord.E(arrayList, i13 + 1, i11)) {
                if (this.L == null) {
                    this.L = new ArrayList<>();
                }
                p pVar = new p(backStackRecord, booleanValue);
                this.L.add(pVar);
                backStackRecord.I(pVar);
                if (booleanValue) {
                    backStackRecord.z();
                } else {
                    backStackRecord.A(false);
                }
                i12--;
                if (i13 != i12) {
                    arrayList.remove(i13);
                    arrayList.add(i12, backStackRecord);
                }
                d(arraySet);
            }
        }
        return i12;
    }

    private void d(ArraySet<Fragment> arraySet) {
        int i10 = this.f2743q;
        if (i10 < 1) {
            return;
        }
        int min = Math.min(i10, 5);
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment.mState < min) {
                U0(fragment, min);
                if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                    arraySet.add(fragment);
                }
            }
        }
    }

    private static void d0(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i10, int i11) {
        while (i10 < i11) {
            BackStackRecord backStackRecord = arrayList.get(i10);
            if (arrayList2.get(i10).booleanValue()) {
                backStackRecord.v(-1);
                backStackRecord.A(i10 == i11 + (-1));
            } else {
                backStackRecord.v(1);
                backStackRecord.z();
            }
            i10++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:96:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0143  */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [boolean, int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void e0(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int i10, int i11) {
        ?? r12;
        int i12;
        boolean z10;
        int i13;
        int i14;
        ArrayList<Boolean> arrayList3;
        int i15;
        ArrayList<Boolean> arrayList4;
        int i16;
        boolean z11;
        int i17;
        boolean z12 = arrayList.get(i10).f2955r;
        ArrayList<Fragment> arrayList5 = this.K;
        if (arrayList5 == null) {
            this.K = new ArrayList<>();
        } else {
            arrayList5.clear();
        }
        this.K.addAll(this.f2729c.n());
        Fragment z02 = z0();
        boolean z13 = false;
        for (int i18 = i10; i18 < i11; i18++) {
            BackStackRecord backStackRecord = arrayList.get(i18);
            if (!arrayList2.get(i18).booleanValue()) {
                z02 = backStackRecord.B(this.K, z02);
            } else {
                z02 = backStackRecord.J(this.K, z02);
            }
            z13 = z13 || backStackRecord.f2946i;
        }
        this.K.clear();
        if (!z12 && this.f2743q >= 1) {
            if (!P) {
                r12 = 1;
                FragmentTransition.B(this.f2744r.f(), this.f2745s, arrayList, arrayList2, i10, i11, false, this.f2740n);
                d0(arrayList, arrayList2, i10, i11);
                if (P) {
                    if (z12) {
                        ArraySet arraySet = new ArraySet();
                        d(arraySet);
                        i12 = r12;
                        z10 = z12;
                        i13 = i11;
                        i14 = i10;
                        arrayList3 = arrayList2;
                        i15 = c1(arrayList, arrayList2, i10, i11, arraySet);
                        Q0(arraySet);
                    } else {
                        i12 = r12;
                        z10 = z12;
                        i13 = i11;
                        i14 = i10;
                        arrayList3 = arrayList2;
                        i15 = i13;
                    }
                    if (i15 == i14 || !z10) {
                        arrayList4 = arrayList3;
                        i16 = i13;
                    } else {
                        if (this.f2743q >= i12) {
                            arrayList4 = arrayList3;
                            int i19 = i15;
                            i16 = i13;
                            z11 = i12;
                            FragmentTransition.B(this.f2744r.f(), this.f2745s, arrayList, arrayList2, i10, i19, true, this.f2740n);
                        } else {
                            arrayList4 = arrayList3;
                            i16 = i13;
                            z11 = i12;
                        }
                        S0(this.f2743q, z11);
                    }
                } else {
                    boolean booleanValue = arrayList2.get(i11 - 1).booleanValue();
                    for (int i20 = i10; i20 < i11; i20++) {
                        BackStackRecord backStackRecord2 = arrayList.get(i20);
                        if (booleanValue) {
                            for (int size = backStackRecord2.f2940c.size() - r12; size >= 0; size--) {
                                Fragment fragment = backStackRecord2.f2940c.get(size).f2958b;
                                if (fragment != null) {
                                    w(fragment).m();
                                }
                            }
                        } else {
                            Iterator<FragmentTransaction.a> it = backStackRecord2.f2940c.iterator();
                            while (it.hasNext()) {
                                Fragment fragment2 = it.next().f2958b;
                                if (fragment2 != null) {
                                    w(fragment2).m();
                                }
                            }
                        }
                    }
                    S0(this.f2743q, r12);
                    for (SpecialEffectsController specialEffectsController : t(arrayList, i10, i11)) {
                        specialEffectsController.r(booleanValue);
                        specialEffectsController.p();
                        specialEffectsController.g();
                    }
                    i16 = i11;
                    arrayList4 = arrayList2;
                }
                for (i17 = i10; i17 < i16; i17++) {
                    BackStackRecord backStackRecord3 = arrayList.get(i17);
                    if (arrayList4.get(i17).booleanValue() && backStackRecord3.f2815v >= 0) {
                        backStackRecord3.f2815v = -1;
                    }
                    backStackRecord3.H();
                }
                if (z13) {
                    return;
                }
                j1();
                return;
            }
            for (int i21 = i10; i21 < i11; i21++) {
                Iterator<FragmentTransaction.a> it2 = arrayList.get(i21).f2940c.iterator();
                while (it2.hasNext()) {
                    Fragment fragment3 = it2.next().f2958b;
                    if (fragment3 != null && fragment3.mFragmentManager != null) {
                        this.f2729c.p(w(fragment3));
                    }
                }
            }
        }
        r12 = 1;
        d0(arrayList, arrayList2, i10, i11);
        if (P) {
        }
        while (i17 < i16) {
        }
        if (z13) {
        }
    }

    private void g0(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        int indexOf;
        int indexOf2;
        ArrayList<p> arrayList3 = this.L;
        int size = arrayList3 == null ? 0 : arrayList3.size();
        int i10 = 0;
        while (i10 < size) {
            p pVar = this.L.get(i10);
            if (arrayList != null && !pVar.f2777a && (indexOf2 = arrayList.indexOf(pVar.f2778b)) != -1 && arrayList2 != null && arrayList2.get(indexOf2).booleanValue()) {
                this.L.remove(i10);
                i10--;
                size--;
                pVar.c();
            } else if (pVar.e() || (arrayList != null && pVar.f2778b.E(arrayList, 0, arrayList.size()))) {
                this.L.remove(i10);
                i10--;
                size--;
                if (arrayList != null && !pVar.f2777a && (indexOf = arrayList.indexOf(pVar.f2778b)) != -1 && arrayList2 != null && arrayList2.get(indexOf).booleanValue()) {
                    pVar.c();
                } else {
                    pVar.d();
                }
            }
            i10++;
        }
    }

    private void h1(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        if (arrayList.isEmpty()) {
            return;
        }
        if (arrayList.size() == arrayList2.size()) {
            g0(arrayList, arrayList2);
            int size = arrayList.size();
            int i10 = 0;
            int i11 = 0;
            while (i10 < size) {
                if (!arrayList.get(i10).f2955r) {
                    if (i11 != i10) {
                        e0(arrayList, arrayList2, i11, i10);
                    }
                    i11 = i10 + 1;
                    if (arrayList2.get(i10).booleanValue()) {
                        while (i11 < size && arrayList2.get(i11).booleanValue() && !arrayList.get(i11).f2955r) {
                            i11++;
                        }
                    }
                    e0(arrayList, arrayList2, i10, i11);
                    i10 = i11 - 1;
                }
                i10++;
            }
            if (i11 != size) {
                e0(arrayList, arrayList2, i11, size);
                return;
            }
            return;
        }
        throw new IllegalStateException("Internal error with the back stack records");
    }

    private void j1() {
        if (this.f2738l != null) {
            for (int i10 = 0; i10 < this.f2738l.size(); i10++) {
                this.f2738l.get(i10).a();
            }
        }
    }

    private void l0() {
        if (P) {
            Iterator<SpecialEffectsController> it = s().iterator();
            while (it.hasNext()) {
                it.next().k();
            }
        } else if (this.L != null) {
            while (!this.L.isEmpty()) {
                this.L.remove(0).d();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int l1(int i10) {
        if (i10 == 4097) {
            return 8194;
        }
        if (i10 != 4099) {
            return i10 != 8194 ? 0 : 4097;
        }
        return 4099;
    }

    private boolean m0(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2) {
        synchronized (this.f2727a) {
            if (this.f2727a.isEmpty()) {
                return false;
            }
            int size = this.f2727a.size();
            boolean z10 = false;
            for (int i10 = 0; i10 < size; i10++) {
                z10 |= this.f2727a.get(i10).a(arrayList, arrayList2);
            }
            this.f2727a.clear();
            this.f2744r.g().removeCallbacks(this.N);
            return z10;
        }
    }

    private void n(Fragment fragment) {
        HashSet<CancellationSignal> hashSet = this.f2739m.get(fragment);
        if (hashSet != null) {
            Iterator<CancellationSignal> it = hashSet.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
            hashSet.clear();
            x(fragment);
            this.f2739m.remove(fragment);
        }
    }

    private FragmentManagerViewModel o0(Fragment fragment) {
        return this.M.i(fragment);
    }

    private void p() {
        if (M0()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    private void q() {
        this.f2728b = false;
        this.J.clear();
        this.I.clear();
    }

    private ViewGroup r0(Fragment fragment) {
        ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup != null) {
            return viewGroup;
        }
        if (fragment.mContainerId > 0 && this.f2745s.d()) {
            View c10 = this.f2745s.c(fragment.mContainerId);
            if (c10 instanceof ViewGroup) {
                return (ViewGroup) c10;
            }
        }
        return null;
    }

    private Set<SpecialEffectsController> s() {
        HashSet hashSet = new HashSet();
        Iterator<FragmentStateManager> it = this.f2729c.k().iterator();
        while (it.hasNext()) {
            ViewGroup viewGroup = it.next().k().mContainer;
            if (viewGroup != null) {
                hashSet.add(SpecialEffectsController.o(viewGroup, A0()));
            }
        }
        return hashSet;
    }

    private void s1(Fragment fragment) {
        ViewGroup r02 = r0(fragment);
        if (r02 == null || fragment.getEnterAnim() + fragment.getExitAnim() + fragment.getPopEnterAnim() + fragment.getPopExitAnim() <= 0) {
            return;
        }
        int i10 = R$id.visible_removing_fragment_view_tag;
        if (r02.getTag(i10) == null) {
            r02.setTag(i10, fragment);
        }
        ((Fragment) r02.getTag(i10)).setPopDirection(fragment.getPopDirection());
    }

    private Set<SpecialEffectsController> t(ArrayList<BackStackRecord> arrayList, int i10, int i11) {
        ViewGroup viewGroup;
        HashSet hashSet = new HashSet();
        while (i10 < i11) {
            Iterator<FragmentTransaction.a> it = arrayList.get(i10).f2940c.iterator();
            while (it.hasNext()) {
                Fragment fragment = it.next().f2958b;
                if (fragment != null && (viewGroup = fragment.mContainer) != null) {
                    hashSet.add(SpecialEffectsController.n(viewGroup, this));
                }
            }
            i10++;
        }
        return hashSet;
    }

    private void u1() {
        Iterator<FragmentStateManager> it = this.f2729c.k().iterator();
        while (it.hasNext()) {
            X0(it.next());
        }
    }

    private void v(Fragment fragment) {
        Animator animator;
        if (fragment.mView != null) {
            FragmentAnim.d c10 = FragmentAnim.c(this.f2744r.f(), fragment, !fragment.mHidden, fragment.getPopDirection());
            if (c10 != null && (animator = c10.f2892b) != null) {
                animator.setTarget(fragment.mView);
                if (fragment.mHidden) {
                    if (fragment.isHideReplaced()) {
                        fragment.setHideReplaced(false);
                    } else {
                        ViewGroup viewGroup = fragment.mContainer;
                        View view = fragment.mView;
                        viewGroup.startViewTransition(view);
                        c10.f2892b.addListener(new h(viewGroup, view, fragment));
                    }
                } else {
                    fragment.mView.setVisibility(0);
                }
                c10.f2892b.start();
            } else {
                if (c10 != null) {
                    fragment.mView.startAnimation(c10.f2891a);
                    c10.f2891a.start();
                }
                fragment.mView.setVisibility((!fragment.mHidden || fragment.isHideReplaced()) ? 0 : 8);
                if (fragment.isHideReplaced()) {
                    fragment.setHideReplaced(false);
                }
            }
        }
        F0(fragment);
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }

    private void v1(RuntimeException runtimeException) {
        Log.e("FragmentManager", runtimeException.getMessage());
        Log.e("FragmentManager", "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        FragmentHostCallback<?> fragmentHostCallback = this.f2744r;
        if (fragmentHostCallback != null) {
            try {
                fragmentHostCallback.h("  ", null, printWriter, new String[0]);
                throw runtimeException;
            } catch (Exception e10) {
                Log.e("FragmentManager", "Failed dumping state", e10);
                throw runtimeException;
            }
        }
        try {
            X("  ", null, printWriter, new String[0]);
            throw runtimeException;
        } catch (Exception e11) {
            Log.e("FragmentManager", "Failed dumping state", e11);
            throw runtimeException;
        }
    }

    private void x(Fragment fragment) {
        fragment.performDestroyView();
        this.f2741o.n(fragment, false);
        fragment.mContainer = null;
        fragment.mView = null;
        fragment.mViewLifecycleOwner = null;
        fragment.mViewLifecycleOwnerLiveData.m(null);
        fragment.mInLayout = false;
    }

    private void x1() {
        synchronized (this.f2727a) {
            if (!this.f2727a.isEmpty()) {
                this.f2734h.f(true);
            } else {
                this.f2734h.f(n0() > 0 && K0(this.f2746t));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        T(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SpecialEffectsControllerFactory A0() {
        SpecialEffectsControllerFactory specialEffectsControllerFactory = this.f2750x;
        if (specialEffectsControllerFactory != null) {
            return specialEffectsControllerFactory;
        }
        Fragment fragment = this.f2746t;
        if (fragment != null) {
            return fragment.mFragmentManager.A0();
        }
        return this.f2751y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(Configuration configuration) {
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean C(MenuItem menuItem) {
        if (this.f2743q < 1) {
            return false;
        }
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewModelStore C0(Fragment fragment) {
        return this.M.l(fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        T(1);
    }

    void D0() {
        b0(true);
        if (this.f2734h.c()) {
            Z0();
        } else {
            this.f2733g.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean E(Menu menu, MenuInflater menuInflater) {
        if (this.f2743q < 1) {
            return false;
        }
        ArrayList<Fragment> arrayList = null;
        boolean z10 = false;
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null && J0(fragment) && fragment.performCreateOptionsMenu(menu, menuInflater)) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(fragment);
                z10 = true;
            }
        }
        if (this.f2731e != null) {
            for (int i10 = 0; i10 < this.f2731e.size(); i10++) {
                Fragment fragment2 = this.f2731e.get(i10);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.f2731e = arrayList;
        return z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E0(Fragment fragment) {
        if (H0(2)) {
            Log.v("FragmentManager", "hide: " + fragment);
        }
        if (fragment.mHidden) {
            return;
        }
        fragment.mHidden = true;
        fragment.mHiddenChanged = true ^ fragment.mHiddenChanged;
        s1(fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F() {
        this.G = true;
        b0(true);
        Y();
        T(-1);
        this.f2744r = null;
        this.f2745s = null;
        this.f2746t = null;
        if (this.f2733g != null) {
            this.f2734h.d();
            this.f2733g = null;
        }
        ActivityResultLauncher<Intent> activityResultLauncher = this.f2752z;
        if (activityResultLauncher != null) {
            activityResultLauncher.c();
            this.A.c();
            this.B.c();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F0(Fragment fragment) {
        if (fragment.mAdded && I0(fragment)) {
            this.D = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G() {
        T(1);
    }

    public boolean G0() {
        return this.G;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H() {
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null) {
                fragment.performLowMemory();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(boolean z10) {
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null) {
                fragment.performMultiWindowModeChanged(z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(Fragment fragment) {
        Iterator<FragmentOnAttachListener> it = this.f2742p.iterator();
        while (it.hasNext()) {
            it.next().a(this, fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean J0(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return fragment.isMenuVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean K(MenuItem menuItem) {
        if (this.f2743q < 1) {
            return false;
        }
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean K0(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        FragmentManager fragmentManager = fragment.mFragmentManager;
        return fragment.equals(fragmentManager.z0()) && K0(fragmentManager.f2746t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(Menu menu) {
        if (this.f2743q < 1) {
            return;
        }
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean L0(int i10) {
        return this.f2743q >= i10;
    }

    public boolean M0() {
        return this.E || this.F;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N() {
        T(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N0(Fragment fragment, String[] strArr, int i10) {
        if (this.B != null) {
            this.C.addLast(new LaunchedFragmentInfo(fragment.mWho, i10));
            this.B.a(strArr);
            return;
        }
        this.f2744r.k(fragment, strArr, i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O(boolean z10) {
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null) {
                fragment.performPictureInPictureModeChanged(z10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O0(Fragment fragment, @SuppressLint({"UnknownNullness"}) Intent intent, int i10, Bundle bundle) {
        if (this.f2752z != null) {
            this.C.addLast(new LaunchedFragmentInfo(fragment.mWho, i10));
            if (intent != null && bundle != null) {
                intent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
            }
            this.f2752z.a(intent);
            return;
        }
        this.f2744r.n(fragment, intent, i10, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean P(Menu menu) {
        boolean z10 = false;
        if (this.f2743q < 1) {
            return false;
        }
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null && J0(fragment) && fragment.performPrepareOptionsMenu(menu)) {
                z10 = true;
            }
        }
        return z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P0(Fragment fragment, @SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i10, Intent intent, int i11, int i12, int i13, Bundle bundle) {
        Intent intent2;
        if (this.A != null) {
            if (bundle != null) {
                if (intent == null) {
                    intent2 = new Intent();
                    intent2.putExtra("androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE", true);
                } else {
                    intent2 = intent;
                }
                if (H0(2)) {
                    Log.v("FragmentManager", "ActivityOptions " + bundle + " were added to fillInIntent " + intent2 + " for fragment " + fragment);
                }
                intent2.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
            } else {
                intent2 = intent;
            }
            IntentSenderRequest a10 = new IntentSenderRequest.b(intentSender).b(intent2).c(i12, i11).a();
            this.C.addLast(new LaunchedFragmentInfo(fragment.mWho, i10));
            if (H0(2)) {
                Log.v("FragmentManager", "Fragment " + fragment + "is launching an IntentSender for result ");
            }
            this.A.a(a10);
            return;
        }
        this.f2744r.o(fragment, intentSender, i10, intent, i11, i12, i13, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Q() {
        x1();
        M(this.f2747u);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        T(7);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R0(Fragment fragment) {
        if (!this.f2729c.c(fragment.mWho)) {
            if (H0(3)) {
                Log.d("FragmentManager", "Ignoring moving " + fragment + " to state " + this.f2743q + "since it is not added to " + this);
                return;
            }
            return;
        }
        T0(fragment);
        View view = fragment.mView;
        if (view != null && fragment.mIsNewlyAdded && fragment.mContainer != null) {
            float f10 = fragment.mPostponedAlpha;
            if (f10 > 0.0f) {
                view.setAlpha(f10);
            }
            fragment.mPostponedAlpha = 0.0f;
            fragment.mIsNewlyAdded = false;
            FragmentAnim.d c10 = FragmentAnim.c(this.f2744r.f(), fragment, true, fragment.getPopDirection());
            if (c10 != null) {
                Animation animation = c10.f2891a;
                if (animation != null) {
                    fragment.mView.startAnimation(animation);
                } else {
                    c10.f2892b.setTarget(fragment.mView);
                    c10.f2892b.start();
                }
            }
        }
        if (fragment.mHiddenChanged) {
            v(fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void S() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        T(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void S0(int i10, boolean z10) {
        FragmentHostCallback<?> fragmentHostCallback;
        if (this.f2744r == null && i10 != -1) {
            throw new IllegalStateException("No activity");
        }
        if (z10 || i10 != this.f2743q) {
            this.f2743q = i10;
            if (P) {
                this.f2729c.r();
            } else {
                Iterator<Fragment> it = this.f2729c.n().iterator();
                while (it.hasNext()) {
                    R0(it.next());
                }
                for (FragmentStateManager fragmentStateManager : this.f2729c.k()) {
                    Fragment k10 = fragmentStateManager.k();
                    if (!k10.mIsNewlyAdded) {
                        R0(k10);
                    }
                    if (k10.mRemoving && !k10.isInBackStack()) {
                        this.f2729c.q(fragmentStateManager);
                    }
                }
            }
            u1();
            if (this.D && (fragmentHostCallback = this.f2744r) != null && this.f2743q == 7) {
                fragmentHostCallback.p();
                this.D = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T0(Fragment fragment) {
        U0(fragment, this.f2743q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U() {
        this.F = true;
        this.M.o(true);
        T(4);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0053, code lost:
    
        if (r10 != 5) goto L101;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void U0(Fragment fragment, int i10) {
        ViewGroup viewGroup;
        FragmentStateManager m10 = this.f2729c.m(fragment.mWho);
        int i11 = 1;
        if (m10 == null) {
            m10 = new FragmentStateManager(this.f2741o, this.f2729c, fragment);
            m10.u(1);
        }
        if (fragment.mFromLayout && fragment.mInLayout && fragment.mState == 2) {
            i10 = Math.max(i10, 2);
        }
        int min = Math.min(i10, m10.d());
        int i12 = fragment.mState;
        if (i12 <= min) {
            if (i12 < min && !this.f2739m.isEmpty()) {
                n(fragment);
            }
            int i13 = fragment.mState;
            if (i13 != -1) {
                if (i13 != 0) {
                    if (i13 != 1) {
                        if (i13 != 2) {
                            if (i13 != 4) {
                            }
                            if (min > 4) {
                                m10.v();
                            }
                            if (min > 5) {
                                m10.p();
                            }
                        }
                        if (min > 2) {
                            m10.a();
                        }
                        if (min > 4) {
                        }
                        if (min > 5) {
                        }
                    }
                    if (min > -1) {
                        m10.j();
                    }
                    if (min > 1) {
                        m10.f();
                    }
                    if (min > 2) {
                    }
                    if (min > 4) {
                    }
                    if (min > 5) {
                    }
                }
            } else if (min > -1) {
                m10.c();
            }
            if (min > 0) {
                m10.e();
            }
            if (min > -1) {
            }
            if (min > 1) {
            }
            if (min > 2) {
            }
            if (min > 4) {
            }
            if (min > 5) {
            }
        } else if (i12 > min) {
            if (i12 != 0) {
                if (i12 != 1) {
                    if (i12 != 2) {
                        if (i12 != 4) {
                            if (i12 != 5) {
                                if (i12 == 7) {
                                    if (min < 7) {
                                        m10.n();
                                    }
                                }
                            }
                            if (min < 5) {
                                m10.w();
                            }
                        }
                        if (min < 4) {
                            if (H0(3)) {
                                Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + fragment);
                            }
                            if (fragment.mView != null && this.f2744r.l(fragment) && fragment.mSavedViewState == null) {
                                m10.t();
                            }
                        }
                    }
                    if (min < 2) {
                        FragmentAnim.d dVar = null;
                        View view = fragment.mView;
                        if (view != null && (viewGroup = fragment.mContainer) != null) {
                            viewGroup.endViewTransition(view);
                            fragment.mView.clearAnimation();
                            if (!fragment.isRemovingParent()) {
                                if (this.f2743q > -1 && !this.G && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f) {
                                    dVar = FragmentAnim.c(this.f2744r.f(), fragment, false, fragment.getPopDirection());
                                }
                                fragment.mPostponedAlpha = 0.0f;
                                ViewGroup viewGroup2 = fragment.mContainer;
                                View view2 = fragment.mView;
                                if (dVar != null) {
                                    FragmentAnim.a(fragment, dVar, this.f2740n);
                                }
                                viewGroup2.removeView(view2);
                                if (H0(2)) {
                                    Log.v("FragmentManager", "Removing view " + view2 + " for fragment " + fragment + " from container " + viewGroup2);
                                }
                                if (viewGroup2 != fragment.mContainer) {
                                    return;
                                }
                            }
                        }
                        if (this.f2739m.get(fragment) == null) {
                            m10.h();
                        }
                    }
                }
                if (min < 1) {
                    if (this.f2739m.get(fragment) == null) {
                        m10.g();
                    }
                    if (i11 < 0) {
                        m10.i();
                    }
                    min = i11;
                }
            }
            i11 = min;
            if (i11 < 0) {
            }
            min = i11;
        }
        if (fragment.mState != min) {
            if (H0(3)) {
                Log.d("FragmentManager", "moveToState: Fragment state for " + fragment + " not updated inline; expected state " + min + " found " + fragment.mState);
            }
            fragment.mState = min;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V() {
        T(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V0() {
        if (this.f2744r == null) {
            return;
        }
        this.E = false;
        this.F = false;
        this.M.o(false);
        for (Fragment fragment : this.f2729c.n()) {
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void W0(FragmentContainerView fragmentContainerView) {
        View view;
        for (FragmentStateManager fragmentStateManager : this.f2729c.k()) {
            Fragment k10 = fragmentStateManager.k();
            if (k10.mContainerId == fragmentContainerView.getId() && (view = k10.mView) != null && view.getParent() == null) {
                k10.mContainer = fragmentContainerView;
                fragmentStateManager.b();
            }
        }
    }

    public void X(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        String str2 = str + "    ";
        this.f2729c.e(str, fileDescriptor, printWriter, strArr);
        ArrayList<Fragment> arrayList = this.f2731e;
        if (arrayList != null && (size2 = arrayList.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i10 = 0; i10 < size2; i10++) {
                Fragment fragment = this.f2731e.get(i10);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i10);
                printWriter.print(": ");
                printWriter.println(fragment.toString());
            }
        }
        ArrayList<BackStackRecord> arrayList2 = this.f2730d;
        if (arrayList2 != null && (size = arrayList2.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i11 = 0; i11 < size; i11++) {
                BackStackRecord backStackRecord = this.f2730d.get(i11);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i11);
                printWriter.print(": ");
                printWriter.println(backStackRecord.toString());
                backStackRecord.x(str2, printWriter);
            }
        }
        printWriter.print(str);
        printWriter.println("Back Stack Index: " + this.f2735i.get());
        synchronized (this.f2727a) {
            int size3 = this.f2727a.size();
            if (size3 > 0) {
                printWriter.print(str);
                printWriter.println("Pending Actions:");
                for (int i12 = 0; i12 < size3; i12++) {
                    n nVar = this.f2727a.get(i12);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i12);
                    printWriter.print(": ");
                    printWriter.println(nVar);
                }
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.f2744r);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.f2745s);
        if (this.f2746t != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.f2746t);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.f2743q);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.E);
        printWriter.print(" mStopped=");
        printWriter.print(this.F);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.G);
        if (this.D) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.D);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X0(FragmentStateManager fragmentStateManager) {
        Fragment k10 = fragmentStateManager.k();
        if (k10.mDeferStart) {
            if (this.f2728b) {
                this.H = true;
                return;
            }
            k10.mDeferStart = false;
            if (P) {
                fragmentStateManager.m();
            } else {
                T0(k10);
            }
        }
    }

    public void Y0(int i10, int i11) {
        if (i10 >= 0) {
            Z(new o(null, i10, i11), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Z(n nVar, boolean z10) {
        if (!z10) {
            if (this.f2744r == null) {
                if (this.G) {
                    throw new IllegalStateException("FragmentManager has been destroyed");
                }
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            p();
        }
        synchronized (this.f2727a) {
            if (this.f2744r == null) {
                if (!z10) {
                    throw new IllegalStateException("Activity has been destroyed");
                }
            } else {
                this.f2727a.add(nVar);
                o1();
            }
        }
    }

    public boolean Z0() {
        return a1(null, -1, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b0(boolean z10) {
        a0(z10);
        boolean z11 = false;
        while (m0(this.I, this.J)) {
            this.f2728b = true;
            try {
                h1(this.I, this.J);
                q();
                z11 = true;
            } catch (Throwable th) {
                q();
                throw th;
            }
        }
        x1();
        W();
        this.f2729c.b();
        return z11;
    }

    boolean b1(ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, String str, int i10, int i11) {
        int i12;
        ArrayList<BackStackRecord> arrayList3 = this.f2730d;
        if (arrayList3 == null) {
            return false;
        }
        if (str == null && i10 < 0 && (i11 & 1) == 0) {
            int size = arrayList3.size() - 1;
            if (size < 0) {
                return false;
            }
            arrayList.add(this.f2730d.remove(size));
            arrayList2.add(Boolean.TRUE);
        } else {
            if (str != null || i10 >= 0) {
                int size2 = arrayList3.size() - 1;
                while (size2 >= 0) {
                    BackStackRecord backStackRecord = this.f2730d.get(size2);
                    if ((str != null && str.equals(backStackRecord.C())) || (i10 >= 0 && i10 == backStackRecord.f2815v)) {
                        break;
                    }
                    size2--;
                }
                if (size2 < 0) {
                    return false;
                }
                if ((i11 & 1) != 0) {
                    while (true) {
                        size2--;
                        if (size2 < 0) {
                            break;
                        }
                        BackStackRecord backStackRecord2 = this.f2730d.get(size2);
                        if (str == null || !str.equals(backStackRecord2.C())) {
                            if (i10 < 0 || i10 != backStackRecord2.f2815v) {
                                break;
                            }
                        }
                    }
                }
                i12 = size2;
            } else {
                i12 = -1;
            }
            if (i12 == this.f2730d.size() - 1) {
                return false;
            }
            for (int size3 = this.f2730d.size() - 1; size3 > i12; size3--) {
                arrayList.add(this.f2730d.remove(size3));
                arrayList2.add(Boolean.TRUE);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c0(n nVar, boolean z10) {
        if (z10 && (this.f2744r == null || this.G)) {
            return;
        }
        a0(z10);
        if (nVar.a(this.I, this.J)) {
            this.f2728b = true;
            try {
                h1(this.I, this.J);
            } finally {
                q();
            }
        }
        x1();
        W();
        this.f2729c.b();
    }

    public void d1(Bundle bundle, String str, Fragment fragment) {
        if (fragment.mFragmentManager != this) {
            v1(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putString(str, fragment.mWho);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(BackStackRecord backStackRecord) {
        if (this.f2730d == null) {
            this.f2730d = new ArrayList<>();
        }
        this.f2730d.add(backStackRecord);
    }

    public void e1(l lVar, boolean z10) {
        this.f2741o.o(lVar, z10);
    }

    void f(Fragment fragment, CancellationSignal cancellationSignal) {
        if (this.f2739m.get(fragment) == null) {
            this.f2739m.put(fragment, new HashSet<>());
        }
        this.f2739m.get(fragment).add(cancellationSignal);
    }

    public boolean f0() {
        boolean b02 = b0(true);
        l0();
        return b02;
    }

    void f1(Fragment fragment, CancellationSignal cancellationSignal) {
        HashSet<CancellationSignal> hashSet = this.f2739m.get(fragment);
        if (hashSet != null && hashSet.remove(cancellationSignal) && hashSet.isEmpty()) {
            this.f2739m.remove(fragment);
            if (fragment.mState < 5) {
                x(fragment);
                T0(fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStateManager g(Fragment fragment) {
        if (H0(2)) {
            Log.v("FragmentManager", "add: " + fragment);
        }
        FragmentStateManager w10 = w(fragment);
        fragment.mFragmentManager = this;
        this.f2729c.p(w10);
        if (!fragment.mDetached) {
            this.f2729c.a(fragment);
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (I0(fragment)) {
                this.D = true;
            }
        }
        return w10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g1(Fragment fragment) {
        if (H0(2)) {
            Log.v("FragmentManager", "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean z10 = !fragment.isInBackStack();
        if (!fragment.mDetached || z10) {
            this.f2729c.s(fragment);
            if (I0(fragment)) {
                this.D = true;
            }
            fragment.mRemoving = true;
            s1(fragment);
        }
    }

    public void h(FragmentOnAttachListener fragmentOnAttachListener) {
        this.f2742p.add(fragmentOnAttachListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment h0(String str) {
        return this.f2729c.f(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(Fragment fragment) {
        this.M.f(fragment);
    }

    public Fragment i0(int i10) {
        return this.f2729c.g(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i1(Fragment fragment) {
        this.M.n(fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j() {
        return this.f2735i.getAndIncrement();
    }

    public Fragment j0(String str) {
        return this.f2729c.h(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @SuppressLint({"SyntheticAccessor"})
    public void k(FragmentHostCallback<?> fragmentHostCallback, FragmentContainer fragmentContainer, Fragment fragment) {
        String str;
        if (this.f2744r == null) {
            this.f2744r = fragmentHostCallback;
            this.f2745s = fragmentContainer;
            this.f2746t = fragment;
            if (fragment != null) {
                h(new i(fragment));
            } else if (fragmentHostCallback instanceof FragmentOnAttachListener) {
                h((FragmentOnAttachListener) fragmentHostCallback);
            }
            if (this.f2746t != null) {
                x1();
            }
            if (fragmentHostCallback instanceof OnBackPressedDispatcherOwner) {
                OnBackPressedDispatcherOwner onBackPressedDispatcherOwner = (OnBackPressedDispatcherOwner) fragmentHostCallback;
                OnBackPressedDispatcher onBackPressedDispatcher = onBackPressedDispatcherOwner.getOnBackPressedDispatcher();
                this.f2733g = onBackPressedDispatcher;
                androidx.lifecycle.o oVar = onBackPressedDispatcherOwner;
                if (fragment != null) {
                    oVar = fragment;
                }
                onBackPressedDispatcher.b(oVar, this.f2734h);
            }
            if (fragment != null) {
                this.M = fragment.mFragmentManager.o0(fragment);
            } else if (fragmentHostCallback instanceof ViewModelStoreOwner) {
                this.M = FragmentManagerViewModel.j(((ViewModelStoreOwner) fragmentHostCallback).getViewModelStore());
            } else {
                this.M = new FragmentManagerViewModel(false);
            }
            this.M.o(M0());
            this.f2729c.x(this.M);
            Object obj = this.f2744r;
            if (obj instanceof ActivityResultRegistryOwner) {
                ActivityResultRegistry activityResultRegistry = ((ActivityResultRegistryOwner) obj).getActivityResultRegistry();
                if (fragment != null) {
                    str = fragment.mWho + ":";
                } else {
                    str = "";
                }
                String str2 = "FragmentManager:" + str;
                this.f2752z = activityResultRegistry.j(str2 + "StartActivityForResult", new b.c(), new j());
                this.A = activityResultRegistry.j(str2 + "StartIntentSenderForResult", new k(), new a());
                this.B = activityResultRegistry.j(str2 + "RequestPermissions", new b.b(), new b());
                return;
            }
            return;
        }
        throw new IllegalStateException("Already attached");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment k0(String str) {
        return this.f2729c.i(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k1(Parcelable parcelable) {
        FragmentStateManager fragmentStateManager;
        if (parcelable == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
        if (fragmentManagerState.f2780e == null) {
            return;
        }
        this.f2729c.t();
        Iterator<FragmentState> it = fragmentManagerState.f2780e.iterator();
        while (it.hasNext()) {
            FragmentState next = it.next();
            if (next != null) {
                Fragment h10 = this.M.h(next.f2789f);
                if (h10 != null) {
                    if (H0(2)) {
                        Log.v("FragmentManager", "restoreSaveState: re-attaching retained " + h10);
                    }
                    fragmentStateManager = new FragmentStateManager(this.f2741o, this.f2729c, h10, next);
                } else {
                    fragmentStateManager = new FragmentStateManager(this.f2741o, this.f2729c, this.f2744r.f().getClassLoader(), s0(), next);
                }
                Fragment k10 = fragmentStateManager.k();
                k10.mFragmentManager = this;
                if (H0(2)) {
                    Log.v("FragmentManager", "restoreSaveState: active (" + k10.mWho + "): " + k10);
                }
                fragmentStateManager.o(this.f2744r.f().getClassLoader());
                this.f2729c.p(fragmentStateManager);
                fragmentStateManager.u(this.f2743q);
            }
        }
        for (Fragment fragment : this.M.k()) {
            if (!this.f2729c.c(fragment.mWho)) {
                if (H0(2)) {
                    Log.v("FragmentManager", "Discarding retained Fragment " + fragment + " that was not found in the set of active Fragments " + fragmentManagerState.f2780e);
                }
                this.M.n(fragment);
                fragment.mFragmentManager = this;
                FragmentStateManager fragmentStateManager2 = new FragmentStateManager(this.f2741o, this.f2729c, fragment);
                fragmentStateManager2.u(1);
                fragmentStateManager2.m();
                fragment.mRemoving = true;
                fragmentStateManager2.m();
            }
        }
        this.f2729c.u(fragmentManagerState.f2781f);
        if (fragmentManagerState.f2782g != null) {
            this.f2730d = new ArrayList<>(fragmentManagerState.f2782g.length);
            int i10 = 0;
            while (true) {
                BackStackState[] backStackStateArr = fragmentManagerState.f2782g;
                if (i10 >= backStackStateArr.length) {
                    break;
                }
                BackStackRecord j10 = backStackStateArr[i10].j(this);
                if (H0(2)) {
                    Log.v("FragmentManager", "restoreAllState: back stack #" + i10 + " (index " + j10.f2815v + "): " + j10);
                    PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                    j10.y("  ", printWriter, false);
                    printWriter.close();
                }
                this.f2730d.add(j10);
                i10++;
            }
        } else {
            this.f2730d = null;
        }
        this.f2735i.set(fragmentManagerState.f2783h);
        String str = fragmentManagerState.f2784i;
        if (str != null) {
            Fragment h02 = h0(str);
            this.f2747u = h02;
            M(h02);
        }
        ArrayList<String> arrayList = fragmentManagerState.f2785j;
        if (arrayList != null) {
            for (int i11 = 0; i11 < arrayList.size(); i11++) {
                Bundle bundle = fragmentManagerState.f2786k.get(i11);
                bundle.setClassLoader(this.f2744r.f().getClassLoader());
                this.f2736j.put(arrayList.get(i11), bundle);
            }
        }
        this.C = new ArrayDeque<>(fragmentManagerState.f2787l);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(Fragment fragment) {
        if (H0(2)) {
            Log.v("FragmentManager", "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            this.f2729c.a(fragment);
            if (H0(2)) {
                Log.v("FragmentManager", "add from attach: " + fragment);
            }
            if (I0(fragment)) {
                this.D = true;
            }
        }
    }

    public FragmentTransaction m() {
        return new BackStackRecord(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Parcelable m1() {
        int size;
        l0();
        Y();
        b0(true);
        this.E = true;
        this.M.o(true);
        ArrayList<FragmentState> v7 = this.f2729c.v();
        BackStackState[] backStackStateArr = null;
        if (v7.isEmpty()) {
            if (H0(2)) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
            }
            return null;
        }
        ArrayList<String> w10 = this.f2729c.w();
        ArrayList<BackStackRecord> arrayList = this.f2730d;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            backStackStateArr = new BackStackState[size];
            for (int i10 = 0; i10 < size; i10++) {
                backStackStateArr[i10] = new BackStackState(this.f2730d.get(i10));
                if (H0(2)) {
                    Log.v("FragmentManager", "saveAllState: adding back stack #" + i10 + ": " + this.f2730d.get(i10));
                }
            }
        }
        FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.f2780e = v7;
        fragmentManagerState.f2781f = w10;
        fragmentManagerState.f2782g = backStackStateArr;
        fragmentManagerState.f2783h = this.f2735i.get();
        Fragment fragment = this.f2747u;
        if (fragment != null) {
            fragmentManagerState.f2784i = fragment.mWho;
        }
        fragmentManagerState.f2785j.addAll(this.f2736j.keySet());
        fragmentManagerState.f2786k.addAll(this.f2736j.values());
        fragmentManagerState.f2787l = new ArrayList<>(this.C);
        return fragmentManagerState;
    }

    public int n0() {
        ArrayList<BackStackRecord> arrayList = this.f2730d;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public Fragment.SavedState n1(Fragment fragment) {
        FragmentStateManager m10 = this.f2729c.m(fragment.mWho);
        if (m10 == null || !m10.k().equals(fragment)) {
            v1(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        return m10.r();
    }

    boolean o() {
        boolean z10 = false;
        for (Fragment fragment : this.f2729c.l()) {
            if (fragment != null) {
                z10 = I0(fragment);
            }
            if (z10) {
                return true;
            }
        }
        return false;
    }

    void o1() {
        synchronized (this.f2727a) {
            ArrayList<p> arrayList = this.L;
            boolean z10 = (arrayList == null || arrayList.isEmpty()) ? false : true;
            boolean z11 = this.f2727a.size() == 1;
            if (z10 || z11) {
                this.f2744r.g().removeCallbacks(this.N);
                this.f2744r.g().post(this.N);
                x1();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentContainer p0() {
        return this.f2745s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p1(Fragment fragment, boolean z10) {
        ViewGroup r02 = r0(fragment);
        if (r02 == null || !(r02 instanceof FragmentContainerView)) {
            return;
        }
        ((FragmentContainerView) r02).setDrawDisappearingViewsLast(!z10);
    }

    public Fragment q0(Bundle bundle, String str) {
        String string = bundle.getString(str);
        if (string == null) {
            return null;
        }
        Fragment h02 = h0(string);
        if (h02 == null) {
            v1(new IllegalStateException("Fragment no longer exists for key " + str + ": unique id " + string));
        }
        return h02;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q1(Fragment fragment, h.c cVar) {
        if (fragment.equals(h0(fragment.mWho)) && (fragment.mHost == null || fragment.mFragmentManager == this)) {
            fragment.mMaxState = cVar;
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    public final void r(String str) {
        this.f2736j.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r1(Fragment fragment) {
        if (fragment != null && (!fragment.equals(h0(fragment.mWho)) || (fragment.mHost != null && fragment.mFragmentManager != this))) {
            throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
        }
        Fragment fragment2 = this.f2747u;
        this.f2747u = fragment;
        M(fragment2);
        M(this.f2747u);
    }

    public FragmentFactory s0() {
        FragmentFactory fragmentFactory = this.f2748v;
        if (fragmentFactory != null) {
            return fragmentFactory;
        }
        Fragment fragment = this.f2746t;
        if (fragment != null) {
            return fragment.mFragmentManager.s0();
        }
        return this.f2749w;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStore t0() {
        return this.f2729c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t1(Fragment fragment) {
        if (H0(2)) {
            Log.v("FragmentManager", "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            fragment.mHiddenChanged = !fragment.mHiddenChanged;
        }
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder(128);
        sb2.append("FragmentManager{");
        sb2.append(Integer.toHexString(System.identityHashCode(this)));
        sb2.append(" in ");
        Fragment fragment = this.f2746t;
        if (fragment != null) {
            sb2.append(fragment.getClass().getSimpleName());
            sb2.append("{");
            sb2.append(Integer.toHexString(System.identityHashCode(this.f2746t)));
            sb2.append("}");
        } else {
            FragmentHostCallback<?> fragmentHostCallback = this.f2744r;
            if (fragmentHostCallback != null) {
                sb2.append(fragmentHostCallback.getClass().getSimpleName());
                sb2.append("{");
                sb2.append(Integer.toHexString(System.identityHashCode(this.f2744r)));
                sb2.append("}");
            } else {
                sb2.append("null");
            }
        }
        sb2.append("}}");
        return sb2.toString();
    }

    void u(BackStackRecord backStackRecord, boolean z10, boolean z11, boolean z12) {
        if (z10) {
            backStackRecord.A(z12);
        } else {
            backStackRecord.z();
        }
        ArrayList arrayList = new ArrayList(1);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList.add(backStackRecord);
        arrayList2.add(Boolean.valueOf(z10));
        if (z11 && this.f2743q >= 1) {
            FragmentTransition.B(this.f2744r.f(), this.f2745s, arrayList, arrayList2, 0, 1, true, this.f2740n);
        }
        if (z12) {
            S0(this.f2743q, true);
        }
        for (Fragment fragment : this.f2729c.l()) {
            if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && backStackRecord.D(fragment.mContainerId)) {
                float f10 = fragment.mPostponedAlpha;
                if (f10 > 0.0f) {
                    fragment.mView.setAlpha(f10);
                }
                if (z12) {
                    fragment.mPostponedAlpha = 0.0f;
                } else {
                    fragment.mPostponedAlpha = -1.0f;
                    fragment.mIsNewlyAdded = false;
                }
            }
        }
    }

    public List<Fragment> u0() {
        return this.f2729c.n();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentHostCallback<?> v0() {
        return this.f2744r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStateManager w(Fragment fragment) {
        FragmentStateManager m10 = this.f2729c.m(fragment.mWho);
        if (m10 != null) {
            return m10;
        }
        FragmentStateManager fragmentStateManager = new FragmentStateManager(this.f2741o, this.f2729c, fragment);
        fragmentStateManager.o(this.f2744r.f().getClassLoader());
        fragmentStateManager.u(this.f2743q);
        return fragmentStateManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LayoutInflater.Factory2 w0() {
        return this.f2732f;
    }

    public void w1(l lVar) {
        this.f2741o.p(lVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentLifecycleCallbacksDispatcher x0() {
        return this.f2741o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(Fragment fragment) {
        if (H0(2)) {
            Log.v("FragmentManager", "detach: " + fragment);
        }
        if (fragment.mDetached) {
            return;
        }
        fragment.mDetached = true;
        if (fragment.mAdded) {
            if (H0(2)) {
                Log.v("FragmentManager", "remove from detach: " + fragment);
            }
            this.f2729c.s(fragment);
            if (I0(fragment)) {
                this.D = true;
            }
            s1(fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment y0() {
        return this.f2746t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        T(4);
    }

    public Fragment z0() {
        return this.f2747u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static class LaunchedFragmentInfo implements Parcelable {
        public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f2757e;

        /* renamed from: f, reason: collision with root package name */
        int f2758f;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<LaunchedFragmentInfo> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public LaunchedFragmentInfo createFromParcel(Parcel parcel) {
                return new LaunchedFragmentInfo(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public LaunchedFragmentInfo[] newArray(int i10) {
                return new LaunchedFragmentInfo[i10];
            }
        }

        LaunchedFragmentInfo(String str, int i10) {
            this.f2757e = str;
            this.f2758f = i10;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeString(this.f2757e);
            parcel.writeInt(this.f2758f);
        }

        LaunchedFragmentInfo(Parcel parcel) {
            this.f2757e = parcel.readString();
            this.f2758f = parcel.readInt();
        }
    }
}
