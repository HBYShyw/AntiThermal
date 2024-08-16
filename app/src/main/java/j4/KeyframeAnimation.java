package j4;

import java.util.List;
import t4.Keyframe;

/* compiled from: KeyframeAnimation.java */
/* renamed from: j4.g, reason: use source file name */
/* loaded from: classes.dex */
abstract class KeyframeAnimation<T> extends BaseKeyframeAnimation<T, T> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyframeAnimation(List<? extends Keyframe<T>> list) {
        super(list);
    }
}
