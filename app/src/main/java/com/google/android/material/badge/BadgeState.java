package com.google.android.material.badge;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import com.google.android.material.R$dimen;
import com.google.android.material.R$plurals;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import java.util.Locale;
import z3.MaterialResources;
import z3.TextAppearance;

/* loaded from: classes.dex */
public final class BadgeState {

    /* renamed from: a, reason: collision with root package name */
    private final State f8274a;

    /* renamed from: b, reason: collision with root package name */
    private final State f8275b;

    /* renamed from: c, reason: collision with root package name */
    final float f8276c;

    /* renamed from: d, reason: collision with root package name */
    final float f8277d;

    /* renamed from: e, reason: collision with root package name */
    final float f8278e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BadgeState(Context context, int i10, int i11, int i12, State state) {
        CharSequence charSequence;
        int i13;
        int i14;
        int i15;
        int intValue;
        int intValue2;
        int intValue3;
        int intValue4;
        int intValue5;
        int intValue6;
        State state2 = new State();
        this.f8275b = state2;
        state = state == null ? new State() : state;
        if (i10 != 0) {
            state.f8279e = i10;
        }
        TypedArray a10 = a(context, state.f8279e, i11, i12);
        Resources resources = context.getResources();
        this.f8276c = a10.getDimensionPixelSize(R$styleable.Badge_badgeRadius, resources.getDimensionPixelSize(R$dimen.mtrl_badge_radius));
        this.f8278e = a10.getDimensionPixelSize(R$styleable.Badge_badgeWidePadding, resources.getDimensionPixelSize(R$dimen.mtrl_badge_long_text_horizontal_padding));
        this.f8277d = a10.getDimensionPixelSize(R$styleable.Badge_badgeWithTextRadius, resources.getDimensionPixelSize(R$dimen.mtrl_badge_with_text_radius));
        state2.f8282h = state.f8282h == -2 ? 255 : state.f8282h;
        if (state.f8286l == null) {
            charSequence = context.getString(R$string.mtrl_badge_numberless_content_description);
        } else {
            charSequence = state.f8286l;
        }
        state2.f8286l = charSequence;
        if (state.f8287m == 0) {
            i13 = R$plurals.mtrl_badge_content_description;
        } else {
            i13 = state.f8287m;
        }
        state2.f8287m = i13;
        if (state.f8288n == 0) {
            i14 = R$string.mtrl_exceed_max_badge_number_content_description;
        } else {
            i14 = state.f8288n;
        }
        state2.f8288n = i14;
        state2.f8290p = Boolean.valueOf(state.f8290p == null || state.f8290p.booleanValue());
        if (state.f8284j == -2) {
            i15 = a10.getInt(R$styleable.Badge_maxCharacterCount, 4);
        } else {
            i15 = state.f8284j;
        }
        state2.f8284j = i15;
        if (state.f8283i != -2) {
            state2.f8283i = state.f8283i;
        } else {
            int i16 = R$styleable.Badge_number;
            if (a10.hasValue(i16)) {
                state2.f8283i = a10.getInt(i16, 0);
            } else {
                state2.f8283i = -1;
            }
        }
        if (state.f8280f == null) {
            intValue = u(context, a10, R$styleable.Badge_backgroundColor);
        } else {
            intValue = state.f8280f.intValue();
        }
        state2.f8280f = Integer.valueOf(intValue);
        if (state.f8281g != null) {
            state2.f8281g = state.f8281g;
        } else {
            int i17 = R$styleable.Badge_badgeTextColor;
            if (a10.hasValue(i17)) {
                state2.f8281g = Integer.valueOf(u(context, a10, i17));
            } else {
                state2.f8281g = Integer.valueOf(new TextAppearance(context, R$style.TextAppearance_MaterialComponents_Badge).i().getDefaultColor());
            }
        }
        if (state.f8289o == null) {
            intValue2 = a10.getInt(R$styleable.Badge_badgeGravity, 8388661);
        } else {
            intValue2 = state.f8289o.intValue();
        }
        state2.f8289o = Integer.valueOf(intValue2);
        if (state.f8291q == null) {
            intValue3 = a10.getDimensionPixelOffset(R$styleable.Badge_horizontalOffset, 0);
        } else {
            intValue3 = state.f8291q.intValue();
        }
        state2.f8291q = Integer.valueOf(intValue3);
        if (state.f8291q == null) {
            intValue4 = a10.getDimensionPixelOffset(R$styleable.Badge_verticalOffset, 0);
        } else {
            intValue4 = state.f8292r.intValue();
        }
        state2.f8292r = Integer.valueOf(intValue4);
        if (state.f8293s == null) {
            intValue5 = a10.getDimensionPixelOffset(R$styleable.Badge_horizontalOffsetWithText, state2.f8291q.intValue());
        } else {
            intValue5 = state.f8293s.intValue();
        }
        state2.f8293s = Integer.valueOf(intValue5);
        if (state.f8294t == null) {
            intValue6 = a10.getDimensionPixelOffset(R$styleable.Badge_verticalOffsetWithText, state2.f8292r.intValue());
        } else {
            intValue6 = state.f8294t.intValue();
        }
        state2.f8294t = Integer.valueOf(intValue6);
        state2.f8295u = Integer.valueOf(state.f8295u == null ? 0 : state.f8295u.intValue());
        state2.f8296v = Integer.valueOf(state.f8296v != null ? state.f8296v.intValue() : 0);
        a10.recycle();
        if (state.f8285k == null) {
            state2.f8285k = Locale.getDefault(Locale.Category.FORMAT);
        } else {
            state2.f8285k = state.f8285k;
        }
        this.f8274a = state;
    }

    private TypedArray a(Context context, int i10, int i11, int i12) {
        AttributeSet attributeSet;
        int i13;
        if (i10 != 0) {
            attributeSet = t3.a.a(context, i10, "badge");
            i13 = attributeSet.getStyleAttribute();
        } else {
            attributeSet = null;
            i13 = 0;
        }
        return ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.Badge, i11, i13 == 0 ? i12 : i13, new int[0]);
    }

    private static int u(Context context, TypedArray typedArray, int i10) {
        return MaterialResources.a(context, typedArray, i10).getDefaultColor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f8275b.f8295u.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c() {
        return this.f8275b.f8296v.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d() {
        return this.f8275b.f8282h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e() {
        return this.f8275b.f8280f.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f() {
        return this.f8275b.f8289o.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g() {
        return this.f8275b.f8281g.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int h() {
        return this.f8275b.f8288n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence i() {
        return this.f8275b.f8286l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j() {
        return this.f8275b.f8287m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f8275b.f8293s.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int l() {
        return this.f8275b.f8291q.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int m() {
        return this.f8275b.f8284j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int n() {
        return this.f8275b.f8283i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Locale o() {
        return this.f8275b.f8285k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public State p() {
        return this.f8274a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int q() {
        return this.f8275b.f8294t.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int r() {
        return this.f8275b.f8292r.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s() {
        return this.f8275b.f8283i != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t() {
        return this.f8275b.f8290p.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(int i10) {
        this.f8274a.f8282h = i10;
        this.f8275b.f8282h = i10;
    }

    /* loaded from: classes.dex */
    public static final class State implements Parcelable {
        public static final Parcelable.Creator<State> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        private int f8279e;

        /* renamed from: f, reason: collision with root package name */
        private Integer f8280f;

        /* renamed from: g, reason: collision with root package name */
        private Integer f8281g;

        /* renamed from: h, reason: collision with root package name */
        private int f8282h;

        /* renamed from: i, reason: collision with root package name */
        private int f8283i;

        /* renamed from: j, reason: collision with root package name */
        private int f8284j;

        /* renamed from: k, reason: collision with root package name */
        private Locale f8285k;

        /* renamed from: l, reason: collision with root package name */
        private CharSequence f8286l;

        /* renamed from: m, reason: collision with root package name */
        private int f8287m;

        /* renamed from: n, reason: collision with root package name */
        private int f8288n;

        /* renamed from: o, reason: collision with root package name */
        private Integer f8289o;

        /* renamed from: p, reason: collision with root package name */
        private Boolean f8290p;

        /* renamed from: q, reason: collision with root package name */
        private Integer f8291q;

        /* renamed from: r, reason: collision with root package name */
        private Integer f8292r;

        /* renamed from: s, reason: collision with root package name */
        private Integer f8293s;

        /* renamed from: t, reason: collision with root package name */
        private Integer f8294t;

        /* renamed from: u, reason: collision with root package name */
        private Integer f8295u;

        /* renamed from: v, reason: collision with root package name */
        private Integer f8296v;

        /* loaded from: classes.dex */
        class a implements Parcelable.Creator<State> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public State createFromParcel(Parcel parcel) {
                return new State(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public State[] newArray(int i10) {
                return new State[i10];
            }
        }

        public State() {
            this.f8282h = 255;
            this.f8283i = -2;
            this.f8284j = -2;
            this.f8290p = Boolean.TRUE;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            parcel.writeInt(this.f8279e);
            parcel.writeSerializable(this.f8280f);
            parcel.writeSerializable(this.f8281g);
            parcel.writeInt(this.f8282h);
            parcel.writeInt(this.f8283i);
            parcel.writeInt(this.f8284j);
            CharSequence charSequence = this.f8286l;
            parcel.writeString(charSequence == null ? null : charSequence.toString());
            parcel.writeInt(this.f8287m);
            parcel.writeSerializable(this.f8289o);
            parcel.writeSerializable(this.f8291q);
            parcel.writeSerializable(this.f8292r);
            parcel.writeSerializable(this.f8293s);
            parcel.writeSerializable(this.f8294t);
            parcel.writeSerializable(this.f8295u);
            parcel.writeSerializable(this.f8296v);
            parcel.writeSerializable(this.f8290p);
            parcel.writeSerializable(this.f8285k);
        }

        State(Parcel parcel) {
            this.f8282h = 255;
            this.f8283i = -2;
            this.f8284j = -2;
            this.f8290p = Boolean.TRUE;
            this.f8279e = parcel.readInt();
            this.f8280f = (Integer) parcel.readSerializable();
            this.f8281g = (Integer) parcel.readSerializable();
            this.f8282h = parcel.readInt();
            this.f8283i = parcel.readInt();
            this.f8284j = parcel.readInt();
            this.f8286l = parcel.readString();
            this.f8287m = parcel.readInt();
            this.f8289o = (Integer) parcel.readSerializable();
            this.f8291q = (Integer) parcel.readSerializable();
            this.f8292r = (Integer) parcel.readSerializable();
            this.f8293s = (Integer) parcel.readSerializable();
            this.f8294t = (Integer) parcel.readSerializable();
            this.f8295u = (Integer) parcel.readSerializable();
            this.f8296v = (Integer) parcel.readSerializable();
            this.f8290p = (Boolean) parcel.readSerializable();
            this.f8285k = (Locale) parcel.readSerializable();
        }
    }
}
