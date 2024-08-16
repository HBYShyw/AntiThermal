package androidx.lifecycle;

import android.content.Context;
import androidx.startup.AppInitializer;
import e0.Initializer;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class ProcessLifecycleInitializer implements Initializer<o> {
    @Override // e0.Initializer
    public List<Class<? extends Initializer<?>>> a() {
        return Collections.emptyList();
    }

    @Override // e0.Initializer
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public o b(Context context) {
        if (AppInitializer.e(context).g(ProcessLifecycleInitializer.class)) {
            LifecycleDispatcher.a(context);
            ProcessLifecycleOwner.i(context);
            return ProcessLifecycleOwner.h();
        }
        throw new IllegalStateException("ProcessLifecycleInitializer cannot be initialized lazily. \nPlease ensure that you have: \n<meta-data\n    android:name='androidx.lifecycle.ProcessLifecycleInitializer' \n    android:value='androidx.startup' /> \nunder InitializationProvider in your AndroidManifest.xml");
    }
}
