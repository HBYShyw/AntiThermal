package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.preference.Preference;

/* loaded from: classes.dex */
public class SeekBarPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    int f3283e;

    /* renamed from: f, reason: collision with root package name */
    int f3284f;

    /* renamed from: g, reason: collision with root package name */
    private int f3285g;

    /* renamed from: h, reason: collision with root package name */
    private int f3286h;

    /* renamed from: i, reason: collision with root package name */
    boolean f3287i;

    /* renamed from: j, reason: collision with root package name */
    SeekBar f3288j;

    /* renamed from: k, reason: collision with root package name */
    private TextView f3289k;

    /* renamed from: l, reason: collision with root package name */
    boolean f3290l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f3291m;

    /* renamed from: n, reason: collision with root package name */
    boolean f3292n;

    /* renamed from: o, reason: collision with root package name */
    private SeekBar.OnSeekBarChangeListener f3293o;

    /* renamed from: p, reason: collision with root package name */
    private View.OnKeyListener f3294p;

    /* loaded from: classes.dex */
    class a implements SeekBar.OnSeekBarChangeListener {
        a() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i10, boolean z10) {
            if (z10) {
                SeekBarPreference seekBarPreference = SeekBarPreference.this;
                if (seekBarPreference.f3292n || !seekBarPreference.f3287i) {
                    seekBarPreference.g(seekBar);
                    return;
                }
            }
            SeekBarPreference seekBarPreference2 = SeekBarPreference.this;
            seekBarPreference2.h(i10 + seekBarPreference2.f3284f);
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            SeekBarPreference.this.f3287i = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            SeekBarPreference.this.f3287i = false;
            int progress = seekBar.getProgress();
            SeekBarPreference seekBarPreference = SeekBarPreference.this;
            if (progress + seekBarPreference.f3284f != seekBarPreference.f3283e) {
                seekBarPreference.g(seekBar);
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements View.OnKeyListener {
        b() {
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i10, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 0) {
                return false;
            }
            SeekBarPreference seekBarPreference = SeekBarPreference.this;
            if ((!seekBarPreference.f3290l && (i10 == 21 || i10 == 22)) || i10 == 23 || i10 == 66) {
                return false;
            }
            SeekBar seekBar = seekBarPreference.f3288j;
            if (seekBar == null) {
                Log.e("SeekBarPreference", "SeekBar view is null and hence cannot be adjusted.");
                return false;
            }
            return seekBar.onKeyDown(i10, keyEvent);
        }
    }

    public SeekBarPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f3293o = new a();
        this.f3294p = new b();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SeekBarPreference, i10, i11);
        this.f3284f = obtainStyledAttributes.getInt(R$styleable.SeekBarPreference_min, 0);
        c(obtainStyledAttributes.getInt(R$styleable.SeekBarPreference_android_max, 100));
        d(obtainStyledAttributes.getInt(R$styleable.SeekBarPreference_seekBarIncrement, 0));
        this.f3290l = obtainStyledAttributes.getBoolean(R$styleable.SeekBarPreference_adjustable, true);
        this.f3291m = obtainStyledAttributes.getBoolean(R$styleable.SeekBarPreference_showSeekBarValue, false);
        this.f3292n = obtainStyledAttributes.getBoolean(R$styleable.SeekBarPreference_updatesContinuously, false);
        obtainStyledAttributes.recycle();
    }

    private void f(int i10, boolean z10) {
        int i11 = this.f3284f;
        if (i10 < i11) {
            i10 = i11;
        }
        int i12 = this.f3285g;
        if (i10 > i12) {
            i10 = i12;
        }
        if (i10 != this.f3283e) {
            this.f3283e = i10;
            h(i10);
            persistInt(i10);
            if (z10) {
                notifyChanged();
            }
        }
    }

    public final void c(int i10) {
        int i11 = this.f3284f;
        if (i10 < i11) {
            i10 = i11;
        }
        if (i10 != this.f3285g) {
            this.f3285g = i10;
            notifyChanged();
        }
    }

    public final void d(int i10) {
        if (i10 != this.f3286h) {
            this.f3286h = Math.min(this.f3285g - this.f3284f, Math.abs(i10));
            notifyChanged();
        }
    }

    public void e(int i10) {
        f(i10, true);
    }

    void g(SeekBar seekBar) {
        int progress = this.f3284f + seekBar.getProgress();
        if (progress != this.f3283e) {
            if (callChangeListener(Integer.valueOf(progress))) {
                f(progress, false);
            } else {
                seekBar.setProgress(this.f3283e - this.f3284f);
                h(this.f3283e);
            }
        }
    }

    void h(int i10) {
        TextView textView = this.f3289k;
        if (textView != null) {
            textView.setText(String.valueOf(i10));
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setOnKeyListener(this.f3294p);
        this.f3288j = (SeekBar) preferenceViewHolder.a(R$id.seekbar);
        TextView textView = (TextView) preferenceViewHolder.a(R$id.seekbar_value);
        this.f3289k = textView;
        if (this.f3291m) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
            this.f3289k = null;
        }
        SeekBar seekBar = this.f3288j;
        if (seekBar == null) {
            Log.e("SeekBarPreference", "SeekBar view is null in onBindViewHolder.");
            return;
        }
        seekBar.setOnSeekBarChangeListener(this.f3293o);
        this.f3288j.setMax(this.f3285g - this.f3284f);
        int i10 = this.f3286h;
        if (i10 != 0) {
            this.f3288j.setKeyProgressIncrement(i10);
        } else {
            this.f3286h = this.f3288j.getKeyProgressIncrement();
        }
        this.f3288j.setProgress(this.f3283e - this.f3284f);
        h(this.f3283e);
        this.f3288j.setEnabled(isEnabled());
    }

    @Override // androidx.preference.Preference
    protected Object onGetDefaultValue(TypedArray typedArray, int i10) {
        return Integer.valueOf(typedArray.getInt(i10, 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.f3283e = savedState.f3295e;
        this.f3284f = savedState.f3296f;
        this.f3285g = savedState.f3297g;
        notifyChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return onSaveInstanceState;
        }
        SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.f3295e = this.f3283e;
        savedState.f3296f = this.f3284f;
        savedState.f3297g = this.f3285g;
        return savedState;
    }

    @Override // androidx.preference.Preference
    protected void onSetInitialValue(Object obj) {
        if (obj == null) {
            obj = 0;
        }
        e(getPersistedInt(((Integer) obj).intValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f3295e;

        /* renamed from: f, reason: collision with root package name */
        int f3296f;

        /* renamed from: g, reason: collision with root package name */
        int f3297g;

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
            this.f3295e = parcel.readInt();
            this.f3296f = parcel.readInt();
            this.f3297g = parcel.readInt();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f3295e);
            parcel.writeInt(this.f3296f);
            parcel.writeInt(this.f3297g);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    public SeekBarPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public SeekBarPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.seekBarPreferenceStyle);
    }
}
