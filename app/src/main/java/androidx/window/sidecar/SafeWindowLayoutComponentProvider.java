package androidx.window.sidecar;

import androidx.window.core.ConsumerAdapter;
import androidx.window.extensions.WindowExtensionsProvider;
import androidx.window.extensions.layout.WindowLayoutComponent;
import gb.KClass;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import kotlin.Metadata;
import xa.JvmClassMapping;
import ya.a;
import za.k;

/* compiled from: SafeWindowLayoutComponentProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0014\u001a\u00020\u0011\u0012\u0006\u0010\u0018\u001a\u00020\u0015¢\u0006\u0004\b)\u0010*J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0004\u001a\u00020\u0002H\u0002J\b\u0010\u0005\u001a\u00020\u0002H\u0002J\b\u0010\u0006\u001a\u00020\u0002H\u0002J\b\u0010\u0007\u001a\u00020\u0002H\u0002J\u0016\u0010\n\u001a\u00020\u00022\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\bH\u0002J\u0018\u0010\u000e\u001a\u00020\u0002*\u00020\u000b2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\fH\u0002J\u0018\u0010\u0010\u001a\u00020\u0002*\u00020\u000b2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u000fH\u0002R\u0014\u0010\u0014\u001a\u00020\u00118\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0018\u001a\u00020\u00158\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0016\u0010\u0017R\u0018\u0010\u001b\u001a\u00020\u0002*\u00020\u000b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0018\u0010\u001e\u001a\u0006\u0012\u0002\b\u00030\u000f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0018\u0010 \u001a\u0006\u0012\u0002\b\u00030\u000f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u001dR\u0018\u0010\"\u001a\u0006\u0012\u0002\b\u00030\u000f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b!\u0010\u001dR\u0018\u0010$\u001a\u0006\u0012\u0002\b\u00030\u000f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b#\u0010\u001dR\u0013\u0010(\u001a\u0004\u0018\u00010%8F¢\u0006\u0006\u001a\u0004\b&\u0010'¨\u0006+"}, d2 = {"Landroidx/window/layout/SafeWindowLayoutComponentProvider;", "", "", "i", "u", "s", "q", "t", "Lkotlin/Function0;", "block", "v", "Ljava/lang/reflect/Method;", "Lgb/d;", "clazz", "j", "Ljava/lang/Class;", "k", "Ljava/lang/ClassLoader;", "a", "Ljava/lang/ClassLoader;", "loader", "Landroidx/window/core/ConsumerAdapter;", "b", "Landroidx/window/core/ConsumerAdapter;", "consumerAdapter", "r", "(Ljava/lang/reflect/Method;)Z", "isPublic", "n", "()Ljava/lang/Class;", "windowExtensionsProviderClass", "m", "windowExtensionsClass", "l", "foldingFeatureClass", "p", "windowLayoutComponentClass", "Landroidx/window/extensions/layout/WindowLayoutComponent;", "o", "()Landroidx/window/extensions/layout/WindowLayoutComponent;", "windowLayoutComponent", "<init>", "(Ljava/lang/ClassLoader;Landroidx/window/core/ConsumerAdapter;)V", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SafeWindowLayoutComponentProvider {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final ClassLoader loader;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final ConsumerAdapter consumerAdapter;

    public SafeWindowLayoutComponentProvider(ClassLoader classLoader, ConsumerAdapter consumerAdapter) {
        k.e(classLoader, "loader");
        k.e(consumerAdapter, "consumerAdapter");
        this.loader = classLoader;
        this.consumerAdapter = consumerAdapter;
    }

    private final boolean i() {
        return u() && s() && t() && q();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean j(Method method, KClass<?> kClass) {
        return k(method, JvmClassMapping.b(kClass));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean k(Method method, Class<?> cls) {
        return method.getReturnType().equals(cls);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Class<?> l() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.layout.FoldingFeature");
        k.d(loadClass, "loader.loadClass(\"androi…s.layout.FoldingFeature\")");
        return loadClass;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Class<?> m() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.WindowExtensions");
        k.d(loadClass, "loader.loadClass(\"androi…nsions.WindowExtensions\")");
        return loadClass;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Class<?> n() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.WindowExtensionsProvider");
        k.d(loadClass, "loader.loadClass(\"androi…indowExtensionsProvider\")");
        return loadClass;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Class<?> p() {
        Class<?> loadClass = this.loader.loadClass("androidx.window.extensions.layout.WindowLayoutComponent");
        k.d(loadClass, "loader.loadClass(\"androi…t.WindowLayoutComponent\")");
        return loadClass;
    }

    private final boolean q() {
        return v(new SafeWindowLayoutComponentProvider$isFoldingFeatureValid$1(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean r(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    private final boolean s() {
        return v(new SafeWindowLayoutComponentProvider$isWindowExtensionsValid$1(this));
    }

    private final boolean t() {
        return v(new SafeWindowLayoutComponentProvider$isWindowLayoutComponentValid$1(this));
    }

    private final boolean u() {
        return v(new SafeWindowLayoutComponentProvider$isWindowLayoutProviderValid$1(this));
    }

    private final boolean v(a<Boolean> aVar) {
        try {
            return aVar.invoke().booleanValue();
        } catch (ClassNotFoundException | NoSuchMethodException unused) {
            return false;
        }
    }

    public final WindowLayoutComponent o() {
        if (i()) {
            try {
                return WindowExtensionsProvider.getWindowExtensions().getWindowLayoutComponent();
            } catch (UnsupportedOperationException unused) {
                return null;
            }
        }
        return null;
    }
}
