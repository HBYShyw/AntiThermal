package androidx.transition;

import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class AutoTransition extends TransitionSet {
    public AutoTransition() {
        v();
    }

    private void v() {
        r(1);
        f(new Fade(2)).f(new ChangeBounds()).f(new Fade(1));
    }

    public AutoTransition(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        v();
    }
}
