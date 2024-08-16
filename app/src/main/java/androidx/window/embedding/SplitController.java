package androidx.window.embedding;

import android.app.Activity;
import androidx.window.core.ExperimentalWindowApi;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.Metadata;
import kotlin.collections.s0;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SplitController.kt */
@ExperimentalWindowApi
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u0000 \u00112\u00020\u0001:\u0001\u0012B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\t\u001a\u00020\u00068\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\n8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\f\u0010\r¨\u0006\u0013"}, d2 = {"Landroidx/window/embedding/SplitController;", "", "Landroid/app/Activity;", "activity", "", "e", "Landroidx/window/embedding/EmbeddingBackend;", "a", "Landroidx/window/embedding/EmbeddingBackend;", "embeddingBackend", "", "Landroidx/window/embedding/EmbeddingRule;", "b", "Ljava/util/Set;", "staticSplitRules", "<init>", "()V", "c", "Companion", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SplitController {

    /* renamed from: d, reason: collision with root package name */
    private static volatile SplitController f4380d;

    /* renamed from: a, reason: collision with root package name and from kotlin metadata */
    private final EmbeddingBackend embeddingBackend;

    /* renamed from: b, reason: collision with root package name and from kotlin metadata */
    private Set<? extends EmbeddingRule> staticSplitRules;

    /* renamed from: c, reason: collision with root package name and from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);

    /* renamed from: e, reason: collision with root package name */
    private static final ReentrantLock f4381e = new ReentrantLock();

    /* compiled from: SplitController.kt */
    @Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\b\u0010\u0003\u001a\u00020\u0002H\u0007R\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0004\u0010\u0005R\u0014\u0010\u0007\u001a\u00020\u00068\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u0014\u0010\n\u001a\u00020\t8\u0000X\u0080T¢\u0006\u0006\n\u0004\b\n\u0010\u000b¨\u0006\u000e"}, d2 = {"Landroidx/window/embedding/SplitController$Companion;", "", "Landroidx/window/embedding/SplitController;", "a", "globalInstance", "Landroidx/window/embedding/SplitController;", "Ljava/util/concurrent/locks/ReentrantLock;", "globalLock", "Ljava/util/concurrent/locks/ReentrantLock;", "", "sDebug", "Z", "<init>", "()V", "window_release"}, k = 1, mv = {1, 6, 0})
    /* loaded from: classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final SplitController a() {
            if (SplitController.f4380d == null) {
                ReentrantLock reentrantLock = SplitController.f4381e;
                reentrantLock.lock();
                try {
                    if (SplitController.f4380d == null) {
                        Companion companion = SplitController.INSTANCE;
                        SplitController.f4380d = new SplitController(null);
                    }
                    Unit unit = Unit.f15173a;
                } finally {
                    reentrantLock.unlock();
                }
            }
            SplitController splitController = SplitController.f4380d;
            k.b(splitController);
            return splitController;
        }
    }

    private SplitController() {
        Set<? extends EmbeddingRule> e10;
        this.embeddingBackend = ExtensionEmbeddingBackend.INSTANCE.a();
        e10 = s0.e();
        this.staticSplitRules = e10;
    }

    public /* synthetic */ SplitController(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public static final SplitController d() {
        return INSTANCE.a();
    }

    public final boolean e(Activity activity) {
        k.e(activity, "activity");
        return this.embeddingBackend.a(activity);
    }
}
