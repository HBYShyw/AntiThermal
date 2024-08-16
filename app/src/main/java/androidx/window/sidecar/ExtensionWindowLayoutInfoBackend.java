package androidx.window.sidecar;

import android.app.Activity;
import androidx.core.util.Consumer;
import androidx.window.core.ConsumerAdapter;
import androidx.window.extensions.layout.WindowLayoutComponent;
import androidx.window.extensions.layout.WindowLayoutInfo;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import ma.Unit;
import za.Reflection;
import za.k;

/* compiled from: ExtensionWindowLayoutInfoBackend.kt */
@Metadata(bv = {}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001:\u0001\"B\u0017\u0012\u0006\u0010\u000e\u001a\u00020\f\u0012\u0006\u0010\u0011\u001a\u00020\u000f¢\u0006\u0004\b \u0010!J&\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\u0016\u0010\u000b\u001a\u00020\t2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016R\u0014\u0010\u000e\u001a\u00020\f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u000b\u0010\rR\u0014\u0010\u0011\u001a\u00020\u000f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\n\u0010\u0010R\u0014\u0010\u0015\u001a\u00020\u00128\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0013\u0010\u0014R \u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00170\u00168\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u0018\u0010\u0019R&\u0010\u001c\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0004\u0012\u00020\u00020\u00168\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u001b\u0010\u0019R \u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u001d0\u00168\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\u001e\u0010\u0019¨\u0006#"}, d2 = {"Landroidx/window/layout/ExtensionWindowLayoutInfoBackend;", "Landroidx/window/layout/WindowBackend;", "Landroid/app/Activity;", "activity", "Ljava/util/concurrent/Executor;", "executor", "Landroidx/core/util/a;", "Landroidx/window/layout/WindowLayoutInfo;", "callback", "Lma/f0;", "b", "a", "Landroidx/window/extensions/layout/WindowLayoutComponent;", "Landroidx/window/extensions/layout/WindowLayoutComponent;", ThermalBaseConfig.SubItem.ATTR_COMPONENT, "Landroidx/window/core/ConsumerAdapter;", "Landroidx/window/core/ConsumerAdapter;", "consumerAdapter", "Ljava/util/concurrent/locks/ReentrantLock;", "c", "Ljava/util/concurrent/locks/ReentrantLock;", "extensionWindowBackendLock", "", "Landroidx/window/layout/ExtensionWindowLayoutInfoBackend$MulticastConsumer;", "d", "Ljava/util/Map;", "activityToListeners", "e", "listenerToActivity", "Landroidx/window/core/ConsumerAdapter$Subscription;", "f", "consumerToToken", "<init>", "(Landroidx/window/extensions/layout/WindowLayoutComponent;Landroidx/window/core/ConsumerAdapter;)V", "MulticastConsumer", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class ExtensionWindowLayoutInfoBackend implements WindowBackend {

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final WindowLayoutComponent component;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final ConsumerAdapter consumerAdapter;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final ReentrantLock extensionWindowBackendLock;

    /* renamed from: d, reason: collision with root package name and from kotlin metadata */
    private final Map<Activity, MulticastConsumer> activityToListeners;

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private final Map<Consumer<WindowLayoutInfo>, Activity> listenerToActivity;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata */
    private final Map<MulticastConsumer, ConsumerAdapter.Subscription> consumerToToken;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: ExtensionWindowLayoutInfoBackend.kt */
    @Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010#\n\u0002\b\u0005\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\u0012\u0006\u0010\u000e\u001a\u00020\f¢\u0006\u0004\b\u0017\u0010\u0018J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0014\u0010\b\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001J\u0014\u0010\t\u001a\u00020\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001J\u0006\u0010\u000b\u001a\u00020\nR\u0014\u0010\u000e\u001a\u00020\f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\rR\u0014\u0010\u0011\u001a\u00020\u000f8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\b\u0010\u0010R\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00068\u0002@\u0002X\u0083\u000e¢\u0006\u0006\n\u0004\b\u000b\u0010\u0012R \u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00010\u00148\u0002X\u0083\u0004¢\u0006\u0006\n\u0004\b\t\u0010\u0015¨\u0006\u0019"}, d2 = {"Landroidx/window/layout/ExtensionWindowLayoutInfoBackend$MulticastConsumer;", "Landroidx/core/util/a;", "Landroidx/window/extensions/layout/WindowLayoutInfo;", ThermalBaseConfig.Item.ATTR_VALUE, "Lma/f0;", "a", "Landroidx/window/layout/WindowLayoutInfo;", "listener", "b", "d", "", "c", "Landroid/app/Activity;", "Landroid/app/Activity;", "activity", "Ljava/util/concurrent/locks/ReentrantLock;", "Ljava/util/concurrent/locks/ReentrantLock;", "multicastConsumerLock", "Landroidx/window/layout/WindowLayoutInfo;", "lastKnownValue", "", "Ljava/util/Set;", "registeredListeners", "<init>", "(Landroid/app/Activity;)V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class MulticastConsumer implements Consumer<WindowLayoutInfo> {

        /* renamed from: a, reason: collision with root package name and from kotlin metadata */
        private final Activity activity;

        /* renamed from: b, reason: collision with root package name and from kotlin metadata */
        private final ReentrantLock multicastConsumerLock;

        /* renamed from: c, reason: collision with root package name and from kotlin metadata */
        private WindowLayoutInfo lastKnownValue;

        /* renamed from: d, reason: collision with root package name and from kotlin metadata */
        private final Set<Consumer<WindowLayoutInfo>> registeredListeners;

        public MulticastConsumer(Activity activity) {
            k.e(activity, "activity");
            this.activity = activity;
            this.multicastConsumerLock = new ReentrantLock();
            this.registeredListeners = new LinkedHashSet();
        }

        @Override // androidx.core.util.Consumer
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void accept(WindowLayoutInfo windowLayoutInfo) {
            k.e(windowLayoutInfo, ThermalBaseConfig.Item.ATTR_VALUE);
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                this.lastKnownValue = ExtensionsWindowLayoutInfoAdapter.f4424a.b(this.activity, windowLayoutInfo);
                Iterator<T> it = this.registeredListeners.iterator();
                while (it.hasNext()) {
                    ((Consumer) it.next()).accept(this.lastKnownValue);
                }
                Unit unit = Unit.f15173a;
            } finally {
                reentrantLock.unlock();
            }
        }

        public final void b(Consumer<WindowLayoutInfo> consumer) {
            k.e(consumer, "listener");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                WindowLayoutInfo windowLayoutInfo = this.lastKnownValue;
                if (windowLayoutInfo != null) {
                    consumer.accept(windowLayoutInfo);
                }
                this.registeredListeners.add(consumer);
            } finally {
                reentrantLock.unlock();
            }
        }

        public final boolean c() {
            return this.registeredListeners.isEmpty();
        }

        public final void d(Consumer<WindowLayoutInfo> consumer) {
            k.e(consumer, "listener");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                this.registeredListeners.remove(consumer);
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public ExtensionWindowLayoutInfoBackend(WindowLayoutComponent windowLayoutComponent, ConsumerAdapter consumerAdapter) {
        k.e(windowLayoutComponent, ThermalBaseConfig.SubItem.ATTR_COMPONENT);
        k.e(consumerAdapter, "consumerAdapter");
        this.component = windowLayoutComponent;
        this.consumerAdapter = consumerAdapter;
        this.extensionWindowBackendLock = new ReentrantLock();
        this.activityToListeners = new LinkedHashMap();
        this.listenerToActivity = new LinkedHashMap();
        this.consumerToToken = new LinkedHashMap();
    }

    @Override // androidx.window.sidecar.WindowBackend
    public void a(Consumer<WindowLayoutInfo> consumer) {
        k.e(consumer, "callback");
        ReentrantLock reentrantLock = this.extensionWindowBackendLock;
        reentrantLock.lock();
        try {
            Activity activity = this.listenerToActivity.get(consumer);
            if (activity == null) {
                return;
            }
            MulticastConsumer multicastConsumer = this.activityToListeners.get(activity);
            if (multicastConsumer == null) {
                return;
            }
            multicastConsumer.d(consumer);
            if (multicastConsumer.c()) {
                ConsumerAdapter.Subscription remove = this.consumerToToken.remove(multicastConsumer);
                if (remove != null) {
                    remove.a();
                }
                this.listenerToActivity.remove(consumer);
                this.activityToListeners.remove(activity);
            }
            Unit unit = Unit.f15173a;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // androidx.window.sidecar.WindowBackend
    public void b(Activity activity, Executor executor, Consumer<WindowLayoutInfo> consumer) {
        Unit unit;
        k.e(activity, "activity");
        k.e(executor, "executor");
        k.e(consumer, "callback");
        ReentrantLock reentrantLock = this.extensionWindowBackendLock;
        reentrantLock.lock();
        try {
            MulticastConsumer multicastConsumer = this.activityToListeners.get(activity);
            if (multicastConsumer != null) {
                multicastConsumer.b(consumer);
                this.listenerToActivity.put(consumer, activity);
                unit = Unit.f15173a;
            } else {
                unit = null;
            }
            if (unit == null) {
                MulticastConsumer multicastConsumer2 = new MulticastConsumer(activity);
                this.activityToListeners.put(activity, multicastConsumer2);
                this.listenerToActivity.put(consumer, activity);
                multicastConsumer2.b(consumer);
                this.consumerToToken.put(multicastConsumer2, this.consumerAdapter.d(this.component, Reflection.b(WindowLayoutInfo.class), "addWindowLayoutInfoListener", "removeWindowLayoutInfoListener", activity, new ExtensionWindowLayoutInfoBackend$registerLayoutChangeCallback$1$2$disposableToken$1(multicastConsumer2)));
            }
            Unit unit2 = Unit.f15173a;
        } finally {
            reentrantLock.unlock();
        }
    }
}
