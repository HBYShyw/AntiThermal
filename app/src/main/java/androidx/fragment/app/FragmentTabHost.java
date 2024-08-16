package androidx.fragment.app;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabHost;
import java.util.ArrayList;

@Deprecated
/* loaded from: classes.dex */
public class FragmentTabHost extends TabHost implements TabHost.OnTabChangeListener {

    /* renamed from: e, reason: collision with root package name */
    private final ArrayList<a> f2801e;

    /* renamed from: f, reason: collision with root package name */
    private Context f2802f;

    /* renamed from: g, reason: collision with root package name */
    private FragmentManager f2803g;

    /* renamed from: h, reason: collision with root package name */
    private int f2804h;

    /* renamed from: i, reason: collision with root package name */
    private TabHost.OnTabChangeListener f2805i;

    /* renamed from: j, reason: collision with root package name */
    private a f2806j;

    /* renamed from: k, reason: collision with root package name */
    private boolean f2807k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        String f2808e;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<SavedState> {
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

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "FragmentTabHost.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " curTab=" + this.f2808e + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeString(this.f2808e);
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.f2808e = parcel.readString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        final String f2809a;

        /* renamed from: b, reason: collision with root package name */
        final Class<?> f2810b;

        /* renamed from: c, reason: collision with root package name */
        final Bundle f2811c;

        /* renamed from: d, reason: collision with root package name */
        Fragment f2812d;
    }

    @Deprecated
    public FragmentTabHost(Context context) {
        super(context, null);
        this.f2801e = new ArrayList<>();
        c(context, null);
    }

    private FragmentTransaction a(String str, FragmentTransaction fragmentTransaction) {
        Fragment fragment;
        a b10 = b(str);
        if (this.f2806j != b10) {
            if (fragmentTransaction == null) {
                fragmentTransaction = this.f2803g.m();
            }
            a aVar = this.f2806j;
            if (aVar != null && (fragment = aVar.f2812d) != null) {
                fragmentTransaction.m(fragment);
            }
            if (b10 != null) {
                Fragment fragment2 = b10.f2812d;
                if (fragment2 == null) {
                    Fragment a10 = this.f2803g.s0().a(this.f2802f.getClassLoader(), b10.f2810b.getName());
                    b10.f2812d = a10;
                    a10.setArguments(b10.f2811c);
                    fragmentTransaction.c(this.f2804h, b10.f2812d, b10.f2809a);
                } else {
                    fragmentTransaction.h(fragment2);
                }
            }
            this.f2806j = b10;
        }
        return fragmentTransaction;
    }

    private a b(String str) {
        int size = this.f2801e.size();
        for (int i10 = 0; i10 < size; i10++) {
            a aVar = this.f2801e.get(i10);
            if (aVar.f2809a.equals(str)) {
                return aVar;
            }
        }
        return null;
    }

    private void c(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{R.attr.inflatedId}, 0, 0);
        this.f2804h = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        super.setOnTabChangedListener(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    @Deprecated
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        String currentTabTag = getCurrentTabTag();
        int size = this.f2801e.size();
        FragmentTransaction fragmentTransaction = null;
        for (int i10 = 0; i10 < size; i10++) {
            a aVar = this.f2801e.get(i10);
            Fragment j02 = this.f2803g.j0(aVar.f2809a);
            aVar.f2812d = j02;
            if (j02 != null && !j02.isDetached()) {
                if (aVar.f2809a.equals(currentTabTag)) {
                    this.f2806j = aVar;
                } else {
                    if (fragmentTransaction == null) {
                        fragmentTransaction = this.f2803g.m();
                    }
                    fragmentTransaction.m(aVar.f2812d);
                }
            }
        }
        this.f2807k = true;
        FragmentTransaction a10 = a(currentTabTag, fragmentTransaction);
        if (a10 != null) {
            a10.i();
            this.f2803g.f0();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    @Deprecated
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f2807k = false;
    }

    @Override // android.view.View
    @Deprecated
    protected void onRestoreInstanceState(@SuppressLint({"UnknownNullness"}) Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentTabByTag(savedState.f2808e);
    }

    @Override // android.view.View
    @Deprecated
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f2808e = getCurrentTabTag();
        return savedState;
    }

    @Override // android.widget.TabHost.OnTabChangeListener
    @Deprecated
    public void onTabChanged(String str) {
        FragmentTransaction a10;
        if (this.f2807k && (a10 = a(str, null)) != null) {
            a10.i();
        }
        TabHost.OnTabChangeListener onTabChangeListener = this.f2805i;
        if (onTabChangeListener != null) {
            onTabChangeListener.onTabChanged(str);
        }
    }

    @Override // android.widget.TabHost
    @Deprecated
    public void setOnTabChangedListener(TabHost.OnTabChangeListener onTabChangeListener) {
        this.f2805i = onTabChangeListener;
    }

    @Override // android.widget.TabHost
    @Deprecated
    public void setup() {
        throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
    }

    @Deprecated
    public FragmentTabHost(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f2801e = new ArrayList<>();
        c(context, attributeSet);
    }
}
