package com.google.android.play.core.splitinstall;

import android.content.Context;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallManager;
import com.oplus.oms.split.core.splitinstall.OplusSplitInstallManagerFactory;
import f4.TaskWrapper;
import java.lang.ref.WeakReference;

/* compiled from: SplitInstallManagerWrapper.java */
/* renamed from: com.google.android.play.core.splitinstall.e, reason: use source file name */
/* loaded from: classes.dex */
public class SplitInstallManagerWrapper implements SplitInstallManager {

    /* renamed from: b, reason: collision with root package name */
    public static WeakReference<OplusSplitInstallManager<SplitInstallSessionState>> f9570b;

    /* renamed from: a, reason: collision with root package name */
    public OplusSplitInstallManager<SplitInstallSessionState> f9571a;

    public SplitInstallManagerWrapper(Context context) {
        c(context);
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public g4.d<Integer> a(SplitInstallRequest splitInstallRequest) {
        return new TaskWrapper(this.f9571a.startInstall(splitInstallRequest));
    }

    @Override // com.google.android.play.core.splitinstall.SplitInstallManager
    public void b(SplitInstallStateUpdatedListener splitInstallStateUpdatedListener) {
        this.f9571a.registerListener(splitInstallStateUpdatedListener);
    }

    public final void c(Context context) {
        synchronized (SplitInstallManagerWrapper.class) {
            WeakReference<OplusSplitInstallManager<SplitInstallSessionState>> weakReference = f9570b;
            if (weakReference != null && weakReference.get() != null) {
                this.f9571a = f9570b.get();
            } else {
                this.f9571a = OplusSplitInstallManagerFactory.create(context, new SplitInstallSessionStateFactoryImpl());
                f9570b = new WeakReference<>(this.f9571a);
            }
        }
    }
}
