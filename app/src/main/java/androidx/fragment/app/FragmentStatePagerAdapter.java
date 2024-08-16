package androidx.fragment.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.h;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;

/* compiled from: FragmentStatePagerAdapter.java */
@Deprecated
/* renamed from: androidx.fragment.app.p, reason: use source file name */
/* loaded from: classes.dex */
public abstract class FragmentStatePagerAdapter extends PagerAdapter {

    /* renamed from: a, reason: collision with root package name */
    private final FragmentManager f2928a;

    /* renamed from: b, reason: collision with root package name */
    private final int f2929b;

    /* renamed from: c, reason: collision with root package name */
    private FragmentTransaction f2930c;

    /* renamed from: d, reason: collision with root package name */
    private ArrayList<Fragment.SavedState> f2931d;

    /* renamed from: e, reason: collision with root package name */
    private ArrayList<Fragment> f2932e;

    /* renamed from: f, reason: collision with root package name */
    private Fragment f2933f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f2934g;

    public abstract Fragment a(int i10);

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i10, Object obj) {
        Fragment fragment = (Fragment) obj;
        if (this.f2930c == null) {
            this.f2930c = this.f2928a.m();
        }
        while (this.f2931d.size() <= i10) {
            this.f2931d.add(null);
        }
        this.f2931d.set(i10, fragment.isAdded() ? this.f2928a.n1(fragment) : null);
        this.f2932e.set(i10, null);
        this.f2930c.q(fragment);
        if (fragment.equals(this.f2933f)) {
            this.f2933f = null;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void finishUpdate(ViewGroup viewGroup) {
        FragmentTransaction fragmentTransaction = this.f2930c;
        if (fragmentTransaction != null) {
            if (!this.f2934g) {
                try {
                    this.f2934g = true;
                    fragmentTransaction.l();
                } finally {
                    this.f2934g = false;
                }
            }
            this.f2930c = null;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i10) {
        Fragment.SavedState savedState;
        Fragment fragment;
        if (this.f2932e.size() > i10 && (fragment = this.f2932e.get(i10)) != null) {
            return fragment;
        }
        if (this.f2930c == null) {
            this.f2930c = this.f2928a.m();
        }
        Fragment a10 = a(i10);
        if (this.f2931d.size() > i10 && (savedState = this.f2931d.get(i10)) != null) {
            a10.setInitialSavedState(savedState);
        }
        while (this.f2932e.size() <= i10) {
            this.f2932e.add(null);
        }
        a10.setMenuVisibility(false);
        if (this.f2929b == 0) {
            a10.setUserVisibleHint(false);
        }
        this.f2932e.set(i10, a10);
        this.f2930c.b(viewGroup.getId(), a10);
        if (this.f2929b == 1) {
            this.f2930c.t(a10, h.c.STARTED);
        }
        return a10;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return ((Fragment) obj).getView() == view;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        if (parcelable != null) {
            Bundle bundle = (Bundle) parcelable;
            bundle.setClassLoader(classLoader);
            Parcelable[] parcelableArray = bundle.getParcelableArray("states");
            this.f2931d.clear();
            this.f2932e.clear();
            if (parcelableArray != null) {
                for (Parcelable parcelable2 : parcelableArray) {
                    this.f2931d.add((Fragment.SavedState) parcelable2);
                }
            }
            for (String str : bundle.keySet()) {
                if (str.startsWith("f")) {
                    int parseInt = Integer.parseInt(str.substring(1));
                    Fragment q02 = this.f2928a.q0(bundle, str);
                    if (q02 != null) {
                        while (this.f2932e.size() <= parseInt) {
                            this.f2932e.add(null);
                        }
                        q02.setMenuVisibility(false);
                        this.f2932e.set(parseInt, q02);
                    } else {
                        Log.w("FragmentStatePagerAdapt", "Bad fragment at key " + str);
                    }
                }
            }
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Parcelable saveState() {
        Bundle bundle;
        if (this.f2931d.size() > 0) {
            bundle = new Bundle();
            Fragment.SavedState[] savedStateArr = new Fragment.SavedState[this.f2931d.size()];
            this.f2931d.toArray(savedStateArr);
            bundle.putParcelableArray("states", savedStateArr);
        } else {
            bundle = null;
        }
        for (int i10 = 0; i10 < this.f2932e.size(); i10++) {
            Fragment fragment = this.f2932e.get(i10);
            if (fragment != null && fragment.isAdded()) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                this.f2928a.d1(bundle, "f" + i10, fragment);
            }
        }
        return bundle;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void setPrimaryItem(ViewGroup viewGroup, int i10, Object obj) {
        Fragment fragment = (Fragment) obj;
        Fragment fragment2 = this.f2933f;
        if (fragment != fragment2) {
            if (fragment2 != null) {
                fragment2.setMenuVisibility(false);
                if (this.f2929b == 1) {
                    if (this.f2930c == null) {
                        this.f2930c = this.f2928a.m();
                    }
                    this.f2930c.t(this.f2933f, h.c.STARTED);
                } else {
                    this.f2933f.setUserVisibleHint(false);
                }
            }
            fragment.setMenuVisibility(true);
            if (this.f2929b == 1) {
                if (this.f2930c == null) {
                    this.f2930c = this.f2928a.m();
                }
                this.f2930c.t(fragment, h.c.RESUMED);
            } else {
                fragment.setUserVisibleHint(true);
            }
            this.f2933f = fragment;
        }
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void startUpdate(ViewGroup viewGroup) {
        if (viewGroup.getId() != -1) {
            return;
        }
        throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
    }
}
