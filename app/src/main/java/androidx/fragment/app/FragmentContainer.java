package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/* compiled from: FragmentContainer.java */
/* renamed from: androidx.fragment.app.e, reason: use source file name */
/* loaded from: classes.dex */
public abstract class FragmentContainer {
    @Deprecated
    public Fragment b(Context context, String str, Bundle bundle) {
        return Fragment.instantiate(context, str, bundle);
    }

    public abstract View c(int i10);

    public abstract boolean d();
}
