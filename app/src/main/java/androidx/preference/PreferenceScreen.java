package androidx.preference;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceManager;

/* loaded from: classes.dex */
public final class PreferenceScreen extends PreferenceGroup {

    /* renamed from: n, reason: collision with root package name */
    private boolean f3282n;

    public PreferenceScreen(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, TypedArrayUtils.a(context, R$attr.preferenceScreenStyle, R.attr.preferenceScreenStyle));
        this.f3282n = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.PreferenceGroup
    public boolean j() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void onClick() {
        PreferenceManager.b g6;
        if (getIntent() != null || getFragment() != null || i() == 0 || (g6 = getPreferenceManager().g()) == null) {
            return;
        }
        g6.onNavigateToScreen(this);
    }

    public boolean s() {
        return this.f3282n;
    }
}
