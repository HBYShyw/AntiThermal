package androidx.loader.app;

import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.o;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* compiled from: LoaderManager.java */
/* renamed from: androidx.loader.app.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class LoaderManager {
    public static <T extends o & ViewModelStoreOwner> LoaderManager b(T t7) {
        return new LoaderManagerImpl(t7, t7.getViewModelStore());
    }

    @Deprecated
    public abstract void a(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract void c();
}
