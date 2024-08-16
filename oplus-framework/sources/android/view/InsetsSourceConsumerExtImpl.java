package android.view;

import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceControl;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/* loaded from: classes.dex */
public class InsetsSourceConsumerExtImpl implements IInsetsSourceConsumerExt {
    private static final String TAG = "InsetsSourceConsumerExtImpl";
    private final InsetsSourceConsumer mBase;

    public InsetsSourceConsumerExtImpl(Object base) {
        this.mBase = (InsetsSourceConsumer) base;
    }

    public void updateLeashPositionIfNeeded(InsetsSourceControl control, InsetsSourceControl lastControl, boolean needAnimation, boolean isAnimationPending, Supplier<SurfaceControl.Transaction> transactionSupplier) {
        final Point currentSurfacePosition = control.getSurfacePosition();
        SurfaceControl currentLeash = control.getLeash();
        boolean positionChanged = Optional.ofNullable(lastControl).map(new Function() { // from class: android.view.InsetsSourceConsumerExtImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Point surfacePosition;
                surfacePosition = ((InsetsSourceControl) obj).getSurfacePosition();
                return surfacePosition;
            }
        }).filter(new Predicate() { // from class: android.view.InsetsSourceConsumerExtImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return InsetsSourceConsumerExtImpl.lambda$updateLeashPositionIfNeeded$0(currentSurfacePosition, (Point) obj);
            }
        }).isPresent();
        boolean leashValid = currentLeash != null && currentLeash.isValid();
        if (this.mBase.getType() == 2 && positionChanged && !needAnimation && !isAnimationPending && leashValid) {
            if (InsetsController.DEBUG) {
                Log.d(TAG, String.format("Surface position of navigation bar changed, last=%s, current=%s", lastControl.getSurfacePosition(), currentSurfacePosition));
            }
            SurfaceControl.Transaction t = transactionSupplier.get();
            t.setPosition(currentLeash, currentSurfacePosition.x, currentSurfacePosition.y);
            t.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$updateLeashPositionIfNeeded$0(Point currentSurfacePosition, Point lastSurfacePosition) {
        return !lastSurfacePosition.equals(currentSurfacePosition);
    }

    public boolean isVisible(InsetsSourceControl control, InsetsController insetsController, InsetsState state, int id) {
        if ("com.android.launcher/com.android.launcher.Launcher".equals(insetsController.getHost().getRootViewTitle())) {
            InsetsSource insetsSource = state.peekSource(id);
            return insetsSource != null && insetsSource.isVisible();
        }
        return control.isInitiallyVisible();
    }
}
