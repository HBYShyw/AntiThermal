package androidx.transition;

import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* compiled from: TransitionValues.java */
/* renamed from: androidx.transition.v, reason: use source file name */
/* loaded from: classes.dex */
public class TransitionValues {

    /* renamed from: b, reason: collision with root package name */
    public View f4153b;

    /* renamed from: a, reason: collision with root package name */
    public final Map<String, Object> f4152a = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    final ArrayList<Transition> f4154c = new ArrayList<>();

    @Deprecated
    public TransitionValues() {
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TransitionValues)) {
            return false;
        }
        TransitionValues transitionValues = (TransitionValues) obj;
        return this.f4153b == transitionValues.f4153b && this.f4152a.equals(transitionValues.f4152a);
    }

    public int hashCode() {
        return (this.f4153b.hashCode() * 31) + this.f4152a.hashCode();
    }

    public String toString() {
        String str = (("TransitionValues@" + Integer.toHexString(hashCode()) + ":\n") + "    view = " + this.f4153b + "\n") + "    values:";
        for (String str2 : this.f4152a.keySet()) {
            str = str + "    " + str2 + ": " + this.f4152a.get(str2) + "\n";
        }
        return str;
    }

    public TransitionValues(View view) {
        this.f4153b = view;
    }
}
