package androidx.constraintlayout.motion.widget;

import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintSet;

/* compiled from: TransitionBuilder.java */
/* renamed from: androidx.constraintlayout.motion.widget.u, reason: use source file name */
/* loaded from: classes.dex */
public class TransitionBuilder {
    public static MotionScene.b a(MotionScene motionScene, int i10, int i11, ConstraintSet constraintSet, int i12, ConstraintSet constraintSet2) {
        MotionScene.b bVar = new MotionScene.b(i10, motionScene, i11, i12);
        b(motionScene, bVar, constraintSet, constraintSet2);
        return bVar;
    }

    private static void b(MotionScene motionScene, MotionScene.b bVar, ConstraintSet constraintSet, ConstraintSet constraintSet2) {
        int A = bVar.A();
        int y4 = bVar.y();
        motionScene.M(A, constraintSet);
        motionScene.M(y4, constraintSet2);
    }
}
