package androidx.window.sidecar;

import android.app.Activity;
import androidx.core.util.Consumer;
import java.util.concurrent.Executor;
import kotlin.Metadata;

/* compiled from: WindowBackend.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b`\u0018\u00002\u00020\u0001J&\u0010\n\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H&J\u0016\u0010\u000b\u001a\u00020\t2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H&¨\u0006\f"}, d2 = {"Landroidx/window/layout/WindowBackend;", "", "Landroid/app/Activity;", "activity", "Ljava/util/concurrent/Executor;", "executor", "Landroidx/core/util/a;", "Landroidx/window/layout/WindowLayoutInfo;", "callback", "Lma/f0;", "b", "a", "window_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public interface WindowBackend {
    void a(Consumer<WindowLayoutInfo> consumer);

    void b(Activity activity, Executor executor, Consumer<WindowLayoutInfo> consumer);
}
