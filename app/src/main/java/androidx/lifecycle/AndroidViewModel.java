package androidx.lifecycle;

import android.annotation.SuppressLint;
import android.app.Application;

/* compiled from: AndroidViewModel.java */
/* renamed from: androidx.lifecycle.a, reason: use source file name */
/* loaded from: classes.dex */
public class AndroidViewModel extends ViewModel {

    /* renamed from: d, reason: collision with root package name */
    @SuppressLint({"StaticFieldLeak"})
    private Application f3147d;

    public AndroidViewModel(Application application) {
        this.f3147d = application;
    }

    public <T extends Application> T f() {
        return (T) this.f3147d;
    }
}
