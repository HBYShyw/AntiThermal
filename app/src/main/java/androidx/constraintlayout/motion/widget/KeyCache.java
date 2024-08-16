package androidx.constraintlayout.motion.widget;

import java.util.Arrays;
import java.util.HashMap;

/* compiled from: KeyCache.java */
/* renamed from: androidx.constraintlayout.motion.widget.e, reason: use source file name */
/* loaded from: classes.dex */
public class KeyCache {

    /* renamed from: a, reason: collision with root package name */
    HashMap<Object, HashMap<String, float[]>> f1505a = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a(Object obj, String str, int i10) {
        if (!this.f1505a.containsKey(obj)) {
            return Float.NaN;
        }
        HashMap<String, float[]> hashMap = this.f1505a.get(obj);
        if (!hashMap.containsKey(str)) {
            return Float.NaN;
        }
        float[] fArr = hashMap.get(str);
        if (fArr.length > i10) {
            return fArr[i10];
        }
        return Float.NaN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(Object obj, String str, int i10, float f10) {
        if (!this.f1505a.containsKey(obj)) {
            HashMap<String, float[]> hashMap = new HashMap<>();
            float[] fArr = new float[i10 + 1];
            fArr[i10] = f10;
            hashMap.put(str, fArr);
            this.f1505a.put(obj, hashMap);
            return;
        }
        HashMap<String, float[]> hashMap2 = this.f1505a.get(obj);
        if (!hashMap2.containsKey(str)) {
            float[] fArr2 = new float[i10 + 1];
            fArr2[i10] = f10;
            hashMap2.put(str, fArr2);
            this.f1505a.put(obj, hashMap2);
            return;
        }
        float[] fArr3 = hashMap2.get(str);
        if (fArr3.length <= i10) {
            fArr3 = Arrays.copyOf(fArr3, i10 + 1);
        }
        fArr3[i10] = f10;
        hashMap2.put(str, fArr3);
    }
}
