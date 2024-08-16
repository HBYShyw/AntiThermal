package androidx.preference;

import android.R;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.recyclerview.widget.RecyclerView;
import c.AppCompatResources;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: PreferenceGroupAdapter.java */
/* renamed from: androidx.preference.h, reason: use source file name */
/* loaded from: classes.dex */
public class PreferenceGroupAdapter extends RecyclerView.h<PreferenceViewHolder> implements Preference.b, PreferenceGroup.c {
    private PreferenceGroup mPreferenceGroup;
    private List<c> mPreferenceResourceDescriptors;
    private List<Preference> mPreferences;
    private List<Preference> mVisiblePreferences;
    private Runnable mSyncRunnable = new a();
    private Handler mHandler = new Handler();

    /* compiled from: PreferenceGroupAdapter.java */
    /* renamed from: androidx.preference.h$a */
    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PreferenceGroupAdapter.this.updatePreferences();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PreferenceGroupAdapter.java */
    /* renamed from: androidx.preference.h$b */
    /* loaded from: classes.dex */
    public class b implements Preference.d {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ PreferenceGroup f3332e;

        b(PreferenceGroup preferenceGroup) {
            this.f3332e = preferenceGroup;
        }

        @Override // androidx.preference.Preference.d
        public boolean M(Preference preference) {
            this.f3332e.p(Integer.MAX_VALUE);
            PreferenceGroupAdapter.this.onPreferenceHierarchyChange(preference);
            PreferenceGroup.b g6 = this.f3332e.g();
            if (g6 == null) {
                return true;
            }
            g6.a();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: PreferenceGroupAdapter.java */
    /* renamed from: androidx.preference.h$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        int f3334a;

        /* renamed from: b, reason: collision with root package name */
        int f3335b;

        /* renamed from: c, reason: collision with root package name */
        String f3336c;

        c(Preference preference) {
            this.f3336c = preference.getClass().getName();
            this.f3334a = preference.getLayoutResource();
            this.f3335b = preference.getWidgetLayoutResource();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof c)) {
                return false;
            }
            c cVar = (c) obj;
            return this.f3334a == cVar.f3334a && this.f3335b == cVar.f3335b && TextUtils.equals(this.f3336c, cVar.f3336c);
        }

        public int hashCode() {
            return ((((527 + this.f3334a) * 31) + this.f3335b) * 31) + this.f3336c.hashCode();
        }
    }

    public PreferenceGroupAdapter(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
        this.mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        this.mPreferences = new ArrayList();
        this.mVisiblePreferences = new ArrayList();
        this.mPreferenceResourceDescriptors = new ArrayList();
        PreferenceGroup preferenceGroup2 = this.mPreferenceGroup;
        if (preferenceGroup2 instanceof PreferenceScreen) {
            setHasStableIds(((PreferenceScreen) preferenceGroup2).s());
        } else {
            setHasStableIds(true);
        }
        updatePreferences();
    }

    private ExpandButton createExpandButton(PreferenceGroup preferenceGroup, List<Preference> list) {
        ExpandButton expandButton = new ExpandButton(preferenceGroup.getContext(), list, preferenceGroup.getId());
        expandButton.setOnPreferenceClickListener(new b(preferenceGroup));
        return expandButton;
    }

    private List<Preference> createVisiblePreferencesList(PreferenceGroup preferenceGroup) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i10 = preferenceGroup.i();
        int i11 = 0;
        for (int i12 = 0; i12 < i10; i12++) {
            Preference h10 = preferenceGroup.h(i12);
            if (h10.isVisible()) {
                if (isGroupExpandable(preferenceGroup) && i11 >= preferenceGroup.f()) {
                    arrayList2.add(h10);
                } else {
                    arrayList.add(h10);
                }
                if (h10 instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup2 = (PreferenceGroup) h10;
                    if (!preferenceGroup2.j()) {
                        continue;
                    } else {
                        if (isGroupExpandable(preferenceGroup) && isGroupExpandable(preferenceGroup2)) {
                            throw new IllegalStateException("Nesting an expandable group inside of another expandable group is not supported!");
                        }
                        for (Preference preference : createVisiblePreferencesList(preferenceGroup2)) {
                            if (isGroupExpandable(preferenceGroup) && i11 >= preferenceGroup.f()) {
                                arrayList2.add(preference);
                            } else {
                                arrayList.add(preference);
                            }
                            i11++;
                        }
                    }
                } else {
                    i11++;
                }
            }
        }
        if (isGroupExpandable(preferenceGroup) && i11 > preferenceGroup.f()) {
            arrayList.add(createExpandButton(preferenceGroup, arrayList2));
        }
        return arrayList;
    }

    private void flattenPreferenceGroup(List<Preference> list, PreferenceGroup preferenceGroup) {
        preferenceGroup.r();
        int i10 = preferenceGroup.i();
        for (int i11 = 0; i11 < i10; i11++) {
            Preference h10 = preferenceGroup.h(i11);
            list.add(h10);
            c cVar = new c(h10);
            if (!this.mPreferenceResourceDescriptors.contains(cVar)) {
                this.mPreferenceResourceDescriptors.add(cVar);
            }
            if (h10 instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup2 = (PreferenceGroup) h10;
                if (preferenceGroup2.j()) {
                    flattenPreferenceGroup(list, preferenceGroup2);
                }
            }
            h10.setOnPreferenceChangeInternalListener(this);
        }
    }

    private boolean isGroupExpandable(PreferenceGroup preferenceGroup) {
        return preferenceGroup.f() != Integer.MAX_VALUE;
    }

    public Preference getItem(int i10) {
        if (i10 < 0 || i10 >= getItemCount()) {
            return null;
        }
        return this.mVisiblePreferences.get(i10);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemCount() {
        return this.mVisiblePreferences.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public long getItemId(int i10) {
        if (hasStableIds()) {
            return getItem(i10).getId();
        }
        return -1L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemViewType(int i10) {
        c cVar = new c(getItem(i10));
        int indexOf = this.mPreferenceResourceDescriptors.indexOf(cVar);
        if (indexOf != -1) {
            return indexOf;
        }
        int size = this.mPreferenceResourceDescriptors.size();
        this.mPreferenceResourceDescriptors.add(cVar);
        return size;
    }

    @Override // androidx.preference.PreferenceGroup.c
    public int getPreferenceAdapterPosition(String str) {
        int size = this.mVisiblePreferences.size();
        for (int i10 = 0; i10 < size; i10++) {
            if (TextUtils.equals(str, this.mVisiblePreferences.get(i10).getKey())) {
                return i10;
            }
        }
        return -1;
    }

    @Override // androidx.preference.Preference.b
    public void onPreferenceChange(Preference preference) {
        int indexOf = this.mVisiblePreferences.indexOf(preference);
        if (indexOf != -1) {
            notifyItemChanged(indexOf, preference);
        }
    }

    @Override // androidx.preference.Preference.b
    public void onPreferenceHierarchyChange(Preference preference) {
        this.mHandler.removeCallbacks(this.mSyncRunnable);
        this.mHandler.post(this.mSyncRunnable);
    }

    @Override // androidx.preference.Preference.b
    public void onPreferenceVisibilityChange(Preference preference) {
        onPreferenceHierarchyChange(preference);
    }

    void updatePreferences() {
        Iterator<Preference> it = this.mPreferences.iterator();
        while (it.hasNext()) {
            it.next().setOnPreferenceChangeInternalListener(null);
        }
        ArrayList arrayList = new ArrayList(this.mPreferences.size());
        this.mPreferences = arrayList;
        flattenPreferenceGroup(arrayList, this.mPreferenceGroup);
        this.mVisiblePreferences = createVisiblePreferencesList(this.mPreferenceGroup);
        PreferenceManager preferenceManager = this.mPreferenceGroup.getPreferenceManager();
        if (preferenceManager != null) {
            preferenceManager.i();
        }
        notifyDataSetChanged();
        Iterator<Preference> it2 = this.mPreferences.iterator();
        while (it2.hasNext()) {
            it2.next().clearWasDetached();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, int i10) {
        getItem(i10).onBindViewHolder(preferenceViewHolder);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.recyclerview.widget.RecyclerView.h
    public PreferenceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i10) {
        c cVar = this.mPreferenceResourceDescriptors.get(i10);
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        TypedArray obtainStyledAttributes = viewGroup.getContext().obtainStyledAttributes((AttributeSet) null, R$styleable.BackgroundStyle);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.BackgroundStyle_android_selectableItemBackground);
        if (drawable == null) {
            drawable = AppCompatResources.b(viewGroup.getContext(), R.drawable.list_selector_background);
        }
        obtainStyledAttributes.recycle();
        View inflate = from.inflate(cVar.f3334a, viewGroup, false);
        if (inflate.getBackground() == null) {
            ViewCompat.p0(inflate, drawable);
        }
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R.id.widget_frame);
        if (viewGroup2 != null) {
            int i11 = cVar.f3335b;
            if (i11 != 0) {
                from.inflate(i11, viewGroup2);
            } else {
                viewGroup2.setVisibility(8);
            }
        }
        return new PreferenceViewHolder(inflate);
    }

    @Override // androidx.preference.PreferenceGroup.c
    public int getPreferenceAdapterPosition(Preference preference) {
        int size = this.mVisiblePreferences.size();
        for (int i10 = 0; i10 < size; i10++) {
            Preference preference2 = this.mVisiblePreferences.get(i10);
            if (preference2 != null && preference2.equals(preference)) {
                return i10;
            }
        }
        return -1;
    }
}
