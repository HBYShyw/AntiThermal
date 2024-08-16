package androidx.preference;

import android.R;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* compiled from: PreferenceFragmentCompat.java */
/* renamed from: androidx.preference.g, reason: use source file name */
/* loaded from: classes.dex */
public abstract class PreferenceFragmentCompat extends Fragment implements PreferenceManager.c, PreferenceManager.a, PreferenceManager.b, DialogPreference.a {
    public static final String ARG_PREFERENCE_ROOT = "androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT";
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private static final String TAG = "PreferenceFragment";
    private boolean mHavePrefs;
    private boolean mInitDone;
    RecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private Runnable mSelectPreferenceRunnable;
    private final d mDividerDecoration = new d();
    private int mLayoutResId = R$layout.preference_list_fragment;
    private Handler mHandler = new a();
    private final Runnable mRequestFocus = new b();

    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$a */
    /* loaded from: classes.dex */
    class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            PreferenceFragmentCompat.this.bindPreferences();
        }
    }

    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$b */
    /* loaded from: classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            RecyclerView recyclerView = PreferenceFragmentCompat.this.mList;
            recyclerView.focusableViewAvailable(recyclerView);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$c */
    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Preference f3320e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f3321f;

        c(Preference preference, String str) {
            this.f3320e = preference;
            this.f3321f = str;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            int preferenceAdapterPosition;
            RecyclerView.h adapter = PreferenceFragmentCompat.this.mList.getAdapter();
            if (!(adapter instanceof PreferenceGroup.c)) {
                if (adapter != 0) {
                    throw new IllegalStateException("Adapter must implement PreferencePositionCallback");
                }
                return;
            }
            Preference preference = this.f3320e;
            if (preference != null) {
                preferenceAdapterPosition = ((PreferenceGroup.c) adapter).getPreferenceAdapterPosition(preference);
            } else {
                preferenceAdapterPosition = ((PreferenceGroup.c) adapter).getPreferenceAdapterPosition(this.f3321f);
            }
            if (preferenceAdapterPosition != -1) {
                PreferenceFragmentCompat.this.mList.scrollToPosition(preferenceAdapterPosition);
            } else {
                adapter.registerAdapterDataObserver(new h(adapter, PreferenceFragmentCompat.this.mList, this.f3320e, this.f3321f));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$d */
    /* loaded from: classes.dex */
    public class d extends RecyclerView.o {

        /* renamed from: a, reason: collision with root package name */
        private Drawable f3323a;

        /* renamed from: b, reason: collision with root package name */
        private int f3324b;

        /* renamed from: c, reason: collision with root package name */
        private boolean f3325c = true;

        d() {
        }

        private boolean m(View view, RecyclerView recyclerView) {
            RecyclerView.c0 childViewHolder = recyclerView.getChildViewHolder(view);
            boolean z10 = false;
            if (!((childViewHolder instanceof PreferenceViewHolder) && ((PreferenceViewHolder) childViewHolder).c())) {
                return false;
            }
            boolean z11 = this.f3325c;
            int indexOfChild = recyclerView.indexOfChild(view);
            if (indexOfChild >= recyclerView.getChildCount() - 1) {
                return z11;
            }
            RecyclerView.c0 childViewHolder2 = recyclerView.getChildViewHolder(recyclerView.getChildAt(indexOfChild + 1));
            if ((childViewHolder2 instanceof PreferenceViewHolder) && ((PreferenceViewHolder) childViewHolder2).b()) {
                z10 = true;
            }
            return z10;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void e(Rect rect, View view, RecyclerView recyclerView, RecyclerView.z zVar) {
            if (m(view, recyclerView)) {
                rect.bottom = this.f3324b;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.o
        public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
            if (this.f3323a == null) {
                return;
            }
            int childCount = recyclerView.getChildCount();
            int width = recyclerView.getWidth();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = recyclerView.getChildAt(i10);
                if (m(childAt, recyclerView)) {
                    int y4 = ((int) childAt.getY()) + childAt.getHeight();
                    this.f3323a.setBounds(0, y4, width, this.f3324b + y4);
                    this.f3323a.draw(canvas);
                }
            }
        }

        public void j(boolean z10) {
            this.f3325c = z10;
        }

        public void k(Drawable drawable) {
            if (drawable != null) {
                this.f3324b = drawable.getIntrinsicHeight();
            } else {
                this.f3324b = 0;
            }
            this.f3323a = drawable;
            PreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }

        public void l(int i10) {
            this.f3324b = i10;
            PreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }
    }

    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$e */
    /* loaded from: classes.dex */
    public interface e {
        boolean a(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference);
    }

    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$f */
    /* loaded from: classes.dex */
    public interface f {
        boolean a(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference);
    }

    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$g */
    /* loaded from: classes.dex */
    public interface g {
        boolean a(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen);
    }

    /* compiled from: PreferenceFragmentCompat.java */
    /* renamed from: androidx.preference.g$h */
    /* loaded from: classes.dex */
    private static class h extends RecyclerView.j {

        /* renamed from: a, reason: collision with root package name */
        private final RecyclerView.h f3327a;

        /* renamed from: b, reason: collision with root package name */
        private final RecyclerView f3328b;

        /* renamed from: c, reason: collision with root package name */
        private final Preference f3329c;

        /* renamed from: d, reason: collision with root package name */
        private final String f3330d;

        public h(RecyclerView.h hVar, RecyclerView recyclerView, Preference preference, String str) {
            this.f3327a = hVar;
            this.f3328b = recyclerView;
            this.f3329c = preference;
            this.f3330d = str;
        }

        private void a() {
            int preferenceAdapterPosition;
            this.f3327a.unregisterAdapterDataObserver(this);
            Preference preference = this.f3329c;
            if (preference != null) {
                preferenceAdapterPosition = ((PreferenceGroup.c) this.f3327a).getPreferenceAdapterPosition(preference);
            } else {
                preferenceAdapterPosition = ((PreferenceGroup.c) this.f3327a).getPreferenceAdapterPosition(this.f3330d);
            }
            if (preferenceAdapterPosition != -1) {
                this.f3328b.scrollToPosition(preferenceAdapterPosition);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onChanged() {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeInserted(int i10, int i11) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeMoved(int i10, int i11, int i12) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeRemoved(int i10, int i11) {
            a();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.j
        public void onItemRangeChanged(int i10, int i11, Object obj) {
            a();
        }
    }

    private void postBindPreferences() {
        if (this.mHandler.hasMessages(1)) {
            return;
        }
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void scrollToPreferenceInternal(Preference preference, String str) {
        c cVar = new c(preference, str);
        if (this.mList == null) {
            this.mSelectPreferenceRunnable = cVar;
        } else {
            cVar.run();
        }
    }

    private void unbindPreferences() {
        getListView().setAdapter(null);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        onUnbindPreferences();
    }

    public void addPreferencesFromResource(int i10) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.m(getContext(), i10, getPreferenceScreen()));
    }

    void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            getListView().setAdapter(onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        onBindPreferences();
    }

    @Override // androidx.preference.DialogPreference.a
    public <T extends Preference> T findPreference(CharSequence charSequence) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null) {
            return null;
        }
        return (T) preferenceManager.a(charSequence);
    }

    public Fragment getCallbackFragment() {
        return null;
    }

    public final RecyclerView getListView() {
        return this.mList;
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.k();
    }

    protected void onBindPreferences() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R$attr.preferenceTheme, typedValue, true);
        int i10 = typedValue.resourceId;
        if (i10 == 0) {
            i10 = R$style.PreferenceThemeOverlay;
        }
        getActivity().getTheme().applyStyle(i10, false);
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        this.mPreferenceManager = preferenceManager;
        preferenceManager.p(this);
        onCreatePreferences(bundle, getArguments() != null ? getArguments().getString(ARG_PREFERENCE_ROOT) : null);
    }

    protected RecyclerView.h onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen);
    }

    public RecyclerView.p onCreateLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public abstract void onCreatePreferences(Bundle bundle, String str);

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView recyclerView;
        if (getContext().getPackageManager().hasSystemFeature("android.hardware.type.automotive") && (recyclerView = (RecyclerView) viewGroup.findViewById(R$id.recycler_view)) != null) {
            return recyclerView;
        }
        RecyclerView recyclerView2 = (RecyclerView) layoutInflater.inflate(R$layout.preference_recyclerview, viewGroup, false);
        recyclerView2.setLayoutManager(onCreateLayoutManager());
        recyclerView2.setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate(recyclerView2));
        return recyclerView2;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, R$styleable.PreferenceFragmentCompat, R$attr.preferenceFragmentCompatStyle, 0);
        this.mLayoutResId = obtainStyledAttributes.getResourceId(R$styleable.PreferenceFragmentCompat_android_layout, this.mLayoutResId);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.PreferenceFragmentCompat_android_divider);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.PreferenceFragmentCompat_android_dividerHeight, -1);
        boolean z10 = obtainStyledAttributes.getBoolean(R$styleable.PreferenceFragmentCompat_allowDividerAfterLastItem, true);
        obtainStyledAttributes.recycle();
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(getContext());
        View inflate = cloneInContext.inflate(this.mLayoutResId, viewGroup, false);
        View findViewById = inflate.findViewById(R.id.list_container);
        if (findViewById instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) findViewById;
            RecyclerView onCreateRecyclerView = onCreateRecyclerView(cloneInContext, viewGroup2, bundle);
            if (onCreateRecyclerView != null) {
                this.mList = onCreateRecyclerView;
                onCreateRecyclerView.addItemDecoration(this.mDividerDecoration);
                setDivider(drawable);
                if (dimensionPixelSize != -1) {
                    setDividerHeight(dimensionPixelSize);
                }
                this.mDividerDecoration.j(z10);
                if (this.mList.getParent() == null) {
                    viewGroup2.addView(this.mList);
                }
                this.mHandler.post(this.mRequestFocus);
                return inflate;
            }
            throw new RuntimeException("Could not create RecyclerView");
        }
        throw new IllegalStateException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            unbindPreferences();
        }
        this.mList = null;
        super.onDestroyView();
    }

    @Override // androidx.preference.PreferenceManager.a
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment z02;
        boolean a10 = getCallbackFragment() instanceof e ? ((e) getCallbackFragment()).a(this, preference) : false;
        if (!a10 && (getActivity() instanceof e)) {
            a10 = ((e) getActivity()).a(this, preference);
        }
        if (!a10 && getParentFragmentManager().j0(DIALOG_FRAGMENT_TAG) == null) {
            if (preference instanceof EditTextPreference) {
                z02 = EditTextPreferenceDialogFragmentCompat.z0(preference.getKey());
            } else if (preference instanceof ListPreference) {
                z02 = ListPreferenceDialogFragmentCompat.z0(preference.getKey());
            } else if (preference instanceof MultiSelectListPreference) {
                z02 = MultiSelectListPreferenceDialogFragmentCompat.z0(preference.getKey());
            } else {
                throw new IllegalArgumentException("Cannot display dialog for an unknown Preference type: " + preference.getClass().getSimpleName() + ". Make sure to implement onPreferenceDisplayDialog() to handle displaying a custom dialog for this Preference.");
            }
            z02.setTargetFragment(this, 0);
            z02.q0(getParentFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    @Override // androidx.preference.PreferenceManager.b
    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        if ((getCallbackFragment() instanceof g ? ((g) getCallbackFragment()).a(this, preferenceScreen) : false) || !(getActivity() instanceof g)) {
            return;
        }
        ((g) getActivity()).a(this, preferenceScreen);
    }

    @Override // androidx.preference.PreferenceManager.c
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getFragment() == null) {
            return false;
        }
        boolean a10 = getCallbackFragment() instanceof f ? ((f) getCallbackFragment()).a(this, preference) : false;
        if (!a10 && (getActivity() instanceof f)) {
            a10 = ((f) getActivity()).a(this, preference);
        }
        if (a10) {
            return true;
        }
        Log.w(TAG, "onPreferenceStartFragment is not implemented in the parent activity - attempting to use a fallback implementation. You should implement this method so that you can configure the new fragment that will be displayed, and set a transition between the fragments.");
        FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
        Bundle extras = preference.getExtras();
        Fragment a11 = supportFragmentManager.s0().a(requireActivity().getClassLoader(), preference.getFragment());
        a11.setArguments(extras);
        a11.setTargetFragment(this, 0);
        supportFragmentManager.m().r(((View) getView().getParent()).getId(), a11).g(null).i();
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle bundle2 = new Bundle();
            preferenceScreen.saveHierarchyState(bundle2);
            bundle.putBundle(PREFERENCES_TAG, bundle2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        this.mPreferenceManager.q(this);
        this.mPreferenceManager.o(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mPreferenceManager.q(null);
        this.mPreferenceManager.o(null);
    }

    protected void onUnbindPreferences() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        Bundle bundle2;
        PreferenceScreen preferenceScreen;
        super.onViewCreated(view, bundle);
        if (bundle != null && (bundle2 = bundle.getBundle(PREFERENCES_TAG)) != null && (preferenceScreen = getPreferenceScreen()) != null) {
            preferenceScreen.restoreHierarchyState(bundle2);
        }
        if (this.mHavePrefs) {
            bindPreferences();
            Runnable runnable = this.mSelectPreferenceRunnable;
            if (runnable != null) {
                runnable.run();
                this.mSelectPreferenceRunnable = null;
            }
        }
        this.mInitDone = true;
    }

    public void scrollToPreference(String str) {
        scrollToPreferenceInternal(null, str);
    }

    public void setDivider(Drawable drawable) {
        this.mDividerDecoration.k(drawable);
    }

    public void setDividerHeight(int i10) {
        this.mDividerDecoration.l(i10);
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (!this.mPreferenceManager.r(preferenceScreen) || preferenceScreen == null) {
            return;
        }
        onUnbindPreferences();
        this.mHavePrefs = true;
        if (this.mInitDone) {
            postBindPreferences();
        }
    }

    public void setPreferencesFromResource(int i10, String str) {
        requirePreferenceManager();
        PreferenceScreen m10 = this.mPreferenceManager.m(getContext(), i10, null);
        Object obj = m10;
        if (str != null) {
            Object e10 = m10.e(str);
            boolean z10 = e10 instanceof PreferenceScreen;
            obj = e10;
            if (!z10) {
                throw new IllegalArgumentException("Preference object with key " + str + " is not a PreferenceScreen");
            }
        }
        setPreferenceScreen((PreferenceScreen) obj);
    }

    public void scrollToPreference(Preference preference) {
        scrollToPreferenceInternal(preference, null);
    }
}
