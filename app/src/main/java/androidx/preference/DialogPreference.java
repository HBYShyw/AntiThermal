package androidx.preference;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;

/* loaded from: classes.dex */
public abstract class DialogPreference extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private CharSequence f3243e;

    /* renamed from: f, reason: collision with root package name */
    private CharSequence f3244f;

    /* renamed from: g, reason: collision with root package name */
    private Drawable f3245g;

    /* renamed from: h, reason: collision with root package name */
    private CharSequence f3246h;

    /* renamed from: i, reason: collision with root package name */
    private CharSequence f3247i;

    /* renamed from: j, reason: collision with root package name */
    private int f3248j;

    /* loaded from: classes.dex */
    public interface a {
        <T extends Preference> T findPreference(CharSequence charSequence);
    }

    public DialogPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DialogPreference, i10, i11);
        String k10 = TypedArrayUtils.k(obtainStyledAttributes, R$styleable.DialogPreference_dialogTitle, R$styleable.DialogPreference_android_dialogTitle);
        this.f3243e = k10;
        if (k10 == null) {
            this.f3243e = getTitle();
        }
        this.f3244f = TypedArrayUtils.k(obtainStyledAttributes, R$styleable.DialogPreference_dialogMessage, R$styleable.DialogPreference_android_dialogMessage);
        this.f3245g = TypedArrayUtils.c(obtainStyledAttributes, R$styleable.DialogPreference_dialogIcon, R$styleable.DialogPreference_android_dialogIcon);
        this.f3246h = TypedArrayUtils.k(obtainStyledAttributes, R$styleable.DialogPreference_positiveButtonText, R$styleable.DialogPreference_android_positiveButtonText);
        this.f3247i = TypedArrayUtils.k(obtainStyledAttributes, R$styleable.DialogPreference_negativeButtonText, R$styleable.DialogPreference_android_negativeButtonText);
        this.f3248j = TypedArrayUtils.j(obtainStyledAttributes, R$styleable.DialogPreference_dialogLayout, R$styleable.DialogPreference_android_dialogLayout, 0);
        obtainStyledAttributes.recycle();
    }

    public Drawable c() {
        return this.f3245g;
    }

    public int d() {
        return this.f3248j;
    }

    public CharSequence e() {
        return this.f3244f;
    }

    public CharSequence f() {
        return this.f3243e;
    }

    public CharSequence g() {
        return this.f3247i;
    }

    public CharSequence h() {
        return this.f3246h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void onClick() {
        getPreferenceManager().u(this);
    }

    public DialogPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public DialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.a(context, R$attr.dialogPreferenceStyle, R.attr.dialogPreferenceStyle));
    }
}
