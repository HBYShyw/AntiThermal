package androidx.preference;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ExpandButton.java */
/* renamed from: androidx.preference.b, reason: use source file name */
/* loaded from: classes.dex */
final class ExpandButton extends Preference {

    /* renamed from: e, reason: collision with root package name */
    private long f3309e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExpandButton(Context context, List<Preference> list, long j10) {
        super(context);
        c();
        d(list);
        this.f3309e = j10 + 1000000;
    }

    private void c() {
        setLayoutResource(R$layout.expand_button);
        setIcon(R$drawable.ic_arrow_down_24dp);
        setTitle(R$string.expand_button_title);
        setOrder(999);
    }

    private void d(List<Preference> list) {
        ArrayList arrayList = new ArrayList();
        CharSequence charSequence = null;
        for (Preference preference : list) {
            CharSequence title = preference.getTitle();
            boolean z10 = preference instanceof PreferenceGroup;
            if (z10 && !TextUtils.isEmpty(title)) {
                arrayList.add((PreferenceGroup) preference);
            }
            if (arrayList.contains(preference.getParent())) {
                if (z10) {
                    arrayList.add((PreferenceGroup) preference);
                }
            } else if (!TextUtils.isEmpty(title)) {
                charSequence = charSequence == null ? title : getContext().getString(R$string.summary_collapsed_preference_list, charSequence, title);
            }
        }
        setSummary(charSequence);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.preference.Preference
    public long getId() {
        return this.f3309e;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.d(false);
    }
}
