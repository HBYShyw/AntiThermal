package m4;

import java.util.Arrays;
import java.util.List;
import t4.Keyframe;

/* compiled from: BaseAnimatableValue.java */
/* renamed from: m4.n, reason: use source file name */
/* loaded from: classes.dex */
abstract class BaseAnimatableValue<V, O> implements AnimatableValue<V, O> {

    /* renamed from: a, reason: collision with root package name */
    final List<Keyframe<V>> f14927a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAnimatableValue(List<Keyframe<V>> list) {
        this.f14927a = list;
    }

    @Override // m4.AnimatableValue
    public List<Keyframe<V>> b() {
        return this.f14927a;
    }

    @Override // m4.AnimatableValue
    public boolean o() {
        return this.f14927a.isEmpty() || (this.f14927a.size() == 1 && this.f14927a.get(0).h());
    }

    public String toString() {
        StringBuilder sb2 = new StringBuilder();
        if (!this.f14927a.isEmpty()) {
            sb2.append("values=");
            sb2.append(Arrays.toString(this.f14927a.toArray()));
        }
        return sb2.toString();
    }
}
