package com.coui.appcompat.viewpager.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.h;
import androidx.lifecycle.o;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.StatefulAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.viewpager.COUIViewPager2;
import j.ArraySet;
import j.LongSparseArray;
import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class COUIFragmentStateAdapter extends RecyclerView.h<e> implements StatefulAdapter {

    /* renamed from: a, reason: collision with root package name */
    final h f8108a;

    /* renamed from: b, reason: collision with root package name */
    final FragmentManager f8109b;

    /* renamed from: c, reason: collision with root package name */
    final LongSparseArray<Fragment> f8110c;

    /* renamed from: d, reason: collision with root package name */
    private final LongSparseArray<Fragment.SavedState> f8111d;

    /* renamed from: e, reason: collision with root package name */
    private final LongSparseArray<Integer> f8112e;

    /* renamed from: f, reason: collision with root package name */
    private FragmentMaxLifecycleEnforcer f8113f;

    /* renamed from: g, reason: collision with root package name */
    boolean f8114g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f8115h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class FragmentMaxLifecycleEnforcer {

        /* renamed from: a, reason: collision with root package name */
        private ViewPager2.i f8121a;

        /* renamed from: b, reason: collision with root package name */
        private RecyclerView.j f8122b;

        /* renamed from: c, reason: collision with root package name */
        private LifecycleEventObserver f8123c;

        /* renamed from: d, reason: collision with root package name */
        private COUIViewPager2 f8124d;

        /* renamed from: e, reason: collision with root package name */
        private long f8125e = -1;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a extends ViewPager2.i {
            a() {
            }

            @Override // androidx.viewpager2.widget.ViewPager2.i
            public void a(int i10) {
                FragmentMaxLifecycleEnforcer.this.d(false);
            }

            @Override // androidx.viewpager2.widget.ViewPager2.i
            public void c(int i10) {
                FragmentMaxLifecycleEnforcer.this.d(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class b extends d {
            b() {
                super(null);
            }

            @Override // com.coui.appcompat.viewpager.adapter.COUIFragmentStateAdapter.d, androidx.recyclerview.widget.RecyclerView.j
            public void onChanged() {
                FragmentMaxLifecycleEnforcer.this.d(true);
            }
        }

        FragmentMaxLifecycleEnforcer() {
        }

        private COUIViewPager2 a(RecyclerView recyclerView) {
            ViewParent parent = recyclerView.getParent();
            if (parent instanceof COUIViewPager2) {
                return (COUIViewPager2) parent;
            }
            throw new IllegalStateException("Expected COUIViewPager instance. Got: " + parent);
        }

        void b(RecyclerView recyclerView) {
            this.f8124d = a(recyclerView);
            a aVar = new a();
            this.f8121a = aVar;
            this.f8124d.j(aVar);
            b bVar = new b();
            this.f8122b = bVar;
            COUIFragmentStateAdapter.this.registerAdapterDataObserver(bVar);
            LifecycleEventObserver lifecycleEventObserver = new LifecycleEventObserver() { // from class: com.coui.appcompat.viewpager.adapter.COUIFragmentStateAdapter.FragmentMaxLifecycleEnforcer.3
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void a(o oVar, h.b bVar2) {
                    FragmentMaxLifecycleEnforcer.this.d(false);
                }
            };
            this.f8123c = lifecycleEventObserver;
            COUIFragmentStateAdapter.this.f8108a.a(lifecycleEventObserver);
        }

        void c(RecyclerView recyclerView) {
            a(recyclerView).q(this.f8121a);
            COUIFragmentStateAdapter.this.unregisterAdapterDataObserver(this.f8122b);
            COUIFragmentStateAdapter.this.f8108a.c(this.f8123c);
            this.f8124d = null;
        }

        void d(boolean z10) {
            int currentItem;
            Fragment e10;
            if (COUIFragmentStateAdapter.this.v() || this.f8124d.getScrollState() != 0 || COUIFragmentStateAdapter.this.f8110c.h() || COUIFragmentStateAdapter.this.getItemCount() == 0 || (currentItem = this.f8124d.getCurrentItem()) >= COUIFragmentStateAdapter.this.getItemCount()) {
                return;
            }
            long itemId = COUIFragmentStateAdapter.this.getItemId(currentItem);
            if ((itemId != this.f8125e || z10) && (e10 = COUIFragmentStateAdapter.this.f8110c.e(itemId)) != null && e10.isAdded()) {
                this.f8125e = itemId;
                FragmentTransaction m10 = COUIFragmentStateAdapter.this.f8109b.m();
                Fragment fragment = null;
                for (int i10 = 0; i10 < COUIFragmentStateAdapter.this.f8110c.n(); i10++) {
                    long i11 = COUIFragmentStateAdapter.this.f8110c.i(i10);
                    Fragment o10 = COUIFragmentStateAdapter.this.f8110c.o(i10);
                    if (o10.isAdded()) {
                        if (i11 != this.f8125e) {
                            m10.t(o10, h.c.STARTED);
                        } else {
                            fragment = o10;
                        }
                        o10.setMenuVisibility(i11 == this.f8125e);
                    }
                }
                if (fragment != null) {
                    m10.t(fragment, h.c.RESUMED);
                }
                if (m10.p()) {
                    return;
                }
                m10.k();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements View.OnLayoutChangeListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ FrameLayout f8130a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ e f8131b;

        a(FrameLayout frameLayout, e eVar) {
            this.f8130a = frameLayout;
            this.f8131b = eVar;
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            if (this.f8130a.getParent() != null) {
                this.f8130a.removeOnLayoutChangeListener(this);
                COUIFragmentStateAdapter.this.r(this.f8131b);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends FragmentManager.l {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Fragment f8133a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ FrameLayout f8134b;

        b(Fragment fragment, FrameLayout frameLayout) {
            this.f8133a = fragment;
            this.f8134b = frameLayout;
        }

        @Override // androidx.fragment.app.FragmentManager.l
        public void m(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
            if (fragment == this.f8133a) {
                fragmentManager.w1(this);
                COUIFragmentStateAdapter.this.c(view, this.f8134b);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUIFragmentStateAdapter cOUIFragmentStateAdapter = COUIFragmentStateAdapter.this;
            cOUIFragmentStateAdapter.f8114g = false;
            cOUIFragmentStateAdapter.h();
        }
    }

    /* loaded from: classes.dex */
    private static abstract class d extends RecyclerView.j {
        private d() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public abstract void onChanged();

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeChanged(int i10, int i11) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeInserted(int i10, int i11) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeMoved(int i10, int i11, int i12) {
            onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeRemoved(int i10, int i11) {
            onChanged();
        }

        /* synthetic */ d(a aVar) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public final void onItemRangeChanged(int i10, int i11, Object obj) {
            onChanged();
        }
    }

    /* loaded from: classes.dex */
    public static final class e extends RecyclerView.c0 {
        private e(FrameLayout frameLayout) {
            super(frameLayout);
        }

        static e a(ViewGroup viewGroup) {
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            frameLayout.setId(ViewCompat.i());
            frameLayout.setSaveEnabled(false);
            return new e(frameLayout);
        }

        FrameLayout b() {
            return (FrameLayout) this.itemView;
        }
    }

    public COUIFragmentStateAdapter(FragmentActivity fragmentActivity) {
        this(fragmentActivity.getSupportFragmentManager(), fragmentActivity.getLifecycle());
    }

    private static String f(String str, long j10) {
        return str + j10;
    }

    private void g(int i10) {
        long itemId = getItemId(i10);
        if (this.f8110c.c(itemId)) {
            return;
        }
        Fragment e10 = e(i10);
        e10.setInitialSavedState(this.f8111d.e(itemId));
        this.f8110c.j(itemId, e10);
    }

    private boolean i(long j10) {
        View view;
        if (this.f8112e.c(j10)) {
            return true;
        }
        Fragment e10 = this.f8110c.e(j10);
        return (e10 == null || (view = e10.getView()) == null || view.getParent() == null) ? false : true;
    }

    private static boolean j(String str, String str2) {
        return str.startsWith(str2) && str.length() > str2.length();
    }

    private Long k(int i10) {
        Long l10 = null;
        for (int i11 = 0; i11 < this.f8112e.n(); i11++) {
            if (this.f8112e.o(i11).intValue() == i10) {
                if (l10 == null) {
                    l10 = Long.valueOf(this.f8112e.i(i11));
                } else {
                    throw new IllegalStateException("Design assumption violated: a ViewHolder can only be bound to one item at a time.");
                }
            }
        }
        return l10;
    }

    private static long q(String str, String str2) {
        return Long.parseLong(str.substring(str2.length()));
    }

    private void s(long j10) {
        ViewParent parent;
        Fragment e10 = this.f8110c.e(j10);
        if (e10 == null) {
            return;
        }
        if (e10.getView() != null && (parent = e10.getView().getParent()) != null) {
            ((FrameLayout) parent).removeAllViews();
        }
        if (!d(j10)) {
            this.f8111d.k(j10);
        }
        if (!e10.isAdded()) {
            this.f8110c.k(j10);
            return;
        }
        if (v()) {
            this.f8115h = true;
            return;
        }
        if (e10.isAdded() && d(j10)) {
            this.f8111d.j(j10, this.f8109b.n1(e10));
        }
        this.f8109b.m().q(e10).k();
        this.f8110c.k(j10);
    }

    private void t() {
        final Handler handler = new Handler(Looper.getMainLooper());
        final c cVar = new c();
        this.f8108a.a(new LifecycleEventObserver() { // from class: com.coui.appcompat.viewpager.adapter.COUIFragmentStateAdapter.5
            @Override // androidx.lifecycle.LifecycleEventObserver
            public void a(o oVar, h.b bVar) {
                if (bVar == h.b.ON_DESTROY) {
                    handler.removeCallbacks(cVar);
                    oVar.getLifecycle().c(this);
                }
            }
        });
        handler.postDelayed(cVar, 10000L);
    }

    private void u(Fragment fragment, FrameLayout frameLayout) {
        this.f8109b.e1(new b(fragment, frameLayout), false);
    }

    @Override // androidx.viewpager2.adapter.StatefulAdapter
    public final Parcelable a() {
        Bundle bundle = new Bundle(this.f8110c.n() + this.f8111d.n());
        for (int i10 = 0; i10 < this.f8110c.n(); i10++) {
            long i11 = this.f8110c.i(i10);
            Fragment e10 = this.f8110c.e(i11);
            if (e10 != null && e10.isAdded()) {
                this.f8109b.d1(bundle, f("f#", i11), e10);
            }
        }
        for (int i12 = 0; i12 < this.f8111d.n(); i12++) {
            long i13 = this.f8111d.i(i12);
            if (d(i13)) {
                bundle.putParcelable(f("s#", i13), this.f8111d.e(i13));
            }
        }
        return bundle;
    }

    @Override // androidx.viewpager2.adapter.StatefulAdapter
    public final void b(Parcelable parcelable) {
        if (this.f8111d.h() && this.f8110c.h()) {
            Bundle bundle = (Bundle) parcelable;
            if (bundle.getClassLoader() == null) {
                bundle.setClassLoader(getClass().getClassLoader());
            }
            for (String str : bundle.keySet()) {
                if (j(str, "f#")) {
                    this.f8110c.j(q(str, "f#"), this.f8109b.q0(bundle, str));
                } else if (j(str, "s#")) {
                    long q10 = q(str, "s#");
                    Fragment.SavedState savedState = (Fragment.SavedState) bundle.getParcelable(str);
                    if (d(q10)) {
                        this.f8111d.j(q10, savedState);
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected key in savedState: " + str);
                }
            }
            if (this.f8110c.h()) {
                return;
            }
            this.f8115h = true;
            this.f8114g = true;
            h();
            t();
            return;
        }
        throw new IllegalStateException("Expected the adapter to be 'fresh' while restoring state.");
    }

    void c(View view, FrameLayout frameLayout) {
        if (frameLayout.getChildCount() <= 1) {
            if (view.getParent() == frameLayout) {
                return;
            }
            if (frameLayout.getChildCount() > 0) {
                frameLayout.removeAllViews();
            }
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            frameLayout.addView(view);
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }

    public boolean d(long j10) {
        return j10 >= 0 && j10 < ((long) getItemCount());
    }

    public abstract Fragment e(int i10);

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public long getItemId(int i10) {
        return i10;
    }

    void h() {
        if (!this.f8115h || v()) {
            return;
        }
        ArraySet arraySet = new ArraySet();
        for (int i10 = 0; i10 < this.f8110c.n(); i10++) {
            long i11 = this.f8110c.i(i10);
            if (!d(i11)) {
                arraySet.add(Long.valueOf(i11));
                this.f8112e.k(i11);
            }
        }
        if (!this.f8114g) {
            this.f8115h = false;
            for (int i12 = 0; i12 < this.f8110c.n(); i12++) {
                long i13 = this.f8110c.i(i12);
                if (!i(i13)) {
                    arraySet.add(Long.valueOf(i13));
                }
            }
        }
        Iterator<E> it = arraySet.iterator();
        while (it.hasNext()) {
            s(((Long) it.next()).longValue());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public final void onBindViewHolder(e eVar, int i10) {
        long itemId = eVar.getItemId();
        int id2 = eVar.b().getId();
        Long k10 = k(id2);
        if (k10 != null && k10.longValue() != itemId) {
            s(k10.longValue());
            this.f8112e.k(k10.longValue());
        }
        this.f8112e.j(itemId, Integer.valueOf(id2));
        g(i10);
        FrameLayout b10 = eVar.b();
        if (ViewCompat.P(b10)) {
            if (b10.getParent() == null) {
                b10.addOnLayoutChangeListener(new a(b10, eVar));
            } else {
                throw new IllegalStateException("Design assumption violated.");
            }
        }
        h();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public final e onCreateViewHolder(ViewGroup viewGroup, int i10) {
        return e.a(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: n, reason: merged with bridge method [inline-methods] */
    public final boolean onFailedToRecycleView(e eVar) {
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: o, reason: merged with bridge method [inline-methods] */
    public final void onViewAttachedToWindow(e eVar) {
        r(eVar);
        h();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (this.f8113f == null) {
            FragmentMaxLifecycleEnforcer fragmentMaxLifecycleEnforcer = new FragmentMaxLifecycleEnforcer();
            this.f8113f = fragmentMaxLifecycleEnforcer;
            fragmentMaxLifecycleEnforcer.b(recyclerView);
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.f8113f.c(recyclerView);
        this.f8113f = null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public final void onViewRecycled(e eVar) {
        Long k10 = k(eVar.b().getId());
        if (k10 != null) {
            s(k10.longValue());
            this.f8112e.k(k10.longValue());
        }
    }

    void r(final e eVar) {
        Fragment e10 = this.f8110c.e(eVar.getItemId());
        if (e10 != null) {
            FrameLayout b10 = eVar.b();
            View view = e10.getView();
            if (!e10.isAdded() && view != null) {
                throw new IllegalStateException("Design assumption violated.");
            }
            if (e10.isAdded() && view == null) {
                u(e10, b10);
                return;
            }
            if (e10.isAdded() && view.getParent() != null) {
                if (view.getParent() != b10) {
                    c(view, b10);
                    return;
                }
                return;
            }
            if (e10.isAdded()) {
                c(view, b10);
                return;
            }
            if (!v()) {
                u(e10, b10);
                this.f8109b.m().e(e10, "f" + eVar.getItemId()).t(e10, h.c.STARTED).k();
                this.f8113f.d(false);
                return;
            }
            if (this.f8109b.G0()) {
                return;
            }
            this.f8108a.a(new LifecycleEventObserver() { // from class: com.coui.appcompat.viewpager.adapter.COUIFragmentStateAdapter.2
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void a(o oVar, h.b bVar) {
                    if (COUIFragmentStateAdapter.this.v()) {
                        return;
                    }
                    oVar.getLifecycle().c(this);
                    if (ViewCompat.P(eVar.b())) {
                        COUIFragmentStateAdapter.this.r(eVar);
                    }
                }
            });
            return;
        }
        throw new IllegalStateException("Design assumption violated.");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public final void setHasStableIds(boolean z10) {
        throw new UnsupportedOperationException("Stable Ids are required for the adapter to function properly, and the adapter takes care of setting the flag.");
    }

    boolean v() {
        return this.f8109b.M0();
    }

    public COUIFragmentStateAdapter(FragmentManager fragmentManager, h hVar) {
        this.f8110c = new LongSparseArray<>();
        this.f8111d = new LongSparseArray<>();
        this.f8112e = new LongSparseArray<>();
        this.f8114g = false;
        this.f8115h = false;
        this.f8109b = fragmentManager;
        this.f8108a = hVar;
        super.setHasStableIds(true);
    }
}
