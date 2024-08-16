package j4;

import java.util.List;
import l4.DocumentData;
import t4.Keyframe;

/* compiled from: TextKeyframeAnimation.java */
/* renamed from: j4.o, reason: use source file name */
/* loaded from: classes.dex */
public class TextKeyframeAnimation extends KeyframeAnimation<DocumentData> {
    public TextKeyframeAnimation(List<Keyframe<DocumentData>> list) {
        super(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // j4.BaseKeyframeAnimation
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public DocumentData i(Keyframe<DocumentData> keyframe, float f10) {
        DocumentData documentData;
        if (f10 == 1.0f && (documentData = keyframe.f18571c) != null) {
            return documentData;
        }
        return keyframe.f18570b;
    }
}
