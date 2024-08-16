package androidx.fragment.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import androidx.fragment.R$id;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.SpecialEffectsController;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.h;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FragmentStateManager.java */
/* renamed from: androidx.fragment.app.o, reason: use source file name */
/* loaded from: classes.dex */
public class FragmentStateManager {

    /* renamed from: a, reason: collision with root package name */
    private final FragmentLifecycleCallbacksDispatcher f2920a;

    /* renamed from: b, reason: collision with root package name */
    private final FragmentStore f2921b;

    /* renamed from: c, reason: collision with root package name */
    private final Fragment f2922c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f2923d = false;

    /* renamed from: e, reason: collision with root package name */
    private int f2924e = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentStateManager.java */
    /* renamed from: androidx.fragment.app.o$a */
    /* loaded from: classes.dex */
    public class a implements View.OnAttachStateChangeListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f2925e;

        a(View view) {
            this.f2925e = view;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            this.f2925e.removeOnAttachStateChangeListener(this);
            ViewCompat.h0(this.f2925e);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FragmentStateManager.java */
    /* renamed from: androidx.fragment.app.o$b */
    /* loaded from: classes.dex */
    public static /* synthetic */ class b {

        /* renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f2927a;

        static {
            int[] iArr = new int[h.c.values().length];
            f2927a = iArr;
            try {
                iArr[h.c.RESUMED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f2927a[h.c.STARTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f2927a[h.c.CREATED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f2927a[h.c.INITIALIZED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment) {
        this.f2920a = fragmentLifecycleCallbacksDispatcher;
        this.f2921b = fragmentStore;
        this.f2922c = fragment;
    }

    private boolean l(View view) {
        if (view == this.f2922c.mView) {
            return true;
        }
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent == this.f2922c.mView) {
                return true;
            }
        }
        return false;
    }

    private Bundle q() {
        Bundle bundle = new Bundle();
        this.f2922c.performSaveInstanceState(bundle);
        this.f2920a.j(this.f2922c, bundle, false);
        if (bundle.isEmpty()) {
            bundle = null;
        }
        if (this.f2922c.mView != null) {
            t();
        }
        if (this.f2922c.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", this.f2922c.mSavedViewState);
        }
        if (this.f2922c.mSavedViewRegistryState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBundle("android:view_registry_state", this.f2922c.mSavedViewRegistryState);
        }
        if (!this.f2922c.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("android:user_visible_hint", this.f2922c.mUserVisibleHint);
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "moveto ACTIVITY_CREATED: " + this.f2922c);
        }
        Fragment fragment = this.f2922c;
        fragment.performActivityCreated(fragment.mSavedFragmentState);
        FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.f2920a;
        Fragment fragment2 = this.f2922c;
        fragmentLifecycleCallbacksDispatcher.a(fragment2, fragment2.mSavedFragmentState, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        int j10 = this.f2921b.j(this.f2922c);
        Fragment fragment = this.f2922c;
        fragment.mContainer.addView(fragment.mView, j10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "moveto ATTACHED: " + this.f2922c);
        }
        Fragment fragment = this.f2922c;
        Fragment fragment2 = fragment.mTarget;
        FragmentStateManager fragmentStateManager = null;
        if (fragment2 != null) {
            FragmentStateManager m10 = this.f2921b.m(fragment2.mWho);
            if (m10 != null) {
                Fragment fragment3 = this.f2922c;
                fragment3.mTargetWho = fragment3.mTarget.mWho;
                fragment3.mTarget = null;
                fragmentStateManager = m10;
            } else {
                throw new IllegalStateException("Fragment " + this.f2922c + " declared target fragment " + this.f2922c.mTarget + " that does not belong to this FragmentManager!");
            }
        } else {
            String str = fragment.mTargetWho;
            if (str != null && (fragmentStateManager = this.f2921b.m(str)) == null) {
                throw new IllegalStateException("Fragment " + this.f2922c + " declared target fragment " + this.f2922c.mTargetWho + " that does not belong to this FragmentManager!");
            }
        }
        if (fragmentStateManager != null && (FragmentManager.P || fragmentStateManager.k().mState < 1)) {
            fragmentStateManager.m();
        }
        Fragment fragment4 = this.f2922c;
        fragment4.mHost = fragment4.mFragmentManager.v0();
        Fragment fragment5 = this.f2922c;
        fragment5.mParentFragment = fragment5.mFragmentManager.y0();
        this.f2920a.g(this.f2922c, false);
        this.f2922c.performAttach();
        this.f2920a.b(this.f2922c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d() {
        Fragment fragment;
        ViewGroup viewGroup;
        Fragment fragment2 = this.f2922c;
        if (fragment2.mFragmentManager == null) {
            return fragment2.mState;
        }
        int i10 = this.f2924e;
        int i11 = b.f2927a[fragment2.mMaxState.ordinal()];
        if (i11 != 1) {
            if (i11 == 2) {
                i10 = Math.min(i10, 5);
            } else if (i11 == 3) {
                i10 = Math.min(i10, 1);
            } else if (i11 != 4) {
                i10 = Math.min(i10, -1);
            } else {
                i10 = Math.min(i10, 0);
            }
        }
        Fragment fragment3 = this.f2922c;
        if (fragment3.mFromLayout) {
            if (fragment3.mInLayout) {
                i10 = Math.max(this.f2924e, 2);
                View view = this.f2922c.mView;
                if (view != null && view.getParent() == null) {
                    i10 = Math.min(i10, 2);
                }
            } else {
                i10 = this.f2924e < 4 ? Math.min(i10, fragment3.mState) : Math.min(i10, 1);
            }
        }
        if (!this.f2922c.mAdded) {
            i10 = Math.min(i10, 1);
        }
        SpecialEffectsController.e.b bVar = null;
        if (FragmentManager.P && (viewGroup = (fragment = this.f2922c).mContainer) != null) {
            bVar = SpecialEffectsController.n(viewGroup, fragment.getParentFragmentManager()).l(this);
        }
        if (bVar == SpecialEffectsController.e.b.ADDING) {
            i10 = Math.min(i10, 6);
        } else if (bVar == SpecialEffectsController.e.b.REMOVING) {
            i10 = Math.max(i10, 3);
        } else {
            Fragment fragment4 = this.f2922c;
            if (fragment4.mRemoving) {
                if (fragment4.isInBackStack()) {
                    i10 = Math.min(i10, 1);
                } else {
                    i10 = Math.min(i10, -1);
                }
            }
        }
        Fragment fragment5 = this.f2922c;
        if (fragment5.mDeferStart && fragment5.mState < 5) {
            i10 = Math.min(i10, 4);
        }
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "computeExpectedState() of " + i10 + " for " + this.f2922c);
        }
        return i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "moveto CREATED: " + this.f2922c);
        }
        Fragment fragment = this.f2922c;
        if (!fragment.mIsCreated) {
            this.f2920a.h(fragment, fragment.mSavedFragmentState, false);
            Fragment fragment2 = this.f2922c;
            fragment2.performCreate(fragment2.mSavedFragmentState);
            FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.f2920a;
            Fragment fragment3 = this.f2922c;
            fragmentLifecycleCallbacksDispatcher.c(fragment3, fragment3.mSavedFragmentState, false);
            return;
        }
        fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
        this.f2922c.mState = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        String str;
        if (this.f2922c.mFromLayout) {
            return;
        }
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.f2922c);
        }
        Fragment fragment = this.f2922c;
        LayoutInflater performGetLayoutInflater = fragment.performGetLayoutInflater(fragment.mSavedFragmentState);
        ViewGroup viewGroup = null;
        Fragment fragment2 = this.f2922c;
        ViewGroup viewGroup2 = fragment2.mContainer;
        if (viewGroup2 != null) {
            viewGroup = viewGroup2;
        } else {
            int i10 = fragment2.mContainerId;
            if (i10 != 0) {
                if (i10 != -1) {
                    viewGroup = (ViewGroup) fragment2.mFragmentManager.p0().c(this.f2922c.mContainerId);
                    if (viewGroup == null) {
                        Fragment fragment3 = this.f2922c;
                        if (!fragment3.mRestored) {
                            try {
                                str = fragment3.getResources().getResourceName(this.f2922c.mContainerId);
                            } catch (Resources.NotFoundException unused) {
                                str = "unknown";
                            }
                            throw new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(this.f2922c.mContainerId) + " (" + str + ") for fragment " + this.f2922c);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Cannot create fragment " + this.f2922c + " for a container view with no id");
                }
            }
        }
        Fragment fragment4 = this.f2922c;
        fragment4.mContainer = viewGroup;
        fragment4.performCreateView(performGetLayoutInflater, viewGroup, fragment4.mSavedFragmentState);
        View view = this.f2922c.mView;
        if (view != null) {
            boolean z10 = false;
            view.setSaveFromParentEnabled(false);
            Fragment fragment5 = this.f2922c;
            fragment5.mView.setTag(R$id.fragment_container_view_tag, fragment5);
            if (viewGroup != null) {
                b();
            }
            Fragment fragment6 = this.f2922c;
            if (fragment6.mHidden) {
                fragment6.mView.setVisibility(8);
            }
            if (ViewCompat.P(this.f2922c.mView)) {
                ViewCompat.h0(this.f2922c.mView);
            } else {
                View view2 = this.f2922c.mView;
                view2.addOnAttachStateChangeListener(new a(view2));
            }
            this.f2922c.performViewCreated();
            FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.f2920a;
            Fragment fragment7 = this.f2922c;
            fragmentLifecycleCallbacksDispatcher.m(fragment7, fragment7.mView, fragment7.mSavedFragmentState, false);
            int visibility = this.f2922c.mView.getVisibility();
            float alpha = this.f2922c.mView.getAlpha();
            if (FragmentManager.P) {
                this.f2922c.setPostOnViewCreatedAlpha(alpha);
                Fragment fragment8 = this.f2922c;
                if (fragment8.mContainer != null && visibility == 0) {
                    View findFocus = fragment8.mView.findFocus();
                    if (findFocus != null) {
                        this.f2922c.setFocusedView(findFocus);
                        if (FragmentManager.H0(2)) {
                            Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + this.f2922c);
                        }
                    }
                    this.f2922c.mView.setAlpha(0.0f);
                }
            } else {
                Fragment fragment9 = this.f2922c;
                if (visibility == 0 && fragment9.mContainer != null) {
                    z10 = true;
                }
                fragment9.mIsNewlyAdded = z10;
            }
        }
        this.f2922c.mState = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        Fragment f10;
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "movefrom CREATED: " + this.f2922c);
        }
        Fragment fragment = this.f2922c;
        boolean z10 = true;
        boolean z11 = fragment.mRemoving && !fragment.isInBackStack();
        if (z11 || this.f2921b.o().p(this.f2922c)) {
            FragmentHostCallback<?> fragmentHostCallback = this.f2922c.mHost;
            if (fragmentHostCallback instanceof ViewModelStoreOwner) {
                z10 = this.f2921b.o().m();
            } else if (fragmentHostCallback.f() instanceof Activity) {
                z10 = true ^ ((Activity) fragmentHostCallback.f()).isChangingConfigurations();
            }
            if (z11 || z10) {
                this.f2921b.o().g(this.f2922c);
            }
            this.f2922c.performDestroy();
            this.f2920a.d(this.f2922c, false);
            for (FragmentStateManager fragmentStateManager : this.f2921b.k()) {
                if (fragmentStateManager != null) {
                    Fragment k10 = fragmentStateManager.k();
                    if (this.f2922c.mWho.equals(k10.mTargetWho)) {
                        k10.mTarget = this.f2922c;
                        k10.mTargetWho = null;
                    }
                }
            }
            Fragment fragment2 = this.f2922c;
            String str = fragment2.mTargetWho;
            if (str != null) {
                fragment2.mTarget = this.f2921b.f(str);
            }
            this.f2921b.q(this);
            return;
        }
        String str2 = this.f2922c.mTargetWho;
        if (str2 != null && (f10 = this.f2921b.f(str2)) != null && f10.mRetainInstance) {
            this.f2922c.mTarget = f10;
        }
        this.f2922c.mState = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        View view;
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "movefrom CREATE_VIEW: " + this.f2922c);
        }
        Fragment fragment = this.f2922c;
        ViewGroup viewGroup = fragment.mContainer;
        if (viewGroup != null && (view = fragment.mView) != null) {
            viewGroup.removeView(view);
        }
        this.f2922c.performDestroyView();
        this.f2920a.n(this.f2922c, false);
        Fragment fragment2 = this.f2922c;
        fragment2.mContainer = null;
        fragment2.mView = null;
        fragment2.mViewLifecycleOwner = null;
        fragment2.mViewLifecycleOwnerLiveData.m(null);
        this.f2922c.mInLayout = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "movefrom ATTACHED: " + this.f2922c);
        }
        this.f2922c.performDetach();
        boolean z10 = false;
        this.f2920a.e(this.f2922c, false);
        Fragment fragment = this.f2922c;
        fragment.mState = -1;
        fragment.mHost = null;
        fragment.mParentFragment = null;
        fragment.mFragmentManager = null;
        if (fragment.mRemoving && !fragment.isInBackStack()) {
            z10 = true;
        }
        if (z10 || this.f2921b.o().p(this.f2922c)) {
            if (FragmentManager.H0(3)) {
                Log.d("FragmentManager", "initState called for fragment: " + this.f2922c);
            }
            this.f2922c.initState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        Fragment fragment = this.f2922c;
        if (fragment.mFromLayout && fragment.mInLayout && !fragment.mPerformedCreateView) {
            if (FragmentManager.H0(3)) {
                Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.f2922c);
            }
            Fragment fragment2 = this.f2922c;
            fragment2.performCreateView(fragment2.performGetLayoutInflater(fragment2.mSavedFragmentState), null, this.f2922c.mSavedFragmentState);
            View view = this.f2922c.mView;
            if (view != null) {
                view.setSaveFromParentEnabled(false);
                Fragment fragment3 = this.f2922c;
                fragment3.mView.setTag(R$id.fragment_container_view_tag, fragment3);
                Fragment fragment4 = this.f2922c;
                if (fragment4.mHidden) {
                    fragment4.mView.setVisibility(8);
                }
                this.f2922c.performViewCreated();
                FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher = this.f2920a;
                Fragment fragment5 = this.f2922c;
                fragmentLifecycleCallbacksDispatcher.m(fragment5, fragment5.mView, fragment5.mSavedFragmentState, false);
                this.f2922c.mState = 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment k() {
        return this.f2922c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        ViewGroup viewGroup3;
        if (this.f2923d) {
            if (FragmentManager.H0(2)) {
                Log.v("FragmentManager", "Ignoring re-entrant call to moveToExpectedState() for " + k());
                return;
            }
            return;
        }
        try {
            this.f2923d = true;
            while (true) {
                int d10 = d();
                Fragment fragment = this.f2922c;
                int i10 = fragment.mState;
                if (d10 == i10) {
                    if (FragmentManager.P && fragment.mHiddenChanged) {
                        if (fragment.mView != null && (viewGroup = fragment.mContainer) != null) {
                            SpecialEffectsController n10 = SpecialEffectsController.n(viewGroup, fragment.getParentFragmentManager());
                            if (this.f2922c.mHidden) {
                                n10.c(this);
                            } else {
                                n10.e(this);
                            }
                        }
                        Fragment fragment2 = this.f2922c;
                        FragmentManager fragmentManager = fragment2.mFragmentManager;
                        if (fragmentManager != null) {
                            fragmentManager.F0(fragment2);
                        }
                        Fragment fragment3 = this.f2922c;
                        fragment3.mHiddenChanged = false;
                        fragment3.onHiddenChanged(fragment3.mHidden);
                    }
                    return;
                }
                if (d10 > i10) {
                    switch (i10 + 1) {
                        case 0:
                            c();
                            break;
                        case 1:
                            e();
                            break;
                        case 2:
                            j();
                            f();
                            break;
                        case 3:
                            a();
                            break;
                        case 4:
                            if (fragment.mView != null && (viewGroup2 = fragment.mContainer) != null) {
                                SpecialEffectsController.n(viewGroup2, fragment.getParentFragmentManager()).b(SpecialEffectsController.e.c.b(this.f2922c.mView.getVisibility()), this);
                            }
                            this.f2922c.mState = 4;
                            break;
                        case 5:
                            v();
                            break;
                        case 6:
                            fragment.mState = 6;
                            break;
                        case 7:
                            p();
                            break;
                    }
                } else {
                    switch (i10 - 1) {
                        case -1:
                            i();
                            break;
                        case 0:
                            g();
                            break;
                        case 1:
                            h();
                            this.f2922c.mState = 1;
                            break;
                        case 2:
                            fragment.mInLayout = false;
                            fragment.mState = 2;
                            break;
                        case 3:
                            if (FragmentManager.H0(3)) {
                                Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + this.f2922c);
                            }
                            Fragment fragment4 = this.f2922c;
                            if (fragment4.mView != null && fragment4.mSavedViewState == null) {
                                t();
                            }
                            Fragment fragment5 = this.f2922c;
                            if (fragment5.mView != null && (viewGroup3 = fragment5.mContainer) != null) {
                                SpecialEffectsController.n(viewGroup3, fragment5.getParentFragmentManager()).d(this);
                            }
                            this.f2922c.mState = 3;
                            break;
                        case 4:
                            w();
                            break;
                        case 5:
                            fragment.mState = 5;
                            break;
                        case 6:
                            n();
                            break;
                    }
                }
            }
        } finally {
            this.f2923d = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "movefrom RESUMED: " + this.f2922c);
        }
        this.f2922c.performPause();
        this.f2920a.f(this.f2922c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(ClassLoader classLoader) {
        Bundle bundle = this.f2922c.mSavedFragmentState;
        if (bundle == null) {
            return;
        }
        bundle.setClassLoader(classLoader);
        Fragment fragment = this.f2922c;
        fragment.mSavedViewState = fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
        Fragment fragment2 = this.f2922c;
        fragment2.mSavedViewRegistryState = fragment2.mSavedFragmentState.getBundle("android:view_registry_state");
        Fragment fragment3 = this.f2922c;
        fragment3.mTargetWho = fragment3.mSavedFragmentState.getString("android:target_state");
        Fragment fragment4 = this.f2922c;
        if (fragment4.mTargetWho != null) {
            fragment4.mTargetRequestCode = fragment4.mSavedFragmentState.getInt("android:target_req_state", 0);
        }
        Fragment fragment5 = this.f2922c;
        Boolean bool = fragment5.mSavedUserVisibleHint;
        if (bool != null) {
            fragment5.mUserVisibleHint = bool.booleanValue();
            this.f2922c.mSavedUserVisibleHint = null;
        } else {
            fragment5.mUserVisibleHint = fragment5.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
        }
        Fragment fragment6 = this.f2922c;
        if (fragment6.mUserVisibleHint) {
            return;
        }
        fragment6.mDeferStart = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "moveto RESUMED: " + this.f2922c);
        }
        View focusedView = this.f2922c.getFocusedView();
        if (focusedView != null && l(focusedView)) {
            boolean requestFocus = focusedView.requestFocus();
            if (FragmentManager.H0(2)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("requestFocus: Restoring focused view ");
                sb2.append(focusedView);
                sb2.append(" ");
                sb2.append(requestFocus ? "succeeded" : "failed");
                sb2.append(" on Fragment ");
                sb2.append(this.f2922c);
                sb2.append(" resulting in focused view ");
                sb2.append(this.f2922c.mView.findFocus());
                Log.v("FragmentManager", sb2.toString());
            }
        }
        this.f2922c.setFocusedView(null);
        this.f2922c.performResume();
        this.f2920a.i(this.f2922c, false);
        Fragment fragment = this.f2922c;
        fragment.mSavedFragmentState = null;
        fragment.mSavedViewState = null;
        fragment.mSavedViewRegistryState = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment.SavedState r() {
        Bundle q10;
        if (this.f2922c.mState <= -1 || (q10 = q()) == null) {
            return null;
        }
        return new Fragment.SavedState(q10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentState s() {
        FragmentState fragmentState = new FragmentState(this.f2922c);
        Fragment fragment = this.f2922c;
        if (fragment.mState > -1 && fragmentState.f2800q == null) {
            Bundle q10 = q();
            fragmentState.f2800q = q10;
            if (this.f2922c.mTargetWho != null) {
                if (q10 == null) {
                    fragmentState.f2800q = new Bundle();
                }
                fragmentState.f2800q.putString("android:target_state", this.f2922c.mTargetWho);
                int i10 = this.f2922c.mTargetRequestCode;
                if (i10 != 0) {
                    fragmentState.f2800q.putInt("android:target_req_state", i10);
                }
            }
        } else {
            fragmentState.f2800q = fragment.mSavedFragmentState;
        }
        return fragmentState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t() {
        if (this.f2922c.mView == null) {
            return;
        }
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        this.f2922c.mView.saveHierarchyState(sparseArray);
        if (sparseArray.size() > 0) {
            this.f2922c.mSavedViewState = sparseArray;
        }
        Bundle bundle = new Bundle();
        this.f2922c.mViewLifecycleOwner.e(bundle);
        if (bundle.isEmpty()) {
            return;
        }
        this.f2922c.mSavedViewRegistryState = bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(int i10) {
        this.f2924e = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "moveto STARTED: " + this.f2922c);
        }
        this.f2922c.performStart();
        this.f2920a.k(this.f2922c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w() {
        if (FragmentManager.H0(3)) {
            Log.d("FragmentManager", "movefrom STARTED: " + this.f2922c);
        }
        this.f2922c.performStop();
        this.f2920a.l(this.f2922c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, ClassLoader classLoader, FragmentFactory fragmentFactory, FragmentState fragmentState) {
        this.f2920a = fragmentLifecycleCallbacksDispatcher;
        this.f2921b = fragmentStore;
        Fragment a10 = fragmentFactory.a(classLoader, fragmentState.f2788e);
        this.f2922c = a10;
        Bundle bundle = fragmentState.f2797n;
        if (bundle != null) {
            bundle.setClassLoader(classLoader);
        }
        a10.setArguments(fragmentState.f2797n);
        a10.mWho = fragmentState.f2789f;
        a10.mFromLayout = fragmentState.f2790g;
        a10.mRestored = true;
        a10.mFragmentId = fragmentState.f2791h;
        a10.mContainerId = fragmentState.f2792i;
        a10.mTag = fragmentState.f2793j;
        a10.mRetainInstance = fragmentState.f2794k;
        a10.mRemoving = fragmentState.f2795l;
        a10.mDetached = fragmentState.f2796m;
        a10.mHidden = fragmentState.f2798o;
        a10.mMaxState = h.c.values()[fragmentState.f2799p];
        Bundle bundle2 = fragmentState.f2800q;
        if (bundle2 != null) {
            a10.mSavedFragmentState = bundle2;
        } else {
            a10.mSavedFragmentState = new Bundle();
        }
        if (FragmentManager.H0(2)) {
            Log.v("FragmentManager", "Instantiated fragment " + a10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentStateManager(FragmentLifecycleCallbacksDispatcher fragmentLifecycleCallbacksDispatcher, FragmentStore fragmentStore, Fragment fragment, FragmentState fragmentState) {
        this.f2920a = fragmentLifecycleCallbacksDispatcher;
        this.f2921b = fragmentStore;
        this.f2922c = fragment;
        fragment.mSavedViewState = null;
        fragment.mSavedViewRegistryState = null;
        fragment.mBackStackNesting = 0;
        fragment.mInLayout = false;
        fragment.mAdded = false;
        Fragment fragment2 = fragment.mTarget;
        fragment.mTargetWho = fragment2 != null ? fragment2.mWho : null;
        fragment.mTarget = null;
        Bundle bundle = fragmentState.f2800q;
        if (bundle != null) {
            fragment.mSavedFragmentState = bundle;
        } else {
            fragment.mSavedFragmentState = new Bundle();
        }
    }
}
