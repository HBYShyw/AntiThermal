package system.ext.preload;

import system.ext.loader.preload.PreloadExtImpl;

/* loaded from: classes.dex */
public class ServicesPreloadExtImpl extends PreloadExtImpl implements IServicesPreloadExt {
    public ServicesPreloadExtImpl(Object base) {
        register("system.ext.registry.ServicesRegistry");
        register("system.ext.registry.BaseServicesCoreRegistry");
        register("system.ext.registry.BiometricsServicesCoreRegistry");
        register("system.ext.registry.SocServicesCoreRegistry");
        register("system.ext.registry.ApexJobschedulerServicesRegistry");
    }
}
