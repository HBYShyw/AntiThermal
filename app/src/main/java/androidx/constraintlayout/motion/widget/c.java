package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.util.HashMap;
import java.util.HashSet;

/* compiled from: Key.java */
/* loaded from: classes.dex */
public abstract class c {

    /* renamed from: f, reason: collision with root package name */
    public static int f1481f = -1;

    /* renamed from: a, reason: collision with root package name */
    int f1482a;

    /* renamed from: b, reason: collision with root package name */
    int f1483b;

    /* renamed from: c, reason: collision with root package name */
    String f1484c;

    /* renamed from: d, reason: collision with root package name */
    protected int f1485d;

    /* renamed from: e, reason: collision with root package name */
    HashMap<String, ConstraintAttribute> f1486e;

    public c() {
        int i10 = f1481f;
        this.f1482a = i10;
        this.f1483b = i10;
        this.f1484c = null;
    }

    public abstract void a(HashMap<String, SplineSet> hashMap);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void b(HashSet<String> hashSet);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void c(Context context, AttributeSet attributeSet);

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(String str) {
        String str2 = this.f1484c;
        if (str2 == null || str == null) {
            return false;
        }
        return str.matches(str2);
    }

    public void e(HashMap<String, Integer> hashMap) {
    }
}
