package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import j.SimpleArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public abstract class PreferenceGroup extends Preference {

    /* renamed from: e, reason: collision with root package name */
    final SimpleArrayMap<String, Long> f3271e;

    /* renamed from: f, reason: collision with root package name */
    private final Handler f3272f;

    /* renamed from: g, reason: collision with root package name */
    private List<Preference> f3273g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f3274h;

    /* renamed from: i, reason: collision with root package name */
    private int f3275i;

    /* renamed from: j, reason: collision with root package name */
    private boolean f3276j;

    /* renamed from: k, reason: collision with root package name */
    private int f3277k;

    /* renamed from: l, reason: collision with root package name */
    private b f3278l;

    /* renamed from: m, reason: collision with root package name */
    private final Runnable f3279m;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this) {
                PreferenceGroup.this.f3271e.clear();
            }
        }
    }

    /* loaded from: classes.dex */
    public interface b {
        void a();
    }

    /* loaded from: classes.dex */
    public interface c {
        int getPreferenceAdapterPosition(Preference preference);

        int getPreferenceAdapterPosition(String str);
    }

    public PreferenceGroup(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f3271e = new SimpleArrayMap<>();
        this.f3272f = new Handler();
        this.f3274h = true;
        this.f3275i = 0;
        this.f3276j = false;
        this.f3277k = Integer.MAX_VALUE;
        this.f3278l = null;
        this.f3279m = new a();
        this.f3273g = new ArrayList();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PreferenceGroup, i10, i11);
        int i12 = R$styleable.PreferenceGroup_orderingFromXml;
        this.f3274h = TypedArrayUtils.b(obtainStyledAttributes, i12, i12, true);
        int i13 = R$styleable.PreferenceGroup_initialExpandedChildrenCount;
        if (obtainStyledAttributes.hasValue(i13)) {
            p(TypedArrayUtils.d(obtainStyledAttributes, i13, i13, Integer.MAX_VALUE));
        }
        obtainStyledAttributes.recycle();
    }

    private boolean o(Preference preference) {
        boolean remove;
        synchronized (this) {
            preference.onPrepareForRemoval();
            if (preference.getParent() == this) {
                preference.assignParent(null);
            }
            remove = this.f3273g.remove(preference);
            if (remove) {
                String key = preference.getKey();
                if (key != null) {
                    this.f3271e.put(key, Long.valueOf(preference.getId()));
                    this.f3272f.removeCallbacks(this.f3279m);
                    this.f3272f.post(this.f3279m);
                }
                if (this.f3276j) {
                    preference.onDetached();
                }
            }
        }
        return remove;
    }

    public void c(Preference preference) {
        d(preference);
    }

    public boolean d(Preference preference) {
        long f10;
        if (this.f3273g.contains(preference)) {
            return true;
        }
        if (preference.getKey() != null) {
            PreferenceGroup preferenceGroup = this;
            while (preferenceGroup.getParent() != null) {
                preferenceGroup = preferenceGroup.getParent();
            }
            String key = preference.getKey();
            if (preferenceGroup.e(key) != null) {
                Log.e("PreferenceGroup", "Found duplicated key: \"" + key + "\". This can cause unintended behaviour, please use unique keys for every preference.");
            }
        }
        if (preference.getOrder() == Integer.MAX_VALUE) {
            if (this.f3274h) {
                int i10 = this.f3275i;
                this.f3275i = i10 + 1;
                preference.setOrder(i10);
            }
            if (preference instanceof PreferenceGroup) {
                ((PreferenceGroup) preference).q(this.f3274h);
            }
        }
        int binarySearch = Collections.binarySearch(this.f3273g, preference);
        if (binarySearch < 0) {
            binarySearch = (binarySearch * (-1)) - 1;
        }
        if (!l(preference)) {
            return false;
        }
        synchronized (this) {
            this.f3273g.add(binarySearch, preference);
        }
        PreferenceManager preferenceManager = getPreferenceManager();
        String key2 = preference.getKey();
        if (key2 != null && this.f3271e.containsKey(key2)) {
            f10 = this.f3271e.get(key2).longValue();
            this.f3271e.remove(key2);
        } else {
            f10 = preferenceManager.f();
        }
        preference.onAttachedToHierarchy(preferenceManager, f10);
        preference.assignParent(this);
        if (this.f3276j) {
            preference.onAttached();
        }
        notifyHierarchyChanged();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void dispatchRestoreInstanceState(Bundle bundle) {
        super.dispatchRestoreInstanceState(bundle);
        int i10 = i();
        for (int i11 = 0; i11 < i10; i11++) {
            h(i11).dispatchRestoreInstanceState(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void dispatchSaveInstanceState(Bundle bundle) {
        super.dispatchSaveInstanceState(bundle);
        int i10 = i();
        for (int i11 = 0; i11 < i10; i11++) {
            h(i11).dispatchSaveInstanceState(bundle);
        }
    }

    public <T extends Preference> T e(CharSequence charSequence) {
        T t7;
        if (charSequence != null) {
            if (TextUtils.equals(getKey(), charSequence)) {
                return this;
            }
            int i10 = i();
            for (int i11 = 0; i11 < i10; i11++) {
                PreferenceGroup preferenceGroup = (T) h(i11);
                if (TextUtils.equals(preferenceGroup.getKey(), charSequence)) {
                    return preferenceGroup;
                }
                if ((preferenceGroup instanceof PreferenceGroup) && (t7 = (T) preferenceGroup.e(charSequence)) != null) {
                    return t7;
                }
            }
            return null;
        }
        throw new IllegalArgumentException("Key cannot be null");
    }

    public int f() {
        return this.f3277k;
    }

    public b g() {
        return this.f3278l;
    }

    public Preference h(int i10) {
        return this.f3273g.get(i10);
    }

    public int i() {
        return this.f3273g.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean j() {
        return true;
    }

    protected boolean l(Preference preference) {
        preference.onParentChanged(this, shouldDisableDependents());
        return true;
    }

    public void m() {
        synchronized (this) {
            List<Preference> list = this.f3273g;
            for (int size = list.size() - 1; size >= 0; size--) {
                o(list.get(0));
            }
        }
        notifyHierarchyChanged();
    }

    public boolean n(Preference preference) {
        boolean o10 = o(preference);
        notifyHierarchyChanged();
        return o10;
    }

    @Override // androidx.preference.Preference
    public void notifyDependencyChange(boolean z10) {
        super.notifyDependencyChange(z10);
        int i10 = i();
        for (int i11 = 0; i11 < i10; i11++) {
            h(i11).onParentChanged(this, z10);
        }
    }

    @Override // androidx.preference.Preference
    public void onAttached() {
        super.onAttached();
        this.f3276j = true;
        int i10 = i();
        for (int i11 = 0; i11 < i10; i11++) {
            h(i11).onAttached();
        }
    }

    @Override // androidx.preference.Preference
    public void onDetached() {
        super.onDetached();
        this.f3276j = false;
        int i10 = i();
        for (int i11 = 0; i11 < i10; i11++) {
            h(i11).onDetached();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable != null && parcelable.getClass().equals(SavedState.class)) {
            SavedState savedState = (SavedState) parcelable;
            this.f3277k = savedState.f3280e;
            super.onRestoreInstanceState(savedState.getSuperState());
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.f3277k);
    }

    public void p(int i10) {
        if (i10 != Integer.MAX_VALUE && !hasKey()) {
            Log.e("PreferenceGroup", getClass().getSimpleName() + " should have a key defined if it contains an expandable preference");
        }
        this.f3277k = i10;
    }

    public void q(boolean z10) {
        this.f3274h = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        synchronized (this) {
            Collections.sort(this.f3273g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f3280e;

        /* loaded from: classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.f3280e = parcel.readInt();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f3280e);
        }

        SavedState(Parcelable parcelable, int i10) {
            super(parcelable);
            this.f3280e = i10;
        }
    }

    public PreferenceGroup(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public PreferenceGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
}
