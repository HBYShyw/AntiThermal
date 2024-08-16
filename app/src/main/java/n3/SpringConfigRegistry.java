package n3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* compiled from: SpringConfigRegistry.java */
/* renamed from: n3.h, reason: use source file name */
/* loaded from: classes.dex */
public class SpringConfigRegistry {

    /* renamed from: b, reason: collision with root package name */
    private static final SpringConfigRegistry f15749b = new SpringConfigRegistry(true);

    /* renamed from: a, reason: collision with root package name */
    private final Map<SpringConfig, String> f15750a = new HashMap();

    SpringConfigRegistry(boolean z10) {
        if (z10) {
            a(SpringConfig.f15746c, "default config");
        }
    }

    public static SpringConfigRegistry c() {
        return f15749b;
    }

    public boolean a(SpringConfig springConfig, String str) {
        if (springConfig == null) {
            throw new IllegalArgumentException("springConfig is required");
        }
        if (str != null) {
            if (this.f15750a.containsKey(springConfig)) {
                return false;
            }
            this.f15750a.put(springConfig, str);
            return true;
        }
        throw new IllegalArgumentException("configName is required");
    }

    public Map<SpringConfig, String> b() {
        return Collections.unmodifiableMap(this.f15750a);
    }
}
