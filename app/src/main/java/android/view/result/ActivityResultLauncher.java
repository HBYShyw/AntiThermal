package android.view.result;

import android.annotation.SuppressLint;
import androidx.core.app.ActivityOptionsCompat;

/* compiled from: ActivityResultLauncher.java */
/* renamed from: androidx.activity.result.b, reason: use source file name */
/* loaded from: classes.dex */
public abstract class ActivityResultLauncher<I> {
    public void a(@SuppressLint({"UnknownNullness"}) I i10) {
        b(i10, null);
    }

    public abstract void b(@SuppressLint({"UnknownNullness"}) I i10, ActivityOptionsCompat activityOptionsCompat);

    public abstract void c();
}
