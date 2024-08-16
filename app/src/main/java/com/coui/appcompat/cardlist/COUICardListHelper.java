package com.coui.appcompat.cardlist;

import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.coui.appcompat.preference.COUICardSupportInterface;
import java.util.ArrayList;

/* compiled from: COUICardListHelper.java */
/* renamed from: com.coui.appcompat.cardlist.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUICardListHelper {
    public static int a(int i10, int i11) {
        if (i10 == 1) {
            return 4;
        }
        if (i11 == 0) {
            return 1;
        }
        return i11 == i10 - 1 ? 3 : 2;
    }

    public static int b(Preference preference) {
        PreferenceGroup parent = preference.getParent();
        int i10 = 0;
        if (parent == null) {
            return 0;
        }
        ArrayList arrayList = new ArrayList();
        for (int i11 = 0; i11 < parent.i(); i11++) {
            Preference h10 = parent.h(i11);
            if (h10.isVisible()) {
                arrayList.add(h10);
            }
        }
        int size = arrayList.size();
        int i12 = 0;
        while (true) {
            if (i12 >= size) {
                break;
            }
            if (preference == arrayList.get(i12)) {
                i10 = i12;
                break;
            }
            i12++;
        }
        Preference preference2 = i10 > 0 ? (Preference) arrayList.get(i10 - 1) : null;
        Preference preference3 = i10 < size - 1 ? (Preference) arrayList.get(i10 + 1) : null;
        int i13 = (preference2 == null || !c(parent, preference2)) ? 1 : 2;
        return (preference3 == null || !c(parent, preference3)) ? i13 == 1 ? 4 : 3 : i13;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean c(PreferenceGroup preferenceGroup, Preference preference) {
        return preferenceGroup instanceof PreferenceScreen ? (preference instanceof COUICardSupportInterface) && ((COUICardSupportInterface) preference).isSupportCardUse() : !(preference instanceof PreferenceCategory);
    }

    public static void d(View view, int i10) {
        if (view == null || !(view instanceof COUICardListSelectedItemLayout)) {
            return;
        }
        ((COUICardListSelectedItemLayout) view).setPositionInGroup(i10);
    }
}
