package system.ext.registry;

import system.ext.loader.core.ExtCreator;
import system.ext.loader.core.ExtRegistry;
import system.ext.preload.IServicesPreloadExt;
import system.ext.preload.ServicesPreloadExtImpl;

/* loaded from: classes.dex */
public class PreloadRegistry {
    static {
        ExtRegistry.registerExt(IServicesPreloadExt.class, new ExtCreator() { // from class: system.ext.registry.PreloadRegistry$$ExternalSyntheticLambda0
            public final Object createExtWith(Object obj) {
                return new ServicesPreloadExtImpl(obj);
            }
        });
    }
}
