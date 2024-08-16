package h8;

import android.os.Bundle;
import androidx.preference.Preference;

/* compiled from: IPowerSavePresenter.java */
/* renamed from: h8.h, reason: use source file name */
/* loaded from: classes.dex */
public interface IPowerSavePresenter extends Preference.c, Preference.d {
    String b0(int i10);

    void onCreate(Bundle bundle);

    void onCreatePreferences(Bundle bundle, String str);

    void onDestroy();
}
